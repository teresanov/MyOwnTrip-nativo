/**
 * Elimina la fila de chips decorativos del Home (viajes / Offline / Wallet).
 * Ejecutar vía figma_execute en Vf2tNMXyKAlJSV53A1v4Is con Bridge en design-file.
 */
if (figma.fileKey !== 'Vf2tNMXyKAlJSV53A1v4Is') {
  throw new Error('ABORT: ejecutar en MyOwnTrip_design-file');
}

const HOME_BODY_ID = '205:1021';

const body = await figma.getNodeByIdAsync(HOME_BODY_ID);
if (!body || !('children' in body)) {
  throw new Error('Home body not found: ' + HOME_BODY_ID);
}

const removed = [];

function isQuickChipNode(node) {
  if (node.type === 'INSTANCE') {
    const name = (node.name || '').toLowerCase();
    if (name.includes('chip')) return true;
    const main = node.mainComponent;
    if (main && /assist|filter|chip/i.test(main.name)) return true;
  }
  if (node.type === 'FRAME' && /quick.?chip|chips/i.test(node.name)) return true;
  return false;
}

function chipLabel(node) {
  if (node.type !== 'INSTANCE') return '';
  const text = node.findOne((n) => n.type === 'TEXT');
  return text && 'characters' in text ? text.characters : '';
}

const quickChipLabels = /viaje|offline|wallet/i;

// 1) Frame contenedor explícito
for (const child of [...body.children]) {
  if (child.type === 'FRAME' && /quick|chip/i.test(child.name)) {
    removed.push(child.name);
    child.remove();
  }
}

// 2) Instancias sueltas de chip con copy de metadata Home
for (const child of [...body.children]) {
  if (!isQuickChipNode(child)) continue;
  const label = chipLabel(child);
  if (quickChipLabels.test(label) || quickChipLabels.test(child.name)) {
    removed.push(label || child.name);
    child.remove();
  }
}

// 3) Fila horizontal que solo contiene chips
for (const child of [...body.children]) {
  if (child.type !== 'FRAME' || child.layoutMode !== 'HORIZONTAL') continue;
  const kids = child.children.filter((n) => n.visible !== false);
  if (kids.length === 0) continue;
  const allChips = kids.every((n) => isQuickChipNode(n));
  if (allChips) {
    removed.push('row:' + kids.map((n) => chipLabel(n) || n.name).join(', '));
    child.remove();
  }
}

figma.viewport.scrollAndZoomIntoView([body]);

return {
  status: removed.length ? 'removed' : 'nothing_found',
  removed,
  hint: removed.length
    ? 'Revisa spacing entre Action bar y TripHeroCard.'
    : 'Abre Shell — Home · flow y ajusta node id si el body cambió.',
};
