/**
 * Renderizadores de la ficha de componente.
 *
 * Orden de la página: primero lo que se escribe a mano (uso, reglas, accesibilidad),
 * después lo que se deriva del código (tokens, parámetros, snippet). Las secciones
 * derivadas llevan la marca "desde el código" para que la deriva se vea.
 *
 * Hay dos APIs:
 *  - Nueva (Usage-first): renderUsage, renderBehavior, renderTokens,
 *    renderImplementation, renderDecisions, renderRelated, renderA11y.
 *    La usa la ficha modelo (button).
 *  - Heredada: renderTokenSection, renderA11ySection, renderComposeSection,
 *    renderChangelog. La usan las fichas aún sin migrar; se puede borrar
 *    cuando todas pasen al esquema nuevo.
 *
 * Todas las secciones emiten <h2 id> para que el índice ancleado (toc.js) las recoja.
 */

export function escapeHtml(s) {
  return String(s)
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;");
}

const PENDING = '<span class="pending">Pendiente</span>';

function val(v) {
  return v || PENDING;
}

export function slugify(s) {
  return String(s)
    .normalize("NFD")
    .replace(/[̀-ͯ]/g, "")
    .toLowerCase()
    .replace(/<[^>]*>/g, "")
    .replace(/[^a-z0-9]+/g, "-")
    .replace(/^-|-$/g, "");
}

function sectionOpen(id, title, { lead = "", generated = false, tocLabel = "" } = {}) {
  const label = tocLabel ? ` data-toc-label="${tocLabel}"` : "";
  return `
  <section class="section">
    <h2 id="${id}"${label}>${title}${
      generated
        ? '<span class="badge-generated" title="Se deriva del código fuente; no se escribe a mano">desde el código</span>'
        : ""
    }</h2>
    ${lead ? `<p class="section-lead">${lead}</p>` : ""}`;
}

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

function statusChipHtml(status) {
  const map = {
    draft: "chip--draft",
    "in-review": "chip--review",
    ready: "chip--ready",
    deprecated: "chip--deprecated",
  };
  return `<span class="chip ${map[status] || "chip--draft"}">${status}</span>`;
}

function formatConsulted(iso) {
  const [y, m, d] = String(iso || "").split("-");
  return y && m && d ? `${d}/${m}/${y}` : iso;
}

/* ---------- Cabecera ---------- */

/**
 * Acepta las dos formas de `figma`: objeto { url, node } (esquema nuevo)
 * o string con la URL (esquema heredado).
 */
export function renderComponentHeader(data, figmaLinkLabel = "Component set en Figma") {
  const figma = typeof data.figma === "string" ? { url: data.figma } : data.figma || {};
  const impl = data.implementation || {};
  const purpose = data.usage?.purpose || data.purpose;

  const links = [
    figma.url
      ? `<a href="${figma.url}" target="_blank" rel="noopener">${
          figma.node ? `Figma · ${figma.node}` : figmaLinkLabel
        } ↗</a>`
      : "",
    impl.sourceFile ? `<span class="header-meta">Código: <code>${impl.sourceFile}</code></span>` : "",
  ]
    .filter(Boolean)
    .join('<span class="header-sep">·</span>');

  return `
    <p class="page-header__eyebrow">Componente · ${data.priority || "P0"}</p>
    <h1>${data.name} ${statusChipHtml(data.status)}</h1>
    <p class="page-header__lead">${val(purpose)}</p>
    ${links ? `<p class="page-header__links">${links}</p>` : ""}
  `;
}

/* ---------- 1. Uso ---------- */

export function renderUsage(data) {
  const u = data.usage;
  if (!u) return sectionOpen("uso", "Uso") + `<p>${PENDING}</p></section>`;

  const when = (u.when || []).length
    ? `<h3 class="subsection-title">Cuándo usarlo</h3>${list(u.when)}`
    : "";

  const whenNot = (u.whenNot || []).length
    ? `<h3 class="subsection-title">Cuándo no usarlo</h3>
       ${table(
         ["En este caso", "Usa esto en su lugar"],
         u.whenNot.map((w) => [w.case, w.href ? `<a href="${w.href}">${w.use}</a>` : w.use])
       )}`
    : "";

  const choose = (u.chooseVariant || []).length
    ? `<h3 class="subsection-title">Elegir variante</h3>
       <p class="section-lead">La jerarquía la marca la variante, no el color. Si dudas, empieza por la fila de arriba.</p>
       ${table(
         ["Si…", "Usa", "Nota"],
         u.chooseVariant.map((c) => [c.if, `<code>${c.use}</code>`, c.note || ""])
       )}`
    : "";

  return sectionOpen("uso", "Uso") + when + whenNot + choose + "</section>";
}

/* ---------- 2. Do / Don't ---------- */

