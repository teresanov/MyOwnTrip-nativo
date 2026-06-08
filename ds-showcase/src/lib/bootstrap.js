import { mountNav } from "./nav.js";
import { initTheme } from "./theme.js";

export function initShowcasePage(pageId, navOptions = {}) {
  mountNav(pageId, navOptions);
  initTheme();
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
