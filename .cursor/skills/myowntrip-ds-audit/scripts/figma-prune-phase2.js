/**
 * Fase 2 — recortar variantes en sets KEEP (MVP+1).
 * Ejecutar tras fase 1 estable. Lotes pequeños por set.
 */

const KEEP_VARIANT_PATTERNS = {
  Button: [/filled/i, /tonal/i, /outlined/i, /text/i, /disabled/i],
  "Icon button": [/standard/i, /disabled/i],
  FAB: [/surface/i, /primary/i, /small/i, /medium/i, /large/i],
  "Text field": [/outlined/i, /filled/i, /disabled/i, /error/i],
  Chip: [/filter/i, /assist/i, /suggestion/i, /input/i],
  "Top app bar": [/small/i, /center/i, /flat/i],
  "Navigation bar": [/.*/],
  "Bottom sheet": [/modal/i, /standard/i],
};

function variantAllowed(setName, variantName) {
  const patterns = KEEP_VARIANT_PATTERNS[setName];
  if (!patterns) return true; // set no mapeado: no podar en fase 2 automática
  return patterns.some((re) => re.test(variantName));
}

async function pruneVariantsInSet(setId, dryRun = true) {
  const set = await figma.getNodeByIdAsync(setId);
  if (!set || set.type !== "COMPONENT_SET") return { error: "not a component set" };

  const toRemove = [];
  for (const child of set.children) {
    if (child.type !== "COMPONENT") continue;
    if (!variantAllowed(set.name, child.name)) {
      toRemove.push({ id: child.id, name: child.name });
    }
  }

  const removed = [];
  if (!dryRun) {
    for (const v of toRemove) {
      const node = await figma.getNodeByIdAsync(v.id);
      if (node && set.children.length > 1) {
        node.remove();
        removed.push(v.name);
      }
    }
  }

  return {
    setName: set.name,
    setId,
    dryRun,
    candidates: toRemove.map((v) => v.name),
    removed,
    remaining: set.children.length,
  };
}
