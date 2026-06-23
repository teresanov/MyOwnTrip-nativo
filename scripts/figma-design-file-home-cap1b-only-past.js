/**
 * Home cap 1b — solo viajes pasados (sin próximos/en curso).
 * Ejecutar vía use_figma en Vf2tNMXyKAlJSV53A1v4Is.
 */
if (figma.fileKey !== 'Vf2tNMXyKAlJSV53A1v4Is') {
  throw new Error('ABORT: ejecutar en MyOwnTrip_design-file');
}

const KEYS = {
  statusBar: '88d8181832a7a046843e790ed88470cda99cabba',
  searchEnabled: 'aa5217e361697f6aaa9a3f61a6234a878edc8600',
  stackedOutlined: '367eeca85c0326cd73af0331f91796fbc18ff636',
  horizontalOutlined: 'ce2023d7839887d98683b00911b560e3a1a5b4b1',
  button: '3bf314ceb2123b2088dc56d86557062902d828c4',
};

const IMAGES = {
  barcelona:
    'https://images.unsplash.com/photo-1539037116277-4db20889f2d4?w=720&q=80',
  lisboa:
    'https://images.unsplash.com/photo-1555881400-74d7acaacd8b?w=720&q=80',
  tokio:
    'https://images.unsplash.com/photo-1540959733332-eab4deabeeaf?w=720&q=80',
};

const M3_COLLECTION_KEY = '39e9d60dbca5b7c05972a54eaf218badec531859';

const VAR_KEYS = {
  'Schemes/Tertiary': '7ab9f863683719424439eae0e6a2db6ef9f62645',
  'Schemes/On Surface': '0f7c3b657886bd85a309a9b0bfb12cf204f341d3',
  'Schemes/On Surface Variant': 'fd99805293a8253fbb6c3d2805699208c0a048b7',
  'Schemes/Background': '5dba8cd338382bc2d791a6c938bcd45b4a0f2f61',
};

const M3_LIGHT_MODE = '54778:1';

const FONTS = [
  ['Inter', 'Regular'],
  ['Inter', 'Medium'],
  ['Fraunces', 'SemiBold'],
];
for (const [family, style] of FONTS) {
  await figma.loadFontAsync({ family, style });
}

const imageCache = {};
async function fetchImage(url) {
  if (imageCache[url]) return imageCache[url];
  const res = await fetch(url);
  const buf = await res.arrayBuffer();
  const img = figma.createImage(new Uint8Array(buf));
  imageCache[url] = img;
  return img;
}

function setProps(inst, map) {
  const props = {};
  for (const key of Object.keys(inst.componentProperties)) {
    for (const [prefix, value] of Object.entries(map)) {
      if (key.startsWith(prefix)) props[key] = value;
    }
  }
  if (Object.keys(props).length) inst.setProperties(props);
}

async function applyImageFill(node, url) {
  const img = await fetchImage(url);
  if ('fills' in node) {
    node.fills = [
      {
        type: 'IMAGE',
        scaleMode: 'FILL',
        imageHash: img.hash,
        visible: true,
      },
    ];
  }
}

async function applyMediaImage(inst, url) {
  const media =
    inst.findOne((n) => n.name === 'Media' && n.type === 'FRAME') ||
    inst.findOne((n) => n.name === 'Media' && n.type === 'RECTANGLE');
  if (!media) return;
  const rect =
    media.type === 'RECTANGLE'
      ? media
      : media.findOne((n) => n.type === 'RECTANGLE' || n.name === 'Media');
  if (rect) await applyImageFill(rect, url);
}

async function importSchemeVars() {
  const libVars = await figma.teamLibrary.getVariablesInLibraryCollectionAsync(
    M3_COLLECTION_KEY,
  );
  const imported = {};
  for (const [name, fallback] of Object.entries(VAR_KEYS)) {
    const lib = libVars.find((v) => v.name === name);
    imported[name] = await figma.variables.importVariableByKeyAsync(lib?.key || fallback);
  }
  return {
    tertiary: imported['Schemes/Tertiary'],
    onSurface: imported['Schemes/On Surface'],
    onSurfaceVariant: imported['Schemes/On Surface Variant'],
    background: imported['Schemes/Background'],
  };
}

function applyM3LightMode(node, collection) {
  if (!node || !collection) return;
  const modeId = collection.modes.find((m) => m.name === 'Light')?.modeId || collection.modes[0].modeId;
  node.setExplicitVariableModeForCollection(collection, modeId);
}

