/**
 * Demos vivas (modelo B): un hueco en la ficha donde se monta el componente real de Compose,
 * compilado a WebAssembly desde live-demos/. La ficha pasa los parámetros como data-p-* y avisa
 * con el evento "mot:props"; Compose recompone.
 *
 * El bundle (public/live/) se genera con: ./gradlew -p live-demos wasmJsBrowserDistribution && npm run live
 */
import { assetUrl } from "./paths.js";

const LOAD_TIMEOUT_MS = 25000;
let requested = false;

const attrs = (props) =>
  Object.entries(props)
    .map(([k, v]) => `data-p-${k}="${String(v).replace(/"/g, "&quot;")}"`)
    .join(" ");

/** HTML del hueco. `id` es el specimen de Kotlin (p. ej. "button/playground"). */
export function liveHostHtml(id, props = {}, { height = 168, label = "Demo viva" } = {}) {
  return `
    <div class="live-host" role="group" aria-label="${label}" style="height:${height}px"
         data-mot-specimen="${id}" data-mot-base="${assetUrl("live/")}" ${attrs(props)}>
      <p class="live-host__msg">Cargando el componente real de Compose…</p>
    </div>`;
}

/** Cambia parámetros de una demo ya montada (o pendiente de montar). */
export function setLiveProps(host, props) {
  if (!host) return;
  Object.entries(props).forEach(([k, v]) => host.setAttribute(`data-p-${k}`, String(v)));
  host.dispatchEvent(new Event("mot:props"));
}

function load(hosts) {
  if (requested) return;
  requested = true;
  const script = document.createElement("script");
  script.src = assetUrl("live/demos.js");
  script.onerror = () => fail(hosts, "No se encuentra el bundle de demos vivas (public/live/).");
  document.head.appendChild(script);
  setTimeout(() => {
    hosts.forEach((h) => {
      if (!h.hasAttribute("data-mot-ready")) {
        fail([h], "No se ha podido cargar la demo. Necesita un navegador reciente con WebAssembly GC (Chrome, Edge, Firefox o Safari actuales).");
      }
    });
  }, LOAD_TIMEOUT_MS);
}

function fail(hosts, text) {
  hosts.forEach((h) => {
    const msg = h.querySelector(".live-host__msg");
    if (msg) msg.textContent = text;
    h.classList.add("live-host--error");
  });
}

/** Carga el bundle cuando la primera demo entra en pantalla (no antes: pesa varios MB). */
export function bootLiveSpecimens() {
  const hosts = [...document.querySelectorAll("[data-mot-specimen]")];
  if (!hosts.length) return;
  if (!("IntersectionObserver" in window)) return load(hosts);
  const io = new IntersectionObserver((entries) => {
    if (entries.some((e) => e.isIntersecting)) {
      io.disconnect();
      load(hosts);
    }
  }, { rootMargin: "200px" });
  hosts.forEach((h) => io.observe(h));
}
