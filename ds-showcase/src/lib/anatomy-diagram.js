/**
 * Diagramas de anatomía y medidas (estilo EightShapes Specs), dibujados en SVG con los
 * roles de color del tema y los valores reales de la ficha (`anatomyDiagram.values`, en dp).
 * Una sola fuente: si cambia un valor en el JSON, cambian el dibujo y la tabla de medidas.
 *
 * `anatomyDiagram` = { kind: "button" | "icon-button" | "list-item" | "filter-chip" | "dialog", values, measures }
 * Los marcadores 1..n siguen el orden de `anatomy[]`.
 */
import { sectionOpen, table } from "./component-page.js";
import { liveHostHtml } from "./live-specimen.js";
import { eyebrowHtml, fieldHtml, snackbarHtml } from "./specimens.js";

const FONT = "Inter, system-ui, sans-serif";

/* ---------- primitivas SVG ---------- */

const svg = (w, h, body, label) =>
  `<svg class="ad-svg" viewBox="0 0 ${w} ${h}" role="img" aria-label="${label}" xmlns="http://www.w3.org/2000/svg">${body}</svg>`;

/** Marcador numerado con línea guía: círculo en (cx,cy), línea hasta (tx,ty). */
const marker = (n, cx, cy, tx, ty) =>
  `<line x1="${cx}" y1="${cy}" x2="${tx}" y2="${ty}" class="ad-leader"/>` +
  `<circle cx="${tx}" cy="${ty}" r="3" class="ad-dot"/>` +
  `<circle cx="${cx}" cy="${cy}" r="15" class="ad-badge"/>` +
  `<text x="${cx}" y="${cy + 5}" text-anchor="middle" class="ad-badge-text">${n}</text>`;

/** Cota de medida: caja translúcida con su valor. */
const box = (x, y, w, h, text, cls = "ad-pad") =>
  `<rect x="${x}" y="${y}" width="${w}" height="${h}" class="${cls}"/>` +
  (text
    ? `<text x="${x + w / 2}" y="${y + h / 2 + 4}" text-anchor="middle" class="ad-dim">${text}</text>`
    : "");

const glyph = (x, y, s, color) =>
  // «add» de Material, escalado a s px
  `<path d="M${x + s * 0.5} ${y + s * 0.15}v${s * 0.7}M${x + s * 0.15} ${y + s * 0.5}h${s * 0.7}" stroke="${color}" stroke-width="${Math.max(1.5, s * 0.08)}" stroke-linecap="square" fill="none"/>`;

/* ---------- Button: el HTML del mot-btn, no un SVG inventado ---------- */

export const STARS_ICON = `<svg class="mot-btn__icon" viewBox="0 0 24 24" width="18" height="18" aria-hidden="true"><circle cx="12" cy="12" r="10" fill="currentColor"/><path fill="var(--primary)" d="M12 6.8 13.6 11h4.6l-3.7 2.7 1.4 4.3L12 15.2l-3.9 2.8 1.4-4.3L5.8 11h4.6L12 6.8z"/></svg>`;

export const CHIP_CHECK = `<svg class="filter-chip__icon" viewBox="0 0 24 24" width="18" height="18" aria-hidden="true"><path fill="currentColor" d="M9.55 18 3.85 12.3l1.4-1.4 4.3 4.3 9.2-9.2 1.4 1.4z"/></svg>`;

export const DIALOG_ERROR = `<svg class="dialog-demo__icon-svg" viewBox="0 0 24 24" width="24" height="24" aria-hidden="true"><path fill="currentColor" d="M12 2 1 21h22L12 2zm1 15h-2v-2h2v2zm0-4h-2V9h2v4z"/></svg>`;

function button(v) {
  const anatomy = `
    <div class="ad-live">
      <button type="button" class="mot-btn mot-btn--filled" tabindex="-1">
        ${STARS_ICON}Abrir cuaderno
      </button>
    </div>`;

  const pill = (n, kind) => `<span class="spec-n spec-n--${kind}">${n}</span>`;

  const measures = `
    <div class="spec-stage">
      <div class="spec-live" style="--spec-pad-x:${v.padH}px;--spec-pad-y:${v.padV}px;--spec-gap:${v.gap}px;--spec-icon:${v.icon}px">
        <button type="button" class="mot-btn mot-btn--filled spec-ghost" tabindex="-1">${STARS_ICON}Abrir cuaderno</button>
        <div class="spec-overlay" aria-hidden="true">
          <span class="spec-band spec-band--pad-l">${pill(v.padH, "pad")}</span>
          <span class="spec-band spec-band--pad-t">${pill(v.padV, "pad")}</span>
          <span class="spec-band spec-band--pad-b"></span>
          <span class="spec-band spec-band--pad-r"></span>
          <span class="spec-band spec-band--gap">${pill(v.gap, "gap")}</span>
        </div>
      </div>
    </div>`;

  return { anatomy, measures };
}

