/**
 * Reconstruye cap 1 Wallet · flujo de app con DS + text properties.
 * Ejecutar vía use_figma en Vf2tNMXyKAlJSV53A1v4Is.
 */
if (figma.fileKey !== 'Vf2tNMXyKAlJSV53A1v4Is') {
  throw new Error('ABORT: ejecutar en MyOwnTrip_design-file');
}

const KEYS = {
  statusBar: '88d8181832a7a046843e790ed88470cda99cabba',
  appBarSet: 'f57fabde1da94e3f942cc75ffd7a844d5bfb532e',
  tabsSet: '5b3e05495acb68822b3e4776811613d852c9b9e6',
  buttonFilledSet: '14e743023239aeab8e4b8dfe20de85ed8e5ba30a',
  buttonOutlineSet: '299fa03b373d5ebc27a4306000649895cf8989b5',
  listItemSet: '38db8d64ec419dbfb5a5028a663a91af3abddf20',
  iconAdd: '76fc3cf84b76e05f778f9ccdbf33ce12578bbf75',
  iconUpload: 'ded19a17fde095004ce0c27e82402fdae15eb668',
  iconArrowBack: '52d53666523506ac044165acdaf9318f4f0baa2f',
  iconFlight: 'fa70b6b4d1467bb1cd0ec149cf91247bed01524a',
  iconHotel: '34c3c17c8fc5010be6d94b31ee58d1fa5e4cc977',
  iconDelete: '2a48234db266f2b0c4a36c4f7ee933a0ea3c52d5',
  iconCar: 'fa70b6b4d1467bb1cd0ec149cf91247bed01524a',
  textHeadlineSmall: '790b1161ee4d5a474e3578be78d583da6fe24233',
  textTitleMedium: '1bc7aec6d9441a6ba4050174a7deed9481779eff',
  textTitleLarge: '240e03c655394d8eedff4761eff21a696519d4df',
  textBodyMedium: '8b638b84df298decb21e343aeab711861094ea7e',
  textLabelLarge: '2dc1c88642ddd9d906d37102f95881a29f64ad99',
};

const TRIP = {
  name: 'Barcelona fin de semana',
  destination: 'Barcelona',
  dates: '14 jun 2026 – 16 jun 2026',
  docCount: '4 documentos',
  subtitle: 'Vuelos, hoteles y documentos en un solo sitio',
};

const DOCS = [
  {
    type: 'Vuelo',
    iconKey: KEYS.iconFlight,
    title: 'IB 3254 · Madrid → Barcelona',
    meta: 'Vuelo · 14 jun 2026 · 09:15',
    schedule: '14 jun 2026 · 09:15',
    hasQr: true,
  },
  {
    type: 'Hotel',
    iconKey: KEYS.iconHotel,
    title: 'Hotel Casa Bonay',
    meta: 'Hotel · 14 jun 2026',
    schedule: '14 jun 2026',
    hasQr: false,
  },
  {
    type: 'Transporte',
    iconKey: KEYS.iconFlight,
    title: 'AVE 03142',
    meta: 'Transporte · 16 jun 2026 · 18:30',
    schedule: '16 jun 2026 · 18:30',
    hasQr: false,
  },
  {
    type: 'Actividad',
    iconKey: KEYS.iconHotel,
    title: 'Entrada Sagrada Familia',
    meta: 'Actividad · 15 jun 2026 · 11:00',
    schedule: '15 jun 2026 · 11:00',
    hasQr: false,
  },
];

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

function fillH(node) {
  if ('layoutSizingHorizontal' in node) node.layoutSizingHorizontal = 'FILL';
}

function clearChildren(frame) {
  [...frame.children].forEach((child) => child.remove());
}

function cleanupCap1(cap) {
  for (const child of [...cap.children]) {
    if (child.name === 'phone') continue;
    if (child.name === 'caption' && child.type === 'TEXT') continue;
    child.remove();
  }
}

function paddedRow(name, paddingLeft, paddingTop = 0) {
  const row = figma.createFrame();
  row.name = name;
  row.layoutMode = 'HORIZONTAL';
  row.primaryAxisSizingMode = 'AUTO';
  row.counterAxisSizingMode = 'AUTO';
  row.paddingLeft = paddingLeft;
  row.paddingTop = paddingTop;
  row.fills = [];
  return row;
}

