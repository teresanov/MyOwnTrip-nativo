/** HTML de especímenes 1px=1dp. No es Compose. */

export const ICON_EDIT = `<svg viewBox="0 0 24 24" width="24" height="24" aria-hidden="true"><path fill="currentColor" d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04a1 1 0 0 0 0-1.41l-2.34-2.34a1 1 0 0 0-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z"/></svg>`;

export const ICON_MORE = `<svg viewBox="0 0 24 24" width="24" height="24" aria-hidden="true"><path fill="currentColor" d="M12 8a2 2 0 1 0 0-4 2 2 0 0 0 0 4zm0 6a2 2 0 1 0 0-4 2 2 0 0 0 0 4zm0 6a2 2 0 1 0 0-4 2 2 0 0 0 0 4z"/></svg>`;

export const ICON_CAL = `<svg viewBox="0 0 24 24" width="24" height="24" aria-hidden="true"><path fill="currentColor" d="M19 4h-1V2h-2v2H8V2H6v2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 16H5V10h14v10zm0-12H5V6h14v2z"/></svg>`;

export const ICON_FLIGHT = `<svg viewBox="0 0 24 24" width="24" height="24" aria-hidden="true"><path fill="currentColor" d="M21 16v-2l-8-5V3.5A1.5 1.5 0 0 0 11.5 2 1.5 1.5 0 0 0 10 3.5V9l-8 5v2l8-2.5V19l-2 1.5V22l3.5-1 3.5 1v-1.5L13 19v-5.5l8 2.5z"/></svg>`;

export const ICON_CLOSE = `<svg viewBox="0 0 24 24" width="18" height="18" aria-hidden="true"><path fill="currentColor" d="M19 6.41 17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/></svg>`;

export function iconButtonHtml(label = "Editar") {
  return `<button type="button" class="mot-icon-btn" aria-label="${label}">${ICON_EDIT}</button>`;
}

export function listItemHtml({
  title = "Billete tren",
  support = "Transporte · 12 jun 2026",
  more = true,
} = {}) {
  return `
    <div class="mot-li">
      <span class="mot-li__lead">${ICON_FLIGHT}</span>
      <span class="mot-li__text"><strong>${title}</strong><span>${support}</span></span>
      ${more ? `<span class="mot-li__trail">${ICON_MORE}</span>` : ""}
    </div>`;
}

export function snackbarHtml({ message = "Gasto guardado", action = "Deshacer", close = true } = {}) {
  return `
    <div class="snackbar-demo">
      <span class="snackbar-demo__text">${message}</span>
      ${action ? `<button type="button" class="snackbar-demo__action" tabindex="-1">${action}</button>` : ""}
      ${close ? `<button type="button" class="snackbar-demo__close" tabindex="-1" aria-label="Cerrar">${ICON_CLOSE}</button>` : ""}
    </div>`;
}

export function eyebrowHtml(text = "Próximo viaje", color = "tertiary", size = "md") {
  return `<span class="eyebrow eyebrow--${color} eyebrow--${size}">${text}</span>`;
}

export function fieldHtml({ label, value = "", placeholder = "", error = "", readonly = false }) {
  const err = error
    ? ` mot-field--error`
    : "";
  return `
    <label class="mot-field${err}">
      <span class="mot-field__label">${label}</span>
      <input class="mot-field__input" type="text" value="${value}" placeholder="${placeholder}" ${
        readonly ? "readonly" : ""
      } autocomplete="off" />
      ${error ? `<span class="mot-field__support">${error}</span>` : ""}
    </label>`;
}
