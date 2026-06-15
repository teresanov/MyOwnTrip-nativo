/**
 * Brand — variables de color en frame Logo (61084:30305).
 * Ejecutar vía figma_execute (Desktop Bridge) en zrGAL4v6MEMc9hzZemU432.
 * Workflow: Bridge fija Figma → figma_export_tokens → M3_MOTrip.json / variables.json → Color.kt
 */
const brandPage = figma.root.children.find((p) => p.id === '61084:30304');
await figma.setCurrentPageAsync(brandPage);

const collections = await figma.variables.getLocalVariableCollectionsAsync();
const vars = await figma.variables.getLocalVariablesAsync();
const brandCol = collections.find((c) => c.name === 'Brand');
const m3Col = collections.find((c) => c.name === 'M3');

function v(name) {
  const found = vars.find((x) => x.name === name);
  if (!found) throw new Error('Missing variable: ' + name);
  return found;
}

let onDark = vars.find((x) => x.name === 'on-dark');
let createdOnDark = false;
if (!onDark) {
  onDark = figma.variables.createVariable('on-dark', brandCol, 'COLOR');
  onDark.scopes = ['TEXT_FILL', 'SHAPE_FILL', 'STROKE_COLOR'];
  onDark.setValueForMode(brandCol.modes[0].modeId, {
    r: 249 / 255,
    g: 239 / 255,
    b: 226 / 255,
  });
  createdOnDark = true;
}

const V = {
  ink: v('ink'),
  inkDeep: v('ink-deep'),
  ocre: v('ocre'),
  paper: v('paper'),
  onDark: onDark,
  onSurfaceVariant: v('Schemes/On Surface Variant'),
  onSurface: v('Schemes/On Surface'),
  surface: v('Schemes/Surface'),
  outlineVariant: v('Schemes/Outline Variant'),
};

// Helpers: scripts/figma-brand-bind-utils.js (mantener en sync)
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
  if (!collection) throw new Error('Missing collection for ' + variable.name);
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
  if (!value || typeof value.r !== 'number') {
    throw new Error('Cannot resolve color for variable: ' + variable.name);
  }
  return { r: value.r, g: value.g, b: value.b };
}

function bindFill(node, variable, opacity) {
  if (!node || !('fills' in node) || !variable) return;
  const color = resolveVariableColor(variable, collections, vars, node);
  const base = { type: 'SOLID', color, visible: true };
  if (opacity !== undefined && opacity < 1) base.opacity = opacity;
  node.fills = [figma.variables.setBoundVariableForPaint(base, 'color', variable)];
}

function bindStroke(node, variable, index) {
  if (!node || !variable || !('strokes' in node)) return;
  const i = index || 0;
  const color = resolveVariableColor(variable, collections, vars, node);
  const base = { type: 'SOLID', color, visible: true };
  const strokes =
    node.strokes && node.strokes.length
      ? node.strokes.map((s) => ({ ...s }))
      : [base];
  const current = strokes[i];
  const paintBase =
    current && current.type === 'SOLID'
      ? { ...current, color, visible: true }
      : base;
  strokes[i] = figma.variables.setBoundVariableForPaint(paintBase, 'color', variable);
  node.strokes = strokes;
}

function clearStrokes(node) {
  if ('strokes' in node) {
    node.strokes = [];
    node.strokeWeight = 0;
  }
}

async function bindTextIn(parent, variable, opacity) {
  if (!parent || !('findAll' in parent)) return;
  for (const t of parent.findAll((n) => n.type === 'TEXT')) {
    await bindFill(t, variable, opacity);
  }
}

async function bindTextNamed(parent, names, variable, opacity) {
  if (!parent) return;
  for (const name of names) {
    const t = parent.findOne((n) => n.type === 'TEXT' && n.name === name);
    if (t) await bindFill(t, variable, opacity);
  }
}

async function bindRibbons(parent) {
  if (!parent) return;
  for (const vec of parent.findAll((n) => n.type === 'VECTOR' && n.name === 'Vector')) {
    await bindFill(vec, V.ocre);
    clearStrokes(vec);
  }
}

// Wordmark W4 / Positive
const wPos = await figma.getNodeByIdAsync('61084:30351');
await bindTextIn(wPos, V.ink);
await bindRibbons(wPos);

// Wordmark W4 / Dark
const wDark = await figma.getNodeByIdAsync('61084:30352');
await bindTextIn(wDark, V.onDark);
await bindRibbons(wDark);

// Wordmark W4 / Monochrome
const wMono = await figma.getNodeByIdAsync('61084:30353');
await bindTextIn(wMono, V.ink);
const monoRibbon = wMono.findOne((n) => n.type === 'VECTOR' && n.name === 'Vector');
if (monoRibbon) {
  await bindFill(monoRibbon, V.ink);
  clearStrokes(monoRibbon);
}

// Monogram MOT / Light
const motLight = await figma.getNodeByIdAsync('61084:30354');
await bindTextNamed(motLight, ['M', 'T'], V.ink, 0.62);
await bindTextNamed(motLight, ['O'], V.inkDeep);
await bindRibbons(motLight);

// Monogram MOT / Dark
const motDark = await figma.getNodeByIdAsync('61084:30355');
await bindTextNamed(motDark, ['M', 'T'], V.onDark, 0.62);
await bindTextNamed(motDark, ['O'], V.onDark);
await bindRibbons(motDark);

return { createdOnDark, onDarkId: onDark.id, part: 1 };
