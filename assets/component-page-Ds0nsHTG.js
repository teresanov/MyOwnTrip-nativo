function o(e){return String(e).replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;")}function c(e,n){return`
  <p class="page-header__eyebrow">Component · ${e.priority||"P0"}</p>
  <h1>${e.name} ${a(e.status)}</h1>
  <p class="page-header__lead">${e.purpose}</p>
  <p><a href="${e.figma}" target="_blank" rel="noopener">${n} ↗</a></p>
`}function a(e){return`<span class="chip ${{draft:"chip--draft","in-review":"chip--review",ready:"chip--ready",deprecated:"chip--deprecated"}[e]||"chip--draft"}">${e}</span>`}function i({title:e,hint:n,canvasHtml:t,controlsHtml:s=""}){return`
  <section class="section playground">
    <h2>${e}</h2>
    ${n?`<p class="playground__hint">${n}</p>`:""}
    <div class="playground__frame">
      <div class="playground__canvas">${t}</div>
      ${s?`<div class="playground__controls">${s}</div>`:""}
    </div>
  </section>
`}function r(e,n="Variantes"){return e!=null&&e.length?`
  <section class="section">
    <h2>${n}</h2>
    <p class="section-lead">Cada variante del set publicado en Figma debe poder trazarse a un estado o escenario real en Compose.</p>
    <table>
      <thead><tr><th>Variante</th><th>Cuándo usarla</th></tr></thead>
      <tbody>
        ${e.map(t=>`<tr><td>${t.label}</td><td>${t.usage}</td></tr>`).join("")}
      </tbody>
    </table>
  </section>
`:""}function l(e){return e!=null&&e.length?`
  <section class="section">
    <h2>Anatomía</h2>
    <p class="section-lead">Partes que componen el control y cómo se nombran en Figma y en Material 3.</p>
    <ol class="list-anatomy">${e.map(n=>`<li>${n}</li>`).join("")}</ol>
  </section>
`:""}function d(e){return`
  <section class="section">
    <h2>Directrices</h2>
    <p class="section-lead">Patrones que el equipo ya acordó para MyOwnTrip; no sustituyen la revisión de pantalla concreta.</p>
    <h3 class="subsection-title">Hacer</h3>
    <ul class="list-dos">${e.dos.map(n=>`<li>${n}</li>`).join("")}</ul>
    <h3 class="subsection-title">Evitar</h3>
    <ul class="list-dos list-dos--dont">${e.donts.map(n=>`<li>${n}</li>`).join("")}</ul>
  </section>
`}function p(e){return`
  <section class="section">
    <h2>Tokens y roles M3</h2>
    <p class="section-lead">En Compose se consumen roles de <code>MaterialTheme.colorScheme</code>, tipografía M3 e, cuando aplica, spacing MOT documentado en <code>docs/design-system/spacing.md</code>.</p>
    <ul class="token-list">${e.map(n=>`<li><code>${o(n)}</code></li>`).join("")}</ul>
  </section>
`}function u(e){return`
  <section class="section">
    <h2>Accesibilidad</h2>
    <ul class="list-dos">${e.map(n=>`<li>${n}</li>`).join("")}</ul>
  </section>
`}function h(e){return`
  <section class="section">
    <h2>Implementación en Compose</h2>
    <p class="section-lead">Fragmento representativo; el código fuente en <code>app/</code> es la fuente de verdad del comportamiento.</p>
    <pre><code>${o(e)}</code></pre>
  </section>
`}function m(e){return`
  <section class="section">
    <h2>Changelog</h2>
    <table>
      <thead><tr><th>Fecha</th><th>Nota</th></tr></thead>
      <tbody>
        ${e.map(n=>`<tr><td>${n.date}</td><td>${n.note}</td></tr>`).join("")}
      </tbody>
    </table>
  </section>
`}export{i as a,r as b,p as c,d,u as e,h as f,m as g,l as h,c as r};
