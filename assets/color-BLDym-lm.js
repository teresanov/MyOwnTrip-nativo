import{i as d}from"./bootstrap-BPzZgXdE.js";import{t as s}from"./tokens-_xhBrHdI.js";d("color");function o(e,t){return`
    <div class="swatch">
      <div class="swatch__color" style="background:${t}"></div>
      <div class="swatch__meta"><strong>${e}</strong><br><code>${t}</code></div>
    </div>`}document.getElementById("seeds-section").innerHTML=`
  <h2>Seeds MTB</h2>
  <div class="card-grid">
    ${Object.entries(s.seeds).map(([e,t])=>o(e,t)).join("")}
  </div>
  <h3>Extensiones</h3>
  <div class="card-grid">
    ${Object.entries(s.extensions).map(([e,t])=>o(e,t)).join("")}
  </div>
`;document.getElementById("roles-section").innerHTML=`
  <h2>Roles Light</h2>
  <div class="card-grid">
    ${Object.entries(s.light).map(([e,t])=>o(e,t)).join("")}
  </div>
`;document.getElementById("state-layers-section").innerHTML=`
  <h2>State layers (runtime)</h2>
  <p>No son tokens de color. Aplicados sobre <code>on*</code> por componentes M3.</p>
  <table>
    <thead><tr><th>Estado</th><th>Opacidad</th></tr></thead>
    <tbody>
      ${Object.entries(s.stateLayers).map(([e,t])=>`<tr><td>${e}</td><td>${t}</td></tr>`).join("")}
    </tbody>
  </table>
  <p style="margin-top:16px"><strong>Regla:</strong> tertiary (#D9382C) ≠ error (#B3261E).</p>
`;
