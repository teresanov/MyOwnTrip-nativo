import { initShowcasePage } from "../lib/bootstrap.js";
import data from "../data/components/basic-dialog.json";
import {
  renderA11ySection,
  renderChangelog,
  renderComponentHeader,
  renderComposeSection,
  renderGuidelines,
  renderPlayground,
  renderTokenSection,
  renderVariantTable,
} from "../lib/component-page.js";

initShowcasePage("components", { componentId: "basic-dialog" });

document.getElementById("component-header").innerHTML = renderComponentHeader(
  data,
  "Abrir Basic dialog en Figma",
);

const caps = (data.walletCaps || [])
  .map(
    (c) =>
      `<tr><td>${c.cap}</td><td><code>${c.figma}</code></td><td>${c.icon ? "Sí" : "No"}</td><td>${c.ctas}</td></tr>`,
  )
  .join("");

document.getElementById("component-main").innerHTML = `
  ${renderPlayground({
    title: "Playground",
    hint: "Abre el diálogo de eliminación, prueba Cancelar o Eliminar y cierra con Escape o clic fuera del panel.",
    canvasHtml: `
      <button type="button" class="btn btn--filled" id="dialog-open">Mostrar diálogo de ejemplo</button>
    `,
  })}

  <div class="dialog-overlay" id="dialog-overlay" hidden>
    <div class="dialog-demo" role="alertdialog" aria-modal="true" aria-labelledby="dialog-demo-title" aria-describedby="dialog-demo-body">
      <div class="dialog-demo__icon" aria-hidden="true">!</div>
      <h3 class="dialog-demo__title" id="dialog-demo-title">¿Eliminar documento?</h3>
      <p class="dialog-demo__body" id="dialog-demo-body">Se borrará el documento y su archivo del dispositivo. Esta acción no se puede deshacer.</p>
      <div class="dialog-demo__actions">
        <button type="button" class="btn btn--text" id="dialog-cancel">Cancelar</button>
        <button type="button" class="btn btn--text dialog-demo__destructive" id="dialog-confirm">Eliminar</button>
      </div>
    </div>
  </div>

  <section class="section">
    <h2>Wallet en Figma</h2>
    <p class="section-lead">Capturas del design-file que ya instancian este set (no sustituyen la preview de Compose).</p>
    <table>
      <thead><tr><th>Cap</th><th>Node</th><th>Icono</th><th>Acciones</th></tr></thead>
      <tbody>${caps}</tbody>
    </table>
  </section>

  ${renderVariantTable(data.variants)}
  ${renderTokenSection(data.tokens)}
  ${renderGuidelines(data)}
  ${renderA11ySection(data.a11y)}
  ${renderComposeSection(data.compose)}
  ${renderChangelog(data.changelog)}
`;

const overlay = document.getElementById("dialog-overlay");
const openBtn = document.getElementById("dialog-open");
const cancelBtn = document.getElementById("dialog-cancel");
const confirmBtn = document.getElementById("dialog-confirm");
let lastFocus = null;

function openDialog() {
  lastFocus = document.activeElement;
  overlay.hidden = false;
  cancelBtn.focus();
}

function closeDialog(returnFocus = true) {
  overlay.hidden = true;
  if (returnFocus && lastFocus instanceof HTMLElement) lastFocus.focus();
}

openBtn.addEventListener("click", openDialog);
cancelBtn.addEventListener("click", () => closeDialog());
confirmBtn.addEventListener("click", () => closeDialog());

overlay.addEventListener("click", (e) => {
  if (e.target === overlay) closeDialog();
});

document.addEventListener("keydown", (e) => {
  if (!overlay.hidden && e.key === "Escape") {
    e.preventDefault();
    closeDialog();
  }
});
