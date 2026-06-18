/**
 * Reconstruye Shell — Home · flow: cap 1 vacío, cap 2 con viajes, cap 3 búsqueda + menú.
 * Ejecutar vía figma_execute en Vf2tNMXyKAlJSV53A1v4Is (Bridge en design-file).
 */
if (figma.fileKey !== 'Vf2tNMXyKAlJSV53A1v4Is') {
  throw new Error('ABORT: ejecutar en MyOwnTrip_design-file');
}

const KEYS = {
  statusBar: '88d8181832a7a046843e790ed88470cda99cabba',
  searchEnabled: 'aa5217e361697f6aaa9a3f61a6234a878edc8600',
  searchPressed: '6988ec397ca021a720e04873c12d4b54760c2ac4',
  menu: '6e4dd0de08c0535ab21479206cc3de6852e84e12',
  stackedMediaOutlined: '40a67d078adad4640b8d37f019cefb44f7341190',
  stackedElevated: '2cc6e26e1bd6d362c56fa3bee3780ff22b26752f',
  stackedOutlined: '367eeca85c0326cd73af0331f91796fbc18ff636',
  horizontalOutlined: 'ce2023d7839887d98683b00911b560e3a1a5b4b1',
  button: '3bf314ceb2123b2088dc56d86557062902d828c4',
  tripHeroElevated: '7ada0034947b879a0b8b0ee34c24dc325e24118a',
};

const IMAGES = {
  barcelona:
    'https://images.unsplash.com/photo-1539037116277-4db20889f2d4?w=720&q=80',
  lisboa:
    'https://images.unsplash.com/photo-1555881400-74d7acaacd8b?w=720&q=80',
  tokio:
    'https://images.unsplash.com/photo-1540959733332-eab4deabeeaf?w=720&q=80',
};

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

function addHeroHeader(parent, { eyebrow, headline, subhead }) {
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
  t1.fontName = { family: 'Inter', style: 'Medium' };
  t1.characters = eyebrow;
  t1.fontSize = 14;
  t1.fills = [{ type: 'SOLID', color: { r: 0.51, g: 0.33, b: 0.08 } }];
  hero.appendChild(t1);

  const t2 = figma.createText();
  t2.name = 'Headline';
  t2.fontName = { family: 'Fraunces', style: 'SemiBold' };
  t2.characters = headline;
  t2.fontSize = 36;
  t2.layoutSizingHorizontal = 'FILL';
  t2.textAutoResize = 'HEIGHT';
  t2.fills = [{ type: 'SOLID', color: { r: 0, g: 0, b: 0 } }];
  hero.appendChild(t2);

  const t3 = figma.createText();
  t3.name = 'Subhead';
  t3.fontName = { family: 'Inter', style: 'Regular' };
  t3.characters = subhead;
  t3.fontSize = 16;
  t3.layoutSizingHorizontal = 'FILL';
  t3.textAutoResize = 'HEIGHT';
  t3.fills = [{ type: 'SOLID', color: { r: 0.29, g: 0.27, b: 0.25 } }];
  hero.appendChild(t3);

  parent.appendChild(hero);
  return hero;
}

async function createSearchBar(pressed, query) {
  const key = pressed ? KEYS.searchPressed : KEYS.searchEnabled;
  const comp = await figma.importComponentByKeyAsync(key);
  const inst = comp.createInstance();
  inst.name = 'Search bar';
  inst.layoutSizingHorizontal = 'FILL';
  setProps(inst, {
    'Show 2nd trailing icon': true,
    'Show 1st trailing icon': true,
    'Show leading icon': true,
    'Show avatar': 'False',
    'Placeholder text': pressed ? '' : query,
  });
  if (pressed) {
    const textNode =
      inst.findOne((n) => n.type === 'TEXT' && /input|supporting|placeholder/i.test(n.name)) ||
      inst.findOne((n) => n.type === 'TEXT');
    if (textNode && 'characters' in textNode) {
      await figma.loadFontAsync(textNode.fontName);
      textNode.characters = query;
    }
  }
  return inst;
}

