import data from "../data/components/icon-button.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { liveHostHtml, setLiveProps } from "../lib/live-specimen.js";
import { iconButtonHtml } from "../lib/specimens.js";

const SIZES = ["XSmall", "Small", "Medium", "Large"];
const WIDTHS = ["Narrow", "Default", "Wide"];
const TYPES = ["Square", "Round"];
const STYLES = [
  ["standard", "Standard (app)"],
  ["filled", "Filled"],
  ["tonal", "Tonal"],
];

const select = (id, label, values, selected) => `
  <label class="control">
    <span>${label}</span>
    <select id="${id}">${values.map((v) => `<option${v === selected ? " selected" : ""}>${v}</option>`).join("")}</select>
  </label>`;

mountComponentPage(data, {
  specimenHtml: iconButtonHtml("Editar"),
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint:
      '<span class="live-badge">Compose real</span> Es <code>MOTIconButton</code> de la app, compilado a WebAssembly. En la app se usa el estilo Standard, sin contenedor: solo se ve el icono hasta que pasas el cursor, enfocas o pulsas. Cambia a Filled o Tonal para ver el contenedor del set de Figma. En Round, el radio de 20 dp ya está en reposo.',
    canvasHtml: `
      ${liveHostHtml(
        "icon-button/playground",
        { size: "Small", width: "Default", type: "Square", style: "standard", enabled: true, theme: "light", reduce: false },
        { label: "MOTIconButton real" }
      )}
      <p class="playground__status" id="demo-status"></p>`,
    controlsHtml: `
      <label class="control">
        <span>Estilo</span>
        <select id="demo-style">${STYLES.map(([v, l]) => `<option value="${v}">${l}</option>`).join("")}</select>
      </label>
      ${select("demo-size", "Tamaño", SIZES, "Small")}
      ${select("demo-width", "Ancho", WIDTHS, "Default")}
      ${select("demo-type", "Tipo", TYPES, "Square")}
      <label class="playground-check"><input type="checkbox" id="demo-enabled" checked /><span>Habilitado</span></label>
      <label class="playground-check"><input type="checkbox" id="demo-reduce" /><span>Reducir movimiento</span></label>
      <label class="playground-check"><input type="checkbox" id="demo-dark" /><span>Tema oscuro</span></label>`,
  },
  doDont: {
    doHtml: iconButtonHtml("Editar"),
    doCaption: "Icono inequívoco y nombre accesible. Standard, sin contenedor.",
    dontHtml: `<button type="button" class="fake-btn" aria-hidden="true">★</button>`,
    dontCaption: "Un símbolo sin nombre ni token. No es MOTIconButton.",
  },
  setup() {
    const host = document.querySelector('[data-mot-specimen="icon-button/playground"]');
    const status = document.getElementById("demo-status");
    const [style, size, width, type, enabled, reduce, dark] = [
      "demo-style",
      "demo-size",
      "demo-width",
      "demo-type",
      "demo-enabled",
      "demo-reduce",
      "demo-dark",
    ].map((id) => document.getElementById(id));

    const sync = () => {
      setLiveProps(host, {
        style: style.value,
        size: size.value,
        width: width.value,
        type: type.value,
        enabled: enabled.checked,
        reduce: reduce.checked,
        theme: dark.checked ? "dark" : "light",
      });
      const colors = {
        filled: ", colors = IconButtonDefaults.filledIconButtonColors()",
        tonal: ", colors = IconButtonDefaults.filledTonalIconButtonColors()",
      }[style.value] || "";
      status.innerHTML = `<code>MOTIconButton(size = ${size.value}, width = ${width.value}, type = ${type.value}${colors}${
        enabled.checked ? "" : ", enabled = false"
      })</code>${style.value === "standard" ? " · Standard: sin contenedor hasta que interactúas" : ""}`;
    };

    [style, size, width, type, enabled, reduce, dark].forEach((el) => {
      el.addEventListener("change", sync);
      el.addEventListener("input", sync);
    });
    sync();
  },
});
