import{i as p,c as s,s as h,b as a,a as m,d as g}from"./bootstrap-Bil26Efv.js";p("overview");const o=s.components.length,i=s.components.filter(e=>e.status==="ready").length,r=s.components.filter(e=>e.status==="in-review").length,c=s.components.filter(e=>e.status==="draft").length,n=s.components.filter(e=>e.status==="in-review").map(e=>e.name);var l;const u=((l=h.nav.find(e=>e.id==="foundations"))==null?void 0:l.children)||[],f={color:"Roles M3, paleta gris-azul y acento ocre",typography:"Fraunces en titulares, Inter en el resto",shape:"Roles de superficie y el morph del botón",spacing:"MOTSpacing de pantalla y padding de componente",iconography:"Material Symbols Sharp, peso 300",motion:"Duraciones, el muelle de liberación y reduce motion"};document.getElementById("page-header").innerHTML=`
  <p class="page-header__eyebrow">Design System · MyOwnTrip Nativo</p>
  <h1>Material 3 con voz editorial</h1>
  <p class="page-header__lead">
    Un sistema construido sobre M3 canónico, sin capa semántica propia.
    La identidad está en la tipografía, en el color con cuentagotas y en un botón
    que es rectángulo hasta que lo tocas.
  </p>
`;document.getElementById("main").innerHTML=`
  <div class="entry-grid">
    <a class="entry-card" href="${a("empezar.html")}">
      <h3>Empezar aquí</h3>
      <p>Cómo diseñar una pantalla y cómo implementarla. Qué documenta cada capa.</p>
    </a>
    <a class="entry-card" href="${a("components.html")}">
      <h3>Componentes</h3>
      <p>${o} fichas: ${i} listas, ${r} en revisión${c?`, ${c} en borrador`:""}.</p>
    </a>
    <a class="entry-card" href="${a("gobernanza.html")}">
      <h3>Gobernanza</h3>
      <p>Estados, criterios para crear un componente propio y gates antes de cerrar.</p>
    </a>
  </div>

  <section class="section">
    <h2 id="foundations">Foundations</h2>
    <p class="section-lead">
      Las decisiones que no son de un componente concreto. Todas tienen su fuente de verdad
      en el código, no en esta web.
    </p>
    <div class="component-grid">
      ${u.map(e=>`
        <a class="component-link" href="${a(e.href)}">
          <h3>${e.label}</h3>
          <p>${f[e.id]||""}</p>
        </a>`).join("")}
    </div>
  </section>

  <section class="section">
    <h2 id="estado">Estado del sistema</h2>
    <p class="section-lead">
      Las ${o} fichas del índice están escritas. ${i} están <strong>listas</strong>
      (Figma publicado, ficha y Compose alineados). ${r} siguen <strong>en revisión</strong>:
      hay ficha, falta cerrar paridad o validar. No hay borradores: la deuda ya no es «falta
      la ficha», es «falta cerrar».
    </p>
    ${n.length?`<p class="section-lead">En revisión: <strong>${n.join("</strong>, <strong>")}</strong>.${n.includes("Swipeable list row")?" Esa ficha aún no usa el esquema Usage-first del resto del índice.":""}</p>`:'<p class="section-lead">Todas las fichas del índice están listas.</p>'}
    <p class="section-lead">
      <strong>Search bar</strong> y <strong>Menu</strong> viven en Figma y en Compose pero no tienen
      ficha en el índice; Home los documenta en
      <a href="${a("patterns.html")}#home-filter-menu">Patrones · menú de filtros</a>.
      La paridad Figma (state layers, iconos, etc.) se sigue en el log del repo, no en esta tabla.
    </p>
    <table>
      <thead><tr><th>Componente</th><th>Estado</th><th>Prioridad</th></tr></thead>
      <tbody>
        ${[...s.components].sort((e,d)=>{const t={"in-review":0,draft:1,ready:2,deprecated:3};return(t[e.status]??9)-(t[d.status]??9)}).map(e=>`
          <tr>
            <td>${e.href==="components.html"?e.name:`<a href="${a(e.href)}">${e.name}</a>`}</td>
            <td>${m(e.status)}</td>
            <td>${e.priority}</td>
          </tr>`).join("")}
      </tbody>
    </table>
  </section>

  <section class="section">
    <h2 id="pipeline">Pipeline</h2>
    <div class="callout">
      <strong>Tokens:</strong> Figma (variables) → <code>variables.json</code> → Compose.
      <strong>Componentes:</strong> Compose → Figma (espejo) → esta web.
      La paridad se anota en el log; Notion registra el trabajo, no especifica.
    </div>
  </section>
`;g();
