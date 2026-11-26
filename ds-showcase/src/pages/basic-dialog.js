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
import { renderAnatomy, renderMeasures } from "../lib/anatomy-diagram.js";
import { bootLiveSpecimens, liveHostHtml, setLiveProps } from "../lib/live-specimen.js";
import { initLiveInspectors } from "../lib/live-inspector.js";

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

/** Parámetros de la demo para un escenario. */
const scenarioProps = (s) => ({
  icon: s.icon,
  title: s.title,
  body: s.body,
  confirm: s.confirm,
  dismiss: s.dismiss,
  destructive: s.destructive,
});

const staticDialog = (props, label) =>
  liveHostHtml("basic-dialog/static", { ...props, theme: "light" }, { height: 400, label });

document.getElementById("component-header").innerHTML = renderComponentHeader({
  ...data,
  specimenHtml: staticDialog(scenarioProps(SCENARIOS.delete), "AlertDialog real"),
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
    hint: '<span class="live-badge">Compose real</span> Es el <code>AlertDialog</code> de Material 3 con acciones <code>MOTTextButton</code>, compilado a WebAssembly. Ábrelo, cambia el escenario y ciérralo con una acción o pulsando fuera.',
    canvasHtml: `
      ${liveHostHtml("basic-dialog/playground", { theme: "light" }, { height: 420, label: "AlertDialog real" })}
      <p class="playground__status"><code>AlertDialog(onDismissRequest, icon, title, text, confirmButton, dismissButton)</code></p>`,
    controlsHtml: `
      <label class="control">
        <span>Escenario</span>
        <select id="dialog-scenario">
          <option value="delete">Eliminar (icono · error)</option>
          <option value="save">Confirmar guardado (sin icono)</option>
          <option value="discard">Descartar cambios</option>
        </select>
      </label>
      <label class="playground-check"><input type="checkbox" id="dialog-dark" /><span>Tema oscuro</span></label>
    `,
  }),
  renderGuidelines(data).replace(
    "</section>",
    `${renderDoDontPair({
      doHtml: staticDialog(scenarioProps(SCENARIOS.delete), "Diálogo bien planteado"),
      doCaption: "Pregunta concreta, consecuencia en el cuerpo, dos text buttons, destructivo en error.",
      dontHtml: staticDialog({ title: "¡Atención!", body: "¿Estás seguro?", confirm: "OK", filledconfirm: true }, "Diálogo mal planteado"),
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

const host = document.querySelector('[data-mot-specimen="basic-dialog/playground"]');
const scenarioSel = document.getElementById("dialog-scenario");
const darkBox = document.getElementById("dialog-dark");
function sync() {
  const s = SCENARIOS[scenarioSel.value] || SCENARIOS.delete;
  setLiveProps(host, {
    ...scenarioProps(s),
    theme: darkBox.checked ? "dark" : "light",
  });
}
[scenarioSel, darkBox].forEach((el) => el.addEventListener("change", sync));
sync();
initLiveInspectors();
bootLiveSpecimens();
setupToc();
