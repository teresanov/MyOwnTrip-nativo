/**
 * Exporta la imagen Media del empty state Home (cap 1) desde el design-file.
 * Guardar salida en: app/src/main/res/drawable-nodpi/home_empty_map.jpg
 *
 * Ejecutar vía figma_execute en Vf2tNMXyKAlJSV53A1v4Is con Desktop Bridge.
 */
if (figma.fileKey !== 'Vf2tNMXyKAlJSV53A1v4Is') {
  throw new Error('ABORT: ejecutar en MyOwnTrip_design-file');
}

const MEDIA_NODE_ID = 'I215:2935;58710:12855';

const node = await figma.getNodeByIdAsync(MEDIA_NODE_ID);
if (!node) {
  throw new Error('Media node no encontrado — buscar instancia Stacked card en cap 1');
}

const bytes = await node.exportAsync({
  format: 'JPG',
  constraint: { type: 'WIDTH', value: 720 },
});

return {
  ok: true,
  nodeId: MEDIA_NODE_ID,
  byteLength: bytes.length,
  base64: figma.base64Encode(bytes),
  hint: 'Decodificar base64 → home_empty_map.jpg y copiar a drawable-nodpi + ds-showcase/public/assets/',
};
