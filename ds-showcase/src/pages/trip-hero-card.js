import { initShowcasePage, statusChip } from "../lib/bootstrap.js";
import data from "../data/components/trip-hero-card.json";

initShowcasePage("components", { componentId: "trip-hero-card" });

document.getElementById("component-header").innerHTML = `
  <p class="page-header__eyebrow">Component · Cards · ${data.priority || "P1"}</p>
  <h1>${data.name} ${statusChip(data.status)}</h1>
  <p class="page-header__lead">${data.purpose}</p>
  <p><a href="${data.figma}" target="_blank" rel="noopener">Sección Cards en Figma ↗</a></p>
`;

document.getElementById("component-main").innerHTML = `
  <section class="section">
    <h2>Anatomía</h2>
    <ol class="list-dos">
      ${data.anatomy.map((a) => `<li>${a}</li>`).join("")}
    </ol>
  </section>

  <section class="section">
    <h2>Variantes (Style)</h2>
    <table>
      <thead><tr><th>Variante</th><th>Uso</th></tr></thead>
      <tbody>
        ${data.variants.map((v) => `<tr><td>${v.label}</td><td>${v.usage}</td></tr>`).join("")}
      </tbody>
    </table>
  </section>

  <section class="section">
    <h2>Properties (instancia)</h2>
    <table>
      <thead><tr><th>Property</th><th>Tipo</th><th>Ejemplo</th></tr></thead>
      <tbody>
        ${data.properties.map((p) => `<tr><td><code>${p.name}</code></td><td>${p.type}</td><td>${p.example}</td></tr>`).join("")}
      </tbody>
    </table>
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
