/**
 * Vocabulario compartido de la web: los nombres de los estados y de las
 * categorias. Vive en un solo archivo porque lo usan tanto la configuracion
 * del menu (astro.config.mjs) como las paginas. Si estuviera duplicado,
 * acabaria diciendo cosas distintas en cada sitio.
 *
 * Es .mjs y no .ts a proposito: astro.config.mjs no puede importar TypeScript.
 */

export const ESTADOS = {
  draft: "Borrador",
  "in-review": "En revisión",
  ready: "Listo",
  deprecated: "Obsoleto",
};

/** Orden y nombre de las categorias del indice. Lo que no este aqui va al final. */
export const CATEGORIAS = [
  ["acciones", "Acciones"],
  ["formularios", "Formularios"],
  ["contenido", "Contenido"],
  ["mensajeria", "Mensajería"],
  ["navegacion", "Navegación"],
  ["otros", "Otros"],
];

/** Agrupa una lista de fichas por categoria, respetando el orden de arriba. */
export function agrupaPorCategoria(fichas, orden = () => 0) {
  const conocidas = CATEGORIAS.map(([slug]) => slug);
  const extras = [...new Set(fichas.map((f) => f.category))]
    .filter((c) => !conocidas.includes(c))
    .map((c) => [c, c.charAt(0).toUpperCase() + c.slice(1)]);

  return [...CATEGORIAS, ...extras]
    .map(([slug, label]) => ({
      slug,
      label,
      items: fichas
        .filter((f) => f.category === slug)
        .sort((a, b) => orden(a) - orden(b) || a.name.localeCompare(b.name, "es")),
    }))
    .filter((g) => g.items.length > 0);
}
