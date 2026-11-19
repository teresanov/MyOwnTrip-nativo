import { initShowcasePage, setupToc } from "./bootstrap.js";
import {
  renderA11y,
  renderBehavior,
  renderComponentHeader,
  renderDecisions,
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

const FIGMA_BASE =
  "https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=";

const figmaHref = (node) => `${FIGMA_BASE}${String(node).replace(":", "-")}`;

/**
 * Tabla Figma ↔ Compose: un set publicado en la librería y el composable que lo implementa.
 * `sets[]` = { figma, node, compose, note }.
 */
export function renderFigmaMap(sets) {
  if (!sets?.length) return "";
  return (
    sectionOpen("figma-compose", "Figma ↔ Compose", {
      lead: "Cada set publicado en la librería de Figma, y el composable que lo implementa. Fuente: <code>docs/design-system/figma-compose-bindings.md</code>.",
    }) +
    table(
      ["Set en Figma", "Composable", "Nota"],
      sets.map((s) => [
        s.node ? `<a href="${figmaHref(s.node)}" target="_blank" rel="noopener">${s.figma}</a>` : s.figma,
        s.compose ? `<code>${s.compose}</code>` : "—",
        s.note || "",
      ])
    ) +
    "</section>"
  );
}

/** Dónde aparece en la app. `screens[]` = { screen, uses }. */
export function renderScreens(screens) {
  if (!screens?.length) return "";
  return (
    sectionOpen("pantallas", "Dónde aparece en la app", {
      lead: "Pantallas que consumen el componente. Si una pantalla necesita algo distinto, se cambia el componente, no la pantalla.",
    }) +
    table(
      ["Pantalla", "Uso"],
      screens.map((s) => [`<code>${s.screen}</code>`, s.uses])
    ) +
    "</section>"
  );
}

/**
 * Monta una ficha completa desde su JSON (esquema de button.json).
 * Opciones:
 *  - playground: { id, title, hint, canvasHtml, controlsHtml }  (ejemplo interactivo)
 *  - setup(): se ejecuta tras pintar el HTML (para enganchar eventos del playground)
 */
export function mountComponentPage(data, { playground = null, setup = null, figmaLinkLabel } = {}) {
  initShowcasePage("components", { componentId: data.id });

  document.getElementById("component-header").innerHTML = renderComponentHeader(data, figmaLinkLabel);

  document.getElementById("component-main").innerHTML = [
    renderUsage(data),
    playground ? renderPlayground(playground) : "",
    renderGuidelines(data),
    renderVariantTable(data.variants),
    renderFigmaMap(data.sets),
    renderAnatomy(data.anatomy, data.anatomyDiagram),
    renderMeasures(data.anatomyDiagram),
    renderBehavior(data.behavior),
    renderA11y(data.a11y),
    renderTokens(data.tokens),
    renderScreens(data.screens),
    renderImplementation(data.implementation),
    renderDecisions(data),
    renderRelated(data.usage?.related),
  ].join("");

  if (setup) setup();
  setupToc();
}
