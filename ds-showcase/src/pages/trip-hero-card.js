import data from "../data/components/trip-hero-card.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { liveHostHtml, setLiveProps } from "../lib/live-specimen.js";

mountComponentPage(data, {
  specimenHtml: liveHostHtml("trip-hero-card/playground", { phase: "upcoming", theme: "light" }, { height: 380, label: "TripHeroCard real" }),
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint: '<span class="live-badge">Compose real</span> Es <code>TripHeroCard</code> del design system, compilado a WebAssembly, con los textos que calcula la app para cada fase. La portada es la de reserva (sin foto): las dos primeras letras del destino. El único control es «Ver detalles».',
    canvasHtml: `
      ${liveHostHtml("trip-hero-card/playground", { phase: "upcoming", theme: "light" }, { height: 400, label: "TripHeroCard real" })}
      <p class="playground__status" id="demo-status"></p>`,
    controlsHtml: `
      <label class="control"><span>Fase del viaje</span>
        <select id="demo-phase"><option value="upcoming">Próximo (con cuenta atrás)</option><option value="current">En curso</option><option value="past">Pasado</option></select>
      </label>
      <label class="playground-check"><input type="checkbox" id="demo-dark" /><span>Tema oscuro</span></label>`,
  },
  setup() {
    const host = document.querySelector('.playground__canvas [data-mot-specimen="trip-hero-card/playground"]');
    const status = document.getElementById("demo-status");
    const [phase, dark] = ["demo-phase", "demo-dark"].map((i) => document.getElementById(i));
    const sync = () => {
      setLiveProps(host, { phase: phase.value, theme: dark.checked ? "dark" : "light" });
      status.innerHTML = "<code>TripHeroCard(trip, today, onClick)</code>";
    };
    [phase, dark].forEach((el) => el.addEventListener("change", sync));
    sync();
  },
});
