import data from "../data/components/swipeable-list-row.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { listItemHtml } from "../lib/specimens.js";

const swipe = `
  <div class="swipe-demo">
    <div class="swipe-demo__rail swipe-demo__rail--start">Archivar</div>
    <div class="swipe-demo__row">${listItemHtml()}</div>
    <div class="swipe-demo__rail swipe-demo__rail--end">Eliminar</div>
  </div>`;

mountComponentPage(data, {
  specimenHtml: swipe,
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint: "Anatomía del gesto, no un swipe real. En Compose, el umbral es ~45 % y Eliminar abre el diálogo; no borra al soltar. El ⋮ hace lo mismo por tap.",
    canvasHtml: swipe,
  },
  doDont: {
    doHtml: swipe,
    doCaption: "Fondos secondaryContainer / errorContainer y menú ⋮ equivalente.",
    dontHtml: `<div class="mot-li-wrap">${listItemHtml({ more: false })}</div>`,
    dontCaption: "Fila sin ⋮: el swipe quedaría como única vía. Prohibido por a11y.",
  },
});
