/**
 * Traduce los enlaces de las fichas al enrutado de esta web.
 *
 * Los JSON se escribieron para la web anterior, donde cada pagina era un archivo
 * (`components/button.html`). Aqui las rutas son limpias (`/componentes/button/`).
 * En lugar de reescribir los JSON, se traducen al renderizar.
 */

const PAGE_MAP: Record<string, string> = {
  "index.html": "",
  "components.html": "componentes/",
  "color.html": "foundations/color/",
  "typography.html": "foundations/tipografia/",
  "shape.html": "foundations/forma/",
  "spacing.html": "foundations/espaciado/",
};

/** Une el `base` del sitio con una ruta, sin barras duplicadas. */
function withBase(route: string): string {
  const base = import.meta.env.BASE_URL ?? "/";
  return `${base.replace(/\/$/, "")}/${route.replace(/^\//, "")}`.replace(/\/{2,}/g, "/");
}

export function resolveHref(href?: string): string | undefined {
  if (!href) return undefined;
  if (/^(https?:|mailto:|#)/.test(href)) return href;

  const clean = href.replace(/^\.?\//, "");

  const ficha = clean.match(/^components\/(.+)\.html$/);
  if (ficha) return withBase(`componentes/${ficha[1]}/`);

  if (clean in PAGE_MAP) return withBase(PAGE_MAP[clean]);

  return withBase(clean);
}

/** Enlace al archivo fuente en GitHub, para los campos que apuntan al repo. */
const REPO = "https://github.com/teresanov/MyOwnTrip-nativo/blob/main";

export function repoUrl(relPath?: string): string | undefined {
  if (!relPath) return undefined;
  return `${REPO}/${relPath.replace(/^\//, "")}`;
}
