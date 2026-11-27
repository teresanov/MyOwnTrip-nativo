import data from "../data/components/list-item.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { liveHostHtml, setLiveProps } from "../lib/live-specimen.js";

mountComponentPage(data, {
  specimenHtml: liveHostHtml("list-item/playground", { warning: "false", availability: "none", theme: "light" }, { height: 110, label: "MotListItem real" }),
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint: '<span class="live-badge">Compose real</span> Es <code>MotListItem</code> del design system, compilado a WebAssembly, con los textos de un documento de Wallet. Prueba el aviso del plan y la disponibilidad sin conexión. La fila no se desliza ni lleva el botón ⋮: eso lo añade la <a href="swipeable-list-row.html">Swipeable list row</a>.',
    canvasHtml: `
      ${liveHostHtml("list-item/playground", { warning: "false", availability: "none", theme: "light" }, { height: 130, label: "MotListItem real" })}
      <p class="playground__status" id="demo-status"></p>`,
    controlsHtml: `
      <label class="playground-check"><input type="checkbox" id="demo-warning" /><span>Aviso del plan</span></label>
      <label class="control"><span>Disponibilidad</span>
        <select id="demo-availability"><option value="none">Sin indicador</option><option value="offline">Guardado en el móvil</option><option value="cloud">En la nube</option></select>
      </label>
      <label class="playground-check"><input type="checkbox" id="demo-dark" /><span>Tema oscuro</span></label>`,
  },
  setup() {
    const host = document.querySelector('.playground__canvas [data-mot-specimen="list-item/playground"]');
    const status = document.getElementById("demo-status");
    const [warning, availability, dark] = ["demo-warning", "demo-availability", "demo-dark"].map((i) => document.getElementById(i));
    const sync = () => {
      setLiveProps(host, { warning: warning.checked, availability: availability.value, theme: dark.checked ? "dark" : "light" });
      status.innerHTML = `<code>MotListItem(title, supportingText, icon${warning.checked ? ", warningSuffix" : ""}${availability.value === "offline" && !warning.checked ? ", dimmed = true" : ""}${availability.value !== "none" ? ", trailing" : ""})</code>`;
    };
    [warning, availability, dark].forEach((el) => el.addEventListener("change", sync));
    sync();
  },
});
