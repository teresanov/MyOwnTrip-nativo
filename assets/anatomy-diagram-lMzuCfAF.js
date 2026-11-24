import{a as T}from"./bootstrap-9SX-LJpI.js";function x(e){return String(e).replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;")}const V='<span class="pending">Pendiente</span>';function y(e){return e||V}function A(e){return String(e).normalize("NFD").replace(/[̀-ͯ]/g,"").toLowerCase().replace(/<[^>]*>/g,"").replace(/[^a-z0-9]+/g,"-").replace(/^-|-$/g,"")}function r(e,a,{lead:s="",generated:t=!1,tocLabel:o=""}={}){const n=o?` data-toc-label="${o}"`:"";return`
  <section class="section">
    <h2 id="${e}"${n}>${a}${t?'<span class="badge-generated" title="Se deriva del código fuente; no se escribe a mano">desde el código</span>':""}</h2>
    ${s?`<p class="section-lead">${s}</p>`:""}`}function b(e,a){return`
    <table>
      <thead><tr>${e.map(s=>`<th>${s}</th>`).join("")}</tr></thead>
      <tbody>${a.map(s=>`<tr>${s.map(t=>`<td>${t}</td>`).join("")}</tr>`).join("")}</tbody>
    </table>`}function h(e,a="list-dos"){return`<ul class="${a}">${e.map(s=>`<li>${s}</li>`).join("")}</ul>`}function E(e){return T(e)}function O(e){const[a,s,t]=String(e||"").split("-");return a&&s&&t?`${t}/${s}/${a}`:e}function Y(e,a="Component set en Figma"){var c;const s=typeof e.figma=="string"?{url:e.figma}:e.figma||{},t=e.implementation||{},o=((c=e.usage)==null?void 0:c.purpose)||e.purpose,n=[s.url?`<a href="${s.url}" target="_blank" rel="noopener">${s.node?`Figma · ${s.node}`:a} ↗</a>`:"",t.sourceFile?`<span class="header-meta">Código: <code>${t.sourceFile}</code></span>`:""].filter(Boolean).join('<span class="header-sep">·</span>');return`
    <p class="page-header__eyebrow">Componente · ${e.priority||"P0"}</p>
    <h1>${e.name} ${E(e.status)}</h1>
    <p class="page-header__lead">${y(o)}</p>
    ${n?`<p class="page-header__links">${n}</p>`:""}
    ${e.specimenHtml?S(e.specimenHtml):""}
  `}function S(e,a){return`
    <div class="specimen-stage">
      ${e}
    </div>
    <p class="specimen-stage__note">HTML con los mismos roles y medidas que Compose (1&nbsp;px = 1&nbsp;dp). No es el runtime de Android.</p>
  `}function Z({doHtml:e,doCaption:a,dontHtml:s,dontCaption:t}){return`
    <div class="dodont-visual">
      <figure class="dodont-visual__card dodont-visual__card--do">
        <p class="dodont-visual__kicker">Bien</p>
        <div class="dodont-visual__stage">${e}</div>
        <figcaption>${a}</figcaption>
      </figure>
      <figure class="dodont-visual__card dodont-visual__card--dont">
        <p class="dodont-visual__kicker">Mal</p>
        <div class="dodont-visual__stage">${s}</div>
        <figcaption>${t}</figcaption>
      </figure>
    </div>
  `}function ee(e){const a=e.usage;if(!a)return r("uso","Uso")+`<p>${V}</p></section>`;const s=(a.when||[]).length?`<h3 class="subsection-title">Cuándo usarlo</h3>${h(a.when)}`:"",t=(a.whenNot||[]).length?`<h3 class="subsection-title">Cuándo no usarlo</h3>
       ${b(["En este caso","Usa esto en su lugar"],a.whenNot.map(n=>[n.case,n.href?`<a href="${n.href}">${n.use}</a>`:n.use]))}`:"",o=(a.chooseVariant||[]).length?`<h3 class="subsection-title">Elegir variante</h3>
       <p class="section-lead">La jerarquía la marca la variante, no el color. Si dudas, empieza por la fila de arriba.</p>
       ${b(["Si…","Usa","Nota"],a.chooseVariant.map(n=>[n.if,`<code>${n.use}</code>`,n.note||""]))}`:"";return r("uso","Uso")+s+t+o+"</section>"}function se(e){const a=e.guidelines,s=e.productRules,t=e.dos||[],o=e.donts||[];let n="";if(a&&(t.length||o.length)){const c=(a.links||[{label:a.label,url:a.url}]).filter(d=>d&&d.url).map(d=>`<a href="${d.url}" target="_blank" rel="noopener">${d.label} ↗</a>`).join(" · ");n+=r("do-dont","Do y Don't",{lead:`Guía oficial de Material Design 3 para este componente: ${c} (consultada el ${O(a.consulted)}).`})+`<div class="guideline-grid">
        <div class="guideline-col guideline-col--do">
          <h3 class="subsection-title">Do</h3>${h(t)}
        </div>
        <div class="guideline-col guideline-col--dont">
          <h3 class="subsection-title">Don't</h3>${o.length?h(o,"list-dos list-dos--dont"):`<p class="section-lead">En las fuentes consultadas, M3 no lista «Don't» explícitos para este componente.</p>`}
        </div>
      </div></section>`}return s&&((s.dos||[]).length||(s.donts||[]).length)&&(n+=r("reglas","Reglas MyOwnTrip",{lead:a?"Acuerdos del equipo sobre cómo usamos el componente en MyOwnTrip. Complementan la guía de M3; no la contradicen.":"Componente propio de MyOwnTrip: no existe una guía de M3 equivalente, así que estas son las reglas del equipo."})+`<div class="guideline-grid">
        <div class="guideline-col guideline-col--do">
          <h3 class="subsection-title">Do</h3>${h(s.dos||[])}
        </div>
        <div class="guideline-col guideline-col--dont">
          <h3 class="subsection-title">Don't</h3>${h(s.donts||[],"list-dos list-dos--dont")}
        </div>
      </div></section>`),n}function ae({id:e,title:a,hint:s,canvasHtml:t,controlsHtml:o=""}){return r(e||A(a),a,{lead:s})+`<div class="playground__frame">
      <div class="playground__canvas">${t}</div>
      ${o?`<div class="playground__controls">${o}</div>`:""}
    </div></section>`}function te(e,a="Variantes del set"){if(!(e!=null&&e.length))return"";const s=e.some(n=>n.composable),t=s?["Variante","Compose","Cuándo usarla"]:["Variante","Cuándo usarla"],o=e.map(n=>s?[n.label,n.composable?`<code>${n.composable}</code>`:V,n.usage]:[n.label,n.usage]);return r(A(a),a,{lead:"Cada variante tiene que poder trazarse a un composable real.",tocLabel:"Variantes"})+b(t,o)+"</section>"}function ne(e){var t,o;if(!e)return"";const a=(e.rows||[]).some(n=>n.corner),s=(e.rows||[]).map(n=>a?[n.state,`<strong>${n.corner}</strong>`,`<code>${n.figma}</code>`,n.extra||""]:[n.state,n.detail||""]);return r("comportamiento","Comportamiento",{lead:e.lead})+(s.length?b(a?["Estado","Radio","Token Figma","Escala"]:["Estado","Qué ocurre"],s):"")+((t=e.notes)!=null&&t.length?`<h3 class="subsection-title">Detalles</h3>${h(e.notes)}`:"")+((o=e.exceptions)!=null&&o.length?`<h3 class="subsection-title">${e.exceptionsTitle||"Qué no lleva morph"}</h3>${h(e.exceptions)}`:"")+"</section>"}function oe(e){return e!=null&&e.length?r("accesibilidad","Accesibilidad",{lead:"Se comprueba en el gate <code>a11yCheck</code> antes de cerrar la pantalla."})+h(e)+"</section>":""}function ce(e){if(!e)return"";const a=[["Color",e.color],["Forma",e.shape],["Espaciado",e.spacing],["Tipografía",e.type]].filter(([,s])=>s==null?void 0:s.length);return a.length?r("tokens","Tokens y roles",{lead:"Roles consumidos en runtime. Ningún valor literal: si falta un rol, se añade al tema, no al componente.",generated:!0})+a.map(([s,t])=>`
        <h3 class="subsection-title">${s}</h3>
        ${b(["Rol","Para qué"],t.map(o=>[`<code>${o.token}</code>`,o.use]))}`).join("")+"</section>":""}function ie(e){if(!e)return"";const a=(e.params||[]).map(s=>[`<code>${s.name}</code>`,`<code>${x(s.type)}</code>`,s.default==="—"?"—":`<code>${x(s.default)}</code>`,s.note||""]);return r("implementacion","Implementación",{lead:`${e.note||""} Módulo <code>${e.module||"?"}</code> · fuente <code>${e.sourceFile||"?"}</code>`,generated:!0})+(e.import?`<pre><code>import ${x(e.import)}</code></pre>`:"")+(a.length?`<h3 class="subsection-title">Parámetros de <code>${e.composable}</code></h3>
         ${b(["Parámetro","Tipo","Por defecto","Nota"],a)}`:"")+(e.snippet?`<h3 class="subsection-title">Uso típico</h3><pre><code>${x(e.snippet)}</code></pre>`:"")+(e.variantsSnippet?`<h3 class="subsection-title">Otras variantes</h3><pre><code>${x(e.variantsSnippet)}</code></pre>`:"")+"</section>"}function le(e){var t;const a=e.decisions||{},s=(a.adr||[]).map(o=>`<li><code>${o.ref}</code> — ${o.label}</li>`).join("");return r("decisiones","Decisiones")+`<dl class="decisions__list">
      <dt>Fuente</dt><dd>${y(a.source)}</dd>
      <dt>Por qué</dt><dd>${y(a.why)}</dd>
      <dt>Capa</dt><dd>${y(a.layer)}</dd>
    </dl>
    ${s?`<h3 class="subsection-title">ADR</h3><ul class="list-dos">${s}</ul>`:""}
    ${(t=e.changelog)!=null&&t.length?`<h3 class="subsection-title">Changelog</h3>${b(["Fecha","Nota"],e.changelog.map(o=>[o.date,o.note]))}`:""}</section>`}function de(e){return e!=null&&e.length?r("relacionados","Relacionados")+`<div class="related-row">${e.map(a=>`<a class="related-link" href="${a.href}">${a.label}</a>`).join("")}</div></section>`:""}const B='<svg viewBox="0 0 24 24" width="24" height="24" aria-hidden="true"><path fill="currentColor" d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04a1 1 0 0 0 0-1.41l-2.34-2.34a1 1 0 0 0-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z"/></svg>',F='<svg viewBox="0 0 24 24" width="24" height="24" aria-hidden="true"><path fill="currentColor" d="M12 8a2 2 0 1 0 0-4 2 2 0 0 0 0 4zm0 6a2 2 0 1 0 0-4 2 2 0 0 0 0 4zm0 6a2 2 0 1 0 0-4 2 2 0 0 0 0 4z"/></svg>',re='<svg viewBox="0 0 24 24" width="24" height="24" aria-hidden="true"><path fill="currentColor" d="M19 4h-1V2h-2v2H8V2H6v2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 16H5V10h14v10zm0-12H5V6h14v2z"/></svg>',I='<svg viewBox="0 0 24 24" width="24" height="24" aria-hidden="true"><path fill="currentColor" d="M21 16v-2l-8-5V3.5A1.5 1.5 0 0 0 11.5 2 1.5 1.5 0 0 0 10 3.5V9l-8 5v2l8-2.5V19l-2 1.5V22l3.5-1 3.5 1v-1.5L13 19v-5.5l8 2.5z"/></svg>',P='<svg viewBox="0 0 24 24" width="18" height="18" aria-hidden="true"><path fill="currentColor" d="M19 6.41 17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/></svg>';function pe(e="Editar"){return`<button type="button" class="mot-icon-btn" aria-label="${e}">${B}</button>`}function ue({title:e="Billete tren",support:a="Transporte · 12 jun 2026",more:s=!0}={}){return`
    <div class="mot-li">
      <span class="mot-li__lead">${I}</span>
      <span class="mot-li__text"><strong>${e}</strong><span>${a}</span></span>
      ${s?`<span class="mot-li__trail">${F}</span>`:""}
    </div>`}function H({message:e="Gasto guardado",action:a="Deshacer",close:s=!0}={}){return`
    <div class="snackbar-demo">
      <span class="snackbar-demo__text">${e}</span>
      ${a?`<button type="button" class="snackbar-demo__action" tabindex="-1">${a}</button>`:""}
      ${s?`<button type="button" class="snackbar-demo__close" tabindex="-1" aria-label="Cerrar">${P}</button>`:""}
    </div>`}function C(e="Próximo viaje",a="tertiary",s="md"){return`<span class="eyebrow eyebrow--${a} eyebrow--${s}">${e}</span>`}function M({label:e,value:a="",placeholder:s="",error:t="",readonly:o=!1}){return`
    <label class="mot-field${t?" mot-field--error":""}">
      <span class="mot-field__label">${e}</span>
      <input class="mot-field__input" type="text" value="${a}" placeholder="${s}" ${o?"readonly":""} autocomplete="off" />
      ${t?`<span class="mot-field__support">${t}</span>`:""}
    </label>`}const k="Inter, system-ui, sans-serif",_=(e,a,s,t)=>`<svg class="ad-svg" viewBox="0 0 ${e} ${a}" role="img" aria-label="${t}" xmlns="http://www.w3.org/2000/svg">${s}</svg>`,u=(e,a,s,t,o)=>`<line x1="${a}" y1="${s}" x2="${t}" y2="${o}" class="ad-leader"/><circle cx="${t}" cy="${o}" r="3" class="ad-dot"/><circle cx="${a}" cy="${s}" r="15" class="ad-badge"/><text x="${a}" y="${s+5}" text-anchor="middle" class="ad-badge-text">${e}</text>`,m=(e,a,s,t,o,n="ad-pad")=>`<rect x="${e}" y="${a}" width="${s}" height="${t}" class="${n}"/>`+(o?`<text x="${e+s/2}" y="${a+t/2+4}" text-anchor="middle" class="ad-dim">${o}</text>`:""),j=(e,a,s,t)=>`<path d="M${e+s*.5} ${a+s*.15}v${s*.7}M${e+s*.15} ${a+s*.5}h${s*.7}" stroke="${t}" stroke-width="${Math.max(1.5,s*.08)}" stroke-linecap="square" fill="none"/>`,z='<svg class="mot-btn__icon" viewBox="0 0 24 24" width="18" height="18" aria-hidden="true"><circle cx="12" cy="12" r="10" fill="currentColor"/><path fill="var(--primary)" d="M12 6.8 13.6 11h4.6l-3.7 2.7 1.4 4.3L12 15.2l-3.9 2.8 1.4-4.3L5.8 11h4.6L12 6.8z"/></svg>',N='<svg class="filter-chip__icon" viewBox="0 0 24 24" width="18" height="18" aria-hidden="true"><path fill="currentColor" d="M9.55 18 3.85 12.3l1.4-1.4 4.3 4.3 9.2-9.2 1.4 1.4z"/></svg>',L='<svg class="dialog-demo__icon-svg" viewBox="0 0 24 24" width="24" height="24" aria-hidden="true"><path fill="currentColor" d="M12 2 1 21h22L12 2zm1 15h-2v-2h2v2zm0-4h-2V9h2v4z"/></svg>';function q(e){const a=`
    <div class="ad-live">
      <button type="button" class="mot-btn mot-btn--filled" tabindex="-1">
        ${z}Abrir cuaderno
      </button>
    </div>`,s=(o,n)=>`<span class="spec-n spec-n--${n}">${o}</span>`,t=`
    <div class="spec-stage">
      <div class="spec-live" style="--spec-pad-x:${e.padH}px;--spec-pad-y:${e.padV}px;--spec-gap:${e.gap}px;--spec-icon:${e.icon}px">
        <button type="button" class="mot-btn mot-btn--filled spec-ghost" tabindex="-1">${z}Abrir cuaderno</button>
        <div class="spec-overlay" aria-hidden="true">
          <span class="spec-band spec-band--pad-l">${s(e.padH,"pad")}</span>
          <span class="spec-band spec-band--pad-t">${s(e.padV,"pad")}</span>
          <span class="spec-band spec-band--pad-b"></span>
          <span class="spec-band spec-band--pad-r"></span>
          <span class="spec-band spec-band--gap">${s(e.gap,"gap")}</span>
        </div>
      </div>
    </div>`;return{anatomy:a,measures:t}}function R(e){const s=e.container*3,t=(560-s)/2,o=70,n=e.touch*3,c=o-(n-s)/2,d=t-(n-s)/2,i=e.icon*3,$=t+(s-i)/2,l=o+(s-i)/2,g=`<rect x="${t}" y="${o}" width="${s}" height="${s}" class="ad-fill-surface"/>`+j($,l,i,"var(--on-surface-variant)"),v=`<rect x="${d}" y="${c}" width="${n}" height="${n}" class="ad-touch"/>`+g+`<rect x="${t+5}" y="${o+5}" width="${s-10}" height="${s-10}" class="ad-layer"/>`+u(1,t-80,o+20,t,o+20)+u(2,t+s+80,o+20,$+i,l+i/2)+u(3,t-80,o+s-20,t+6,o+s-20)+u(4,280,c+n+42,280,c+n),p=(e.container-e.icon)/2*3,w=g+m(t,o,p,s,(e.container-e.icon)/2)+m(t+s-p,o,p,s,(e.container-e.icon)/2)+m(t+p,o,i,p,"","ad-pad ad-pad-v")+m(t+p,o+s-p,i,p,"","ad-pad ad-pad-v")+`<line x1="${t}" y1="${o+s+22}" x2="${t+s}" y2="${o+s+22}" class="ad-line"/><text x="${t+s/2}" y="${o+s+40}" text-anchor="middle" class="ad-dim ad-dim-out">${e.container} dp</text><line x1="${d}" y1="${c-18}" x2="${d+n}" y2="${c-18}" class="ad-line ad-line-touch"/><text x="${d+n/2}" y="${c-26}" text-anchor="middle" class="ad-dim ad-dim-out">${e.touch} dp (táctil)</text>`;return{anatomy:_(560,260,v,"Anatomía del icon button"),measures:_(560,260,w,"Medidas del icon button")}}function W(e){const n=e.circle*1.5,c=e.padH*1.5,d=e.padV*1.5,i=(e.circle+e.padV*2+8)*1.5,$=10+c+n/2,l=58+i/2,g=10+c+n+e.gap*1.5,v=e.glyph*1.5,p=`<rect x="10" y="58" width="540" height="${i}" class="ad-fill-row"/><rect x="10" y="58" width="4" height="${i}" class="ad-drift"/><circle cx="${$}" cy="${l}" r="${n/2}" class="ad-fill-container"/>`+j($-v/2,l-v/2,v,"var(--on-surface-variant)")+`<circle cx="${$+n/2-4}" cy="${l-n/2+4}" r="${e.dot*1.5/2}" class="ad-drift-dot"/><text x="${g}" y="${l-4}" font-family="${k}" font-weight="500" font-size="${16*1.5*.8}" fill="var(--on-surface)">Vuelo Lisboa – Madrid</text><text x="${g}" y="${l+18}" font-family="${k}" font-size="${14*1.5*.8}" fill="var(--on-surface-variant)">Vuelo · 14 mar · plan distinto</text><g class="ad-trailing"><circle cx="${550-c-6}" cy="${l}" r="3" fill="var(--on-surface-variant)"/><circle cx="${550-c-6}" cy="${l-9}" r="3" fill="var(--on-surface-variant)"/><circle cx="${550-c-6}" cy="${l+9}" r="3" fill="var(--on-surface-variant)"/></g>`,w=p+u(1,530,32,530,58)+u(2,$,32,$,l-n/2+2)+u(3,g+70,32,g+70,l-18)+u(4,g+150,58+i+34,g+150,l+12)+u(5,480,58+i+34,550-c-6,l+14)+u(6,50,58+i+34,12,58+i-6),D=p+m(14,58,c-4,i,e.padH,"ad-pad")+m(10+c,58,n,d,e.padV,"ad-pad ad-pad-v")+m(10+c,58+i-d,n,d,e.padV,"ad-pad ad-pad-v")+m(10+c+n,l-8,e.gap*1.5,16,"","ad-gap")+`<text x="${10+c+n+e.gap*1.5/2}" y="${58+i+26}" text-anchor="middle" class="ad-dim ad-dim-out">${e.gap}</text><text x="${$}" y="${58+i+26}" text-anchor="middle" class="ad-dim ad-dim-out">${e.circle}</text>`;return{anatomy:_(560,200,w,"Anatomía del list item"),measures:_(560,170,D,"Medidas del list item")}}function G(e){const s=`
    <div class="ad-live">
      <div class="filter-chip-row">
        ${`<button type="button" class="filter-chip filter-chip--selected" tabindex="-1">${N}Activos</button>`}
        <button type="button" class="filter-chip" tabindex="-1">Archivados</button>
        <button type="button" class="filter-chip" tabindex="-1">Todos</button>
      </div>
    </div>`,t=(n,c)=>`<span class="spec-n spec-n--${c}">${n}</span>`,o=`
    <div class="spec-stage">
      <div class="spec-live spec-live--chip" style="--spec-pad-x:${e.padHSelected}px;--spec-pad-y:${e.padV}px;--spec-gap:${e.gap}px;--spec-icon:${e.icon}px">
        <button type="button" class="filter-chip filter-chip--selected spec-ghost" tabindex="-1">${N}Activos</button>
        <div class="spec-overlay" aria-hidden="true">
          <span class="spec-band spec-band--pad-l">${t(e.padHSelected,"pad")}</span>
          <span class="spec-band spec-band--pad-t">${t(e.padV,"pad")}</span>
          <span class="spec-band spec-band--pad-b"></span>
          <span class="spec-band spec-band--pad-r"></span>
          <span class="spec-band spec-band--gap">${t(e.gap,"gap")}</span>
        </div>
      </div>
    </div>`;return{anatomy:s,measures:o}}function U(e){const a=`
      <div class="dialog-demo__icon">${L}</div>
      <h3 class="dialog-demo__title">¿Eliminar documento?</h3>
      <p class="dialog-demo__body">Se borrará el documento y su archivo del dispositivo. Esta acción no se puede deshacer.</p>
      <div class="dialog-demo__actions">
        <button type="button" class="mot-btn mot-btn--text" tabindex="-1">Cancelar</button>
        <button type="button" class="mot-btn mot-btn--text dialog-demo__destructive" tabindex="-1">Eliminar</button>
      </div>`,s=`<div class="ad-live ad-live--dialog"><div class="dialog-demo">${a}</div></div>`,t=(n,c)=>`<span class="spec-n spec-n--${c}">${n}</span>`,o=`
    <div class="spec-stage spec-stage--dialog">
      <div class="spec-live spec-live--dialog" style="--spec-pad-x:${e.pad}px;--spec-pad-y:${e.pad}px;--spec-gap:${e.actions}px">
        <div class="dialog-demo spec-ghost">${a}</div>
        <div class="spec-overlay" aria-hidden="true">
          <span class="spec-band spec-band--pad-l">${t(e.pad,"pad")}</span>
          <span class="spec-band spec-band--pad-t">${t(e.pad,"pad")}</span>
          <span class="spec-band spec-band--pad-b"></span>
          <span class="spec-band spec-band--pad-r"></span>
        </div>
      </div>
    </div>`;return{anatomy:s,measures:o}}function Q(e){const a=`<div class="ad-live">${H()}</div>`,s=(o,n)=>`<span class="spec-n spec-n--${n}">${o}</span>`,t=`
    <div class="spec-stage">
      <div class="spec-live spec-live--snackbar" style="--spec-pad-x:${e.padH}px;--spec-pad-y:${e.padV}px;--spec-gap:${e.gap}px;--spec-icon:0px">
        <div class="spec-ghost">${H()}</div>
        <div class="spec-overlay" aria-hidden="true">
          <span class="spec-band spec-band--pad-l">${s(e.padH,"pad")}</span>
          <span class="spec-band spec-band--pad-t">${s(e.padV,"pad")}</span>
          <span class="spec-band spec-band--pad-b"></span>
          <span class="spec-band spec-band--pad-r"></span>
        </div>
      </div>
    </div>`;return{anatomy:a,measures:t}}function K(e){const a=`<div class="ad-live ad-live--field">${M({label:"Nombre del viaje",value:"Barcelona 2026"})}</div>`,s=(o,n)=>`<span class="spec-n spec-n--${n}">${o}</span>`,t=`
    <div class="spec-stage">
      <div class="spec-live spec-live--field" style="--spec-pad-x:${e.padH}px;--spec-pad-y:${e.padV}px">
        <div class="spec-ghost">${M({label:"Nombre del viaje",value:"Barcelona 2026"})}</div>
        <div class="spec-overlay" aria-hidden="true">
          <span class="spec-band spec-band--pad-l">${s(e.padH,"pad")}</span>
          <span class="spec-band spec-band--pad-t">${s(e.padV,"pad")}</span>
          <span class="spec-band spec-band--pad-b"></span>
          <span class="spec-band spec-band--pad-r"></span>
        </div>
      </div>
    </div>`;return{anatomy:a,measures:t}}function J(){const e=`<div class="ad-live">
    ${C("Próximo viaje","tertiary","md")}
    ${C("En destino","surface","md")}
    ${C("Recuerdo","secondary","sm")}
  </div>`;return{anatomy:e,measures:e}}const f={button:q,"icon-button":R,"list-item":W,"filter-chip":G,dialog:U,snackbar:Q,"text-field":K,eyebrow:J};function $e(e,a){var o;if(!(e!=null&&e.length))return"";const s=a&&((o=f[a.kind])==null?void 0:o.call(f,a.values)),t=`<ol class="list-anatomy ad-legend">${e.map(n=>`<li>${n}</li>`).join("")}</ol>`;return r("anatomia","Anatomía",{lead:s?"Las partes del componente y cómo las llamamos en diseño y en código.":"Partes que componen el control y cómo se nombran en Figma y en Material 3."})+(s?`<div class="ad-layout"><figure class="ad-figure">${s.anatomy}</figure>${t}</div>`:t)+"</section>"}function ge(e){var s,t;const a=e&&((s=f[e.kind])==null?void 0:s.call(f,e.values));return a?r("medidas","Medidas",{lead:"El control se ve en velo; en color, solo padding y huecos (1 px = 1 dp)."})+`<div class="ad-layout"><figure class="ad-figure">${a.measures}</figure>`+((t=e.measures)!=null&&t.length?b(["Medida","Valor"],e.measures.map(o=>[o.label,`<strong>${o.value}</strong>`])):"")+"</div></section>":""}export{N as C,L as D,re as I,z as S,ee as a,ae as b,se as c,Z as d,te as e,$e as f,ge as g,ne as h,oe as i,ce as j,ie as k,le as l,de as m,C as n,B as o,pe as p,ue as q,Y as r,r as s,b as t,H as u,M as v};
