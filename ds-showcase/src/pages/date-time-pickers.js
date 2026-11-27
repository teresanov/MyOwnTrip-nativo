import data from "../data/components/date-time-pickers.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { liveHostHtml, setLiveProps } from "../lib/live-specimen.js";

/** Piezas reales de la app: dónde se usa cada campo y qué código lo pinta. */
const PIECES = {
  range: { label: "Fechas del viaje (Crear y Editar viaje)", code: 'MotDateRangeField(startDate, endDate, onRangeChange, label = "Fechas del viaje")' },
  date: { label: "Fecha de un documento (Wallet)", code: 'MotDateField(date, onDateChange, label = "Fecha (opcional)")' },
  time: { label: "Hora de un documento (Wallet)", code: "MotTimeField(time, onTimeChange)" },
};

mountComponentPage(data, {
  specimenHtml: liveHostHtml("date-time-pickers/playground", { piece: "range", theme: "light" }, { height: 110, label: "Campo de fechas real" }),
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint: '<span class="live-badge">Compose real</span> Son los campos del design system, compilados a WebAssembly, con los textos de la app. Escribe una fecha y sal del campo para ver cómo se comprueba, o pulsa el icono del final: abre el diálogo real.',
    canvasHtml: `
      ${liveHostHtml("date-time-pickers/playground", { piece: "range", theme: "light" }, { height: 620, label: "Campos de fecha y hora reales" })}
      <p class="playground__status" id="demo-status"></p>`,
    controlsHtml: `
      <label class="control"><span>Campo</span>
        <select id="demo-piece">${Object.entries(PIECES).map(([k, c]) => `<option value="${k}">${c.label}</option>`).join("")}</select>
      </label>
      <label class="playground-check"><input type="checkbox" id="demo-dark" /><span>Tema oscuro</span></label>`,
  },
  doDont: {
    doHtml: liveHostHtml("date-time-pickers/playground", { piece: "range", theme: "light" }, { height: 110, label: "Campo de fechas real" }),
    doCaption: "Los dos campos de texto y el botón de calendario del design system.",
    dontHtml: `<input type="date" />`,
    dontCaption: "El selector de fechas del navegador o del sistema no es el de la app.",
  },
  setup() {
    const host = document.querySelector('.playground__canvas [data-mot-specimen="date-time-pickers/playground"]');
    const status = document.getElementById("demo-status");
    const piece = document.getElementById("demo-piece");
    const dark = document.getElementById("demo-dark");
    const sync = () => {
      setLiveProps(host, { piece: piece.value, theme: dark.checked ? "dark" : "light" });
      status.innerHTML = `<code>${PIECES[piece.value].code}</code>`;
    };
    piece.addEventListener("change", sync);
    dark.addEventListener("change", sync);
    sync();
  },
});
