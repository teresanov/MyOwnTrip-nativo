/**
 * Filter menu cap 3 — reset overrides, props de selección, variables M3.
 * Ejecutar en design-file Vf2tNMXyKAlJSV53A1v4Is.
 */
if (figma.fileKey !== 'Vf2tNMXyKAlJSV53A1v4Is') {
  throw new Error('ABORT: ejecutar en MyOwnTrip_design-file');
}

const FILTER_MENU_ID = '228:8306';
const M3_COLLECTION_KEY = '39e9d60dbca5b7c05972a54eaf218badec531859';
const CHECK_KEY = '2a44aa4da65888b360a84473faaf822deff6d5c3';

const VAR_KEYS = {
  'Schemes/Surface Container Low': '9fc6dbf707bd78f9d1abc5e587d1392debb2fe76',
  'Schemes/Secondary Container': '366811e6d0ed3a328f2a4273ec620a9c9e2fd84e',
  'Schemes/On Secondary Container': '435fdc5dca8fd67daef0ec48559c158a86740052',
  'Schemes/On Surface': '0f7c3b657886bd85a309a9b0bfb12cf204f341d3',
  'Schemes/Primary': '2522767ed48779c106a9a29f29cc14952c82fc7f',
  'Schemes/Outline': 'b075dc530cec1d03f152ca87f265e06cb7cee350',
};

function getEffectiveModeId(node, collection) {
  let current = node;
  while (current) {
    const mode = current.explicitVariableModes?.[collection.id];
    if (mode) return mode;
    current = current.parent;
  }
  return collection.modes[0].modeId;
}

function resolveVariableColor(variable, collections, allVars, node) {
  const collection = collections.find((c) => c.id === variable.variableCollectionId);
  let modeId = getEffectiveModeId(node, collection);
  let value = variable.valuesByMode[modeId];
  let depth = 0;
  while (value?.type === 'VARIABLE_ALIAS' && depth < 8) {
    const target = allVars.find((v) => v.id === value.id);
    if (!target) break;
    const targetCol = collections.find((c) => c.id === target.variableCollectionId);
    modeId = getEffectiveModeId(node, targetCol);
    value = target.valuesByMode[modeId];
    depth++;
  }
  return value;
}

function bindFill(node, variable, collections, allVars) {
  const color = resolveVariableColor(variable, collections, allVars, node);
  const base = { type: 'SOLID', color, visible: true };
  node.fills = [figma.variables.setBoundVariableForPaint(base, 'color', variable)];
}

function bindStroke(node, variable, collections, allVars) {
  const color = resolveVariableColor(variable, collections, allVars, node);
  const base = { type: 'SOLID', color, visible: true };
  node.strokes = [figma.variables.setBoundVariableForPaint(base, 'color', variable)];
  node.strokeWeight = 1;
}

function setSelectionProps(item, selected) {
  const updates = {};
  for (const key of Object.keys(item.componentProperties || {})) {
    const base = key.split('#')[0].trim();
    if (/^Selected$/i.test(base)) updates[key] = selected ? 'True' : 'False';
    if (/Show leading element/i.test(base)) updates[key] = true;
    if (/Show trailing element/i.test(base)) updates[key] = false;
  }
  if (Object.keys(updates).length) item.setProperties(updates);
}

async function configureItem(item, selected, checkMc, V, collections, allVars) {
  if (typeof item.resetOverrides === 'function') item.resetOverrides();
  setSelectionProps(item, selected);

  const state = item.findOne((n) => /state layer/i.test(n.name));
  const label = item.findOne((n) => n.type === 'TEXT' && /label/i.test(n.name));
  const trailing = item.findOne((n) => /trailing element/i.test(n.name));
  const leading = item.findOne((n) => /leading element/i.test(n.name));
  if (trailing) trailing.visible = false;

  if (state) {
    state.cornerRadius = 12;
    if (selected) {
      bindFill(state, V.secondaryContainer, collections, allVars);
      bindStroke(state, V.outline, collections, allVars);
    } else {
      state.fills = [];
      state.strokes = [];
    }
  }

  if (label) {
    bindFill(label, selected ? V.onSecondaryContainer : V.onSurface, collections, allVars);
  }

  if (leading) {
    leading.visible = true;
    const icon = leading.findOne((n) => n.type === 'INSTANCE' && n.name === 'Icon');
    if (icon) {
      if (selected) {
        icon.swapComponent(checkMc);
        icon.visible = true;
        const vec = icon.findOne((n) => n.type === 'VECTOR');
        if (vec) bindFill(vec, V.primary, collections, allVars);
      } else {
        icon.visible = false;
      }
    }
  }
}

const libVars = await figma.teamLibrary.getVariablesInLibraryCollectionAsync(M3_COLLECTION_KEY);
const imported = {};
for (const [name, fallback] of Object.entries(VAR_KEYS)) {
  const lib = libVars.find((v) => v.name === name);
  imported[name] = await figma.variables.importVariableByKeyAsync(lib?.key || fallback);
}
const Vmap = {
  surfaceContainerLow: imported['Schemes/Surface Container Low'],
  secondaryContainer: imported['Schemes/Secondary Container'],
  onSecondaryContainer: imported['Schemes/On Secondary Container'],
  onSurface: imported['Schemes/On Surface'],
  primary: imported['Schemes/Primary'],
  outline: imported['Schemes/Outline'],
};

const collections = await figma.variables.getLocalVariableCollectionsAsync();
const allVars = await figma.variables.getLocalVariablesAsync('COLOR');
const checkMc = await figma.importComponentByKeyAsync(CHECK_KEY);

const menu = await figma.getNodeByIdAsync(FILTER_MENU_ID);
if (!menu) throw new Error('Filter menu not found');

bindFill(menu, Vmap.surfaceContainerLow, collections, allVars);
menu
  .findAll((n) => /Menu-item 0[456]/.test(n.name))
  .forEach((n) => {
    n.visible = false;
  });

const list1Labels = ['Todos los viajes', 'En curso', 'Próximos'];
const list2Labels = ['Fecha — próximo primero', 'Nombre A—Z', 'Destino A—Z'];
const lists = menu.children.filter((n) => /^List \d/.test(n.name));

for (let li = 0; li < lists.length; li++) {
  const labels = li === 0 ? list1Labels : list2Labels;
  const sectionLabel = lists[li].findOne((n) => /Label \d/i.test(n.name));
  if (sectionLabel) {
    sectionLabel.visible = true;
    const t = sectionLabel.findOne((n) => n.type === 'TEXT');
    if (t) t.characters = li === 0 ? 'Mostrar' : 'Ordenar';
  }
  const items = lists[li].children.filter(
    (n) => /Menu-item 0[123]/.test(n.name) && n.visible !== false,
  );
  for (let i = 0; i < items.length; i++) {
    const label = items[i].findOne((n) => n.type === 'TEXT' && /label/i.test(n.name));
    if (label) label.characters = labels[i];
    await configureItem(items[i], i === 0, checkMc, Vmap, collections, allVars);
  }
}

return { status: 'filter-menu fixed', menuId: menu.id };
