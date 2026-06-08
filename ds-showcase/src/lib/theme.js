const STORAGE_KEY = "mot-showcase-theme";

export function initTheme() {
  const saved = localStorage.getItem(STORAGE_KEY);
  const prefersDark = window.matchMedia("(prefers-color-scheme: dark)").matches;
  const theme = saved || (prefersDark ? "dark" : "light");
  applyTheme(theme);

  const toggle = document.getElementById("theme-toggle");
  if (!toggle) return;

  toggle.setAttribute("aria-pressed", theme === "dark" ? "true" : "false");
  toggle.textContent = theme === "dark" ? "Tema claro" : "Tema oscuro";
  toggle.addEventListener("click", () => {
    const next = document.documentElement.dataset.theme === "dark" ? "light" : "dark";
    applyTheme(next);
    localStorage.setItem(STORAGE_KEY, next);
    toggle.setAttribute("aria-pressed", next === "dark" ? "true" : "false");
    toggle.textContent = next === "dark" ? "Tema claro" : "Tema oscuro";
  });
}

function applyTheme(theme) {
  document.documentElement.dataset.theme = theme;
}
