import{i}from"./bootstrap-BweT237-.js";import{r as d,a as l,h as m,b as u,c as b,d as g,e as p,f as k,g as h}from"./component-page-Ds0nsHTG.js";const v="Snackbar",y="ready",S="P0",f="https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=53977-33575",H="Las snackbars confirman que una acción se completó o ofrecen una vía rápida para deshacerla, sin sacar al usuario de la pantalla. Aparecen en Day Hub, Diario, Gastos y Documentos cuando guardamos o revertimos cambios menores. En Compose van dentro de un `SnackbarHost` en el `Scaffold`, usando el componente `Snackbar` de Material 3.",E=["Container — Inverse surface, `Corner/Extra-small` (4dp)","Message — `bodyMedium`, inverse on surface","Action — `labelLarge`, inverse primary (opcional)","Close — icon button inverse (variantes con Show close=True)"],T=[{id:"text-only",label:"Text only",usage:"Ack simple sin acción."},{id:"text-action",label:"Text + action",usage:"Deshacer o reintentar."},{id:"two-lines",label:"Two lines",usage:"Mensaje largo; gap `mot/spacing/snackbar/gap` (12)."}],w=["inverseSurface / inverseOnSurface / inversePrimary","Corner/Extra-small (4dp)","mot/padding/snackbar/horizontal (16)","mot/padding/snackbar/vertical (12)","mot/spacing/snackbar/gap (12)","mot/spacing/snackbar/gap-sm · gap-xs"],M=["Un snackbar a la vez; duración corta M3.","Acción opcional con verbo claro (Deshacer, Ver).","Mismo inverse scheme Light/Dark vía colorScheme."],x=["No usar snackbar para errores críticos — preferir dialog o inline error con icono.","No sustituir confirmaciones H7 Wallet.","No tokens snackbar/hover."],I=["`SnackbarHostState` anuncia mensaje; acción focusable.","No depender solo del color inverse para significado.","Alternativa por tap si el gesto de dismiss no es obvio."],L=`val snackbarHostState = remember { SnackbarHostState() }
// …
Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
) { /* … */ }
// scope.launch { snackbarHostState.showSnackbar("Gasto guardado") }`,A=[{date:"2026-10-29",note:"Fase B.3: Inverse + Extra-small + mot/padding|spacing/snackbar/*; gap longer-action fix."},{date:"2026-10-30",note:"Playground con acción Deshacer y auto-ocultado."}],a={name:v,status:y,priority:S,figma:f,purpose:H,anatomy:E,variants:T,tokens:w,dos:M,donts:x,a11y:I,compose:L,changelog:A};i("components",{componentId:"snackbar"});document.getElementById("component-header").innerHTML=d(a,"Abrir Snackbar en Figma");document.getElementById("component-main").innerHTML=`
  ${l({title:"Playground",hint:"Simula el feedback tras guardar un gasto. El mensaje aparece abajo, se puede deshacer y se oculta solo al cabo de unos segundos.",canvasHtml:`
      <button type="button" class="btn btn--filled" id="snackbar-trigger">Simular «Gasto guardado»</button>
    `,controlsHtml:`
      <label class="playground-check">
        <input type="checkbox" id="snackbar-with-action" checked />
        Incluir acción «Deshacer»
      </label>
    `})}

  <div class="snackbar-host" id="snackbar-host" hidden aria-live="polite"></div>

  ${m(a.anatomy)}
  ${u(a.variants,"Variantes del set")}
  ${b(a.tokens)}
  ${g(a)}
  ${p(a.a11y)}
  ${k(a.compose)}
  ${h(a.changelog)}
`;const o=document.getElementById("snackbar-host"),r=document.getElementById("snackbar-trigger"),D=document.getElementById("snackbar-with-action");let n=null;function e(){o.hidden=!0,o.innerHTML="",n&&(clearTimeout(n),n=null)}function C(){var s,t;e();const c=D.checked?'<button type="button" class="snackbar-demo__action" id="snackbar-undo">Deshacer</button>':"";o.innerHTML=`
    <div class="snackbar-demo">
      <span class="snackbar-demo__text">Gasto guardado</span>
      ${c}
      <button type="button" class="snackbar-demo__close" id="snackbar-close" aria-label="Cerrar">×</button>
    </div>
  `,o.hidden=!1,(s=document.getElementById("snackbar-close"))==null||s.addEventListener("click",e),(t=document.getElementById("snackbar-undo"))==null||t.addEventListener("click",()=>{e(),r.focus()}),n=setTimeout(e,4500)}r.addEventListener("click",C);
