import{i as o}from"./bootstrap-CkAT-PS0.js";import{m as t}from"./decisions-Ij7LKPhR.js";import{r as i,a as n,o as s,b as r,c as d,d as l,e as c,f as m,g as p}from"./component-page-DlvNXZvT.js";const g="Date & time pickers",u="ready",h="P0",f="https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61340-8809",M="Entrada de fechas y horas en formularios de viaje, Wallet y plan. El producto usa composables propios en `ui/components/date/` (campos Outlined + modales M3 adaptados), no los sets M3 de catálogo archivados en Figma. El rango de viaje permite teclado `dd/MM/yyyy` y calendario acotado; Wallet usa fecha única y hora 24 h.",D=["Campo rango — 2× OutlinedTextField editables + MOTIconButton calendario → MotDateRangePickerDialog","Campo fecha — OutlinedTextField read-only + trailing calendario → MotDatePickerDialog","Campo hora — OutlinedTextField read-only + trailing schedule → MotTimePickerDialog","Modal rango — DatePickerDialog + cabecera supporting/headline + MotBoundedMonthCalendar (Days · Year · Month)","Modal fecha — DatePickerDialog + DatePicker M3 (sin mode toggle)","Modal hora — Dialog + Surface extraLarge + TimeInput / TimePicker + acciones MOTTextButton"],y=[{id:"mot-date-range-field",label:"MotDateRangeField",usage:"Crear y editar viaje — fechas inicio/fin."},{id:"mot-date-range-dialog",label:"MotDateRangePickerDialog · Surface",usage:"Days (mes con flechas), Year, Month — selección rango."},{id:"mot-date-field",label:"MotDateField",usage:"Wallet — fecha del documento."},{id:"mot-date-dialog",label:"MotDatePickerDialog",usage:"Picker día único desde MotDateField."},{id:"mot-time-field",label:"MotTimeField",usage:"Wallet y plan — hora 24 h."},{id:"mot-time-dialog",label:"MotTimePickerDialog",usage:"Teclado horario por defecto; reloj opcional."}],b=["surfaceContainerHigh (modales)","onSurface / onSurfaceVariant","primary (selección calendario)","mot/spacing/component-sm (gap fila campos)","mot/spacing/screen-horizontal (margen H diálogos)","mot/spacing/layout-md","mot/spacing/component-xs","mot/spacing/date-picker/day-grid-gap (2)","Corner/Extra-large (modales)"],T={links:[{label:"Material Design 3 · Date pickers",url:"https://m3.material.io/components/date-pickers/overview"},{label:"Material Design 3 · Time pickers",url:"https://m3.material.io/components/time-pickers/overview"}],consulted:"2026-11-18"},k=["Ofrece entrada por teclado cuando el usuario escribe fechas con frecuencia (rango de viaje).","Limita el rango seleccionable para evitar fechas absurdas.","Muestra errores con borde, supporting text e icono si aplica — nunca solo color.","En hora, usa formato 24 h coherente con el mercado español.","Cierra el modal con Cancelar sin mutar el valor, y confirma solo con Aceptar activo."],C=["No uses el kit M3 «Input date picker» / «Modal date picker» del ARCHIVE en design-file.","No sustituyas el calendario de un mes por DateRangePicker de lista vertical.","No enlaces tokens kit-only (`header-gap`, `field-top`, `label-gap`, `time-input/*`) en láminas PRODUCT.","No documentes MotTripDatePickerDialog como patrón de producto hasta que una pantalla lo use."],v={dos:["Instanciar los 6 COMPONENT_SET PRODUCT en Figma (`61340:8809` … `61348:29338`).","Design-file: Crear/editar viaje → MotDateRangeField; Wallet manual → MotDateField + MotTimeField.","Contrato vigente: audits/figma-date-product-contract-2026-11-03.md; smoke 2026-11-18 en parity-log.","Espaciado entre controles de fila = `MOTSpacing.componentSm`."],donts:["No dibujar campos con frames sueltos: Text field Outlined + icon slot.","No promover Keyboard picker `52949:28053` como SoT de producto.","No ampliar MOTSpacing.DatePicker.* de kit sin acuerdo explícito."]},F=["Campos read-only: contentDescription con label + valor formateado.","Iconos trailing: descripción «Abrir calendario» / «Abrir selector de hora».","Errores de parseo anunciados en supporting text al perder foco.","Modales: dismiss con back; Aceptar deshabilitado hasta rango completo (fin sin elegir)."],P=`MotDateRangeField(
    startDate = state.start,
    endDate = state.end,
    onRangeChange = viewModel::onDatesChange,
    modifier = Modifier.fillMaxWidth(),
)

MotDateField(
    date = state.documentDate,
    onDateChange = viewModel::onDateChange,
    label = "Fecha del documento",
)

MotTimeField(
    time = state.time,
    onTimeChange = viewModel::onTimeChange,
)`,R=[{screen:"CreateTripScreen / EditTripScreen",fields:"MotDateRangeField"},{screen:"WalletFormScreen",fields:"MotDateField · MotTimeField"},{screen:"AddPlanActivitySheet / UpdateDocumentPlanSheet",fields:"MotTimeField / MotTimeTextField"}],x=[{compose:"MotDateRangeField",figma:"61340:8809",node:"61340-8809"},{compose:"MotDateRangePickerDialog",figma:"61348:28897",node:"61348-28897"},{compose:"MotDateField",figma:"61348:29097",node:"61348-29097"},{compose:"MotDatePickerDialog",figma:"61348:29373",node:"61348-29373"},{compose:"MotTimeField",figma:"61348:29337",node:"61348-29337"},{compose:"MotTimePickerDialog",figma:"61348:29338",node:"61348-29338"}],S=[{name:"Input date picker",id:"51954:18540"},{name:"Modal date picker",id:"51954:18136"},{name:"Keyboard picker set",id:"52949:28053"}],O={source:"Contrato figma-date-product-contract-2026-11-03.md · Compose `ui/components/date/*`",why:"Compose define interacción real (rango editable, un mes, hora 24 h); Figma es espejo PRODUCT.",layer:"M3 directo (OutlinedTextField, DatePickerDialog, TimeInput) + wrappers Mot* en app"},w=[{date:"2026-11-18",note:"Ficha showcase R0: 6 PRODUCT + tabla ARCHIVE; estado Ready tras smoke parity-log."}],e={name:g,status:u,priority:h,figma:f,purpose:M,anatomy:D,variants:y,tokens:b,guidelines:T,dos:k,donts:C,productRules:v,a11y:F,compose:P,productExamples:R,productSets:x,archiveSets:S,decisions:O,changelog:w};o("components",{componentId:"date-time-pickers"});t(e);document.getElementById("component-header").innerHTML=i(e,"Abrir MotDateRangeField en Figma");const E="https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=",$=(e.productSets||[]).map(a=>`<tr><td><code>${a.compose}</code></td><td><a href="${E}${a.node}" target="_blank" rel="noopener">${a.figma}</a></td></tr>`).join(""),A=(e.archiveSets||[]).map(a=>`<tr><td>${a.name}</td><td><code>${a.id}</code></td><td>ARCHIVE · no design-file</td></tr>`).join(""),I=(e.productExamples||[]).map(a=>`<tr><td><code>${a.screen}</code></td><td>${a.fields}</td></tr>`).join("");document.getElementById("component-main").innerHTML=`
  ${n({title:"Playground",hint:"Demostración estática del patrón rango: dos entradas + icono calendario. Los modales reales viven en Compose y en los sets Figma PRODUCT.",canvasHtml:`
      <div class="demo-stack">
        <p class="demo-label">MotDateRangeField (ilustración)</p>
        <div class="demo-row" style="display:flex;gap:8px;flex-wrap:wrap;align-items:flex-end">
          <label class="mot-field" style="flex:1;min-width:8rem">
            <span class="mot-field__label">Fecha inicio</span>
            <input class="mot-field__input" type="text" value="01/06/2026" readonly />
          </label>
          <label class="mot-field" style="flex:1;min-width:8rem">
            <span class="mot-field__label">Fecha fin</span>
            <input class="mot-field__input" type="text" value="08/06/2026" readonly />
          </label>
          <button type="button" class="btn btn--icon" aria-label="Abrir calendario" title="Abrir calendario">📅</button>
        </div>
      </div>
    `})}

  ${s(e.anatomy)}
  ${r(e.variants,"Patrones PRODUCT")}

  <section class="section">
    <h2 id="figma-compose">Figma ↔ Compose</h2>
    <table>
      <thead><tr><th>Composable</th><th>Figma PRODUCT</th></tr></thead>
      <tbody>${$}</tbody>
    </table>
  </section>

  <section class="section">
    <h2 id="archive">ARCHIVE (no instanciar en design-file)</h2>
    <table>
      <thead><tr><th>Set</th><th>Node</th><th>Nota</th></tr></thead>
      <tbody>${A}</tbody>
    </table>
  </section>

  <section class="section">
    <h2 id="screens">Dónde aparece en la app</h2>
    <table>
      <thead><tr><th>Pantalla</th><th>Composables</th></tr></thead>
      <tbody>${I}</tbody>
    </table>
  </section>

  ${d(e.tokens)}
  ${l(e)}
  ${c(e.a11y)}
  ${m(e.compose)}
  ${p(e.changelog)}
`;
