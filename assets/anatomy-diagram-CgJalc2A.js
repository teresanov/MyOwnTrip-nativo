import{a as I,b as N}from"./bootstrap-Cd92Zey5.js";function y(e){return String(e).replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;")}const M='<span class="pending">Pendiente</span>';function x(e){return e||M}function S(e){return String(e).normalize("NFD").replace(/[̀-ͯ]/g,"").toLowerCase().replace(/<[^>]*>/g,"").replace(/[^a-z0-9]+/g,"-").replace(/^-|-$/g,"")}function d(e,a,{lead:s="",generated:t=!1,tocLabel:o=""}={}){const n=o?` data-toc-label="${o}"`:"";return`
  <section class="section">
    <h2 id="${e}"${n}>${a}${t?'<span class="badge-generated" title="Se deriva del código fuente; no se escribe a mano">desde el código</span>':""}</h2>
    ${s?`<p class="section-lead">${s}</p>`:""}`}function b(e,a){return`
    <table>
      <thead><tr>${e.map(s=>`<th>${s}</th>`).join("")}</tr></thead>
      <tbody>${a.map(s=>`<tr>${s.map(t=>`<td>${t}</td>`).join("")}</tr>`).join("")}</tbody>
    </table>`}function g(e,a="list-dos"){return`<ul class="${a}">${e.map(s=>`<li>${s}</li>`).join("")}</ul>`}function P(e){return I(e)}function D(e){const[a,s,t]=String(e||"").split("-");return a&&s&&t?`${t}/${s}/${a}`:e}function le(e,a="Component set en Figma"){var i;const s=typeof e.figma=="string"?{url:e.figma}:e.figma||{},t=e.implementation||{},o=((i=e.usage)==null?void 0:i.purpose)||e.purpose,n=[s.url?`<a href="${s.url}" target="_blank" rel="noopener">${s.node?`Figma · ${s.node}`:a} ↗</a>`:"",t.sourceFile?`<span class="header-meta">Código: <code>${t.sourceFile}</code></span>`:""].filter(Boolean).join('<span class="header-sep">·</span>');return`
    <p class="page-header__eyebrow">Componente · ${e.priority||"P0"}</p>
    <h1>${e.name} ${P(e.status)}</h1>
    <p class="page-header__lead">${x(o)}</p>
    ${n?`<p class="page-header__links">${n}</p>`:""}
    ${e.specimenHtml?F(e.specimenHtml):""}
  `}function F(e,a){return`
    <div class="specimen-stage">
      ${e}
    </div>
    <p class="specimen-stage__note">${e.includes("data-mot-specimen")?"El componente real de la app, compilado a WebAssembly.":"Recreación en HTML con los mismos roles y medidas que Compose (1&nbsp;px = 1&nbsp;dp). No es el componente real."}</p>
  `}function de({doHtml:e,doCaption:a,dontHtml:s,dontCaption:t}){return`
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
  `}function re(e){const a=e.usage;if(!a)return d("uso","Uso")+`<p>${M}</p></section>`;const s=(a.when||[]).length?`<h3 class="subsection-title">Cuándo usarlo</h3>${g(a.when)}`:"",t=(a.whenNot||[]).length?`<h3 class="subsection-title">Cuándo no usarlo</h3>
       ${b(["En este caso","Usa esto en su lugar"],a.whenNot.map(n=>[n.case,n.href?`<a href="${n.href}">${n.use}</a>`:n.use]))}`:"",o=(a.chooseVariant||[]).length?`<h3 class="subsection-title">Elegir variante</h3>
       <p class="section-lead">La jerarquía la marca la variante, no el color. Si dudas, empieza por la fila de arriba.</p>
       ${b(["Si…","Usa","Nota"],a.chooseVariant.map(n=>[n.if,`<code>${n.use}</code>`,n.note||""]))}`:"";return d("uso","Uso")+s+t+o+"</section>"}function pe(e){const a=e.guidelines,s=e.productRules,t=e.dos||[],o=e.donts||[];let n="";if(a&&(t.length||o.length)){const i=(a.links||[{label:a.label,url:a.url}]).filter(r=>r&&r.url).map(r=>`<a href="${r.url}" target="_blank" rel="noopener">${r.label} ↗</a>`).join(" · ");n+=d("do-dont","Do y Don't",{lead:`Guía oficial de Material Design 3 para este componente: ${i} (consultada el ${D(a.consulted)}).`})+`<div class="guideline-grid">
        <div class="guideline-col guideline-col--do">
          <h3 class="subsection-title">Do</h3>${g(t)}
        </div>
        <div class="guideline-col guideline-col--dont">
          <h3 class="subsection-title">Don't</h3>${o.length?g(o,"list-dos list-dos--dont"):`<p class="section-lead">En las fuentes consultadas, M3 no lista «Don't» explícitos para este componente.</p>`}
        </div>
      </div></section>`}return s&&((s.dos||[]).length||(s.donts||[]).length)&&(n+=d("reglas","Reglas MyOwnTrip",{lead:a?"Acuerdos del equipo sobre cómo usamos el componente en MyOwnTrip. Complementan la guía de M3; no la contradicen.":"Componente propio de MyOwnTrip: no existe una guía de M3 equivalente, así que estas son las reglas del equipo."})+`<div class="guideline-grid">
        <div class="guideline-col guideline-col--do">
          <h3 class="subsection-title">Do</h3>${g(s.dos||[])}
        </div>
        <div class="guideline-col guideline-col--dont">
          <h3 class="subsection-title">Don't</h3>${g(s.donts||[],"list-dos list-dos--dont")}
        </div>
      </div></section>`),n}function ue({id:e,title:a,hint:s,canvasHtml:t,controlsHtml:o=""}){return d(e||S(a),a,{lead:s})+`<div class="playground__frame">
      <div class="playground__canvas">${t}</div>
      ${o?`<div class="playground__controls">${o}</div>`:""}
    </div></section>`}function $e(e,a="Variantes del set"){if(!(e!=null&&e.length))return"";const s=e.some(n=>n.composable),t=s?["Variante","Compose","Cuándo usarla"]:["Variante","Cuándo usarla"],o=e.map(n=>s?[n.label,n.composable?`<code>${n.composable}</code>`:M,n.usage]:[n.label,n.usage]);return d(S(a),a,{lead:"Cada variante tiene que poder trazarse a un composable real.",tocLabel:"Variantes"})+b(t,o)+"</section>"}function me(e){var t,o;if(!e)return"";const a=(e.rows||[]).some(n=>n.corner),s=(e.rows||[]).map(n=>a?[n.state,`<strong>${n.corner}</strong>`,`<code>${n.figma}</code>`,n.extra||""]:[n.state,n.detail||""]);return d("comportamiento","Comportamiento",{lead:e.lead})+(s.length?b(a?["Estado","Radio","Token Figma","Escala"]:["Estado","Qué ocurre"],s):"")+((t=e.notes)!=null&&t.length?`<h3 class="subsection-title">Detalles</h3>${g(e.notes)}`:"")+((o=e.exceptions)!=null&&o.length?`<h3 class="subsection-title">${e.exceptionsTitle||"Qué no lleva morph"}</h3>${g(e.exceptions)}`:"")+"</section>"}function ge(e){return e!=null&&e.length?d("accesibilidad","Accesibilidad",{lead:"Se comprueba en el gate <code>a11yCheck</code> antes de cerrar la pantalla."})+g(e)+"</section>":""}function he(e){if(!e)return"";const a=[["Color",e.color],["Forma",e.shape],["Espaciado",e.spacing],["Tipografía",e.type]].filter(([,s])=>s==null?void 0:s.length);return a.length?d("tokens","Tokens y roles",{lead:"Roles consumidos en runtime. Ningún valor literal: si falta un rol, se añade al tema, no al componente.",generated:!0})+a.map(([s,t])=>`
        <h3 class="subsection-title">${s}</h3>
        ${b(["Rol","Para qué"],t.map(o=>[`<code>${o.token}</code>`,o.use]))}`).join("")+"</section>":""}const R={material3:"de Material 3, sin componente propio",designsystem:"del design system de MyOwnTrip",app:"de la propia app (todavía fuera del design system)","material3 + designsystem":"de Material 3, con botones del design system"};function W(e){const a=R[e.module],s=e.sourceFile||"",t=[];if(a&&t.push(`Viene ${a}.`),s&&!s.startsWith("androidx.")){const o=s.split("/").pop(),n=o.includes(".")?`<code>${o}</code>`:`la carpeta <code>${o}</code>`,i=e.defines||s.startsWith("designsystem/")||s.includes("/ui/components/");t.push(i?`Está definido en ${n}.`:`Un ejemplo de uso en la app: ${n}.`)}return t.join(" ")}function be(e){if(!e)return"";const a=(e.params||[]).map(s=>[`<code>${s.name}</code>`,`<code>${y(s.type)}</code>`,s.default==="—"?"—":`<code>${y(s.default)}</code>`,s.note||""]);return d("implementacion","Implementación",{lead:`${e.note||""} ${W(e)}`,generated:!0})+(e.import?`<pre><code>import ${y(e.import)}</code></pre>`:"")+(a.length?`<h3 class="subsection-title">Parámetros de <code>${e.composable}</code></h3>
         ${b(["Parámetro","Tipo","Por defecto","Nota"],a)}`:"")+(e.snippet?`<h3 class="subsection-title">Uso típico</h3><pre><code>${y(e.snippet)}</code></pre>`:"")+(e.variantsSnippet?`<h3 class="subsection-title">Otras variantes</h3><pre><code>${y(e.variantsSnippet)}</code></pre>`:"")+"</section>"}function fe(e){var t;const a=e.decisions||{},s=(a.adr||[]).map(o=>`<li><code>${o.ref}</code> — ${o.label}</li>`).join("");return d("decisiones","Decisiones")+`<dl class="decisions__list">
      <dt>Fuente</dt><dd>${x(a.source)}</dd>
      <dt>Por qué</dt><dd>${x(a.why)}</dd>
      <dt>Capa</dt><dd>${x(a.layer)}</dd>
    </dl>
    ${s?`<h3 class="subsection-title">ADR</h3><ul class="list-dos">${s}</ul>`:""}
    ${(t=e.changelog)!=null&&t.length?`<h3 class="subsection-title">Changelog</h3>${b(["Fecha","Nota"],e.changelog.map(o=>[o.date,o.note]))}`:""}</section>`}function ve(e){return e!=null&&e.length?d("relacionados","Relacionados")+`<div class="related-row">${e.map(a=>`<a class="related-link" href="${a.href}">${a.label}</a>`).join("")}</div></section>`:""}const B=25e3;let V=!1;const G=e=>Object.entries(e).map(([a,s])=>`data-p-${a}="${String(s).replace(/"/g,"&quot;")}"`).join(" ");function L(e,a={},{height:s=168,label:t="Demo viva"}={}){return`
    <div class="live-host" role="group" aria-label="${t}" style="height:${s}px"
         data-mot-specimen="${e}" data-mot-base="${N("live/")}" ${G(a)}>
      <p class="live-host__msg">Cargando el componente real de Compose…</p>
    </div>`}function ye(e,a){e&&(Object.entries(a).forEach(([s,t])=>e.setAttribute(`data-p-${s}`,String(t))),e.dispatchEvent(new Event("mot:props")))}function H(e){if(V)return;V=!0;const a=document.createElement("script");a.src=N("live/demos.js"),a.onerror=()=>k(e,"No se encuentra el bundle de demos vivas (public/live/)."),document.head.appendChild(a),setTimeout(()=>{e.forEach(s=>{s.hasAttribute("data-mot-ready")||k([s],"No se ha podido cargar la demo. Necesita un navegador reciente con WebAssembly GC (Chrome, Edge, Firefox o Safari actuales).")})},B)}function k(e,a){e.forEach(s=>{const t=s.querySelector(".live-host__msg");t&&(t.textContent=a),s.classList.add("live-host--error")})}function xe(){const e=[...document.querySelectorAll("[data-mot-specimen]")];if(!e.length)return;if(!("IntersectionObserver"in window))return H(e);const a=new IntersectionObserver(s=>{s.some(t=>t.isIntersecting)&&(a.disconnect(),H(e))},{rootMargin:"200px"});e.forEach(s=>a.observe(s))}const U='<svg viewBox="0 0 24 24" width="24" height="24" aria-hidden="true"><path fill="currentColor" d="M12 8a2 2 0 1 0 0-4 2 2 0 0 0 0 4zm0 6a2 2 0 1 0 0-4 2 2 0 0 0 0 4zm0 6a2 2 0 1 0 0-4 2 2 0 0 0 0 4z"/></svg>',_e='<svg viewBox="0 0 24 24" width="24" height="24" aria-hidden="true"><path fill="currentColor" d="M19 4h-1V2h-2v2H8V2H6v2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 16H5V10h14v10zm0-12H5V6h14v2z"/></svg>',J='<svg viewBox="0 0 24 24" width="24" height="24" aria-hidden="true"><path fill="currentColor" d="M21 16v-2l-8-5V3.5A1.5 1.5 0 0 0 11.5 2 1.5 1.5 0 0 0 10 3.5V9l-8 5v2l8-2.5V19l-2 1.5V22l3.5-1 3.5 1v-1.5L13 19v-5.5l8 2.5z"/></svg>',Q='<svg viewBox="0 0 24 24" width="18" height="18" aria-hidden="true"><path fill="currentColor" d="M19 6.41 17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/></svg>';function we({title:e="Billete tren",support:a="Transporte · 12 jun 2026",more:s=!0}={}){return`
    <div class="mot-li">
      <span class="mot-li__lead">${J}</span>
      <span class="mot-li__text"><strong>${e}</strong><span>${a}</span></span>
      ${s?`<span class="mot-li__trail">${U}</span>`:""}
    </div>`}function E({message:e="Gasto guardado",action:a="Deshacer",close:s=!0}={}){return`
    <div class="snackbar-demo">
      <span class="snackbar-demo__text">${e}</span>
      ${a?`<button type="button" class="snackbar-demo__action" tabindex="-1">${a}</button>`:""}
      ${s?`<button type="button" class="snackbar-demo__close" tabindex="-1" aria-label="Cerrar">${Q}</button>`:""}
    </div>`}function C(e="Próximo viaje",a="tertiary",s="md"){return`<span class="eyebrow eyebrow--${a} eyebrow--${s}">${e}</span>`}function z({label:e,value:a="",placeholder:s="",error:t="",readonly:o=!1}){return`
    <label class="mot-field${t?" mot-field--error":""}">
      <span class="mot-field__label">${e}</span>
      <input class="mot-field__input" type="text" value="${a}" placeholder="${s}" ${o?"readonly":""} autocomplete="off" />
      ${t?`<span class="mot-field__support">${t}</span>`:""}
    </label>`}const A="Inter, system-ui, sans-serif",_=(e,a,s,t)=>`<svg class="ad-svg" viewBox="0 0 ${e} ${a}" role="img" aria-label="${t}" xmlns="http://www.w3.org/2000/svg">${s}</svg>`,u=(e,a,s,t,o)=>`<line x1="${a}" y1="${s}" x2="${t}" y2="${o}" class="ad-leader"/><circle cx="${t}" cy="${o}" r="3" class="ad-dot"/><circle cx="${a}" cy="${s}" r="15" class="ad-badge"/><text x="${a}" y="${s+5}" text-anchor="middle" class="ad-badge-text">${e}</text>`,h=(e,a,s,t,o,n="ad-pad")=>`<rect x="${e}" y="${a}" width="${s}" height="${t}" class="${n}"/>`+(o?`<text x="${e+s/2}" y="${a+t/2+4}" text-anchor="middle" class="ad-dim">${o}</text>`:""),T=(e,a,s,t)=>`<path d="M${e+s*.5} ${a+s*.15}v${s*.7}M${e+s*.15} ${a+s*.5}h${s*.7}" stroke="${t}" stroke-width="${Math.max(1.5,s*.08)}" stroke-linecap="square" fill="none"/>`,O='<svg class="mot-btn__icon" viewBox="0 0 24 24" width="18" height="18" aria-hidden="true"><circle cx="12" cy="12" r="10" fill="currentColor"/><path fill="var(--primary)" d="M12 6.8 13.6 11h4.6l-3.7 2.7 1.4 4.3L12 15.2l-3.9 2.8 1.4-4.3L5.8 11h4.6L12 6.8z"/></svg>',j='<svg class="filter-chip__icon" viewBox="0 0 24 24" width="18" height="18" aria-hidden="true"><path fill="currentColor" d="M9.55 18 3.85 12.3l1.4-1.4 4.3 4.3 9.2-9.2 1.4 1.4z"/></svg>',K='<svg class="dialog-demo__icon-svg" viewBox="0 0 24 24" width="24" height="24" aria-hidden="true"><path fill="currentColor" d="M12 2 1 21h22L12 2zm1 15h-2v-2h2v2zm0-4h-2V9h2v4z"/></svg>';function X(e){const a=`
    <div class="ad-live">
      <button type="button" class="mot-btn mot-btn--filled" tabindex="-1">
        ${O}Abrir cuaderno
      </button>
    </div>`,s=(o,n)=>`<span class="spec-n spec-n--${n}">${o}</span>`,t=`
    <div class="spec-stage">
      <div class="spec-live" style="--spec-pad-x:${e.padH}px;--spec-pad-y:${e.padV}px;--spec-gap:${e.gap}px;--spec-icon:${e.icon}px">
        <button type="button" class="mot-btn mot-btn--filled spec-ghost" tabindex="-1">${O}Abrir cuaderno</button>
        <div class="spec-overlay" aria-hidden="true">
          <span class="spec-band spec-band--pad-l">${s(e.padH,"pad")}</span>
          <span class="spec-band spec-band--pad-t">${s(e.padV,"pad")}</span>
          <span class="spec-band spec-band--pad-b"></span>
          <span class="spec-band spec-band--pad-r"></span>
          <span class="spec-band spec-band--gap">${s(e.gap,"gap")}</span>
        </div>
      </div>
    </div>`;return{anatomy:a,measures:t}}function Y(e){const s=e.container*3,t=(560-s)/2,o=70,n=e.touch*3,i=o-(n-s)/2,r=t-(n-s)/2,c=e.icon*3,$=t+(s-c)/2,l=o+(s-c)/2,m=`<rect x="${t}" y="${o}" width="${s}" height="${s}" class="ad-fill-surface"/>`+T($,l,c,"var(--on-surface-variant)"),v=`<rect x="${r}" y="${i}" width="${n}" height="${n}" class="ad-touch"/>`+m+`<rect x="${t+5}" y="${o+5}" width="${s-10}" height="${s-10}" class="ad-layer"/>`+u(1,t-80,o+20,t,o+20)+u(2,t+s+80,o+20,$+c,l+c/2)+u(3,t-80,o+s-20,t+6,o+s-20)+u(4,280,i+n+42,280,i+n),p=(e.container-e.icon)/2*3,w=m+h(t,o,p,s,(e.container-e.icon)/2)+h(t+s-p,o,p,s,(e.container-e.icon)/2)+h(t+p,o,c,p,"","ad-pad ad-pad-v")+h(t+p,o+s-p,c,p,"","ad-pad ad-pad-v")+`<line x1="${t}" y1="${o+s+22}" x2="${t+s}" y2="${o+s+22}" class="ad-line"/><text x="${t+s/2}" y="${o+s+40}" text-anchor="middle" class="ad-dim ad-dim-out">${e.container} dp</text><line x1="${r}" y1="${i-18}" x2="${r+n}" y2="${i-18}" class="ad-line ad-line-touch"/><text x="${r+n/2}" y="${i-26}" text-anchor="middle" class="ad-dim ad-dim-out">${e.touch} dp (táctil)</text>`;return{anatomy:_(560,260,v,"Anatomía del icon button"),measures:_(560,260,w,"Medidas del icon button")}}function Z(e){const n=e.circle*1.5,i=e.padH*1.5,r=e.padV*1.5,c=(e.circle+e.padV*2+8)*1.5,$=10+i+n/2,l=58+c/2,m=10+i+n+e.gap*1.5,v=e.glyph*1.5,p=`<rect x="10" y="58" width="540" height="${c}" class="ad-fill-row"/><rect x="10" y="58" width="4" height="${c}" class="ad-drift"/><circle cx="${$}" cy="${l}" r="${n/2}" class="ad-fill-container"/>`+T($-v/2,l-v/2,v,"var(--on-surface-variant)")+`<circle cx="${$+n/2-4}" cy="${l-n/2+4}" r="${e.dot*1.5/2}" class="ad-drift-dot"/><text x="${m}" y="${l-4}" font-family="${A}" font-weight="500" font-size="${16*1.5*.8}" fill="var(--on-surface)">Vuelo Lisboa – Madrid</text><text x="${m}" y="${l+18}" font-family="${A}" font-size="${14*1.5*.8}" fill="var(--on-surface-variant)">Vuelo · 14 mar · plan distinto</text><g class="ad-trailing"><circle cx="${550-i-6}" cy="${l}" r="3" fill="var(--on-surface-variant)"/><circle cx="${550-i-6}" cy="${l-9}" r="3" fill="var(--on-surface-variant)"/><circle cx="${550-i-6}" cy="${l+9}" r="3" fill="var(--on-surface-variant)"/></g>`,w=p+u(1,530,32,530,58)+u(2,$,32,$,l-n/2+2)+u(3,m+70,32,m+70,l-18)+u(4,m+150,58+c+34,m+150,l+12)+u(5,480,58+c+34,550-i-6,l+14)+u(6,50,58+c+34,12,58+c-6),q=p+h(14,58,i-4,c,e.padH,"ad-pad")+h(10+i,58,n,r,e.padV,"ad-pad ad-pad-v")+h(10+i,58+c-r,n,r,e.padV,"ad-pad ad-pad-v")+h(10+i+n,l-8,e.gap*1.5,16,"","ad-gap")+`<text x="${10+i+n+e.gap*1.5/2}" y="${58+c+26}" text-anchor="middle" class="ad-dim ad-dim-out">${e.gap}</text><text x="${$}" y="${58+c+26}" text-anchor="middle" class="ad-dim ad-dim-out">${e.circle}</text>`;return{anatomy:_(560,200,w,"Anatomía del list item"),measures:_(560,170,q,"Medidas del list item")}}function ee(e){const s=`
    <div class="ad-live">
      <div class="filter-chip-row">
        ${`<button type="button" class="filter-chip filter-chip--selected" tabindex="-1">${j}Activos</button>`}
        <button type="button" class="filter-chip" tabindex="-1">Archivados</button>
        <button type="button" class="filter-chip" tabindex="-1">Todos</button>
      </div>
    </div>`,t=(n,i)=>`<span class="spec-n spec-n--${i}">${n}</span>`,o=`
    <div class="spec-stage">
      <div class="spec-live spec-live--chip" style="--spec-pad-x:${e.padHSelected}px;--spec-pad-y:${e.padV}px;--spec-gap:${e.gap}px;--spec-icon:${e.icon}px">
        <button type="button" class="filter-chip filter-chip--selected spec-ghost" tabindex="-1">${j}Activos</button>
        <div class="spec-overlay" aria-hidden="true">
          <span class="spec-band spec-band--pad-l">${t(e.padHSelected,"pad")}</span>
          <span class="spec-band spec-band--pad-t">${t(e.padV,"pad")}</span>
          <span class="spec-band spec-band--pad-b"></span>
          <span class="spec-band spec-band--pad-r"></span>
          <span class="spec-band spec-band--gap">${t(e.gap,"gap")}</span>
        </div>
      </div>
    </div>`;return{anatomy:s,measures:o}}function se(e){const a=`
      <div class="dialog-demo__icon">${K}</div>
      <h3 class="dialog-demo__title">¿Eliminar documento?</h3>
      <p class="dialog-demo__body">Se borrará el documento y su archivo del dispositivo. Esta acción no se puede deshacer.</p>
      <div class="dialog-demo__actions">
        <button type="button" class="mot-btn mot-btn--text" tabindex="-1">Cancelar</button>
        <button type="button" class="mot-btn mot-btn--text dialog-demo__destructive" tabindex="-1">Eliminar</button>
      </div>`,s=`<div class="ad-live ad-live--dialog"><div class="dialog-demo">${a}</div></div>`,t=(n,i)=>`<span class="spec-n spec-n--${i}">${n}</span>`,o=`
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
    </div>`;return{anatomy:s,measures:o}}function ae(e){const a=`<div class="ad-live">${E()}</div>`,s=(o,n)=>`<span class="spec-n spec-n--${n}">${o}</span>`,t=`
    <div class="spec-stage">
      <div class="spec-live spec-live--snackbar" style="--spec-pad-x:${e.padH}px;--spec-pad-y:${e.padV}px;--spec-gap:${e.gap}px;--spec-icon:0px">
        <div class="spec-ghost">${E()}</div>
        <div class="spec-overlay" aria-hidden="true">
          <span class="spec-band spec-band--pad-l">${s(e.padH,"pad")}</span>
          <span class="spec-band spec-band--pad-t">${s(e.padV,"pad")}</span>
          <span class="spec-band spec-band--pad-b"></span>
          <span class="spec-band spec-band--pad-r"></span>
        </div>
      </div>
    </div>`;return{anatomy:a,measures:t}}function te(e){const a=`<div class="ad-live ad-live--field">${z({label:"Nombre del viaje",value:"Barcelona 2026"})}</div>`,s=(o,n)=>`<span class="spec-n spec-n--${n}">${o}</span>`,t=`
    <div class="spec-stage">
      <div class="spec-live spec-live--field" style="--spec-pad-x:${e.padH}px;--spec-pad-y:${e.padV}px">
        <div class="spec-ghost">${z({label:"Nombre del viaje",value:"Barcelona 2026"})}</div>
        <div class="spec-overlay" aria-hidden="true">
          <span class="spec-band spec-band--pad-l">${s(e.padH,"pad")}</span>
          <span class="spec-band spec-band--pad-t">${s(e.padV,"pad")}</span>
          <span class="spec-band spec-band--pad-b"></span>
          <span class="spec-band spec-band--pad-r"></span>
        </div>
      </div>
    </div>`;return{anatomy:a,measures:t}}function ne(){const e=`<div class="ad-live">
    ${C("Próximo viaje","tertiary","md")}
    ${C("En destino","surface","md")}
    ${C("Recuerdo","secondary","sm")}
  </div>`;return{anatomy:e,measures:e}}const f={button:X,"icon-button":Y,"list-item":Z,"filter-chip":ee,dialog:se,snackbar:ae,"text-field":te,eyebrow:ne};function oe(e,a){const s=`<ol class="list-anatomy ad-legend" id="li-legend">${e.map((t,o)=>{var n;return`<li tabindex="0" data-part="${((n=a.markers[o])==null?void 0:n.part)||""}">${t}</li>`}).join("")}</ol>`;return d("anatomia","Anatomía",{lead:"Las partes del componente y cómo las llamamos en diseño y en código. El dibujo es el componente real de la app: cada línea llega a la pieza que mide Compose. Pasa el cursor por la lista para resaltar cada parte."})+`<div class="ad-layout ad-layout--live">
       <figure class="ad-figure li-wrap" data-inspect="anatomy" data-legend="#li-legend" data-markers='${JSON.stringify(a.markers)}'>
         ${L(a.specimen,{zoom:a.zoom,theme:"light",...a.props||{}},{height:a.height,label:"Anatomía del componente real"})}
         <svg class="li-overlay" aria-hidden="true"></svg>
       </figure>
       ${s}
     </div></section>`}function ie(e){const a=e.live,s=e.values||{};return d("medidas","Medidas",{lead:"Pasa el cursor sobre el componente: como el inspector del navegador, se resalta cada pieza con su tamaño y sus distancias en dp. En el contenedor, el verde es el espacio interior y el azul, el contenido. Los números se miden en el componente real, no se escriben a mano."})+`<div class="ad-layout ad-layout--live">
       <figure class="ad-figure li-wrap" data-inspect="measures" data-table="#li-measures" data-rows='${JSON.stringify(a.rows||null)}' data-gaps='${JSON.stringify(a.gaps||[])}'>
         ${L(a.specimen,{zoom:a.zoom,theme:"light",...a.props||{}},{height:a.height,label:"Medidas del componente real"})}
         <svg class="li-overlay" aria-hidden="true"></svg>
         <div class="li-hit" aria-hidden="true"></div>
       </figure>
       <div>
         <table><thead><tr><th>Medida</th><th>Medido en Compose</th></tr></thead>
           <tbody id="li-measures"><tr><td colspan="2">Midiendo el componente…</td></tr></tbody></table>
         ${s.padH!=null?`<p class="li-note">Padding declarado en código: ${s.padH} × ${s.padV} dp. El espacio arriba y abajo puede ser mayor porque el contenedor tiene una altura mínima y centra el contenido.</p>`:""}
       </div>
     </div></section>`}function Ce(e,a){var o;if(!(e!=null&&e.length))return"";if(a!=null&&a.live)return oe(e,a.live);const s=a&&((o=f[a.kind])==null?void 0:o.call(f,a.values)),t=`<ol class="list-anatomy ad-legend">${e.map(n=>`<li>${n}</li>`).join("")}</ol>`;return d("anatomia","Anatomía",{lead:s?"Las partes del componente y cómo las llamamos en diseño y en código.":"Partes que componen el control y cómo se nombran en Figma y en Material 3."})+(s?`<div class="ad-layout"><figure class="ad-figure">${s.anatomy}</figure>${t}</div>`:t)+"</section>"}function Me(e){var s,t;if(e!=null&&e.live)return ie(e);const a=e&&((s=f[e.kind])==null?void 0:s.call(f,e.values));return a?d("medidas","Medidas",{lead:"El control se ve en velo; en color, solo padding y huecos (1 px = 1 dp)."})+`<div class="ad-layout"><figure class="ad-figure">${a.measures}</figure>`+((t=e.measures)!=null&&t.length?b(["Medida","Valor"],e.measures.map(o=>[o.label,`<strong>${o.value}</strong>`])):"")+"</div></section>":""}export{_e as I,O as S,re as a,d as b,ue as c,pe as d,de as e,$e as f,Ce as g,Me as h,me as i,ge as j,he as k,L as l,be as m,fe as n,ve as o,xe as p,C as q,le as r,ye as s,b as t,we as u};