/* ---------- Icon button ---------- */

function iconButton(v) {
  const s = 3;
  const c = v.container * s;
  const x = (560 - c) / 2;
  const y = 70;
  const t = v.touch * s;
  const ty = y - (t - c) / 2;
  const tx = x - (t - c) / 2;
  const i = v.icon * s;
  const ix = x + (c - i) / 2;
  const iy = y + (c - i) / 2;

  const piece = `<rect x="${x}" y="${y}" width="${c}" height="${c}" class="ad-fill-surface"/>` +
    glyph(ix, iy, i, "var(--on-surface-variant)");

  const anatomy =
    `<rect x="${tx}" y="${ty}" width="${t}" height="${t}" class="ad-touch"/>` +
    piece +
    `<rect x="${x + 5}" y="${y + 5}" width="${c - 10}" height="${c - 10}" class="ad-layer"/>` +
    marker(1, x - 80, y + 20, x, y + 20) +
    marker(2, x + c + 80, y + 20, ix + i, iy + i / 2) +
    marker(3, x - 80, y + c - 20, x + 6, y + c - 20) +
    marker(4, 280, ty + t + 42, 280, ty + t);

  const pad = ((v.container - v.icon) / 2) * s;
  const measures =
    piece +
    box(x, y, pad, c, (v.container - v.icon) / 2) +
    box(x + c - pad, y, pad, c, (v.container - v.icon) / 2) +
    box(x + pad, y, i, pad, "", "ad-pad ad-pad-v") +
    box(x + pad, y + c - pad, i, pad, "", "ad-pad ad-pad-v") +
    `<line x1="${x}" y1="${y + c + 22}" x2="${x + c}" y2="${y + c + 22}" class="ad-line"/>` +
    `<text x="${x + c / 2}" y="${y + c + 40}" text-anchor="middle" class="ad-dim ad-dim-out">${v.container} dp</text>` +
    `<line x1="${tx}" y1="${ty - 18}" x2="${tx + t}" y2="${ty - 18}" class="ad-line ad-line-touch"/>` +
    `<text x="${tx + t / 2}" y="${ty - 26}" text-anchor="middle" class="ad-dim ad-dim-out">${v.touch} dp (táctil)</text>`;

  return { anatomy: svg(560, 260, anatomy, "Anatomía del icon button"), measures: svg(560, 260, measures, "Medidas del icon button") };
}

/* ---------- List item ---------- */

