/**
 * Brand — variables C1 + preview frames (parte 2 de bind).
 * Ejecutar vía figma_execute (Desktop Bridge) en zrGAL4v6MEMc9hzZemU432.
 */
const brandPage = figma.root.children.find((p) => p.id === '61084:30304');
await figma.setCurrentPageAsync(brandPage);

const collections = await figma.variables.getLocalVariableCollectionsAsync();
const vars = await figma.variables.getLocalVariablesAsync();
const m3Col = collections.find((c) => c.name === 'M3');
const darkModeId = m3Col.modes.find((m) => m.name === 'Dark').modeId;

function v(name) {
  return vars.find((x) => x.name === name);
}

const V = {
  ink: v('ink'),
  ocre: v('ocre'),
  paper: v('paper'),
  onDark: v('on-dark'),
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

function bindStroke(node, variable) {
  bindStrokeAt(node, variable, 0);
}

function bindStrokeAt(node, variable, index) {
  if (!node || !variable || !('strokes' in node)) return;
  const color = resolveVariableColor(variable, collections, vars, node);
  const base = { type: 'SOLID', color, visible: true };
  const strokes =
    node.strokes && node.strokes.length
      ? node.strokes.map((s) => ({ ...s }))
      : [base];
  const current = strokes[index];
  const paintBase =
    current && current.type === 'SOLID'
      ? { ...current, color, visible: true }
      : base;
  strokes[index] = figma.variables.setBoundVariableForPaint(paintBase, 'color', variable);
  node.strokes = strokes;
}

function clearStrokes(node) {
  if (node && 'strokes' in node) {
    node.strokes = [];
    node.strokeWeight = 0;
  }
}

// C1 Default
const c1 = await figma.getNodeByIdAsync('61084:30347');
const rect = c1.findOne((n) => n.name === 'Rectangle');
const m1 = c1.findOne((n) => n.type === 'TEXT' && n.name === 'M');
if (rect) {
  await bindFill(rect, V.paper);
  await bindStroke(rect, V.ink);
}
if (m1) await bindFill(m1, V.ink);
for (const vec of c1.findAll((n) => n.type === 'VECTOR')) {
  await bindFill(vec, V.ocre);
  clearStrokes(vec);
}

// C1 Variant2 (48px)
const c1s = await figma.getNodeByIdAsync('61091:30401');
await bindFill(c1s, V.paper);
await bindStroke(c1s, V.ink);
const m2 = c1s.findOne((n) => n.type === 'TEXT' && n.name === 'M');
if (m2) await bindFill(m2, V.ink);
for (const vec of c1s.findAll((n) => n.type === 'VECTOR')) {
  await bindFill(vec, V.ocre);
  clearStrokes(vec);
}

// Preview labels + frames in 61084:30305
const frame = await figma.getNodeByIdAsync('61084:30305');
const labelNames = ['Positive', 'Dark', 'Monochrome', 'Light'];
for (const name of labelNames) {
  const texts = frame.findAll((n) => n.type === 'TEXT' && n.name === name);
  for (const t of texts) await bindFill(t, V.onSurfaceVariant);
}

const note = frame.findOne((n) => n.id === '61084:30306');
if (note) await bindFill(note, V.onSurfaceVariant);

const c1label = frame.findOne((n) => n.id === '61084:30346');
if (c1label) await bindFill(c1label, V.onSurface);

// Preview card strokes (outline)
for (const id of ['61084:30308', '61084:30322', '61084:30330']) {
  const card = await figma.getNodeByIdAsync(id);
  if (card) await bindStroke(card, V.outlineVariant);
}

// Dark preview containers
for (const id of ['61084:30315', '61084:30337']) {
  const darkBox = await figma.getNodeByIdAsync(id);
  if (!darkBox) continue;
  darkBox.setExplicitVariableModeForCollection(m3Col, darkModeId);
  await bindFill(darkBox, V.surface);
  const darkLabel = darkBox.findOne((n) => n.type === 'TEXT' && n.name === 'Dark');
  if (darkLabel) await bindFill(darkLabel, V.onSurface);
}

return { part: 2, ok: true };
