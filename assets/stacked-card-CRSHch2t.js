import{i as a,s as o}from"./bootstrap-BSec5oP0.js";const d="Stacked card",s="ready",n="P1",c="https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=52346-27573",i="Card vertical editorial: Outlined en listas/feeds, Elevated en destacados. Layouts Media & text (avatar + media + copy) y Text only (header + supporting + dismiss). Filled está CUT.",r=["Surface — OutlinedCard o ElevatedCard, shapes.medium (12dp)","Header row — Header text + Subhead text (avatar oculto en Home vacío Figma)","Dismiss — IconButton close si Show secondary action","Media — imagen full-bleed bajo header (Media & text)","Supporting text — cuerpo en Text only"],l=[{id:"outlined-media-text",label:"Outlined · Media & text",usage:"Home vacío, cards editoriales en feed."},{id:"elevated-text-only",label:"Elevated · Text only",usage:"Avisos descartables o mensajes destacados (si el producto los necesita)."},{id:"outlined-text-only",label:"Outlined · Text only",usage:"Mensajes inline en listas (filtro vacío)."}],m=["surfaceContainerLow + onSurface / onSurfaceVariant (Elevated · Text only)","onSurface / onSurfaceVariant (Outlined · Media & text)","surfaceContainerHigh (avatar)","shapes.medium"],p=["Outlined en feeds; Elevated en promos destacadas.","Mapear properties Figma 1:1 a MotStackedCard.","Home vacío: HomeEmptyStackedCard (instancia documentada)."],h=["No Filled (CUT).","No OutlinedCard ad hoc en pantallas si encaja un layout de este set.","No icono decorativo arbitrario en Text only — copy + dismiss si aplica."],u=`MotStackedCard(style, headerText, subheadText, …)
HomeEmptyStackedCard()`,g=[{date:"2026-06-19",note:"Wallet promo retirada de Home; Elevated · Text only queda como layout genérico vía MotStackedCard."},{date:"2026-06-17",note:"Binding Figma → MotStackedCard; fichas Home cap 1 y Wallet."}],t={name:d,status:s,priority:n,figma:c,purpose:i,anatomy:r,variants:l,tokens:m,dos:p,donts:h,compose:u,changelog:g};a("components",{componentId:"stacked-card"});document.getElementById("component-header").innerHTML=`
  <p class="page-header__eyebrow">Component · Cards · ${t.priority}</p>
  <h1>${t.name} ${o(t.status)}</h1>
  <p class="page-header__lead">${t.purpose}</p>
  <p><a href="${t.figma}" target="_blank" rel="noopener">Stacked card en Figma ↗</a> · <a href="../../docs/design-system/figma-compose-bindings.md">Bindings Figma ↔ Compose</a></p>
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
      ${t.anatomy.map(e=>`<li>${e}</li>`).join("")}
    </ol>
  </section>

  <section class="section">
    <h2>Variantes</h2>
    <table>
      <thead><tr><th>Variante</th><th>Uso</th></tr></thead>
      <tbody>
        ${t.variants.map(e=>`<tr><td>${e.label}</td><td>${e.usage}</td></tr>`).join("")}
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
    <p><code>${t.tokens.join("</code>, <code>")}</code></p>
  </section>

  <section class="section">
    <h2>Do</h2>
    <ul class="list-dos">${t.dos.map(e=>`<li>${e}</li>`).join("")}</ul>
  </section>

  <section class="section">
    <h2>Don't</h2>
    <ul class="list-dos list-dos--dont">${t.donts.map(e=>`<li>${e}</li>`).join("")}</ul>
  </section>

  <section class="section">
    <h2>Compose</h2>
    <pre><code>${y(t.compose)}</code></pre>
  </section>

  <section class="section">
    <h2>Changelog</h2>
    <table>
      <thead><tr><th>Fecha</th><th>Nota</th></tr></thead>
      <tbody>
        ${t.changelog.map(e=>`<tr><td>${e.date}</td><td>${e.note}</td></tr>`).join("")}
      </tbody>
    </table>
  </section>
`;function y(e){return e.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;")}
