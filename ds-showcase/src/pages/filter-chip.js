import { initShowcasePage } from "../lib/bootstrap.js";
import data from "../data/components/filter-chip.json";
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

initShowcasePage("components", { componentId: "filter-chip" });

document.getElementById("component-header").innerHTML = renderComponentHeader(
  data,
  "Abrir Filter chip en Figma",
);

const examples = (data.productExamples || [])
  .map((e) => `<tr><td><code>${e.screen}</code></td><td>${e.pattern}</td></tr>`)
  .join("");

document.getElementById("component-main").innerHTML = `
  ${renderPlayground({
    title: "Playground",
    hint: "Pulsa un chip para cambiar el filtro activo. En la app, la fila es scroll horizontal y solo puede haber una opción seleccionada por eje.",
    canvasHtml: `
      <div class="filter-chip-row filter-chip-demo" role="group" aria-label="Filtro de documentos">
        <button type="button" class="filter-chip filter-chip--selected" aria-pressed="true" data-value="Activos">Activos</button>
        <button type="button" class="filter-chip" aria-pressed="false" data-value="Archivados">Archivados</button>
        <button type="button" class="filter-chip" aria-pressed="false" data-value="Todos">Todos</button>
      </div>
      <p class="playground__status" id="filter-chip-status" aria-live="polite">Filtro activo: Activos</p>
    `,
  })}

  ${renderAnatomy(data.anatomy)}
  ${renderVariantTable(data.variants)}

  <section class="section">
    <h2>Dónde aparece en la app</h2>
    <p class="section-lead">Referencia rápida a pantallas que ya consumen este patrón en Compose.</p>
    <table>
      <thead><tr><th>Pantalla</th><th>Patrón</th></tr></thead>
      <tbody>${examples}</tbody>
    </table>
  </section>

  ${renderTokenSection(data.tokens)}
  ${renderGuidelines(data)}
  ${renderA11ySection(data.a11y)}
  ${renderComposeSection(data.compose)}
  ${renderChangelog(data.changelog)}
`;

const chips = document.querySelectorAll(".filter-chip-demo .filter-chip");
const statusEl = document.getElementById("filter-chip-status");

chips.forEach((chip) => {
  chip.addEventListener("click", () => {
    chips.forEach((other) => {
      other.classList.remove("filter-chip--selected");
      other.setAttribute("aria-pressed", "false");
    });
    chip.classList.add("filter-chip--selected");
    chip.setAttribute("aria-pressed", "true");
    statusEl.textContent = `Filtro activo: ${chip.dataset.value}`;
  });
});
