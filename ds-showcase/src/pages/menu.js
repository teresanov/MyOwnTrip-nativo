import data from "../data/components/menu.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { liveHostHtml, setLiveProps } from "../lib/live-specimen.js";

const SHOW = ["Todos los viajes", "En curso", "Próximos", "Pasados", "Archivados"];
const SORT = ["Fecha — próximo primero", "Nombre A—Z", "Destino A—Z"];
const options = (list) => list.map((l, i) => `<option value="${i}">${l}</option>`).join("");

mountComponentPage(data, {
  specimenHtml: liveHostHtml("menu/static", { show: "0", sort: "0", theme: "light" }, { height: 520, label: "Menu real" }),
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint: '<span class="live-badge">Compose real</span> Es el menú de filtros de Home, hecho con las piezas del design system y compilado a WebAssembly. Aquí está quieto para poder verlo entero; puedes pulsar sus opciones. En la app se abre desde el botón de filtros de la <a href="search-bar.html">Search bar</a> y se cierra al elegir.',
    canvasHtml: `
      ${liveHostHtml("menu/static", { show: "0", sort: "0", theme: "light" }, { height: 540, label: "Menu real" })}
      <p class="playground__status"><code>MotMenuPanel { MotMenuGroup { MotMenuSectionLabel("Mostrar"); MotMenuItem(…) } … }</code></p>`,
    controlsHtml: `
      <label class="control"><span>Mostrar</span><select id="demo-show">${options(SHOW)}</select></label>
      <label class="control"><span>Ordenar</span><select id="demo-sort">${options(SORT)}</select></label>
      <label class="playground-check"><input type="checkbox" id="demo-dark" /><span>Tema oscuro</span></label>`,
  },
  setup() {
    const host = document.querySelector('.playground__canvas [data-mot-specimen="menu/static"]');
    const [show, sort, dark] = ["demo-show", "demo-sort", "demo-dark"].map((i) => document.getElementById(i));
    const sync = () => setLiveProps(host, { show: show.value, sort: sort.value, theme: dark.checked ? "dark" : "light" });
    [show, sort, dark].forEach((el) => el.addEventListener("change", sync));
    sync();
  },
});
