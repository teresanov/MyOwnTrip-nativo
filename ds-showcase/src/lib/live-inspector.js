/**
 * Anatomía y medidas sobre el componente real (demo viva de Compose).
 *
 * El specimen de Kotlin informa de la caja de cada pieza (evento "mot:parts", atributo
 * data-mot-parts, en px CSS relativos al hueco). Aquí solo se dibuja encima:
 *  - Anatomía: marcadores numerados con línea guía hasta el punto exacto de cada pieza (estilo Specs).
 *  - Medidas: inspector al pasar el cursor, como las DevTools del navegador (caja, espacios, tamaño en dp).
 *
 * Nada se escribe a mano: si el componente cambia en código, cambian el dibujo y los números.
 */

const NS = "http://www.w3.org/2000/svg";
const dp = (cssPx, zoom) => Math.round((cssPx / zoom) * 2) / 2;
const fmt = (v) => (Number.isInteger(v) ? `${v}` : v.toFixed(1));

function readGeometry(host) {
  // Solo las piezas que la ficha declara (marcadores y filas): un componente puede llevar otros dentro
  // (p. ej. el Eyebrow label dentro de TripHeroCard) y sus piezas no deben mezclarse.
  const allow = JSON.parse(host.closest("[data-parts]")?.getAttribute("data-parts") || "null");
  const raw = host.getAttribute("data-mot-parts");
  if (!raw) return null;
  let data;
  try {
    data = JSON.parse(raw);
  } catch {
    return null;
  }
  const zoom = Number(host.getAttribute("data-p-zoom")) || 1;
  const parts = {};
  for (const [name, [x, y, w, h]] of Object.entries(data.parts || {})) {
    if (allow && !allow.includes(name)) continue;
    parts[name] = { x, y, w, h };
  }
  const c = parts.container;
  if (c) {
    // La capa de estado ocupa el contenedor.
    parts.stateLayer = { ...c };
    // Área táctil: mínimo del tema (LocalMinimumInteractiveComponentSize), centrada en el contenedor.
    const t = (data.meta?.touchDp || 0) * zoom;
    if (t) {
      const w = Math.max(c.w, t);
      const h = Math.max(c.h, t);
      parts.touch = { x: c.x + c.w / 2 - w / 2, y: c.y + c.h / 2 - h / 2, w, h };
    }
  }
  return { parts, zoom };
}

const el = (tag, attrs = {}, text) => {
  const n = document.createElementNS(NS, tag);
  Object.entries(attrs).forEach(([k, v]) => n.setAttribute(k, v));
  if (text != null) n.textContent = text;
  return n;
};

/* ---------------- Anatomía ---------------- */

/** Punto de anclaje: "left@0.3" = borde izquierdo al 30 % del alto; "top@0.5" = centro del borde superior. */
function anchorPoint(r, spec) {
  const [edge, f = "0.5"] = spec.split("@");
  const t = Number(f);
  switch (edge) {
    case "left": return { x: r.x, y: r.y + r.h * t };
    case "right": return { x: r.x + r.w, y: r.y + r.h * t };
    case "top": return { x: r.x + r.w * t, y: r.y };
    case "bottom": return { x: r.x + r.w * t, y: r.y + r.h };
    case "at": {
      // "at@0.08,0.12": punto interior (fracciones de ancho y alto).
      const [fx, fy] = f.split(",").map(Number);
      return { x: r.x + r.w * fx, y: r.y + r.h * fy };
    }
    default: return { x: r.x + r.w / 2, y: r.y + r.h / 2 };
  }
}

