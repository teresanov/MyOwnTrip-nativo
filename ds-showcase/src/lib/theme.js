const STORAGE_KEY = "mot-showcase-theme";

// Embedded in the portfolio (productdesigner.teresanovoa.com/lab/myowntrip/showcase/):
// the host page sends its theme (?theme=light|dark on first load, then postMessage).
// That choice lives only for the session, so it never overrides the guide's own
// saved preference when it is opened on its own.
const EMBED_KEY = "mot-showcase-embed-theme";
const EMBED_ORIGINS = [
  "https://productdesigner.teresanovoa.com",
  "http://localhost:4321",
  "http://127.0.0.1:4321",
];
const isTheme = (t) => t === "light" || t === "dark";

function readEmbedTheme() {
  const fromUrl = new URLSearchParams(location.search).get("theme");
  if (isTheme(fromUrl)) {
    try { sessionStorage.setItem(EMBED_KEY, fromUrl); } catch {}
    return fromUrl;
  }
  try {
    const saved = sessionStorage.getItem(EMBED_KEY);
    return isTheme(saved) ? saved : null;
  } catch {
    return null;
  }
}

export function initTheme() {
  let saved = null;
  try { saved = localStorage.getItem(STORAGE_KEY); } catch {}
  const prefersDark = window.matchMedia("(prefers-color-scheme: dark)").matches;
  const theme = readEmbedTheme() || saved || (prefersDark ? "dark" : "light");
  applyTheme(theme);

  const toggle = document.getElementById("theme-toggle");
  const syncToggle = (t) => {
    if (!toggle) return;
    toggle.setAttribute("aria-pressed", t === "dark" ? "true" : "false");
    toggle.textContent = t === "dark" ? "Tema claro" : "Tema oscuro";
  };
  syncToggle(theme);

  window.addEventListener("message", (e) => {
    if (!EMBED_ORIGINS.includes(e.origin)) return;
    const t = e.data && e.data.type === "mot-theme" ? e.data.theme : null;
    if (!isTheme(t)) return;
    applyTheme(t);
    try { sessionStorage.setItem(EMBED_KEY, t); } catch {}
    syncToggle(t);
  });

  if (!toggle) return;
  toggle.addEventListener("click", () => {
    const next = document.documentElement.dataset.theme === "dark" ? "light" : "dark";
    applyTheme(next);
    try {
      localStorage.setItem(STORAGE_KEY, next);
      sessionStorage.removeItem(EMBED_KEY);
    } catch {}
    syncToggle(next);
  });
}

function applyTheme(theme) {
  document.documentElement.dataset.theme = theme;
}
