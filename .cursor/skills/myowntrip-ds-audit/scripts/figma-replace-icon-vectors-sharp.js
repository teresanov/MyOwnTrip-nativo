/**
 * Sustituye la geometría kit (VECTOR "icon") por SVG Material Symbols Sharp w200
 * **dentro del mismo COMPONENT main** — preserva instancias.
 *
 * Ejecutar vía Desktop Bridge en archivo principal solamente.
 * Uso: pasar `ICONS` (name + svg) y opcional `PILOT_ONLY` lista de nombres.
 */

const MAIN_FILE_KEY = "zrGAL4v6MEMc9hzZemU432";
const SHARP_SECTION_ID = "55594:2485";

function assertMainFile() {
  if (figma.fileKey !== MAIN_FILE_KEY) {
    throw new Error(`ABORT: archivo ${figma.fileKey} — solo ${MAIN_FILE_KEY}`);
  }
}

function isKitVector(comp) {
  const kids = comp.children;
  return (
    kids.length === 1 &&
    kids[0].type === "VECTOR" &&
    kids[0].name === "icon"
  );
}

function applyFills(node, fills) {
  if (node.type === "VECTOR" && fills.length) node.fills = fills;
  if ("children" in node) for (const c of node.children) applyFills(c, fills);
}

function replaceIconGeometry(comp, svg) {
  const size = comp.width || 24;
  const old = comp.children.slice();
  const oldIcon = comp.findOne((n) => n.name === "icon" || n.type === "VECTOR");
  const fills = oldIcon?.fills ? JSON.parse(JSON.stringify(oldIcon.fills)) : [];

  const svgNode = figma.createNodeFromSvg(svg);
  const scale = Math.min(size / svgNode.width, size / svgNode.height);
  svgNode.rescale(scale);
  svgNode.x = (size - svgNode.width) / 2;
  svgNode.y = (size - svgNode.height) / 2;
  svgNode.name = "icon";

  for (const c of old) c.remove();
  comp.appendChild(svgNode);
  applyFills(svgNode, fills);
  return { id: comp.id, name: comp.name, w: comp.width, h: comp.height };
}

async function replaceSharpVectors(icons, pilotOnly = null) {
  assertMainFile();
  const section = await figma.getNodeByIdAsync(SHARP_SECTION_ID);
  if (!section) throw new Error("Sección Sharp no encontrada");

  const byName = new Map();
  for (const c of section.findAll((n) => n.type === "COMPONENT")) {
    if (!byName.has(c.name)) byName.set(c.name, []);
    byName.get(c.name).push(c);
  }

  const results = { replaced: [], skipped: [], errors: [] };

  for (const item of icons) {
    if (pilotOnly && !pilotOnly.includes(item.name)) continue;
    const candidates = byName.get(item.name) || [];
    const kit = candidates.find(isKitVector);
    const comp = kit || candidates[0];
    if (!comp) {
      results.skipped.push({ name: item.name, reason: "no component" });
      continue;
    }
    if (!isKitVector(comp)) {
      results.skipped.push({ name: item.name, reason: "already replaced", id: comp.id });
      continue;
    }
    try {
      results.replaced.push(replaceIconGeometry(comp, item.svg));
    } catch (e) {
      results.errors.push({ name: item.name, err: String(e) });
    }
  }

  return results;
}
