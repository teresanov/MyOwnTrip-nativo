import { initShowcasePage } from "../lib/bootstrap.js";
import tokens from "../data/tokens.json";

initShowcasePage("color");

function swatch(name, hex) {
  return `
    <div class="swatch">
      <div class="swatch__color" style="background-color:${hex}" aria-hidden="true"></div>
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

const keyRoles = ["primary", "onPrimary", "tertiary", "onTertiary", "error", "onError", "surface", "onSurface"];

document.getElementById("intro-section").innerHTML = `
  <p>Roles desde <code>${tokens.source}</code> · colección M3 · modos Light/Dark.</p>
  <div class="callout">
    <strong>Vista rápida Light:</strong>
    <div class="card-grid" style="margin-top:12px;grid-template-columns:repeat(auto-fill,minmax(120px,1fr))">
      ${keyRoles.map((role) => swatch(role, tokens.light[role])).join("")}
    </div>
  </div>
`;

document.getElementById("roles-section").innerHTML = `
  <h2>Roles Light</h2>
  ${roleGrid(tokens.light)}
  <h2 style="margin-top:32px">Roles Dark</h2>
  ${roleGrid(tokens.dark)}
`;

document.getElementById("extensions-section").innerHTML = `
  <h2>Extensiones (no M3)</h2>
  ${extensionGrid(tokens.extensions)}
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
