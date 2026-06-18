import{i as t,a as n,c as o,s}from"./bootstrap-BJUcQClb.js";t("overview");const a=[{href:"color.html",title:"Color",desc:"Roles M3 desde variables.json"},{href:"typography.html",title:"Typography",desc:"Fraunces + Inter"}];document.getElementById("foundations-grid").innerHTML=a.map(e=>`
    <a class="component-link" href="${n(e.href)}">
      <h3>${e.title}</h3>
      <p>${e.desc}</p>
    </a>`).join("");document.getElementById("components-grid").innerHTML=o.components.map(e=>`
    <a class="component-link" href="${n(e.href)}" style="margin-bottom:12px">
      ${s(e.status)} <span style="margin-left:8px;font-size:0.75rem;color:var(--on-surface-variant)">${e.priority}</span>
      <h3>${e.name}</h3>
      <p>${e.summary}</p>
    </a>`).join("");