function drawAnatomy(svg, geo, markers, highlight) {
  svg.replaceChildren();
  const { parts } = geo;
  const rects = Object.entries(parts)
    .filter(([k]) => !BACKDROP.has(k))
    .map(([, r]) => r);
  if (!rects.length) return;
  // Los marcadores van fuera de todo el componente (unión de sus piezas, incluida el área táctil).
  const x0 = Math.min(...rects.map((r) => r.x));
  const y0 = Math.min(...rects.map((r) => r.y));
  const outer = {
    x: x0,
    y: y0,
    w: Math.max(...rects.map((r) => r.x + r.w)) - x0,
    h: Math.max(...rects.map((r) => r.y + r.h)) - y0,
  };

  if (parts.touch) {
    svg.append(el("rect", { x: parts.touch.x, y: parts.touch.y, width: parts.touch.w, height: parts.touch.h, class: "li-touch" }));
  }
  if (highlight && parts[highlight]) {
    const r = parts[highlight];
    svg.append(el("rect", { x: r.x, y: r.y, width: r.w, height: r.h, class: "li-hl" }));
  }

  const GAP = 44;
  markers.forEach((m) => {
    const r = parts[m.part];
    if (!r) return;
    const a = anchorPoint(r, m.anchor);
    let mx = a.x;
    let my = a.y;
    if (m.side === "left") mx = outer.x - GAP;
    if (m.side === "right") mx = outer.x + outer.w + GAP;
    if (m.side === "top") my = outer.y - GAP;
    if (m.side === "bottom") my = outer.y + outer.h + GAP;
    if (m.side === "inset") {
      mx = a.x + (m.dx || 0);
      my = a.y + (m.dy || 0);
    }
    // Que el marcador no se salga del recuadro (componentes anchos como el diálogo).
    const W = svg.clientWidth || svg.getBoundingClientRect().width;
    const H = svg.clientHeight || svg.getBoundingClientRect().height;
    mx = Math.min(Math.max(mx, 16), W - 16);
    my = Math.min(Math.max(my, 16), H - 16);
    const on = highlight === m.part;
    const g = el("g", { class: `li-marker${on ? " is-on" : ""}` });
    g.append(
      el("line", { x1: mx, y1: my, x2: a.x, y2: a.y, class: "li-leader" }),
      el("circle", { cx: a.x, cy: a.y, r: 3.5, class: "li-dot" }),
      el("circle", { cx: mx, cy: my, r: 12, class: "li-badge" }),
      el("text", { x: mx, y: my + 4.5, "text-anchor": "middle", class: "li-badge-text" }, String(m.n))
    );
    svg.append(g);
  });
}

function attachAnatomy(wrap) {
  const host = wrap.querySelector("[data-mot-specimen]");
  const svg = wrap.querySelector("svg.li-overlay");
  const markers = JSON.parse(wrap.getAttribute("data-markers") || "[]");
  const legend = document.querySelector(wrap.getAttribute("data-legend"));
  let geo = null;
  let highlight = null;
  const redraw = () => geo && drawAnatomy(svg, geo, markers, highlight);

  host.addEventListener("mot:parts", () => {
    geo = readGeometry(host);
    redraw();
  });
  legend?.querySelectorAll("li[data-part]").forEach((li) => {
    const on = () => { highlight = li.dataset.part; li.classList.add("is-on"); redraw(); };
    const off = () => { highlight = null; li.classList.remove("is-on"); redraw(); };
    li.addEventListener("mouseenter", on);
    li.addEventListener("mouseleave", off);
    li.addEventListener("focus", on);
    li.addEventListener("blur", off);
  });
}

/* ---------------- Medidas (inspector tipo DevTools) ---------------- */

const LABELS = {
  container: "Contenedor",
  content: "Contenido",
  icon: "Icono",
  label: "Label",
  touch: "Área táctil",
  field: "Campo",
  input: "Texto",
  trailing: "Icono final",
  support: "Texto de apoyo",
  chip2: "Chip sin seleccionar",
  label2: "Label",
  message: "Mensaje",
  action: "Acción",
  close: "Cerrar",
  title: "Título",
  body: "Texto",
  confirm: "Acción de confirmar",
  dismiss: "Acción de descartar",
  scrim: "Scrim",
  header: "Encabezado",
  subhead: "Subtítulo",
  supporting: "Texto de apoyo",
  image: "Imagen",
  leading: "Inicial",
  card: "Card",
  cover: "Portada",
  eyebrow: "Eyebrow",
  text: "Bloque de texto",
  countdown: "Cuenta atrás",
  meta: "Fechas",
  cta: "Botón",
  tab: "Pestaña",
  tab2: "Pestaña",
  indicator: "Indicador",
  divider: "Divisor",
  line: "Línea",
  nav: "Volver",
};
// Piezas que ocupan toda la escena: no cuentan para colocar los marcadores alrededor del componente.
const BACKDROP = new Set(["scrim"]);
// Al pasar el cursor gana la pieza más pequeña que contiene el punto (como en las DevTools).
const NOT_HITTABLE = new Set(["stateLayer"]);
const area = (r) => r.w * r.h;

