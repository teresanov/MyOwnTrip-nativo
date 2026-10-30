import{i as n}from"./bootstrap-BweT237-.js";import{r as s,a as i,h as l,b as r,c,d,e as p,f as u,g as m}from"./component-page-Ds0nsHTG.js";const h="Filter chip",g="ready",f="P0",v="https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=53923-28270",y="Los filter chips permiten elegir una opción dentro de un conjunto pequeño y cambiarla con un solo toque. En MyOwnTrip los usamos en filas horizontales con scroll —por ejemplo, Activos / Archivados / Todos en Wallet— y en formularios cuando hay pocas categorías. Implementamos el componente Material 3 `FilterChip` sin wrapper propio; en Home el filtro de viajes sigue el patrón Menu, no chips.",b=["Container — Outlined pill, `Corner/Small` (8dp) ↔ `MaterialTheme.shapes.small`","Label — Inter `labelLarge`","Leading icon — opcional en set; Wallet caps = Label only","Selected — fill `secondaryContainer` + check; unselected — outline"],A=[{id:"outlined-unselected",label:"Outlined · Selected=False",usage:"Opciones no activas en la fila (p. ej. «Archivados»)."},{id:"outlined-selected",label:"Outlined · Selected=True",usage:"Filtro activo (p. ej. «Activos» en Wallet)."}],S=["secondaryContainer / onSecondaryContainer (selected)","outline / onSurface (unselected)","Corner/Small (8dp)","mot/padding/chip/horizontal-sm (8) · horizontal-lg (16)","mot/padding/chip/vertical (6)","mot/spacing/chip/gap (8) — gap entre chips en fila","MOTSpacing.componentSm — gap entre chips en Compose (Wallet)"],C=["Agrupa los chips en una fila con scroll horizontal y separación `MOTSpacing.componentSm` entre elementos.","Mantén una sola opción seleccionada por eje de filtro (como Activos / Archivados / Todos).","Usa copy corto y claro en español; en Figma ajusta la propiedad Label de la instancia, no texto suelto encima.","Reserva AssistChip para los pocos flujos que ya lo tienen; no lo uses donde corresponde Eyebrow label."],T=["No reintroduzcas la variante Elevated del set (fue eliminada en la Fase D de la librería).","No uses esquinas Extra-small en chips: ese radio pertenece al Text field.","No dibujes chips con frames sueltos en el design-file; instancia siempre el set publicado.","No sustituyas el menú de filtro de Home por FilterChip: el patrón acordado es Menu."],F=["Cada chip: rol toggleable; anunciar selected/unselected.","Alternativa sin scroll horizontal forzado: mismo filtro accesible vía Menu donde aplique.","Touch target ≥ 48dp vía padding M3."],M=`Row(
    modifier = Modifier.horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
) {
    FilterChip(
        selected = filterPhase == WalletDocumentFilterPhase.Active,
        onClick = { onFilterPhaseChange(WalletDocumentFilterPhase.Active) },
        label = { Text("Activos") },
    )
    // Archivados, Todos…
}`,E=[{screen:"WalletScreen",pattern:"WalletDocumentFilterChips — Activos / Archivados / Todos"},{screen:"ExpenseFormScreen",pattern:"Categoría y método de pago"},{screen:"JournalAddScreen",pattern:"Tipo de entrada"}],L=[{date:"2026-10-29",note:"Fase B.3: Corner/Small + mot/padding/chip/*; Wallet 6 instancias; 0 LEGACY."},{date:"2026-10-30",note:"Ficha showcase: playgrounds interactivos + copy legible (estilo Material)."}],e={name:h,status:g,priority:f,figma:v,purpose:y,anatomy:b,variants:A,tokens:S,dos:C,donts:T,a11y:F,compose:M,productExamples:E,changelog:L};n("components",{componentId:"filter-chip"});document.getElementById("component-header").innerHTML=s(e,"Abrir Filter chip en Figma");const z=(e.productExamples||[]).map(a=>`<tr><td><code>${a.screen}</code></td><td>${a.pattern}</td></tr>`).join("");document.getElementById("component-main").innerHTML=`
  ${i({title:"Playground",hint:"Pulsa un chip para cambiar el filtro activo. En la app, la fila es scroll horizontal y solo puede haber una opción seleccionada por eje.",canvasHtml:`
      <div class="filter-chip-row filter-chip-demo" role="group" aria-label="Filtro de documentos">
        <button type="button" class="filter-chip filter-chip--selected" aria-pressed="true" data-value="Activos">Activos</button>
        <button type="button" class="filter-chip" aria-pressed="false" data-value="Archivados">Archivados</button>
        <button type="button" class="filter-chip" aria-pressed="false" data-value="Todos">Todos</button>
      </div>
      <p class="playground__status" id="filter-chip-status" aria-live="polite">Filtro activo: Activos</p>
    `})}

  ${l(e.anatomy)}
  ${r(e.variants)}

  <section class="section">
    <h2>Dónde aparece en la app</h2>
    <p class="section-lead">Referencia rápida a pantallas que ya consumen este patrón en Compose.</p>
    <table>
      <thead><tr><th>Pantalla</th><th>Patrón</th></tr></thead>
      <tbody>${z}</tbody>
    </table>
  </section>

  ${c(e.tokens)}
  ${d(e)}
  ${p(e.a11y)}
  ${u(e.compose)}
  ${m(e.changelog)}
`;const o=document.querySelectorAll(".filter-chip-demo .filter-chip"),$=document.getElementById("filter-chip-status");o.forEach(a=>{a.addEventListener("click",()=>{o.forEach(t=>{t.classList.remove("filter-chip--selected"),t.setAttribute("aria-pressed","false")}),a.classList.add("filter-chip--selected"),a.setAttribute("aria-pressed","true"),$.textContent=`Filtro activo: ${a.dataset.value}`})});
