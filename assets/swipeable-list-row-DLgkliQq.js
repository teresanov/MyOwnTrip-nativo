import{i as a,a as i}from"./bootstrap-C_ZQ7Ieu.js";import{m as n}from"./decisions-Ij7LKPhR.js";import{d as t}from"./component-page-Cq4C7yJC.js";const r="Swipeable list row",s="in-review",c="P1",l="Fila de lista con acciones laterales tipo Gmail: deslizar para archivar o eliminar, con menú ⋮ equivalente por tap. Patrón Compose compartido en Home («Más viajes») y Wallet (lista plana). No usar en el hero destacado ni en carruseles.",d=["SwipeToDismissBox — umbral ~45% del ancho; no confirma dismiss (acción inmediata + fila vuelve a reposo)","Background start — secondaryContainer + icono Archivar o Restaurar","Background end — errorContainer + icono Eliminar","Content — card/fila existente (TripListCard, WalletDocumentRow) + MOTIconButton ⋮","DropdownMenu — Archivar/Restaurar + Eliminar (error)"],p=[{id:"active",label:"Lista activa",usage:"Swipe derecha → Archivar; swipe izquierda → Eliminar (con diálogo de confirmación)."},{id:"archived",label:"Lista archivados",usage:"Swipe derecha → Restaurar; swipe izquierda → Eliminar."}],m=["secondaryContainer / onSecondaryContainer","errorContainer / onErrorContainer","error (texto e icono en menú Eliminar)","SwipeToDismissBox (Material 3)"],h={dos:["Siempre ofrecer la misma acción por menú ⋮ que por swipe (política android-compose-ux).","Registrar customActions de TalkBack: «Archivar viaje» / «Restaurar viaje» y «Eliminar viaje» (o equivalente documento).","Snackbar con Deshacer tras archivar cuando el producto lo permita.","Eliminar siempre con diálogo de confirmación — el swipe solo abre el diálogo."],donts:["No envolver el viaje destacado (TripHeroCard) ni filas del carrusel Wallet «Próximos».","No usar swipe como única vía para eliminar sin alternativa por tap.","No colores primitivos en fondos de swipe — solo roles M3 del colorScheme.","No crear component set en Figma para este patrón; documentar en showcase + referencia Compose."]},u=["customActions en semantics de la fila (TalkBack: acciones personalizadas).","Iconos de fondo con contentDescription («Archivar», «Eliminar», «Restaurar»).","Menú ⋮: contentDescription «Acciones del viaje» / «Acciones del documento».","Eliminar en menú: icono + texto error — nunca solo color.","Ver checklist manual: docs/ux/talkback-checklist-swipe.md"],v=`// Home — lista «Más viajes»
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
)`,w={source:"Pendiente",why:"Acciones laterales (archivar, eliminar) con alternativa por tap (menú ⋮) para accesibilidad.",layer:"Patrón Compose compartido (Home «Más viajes», Wallet)"},y=[{date:"2026-06-18",note:"Ficha inicial del patrón swipe archivar/eliminar (Home + Wallet)."}],e={name:r,status:s,priority:c,purpose:l,anatomy:d,variants:p,tokens:m,productRules:h,a11y:u,compose:v,decisions:w,changelog:y};a("components",{componentId:"swipeable-list-row"});n(e);const g="<p><em>Sin component set en Figma — patrón documentado en Compose y showcase.</em></p>";document.getElementById("component-header").innerHTML=`
  <p class="page-header__eyebrow">Component · Patrones · ${e.priority}</p>
  <h1>${e.name} ${i(e.status)}</h1>
  <p class="page-header__lead">${e.purpose}</p>
  ${g}
`;document.getElementById("component-main").innerHTML=`
  <section class="section">
    <h2>Anatomía</h2>
    <ol class="list-dos">
      ${e.anatomy.map(o=>`<li>${o}</li>`).join("")}
    </ol>
  </section>

  <section class="section">
    <h2>Variantes</h2>
    <table>
      <thead><tr><th>Variante</th><th>Uso</th></tr></thead>
      <tbody>
        ${e.variants.map(o=>`<tr><td>${o.label}</td><td>${o.usage}</td></tr>`).join("")}
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

  ${t(e)}

  <section class="section">
    <h2>Accesibilidad</h2>
    <ul class="list-dos">${e.a11y.map(o=>`<li>${o}</li>`).join("")}</ul>
  </section>

  <section class="section">
    <h2>Compose</h2>
    <pre><code>${A(e.compose)}</code></pre>
  </section>

  <section class="section">
    <h2>Changelog</h2>
    <table>
      <thead><tr><th>Fecha</th><th>Nota</th></tr></thead>
      <tbody>
        ${e.changelog.map(o=>`<tr><td>${o.date}</td><td>${o.note}</td></tr>`).join("")}
      </tbody>
    </table>
  </section>
`;function A(o){return o.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;")}
