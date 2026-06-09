/**
 * FASE A — Vincular fills de paint styles a variables M3 (sin tocar componentes).
 * Ejecutar vía figma_execute (Desktop Bridge).
 */
async function phaseABindStyles() {
  function camelToTitle(s) {
    return s
      .replace(/([a-z])([A-Z])/g, "$1 $2")
      .split(" ")
      .map((w) => w.charAt(0).toUpperCase() + w.slice(1))
      .join(" ");
  }

  function kebabToScheme(role) {
    return (
      "Schemes/" +
      role
        .split("-")
        .map((w) => w.charAt(0).toUpperCase() + w.slice(1))
        .join(" ")
    );
  }

  function isHexFixed(style) {
    const p = style.paints[0];
    return p && p.type === "SOLID" && !p.boundVariables?.color?.id;
  }

  function bindPaintFromRef(targetStyle, refPaint) {
    const p = targetStyle.paints[0];
    if (!p || p.type !== "SOLID" || !refPaint?.boundVariables?.color) {
      return false;
    }
    const newPaint = {
      type: "SOLID",
      visible: p.visible !== false,
      opacity: p.opacity ?? 1,
      blendMode: p.blendMode || "NORMAL",
      color: { ...p.color },
      boundVariables: {
        color: { ...refPaint.boundVariables.color },
      },
    };
    targetStyle.paints = [newPaint];
    return true;
  }

  const allStyles = await figma.getLocalPaintStylesAsync();
  const byName = Object.fromEntries(allStyles.map((s) => [s.name, s]));

  const collections = await figma.variables.getLocalVariableCollectionsAsync();
  const m3col = collections.find((c) => c.name === "M3");
  const varByName = {};
  for (const vid of m3col.variableIds) {
    const v = await figma.variables.getVariableByIdAsync(vid);
    if (v) varByName[v.name] = v;
  }

  const results = { bound: [], skipped: [], failed: [], already: [] };

  function resolveVariableName(styleName) {
    // M3/sys/light/on-surface
    let m = styleName.match(/^M3\/sys\/light\/(.+)$/);
    if (m) return kebabToScheme(m[1]);

    // M3/state-layers/light/onSurfaceVariant/opacity-0.08
    m = styleName.match(
      /^M3\/state-layers\/light\/([^/]+)\/opacity-([0-9.]+)$/
    );
    if (m) {
      const op = String(Math.round(parseFloat(m[2]) * 100)).padStart(2, "0");
      return `State Layers/${camelToTitle(m[1])}/Opacity-${op}`;
    }

    if (/M3\s*\/\s*ref\s*\/\s*neutral\s*\/\s*neutral100/i.test(styleName)) {
      return "Palettes/Neutral 100";
    }

    if (styleName === "M3/white" || styleName === "M3 / white") {
      return "Palettes/Neutral 100";
    }

    return null;
  }

  function findReferencePaint(styleName) {
    // 1) Style canónico con espacios (sys/light)
    const compactSys = styleName.match(/^M3\/sys\/light\/(.+)$/);
    if (compactSys) {
      const canonical = byName[`M3 / sys / light / ${compactSys[1]}`];
      if (canonical?.paints[0]?.boundVariables?.color) {
        return { paint: canonical.paints[0], via: "canonical-sys" };
      }
    }

    // 2) State-layer variable/* espejo
    const compactSl = styleName.match(
      /^M3\/state-layers\/light\/([^/]+)\/opacity-([0-9.]+)$/
    );
    if (compactSl) {
      const varStyle =
        byName[
          `M3/state-layers/variable/${compactSl[1]}/opacity-${compactSl[2]}`
        ];
      if (varStyle?.paints[0]?.boundVariables?.color) {
        return { paint: varStyle.paints[0], via: "state-layer-variable" };
      }
      const spaced =
        byName[
          `M3 / state-layers / light / ${compactSl[1]} / opacity-${compactSl[2]}`
        ];
      if (spaced?.paints[0]?.boundVariables?.color) {
        return { paint: spaced.paints[0], via: "canonical-state-layer" };
      }
    }

    return null;
  }

  // Prioridad: hex-fixed usados en auditoría + toda familia compacta sys/state-layers light
  const targets = allStyles.filter((s) => {
    if (!isHexFixed(s)) return false;
    return (
      /^M3\/sys\/light\//.test(s.name) ||
      /^M3\/state-layers\/light\//.test(s.name) ||
      /ref\s*\/\s*neutral\s*\/\s*neutral100/i.test(s.name) ||
      s.name === "M3/white" ||
      s.name === "M3 / white"
    );
  });

  for (const style of targets) {
    if (!isHexFixed(style)) {
      results.already.push(style.name);
      continue;
    }

    const ref = findReferencePaint(style.name);
    if (ref && bindPaintFromRef(style, ref.paint)) {
      results.bound.push({ name: style.name, via: ref.via });
      continue;
    }

    const varName = resolveVariableName(style.name);
    const variable = varName ? varByName[varName] : null;
    if (variable) {
      const p = style.paints[0];
      const newPaint = {
        type: "SOLID",
        visible: p.visible !== false,
        opacity: p.opacity ?? 1,
        blendMode: p.blendMode || "NORMAL",
        color: { ...p.color },
        boundVariables: {
          color: figma.variables.createVariableAlias(variable),
        },
      };
      style.paints = [newPaint];
      results.bound.push({ name: style.name, via: "var-map", varName });
      continue;
    }

    results.failed.push({ name: style.name, varName });
  }

  return {
    targetCount: targets.length,
    boundCount: results.bound.length,
    failedCount: results.failed.length,
    bound: results.bound,
    failed: results.failed,
  };
}

return await phaseABindStyles();
