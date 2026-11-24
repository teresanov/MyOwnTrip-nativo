import{i as t,a as r}from"./bootstrap-Bil26Efv.js";import{m as s}from"./decisions-Ij7LKPhR.js";import{d as c}from"./component-page-7gUNYbPL.js";const l="Swipeable list row",d="in-review",p="P1",m={label:"List Item - Swipe",node:"59106:13321",url:"https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=59106-13321"},h="Fila de lista con acciones laterales tipo Gmail: deslizar para archivar o eliminar, con menú ⋮ equivalente por tap. Patrón Compose compartido en Home («Más viajes») y Wallet (lista plana). No usar en el hero destacado ni en carruseles.",u=["SwipeToDismissBox — umbral ~45% del ancho; no confirma dismiss (acción inmediata + fila vuelve a reposo)","Background start — secondaryContainer + icono Archivar o Restaurar","Background end — errorContainer + icono Eliminar","Content — card/fila existente (TripListCard, WalletDocumentRow) + MOTIconButton ⋮","DropdownMenu — Archivar/Restaurar + Eliminar (error)"],v=[{id:"active",label:"Lista activa",usage:"Swipe derecha → Archivar; swipe izquierda → Eliminar (con diálogo de confirmación)."},{id:"archived",label:"Lista archivados",usage:"Swipe derecha → Restaurar; swipe izquierda → Eliminar."}],w=["secondaryContainer / onSecondaryContainer","errorContainer / onErrorContainer","error (texto e icono en menú Eliminar)","SwipeToDismissBox (Material 3)"],g={dos:["Siempre ofrecer la misma acción por menú ⋮ que por swipe (política android-compose-ux).","Registrar customActions de TalkBack: «Archivar viaje» / «Restaurar viaje» y «Eliminar viaje» (o equivalente documento).","Snackbar con Deshacer tras archivar cuando el producto lo permita.","Eliminar siempre con diálogo de confirmación — el swipe solo abre el diálogo."],donts:["No envolver el viaje destacado (TripHeroCard) ni filas del carrusel Wallet «Próximos».","No usar swipe como única vía para eliminar sin alternativa por tap.","No colores primitivos en fondos de swipe — solo roles M3 del colorScheme.","No redibujar el swipe con frames sueltos: instanciar List Item - Swipe del DS."]},y=["customActions en semantics de la fila (TalkBack: acciones personalizadas).","Iconos de fondo con contentDescription («Archivar», «Eliminar», «Restaurar»).","Menú ⋮: contentDescription «Acciones del viaje» / «Acciones del documento».","Eliminar en menú: icono + texto error — nunca solo color.","Ver checklist manual: docs/ux/talkback-checklist-swipe.md"],b=`// Home — lista «Más viajes»
SwipeableTripListRow(
    trip = trip,
    today = today,
    showArchivedActions = filterPhase == Archived,
    onClick = { onTripClick(trip.id) },
    onArchive = { onArchiveTrip(trip.id) },
    onUnarchive = { onUnarchiveTrip(trip.id) },
    onDeleteRequest = { requestDelete(trip.id) },
)

// Wallet — lista plana
SwipeableWalletDocumentRow(
    entry = entry,
    showArchivedActions = showArchived,
    onClick = { onEntryClick(entry.id) },
    onArchive = { onArchive(entry.id) },
    onUnarchive = { onUnarchive(entry.id) },
    onDeleteRequest = { requestDelete(entry) },
)`,f={source:"Figma List Item - Swipe 59106:13321 · SwipeableTripListRow / SwipeableWalletDocumentRow (app)",why:"Acciones laterales (archivar, eliminar) con alternativa por tap (menú ⋮) para accesibilidad.",layer:"Patrón Compose compartido (Home «Más viajes», Wallet)"},A=[{date:"2026-06-18",note:"Ficha inicial del patrón swipe archivar/eliminar (Home + Wallet)."}],e={name:l,status:d,priority:p,figma:m,purpose:h,anatomy:u,variants:v,tokens:w,productRules:g,a11y:y,compose:b,decisions:f,changelog:A};t("components",{componentId:"swipeable-list-row"});s(e);var a;const o=((a=e.figma)==null?void 0:a.url)||(typeof e.figma=="string"?e.figma:null);var n;const S=o?`<p><a href="${o}" target="_blank" rel="noopener">${((n=e.figma)==null?void 0:n.label)||"Referencia en Figma"} ↗</a></p>`:"<p><em>Sin set publicado en Figma — patrón documentado en Compose y showcase.</em></p>";document.getElementById("component-header").innerHTML=`
  <p class="page-header__eyebrow">Component · Patrones · ${e.priority||"P1"}</p>
  <h1>${e.name} ${r(e.status)}</h1>
  <p class="page-header__lead">${e.purpose}</p>
  ${S}
`;document.getElementById("component-main").innerHTML=`
  <section class="section">
    <h2>Anatomía</h2>
    <ol class="list-dos">
      ${e.anatomy.map(i=>`<li>${i}</li>`).join("")}
    </ol>
  </section>

  <section class="section">
    <h2>Variantes</h2>
    <table>
      <thead><tr><th>Variante</th><th>Uso</th></tr></thead>
      <tbody>
        ${e.variants.map(i=>`<tr><td>${i.label}</td><td>${i.usage}</td></tr>`).join("")}
      </tbody>
    </table>
  </section>

  <section class="section">
    <h2>Implementaciones</h2>
    <ul class="list-dos">
      <li><code>SwipeableTripListRow.kt</code> — Home, sección «Más viajes» y filtro Archivados</li>
      <li><code>SwipeableWalletDocumentRow.kt</code> — Wallet, lista plana (chips Activos / Archivados / Todos)</li>
    </ul>
  </section>

  <section class="section">
    <h2>Tokens M3</h2>
    <p><code>${e.tokens.join("</code>, <code>")}</code></p>
  </section>

  ${c(e)}

  <section class="section">
    <h2>Accesibilidad</h2>
    <ul class="list-dos">${e.a11y.map(i=>`<li>${i}</li>`).join("")}</ul>
  </section>

  <section class="section">
    <h2>Compose</h2>
    <pre><code>${k(e.compose)}</code></pre>
  </section>

  <section class="section">
    <h2>Changelog</h2>
    <table>
      <thead><tr><th>Fecha</th><th>Nota</th></tr></thead>
      <tbody>
        ${e.changelog.map(i=>`<tr><td>${i.date}</td><td>${i.note}</td></tr>`).join("")}
      </tbody>
    </table>
  </section>
`;function k(i){return i.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;")}
