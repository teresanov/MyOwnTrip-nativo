import data from "../data/components/stacked-card.json";
import { mountComponentPage } from "../lib/generic-component.js";

const mediaCard = `
  <figure class="card-demo card-demo--stacked">
    <div class="card-demo__header">
      <div>
        <strong>Sin viajes todavía</strong>
        <p>Planea tu primera aventura</p>
      </div>
    </div>
    <img src="/assets/home_empty_map.jpg" alt="" width="360" height="200" />
  </figure>`;

mountComponentPage(data, {
  specimenHtml: mediaCard,
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint: "Home vacío: Outlined · Media & text. La imagen es el mapa bundled (home_empty_map), no una URL.",
    canvasHtml: mediaCard,
  },
  doDont: {
    doHtml: mediaCard,
    doCaption: "Un tema, header + media, Outlined.",
    dontHtml: `<button type="button" class="mot-btn mot-btn--filled">Crear viaje</button>`,
    dontCaption: "El empty state no es un botón suelto: es esta card (y el CTA va aparte, si lo hay).",
  },
});
