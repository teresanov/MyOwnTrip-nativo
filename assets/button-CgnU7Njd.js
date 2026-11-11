import{i as o,s as n}from"./bootstrap-DeDV_W93.js";import{m as t,d as s}from"./component-page-DipGpmBb.js";const i="Button",r="draft",l="https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=55141-14168",c="Ejecutar acciones: confirmar, guardar, continuar. En MyOwnTrip: crear viaje, guardar gasto, confirmar entrada Wallet (H7).",d=[{id:"filled",label:"Filled",usage:"Acción principal de la pantalla (una por vista)."},{id:"tonal",label:"Tonal",usage:"Acción secundaria con énfasis moderado (`secondaryContainer`)."},{id:"outlined",label:"Outlined",usage:"Acciones alternativas o cancelar."},{id:"text",label:"Text",usage:"Acciones terciarias, bajo peso visual."}],p=["primary","onPrimary","secondaryContainer","onSecondaryContainer","outline"],u={links:[{label:"Material Design 3 · All buttons",url:"https://m3.material.io/components/all-buttons"},{label:"Material Web · Buttons",url:"https://material-web.dev/components/button/"}],consulted:"2026-11-10"},m=["Una sola acción de alto énfasis (Filled) por pantalla: la acción principal.","Usa Filled en solitario cuando hay una única acción importante.","Combina niveles de énfasis para marcar la jerarquía: Filled junto a Text, Outlined junto a Filled, Text junto a Outlined.","Reserva Filled para acciones finales que completan un flujo (Guardar, Confirmar, Hecho).","Usa Tonal para acciones de apoyo que necesitan algo más de énfasis que un Outlined, como «Siguiente» en un onboarding.","Usa Outlined para acciones importantes que no son la principal, y para dar la opción de cambiar de opinión o salir del flujo.","Usa Text para las acciones de menor prioridad, sobre todo cuando se presentan varias opciones."],b=["No coloques un botón debajo de otro si hay espacio para ponerlos uno al lado del otro.","No uses Elevated salvo que el botón necesite separarse visualmente de un fondo con patrón: evita acumular sombras."],h={dos:["Un solo botón Filled como CTA principal por pantalla.","Etiquetas con verbos en Inter (`labelLarge`).","Estados interactivos vía state layers M3 (no colores custom por estado)."],donts:["No usar `tertiary` para acciones destructivas — usar `error` + confirmación.","No crear tokens `button/hover` o similares.","No Fraunces en el label del botón."]},g=["Touch target mínimo 48dp.","Foco visible: anillo 2dp separado del contenido.","Estado disabled: opacidad 38% / 12%, no color distinto."],y=`Button(
    onClick = { onSave() },
    modifier = Modifier.fillMaxWidth(),
) {
    Text("Guardar")
}`,f={source:"Figma node 55141-14168 · Compose `Button` (M3)",why:"Acciones de confirmar, guardar y continuar con M3 directo; la jerarquía la marcan las variantes.",layer:"Componente M3 directo"},v=[{date:"2026-06-08",note:"Ficha inicial en showcase estático. Compose: M3 directo."}],a={name:i,status:r,figma:l,purpose:c,variants:d,tokens:p,guidelines:u,dos:m,donts:b,productRules:h,a11y:g,compose:y,decisions:f,changelog:v};o("components",{componentId:"button"});t(a);document.getElementById("component-header").innerHTML=`
  <p class="page-header__eyebrow">Component · ${a.priority||"P0"}</p>
  <h1>${a.name} ${n(a.status)}</h1>
  <p class="page-header__lead">${a.purpose}</p>
  <p><a href="${a.figma}" target="_blank" rel="noopener">Component set en Figma ↗</a></p>
`;const $={filled:"btn--filled",tonal:"btn--tonal",outlined:"btn--outlined",text:"btn--text"};document.getElementById("component-main").innerHTML=`
  <section class="section">
    <h2>Variantes</h2>
    <div class="btn-row">
      ${a.variants.map(e=>`<button type="button" class="btn ${$[e.id]}">${e.label}</button>`).join("")}
    </div>
    <table style="margin-top:24px">
      <thead><tr><th>Variante</th><th>Uso</th></tr></thead>
      <tbody>
        ${a.variants.map(e=>`<tr><td>${e.label}</td><td>${e.usage}</td></tr>`).join("")}
      </tbody>
    </table>
  </section>

  <section class="section">
    <h2>Tokens M3</h2>
    <p><code>${a.tokens.join("</code>, <code>")}</code></p>
  </section>

  ${s(a)}

  <section class="section">
    <h2>Accesibilidad</h2>
    <ul class="list-dos">${a.a11y.map(e=>`<li>${e}</li>`).join("")}</ul>
  </section>

  <section class="section">
    <h2>Compose</h2>
    <pre><code>${j(a.compose)}</code></pre>
  </section>

  <section class="section">
    <h2>Changelog</h2>
    <table>
      <thead><tr><th>Fecha</th><th>Nota</th></tr></thead>
      <tbody>
        ${a.changelog.map(e=>`<tr><td>${e.date}</td><td>${e.note}</td></tr>`).join("")}
      </tbody>
    </table>
  </section>
`;function j(e){return e.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;")}
