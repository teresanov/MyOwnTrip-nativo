/**
 * Tokens Android-only en colección AOSP (sysUI, Shade, widgets…).
 * Derivan de roles MyOwnTrip — no existen en M3_MOTrip.json.
 * Ejecutar vía figma_execute en WyjZMISihVMTMg5RYSJWba.
 */
const TARGET_FILE = "WyjZMISihVMTMg5RYSJWba";
const COLLECTION = "AOSP";

// Light / Dark + mismos valores en modos contrast (mejor que verde Material)
const CHROME = {
  Light: {
    "Schemes/Shade Active": "#D5E4F3",
    "Schemes/On Shade Active": "#2E363D",
    "Schemes/On Shade Active Variant": "#3A444C",
    "Schemes/Shade Inactive": "#7D7667",
    "Schemes/On Shade Inactive": "#FFF8F2",
    "Schemes/On Shade Inactive Variant": "#4C4639",
    "Schemes/Shade Disabled": "#CEC6B4",
    "Schemes/Under Surface": "#F6EDDF",
    "Schemes/Theme app": "#4A5864",
    "Schemes/On Theme App": "#FFFFFF",
    "Schemes/Theme Notif": "#F1E7D9",
    "Schemes/Theme App Ring": "#825513",
    "Schemes/Weather Temp": "#F8BB71",
    "Schemes/Clock Hour": "#4A5864",
    "Schemes/Clock Second": "#FCF2E5",
    "Schemes/Clock Minute": "#FFDDB8",
    "Schemes/Widget Background": "#F6EDDF",
    "Schemes/Overview Background": "#EBE1D4",
  },
  Dark: {
    "Schemes/Shade Active": "#3A444C",
    "Schemes/On Shade Active": "#E4E1DC",
    "Schemes/On Shade Active Variant": "#B4BAC2",
    "Schemes/Shade Inactive": "#4C4639",
    "Schemes/On Shade Inactive": "#EBE1D4",
    "Schemes/On Shade Inactive Variant": "#989080",
    "Schemes/Shade Disabled": "#2E2921",
    "Schemes/Under Surface": "#231F17",
    "Schemes/Theme app": "#B4BAC2",
    "Schemes/On Theme App": "#1A2228",
    "Schemes/Theme Notif": "#2E2921",
    "Schemes/Theme App Ring": "#F8BB71",
    "Schemes/Weather Temp": "#F8BB71",
    "Schemes/Clock Hour": "#B4BAC2",
    "Schemes/Clock Second": "#39342B",
    "Schemes/Clock Minute": "#663E00",
    "Schemes/Widget Background": "#231F17",
    "Schemes/Overview Background": "#39342B",
  },
  AOD: {
    "Schemes/Shade Active": "#3A444C",
    "Schemes/On Shade Active": "#E4E1DC",
    "Schemes/Theme app": "#B4BAC2",
    "Schemes/On Theme App": "#1A2228",
    "Schemes/Widget Background": "#17130B",
    "Schemes/Overview Background": "#2E2921",
  },
};

function hexToRgb(hex) {
  const n = parseInt(hex.slice(1), 16);
  return { r: ((n >> 16) & 255) / 255, g: ((n >> 8) & 255) / 255, b: (n & 255) / 255 };
}

async function fixAospChrome() {
  if (figma.fileKey !== TARGET_FILE) throw new Error("Wrong file");

  const collections = await figma.variables.getLocalVariableCollectionsAsync();
  const col = collections.find((c) => c.name === COLLECTION);
  if (!col) throw new Error("AOSP collection missing");

  const vars = await figma.variables.getLocalVariablesAsync("COLOR");
  const byName = {};
  for (const v of vars) {
    if (v.variableCollectionId === col.id) byName[v.name] = v;
  }

  const contrastModes = col.modes
    .map((m) => m.name)
    .filter((n) => n.includes("Contrast"));

  let updated = 0;
  const samples = [];

  for (const mode of col.modes) {
    let targets = CHROME[mode.name];
    if (!targets) {
      if (mode.name.includes("High Contrast") || mode.name.includes("Medium Contrast")) {
        targets = mode.name.startsWith("Dark") ? CHROME.Dark : CHROME.Light;
      } else continue;
    }

    for (const [name, hex] of Object.entries(targets)) {
      const variable = byName[name];
      if (!variable) continue;
      variable.setValueForMode(mode.modeId, hexToRgb(hex));
      updated++;
      if (samples.length < 8) samples.push({ mode: mode.name, name, hex });
    }
  }

  return { updated, samples, contrastModes };
}

return fixAospChrome();