export function renderGuidelines(data) {
  const g = data.guidelines;
  const rules = data.productRules;
  const dos = data.dos || [];
  const donts = data.donts || [];
  let html = "";

  if (g && (dos.length || donts.length)) {
    const links = (g.links || [{ label: g.label, url: g.url }])
      .filter((l) => l && l.url)
      .map((l) => `<a href="${l.url}" target="_blank" rel="noopener">${l.label} ↗</a>`)
      .join(" · ");
    html +=
      sectionOpen("do-dont", "Do y Don't", {
        lead: `Guía oficial de Material Design 3 para este componente: ${links} (consultada el ${formatConsulted(
          g.consulted
        )}).`,
      }) +
      `<div class="guideline-grid">
        <div class="guideline-col guideline-col--do">
          <h3 class="subsection-title">Do</h3>${list(dos)}
        </div>
        <div class="guideline-col guideline-col--dont">
          <h3 class="subsection-title">Don't</h3>${
            donts.length
              ? list(donts, "list-dos list-dos--dont")
              : `<p class="section-lead">En las fuentes consultadas, M3 no lista «Don't» explícitos para este componente.</p>`
          }
        </div>
      </div>` +
      "</section>";
  }

  if (rules && ((rules.dos || []).length || (rules.donts || []).length)) {
    const lead = g
      ? "Acuerdos del equipo sobre cómo usamos el componente en MyOwnTrip. Complementan la guía de M3; no la contradicen."
      : "Componente propio de MyOwnTrip: no existe una guía de M3 equivalente, así que estas son las reglas del equipo.";
    html +=
      sectionOpen("reglas", "Reglas MyOwnTrip", { lead }) +
      `<div class="guideline-grid">
        <div class="guideline-col guideline-col--do">
          <h3 class="subsection-title">Do</h3>${list(rules.dos || [])}
        </div>
        <div class="guideline-col guideline-col--dont">
          <h3 class="subsection-title">Don't</h3>${list(rules.donts || [], "list-dos list-dos--dont")}
        </div>
      </div>` +
      "</section>";
  }

  return html;
}

/* ---------- 3. Playground ---------- */

export function renderPlayground({ id, title, hint, canvasHtml, controlsHtml = "" }) {
  return (
    sectionOpen(id || slugify(title), title, { lead: hint }) +
    `<div class="playground__frame">
      <div class="playground__canvas">${canvasHtml}</div>
      ${controlsHtml ? `<div class="playground__controls">${controlsHtml}</div>` : ""}
    </div>` +
    "</section>"
  );
}

/* ---------- 4. Variantes ---------- */

export function renderVariantTable(variants, heading = "Variantes del set") {
  if (!variants?.length) return "";
  const hasComposable = variants.some((v) => v.composable);
  const head = hasComposable ? ["Variante", "Compose", "Cuándo usarla"] : ["Variante", "Cuándo usarla"];
  const rows = variants.map((v) =>
    hasComposable
      ? [v.label, v.composable ? `<code>${v.composable}</code>` : PENDING, v.usage]
      : [v.label, v.usage]
  );

  return (
    sectionOpen(slugify(heading), heading, {
      lead: "Cada variante publicada en Figma tiene que poder trazarse a un composable real.",
      tocLabel: "Variantes",
    }) +
    table(head, rows) +
    "</section>"
  );
}

/* ---------- 5. Anatomía ---------- */

export function renderAnatomy(anatomy) {
  if (!anatomy?.length) return "";
  return (
    sectionOpen("anatomia", "Anatomía", {
      lead: "Partes que componen el control y cómo se nombran en Figma y en Material 3.",
    }) +
    `<ol class="list-anatomy">${anatomy.map((a) => `<li>${a}</li>`).join("")}</ol>` +
    "</section>"
  );
}

/* ---------- 6. Comportamiento ---------- */

export function renderBehavior(behavior) {
  if (!behavior) return "";
  const rows = (behavior.rows || []).map((r) => [
    r.state,
    `<strong>${r.corner}</strong>`,
    `<code>${r.figma}</code>`,
    r.extra || "",
  ]);

  return (
    sectionOpen("comportamiento", "Comportamiento", { lead: behavior.lead }) +
    (rows.length ? table(["Estado", "Radio", "Token Figma", "Escala"], rows) : "") +
    (behavior.notes?.length ? `<h3 class="subsection-title">Detalles</h3>${list(behavior.notes)}` : "") +
    (behavior.exceptions?.length
      ? `<h3 class="subsection-title">Qué no lleva morph</h3>${list(behavior.exceptions)}`
      : "") +
    "</section>"
  );
}

/* ---------- 7. Accesibilidad ---------- */

export function renderA11y(a11y) {
  if (!a11y?.length) return "";
  return (
    sectionOpen("accesibilidad", "Accesibilidad", {
      lead: 'Se comprueba en el gate <code>a11yCheck</code> antes de cerrar la pantalla.',
    }) +
    list(a11y) +
    "</section>"
  );
}

/* ---------- 8. Tokens (derivado) ---------- */

export function renderTokens(tokens) {
  if (!tokens) return "";
  const groups = [
    ["Color", tokens.color],
    ["Forma", tokens.shape],
    ["Espaciado", tokens.spacing],
    ["Tipografía", tokens.type],
  ].filter(([, v]) => v?.length);

  if (!groups.length) return "";

  return (
    sectionOpen("tokens", "Tokens y roles", {
      lead:
        "Roles consumidos en runtime. Ningún valor literal: si falta un rol, se añade al tema, no al componente.",
      generated: true,
    }) +
    groups
      .map(
        ([label, items]) => `
        <h3 class="subsection-title">${label}</h3>
        ${table(["Rol", "Para qué"], items.map((t) => [`<code>${t.token}</code>`, t.use]))}`
      )
      .join("") +
    "</section>"
  );
}

/* ---------- 9. Implementación (derivado) ---------- */

export function renderImplementation(impl) {
  if (!impl) return "";
  const params = (impl.params || []).map((p) => [
    `<code>${p.name}</code>`,
    `<code>${escapeHtml(p.type)}</code>`,
    p.default === "—" ? "—" : `<code>${escapeHtml(p.default)}</code>`,
    p.note || "",
  ]);

  return (
    sectionOpen("implementacion", "Implementación", {
      lead: `${impl.note || ""} Módulo <code>${impl.module || "?"}</code> · fuente <code>${
        impl.sourceFile || "?"
      }</code>`,
      generated: true,
    }) +
    (impl.import ? `<pre><code>import ${escapeHtml(impl.import)}</code></pre>` : "") +
    (params.length
      ? `<h3 class="subsection-title">Parámetros de <code>${impl.composable}</code></h3>
         ${table(["Parámetro", "Tipo", "Por defecto", "Nota"], params)}`
      : "") +
    (impl.snippet
      ? `<h3 class="subsection-title">Uso típico</h3><pre><code>${escapeHtml(impl.snippet)}</code></pre>`
      : "") +
    (impl.variantsSnippet
      ? `<h3 class="subsection-title">Otras variantes</h3><pre><code>${escapeHtml(
          impl.variantsSnippet
        )}</code></pre>`
      : "") +
    "</section>"
  );
}

/* ---------- 10. Decisiones y changelog ---------- */

export function renderDecisions(data) {
  const d = data.decisions || {};
  const adr = (d.adr || []).map((a) => `<li><code>${a.ref}</code> — ${a.label}</li>`).join("");

  return (
    sectionOpen("decisiones", "Decisiones") +
    `<dl class="decisions__list">
      <dt>Fuente</dt><dd>${val(d.source)}</dd>
      <dt>Por qué</dt><dd>${val(d.why)}</dd>
      <dt>Capa</dt><dd>${val(d.layer)}</dd>
    </dl>
    ${adr ? `<h3 class="subsection-title">ADR</h3><ul class="list-dos">${adr}</ul>` : ""}
    ${
      data.changelog?.length
        ? `<h3 class="subsection-title">Changelog</h3>${table(
            ["Fecha", "Nota"],
            data.changelog.map((e) => [e.date, e.note])
          )}`
        : ""
    }` +
    "</section>"
  );
}

/* ---------- Relacionados ---------- */

export function renderRelated(related) {
  if (!related?.length) return "";
  return (
    sectionOpen("relacionados", "Relacionados") +
    `<div class="related-row">${related
      .map((r) => `<a class="related-link" href="${r.href}">${r.label}</a>`)
      .join("")}</div>` +
    "</section>"
  );
}

/* ====================================================================
 * API heredada — la usan las fichas aún sin migrar al esquema nuevo.
 * Borrar cuando todas usen renderTokens / renderA11y / renderImplementation.
 * ==================================================================== */

export function renderTokenSection(tokens) {
  if (!tokens?.length) return "";
  return (
    sectionOpen("tokens", "Tokens y roles M3", {
      lead:
        "En Compose se consumen roles de <code>MaterialTheme.colorScheme</code>, tipografía M3 e, cuando aplica, spacing MOT documentado en <code>docs/design-system/spacing.md</code>.",
    }) +
    `<ul class="token-list">${tokens.map((t) => `<li><code>${escapeHtml(t)}</code></li>`).join("")}</ul>` +
    "</section>"
  );
}

export function renderA11ySection(a11y) {
  if (!a11y?.length) return "";
  return sectionOpen("accesibilidad", "Accesibilidad") + list(a11y) + "</section>";
}

export function renderComposeSection(compose) {
  if (!compose) return "";
  return (
    sectionOpen("implementacion", "Implementación en Compose", {
      lead:
        "Fragmento representativo; el código fuente en <code>app/</code> es la fuente de verdad del comportamiento.",
    }) +
    `<pre><code>${escapeHtml(compose)}</code></pre>` +
    "</section>"
  );
}

export function renderChangelog(changelog) {
  if (!changelog?.length) return "";
  return (
    sectionOpen("changelog", "Changelog") +
    table(["Fecha", "Nota"], changelog.map((e) => [e.date, e.note])) +
    "</section>"
  );
}
