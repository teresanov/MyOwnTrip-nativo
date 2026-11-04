/**
 * Wallet design-file: cap 10 QR card + cap 9 footer CTA.
 * Ejecutar en Vf2tNMXyKAlJSV53A1v4Is (Desktop Bridge).
 */
if (figma.fileKey !== 'Vf2tNMXyKAlJSV53A1v4Is') {
  throw new Error('ABORT: abrir MyOwnTrip_design-file');
}

const KEYS = {
  statusBar: '88d8181832a7a046843e790ed88470cda99cabba',
  appBarSet: 'f57fabde1da94e3f942cc75ffd7a844d5bfb532e',
  iconArrowBack: '52d53666523506ac044165acdaf9318f4f0baa2f',
  iconDelete: '2a48234db266f2b0c4a36c4f7ee933a0ea3c52d5',
  iconQr: '9ce1f9e863b93e3dc9274da72717a635e0baeaa3',
  buttonFilled: '14e743023239aeab8e4b8dfe20de85ed8e5ba30a',
  textLabelLarge: '2dc1c88642ddd9d906d37102f95881a29f64ad99',
  textLabelMedium: '790b1161ee4d5a474e3578be78d583da6fe24233',
  textBodyMedium: '8b638b84df298decb21e343aeab711861094ea7e',
  textBodySmall: '1bc7aec6d9441a6ba4050174a7deed9481779eff',
};

