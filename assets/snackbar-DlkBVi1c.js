import{i}from"./bootstrap-C_ZQ7Ieu.js";import{m as l}from"./decisions-Ij7LKPhR.js";import{r as d,a as u,i as m,b as p,c as b,d as g,e as k,f as h,g as v}from"./component-page-Cq4C7yJC.js";const f="Snackbar",y="ready",S="P0",H="https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=53977-33575",x="Las snackbars confirman que una acción se completó o ofrecen una vía rápida para deshacerla, sin sacar al usuario de la pantalla. Aparecen en Day Hub, Diario, Gastos y Documentos cuando guardamos o revertimos cambios menores. En Compose van dentro de un `SnackbarHost` en el `Scaffold`, usando el componente `Snackbar` de Material 3.",w=["Container — Inverse surface, `Corner/Extra-small` (4dp)","Message — `bodyMedium`, inverse on surface","Action — `labelLarge`, inverse primary (opcional)","Close — icon button inverse (variantes con Show close=True)"],E=[{id:"text-only",label:"Text only",usage:"Ack simple sin acción."},{id:"text-action",label:"Text + action",usage:"Deshacer o reintentar."},{id:"two-lines",label:"Two lines",usage:"Obligatoria cuando el mensaje ocupa dos líneas (p. ej. Wallet cap 1i «archivado» + Deshacer). No forzar One line y dejar que el texto se corte o desborde."}],T=["inverseSurface / inverseOnSurface / inversePrimary","Corner/Extra-small (4dp)","mot/padding/snackbar/horizontal (16)","mot/padding/snackbar/vertical (12)","mot/spacing/snackbar/gap (12)","mot/spacing/snackbar/gap-sm · gap-xs"],M={label:"Material Design 3 · Snackbar",url:"https://m3.material.io/components/snackbar/guidelines",consulted:"2026-11-10"},D=["Úsalo para mensajes de baja prioridad que no requieren acción: informar de un proceso que la app ha hecho o hará.","Muestra un solo snackbar a la vez; uno con información actualizada puede reemplazar al anterior.","Texto breve, de una línea si es posible; en móvil, hasta dos líneas.","Incluye como máximo una acción, con botón de texto; ofrece «Deshacer» para que se pueda corregir la elección.","Los snackbars sin acción desaparecen solos (4–10 s según la plataforma); los que tienen acción permanecen hasta que se actúa o se descartan.","Colócalo abajo, delante del contenido principal y por encima de un FAB."],C=["No lo uses para información crítica o que exige acción inmediata: ahí va un diálogo.","No añadas iconos; si el mensaje necesita un icono, usa otro componente, como un diálogo.","No uses texto con estilos ni enlaces en línea: si hace falta un enlace, usa un botón.","No uses botones Filled ni Elevated en un snackbar: atraen demasiada atención.","No lo coloques delante de la navegación, de elementos de uso frecuente ni delante o detrás de un FAB.","No apiles snackbars ni los pongas uno junto a otro.","No hagas del snackbar la única vía para acceder a un caso de uso esencial."],N={dos:["Un snackbar a la vez; duración corta M3.","Acción opcional con verbo claro (Deshacer, Ver).","Copy de dos líneas → variante Figma `# of lines=Two lines` (cap 1i). One line solo si el mensaje cabe en una.","Mismo inverse scheme Light/Dark vía colorScheme."],donts:["No usar snackbar para errores críticos — preferir dialog o inline error con icono.","No sustituir confirmaciones H7 Wallet.","No tokens snackbar/hover.","No usar One line si el texto del preview/cap es de dos líneas."]},j=["`SnackbarHostState` anuncia mensaje; acción focusable.","No depender solo del color inverse para significado.","Alternativa por tap si el gesto de dismiss no es obvio."],A=`val snackbarHostState = remember { SnackbarHostState() }
// …
Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
) { /* … */ }
// scope.launch { snackbarHostState.showSnackbar("Gasto guardado") }`,I={source:"Figma node 53977-33575 · Compose `Snackbar` (M3)",why:"Confirmar una acción completada u ofrecer deshacer sin sacar al usuario de la pantalla.",layer:"M3 directo dentro de `SnackbarHost` del `Scaffold`"},L=[{date:"2026-10-29",note:"Fase B.3: Inverse + Extra-small + mot/padding|spacing/snackbar/*; gap longer-action fix."},{date:"2026-10-30",note:"Playground con acción Deshacer y auto-ocultado."},{date:"2026-11-11",note:"Two lines si el copy ocupa dos líneas (cap 1i); no apañar con One line."}],a={name:f,status:y,priority:S,figma:H,purpose:x,anatomy:w,variants:E,tokens:T,guidelines:M,dos:D,donts:C,productRules:N,a11y:j,compose:A,decisions:I,changelog:L};i("components",{componentId:"snackbar"});l(a);document.getElementById("component-header").innerHTML=d(a,"Abrir Snackbar en Figma");document.getElementById("component-main").innerHTML=`
  ${u({title:"Playground",hint:"Simula el feedback tras guardar un gasto. El mensaje aparece abajo, se puede deshacer y se oculta solo al cabo de unos segundos.",canvasHtml:`
      <button type="button" class="btn btn--filled" id="snackbar-trigger">Simular «Gasto guardado»</button>
    `,controlsHtml:`
      <label class="playground-check">
        <input type="checkbox" id="snackbar-with-action" checked />
        Incluir acción «Deshacer»
      </label>
    `})}

  <div class="snackbar-host" id="snackbar-host" hidden aria-live="polite"></div>

  ${m(a.anatomy)}
  ${p(a.variants,"Variantes del set")}
  ${b(a.tokens)}
  ${g(a)}
  ${k(a.a11y)}
  ${h(a.compose)}
  ${v(a.changelog)}
`;const o=document.getElementById("snackbar-host"),t=document.getElementById("snackbar-trigger"),q=document.getElementById("snackbar-with-action");let n=null;function e(){o.hidden=!0,o.innerHTML="",n&&(clearTimeout(n),n=null)}function B(){var s,c;e();const r=q.checked?'<button type="button" class="snackbar-demo__action" id="snackbar-undo">Deshacer</button>':"";o.innerHTML=`
    <div class="snackbar-demo">
      <span class="snackbar-demo__text">Gasto guardado</span>
      ${r}
      <button type="button" class="snackbar-demo__close" id="snackbar-close" aria-label="Cerrar">×</button>
    </div>
  `,o.hidden=!1,(s=document.getElementById("snackbar-close"))==null||s.addEventListener("click",e),(c=document.getElementById("snackbar-undo"))==null||c.addEventListener("click",()=>{e(),t.focus()}),n=setTimeout(e,4500)}t.addEventListener("click",B);
