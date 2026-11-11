function d(n,o){return`
    <table>
      <thead><tr>${n.map(t=>`<th>${t}</th>`).join("")}</tr></thead>
      <tbody>${o.map(t=>`<tr>${t.map(e=>`<td>${e}</td>`).join("")}</tr>`).join("")}</tbody>
    </table>`}function i(n,o="list-dos"){return`<ul class="${o}">${n.map(t=>`<li>${t}</li>`).join("")}</ul>`}function c(n){return`
    <p class="page-header__eyebrow">${n.eyebrow||"Foundations"}</p>
    <h1>${n.name}</h1>
    <p class="page-header__lead">${n.lead}</p>
    ${n.sourceOfTruth?`<p class="page-header__links"><span class="header-meta">Fuente de verdad: <code>${n.sourceOfTruth}</code></span></p>`:""}
  `}function a(n){const o=(n.sections||[]).map(e=>{var s;const l=(e.dos||[]).length||(e.donts||[]).length?`<div class="guideline-grid">
              ${(e.dos||[]).length?`<div class="guideline-col guideline-col--do"><h3 class="subsection-title">Do</h3>${i(e.dos)}</div>`:""}
              ${(e.donts||[]).length?`<div class="guideline-col guideline-col--dont"><h3 class="subsection-title">Don't</h3>${i(e.donts,"list-dos list-dos--dont")}</div>`:""}
            </div>`:"";return`
      <section class="section">
        <h2 id="${e.id}">${e.title}</h2>
        ${e.lead?`<p class="section-lead">${e.lead}</p>`:""}
        ${e.table?d(e.table.head,e.table.rows):""}
        ${(s=e.notes)!=null&&s.length?`<h3 class="subsection-title">Notas</h3>${i(e.notes)}`:""}
        ${l}
      </section>`}).join(""),t=(n.refs||[]).length?`<section class="section">
        <h2 id="referencias">Referencias</h2>
        <ul class="list-dos">${n.refs.map(e=>`<li><code>${e.ref}</code>${e.label!==e.ref?` — ${e.label}`:""}</li>`).join("")}</ul>
      </section>`:"";return o+t}function r(n){const o=document.getElementById("page-header"),t=document.getElementById("foundation-main");o&&(o.innerHTML=c(n)),t&&(t.innerHTML=a(n))}export{r as m};