async function applyTextStyle(node, styleKey) {
  try {
    const style = await figma.importStyleByKeyAsync(styleKey);
    if (node.type === 'TEXT') node.textStyleId = style.id;
  } catch (_) {}
}

async function createAppBar(title) {
  const set = await figma.importComponentSetByKeyAsync(KEYS.appBarSet);
  const comp = set.children.find((c) => c.name === 'Configuration=Small, Elevation=Flat');
  const inst = comp.createInstance();
  inst.name = 'App bar';
  inst.layoutSizingHorizontal = 'FILL';
  setProps(inst, {
    'Show 1st trailing': false,
    'Show 2nd trailing': false,
    'Show 3rd trailing': false,
  });
  const textContent = inst.findOne((n) => n.name === 'Text content');
  if (textContent) {
    setProps(textContent, {
      Headline: title,
      'Show-supporting-text': false,
    });
    const supporting = textContent.findOne((n) => n.name === 'Supporting-text');
    if (supporting) supporting.visible = false;
  }
  const trailing = inst.findOne((n) => n.name === 'Trailing elements');
  if (trailing) trailing.visible = false;
  const leading = inst.findOne((n) => n.name === 'Leading icon');
  const iconNode = leading?.findOne((n) => n.type === 'INSTANCE' && n.name === 'Icon');
  if (iconNode) iconNode.swapComponent(await getIcon(KEYS.iconArrowBack));
  return inst;
}

async function createTabs() {
  const set = await figma.importComponentSetByKeyAsync(KEYS.tabsSet);
  const comp = set.children.find(
    (c) => c.name === 'Type=Scrollable, Style=Primary, Configuration=Label only',
  );
  const inst = comp.createInstance();
  inst.name = 'Tabs';
  inst.layoutSizingHorizontal = 'FILL';
  const labels = ['Wallet', 'Días', 'Gastos', 'Sitios'];
  const tabs = inst.findAll((n) => /^Tab \d$/.test(n.name));
  labels.forEach((label, i) => {
    if (!tabs[i]) return;
    setProps(tabs[i], {
      'Label text': label,
      Selected: i === 0 ? 'True' : 'False',
      State: 'Enabled',
    });
  });
  tabs.forEach((tab) => {
    const num = parseInt(tab.name.replace('Tab ', ''), 10);
    if (num > 4) tab.visible = false;
  });
  return inst;
}

async function createFilledButton(label, iconKey) {
  const set = await figma.importComponentSetByKeyAsync(KEYS.buttonFilledSet);
  const comp = set.children.find((c) => c.name === 'Type=Square, Size=Medium, State=Enabled');
  const inst = comp.createInstance();
  inst.layoutGrow = 1;
  setProps(inst, { 'Label text': label, 'Show icon': true });
  const iconNode = inst.findOne((n) => n.type === 'INSTANCE' && n.name === 'Icon');
  if (iconNode) iconNode.swapComponent(await getIcon(iconKey));
  return inst;
}

async function createOutlineButton(label, iconKey) {
  const set = await figma.importComponentSetByKeyAsync(KEYS.buttonOutlineSet);
  const comp = set.children.find((c) => c.name === 'Type=Square, Size=Medium, State=Enabled');
  const inst = comp.createInstance();
  inst.layoutGrow = 1;
  setProps(inst, { 'Label text': label, 'Show icon': true });
  const iconNode = inst.findOne((n) => n.type === 'INSTANCE' && n.name === 'Icon');
  if (iconNode) iconNode.swapComponent(await getIcon(iconKey));
  return inst;
}

async function styledText(name, characters, styleKey) {
  await figma.loadFontAsync({ family: 'Inter', style: 'Regular' });
  await figma.loadFontAsync({ family: 'Fraunces', style: 'SemiBold' });
  const t = figma.createText();
  t.name = name;
  t.characters = characters;
  t.textAutoResize = 'HEIGHT';
  t.layoutSizingHorizontal = 'FILL';
  await applyTextStyle(t, styleKey);
  return t;
}

