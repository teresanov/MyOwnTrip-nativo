import data from "../data/components/snackbar.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { liveHostHtml, setLiveProps } from "../lib/live-specimen.js";
import { snackbarHtml } from "../lib/specimens.js";

mountComponentPage(data, {
  specimenHtml: snackbarHtml(),
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint: '<span class="live-badge">Compose real</span> Es <code>SnackbarHost</code> de Material 3 con el tema de la app, compilado a WebAssembly. Pulsa «Guardar gasto»: el snackbar entra desde abajo, se oculta solo y la acción lo cierra.',
    canvasHtml: `
      ${liveHostHtml("snackbar/playground", { action: true, close: false, theme: "light" }, { height: 220, label: "Snackbar real" })}
      <p class="playground__status"><code>snackbarHostState.showSnackbar(message, actionLabel, withDismissAction)</code></p>`,
    controlsHtml: `
      <label class="playground-check"><input type="checkbox" id="snackbar-with-action" checked /><span>Acción «Deshacer»</span></label>
      <label class="playground-check"><input type="checkbox" id="snackbar-with-close" /><span>Botón de cerrar</span></label>
      <label class="playground-check"><input type="checkbox" id="snackbar-dark" /><span>Tema oscuro</span></label>`,
  },
  doDont: {
    doHtml: snackbarHtml(),
    doCaption: "Ack breve, una acción de texto, inverseSurface.",
    dontHtml: `<div class="snackbar-demo"><span>Error crítico: no se pudo borrar</span><button type="button" class="mot-btn mot-btn--filled" tabindex="-1">OK</button></div>`,
    dontCaption: "Un error que exige decisión es un diálogo, no un snackbar con Filled.",
  },
  setup() {
    const host = document.querySelector('[data-mot-specimen="snackbar/playground"]');
    const action = document.getElementById("snackbar-with-action");
    const close = document.getElementById("snackbar-with-close");
    const dark = document.getElementById("snackbar-dark");
    const sync = () =>
      setLiveProps(host, { action: action.checked, close: close.checked, theme: dark.checked ? "dark" : "light" });
    [action, close, dark].forEach((el) => el.addEventListener("change", sync));
    sync();
  },
});
