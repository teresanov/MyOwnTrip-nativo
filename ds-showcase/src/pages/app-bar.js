import data from "../data/components/app-bar.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { liveHostHtml, setLiveProps } from "../lib/live-specimen.js";

/** Casos reales de la app: título y acciones de cada pantalla. */
const CASES = {
  form: { title: "Nuevo viaje", label: "Formulario: Nuevo viaje", code: "" },
  trip: { title: "Cracovia finde", label: "Detalle del viaje: Editar viaje", code: "MOTIconButton(Editar viaje)" },
  document: { title: "IB 3254 · Madrid → Barcelona", label: "Documento de Wallet: Eliminar", code: "MOTIconButton(Eliminar documento)" },
  viewer: { title: "entrada-teatro.jpg", label: "Visor de archivo: Abrir con otra app", code: "MOTIconButton(Abrir con otra app)" },
  memory: { title: "Recuerdo", label: "Recuerdo del Diario: Editar y Eliminar", code: "MOTIconButton(Editar recuerdo); MOTIconButton(Eliminar recuerdo)" },
  sketch: { title: "Boceto", label: "Boceto: Guardar", code: "MOTIconButton(Guardar boceto)" },
};

mountComponentPage(data, {
  specimenHtml: liveHostHtml("app-bar/playground", { title: "Cracovia finde", case: "trip", theme: "light" }, { height: 110, label: "App bar real" }),
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint: '<span class="live-badge">Compose real</span> Es el <code>TopAppBar</code> de Material 3 con <code>MOTIconButton</code>, compilado a WebAssembly. Elige una pantalla de la app para ver su título y sus acciones; puedes cambiar el título para probar uno largo.',
    canvasHtml: `
      ${liveHostHtml("app-bar/playground", { title: "Nuevo viaje", case: "form", theme: "light" }, { height: 130, label: "App bar real" })}
      <p class="playground__status" id="demo-status"></p>`,
    controlsHtml: `
      <label class="control"><span>Pantalla</span>
        <select id="demo-case">${Object.entries(CASES).map(([k, c]) => `<option value="${k}">${c.label}</option>`).join("")}</select>
      </label>
      <label class="control"><span>Título</span><input type="text" id="demo-title" value="Nuevo viaje" /></label>
      <label class="playground-check"><input type="checkbox" id="demo-dark" /><span>Tema oscuro</span></label>`,
  },
  setup() {
    const host = document.querySelector('.playground__canvas [data-mot-specimen="app-bar/playground"]');
    const status = document.getElementById("demo-status");
    const els = Object.fromEntries(["case", "title", "dark"].map((k) => [k, document.getElementById("demo-" + k)]));
    const sync = () => {
      const c = CASES[els.case.value];
      setLiveProps(host, { title: els.title.value, case: els.case.value, theme: els.dark.checked ? "dark" : "light" });
      status.innerHTML = "<code>" + `TopAppBar(title, navigationIcon = { MOTIconButton(Volver) }${c.code ? `, actions = { ${c.code} }` : ""})` + "</code>";
    };
    els.case.addEventListener("change", () => {
      els.title.value = CASES[els.case.value].title;
      sync();
    });
    els.title.addEventListener("input", sync);
    els.dark.addEventListener("change", sync);
    sync();
  },
});
