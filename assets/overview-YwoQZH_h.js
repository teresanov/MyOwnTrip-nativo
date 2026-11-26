import{i as p,c as a,s as m,b as t,a as h,d as u}from"./bootstrap-BsJu56ug.js";p("overview");const i=a.components.length,r=a.components.filter(e=>e.status==="ready").length,s=a.components.filter(e=>e.status==="in-review").length,c=a.components.filter(e=>e.status==="draft").length,n=a.components.filter(e=>e.status==="in-review").map(e=>e.name);var l;const g=((l=m.nav.find(e=>e.id==="foundations"))==null?void 0:l.children)||[],b={color:"Roles M3, paleta gris-azul y acento ocre",typography:"Fraunces en titulares, Inter en el resto",shape:"Roles de superficie y el morph del botón",spacing:"MOTSpacing de pantalla y padding de componente",iconography:"Material Symbols Sharp, peso 300",motion:"Duraciones, el muelle de liberación y reduce motion"};document.getElementById("page-header").innerHTML=`
  <p class="page-header__eyebrow">Design System · MyOwnTrip Nativo</p>
  <h1>Material 3 con voz editorial</h1>
  <p class="page-header__lead">
    Un sistema construido sobre M3 canónico, sin capa semántica propia.
    La identidad está en la tipografía, en el color con cuentagotas y en un botón
    que es rectángulo hasta que lo tocas.
  </p>
  <div class="specimen-stage">
    <button type="button" class="mot-btn mot-btn--filled">Guardar gasto</button>
    <button type="button" class="mot-btn mot-btn--tonal">Siguiente</button>
    <button type="button" class="mot-btn mot-btn--outlined">Cancelar</button>
    <button type="button" class="filter-chip filter-chip--selected">Activos</button>
    <button type="button" class="filter-chip">Archivados</button>
  </div>
  <p class="specimen-stage__note">
    Los mismos roles que en Compose. El showcase no monta Android: esto es HTML a 1&nbsp;px = 1&nbsp;dp.
  </p>
`;document.getElementById("main").innerHTML=`
  <div class="entry-grid">
    <a class="entry-card" href="${t("empezar.html")}">
      <h3>Empezar aquí</h3>
      <p>Cómo diseñar una pantalla y cómo implementarla. Qué documenta cada capa.</p>
    </a>
    <a class="entry-card" href="${t("components.html")}">
      <h3>Componentes</h3>
      <p>${i} fichas: ${r} listas, ${s} en revisión${c?`, ${c} en borrador`:""}.</p>
    </a>
    <a class="entry-card" href="${t("gobernanza.html")}">
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
      ${g.map(e=>`
        <a class="component-link" href="${t(e.href)}">
          <h3>${e.label}</h3>
          <p>${b[e.id]||""}</p>
        </a>`).join("")}
    </div>
  </section>

  <section class="section">
    <h2 id="estado">Estado del sistema</h2>
    <p class="section-lead">
      Las ${i} fichas del índice están escritas. ${r} están <strong>listas</strong>
      (Figma publicado, ficha y Compose alineados). ${s?`${s} ${s===1?"sigue":"siguen"} <strong>en revisión</strong>: hay ficha, falta validar.`:"Ninguna está en revisión."}
      No hay borradores: la deuda ya no es «falta la ficha», es «falta cerrar».
    </p>
    ${n.length?`<p class="section-lead">En revisión: <strong>${n.join("</strong>, <strong>")}</strong>.${n.includes("Swipeable list row")?" Esa ficha aún no usa el esquema Usage-first del resto del índice.":""}</p>`:'<p class="section-lead">Todas las fichas del índice están listas.</p>'}
    <p class="section-lead">
      <strong>Search bar</strong> y <strong>Menu</strong> viven en Figma y en Compose pero no tienen
      ficha en el índice; Home los documenta en
      <a href="${t("patterns.html")}#home-filter-menu">Patrones · menú de filtros</a>.
      La paridad Figma (state layers, iconos, etc.) se sigue en el log del repo, no en esta tabla.
    </p>
    <table>
      <thead><tr><th>Componente</th><th>Estado</th><th>Prioridad</th></tr></thead>
      <tbody>
        ${[...a.components].sort((e,d)=>{const o={"in-review":0,draft:1,ready:2,deprecated:3};return(o[e.status]??9)-(o[d.status]??9)}).map(e=>`
          <tr>
            <td>${e.href==="components.html"?e.name:`<a href="${t(e.href)}">${e.name}</a>`}</td>
            <td>${h(e.status)}</td>
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
`;u();
