import data from "../data/components/fab.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { liveHostHtml, setLiveProps } from "../lib/live-specimen.js";

mountComponentPage(data, {
  specimenHtml: liveHostHtml("fab/playground", { theme: "light" }, { height: 120, label: "FAB real" }),
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint: '<span class="live-badge">Compose real</span> Es el <code>FloatingActionButton</code> de Material 3 con el tema de la app, compilado a WebAssembly, como en las pestañas del viaje. Pasa el cursor o púlsalo para ver la capa de estado.',
    canvasHtml: `
      ${liveHostHtml("fab/playground", { theme: "light" }, { height: 140, label: "FAB real" })}
      <p class="playground__status" id="demo-status"></p>`,
    controlsHtml: `
      <label class="playground-check"><input type="checkbox" id="demo-dark" /><span>Tema oscuro</span></label>`,
  },
  setup() {
    const host = document.querySelector('.playground__canvas [data-mot-specimen="fab/playground"]');
    const status = document.getElementById("demo-status");
    const els = Object.fromEntries(["dark"].map((k) => [k, document.getElementById("demo-" + k)]));
    const sync = () => {
      const v = {  };
      setLiveProps(host, { ...v, theme: els.dark.checked ? "dark" : "light" });
      status.innerHTML = "<code>" + `FloatingActionButton(onClick) { Icon(MOTIcons.Add, contentDescription = null) }` + "</code>";
    };
    Object.values(els).forEach((el) => {
      el.addEventListener("change", sync);
      el.addEventListener("input", sync);
    });
    sync();
  },
});
