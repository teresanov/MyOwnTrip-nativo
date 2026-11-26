import data from "../data/components/eyebrow-label.json";
import { mountComponentPage } from "../lib/generic-component.js";
import { liveHostHtml, setLiveProps } from "../lib/live-specimen.js";

mountComponentPage(data, {
  specimenHtml: liveHostHtml("eyebrow-label/playground", { text: "Próximo viaje", theme: "light" }, { height: 100, label: "EyebrowLabel real" }),
  playground: {
    id: "ejemplo",
    title: "Ejemplo",
    hint: '<span class="live-badge">Compose real</span> Es <code>EyebrowLabel</code> del design system, compilado a WebAssembly. La app solo usa el color Tertiary, en Medium (portada del viaje) y en Small (álbum del Diario). No es un chip: no se pulsa ni se selecciona.',
    canvasHtml: `
      ${liveHostHtml("eyebrow-label/playground", { text: "Próximo viaje", color: "tertiary", size: "medium", theme: "light" }, { height: 120, label: "EyebrowLabel real" })}
      <p class="playground__status" id="demo-status"></p>`,
    controlsHtml: `
      <label class="control"><span>Texto</span><input type="text" id="demo-text" value="Próximo viaje" /></label>
      <label class="control"><span>Color</span>
        <select id="demo-color"><option value="tertiary">Tertiary (el que usa la app)</option><option value="surface">Surface (sin uso)</option><option value="secondary">Secondary (sin uso)</option></select>
      </label>
      <label class="control"><span>Tamaño</span>
        <select id="demo-size"><option value="medium">Medium</option><option value="small">Small</option></select>
      </label>
      <label class="playground-check"><input type="checkbox" id="demo-dark" /><span>Tema oscuro</span></label>`,
  },
  extraAfterUsage: `
    <section class="section">
      <h2 id="no-es-un-chip">No es un chip</h2>
      <p class="section-lead">Misma píldora visual; función distinta.</p>
      <table>
        <thead><tr><th></th><th>Filter chip</th><th>Eyebrow label</th></tr></thead>
        <tbody>
          <tr><td>Función</td><td>Filtro, selección</td><td>Contexto informativo</td></tr>
          <tr><td>Interactivo</td><td>Sí</td><td>No</td></tr>
          <tr><td>Sobre foto</td><td>No (outline)</td><td>Sí (Tertiary)</td></tr>
        </tbody>
      </table>
    </section>`,
  setup() {
    const host = document.querySelector('.playground__canvas [data-mot-specimen="eyebrow-label/playground"]');
    const status = document.getElementById("demo-status");
    const [text, color, size, dark] = ["demo-text", "demo-color", "demo-size", "demo-dark"].map((i) => document.getElementById(i));
    const cap = (s) => s[0].toUpperCase() + s.slice(1);
    const sync = () => {
      setLiveProps(host, { text: text.value, color: color.value, size: size.value, theme: dark.checked ? "dark" : "light" });
      status.innerHTML = `<code>EyebrowLabel(text, color = EyebrowLabelColor.${cap(color.value)}, size = EyebrowLabelSize.${cap(size.value)})</code>`;
    };
    [text, color, size, dark].forEach((el) => { el.addEventListener("change", sync); el.addEventListener("input", sync); });
    sync();
  },
});
