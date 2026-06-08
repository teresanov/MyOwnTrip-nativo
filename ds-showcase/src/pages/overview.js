import { initShowcasePage, statusChip } from "../lib/bootstrap.js";
import componentsManifest from "../data/components.json";

initShowcasePage("overview");

const foundations = [
  { href: "/color.html", title: "Color", desc: "Roles M3, seeds MTB, extensiones" },
  { href: "/typography.html", title: "Typography", desc: "Fraunces + Inter" },
];

document.getElementById("foundations-grid").innerHTML = foundations
  .map(
    (f) => `
    <a class="component-link" href="${f.href}">
      <h3>${f.title}</h3>
      <p>${f.desc}</p>
    </a>`
  )
  .join("");

document.getElementById("components-grid").innerHTML = componentsManifest.components
  .map(
    (c) => `
    <a class="component-link" href="${c.href}" style="margin-bottom:12px">
      ${statusChip(c.status)} <span style="margin-left:8px;font-size:0.75rem;color:var(--on-surface-variant)">${c.priority}</span>
      <h3>${c.name}</h3>
      <p>${c.summary}</p>
    </a>`
  )
  .join("");
