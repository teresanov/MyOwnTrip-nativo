import data from "../data/components/stacked-card.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { liveHostHtml, setLiveProps } from "../lib/live-specimen.js";

mountComponentPage(data, {
  specimenHtml: liveHostHtml("stacked-card/playground", { layout: "media", style: "outlined", theme: "light" }, { height: 340, label: "MotStackedCard real" }),
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint: '<span class="live-badge">Compose real</span> Es <code>MotStackedCard</code> del design system, compilado a WebAssembly, con los tres usos de Home: sin viajes (con el mapa), solo viajes pasados y búsqueda sin resultados.',
    canvasHtml: `
      ${liveHostHtml("stacked-card/playground", { layout: "media", style: "outlined", theme: "light" }, { height: 360, label: "MotStackedCard real" })}
      <p class="playground__status" id="demo-status"></p>`,
    controlsHtml: `
      <label class="control"><span>Caso</span>
        <select id="demo-layout"><option value="media">Sin viajes (Media &amp; text)</option><option value="text">Solo viajes pasados (Text only)</option><option value="search">Búsqueda sin resultados</option></select>
      </label>
      <label class="control"><span>Estilo</span>
        <select id="demo-style"><option value="outlined">Outlined (el que usa la app)</option><option value="elevated">Elevated</option></select>
      </label>
      <label class="playground-check"><input type="checkbox" id="demo-dark" /><span>Tema oscuro</span></label>`,
  },
  setup() {
    const host = document.querySelector('.playground__canvas [data-mot-specimen="stacked-card/playground"]');
    const status = document.getElementById("demo-status");
    const [layout, style, dark] = ["demo-layout", "demo-style", "demo-dark"].map((i) => document.getElementById(i));
    const sync = () => {
      setLiveProps(host, { layout: layout.value, style: style.value, theme: dark.checked ? "dark" : "light" });
      status.innerHTML = `<code>MotStackedCard(style = MotCardStyle.${style.value === "elevated" ? "Elevated" : "Outlined"}, headerText, subheadText${layout.value === "media" ? ", image" : layout.value === "text" ? ", supportingText" : ""})</code>`;
    };
    [layout, style, dark].forEach((el) => el.addEventListener("change", sync));
    sync();
  },
});
