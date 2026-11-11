import { initShowcasePage, statusChip } from "../lib/bootstrap.js";
import componentsManifest from "../data/components.json";
import site from "../data/site.json";
import { assetUrl } from "../lib/paths.js";

initShowcasePage("components");

const byCategory = (categoryId) =>
  componentsManifest.components.filter((c) => c.category === categoryId);

const uncategorised = componentsManifest.components.filter(
  (c) => !site.categories.some((cat) => cat.id === c.category)
);

function cardFor(c) {
  const documented = c.href !== "components.html";
  const inner = `
    <span class="component-link__meta">${statusChip(c.status)}<span class="component-link__priority">${c.priority}</span></span>
    <h3>${c.name}</h3>
    <p>${c.summary}</p>
    ${documented ? "" : '<p class="component-link__note">Sin ficha todavía</p>'}
  `;
  return documented
    ? `<a class="component-link" href="${assetUrl(c.href)}">${inner}</a>`
    : `<div class="component-link component-link--empty">${inner}</div>`;
}

const groups = site.categories
  .map((cat) => {
    const items = byCategory(cat.id);
    if (!items.length) return "";
    return `
      <section class="section">
        <h2 id="${cat.id}">${cat.label}</h2>
        <div class="component-grid">${items.map(cardFor).join("")}</div>
      </section>`;
  })
  .join("");

document.getElementById("components-list").innerHTML =
  groups +
  (uncategorised.length
    ? `<section class="section">
         <h2 id="otros">Otros</h2>
         <div class="component-grid">${uncategorised.map(cardFor).join("")}</div>
       </section>`
    : "");
