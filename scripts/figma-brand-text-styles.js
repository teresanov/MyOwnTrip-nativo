/**
 * Brand — estilos de texto Brand/* + aplicación en frame Logo.
 * Ejecutar vía figma_execute (Desktop Bridge) en zrGAL4v6MEMc9hzZemU432.
 * Color en nodos (variables); tipografía en text styles (sin color — limitación API).
 */
const brandPage = figma.root.children.find((p) => p.id === '61084:30304');
await figma.setCurrentPageAsync(brandPage);

const collections = await figma.variables.getLocalVariableCollectionsAsync();
const m3Col = collections.find((c) => c.name === 'M3');
const darkModeId = m3Col.modes.find((m) => m.name === 'Dark').modeId;
const allVars = await figma.variables.getLocalVariablesAsync();

function v(name) {
  const found = allVars.find((x) => x.name === name);
  if (!found) throw new Error('Missing variable: ' + name);
  return found;
}

const V = {
  ink: v('ink'),
  ocre: v('ocre'),
  onDark: v('on-dark'),
  onSurfaceVariant: v('Schemes/On Surface Variant'),
  onSurface: v('Schemes/On Surface'),
};

const fonts = [
  ['Fraunces', 'SemiBold'],
  ['Fraunces', 'Italic'],
  ['Fraunces', 'Light'],
  ['Fraunces', 'Bold'],
  ['Inter', 'Regular'],
  ['Inter', 'Medium'],
];
for (const f of fonts) await figma.loadFontAsync({ family: f[0], style: f[1] });

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

function paintVar(variable, node) {
  const color = resolveVariableColor(variable, collections, allVars, node);
  const base = { type: 'SOLID', color, visible: true };
  return figma.variables.setBoundVariableForPaint(base, 'color', variable);
}

async function ensureTypoStyle(name, fontName, fontSize, letterSpacing) {
  const all = await figma.getLocalTextStylesAsync();
  let style = all.find((s) => s.name === name);
  if (style) style.remove();
  style = figma.createTextStyle();
  style.name = name;
  style.fontName = fontName;
  style.fontSize = fontSize;
  style.letterSpacing = letterSpacing;
  return style;
}

const LS_W4 = { value: -3, unit: 'PERCENT' };
const LS_MOT = { value: -2, unit: 'PERCENT' };
const LS_NONE = { value: 0, unit: 'PERCENT' };

const styles = {
  w4Trip: await ensureTypoStyle('Brand/W4/My-Trip', { family: 'Fraunces', style: 'SemiBold' }, 32, LS_W4),
  w4Own: await ensureTypoStyle('Brand/W4/Own', { family: 'Fraunces', style: 'Italic' }, 32, LS_W4),
  motMuted: await ensureTypoStyle('Brand/MOT/Muted', { family: 'Fraunces', style: 'Light' }, 21, LS_MOT),
  motEmphasis: await ensureTypoStyle('Brand/MOT/Emphasis', { family: 'Fraunces', style: 'Bold' }, 21, LS_MOT),
  c1M96: await ensureTypoStyle('Brand/C1/M/96', { family: 'Fraunces', style: 'SemiBold' }, 64, LS_W4),
  c1M48: await ensureTypoStyle('Brand/C1/M/48', { family: 'Fraunces', style: 'SemiBold' }, 36, LS_W4),
  metaLabel: await ensureTypoStyle('Brand/Meta/Label', { family: 'Inter', style: 'Regular' }, 11, LS_NONE),
  metaSection: await ensureTypoStyle('Brand/Meta/Section-Title', { family: 'Inter', style: 'Medium' }, 11, LS_NONE),
  metaNote: await ensureTypoStyle('Brand/Meta/Note', { family: 'Inter', style: 'Regular' }, 13, LS_NONE),
};

async function applyStyle(node, style) {
  if (!node || node.type !== 'TEXT' || !style) return;
  await figma.loadFontAsync(style.fontName);
  await node.setTextStyleIdAsync(style.id);
}

async function bindFill(node, variable) {
  if (!node || !variable) return;
  node.fills = [paintVar(variable, node)];
}

