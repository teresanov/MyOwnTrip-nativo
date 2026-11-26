import data from "../data/components/divider.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { liveHostHtml, setLiveProps } from "../lib/live-specimen.js";

mountComponentPage(data, {
  specimenHtml: liveHostHtml("divider/playground", { inset: true, theme: "light" }, { height: 140, label: "Divider real" }),
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint: '<span class="live-badge">Compose real</span> Es <code>MOTHorizontalDivider</code>, compilado a WebAssembly, entre dos filas como en Wallet. Quita la sangría para ver la línea a todo el ancho, como entre bloques de un formulario.',
    canvasHtml: `
      ${liveHostHtml("divider/playground", { inset: true, theme: "light" }, { height: 160, label: "Divider real" })}
      <p class="playground__status" id="demo-status"></p>`,
    controlsHtml: `
      <label class="playground-check"><input type="checkbox" id="demo-inset" checked /><span>Con sangría (inset)</span></label>
      <label class="playground-check"><input type="checkbox" id="demo-dark" /><span>Tema oscuro</span></label>`,
  },
  setup() {
    const host = document.querySelector('.playground__canvas [data-mot-specimen="divider/playground"]');
    const status = document.getElementById("demo-status");
    const els = Object.fromEntries(["inset","dark"].map((k) => [k, document.getElementById("demo-" + k)]));
    const sync = () => {
      const v = { inset: els.inset.checked };
      setLiveProps(host, { ...v, theme: els.dark.checked ? "dark" : "light" });
      status.innerHTML = "<code>" + `MOTHorizontalDivider(${v.inset ? "inset = true" : ""})` + "</code>";
    };
    Object.values(els).forEach((el) => {
      el.addEventListener("change", sync);
      el.addEventListener("input", sync);
    });
    sync();
  },
});
