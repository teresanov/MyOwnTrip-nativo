import data from "../data/components/app-bar.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { liveHostHtml, setLiveProps } from "../lib/live-specimen.js";

mountComponentPage(data, {
  specimenHtml: liveHostHtml("app-bar/playground", { title: "Cracovia finde", action: "edit", theme: "light" }, { height: 110, label: "App bar real" }),
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint: '<span class="live-badge">Compose real</span> Es el <code>TopAppBar</code> de Material 3 con <code>MOTIconButton</code>, compilado a WebAssembly. Cambia el título y la acción para ver los casos de la app.',
    canvasHtml: `
      ${liveHostHtml("app-bar/playground", { title: "Editar viaje", action: "none", theme: "light" }, { height: 130, label: "App bar real" })}
      <p class="playground__status" id="demo-status"></p>`,
    controlsHtml: `
      <label class="control"><span>Título</span><input type="text" id="demo-title" value="Editar viaje" /></label>
      <label class="control"><span>Acción</span>
        <select id="demo-action"><option value="none">Ninguna (Editar viaje)</option><option value="edit">Editar (detalle del viaje)</option><option value="delete">Eliminar (detalle de Wallet)</option></select>
      </label>
      <label class="playground-check"><input type="checkbox" id="demo-dark" /><span>Tema oscuro</span></label>`,
  },
  setup() {
    const host = document.querySelector('.playground__canvas [data-mot-specimen="app-bar/playground"]');
    const status = document.getElementById("demo-status");
    const els = Object.fromEntries(["title","action","dark"].map((k) => [k, document.getElementById("demo-" + k)]));
    const sync = () => {
      const v = { title: els.title.value, action: els.action.value };
      setLiveProps(host, { ...v, theme: els.dark.checked ? "dark" : "light" });
      status.innerHTML = "<code>" + `TopAppBar(title, navigationIcon = { MOTIconButton(…) }${v.action !== "none" ? ", actions = { MOTIconButton(…) }" : ""})` + "</code>";
    };
    Object.values(els).forEach((el) => {
      el.addEventListener("change", sync);
      el.addEventListener("input", sync);
    });
    sync();
  },
});
