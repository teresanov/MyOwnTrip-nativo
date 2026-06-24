import { initShowcasePage, statusChip } from "../lib/bootstrap.js";
import data from "../data/components/swipeable-list-row.json";

initShowcasePage("components", { componentId: "swipeable-list-row" });

const figmaLine = data.figma
  ? `<p><a href="${data.figma}" target="_blank" rel="noopener">Referencia en Figma ↗</a></p>`
  : "<p><em>Sin component set en Figma — patrón documentado en Compose y showcase.</em></p>";

document.getElementById("component-header").innerHTML = `
  <p class="page-header__eyebrow">Component · Patrones · ${data.priority || "P1"}</p>
  <h1>${data.name} ${statusChip(data.status)}</h1>
  <p class="page-header__lead">${data.purpose}</p>
  ${figmaLine}
`;

document.getElementById("component-main").innerHTML = `
  <section class="section">
    <h2>Anatomía</h2>
    <ol class="list-dos">
      ${data.anatomy.map((a) => `<li>${a}</li>`).join("")}
    </ol>
  </section>

  <section class="section">
    <h2>Variantes</h2>
    <table>
      <thead><tr><th>Variante</th><th>Uso</th></tr></thead>
      <tbody>
        ${data.variants.map((v) => `<tr><td>${v.label}</td><td>${v.usage}</td></tr>`).join("")}
      </tbody>
    </table>
  </section>

  <section class="section">
    <h2>Implementaciones</h2>
    <ul class="list-dos">
      <li><code>SwipeableTripListRow.kt</code> — Home, sección «Más viajes» y filtro Archivados</li>
      <li><code>SwipeableWalletDocumentRow.kt</code> — Wallet, lista plana (chips Activos / Archivados / Todos)</li>
    </ul>
  </section>

  <section class="section">
    <h2>Tokens M3</h2>
    <p><code>${data.tokens.join("</code>, <code>")}</code></p>
  </section>

  <section class="section">
    <h2>Do</h2>
    <ul class="list-dos">${data.dos.map((d) => `<li>${d}</li>`).join("")}</ul>
  </section>

  <section class="section">
    <h2>Don't</h2>
    <ul class="list-dos list-dos--dont">${data.donts.map((d) => `<li>${d}</li>`).join("")}</ul>
  </section>

  <section class="section">
    <h2>Accesibilidad</h2>
    <ul class="list-dos">${data.a11y.map((a) => `<li>${a}</li>`).join("")}</ul>
  </section>

  <section class="section">
    <h2>Compose</h2>
    <pre><code>${escapeHtml(data.compose)}</code></pre>
  </section>

  <section class="section">
    <h2>Changelog</h2>
    <table>
      <thead><tr><th>Fecha</th><th>Nota</th></tr></thead>
      <tbody>
        ${data.changelog.map((e) => `<tr><td>${e.date}</td><td>${e.note}</td></tr>`).join("")}
      </tbody>
    </table>
  </section>
`;

function escapeHtml(s) {
  return s.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
}