function hLine(svg, x1, x2, y, text) {
  if (Math.abs(x2 - x1) < 1) return;
  svg.append(
    el("line", { x1, y1: y, x2, y2: y, class: "li-red" }),
    el("line", { x1, y1: y - 4, x2: x1, y2: y + 4, class: "li-red" }),
    el("line", { x1: x2, y1: y - 4, x2, y2: y + 4, class: "li-red" })
  );
  const t = el("text", { x: (x1 + x2) / 2, y: y - 6, "text-anchor": "middle", class: "li-red-text" }, text);
  svg.append(t);
}

function vLine(svg, y1, y2, x, text) {
  if (Math.abs(y2 - y1) < 1) return;
  svg.append(
    el("line", { x1: x, y1, x2: x, y2, class: "li-red" }),
    el("line", { x1: x - 4, y1, x2: x + 4, y2: y1, class: "li-red" }),
    el("line", { x1: x - 4, y1: y2, x2: x + 4, y2, class: "li-red" })
  );
  svg.append(el("text", { x: x + 6, y: (y1 + y2) / 2 + 4, class: "li-red-text" }, text));
}

function tooltip(svg, r, text, bounds) {
  const w = text.length * 6.6 + 16;
  let x = r.x;
  let y = r.y - 30;
  if (y < 4) y = r.y + r.h + 8;
  x = Math.min(Math.max(4, x), bounds.w - w - 4);
  svg.append(
    el("rect", { x, y, width: w, height: 22, rx: 4, class: "li-tip" }),
    el("text", { x: x + 8, y: y + 15, class: "li-tip-text" }, text)
  );
}

/** Dibuja las cotas de las filas declaradas (left/right/top/gap/vgap). Con only, solo las de esa pieza. */
function drawRedlines(svg, geo, rows, only) {
  const { parts, zoom } = geo;
  const z = (v) => fmt(dp(v, zoom));
  for (const r of rows || []) {
    const pair = r.left || r.right || r.top || r.bottom || r.gap || r.vgap;
    if (!pair) continue;
    if (only && !pair.includes(only)) continue;
    const [a, b] = pair.map((n) => parts[n]);
    if (!a || !b) continue;
    if (r.left) hLine(svg, a.x, b.x, b.y + b.h / 2, z(b.x - a.x));
    else if (r.right) hLine(svg, b.x + b.w, a.x + a.w, b.y + b.h / 2, z(a.x + a.w - b.x - b.w));
    else if (r.top) vLine(svg, a.y, b.y, b.x + Math.min(b.w / 2, 28), z(b.y - a.y));
    else if (r.bottom) vLine(svg, b.y + b.h, a.y + a.h, b.x + Math.min(b.w / 2, 28), z(a.y + a.h - b.y - b.h));
    else if (r.gap) hLine(svg, a.x + a.w, b.x, Math.min(a.y + a.h / 2, b.y + b.h / 2), z(b.x - a.x - a.w));
    else if (r.vgap) vLine(svg, a.y + a.h, b.y, b.x + Math.min(b.w / 2, 28), z(b.y - a.y - a.h));
  }
}