async function createTripHero(url) {
  try {
    const comp = await figma.importComponentByKeyAsync(KEYS.tripHeroElevated);
    const inst = comp.createInstance();
    inst.name = 'TripHeroCard';
    inst.layoutSizingHorizontal = 'FILL';
    setProps(inst, {
      'Countdown text': 'Sale en 3 días',
      'Show countdown': true,
      'Title text': 'Barcelona fin de semana',
      'Meta text': '20 jun 2026 – 22 jun 2026 · 3 días',
    });
    const eyebrow = inst.findOne((n) => /eyebrow/i.test(n.name) && n.type === 'INSTANCE');
    if (eyebrow) {
      setProps(eyebrow, { 'Label text': 'Próximo viaje' });
    }
    const cover = inst.findOne((n) => /cover|background|media/i.test(n.name));
    if (cover) await applyImageFill(cover, url);
    return inst;
  } catch (_) {
    const wrap = figma.createFrame();
    wrap.name = 'TripHeroCard';
    wrap.layoutMode = 'VERTICAL';
    wrap.primaryAxisSizingMode = 'AUTO';
    wrap.counterAxisSizingMode = 'FIXED';
    wrap.layoutSizingHorizontal = 'FILL';
    wrap.itemSpacing = 8;
    wrap.fills = [];

    const mediaMc = await figma.importComponentByKeyAsync(KEYS.stackedMediaOutlined);
    const media = mediaMc.createInstance();
    media.name = 'Portada';
    media.layoutSizingHorizontal = 'FILL';
    media.resize(328, 280);
    await applyMediaImage(media, url);
    wrap.appendChild(media);

    const btnMc = await figma.importComponentByKeyAsync(KEYS.button);
    const btn = btnMc.createInstance();
    btn.name = 'Ver detalles';
    btn.layoutSizingHorizontal = 'FILL';
    setProps(btn, { 'Label text': 'Ver detalles', 'Show icon': false });
    wrap.appendChild(btn);
    return wrap;
  }
}

async function createWalletBanner() {
  const mc = await figma.importComponentByKeyAsync(KEYS.stackedElevated);
  const inst = mc.createInstance();
  inst.name = 'Wallet promo';
  inst.layoutSizingHorizontal = 'FILL';
  setProps(inst, {
    'Header text': 'Todo en Wallet',
    'Subhead text': 'Documentos y reservas',
    'Supporting text':
      'Vuelos, hoteles y PDFs en un solo sitio — incluso sin red.',
    'Show secondary action': true,
  });
  return inst;
}

async function createHorizontalTrip({ name, destination, meta, url }) {
  const mc = await figma.importComponentByKeyAsync(KEYS.horizontalOutlined);
  const inst = mc.createInstance();
  inst.name = name;
  inst.layoutSizingHorizontal = 'FILL';
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
  await applyMediaImage(inst, url);
  return inst;
}

function addSectionTitle(parent, text) {
  const t = figma.createText();
  t.name = 'Section title';
  t.fontName = { family: 'Inter', style: 'Medium' };
  t.characters = text;
  t.fontSize = 22;
  t.fills = [{ type: 'SOLID', color: { r: 0.12, g: 0.11, b: 0.07 } }];
  parent.appendChild(t);
  return t;
}

async function buildEmptyBody(body) {
  while (body.children.length) body.children[0].remove();

  addHeroHeader(body, {
    eyebrow: 'Hola Raquel,',
    headline: 'Tu próximo viaje empieza aquí',
    subhead: 'Crea un cuaderno, guarda documentos y anota recuerdos.',
  });
  body.appendChild(await createSearchBar(false, 'Buscar destinos o viajes'));

  const emptyMc = await figma.importComponentByKeyAsync(KEYS.stackedOutlined);
  const empty = emptyMc.createInstance();
  empty.name = 'Empty state';
  empty.layoutSizingHorizontal = 'FILL';
  setProps(empty, {
    'Header text': 'Sin viajes todavía',
    'Subhead text': 'Planea tu primera aventura',
    'Supporting text': 'La primera aventura es la mejor',
    'Show secondary action': false,
  });
  body.appendChild(empty);

  body.appendChild(await createWalletBanner());

  const btnMc = await figma.importComponentByKeyAsync(KEYS.button);
  const btn = btnMc.createInstance();
  btn.name = 'Crear viaje CTA';
  btn.layoutSizingHorizontal = 'FILL';
  setProps(btn, { 'Label text': 'Crear mi primer viaje', 'Show icon': true });
  body.appendChild(btn);
}

