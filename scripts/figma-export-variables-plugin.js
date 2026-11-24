/**
 * Exporta variables locales de Figma al formato del repo (variables.json v1.0.4).
 * Ejecutar con:
 *   set FIGMA_FILE_KEY=zrGAL4v6MEMc9hzZemU432
 *   node scripts/run-figma-bridge-execute.js scripts/figma-export-variables-plugin.js
 *
 * Requiere: Figma Desktop + plugin Figma Console / Bridge en el archivo indicado.
 */
function rgbaToHex(c) {
  const r = Math.round(c.r * 255);
  const g = Math.round(c.g * 255);
  const b = Math.round(c.b * 255);
  const a = Math.round((c.a !== undefined ? c.a : 1) * 255);
  const h = (n) => n.toString(16).padStart(2, '0');
  if (a < 255) return '#' + h(r) + h(g) + h(b) + h(a);
  return '#' + h(r) + h(g) + h(b);
}

function mapType(resolvedType) {
  if (resolvedType === 'COLOR') return 'color';
  if (resolvedType === 'FLOAT') return 'number';
  if (resolvedType === 'STRING') return 'string';
  if (resolvedType === 'BOOLEAN') return 'boolean';
  return String(resolvedType).toLowerCase();
}

return (async () => {
  const collections = await figma.variables.getLocalVariableCollectionsAsync();
  const allVars = await figma.variables.getLocalVariablesAsync();
  const varById = Object.fromEntries(allVars.map((v) => [v.id, v]));
  const collById = Object.fromEntries(collections.map((c) => [c.id, c]));

  const out = [];
  for (const coll of collections) {
    const modes = coll.modes.map((mode) => ({
      name: mode.name,
      variables: allVars
        .filter((v) => v.variableCollectionId === coll.id)
        .map((v) => {
          const raw = v.valuesByMode[mode.modeId];
          if (raw && typeof raw === 'object' && raw.type === 'VARIABLE_ALIAS') {
            const target = varById[raw.id];
            if (!target) {
              throw new Error('Alias sin destino: ' + v.name);
            }
            const targetColl = collById[target.variableCollectionId];
            return {
              name: v.name,
              type: mapType(v.resolvedType),
              isAlias: true,
              value: { collection: targetColl.name, name: target.name },
            };
          }
          let value = raw;
          if (v.resolvedType === 'COLOR') value = rgbaToHex(raw);
          return {
            name: v.name,
            type: mapType(v.resolvedType),
            isAlias: false,
            value,
          };
        })
        .sort((a, b) => a.name.localeCompare(b.name)),
    }));
    out.push({ name: coll.name, modes });
  }

  return {
    version: '1.0.4',
    metadata: {},
    collections: out,
    counts: { variables: allVars.length, collections: collections.length },
  };
})();