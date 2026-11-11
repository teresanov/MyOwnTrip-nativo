import site from "../data/site.json";
import componentsManifest from "../data/components.json";
import { assetUrl } from "./paths.js";

/**
 * Rail lateral. La arquitectura vive en src/data/site.json, no aquí:
 * añadir una página es añadir una entrada al JSON y su entry en vite.config.js.
 */
export function mountNav(currentPageId, { componentId = null } = {}) {
  const root = document.getElementById("site-nav");
  if (!root) return;

  const inComponents = currentPageId === "components" || Boolean(componentId);

  const items = site.nav
    .map((entry) => {
      if (entry.children) {
        const open = entry.children.some((c) => c.id === currentPageId);
        return `
          <li class="rail__group">
            <span class="rail__group-label">${entry.label}</span>
            <ul class="rail__sublist">
              ${entry.children.map((c) => linkItem(c, currentPageId)).join("")}
            </ul>
          </li>`;
      }

      if (entry.dynamic === "components") {
        return `
          <li class="rail__group">
            <a href="${assetUrl(entry.href)}" class="rail__group-link${inComponents ? " is-active" : ""}"${
              currentPageId === "components" ? ' aria-current="page"' : ""
            }>${entry.label}</a>
            ${inComponents ? `<ul class="rail__sublist">${componentLinks(componentId)}</ul>` : ""}
          </li>`;
      }

      return linkItem(entry, currentPageId);
    })
    .join("");

  root.innerHTML = `
    <div class="rail__brand">
      <a href="${assetUrl("index.html")}" class="rail__brand-link">
        <span class="rail__brand-title">MyOwnTrip</span>
        <span class="rail__brand-sub">Design System</span>
      </a>
    </div>
    <ul class="rail__list">${items}</ul>
    <div class="rail__footer">
      <button type="button" class="btn btn--outlined" id="theme-toggle">Cambiar tema</button>
      <p class="rail__meta">M3 nativo · Compose</p>
    </div>
  `;
}

function componentLinks(activeId) {
  return componentsManifest.components
    .map((c) => {
      const active = activeId === c.id;
      return `<li><a href="${assetUrl(c.href)}" class="${active ? "is-active" : ""}"${
        active ? ' aria-current="page"' : ""
      }>${c.name}</a></li>`;
    })
    .join("");
}

function linkItem(page, currentPageId) {
  const active = page.id === currentPageId;
  return `<li><a href="${assetUrl(page.href)}" class="${active ? "is-active" : ""}"${
    active ? ' aria-current="page"' : ""
  }>${page.label}</a></li>`;
}
