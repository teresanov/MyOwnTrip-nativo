/** Resuelve rutas con el base de Vite (GitHub Pages: /MyOwnTrip-nativo/). */
export function assetUrl(path) {
  const clean = path.replace(/^\//, "");
  return `${import.meta.env.BASE_URL}${clean}`;
}
