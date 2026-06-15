#!/usr/bin/env node
/**
 * Genera el código plugin para figma_execute a partir de M3_MOTrip.json.
 * Uso: node build-android-ui-kit-plugin.js > /tmp/android-ui-fix.js
 */
const fs = require("fs");
const path = require("path");

const collectionArg = process.argv[2]; // optional: Content | AOSP
const filterArg = process.argv[3] || "schemes,state"; // schemes | state | palettes | all
const modeArg = process.argv[4]; // optional: Light | Dark | Light High Contrast | ...
const root = path.resolve(__dirname, "../../../..");
const motrip = JSON.parse(
  fs.readFileSync(path.join(root, "M3_MOTrip.json"), "utf8")
);
const template = fs.readFileSync(
  path.join(__dirname, "figma-fix-android-ui-kit-colors.js"),
  "utf8"
);

const m3 = motrip.collections.find((c) => c.name === "M3");
if (!m3) throw new Error("M3 collection not found in M3_MOTrip.json");

const filters = filterArg === "all" ? null : new Set(filterArg.split(","));

function includeName(name) {
  if (!filters) return true;
  if (filters.has("schemes") && name.startsWith("Schemes/")) return true;
  if (filters.has("state") && name.startsWith("State Layers/")) return true;
  if (filters.has("palettes") && name.startsWith("Palettes/")) return true;
  if (filters.has("extended") && name.startsWith("Extended Colors/")) return true;
  return false;
}

const TARGETS = {};
for (const mode of m3.modes) {
  if (modeArg && mode.name !== modeArg) continue;
  TARGETS[mode.name] = {};
  for (const v of mode.variables) {
    if (v.type === "color" && !v.isAlias && v.value && includeName(v.name)) {
      TARGETS[mode.name][v.name] = v.value;
    }
  }
}

const code = template
  .replace("__TARGETS_PLACEHOLDER__", JSON.stringify(TARGETS))
  .replace(
    'const COLLECTIONS = ["Content", "AOSP"];',
    collectionArg
      ? `const COLLECTIONS = [${JSON.stringify(collectionArg)}];`
      : 'const COLLECTIONS = ["Content", "AOSP"];'
  );
process.stdout.write(code);
