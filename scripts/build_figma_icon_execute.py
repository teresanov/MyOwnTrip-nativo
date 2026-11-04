"""Genera payload JS para figma_execute (iconos MOTIcons faltantes en Figma)."""

import importlib.util
import json
import pathlib
import urllib.request

ROOT = pathlib.Path(__file__).resolve().parents[1]
GEN = ROOT / "scripts/generate_material_symbols.py"
OUT = ROOT / "scripts/.tmp_figma_icon_execute.js"
URL = "https://fonts.gstatic.com/s/i/short-term/release/materialsymbolssharp/{name}/wght300/24px.svg"

PREAMBLE = r"""
async function fillIconFrame(iconFrame, svg, varId) {
  for (const child of [...iconFrame.children]) child.remove();
  const imported = figma.createNodeFromSvg(svg);
  imported.resize(24, 24);
  const vectors = imported.findAll(n => n.type === "VECTOR");
  for (const v of vectors) {
    const clone = v.clone();
    const fill = figma.util.solidPaint("#000000");
    fill.boundVariables = { color: { type: "VARIABLE_ALIAS", id: varId } };
    clone.fills = [fill];
    iconFrame.appendChild(clone);
    clone.x = v.x + imported.x;
    clone.y = v.y + imported.y;
  }
  imported.remove();
}

const ICONS = """


def main() -> None:
    spec = importlib.util.spec_from_file_location("gen", GEN)
    gen = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(gen)
    symbols = sorted({v[0] for v in gen.ICONS.values()})
    icons = {}
    for sym in symbols:
        with urllib.request.urlopen(URL.format(name=sym), timeout=30) as resp:
            icons[sym] = resp.read().decode("utf-8")
    body = json.dumps(icons, ensure_ascii=False)
    tail = r""";
const section = await figma.getNodeByIdAsync("55594:2485");
const template = await figma.getNodeByIdAsync("54616:25407");
const varId = "VariableID:54778:427";
const gap = 56;
let x = 256;
let y = 1739;
const created = [];
const skipped = [];
for (const name of Object.keys(ICONS).sort()) {
  if (section.findOne(n => n.type === "COMPONENT" && n.name === name)) {
    skipped.push(name);
    continue;
  }
  const comp = template.clone();
  comp.name = name;
  const iconFrame = comp.findOne(n => n.name === "icon");
  await fillIconFrame(iconFrame, ICONS[name], varId);
  comp.x = x;
  comp.y = y;
  section.appendChild(comp);
  created.push({ name, id: comp.id, x, y });
  x += gap;
  if (x > 860) {
    x = 32;
    y += gap;
  }
}
return { created, skipped, count: created.length };
"""
    OUT.write_text(PREAMBLE + body + tail, encoding="utf-8")
    print(f"written {OUT} ({OUT.stat().st_size} bytes)")


if __name__ == "__main__":
    main()
