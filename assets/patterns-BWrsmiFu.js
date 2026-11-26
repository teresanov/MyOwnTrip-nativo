import{i as n,a as o,d as t}from"./bootstrap-C0VoiN9M.js";const i="Un patrón resuelve un problema recurrente combinando componentes que ya existen. Si algo se puede resolver con un componente suelto, no es un patrón.",l=[{id:"home-filter-menu",name:"Home · menú de filtros y orden",status:"ready",problem:"Filtrar y ordenar la lista de viajes sin ocupar espacio permanente en una pantalla que ya tiene búsqueda.",solution:"Search bar con icono <code>tune</code>; al pulsarlo se abre un Menu con dos grupos (Mostrar y Ordenar). Pulsar una fila aplica el filtro y cierra el menú.",selection:{title:"Qué icono marca la opción activa",paragraphs:["En este menú la marca es <code>check</code>, a la izquierda. Todas las filas reservan la misma caja de 24 dp, también las que no están activas, para que los textos alineen.","<code>chevron_right</code> significaría que la fila abre un submenú. Aquí no hay submenú: pulsar cierra y aplica. Un radio (<code>radio_button_checked</code>) es el control de un formulario que sigue abierto; no es este patrón."],donts:["No poner <code>chevron_right</code> en una fila que no abre otro nivel.","No usar <code>radio_button_checked</code> para marcar la opción activa de este menú."]},components:["Search bar","Menu","Filter chip (no se usa aquí)"],ref:"docs/design-system/patterns/home-filter-menu.md"}],c=["Formulario de gasto (campo + validación + guardar)","Confirmación destructiva (diálogo + snackbar con deshacer)","Estado vacío de lista"],s={lead:i,patterns:l,pending:c};n("patterns");document.getElementById("page-header").innerHTML=`
  <p class="page-header__eyebrow">Patrones</p>
  <h1>Patrones de producto</h1>
  <p class="page-header__lead">${s.lead}</p>
`;document.getElementById("main").innerHTML=`
  ${s.patterns.map(e=>`
    <section class="section">
      <h2 id="${e.id}">${e.name} ${o(e.status)}</h2>
      <h3 class="subsection-title">Problema</h3>
      <p class="section-lead">${e.problem}</p>
      <h3 class="subsection-title">Solución</h3>
      <p class="section-lead">${e.solution}</p>
      ${e.selection?`
      <h3 class="subsection-title">${e.selection.title}</h3>
      ${(e.selection.paragraphs||[]).map(a=>`<p class="section-lead">${a}</p>`).join("")}
      ${(e.selection.donts||[]).length?`<div class="guideline-col guideline-col--dont"><h3 class="subsection-title">Don't</h3><ul class="list-dos list-dos--dont">${e.selection.donts.map(a=>`<li>${a}</li>`).join("")}</ul></div>`:""}`:""}
      <h3 class="subsection-title">Componentes implicados</h3>
      <ul class="list-dos">${e.components.map(a=>`<li>${a}</li>`).join("")}</ul>
      <p class="section-lead"><span class="header-meta">Detalle: <code>${e.ref}</code></span></p>
    </section>`).join("")}

  <section class="section">
    <h2 id="pendientes">Pendientes de documentar</h2>
    <p class="section-lead">
      Patrones que ya existen en la app pero no están escritos. Mientras no lo estén,
      cada pantalla los resuelve a su manera.
    </p>
    <ul class="list-dos">${s.pending.map(e=>`<li>${e}</li>`).join("")}</ul>
  </section>
`;t();
