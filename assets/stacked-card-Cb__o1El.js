import{i as a,s as o}from"./bootstrap-DeDV_W93.js";import{m as d,d as s}from"./component-page-DipGpmBb.js";const n="Stacked card",i="ready",c="P1",r="https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=52346-27573",l="Card vertical editorial: Outlined en listas/feeds, Elevated en destacados. Layouts Media & text (avatar + media + copy) y Text only (header + supporting + dismiss). Filled está CUT.",m=["Surface — OutlinedCard o ElevatedCard, shapes.medium (12dp)","Header row — Header text + Subhead text (avatar oculto en Home vacío Figma)","Dismiss — IconButton close si Show secondary action","Media — imagen full-bleed bajo header (Media & text)","Supporting text — cuerpo en Text only"],p=[{id:"outlined-media-text",label:"Outlined · Media & text",usage:"Home vacío, cards editoriales en feed."},{id:"elevated-text-only",label:"Elevated · Text only",usage:"Avisos descartables o mensajes destacados (si el producto los necesita)."},{id:"outlined-text-only",label:"Outlined · Text only",usage:"Mensajes inline en listas (filtro vacío)."}],u=["surfaceContainerLow + onSurface / onSurfaceVariant (Elevated · Text only)","onSurface / onSurfaceVariant (Outlined · Media & text)","surfaceContainerHigh (avatar)","shapes.medium"],h={label:"Material Design 3 · Cards",url:"https://m3.material.io/components/cards/guidelines",consulted:"2026-11-10"},g=["Usa una card para mostrar contenido y acciones sobre un único tema.","Facilita el escaneo: la jerarquía de texto e imágenes debe ser clara (titular, subtítulo, texto de apoyo).","Elige la variante (Elevated, Filled u Outlined) por estilo: ofrecen la misma legibilidad y funcionalidad; Outlined aporta más énfasis y Filled menos.","Muestra las cards en colecciones (grid, lista vertical o carrusel); en una colección comparten la misma elevación en reposo.","Coloca los filtros y el orden fuera de la colección de cards; el filtro debe aplicarse a todas.","Si pones texto o iconos sobre una imagen, asegura el contraste con un scrim translúcido o una forma contenedora."],y=["No fuerces contenido en cards cuando el espaciado, los titulares o los divisores darían una jerarquía más simple.","Evita poner texto o iconos sobre imágenes si no es necesario."],b={dos:["Outlined en feeds; Elevated en promos destacadas.","Mapear properties Figma 1:1 a MotStackedCard.","Home vacío: HomeEmptyStackedCard (instancia documentada)."],donts:["No Filled (CUT).","No OutlinedCard ad hoc en pantallas si encaja un layout de este set.","No icono decorativo arbitrario en Text only — copy + dismiss si aplica."]},v=`MotStackedCard(style, headerText, subheadText, …)
HomeEmptyStackedCard()`,f={source:"Figma node 52346-27573 · Compose `MotStackedCard` (MOT)",why:"Card editorial: Outlined en listas y feeds, Elevated en destacados.",layer:"Componente MOT (`MotStackedCard`)"},C=[{date:"2026-06-19",note:"Wallet promo retirada de Home; Elevated · Text only queda como layout genérico vía MotStackedCard."},{date:"2026-06-17",note:"Binding Figma → MotStackedCard; fichas Home cap 1 y Wallet."}],e={name:n,status:i,priority:c,figma:r,purpose:l,anatomy:m,variants:p,tokens:u,guidelines:h,dos:g,donts:y,productRules:b,compose:v,decisions:f,changelog:C};a("components",{componentId:"stacked-card"});d(e);document.getElementById("component-header").innerHTML=`
  <p class="page-header__eyebrow">Component · Cards · ${e.priority}</p>
  <h1>${e.name} ${o(e.status)}</h1>
  <p class="page-header__lead">${e.purpose}</p>
  <p><a href="${e.figma}" target="_blank" rel="noopener">Stacked card en Figma ↗</a> · <a href="../../docs/design-system/figma-compose-bindings.md">Bindings Figma ↔ Compose</a></p>
`;document.getElementById("component-main").innerHTML=`
  <section class="section">
    <h2>Home vacío · Media & text (205:816)</h2>
    <figure class="card-demo card-demo--stacked">
      <div class="card-demo__header">
        <div>
          <strong>Sin viajes todavía</strong>
          <p>Planea tu primera aventura</p>
        </div>
      </div>
      <img src="/assets/home_empty_map.jpg" alt="Mapa con chincheta de viaje" width="360" height="200" loading="lazy" />
    </figure>
    <p class="lead">Imagen bundled en app: <code>res/drawable-nodpi/home_empty_map.jpg</code> → <code>imageRes</code> (sin red).</p>
  </section>

  <section class="section">
    <h2>Anatomía</h2>
    <ol class="list-dos">
      ${e.anatomy.map(t=>`<li>${t}</li>`).join("")}
    </ol>
  </section>

  <section class="section">
    <h2>Variantes</h2>
    <table>
      <thead><tr><th>Variante</th><th>Uso</th></tr></thead>
      <tbody>
        ${e.variants.map(t=>`<tr><td>${t.label}</td><td>${t.usage}</td></tr>`).join("")}
      </tbody>
    </table>
  </section>

  <section class="section">
    <h2>Properties → Compose</h2>
    <table>
      <thead><tr><th>Property Figma</th><th>Parámetro</th></tr></thead>
      <tbody>
        <tr><td><code>Header text</code></td><td><code>headerText</code></td></tr>
        <tr><td><code>Subhead text</code></td><td><code>subheadText</code></td></tr>
        <tr><td><code>Supporting text</code></td><td><code>supportingText</code></td></tr>
        <tr><td><code>Show secondary action</code></td><td><code>onDismiss</code></td></tr>
        <tr><td><code>Style</code></td><td><code>MotCardStyle</code></td></tr>
      </tbody>
    </table>
  </section>

  <section class="section">
    <h2>Instancias producto (Home)</h2>
    <table>
      <thead><tr><th>Frame</th><th>Composable</th></tr></thead>
      <tbody>
        <tr><td>205:816 vacío</td><td><code>HomeEmptyStackedCard</code></td></tr>
        <tr><td>Filtro vacío</td><td><code>MotStackedCard(Outlined, …)</code></td></tr>
      </tbody>
    </table>
  </section>

  <section class="section">
    <h2>Tokens M3</h2>
    <p><code>${e.tokens.join("</code>, <code>")}</code></p>
  </section>

  ${s(e)}

  <section class="section">
    <h2>Compose</h2>
    <pre><code>${x(e.compose)}</code></pre>
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
`;function x(t){return t.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;")}
