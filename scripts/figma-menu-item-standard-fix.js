/**
 * Menu-item/Standard — menú de selección M3 Expressive + variables enlazadas.
 * Ejecutar vía figma_execute en librería DS zrGAL4v6MEMc9hzZemU432 (Bridge abierto).
 */
if (figma.fileKey !== 'zrGAL4v6MEMc9hzZemU432') {
  throw new Error('ABORT: abrir MyOwnTrip_nativo — Design System');
}

const MENU_SECTION_ID = '55141:14250';
const CHECK_ICON_KEY = '2a44aa4da65888b360a84473faaf822deff6d5c3';
const SELECTED_SHAPE_RADIUS = 12;

function varByName(allVars, name) {
  const v = allVars.find((x) => x.name === name);
  if (!v) throw new Error('Missing variable: ' + name);
  return v;
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
  if (!node || !('fills' in node) || !variable) return;
  const color = resolveVariableColor(variable, collections, allVars, node);
  const base = { type: 'SOLID', color, visible: true };
  node.fills = [figma.variables.setBoundVariableForPaint(base, 'color', variable)];
}

function bindStroke(node, variable, collections, allVars, weight = 1) {
  if (!node || !variable || !('strokes' in node)) return;
  const color = resolveVariableColor(variable, collections, allVars, node);
  const base = { type: 'SOLID', color, visible: true };
  node.strokes = [figma.variables.setBoundVariableForPaint(base, 'color', variable)];
  node.strokeWeight = weight;
}

function clearPaint(node) {
  if (!node) return;
  if ('fills' in node) node.fills = [];
  if ('strokes' in node) node.strokes = [];
}

function findMenuItemSet(root) {
  return root.findOne(
    (n) =>
      n.type === 'COMPONENT_SET' &&
      /Menu[- ]?item/i.test(n.name) &&
      /Standard/i.test(n.name),
  );
}

function isSelectedVariant(component) {
  return /Selected=True/i.test(component.name);
}

async function fixVariant(component, V, collections, allVars, checkMc) {
  const selected = isSelectedVariant(component);
  const state = component.findOne((n) => /state layer/i.test(n.name));
  const label = component.findOne((n) => n.type === 'TEXT' && /label/i.test(n.name));
  const content = component.findOne((n) => n.name === 'Content' && n.type === 'FRAME');
  const leading = component.findOne((n) => /leading element/i.test(n.name));
  const trailing = component.findOne((n) => /trailing element/i.test(n.name));

  if (trailing) trailing.visible = false;
  component
    .findAll(
      (n) =>
        n.type === 'INSTANCE' &&
        (/chevron_right/i.test(n.name) ||
          /radio_button_checked/i.test(n.name) ||
          n.name === 'radio_button_checked'),
    )
    .forEach((n) => {
      n.visible = false;
    });

  if (content) {
    clearPaint(content);
    bindFill(content, V.surfaceContainerLow, collections, allVars);
  }

  if (state) {
    state.cornerRadius = SELECTED_SHAPE_RADIUS;
    if (selected) {
      bindFill(state, V.secondaryContainer, collections, allVars);
      bindStroke(state, V.outline, collections, allVars);
    } else {
      clearPaint(state);
    }
  }

  if (label) {
    bindFill(label, selected ? V.onSecondaryContainer : V.onSurface, collections, allVars);
  }

  if (leading) {
    leading.visible = true;
    const icon = leading.findOne((n) => n.type === 'INSTANCE' && n.name === 'Icon');
    if (icon) {
      if (selected && checkMc) {
        icon.swapComponent(checkMc);
        icon.visible = true;
        const vec = icon.findOne((n) => n.type === 'VECTOR');
        if (vec) bindFill(vec, V.primary, collections, allVars);
      } else {
        icon.visible = false;
      }
    }
  }

  return { name: component.name, selected };
}

const collections = await figma.variables.getLocalVariableCollectionsAsync();
const allVars = await figma.variables.getLocalVariablesAsync('COLOR');
const V = {
  surfaceContainerLow: varByName(allVars, 'Schemes/Surface Container Low'),
  secondaryContainer: varByName(allVars, 'Schemes/Secondary Container'),
  onSecondaryContainer: varByName(allVars, 'Schemes/On Secondary Container'),
  onSurface: varByName(allVars, 'Schemes/On Surface'),
  primary: varByName(allVars, 'Schemes/Primary'),
  outline: varByName(allVars, 'Schemes/Outline'),
};

const checkMc = await figma.importComponentByKeyAsync(CHECK_ICON_KEY);
const section = await figma.getNodeByIdAsync(MENU_SECTION_ID);
const set = findMenuItemSet(section || figma.currentPage);
if (!set) throw new Error('Menu-item/Standard not found');

const fixed = [];
for (const child of set.children) {
  if (child.type !== 'COMPONENT') continue;
  fixed.push(await fixVariant(child, V, collections, allVars, checkMc));
}

figma.viewport.scrollAndZoomIntoView([set]);

return {
  status: 'Menu-item/Standard fixed',
  setId: set.id,
  variants: fixed.length,
  fixed,
};
