import { initShowcasePage, setupToc } from "../lib/bootstrap.js";
import data from "../data/components/filter-chip.json";
import {
  renderA11y,
  renderBehavior,
  renderComponentHeader,
  renderDecisions,
  renderDoDontPair,
  renderGuidelines,
  renderImplementation,
  renderPlayground,
  renderRelated,
  renderTokens,
  renderUsage,
  renderVariantTable,
  sectionOpen,
  table,
} from "../lib/component-page.js";
import { CHIP_CHECK, renderAnatomy, renderMeasures } from "../lib/anatomy-diagram.js";

initShowcasePage("components", { componentId: "filter-chip" });

function chipButton(label, selected) {
  return `<button type="button" class="filter-chip${
    selected ? " filter-chip--selected" : ""
  }" aria-pressed="${selected}" data-value="${label}">${selected ? CHIP_CHECK : ""}${label}</button>`;
}

document.getElementById("component-header").innerHTML = renderComponentHeader({
  ...data,
  specimenHtml: `
    <div class="filter-chip-row">
      ${chipButton("Activos", true)}
      ${chipButton("Archivados", false)}
      ${chipButton("Todos", false)}
    </div>
  `,
});

const screens = (data.screens || []).map((e) => [`<code>${e.screen}</code>`, e.pattern]);

document.getElementById("component-main").innerHTML = [
  renderUsage(data),
  screens.length
    ? sectionOpen("en-la-app", "En la app", {
        lead: "Pantallas Compose que ya usan FilterChip. Esta tabla es la ficha; no hace falta abrir Figma para saber dónde va.",
      }) +
      table(["Pantalla", "Patrón"], screens) +
      "</section>"
    : "",
  renderPlayground({
    id: "ejemplo",
    title: "Ejemplo",
    hint: "Pulsa un chip para cambiar el filtro. En Wallet la fila hace scroll horizontal y solo hay una opción seleccionada por eje.",
    canvasHtml: `
      <div class="filter-chip-row filter-chip-demo" role="group" aria-label="Filtro de documentos">
        ${chipButton("Activos", true)}
        ${chipButton("Archivados", false)}
        ${chipButton("Todos", false)}
      </div>
      <p class="playground__status" id="filter-chip-status" aria-live="polite">Filtro activo: Activos</p>
    `,
  }),
  renderGuidelines(data).replace(
    "</section>",
    `${renderDoDontPair({
      doHtml: `<div class="filter-chip-row">${chipButton("Activos", true)}${chipButton(
        "Archivados",
        false,
      )}${chipButton("Todos", false)}</div>`,
      doCaption: "Un conjunto, una selección, copy de categoría.",
      dontHtml: `<button type="button" class="filter-chip filter-chip--selected">${CHIP_CHECK}Guardar</button>`,
      dontCaption: "Un chip suelto con un verbo de acción. Eso es un botón.",
    })}</section>`,
  ),
  renderVariantTable(data.variants, "Variantes"),
  renderAnatomy(data.anatomy, data.anatomyDiagram),
  renderMeasures(data.anatomyDiagram),
  renderBehavior(data.behavior),
  renderA11y(data.a11y),
  renderTokens(data.tokens),
  renderImplementation(data.implementation),
  renderDecisions(data),
  renderRelated(data.usage?.related),
].join("");

const chips = document.querySelectorAll(".filter-chip-demo .filter-chip");
const statusEl = document.getElementById("filter-chip-status");

chips.forEach((chip) => {
  chip.addEventListener("click", () => {
    chips.forEach((other) => {
      other.classList.remove("filter-chip--selected");
      other.setAttribute("aria-pressed", "false");
      other.innerHTML = other.dataset.value;
    });
    chip.classList.add("filter-chip--selected");
    chip.setAttribute("aria-pressed", "true");
    chip.innerHTML = CHIP_CHECK + chip.dataset.value;
    statusEl.textContent = `Filtro activo: ${chip.dataset.value}`;
  });
});

setupToc();
