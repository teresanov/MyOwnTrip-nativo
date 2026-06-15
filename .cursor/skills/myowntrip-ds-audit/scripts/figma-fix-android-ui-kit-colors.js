/**
 * Aplicar paleta MyOwnTrip (gris-azul + ocre) al Android UI Kit.
 * Colecciones: Content + AOSP (no "M3").
 * Fuente: M3_MOTrip.json · Ejecutar vía figma_execute en WyjZMISihVMTMg5RYSJWba.
 */
const TARGET_FILE = "WyjZMISihVMTMg5RYSJWba";
const COLLECTIONS = ["Content", "AOSP"];

// TARGETS inlined by build script or copied from figma-fix-gris-azul-colors.js
const TARGETS = __TARGETS_PLACEHOLDER__;

function hexToRgb(hex) {
  let h = hex;
  if (h.length === 9) h = h.slice(0, 7);
  const n = parseInt(h.slice(1), 16);
  return { r: ((n >> 16) & 255) / 255, g: ((n >> 8) & 255) / 255, b: (n & 255) / 255 };
}

function valueToHex(value) {
  if (!value) return null;
  if (typeof value === "object" && "r" in value) {
    const c = (x) => Math.round(Math.max(0, Math.min(1, x)) * 255);
    const hex =
      "#" +
      [c(value.r), c(value.g), c(value.b)]
        .map((v) => v.toString(16).padStart(2, "0"))
        .join("")
        .toUpperCase();
    const a = value.a ?? 1;
    if (a < 0.999) {
      return hex + Math.round(a * 255).toString(16).padStart(2, "0").toUpperCase();
    }
    return hex;
  }
  return null;
}

function colorEqual(current, targetHex) {
  return valueToHex(current) === targetHex.toUpperCase();
}

async function fixAndroidUiKitColors() {
  if (figma.fileKey !== TARGET_FILE) {
    throw new Error("ABORT: archivo incorrecto — " + figma.fileKey);
  }

  const collections = await figma.variables.getLocalVariableCollectionsAsync();
  const vars = await figma.variables.getLocalVariablesAsync("COLOR");
  const result = { updated: 0, skipped: 0, missing: [], samples: [], collections: [] };

  for (const colName of COLLECTIONS) {
    const col = collections.find((c) => c.name === colName);
    if (!col) {
      result.collections.push({ name: colName, status: "not_found" });
      continue;
    }

    const byName = {};
    for (const v of vars) {
      if (v.variableCollectionId === col.id) byName[v.name] = v;
    }

    let colUpdated = 0;
    let colSkipped = 0;

    for (const mode of col.modes) {
      const modeTargets = TARGETS[mode.name];
      if (!modeTargets) continue;

      for (const [name, hex] of Object.entries(modeTargets)) {
        const variable = byName[name];
        if (!variable) {
          result.missing.push(colName + "/" + name);
          continue;
        }

        const opacity = hex.length === 9 ? parseInt(hex.slice(7), 16) / 255 : 1;
        const rgb = hexToRgb(hex);
        const current = variable.valuesByMode[mode.modeId];
        const nextValue = opacity < 1 ? { ...rgb, a: opacity } : rgb;

        if (colorEqual(current, hex)) {
          result.skipped++;
          colSkipped++;
          continue;
        }

        variable.setValueForMode(mode.modeId, nextValue);
        result.updated++;
        colUpdated++;
        if (result.samples.length < 12) {
          result.samples.push({ collection: colName, mode: mode.name, name, to: hex });
        }
      }
    }

    result.collections.push({ name: colName, updated: colUpdated, skipped: colSkipped });
  }

  result.missing = [...new Set(result.missing)].slice(0, 30);
  return result;
}

return fixAndroidUiKitColors();
