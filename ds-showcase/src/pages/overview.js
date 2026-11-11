import { initShowcasePage, statusChip, setupToc } from "../lib/bootstrap.js";
import componentsManifest from "../data/components.json";
import site from "../data/site.json";
import { assetUrl } from "../lib/paths.js";

initShowcasePage("overview");

const total = componentsManifest.components.length;
const ready = componentsManifest.components.filter((c) => c.status === "ready").length;
const review = componentsManifest.components.filter((c) => c.status === "in-review").length;
const draft = componentsManifest.components.filter((c) => c.status === "draft").length;

const FOUNDATIONS = site.nav.find((n) => n.id === "foundations")?.children || [];

const FOUNDATION_DESC = {
  color: "Roles M3, paleta gris-azul y acento ocre",
  typography: "Fraunces en titulares, Inter en el resto",
  shape: "Roles de superficie y el morph del botón",
  spacing: "MOTSpacing de pantalla y padding de componente",
  iconography: "Material Symbols Sharp, peso 300",
  motion: "Duraciones, el muelle de liberación y reduce motion",
};

document.getElementById("page-header").innerHTML = `
  <p class="page-header__eyebrow">Design System · MyOwnTrip Nativo</p>
  <h1>Material 3 con voz editorial</h1>
  <p class="page-header__lead">
    Un sistema construido sobre M3 canónico, sin capa semántica propia.
    La identidad está en la tipografía, en el color con cuentagotas y en un botón
    que es rectángulo hasta que lo tocas.
  </p>
`;

document.getElementById("main").innerHTML = `
  <div class="entry-grid">
    <a class="entry-card" href="${assetUrl("empezar.html")}">
      <h3>Empezar aquí</h3>
      <p>Cómo diseñar una pantalla y cómo implementarla. Qué documenta cada capa.</p>
    </a>
    <a class="entry-card" href="${assetUrl("components.html")}">
      <h3>Componentes</h3>
      <p>${total} componentes: ${ready} listos, ${review} en revisión, ${draft} sin ficha.</p>
    </a>
    <a class="entry-card" href="${assetUrl("gobernanza.html")}">
      <h3>Gobernanza</h3>
      <p>Estados, criterios para crear un componente propio y gates antes de cerrar.</p>
    </a>
  </div>

  <section class="section">
    <h2 id="foundations">Foundations</h2>
    <p class="section-lead">
      Las decisiones que no son de un componente concreto. Todas tienen su fuente de verdad
      en el código, no en esta web.
    </p>
    <div class="component-grid">
      ${FOUNDATIONS.map(
        (f) => `
        <a class="component-link" href="${assetUrl(f.href)}">
          <h3>${f.label}</h3>
          <p>${FOUNDATION_DESC[f.id] || ""}</p>
        </a>`
      ).join("")}
    </div>
  </section>

  <section class="section">
    <h2 id="estado">Estado del sistema</h2>
    <p class="section-lead">
      Una ficha en <strong>borrador</strong> significa que el componente existe en Figma o en
      código pero nadie ha escrito cuándo usarlo. Es la deuda visible del sistema.
    </p>
    <table>
      <thead><tr><th>Componente</th><th>Estado</th><th>Prioridad</th></tr></thead>
      <tbody>
        ${componentsManifest.components
          .map(
            (c) => `
          <tr>
            <td>${
              c.href === "components.html"
                ? c.name
                : `<a href="${assetUrl(c.href)}">${c.name}</a>`
            }</td>
            <td>${statusChip(c.status)}</td>
            <td>${c.priority}</td>
          </tr>`
          )
          .join("")}
      </tbody>
    </table>
  </section>

  <section class="section">
    <h2 id="pipeline">Pipeline</h2>
    <div class="callout">
      <strong>Figma</strong> (variables, vía Desktop Bridge) → <code>variables.json</code> →
      <strong>Compose</strong> → esta web y el contexto para agentes.
      Figma es librería visual; la documentación de componentes vive aquí.
    </div>
  </section>
`;

setupToc();
