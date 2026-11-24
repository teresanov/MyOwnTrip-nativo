import data from "../data/components/date-time-pickers.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { ICON_CAL } from "../lib/specimens.js";

const range = `
  <div class="mot-date-row">
    <label class="mot-field">
      <span class="mot-field__label">Fecha inicio</span>
      <input class="mot-field__input" type="text" value="01/06/2026" readonly />
    </label>
    <label class="mot-field">
      <span class="mot-field__label">Fecha fin</span>
      <input class="mot-field__input" type="text" value="08/06/2026" readonly />
    </label>
    <button type="button" class="mot-icon-btn" aria-label="Abrir calendario">${ICON_CAL}</button>
  </div>`;

mountComponentPage(data, {
  specimenHtml: range,
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint: "Ilustración de MotDateRangeField: dos Outlined + MOTIconButton. El calendario modal es Compose, no este HTML.",
    canvasHtml: range,
  },
  doDont: {
    doHtml: range,
    doCaption: "Campos outlined + icono calendario. Gap 8 dp.",
    dontHtml: `<input type="date" />`,
    dontCaption: "El date nativo del browser no es el patrón Mot*.",
  },
});
