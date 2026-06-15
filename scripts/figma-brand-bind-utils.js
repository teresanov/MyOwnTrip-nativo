/**
 * Helpers para bindear variables de color en Figma vía Bridge.
 *
 * Quirk Bridge/API: setBoundVariableForPaint con color base {0,0,0} engancha la
 * variable pero el canvas muestra negro/blanco hasta unlink+⌘Z. Fix: resolver el
 * valor de la variable y usarlo como color del paint antes de bindear.
 *
 * MOT M/T muted: fill enlazado a variable al 100% + Appearance (node.opacity).
 * No usar opacity en el paint — rompe el link en la UI manual de Figma.
 */
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

function bindFill(node, variable, collections, allVars) {
  if (!node || !('fills' in node) || !variable) return;
  const color = resolveVariableColor(variable, collections, allVars, node);
  const base = { type: 'SOLID', color, visible: true };
  node.fills = [figma.variables.setBoundVariableForPaint(base, 'color', variable)];
}

/** Equivalente a Appearance en Figma (preserva enlace a variable en el fill). */
function bindFillWithAppearance(node, variable, layerOpacity, collections, allVars) {
  bindFill(node, variable, collections, allVars);
  if (!node) return;
  node.opacity =
    layerOpacity !== undefined && layerOpacity < 1 ? layerOpacity : 1;
}

const MOT_MUTED_APPEARANCE = 0.85;

function bindStroke(node, variable, collections, allVars, index = 0) {
  if (!node || !variable || !('strokes' in node)) return;
  const color = resolveVariableColor(variable, collections, allVars, node);
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

function paintVar(variable, collections, allVars, node) {
  const color = resolveVariableColor(variable, collections, allVars, node);
  const base = { type: 'SOLID', color, visible: true };
  return figma.variables.setBoundVariableForPaint(base, 'color', variable);
}
