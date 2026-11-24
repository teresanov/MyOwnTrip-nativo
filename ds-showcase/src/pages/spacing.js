import { initShowcasePage } from "../lib/bootstrap.js";
import { mountFoundation } from "../lib/foundation-page.js";
import { STARS_ICON } from "../lib/anatomy-diagram.js";
import data from "../data/foundations/spacing.json";

initShowcasePage("spacing");
mountFoundation(data);

const SCALE = [
  { compose: "componentXs", figma: "component-xs", dp: 4 },
  { compose: "componentSm", figma: "component-sm", dp: 8 },
  { compose: "componentMd", figma: "component-md", dp: 12 },
  { compose: "componentLg", figma: "component-lg", dp: 16 },
  { compose: "layoutMd", figma: "layout-md", dp: 16 },
  { compose: "screenHorizontal", figma: "screen-horizontal", dp: 16 },
  { compose: "gutterGrid", figma: "gutter-grid", dp: 16 },
  { compose: "layoutLg", figma: "layout-lg", dp: 24 },
  { compose: "screenContentBottom", figma: "screen-content-bottom", dp: 24 },
  { compose: "screenContentBottomWithFab", figma: "screen-content-bottom-with-fab", dp: 88 },
];

const pill = (n, kind, sticky = false) =>
  `<span class="spec-n spec-n--${kind}${sticky ? "" : " live-redline"}">${n}</span>`;

const token = (compose, figma) =>
  `<code>${compose}</code><span class="token-alias">${figma}</span>`;

const FLIGHT = `<svg viewBox="0 0 24 24" aria-hidden="true"><path fill="currentColor" d="M21 16v-2l-8-5V3.5A1.5 1.5 0 0 0 11.5 2 1.5 1.5 0 0 0 10 3.5V9l-8 5v2l8-2.5V19l-2 1.5V22l3.5-1 3.5 1v-1.5L13 19v-5.5z"/></svg>`;
const MORE = `<svg class="mot-li__trail" viewBox="0 0 24 24" aria-hidden="true"><circle cx="12" cy="5" r="2" fill="currentColor"/><circle cx="12" cy="12" r="2" fill="currentColor"/><circle cx="12" cy="19" r="2" fill="currentColor"/></svg>`;

function scaleHtml() {
  return `
    <div class="sp-scale" role="img" aria-label="Escala MOTSpacing en dp">
      ${SCALE.map(
        (t) => `
        <div class="sp-scale__row">
          <code class="sp-scale__name">${t.compose}</code>
          <span class="sp-scale__dp">${t.dp}</span>
          <div class="sp-scale__track">
            <span class="sp-scale__bar" style="width:${t.dp}px"></span>
          </div>
        </div>`
      ).join("")}
    </div>`;
}

function phoneHtml() {
  return `
    <div class="sp-phone-stage">
      <div class="sp-phone">
        <div class="sp-phone__pad sp-phone__pad--t">${pill(16, "pad")}</div>
        <div class="sp-phone__mid">
          <div class="sp-phone__pad sp-phone__pad--x">${pill(16, "pad")}</div>
          <div class="sp-phone__col">
            <div class="sp-phone__block sp-phone__block--hero">
              <p class="sp-phone__kicker">Viaje</p>
              <p class="sp-phone__title">Barcelona</p>
            </div>
            <div class="sp-phone__gap">${pill(16, "gap")}</div>
            <div class="sp-phone__block">
              <strong>Wallet</strong>
              <span>3 apuntes esta semana</span>
            </div>
            <div class="sp-phone__gap">${pill(16, "gap")}</div>
            <button type="button" class="mot-btn mot-btn--filled sp-phone__cta" tabindex="-1">${STARS_ICON}Abrir cuaderno</button>
          </div>
          <div class="sp-phone__pad sp-phone__pad--x"></div>
        </div>
        <div class="sp-phone__pad sp-phone__pad--b">${pill(24, "pad")}</div>
      </div>
      <p class="sp-phone__caption">360 dp · <code>screenHorizontal</code> 16 · <code>layoutMd</code> 16 · <code>screenContentBottom</code> 24</p>
    </div>`;
}

