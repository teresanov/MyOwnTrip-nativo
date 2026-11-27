import { initShowcasePage, setupToc } from "../lib/bootstrap.js";
import { statusChip } from "../lib/bootstrap.js";
import patterns from "../data/patterns.json";

initShowcasePage("patterns");

document.getElementById("page-header").innerHTML = `
  <p class="page-header__eyebrow">Patrones</p>
  <h1>Patrones de producto ${statusChip("draft")}</h1>
  <p class="page-header__lead">${patterns.lead}</p>
`;

document.getElementById("main").innerHTML = `
  ${patterns.patterns
    .map(
      (p) => `
    <section class="section">
      <h2 id="${p.id}">${p.name} ${statusChip(p.status)}</h2>
      <h3 class="subsection-title">Problema</h3>
      <p class="section-lead">${p.problem}</p>
      <h3 class="subsection-title">Solución</h3>
      <p class="section-lead">${p.solution}</p>
      ${
        p.selection
          ? `
      <h3 class="subsection-title">${p.selection.title}</h3>
      ${(p.selection.paragraphs || []).map((para) => `<p class="section-lead">${para}</p>`).join("")}
      ${
        (p.selection.donts || []).length
          ? `<div class="guideline-col guideline-col--dont"><h3 class="subsection-title">Don't</h3><ul class="list-dos list-dos--dont">${p.selection.donts.map((d) => `<li>${d}</li>`).join("")}</ul></div>`
          : ""
      }`
          : ""
      }
      <h3 class="subsection-title">Componentes implicados</h3>
      <ul class="list-dos">${p.components.map((c) => `<li>${c}</li>`).join("")}</ul>
      <p class="section-lead"><span class="header-meta">Detalle: <code>${p.ref}</code></span></p>
    </section>`
    )
    .join("")}

  <section class="section">
    <h2 id="pendientes">Pendientes de documentar</h2>
    <p class="section-lead">
      Patrones que ya existen en la app pero no están escritos. Mientras no lo estén,
      cada pantalla los resuelve a su manera.
    </p>
    <ul class="list-dos">${patterns.pending.map((p) => `<li>${p}</li>`).join("")}</ul>
  </section>
`;

setupToc();
