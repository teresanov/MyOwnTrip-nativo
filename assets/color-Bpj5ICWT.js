import{i as n}from"./bootstrap-BJUcQClb.js";import{t as e}from"./tokens-CH2VayQ6.js";n("color");function i(t,o){return`
    <div class="swatch">
      <div class="swatch__color" style="background-color:${o}" aria-hidden="true"></div>
      <div class="swatch__meta"><strong>${t}</strong><br><code>${o}</code></div>
    </div>`}function s(t){return`<div class="card-grid">${Object.entries(t).map(([o,r])=>i(o,r)).join("")}</div>`}function a(t){return Object.entries(t).map(([o,r])=>`
      <div class="card" style="margin-bottom:12px">
        <h4 style="margin:0 0 8px">${o}</h4>
        <div class="card-grid" style="grid-template-columns:repeat(auto-fill,minmax(140px,1fr))">
          ${i("light",r.light)}
          ${i("dark",r.dark)}
        </div>
      </div>`).join("")}const d=["primary","onPrimary","tertiary","onTertiary","error","onError","surface","onSurface"];document.getElementById("intro-section").innerHTML=`
  <p>Roles desde <code>${e.source}</code> · colección M3 · modos Light/Dark.</p>
  <div class="callout">
    <strong>Vista rápida Light:</strong>
    <div class="card-grid" style="margin-top:12px;grid-template-columns:repeat(auto-fill,minmax(120px,1fr))">
      ${d.map(t=>i(t,e.light[t])).join("")}
    </div>
  </div>
`;document.getElementById("roles-section").innerHTML=`
  <h2>Roles Light</h2>
  ${s(e.light)}
  <h2 style="margin-top:32px">Roles Dark</h2>
  ${s(e.dark)}
`;document.getElementById("extensions-section").innerHTML=`
  <h2>Extensiones (no M3)</h2>
  ${a(e.extensions)}
`;const c=Object.entries(e.stateLayers).filter(([t])=>t!=="note").map(([t,o])=>`<tr><td>${t}</td><td>${o}</td></tr>`).join("");document.getElementById("state-layers-section").innerHTML=`
  <h2>State layers (runtime)</h2>
  <p>No son tokens de color. Aplicados sobre <code>on*</code> por componentes M3 en Compose.</p>
  <table>
    <thead><tr><th>Estado</th><th>Opacidad</th></tr></thead>
    <tbody>${c}</tbody>
  </table>
  <p class="callout" style="margin-top:16px">${e.stateLayers.note}</p>
  <p><strong>Regla:</strong> <code>tertiary</code> (${e.light.tertiary}) ≠ <code>error</code> (${e.light.error}).</p>
`;
