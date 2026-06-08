import{i as a,c as n,a as t,s as e}from"./bootstrap-DsljI-O4.js";a("components");document.getElementById("components-list").innerHTML=n.components.map(s=>`
    <a class="component-link" href="${t(s.href)}" style="display:block;margin-bottom:16px">
      ${e(s.status)}
      <span style="margin-left:8px;font-size:0.75rem;color:var(--on-surface-variant)">${s.priority}</span>
      <h3>${s.name}</h3>
      <p>${s.summary}</p>
    </a>`).join("");
