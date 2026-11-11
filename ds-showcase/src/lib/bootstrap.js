import { mountNav } from "./nav.js";
import { initTheme } from "./theme.js";
import { mountToc } from "./toc.js";

/**
 * Arranque común de cada página.
 *
 * El índice ancleado se monta en un microtask: el cuerpo del módulo de la página
 * inyecta su contenido justo después de llamar aquí, así que cuando el microtask
 * corre ya hay <h2 id> que leer. Así ninguna página tiene que acordarse de pedirlo.
 */
export function initShowcasePage(pageId, navOptions = {}) {
  mountNav(pageId, navOptions);
  initTheme();
  queueMicrotask(() => setupToc());
}

let tocDone = false;

export function setupToc() {
  if (tocDone) return;
  const scope = document.getElementById("contenido");
  const content = document.querySelector(".content");
  if (!scope || !content) return;
  if (scope.querySelectorAll("h2[id]").length < 2) return;

  let nav = document.getElementById("page-toc");
  if (!nav) {
    nav = document.createElement("aside");
    nav.id = "page-toc";
    nav.className = "toc";
    nav.setAttribute("aria-label", "Índice de la página");
    content.appendChild(nav);
  }
  content.classList.add("has-toc");
  mountToc("page-toc", "contenido");
  tocDone = true;
}

export function statusChip(status) {
  const map = {
    draft: "chip--draft",
    "in-review": "chip--review",
    ready: "chip--ready",
    deprecated: "chip--deprecated",
  };
  return `<span class="chip ${map[status] || "chip--draft"}">${status}</span>`;
}
