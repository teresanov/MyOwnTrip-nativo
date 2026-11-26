import data from "../data/components/list-item.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { liveHostHtml, setLiveProps } from "../lib/live-specimen.js";

mountComponentPage(data, {
  specimenHtml: liveHostHtml("list-item/playground", { warning: "false", availability: "none", theme: "light" }, { height: 110, label: "MotListItem real" }),
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint: '<span class="live-badge">Compose real</span> Es <code>MotListItem</code> del design system, compilado a WebAssembly, con los textos de un documento de Wallet. Prueba el aviso del plan y la disponibilidad sin conexión.',
    canvasHtml: `
      ${liveHostHtml("list-item/playground", { warning: "false", availability: "none", menu: "false", theme: "light" }, { height: 130, label: "MotListItem real" })}
      <p class="playground__status" id="demo-status"></p>`,
    controlsHtml: `
      <label class="playground-check"><input type="checkbox" id="demo-warning" /><span>Aviso del plan</span></label>
      <label class="control"><span>Disponibilidad</span>
        <select id="demo-availability"><option value="none">Sin indicador</option><option value="offline">Guardado en el móvil</option><option value="cloud">En la nube</option></select>
      </label>
      <label class="playground-check"><input type="checkbox" id="demo-menu" /><span>Menú ⋮ (fila deslizable)</span></label>
      <label class="playground-check"><input type="checkbox" id="demo-dark" /><span>Tema oscuro</span></label>`,
  },
  setup() {
    const host = document.querySelector('.playground__canvas [data-mot-specimen="list-item/playground"]');
    const status = document.getElementById("demo-status");
    const [warning, availability, menu, dark] = ["demo-warning", "demo-availability", "demo-menu", "demo-dark"].map((i) => document.getElementById(i));
    const sync = () => {
      setLiveProps(host, { warning: warning.checked, availability: availability.value, menu: menu.checked, theme: dark.checked ? "dark" : "light" });
      status.innerHTML = `<code>MotListItem(title, supportingText, icon${warning.checked ? ", warningSuffix" : ""}${availability.value === "offline" && !warning.checked ? ", dimmed = true" : ""}${availability.value !== "none" || menu.checked ? ", trailing" : ""})</code>`;
    };
    [warning, availability, menu, dark].forEach((el) => el.addEventListener("change", sync));
    sync();
  },
});
