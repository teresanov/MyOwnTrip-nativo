/**
 * Exporta imágenes Media de las Horizontal cards Home (Lisboa, Tokio) desde design-file cap 3.
 * Guardar en app/src/main/res/drawable-nodpi/home_trip_lisboa.jpg · home_trip_tokio.jpg
 *
 * Nodos cap 3 (`228:8161`):
 *   Barcelona — exportar solo el image fill del Background (280dp), no el TripHeroCard entero
 *   Lisboa — `228:8174` (Horizontal card)
 *   Tokio  — `228:8175` (Horizontal card)
 *
 * Ejecutar vía figma_execute en Vf2tNMXyKAlJSV53A1v4Is con Desktop Bridge.
 */
if (figma.fileKey !== 'Vf2tNMXyKAlJSV53A1v4Is') {
  throw new Error('ABORT: ejecutar en MyOwnTrip_design-file');
}

const COVERS = [
  { key: 'barcelona', nodeId: '228:8171', file: 'home_trip_barcelona.jpg' },
  { key: 'lisboa', nodeId: '228:8174', file: 'home_trip_lisboa.jpg' },
  { key: 'tokio', nodeId: '228:8175', file: 'home_trip_tokio.jpg' },
];

function findMediaFill(node) {
  if ('fills' in node && Array.isArray(node.fills)) {
    const img = node.fills.find((f) => f.type === 'IMAGE' && f.visible !== false);
    if (img) return node;
  }
  if ('children' in node) {
    for (const child of node.children) {
      const found = findMediaFill(child);
      if (found) return found;
    }
  }
  return null;
}

const out = {};
for (const { key, nodeId, file } of COVERS) {
  const card = await figma.getNodeByIdAsync(nodeId);
  if (!card) throw new Error(`Card no encontrada: ${nodeId}`);
  const media = findMediaFill(card) || card;
  const bytes = await media.exportAsync({
    format: 'JPG',
    constraint: { type: 'WIDTH', value: 320 },
  });
  out[key] = {
    nodeId,
    file,
    byteLength: bytes.length,
    base64: figma.base64Encode(bytes),
  };
}

return {
  ok: true,
  covers: out,
  hint: 'Decodificar base64 → drawable-nodpi + ds-showcase/public/assets/',
};
