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

function roleGrid(roles) {
  return `<div class="card-grid">${Object.entries(roles).map(([k, v]) => swatch(k, v)).join("")}</div>`;
}

function extensionGrid(ext) {
  return Object.entries(ext)
    .map(
      ([name, modes]) => `
      <div class="card" style="margin-bottom:12px">
        <h4 style="margin:0 0 8px">${name}</h4>
        <div class="card-grid" style="grid-template-columns:repeat(auto-fill,minmax(140px,1fr))">
          ${swatch("light", modes.light)}
          ${swatch("dark", modes.dark)}
        </div>
      </div>`
    )
    .join("");
}

document.getElementById("seeds-section").innerHTML = `
  <h2>Fuente canónica</h2>
  <p>Roles desde <code>${tokens.source}</code> · colección M3 · modos Light/Dark.</p>
  <h3>Extensiones (no M3)</h3>
  ${extensionGrid(tokens.extensions)}
`;

document.getElementById("roles-section").innerHTML = `
  <h2>Roles Light</h2>
  ${roleGrid(tokens.light)}
  <h2 style="margin-top:32px">Roles Dark</h2>
  ${roleGrid(tokens.dark)}
`;

const stateRows = Object.entries(tokens.stateLayers)
  .filter(([k]) => k !== "note")
  .map(([k, v]) => `<tr><td>${k}</td><td>${v}</td></tr>`)
  .join("");

document.getElementById("state-layers-section").innerHTML = `
  <h2>State layers (runtime)</h2>
  <p>No son tokens de color. Aplicados sobre <code>on*</code> por componentes M3 en Compose.</p>
  <table>
    <thead><tr><th>Estado</th><th>Opacidad</th></tr></thead>
    <tbody>${stateRows}</tbody>
  </table>
  <p class="callout" style="margin-top:16px">${tokens.stateLayers.note}</p>
  <p><strong>Regla:</strong> <code>tertiary</code> (${tokens.light.tertiary}) ≠ <code>error</code> (${tokens.light.error}).</p>
`;
