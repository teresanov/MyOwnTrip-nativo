// Bloque "Decisiones" de cada ficha (patrón de Lumiboard).
// Campos: propósito, fuente, por qué y capa.
// Lo que la ficha no documenta se muestra como "Pendiente": no se inventa.

const PENDING = "Pendiente";

function escapeHtml(s) {
  return String(s).replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
}

function field(value) {
  return value ? escapeHtml(value) : PENDING;
}

export function renderDecisions(data) {
  const d = data.decisions || {};
  return `
  <section class="section decisions" aria-labelledby="decisions-title">
    <h2 id="decisions-title">Decisiones</h2>
    <dl class="decisions__list">
      <dt>Propósito</dt><dd>${field(data.purpose)}</dd>
      <dt>Fuente</dt><dd>${field(d.source)}</dd>
      <dt>Por qué</dt><dd>${field(d.why)}</dd>
      <dt>Capa</dt><dd>${field(d.layer)}</dd>
    </dl>
  </section>`;
}

export function mountDecisions(data) {
  const header = document.getElementById("component-header");
  if (!header) return;
  header.insertAdjacentHTML("afterend", renderDecisions(data));
}
