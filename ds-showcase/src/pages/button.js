import { initShowcasePage, statusChip } from "../lib/bootstrap.js";
import data from "../data/components/button.json";

initShowcasePage("components", { componentId: "button" });

document.getElementById("component-header").innerHTML = `
  <p class="page-header__eyebrow">Component · ${data.priority || "P0"}</p>
  <h1>${data.name} ${statusChip(data.status)}</h1>
  <p class="page-header__lead">${data.purpose}</p>
  <p><a href="${data.figma}" target="_blank" rel="noopener">Component set en Figma ↗</a></p>
`;

const variantDemo = {
  filled: "btn--filled",
  tonal: "btn--tonal",
  outlined: "btn--outlined",
  text: "btn--text",
};

document.getElementById("component-main").innerHTML = `
  <section class="section">
    <h2>Variantes</h2>
    <div class="btn-row">
      ${data.variants
        .map((v) => `<button type="button" class="btn ${variantDemo[v.id]}">${v.label}</button>`)
        .join("")}
    </div>
    <table style="margin-top:24px">
      <thead><tr><th>Variante</th><th>Uso</th></tr></thead>
      <tbody>
        ${data.variants.map((v) => `<tr><td>${v.label}</td><td>${v.usage}</td></tr>`).join("")}
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