async function varByName(name) {
  const all = await figma.variables.getLocalVariablesAsync('COLOR');
  const found = all.find((v) => v.name === name);
  if (!found) throw new Error('Missing variable: ' + name);
  return found;
}

async function resolveRgb(variable) {
  const collection = collections.find((c) => c.id === variable.variableCollectionId);
  const modeId =
    collection?.modes.find((m) => m.name === 'Light')?.modeId ||
    collection?.modes[0]?.modeId ||
    M3_LIGHT_MODE;
  let value = variable.valuesByMode[modeId];
  let depth = 0;
  while (value?.type === 'VARIABLE_ALIAS' && depth < 8) {
    const target = await figma.variables.getVariableByIdAsync(value.id);
    if (!target) break;
    value = target.valuesByMode[modeId];
    depth++;
  }
  if (!value || typeof value.r !== 'number') {
    throw new Error('Cannot resolve: ' + variable.name);
  }
  return { r: value.r, g: value.g, b: value.b };
}

async function bindFill(node, variable) {
  if (!node || !('fills' in node) || !variable) return;
  const color = await resolveRgb(variable);
  const base = { type: 'SOLID', color, visible: true };
  node.fills = [figma.variables.setBoundVariableForPaint(base, 'color', variable)];
}

function findTextStyle(namePart) {
  return figma.getLocalTextStyles().find((s) => s.name.includes(namePart));
}

async function applyTextStyle(textNode, stylePart, fallback) {
  const style = findTextStyle(stylePart);
  if (style) {
    await figma.loadFontAsync(style.fontName);
    textNode.textStyleId = style.id;
  } else if (fallback) {
    textNode.fontName = fallback.fontName;
    textNode.fontSize = fallback.fontSize;
  }
}

const scheme = await importSchemeVars();
const collections = await figma.variables.getLocalVariableCollectionsAsync();
const m3Collection =
  collections.find((c) => c.key === M3_COLLECTION_KEY) || collections[0];

async function addHeroHeader(parent, { eyebrow, headline, subhead }) {
  const hero = figma.createFrame();
  hero.name = 'Hero header';
  hero.layoutMode = 'VERTICAL';
  hero.primaryAxisSizingMode = 'AUTO';
  hero.counterAxisSizingMode = 'FIXED';
  hero.layoutSizingHorizontal = 'FILL';
  hero.itemSpacing = 8;
  hero.fills = [];

  const t1 = figma.createText();
  t1.name = 'Eyebrow';
  t1.characters = eyebrow;
  t1.textAutoResize = 'HEIGHT';
  hero.appendChild(t1);
  t1.layoutSizingHorizontal = 'FILL';
  await applyTextStyle(t1, 'label/large', {
    fontName: { family: 'Inter', style: 'Medium' },
    fontSize: 14,
  });
  await bindFill(t1, scheme.tertiary);

  const t2 = figma.createText();
  t2.name = 'Headline';
  t2.characters = headline;
  t2.textAutoResize = 'HEIGHT';
  hero.appendChild(t2);
  t2.layoutSizingHorizontal = 'FILL';
  await applyTextStyle(t2, 'display/small', {
    fontName: { family: 'Fraunces', style: 'SemiBold' },
    fontSize: 36,
  });
  await bindFill(t2, scheme.onSurface);

  const t3 = figma.createText();
  t3.name = 'Subhead';
  t3.characters = subhead;
  t3.textAutoResize = 'HEIGHT';
  hero.appendChild(t3);
  t3.layoutSizingHorizontal = 'FILL';
  await applyTextStyle(t3, 'body/large', {
    fontName: { family: 'Inter', style: 'Regular' },
    fontSize: 16,
  });
  await bindFill(t3, scheme.onSurfaceVariant);

  parent.appendChild(hero);
  return hero;
}

async function createSearchBar(placeholder) {
  const comp = await figma.importComponentByKeyAsync(KEYS.searchEnabled);
  const inst = comp.createInstance();
  inst.name = 'Search bar';
  setProps(inst, {
    'Show 2nd trailing icon': true,
    'Show 1st trailing icon': true,
    'Show leading icon': true,
    'Show avatar': 'False',
    'Placeholder text': placeholder,
  });
  return inst;
}

