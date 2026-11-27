import data from "../data/components/search-bar.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { liveHostHtml, setLiveProps } from "../lib/live-specimen.js";

mountComponentPage(data, {
  specimenHtml: liveHostHtml("search-bar/playground", { query: "", theme: "light" }, { height: 100, label: "Search bar real" }),
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint: '<span class="live-badge">Compose real</span> Es <code>MotSearchBar</code> del design system, compilado a WebAssembly, con los textos de Home. Escribe en la barra y pulsa el botón de filtros: abre el menú real.',
    canvasHtml: `
      ${liveHostHtml("search-bar/playground", { query: "", theme: "light" }, { height: 560, label: "Search bar real" })}
      <p class="playground__status" id="demo-status"></p>`,
    controlsHtml: `
      <label class="control"><span>Texto inicial</span><input type="text" id="demo-query" value="" placeholder="Vacía" /></label>
      <label class="playground-check"><input type="checkbox" id="demo-dark" /><span>Tema oscuro</span></label>`,
  },
  setup() {
    const host = document.querySelector('.playground__canvas [data-mot-specimen="search-bar/playground"]');
    const status = document.getElementById("demo-status");
    const [query, dark] = ["demo-query", "demo-dark"].map((i) => document.getElementById(i));
    const sync = () => {
      setLiveProps(host, { query: query.value, theme: dark.checked ? "dark" : "light" });
      status.innerHTML = `<code>MotSearchBar(query = "${query.value}", placeholder = "Buscar viajes", onFilterClick, menu = { MotMenuDropdown(…) })</code>`;
    };
    query.addEventListener("change", sync);
    dark.addEventListener("change", sync);
    sync();
  },
});