async function createListItem({ title, meta, iconKey, showDelete }) {
  const set = await figma.importComponentSetByKeyAsync(KEYS.listItemSet);
  const comp = set.children.find(
    (c) =>
      c.name ===
      'Condition=2 line, Leading=Icon, Trailing=Icon, Show overline=False, Show supporting text=True',
  );
  const inst = comp.createInstance();
  inst.name = title;
  inst.layoutSizingHorizontal = 'FILL';
  setProps(inst, {
    Headline: title,
    'Supporting text': meta,
    'Show divider': true,
  });
  const leadIcon = inst
    .findOne((n) => n.name === 'Leading element')
    ?.findOne((n) => n.type === 'INSTANCE');
  if (leadIcon) leadIcon.swapComponent(await getIcon(iconKey));
  if (showDelete) {
    const trailIcon = inst
      .findOne((n) => n.name === 'Trailing element')
      ?.findOne((n) => n.type === 'INSTANCE');
    if (trailIcon) trailIcon.swapComponent(await getIcon(KEYS.iconDelete));
  }
  return inst;
}

async function createHighlightCard(doc) {
  const card = figma.createFrame();
  card.name = `Highlight · ${doc.title}`;
  card.layoutMode = 'VERTICAL';
  card.primaryAxisSizingMode = 'AUTO';
  card.counterAxisSizingMode = 'AUTO';
  card.minWidth = 248;
  card.layoutSizingHorizontal = 'HUG';
  card.itemSpacing = 8;
  card.paddingLeft = 16;
  card.paddingRight = 16;
  card.paddingTop = 16;
  card.paddingBottom = 16;
  card.cornerRadius = 12;
  card.fills = [{ type: 'SOLID', color: { r: 0.93, g: 0.9, b: 0.86 } }];

  const row = figma.createFrame();
  row.name = 'Type row';
  row.layoutMode = 'HORIZONTAL';
  row.primaryAxisSizingMode = 'AUTO';
  row.counterAxisSizingMode = 'AUTO';
  row.itemSpacing = 10;
  row.fills = [];
  card.appendChild(row);

  const iconWrap = figma.createFrame();
  iconWrap.name = 'Icon';
  iconWrap.resize(36, 36);
  iconWrap.layoutMode = 'HORIZONTAL';
  iconWrap.primaryAxisSizingMode = 'FIXED';
  iconWrap.counterAxisSizingMode = 'FIXED';
  iconWrap.primaryAxisAlignItems = 'CENTER';
  iconWrap.counterAxisAlignItems = 'CENTER';
  iconWrap.cornerRadius = 18;
  iconWrap.fills = [{ type: 'SOLID', color: { r: 0.9, g: 0.87, b: 0.83 } }];
  const iconInst = (await getIcon(doc.iconKey)).createInstance();
  iconInst.resize(20, 20);
  iconWrap.appendChild(iconInst);
  row.appendChild(iconWrap);

  const typeLabel = await styledText('Type', doc.type, KEYS.textLabelLarge);
  typeLabel.layoutSizingHorizontal = 'HUG';
  typeLabel.fills = [{ type: 'SOLID', color: { r: 0.51, g: 0.33, b: 0.08 } }];
  row.appendChild(typeLabel);

  const title = await styledText('Title', doc.title, KEYS.textTitleLarge);
  title.layoutSizingHorizontal = 'FILL';
  card.appendChild(title);

  const sched = await styledText('Schedule', doc.schedule, KEYS.textBodyMedium);
  sched.fills = [{ type: 'SOLID', color: { r: 0.29, g: 0.27, b: 0.25 } }];
  card.appendChild(sched);

  return card;
}

