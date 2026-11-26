import data from "../data/components/text-field.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { liveHostHtml, setLiveProps } from "../lib/live-specimen.js";
import { fieldHtml } from "../lib/specimens.js";

mountComponentPage(data, {
  specimenHtml: fieldHtml({ label: "Nombre del viaje", value: "Barcelona 2026" }),
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint:
      '<span class="live-badge">Compose real</span> Es el <code>OutlinedTextField</code> de Material 3 con el tema de la app, compilado a WebAssembly. Escribe en él: el label sube y el borde cambia con el foco. El texto de apoyo es opcional; con error aparece siempre, porque es lo que explica qué falla. En la app solo hay icono final en campos de fecha (calendario) y en desplegables (chevron).',
    canvasHtml: `
      ${liveHostHtml(
        "text-field/playground",
        { label: "Destino", value: "", support: false, trailing: "none", error: false, enabled: true, theme: "light" },
        { height: 180, label: "OutlinedTextField real" }
      )}
      <p class="playground__status" id="demo-status"></p>`,
    controlsHtml: `
      <label class="control"><span>Label</span><input type="text" id="demo-label" value="Destino" /></label>
      <label class="control"><span>Icono final</span>
        <select id="demo-trailing">
          <option value="none">Ninguno</option>
          <option value="calendar">Calendario (campo de fecha)</option>
          <option value="dropdown">Desplegable (chevron)</option>
        </select>
      </label>
      <label class="playground-check"><input type="checkbox" id="demo-support" /><span>Texto de apoyo</span></label>
      <label class="playground-check"><input type="checkbox" id="demo-error" /><span>Error</span></label>
      <label class="playground-check"><input type="checkbox" id="demo-enabled" checked /><span>Habilitado</span></label>
      <label class="playground-check"><input type="checkbox" id="demo-dark" /><span>Tema oscuro</span></label>`,
  },
  doDont: {
    doHtml: fieldHtml({ label: "Destino", value: "", error: "El destino es obligatorio" }),
    doCaption: "Error con borde, label y texto. Nunca solo el color.",
    dontHtml: fieldHtml({ label: "Destino", value: "" }),
    dontCaption: "Un campo vacío sin mensaje no comunica el error.",
  },
  setup() {
    const host = document.querySelector('[data-mot-specimen="text-field/playground"]');
    const status = document.getElementById("demo-status");
    const label = document.getElementById("demo-label");
    const error = document.getElementById("demo-error");
    const support = document.getElementById("demo-support");
    const trailing = document.getElementById("demo-trailing");
    const enabled = document.getElementById("demo-enabled");
    const dark = document.getElementById("demo-dark");
    const sync = () => {
      setLiveProps(host, {
        label: label.value || "Label",
        error: error.checked,
        support: support.checked,
        trailing: trailing.value,
        enabled: enabled.checked,
        theme: dark.checked ? "dark" : "light",
      });
      status.innerHTML = `<code>OutlinedTextField(label = { Text("${label.value || "Label"}") }${
        error.checked ? ", isError = true" : ""
      }${enabled.checked ? "" : ", enabled = false"})</code>`;
    };
    [label, error, support, trailing, enabled, dark].forEach((el) => {
      el.addEventListener("input", sync);
      el.addEventListener("change", sync);
    });
    sync();
  },
});