function listItem(v) {
  const s = 1.5;
  const W = 360 * s;
  const x = (560 - W) / 2;
  const y = 58;
  const circle = v.circle * s;
  const padH = v.padH * s;
  const padV = v.padV * s;
  const rowH = (v.circle + v.padV * 2 + 8) * s;
  const cx = x + padH + circle / 2;
  const cy = y + rowH / 2;
  const tx = x + padH + circle + v.gap * s;
  const glyphSize = v.glyph * s;

  const row =
    `<rect x="${x}" y="${y}" width="${W}" height="${rowH}" class="ad-fill-row"/>` +
    `<rect x="${x}" y="${y}" width="4" height="${rowH}" class="ad-drift"/>` +
    `<circle cx="${cx}" cy="${cy}" r="${circle / 2}" class="ad-fill-container"/>` +
    glyph(cx - glyphSize / 2, cy - glyphSize / 2, glyphSize, "var(--on-surface-variant)") +
    `<circle cx="${cx + circle / 2 - 4}" cy="${cy - circle / 2 + 4}" r="${(v.dot * s) / 2}" class="ad-drift-dot"/>` +
    `<text x="${tx}" y="${cy - 4}" font-family="${FONT}" font-weight="500" font-size="${16 * s * 0.8}" fill="var(--on-surface)">Vuelo Lisboa – Madrid</text>` +
    `<text x="${tx}" y="${cy + 18}" font-family="${FONT}" font-size="${14 * s * 0.8}" fill="var(--on-surface-variant)">Vuelo · 14 mar · plan distinto</text>` +
    `<g class="ad-trailing"><circle cx="${x + W - padH - 6}" cy="${cy}" r="3" fill="var(--on-surface-variant)"/><circle cx="${x + W - padH - 6}" cy="${cy - 9}" r="3" fill="var(--on-surface-variant)"/><circle cx="${x + W - padH - 6}" cy="${cy + 9}" r="3" fill="var(--on-surface-variant)"/></g>`;

  const anatomy =
    row +
    marker(1, x + W - 20, y - 26, x + W - 20, y) +
    marker(2, cx, y - 26, cx, cy - circle / 2 + 2) +
    marker(3, tx + 70, y - 26, tx + 70, cy - 18) +
    marker(4, tx + 150, y + rowH + 34, tx + 150, cy + 12) +
    marker(5, x + W - 70, y + rowH + 34, x + W - padH - 6, cy + 14) +
    marker(6, x + 40, y + rowH + 34, x + 2, y + rowH - 6);

  const measures =
    row +
    box(x + 4, y, padH - 4, rowH, v.padH, "ad-pad") +
    box(x + padH, y, circle, padV, v.padV, "ad-pad ad-pad-v") +
    box(x + padH, y + rowH - padV, circle, padV, v.padV, "ad-pad ad-pad-v") +
    box(x + padH + circle, cy - 8, v.gap * s, 16, "", "ad-gap") +
    `<text x="${x + padH + circle + (v.gap * s) / 2}" y="${y + rowH + 26}" text-anchor="middle" class="ad-dim ad-dim-out">${v.gap}</text>` +
    `<text x="${cx}" y="${y + rowH + 26}" text-anchor="middle" class="ad-dim ad-dim-out">${v.circle}</text>`;

  return { anatomy: svg(560, 200, anatomy, "Anatomía del list item"), measures: svg(560, 170, measures, "Medidas del list item") };
}

function filterChip(v) {
  const selected = `<button type="button" class="filter-chip filter-chip--selected" tabindex="-1">${CHIP_CHECK}Activos</button>`;
  const anatomy = `
    <div class="ad-live">
      <div class="filter-chip-row">
        ${selected}
        <button type="button" class="filter-chip" tabindex="-1">Archivados</button>
        <button type="button" class="filter-chip" tabindex="-1">Todos</button>
      </div>
    </div>`;

  const pill = (n, kind) => `<span class="spec-n spec-n--${kind}">${n}</span>`;
  const measures = `
    <div class="spec-stage">
      <div class="spec-live spec-live--chip" style="--spec-pad-x:${v.padHSelected}px;--spec-pad-y:${v.padV}px;--spec-gap:${v.gap}px;--spec-icon:${v.icon}px">
        <button type="button" class="filter-chip filter-chip--selected spec-ghost" tabindex="-1">${CHIP_CHECK}Activos</button>
        <div class="spec-overlay" aria-hidden="true">
          <span class="spec-band spec-band--pad-l">${pill(v.padHSelected, "pad")}</span>
          <span class="spec-band spec-band--pad-t">${pill(v.padV, "pad")}</span>
          <span class="spec-band spec-band--pad-b"></span>
          <span class="spec-band spec-band--pad-r"></span>
          <span class="spec-band spec-band--gap">${pill(v.gap, "gap")}</span>
        </div>
      </div>
    </div>`;

  return { anatomy, measures };
}

