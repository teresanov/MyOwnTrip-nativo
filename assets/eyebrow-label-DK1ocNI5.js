import{i as o,a}from"./bootstrap-C_ZQ7Ieu.js";import{m as n}from"./decisions-Ij7LKPhR.js";import{d as i}from"./component-page-Cq4C7yJC.js";const s="Eyebrow label",r="ready",l="P1",c="https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61202-16834",d="Etiqueta informativa no interactiva para contexto editorial (fase del viaje, categoría, aviso breve). Sustituye al Assist chip cuando el fondo es variable (fotos, scrims) y Outlined no garantiza contraste. Los chips del DS siguen reservados a filtros, selección y acciones.",p=["Container — pill auto-layout horizontal, **HUG contents** (ancho = texto + padding), fill según Color, Corner/Small (8dp), sin sombra ni borde","Label text — Inter Medium, tamaño según Size, textAutoResize WIDTH_AND_HEIGHT"],m=[{id:"tertiary-medium",label:"Color=Tertiary · Size=Medium",usage:"Default en TripHeroCard y portadas sobre media."},{id:"tertiary-small",label:"Color=Tertiary · Size=Small",usage:"Portadas compactas o overlays densos."},{id:"surface-medium",label:"Color=Surface · Size=Medium",usage:"Etiqueta sobre surface o cards sin foto."},{id:"surface-small",label:"Color=Surface · Size=Small",usage:"Variante compacta en listas."},{id:"secondary-medium",label:"Color=Secondary · Size=Medium",usage:"Acento suave en superficies claras."},{id:"secondary-small",label:"Color=Secondary · Size=Small",usage:"Misma función en espacios reducidos."}],u=[{name:"Label text",type:"TEXT",example:"Próximo viaje"},{name:"Color",type:"VARIANT",example:"Tertiary | Surface | Secondary"},{name:"Size",type:"VARIANT",example:"Medium (default) | Small"}],h=["tertiaryFixedDim + onTertiaryContainer (Color=Tertiary)","surfaceContainerHigh + onSurfaceVariant (Color=Surface)","secondaryContainer + onSecondaryContainer (Color=Secondary)","Corner/Small (8dp)","labelMedium / labelSmall (Inter Medium)"],y={dos:["Usar para fase o contexto («Próximo viaje», «En destino», «Recuerdo»).","Color=Tertiary sobre fotos y scrims.","Color=Surface o Secondary en superficies planas.","Property Label text en instancia — no duplicar capas de texto sueltas.","Sizing HUG: el ancho crece con el copy; no FILL del contenedor padre."],donts:["No usar como sustituto de Filter chip ni Assist chip interactivo.","No añadir sombra Elevated ni borde Outlined — no es chip.","No hacer clickable en producto.","No usar Assist chip Elevated (CUT) para este patrón."]},b=["Decorativo en sí: el texto se anuncia en el bloque padre (card o sección).","Modifier.semantics { invisibleToUser() } si el padre ya incluye la fase en contentDescription.","Nunca role Button ni onClick.","Contraste mínimo 4.5:1 entre label y container en todos los pares Color."],g="com.myowntrip.app.ui.components.EyebrowLabel(text, color = EyebrowLabelColor.Tertiary)",C={source:"Figma node 61202-16834 · Compose `EyebrowLabel` (MOT)",why:"Etiqueta no interactiva para fondos variables (fotos, scrims), donde Assist chip y Outlined no garantizan contraste.",layer:"Componente MOT (`EyebrowLabel`)"},f=[{date:"2026-06-17",note:"Component set Eyebrow label en página Labels — 3×2 variantes Color×Size."}],t={name:s,status:r,priority:l,figma:c,purpose:d,anatomy:p,variants:m,properties:u,tokens:h,productRules:y,a11y:b,compose:g,decisions:C,changelog:f};o("components",{componentId:"eyebrow-label"});n(t);document.getElementById("component-header").innerHTML=`
  <p class="page-header__eyebrow">Component · Labels · ${t.priority}</p>
  <h1>${t.name} ${a(t.status)}</h1>
  <p class="page-header__lead">${t.purpose}</p>
  <p><a href="${t.figma}" target="_blank" rel="noopener">Página Labels en Figma ↗</a></p>
`;document.getElementById("component-main").innerHTML=`
  <section class="section">
    <h2>Anatomía</h2>
    <ol class="list-dos">
      ${t.anatomy.map(e=>`<li>${e}</li>`).join("")}
    </ol>
  </section>

  <section class="section">
    <h2>Variantes (Color × Size)</h2>
    <table>
      <thead><tr><th>Variante</th><th>Uso</th></tr></thead>
      <tbody>
        ${t.variants.map(e=>`<tr><td>${e.label}</td><td>${e.usage}</td></tr>`).join("")}
      </tbody>
    </table>
  </section>

  <section class="section">
    <h2>Properties (instancia)</h2>
    <table>
      <thead><tr><th>Property</th><th>Tipo</th><th>Ejemplo</th></tr></thead>
      <tbody>
        ${t.properties.map(e=>`<tr><td><code>${e.name}</code></td><td>${e.type}</td><td>${e.example}</td></tr>`).join("")}
      </tbody>
    </table>
  </section>

  <section class="section">
    <h2>Chips vs Eyebrow label</h2>
    <table>
      <thead><tr><th></th><th>Chip (Outlined)</th><th>Eyebrow label</th></tr></thead>
      <tbody>
        <tr><td>Función</td><td>Filtro, selección, acción</td><td>Contexto informativo</td></tr>
        <tr><td>Interactivo</td><td>Sí</td><td>No</td></tr>
        <tr><td>Sobre foto</td><td>No (Outlined)</td><td>Sí (Color=Tertiary)</td></tr>
      </tbody>
    </table>
  </section>

  <section class="section">
    <h2>Tokens M3</h2>
    <p><code>${t.tokens.join("</code>, <code>")}</code></p>
  </section>

  ${i(t)}

  <section class="section">
    <h2>Accesibilidad</h2>
    <ul class="list-dos">${t.a11y.map(e=>`<li>${e}</li>`).join("")}</ul>
  </section>

  <section class="section">
    <h2>Compose</h2>
    <pre><code>${S(t.compose)}</code></pre>
  </section>

  <section class="section">
    <h2>Changelog</h2>
    <table>
      <thead><tr><th>Fecha</th><th>Nota</th></tr></thead>
      <tbody>
        ${t.changelog.map(e=>`<tr><td>${e.date}</td><td>${e.note}</td></tr>`).join("")}
      </tbody>
    </table>
  </section>
`;function S(e){return e.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;")}
