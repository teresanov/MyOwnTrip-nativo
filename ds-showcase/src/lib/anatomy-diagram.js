/**
 * Diagramas de anatomía y medidas (estilo EightShapes Specs), dibujados en SVG con los
 * roles de color del tema y los valores reales de la ficha (`anatomyDiagram.values`, en dp).
 * Una sola fuente: si cambia un valor en el JSON, cambian el dibujo y la tabla de medidas.
 *
 * `anatomyDiagram` = { kind: "button" | "icon-button" | "list-item", values: {...}, measures: [{label, value}] }
 * Los marcadores 1..n siguen el orden de `anatomy[]`.
 */
import { sectionOpen, table } from "./component-page.js";

const FONT = "Inter, system-ui, sans-serif";

/* ---------- primitivas SVG ---------- */

const svg = (w, h, body, label) =>
  `<svg class="ad-svg" viewBox="0 0 ${w} ${h}" role="img" aria-label="${label}" xmlns="http://www.w3.org/2000/svg">${body}</svg>`;

/** Marcador numerado con línea guía: círculo en (cx,cy), línea hasta (tx,ty). */
const marker = (n, cx, cy, tx, ty) =>
  `<line x1="${cx}" y1="${cy}" x2="${tx}" y2="${ty}" class="ad-leader"/>` +
  `<circle cx="${tx}" cy="${ty}" r="3" class="ad-dot"/>` +
  `<circle cx="${cx}" cy="${cy}" r="13" class="ad-badge"/>` +
  `<text x="${cx}" y="${cy + 5}" text-anchor="middle" class="ad-badge-text">${n}</text>`;

/** Cota de medida: caja translúcida con su valor. */
const box = (x, y, w, h, text, cls = "ad-pad") =>
  `<rect x="${x}" y="${y}" width="${w}" height="${h}" class="${cls}"/>` +
  (text
    ? `<text x="${x + w / 2}" y="${y + h / 2 + 4}" text-anchor="middle" class="ad-dim">${text}</text>`
    : "");

const glyph = (x, y, s, color) =>
  // «add» de Material, escalado a s px
  `<path d="M${x + s * 0.5} ${y + s * 0.15}v${s * 0.7}M${x + s * 0.15} ${y + s * 0.5}h${s * 0.7}" stroke="${color}" stroke-width="${s * 0.1}" fill="none"/>`;

/* ---------- Button ---------- */

function button(v) {
  const s = 2; // 1 dp = 2 px de dibujo
  const w = (v.padH * 2 + v.icon + v.gap + v.label) * s;
  const h = v.height * s;
  const x = (560 - w) / 2;
  const y = 112;
  const touchH = v.touch * s;
  const ty = y - (touchH - h) / 2;
  const ix = x + v.padH * s;
  const lx = ix + (v.icon + v.gap) * s;
  const base = y + h / 2 + 5;

  const piece = (extra) =>
    `<rect x="${x}" y="${y}" width="${w}" height="${h}" class="ad-fill-primary"/>` +
    glyph(ix, y + (h - v.icon * s) / 2, v.icon * s, "var(--on-primary)") +
    `<text x="${lx}" y="${base}" font-family="${FONT}" font-weight="500" font-size="${14 * s}" fill="var(--on-primary)" textLength="${v.label * s}" lengthAdjust="spacingAndGlyphs">Guardar gasto</text>` +
    (extra || "");

  const anatomy =
    `<rect x="${x}" y="${ty}" width="${w}" height="${touchH}" class="ad-touch"/>` +
    piece(`<rect x="${x + 5}" y="${y + 5}" width="${w - 10}" height="${h - 10}" class="ad-layer"/>`) +
    marker(1, x - 58, y + 18, x, y + 18) +
    marker(2, lx + (v.label * s) / 2, 48, lx + (v.label * s) / 2, y + 22) +
    marker(3, ix + (v.icon * s) / 2, 48, ix + (v.icon * s) / 2, y + (h - v.icon * s) / 2) +
    marker(4, x + w + 58, y + h - 18, x + w - 6, y + h - 18) +
    marker(5, 280, ty + touchH + 50, 280, ty + touchH);

  const px = v.padH * s;
  const py = v.padV * s;
  const measures =
    piece() +
    box(x, y, px, h, v.padH, "ad-pad") +
    box(x + w - px, y, px, h, v.padH, "ad-pad") +
    box(x + px, y, w - px * 2, py, v.padV, "ad-pad ad-pad-v") +
    box(x + px, y + h - py, w - px * 2, py, v.padV, "ad-pad ad-pad-v") +
    box(ix + v.icon * s, y + (h - 14) / 2, v.gap * s, 14, "", "ad-gap") +
    `<text x="${ix + v.icon * s + (v.gap * s) / 2}" y="${y + h + 22}" text-anchor="middle" class="ad-dim ad-dim-out">${v.gap}</text>` +
    `<line x1="${x - 24}" y1="${y}" x2="${x - 24}" y2="${y + h}" class="ad-line"/>` +
    `<text x="${x - 32}" y="${y + h / 2 + 4}" text-anchor="end" class="ad-dim ad-dim-out">${v.height}</text>` +
    `<line x1="${x + w + 24}" y1="${ty}" x2="${x + w + 24}" y2="${ty + touchH}" class="ad-line ad-line-touch"/>` +
    `<text x="${x + w + 32}" y="${ty + touchH / 2 + 4}" class="ad-dim ad-dim-out">${v.touch} (táctil)</text>`;

  return { anatomy: svg(560, 280, anatomy, "Anatomía del botón"), measures: svg(560, 220, measures, "Medidas del botón") };
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

const BUILDERS = { button, "icon-button": iconButton, "list-item": listItem };

/* ---------- Secciones ---------- */

/** Anatomía: dibujo con marcadores a la izquierda y leyenda numerada (anatomy[]) a la derecha. */
export function renderAnatomy(anatomy, diagram) {
  if (!anatomy?.length) return "";
  const built = diagram && BUILDERS[diagram.kind]?.(diagram.values);
  const legend = `<ol class="list-anatomy ad-legend">${anatomy.map((a) => `<li>${a}</li>`).join("")}</ol>`;
  return (
    sectionOpen("anatomia", "Anatomía", {
      lead: built
        ? "Las partes del componente y cómo las llamamos en diseño y en código. El número del dibujo es el de la lista."
        : "Partes que componen el control y cómo se nombran en Figma y en Material 3.",
    }) +
    (built ? `<div class="ad-layout"><figure class="ad-figure">${built.anatomy}</figure>${legend}</div>` : legend) +
    "</section>"
  );
}

/** Medidas: mismo componente con las cotas (padding, huecos, área táctil) y la tabla de valores. */
export function renderMeasures(diagram) {
  const built = diagram && BUILDERS[diagram.kind]?.(diagram.values);
  if (!built) return "";
  return (
    sectionOpen("medidas", "Medidas", {
      lead: "Espacios del componente en dp (1 dp del dibujo = 2 px). Salen de los mismos valores que usa la app.",
    }) +
    `<div class="ad-layout"><figure class="ad-figure">${built.measures}</figure>` +
    (diagram.measures?.length ? table(["Medida", "Valor"], diagram.measures.map((m) => [m.label, `<strong>${m.value}</strong>`])) : "") +
    "</div></section>"
  );
}
