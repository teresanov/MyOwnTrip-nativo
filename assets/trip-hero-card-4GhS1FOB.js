import{i as t,s as o}from"./bootstrap-BSec5oP0.js";const n="TripHeroCard",i="ready",s="P1",l="https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61199-7862",d="Portada del viaje destacado en Home: Background con image fill + scrim editorial, Eyebrow label de fase, countdown opcional, título y meta; CTA tonal «Ver detalles» debajo de la portada (única acción). Patrón MyOwnTrip — no es variante de Stacked card.",r=["Background — Card states Elevated u Outlined (280dp alto) con image fill full-bleed","Scrim — degradado vertical sobre la portada","Eyebrow label — instancia Color=Tertiary, Size=Medium («Próximo viaje», «En destino», «Recuerdo») — no interactiva","Content — Countdown (opcional) + Title (Fraunces) + Meta (Inter)","Button - tonal — XSmall hug contents bajo la portada: «Ver detalles»"],c=[{id:"elevated",label:"Elevated",usage:"Default en Home — viaje destacado."},{id:"outlined",label:"Outlined",usage:"Misma anatomía con marco Outlined."}],p=[{name:"Countdown text",type:"TEXT",example:"Sale en 3 días"},{name:"Show countdown",type:"BOOLEAN",example:"true si próximo; false en curso o pasado"},{name:"Title text",type:"TEXT",example:"Barcelona fin de semana"},{name:"Meta text",type:"TEXT",example:"4 jul 2026 – 6 jul 2026 · 3 días"}],m=["ElevatedCard / OutlinedCard","scrim","tertiaryFixedDim + onTertiaryContainer (Eyebrow label)","tertiaryFixedDim (countdown)","onPrimary (título y meta sobre scrim)","secondaryContainer (Button tonal)","headlineSmall-emphasized / titleMedium / bodyMedium","shapes.medium (12dp)"],h=["Una TripHeroCard por Home (viaje destacado).","Image fill en Background — se adapta al ancho del card con padding de pantalla.","Eyebrow label Color=Tertiary según fase del viaje.","CTA «Ver detalles» como única acción interactiva.","Instanciar desde librería DS publicada."],u=["No usar Stacked · Media only para este patrón.","No Assist chip ni Filter chip en la portada.","No hacer clickable la portada ni el eyebrow.","No duplicar el saludo/displaySmall del hero de página.","No avatar ni menú en la portada."],y=["Único control interactivo: CTA «Ver detalles» (48dp).","Eyebrow y textos de portada: incluir fase y countdown en contentDescription del bloque o del CTA según implementación.","stateDescription según fase (próximo / en curso / pasado).","Imagen decorativa — sin alt aparte.","Show countdown=false oculta la línea de countdown."],b="com.myowntrip.app.ui.components.TripHeroCard(trip, today, onClick)",g=[{date:"2026-06-17",note:"Component set TripHeroCard en librería Cards."},{date:"2026-06-17",note:"Eyebrow label (no chip); image fill en Background; CTA única acción."}],a={name:n,status:i,priority:s,figma:l,purpose:d,anatomy:r,variants:c,properties:p,tokens:m,dos:h,donts:u,a11y:y,compose:b,changelog:g};t("components",{componentId:"trip-hero-card"});document.getElementById("component-header").innerHTML=`
  <p class="page-header__eyebrow">Component · Cards · ${a.priority}</p>
  <h1>${a.name} ${o(a.status)}</h1>
  <p class="page-header__lead">${a.purpose}</p>
  <p><a href="${a.figma}" target="_blank" rel="noopener">Sección Cards en Figma ↗</a></p>
`;document.getElementById("component-main").innerHTML=`
  <section class="section">
    <h2>Anatomía</h2>
    <ol class="list-dos">
      ${a.anatomy.map(e=>`<li>${e}</li>`).join("")}
    </ol>
  </section>

  <section class="section">
    <h2>Variantes (Style)</h2>
    <table>
      <thead><tr><th>Variante</th><th>Uso</th></tr></thead>
      <tbody>
        ${a.variants.map(e=>`<tr><td>${e.label}</td><td>${e.usage}</td></tr>`).join("")}
      </tbody>
    </table>
  </section>

  <section class="section">
    <h2>Properties (instancia)</h2>
    <table>
      <thead><tr><th>Property</th><th>Tipo</th><th>Ejemplo</th></tr></thead>
      <tbody>
        ${a.properties.map(e=>`<tr><td><code>${e.name}</code></td><td>${e.type}</td><td>${e.example}</td></tr>`).join("")}
      </tbody>
    </table>
  </section>

  <section class="section">
    <h2>Tokens M3</h2>
    <p><code>${a.tokens.join("</code>, <code>")}</code></p>
  </section>

  <section class="section">
    <h2>Do</h2>
    <ul class="list-dos">${a.dos.map(e=>`<li>${e}</li>`).join("")}</ul>
  </section>

  <section class="section">
    <h2>Don't</h2>
    <ul class="list-dos list-dos--dont">${a.donts.map(e=>`<li>${e}</li>`).join("")}</ul>
  </section>

  <section class="section">
    <h2>Accesibilidad</h2>
    <ul class="list-dos">${a.a11y.map(e=>`<li>${e}</li>`).join("")}</ul>
  </section>

  <section class="section">
    <h2>Compose</h2>
    <pre><code>${C(a.compose)}</code></pre>
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
`;function C(e){return e.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;")}