function copyMediaFromRef(targetInst, refInst) {
  const refMedia =
    refInst.findOne((n) => n.name === 'Media' && n.type === 'FRAME') ||
    refInst.findOne((n) => n.name === 'Media' && n.type === 'RECTANGLE');
  const targetMedia =
    targetInst.findOne((n) => n.name === 'Media' && n.type === 'FRAME') ||
    targetInst.findOne((n) => n.name === 'Media' && n.type === 'RECTANGLE');
  if (!refMedia || !targetMedia) return;
  const refRect =
    refMedia.type === 'RECTANGLE'
      ? refMedia
      : refMedia.findOne((n) => n.type === 'RECTANGLE' || n.name === 'Media');
  const targetRect =
    targetMedia.type === 'RECTANGLE'
      ? targetMedia
      : targetMedia.findOne((n) => n.type === 'RECTANGLE' || n.name === 'Media');
  if (refRect?.fills?.[0]?.type === 'IMAGE' && targetRect && 'fills' in targetRect) {
    targetRect.fills = JSON.parse(JSON.stringify(refRect.fills));
  }
}

async function createHorizontalTrip({ name, destination, meta, url, refInst }) {
  const mc = await figma.importComponentByKeyAsync(KEYS.horizontalOutlined);
  const inst = mc.createInstance();
  inst.name = name;
  setProps(inst, {
    'Header text': name,
    'Subhead text': destination,
  });
  const supporting = inst.findOne((n) => n.name === 'Supporting text' && n.type === 'TEXT');
  if (supporting) {
    await figma.loadFontAsync(supporting.fontName);
    supporting.characters = meta;
    supporting.visible = true;
  }
  if (refInst) {
    copyMediaFromRef(inst, refInst);
  } else if (url) {
    await applyMediaImage(inst, url);
  }
  return inst;
}

async function addSectionTitle(parent, text) {
  const t = figma.createText();
  t.name = 'Section title';
  t.characters = text;
  t.textAutoResize = 'HEIGHT';
  parent.appendChild(t);
  t.layoutSizingHorizontal = 'FILL';
  await applyTextStyle(t, 'title/large', {
    fontName: { family: 'Inter', style: 'Medium' },
    fontSize: 22,
  });
  await bindFill(t, scheme.onSurface);
  return t;
}

async function appendFillWidth(parent, child) {
  parent.appendChild(child);
  if ('layoutSizingHorizontal' in child) {
    child.layoutSizingHorizontal = 'FILL';
  }
}

async function buildOnlyPastBody(body, mediaRefs) {
  while (body.children.length) body.children[0].remove();

  await addHeroHeader(body, {
    eyebrow: 'Buenas tardes, Raquel',
    headline: 'No tienes viajes planeados',
    subhead: 'Crea uno nuevo o revisa tus viajes anteriores.',
  });
  await appendFillWidth(body, await createSearchBar('Buscar viajes'));

  const planMc = await figma.importComponentByKeyAsync(KEYS.stackedOutlined);
  const plan = planMc.createInstance();
  plan.name = 'Plan next trip';
  setProps(plan, {
    'Header text': '¿Listo para el siguiente?',
    'Subhead text': 'Crea un cuaderno nuevo',
    'Supporting text':
      'Tus documentos y recuerdos de viajes anteriores siguen en Wallet.',
    'Show secondary action': false,
  });
  await appendFillWidth(body, plan);

  const btnMc = await figma.importComponentByKeyAsync(KEYS.button);
  const btn = btnMc.createInstance();
  btn.name = 'Crear viaje CTA';
  setProps(btn, { 'Label text': 'Crear viaje', 'Show icon': true });
  await appendFillWidth(body, btn);

  await addSectionTitle(body, 'Viajes anteriores');
  await appendFillWidth(
    body,
    await createHorizontalTrip({
      name: 'Barcelona fin de semana',
      destination: 'Barcelona',
      meta: '4 jul 2026 – 6 jul 2026 · 3 días',
      url: IMAGES.barcelona,
      refInst: mediaRefs[0],
    }),
  );
  await appendFillWidth(
    body,
    await createHorizontalTrip({
      name: 'Lisboa en abril',
      destination: 'Lisboa',
      meta: '12 abr 2026 – 18 abr 2026 · 7 días',
      url: IMAGES.lisboa,
      refInst: mediaRefs[1],
    }),
  );
  await appendFillWidth(
    body,
    await createHorizontalTrip({
      name: 'Tokio otoño',
      destination: 'Tokio',
      meta: '1 nov 2025 – 10 nov 2025 · 10 días',
      url: IMAGES.tokio,
      refInst: mediaRefs[2],
    }),
  );
}

