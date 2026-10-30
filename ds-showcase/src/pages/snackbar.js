import { initShowcasePage } from "../lib/bootstrap.js";
import data from "../data/components/snackbar.json";
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

initShowcasePage("components", { componentId: "snackbar" });

document.getElementById("component-header").innerHTML = renderComponentHeader(
  data,
  "Abrir Snackbar en Figma",
);

document.getElementById("component-main").innerHTML = `
  ${renderPlayground({
    title: "Playground",
    hint: "Simula el feedback tras guardar un gasto. El mensaje aparece abajo, se puede deshacer y se oculta solo al cabo de unos segundos.",
    canvasHtml: `
      <button type="button" class="btn btn--filled" id="snackbar-trigger">Simular «Gasto guardado»</button>
    `,
    controlsHtml: `
      <label class="playground-check">
        <input type="checkbox" id="snackbar-with-action" checked />
        Incluir acción «Deshacer»
      </label>
    `,
  })}

  <div class="snackbar-host" id="snackbar-host" hidden aria-live="polite"></div>

  ${renderAnatomy(data.anatomy)}
  ${renderVariantTable(data.variants, "Variantes del set")}
  ${renderTokenSection(data.tokens)}
  ${renderGuidelines(data)}
  ${renderA11ySection(data.a11y)}
  ${renderComposeSection(data.compose)}
  ${renderChangelog(data.changelog)}
`;

const host = document.getElementById("snackbar-host");
const trigger = document.getElementById("snackbar-trigger");
const withAction = document.getElementById("snackbar-with-action");
let hideTimer = null;

function hideSnackbar() {
  host.hidden = true;
  host.innerHTML = "";
  if (hideTimer) {
    clearTimeout(hideTimer);
    hideTimer = null;
  }
}

function showSnackbar() {
  hideSnackbar();
  const actionHtml = withAction.checked
    ? `<button type="button" class="snackbar-demo__action" id="snackbar-undo">Deshacer</button>`
    : "";
  host.innerHTML = `
    <div class="snackbar-demo">
      <span class="snackbar-demo__text">Gasto guardado</span>
      ${actionHtml}
      <button type="button" class="snackbar-demo__close" id="snackbar-close" aria-label="Cerrar">×</button>
    </div>
  `;
  host.hidden = false;

  document.getElementById("snackbar-close")?.addEventListener("click", hideSnackbar);
  document.getElementById("snackbar-undo")?.addEventListener("click", () => {
    hideSnackbar();
    trigger.focus();
  });

  hideTimer = setTimeout(hideSnackbar, 4500);
}

trigger.addEventListener("click", showSnackbar);
