/**
 * R4 — Instancias PRODUCT date/time en design-file (paridad Compose).
 * Ejecutar con Desktop Bridge en MyOwnTrip_design-file (Vf2tNMXyKAlJSV53A1v4Is).
 *
 * Prerrequisito librería DS (zrGAL4v6): publicar **MotDateField** (61348:29097) y
 * **MotTimeField** (61348:29337). Sin publish, `importComponentByKeyAsync` falla en Wallet
 * (MotDateRangeField sí importa — ya publicado).
 */
if (figma.fileKey !== 'Vf2tNMXyKAlJSV53A1v4Is') {
  throw new Error('ABORT: abrir MyOwnTrip_design-file');
}

const KEYS = {
  statusBar: '88d8181832a7a046843e790ed88470cda99cabba',
  appBarSet: 'f57fabde1da94e3f942cc75ffd7a844d5bfb532e',
  iconArrowBack: '52d53666523506ac044165acdaf9318f4f0baa2f',
  textField: '43c1c32c7c34608c57222f4bc04c00a6e3e543f0',
  buttonFilled: '14e743023239aeab8e4b8dfe20de85ed8e5ba30a',
  motDateRange: '4ed511638d6a1846f998314c38ed65b3ca7f5f1b',
  motDate: '65528979da01f4260e2caecc9945dfa25160e3db',
  motTime: '45241b6dad95ebfc29a8d90d952f15b58d7cbd88',
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

async function createAppBar(title) {
  const set = await figma.importComponentSetByKeyAsync(KEYS.appBarSet);
  const comp = set.children.find((c) => c.name === 'Configuration=Small, Elevation=Flat');
  const inst = comp.createInstance();
  inst.name = 'App bar';
  setProps(inst, {
    'Show 1st trailing action': false,
    'Show 2nd trailing action': false,
    'Show 3rd trailing action': false,
  });
  const textContent = inst.findOne((n) => n.name === 'Text content');
  if (textContent) {
    setProps(textContent, { Headline: title, 'Show-supporting-text': false });
  }
  const leading = inst.findOne((n) => n.name === 'Leading icon');
  const iconNode = leading?.findOne((n) => n.type === 'INSTANCE' && n.name === 'Icon');
  if (iconNode) iconNode.swapComponent(await getIcon(KEYS.iconArrowBack));
  return inst;
}

async function createTextField(label, value) {
  const mc = await figma.importComponentByKeyAsync(KEYS.textField);
  const inst = mc.createInstance();
  setProps(inst, {
    'Label text#52808:484': label,
    'Input text#52808:726': value,
    'Show supporting text#52051:0': false,
  });
  return inst;
}

async function createFilledButton(label) {
  const set = await figma.importComponentSetByKeyAsync(KEYS.buttonFilled);
  const comp = set.children.find((c) => c.name === 'Type=Square, Size=Medium, State=Enabled');
  const inst = comp.createInstance();
  setProps(inst, { 'Label text': label, 'Show icon': false });
  return inst;
}

async function importProduct(key, name) {
  const mc = await figma.importComponentByKeyAsync(key);
  const inst = mc.createInstance();
  inst.name = name;
  return inst;
}

function appendFill(parent, node) {
  parent.appendChild(node);
  if ('layoutSizingHorizontal' in node) node.layoutSizingHorizontal = 'FILL';
  return node;
}

function configurePhone(phone) {
  phone.layoutMode = 'VERTICAL';
  phone.primaryAxisSizingMode = 'FIXED';
  phone.counterAxisSizingMode = 'FIXED';
  phone.resize(360, 800);
  phone.itemSpacing = 0;
  phone.fills = [{ type: 'SOLID', color: { r: 1, g: 0.985, b: 0.969 } }];
  phone.clipsContent = true;
}

async function ensureBody(phone) {
  let body = phone.findOne((n) => n.name === 'Body');
  if (!body) {
    body = figma.createFrame();
    body.name = 'Body';
    phone.appendChild(body);
  }
  body.layoutMode = 'VERTICAL';
  body.layoutGrow = 1;
  body.primaryAxisSizingMode = 'AUTO';
  body.counterAxisSizingMode = 'FIXED';
  body.itemSpacing = 16;
  body.paddingLeft = 16;
  body.paddingRight = 16;
  body.paddingTop = 16;
  body.paddingBottom = 24;
  body.fills = [];
  body.children.forEach((c) => c.remove());
  if (phone.layoutMode !== 'NONE') {
    body.layoutSizingHorizontal = 'FILL';
  }
  return body;
}

async function buildTripFormPhone(phoneId, appBarTitle, startDate, endDate) {
  const phone = await figma.getNodeByIdAsync(phoneId);
  if (!phone) throw new Error('phone ' + phoneId);
  configurePhone(phone);
  phone.children.forEach((c) => c.remove());

  try {
    const sbMc = await figma.importComponentByKeyAsync(KEYS.statusBar);
    const sb = sbMc.createInstance();
    sb.name = 'Status bar';
    appendFill(phone, sb);
  } catch (_) {}

  appendFill(phone, await createAppBar(appBarTitle));
  const body = await ensureBody(phone);

  appendFill(body, await createTextField('Nombre del viaje', 'Barcelona fin de semana'));
  appendFill(body, await createTextField('Destino', 'Barcelona'));

  const range = await importProduct(KEYS.motDateRange, 'MotDateRangeField');
  const start = range.findOne((n) => n.name === 'Start date');
  const end = range.findOne((n) => n.name === 'End date');
  if (start) {
    setProps(start, {
      'Label text#52808:484': 'Fecha inicio',
      'Input text#52808:726': startDate,
    });
  }
  if (end) {
    setProps(end, {
      'Label text#52808:484': 'Fecha fin',
      'Input text#52808:726': endDate,
    });
  }
  appendFill(body, range);

  appendFill(body, await createFilledButton('Guardar viaje'));
}

async function buildWalletManualPhone(phoneId) {
  const phone = await figma.getNodeByIdAsync(phoneId);
  if (!phone) throw new Error('phone ' + phoneId);
  configurePhone(phone);
  phone.children.forEach((c) => c.remove());

  try {
    const sbMc = await figma.importComponentByKeyAsync(KEYS.statusBar);
    const sb = sbMc.createInstance();
    sb.name = 'Status bar';
    appendFill(phone, sb);
  } catch (_) {}

  appendFill(phone, await createAppBar('Entrada manual'));
  const body = figma.createFrame();
  body.name = 'Body';
  body.layoutMode = 'VERTICAL';
  body.layoutGrow = 1;
  body.primaryAxisSizingMode = 'FIXED';
  body.counterAxisSizingMode = 'FIXED';
  body.paddingLeft = 16;
  body.paddingRight = 16;
  body.paddingTop = 16;
  body.paddingBottom = 16;
  body.fills = [];
  appendFill(phone, body);

  const scroll = figma.createFrame();
  scroll.name = 'Scroll';
  scroll.layoutMode = 'VERTICAL';
  scroll.layoutGrow = 1;
  scroll.itemSpacing = 16;
  scroll.fills = [];
  scroll.primaryAxisSizingMode = 'AUTO';

  const footer = figma.createFrame();
  footer.name = 'Footer';
  footer.layoutMode = 'VERTICAL';
  footer.paddingTop = 8;
  footer.fills = [];

  appendFill(body, scroll);
  appendFill(body, footer);

  appendFill(scroll, await createTextField('Título', 'IB 3254 · Madrid → Barcelona'));
  appendFill(scroll, await importProduct(KEYS.motDate, 'MotDateField'));
  appendFill(scroll, await importProduct(KEYS.motTime, 'MotTimeField'));
  appendFill(footer, await createFilledButton('Guardar'));
}

await figma.loadAllPagesAsync();
await figma.setCurrentPageAsync(figma.root.children.find((p) => p.name === '02 · Viajes — crear y editar'));
await buildTripFormPhone('261:926', 'Nuevo viaje', '04/07/2026', '06/07/2026');
await buildTripFormPhone('261:929', 'Editar viaje', '04/07/2026', '06/07/2026');

await figma.setCurrentPageAsync(figma.root.children.find((p) => p.name === '04 · Wallet'));
await buildWalletManualPhone('491:13980');

figma.notify('R4 date/time PRODUCT instancias aplicadas');
