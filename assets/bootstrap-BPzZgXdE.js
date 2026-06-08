(function(){const o=document.createElement("link").relList;if(o&&o.supports&&o.supports("modulepreload"))return;for(const e of document.querySelectorAll('link[rel="modulepreload"]'))n(e);new MutationObserver(e=>{for(const r of e)if(r.type==="childList")for(const i of r.addedNodes)i.tagName==="LINK"&&i.rel==="modulepreload"&&n(i)}).observe(document,{childList:!0,subtree:!0});function s(e){const r={};return e.integrity&&(r.integrity=e.integrity),e.referrerPolicy&&(r.referrerPolicy=e.referrerPolicy),e.crossOrigin==="use-credentials"?r.credentials="include":e.crossOrigin==="anonymous"?r.credentials="omit":r.credentials="same-origin",r}function n(e){if(e.ep)return;e.ep=!0;const r=s(e);fetch(e.href,r)}})();const y=[{id:"button",name:"Button",status:"draft",priority:"P0",href:"/components/button.html",summary:"Acciones primarias y secundarias. Material 3 directo en Compose."}],_={components:y},c=[{id:"overview",href:"/index.html",label:"Overview"},{id:"color",href:"/color.html",label:"Color",group:"foundations"},{id:"typography",href:"/typography.html",label:"Typography",group:"foundations"},{id:"components",href:"/components.html",label:"Components"}];function b(t,{componentId:o=null}={}){const s=document.getElementById("site-nav");if(!s)return;const n=c.find(a=>a.id==="overview"),e=c.filter(a=>a.group==="foundations"),r=c.find(a=>a.id==="components"),i=e.map(a=>d(a,t)).join(""),f=_.components.map(a=>{const u=o===a.id,h=u?" is-active":"",g=u?' aria-current="page"':"";return`<li><a href="${a.href}" class="${h.trim()}"${g}>${a.name}</a></li>`}).join(""),l=t==="components"||o;s.innerHTML=`
    <div class="rail__brand">
      <a href="/index.html" class="rail__brand-link">
        <span class="rail__brand-title">MyOwnTrip</span>
        <span class="rail__brand-sub">Design System</span>
      </a>
    </div>
    <ul class="rail__list">
      ${d(n,t)}
      <li class="rail__group">
        <span class="rail__group-label">Foundations</span>
        <ul class="rail__sublist">${i}</ul>
      </li>
      <li class="rail__group">
        <a href="${r.href}" class="rail__group-link${l?" is-active":""}"${t==="components"&&!o?' aria-current="page"':""}>Components</a>
        ${l?`<ul class="rail__sublist">${f}</ul>`:""}
      </li>
    </ul>
    <div class="rail__footer">
      <button type="button" class="btn btn--outlined" id="theme-toggle">Cambiar tema</button>
      <p class="rail__meta">Showcase estático · M3 nativo</p>
    </div>
  `}function d(t,o){const s=t.id===o,n=s?" is-active":"",e=s?' aria-current="page"':"";return`<li><a href="${t.href}" class="${n.trim()}"${e}>${t.label}</a></li>`}const m="mot-showcase-theme";function v(){const t=localStorage.getItem(m),o=window.matchMedia("(prefers-color-scheme: dark)").matches,s=t||(o?"dark":"light");p(s);const n=document.getElementById("theme-toggle");n&&(n.setAttribute("aria-pressed",s==="dark"?"true":"false"),n.textContent=s==="dark"?"Tema claro":"Tema oscuro",n.addEventListener("click",()=>{const e=document.documentElement.dataset.theme==="dark"?"light":"dark";p(e),localStorage.setItem(m,e),n.setAttribute("aria-pressed",e==="dark"?"true":"false"),n.textContent=e==="dark"?"Tema claro":"Tema oscuro"}))}function p(t){document.documentElement.dataset.theme=t}function $(t,o={}){b(t,o),v()}function w(t){return`<span class="chip ${{draft:"chip--draft","in-review":"chip--review",ready:"chip--ready",deprecated:"chip--deprecated"}[t]||"chip--draft"}">${t}</span>`}export{_ as c,$ as i,w as s};