async function buildTripsBody(body) {
  while (body.children.length) body.children[0].remove();

  addHeroHeader(body, {
    eyebrow: 'Buenos días',
    headline: 'Barcelona',
    subhead: 'Sale en 3 días',
  });
  body.appendChild(await createSearchBar(false, 'Barcelona'));
  body.appendChild(await createTripHero(IMAGES.barcelona));
  body.appendChild(await createWalletBanner());
  addSectionTitle(body, 'Más viajes');
  body.appendChild(
    await createHorizontalTrip({
      name: 'Lisboa en abril',
      destination: 'Lisboa',
      meta: '12 abr 2026 – 18 abr 2026 · 7 días',
      url: IMAGES.lisboa,
    }),
  );
  body.appendChild(
    await createHorizontalTrip({
      name: 'Tokio otoño',
      destination: 'Tokio',
      meta: '1 nov 2025 – 10 nov 2025 · 10 días',
      url: IMAGES.tokio,
    }),
  );
}

function relayoutMenu(menu, searchInst, phone) {
  const phoneBox = phone.absoluteBoundingBox;
  const searchBox = searchInst.absoluteBoundingBox;
  if (!phoneBox || !searchBox) return;
  menu.x = searchBox.x - phoneBox.x + searchBox.width - menu.width;
  menu.y = searchBox.y - phoneBox.y + searchBox.height + 4;
}

function setMenuItemLabel(item, text, selected) {
  const label =
    item.findOne((n) => n.type === 'TEXT' && /label/i.test(n.name)) ||
    item.findAll((n) => n.type === 'TEXT').pop();
  if (label) label.characters = text;
  for (const key of Object.keys(item.componentProperties || {})) {
    if (/selected/i.test(key)) item.setProperties({ [key]: selected ? 'True' : 'False' });
    if (/leading element/i.test(key)) item.setProperties({ [key]: false });
    if (/trailing element/i.test(key) && selected) item.setProperties({ [key]: true });
  }
}

function relabelMenu(menu) {
  const list1Labels = ['Todos los viajes', 'En curso', 'Próximos', 'Pasados'];
  const list2Labels = ['Fecha — próximo primero', 'Nombre A–Z', 'Destino A–Z'];
  const lists = menu.children.filter((n) => /^List \d/.test(n.name));
  const list1Items = lists[0]
    ? lists[0].children.filter((n) => /Menu-item/i.test(n.name) && n.visible !== false)
    : [];
  const list2Items = lists[1]
    ? lists[1].children.filter((n) => /Menu-item/i.test(n.name) && n.visible !== false)
    : [];
  list1Labels.forEach((text, i) => {
    if (list1Items[i]) setMenuItemLabel(list1Items[i], text, i === 0);
  });
  list2Labels.forEach((text, i) => {
    if (list2Items[i]) setMenuItemLabel(list2Items[i], text, i === 0);
  });
  for (const list of lists) {
    const sectionLabel = list.findOne((n) => /Label \d/i.test(n.name));
    if (sectionLabel?.type === 'INSTANCE') {
      sectionLabel.visible = true;
      const t = sectionLabel.findOne((n) => n.type === 'TEXT');
      if (t) t.characters = list.name === 'List 1' ? 'Mostrar' : 'Ordenar';
    }
  }
}

async function applySearchMenuCap(cap, captionText) {
  const caption = cap.findOne((n) => n.type === 'TEXT' && /cap/i.test(n.name));
  if (caption) caption.characters = captionText;

  const phone = cap.findOne((n) => n.name === 'phone');
  const body = phone?.findOne((n) => n.name === 'Body');
  if (!body) throw new Error('Body missing in ' + cap.name);

  const oldSearch = body.findOne((n) => n.name === 'Search bar');
  const idx = oldSearch ? body.children.indexOf(oldSearch) : 1;
  if (oldSearch) oldSearch.remove();

  const searchInst = await createSearchBar(true, 'Barcelona');
  body.insertChild(idx, searchInst);

  body.findOne((n) => n.name === 'Filter menu')?.remove();
  const menuMc = await figma.importComponentByKeyAsync(KEYS.menu);
  const menu = menuMc.createInstance();
  menu.name = 'Filter menu';
  body.appendChild(menu);
  relabelMenu(menu);
  relayoutMenu(menu, searchInst, phone);
}

