/**
 * Renderizador genérico de páginas de foundations.
 * El contenido vive en src/data/foundations/<id>.json; esta función solo lo pinta,
 * así que añadir una foundation no requiere escribir JS nuevo.
 */

function table(head, rows) {
  return `
    <table>
      <thead><tr>${head.map((h) => `<th>${h}</th>`).join("")}</tr></thead>
      <tbody>${rows.map((r) => `<tr>${r.map((c) => `<td>${c}</td>`).join("")}</tr>`).join("")}</tbody>
    </table>`;
}

function list(items, cls = "list-dos") {
  return `<ul class="${cls}">${items.map((i) => `<li>${i}</li>`).join("")}</ul>`;
}

export function renderFoundationHeader(data) {
  return `
    <p class="page-header__eyebrow">${data.eyebrow || "Foundations"}</p>
    <h1>${data.name}</h1>
    <p class="page-header__lead">${data.lead}</p>
    ${
      data.sourceOfTruth
        ? `<p class="page-header__links"><span class="header-meta">Fuente de verdad: <code>${data.sourceOfTruth}</code></span></p>`
        : ""
    }
  `;
}

export function renderFoundationBody(data) {
  const sections = (data.sections || [])
    .map((s) => {
      const guidelines =
        (s.dos || []).length || (s.donts || []).length
          ? `<div class="guideline-grid">
              ${
                (s.dos || []).length
                  ? `<div class="guideline-col guideline-col--do"><h3 class="subsection-title">Do</h3>${list(s.dos)}</div>`
                  : ""
              }
              ${
                (s.donts || []).length
                  ? `<div class="guideline-col guideline-col--dont"><h3 class="subsection-title">Don't</h3>${list(
                      s.donts,
                      "list-dos list-dos--dont"
                    )}</div>`
                  : ""
              }
            </div>`
          : "";

      return `
      <section class="section">
        <h2 id="${s.id}">${s.title}</h2>
        ${s.lead ? `<p class="section-lead">${s.lead}</p>` : ""}
        ${s.table ? table(s.table.head, s.table.rows) : ""}
        ${s.notes?.length ? `<h3 class="subsection-title">Notas</h3>${list(s.notes)}` : ""}
        ${guidelines}
      </section>`;
    })
    .join("");

  const refs = (data.refs || []).length
    ? `<section class="section">
        <h2 id="referencias">Referencias</h2>
        <ul class="list-dos">${data.refs
          .map((r) => `<li><code>${r.ref}</code>${r.label !== r.ref ? ` — ${r.label}` : ""}</li>`)
          .join("")}</ul>
      </section>`
    : "";

  return sections + refs;
}

export function mountFoundation(data) {
  const header = document.getElementById("page-header");
  const main = document.getElementById("foundation-main");
  if (header) header.innerHTML = renderFoundationHeader(data);
  if (main) main.innerHTML = renderFoundationBody(data);
}
