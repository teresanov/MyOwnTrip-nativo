import { initShowcasePage } from "../lib/bootstrap.js";
import data from "../data/components/text-field.json";
import {
  renderA11ySection,
  renderAnatomy,
  renderChangelog,
  renderComponentHeader,
  renderComposeSection,
  renderGuidelines,
  renderPlayground,
  renderTokenSection,
  renderVariantTable,
} from "../lib/component-page.js";

initShowcasePage("components", { componentId: "text-field" });

document.getElementById("component-header").innerHTML = renderComponentHeader(
  data,
  "Abrir Text field en Figma",
);

const examples = (data.productExamples || [])
  .map((e) => `<tr><td><code>${e.screen}</code></td><td>${e.fields}</td></tr>`)
  .join("");

document.getElementById("component-main").innerHTML = `
  ${renderPlayground({
    title: "Playground",
    hint: "Escribe en los campos y activa el error de validación para ver borde, label y texto de apoyo como en un formulario real.",
    canvasHtml: `
      <div class="demo-stack">
        <label class="mot-field">
          <span class="mot-field__label">Nombre del viaje</span>
          <input class="mot-field__input" id="demo-trip-name" type="text" value="Barcelona 2026" autocomplete="off" />
        </label>
        <label class="mot-field" id="demo-dest-field">
          <span class="mot-field__label">Destino</span>
          <input class="mot-field__input" id="demo-dest-input" type="text" placeholder="Ciudad o región" autocomplete="off" />
          <span class="mot-field__support" id="demo-dest-error" hidden>El destino es obligatorio</span>
        </label>
      </div>
    `,
    controlsHtml: `
      <label class="playground-check">
        <input type="checkbox" id="demo-show-error" />
        Mostrar error en «Destino»
      </label>
    `,
  })}

  ${renderAnatomy(data.anatomy)}
  ${renderVariantTable(data.variants, "Estados")}

  <section class="section">
    <h2>Dónde aparece en la app</h2>
    <table>
      <thead><tr><th>Pantalla</th><th>Campos</th></tr></thead>
      <tbody>${examples}</tbody>
    </table>
  </section>

  ${renderTokenSection(data.tokens)}
  ${renderGuidelines(data)}
  ${renderA11ySection(data.a11y)}
  ${renderComposeSection(data.compose)}
  ${renderChangelog(data.changelog)}
`;

const destField = document.getElementById("demo-dest-field");
const destError = document.getElementById("demo-dest-error");
const showError = document.getElementById("demo-show-error");

function syncErrorState() {
  const on = showError.checked;
  destField.classList.toggle("mot-field--error", on);
  destError.hidden = !on;
}

showError.addEventListener("change", syncErrorState);
