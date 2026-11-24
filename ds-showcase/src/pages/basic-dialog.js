import { initShowcasePage, setupToc } from "../lib/bootstrap.js";
import data from "../data/components/basic-dialog.json";
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
import { DIALOG_ERROR, renderAnatomy, renderMeasures } from "../lib/anatomy-diagram.js";

initShowcasePage("components", { componentId: "basic-dialog" });

const SCENARIOS = {
  delete: {
    icon: true,
    title: "¿Eliminar documento?",
    body: "Se borrará «Billete tren» y su archivo del dispositivo. Esta acción no se puede deshacer.",
    dismiss: "Cancelar",
    confirm: "Eliminar",
    destructive: true,
  },
  save: {
    icon: false,
    title: "Confirmar guardado",
    body: "Tipo: Transporte · Título: Billete tren. El archivo queda en el teléfono, disponible sin conexión.",
    dismiss: "Cancelar",
    confirm: "Guardar",
    destructive: false,
  },
  discard: {
    icon: true,
    title: "¿Descartar cambios?",
    body: "Si sales ahora se pierde lo que no hayas guardado.",
    dismiss: "Seguir editando",
    confirm: "Descartar",
    destructive: true,
  },
};

function dialogPanel({ icon, title, body, dismiss, confirm, destructive }, ids = {}) {
  const titleId = ids.title || "";
  const bodyId = ids.body || "";
  return `
    ${icon ? `<div class="dialog-demo__icon">${DIALOG_ERROR}</div>` : ""}
    <h3 class="dialog-demo__title"${titleId ? ` id="${titleId}"` : ""}>${title}</h3>
    <p class="dialog-demo__body"${bodyId ? ` id="${bodyId}"` : ""}>${body}</p>
    <div class="dialog-demo__actions">
      <button type="button" class="mot-btn mot-btn--text" ${ids.cancel ? `id="${ids.cancel}"` : "tabindex=\"-1\""}>${dismiss}</button>
      <button type="button" class="mot-btn mot-btn--text${
        destructive ? " dialog-demo__destructive" : ""
      }" ${ids.confirm ? `id="${ids.confirm}"` : "tabindex=\"-1\""}>${confirm}</button>
    </div>
  `;
}

document.getElementById("component-header").innerHTML = renderComponentHeader({
  ...data,
  specimenHtml: `<div class="dialog-demo dialog-demo--specimen">${dialogPanel(SCENARIOS.delete)}</div>`,
});

const screens = (data.screens || []).map((e) => [`<code>${e.screen}</code>`, e.pattern]);

document.getElementById("component-main").innerHTML = [
  renderUsage(data),
  screens.length
    ? sectionOpen("en-la-app", "En la app", {
        lead: "Composables reales. El diálogo se documenta aquí; las caps de Figma son espejo de pantalla, no la ficha.",
      }) +
      table(["Composable", "Patrón"], screens) +
      "</section>"
    : "",
  renderPlayground({
    id: "ejemplo",
    title: "Ejemplo",
    hint: "Abre el diálogo, cambia el escenario y cierra con Cancelar, el scrim o Escape. Las acciones son MOTTextButton, como en Compose.",
    canvasHtml: `<button type="button" class="mot-btn mot-btn--filled" id="dialog-open">Mostrar diálogo</button>`,
    controlsHtml: `
      <label class="control">
        <span>Escenario</span>
        <select id="dialog-scenario">
          <option value="delete">Eliminar (icono · error)</option>
          <option value="save">Confirmar guardado (sin icono)</option>
          <option value="discard">Descartar cambios</option>
        </select>
      </label>
    `,
  }),
  `<div class="dialog-overlay" id="dialog-overlay" hidden>
    <div class="dialog-demo" role="alertdialog" aria-modal="true" aria-labelledby="dialog-demo-title" aria-describedby="dialog-demo-body" id="dialog-panel">
      ${dialogPanel(SCENARIOS.delete, {
        title: "dialog-demo-title",
        body: "dialog-demo-body",
        cancel: "dialog-cancel",
        confirm: "dialog-confirm",
      })}
    </div>
  </div>`,
  renderGuidelines(data).replace(
    "</section>",
    `${renderDoDontPair({
      doHtml: `<div class="dialog-demo dialog-demo--specimen">${dialogPanel(SCENARIOS.delete)}</div>`,
      doCaption: "Pregunta concreta, consecuencia en el cuerpo, dos text buttons, destructivo en error.",
      dontHtml: `<div class="dialog-demo dialog-demo--specimen">
        <h3 class="dialog-demo__title">¡Atención!</h3>
        <p class="dialog-demo__body">¿Estás seguro?</p>
        <div class="dialog-demo__actions">
          <button type="button" class="mot-btn mot-btn--filled" tabindex="-1">OK</button>
        </div>
      </div>`,
      dontCaption: "Alarma, pregunta vacía y un filled que no es MOTTextButton.",
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

const overlay = document.getElementById("dialog-overlay");
const panel = document.getElementById("dialog-panel");
const openBtn = document.getElementById("dialog-open");
const scenarioSel = document.getElementById("dialog-scenario");
let lastFocus = null;

function paintScenario() {
  const s = SCENARIOS[scenarioSel.value] || SCENARIOS.delete;
  panel.innerHTML = dialogPanel(s, {
    title: "dialog-demo-title",
    body: "dialog-demo-body",
    cancel: "dialog-cancel",
    confirm: "dialog-confirm",
  });
  bindActions();
}

function bindActions() {
  document.getElementById("dialog-cancel")?.addEventListener("click", () => closeDialog());
  document.getElementById("dialog-confirm")?.addEventListener("click", () => closeDialog());
}

function openDialog() {
  paintScenario();
  lastFocus = document.activeElement;
  overlay.hidden = false;
  document.getElementById("dialog-cancel")?.focus();
}

function closeDialog(returnFocus = true) {
  overlay.hidden = true;
  if (returnFocus && lastFocus instanceof HTMLElement) lastFocus.focus();
}

openBtn.addEventListener("click", openDialog);
scenarioSel.addEventListener("change", () => {
  if (!overlay.hidden) paintScenario();
});
overlay.addEventListener("click", (e) => {
  if (e.target === overlay) closeDialog();
});
document.addEventListener("keydown", (e) => {
  if (!overlay.hidden && e.key === "Escape") {
    e.preventDefault();
    closeDialog();
  }
});
bindActions();
setupToc();