function dialog(v) {
  const inner = `
      <div class="dialog-demo__icon">${DIALOG_ERROR}</div>
      <h3 class="dialog-demo__title">¿Eliminar documento?</h3>
      <p class="dialog-demo__body">Se borrará el documento y su archivo del dispositivo. Esta acción no se puede deshacer.</p>
      <div class="dialog-demo__actions">
        <button type="button" class="mot-btn mot-btn--text" tabindex="-1">Cancelar</button>
        <button type="button" class="mot-btn mot-btn--text dialog-demo__destructive" tabindex="-1">Eliminar</button>
      </div>`;

  const anatomy = `<div class="ad-live ad-live--dialog"><div class="dialog-demo">${inner}</div></div>`;
  const pill = (n, kind) => `<span class="spec-n spec-n--${kind}">${n}</span>`;
  const measures = `
    <div class="spec-stage spec-stage--dialog">
      <div class="spec-live spec-live--dialog" style="--spec-pad-x:${v.pad}px;--spec-pad-y:${v.pad}px;--spec-gap:${v.actions}px">
        <div class="dialog-demo spec-ghost">${inner}</div>
        <div class="spec-overlay" aria-hidden="true">
          <span class="spec-band spec-band--pad-l">${pill(v.pad, "pad")}</span>
          <span class="spec-band spec-band--pad-t">${pill(v.pad, "pad")}</span>
          <span class="spec-band spec-band--pad-b"></span>
          <span class="spec-band spec-band--pad-r"></span>
        </div>
      </div>
    </div>`;

  return { anatomy, measures };
}

function snackbar(v) {
  const anatomy = `<div class="ad-live">${snackbarHtml()}</div>`;
  const pill = (n, kind) => `<span class="spec-n spec-n--${kind}">${n}</span>`;
  const measures = `
    <div class="spec-stage">
      <div class="spec-live spec-live--snackbar" style="--spec-pad-x:${v.padH}px;--spec-pad-y:${v.padV}px;--spec-gap:${v.gap}px;--spec-icon:0px">
        <div class="spec-ghost">${snackbarHtml()}</div>
        <div class="spec-overlay" aria-hidden="true">
          <span class="spec-band spec-band--pad-l">${pill(v.padH, "pad")}</span>
          <span class="spec-band spec-band--pad-t">${pill(v.padV, "pad")}</span>
          <span class="spec-band spec-band--pad-b"></span>
          <span class="spec-band spec-band--pad-r"></span>
        </div>
      </div>
    </div>`;
  return { anatomy, measures };
}

function textField(v) {
  const anatomy = `<div class="ad-live ad-live--field">${fieldHtml({
    label: "Nombre del viaje",
    value: "Barcelona 2026",
  })}</div>`;
  const pill = (n, kind) => `<span class="spec-n spec-n--${kind}">${n}</span>`;
  const measures = `
    <div class="spec-stage">
      <div class="spec-live spec-live--field" style="--spec-pad-x:${v.padH}px;--spec-pad-y:${v.padV}px">
        <div class="spec-ghost">${fieldHtml({ label: "Nombre del viaje", value: "Barcelona 2026" })}</div>
        <div class="spec-overlay" aria-hidden="true">
          <span class="spec-band spec-band--pad-l">${pill(v.padH, "pad")}</span>
          <span class="spec-band spec-band--pad-t">${pill(v.padV, "pad")}</span>
          <span class="spec-band spec-band--pad-b"></span>
          <span class="spec-band spec-band--pad-r"></span>
        </div>
      </div>
    </div>`;
  return { anatomy, measures };
}

function eyebrow() {
  const anatomy = `<div class="ad-live">
    ${eyebrowHtml("Próximo viaje", "tertiary", "md")}
    ${eyebrowHtml("En destino", "surface", "md")}
    ${eyebrowHtml("Recuerdo", "secondary", "sm")}
  </div>`;
  return { anatomy, measures: anatomy };
}

const BUILDERS = {
  button,
  "icon-button": iconButton,
  "list-item": listItem,
  "filter-chip": filterChip,
  dialog,
  snackbar,
  "text-field": textField,
  eyebrow,
};

/* ---------- Secciones ---------- */

/** Anatomía: dibujo con marcadores a la izquierda y leyenda numerada (anatomy[]) a la derecha. */
/** Anatomía sobre el componente real: marcadores con línea guía hasta cada pieza medida en Compose. */
function renderLiveAnatomy(anatomy, live) {
  const legend = `<ol class="list-anatomy ad-legend" id="li-legend">${anatomy
    .map((a, i) => `<li tabindex="0" data-part="${live.markers[i]?.part || ""}">${a}</li>`)
    .join("")}</ol>`;
  return (
    sectionOpen("anatomia", "Anatomía", {
      lead: "Las partes del componente y cómo las llamamos en diseño y en código. El dibujo es el componente real de la app: cada línea llega a la pieza que mide Compose. Pasa el cursor por la lista para resaltar cada parte.",
    }) +
    `<div class="ad-layout ad-layout--live">
       <figure class="ad-figure li-wrap" data-parts='${JSON.stringify(declaredParts(live))}' data-inspect="anatomy" data-legend="#li-legend" data-markers='${JSON.stringify(live.markers)}'>
         ${liveHostHtml(live.specimen, { zoom: live.zoom, theme: "light", ...(live.props || {}) }, { height: live.height, label: "Anatomía del componente real" })}
         <svg class="li-overlay" aria-hidden="true"></svg>
       </figure>
       ${legend}
     </div></section>`
  );
}

