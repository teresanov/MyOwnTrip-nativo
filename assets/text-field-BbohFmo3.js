import{i as t}from"./bootstrap-BweT237-.js";import{r as n,a as r,h as l,b as s,c as i,d,e as c,f as m,g as p}from"./component-page-Ds0nsHTG.js";const u="Text field",g="ready",b="P0",f="https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=52798-24373",y="Los campos de texto recogen la información que el usuario escribe en formularios: nombre del viaje, importes, títulos de documentos y notas. En pantallas nuevas usamos siempre la variante Outlined de Material 3 (`OutlinedTextField`), enlazada al tema de la app sin wrapper MOT adicional.",h=["Container — Outlined box, `Corner/Extra-small` (4dp) ↔ `MaterialTheme.shapes.extraSmall`","Label — Inter, `bodySmall` flotante","Input text — Inter, `bodyLarge`","Supporting text — error o hint bajo el campo (`bodySmall` + `error` si aplica)","Leading / trailing icon — opcional en set Figma; Compose los usa puntualmente (p. ej. búsqueda)"],x=[{id:"outlined-enabled",label:"Outlined · Enabled",usage:"Estado por defecto en pantallas de formulario."},{id:"outlined-focus",label:"Outlined · Focused",usage:"Borde y label en `primary`; state layer M3 en runtime."},{id:"outlined-error",label:"Outlined · Error",usage:"Validación inline: borde + supporting text + icono de error (nunca solo color)."},{id:"outlined-disabled",label:"Outlined · Disabled",usage:"Opacidad 38% contenido / 12% contenedor; p. ej. campos bloqueados tras guardar viaje."}],E=["outline / outlineVariant","primary (focus)","error / onErrorContainer","onSurface / onSurfaceVariant","Corner/Extra-small (4dp)","mot/padding/text-field/horizontal (16)","mot/padding/text-field/vertical (4)","mot/spacing/component-xs"],v=["Declara un campo por dato y dale ancho completo en columnas de formulario (`Modifier.fillMaxWidth()`).","Comunica errores con `isError`, texto de apoyo y el icono que aporta Material cuando corresponda — nunca solo color.","Centraliza labels y mensajes en los strings de producto (CreateTrip, Wallet, Expenses).","Respeta `MaterialTheme.shapes.extraSmall` (4 dp), alineado a `Corner/Extra-small` en Figma."],T=["No añadas TextField Filled en flujos nuevos salvo que documentes un gap explícito.","No inventes tokens por estado (`hover`, `pressed`) ni primitivos legacy en UI.","No uses Fraunces en label ni en el texto del input.","No simules campos con rectángulos sueltos en Figma: instancia el set Outlined."],C=["Label asociado al campo (TalkBack lee label + valor + error).","Supporting text de error en `liveRegion` cuando cambia tras submit.","Teclado: `ImeAction` coherente (Next / Done).","Contraste label/input ≥ 4.5:1 sobre surface."],M=`OutlinedTextField(
    value = state.name,
    onValueChange = viewModel::onNameChange,
    label = { Text("Nombre del viaje") },
    isError = state.nameError != null,
    supportingText = state.nameError?.let { { Text(it) } },
    modifier = Modifier.fillMaxWidth(),
)`,S=[{screen:"CreateTripScreen",fields:"Nombre del viaje, Destino"},{screen:"WalletFormScreen",fields:"Título, notas, metadatos adjunto"},{screen:"ExpenseFormScreen",fields:"Importe, concepto, categoría"}],F=[{date:"2026-10-29",note:"Fase B.3: set Outlined → Corner/Extra-small + mot/padding/text-field/*; 0 LEGACY."},{date:"2026-10-30",note:"Playground editable + toggle de error; copy orientado a diseño."}],e={name:u,status:g,priority:b,figma:f,purpose:y,anatomy:h,variants:x,tokens:E,dos:v,donts:T,a11y:C,compose:M,productExamples:S,changelog:F};t("components",{componentId:"text-field"});document.getElementById("component-header").innerHTML=n(e,"Abrir Text field en Figma");const O=(e.productExamples||[]).map(o=>`<tr><td><code>${o.screen}</code></td><td>${o.fields}</td></tr>`).join("");document.getElementById("component-main").innerHTML=`
  ${r({title:"Playground",hint:"Escribe en los campos y activa el error de validación para ver borde, label y texto de apoyo como en un formulario real.",canvasHtml:`
      <div class="demo-stack">
        <label class="mot-field">
          <span class="mot-field__label">Nombre del viaje</span>
          <input class="mot-field__input" id="demo-trip-name" type="text" value="Barcelona 2026" autocomplete="off" />
        </label>
        <label class="mot-field" id="demo-dest-field">
          <span class="mot-field__label">Destino</span>
          <input class="mot-field__input" id="demo-dest-input" type="text" placeholder="Ciudad o región" autocomplete="off" />
          <span class="mot-field__support" id="demo-dest-error" hidden>El destino es obligatorio</span>
        </label>
      </div>
    `,controlsHtml:`
      <label class="playground-check">
        <input type="checkbox" id="demo-show-error" />
        Mostrar error en «Destino»
      </label>
    `})}

  ${l(e.anatomy)}
  ${s(e.variants,"Estados")}

  <section class="section">
    <h2>Dónde aparece en la app</h2>
    <table>
      <thead><tr><th>Pantalla</th><th>Campos</th></tr></thead>
      <tbody>${O}</tbody>
    </table>
  </section>

  ${i(e.tokens)}
  ${d(e)}
  ${c(e.a11y)}
  ${m(e.compose)}
  ${p(e.changelog)}
`;const j=document.getElementById("demo-dest-field"),I=document.getElementById("demo-dest-error"),a=document.getElementById("demo-show-error");function L(){const o=a.checked;j.classList.toggle("mot-field--error",o),I.hidden=!o}a.addEventListener("change",L);
