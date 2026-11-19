import { initShowcasePage } from "../lib/bootstrap.js";
import data from "../data/components/date-time-pickers.json";
import { mountDecisions } from "../lib/decisions.js";
import {
  renderA11ySection,
  renderAnatomy,
  renderChangelog,
  renderComponentHeader,
  renderComposeSection,
  renderGuidelines,
  renderPlayground,
  renderTokenSection,
  renderVariantTable,
} from "../lib/component-page.js";

initShowcasePage("components", { componentId: "date-time-pickers" });

mountDecisions(data);

document.getElementById("component-header").innerHTML = renderComponentHeader(
  data,
  "Abrir MotDateRangeField en Figma",
);

const figmaBase =
  "https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=";

const productRows = (data.productSets || [])
  .map(
    (row) =>
      `<tr><td><code>${row.compose}</code></td><td><a href="${figmaBase}${row.node}" target="_blank" rel="noopener">${row.figma}</a></td></tr>`,
  )
  .join("");

const archiveRows = (data.archiveSets || [])
  .map((row) => `<tr><td>${row.name}</td><td><code>${row.id}</code></td><td>ARCHIVE · no design-file</td></tr>`)
  .join("");

const examples = (data.productExamples || [])
  .map((e) => `<tr><td><code>${e.screen}</code></td><td>${e.fields}</td></tr>`)
  .join("");

document.getElementById("component-main").innerHTML = `
  ${renderPlayground({
    title: "Playground",
    hint: "Demostración estática del patrón rango: dos entradas + icono calendario. Los modales reales viven en Compose y en los sets Figma PRODUCT.",
    canvasHtml: `
      <div class="demo-stack">
        <p class="demo-label">MotDateRangeField (ilustración)</p>
        <div class="demo-row" style="display:flex;gap:8px;flex-wrap:wrap;align-items:flex-end">
          <label class="mot-field" style="flex:1;min-width:8rem">
            <span class="mot-field__label">Fecha inicio</span>
            <input class="mot-field__input" type="text" value="01/06/2026" readonly />
          </label>
          <label class="mot-field" style="flex:1;min-width:8rem">
            <span class="mot-field__label">Fecha fin</span>
            <input class="mot-field__input" type="text" value="08/06/2026" readonly />
          </label>
          <button type="button" class="btn btn--icon" aria-label="Abrir calendario" title="Abrir calendario">📅</button>
        </div>
      </div>
    `,
  })}

  ${renderAnatomy(data.anatomy)}
  ${renderVariantTable(data.variants, "Patrones PRODUCT")}

  <section class="section">
    <h2 id="figma-compose">Figma ↔ Compose</h2>
    <table>
      <thead><tr><th>Composable</th><th>Figma PRODUCT</th></tr></thead>
      <tbody>${productRows}</tbody>
    </table>
  </section>

  <section class="section">
    <h2 id="archive">ARCHIVE (no instanciar en design-file)</h2>
    <table>
      <thead><tr><th>Set</th><th>Node</th><th>Nota</th></tr></thead>
      <tbody>${archiveRows}</tbody>
    </table>
  </section>

  <section class="section">
    <h2 id="screens">Dónde aparece en la app</h2>
    <table>
      <thead><tr><th>Pantalla</th><th>Composables</th></tr></thead>
      <tbody>${examples}</tbody>
    </table>
  </section>

  ${renderTokenSection(data.tokens)}
  ${renderGuidelines(data)}
  ${renderA11ySection(data.a11y)}
  ${renderComposeSection(data.compose)}
  ${renderChangelog(data.changelog)}
`;