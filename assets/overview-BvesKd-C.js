import{i as d,c as n,s as p,b as t,a as m,d as u}from"./bootstrap-D3HXQIgy.js";d("overview");const s=n.components.length,i=n.components.filter(e=>e.status==="ready").length,r=n.components.filter(e=>e.status==="in-review").length,a=n.components.filter(e=>e.status==="draft").length;var c;const h=((c=p.nav.find(e=>e.id==="foundations"))==null?void 0:c.children)||[],b={color:"Roles M3, paleta gris-azul y acento ocre",typography:"Fraunces en titulares, Inter en el resto",shape:"Roles de superficie y el morph del botón",spacing:"MOTSpacing de pantalla y padding de componente",iconography:"Material Symbols Sharp, peso 300",motion:"Duraciones, el muelle de liberación y reduce motion"};document.getElementById("page-header").innerHTML=`
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
      <p>${s} fichas: ${i} listas, ${r} en revisión${a?`, ${a} en borrador`:""}.</p>
    </a>
    <a class="entry-card" href="${t("gobernanza.html")}">
      <h3>Gobernanza</h3>
      <p>Estados de las fichas, cuándo crear un componente propio y qué se comprueba antes de cerrar.</p>
    </a>
  </div>

  <section class="section">
    <h2 id="foundations">Foundations</h2>
    <p class="section-lead">
      Las decisiones que no son de un componente concreto. Todas tienen su fuente de verdad
      en el código, no en esta web.
    </p>
    <div class="component-grid">
      ${h.map(e=>`
        <a class="component-link" href="${t(e.href)}">
          <h3>${e.label}</h3>
          <p>${b[e.id]||""}</p>
        </a>`).join("")}
    </div>
  </section>

  <section class="section">
    <h2 id="estado">Estado del sistema</h2>
    <p class="section-lead">
      Esta web es un trabajo en curso: cada ficha dice en qué punto está, con el criterio que explica
      <a href="${t("gobernanza.html")}#estados">Gobernanza</a>.
      De ${s} fichas, ${i} están <strong>listas</strong> (ejemplo con el componente real, anatomía y medidas,
      revisados por diseño), ${r} están <strong>en revisión</strong> y ${a}
      ${a===1?"sigue":"siguen"} <strong>en construcción</strong>.
    </p>
    <p class="section-lead">
      Las diferencias pendientes entre Figma y la app se siguen en el repositorio, no en esta tabla.
    </p>
    <table>
      <thead><tr><th>Componente</th><th>Estado</th><th>Prioridad</th></tr></thead>
      <tbody>
        ${[...n.components].sort((e,l)=>{const o={draft:0,"in-review":1,ready:2};return(o[e.status]??9)-(o[l.status]??9)}).map(e=>`
          <tr>
            <td>${e.href==="components.html"?e.name:`<a href="${t(e.href)}">${e.name}</a>`}</td>
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
`;u();
