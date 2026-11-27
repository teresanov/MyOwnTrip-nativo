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
  <div class="specimen-stage">
    <button type="button" class="mot-btn mot-btn--filled">Guardar gasto</button>
    <button type="button" class="mot-btn mot-btn--tonal">Siguiente</button>
    <button type="button" class="mot-btn mot-btn--outlined">Cancelar</button>
    <button type="button" class="filter-chip filter-chip--selected">Activos</button>
    <button type="button" class="filter-chip">Archivados</button>
  </div>
  <p class="specimen-stage__note">
    Los mismos roles que en Compose. El showcase no monta Android: esto es HTML a 1&nbsp;px = 1&nbsp;dp.
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
      <p>${total} fichas: ${ready} listas, ${review} en revisión${draft ? `, ${draft} en borrador` : ""}.</p>
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
      Esta web es un trabajo en curso: cada ficha dice en qué punto está, con el criterio que explica
      <a href="${assetUrl("gobernanza.html")}#estados">Gobernanza</a>.
      De ${total} fichas, ${ready} están <strong>listas</strong> (ejemplo con el componente real, anatomía y medidas,
      revisados por diseño), ${review} están <strong>en revisión</strong> y ${draft}
      ${draft === 1 ? "sigue" : "siguen"} <strong>en construcción</strong>.
    </p>
    <p class="section-lead">
      <strong>Search bar</strong> y <strong>Menu</strong> viven en Figma y en Compose pero no tienen
      ficha en el índice; Home los documenta en
      <a href="${assetUrl("patterns.html")}#home-filter-menu">Patrones · menú de filtros</a>.
      Las diferencias pendientes entre Figma y la app se siguen en el repositorio, no en esta tabla.
    </p>
    <table>
      <thead><tr><th>Componente</th><th>Estado</th><th>Prioridad</th></tr></thead>
      <tbody>
        ${[...componentsManifest.components]
          .sort((a, b) => {
            const order = { draft: 0, "in-review": 1, ready: 2 };
            return (order[a.status] ?? 9) - (order[b.status] ?? 9);
          })
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
      <strong>Tokens:</strong> Figma (variables) → <code>variables.json</code> → Compose.
      <strong>Componentes:</strong> Compose → Figma (espejo) → esta web.
      La paridad se anota en el log; Notion registra el trabajo, no especifica.
    </div>
  </section>
`;

setupToc();
