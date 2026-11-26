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
import { renderAnatomy, renderMeasures } from "../lib/anatomy-diagram.js";
import { bootLiveSpecimens, liveHostHtml, setLiveProps } from "../lib/live-specimen.js";
import { initLiveInspectors } from "../lib/live-inspector.js";

initShowcasePage("components", { componentId: "filter-chip" });

document.getElementById("component-header").innerHTML = renderComponentHeader({
  ...data,
  specimenHtml: liveHostHtml("filter-chip/playground", { theme: "light" }, { height: 100, label: "FilterChip reales" }),
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
    hint: '<span class="live-badge">Compose real</span> Son <code>FilterChip</code> de Material 3 con el tema de la app, compilados a WebAssembly. Pulsa un chip: solo hay una opción seleccionada por eje, como en Wallet.',
    canvasHtml: `
      ${liveHostHtml("filter-chip/playground", { enabled: true, theme: "light" }, { height: 140, label: "FilterChip real" })}
      <p class="playground__status"><code>FilterChip(selected, onClick, label = { Text(…) })</code></p>`,
    controlsHtml: `
      <label class="playground-check"><input type="checkbox" id="demo-enabled" checked /><span>Habilitado</span></label>
      <label class="playground-check"><input type="checkbox" id="demo-dark" /><span>Tema oscuro</span></label>`,
  }),
  renderGuidelines(data).replace(
    "</section>",
    `${renderDoDontPair({
      doHtml: liveHostHtml("filter-chip/playground", { theme: "light" }, { height: 100, label: "Filtros reales" }),
      doCaption: "Un conjunto, una selección, copy de categoría.",
      dontHtml: liveHostHtml("filter-chip/single", { label: "Guardar", theme: "light" }, { height: 100, label: "Chip suelto con un verbo" }),
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

const host = document.querySelector('.playground__canvas [data-mot-specimen="filter-chip/playground"]');
const enabledBox = document.getElementById("demo-enabled");
const darkBox = document.getElementById("demo-dark");
const sync = () => setLiveProps(host, { enabled: enabledBox.checked, theme: darkBox.checked ? "dark" : "light" });
[enabledBox, darkBox].forEach((el) => el.addEventListener("change", sync));
sync();
initLiveInspectors();
bootLiveSpecimens();
setupToc();