async function buildWalletBody(body) {
  clearChildren(body);

  const header = figma.createFrame();
  header.name = 'Wallet header';
  header.layoutMode = 'VERTICAL';
  header.primaryAxisSizingMode = 'AUTO';
  header.counterAxisSizingMode = 'FIXED';
  header.layoutSizingHorizontal = 'FILL';
  header.itemSpacing = 2;
  header.paddingLeft = 16;
  header.paddingRight = 16;
  header.paddingTop = 8;
  header.paddingBottom = 8;
  header.fills = [];

  const dest = await styledText('destination', TRIP.destination, KEYS.textHeadlineSmall);
  header.appendChild(dest);
  fillH(dest);
  const dates = await styledText('dates', TRIP.dates, KEYS.textBodyMedium);
  dates.fills = [{ type: 'SOLID', color: { r: 0.29, g: 0.27, b: 0.25 } }];
  header.appendChild(dates);
  const count = await styledText('doc_count', TRIP.docCount, KEYS.textTitleLarge);
  count.layoutSizingHorizontal = 'FILL';
  header.appendChild(count);
  const sub = await styledText('subtitle', TRIP.subtitle, KEYS.textBodyMedium);
  sub.fills = [{ type: 'SOLID', color: { r: 0.29, g: 0.27, b: 0.25 } }];
  header.appendChild(sub);
  body.appendChild(header);
  fillH(header);

  const actions = figma.createFrame();
  actions.name = 'Quick actions';
  actions.layoutMode = 'HORIZONTAL';
  actions.primaryAxisSizingMode = 'FIXED';
  actions.counterAxisSizingMode = 'AUTO';
  actions.layoutSizingHorizontal = 'FILL';
  actions.itemSpacing = 12;
  actions.paddingLeft = 16;
  actions.paddingRight = 16;
  actions.fills = [];
  const addBtn = await createFilledButton('Añadir', KEYS.iconAdd);
  const importBtn = await createOutlineButton('Importar', KEYS.iconUpload);
  actions.appendChild(addBtn);
  fillH(addBtn);
  actions.appendChild(importBtn);
  fillH(importBtn);
  fillH(actions);
  body.appendChild(actions);

  const proxSection = figma.createFrame();
  proxSection.name = 'Próximos section';
  proxSection.layoutMode = 'VERTICAL';
  proxSection.primaryAxisSizingMode = 'AUTO';
  proxSection.counterAxisSizingMode = 'AUTO';
  proxSection.layoutSizingHorizontal = 'FILL';
  proxSection.itemSpacing = 8;
  proxSection.fills = [];
  const proxTitleRow = paddedRow('Próximos title row', 16);
  const proxTitle = await styledText('Section', 'Próximos', KEYS.textTitleMedium);
  fillH(proxTitle);
  proxTitleRow.appendChild(proxTitle);
  fillH(proxTitleRow);
  proxSection.appendChild(proxTitleRow);
  const cards = figma.createFrame();
  cards.name = 'Highlight cards';
  cards.layoutMode = 'HORIZONTAL';
  cards.primaryAxisSizingMode = 'AUTO';
  cards.counterAxisSizingMode = 'AUTO';
  cards.itemSpacing = 12;
  cards.paddingLeft = 16;
  cards.paddingRight = 16;
  cards.fills = [];
  for (const doc of DOCS.slice(0, 2)) cards.appendChild(await createHighlightCard(doc));
  proxSection.appendChild(cards);
  body.appendChild(proxSection);

  const allTitleRow = paddedRow('All docs title row', 16, 8);
  const allTitle = await styledText('All docs title', 'Todos los documentos', KEYS.textTitleMedium);
  fillH(allTitle);
  allTitleRow.appendChild(allTitle);
  fillH(allTitleRow);
  body.appendChild(allTitleRow);

  for (const doc of DOCS) {
    body.appendChild(
      await createListItem({
        title: doc.title,
        meta: doc.meta,
        iconKey: doc.iconKey,
        showDelete: true,
      }),
    );
  }
}

