import{i as n}from"./bootstrap-Cqc7PUA6.js";import{m as s}from"./decisions-Ij7LKPhR.js";import{r as i,a as l,i as r,b as c,c as p,d,e as u,f as m,g as h}from"./component-page-Cq4C7yJC.js";const g="Filter chip",f="ready",v="P0",y="https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=53923-28270",b="Los filter chips permiten elegir una opción dentro de un conjunto pequeño y cambiarla con un solo toque. En MyOwnTrip los usamos en filas horizontales con scroll —por ejemplo, Activos / Archivados / Todos en Wallet— y en formularios cuando hay pocas categorías. Implementamos el componente Material 3 `FilterChip` sin wrapper propio; en Home el filtro de viajes sigue el patrón Menu, no chips.",A=["Container — Outlined pill, `Corner/Small` (8dp) ↔ `MaterialTheme.shapes.small`","Label — Inter `labelLarge`","Leading icon — opcional en set; Wallet caps = Label only","Selected — fill `secondaryContainer` + check; unselected — outline"],C=[{id:"outlined-unselected",label:"Outlined · Selected=False",usage:"Opciones no activas en la fila (p. ej. «Archivados»)."},{id:"outlined-selected",label:"Outlined · Selected=True",usage:"Filtro activo (p. ej. «Activos» en Wallet)."}],S=["secondaryContainer / onSecondaryContainer (selected)","outline / onSurface (unselected)","Corner/Small (8dp)","mot/padding/chip/horizontal-sm (8) · horizontal-lg (16)","mot/padding/chip/vertical (6)","mot/spacing/chip/gap (8) — gap entre chips en fila","MOTSpacing.componentSm — gap entre chips en Compose (Wallet)"],F={label:"Material Design 3 · Chips",url:"https://m3.material.io/components/chips/guidelines",consulted:"2026-11-10"},M=["Usa chips para presentar opciones contextuales y complementarias.","Muestra los chips siempre en un conjunto; pueden desplazarse horizontalmente.","Etiquetas cortas (20 caracteres o menos), con la misma tipografía que los botones.","Redacta los filter chips con sustantivos que describan la categoría que se incluye en los resultados.","Al tocar un filter chip se activa y aparece un check al inicio de la etiqueta; puede permitirse selección múltiple o de un solo chip.","Define el borde del chip con un contorno (outline) sobre fondos normales; la elevación solo si va sobre una imagen."],E=["No sustituyas acciones principales por chips: las acciones que avanzan o retroceden en un paso van siempre como botones.","No uses chips para terminar o avanzar una tarea; el último paso de una tarea es un botón.","No muestres un chip suelto: deben aparecer en un conjunto.","No mezcles selección única y múltiple entre los conjuntos de chips de una misma pantalla.","No eleves los chips cuando están directamente sobre la página, ni uses la elevación para indicar el estado pulsado (usa el ripple).","Evita frases negativas en los filter chips, como «Excluir imágenes»."],T={dos:["Agrupa los chips en una fila con scroll horizontal y separación `MOTSpacing.componentSm` entre elementos.","Mantén una sola opción seleccionada por eje de filtro (como Activos / Archivados / Todos).","Usa copy corto y claro en español; en Figma ajusta la propiedad Label de la instancia, no texto suelto encima.","Reserva AssistChip para los pocos flujos que ya lo tienen; no lo uses donde corresponde Eyebrow label."],donts:["No reintroduzcas la variante Elevated del set (fue eliminada en la Fase D de la librería).","No uses esquinas Extra-small en chips: ese radio pertenece al Text field.","No dibujes chips con frames sueltos en el design-file; instancia siempre el set publicado.","No sustituyas el menú de filtro de Home por FilterChip: el patrón acordado es Menu."]},z=["Cada chip: rol toggleable; anunciar selected/unselected.","Alternativa sin scroll horizontal forzado: mismo filtro accesible vía Menu donde aplique.","Touch target ≥ 48dp vía padding M3."],j=`Row(
    modifier = Modifier.horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
) {
    FilterChip(
        selected = filterPhase == WalletDocumentFilterPhase.Active,
        onClick = { onFilterPhaseChange(WalletDocumentFilterPhase.Active) },
        label = { Text("Activos") },
    )
    // Archivados, Todos…
}`,q=[{screen:"WalletScreen",pattern:"WalletDocumentFilterChips — Activos / Archivados / Todos"},{screen:"ExpenseFormScreen",pattern:"Categoría y método de pago"},{screen:"JournalAddScreen",pattern:"Tipo de entrada"}],w={source:"Figma node 53923-28270 · Compose `FilterChip` (M3)",why:"Elegir una opción dentro de un conjunto pequeño con un toque (Wallet, formularios).",layer:"M3 directo, sin wrapper"},L=[{date:"2026-10-29",note:"Fase B.3: Corner/Small + mot/padding/chip/*; Wallet 6 instancias; 0 LEGACY."},{date:"2026-10-30",note:"Ficha showcase: playgrounds interactivos + copy legible (estilo Material)."}],e={name:g,status:f,priority:v,figma:y,purpose:b,anatomy:A,variants:C,tokens:S,guidelines:F,dos:M,donts:E,productRules:T,a11y:z,compose:j,productExamples:q,decisions:w,changelog:L};n("components",{componentId:"filter-chip"});s(e);document.getElementById("component-header").innerHTML=i(e,"Abrir Filter chip en Figma");const $=(e.productExamples||[]).map(a=>`<tr><td><code>${a.screen}</code></td><td>${a.pattern}</td></tr>`).join("");document.getElementById("component-main").innerHTML=`
  ${l({title:"Playground",hint:"Pulsa un chip para cambiar el filtro activo. En la app, la fila es scroll horizontal y solo puede haber una opción seleccionada por eje.",canvasHtml:`
      <div class="filter-chip-row filter-chip-demo" role="group" aria-label="Filtro de documentos">
        <button type="button" class="filter-chip filter-chip--selected" aria-pressed="true" data-value="Activos">Activos</button>
        <button type="button" class="filter-chip" aria-pressed="false" data-value="Archivados">Archivados</button>
        <button type="button" class="filter-chip" aria-pressed="false" data-value="Todos">Todos</button>
      </div>
      <p class="playground__status" id="filter-chip-status" aria-live="polite">Filtro activo: Activos</p>
    `})}

  ${r(e.anatomy)}
  ${c(e.variants)}

  <section class="section">
    <h2>Dónde aparece en la app</h2>
    <p class="section-lead">Referencia rápida a pantallas que ya consumen este patrón en Compose.</p>
    <table>
      <thead><tr><th>Pantalla</th><th>Patrón</th></tr></thead>
      <tbody>${$}</tbody>
    </table>
  </section>

  ${p(e.tokens)}
  ${d(e)}
  ${u(e.a11y)}
  ${m(e.compose)}
  ${h(e.changelog)}
`;const t=document.querySelectorAll(".filter-chip-demo .filter-chip"),x=document.getElementById("filter-chip-status");t.forEach(a=>{a.addEventListener("click",()=>{t.forEach(o=>{o.classList.remove("filter-chip--selected"),o.setAttribute("aria-pressed","false")}),a.classList.add("filter-chip--selected"),a.setAttribute("aria-pressed","true"),x.textContent=`Filtro activo: ${a.dataset.value}`})});
