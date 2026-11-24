import data from "../data/components/text-field.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { fieldHtml } from "../lib/specimens.js";

mountComponentPage(data, {
  specimenHtml: fieldHtml({ label: "Nombre del viaje", value: "Barcelona 2026" }),
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint: "Escribe y activa el error de Destino: borde, label y supporting text juntos.",
    canvasHtml: `
      <div class="demo-stack">
        ${fieldHtml({ label: "Nombre del viaje", value: "Barcelona 2026" }).replace(
          "class=\"mot-field__input\"",
          "class=\"mot-field__input\" id=\"demo-trip-name\"",
        )}
        <label class="mot-field" id="demo-dest-field">
          <span class="mot-field__label">Destino</span>
          <input class="mot-field__input" id="demo-dest-input" type="text" placeholder="Ciudad o región" autocomplete="off" />
          <span class="mot-field__support" id="demo-dest-error" hidden>El destino es obligatorio</span>
        </label>
      </div>`,
    controlsHtml: `
      <label class="playground-check">
        <input type="checkbox" id="demo-show-error" />
        <span>Mostrar error en «Destino»</span>
      </label>`,
  },
  doDont: {
    doHtml: fieldHtml({
      label: "Destino",
      value: "",
      error: "El destino es obligatorio",
    }),
    doCaption: "Error con borde, label y texto. Nunca solo el color.",
    dontHtml: fieldHtml({ label: "Destino", value: "" }),
    dontCaption: "Un campo vacío sin mensaje no comunica el error.",
  },
  setup() {
    const destField = document.getElementById("demo-dest-field");
    const destError = document.getElementById("demo-dest-error");
    const showError = document.getElementById("demo-show-error");
    if (!destField || !showError) return;
    showError.addEventListener("change", () => {
      const on = showError.checked;
      destField.classList.toggle("mot-field--error", on);
      destError.hidden = !on;
    });
  },
});
