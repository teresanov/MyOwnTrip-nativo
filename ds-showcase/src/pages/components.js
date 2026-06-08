import { initShowcasePage, statusChip } from "../lib/bootstrap.js";
import componentsManifest from "../data/components.json";
import { assetUrl } from "../lib/paths.js";

initShowcasePage("components");

document.getElementById("components-list").innerHTML = componentsManifest.components
  .map(
    (c) => `
    <a class="component-link" href="${assetUrl(c.href)}" style="display:block;margin-bottom:16px">
      ${statusChip(c.status)}
      <span style="margin-left:8px;font-size:0.75rem;color:var(--on-surface-variant)">${c.priority}</span>
      <h3>${c.name}</h3>
      <p>${c.summary}</p>
    </a>`
  )
  .join("");
