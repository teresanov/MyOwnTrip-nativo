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

function guidelineList(items, cls = "list-dos") {
  return `<ul class="${cls}">${items.map((i) => `<li>${i}</li>`).join("")}</ul>`;
}

function formatConsulted(iso) {
  const [y, m, d] = String(iso || "").split("-");
  return y && m && d ? `${d}/${m}/${y}` : iso;
}

/**
 * Do / Don't de la guía oficial de M3 (si el componente existe en M3) y,
 * aparte, las reglas del equipo MyOwnTrip. Los componentes propios no tienen guía M3.
 */
export function renderGuidelines(data) {
  const g = data.guidelines;
  const rules = data.productRules;
  const dos = data.dos || [];
  const donts = data.donts || [];
  let html = "";

  if (g && (dos.length || donts.length)) {
    const links = (g.links || [{ label: g.label, url: g.url }])
      .map((l) => `<a href="${l.url}" target="_blank" rel="noopener">${l.label} ↗</a>`)
      .join(" · ");
    html += `
  <section class="section">
    <h2>Do y Don't</h2>
    <p class="section-lead">Guía oficial de Material Design 3 para este componente: ${links} (consultada el ${formatConsulted(g.consulted)}).</p>
    ${dos.length ? `<h3 class="subsection-title">Do</h3>${guidelineList(dos)}` : ""}
    ${donts.length ? `<h3 class="subsection-title">Don't</h3>${guidelineList(donts, "list-dos list-dos--dont")}` : `<p class="section-lead">En las fuentes consultadas, M3 no lista «Don't» explícitos para este componente.</p>`}
  </section>
`;
  }

  if (rules && ((rules.dos || []).length || (rules.donts || []).length)) {
    const lead = g
      ? "Acuerdos del equipo sobre cómo usamos el componente en MyOwnTrip; complementan la guía de M3."
      : "Componente propio de MyOwnTrip: no existe una guía de M3 equivalente, así que estas son las reglas del equipo.";
    html += `
  <section class="section">
    <h2>Reglas MyOwnTrip</h2>
    <p class="section-lead">${lead}</p>
    ${(rules.dos || []).length ? `<h3 class="subsection-title">Do</h3>${guidelineList(rules.dos)}` : ""}
    ${(rules.donts || []).length ? `<h3 class="subsection-title">Don't</h3>${guidelineList(rules.donts, "list-dos list-dos--dont")}` : ""}
  </section>
`;
  }

  return html;
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