async function ensureCap(screens, index, name, captionText, buildBody) {
  let cap = screens.children[index];
  if (!cap || !/^cap/i.test(cap.name)) {
    cap = figma.createFrame();
    cap.name = name;
    cap.layoutMode = 'VERTICAL';
    cap.itemSpacing = 16;
    cap.fills = [];
    screens.appendChild(cap);
  }
  cap.name = name;

  let caption = cap.findOne((n) => n.type === 'TEXT' && /cap/i.test(n.name));
  if (!caption) {
    caption = figma.createText();
    caption.name = 'caption';
    caption.fontName = { family: 'Inter', style: 'Regular' };
    caption.fontSize = 12;
    caption.fills = [{ type: 'SOLID', color: { r: 0.4, g: 0.4, b: 0.4 } }];
    cap.insertChild(0, caption);
  }
  caption.characters = captionText;
  caption.textAutoResize = 'HEIGHT';
  caption.layoutSizingHorizontal = 'FILL';

  let phone = cap.findOne((n) => n.name === 'phone');
  if (!phone) {
    phone = figma.createFrame();
    phone.name = 'phone';
    phone.resize(360, 800);
    phone.layoutMode = 'VERTICAL';
    phone.clipsContent = true;
    phone.fills = [{ type: 'SOLID', color: { r: 1, g: 0.985, b: 0.969 } }];
    cap.appendChild(phone);

    const sbMc = await figma.importComponentByKeyAsync(KEYS.statusBar);
    const sb = sbMc.createInstance();
    sb.name = 'Status bar';
    sb.layoutSizingHorizontal = 'FILL';
    phone.appendChild(sb);
  }

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

  await buildBody(body);
  return cap;
}

const page = figma.root.children.find((p) => p.name.includes('Shell'));
if (!page) throw new Error('Shell page not found');
await figma.setCurrentPageAsync(page);

const flow = page.findOne((n) => n.name === 'Shell — Home · flow');
if (!flow) throw new Error('Shell — Home · flow not found');

const screens = flow.findOne((n) => n.name === 'Screens');
if (!screens) throw new Error('Screens frame not found');

const cap1 = await ensureCap(
  screens,
  0,
  'cap 1',
  'cap 1 · Home vacío — route: trip_list',
  buildEmptyBody,
);

// cap 2: conservar si ya existe; solo crear/rellenar si falta
let cap2 = screens.children.find((c) => /cap\s*2/i.test(c.name));
if (!cap2) {
  cap2 = await ensureCap(
    screens,
    screens.children.length,
    'cap 2',
    'cap 2 · Home con viajes — route: trip_list',
    buildTripsBody,
  );
}

// cap 3 = duplicar cap 2 (sin tocar cap 2) + menú filtros
screens.children.find((c) => /cap\s*3/i.test(c.name))?.remove();
const cap3 = cap2.clone();
cap3.name = 'cap 3';
screens.appendChild(cap3);
cap3.x = cap2.x + cap2.width + 40;
cap3.y = cap2.y;
await applySearchMenuCap(
  cap3,
  'cap 3 · Home — búsqueda activa · menú filtros\nroute: TripListScreen (query + filterMenu)',
);

// Layout caps horizontally
const caps = [cap1, cap2, cap3];
let x = caps[0].x || 0;
for (const cap of caps) {
  cap.y = caps[0].y;
  cap.x = x;
  x += cap.width + 40;
}

figma.viewport.scrollAndZoomIntoView(caps);

return {
  status: 'rebuilt',
  caps: caps.map((c) => ({
    name: c.name,
    id: c.id,
    url: `https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=${c.id.replace(':', '-')}`,
  })),
};
