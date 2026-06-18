/**
 * Menu-item/Standard → patrón menú de selección (filtros Home).
 *
 * M3: selección única sin submenú = leading slot reservado (caja 24dp) + check al seleccionar;
 * trailing/chevron oculto siempre.
 *
 * Ejecutar vía figma_execute (Desktop Bridge):
 * 1. Librería DS (zrGAL4v6MEMc9hzZemU432) — corrige el component set publicado
 * 2. Design-file (Vf2tNMXyKAlJSV53A1v4Is) — reaplica instancias Filter menu / cap 3
 *
 * Compose: DropdownMenuItem(selected, selectedLeadingIcon = { Icon(Check) }, trailingIcon = null)
 */
const DS_FILE = 'zrGAL4v6MEMc9hzZemU432';
const DESIGN_FILE = 'Vf2tNMXyKAlJSV53A1v4Is';
const MENU_SECTION_ID = '55141:14250';
const FILTER_MENU_INSTANCE_ID = '228:8306';
const CHECK_ICON_KEY = '2a44aa4da65888b360a84473faaf822deff6d5c3';

/** M3 Expressive · selectableItemColors — ver variables.json / Theme.kt */
const EXPRESSIVE_SELECTION = {
  menuContainer: { r: 0.988, g: 0.949, b: 0.898 }, // surfaceContainerLow
  selectedContainer: { r: 0.894, g: 0.882, b: 0.863 }, // secondaryContainer
  selectedText: { r: 0.18, g: 0.212, b: 0.239 }, // onSecondaryContainer
  unselectedText: { r: 0.122, g: 0.106, b: 0.075 }, // onSurface
  checkIcon: { r: 0.29, g: 0.345, b: 0.392 }, // primary
  outline: { r: 0.49, g: 0.463, b: 0.404 }, // outline — borde 1.4.11 vs menú
  selectedShapeRadius: 12,
};

function solidPaint(color, opacity = 1) {
  return [{ type: 'SOLID', color, opacity, visible: true }];
}

function setIconVectorFill(iconInst, color) {
  const vec = iconInst?.findOne((n) => n.type === 'VECTOR');
  if (vec) vec.fills = solidPaint(color);
}

function applyExpressiveSelectionStyle(item, selected, checkComponent) {
  const state = item.findOne((n) => /state layer/i.test(n.name));
  const label = item.findOne((n) => n.type === 'TEXT' && /label/i.test(n.name));
  const leading = item.findOne((n) => /leading element/i.test(n.name));
  const trailing = item.findOne((n) => /trailing element/i.test(n.name));
  if (trailing) trailing.visible = false;

  if (state) {
    state.cornerRadius = EXPRESSIVE_SELECTION.selectedShapeRadius;
    if (selected) {
      state.fills = solidPaint(EXPRESSIVE_SELECTION.selectedContainer);
      state.strokes = solidPaint(EXPRESSIVE_SELECTION.outline);
      state.strokeWeight = 1;
      state.visible = true;
    } else {
      state.fills = [];
      state.strokes = [];
    }
  }

  if (label) {
    label.fills = solidPaint(
      selected ? EXPRESSIVE_SELECTION.selectedText : EXPRESSIVE_SELECTION.unselectedText,
    );
  }

  if (leading) {
    leading.visible = true;
    const icon = leading.findOne((n) => n.type === 'INSTANCE' && n.name === 'Icon');
    if (icon) {
      if (selected && checkComponent) {
        icon.swapComponent(checkComponent);
        icon.visible = true;
        setIconVectorFill(icon, EXPRESSIVE_SELECTION.checkIcon);
      } else {
        icon.visible = false;
      }
    }
  }
}

function applyExpressiveMenuContainer(menu) {
  if (menu && 'fills' in menu) {
    menu.fills = solidPaint(EXPRESSIVE_SELECTION.menuContainer);
  }
}

function configureSelectionMenuItem(item, text, selected, checkComponent) {
  const label =
    item.findOne((n) => n.type === 'TEXT' && /label/i.test(n.name)) ||
    item.findAll((n) => n.type === 'TEXT').pop();
  if (label && text != null) {
    label.characters = text;
  }

  const updates = {};
  for (const key of Object.keys(item.componentProperties || {})) {
    const base = key.split('#')[0].trim();
    if (/^Selected$/i.test(base)) {
      updates[key] = selected ? 'True' : 'False';
    }
    if (/Show leading element/i.test(base)) {
      updates[key] = true;
    }
    if (/Show trailing element/i.test(base)) {
      updates[key] = false;
    }
    if (/Show chevron/i.test(base)) {
      updates[key] = false;
    }
  }
  if (Object.keys(updates).length) {
    item.setProperties(updates);
  }

  applyExpressiveSelectionStyle(item, selected, checkComponent);
}

function findMenuItemSet(root) {
  return (
    root.findOne(
      (n) =>
        n.type === 'COMPONENT_SET' &&
        /Menu[- ]?item/i.test(n.name) &&
        /Standard/i.test(n.name),
    ) ||
    root.findOne(
      (n) => n.type === 'COMPONENT_SET' && /Menu[- ]?item\/Standard/i.test(n.name),
    )
  );
}

function findCheckIconComponent(root) {
  const byName = root.findOne(
    (n) =>
      n.type === 'COMPONENT' &&
      /^check$/i.test(n.name) &&
      /icon/i.test(n.parent?.name || ''),
  );
  if (byName) return byName;

  return root.findOne(
    (n) =>
      n.type === 'COMPONENT' &&
      n.name === 'check' &&
      n.parent?.type === 'FRAME' &&
      /sharp|icon/i.test(n.parent?.name || ''),
  );
}

