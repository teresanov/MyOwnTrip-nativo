// Copia el bundle Wasm de live-demos/ (demos vivas de Compose) a public/live/.
// Antes: ./gradlew -p live-demos wasmJsBrowserDistribution  (con el JDK de Android Studio).
import fs from "node:fs";
import path from "node:path";
import { fileURLToPath } from "node:url";

const here = path.dirname(fileURLToPath(import.meta.url));
const src = path.resolve(here, "../../live-demos/build/dist/wasmJs/productionExecutable");
const out = path.resolve(here, "../public/live");

if (!fs.existsSync(path.join(src, "demos.js"))) {
  console.error(`No hay bundle en ${src}. Ejecuta antes: ./gradlew -p live-demos wasmJsBrowserDistribution`);
  process.exit(1);
}

fs.rmSync(out, { recursive: true, force: true });
fs.mkdirSync(path.join(out, "font"), { recursive: true });

for (const f of fs.readdirSync(src)) {
  if (f === "demos.js" || f.endsWith(".wasm")) fs.copyFileSync(path.join(src, f), path.join(out, f));
}
for (const f of ["inter.ttf", "fraunces.ttf"]) {
  fs.copyFileSync(path.join(src, "font", f), path.join(out, "font", f));
}
console.log("Demos vivas copiadas a", out);
