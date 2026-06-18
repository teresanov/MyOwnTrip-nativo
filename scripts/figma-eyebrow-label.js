/**
 * Eyebrow label — component set en página Labels (DS).
 * Ejecutar vía figma_execute (Desktop Bridge) en zrGAL4v6MEMc9hzZemU432.
 *
 * Etiqueta informativa no interactiva (fase, contexto). Sustituto visual del chip
 * cuando el fondo variable impide Assist Outlined. Chips = filtros/acciones.
 */
if (figma.fileKey !== 'zrGAL4v6MEMc9hzZemU432') {
  throw new Error('ABORT: ejecutar en MyOwnTrip_nativo — Design System');
}

const LABELS_PAGE_ID = '61202:16812';

await figma.loadFontAsync({ family: 'Inter', style: 'Medium' });

const allVars = await figma.variables.getLocalVariablesAsync();
const collections = await figma.variables.getLocalVariableCollectionsAsync();

function varByName(name) {
  return allVars.find((v) => v.name === name) || null;
}

function getEffectiveModeId(node, collection) {
  let current = node;
  while (current) {
    const mode = current.explicitVariableModes?.[collection.id];
    if (mode) return mode;
    current = current.parent;
  }
  return collection.modes[0].modeId;
}

function resolveVariableColor(variable, node) {
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

function bindFill(node, variable) {
  if (!node || !variable || !('fills' in node)) return;
  const color = resolveVariableColor(variable, node);
  const base = { type: 'SOLID', color, visible: true };
  node.fills = [figma.variables.setBoundVariableForPaint(base, 'color', variable)];
}

function bindTextFill(node, variable) {
  if (!node || !variable || node.type !== 'TEXT') return;
  const color = resolveVariableColor(variable, node);
  const base = { type: 'SOLID', color, visible: true };
  node.fills = [figma.variables.setBoundVariableForPaint(base, 'color', variable)];
}

function bindCorner(node, variable) {
  if (!node || !variable) return;
  node.setBoundVariable('topLeftRadius', variable);
  node.setBoundVariable('topRightRadius', variable);
  node.setBoundVariable('bottomLeftRadius', variable);
  node.setBoundVariable('bottomRightRadius', variable);
}

const V = {
  tertiaryFixedDim: varByName('Schemes/Tertiary Fixed Dim'),
  onTertiaryContainer: varByName('Schemes/On Tertiary Container'),
  secondaryContainer: varByName('Schemes/Secondary Container'),
  onSecondaryContainer: varByName('Schemes/On Secondary Container'),
  surfaceContainerHigh: varByName('Schemes/Surface Container High'),
  onSurfaceVariant: varByName('Schemes/On Surface Variant'),
  cornerSmall: varByName('Corner/Small'),
};

const COLOR_SPECS = [
  {
    name: 'Tertiary',
    container: V.tertiaryFixedDim,
    label: V.onTertiaryContainer,
    usage: 'Sobre fotos y scrims (TripHeroCard, portadas).',
  },
  {
    name: 'Surface',
    container: V.surfaceContainerHigh,
    label: V.onSurfaceVariant,
    usage: 'Sobre surface plana (cards, listas).',
  },
  {
    name: 'Secondary',
    container: V.secondaryContainer,
    label: V.onSecondaryContainer,
    usage: 'Acento suave en superficies claras.',
  },
];

const SIZE_SPECS = [
  { name: 'Small', fontSize: 12, padH: 8, padV: 4 },
  { name: 'Medium', fontSize: 14, padH: 16, padV: 6 },
];

const page = await figma.getNodeByIdAsync(LABELS_PAGE_ID);
if (!page || page.type !== 'PAGE') {
  throw new Error('Labels page not found: ' + LABELS_PAGE_ID);
}

let section = page.findOne(
  (n) => n.type === 'SECTION' && n.name === 'Eyebrow label',
);
if (!section) {
  section = figma.createSection();
  section.name = 'Eyebrow label';
  page.appendChild(section);
  section.x = 0;
  section.y = 0;
  section.resizeWithoutConstraints(2400, 900);
}

const existing = section.findOne(
  (n) => n.type === 'COMPONENT_SET' && n.name === 'Eyebrow label',
);
if (existing) existing.remove();

function buildVariant(colorSpec, sizeSpec) {
  const root = figma.createFrame();
  root.name = `Color=${colorSpec.name}, Size=${sizeSpec.name}`;
  root.layoutMode = 'HORIZONTAL';
  root.primaryAxisSizingMode = 'AUTO';
  root.counterAxisSizingMode = 'AUTO';
  root.layoutSizingHorizontal = 'HUG';
  root.layoutSizingVertical = 'HUG';
  root.primaryAxisAlignItems = 'CENTER';
  root.counterAxisAlignItems = 'CENTER';
  root.paddingLeft = sizeSpec.padH;
  root.paddingRight = sizeSpec.padH;
  root.paddingTop = sizeSpec.padV;
  root.paddingBottom = sizeSpec.padV;
  root.itemSpacing = 0;
  root.cornerRadius = 8;
  root.clipsContent = true;
  root.fills = [{ type: 'SOLID', color: { r: 0.97, g: 0.73, b: 0.44 } }];

  const label = figma.createText();
  label.name = 'Label text';
  label.fontName = { family: 'Inter', style: 'Medium' };
  label.characters = 'Próximo viaje';
  label.fontSize = sizeSpec.fontSize;
  label.textAlignHorizontal = 'CENTER';
  label.textAlignVertical = 'CENTER';
  label.fills = [{ type: 'SOLID', color: { r: 0.4, g: 0.24, b: 0 } }];
  root.appendChild(label);

  bindFill(root, colorSpec.container);
  bindTextFill(label, colorSpec.label);
  if (V.cornerSmall) bindCorner(root, V.cornerSmall);

  return figma.createComponentFromNode(root);
}

const variants = [];
for (const color of COLOR_SPECS) {
  for (const size of SIZE_SPECS) {
    variants.push(buildVariant(color, size));
  }
}

const set = figma.combineAsVariants(variants, section);
set.name = 'Eyebrow label';
set.description =
  'Etiqueta informativa no interactiva (fase, contexto editorial). Usar en lugar de Assist chip cuando el fondo variable impide Outlined. Chips = filtros y acciones. Ver showcase Eyebrow label.';

set.addComponentProperty('Label text', 'TEXT', 'Próximo viaje');

const defs = set.componentPropertyDefinitions;
const labelKey =
  Object.keys(defs).find((k) => k.startsWith('Label text')) || null;

if (labelKey) {
  for (const child of set.children) {
    child.primaryAxisSizingMode = 'AUTO';
    child.counterAxisSizingMode = 'AUTO';
    child.layoutSizingHorizontal = 'HUG';
    child.layoutSizingVertical = 'HUG';
    const textNode = child.findOne((n) => n.name === 'Label text' && n.type === 'TEXT');
    if (textNode) {
      textNode.componentPropertyReferences = { characters: labelKey };
      textNode.textAutoResize = 'WIDTH_AND_HEIGHT';
      textNode.layoutSizingHorizontal = 'HUG';
      textNode.layoutSizingVertical = 'HUG';
    }
  }
}

// Grid layout: rows = Color, cols = Size
const COL_GAP = 32;
const ROW_GAP = 48;
const COL_W = 200;
let row = 0;
for (const color of COLOR_SPECS) {
  let col = 0;
  for (const size of SIZE_SPECS) {
    const variant = set.children.find(
      (c) => c.name === `Color=${color.name}, Size=${size.name}`,
    );
    if (variant) {
      variant.x = 80 + col * (COL_W + COL_GAP);
      variant.y = 80 + row * (ROW_GAP + 48);
    }
    col++;
  }
  row++;
}

set.x = 80;
set.y = 280;

figma.viewport.scrollAndZoomIntoView([set]);

return {
  setId: set.id,
  setKey: set.key,
  figmaUrl: `https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=${set.id.replace(':', '-')}`,
  variants: set.children.map((c) => ({ id: c.id, name: c.name, key: c.key })),
};
