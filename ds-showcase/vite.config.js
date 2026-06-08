import path from "node:path";
import { defineConfig } from "vite";

export default defineConfig(({ mode }) => ({
  base: mode === "production" ? "/MyOwnTrip-nativo/" : "/",
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
}));