/** Medidas sobre el componente real: inspector al pasar el cursor y tabla medida en Compose. */
function renderLiveMeasures(diagram) {
  const live = diagram.live;
  const v = diagram.values || {};
  return (
    sectionOpen("medidas", "Medidas", {
      lead: "Pasa el cursor sobre el componente: como el inspector del navegador, se resalta cada pieza con su tamaño y sus distancias en dp. En el contenedor, el verde es el espacio interior y el azul, el contenido. Los números se miden en el componente real, no se escriben a mano.",
    }) +
    `<div class="ad-layout ad-layout--live">
       <figure class="ad-figure li-wrap" data-parts='${JSON.stringify(declaredParts(live))}' data-inspect="measures" data-table="#li-measures" data-rows='${JSON.stringify(live.rows || null)}' data-gaps='${JSON.stringify(live.gaps || [])}'>
         ${liveHostHtml(live.specimen, { zoom: live.zoom, theme: "light", ...(live.props || {}) }, { height: live.height, label: "Medidas del componente real" })}
         <svg class="li-overlay" aria-hidden="true"></svg>
         <div class="li-hit" aria-hidden="true"></div>
       </figure>
       <div>
         <table><thead><tr><th>Medida</th><th>Medido en Compose</th></tr></thead>
           <tbody id="li-measures"><tr><td colspan="2">Midiendo el componente…</td></tr></tbody></table>
         ${v.padH != null ? `<p class="li-note">Padding declarado en código: ${v.padH} × ${v.padV} dp. El espacio arriba y abajo puede ser mayor porque el contenedor tiene una altura mínima y centra el contenido.</p>` : ""}
       </div>
     </div></section>`
  );
}

/** Piezas que la ficha declara: marcadores, filas de medidas y huecos. */
function declaredParts(live) {
  const names = new Set((live.markers || []).map((m) => m.part));
  for (const r of live.rows || []) for (const k of ["size", "left", "right", "top", "gap", "vgap"]) [].concat(r[k] || []).forEach((n) => names.add(n));
  for (const g of live.gaps || []) g.forEach((n) => names.add(n));
  return [...names];
}

export function renderAnatomy(anatomy, diagram) {
  if (!anatomy?.length) return "";
  if (diagram?.live) return renderLiveAnatomy(anatomy, diagram.live);
  const built = diagram && BUILDERS[diagram.kind]?.(diagram.values);
  const legend = `<ol class="list-anatomy ad-legend">${anatomy.map((a) => `<li>${a}</li>`).join("")}</ol>`;
  return (
    sectionOpen("anatomia", "Anatomía", {
      lead: built
        ? "Las partes del componente y cómo las llamamos en diseño y en código."
        : "Partes que componen el control y cómo se nombran en Figma y en Material 3.",
    }) +
    (built ? `<div class="ad-layout"><figure class="ad-figure">${built.anatomy}</figure>${legend}</div>` : legend) +
    "</section>"
  );
}

/** Medidas: mismo componente con las cotas (padding, huecos, área táctil) y la tabla de valores. */
export function renderMeasures(diagram) {
  if (diagram?.live) return renderLiveMeasures(diagram);
  const built = diagram && BUILDERS[diagram.kind]?.(diagram.values);
  if (!built) return "";
  return (
    sectionOpen("medidas", "Medidas", {
      lead: "El control se ve en velo; en color, solo padding y huecos (1 px = 1 dp).",
    }) +
    `<div class="ad-layout"><figure class="ad-figure">${built.measures}</figure>` +
    (diagram.measures?.length ? table(["Medida", "Valor"], diagram.measures.map((m) => [m.label, `<strong>${m.value}</strong>`])) : "") +
    "</div></section>"
  );
}
