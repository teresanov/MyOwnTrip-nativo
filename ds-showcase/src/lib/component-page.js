/** Helpers compartidos para fichas de componente (estilo doc legible + playgrounds). */

export function escapeHtml(s) {
  return String(s)
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;");
}

export function renderComponentHeader(data, figmaLinkLabel) {
  return `
  <p class="page-header__eyebrow">Component · ${data.priority || "P0"}</p>
  <h1>${data.name} ${statusChipHtml(data.status)}</h1>
  <p class="page-header__lead">${data.purpose}</p>
  <p><a href="${data.figma}" target="_blank" rel="noopener">${figmaLinkLabel} ↗</a></p>
`;
}

function statusChipHtml(status) {
  const map = {
    draft: "chip--draft",
    "in-review": "chip--review",
    ready: "chip--ready",
    deprecated: "chip--deprecated",
  };
  return `<span class="chip ${map[status] || "chip--draft"}">${status}</span>`;
}

export function renderPlayground({ title, hint, canvasHtml, controlsHtml = "" }) {
  return `
  <section class="section playground">
    <h2>${title}</h2>
    ${hint ? `<p class="playground__hint">${hint}</p>` : ""}
    <div class="playground__frame">
      <div class="playground__canvas">${canvasHtml}</div>
      ${controlsHtml ? `<div class="playground__controls">${controlsHtml}</div>` : ""}
    </div>
  </section>
`;
}

export function renderVariantTable(variants, heading = "Variantes") {
  if (!variants?.length) return "";
  return `
  <section class="section">
    <h2>${heading}</h2>
    <p class="section-lead">Cada variante del set publicado en Figma debe poder trazarse a un estado o escenario real en Compose.</p>
    <table>
      <thead><tr><th>Variante</th><th>Cuándo usarla</th></tr></thead>
      <tbody>
        ${variants.map((v) => `<tr><td>${v.label}</td><td>${v.usage}</td></tr>`).join("")}
      </tbody>
    </table>
  </section>
`;
}

export function renderAnatomy(anatomy) {
  if (!anatomy?.length) return "";
  return `
  <section class="section">
    <h2>Anatomía</h2>
    <p class="section-lead">Partes que componen el control y cómo se nombran en Figma y en Material 3.</p>
    <ol class="list-anatomy">${anatomy.map((a) => `<li>${a}</li>`).join("")}</ol>
  </section>
`;
}

export function renderGuidelines(data) {
  return `
  <section class="section">
    <h2>Directrices</h2>
    <p class="section-lead">Patrones que el equipo ya acordó para MyOwnTrip; no sustituyen la revisión de pantalla concreta.</p>
    <h3 class="subsection-title">Hacer</h3>
    <ul class="list-dos">${data.dos.map((d) => `<li>${d}</li>`).join("")}</ul>
    <h3 class="subsection-title">Evitar</h3>
    <ul class="list-dos list-dos--dont">${data.donts.map((d) => `<li>${d}</li>`).join("")}</ul>
  </section>
`;
}

export function renderTokenSection(tokens) {
  return `
  <section class="section">
    <h2>Tokens y roles M3</h2>
    <p class="section-lead">En Compose se consumen roles de <code>MaterialTheme.colorScheme</code>, tipografía M3 e, cuando aplica, spacing MOT documentado en <code>docs/design-system/spacing.md</code>.</p>
    <ul class="token-list">${tokens.map((t) => `<li><code>${escapeHtml(t)}</code></li>`).join("")}</ul>
  </section>
`;
}

export function renderA11ySection(a11y) {
  return `
  <section class="section">
    <h2>Accesibilidad</h2>
    <ul class="list-dos">${a11y.map((a) => `<li>${a}</li>`).join("")}</ul>
  </section>
`;
}

export function renderComposeSection(compose) {
  return `
  <section class="section">
    <h2>Implementación en Compose</h2>
    <p class="section-lead">Fragmento representativo; el código fuente en <code>app/</code> es la fuente de verdad del comportamiento.</p>
    <pre><code>${escapeHtml(compose)}</code></pre>
  </section>
`;
}

export function renderChangelog(changelog) {
  return `
  <section class="section">
    <h2>Changelog</h2>
    <table>
      <thead><tr><th>Fecha</th><th>Nota</th></tr></thead>
      <tbody>
        ${changelog.map((e) => `<tr><td>${e.date}</td><td>${e.note}</td></tr>`).join("")}
      </tbody>
    </table>
  </section>
`;
}
