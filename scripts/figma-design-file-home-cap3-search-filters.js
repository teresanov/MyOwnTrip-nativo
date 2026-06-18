/**
 * Duplica cap 2 (Home con viajes) → cap 3 con búsqueda activa + menú filtros.
 * NO modifica ni borra cap 2.
 * Ejecutar vía figma_execute en Vf2tNMXyKAlJSV53A1v4Is (Bridge en design-file).
 */
if (figma.fileKey !== 'Vf2tNMXyKAlJSV53A1v4Is') {
  throw new Error('ABORT: ejecutar en MyOwnTrip_design-file');
}

const CAP2_ID = '205:1018'; // actualizar si cambia el id de cap 2
const MENU_KEY = '6e4dd0de08c0535ab21479206cc3de6852e84e12';
const SEARCH_PRESSED_KEY = '6988ec397ca021a720e04873c12d4b54760c2ac4';

await figma.loadFontAsync({ family: 'Roboto', style: 'Regular' });
await figma.loadFontAsync({ family: 'Inter', style: 'Regular' });
await figma.loadFontAsync({ family: 'Inter', style: 'Medium' });
await figma.loadFontAsync({ family: 'Fraunces', style: 'SemiBold' });

function findScreens(node) {
  let current = node;
  while (current) {
    if (current.name === 'Screens') return current;
    current = current.parent;
  }
  return null;
}

function findSearchBar(root) {
  return root.findOne((n) => {
    if (n.type !== 'INSTANCE') return false;
    const name = n.mainComponent?.parent?.name || n.mainComponent?.name || n.name;
    return /search bar/i.test(name) || n.name === 'Search bar';
  });
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

async function setSearchPressed(searchInst, query) {
  const pressed = await figma.importComponentByKeyAsync(SEARCH_PRESSED_KEY);
  const mc = await searchInst.getMainComponentAsync();
  if (mc?.key !== SEARCH_PRESSED_KEY) {
    searchInst.swapComponent(pressed);
  }
  setProps(searchInst, {
    'Show 2nd trailing icon': true,
    'Show 1st trailing icon': true,
    'Show leading icon': true,
    'Show avatar': 'False',
    'Placeholder text': '',
  });
  const textNode =
    searchInst.findOne(
      (n) => n.type === 'TEXT' && /input|supporting|placeholder/i.test(n.name),
    ) || searchInst.findOne((n) => n.type === 'TEXT');
  if (textNode && 'characters' in textNode) {
    await figma.loadFontAsync(textNode.fontName);
    textNode.characters = query;
  }
  return searchInst;
}

function relayoutMenu(menu, searchInst) {
  menu.layoutPositioning = 'ABSOLUTE';
  menu.resize(328, menu.height);
  menu.x = 0;
  menu.y = searchInst.y + searchInst.height + 8;
}

async function setMenuItemLabel(item, text, selected) {
  const label =
    item.findOne((n) => n.type === 'TEXT' && /label/i.test(n.name)) ||
    item.findAll((n) => n.type === 'TEXT').pop();
  if (label) {
    await figma.loadFontAsync(label.fontName);
    label.characters = text;
  }
  setProps(item, {
    Selected: selected ? 'True' : 'False',
    'Show leading element': false,
    'Show trailing element': selected,
  });
}

async function relabelMenu(menu) {
  setProps(menu, { 'Show section label': true });
  const list1Labels = ['Todos los viajes', 'En curso', 'Próximos', 'Pasados'];
  const list2Labels = ['Fecha — próximo primero', 'Nombre A–Z', 'Destino A–Z'];
  const lists = menu.children.filter((n) => /^List \d/.test(n.name));
  const sectionTitles = ['Mostrar', 'Ordenar'];
  for (let li = 0; li < lists.length; li++) {
    const list = lists[li];
    const sectionLabel = list.findOne((n) => /Label \d/i.test(n.name));
    if (sectionLabel) {
      sectionLabel.visible = true;
      const t = sectionLabel.findOne((n) => n.type === 'TEXT');
      if (t) {
        await figma.loadFontAsync(t.fontName);
        t.characters = sectionTitles[li] || '';
      }
    }
    list
      .findAll((n) => /Menu-item 06|Menu-item 04|Menu-item 05/.test(n.name))
      .forEach((n) => {
        n.visible = false;
      });
    const items = list.children.filter((n) => /Menu-item 0[123]/.test(n.name));
    const labels = li === 0 ? list1Labels : list2Labels;
    for (let i = 0; i < labels.length; i++) {
      if (items[i]) await setMenuItemLabel(items[i], labels[i], i === 0);
    }
  }
}

async function alignHeroToReference(cap) {
  const hero = cap.findOne((n) => n.name === 'Hero header');
  if (!hero) return;
  const texts = hero.findAll((n) => n.type === 'TEXT');
  if (texts[0]) {
    await figma.loadFontAsync(texts[0].fontName);
    texts[0].characters = 'Buenas tardes, Raquel';
  }
  if (texts[1]) {
    await figma.loadFontAsync(texts[1].fontName);
    texts[1].characters = 'Barcelona';
  }
  if (texts[2]) {
    await figma.loadFontAsync(texts[2].fontName);
    texts[2].characters = 'Sale en 3 días · 3 viajes';
  }
}

const cap2 =
  (await figma.getNodeByIdAsync(CAP2_ID)) ||
  findScreens(figma.currentPage)?.children.find((c) => /cap\s*2/i.test(c.name));
if (!cap2) throw new Error('cap 2 not found — abre Shell — Home · flow');

const screens = findScreens(cap2);
if (!screens) throw new Error('Screens frame not found');

// Solo eliminar cap 3 previo (re-ejecución), nunca cap 2
const existingCap3 = screens.children.find((c) => /cap\s*3/i.test(c.name));
if (existingCap3) existingCap3.remove();

const cap3 = cap2.clone();
cap3.name = 'cap 3';
screens.appendChild(cap3);
cap3.x = cap2.x + cap2.width + 40;
cap3.y = cap2.y;

const caption = cap3.findOne((n) => n.type === 'TEXT' && /cap/i.test(n.name));
if (caption) {
  caption.characters =
    'cap 3 · Home — búsqueda activa · menú filtros\nroute: TripListScreen (query + filterMenu)';
}

const phone = cap3.findOne((n) => n.name === 'phone');
if (!phone) throw new Error('phone frame not found in cap 3 clone');

const body =
  phone.findOne((n) => n.name === 'Body') ||
  phone.children.find((n) => n.type === 'FRAME' && n.height >= 500);
if (!body) throw new Error('Body not found in cap 3');

await alignHeroToReference(cap3);

const searchInst = findSearchBar(body);
if (!searchInst) throw new Error('Search bar not found in cap 3');

await setSearchPressed(searchInst, 'Barcelona');

body.findOne((n) => n.name === 'Filter menu')?.remove();
const menuMc = await figma.importComponentByKeyAsync(MENU_KEY);
const menu = menuMc.createInstance();
menu.name = 'Filter menu';
body.appendChild(menu);
await relabelMenu(menu);
relayoutMenu(menu, searchInst);

figma.viewport.scrollAndZoomIntoView([cap2, cap3]);

return {
  status: 'cap 3 created from cap 2 clone',
  cap2Id: cap2.id,
  cap3Id: cap3.id,
  cap2Url: `https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=${cap2.id.replace(':', '-')}`,
  cap3Url: `https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=${cap3.id.replace(':', '-')}`,
};