const iconCache = {};
async function getIcon(key) {
  if (!iconCache[key]) iconCache[key] = await figma.importComponentByKeyAsync(key);
  return iconCache[key];
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

function appendFill(parent, node) {
  parent.appendChild(node);
  if ('layoutSizingHorizontal' in node) node.layoutSizingHorizontal = 'FILL';
  return node;
}

async function applyTextStyle(node, styleKey) {
  try {
    const style = await figma.importStyleByKeyAsync(styleKey);
    if (node.type === 'TEXT') node.textStyleId = style.id;
  } catch (_) {}
}

async function loadFonts() {
  await figma.loadFontAsync({ family: 'Inter', style: 'Regular' });
  await figma.loadFontAsync({ family: 'Inter', style: 'Medium' });
}

async function styledText(name, characters, styleKey) {
  await loadFonts();
  const t = figma.createText();
  t.name = name;
  t.characters = characters;
  t.textAutoResize = 'HEIGHT';
  await applyTextStyle(t, styleKey);
  return t;
}

async function createAppBar(title, { showDelete = false } = {}) {
  const set = await figma.importComponentSetByKeyAsync(KEYS.appBarSet);
  const comp = set.children.find((c) => c.name === 'Configuration=Small, Elevation=Flat');
  const inst = comp.createInstance();
  inst.name = 'App bar';
  setProps(inst, {
    'Show 1st trailing action': showDelete,
    'Show 2nd trailing action': false,
    'Show 3rd trailing action': false,
  });
  const textContent = inst.findOne((n) => n.name === 'Text content');
  if (textContent) {
    setProps(textContent, { Headline: title, 'Show-supporting-text': false });
  }
  const leading = inst.findOne((n) => n.name === 'Leading icon');
  const leadIcon = leading?.findOne((n) => n.type === 'INSTANCE' && n.name === 'Icon');
  if (leadIcon) leadIcon.swapComponent(await getIcon(KEYS.iconArrowBack));
  if (showDelete) {
    const trail = inst.findOne((n) => n.name === 'Trailing action 1');
    const trailIcon = trail?.findOne((n) => n.type === 'INSTANCE' && n.name === 'Icon');
    if (trailIcon) trailIcon.swapComponent(await getIcon(KEYS.iconDelete));
  }
  return inst;
}

function createQrSvgNode(size) {
  const cells = 21;
  const module = size / cells;
  let rects = '';
  for (let y = 0; y < cells; y++) {
    for (let x = 0; x < cells; x++) {
      const on =
        (x < 7 && y < 7) ||
        (x > 13 && y < 7) ||
        (x < 7 && y > 13) ||
        ((x + y) % 3 === 0 && x > 6 && x < 15 && y > 6 && y < 15) ||
        (x % 2 === 0 && y % 2 === 1);
      if (on) rects += `<rect x="${x * module}" y="${y * module}" width="${module}" height="${module}" fill="#1a1a1a"/>`;
    }
  }
  const svg = `<svg xmlns="http://www.w3.org/2000/svg" width="${size}" height="${size}" viewBox="0 0 ${size} ${size}"><rect width="100%" height="100%" fill="#ffffff"/>${rects}</svg>`;
  const node = figma.createNodeFromSvg(svg);
  node.name = 'QR code';
  node.resize(size, size);
  return node;
}

async function createWalletQrCard(compact) {
  const size = compact ? 200 : 260;
  const card = figma.createFrame();
  card.name = 'Wallet QR card';
  card.layoutMode = 'VERTICAL';
  card.primaryAxisSizingMode = 'AUTO';
  card.counterAxisSizingMode = 'FIXED';
  card.paddingLeft = 16;
  card.paddingRight = 16;
  card.paddingTop = 16;
  card.paddingBottom = 16;
  card.itemSpacing = 8;
  card.cornerRadius = 12;
  card.fills = [{ type: 'SOLID', color: { r: 0.98, g: 0.97, b: 0.95 } }];
  card.strokes = [{ type: 'SOLID', color: { r: 0.75, g: 0.72, b: 0.68 } }];
  card.strokeWeight = 1;

  const label = await styledText('Label', 'Código QR', KEYS.textLabelLarge);
  card.appendChild(label);

  const qrWrap = figma.createFrame();
  qrWrap.name = 'QR image';
  qrWrap.layoutMode = 'VERTICAL';
  qrWrap.primaryAxisAlignItems = 'CENTER';
  qrWrap.counterAxisAlignItems = 'CENTER';
  qrWrap.resize(size, size);
  qrWrap.fills = [];
  qrWrap.appendChild(createQrSvgNode(size));
  card.appendChild(qrWrap);

  const hint = await styledText(
    'Hint',
    'Muéstralo en la puerta de embarque. Disponible sin conexión.',
    KEYS.textBodySmall,
  );
  card.appendChild(hint);
  return card;
}

async function createOfflineQrRow() {
  const row = figma.createFrame();
  row.name = 'Offline indicator';
  row.layoutMode = 'HORIZONTAL';
  row.primaryAxisAlignItems = 'CENTER';
  row.itemSpacing = 4;
  row.fills = [];
  const iconMc = await getIcon(KEYS.iconQr);
  const iconInst = iconMc.createInstance();
  iconInst.name = 'Icon';
  iconInst.resize(20, 20);
  row.appendChild(iconInst);
  const label = await styledText('Label', 'QR sin conexión', KEYS.textLabelMedium);
  row.appendChild(label);
  return row;
}

function configureScrollBody(body) {
  body.layoutMode = 'VERTICAL';
  body.layoutGrow = 1;
  body.primaryAxisSizingMode = 'FIXED';
  body.counterAxisSizingMode = 'FIXED';
  body.itemSpacing = 0;
  body.paddingLeft = 16;
  body.paddingRight = 16;
  body.paddingTop = 16;
  body.paddingBottom = 16;
  body.fills = [];
  body.children.forEach((c) => c.remove());
}

async function buildDetailPhone(phoneId) {
  const phone = await figma.getNodeByIdAsync(phoneId);
  if (!phone) throw new Error('phone ' + phoneId);
  phone.layoutMode = 'VERTICAL';
  phone.primaryAxisSizingMode = 'FIXED';
  phone.counterAxisSizingMode = 'FIXED';
  phone.resize(360, 800);
  phone.itemSpacing = 0;
  phone.fills = [{ type: 'SOLID', color: { r: 1, g: 0.985, b: 0.969 } }];
  phone.clipsContent = true;
  phone.children.forEach((c) => c.remove());

  try {
    const sb = (await figma.importComponentByKeyAsync(KEYS.statusBar)).createInstance();
    sb.name = 'Status bar';
    appendFill(phone, sb);
  } catch (_) {}

  appendFill(phone, await createAppBar('IB 3254 · Madrid → Barcelona', { showDelete: true }));

  let body = phone.findOne((n) => n.name === 'Body');
  if (!body) {
    body = figma.createFrame();
    body.name = 'Body';
  }
  configureScrollBody(body);
  appendFill(phone, body);

  const scroll = figma.createFrame();
  scroll.name = 'Scroll';
  scroll.layoutMode = 'VERTICAL';
  scroll.itemSpacing = 12;
  scroll.fills = [];
  scroll.clipsContent = true;
  scroll.primaryAxisSizingMode = 'AUTO';
  scroll.layoutGrow = 1;

  appendFill(body, scroll);
  appendFill(scroll, await styledText('Type', 'Vuelo', KEYS.textLabelLarge));
  appendFill(scroll, await createOfflineQrRow());
  appendFill(scroll, await styledText('Fecha', 'Fecha: 4 jul 2026', KEYS.textBodyMedium));
  appendFill(scroll, await styledText('Hora', 'Hora: 09:15', KEYS.textBodyMedium));
  appendFill(scroll, await createWalletQrCard(true));
}

async function fixManualFooter(phoneId) {
  const phone = await figma.getNodeByIdAsync(phoneId);
  const body = phone.findOne((n) => n.name === 'Body');
  if (!body) throw new Error('Body missing');

  const button = body.findOne(
    (n) => n.type === 'INSTANCE' && (n.name === 'Button' || n.name.startsWith('Button')),
  );
  const fields = body.children.filter((c) => c !== button);

  body.layoutMode = 'VERTICAL';
  body.layoutGrow = 1;
  body.primaryAxisSizingMode = 'FIXED';
  body.counterAxisSizingMode = 'FIXED';
  body.itemSpacing = 0;
  body.fills = [];
  body.children.forEach((c) => c.remove());

  const scroll = figma.createFrame();
  scroll.name = 'Scroll';
  scroll.layoutMode = 'VERTICAL';
  scroll.layoutGrow = 1;
  scroll.itemSpacing = 16;
  scroll.fills = [];
  scroll.clipsContent = true;
  scroll.primaryAxisSizingMode = 'AUTO';

  const footer = figma.createFrame();
  footer.name = 'Footer';
  footer.layoutMode = 'VERTICAL';
  footer.itemSpacing = 8;
  footer.fills = [];
  footer.paddingTop = 8;

  appendFill(body, scroll);
  appendFill(body, footer);

  for (const f of fields) appendFill(scroll, f);
  if (button) appendFill(footer, button);

  if (phone.layoutMode !== 'NONE') {
    body.layoutSizingHorizontal = 'FILL';
  }
}

await figma.loadAllPagesAsync();
await figma.setCurrentPageAsync(figma.root.children.find((p) => p.name === '04 · Wallet'));
await buildDetailPhone('491:13987');
await fixManualFooter('491:13980');

figma.notify('Wallet detalle QR + manual footer OK');
