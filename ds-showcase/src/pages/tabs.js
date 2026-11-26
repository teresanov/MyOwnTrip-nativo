import data from "../data/components/tabs.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { liveHostHtml, setLiveProps } from "../lib/live-specimen.js";

mountComponentPage(data, {
  specimenHtml: liveHostHtml("tabs/playground", { row: "scrollable", theme: "light" }, { height: 100, label: "Tabs real" }),
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint: '<span class="live-badge">Compose real</span> Es <code>MOTTab</code> sobre la fila de pestañas de Material 3, compilado a WebAssembly. Toca una pestaña: el indicador se desliza hasta ella. Cambia entre la fila fija del día y la fila con desplazamiento del viaje.',
    canvasHtml: `
      ${liveHostHtml("tabs/playground", { row: "fixed", theme: "light" }, { height: 140, label: "Tabs real" })}
      <p class="playground__status" id="demo-status"></p>`,
    controlsHtml: `
      <label class="control"><span>Fila</span>
        <select id="demo-row"><option value="fixed">Fija: Plan · Diario (día)</option><option value="scrollable">Con desplazamiento: secciones del viaje</option></select>
      </label>
      <label class="playground-check"><input type="checkbox" id="demo-dark" /><span>Tema oscuro</span></label>`,
  },
  setup() {
    const host = document.querySelector('.playground__canvas [data-mot-specimen="tabs/playground"]');
    const status = document.getElementById("demo-status");
    const els = Object.fromEntries(["row","dark"].map((k) => [k, document.getElementById("demo-" + k)]));
    const sync = () => {
      const v = { row: els.row.value };
      setLiveProps(host, { ...v, theme: els.dark.checked ? "dark" : "light" });
      status.innerHTML = "<code>" + `${v.row === "scrollable" ? "ScrollableTabRow(edgePadding = MOTSpacing.none)" : "TabRow"} { MOTTab(selected, onClick, label) }` + "</code>";
    };
    Object.values(els).forEach((el) => {
      el.addEventListener("change", sync);
      el.addEventListener("input", sync);
    });
    sync();
  },
});
