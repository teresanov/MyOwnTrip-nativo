import { initShowcasePage, statusChip } from "../lib/bootstrap.js";
import data from "../data/components/stacked-card.json";

initShowcasePage("components", { componentId: "stacked-card" });

document.getElementById("component-header").innerHTML = `
  <p class="page-header__eyebrow">Component · Cards · ${data.priority || "P1"}</p>
  <h1>${data.name} ${statusChip(data.status)}</h1>
  <p class="page-header__lead">${data.purpose}</p>
  <p><a href="${data.figma}" target="_blank" rel="noopener">Stacked card en Figma ↗</a> · <a href="../../docs/design-system/figma-compose-bindings.md">Bindings Figma ↔ Compose</a></p>
`;

document.getElementById("component-main").innerHTML = `
  <section class="section">
    <h2>Home vacío · Media & text (205:816)</h2>
    <figure class="card-demo card-demo--stacked">
      <div class="card-demo__header">
        <div>
          <strong>Sin viajes todavía</strong>
          <p>Planea tu primera aventura</p>
        </div>
      </div>
      <img src="/assets/home_empty_map.jpg" alt="Mapa con chincheta de viaje" width="360" height="200" loading="lazy" />
    </figure>
    <p class="lead">Imagen bundled en app: <code>res/drawable-nodpi/home_empty_map.jpg</code> → <code>imageRes</code> (sin red).</p>
  </section>

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
    <h2>Properties → Compose</h2>
    <table>
      <thead><tr><th>Property Figma</th><th>Parámetro</th></tr></thead>
      <tbody>
        <tr><td><code>Header text</code></td><td><code>headerText</code></td></tr>
        <tr><td><code>Subhead text</code></td><td><code>subheadText</code></td></tr>
        <tr><td><code>Supporting text</code></td><td><code>supportingText</code></td></tr>
        <tr><td><code>Show secondary action</code></td><td><code>onDismiss</code></td></tr>
        <tr><td><code>Style</code></td><td><code>MotCardStyle</code></td></tr>
      </tbody>
    </table>
  </section>

  <section class="section">
    <h2>Instancias producto (Home)</h2>
    <table>
      <thead><tr><th>Frame</th><th>Composable</th></tr></thead>
      <tbody>
        <tr><td>205:816 vacío</td><td><code>HomeEmptyStackedCard</code></td></tr>
        <tr><td>Wallet promo</td><td><code>WalletPromoCard</code></td></tr>
        <tr><td>Filtro vacío</td><td><code>MotStackedCard(Outlined, …)</code></td></tr>
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
