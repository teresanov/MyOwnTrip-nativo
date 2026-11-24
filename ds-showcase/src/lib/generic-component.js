import { initShowcasePage, setupToc } from "./bootstrap.js";
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
} from "./component-page.js";
import { renderAnatomy, renderMeasures } from "./anatomy-diagram.js";

/** Dónde aparece en la app. `screens[]` = { screen, uses | pattern | fields }. */
export function renderScreens(screens) {
  if (!screens?.length) return "";
  return (
    sectionOpen("en-la-app", "En la app", {
      lead: "Pantallas Compose que ya usan este control. La ficha vive aquí, no en Figma.",
    }) +
    table(
      ["Pantalla", "Uso"],
      screens.map((s) => [`<code>${s.screen}</code>`, s.uses || s.pattern || s.fields || ""]),
    ) +
    "</section>"
  );
}

/**
 * Monta una ficha usage-first (mismo esqueleto que Button / Filter chip / Basic dialog).
 *
 * Opciones:
 *  - specimenHtml: ejemplar vivo en la cabecera
 *  - playground: { id, title, hint, canvasHtml, controlsHtml }
 *  - doDont: { doHtml, doCaption, dontHtml, dontCaption }
 *  - extraAfterUsage: HTML extra (tablas propias, etc.)
 *  - setup(): eventos del playground
 */
export function mountComponentPage(data, opts = {}) {
  const { playground = null, setup = null, doDont = null, specimenHtml = "", extraAfterUsage = "" } = opts;

  initShowcasePage("components", { componentId: data.id });

  document.getElementById("component-header").innerHTML = renderComponentHeader({
    ...data,
    specimenHtml: specimenHtml || data.specimenHtml || "",
  });

  const guidelines = renderGuidelines(data);
  const guidelinesWithPair = doDont
    ? guidelines.replace("</section>", `${renderDoDontPair(doDont)}</section>`)
    : guidelines;

  document.getElementById("component-main").innerHTML = [
    renderUsage(data),
    extraAfterUsage,
    renderScreens(data.screens),
    playground ? renderPlayground(playground) : "",
    guidelinesWithPair,
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

  if (setup) setup();
  setupToc();
}
