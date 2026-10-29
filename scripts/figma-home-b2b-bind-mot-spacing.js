/**
 * Fase B.2b — design-file Home: rebind page chrome → MOT · Spacing
 *
 * Run inside Figma Desktop Bridge on file Vf2tNMXyKAlJSV53A1v4Is
 * (figma_execute / plugin console). Scope: caps under shell 205:813 + 313:501.
 *
 * Policy:
 * - Only auto-layout nodes that are PAGE chrome (not deep inside INSTANCE).
 * - Map raw pads/gaps 4/8/16/24/88 (and LEGACY semantic/*) → mot/spacing/*.
 * - Skip component-instance internals (Button/Card chrome).
 */
(async () => {
  const CAP_IDS = ['205:813', '205:816', '205:1018', '228:8161', '313:501'];

  const VALUE_TO_MOT = {
    4: 'mot/spacing/component-xs',
    8: 'mot/spacing/component-sm',
    12: 'mot/spacing/component-md',
    16: 'mot/spacing/layout-md', // also screen-horizontal — prefer by prop below
    24: 'mot/spacing/layout-lg', // also screen-content-bottom
    88: 'mot/spacing/screen-content-bottom-with-fab',
  };

  const cols = await figma.variables.getLocalVariableCollectionsAsync();
  const byName = {};
  for (const col of cols) {
    for (const vid of col.variableIds) {
      const v = await figma.variables.getVariableByIdAsync(vid);
      if (v) byName[v.name] = v;
    }
  }

  // Also pull library vars if subscribed (name may match after import)
  async function resolveMot(name) {
    if (byName[name]) return byName[name];
    return null;
  }

  function isInsideInstance(node) {
    let p = node.parent;
    while (p) {
      if (p.type === 'INSTANCE') return true;
      p = p.parent;
    }
    return false;
  }

  function pickHorizontal(val) {
    if (val === 16) return 'mot/spacing/screen-horizontal';
    return VALUE_TO_MOT[val] || null;
  }
  function pickVertical(val) {
    if (val === 24) return 'mot/spacing/screen-content-bottom';
    if (val === 16) return 'mot/spacing/layout-md';
    return VALUE_TO_MOT[val] || null;
  }
  function pickGap(val) {
    if (val === 16) return 'mot/spacing/layout-md';
    if (val === 24) return 'mot/spacing/layout-lg';
    return VALUE_TO_MOT[val] || null;
  }

  async function tokenName(ref) {
    if (!ref) return null;
    const v = await figma.variables.getVariableByIdAsync(ref.id);
    return v ? v.name : null;
  }

  const audit = { caps: [], rebound: 0, skippedInstance: 0, missingToken: [], legacyHits: [] };

  for (const id of CAP_IDS) {
    const root = await figma.getNodeByIdAsync(id);
    if (!root) {
      audit.caps.push({ id, error: 'missing' });
      continue;
    }
    const cap = { id, name: root.name, unbound: [], rebound: 0, legacy: [] };
    const nodes = 'findAll' in root
      ? root.findAll((n) => 'layoutMode' in n && n.layoutMode !== 'NONE')
      : [];

    for (const n of nodes) {
      if (isInsideInstance(n)) {
        audit.skippedInstance++;
        continue;
      }
      const bv = n.boundVariables || {};
      const props = [
        ['paddingLeft', pickHorizontal, n.paddingLeft],
        ['paddingRight', pickHorizontal, n.paddingRight],
        ['paddingTop', pickVertical, n.paddingTop],
        ['paddingBottom', pickVertical, n.paddingBottom],
        ['itemSpacing', pickGap, n.itemSpacing],
      ];
      for (const [prop, picker, raw] of props) {
        const bound = bv[prop];
        const boundName = bound ? await tokenName(bound) : null;
        if (boundName && /LEGACY|semantic\//i.test(boundName)) {
          cap.legacy.push({ node: n.name, prop, boundName, raw });
          audit.legacyHits.push({ cap: id, node: n.name, prop, boundName });
        }
        const targetName =
          boundName && /mot\/spacing\//.test(boundName)
            ? null // already MOT
            : picker(Math.round(raw));
        if (!targetName) {
          if (!bound && raw > 0 && [4, 8, 12, 16, 24, 88].includes(Math.round(raw))) {
            cap.unbound.push({ node: n.name, prop, raw });
          }
          continue;
        }
        // Rebind if unbound OR legacy OR wrong collection
        if (!boundName || /LEGACY|semantic\//i.test(boundName) || !/mot\/spacing\//.test(boundName)) {
          const tok = await resolveMot(targetName);
          if (!tok) {
            audit.missingToken.push(targetName);
            continue;
          }
          // Heuristic overrides for known page roles
          let use = tok;
          if ((prop === 'paddingLeft' || prop === 'paddingRight') && Math.round(raw) === 16) {
            use = (await resolveMot('mot/spacing/screen-horizontal')) || tok;
          }
          if (prop === 'paddingBottom' && Math.round(raw) === 24) {
            use = (await resolveMot('mot/spacing/screen-content-bottom')) || tok;
          }
          n.setBoundVariable(prop, use);
          cap.rebound++;
          audit.rebound++;
        }
      }
    }
    audit.caps.push(cap);
  }

  audit.missingToken = [...new Set(audit.missingToken)];
  return audit;
})();
