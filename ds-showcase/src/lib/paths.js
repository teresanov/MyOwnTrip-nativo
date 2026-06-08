/** Enlaces internos respetando <base href> (GH Pages: /MyOwnTrip-nativo/). */
export function assetUrl(path) {
  const clean = path.replace(/^\//, "");
  return new URL(clean, document.baseURI).pathname;
}