async function ensurePhoneShell(cap, buildBody) {
  let phone = cap.findOne((n) => n.name === 'phone');
  if (!phone) {
    phone = figma.createFrame();
    phone.name = 'phone';
    phone.resize(360, 800);
    phone.layoutMode = 'VERTICAL';
    phone.clipsContent = true;
    phone.primaryAxisSizingMode = 'FIXED';
    phone.counterAxisSizingMode = 'FIXED';
    phone.fills = [{ type: 'SOLID', color: { r: 1, g: 0.985, b: 0.969 } }];
    cap.appendChild(phone);
  }

  phone.children.forEach((c) => {
    if (!['Status bar', 'App bar', 'Tabs', 'Body'].includes(c.name)) c.remove();
  });

  if (!phone.findOne((n) => n.name === 'Status bar')) {
    try {
      const sbMc = await figma.importComponentByKeyAsync(KEYS.statusBar);
      const sb = sbMc.createInstance();
      sb.name = 'Status bar';
      sb.layoutSizingHorizontal = 'FILL';
      phone.insertChild(0, sb);
    } catch (_) {
      const homePage = figma.root.children.find((p) => p.name.includes('01'));
      const ref = homePage?.findOne((n) => n.name === 'Status bar' && n.type === 'INSTANCE');
      if (ref) phone.insertChild(0, ref.clone());
    }
  }

  let appBar = phone.findOne((n) => n.name === 'App bar');
  if (appBar) appBar.remove();
  appBar = await createAppBar(TRIP.name);
  phone.insertChild(1, appBar);

  let tabs = phone.findOne((n) => n.name === 'Tabs');
  if (tabs) tabs.remove();
  tabs = await createTabs();
  phone.insertChild(2, tabs);

  let body = phone.findOne((n) => n.name === 'Body');
  if (!body) {
    body = figma.createFrame();
    body.name = 'Body';
    body.layoutMode = 'VERTICAL';
    body.layoutSizingHorizontal = 'FILL';
    body.layoutGrow = 1;
    body.primaryAxisSizingMode = 'AUTO';
    body.counterAxisSizingMode = 'FIXED';
    body.itemSpacing = 8;
    body.paddingBottom = 24;
    body.fills = [{ type: 'SOLID', color: { r: 1, g: 0.985, b: 0.969 } }];
    phone.appendChild(body);
  }
  body.paddingLeft = 0;
  body.paddingRight = 0;

  await buildBody(body);
}

function hugWidth(node) {
  if ('layoutSizingHorizontal' in node) node.layoutSizingHorizontal = 'HUG';
  if ('primaryAxisSizingMode' in node && node.layoutMode) node.primaryAxisSizingMode = 'AUTO';
  if ('counterAxisSizingMode' in node && node.layoutMode) node.counterAxisSizingMode = 'AUTO';
}

await figma.setCurrentPageAsync(figma.root.children.find((p) => p.id === '71:3'));
const flow = figma.currentPage.findOne((n) => n.id === '266:75');
if (!flow) throw new Error('Wallet flow not found');
const screens = flow.findOne((n) => n.name === 'Screens');
if (!screens) throw new Error('Screens not found');

let cap1 = screens.children.find((c) => /cap\s*1/i.test(c.name));
if (!cap1) {
  cap1 = figma.createFrame();
  cap1.name = 'cap 1 — wallet con documentos';
  cap1.layoutMode = 'VERTICAL';
  cap1.itemSpacing = 16;
  cap1.fills = [];
  screens.appendChild(cap1);
}

let caption = cap1.findOne((n) => n.name === 'caption' && n.type === 'TEXT');
if (!caption) {
  caption = figma.createText();
  caption.name = 'caption';
  await figma.loadFontAsync({ family: 'Inter', style: 'Regular' });
  caption.fontSize = 12;
  caption.fills = [{ type: 'SOLID', color: { r: 0.4, g: 0.4, b: 0.4 } }];
  cap1.insertChild(0, caption);
}
caption.characters =
  'cap 1 · Wallet · con documentos\npreview: TripDetailWalletTabPreview · route: trip_detail/{tripId} · tab Wallet';
caption.textAutoResize = 'HEIGHT';
caption.layoutSizingHorizontal = 'FILL';

cleanupCap1(cap1);
await ensurePhoneShell(cap1, buildWalletBody);

hugWidth(flow);
hugWidth(screens);
hugWidth(cap1);

// Limpieza: instancias huérfanas sueltas en la página (provocan errores de selección en Bridge)
const keep = new Set([flow.id]);
flow.findAll().forEach((n) => keep.add(n.id));
for (const child of [...figma.currentPage.children]) {
  if (!keep.has(child.id)) child.remove();
}

flow.layoutMode = 'VERTICAL';
flow.primaryAxisSizingMode = 'AUTO';
flow.counterAxisSizingMode = 'AUTO';
screens.layoutMode = 'HORIZONTAL';
screens.primaryAxisSizingMode = 'AUTO';
screens.counterAxisSizingMode = 'AUTO';
screens.itemSpacing = 40;
cap1.layoutMode = 'VERTICAL';
cap1.primaryAxisSizingMode = 'AUTO';
cap1.counterAxisSizingMode = 'AUTO';

figma.viewport.scrollAndZoomIntoView([cap1]);

return {
  status: 'rebuilt',
  cap1Id: cap1.id,
  url: `https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is?node-id=${cap1.id.replace(':', '-')}`,
};
