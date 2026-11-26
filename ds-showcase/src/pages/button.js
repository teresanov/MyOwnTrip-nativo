import { initShowcasePage, setupToc } from "../lib/bootstrap.js";
import data from "../data/components/button.json";
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
} from "../lib/component-page.js";
import { renderAnatomy, renderMeasures } from "../lib/anatomy-diagram.js";
import { bootLiveSpecimens, liveHostHtml, setLiveProps } from "../lib/live-specimen.js";
import { initLiveInspectors } from "../lib/live-inspector.js";

initShowcasePage("components", { componentId: "button" });

const playgroundCanvas = `
  ${liveHostHtml("button/playground", { variant: "filled", label: "Guardar gasto", enabled: true, reduce: false, theme: "light", icon: false }, { label: "MOTButton real" })}
  <p class="playground__status" id="demo-status"></p>
`;

const playgroundControls = `
  <label class="control">
    <span>Variante</span>
    <select id="demo-variant">
      ${data.variants.map((v) => `<option value="${v.id}">${v.label} · ${v.composable}</option>`).join("")}
    </select>
  </label>
  <label class="control">
    <span>Etiqueta</span>
    <input type="text" id="demo-label" value="Guardar gasto" />
  </label>
  <label class="playground-check">
    <input type="checkbox" id="demo-enabled" checked />
    <span>Habilitado</span>
  </label>
  <label class="playground-check">
    <input type="checkbox" id="demo-icon" />
    <span>Icono</span>
  </label>
  <label class="playground-check">
    <input type="checkbox" id="demo-reduce" />
    <span>Reducir movimiento</span>
  </label>
  <label class="playground-check">
    <input type="checkbox" id="demo-dark" />
    <span>Tema oscuro</span>
  </label>
`;

document.getElementById("component-header").innerHTML = renderComponentHeader({
  ...data,
  specimenHtml: liveHostHtml("button/set", { theme: "light" }, { height: 110, label: "MOTButton reales" }),
});

document.getElementById("component-main").innerHTML = [
  renderUsage(data),
  renderPlayground({
    id: "ejemplo",
    title: "Ejemplo",
    hint:
      '<span class="live-badge">Compose real</span> Es <code>MOTButton</code> de la app, compilado a WebAssembly: el morph de 0 a 20 dp, la compresión a 0.96, el muelle de salida y las state layers son los del código, no una imitación. Pasa el cursor, enfoca con el tabulador o pulsa y mantén.',
    canvasHtml: playgroundCanvas,
    controlsHtml: playgroundControls,
  }),
  renderGuidelines(data).replace(
    "</section>",
    `${renderDoDontPair({
      doHtml: liveHostHtml("button/set", { set: "do", theme: "light" }, { height: 110, label: "Filled y Outlined reales" }),
      doCaption: "La jerarquía la marca la variante (Filled / Outlined), no un color inventado.",
      dontHtml: `<button type="button" class="fake-btn">Guardar</button>`,
      dontCaption: "Un botón a mano con hex y radio de píldora. No es el sistema.",
    })}</section>`
  ),
  renderVariantTable(data.variants),
  renderAnatomy(data.anatomy, data.anatomyDiagram),
  renderMeasures(data.anatomyDiagram),
  renderBehavior(data.behavior),
  renderA11y(data.a11y),
  renderTokens(data.tokens),
  renderImplementation(data.implementation),
  renderDecisions(data),
  renderRelated(data.usage?.related),
].join("");

/* ---------- Playground (demo viva) ---------- */

const host = document.querySelector('[data-mot-specimen="button/playground"]');
const status = document.getElementById("demo-status");
const variantSel = document.getElementById("demo-variant");
const labelInput = document.getElementById("demo-label");
const enabledBox = document.getElementById("demo-enabled");
const iconBox = document.getElementById("demo-icon");
const reduceBox = document.getElementById("demo-reduce");
const darkBox = document.getElementById("demo-dark");

function sync() {
  const variant = variantSel.value;
  setLiveProps(host, {
    variant,
    label: labelInput.value || "Etiqueta",
    enabled: enabledBox.checked,
    icon: iconBox.checked,
    reduce: reduceBox.checked,
    theme: darkBox.checked ? "dark" : "light",
  });

  const composable = data.variants.find((v) => v.id === variant)?.composable;
  const padding = variant === "tonal" || variant === "text" ? "MOTButtonSize.XSmall" : "MOTButtonSize.Small";
  status.innerHTML = enabledBox.checked
    ? `<code>${composable}</code> · tamaño por defecto <code>${padding}</code>${
        reduceBox.checked ? " · <code>LocalReduceMotion = true</code>: el cambio de forma es instantáneo" : ""
      }`
    : `<code>${composable}(enabled = false)</code> · sin cambio de forma; contenido y contenedor con opacidad`;
}

[variantSel, labelInput, enabledBox, iconBox, reduceBox, darkBox].forEach((el) => {
  el.addEventListener("input", sync);
  el.addEventListener("change", sync);
});

sync();
initLiveInspectors();
bootLiveSpecimens();
setupToc();
