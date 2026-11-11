import{i as t}from"./bootstrap-DeDV_W93.js";import{m as n,r,a as l,h as s,b as i,c as d,d as c,e as m,f as p,g as u}from"./component-page-DipGpmBb.js";const g="Text field",b="ready",f="P0",x="https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=52798-24373",y="Los campos de texto recogen la información que el usuario escribe en formularios: nombre del viaje, importes, títulos de documentos y notas. En pantallas nuevas usamos siempre la variante Outlined de Material 3 (`OutlinedTextField`), enlazada al tema de la app sin wrapper MOT adicional.",h=["Container — Outlined box, `Corner/Extra-small` (4dp) ↔ `MaterialTheme.shapes.extraSmall`","Label — Inter, `bodySmall` flotante","Input text — Inter, `bodyLarge`","Supporting text — error o hint bajo el campo (`bodySmall` + `error` si aplica)","Leading / trailing icon — opcional en set Figma; Compose los usa puntualmente (p. ej. búsqueda)"],v=[{id:"outlined-enabled",label:"Outlined · Enabled",usage:"Estado por defecto en pantallas de formulario."},{id:"outlined-focus",label:"Outlined · Focused",usage:"Borde y label en `primary`; state layer M3 en runtime."},{id:"outlined-error",label:"Outlined · Error",usage:"Validación inline: borde + supporting text + icono de error (nunca solo color)."},{id:"outlined-disabled",label:"Outlined · Disabled",usage:"Opacidad 38% contenido / 12% contenedor; p. ej. campos bloqueados tras guardar viaje."}],E=["outline / outlineVariant","primary (focus)","error / onErrorContainer","onSurface / onSurfaceVariant","Corner/Extra-small (4dp)","mot/padding/text-field/horizontal (16)","mot/padding/text-field/vertical (4)","mot/spacing/component-xs"],T={links:[{label:"Material Design 3 · Text fields",url:"https://m3.material.io/components/text-fields/overview"},{label:"Material Web · Text field",url:"https://material-web.dev/components/text-field/"}],consulted:"2026-11-10"},M=["Haz que los campos parezcan interactivos.","Elige Outlined en formularios largos (su menor énfasis simplifica el layout) y Filled en diálogos y formularios cortos (llama más la atención).","Haz visible el estado del campo (vacío, con texto, error…) de un vistazo.","Mantén las etiquetas y los mensajes de error breves y fáciles de accionar.","Etiqueta cada campo —label flotante, placeholder o etiqueta externa— para que se entienda qué dato se espera.","Usa el texto de apoyo para explicar cómo se usará el dato; al validar, sustitúyelo por el texto de error.","Usa iconos para indicar el método de entrada (buscar), añadir una función (borrar) o expresar un error.","Elige el teclado y el tipo de entrada adecuados al dato (email, número, teléfono…)."],C=[],F={dos:["Declara un campo por dato y dale ancho completo en columnas de formulario (`Modifier.fillMaxWidth()`).","Comunica errores con `isError`, texto de apoyo y el icono que aporta Material cuando corresponda — nunca solo color.","Centraliza labels y mensajes en los strings de producto (CreateTrip, Wallet, Expenses).","Respeta `MaterialTheme.shapes.extraSmall` (4 dp), alineado a `Corner/Extra-small` en Figma."],donts:["No añadas TextField Filled en flujos nuevos salvo que documentes un gap explícito.","No inventes tokens por estado (`hover`, `pressed`) ni primitivos legacy en UI.","No uses Fraunces en label ni en el texto del input.","No simules campos con rectángulos sueltos en Figma: instancia el set Outlined."]},S=["Label asociado al campo (TalkBack lee label + valor + error).","Supporting text de error en `liveRegion` cuando cambia tras submit.","Teclado: `ImeAction` coherente (Next / Done).","Contraste label/input ≥ 4.5:1 sobre surface."],O=`OutlinedTextField(
    value = state.name,
    onValueChange = viewModel::onNameChange,
    label = { Text("Nombre del viaje") },
    isError = state.nameError != null,
    supportingText = state.nameError?.let { { Text(it) } },
    modifier = Modifier.fillMaxWidth(),
)`,j=[{screen:"CreateTripScreen",fields:"Nombre del viaje, Destino"},{screen:"WalletFormScreen",fields:"Título, notas, metadatos adjunto"},{screen:"ExpenseFormScreen",fields:"Importe, concepto, categoría"}],w={source:"Figma node 52798-24373 · Compose `OutlinedTextField` (M3)",why:"Recoger texto del usuario en formularios; en pantallas nuevas, Outlined de M3 enlazada al tema.",layer:"M3 directo, enlazado al tema"},I=[{date:"2026-10-29",note:"Fase B.3: set Outlined → Corner/Extra-small + mot/padding/text-field/*; 0 LEGACY."},{date:"2026-10-30",note:"Playground editable + toggle de error; copy orientado a diseño."}],e={name:g,status:b,priority:f,figma:x,purpose:y,anatomy:h,variants:v,tokens:E,guidelines:T,dos:M,donts:C,productRules:F,a11y:S,compose:O,productExamples:j,decisions:w,changelog:I};t("components",{componentId:"text-field"});n(e);document.getElementById("component-header").innerHTML=r(e,"Abrir Text field en Figma");const k=(e.productExamples||[]).map(a=>`<tr><td><code>${a.screen}</code></td><td>${a.fields}</td></tr>`).join("");document.getElementById("component-main").innerHTML=`
  ${l({title:"Playground",hint:"Escribe en los campos y activa el error de validación para ver borde, label y texto de apoyo como en un formulario real.",canvasHtml:`
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

  ${s(e.anatomy)}
  ${i(e.variants,"Estados")}

  <section class="section">
    <h2>Dónde aparece en la app</h2>
    <table>
      <thead><tr><th>Pantalla</th><th>Campos</th></tr></thead>
      <tbody>${k}</tbody>
    </table>
  </section>

  ${d(e.tokens)}
  ${c(e)}
  ${m(e.a11y)}
  ${p(e.compose)}
  ${u(e.changelog)}
`;const q=document.getElementById("demo-dest-field"),z=document.getElementById("demo-dest-error"),o=document.getElementById("demo-show-error");function L(){const a=o.checked;q.classList.toggle("mot-field--error",a),z.hidden=!a}o.addEventListener("change",L);
