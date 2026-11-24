import data from "../data/components/list-item.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { listItemHtml } from "../lib/specimens.js";

mountComponentPage(data, {
  specimenHtml: `<div class="mot-li-wrap">${listItemHtml()}</div>`,
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint: "Fila de Wallet: círculo 40, glifo 24, titular, tipo · fecha, menú ⋮. La fila entera es el área pulsable.",
    canvasHtml: `<div class="mot-li-wrap">${listItemHtml()}</div>`,
  },
  doDont: {
    doHtml: `<div class="mot-li-wrap">${listItemHtml()}</div>`,
    doCaption: "Leading a la izquierda, titular breve, trailing de acción.",
    dontHtml: `<div class="mot-li" style="justify-content:center"><strong>Billete</strong></div>`,
    dontCaption: "Visuales en el centro: no se escanea.",
  },
});
