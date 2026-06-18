/**
 * @deprecated Usar figma-design-file-home-cap3-search-filters.js
 * Home cap 3 — duplica cap 2 y aplica búsqueda activa + menú filtros (no modifica cap 2).
 * Ejecutar vía figma_execute en Vf2tNMXyKAlJSV53A1v4Is (Desktop Bridge en design-file).
 */
if (figma.fileKey !== 'Vf2tNMXyKAlJSV53A1v4Is') {
  throw new Error('ABORT: ejecutar en MyOwnTrip_design-file');
}

const ANCHOR_ID = '205:1020';
const MENU_KEY = '6e4dd0de08c0535ab21479206cc3de6852e84e12'; // Menu · Groups=2
const SEARCH_PRESSED_KEY = '6988ec397ca021a720e04873c12d4b54760c2ac4'; // Pressed · no avatar
const TUNE_ICON_KEY = '54616:25409'; // tune / filter_list

await figma.loadFontAsync({ family: 'Inter', style: 'Regular' });
await figma.loadFontAsync({ family: 'Inter', style: 'Medium' });

function findScreens(node) {
  let current = node;
  while (current) {
    if (current.name === 'Screens') return current;
    current = current.parent;
  }
  return null;
}

function findPhone(column) {
  return column.findOne((n) => n.type === 'FRAME' && n.name === 'phone');
}

function findBody(phone) {
  return (
    phone.findOne((n) => n.id === '205:1021') ||
    phone.findOne((n) => n.name === 'Body' || n.name === 'Content') ||
    phone.children.find((n) => n.type === 'FRAME' && n.height >= 700)
  );
}

function findSearchBar(root) {
  return root.findOne((n) => {
    if (n.type !== 'INSTANCE') return false;
    const name = n.mainComponent?.parent?.name || n.mainComponent?.name || n.name;
    return /search bar/i.test(name);
  });
}

async function setSearchPressed(searchInst) {
  const pressed = await figma.importComponentByKeyAsync(SEARCH_PRESSED_KEY);
  if (searchInst.mainComponent?.key !== SEARCH_PRESSED_KEY) {
    searchInst.swapComponent(pressed);
  }
  const props = searchInst.componentProperties;
  for (const key of Object.keys(props)) {
    if (/2nd trailing|second trailing/i.test(key)) {
      searchInst.setProperties({ [key]: true });
    }
    if (/avatar/i.test(key) && props[key].type === 'BOOLEAN') {
      searchInst.setProperties({ [key]: false });
    }
  }
  const tune = searchInst.findOne(
    (n) => n.name === '2nd trailing-icon' || /2nd trailing/i.test(n.name),
  );
  if (tune && tune.type === 'INSTANCE') {
    try {
      const tuneMc = await figma.importComponentByKeyAsync(TUNE_ICON_KEY);
      tune.swapComponent(tuneMc);
      tune.visible = true;
    } catch (_) {
      tune.visible = true;
    }
  }
  const textNode =
    searchInst.findOne((n) => n.type === 'TEXT' && /input|supporting/i.test(n.name)) ||
    searchInst.findOne((n) => n.type === 'TEXT');
  if (textNode && 'characters' in textNode) {
    textNode.characters = 'Barcelona';
    textNode.fills = [
      {
        type: 'SOLID',
        color: { r: 0.12, g: 0.11, b: 0.07 },
        visible: true,
      },
    ];
  }
  return searchInst;
}

function relayoutMenu(menu, searchInst, phone) {
  const phoneBox = phone.absoluteBoundingBox;
  const searchBox = searchInst.absoluteBoundingBox;
  if (!phoneBox || !searchBox) return;
  menu.x = searchBox.x - phoneBox.x + searchBox.width - menu.width;
  menu.y = searchBox.y - phoneBox.y + searchBox.height + 4;
}

function setMenuItemLabel(item, text, selected) {
  const label = item.findOne(
    (n) => n.type === 'TEXT' && /label/i.test(n.name),
  ) || item.findAll((n) => n.type === 'TEXT').pop();
  if (label) label.characters = text;
  const props = item.componentProperties || {};
  for (const key of Object.keys(props)) {
    if (/selected/i.test(key)) {
      item.setProperties({ [key]: selected ? 'True' : 'False' });
    }
    if (/leading element/i.test(key)) {
      item.setProperties({ [key]: true });
    }
    if (/trailing element/i.test(key)) {
      item.setProperties({ [key]: false });
    }
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
    if (sectionLabel && sectionLabel.type === 'INSTANCE') {
      sectionLabel.visible = true;
      const t = sectionLabel.findOne((n) => n.type === 'TEXT');
      if (t) {
        t.characters = list.name === 'List 1' ? 'Mostrar' : 'Ordenar';
      }
    }
  }
}

const anchor = await figma.getNodeByIdAsync(ANCHOR_ID);
if (!anchor) throw new Error('Anchor not found: ' + ANCHOR_ID);

const screens = findScreens(anchor);
if (!screens) throw new Error('Screens frame not found above ' + ANCHOR_ID);

let cap1 = anchor;
while (cap1 && cap1.parent !== screens) cap1 = cap1.parent;
if (!cap1 || cap1.parent !== screens) {
  cap1 = screens.children.find((c) => /cap\s*1/i.test(c.name)) || screens.children[0];
}
if (!cap1) throw new Error('cap 1 column not found');

const existingCap2 = screens.children.find((c) => /cap\s*2/i.test(c.name));
if (existingCap2) existingCap2.remove();

const cap2 = cap1.clone();
cap2.name = 'cap 2';
screens.appendChild(cap2);
cap2.x = cap1.x + cap1.width + 40;
cap2.y = cap1.y;

const caption = cap2.findOne((n) => n.type === 'TEXT' && /cap/i.test(n.name));
if (caption) {
  caption.characters =
    'cap 2 · Home — búsqueda activa · menú filtros\nroute: TripListScreen (query + filterMenu)';
}

const phone = findPhone(cap2);
if (!phone) throw new Error('phone frame not found in cap 2');

const body = findBody(phone);
if (!body) throw new Error('body/content not found in phone');

const searchInst = findSearchBar(body);
if (!searchInst) throw new Error('Search bar instance not found in cap 2');

await setSearchPressed(searchInst);

const oldMenu = body.findOne((n) => n.name === 'Filter menu');
if (oldMenu) oldMenu.remove();

const menuMc = await figma.importComponentByKeyAsync(MENU_KEY);
const menu = menuMc.createInstance();
menu.name = 'Filter menu';
body.appendChild(menu);
relabelMenu(menu);
relayoutMenu(menu, searchInst, phone);

figma.viewport.scrollAndZoomIntoView([cap2]);

return {
  status: 'cap 2 created',
  cap2Id: cap2.id,
  figmaUrl: `https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=${cap2.id.replace(':', '-')}`,
};
