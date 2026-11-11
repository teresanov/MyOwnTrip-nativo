const n="Pendiente";function i(e){return String(e).replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;")}function t(e){return e?i(e):n}function s(e){const d=e.decisions||{};return`
  <section class="section decisions" aria-labelledby="decisions-title">
    <h2 id="decisions-title">Decisiones</h2>
    <dl class="decisions__list">
      <dt>Propósito</dt><dd>${t(e.purpose)}</dd>
      <dt>Fuente</dt><dd>${t(d.source)}</dd>
      <dt>Por qué</dt><dd>${t(d.why)}</dd>
      <dt>Capa</dt><dd>${t(d.layer)}</dd>
    </dl>
  </section>`}function c(e){const d=document.getElementById("component-header");d&&d.insertAdjacentHTML("afterend",s(e))}export{c as m};