function buttonSpecimen(cls, padX, padY, gap, iconPx, label, part) {
  return `
    <div class="spec-live" data-part="${part}" style="--spec-pad-x:${padX}px;--spec-pad-y:${padY}px;--spec-gap:${gap}px;--spec-icon:${iconPx}px">
      <button type="button" class="mot-btn ${cls} spec-ghost" tabindex="-1">${STARS_ICON}${label}</button>
      <div class="spec-overlay live-redline" aria-hidden="true">
        <span class="spec-band spec-band--pad-l">${pill(padX, "pad")}</span>
        <span class="spec-band spec-band--pad-t">${pill(padY, "pad")}</span>
        <span class="spec-band spec-band--pad-b"></span>
        <span class="spec-band spec-band--pad-r"></span>
        <span class="spec-band spec-band--gap">${pill(gap, "gap")}</span>
      </div>
    </div>`;
}

function rowsHtml(rows) {
  return rows
    .map(
      (r) => `
      <tr data-part="${r.part}">
        <td>${r.parte}</td>
        <td>${r.kind === "pad" ? "Padding" : "Gap"}</td>
        <td>${r.value}</td>
        <td>${token(r.compose, r.figma)}</td>
      </tr>`
    )
    .join("");
}

function tableHtml(id, rows) {
  return `
    <div class="live-kit__table-wrap" data-table="${id}">
      <table>
        <thead><tr><th>Parte</th><th>Propiedad</th><th>Valor</th><th>Token</th></tr></thead>
        <tbody>${rowsHtml(rows)}</tbody>
      </table>
    </div>`;
}

const BUTTON_ROWS = [
  {
    part: "btn-small",
    parte: "MOTButton",
    kind: "pad",
    value: "24 × 8 dp",
    compose: "MOTSpacing.ButtonSmall",
    figma: "mot/padding/button/small-*",
  },
  {
    part: "btn-small",
    parte: "MOTButton · icono y label",
    kind: "gap",
    value: "8 dp",
    compose: "MOTSpacing.componentSm",
    figma: "mot/spacing/component-sm",
  },
  {
    part: "btn-xs",
    parte: "MOTTonalButton",
    kind: "pad",
    value: "12 × 6 dp",
    compose: "MOTSpacing.ButtonXSmall",
    figma: "mot/padding/button/xsmall-*",
  },
  {
    part: "btn-xs",
    parte: "MOTTonalButton · icono y label",
    kind: "gap",
    value: "8 dp",
    compose: "MOTSpacing.componentSm",
    figma: "mot/spacing/component-sm",
  },
];

const CHIP_ROWS = [
  {
    part: "chip",
    parte: "FilterChip",
    kind: "pad",
    value: "16 × 6 dp",
    compose: "MOTSpacing.Chip.horizontalLg × vertical",
    figma: "mot/padding/chip/horizontal-lg · vertical",
  },
  {
    part: "chip-gap",
    parte: "Fila Wallet · entre chips",
    kind: "gap",
    value: "8 dp",
    compose: "MOTSpacing.componentSm",
    figma: "mot/spacing/chip/gap",
  },
];

const LIST_ROWS = [
  {
    part: "li",
    parte: "WalletDocumentRow",
    kind: "pad",
    value: "16 × 12 dp",
    compose: "MOTSpacing.ListItem.horizontal × vertical",
    figma: "mot/padding/list-item/horizontal · vertical",
  },
  {
    part: "li",
    parte: "Leading · texto",
    kind: "gap",
    value: "12 dp",
    compose: "MOTSpacing.ListItem.gap",
    figma: "mot/spacing/list-item/gap",
  },
  {
    part: "li",
    parte: "Texto · trailing",
    kind: "gap",
    value: "10 dp",
    compose: "MOTSpacing.ListItem.trailingGap",
    figma: "mot/spacing/list-item/trailing-gap",
  },
];

