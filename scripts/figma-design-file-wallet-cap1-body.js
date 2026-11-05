/**
 * Cap 1 step 2/2 — body (header, CTAs, chips, highlights, lista).
 * fileKey Vf2tNMXyKAlJSV53A1v4Is · phone 491:13876
 */
if (figma.fileKey !== 'Vf2tNMXyKAlJSV53A1v4Is') throw new Error('ABORT design-file');

const KEYS = {
  buttonFilledSet: '14e743023239aeab8e4b8dfe20de85ed8e5ba30a',
  buttonOutlineSet: '299fa03b373d5ebc27a4306000649895cf8989b5',
  listItemSet: '38db8d64ec419dbfb5a5028a663a91af3abddf20',
  iconAdd: '76fc3cf84b76e05f778f9ccdbf33ce12578bbf75',
  iconUpload: 'ded19a17fde095004ce0c27e82402fdae15eb668',
  iconFlight: 'fa70b6b4d1467bb1cd0ec149cf91247bed01524a',
  iconHotel: '34c3c17c8fc5010be6d94b31ee58d1fa5e4cc977',
  textHeadlineSmall: '790b1161ee4d5a474e3578be78d583da6fe24233',
  textTitleMedium: '1bc7aec6d9441a6ba4050174a7deed9481779eff',
  textTitleLarge: '240e03c655394d8eedff4761eff21a696519d4df',
  textBodyMedium: '8b638b84df298decb21e343aeab711861094ea7e',
  textLabelLarge: '2dc1c88642ddd9d906d37102f95881a29f64ad99',
};
const DOCS = [
  { type: 'Vuelo', iconKey: KEYS.iconFlight, title: 'IB 3254 · Madrid → Barcelona', meta: 'Vuelo · 4 jul 2026 · 09:15', schedule: '4 jul 2026 · 09:15' },
  { type: 'Hotel', iconKey: KEYS.iconHotel, title: 'Hotel Casa Bonay', meta: 'Hotel · 4 jul 2026', schedule: '4 jul 2026' },
  { type: 'Transporte', iconKey: KEYS.iconFlight, title: 'AVE 03142', meta: 'Transporte · 6 jul 2026 · 18:30', schedule: '6 jul 2026 · 18:30' },
  { type: 'Actividad', iconKey: KEYS.iconHotel, title: 'Entrada Sagrada Familia', meta: 'Actividad · 5 jul 2026 · 11:00', schedule: '5 jul 2026 · 11:00' },
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
async function applyTextStyle(node, styleKey) {
  try {
    const style = await figma.importStyleByKeyAsync(styleKey);
    if (node.type === 'TEXT') await node.setTextStyleIdAsync(style.id);
  } catch (_) {}
}
async function styledText(name, characters, styleKey, fill) {
  await figma.loadFontAsync({ family: 'Inter', style: 'Regular' });
  await figma.loadFontAsync({ family: 'Fraunces', style: 'SemiBold' });
  const t = figma.createText();
  t.name = name;
  t.characters = characters;
  t.textAutoResize = 'HEIGHT';
  await applyTextStyle(t, styleKey);
  if (fill) t.fills = [{ type: 'SOLID', color: fill }];
  return t;
}
async function button(setKey, label, iconKey) {
  const set = await figma.importComponentSetByKeyAsync(setKey);
  const comp = set.children.find((c) => c.name === 'Type=Square, Size=Medium, State=Enabled');
  const inst = comp.createInstance();
  setProps(inst, { 'Label text': label, 'Show icon': true });
  const iconNode = inst.findOne((n) => n.type === 'INSTANCE' && n.name === 'Icon');
  if (iconNode) iconNode.swapComponent(await getIcon(iconKey));
  return inst;
}

const phone = await figma.getNodeByIdAsync('491:13876');
const body = phone.findOne((n) => n.name === 'Body');
if (!body) throw new Error('Body missing — run shell first');
[...body.children].forEach((c) => c.remove());

const muted = { r: 0.29, g: 0.27, b: 0.25 };
const header = figma.createFrame();
header.name = 'Wallet header';
header.layoutMode = 'VERTICAL';
header.itemSpacing = 2;
header.paddingLeft = header.paddingRight = 16;
header.paddingTop = header.paddingBottom = 8;
header.fills = [];
header.appendChild(await styledText('destination', 'Barcelona', KEYS.textHeadlineSmall));
header.appendChild(await styledText('dates', '4 jul 2026 – 6 jul 2026', KEYS.textBodyMedium, muted));
header.appendChild(await styledText('doc_count', '4 documentos', KEYS.textTitleLarge));
header.appendChild(await styledText('subtitle', 'Vuelos, hoteles y documentos en un solo sitio', KEYS.textBodyMedium, muted));
body.appendChild(header);
fillH(header);
[...header.children].forEach(fillH);

const actions = figma.createFrame();
actions.name = 'Quick actions';
actions.layoutMode = 'HORIZONTAL';
actions.itemSpacing = 12;
actions.paddingLeft = actions.paddingRight = 16;
actions.fills = [];
const addBtn = await button(KEYS.buttonFilledSet, 'Añadir', KEYS.iconAdd);
const importBtn = await button(KEYS.buttonOutlineSet, 'Importar', KEYS.iconUpload);
actions.appendChild(addBtn);
actions.appendChild(importBtn);
body.appendChild(actions);
fillH(actions);
fillH(addBtn);
fillH(importBtn);
addBtn.layoutGrow = 1;
importBtn.layoutGrow = 1;

const cap2 = await figma.getNodeByIdAsync('491:13915');
const chipTpl = cap2?.findOne((n) => n.name === 'Filter chips' && n.type === 'FRAME');
if (chipTpl) {
  const chips = chipTpl.clone();
  chips.name = 'Filter chips';
  body.appendChild(chips);
  fillH(chips);
}

const prox = figma.createFrame();
prox.name = 'Próximos section';
prox.layoutMode = 'VERTICAL';
prox.itemSpacing = 8;
prox.fills = [];
const proxTitle = figma.createFrame();
proxTitle.layoutMode = 'HORIZONTAL';
proxTitle.paddingLeft = 16;
proxTitle.fills = [];
proxTitle.appendChild(await styledText('Section', 'Próximos', KEYS.textTitleMedium));
prox.appendChild(proxTitle);
fillH(proxTitle);
fillH(proxTitle.children[0]);
const cards = figma.createFrame();
cards.name = 'Highlight cards';
cards.layoutMode = 'HORIZONTAL';
cards.itemSpacing = 12;
cards.paddingLeft = cards.paddingRight = 16;
cards.fills = [];
for (const doc of DOCS) {
  const card = figma.createFrame();
  card.name = 'Highlight · ' + doc.title;
  card.layoutMode = 'VERTICAL';
  card.minWidth = 248;
  card.itemSpacing = 8;
  card.paddingLeft = card.paddingRight = card.paddingTop = card.paddingBottom = 16;
  card.cornerRadius = 12;
  card.fills = [{ type: 'SOLID', color: { r: 0.93, g: 0.9, b: 0.86 } }];
  const row = figma.createFrame();
  row.layoutMode = 'HORIZONTAL';
  row.itemSpacing = 10;
  row.primaryAxisAlignItems = 'CENTER';
  row.fills = [];
  const iconWrap = figma.createFrame();
  iconWrap.resize(36, 36);
  iconWrap.layoutMode = 'HORIZONTAL';
  iconWrap.primaryAxisAlignItems = iconWrap.counterAxisAlignItems = 'CENTER';
  iconWrap.cornerRadius = 18;
  iconWrap.fills = [{ type: 'SOLID', color: { r: 0.9, g: 0.87, b: 0.83 } }];
  const iconInst = (await getIcon(doc.iconKey)).createInstance();
  iconInst.resize(20, 20);
  iconWrap.appendChild(iconInst);
  row.appendChild(iconWrap);
  const typeLabel = await styledText('Type', doc.type, KEYS.textLabelLarge, { r: 0.51, g: 0.33, b: 0.08 });
  row.appendChild(typeLabel);
  card.appendChild(row);
  const title = await styledText('Title', doc.title, KEYS.textTitleLarge);
  card.appendChild(title);
  fillH(title);
  card.appendChild(await styledText('Schedule', doc.schedule, KEYS.textBodyMedium, muted));
  cards.appendChild(card);
}
prox.appendChild(cards);
body.appendChild(prox);
fillH(prox);

const allTitle = figma.createFrame();
allTitle.name = 'All docs title row';
allTitle.layoutMode = 'HORIZONTAL';
allTitle.paddingLeft = 16;
allTitle.paddingTop = 8;
allTitle.fills = [];
allTitle.appendChild(await styledText('All docs title', 'Documentos activos', KEYS.textTitleMedium));
body.appendChild(allTitle);
fillH(allTitle);
fillH(allTitle.children[0]);

const listSet = await figma.importComponentSetByKeyAsync(KEYS.listItemSet);
const listComp = listSet.children.find(
  (c) =>
    c.name ===
    'Condition=2 line, Leading=Icon, Trailing=Icon, Show overline=False, Show supporting text=True',
);
for (const doc of DOCS) {
  const inst = listComp.createInstance();
  inst.name = doc.title;
  setProps(inst, { Headline: doc.title, 'Supporting text': doc.meta, 'Show divider': true });
  const leadIcon = inst.findOne((n) => n.name === 'Leading element')?.findOne((n) => n.type === 'INSTANCE');
  if (leadIcon) leadIcon.swapComponent(await getIcon(doc.iconKey));
  const trailing = inst.findOne((n) => n.name === 'Trailing element');
  if (trailing) trailing.visible = false;
  body.appendChild(inst);
  fillH(inst);
}

figma.notify('cap 1 body OK — revisar vs cap-01.png');
return {
  step: 'body',
  bodyKids: body.children.map((c) => c.name),
  url: 'https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is?node-id=491-13876',
};
