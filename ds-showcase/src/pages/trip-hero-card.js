import data from "../data/components/trip-hero-card.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { eyebrowHtml } from "../lib/specimens.js";

const hero = `
  <article class="trip-hero">
    <div class="trip-hero__cover">
      ${eyebrowHtml("Próximo viaje")}
      <p class="trip-hero__count">Sale en 3 días</p>
      <h3 class="trip-hero__title">Barcelona fin de semana</h3>
      <p class="trip-hero__meta">4 jul 2026 – 6 jul 2026 · 3 días</p>
    </div>
    <button type="button" class="mot-btn mot-btn--tonal mot-btn--xs" tabindex="-1">Ver detalles</button>
  </article>`;

mountComponentPage(data, {
  specimenHtml: hero,
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint: "La portada no es un botón. El único control es «Ver detalles» (MOTTonalButton).",
    canvasHtml: hero.replace('tabindex="-1"', ""),
  },
  doDont: {
    doHtml: hero,
    doCaption: "Foto, eyebrow, título y un CTA debajo.",
    dontHtml: `<button type="button" class="filter-chip">Ver detalles</button>`,
    dontCaption: "Ni chip ni portada pulsable. El CTA es tonal.",
  },
});
