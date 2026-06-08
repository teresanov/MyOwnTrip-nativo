import path from "node:path";
import { defineConfig } from "vite";

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
        input: {
          overview: path.resolve(__dirname, "index.html"),
          color: path.resolve(__dirname, "color.html"),
          typography: path.resolve(__dirname, "typography.html"),
          components: path.resolve(__dirname, "components.html"),
          button: path.resolve(__dirname, "components/button.html"),
        },
      },
    },
  };
});