function liveHtml() {
  return `
    <div class="live-kit" data-tab="button" data-names="compose" data-redlines="on">
      <div class="live-kit__bar">
        <div class="live-seg" role="tablist" aria-label="Pieza">
          <button type="button" role="tab" aria-selected="true" data-tab="button">Botón</button>
          <button type="button" role="tab" aria-selected="false" data-tab="chip">Filter chip</button>
          <button type="button" role="tab" aria-selected="false" data-tab="list">List item</button>
        </div>
        <div class="live-seg" aria-label="Lectura">
          <button type="button" data-names="figma" aria-pressed="false">Figma</button>
          <button type="button" data-names="compose" aria-pressed="true">Compose</button>
          <button type="button" data-redlines aria-pressed="true">Redlines</button>
        </div>
      </div>

      <div class="live-kit__stage">
        <div class="live-kit__panel live-kit__panel--button" data-panel="button">
          ${buttonSpecimen("mot-btn--filled", 24, 8, 8, 18, "Abrir cuaderno", "btn-small")}
          ${buttonSpecimen("mot-btn--tonal mot-btn--xs", 12, 6, 8, 18, "Siguiente", "btn-xs")}
        </div>
        <div class="live-kit__panel live-kit__panel--chip" data-panel="chip">
          <div class="live-chip-row">
            <div class="spec-live" data-part="chip" style="--spec-pad-x:16px;--spec-pad-y:6px">
              <button type="button" class="filter-chip filter-chip--selected spec-ghost" tabindex="-1">Activos</button>
              <div class="spec-overlay live-redline" aria-hidden="true">
                <span class="spec-band spec-band--pad-l">${pill(16, "pad")}</span>
                <span class="spec-band spec-band--pad-t">${pill(6, "pad")}</span>
                <span class="spec-band spec-band--pad-b"></span>
                <span class="spec-band spec-band--pad-r"></span>
              </div>
            </div>
            <div class="live-slot live-slot--h live-redline" data-part="chip-gap" style="--slot:8px">${pill(8, "gap")}</div>
            <button type="button" class="filter-chip spec-ghost" tabindex="-1">Archivados</button>
            <div class="live-slot live-slot--h live-redline" data-part="chip-gap" style="--slot:8px"></div>
            <button type="button" class="filter-chip spec-ghost" tabindex="-1">Todos</button>
          </div>
        </div>
        <div class="live-kit__panel live-kit__panel--list" data-panel="list">
          <div class="spec-live mot-li-wrap" data-part="li" style="--spec-pad-x:16px;--spec-pad-y:12px;--spec-gap:12px;--spec-icon:40px;--spec-trail:10px">
            <div class="mot-li spec-ghost">
              <span class="mot-li__lead">${FLIGHT}</span>
              <div class="mot-li__text"><strong>Vuelo Lisboa – Madrid</strong><span>Vuelo · 14 mar</span></div>
              ${MORE}
            </div>
            <div class="spec-overlay live-redline" aria-hidden="true">
              <span class="spec-band spec-band--pad-l">${pill(16, "pad")}</span>
              <span class="spec-band spec-band--pad-t">${pill(12, "pad")}</span>
              <span class="spec-band spec-band--pad-b"></span>
              <span class="spec-band spec-band--pad-r"></span>
              <span class="spec-band spec-band--gap">${pill(12, "gap")}</span>
              <span class="spec-band spec-band--trail">${pill(10, "gap")}</span>
            </div>
          </div>
        </div>
      </div>

      <p class="live-kit__legend">
        <span><i class="live-kit__swatch live-kit__swatch--pad"></i>Padding</span>
        <span><i class="live-kit__swatch live-kit__swatch--gap"></i>Gap</span>
        <span>Todos los valores salen de un token</span>
      </p>
      ${tableHtml("button", BUTTON_ROWS)}
      ${tableHtml("chip", CHIP_ROWS)}
      ${tableHtml("list", LIST_ROWS)}
    </div>`;
}

