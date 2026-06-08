import { initShowcasePage } from "../lib/bootstrap.js";
import tokens from "../data/tokens.json";

initShowcasePage("color");

function swatch(name, hex) {
  return `
    <div class="swatch">
      <div class="swatch__color" style="background:${hex}"></div>
      <div class="swatch__meta"><strong>${name}</strong><br><code>${hex}</code></div>
    </div>`;
}

document.getElementById("seeds-section").innerHTML = `
  <h2>Seeds MTB</h2>
  <div class="card-grid">
    ${Object.entries(tokens.seeds).map(([k, v]) => swatch(k, v)).join("")}
  </div>
  <h3>Extensiones</h3>
  <div class="card-grid">
    ${Object.entries(tokens.extensions).map(([k, v]) => swatch(k, v)).join("")}
  </div>
`;

document.getElementById("roles-section").innerHTML = `
  <h2>Roles Light</h2>
  <div class="card-grid">
    ${Object.entries(tokens.light).map(([k, v]) => swatch(k, v)).join("")}
  </div>
`;

document.getElementById("state-layers-section").innerHTML = `
  <h2>State layers (runtime)</h2>
  <p>No son tokens de color. Aplicados sobre <code>on*</code> por componentes M3.</p>
  <table>
    <thead><tr><th>Estado</th><th>Opacidad</th></tr></thead>
    <tbody>
      ${Object.entries(tokens.stateLayers)
        .map(([k, v]) => `<tr><td>${k}</td><td>${v}</td></tr>`)
        .join("")}
    </tbody>
  </table>
  <p style="margin-top:16px"><strong>Regla:</strong> tertiary (#D9382C) ≠ error (#B3261E).</p>
`;
