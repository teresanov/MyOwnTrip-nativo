import data from "../data/components/eyebrow-label.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { eyebrowHtml } from "../lib/specimens.js";

mountComponentPage(data, {
  specimenHtml: `${eyebrowHtml("Próximo viaje")}${eyebrowHtml("En destino", "surface")}${eyebrowHtml("Recuerdo", "secondary", "sm")}`,
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint: "Tres colores del composable. No son chips: no hay pressed ni selected.",
    canvasHtml: `<div class="eyebrow-row">
      ${eyebrowHtml("Próximo viaje")}
      ${eyebrowHtml("En destino", "surface")}
      ${eyebrowHtml("Recuerdo", "secondary")}
    </div>`,
  },
  extraAfterUsage: `
    <section class="section">
      <h2 id="no-es-un-chip">No es un chip</h2>
      <p class="section-lead">Misma píldora visual; función distinta.</p>
      <table>
        <thead><tr><th></th><th>Filter chip</th><th>Eyebrow label</th></tr></thead>
        <tbody>
          <tr><td>Función</td><td>Filtro, selección</td><td>Contexto informativo</td></tr>
          <tr><td>Interactivo</td><td>Sí</td><td>No</td></tr>
          <tr><td>Sobre foto</td><td>No (outline)</td><td>Sí (Tertiary)</td></tr>
        </tbody>
      </table>
    </section>`,
  doDont: {
    doHtml: eyebrowHtml("Próximo viaje"),
    doCaption: "Fase sobre la portada. HUG, Tertiary.",
    dontHtml: `<button type="button" class="filter-chip filter-chip--selected">Próximo viaje</button>`,
    dontCaption: "Un chip selected no es un eyebrow. No filtra nada.",
  },
});