function drawInspect(svg, geo, name, bounds, gaps = [], redlines = null) {
  if (redlines) {
    // Ficha con cotas fijas: sin cursor se ven todos los rellenos y huecos; con cursor, la pieza
    // resaltada, su tamaño y las cotas en las que participa.
    svg.replaceChildren();
    const { parts, zoom } = geo;
    const z = (v) => fmt(dp(v, zoom));
    if (!name) {
      drawRedlines(svg, geo, redlines);
      return;
    }
    const r = parts[name];
    const frame = parts.container || parts.card;
    if (frame && frame !== r) svg.append(el("rect", { x: frame.x, y: frame.y, width: frame.w, height: frame.h, class: "li-ghost" }));
    svg.append(el("rect", { x: r.x, y: r.y, width: r.w, height: r.h, class: "li-content" }));
    drawRedlines(svg, geo, redlines, name);
    tooltip(svg, r, `${LABELS[name] || name} · ${z(r.w)} × ${z(r.h)} dp`, bounds);
    return;
  }
  svg.replaceChildren();
  const { parts, zoom } = geo;
  const c = parts.container;
  const n = parts.content || parts.icon;
  if (!c) return;
  const z = (v) => fmt(dp(v, zoom));

  if (!name) {
    if (parts.touch) svg.append(el("rect", { x: parts.touch.x, y: parts.touch.y, width: parts.touch.w, height: parts.touch.h, class: "li-touch" }));
    return;
  }
  const r = parts[name];

  if (name === "touch") {
    svg.append(el("rect", { x: r.x, y: r.y, width: r.w, height: r.h, class: "li-box-touch" }));
    vLine(svg, r.y, r.y + r.h, r.x + r.w + 10, `${z(r.h)} dp`);
  } else if (name === "container" && n) {
    // Sin pieza «contenido» (icon button), el icono hace de contenido.
    // Como DevTools: espacio interior en verde, contenido en azul.
    svg.append(el("rect", { x: c.x, y: c.y, width: c.w, height: c.h, class: "li-pad" }));
    svg.append(el("rect", { x: n.x, y: n.y, width: n.w, height: n.h, class: "li-content" }));
    hLine(svg, c.x, n.x, c.y + c.h / 2, z(n.x - c.x));
    hLine(svg, n.x + n.w, c.x + c.w, c.y + c.h / 2, z(c.x + c.w - n.x - n.w));
    vLine(svg, c.y, n.y, n.x + n.w / 2, z(n.y - c.y));
    vLine(svg, n.y + n.h, c.y + c.h, n.x + n.w / 2, z(c.y + c.h - n.y - n.h));
  } else {
    svg.append(el("rect", { x: c.x, y: c.y, width: c.w, height: c.h, class: "li-ghost" }));
    svg.append(el("rect", { x: r.x, y: r.y, width: r.w, height: r.h, class: "li-content" }));
    // Distancias a los bordes del contenedor y al vecino.
    const i = parts.icon;
    const l = parts.label;
    if (name === "icon" && i) {
      hLine(svg, c.x, i.x, i.y + i.h + 10, z(i.x - c.x));
      if (l) hLine(svg, i.x + i.w, l.x, i.y - 8, z(l.x - i.x - i.w));
    }
    if (name === "label" && l) {
      hLine(svg, l.x + l.w, c.x + c.w, l.y + l.h + 10, z(c.x + c.w - l.x - l.w));
      if (i) hLine(svg, i.x + i.w, l.x, l.y - 8, z(l.x - i.x - i.w));
    }
    vLine(svg, c.y, r.y, r.x + r.w + 8, z(r.y - c.y));
  }
  // Huecos entre piezas hermanas (p. ej. entre dos chips): se marcan al pasar por cualquiera de las dos.
  gaps.forEach(([a, b]) => {
    if (name !== a && name !== b) return;
    const A = parts[a];
    const B = parts[b];
    if (!A || !B) return;
    const other = name === a ? B : A;
    svg.append(el("rect", { x: other.x, y: other.y, width: other.w, height: other.h, class: "li-ghost" }));
    hLine(svg, A.x + A.w, B.x, Math.max(A.y + A.h, B.y + B.h) + 12, `${z(B.x - A.x - A.w)} dp`);
  });
  tooltip(svg, r, `${LABELS[name] || name} · ${z(r.w)} × ${z(r.h)} dp`, bounds);
}

