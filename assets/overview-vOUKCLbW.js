import{i as n,c as t,s as o}from"./bootstrap-BPzZgXdE.js";n("overview");const s=[{href:"/color.html",title:"Color",desc:"Roles M3, seeds MTB, extensiones"},{href:"/typography.html",title:"Typography",desc:"Fraunces + Inter"}];document.getElementById("foundations-grid").innerHTML=s.map(e=>`
    <a class="component-link" href="${e.href}">
      <h3>${e.title}</h3>
      <p>${e.desc}</p>
    </a>`).join("");document.getElementById("components-grid").innerHTML=t.components.map(e=>`
    <a class="component-link" href="${e.href}" style="margin-bottom:12px">
      ${o(e.status)} <span style="margin-left:8px;font-size:0.75rem;color:var(--on-surface-variant)">${e.priority}</span>
      <h3>${e.name}</h3>
      <p>${e.summary}</p>
    </a>`).join("");
