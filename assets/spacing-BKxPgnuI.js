import{i as L}from"./bootstrap-BsJu56ug.js";import{m as B}from"./foundation-page-DE6ZXlIT.js";import{S as r}from"./anatomy-diagram-06YisZb7.js";const A="Espaciado",E="Foundations",C="Las pantallas miden con <code>MOTSpacing</code>. El padding interno de un componente M3 es otra cosa: no se sustituye por un hueco de pantalla.",q="MOTSpacing.kt · variables mot/spacing/* y mot/padding/*",H=[{id:"principio",title:"Cómo se usa",paragraphs:["En UI se consume el nombre semántico: <code>MOTSpacing.screenHorizontal</code> es el mismo valor que la variable <code>mot/spacing/screen-horizontal</code>. Los primitivos (<code>spacing/12</code>, <code>spacing/16</code>…) solo existen como alias detrás de ese nombre. Nunca se usan en un frame de producto.","El orden es Compose primero, luego el token. No se crea un <code>mot/*</code> sin espejo en Kotlin."],dos:["Padding de página y columnas con <code>screen-horizontal</code>, <code>layout-md</code> / <code>layout-lg</code>, <code>component-sm</code> / <code>component-xs</code>.","En Compose: <code>Modifier.padding(horizontal = MOTSpacing.screenHorizontal)</code>."],donts:["Bindear un primitivo (<code>spacing/12</code>) en un nodo de UI.","Usar <code>LEGACY · Semantic Layout</code> ni portar <code>LayoutTokens</code> del repo archivo.","Confundir un inset de pantalla (<code>layout-md</code>) con el padding de un botón (<code>mot/padding/button/*</code>)."]},{id:"escala",title:"Escala",lead:"Cada barra mide el valor real (1 px = 1 dp). Varios roles pueden compartir el mismo primitivo: el nombre dice para qué sirve, no el número."},{id:"pantalla",title:"Espaciado de pantalla",lead:"Márgenes del Body, gutters y huecos entre bloques. El dibujo es un cuerpo de 360 dp con los tokens de una pantalla MOT.",table:{head:["Compose","Variable","dp","Uso"],rows:[["<code>screenHorizontal</code>","<code>mot/spacing/screen-horizontal</code>","16","Padding H del Body (a menudo también arriba)"],["<code>screenContentBottom</code>","<code>mot/spacing/screen-content-bottom</code>","24","Colchón inferior sin FAB"],["<code>screenContentBottomWithFab</code>","<code>mot/spacing/screen-content-bottom-with-fab</code>","88","Listas con FAB en la misma pantalla"],["<code>layoutMd</code>","<code>mot/spacing/layout-md</code>","16","Hueco entre bloques"],["<code>layoutLg</code>","<code>mot/spacing/layout-lg</code>","24","Hueco de sección"],["<code>gutterGrid</code>","<code>mot/spacing/gutter-grid</code>","16","Gutter de grid"]]}},{id:"componente",title:"En vivo",lead:"Las tablas dicen qué tokens existen; aquí se ve en qué pieza se aplican. Son especímenes del kit (1 px = 1 dp). Verde es padding, ámbar es gap. Pasa el cursor por la pieza o por la fila."},{id:"padding",title:"Padding interno",lead:"El padding es el colchón <strong>dentro</strong> del control: entre su borde y el icono o el texto. No es el margen de la pantalla ni el hueco entre dos piezas.",paragraphs:["Hay tres medidas distintas y no se intercambian. La pantalla usa <code>mot/spacing/screen-horizontal</code> (16). El hueco icono–label o entre chips usa <code>mot/spacing/component-*</code> o un gap del componente. El padding del control vive en <code>mot/padding/…</code> y en objetos anidados de <code>MOTSpacing</code> (<code>ButtonSmall</code>, <code>Chip</code>, <code>ListItem</code>…).","<code>MOTButton</code> (filled y outlined) mide 24 × 8: el padding por defecto de Material 3, en código <code>MOTSpacing.ButtonSmall</code>. <code>MOTTonalButton</code> es más compacto: 12 × 6 (<code>ButtonXSmall</code>). Medium (24 × 16) y Large (48 × 32) están en <code>MOTSpacing</code> y se dibujan abajo; no hay composable <code>MOTButton</code> para esas tallas.","Cada ficha es el control con su padding. El overlay verde es solo el padding; los huecos están en En vivo."]}],z=[{label:"docs/design-system/spacing.md",ref:"docs/design-system/spacing.md"},{label:"MOTSpacing.kt",ref:"designsystem/src/main/java/com/myowntrip/app/ui/theme/MOTSpacing.kt"}],w={name:A,eyebrow:E,lead:C,sourceOfTruth:q,sections:H,refs:z};L("spacing");B(w);const F=[{compose:"componentXs",figma:"component-xs",dp:4},{compose:"componentSm",figma:"component-sm",dp:8},{compose:"componentMd",figma:"component-md",dp:12},{compose:"componentLg",figma:"component-lg",dp:16},{compose:"layoutMd",figma:"layout-md",dp:16},{compose:"screenHorizontal",figma:"screen-horizontal",dp:16},{compose:"gutterGrid",figma:"gutter-grid",dp:16},{compose:"layoutLg",figma:"layout-lg",dp:24},{compose:"screenContentBottom",figma:"screen-content-bottom",dp:24},{compose:"screenContentBottomWithFab",figma:"screen-content-bottom-with-fab",dp:88}],s=(e,t,n=!1)=>`<span class="spec-n spec-n--${t}${n?"":" live-redline"}">${e}</span>`,V=(e,t)=>`<code>${e}</code><span class="token-alias">${t}</span>`,M='<svg viewBox="0 0 24 24" aria-hidden="true"><path fill="currentColor" d="M21 16v-2l-8-5V3.5A1.5 1.5 0 0 0 11.5 2 1.5 1.5 0 0 0 10 3.5V9l-8 5v2l8-2.5V19l-2 1.5V22l3.5-1 3.5 1v-1.5L13 19v-5.5z"/></svg>',O='<svg class="mot-li__trail" viewBox="0 0 24 24" aria-hidden="true"><circle cx="12" cy="5" r="2" fill="currentColor"/><circle cx="12" cy="12" r="2" fill="currentColor"/><circle cx="12" cy="19" r="2" fill="currentColor"/></svg>';function j(){return`
    <div class="sp-scale" role="img" aria-label="Escala MOTSpacing en dp">
      ${F.map(e=>`
        <div class="sp-scale__row">
          <code class="sp-scale__name">${e.compose}</code>
          <span class="sp-scale__dp">${e.dp}</span>
          <div class="sp-scale__track">
            <span class="sp-scale__bar" style="width:${e.dp}px"></span>
          </div>
        </div>`).join("")}
    </div>`}function P(){return`
    <div class="sp-phone-stage">
      <div class="sp-phone">
        <div class="sp-phone__pad sp-phone__pad--t">${s(16,"pad")}</div>
        <div class="sp-phone__mid">
          <div class="sp-phone__pad sp-phone__pad--x">${s(16,"pad")}</div>
          <div class="sp-phone__col">
            <div class="sp-phone__block sp-phone__block--hero">
              <p class="sp-phone__kicker">Viaje</p>
              <p class="sp-phone__title">Barcelona</p>
            </div>
            <div class="sp-phone__gap">${s(16,"gap")}</div>
            <div class="sp-phone__block">
              <strong>Wallet</strong>
              <span>3 apuntes esta semana</span>
            </div>
            <div class="sp-phone__gap">${s(16,"gap")}</div>
            <button type="button" class="mot-btn mot-btn--filled sp-phone__cta" tabindex="-1">${r}Abrir cuaderno</button>
          </div>
          <div class="sp-phone__pad sp-phone__pad--x"></div>
        </div>
        <div class="sp-phone__pad sp-phone__pad--b">${s(24,"pad")}</div>
      </div>
      <p class="sp-phone__caption">360 dp · <code>screenHorizontal</code> 16 · <code>layoutMd</code> 16 · <code>screenContentBottom</code> 24</p>
    </div>`}function h(e,t,n,c,a,o,m){return`
    <div class="spec-live" data-part="${m}" style="--spec-pad-x:${t}px;--spec-pad-y:${n}px;--spec-gap:${c}px;--spec-icon:${a}px">
      <button type="button" class="mot-btn ${e} spec-ghost" tabindex="-1">${r}${o}</button>
      <div class="spec-overlay live-redline" aria-hidden="true">
        <span class="spec-band spec-band--pad-l">${s(t,"pad")}</span>
        <span class="spec-band spec-band--pad-t">${s(n,"pad")}</span>
        <span class="spec-band spec-band--pad-b"></span>
        <span class="spec-band spec-band--pad-r"></span>
        <span class="spec-band spec-band--gap">${s(c,"gap")}</span>
      </div>
    </div>`}function I(e){return e.map(t=>`
      <tr data-part="${t.part}">
        <td>${t.parte}</td>
        <td>${t.kind==="pad"?"Padding":"Gap"}</td>
        <td>${t.value}</td>
        <td>${V(t.compose,t.figma)}</td>
      </tr>`).join("")}function u(e,t){return`
    <div class="live-kit__table-wrap" data-table="${e}">
      <table>
        <thead><tr><th>Parte</th><th>Propiedad</th><th>Valor</th><th>Token</th></tr></thead>
        <tbody>${I(t)}</tbody>
      </table>
    </div>`}const G=[{part:"btn-small",parte:"MOTButton",kind:"pad",value:"24 × 8 dp",compose:"MOTSpacing.ButtonSmall",figma:"mot/padding/button/small-*"},{part:"btn-small",parte:"MOTButton · icono y label",kind:"gap",value:"8 dp",compose:"MOTSpacing.componentSm",figma:"mot/spacing/component-sm"},{part:"btn-xs",parte:"MOTTonalButton",kind:"pad",value:"12 × 6 dp",compose:"MOTSpacing.ButtonXSmall",figma:"mot/padding/button/xsmall-*"},{part:"btn-xs",parte:"MOTTonalButton · icono y label",kind:"gap",value:"8 dp",compose:"MOTSpacing.componentSm",figma:"mot/spacing/component-sm"}],N=[{part:"chip",parte:"FilterChip",kind:"pad",value:"16 × 6 dp",compose:"MOTSpacing.Chip.horizontalLg × vertical",figma:"mot/padding/chip/horizontal-lg · vertical"},{part:"chip-gap",parte:"Fila Wallet · entre chips",kind:"gap",value:"8 dp",compose:"MOTSpacing.componentSm",figma:"mot/spacing/chip/gap"}],W=[{part:"li",parte:"WalletDocumentRow",kind:"pad",value:"16 × 12 dp",compose:"MOTSpacing.ListItem.horizontal × vertical",figma:"mot/padding/list-item/horizontal · vertical"},{part:"li",parte:"Leading · texto",kind:"gap",value:"12 dp",compose:"MOTSpacing.ListItem.gap",figma:"mot/spacing/list-item/gap"},{part:"li",parte:"Texto · trailing",kind:"gap",value:"10 dp",compose:"MOTSpacing.ListItem.trailingGap",figma:"mot/spacing/list-item/trailing-gap"}];function R(){return`
    <div class="live-kit" data-tab="button" data-names="compose" data-redlines="on">
      <div class="live-kit__bar">
        <div class="live-seg" role="tablist" aria-label="Pieza">
          <button type="button" role="tab" aria-selected="true" data-tab="button">Botón</button>
          <button type="button" role="tab" aria-selected="false" data-tab="chip">Filter chip</button>
          <button type="button" role="tab" aria-selected="false" data-tab="list">List item</button>
        </div>
        <div class="live-seg" aria-label="Lectura">
          <button type="button" data-names="figma" aria-pressed="false">Figma</button>
          <button type="button" data-names="compose" aria-pressed="true">Compose</button>
          <button type="button" data-redlines aria-pressed="true">Redlines</button>
        </div>
      </div>

      <div class="live-kit__stage">
        <div class="live-kit__panel live-kit__panel--button" data-panel="button">
          ${h("mot-btn--filled",24,8,8,18,"Abrir cuaderno","btn-small")}
          ${h("mot-btn--tonal mot-btn--xs",12,6,8,18,"Siguiente","btn-xs")}
        </div>
        <div class="live-kit__panel live-kit__panel--chip" data-panel="chip">
          <div class="live-chip-row">
            <div class="spec-live" data-part="chip" style="--spec-pad-x:16px;--spec-pad-y:6px">
              <button type="button" class="filter-chip filter-chip--selected spec-ghost" tabindex="-1">Activos</button>
              <div class="spec-overlay live-redline" aria-hidden="true">
                <span class="spec-band spec-band--pad-l">${s(16,"pad")}</span>
                <span class="spec-band spec-band--pad-t">${s(6,"pad")}</span>
                <span class="spec-band spec-band--pad-b"></span>
                <span class="spec-band spec-band--pad-r"></span>
              </div>
            </div>
            <div class="live-slot live-slot--h live-redline" data-part="chip-gap" style="--slot:8px">${s(8,"gap")}</div>
            <button type="button" class="filter-chip spec-ghost" tabindex="-1">Archivados</button>
            <div class="live-slot live-slot--h live-redline" data-part="chip-gap" style="--slot:8px"></div>
            <button type="button" class="filter-chip spec-ghost" tabindex="-1">Todos</button>
          </div>
        </div>
        <div class="live-kit__panel live-kit__panel--list" data-panel="list">
          <div class="spec-live mot-li-wrap" data-part="li" style="--spec-pad-x:16px;--spec-pad-y:12px;--spec-gap:12px;--spec-icon:40px;--spec-trail:10px">
            <div class="mot-li spec-ghost">
              <span class="mot-li__lead">${M}</span>
              <div class="mot-li__text"><strong>Vuelo Lisboa – Madrid</strong><span>Vuelo · 14 mar</span></div>
              ${O}
            </div>
            <div class="spec-overlay live-redline" aria-hidden="true">
              <span class="spec-band spec-band--pad-l">${s(16,"pad")}</span>
              <span class="spec-band spec-band--pad-t">${s(12,"pad")}</span>
              <span class="spec-band spec-band--pad-b"></span>
              <span class="spec-band spec-band--pad-r"></span>
              <span class="spec-band spec-band--gap">${s(12,"gap")}</span>
              <span class="spec-band spec-band--trail">${s(10,"gap")}</span>
            </div>
          </div>
        </div>
      </div>

      <p class="live-kit__legend">
        <span><i class="live-kit__swatch live-kit__swatch--pad"></i>Padding</span>
        <span><i class="live-kit__swatch live-kit__swatch--gap"></i>Gap</span>
        <span>Todos los valores salen de un token</span>
      </p>
      ${u("button",G)}
      ${u("chip",N)}
      ${u("list",W)}
    </div>`}function U(e){const t=a=>{e.dataset.tab=a,e.querySelectorAll("[data-tab]").forEach(o=>{o.getAttribute("data-tab")&&o.tagName==="BUTTON"&&o.closest("[role=tablist]")&&o.setAttribute("aria-selected",String(o.getAttribute("data-tab")===a))}),e.querySelectorAll("[data-table]").forEach(o=>{o.hidden=o.getAttribute("data-table")!==a})};e.querySelectorAll("[role=tablist] [data-tab]").forEach(a=>{a.addEventListener("click",()=>t(a.getAttribute("data-tab")))}),e.querySelectorAll("[data-names]").forEach(a=>{a.tagName==="BUTTON"&&a.addEventListener("click",()=>{e.dataset.names=a.getAttribute("data-names"),e.querySelectorAll("button[data-names]").forEach(o=>{o.setAttribute("aria-pressed",String(o===a))})})});const n=e.querySelector("[data-redlines]");n==null||n.addEventListener("click",()=>{const a=e.dataset.redlines!=="on";e.dataset.redlines=a?"on":"off",n.setAttribute("aria-pressed",String(a))});const c=(a,o)=>{e.querySelectorAll(`[data-part="${a}"]`).forEach(m=>{m.classList.toggle("is-hot",o)})};e.querySelectorAll("[data-part]").forEach(a=>{a.addEventListener("mouseenter",()=>c(a.getAttribute("data-part"),!0)),a.addEventListener("mouseleave",()=>c(a.getAttribute("data-part"),!1))}),t("button")}function i(e,t,n){return`
    <div class="spec-live" style="--spec-pad-x:${t}px;--spec-pad-y:${n}px">
      ${e}
      <div class="spec-overlay" aria-hidden="true">
        <span class="spec-band spec-band--pad-l">${s(t,"pad",!0)}</span>
        <span class="spec-band spec-band--pad-t">${s(n,"pad",!0)}</span>
        <span class="spec-band spec-band--pad-b"></span>
        <span class="spec-band spec-band--pad-r"></span>
      </div>
    </div>`}function l({title:e,codes:t,href:n,label:c,inner:a}){return`
    <article class="pad-card">
      <header class="pad-card__head">
        <h3 class="pad-card__title">${e}</h3>
        <p class="pad-card__codes">${t}</p>
        <p class="pad-card__link">Espécimen de <a href="${n}">${c}</a></p>
      </header>
      <div class="playground__frame">
        <div class="playground__canvas pad-card__canvas">${a}</div>
      </div>
    </article>`}function D(){return`
    <div class="pad-layers" aria-label="Tres capas de medida">
      <figure class="pad-layers__item">
        <div class="pad-layers__screen">
          <span class="pad-layers__band pad-layers__band--pad">${s(16,"pad",!0)}</span>
          <span class="pad-layers__fill">Body</span>
          <span class="pad-layers__band pad-layers__band--pad"></span>
        </div>
        <figcaption>Pantalla · <code>screenHorizontal</code> 16</figcaption>
      </figure>
      <figure class="pad-layers__item">
        <div class="pad-layers__gap">
          <span class="pad-layers__box"></span>
          <span class="live-slot live-slot--h" style="--slot:8px">${s(8,"gap",!0)}</span>
          <span class="pad-layers__box pad-layers__box--label">Label</span>
        </div>
        <figcaption>Hueco · <code>componentSm</code> 8</figcaption>
      </figure>
      <figure class="pad-layers__item">
        ${i(`<button type="button" class="mot-btn mot-btn--filled spec-ghost" tabindex="-1">${r}Abrir cuaderno</button>`,24,8)}
        <figcaption>Dentro · <code>ButtonSmall</code> 24 × 8</figcaption>
      </figure>
    </div>
    <div class="pad-catalog">
      ${l({title:"MOTButton · 24 × 8",codes:"<code>MOTSpacing.ButtonSmall</code> · <code>mot/padding/button/small-*</code>",href:"components/button.html",label:"Button",inner:i(`<button type="button" class="mot-btn mot-btn--filled spec-ghost" tabindex="-1">${r}Abrir cuaderno</button>`,24,8)})}
      ${l({title:"MOTTonalButton · 12 × 6",codes:"<code>MOTSpacing.ButtonXSmall</code> · <code>mot/padding/button/xsmall-*</code>",href:"components/button.html",label:"Button",inner:i(`<button type="button" class="mot-btn mot-btn--tonal mot-btn--xs spec-ghost" tabindex="-1">${r}Siguiente</button>`,12,6)})}
      ${l({title:"Filter chip · 16 × 6",codes:"<code>MOTSpacing.Chip</code> · <code>mot/padding/chip/horizontal-lg</code> · <code>vertical</code>",href:"components/filter-chip.html",label:"Filter chip",inner:i('<button type="button" class="filter-chip filter-chip--selected spec-ghost" tabindex="-1">Activos</button>',16,6)})}
      ${l({title:"WalletDocumentRow · 16 × 12",codes:"<code>MOTSpacing.ListItem</code> · <code>mot/padding/list-item/*</code>",href:"components/list-item.html",label:"List item",inner:i(`<div class="mot-li spec-ghost"><span class="mot-li__lead">${M}</span><div class="mot-li__text"><strong>Vuelo Lisboa – Madrid</strong><span>Vuelo · 14 mar</span></div>${O}</div>`,16,12)})}
      ${l({title:"Outlined text field · 16",codes:"<code>MOTSpacing.TextField.outlinedVertical</code> · <code>mot/padding/text-field/outlined-vertical</code>",href:"components/text-field.html",label:"Text field",inner:i('<input class="mot-field__input pad-field spec-ghost" type="text" value="Barcelona 2026" readonly tabindex="-1" />',16,16)})}
      ${l({title:"Snackbar · 16 × 12",codes:"<code>MOTSpacing.Snackbar</code> · <code>mot/padding/snackbar/*</code>",href:"components/snackbar.html",label:"Snackbar",inner:i('<div class="snackbar-demo spec-ghost" style="margin:0"><span class="snackbar-demo__text">Gasto guardado</span></div>',16,12)})}
      ${l({title:"Dialog · 24",codes:"<code>MOTSpacing.Dialog.container</code> · <code>mot/padding/dialog/container</code>",href:"components/basic-dialog.html",label:"Basic dialog",inner:i(`<div class="dialog-demo spec-ghost" style="max-width:280px;margin:0">
            <p class="dialog-demo__title" style="margin-bottom:8px">¿Eliminar documento?</p>
            <p class="dialog-demo__body" style="margin:0">Se borrará del dispositivo.</p>
          </div>`,24,24)})}
    </div>`}const d=document.getElementById("foundation-main");var f;const g=(f=d==null?void 0:d.querySelector("#escala"))==null?void 0:f.closest(".section");var y;const b=(y=d==null?void 0:d.querySelector("#pantalla"))==null?void 0:y.closest(".section");var S;const p=(S=d==null?void 0:d.querySelector("#componente"))==null?void 0:S.closest(".section");var $;const v=($=d==null?void 0:d.querySelector("#padding"))==null?void 0:$.closest(".section");g==null||g.insertAdjacentHTML("beforeend",j());var x;(x=b==null?void 0:b.querySelector(".section-lead"))==null||x.insertAdjacentHTML("afterend",P());var T;(T=p==null?void 0:p.querySelector(".section-lead"))==null||T.insertAdjacentHTML("afterend",R());const _=p==null?void 0:p.querySelector(".live-kit");_&&U(_);const K=v?[...v.querySelectorAll(":scope > p")]:[];var k;(k=K.at(-1)||v)==null||k.insertAdjacentHTML("afterend",D());
