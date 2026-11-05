/**
 * Cap 1 step 1/2 — shell (Status bar, App bar, Tabs, Body vacío).
 * fileKey Vf2tNMXyKAlJSV53A1v4Is · phone 491:13876
 */
if (figma.fileKey !== 'Vf2tNMXyKAlJSV53A1v4Is') throw new Error('ABORT design-file');

const KEYS = {
  statusBar: '88d8181832a7a046843e790ed88470cda99cabba',
  appBarSet: 'f57fabde1da94e3f942cc75ffd7a844d5bfb532e',
  tabsSet: '5b3e05495acb68822b3e4776811613d852c9b9e6',
  iconArrowBack: '52d53666523506ac044165acdaf9318f4f0baa2f',
};

function fillH(node) {
  if ('layoutSizingHorizontal' in node) node.layoutSizingHorizontal = 'FILL';
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

await figma.loadFontAsync({ family: 'Inter', style: 'Regular' });
const phone = await figma.getNodeByIdAsync('491:13876');
if (!phone || phone.name !== 'phone') throw new Error('phone 491:13876 missing');
const cap = phone.parent;
phone.resize(360, 800);
phone.clipsContent = true;
phone.layoutMode = 'VERTICAL';
phone.fills = [{ type: 'SOLID', color: { r: 1, g: 0.985, b: 0.969 } }];

for (const child of [...cap.children]) {
  if (child.name === 'phone') continue;
  if (child.name === 'caption' && child.type === 'TEXT') continue;
  child.remove();
}

let caption = cap.findOne((n) => n.name === 'caption' && n.type === 'TEXT');
if (!caption) {
  caption = figma.createText();
  caption.name = 'caption';
  cap.insertChild(0, caption);
}
caption.fontSize = 12;
caption.fills = [{ type: 'SOLID', color: { r: 0.4, g: 0.4, b: 0.4 } }];
caption.characters =
  'cap 1 · Wallet · con documentos\npreview: TripDetailWalletTabPreview · capture: cap-01.png';
caption.textAutoResize = 'HEIGHT';

[...phone.children]
  .filter((c) => !['Status bar', 'Body'].includes(c.name))
  .forEach((c) => c.remove());

if (!phone.findOne((n) => n.name === 'Status bar')) {
  const sb = (await figma.importComponentByKeyAsync(KEYS.statusBar)).createInstance();
  sb.name = 'Status bar';
  phone.insertChild(0, sb);
  fillH(sb);
}

const appSet = await figma.importComponentSetByKeyAsync(KEYS.appBarSet);
const appComp = appSet.children.find((c) => c.name === 'Configuration=Small, Elevation=Flat');
const appBar = appComp.createInstance();
appBar.name = 'App bar';
setProps(appBar, {
  'Show 1st trailing action': false,
  'Show 2nd trailing action': false,
  'Show 3rd trailing action': false,
});
const textContent = appBar.findOne((n) => n.name === 'Text content');
if (textContent) {
  setProps(textContent, { Headline: 'Barcelona fin de semana', 'Show-supporting-text': false });
  const supporting = textContent.findOne((n) => n.name === 'Supporting-text');
  if (supporting) supporting.visible = false;
}
const trailing = appBar.findOne((n) => n.name === 'Trailing elements');
if (trailing) trailing.visible = false;
const leadIcon = appBar
  .findOne((n) => n.name === 'Leading icon')
  ?.findOne((n) => n.type === 'INSTANCE' && n.name === 'Icon');
if (leadIcon) leadIcon.swapComponent(await figma.importComponentByKeyAsync(KEYS.iconArrowBack));
phone.insertChild(1, appBar);
fillH(appBar);

const tabsSet = await figma.importComponentSetByKeyAsync(KEYS.tabsSet);
const tabsComp = tabsSet.children.find(
  (c) => c.name === 'Type=Scrollable, Style=Primary, Configuration=Label only',
);
const tabs = tabsComp.createInstance();
tabs.name = 'Tabs';
const labels = ['Wallet', 'Días', 'Gastos', 'Sitios'];
const tabNodes = tabs.findAll((n) => /^Tab \d$/.test(n.name));
labels.forEach((label, i) => {
  if (!tabNodes[i]) return;
  setProps(tabNodes[i], { 'Label text': label, Selected: i === 0 ? 'True' : 'False', State: 'Enabled' });
});
tabNodes.forEach((tab) => {
  const num = parseInt(tab.name.replace('Tab ', ''), 10);
  if (num > 4) tab.visible = false;
});
phone.insertChild(2, tabs);
fillH(tabs);

let body = phone.findOne((n) => n.name === 'Body');
if (!body) {
  body = figma.createFrame();
  body.name = 'Body';
  phone.appendChild(body);
}
body.layoutMode = 'VERTICAL';
body.layoutGrow = 1;
body.itemSpacing = 8;
body.paddingBottom = 24;
body.fills = [{ type: 'SOLID', color: { r: 1, g: 0.985, b: 0.969 } }];
fillH(body);
[...body.children].forEach((c) => c.remove());

figma.viewport.scrollAndZoomIntoView([phone]);
figma.notify('cap 1 shell OK — espera step 2 (body)');
return { step: 'shell', phoneId: phone.id, kids: phone.children.map((c) => c.name) };
