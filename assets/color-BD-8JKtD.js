import{i}from"./bootstrap-DsljI-O4.js";import{t as o}from"./tokens-BB720Taq.js";i("color");function s(t,e){return`
    <div class="swatch">
      <div class="swatch__color" style="background:${e}"></div>
      <div class="swatch__meta"><strong>${t}</strong><br><code>${e}</code></div>
    </div>`}function n(t){return`<div class="card-grid">${Object.entries(t).map(([e,r])=>s(e,r)).join("")}</div>`}function a(t){return Object.entries(t).map(([e,r])=>`
      <div class="card" style="margin-bottom:12px">
        <h4 style="margin:0 0 8px">${e}</h4>
        <div class="card-grid" style="grid-template-columns:repeat(auto-fill,minmax(140px,1fr))">
          ${s("light",r.light)}
          ${s("dark",r.dark)}
        </div>
      </div>`).join("")}document.getElementById("seeds-section").innerHTML=`
  <h2>Fuente canónica</h2>
  <p>Roles desde <code>${o.source}</code> · colección M3 · modos Light/Dark.</p>
  <h3>Extensiones (no M3)</h3>
  ${a(o.extensions)}
`;document.getElementById("roles-section").innerHTML=`
  <h2>Roles Light</h2>
  ${n(o.light)}
  <h2 style="margin-top:32px">Roles Dark</h2>
  ${n(o.dark)}
`;const d=Object.entries(o.stateLayers).filter(([t])=>t!=="note").map(([t,e])=>`<tr><td>${t}</td><td>${e}</td></tr>`).join("");document.getElementById("state-layers-section").innerHTML=`
  <h2>State layers (runtime)</h2>
  <p>No son tokens de color. Aplicados sobre <code>on*</code> por componentes M3 en Compose.</p>
  <table>
    <thead><tr><th>Estado</th><th>Opacidad</th></tr></thead>
    <tbody>${d}</tbody>
  </table>
  <p class="callout" style="margin-top:16px">${o.stateLayers.note}</p>
  <p><strong>Regla:</strong> <code>tertiary</code> (${o.light.tertiary}) ≠ <code>error</code> (${o.light.error}).</p>
`;
