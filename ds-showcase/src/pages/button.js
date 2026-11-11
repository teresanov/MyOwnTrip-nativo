import { initShowcasePage, setupToc } from "../lib/bootstrap.js";
import data from "../data/components/button.json";
import {
  renderA11y,
  renderAnatomy,
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
} from "../lib/component-page.js";

initShowcasePage("components", { componentId: "button" });

const VARIANT_CLASS = {
  filled: "mot-btn--filled",
  tonal: "mot-btn--tonal",
  outlined: "mot-btn--outlined",
  text: "mot-btn--text",
};

const FORCED_STATES = [
  { id: "none", label: "Interactivo" },
  { id: "hover", label: "Hover" },
  { id: "focus", label: "Focus" },
  { id: "pressed", label: "Pressed" },
];

const playgroundCanvas = `
  <div class="btn-stage">
    <button type="button" class="mot-btn mot-btn--filled" id="demo-btn">Guardar gasto</button>
  </div>
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
    <span>Estado forzado</span>
    <select id="demo-state">
      ${FORCED_STATES.map((s) => `<option value="${s.id}">${s.label}</option>`).join("")}
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
    <input type="checkbox" id="demo-reduce" />
    <span>Reducir movimiento</span>
  </label>
`;

document.getElementById("component-header").innerHTML = renderComponentHeader(data);

document.getElementById("component-main").innerHTML = [
  renderUsage(data),
  renderPlayground({
    id: "ejemplo",
    title: "Ejemplo",
    hint:
      "Pulsa y mantén para ver el morph: el radio pasa de 0 a 20 dp y el botón se comprime a 0.96. Es una reproducción en CSS de los tiempos reales de <code>AppMotion</code>; el muelle de salida se aproxima con una curva, porque CSS no tiene muelles.",
    canvasHtml: playgroundCanvas,
    controlsHtml: playgroundControls,
  }),
  renderGuidelines(data),
  renderVariantTable(data.variants),
  renderAnatomy(data.anatomy),
  renderBehavior(data.behavior),
  renderA11y(data.a11y),
  renderTokens(data.tokens),
  renderImplementation(data.implementation),
  renderDecisions(data),
  renderRelated(data.usage?.related),
].join("");

/* ---------- Playground ---------- */

const btn = document.getElementById("demo-btn");
const status = document.getElementById("demo-status");
const variantSel = document.getElementById("demo-variant");
const stateSel = document.getElementById("demo-state");
const labelInput = document.getElementById("demo-label");
const enabledBox = document.getElementById("demo-enabled");
const reduceBox = document.getElementById("demo-reduce");

function sync() {
  const variant = variantSel.value;
  const forced = stateSel.value;

  btn.className = `mot-btn ${VARIANT_CLASS[variant]}`;
  if (forced !== "none") btn.classList.add(`is-${forced}`);
  if (reduceBox.checked) btn.classList.add("is-reduced");

  btn.textContent = labelInput.value || "Etiqueta";
  btn.disabled = !enabledBox.checked;

  const composable = data.variants.find((v) => v.id === variant)?.composable;
  const padding = variant === "tonal" ? "MOTSpacing.ButtonXSmall (12 × 6)" : "MOTSpacing.ButtonSmall (24 × 8)";
  status.innerHTML = enabledBox.checked
    ? `<code>${composable}</code> · padding <code>${padding}</code>${
        reduceBox.checked ? " · <code>LocalReduceMotion = true</code>: el cambio es instantáneo" : ""
      }`
    : `<code>${composable}</code> · disabled: 38 % contenido y 12 % contenedor, sin cambio de color ni de forma`;
}

[variantSel, stateSel, labelInput, enabledBox, reduceBox].forEach((el) => {
  el.addEventListener("input", sync);
  el.addEventListener("change", sync);
});

sync();
setupToc();
