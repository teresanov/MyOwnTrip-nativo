import fs from "node:fs";
import path from "node:path";
import { defineConfig } from "astro/config";
import starlight from "@astrojs/starlight";
import { ESTADOS, agrupaPorCategoria } from "./src/lib/vocabulario.mjs";

// Los datos de componente viven (todavía) en ds-showcase: una sola fuente, dos webs.
// Cuando ds-showcase se retire, mueve la carpeta aquí y cambia solo esta constante.
const DATA_DIR = path.resolve("../ds-showcase/src/data/components");
const MANIFEST = path.resolve("../ds-showcase/src/data/components.json");

/**
 * Construye el apartado "Componentes" del menu leyendo los JSON.
 * Añadir un componente = añadir su JSON. No se toca este archivo.
 */
function componentSidebar() {
  if (!fs.existsSync(DATA_DIR)) return [];

  // El indice aporta la categoria y el orden de presentacion de cada componente.
  const indice = fs.existsSync(MANIFEST)
    ? (JSON.parse(fs.readFileSync(MANIFEST, "utf8")).components ?? [])
    : [];
  const porId = Object.fromEntries(indice.map((c) => [c.id, c]));
  const orden = Object.fromEntries(indice.map((c, i) => [c.id, i]));

  const entries = fs
    .readdirSync(DATA_DIR)
    .filter((f) => f.endsWith(".json"))
    .map((f) => {
      const data = JSON.parse(fs.readFileSync(path.join(DATA_DIR, f), "utf8"));
      const id = data.id ?? f.replace(/\.json$/, "");
      return {
        id,
        name: data.name ?? f,
        status: data.status ?? "draft",
        category: data.category ?? porId[id]?.category ?? "otros",
        orden: orden[id] ?? Number.MAX_SAFE_INTEGER,
      };
    });

  return agrupaPorCategoria(entries, (e) => e.orden).map((grupo) => ({
    label: grupo.label,
    items: grupo.items.map((e) => ({
      label: e.name,
      link: `/componentes/${e.id}/`,
      // Starlight pinta este badge junto al enlace: lo que no esta listo se ve
      // antes de entrar. Lo que si lo esta no necesita etiqueta.
      badge:
        e.status === "ready"
          ? undefined
          : {
              text: ESTADOS[e.status] ?? e.status,
              variant: e.status === "deprecated" ? "danger" : "caution",
            },
    })),
  }));
}

export default defineConfig({
  site: "https://teresanov.github.io",
  // En GitHub Pages el sitio se sirve en un subdirectorio. En local no.
  base: process.env.DS_DOCS_BASE ?? "/",
  integrations: [
    starlight({
      title: "MyOwnTrip · Design System",
      description:
        "Documentación del design system de MyOwnTrip: foundations, componentes y patrones sobre Material 3 nativo.",
      defaultLocale: "root",
      locales: { root: { label: "Español", lang: "es" } },
      // La tabla de contenidos de la derecha solo necesita h2 y h3.
      tableOfContents: { minHeadingLevel: 2, maxHeadingLevel: 3 },
      // Fraunces e Inter, para que las muestras de la pagina de Tipografia se
      // vean con la letra real y no con una sustituta del navegador.
      head: [
        { tag: "link", attrs: { rel: "preconnect", href: "https://fonts.googleapis.com" } },
        {
          tag: "link",
          attrs: { rel: "preconnect", href: "https://fonts.gstatic.com", crossorigin: true },
        },
        {
          tag: "link",
          attrs: {
            rel: "stylesheet",
            href: "https://fonts.googleapis.com/css2?family=Fraunces:opsz,wght@9..144,300..900&family=Inter:wght@400;500;600&display=swap",
          },
        },
      ],
      sidebar: [
        { label: "Inicio", link: "/" },
        // Un grupo con `items: [{ autogenerate }]` recorre la carpeta sola:
        // las paginas nuevas de Markdown aparecen en el menu sin tocar este archivo.
        { label: "Empezar", items: [{ autogenerate: { directory: "empezar" } }] },
        { label: "Foundations", items: [{ autogenerate: { directory: "foundations" } }] },
        { label: "Componentes", items: componentSidebar() },
        { label: "Gobernanza", items: [{ autogenerate: { directory: "gobernanza" } }] },
      ],
      customCss: ["./src/styles/custom.css"],
      pagefind: true,
    }),
  ],
  vite: {
    server: {
      // Permite leer los JSON de ds-showcase, que estan fuera de esta carpeta.
      fs: { allow: [".."] },
    },
  },
});
