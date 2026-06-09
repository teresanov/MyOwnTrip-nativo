/**
 * FASE B — Desenganchar fillStyleId y strokeStyleId en árboles COMPONENT;
 * conservar variables directas (boundVariables en paint).
 * Ejecutar vía figma_execute (Desktop Bridge).
 */
async function phaseBDetachPaintStyles(pageNames) {
  const skipPages = new Set([
    "Table of contents",
    "Examples",
    "Reference",
    "---",
    "_archive",
  ]);

  const stats = {
    fillsDetached: 0,
    strokesDetached: 0,
    skippedNoVar: 0,
    boundFromStyle: 0,
    errors: 0,
    errorSamples: [],
    componentsProcessed: 0,
  };

  async function detachFillStyle(node) {
    if (!("fillStyleId" in node) || !node.fillStyleId) return null;

    const style = await figma.getStyleByIdAsync(node.fillStyleId);
    const stylePaint = style?.paints?.[0];
    const styleVarId = stylePaint?.boundVariables?.color?.id;

    const fills = node.fills;
    if (fills === figma.mixed || !Array.isArray(fills) || fills.length === 0) {
      return "skipped-mixed";
    }

    const fill0 = fills[0];
    const fillVarId = fill0?.boundVariables?.color?.id;
    const nodeBoundId = node.boundVariables?.fills?.[0]?.id;

    if (!styleVarId && !fillVarId && !nodeBoundId) {
      return "skipped-no-var";
    }

    if (!fillVarId && !nodeBoundId && styleVarId) {
      const variable = await figma.variables.getVariableByIdAsync(styleVarId);
      if (!variable) return "skipped-no-var";
      figma.variables.setBoundVariableForPaint(node, "fills", 0, variable);
      stats.boundFromStyle++;
    }

    await node.setFillStyleIdAsync("");
    return "detached";
  }

  async function detachStrokeStyle(node) {
    if (!("strokeStyleId" in node) || !node.strokeStyleId) return null;

    const style = await figma.getStyleByIdAsync(node.strokeStyleId);
    const stylePaint = style?.paints?.[0];
    const styleVarId = stylePaint?.boundVariables?.color?.id;

    const strokes = node.strokes;
    if (strokes === figma.mixed || !Array.isArray(strokes) || strokes.length === 0) {
      return "skipped-mixed";
    }

    const stroke0 = strokes[0];
    const strokeVarId = stroke0?.boundVariables?.color?.id;
    const nodeBoundId = node.boundVariables?.strokes?.[0]?.id;

    if (!styleVarId && !strokeVarId && !nodeBoundId) {
      return "skipped-no-var";
    }

    if (!strokeVarId && !nodeBoundId && styleVarId) {
      const variable = await figma.variables.getVariableByIdAsync(styleVarId);
      if (!variable) return "skipped-no-var";
      figma.variables.setBoundVariableForPaint(node, "strokes", 0, variable);
      stats.boundFromStyle++;
    }

    await node.setStrokeStyleIdAsync("");
    return "detached";
  }

  async function walkComponentTree(node) {
    const fillResult = await detachFillStyle(node);
    if (fillResult === "detached") stats.fillsDetached++;
    else if (fillResult === "skipped-no-var" || fillResult === "skipped-mixed")
      stats.skippedNoVar++;

    const strokeResult = await detachStrokeStyle(node);
    if (strokeResult === "detached") stats.strokesDetached++;
    else if (strokeResult === "skipped-no-var" || strokeResult === "skipped-mixed")
      stats.skippedNoVar++;

    if ("children" in node) {
      for (const child of node.children) {
        await walkComponentTree(child);
      }
    }
  }

  const pages = figma.root.children.filter(
    (p) =>
      !skipPages.has(p.name) &&
      !p.name.startsWith("_") &&
      (!pageNames?.length || pageNames.includes(p.name))
  );

  function findComponents(root) {
    if (root.type === "COMPONENT_SET") {
      for (const c of root.children) {
        if (c.type === "COMPONENT") {
          stats.componentsProcessed++;
          return [c];
        }
      }
      return root.children.filter((c) => c.type === "COMPONENT");
    }
    if (root.type === "COMPONENT") return [root];
    const out = [];
    if ("children" in root) {
      for (const ch of root.children) out.push(...findComponents(ch));
    }
    return out;
  }

  async function walkPage(node) {
    if (node.type === "COMPONENT_SET") {
      for (const c of node.children) {
        if (c.type === "COMPONENT") {
          stats.componentsProcessed++;
          try {
            await walkComponentTree(c);
          } catch (e) {
            stats.errors++;
            if (stats.errorSamples.length < 5) {
              stats.errorSamples.push({
                component: c.name,
                error: String(e),
              });
            }
          }
        }
      }
      return;
    }
    if (node.type === "COMPONENT") {
      stats.componentsProcessed++;
      try {
        await walkComponentTree(node);
      } catch (e) {
        stats.errors++;
        if (stats.errorSamples.length < 5) {
          stats.errorSamples.push({ component: node.name, error: String(e) });
        }
      }
      return;
    }
    if ("children" in node) {
      for (const ch of node.children) await walkPage(ch);
    }
  }

  for (const page of pages) {
    await walkPage(page);
  }

  return { pages: pages.map((p) => p.name), ...stats };
}

// Sin argumento = todas las páginas activas
return await phaseBDetachPaintStyles();