function hideTrailingInNode(node) {
  const trailing =
    node.findOne((n) => /trailing element/i.test(n.name)) ||
    node.findOne((n) => /trailing/i.test(n.name) && n.type !== 'TEXT');
  if (trailing) trailing.visible = false;

  node
    .findAll(
      (n) =>
        n.type === 'INSTANCE' &&
        (/chevron_right/i.test(n.name) ||
          /radio_button_checked/i.test(n.name) ||
          n.mainComponent?.name === 'radio_button_checked'),
    )
    .forEach((n) => {
      n.visible = false;
    });
}

function ensureLeadingCheckSlot(node, checkComponent) {
  const leading =
    node.findOne((n) => /leading element/i.test(n.name)) ||
    node.findOne((n) => /leading/i.test(n.name) && n.type !== 'TEXT');
  if (!leading) return false;

  leading.visible = true;

  const isSelected = /Selected=True/i.test(node.name) || /selected=true/i.test(node.name);

  const iconInst = leading.findOne(
    (n) =>
      n.type === 'INSTANCE' &&
      (n.name === 'Icon' || /icon/i.test(n.name)),
  );

  if (isSelected && checkComponent && iconInst?.type === 'INSTANCE') {
    if (iconInst.mainComponent?.name !== checkComponent.name) {
      iconInst.swapComponent(checkComponent);
    }
    iconInst.visible = true;
  } else if (iconInst) {
    iconInst.visible = isSelected;
  }

  return true;
}

function fixMenuItemComponentSet(set, checkComponent) {
  const report = { variants: 0, leadingFixed: 0, trailingHidden: 0 };

  for (const variant of set.children) {
    if (variant.type !== 'COMPONENT') continue;
    report.variants += 1;
    hideTrailingInNode(variant);
    report.trailingHidden += 1;
    if (ensureLeadingCheckSlot(variant, checkComponent)) {
      report.leadingFixed += 1;
    }
  }

  return report;
}

function relabelFilterMenu(menu, checkComponent) {
  applyExpressiveMenuContainer(menu);
  const list1Labels = ['Todos los viajes', 'En curso', 'Próximos', 'Pasados'];
  const list2Labels = ['Fecha — próximo primero', 'Nombre A—Z', 'Destino A—Z'];
  const sectionTitles = ['Mostrar', 'Ordenar'];
  const lists = menu.children.filter((n) => /^List \d/.test(n.name));

  for (let li = 0; li < lists.length; li++) {
    const list = lists[li];
    const sectionLabel = list.findOne((n) => /Label \d/i.test(n.name));
    if (sectionLabel) {
      sectionLabel.visible = true;
      const t = sectionLabel.findOne((n) => n.type === 'TEXT');
      if (t) t.characters = sectionTitles[li] || '';
    }

    list
      .findAll((n) => /Menu-item 0[456]/.test(n.name))
      .forEach((n) => {
        n.visible = false;
      });

    const items = list.children.filter(
      (n) => /Menu-item 0[123]/.test(n.name) && n.visible !== false,
    );
    const labels = li === 0 ? list1Labels : list2Labels;
    for (let i = 0; i < labels.length; i++) {
      if (items[i]) configureSelectionMenuItem(items[i], labels[i], i === 0, checkComponent);
    }
  }
}

function fixMenuTree(root, checkComponent) {
  let items = 0;
  root
    .findAll((n) => n.type === 'INSTANCE' && /Menu-item/i.test(n.name))
    .forEach((item) => {
      const selected = Object.entries(item.componentProperties || {}).some(
        ([k, v]) => /^Selected$/i.test(k.split('#')[0]) && String(v?.value ?? v) === 'True',
      );
      configureSelectionMenuItem(item, null, selected, checkComponent);
      items += 1;
    });
  return items;
}

const fileKey = figma.fileKey;
if (fileKey !== DS_FILE && fileKey !== DESIGN_FILE) {
  throw new Error(`ABORT: abrir DS (${DS_FILE}) o design-file (${DESIGN_FILE})`);
}

const result = { fileKey, phase: [] };

if (fileKey === DS_FILE) {
  const section = await figma.getNodeByIdAsync(MENU_SECTION_ID);
  const scope = section || figma.currentPage;
  const set = findMenuItemSet(scope);
  if (!set) throw new Error('Menu item/Standard component set not found');

  const checkComponent = findCheckIconComponent(figma.root);
  result.phase.push({
    step: 'fix-component-set',
    setId: set.id,
    setName: set.name,
    checkIcon: checkComponent?.name || null,
    ...fixMenuItemComponentSet(set, checkComponent),
  });
}

if (fileKey === DESIGN_FILE) {
  const checkComponent = await figma.importComponentByKeyAsync(CHECK_ICON_KEY);
  const filterMenu =
    (await figma.getNodeByIdAsync(FILTER_MENU_INSTANCE_ID)) ||
    figma.currentPage.findOne((n) => n.name === 'Filter menu');
  if (filterMenu) {
    relabelFilterMenu(filterMenu, checkComponent);
    result.phase.push({
      step: 'relabel-filter-menu',
      nodeId: filterMenu.id,
      items: fixMenuTree(filterMenu, checkComponent),
    });
  }

  const cap3 = await figma.getNodeByIdAsync('228:8161');
  if (cap3) {
    const menu = cap3.findOne((n) => n.name === 'Filter menu');
    if (menu && menu.id !== filterMenu?.id) {
      relabelFilterMenu(menu, checkComponent);
      result.phase.push({ step: 'relabel-cap3-menu', nodeId: menu.id, items: fixMenuTree(menu, checkComponent) });
    }
  }
}

result.status = 'menu-item-selection-filter applied';
result.compose =
  'DropdownMenuItem(selected, selectedLeadingIcon = { Icon(Check) }; trailingIcon = null)';
return result;
