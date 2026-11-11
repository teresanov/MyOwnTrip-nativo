const p="Pendiente";function u(e){return String(e).replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;")}function o(e){return e?u(e):p}function h(e){const n=e.decisions||{};return`
  <section class="section decisions" aria-labelledby="decisions-title">
    <h2 id="decisions-title">Decisiones</h2>
    <dl class="decisions__list">
      <dt>Propósito</dt><dd>${o(e.purpose)}</dd>
      <dt>Fuente</dt><dd>${o(n.source)}</dd>
      <dt>Por qué</dt><dd>${o(n.why)}</dd>
      <dt>Capa</dt><dd>${o(n.layer)}</dd>
    </dl>
  </section>`}function $(e){const n=document.getElementById("component-header");n&&n.insertAdjacentHTML("afterend",h(e))}function r(e){return String(e).replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;")}function f(e,n){return`
  <p class="page-header__eyebrow">Component · ${e.priority||"P0"}</p>
  <h1>${e.name} ${g(e.status)}</h1>
  <p class="page-header__lead">${e.purpose}</p>
  <p><a href="${e.figma}" target="_blank" rel="noopener">${n} ↗</a></p>
`}function g(e){return`<span class="chip ${{draft:"chip--draft","in-review":"chip--review",ready:"chip--ready",deprecated:"chip--deprecated"}[e]||"chip--draft"}">${e}</span>`}function b({title:e,hint:n,canvasHtml:t,controlsHtml:s=""}){return`
  <section class="section playground">
    <h2>${e}</h2>
    ${n?`<p class="playground__hint">${n}</p>`:""}
    <div class="playground__frame">
      <div class="playground__canvas">${t}</div>
      ${s?`<div class="playground__controls">${s}</div>`:""}
    </div>
  </section>
`}function y(e,n="Variantes"){return e!=null&&e.length?`
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
`:""}function _(e){return e!=null&&e.length?`
  <section class="section">
    <h2>Anatomía</h2>
    <p class="section-lead">Partes que componen el control y cómo se nombran en Figma y en Material 3.</p>
    <ol class="list-anatomy">${e.map(n=>`<li>${n}</li>`).join("")}</ol>
  </section>
`:""}function i(e,n="list-dos"){return`<ul class="${n}">${e.map(t=>`<li>${t}</li>`).join("")}</ul>`}function m(e){const[n,t,s]=String(e||"").split("-");return n&&t&&s?`${s}/${t}/${n}`:e}function C(e){const n=e.guidelines,t=e.productRules,s=e.dos||[],c=e.donts||[];let a="";if(n&&(s.length||c.length)){const l=(n.links||[{label:n.label,url:n.url}]).map(d=>`<a href="${d.url}" target="_blank" rel="noopener">${d.label} ↗</a>`).join(" · ");a+=`
  <section class="section">
    <h2>Do y Don't</h2>
    <p class="section-lead">Guía oficial de Material Design 3 para este componente: ${l} (consultada el ${m(n.consulted)}).</p>
    ${s.length?`<h3 class="subsection-title">Do</h3>${i(s)}`:""}
    ${c.length?`<h3 class="subsection-title">Don't</h3>${i(c,"list-dos list-dos--dont")}`:`<p class="section-lead">En las fuentes consultadas, M3 no lista «Don't» explícitos para este componente.</p>`}
  </section>
`}return t&&((t.dos||[]).length||(t.donts||[]).length)&&(a+=`
  <section class="section">
    <h2>Reglas MyOwnTrip</h2>
    <p class="section-lead">${n?"Acuerdos del equipo sobre cómo usamos el componente en MyOwnTrip; complementan la guía de M3.":"Componente propio de MyOwnTrip: no existe una guía de M3 equivalente, así que estas son las reglas del equipo."}</p>
    ${(t.dos||[]).length?`<h3 class="subsection-title">Do</h3>${i(t.dos)}`:""}
    ${(t.donts||[]).length?`<h3 class="subsection-title">Don't</h3>${i(t.donts,"list-dos list-dos--dont")}`:""}
  </section>
`),a}function M(e){return`
  <section class="section">
    <h2>Tokens y roles M3</h2>
    <p class="section-lead">En Compose se consumen roles de <code>MaterialTheme.colorScheme</code>, tipografía M3 e, cuando aplica, spacing MOT documentado en <code>docs/design-system/spacing.md</code>.</p>
    <ul class="token-list">${e.map(n=>`<li><code>${r(n)}</code></li>`).join("")}</ul>
  </section>
`}function D(e){return`
  <section class="section">
    <h2>Accesibilidad</h2>
    <ul class="list-dos">${e.map(n=>`<li>${n}</li>`).join("")}</ul>
  </section>
`}function T(e){return`
  <section class="section">
    <h2>Implementación en Compose</h2>
    <p class="section-lead">Fragmento representativo; el código fuente en <code>app/</code> es la fuente de verdad del comportamiento.</p>
    <pre><code>${r(e)}</code></pre>
  </section>
`}function j(e){return`
  <section class="section">
    <h2>Changelog</h2>
    <table>
      <thead><tr><th>Fecha</th><th>Nota</th></tr></thead>
      <tbody>
        ${e.map(n=>`<tr><td>${n.date}</td><td>${n.note}</td></tr>`).join("")}
      </tbody>
    </table>
  </section>
`}export{b as a,y as b,M as c,C as d,D as e,T as f,j as g,_ as h,$ as m,f as r};