/** Filas declaradas en la ficha: { label, size | height | left | top | right | gap | vgap }. */
function declaredRows(geo, rows) {
  const { parts, zoom } = geo;
  const z = (v) => `${fmt(dp(v, zoom))} dp`;
  const P = (n) => parts[n];
  return rows
    .map((r) => {
      try {
        if (r.size) return [r.label, `${z(P(r.size).w)} × ${z(P(r.size).h)}`];
        if (r.height) return [r.label, z(P(r.height).h)];
        if (r.width) return [r.label, z(P(r.width).w)];
        if (r.left) return [r.label, z(P(r.left[1]).x - P(r.left[0]).x)];
        if (r.top) return [r.label, z(P(r.top[1]).y - P(r.top[0]).y)];
        if (r.bottom) {
          const [o, i] = r.bottom.map(P);
          return [r.label, z(o.y + o.h - i.y - i.h)];
        }
        if (r.right) {
          const [o, i] = r.right.map(P);
          return [r.label, z(o.x + o.w - i.x - i.w)];
        }
        if (r.gap) {
          const [a, b] = r.gap.map(P);
          return [r.label, z(b.x - a.x - a.w)];
        }
        if (r.vgap) {
          const [a, b] = r.vgap.map(P);
          return [r.label, z(b.y - a.y - a.h)];
        }
      } catch {
        return null;
      }
      return null;
    })
    .filter(Boolean);
}

function measuresTable(geo, tbody, declared) {
  if (declared?.length) {
    const rows = declaredRows(geo, declared);
    tbody.innerHTML = rows.map(([a, b]) => `<tr><td>${a}</td><td><strong>${b}</strong></td></tr>`).join("");
    return;
  }
  const { parts, zoom } = geo;
  const { container: c, icon: i, label: l, touch: t } = parts;
  const n = parts.content || i;
  if (!c || !n) return;
  const z = (v) => `${fmt(dp(v, zoom))} dp`;
  const rows = [
    ["Contenedor", `${z(c.w)} × ${z(c.h)}`],
    ["Espacio a los lados", z(n.x - c.x)],
    ["Espacio arriba y abajo", z(n.y - c.y)],
    i ? ["Icono", `${z(i.w)} × ${z(i.h)}`] : null,
    i && l ? ["Hueco icono–label", z(l.x - i.x - i.w)] : null,
    l ? ["Alto de línea del label", z(l.h)] : null,
    t ? ["Área táctil mínima", z(t.h)] : null,
  ].filter(Boolean);
  tbody.innerHTML = rows.map(([a, b]) => `<tr><td>${a}</td><td><strong>${b}</strong></td></tr>`).join("");
}

function attachMeasures(wrap) {
  const host = wrap.querySelector("[data-mot-specimen]");
  const svg = wrap.querySelector("svg.li-overlay");
  const hit = wrap.querySelector(".li-hit");
  const tbody = document.querySelector(wrap.getAttribute("data-table"));
  const declared = JSON.parse(wrap.getAttribute("data-rows") || "null");
  const gaps = JSON.parse(wrap.getAttribute("data-gaps") || "[]");
  const redlines = wrap.hasAttribute("data-redlines") ? declared : null;
  let geo = null;
  let current = null;

  host.addEventListener("mot:parts", () => {
    geo = readGeometry(host);
    if (geo && tbody) measuresTable(geo, tbody, declared);
    if (geo) drawInspect(svg, geo, current, { w: wrap.clientWidth, h: wrap.clientHeight }, gaps, redlines);
  });

  hit.addEventListener("mousemove", (e) => {
    if (!geo) return;
    const b = wrap.getBoundingClientRect();
    const x = e.clientX - b.left;
    const y = e.clientY - b.top;
    const hits = Object.entries(geo.parts)
      .filter(([k, r]) => !NOT_HITTABLE.has(k) && x >= r.x && x <= r.x + r.w && y >= r.y && y <= r.y + r.h)
      .sort((a, b) => area(a[1]) - area(b[1]));
    const name = hits[0]?.[0] || null;
    if (name !== current) {
      current = name;
      drawInspect(svg, geo, current, { w: b.width, h: b.height }, gaps, redlines);
    }
  });
  hit.addEventListener("mouseleave", () => {
    current = null;
    if (geo) drawInspect(svg, geo, null, { w: wrap.clientWidth, h: wrap.clientHeight }, gaps, redlines);
  });
}

/** Engancha todos los inspectores de la página (llamar tras pintar el HTML). */
export function initLiveInspectors() {
  document.querySelectorAll(".li-wrap[data-inspect='anatomy']").forEach(attachAnatomy);
  document.querySelectorAll(".li-wrap[data-inspect='measures']").forEach(attachMeasures);
}
