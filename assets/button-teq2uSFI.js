import{i as o,s as a}from"./bootstrap-BSec5oP0.js";const n="Button",s="draft",i="https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=55141-14168",c="Ejecutar acciones: confirmar, guardar, continuar. En MyOwnTrip: crear viaje, guardar gasto, confirmar entrada Wallet (H7).",l=[{id:"filled",label:"Filled",usage:"Acción principal de la pantalla (una por vista)."},{id:"tonal",label:"Tonal",usage:"Acción secundaria con énfasis moderado (`secondaryContainer`)."},{id:"outlined",label:"Outlined",usage:"Acciones alternativas o cancelar."},{id:"text",label:"Text",usage:"Acciones terciarias, bajo peso visual."}],r=["primary","onPrimary","secondaryContainer","onSecondaryContainer","outline"],d=["Un solo botón Filled como CTA principal por pantalla.","Etiquetas con verbos en Inter (`labelLarge`).","Estados interactivos vía state layers M3 (no colores custom por estado)."],p=["No usar `tertiary` para acciones destructivas — usar `error` + confirmación.","No crear tokens `button/hover` o similares.","No Fraunces en el label del botón."],m=["Touch target mínimo 48dp.","Foco visible: anillo 2dp separado del contenido.","Estado disabled: opacidad 38% / 12%, no color distinto."],u=`Button(
    onClick = { onSave() },
    modifier = Modifier.fillMaxWidth(),
) {
    Text("Guardar")
}`,h=[{date:"2026-06-08",note:"Ficha inicial en showcase estático. Compose: M3 directo."}],e={name:n,status:s,figma:i,purpose:c,variants:l,tokens:r,dos:d,donts:p,a11y:m,compose:u,changelog:h};o("components",{componentId:"button"});document.getElementById("component-header").innerHTML=`
  <p class="page-header__eyebrow">Component · ${e.priority||"P0"}</p>
  <h1>${e.name} ${a(e.status)}</h1>
  <p class="page-header__lead">${e.purpose}</p>
  <p><a href="${e.figma}" target="_blank" rel="noopener">Component set en Figma ↗</a></p>
`;const b={filled:"btn--filled",tonal:"btn--tonal",outlined:"btn--outlined",text:"btn--text"};document.getElementById("component-main").innerHTML=`
  <section class="section">
    <h2>Variantes</h2>
    <div class="btn-row">
      ${e.variants.map(t=>`<button type="button" class="btn ${b[t.id]}">${t.label}</button>`).join("")}
    </div>
    <table style="margin-top:24px">
      <thead><tr><th>Variante</th><th>Uso</th></tr></thead>
      <tbody>
        ${e.variants.map(t=>`<tr><td>${t.label}</td><td>${t.usage}</td></tr>`).join("")}
      </tbody>
    </table>
  </section>

  <section class="section">
    <h2>Tokens M3</h2>
    <p><code>${e.tokens.join("</code>, <code>")}</code></p>
  </section>

  <section class="section">
    <h2>Do</h2>
    <ul class="list-dos">${e.dos.map(t=>`<li>${t}</li>`).join("")}</ul>
  </section>

  <section class="section">
    <h2>Don't</h2>
    <ul class="list-dos list-dos--dont">${e.donts.map(t=>`<li>${t}</li>`).join("")}</ul>
  </section>

  <section class="section">
    <h2>Accesibilidad</h2>
    <ul class="list-dos">${e.a11y.map(t=>`<li>${t}</li>`).join("")}</ul>
  </section>

  <section class="section">
    <h2>Compose</h2>
    <pre><code>${g(e.compose)}</code></pre>
  </section>

  <section class="section">
    <h2>Changelog</h2>
    <table>
      <thead><tr><th>Fecha</th><th>Nota</th></tr></thead>
      <tbody>
        ${e.changelog.map(t=>`<tr><td>${t.date}</td><td>${t.note}</td></tr>`).join("")}
      </tbody>
    </table>
  </section>
`;function g(t){return t.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;")}
