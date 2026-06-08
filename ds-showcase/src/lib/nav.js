import componentsManifest from "../data/components.json";
import { assetUrl } from "./paths.js";

const PAGES = [
  { id: "overview", href: "index.html", label: "Overview" },
  { id: "color", href: "color.html", label: "Color", group: "foundations" },
  { id: "typography", href: "typography.html", label: "Typography", group: "foundations" },
  { id: "components", href: "components.html", label: "Components" },
];

export function mountNav(currentPageId, { componentId = null } = {}) {
  const root = document.getElementById("site-nav");
  if (!root) return;

  const overview = PAGES.find((p) => p.id === "overview");
  const foundations = PAGES.filter((p) => p.group === "foundations");
  const components = PAGES.find((p) => p.id === "components");

  const foundationsLinks = foundations
    .map((p) => linkItem(p, currentPageId))
    .join("");

  const componentLinks = componentsManifest.components
    .map((c) => {
      const active = componentId === c.id;
      const cls = active ? " is-active" : "";
      const current = active ? ' aria-current="page"' : "";
      return `<li><a href="${assetUrl(c.href)}" class="${cls.trim()}"${current}>${c.name}</a></li>`;
    })
    .join("");

  const onComponentsSection = currentPageId === "components" || componentId;

  root.innerHTML = `
    <div class="rail__brand">
      <a href="${assetUrl("index.html")}" class="rail__brand-link">
        <span class="rail__brand-title">MyOwnTrip</span>
        <span class="rail__brand-sub">Design System</span>
      </a>
    </div>
    <ul class="rail__list">
      ${linkItem(overview, currentPageId)}
      <li class="rail__group">
        <span class="rail__group-label">Foundations</span>
        <ul class="rail__sublist">${foundationsLinks}</ul>
      </li>
      <li class="rail__group">
        <a href="${assetUrl(components.href)}" class="rail__group-link${onComponentsSection ? " is-active" : ""}"${currentPageId === "components" && !componentId ? ' aria-current="page"' : ""}>Components</a>
        ${onComponentsSection ? `<ul class="rail__sublist">${componentLinks}</ul>` : ""}
      </li>
    </ul>
    <div class="rail__footer">
      <button type="button" class="btn btn--outlined" id="theme-toggle">Cambiar tema</button>
      <p class="rail__meta">Showcase estático · M3 nativo</p>
    </div>
  `;
}

function linkItem(page, currentPageId) {
  const active = page.id === currentPageId;
  const cls = active ? " is-active" : "";
  const current = active ? ' aria-current="page"' : "";
  return `<li><a href="${assetUrl(page.href)}" class="${cls.trim()}"${current}>${page.label}</a></li>`;
}