function bindLiveKit(root) {
  const setTab = (tab) => {
    root.dataset.tab = tab;
    root.querySelectorAll("[data-tab]").forEach((btn) => {
      if (btn.getAttribute("data-tab") && btn.tagName === "BUTTON" && btn.closest("[role=tablist]")) {
        btn.setAttribute("aria-selected", String(btn.getAttribute("data-tab") === tab));
      }
    });
    root.querySelectorAll("[data-table]").forEach((el) => {
      el.hidden = el.getAttribute("data-table") !== tab;
    });
  };

  root.querySelectorAll("[role=tablist] [data-tab]").forEach((btn) => {
    btn.addEventListener("click", () => setTab(btn.getAttribute("data-tab")));
  });

  root.querySelectorAll("[data-names]").forEach((btn) => {
    if (btn.tagName !== "BUTTON") return;
    btn.addEventListener("click", () => {
      root.dataset.names = btn.getAttribute("data-names");
      root.querySelectorAll("button[data-names]").forEach((b) => {
        b.setAttribute("aria-pressed", String(b === btn));
      });
    });
  });

  const redBtn = root.querySelector("[data-redlines]");
  redBtn?.addEventListener("click", () => {
    const on = root.dataset.redlines !== "on";
    root.dataset.redlines = on ? "on" : "off";
    redBtn.setAttribute("aria-pressed", String(on));
  });

  const hot = (part, on) => {
    root.querySelectorAll(`[data-part="${part}"]`).forEach((el) => {
      el.classList.toggle("is-hot", on);
    });
  };

  root.querySelectorAll("[data-part]").forEach((el) => {
    el.addEventListener("mouseenter", () => hot(el.getAttribute("data-part"), true));
    el.addEventListener("mouseleave", () => hot(el.getAttribute("data-part"), false));
  });

  setTab("button");
}

function padFrame(inner, padX, padY) {
  return `
    <div class="spec-live" style="--spec-pad-x:${padX}px;--spec-pad-y:${padY}px">
      ${inner}
      <div class="spec-overlay" aria-hidden="true">
        <span class="spec-band spec-band--pad-l">${pill(padX, "pad", true)}</span>
        <span class="spec-band spec-band--pad-t">${pill(padY, "pad", true)}</span>
        <span class="spec-band spec-band--pad-b"></span>
        <span class="spec-band spec-band--pad-r"></span>
      </div>
    </div>`;
}

function padCard({ title, codes, href, label, inner }) {
  return `
    <article class="pad-card">
      <header class="pad-card__head">
        <h3 class="pad-card__title">${title}</h3>
        <p class="pad-card__codes">${codes}</p>
        <p class="pad-card__link">Espécimen de <a href="${href}">${label}</a></p>
      </header>
      <div class="playground__frame">
        <div class="playground__canvas pad-card__canvas">${inner}</div>
      </div>
    </article>`;
}

