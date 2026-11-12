import{i as o,c as n,s,b as a,a as i,d as r}from"./bootstrap-Cqc7PUA6.js";o("overview");const c=n.components.length,d=n.components.filter(e=>e.status==="ready").length,l=n.components.filter(e=>e.status==="in-review").length,p=n.components.filter(e=>e.status==="draft").length;var t;const h=((t=s.nav.find(e=>e.id==="foundations"))==null?void 0:t.children)||[],m={color:"Roles M3, paleta gris-azul y acento ocre",typography:"Fraunces en titulares, Inter en el resto",shape:"Roles de superficie y el morph del botón",spacing:"MOTSpacing de pantalla y padding de componente",iconography:"Material Symbols Sharp, peso 300",motion:"Duraciones, el muelle de liberación y reduce motion"};document.getElementById("page-header").innerHTML=`
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
      <p>${c} componentes: ${d} listos, ${l} en revisión, ${p} sin ficha.</p>
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
      ${h.map(e=>`
        <a class="component-link" href="${a(e.href)}">
          <h3>${e.label}</h3>
          <p>${m[e.id]||""}</p>
        </a>`).join("")}
    </div>
  </section>

  <section class="section">
    <h2 id="estado">Estado del sistema</h2>
    <p class="section-lead">
      Una ficha en <strong>borrador</strong> significa que el componente existe en Figma o en
      código pero nadie ha escrito cuándo usarlo. Es la deuda visible del sistema.
    </p>
    <table>
      <thead><tr><th>Componente</th><th>Estado</th><th>Prioridad</th></tr></thead>
      <tbody>
        ${n.components.map(e=>`
          <tr>
            <td>${e.href==="components.html"?e.name:`<a href="${a(e.href)}">${e.name}</a>`}</td>
            <td>${i(e.status)}</td>
            <td>${e.priority}</td>
          </tr>`).join("")}
      </tbody>
    </table>
  </section>

  <section class="section">
    <h2 id="pipeline">Pipeline</h2>
    <div class="callout">
      <strong>Figma</strong> (variables, vía Desktop Bridge) → <code>variables.json</code> →
      <strong>Compose</strong> → esta web y el contexto para agentes.
      Figma es librería visual; la documentación de componentes vive aquí.
    </div>
  </section>
`;r();
