import{i as r}from"./bootstrap-DeDV_W93.js";import{m as c,r as s,a as d,b as l,c as m,d as u,e as g,f as p,g as b}from"./component-page-DipGpmBb.js";const f="Basic dialog",y="ready",h="P0",v="https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=50723-10929",E="Los diálogos básicos interrumpen la tarea para pedir confirmación o advertir de una pérdida de datos. Los usamos al guardar documentos en Wallet (hipótesis H7), al eliminar entradas, al descartar cambios o al salir de un formulario sin guardar. En Compose combinamos `AlertDialog` de Material 3 con `MOTTextButton` en las acciones.",C=[{id:"icon-false",label:"Icon=False",usage:"Confirmación neutra — cap 7 Wallet «Confirmar guardado» (Figma resume copy; Compose puede incluir Switch plan)."},{id:"icon-true",label:"Icon=True",usage:"Riesgo o pérdida — caps 11 eliminar, 12 descartar (glyph error)."}],T=["surfaceContainerHigh","onSurface / onSurfaceVariant","error (icon + confirm destructivo)","Corner/Extra-large (28dp)","mot/padding/dialog/container (24)","mot/spacing/dialog/divider-gap (12)","mot/spacing/dialog/section-gap (16)","mot/spacing/dialog/actions-gap (8)"],D={label:"Material Design 3 · Dialogs",url:"https://m3.material.io/components/dialogs/guidelines",consulted:"2026-11-10"},B=["Úsalo con moderación, para avisos que bloquean el funcionamiento normal o información crítica que requiere una decisión, una tarea o una confirmación.","Titular breve y claro: una afirmación o pregunta concreta que explique qué implica la petición.","Como máximo dos acciones: si hay una sola, debe ser una confirmación de lectura; si hay dos, una confirma y la otra descarta.","Alinea los botones al borde final del diálogo: la acción de confirmar, más cerca del borde, y la de descartar a su izquierda.","Deshabilita la acción de confirmar hasta que se haya hecho una elección; las acciones de descartar nunca se deshabilitan.","Muestra dos botones de texto, uno junto a otro."],x=["No uses un diálogo para información de prioridad baja o media: usa un snackbar.","No empieces el titular con disculpas («Perdona la interrupción»), alarma («¡Atención!») ni ambigüedad («¿Estás seguro?»).","No coloques las acciones de descarte a la derecha de las de confirmación.","Evita opciones poco claras: «Cancelar» no tiene sentido si no se propone una acción concreta.","No añadas una tercera acción como «Más información»: saca al usuario del diálogo y deja la tarea sin terminar."],M={dos:["Instancia `Basic dialog` en design-file; properties TEXT del set.","Icono + texto en errores; confirm destructivo con color `error` en label.","Wallet caps 7/11/12 alineados a `WalletConfirmDialog` / `WalletDeleteEntryDialog` / `WalletDiscardDialog`.","`BackHandler` + diálogo descartable solo con cambios sin guardar."],donts:["No BODY con TEXT suelto encima del dialog en Figma.","No mezclar `Corner/Medium` de cards — dialog = Extra-large.","No añadir Switch suelto en cap Figma si no existe en la variante del set.","No bloquear dismiss sin copy claro (Seguir editando / Cancelar)."]},I=["Foco atrapado en dialog; dismiss con back donde aplique.","Headline leído antes que supporting text.","Botón destructivo anunciado con consecuencia en supporting text.","Icon decorativo: `contentDescription = null` si el título ya comunica el riesgo."],S=`AlertDialog(
    onDismissRequest = onDismiss,
    icon = {
        Icon(Icons.Default.Error, contentDescription = null,
            tint = MaterialTheme.colorScheme.error)
    },
    title = { Text("¿Eliminar documento?") },
    text = { Text("Se borrará «$entryTitle»…") },
    confirmButton = {
        MOTTextButton(onClick = onConfirmDelete) {
            Text("Eliminar", color = MaterialTheme.colorScheme.error)
        }
    },
    dismissButton = {
        MOTTextButton(onClick = onDismiss) { Text("Cancelar") }
    },
)`,q=[{cap:"7 · confirmar",figma:"492:3332",icon:!1,ctas:"Cancelar / Guardar"},{cap:"11 · eliminar",figma:"492:3372",icon:!0,ctas:"Cancelar / Eliminar"},{cap:"12 · descartar",figma:"492:3414",icon:!0,ctas:"Seguir editando / Descartar"}],k={source:"Figma node 50723-10929 · Compose `AlertDialog` (M3)",why:"Interrumpir la tarea solo para confirmar o advertir de pérdida de datos: guardar en Wallet (H7), eliminar, descartar cambios.",layer:"M3 `AlertDialog` con `MOTTextButton` en las acciones"},$=[{date:"2026-10-29",note:"Fase B.3: Extra-large + mot/padding|spacing/dialog/*; Wallet caps instanciados."},{date:"2026-10-30",note:"Playground modal con Escape y scrim; copy ampliado."}],a={name:f,status:y,priority:h,figma:v,purpose:E,variants:C,tokens:T,guidelines:D,dos:B,donts:x,productRules:M,a11y:I,compose:S,walletCaps:q,decisions:k,changelog:$};r("components",{componentId:"basic-dialog"});c(a);document.getElementById("component-header").innerHTML=s(a,"Abrir Basic dialog en Figma");const w=(a.walletCaps||[]).map(e=>`<tr><td>${e.cap}</td><td><code>${e.figma}</code></td><td>${e.icon?"Sí":"No"}</td><td>${e.ctas}</td></tr>`).join("");document.getElementById("component-main").innerHTML=`
  ${d({title:"Playground",hint:"Abre el diálogo de eliminación, prueba Cancelar o Eliminar y cierra con Escape o clic fuera del panel.",canvasHtml:`
      <button type="button" class="btn btn--filled" id="dialog-open">Mostrar diálogo de ejemplo</button>
    `})}

  <div class="dialog-overlay" id="dialog-overlay" hidden>
    <div class="dialog-demo" role="alertdialog" aria-modal="true" aria-labelledby="dialog-demo-title" aria-describedby="dialog-demo-body">
      <div class="dialog-demo__icon" aria-hidden="true">!</div>
      <h3 class="dialog-demo__title" id="dialog-demo-title">¿Eliminar documento?</h3>
      <p class="dialog-demo__body" id="dialog-demo-body">Se borrará el documento y su archivo del dispositivo. Esta acción no se puede deshacer.</p>
      <div class="dialog-demo__actions">
        <button type="button" class="btn btn--text" id="dialog-cancel">Cancelar</button>
        <button type="button" class="btn btn--text dialog-demo__destructive" id="dialog-confirm">Eliminar</button>
      </div>
    </div>
  </div>

  <section class="section">
    <h2>Wallet en Figma</h2>
    <p class="section-lead">Capturas del design-file que ya instancian este set (no sustituyen la preview de Compose).</p>
    <table>
      <thead><tr><th>Cap</th><th>Node</th><th>Icono</th><th>Acciones</th></tr></thead>
      <tbody>${w}</tbody>
    </table>
  </section>

  ${l(a.variants)}
  ${m(a.tokens)}
  ${u(a)}
  ${g(a.a11y)}
  ${p(a.compose)}
  ${b(a.changelog)}
`;const o=document.getElementById("dialog-overlay"),A=document.getElementById("dialog-open"),t=document.getElementById("dialog-cancel"),L=document.getElementById("dialog-confirm");let i=null;function _(){i=document.activeElement,o.hidden=!1,t.focus()}function n(e=!0){o.hidden=!0,e&&i instanceof HTMLElement&&i.focus()}A.addEventListener("click",_);t.addEventListener("click",()=>n());L.addEventListener("click",()=>n());o.addEventListener("click",e=>{e.target===o&&n()});document.addEventListener("keydown",e=>{!o.hidden&&e.key==="Escape"&&(e.preventDefault(),n())});
