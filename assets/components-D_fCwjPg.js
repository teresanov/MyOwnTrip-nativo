import{i as c,c as e,s as i,a as r,b as p}from"./bootstrap-Cqc7PUA6.js";c("components");const m=n=>e.components.filter(o=>o.category===n),t=e.components.filter(n=>!i.categories.some(o=>o.id===n.category));function a(n){const o=n.href!=="components.html",s=`
    <span class="component-link__meta">${r(n.status)}<span class="component-link__priority">${n.priority}</span></span>
    <h3>${n.name}</h3>
    <p>${n.summary}</p>
    ${o?"":'<p class="component-link__note">Sin ficha todavía</p>'}
  `;return o?`<a class="component-link" href="${p(n.href)}">${s}</a>`:`<div class="component-link component-link--empty">${s}</div>`}const l=i.categories.map(n=>{const o=m(n.id);return o.length?`
      <section class="section">
        <h2 id="${n.id}">${n.label}</h2>
        <div class="component-grid">${o.map(a).join("")}</div>
      </section>`:""}).join("");document.getElementById("components-list").innerHTML=l+(t.length?`<section class="section">
         <h2 id="otros">Otros</h2>
         <div class="component-grid">${t.map(a).join("")}</div>
       </section>`:"");
