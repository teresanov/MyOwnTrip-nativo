import{i as n,a as o,d as t}from"./bootstrap-C_ZQ7Ieu.js";const c="Un patrón resuelve un problema recurrente combinando componentes que ya existen. Si algo se puede resolver con un componente suelto, no es un patrón.",i=[{id:"home-filter-menu",name:"Home · menú de filtros y orden",status:"ready",problem:"Filtrar y ordenar la lista de viajes sin ocupar espacio permanente en una pantalla que ya tiene búsqueda.",solution:"Search bar con icono <code>tune</code>; al pulsarlo se abre un Menu con <code>Menu Groups = 2</code> (filtro y orden). La selección se marca con <code>check</code> leading en una caja reservada, y el trailing queda oculto.",components:["Search bar","Menu","Filter chip (no se usa aquí)"],ref:"docs/design-system/patterns/home-filter-menu.md"}],l=["Formulario de gasto (campo + validación + guardar)","Confirmación destructiva (diálogo + snackbar con deshacer)","Estado vacío de lista"],a={lead:c,patterns:i,pending:l};n("patterns");document.getElementById("page-header").innerHTML=`
  <p class="page-header__eyebrow">Patrones</p>
  <h1>Patrones de producto</h1>
  <p class="page-header__lead">${a.lead}</p>
`;document.getElementById("main").innerHTML=`
  ${a.patterns.map(e=>`
    <section class="section">
      <h2 id="${e.id}">${e.name} ${o(e.status)}</h2>
      <h3 class="subsection-title">Problema</h3>
      <p class="section-lead">${e.problem}</p>
      <h3 class="subsection-title">Solución</h3>
      <p class="section-lead">${e.solution}</p>
      <h3 class="subsection-title">Componentes implicados</h3>
      <ul class="list-dos">${e.components.map(s=>`<li>${s}</li>`).join("")}</ul>
      <p class="section-lead"><span class="header-meta">Detalle: <code>${e.ref}</code></span></p>
    </section>`).join("")}

  <section class="section">
    <h2 id="pendientes">Pendientes de documentar</h2>
    <p class="section-lead">
      Patrones que ya existen en la app pero no están escritos. Mientras no lo estén,
      cada pantalla los resuelve a su manera.
    </p>
    <ul class="list-dos">${a.pending.map(e=>`<li>${e}</li>`).join("")}</ul>
  </section>
`;t();