async function ensureCap1b(screens) {
  let cap = screens.children.find((c) => /cap\s*1b/i.test(c.name));
  if (!cap) {
    cap = figma.createFrame();
    cap.name = 'cap 1b';
    cap.layoutMode = 'VERTICAL';
    cap.itemSpacing = 8;
    cap.fills = [];
    screens.appendChild(cap);
  }

  const captionText =
    'cap 1b · Home solo pasados — route: trip_list · sin próximos/en curso';
  let caption = cap.findOne((n) => n.type === 'TEXT' && /cap/i.test(n.name));
  if (!caption) {
    caption = figma.createText();
    caption.name = 'cap label';
    caption.fontName = { family: 'Inter', style: 'Regular' };
    caption.fontSize = 12;
    caption.textAutoResize = 'HEIGHT';
    cap.insertChild(0, caption);
    caption.layoutSizingHorizontal = 'FILL';
  }
  caption.characters = captionText;
  await bindFill(caption, scheme.onSurfaceVariant);

  let phone = cap.findOne((n) => n.name === 'phone');
  if (!phone) {
    phone = figma.createFrame();
    phone.name = 'phone';
    phone.resize(360, 920);
    phone.layoutMode = 'VERTICAL';
    phone.clipsContent = true;
    phone.fills = [{ type: 'SOLID', color: { r: 1, g: 0.985, b: 0.969 } }];
    cap.appendChild(phone);

    const sbMc = await figma.importComponentByKeyAsync(KEYS.statusBar);
    const sb = sbMc.createInstance();
    sb.name = 'Status bar';
    phone.appendChild(sb);
    sb.layoutSizingHorizontal = 'FILL';
  }
  phone.resize(360, 920);
  applyM3LightMode(phone, m3Collection);
  await bindFill(phone, scheme.background);

  let body = phone.findOne((n) => n.name === 'Body');
  if (!body) {
    body = figma.createFrame();
    body.name = 'Body';
    body.layoutMode = 'VERTICAL';
    body.layoutSizingHorizontal = 'FILL';
    body.layoutGrow = 1;
    body.primaryAxisSizingMode = 'AUTO';
    body.counterAxisSizingMode = 'FIXED';
    body.paddingLeft = 16;
    body.paddingRight = 16;
    body.paddingTop = 16;
    body.paddingBottom = 24;
    body.itemSpacing = 24;
    body.fills = [{ type: 'SOLID', color: { r: 1, g: 0.985, b: 0.969 } }];
    phone.appendChild(body);
  }
  applyM3LightMode(body, m3Collection);
  await bindFill(body, scheme.background);

  const cap2 = screens.children.find((c) => /^cap\s*2$/i.test(c.name));
  const mediaRefs = cap2
    ? cap2
        .findAll((n) => n.type === 'INSTANCE' && /Horizontal card/i.test(n.name))
        .slice(0, 3)
    : [];

  await buildOnlyPastBody(body, mediaRefs);
  return cap;
}

const page =
  figma.root.children.find((p) => p.name.includes('01')) ||
  figma.root.children.find((p) => p.name.includes('Shell')) ||
  figma.root.children.find((p) => p.name.includes('Home'));
if (!page) throw new Error('Home page not found');
await figma.setCurrentPageAsync(page);

const flow = page.findOne((n) => n.name === 'Shell — Home · flow');
if (!flow) throw new Error('Shell — Home · flow not found');

const screens = flow.findOne((n) => n.name === 'Screens');
if (!screens) throw new Error('Screens frame not found');

const cap1b = await ensureCap1b(screens);

const cap1Index = screens.children.findIndex((c) => /^cap\s*1$/i.test(c.name));
if (cap1Index >= 0 && screens.children.indexOf(cap1b) !== cap1Index + 1) {
  screens.insertChild(cap1Index + 1, cap1b);
}

const caps = screens.children.filter((c) => /^cap/i.test(c.name));
caps.forEach((c, i) => {
  c.x = i * 400;
  c.y = 0;
});

screens.layoutMode = 'HORIZONTAL';
screens.primaryAxisSizingMode = 'AUTO';
screens.counterAxisSizingMode = 'AUTO';
screens.itemSpacing = 40;
screens.resize(Math.max(screens.width, caps.length * 400 - 40), Math.max(screens.height, 980));

flow.resize(Math.max(flow.width, screens.width), Math.max(flow.height, screens.height + 60));

return {
  status: 'cap 1b created',
  capId: cap1b.id,
  capName: cap1b.name,
  screensWidth: screens.width,
};
