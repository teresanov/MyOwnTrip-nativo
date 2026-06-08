(function(){const n=document.createElement("link").relList;if(n&&n.supports&&n.supports("modulepreload"))return;for(const e of document.querySelectorAll('link[rel="modulepreload"]'))o(e);new MutationObserver(e=>{for(const r of e)if(r.type==="childList")for(const i of r.addedNodes)i.tagName==="LINK"&&i.rel==="modulepreload"&&o(i)}).observe(document,{childList:!0,subtree:!0});function s(e){const r={};return e.integrity&&(r.integrity=e.integrity),e.referrerPolicy&&(r.referrerPolicy=e.referrerPolicy),e.crossOrigin==="use-credentials"?r.credentials="include":e.crossOrigin==="anonymous"?r.credentials="omit":r.credentials="same-origin",r}function o(e){if(e.ep)return;e.ep=!0;const r=s(e);fetch(e.href,r)}})();const v=[{id:"button",name:"Button",status:"draft",priority:"P0",href:"components/button.html",summary:"Acciones primarias y secundarias. Material 3 directo en Compose."}],_={components:v};function c(t){return`/MyOwnTrip-nativo/${t.replace(/^\//,"")}`}const l=[{id:"overview",href:"index.html",label:"Overview"},{id:"color",href:"color.html",label:"Color",group:"foundations"},{id:"typography",href:"typography.html",label:"Typography",group:"foundations"},{id:"components",href:"components.html",label:"Components"}];function b(t,{componentId:n=null}={}){const s=document.getElementById("site-nav");if(!s)return;const o=l.find(a=>a.id==="overview"),e=l.filter(a=>a.group==="foundations"),r=l.find(a=>a.id==="components"),i=e.map(a=>p(a,t)).join(""),h=_.components.map(a=>{const d=n===a.id,g=d?" is-active":"",y=d?' aria-current="page"':"";return`<li><a href="${c(a.href)}" class="${g.trim()}"${y}>${a.name}</a></li>`}).join(""),u=t==="components"||n;s.innerHTML=`
    <div class="rail__brand">
      <a href="${c("index.html")}" class="rail__brand-link">
        <span class="rail__brand-title">MyOwnTrip</span>
        <span class="rail__brand-sub">Design System</span>
      </a>
    </div>
    <ul class="rail__list">
      ${p(o,t)}
      <li class="rail__group">
        <span class="rail__group-label">Foundations</span>
        <ul class="rail__sublist">${i}</ul>
      </li>
      <li class="rail__group">
        <a href="${c(r.href)}" class="rail__group-link${u?" is-active":""}"${t==="components"&&!n?' aria-current="page"':""}>Components</a>
        ${u?`<ul class="rail__sublist">${h}</ul>`:""}
      </li>
    </ul>
    <div class="rail__footer">
      <button type="button" class="btn btn--outlined" id="theme-toggle">Cambiar tema</button>
      <p class="rail__meta">Showcase estático · M3 nativo</p>
    </div>
  `}function p(t,n){const s=t.id===n,o=s?" is-active":"",e=s?' aria-current="page"':"";return`<li><a href="${c(t.href)}" class="${o.trim()}"${e}>${t.label}</a></li>`}const m="mot-showcase-theme";function $(){const t=localStorage.getItem(m),n=window.matchMedia("(prefers-color-scheme: dark)").matches,s=t||(n?"dark":"light");f(s);const o=document.getElementById("theme-toggle");o&&(o.setAttribute("aria-pressed",s==="dark"?"true":"false"),o.textContent=s==="dark"?"Tema claro":"Tema oscuro",o.addEventListener("click",()=>{const e=document.documentElement.dataset.theme==="dark"?"light":"dark";f(e),localStorage.setItem(m,e),o.setAttribute("aria-pressed",e==="dark"?"true":"false"),o.textContent=e==="dark"?"Tema claro":"Tema oscuro"}))}function f(t){document.documentElement.dataset.theme=t}function w(t,n={}){b(t,n),$()}function T(t){return`<span class="chip ${{draft:"chip--draft","in-review":"chip--review",ready:"chip--ready",deprecated:"chip--deprecated"}[t]||"chip--draft"}">${t}</span>`}export{c as a,_ as c,w as i,T as s};
