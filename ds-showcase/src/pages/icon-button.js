import data from "../data/components/icon-button.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { ICON_EDIT, iconButtonHtml } from "../lib/specimens.js";

mountComponentPage(data, {
  specimenHtml: iconButtonHtml("Editar"),
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint: "Pulsa: el radio pasa de 0 a 20 dp (mismo morph que Button). El nombre accesible es «Editar».",
    canvasHtml: `<button type="button" class="mot-icon-btn" id="demo-icon" aria-label="Editar">${ICON_EDIT}</button>
      <p class="playground__status" id="demo-status"></p>`,
  },
  doDont: {
    doHtml: iconButtonHtml("Editar"),
    doCaption: "Icono inequívoco y nombre accesible. Standard, sin contenedor.",
    dontHtml: `<button type="button" class="fake-btn" aria-hidden="true">★</button>`,
    dontCaption: "Un símbolo sin nombre ni token. No es MOTIconButton.",
  },
  setup() {
    const btn = document.getElementById("demo-icon");
    const status = document.getElementById("demo-status");
    if (!btn || !status) return;
    status.innerHTML =
      "<code>MOTIconButton</code> · reposo 0 dp · interacción 20 dp · icono 24 dp · táctil 48 dp";
    btn.addEventListener("pointerdown", () => btn.classList.add("is-pressed"));
    btn.addEventListener("pointerup", () => btn.classList.remove("is-pressed"));
    btn.addEventListener("pointerleave", () => btn.classList.remove("is-pressed"));
  },
});
