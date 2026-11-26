import data from "../data/components/swipeable-list-row.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { liveHostHtml, setLiveProps } from "../lib/live-specimen.js";

mountComponentPage(data, {
  specimenHtml: liveHostHtml("swipeable-list-row/playground", { theme: "light" }, { height: 150, label: "MotSwipeableRow real" }),
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint: '<span class="live-badge">Compose real</span> Es <code>MotSwipeableRow</code> del design system, compilado a WebAssembly, con una fila de Wallet. Arrastra la fila con el ratón hacia un lado u otro, o abre el menú ⋮. Debajo se ve qué haría la app.',
    canvasHtml: `
      ${liveHostHtml("swipeable-list-row/playground", { archived: "false", theme: "light" }, { height: 170, label: "MotSwipeableRow real" })}
      <p class="playground__status"><code>MotSwipeableRow(archived, itemNoun = "documento", onArchive, onUnarchive, onDeleteRequest) { menuButton -> … }</code></p>`,
    controlsHtml: `
      <label class="playground-check"><input type="checkbox" id="demo-archived" /><span>Lista de archivados</span></label>
      <label class="playground-check"><input type="checkbox" id="demo-dark" /><span>Tema oscuro</span></label>`,
  },
  setup() {
    const host = document.querySelector('.playground__canvas [data-mot-specimen="swipeable-list-row/playground"]');
    const [archived, dark] = ["demo-archived", "demo-dark"].map((i) => document.getElementById(i));
    const sync = () => setLiveProps(host, { archived: archived.checked, theme: dark.checked ? "dark" : "light" });
    [archived, dark].forEach((el) => el.addEventListener("change", sync));
    sync();
  },
});
