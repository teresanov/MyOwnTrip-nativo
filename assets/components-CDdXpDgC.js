import{i as s,c as t,s as a}from"./bootstrap-BPzZgXdE.js";s("components");document.getElementById("components-list").innerHTML=t.components.map(n=>`
    <a class="component-link" href="${n.href}" style="display:block;margin-bottom:16px">
      ${a(n.status)}
      <span style="margin-left:8px;font-size:0.75rem;color:var(--on-surface-variant)">${n.priority}</span>
      <h3>${n.name}</h3>
      <p>${n.summary}</p>
    </a>`).join("");
