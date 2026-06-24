import{i,s as a}from"./bootstrap-BSec5oP0.js";const n="Swipeable list row",t="in-review",s="P1",c="Fila de lista con acciones laterales tipo Gmail: deslizar para archivar o eliminar, con menú ⋮ equivalente por tap. Patrón Compose compartido en Home («Más viajes») y Wallet (lista plana). No usar en el hero destacado ni en carruseles.",r=["SwipeToDismissBox — umbral ~45% del ancho; no confirma dismiss (acción inmediata + fila vuelve a reposo)","Background start — secondaryContainer + icono Archivar o Restaurar","Background end — errorContainer + icono Eliminar","Content — card/fila existente (TripListCard, WalletDocumentRow) + MOTIconButton ⋮","DropdownMenu — Archivar/Restaurar + Eliminar (error)"],l=[{id:"active",label:"Lista activa",usage:"Swipe derecha → Archivar; swipe izquierda → Eliminar (con diálogo de confirmación)."},{id:"archived",label:"Lista archivados",usage:"Swipe derecha → Restaurar; swipe izquierda → Eliminar."}],d=["secondaryContainer / onSecondaryContainer","errorContainer / onErrorContainer","error (texto e icono en menú Eliminar)","SwipeToDismissBox (Material 3)"],p=["Siempre ofrecer la misma acción por menú ⋮ que por swipe (política android-compose-ux).","Registrar customActions de TalkBack: «Archivar viaje» / «Restaurar viaje» y «Eliminar viaje» (o equivalente documento).","Snackbar con Deshacer tras archivar cuando el producto lo permita.","Eliminar siempre con diálogo de confirmación — el swipe solo abre el diálogo."],m=["No envolver el viaje destacado (TripHeroCard) ni filas del carrusel Wallet «Próximos».","No usar swipe como única vía para eliminar sin alternativa por tap.","No colores primitivos en fondos de swipe — solo roles M3 del colorScheme.","No crear component set en Figma para este patrón; documentar en showcase + referencia Compose."],h=["customActions en semantics de la fila (TalkBack: acciones personalizadas).","Iconos de fondo con contentDescription («Archivar», «Eliminar», «Restaurar»).","Menú ⋮: contentDescription «Acciones del viaje» / «Acciones del documento».","Eliminar en menú: icono + texto error — nunca solo color.","Ver checklist manual: docs/ux/talkback-checklist-swipe.md"],u=`// Home — lista «Más viajes»
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
)`,v=[{date:"2026-06-18",note:"Ficha inicial del patrón swipe archivar/eliminar (Home + Wallet)."}],o={name:n,status:t,priority:s,purpose:c,anatomy:r,variants:l,tokens:d,dos:p,donts:m,a11y:h,compose:u,changelog:v};i("components",{componentId:"swipeable-list-row"});const w="<p><em>Sin component set en Figma — patrón documentado en Compose y showcase.</em></p>";document.getElementById("component-header").innerHTML=`
  <p class="page-header__eyebrow">Component · Patrones · ${o.priority}</p>
  <h1>${o.name} ${a(o.status)}</h1>
  <p class="page-header__lead">${o.purpose}</p>
  ${w}
`;document.getElementById("component-main").innerHTML=`
  <section class="section">
    <h2>Anatomía</h2>
    <ol class="list-dos">
      ${o.anatomy.map(e=>`<li>${e}</li>`).join("")}
    </ol>
  </section>

  <section class="section">
    <h2>Variantes</h2>
    <table>
      <thead><tr><th>Variante</th><th>Uso</th></tr></thead>
      <tbody>
        ${o.variants.map(e=>`<tr><td>${e.label}</td><td>${e.usage}</td></tr>`).join("")}
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
    <p><code>${o.tokens.join("</code>, <code>")}</code></p>
  </section>

  <section class="section">
    <h2>Do</h2>
    <ul class="list-dos">${o.dos.map(e=>`<li>${e}</li>`).join("")}</ul>
  </section>

  <section class="section">
    <h2>Don't</h2>
    <ul class="list-dos list-dos--dont">${o.donts.map(e=>`<li>${e}</li>`).join("")}</ul>
  </section>

  <section class="section">
    <h2>Accesibilidad</h2>
    <ul class="list-dos">${o.a11y.map(e=>`<li>${e}</li>`).join("")}</ul>
  </section>

  <section class="section">
    <h2>Compose</h2>
    <pre><code>${y(o.compose)}</code></pre>
  </section>

  <section class="section">
    <h2>Changelog</h2>
    <table>
      <thead><tr><th>Fecha</th><th>Nota</th></tr></thead>
      <tbody>
        ${o.changelog.map(e=>`<tr><td>${e.date}</td><td>${e.note}</td></tr>`).join("")}
      </tbody>
    </table>
  </section>
`;function y(e){return e.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;")}
