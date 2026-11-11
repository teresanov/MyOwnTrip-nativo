import path from "node:path";
import { defineConfig } from "vite";

/** Cada página HTML necesita su entrada aquí o no se construye. */
const PAGES = [
  ["overview", "index.html"],
  ["empezar", "empezar.html"],
  // Foundations
  ["color", "color.html"],
  ["typography", "typography.html"],
  ["shape", "shape.html"],
  ["spacing", "spacing.html"],
  ["iconography", "iconography.html"],
  ["motion", "motion.html"],
  // Componentes
  ["components", "components.html"],
  ["button", "components/button.html"],
  ["text-field", "components/text-field.html"],
  ["filter-chip", "components/filter-chip.html"],
  ["basic-dialog", "components/basic-dialog.html"],
  ["snackbar", "components/snackbar.html"],
  ["stacked-card", "components/stacked-card.html"],
  ["trip-hero-card", "components/trip-hero-card.html"],
  ["eyebrow-label", "components/eyebrow-label.html"],
  ["swipeable-list-row", "components/swipeable-list-row.html"],
  // Resto
  ["patterns", "patterns.html"],
  ["governance", "gobernanza.html"],
];

export default defineConfig(({ mode }) => {
  const base = mode === "production" ? "/MyOwnTrip-nativo/" : "/";

  return {
    base,
    plugins: [
      {
        name: "inject-base",
        transformIndexHtml(html) {
          if (html.includes("<base ")) return html;
          return html.replace("<head>", `<head>\n    <base href="${base}" />`);
        },
      },
    ],
    preview: {
      // npm run preview → abrir /MyOwnTrip-nativo/ (misma ruta que GH Pages)
      open: "/MyOwnTrip-nativo/",
    },
    build: {
      rollupOptions: {
        input: Object.fromEntries(
          PAGES.map(([name, file]) => [name, path.resolve(__dirname, file)])
        ),
      },
    },
  };
});
