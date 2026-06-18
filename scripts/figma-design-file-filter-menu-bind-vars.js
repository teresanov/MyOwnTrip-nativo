/**
 * Filter menu (228:8306) — enlazar overrides de color a variables M3 importadas.
 * Ejecutar vía figma_execute en design-file Vf2tNMXyKAlJSV53A1v4Is.
 *
 * Nota: la propagación durable es corregir Menu-item/Standard en librería DS
 * (variantes Selected True/False). Este script solo arregla la instancia cap 3.
 */
if (figma.fileKey !== 'Vf2tNMXyKAlJSV53A1v4Is') {
  throw new Error('ABORT: ejecutar en MyOwnTrip_design-file');
}

const FILTER_MENU_ID = '228:8306';

/** Modo Light de M3 Schemes (design-file sin colección local importada). */
const M3_LIGHT_MODE = '54778:1';

/** IDs de variables ya referenciadas en el design-file (evita importVariableByKeyAsync lento). */
const VAR_IDS = {
  'Schemes/Surface Container Low':
    'VariableID:9fc6dbf707bd78f9d1abc5e587d1392debb2fe76/61097:1446',
  'Schemes/Secondary Container':
    'VariableID:366811e6d0ed3a328f2a4273ec620a9c9e2fd84e/61098:1090',
  'Schemes/On Secondary Container':
    'VariableID:435fdc5dca8fd67daef0ec48559c158a86740052/61097:951',
  'Schemes/On Surface': 'VariableID:0f7c3b657886bd85a309a9b0bfb12cf204f341d3/61097:1096',
  'Schemes/Primary': 'VariableID:2522767ed48779c106a9a29f29cc14952c82fc7f/61098:1083',
  'Schemes/Outline': 'VariableID:b075dc530cec1d03f152ca87f265e06cb7cee350/61097:1151',
};

async function resolveRgb(variable, modeId = M3_LIGHT_MODE) {
  let value = variable.valuesByMode[modeId];
  let depth = 0;
  while (value?.type === 'VARIABLE_ALIAS' && depth < 8) {
    const target = await figma.variables.getVariableByIdAsync(value.id);
    if (!target) break;
    value = target.valuesByMode[modeId];
    depth++;
  }
  if (!value || typeof value.r !== 'number') {
    throw new Error('Cannot resolve color for variable: ' + variable.name);
  }
  return { r: value.r, g: value.g, b: value.b };
}

async function bindFill(node, variable) {
  const color = await resolveRgb(variable);
  const base = { type: 'SOLID', color, visible: true };
  node.fills = [figma.variables.setBoundVariableForPaint(base, 'color', variable)];
}

async function bindStroke(node, variable) {
  const color = await resolveRgb(variable);
  const base = { type: 'SOLID', color, visible: true };
  node.strokes = [figma.variables.setBoundVariableForPaint(base, 'color', variable)];
  node.strokeWeight = 1;
}

const imported = {};
for (const [name, id] of Object.entries(VAR_IDS)) {
  imported[name] = await figma.variables.getVariableByIdAsync(id);
}

const menu = await figma.getNodeByIdAsync(FILTER_MENU_ID);
if (!menu) throw new Error('Filter menu not found');

let bound = 0;
await bindFill(menu, imported['Schemes/Surface Container Low']);
bound++;

const items = menu.findAll(
  (n) => n.type === 'INSTANCE' && /Menu-item 0[123]/.test(n.name) && n.visible !== false,
);

for (const item of items) {
  const selected = item.componentProperties?.Selected?.value === 'True';
  const state = item.findOne((n) => /state layer/i.test(n.name));
  const label = item.findOne((n) => n.type === 'TEXT' && /label/i.test(n.name));
  const iconVec = item
    .findOne((n) => /leading element/i.test(n.name))
    ?.findOne((n) => n.type === 'VECTOR');

  if (state) {
    if (selected) {
      await bindFill(state, imported['Schemes/Secondary Container']);
      await bindStroke(state, imported['Schemes/Outline']);
      bound += 2;
    } else {
      state.fills = [];
      state.strokes = [];
    }
  }
  if (label) {
    await bindFill(
      label,
      selected ? imported['Schemes/On Secondary Container'] : imported['Schemes/On Surface'],
    );
    bound++;
  }
  if (selected && iconVec) {
    await bindFill(iconVec, imported['Schemes/Primary']);
    bound++;
  }
}

return { status: 'filter-menu vars bound', bound, items: items.length };