const MOT_MUTED_APPEARANCE = 0.85;

async function bindFillWithAppearance(node, variable, layerOpacity) {
  await bindFill(node, variable);
  if (node) node.opacity = layerOpacity !== undefined && layerOpacity < 1 ? layerOpacity : 1;
}

async function applyWordmark(componentId, tripVar, ownVar) {
  const comp = await figma.getNodeByIdAsync(componentId);
  if (!comp) return;
  for (const t of comp.findAll((n) => n.type === 'TEXT')) {
    if (t.name === 'Own') await applyStyle(t, styles.w4Own);
    else await applyStyle(t, styles.w4Trip);
    await bindFill(t, t.name === 'Own' ? ownVar : tripVar);
  }
}

await applyWordmark('61084:30351', V.ink, V.ocre);
await applyWordmark('61084:30352', V.onDark, V.ocre);
await applyWordmark('61084:30353', V.ink, V.ink);

async function applyMot(componentId, mutedVar, emphasisVar, mutedAppearance) {
  const comp = await figma.getNodeByIdAsync(componentId);
  if (!comp) return;
  for (const t of comp.findAll((n) => n.type === 'TEXT')) {
    if (t.name === 'M' || t.name === 'T') {
      await applyStyle(t, styles.motMuted);
      await bindFillWithAppearance(t, mutedVar, mutedAppearance);
    } else if (t.name === 'O') {
      await applyStyle(t, styles.motEmphasis);
      await bindFill(t, emphasisVar);
      t.opacity = 1;
    }
  }
}

await applyMot('61084:30354', V.ink, V.ocre, MOT_MUTED_APPEARANCE);
await applyMot('61084:30355', V.onDark, V.onDark, MOT_MUTED_APPEARANCE);

const c1m = await figma.getNodeByIdAsync('61084:30349');
if (c1m) {
  await applyStyle(c1m, styles.c1M96);
  await bindFill(c1m, V.ink);
}
const c1ms = await figma.getNodeByIdAsync('61091:30403');
if (c1ms) {
  await applyStyle(c1ms, styles.c1M48);
  await bindFill(c1ms, V.ink);
}

const frame = await figma.getNodeByIdAsync('61084:30305');
for (const name of ['Positive', 'Monochrome', 'Light']) {
  for (const t of frame.findAll((n) => n.type === 'TEXT' && n.name === name)) {
    await applyStyle(t, styles.metaLabel);
    await bindFill(t, V.onSurfaceVariant);
  }
}
for (const id of ['61084:30315', '61084:30337']) {
  const darkBox = await figma.getNodeByIdAsync(id);
  if (!darkBox) continue;
  const darkLabel = darkBox.findOne((n) => n.type === 'TEXT' && n.name === 'Dark');
  if (darkLabel) {
    darkBox.setExplicitVariableModeForCollection(m3Col, darkModeId);
    await applyStyle(darkLabel, styles.metaLabel);
    await bindFill(darkLabel, V.onSurface);
  }
}
const note = frame.findOne((n) => n.id === '61084:30306');
if (note) {
  await applyStyle(note, styles.metaNote);
  await bindFill(note, V.onSurfaceVariant);
}
const c1label = frame.findOne((n) => n.id === '61084:30346');
if (c1label) {
  await applyStyle(c1label, styles.metaSection);
  await bindFill(c1label, V.onSurface);
}

const texts = [];
function walk(node) {
  if (node.type === 'TEXT') texts.push({ name: node.name, textStyleId: node.textStyleId });
  if ('children' in node) for (const c of node.children) walk(c);
}
walk(frame);
const allStyles = await figma.getLocalTextStylesAsync();
const linked = texts
  .filter((t) => t.textStyleId)
  .map((t) => ({ name: t.name, style: (allStyles.find((s) => s.id === t.textStyleId) || {}).name }));

return {
  brandStyles: Object.keys(styles).length,
  linkedCount: linked.length,
  totalTexts: texts.length,
  unlinked: texts.filter((t) => !t.textStyleId).map((t) => t.name),
  linked,
};
