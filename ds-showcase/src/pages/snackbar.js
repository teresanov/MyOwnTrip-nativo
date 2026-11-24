import data from "../data/components/snackbar.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { snackbarHtml } from "../lib/specimens.js";

mountComponentPage(data, {
  specimenHtml: snackbarHtml(),
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint: "Simula el feedback tras guardar un gasto. El mensaje aparece abajo y se oculta solo.",
    canvasHtml: `<button type="button" class="mot-btn mot-btn--filled" id="snackbar-trigger">Simular «Gasto guardado»</button>`,
    controlsHtml: `
      <label class="playground-check">
        <input type="checkbox" id="snackbar-with-action" checked />
        <span>Incluir acción «Deshacer»</span>
      </label>`,
  },
  extraAfterUsage: `<div class="snackbar-host" id="snackbar-host" hidden aria-live="polite"></div>`,
  doDont: {
    doHtml: snackbarHtml(),
    doCaption: "Ack breve, una acción de texto, inverseSurface.",
    dontHtml: `<div class="snackbar-demo"><span>Error crítico: no se pudo borrar</span><button type="button" class="mot-btn mot-btn--filled" tabindex="-1">OK</button></div>`,
    dontCaption: "Un error que exige decisión es un diálogo, no un snackbar con Filled.",
  },
  setup() {
    const host = document.getElementById("snackbar-host");
    const trigger = document.getElementById("snackbar-trigger");
    const withAction = document.getElementById("snackbar-with-action");
    if (!host || !trigger) return;
    let hideTimer = null;
    function hideSnackbar() {
      host.hidden = true;
      host.innerHTML = "";
      if (hideTimer) clearTimeout(hideTimer);
      hideTimer = null;
    }
    function showSnackbar() {
      hideSnackbar();
      host.innerHTML = snackbarHtml({
        action: withAction.checked ? "Deshacer" : "",
      }).replace('tabindex="-1"', 'id="snackbar-undo"').replace('aria-label="Cerrar"', 'id="snackbar-close" aria-label="Cerrar"');
      host.hidden = false;
      document.getElementById("snackbar-close")?.addEventListener("click", hideSnackbar);
      document.getElementById("snackbar-undo")?.addEventListener("click", () => {
        hideSnackbar();
        trigger.focus();
      });
      hideTimer = setTimeout(hideSnackbar, 4500);
    }
    trigger.addEventListener("click", showSnackbar);
  },
});
