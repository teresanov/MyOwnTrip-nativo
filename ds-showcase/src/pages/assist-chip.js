import data from "../data/components/assist-chip.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { liveHostHtml, setLiveProps } from "../lib/live-specimen.js";

mountComponentPage(data, {
  specimenHtml: liveHostHtml("assist-chip/playground", { theme: "light" }, { height: 100, label: "Assist chip real" }),
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint: '<span class="live-badge">Compose real</span> Es el chip del detalle de Wallet (<code>PlanPlacementSourceChip</code>, un <code>AssistChip</code> de Material 3), compilado a WebAssembly. Quita el destino para verlo deshabilitado.',
    canvasHtml: `
      ${liveHostHtml("assist-chip/playground", { enabled: true, theme: "light" }, { height: 120, label: "Assist chip real" })}
      <p class="playground__status" id="demo-status"></p>`,
    controlsHtml: `
      <label class="playground-check"><input type="checkbox" id="demo-enabled" checked /><span>Con día al que ir (onClick)</span></label>
      <label class="playground-check"><input type="checkbox" id="demo-dark" /><span>Tema oscuro</span></label>`,
  },
  setup() {
    const host = document.querySelector('.playground__canvas [data-mot-specimen="assist-chip/playground"]');
    const status = document.getElementById("demo-status");
    const els = Object.fromEntries(["enabled","dark"].map((k) => [k, document.getElementById("demo-" + k)]));
    const sync = () => {
      const v = { enabled: els.enabled.checked };
      setLiveProps(host, { ...v, theme: els.dark.checked ? "dark" : "light" });
      status.innerHTML = "<code>" + `PlanPlacementSourceChip(label = "Actualizada en el plan · Día 2"${v.enabled ? ", onClick = { … }" : ""})` + "</code>";
    };
    Object.values(els).forEach((el) => {
      el.addEventListener("change", sync);
      el.addEventListener("input", sync);
    });
    sync();
  },
});