function paddingHtml() {
  return `
    <div class="pad-layers" aria-label="Tres capas de medida">
      <figure class="pad-layers__item">
        <div class="pad-layers__screen">
          <span class="pad-layers__band pad-layers__band--pad">${pill(16, "pad", true)}</span>
          <span class="pad-layers__fill">Body</span>
          <span class="pad-layers__band pad-layers__band--pad"></span>
        </div>
        <figcaption>Pantalla · <code>screenHorizontal</code> 16</figcaption>
      </figure>
      <figure class="pad-layers__item">
        <div class="pad-layers__gap">
          <span class="pad-layers__box"></span>
          <span class="live-slot live-slot--h" style="--slot:8px">${pill(8, "gap", true)}</span>
          <span class="pad-layers__box pad-layers__box--label">Label</span>
        </div>
        <figcaption>Hueco · <code>componentSm</code> 8</figcaption>
      </figure>
      <figure class="pad-layers__item">
        ${padFrame(
          `<button type="button" class="mot-btn mot-btn--filled spec-ghost" tabindex="-1">${STARS_ICON}Abrir cuaderno</button>`,
          24,
          8
        )}
        <figcaption>Dentro · <code>ButtonSmall</code> 24 × 8</figcaption>
      </figure>
    </div>
    <div class="pad-catalog">
      ${padCard({
        title: "MOTButton · 24 × 8",
        codes: "<code>MOTSpacing.ButtonSmall</code> · <code>mot/padding/button/small-*</code>",
        href: "components/button.html",
        label: "Button",
        inner: padFrame(
          `<button type="button" class="mot-btn mot-btn--filled spec-ghost" tabindex="-1">${STARS_ICON}Abrir cuaderno</button>`,
          24,
          8
        ),
      })}
      ${padCard({
        title: "MOTTonalButton · 12 × 6",
        codes: "<code>MOTSpacing.ButtonXSmall</code> · <code>mot/padding/button/xsmall-*</code>",
        href: "components/button.html",
        label: "Button",
        inner: padFrame(
          `<button type="button" class="mot-btn mot-btn--tonal mot-btn--xs spec-ghost" tabindex="-1">${STARS_ICON}Siguiente</button>`,
          12,
          6
        ),
      })}
      ${padCard({
        title: "Filter chip · 16 × 6",
        codes: "<code>MOTSpacing.Chip</code> · <code>mot/padding/chip/horizontal-lg</code> · <code>vertical</code>",
        href: "components/filter-chip.html",
        label: "Filter chip",
        inner: padFrame(
          `<button type="button" class="filter-chip filter-chip--selected spec-ghost" tabindex="-1">Activos</button>`,
          16,
          6
        ),
      })}
      ${padCard({
        title: "WalletDocumentRow · 16 × 12",
        codes: "<code>MOTSpacing.ListItem</code> · <code>mot/padding/list-item/*</code>",
        href: "components/list-item.html",
        label: "List item",
        inner: padFrame(
          `<div class="mot-li spec-ghost"><span class="mot-li__lead">${FLIGHT}</span><div class="mot-li__text"><strong>Vuelo Lisboa – Madrid</strong><span>Vuelo · 14 mar</span></div>${MORE}</div>`,
          16,
          12
        ),
      })}
      ${padCard({
        title: "Outlined text field · 16",
        codes: "<code>MOTSpacing.TextField.outlinedVertical</code> · <code>mot/padding/text-field/outlined-vertical</code>",
        href: "components/text-field.html",
        label: "Text field",
        inner: padFrame(
          `<input class="mot-field__input pad-field spec-ghost" type="text" value="Barcelona 2026" readonly tabindex="-1" />`,
          16,
          16
        ),
      })}
      ${padCard({
        title: "Snackbar · 16 × 12",
        codes: "<code>MOTSpacing.Snackbar</code> · <code>mot/padding/snackbar/*</code>",
        href: "components/snackbar.html",
        label: "Snackbar",
        inner: padFrame(
          `<div class="snackbar-demo spec-ghost" style="margin:0"><span class="snackbar-demo__text">Gasto guardado</span></div>`,
          16,
          12
        ),
      })}
      ${padCard({
        title: "Dialog · 24",
        codes: "<code>MOTSpacing.Dialog.container</code> · <code>mot/padding/dialog/container</code>",
        href: "components/basic-dialog.html",
        label: "Basic dialog",
        inner: padFrame(
          `<div class="dialog-demo spec-ghost" style="max-width:280px;margin:0">
            <p class="dialog-demo__title" style="margin-bottom:8px">¿Eliminar documento?</p>
            <p class="dialog-demo__body" style="margin:0">Se borrará del dispositivo.</p>
          </div>`,
          24,
          24
        ),
      })}
    </div>`;
}

const main = document.getElementById("foundation-main");
const escala = main?.querySelector("#escala")?.closest(".section");
const pantalla = main?.querySelector("#pantalla")?.closest(".section");
const componente = main?.querySelector("#componente")?.closest(".section");
const padding = main?.querySelector("#padding")?.closest(".section");

escala?.insertAdjacentHTML("beforeend", scaleHtml());
pantalla?.querySelector(".section-lead")?.insertAdjacentHTML("afterend", phoneHtml());
componente?.querySelector(".section-lead")?.insertAdjacentHTML("afterend", liveHtml());
const kit = componente?.querySelector(".live-kit");
if (kit) bindLiveKit(kit);
const paddingPs = padding ? [...padding.querySelectorAll(":scope > p")] : [];
(paddingPs.at(-1) || padding)?.insertAdjacentHTML("afterend", paddingHtml());
