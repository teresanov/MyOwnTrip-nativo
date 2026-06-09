(function(){const r=document.createElement("link").relList;if(r&&r.supports&&r.supports("modulepreload"))return;for(const e of document.querySelectorAll('link[rel="modulepreload"]'))s(e);new MutationObserver(e=>{for(const n of e)if(n.type==="childList")for(const i of n.addedNodes)i.tagName==="LINK"&&i.rel==="modulepreload"&&s(i)}).observe(document,{childList:!0,subtree:!0});function o(e){const n={};return e.integrity&&(n.integrity=e.integrity),e.referrerPolicy&&(n.referrerPolicy=e.referrerPolicy),e.crossOrigin==="use-credentials"?n.credentials="include":e.crossOrigin==="anonymous"?n.credentials="omit":n.credentials="same-origin",n}function s(e){if(e.ep)return;e.ep=!0;const n=o(e);fetch(e.href,n)}})();const b=[{id:"button",name:"Button",status:"in-review",priority:"P0",href:"components/button.html",summary:"Acciones primarias y secundarias. Material 3 directo en Compose."},{id:"text-field",name:"Text field",status:"draft",priority:"P0",href:"components.html",summary:"OutlinedTextField en formularios viaje, wallet y gastos."},{id:"filter-chip",name:"Filter chip",status:"draft",priority:"P0",href:"components.html",summary:"Categorías de gasto y filtros rápidos."},{id:"icon-button",name:"Icon button",status:"draft",priority:"P1",href:"components.html",summary:"Acciones compactas en top app bar y formularios."},{id:"list-item",name:"List item",status:"draft",priority:"P1",href:"components.html",summary:"Filas en listas de viajes, wallet y restaurantes."},{id:"card",name:"Card",status:"draft",priority:"P1",href:"components.html",summary:"Contenedores de viaje y entradas. Restaurar set en Figma desde backup."}],v={components:b};function c(t){const r=t.replace(/^\//,"");return new URL(r,document.baseURI).pathname}const l=[{id:"overview",href:"index.html",label:"Overview"},{id:"color",href:"color.html",label:"Color",group:"foundations"},{id:"typography",href:"typography.html",label:"Typography",group:"foundations"},{id:"components",href:"components.html",label:"Components"}];function _(t,{componentId:r=null}={}){const o=document.getElementById("site-nav");if(!o)return;const s=l.find(a=>a.id==="overview"),e=l.filter(a=>a.group==="foundations"),n=l.find(a=>a.id==="components"),i=e.map(a=>u(a,t)).join(""),h=v.components.map(a=>{const d=r===a.id,y=d?" is-active":"",g=d?' aria-current="page"':"";return`<li><a href="${c(a.href)}" class="${y.trim()}"${g}>${a.name}</a></li>`}).join(""),m=t==="components"||r;o.innerHTML=`
    <div class="rail__brand">
      <a href="${c("index.html")}" class="rail__brand-link">
        <span class="rail__brand-title">MyOwnTrip</span>
        <span class="rail__brand-sub">Design System</span>
      </a>
    </div>
    <ul class="rail__list">
      ${u(s,t)}
      <li class="rail__group">
        <span class="rail__group-label">Foundations</span>
        <ul class="rail__sublist">${i}</ul>
      </li>
      <li class="rail__group">
        <a href="${c(n.href)}" class="rail__group-link${m?" is-active":""}"${t==="components"&&!r?' aria-current="page"':""}>Components</a>
        ${m?`<ul class="rail__sublist">${h}</ul>`:""}
      </li>
    </ul>
    <div class="rail__footer">
      <button type="button" class="btn btn--outlined" id="theme-toggle">Cambiar tema</button>
      <p class="rail__meta">Showcase estático · M3 nativo</p>
    </div>
  `}function u(t,r){const o=t.id===r,s=o?" is-active":"",e=o?' aria-current="page"':"";return`<li><a href="${c(t.href)}" class="${s.trim()}"${e}>${t.label}</a></li>`}const p="mot-showcase-theme";function $(){const t=localStorage.getItem(p),r=window.matchMedia("(prefers-color-scheme: dark)").matches,o=t||(r?"dark":"light");f(o);const s=document.getElementById("theme-toggle");s&&(s.setAttribute("aria-pressed",o==="dark"?"true":"false"),s.textContent=o==="dark"?"Tema claro":"Tema oscuro",s.addEventListener("click",()=>{const e=document.documentElement.dataset.theme==="dark"?"light":"dark";f(e),localStorage.setItem(p,e),s.setAttribute("aria-pressed",e==="dark"?"true":"false"),s.textContent=e==="dark"?"Tema claro":"Tema oscuro"}))}function f(t){document.documentElement.dataset.theme=t}function w(t,r={}){_(t,r),$()}function C(t){return`<span class="chip ${{draft:"chip--draft","in-review":"chip--review",ready:"chip--ready",deprecated:"chip--deprecated"}[t]||"chip--draft"}">${t}</span>`}export{c as a,v as c,w as i,C as s};
