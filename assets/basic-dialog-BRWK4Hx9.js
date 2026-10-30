import{i as d}from"./bootstrap-BweT237-.js";import{r,a as c,b as l,c as s,d as m,e as g,f as u,g as p}from"./component-page-Ds0nsHTG.js";const b="Basic dialog",y="ready",f="P0",h="https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=50723-10929",v="Los diálogos básicos interrumpen la tarea para pedir confirmación o advertir de una pérdida de datos. Los usamos al guardar documentos en Wallet (hipótesis H7), al eliminar entradas, al descartar cambios o al salir de un formulario sin guardar. En Compose combinamos `AlertDialog` de Material 3 con `MOTTextButton` en las acciones.",E=[{id:"icon-false",label:"Icon=False",usage:"Confirmación neutra — cap 7 Wallet «Confirmar guardado» (Figma resume copy; Compose puede incluir Switch plan)."},{id:"icon-true",label:"Icon=True",usage:"Riesgo o pérdida — caps 11 eliminar, 12 descartar (glyph error)."}],C=["surfaceContainerHigh","onSurface / onSurfaceVariant","error (icon + confirm destructivo)","Corner/Extra-large (28dp)","mot/padding/dialog/container (24)","mot/spacing/dialog/title-body-gap (12)","mot/spacing/dialog/section-gap (16)","mot/spacing/dialog/actions-gap (8)"],T=["Instancia `Basic dialog` en design-file; properties TEXT del set.","Icono + texto en errores; confirm destructivo con color `error` en label.","Wallet caps 7/11/12 alineados a `WalletConfirmDialog` / `WalletDeleteEntryDialog` / `WalletDiscardDialog`.","`BackHandler` + diálogo descartable solo con cambios sin guardar."],B=["No BODY con TEXT suelto encima del dialog en Figma.","No mezclar `Corner/Medium` de cards — dialog = Extra-large.","No añadir Switch suelto en cap Figma si no existe en la variante del set.","No bloquear dismiss sin copy claro (Seguir editando / Cancelar)."],D=["Foco atrapado en dialog; dismiss con back donde aplique.","Headline leído antes que supporting text.","Botón destructivo anunciado con consecuencia en supporting text.","Icon decorativo: `contentDescription = null` si el título ya comunica el riesgo."],x=`AlertDialog(
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
)`,S=[{cap:"7 · confirmar",figma:"492:3332",icon:!1,ctas:"Cancelar / Guardar"},{cap:"11 · eliminar",figma:"492:3372",icon:!0,ctas:"Cancelar / Eliminar"},{cap:"12 · descartar",figma:"492:3414",icon:!0,ctas:"Seguir editando / Descartar"}],I=[{date:"2026-10-29",note:"Fase B.3: Extra-large + mot/padding|spacing/dialog/*; Wallet caps instanciados."},{date:"2026-10-30",note:"Playground modal con Escape y scrim; copy ampliado."}],a={name:b,status:y,priority:f,figma:h,purpose:v,variants:E,tokens:C,dos:T,donts:B,a11y:D,compose:x,walletCaps:S,changelog:I};d("components",{componentId:"basic-dialog"});document.getElementById("component-header").innerHTML=r(a,"Abrir Basic dialog en Figma");const M=(a.walletCaps||[]).map(e=>`<tr><td>${e.cap}</td><td><code>${e.figma}</code></td><td>${e.icon?"Sí":"No"}</td><td>${e.ctas}</td></tr>`).join("");document.getElementById("component-main").innerHTML=`
  ${c({title:"Playground",hint:"Abre el diálogo de eliminación, prueba Cancelar o Eliminar y cierra con Escape o clic fuera del panel.",canvasHtml:`
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
      <tbody>${M}</tbody>
    </table>
  </section>

  ${l(a.variants)}
  ${s(a.tokens)}
  ${m(a)}
  ${g(a.a11y)}
  ${u(a.compose)}
  ${p(a.changelog)}
`;const o=document.getElementById("dialog-overlay"),k=document.getElementById("dialog-open"),i=document.getElementById("dialog-cancel"),$=document.getElementById("dialog-confirm");let t=null;function w(){t=document.activeElement,o.hidden=!1,i.focus()}function n(e=!0){o.hidden=!0,e&&t instanceof HTMLElement&&t.focus()}k.addEventListener("click",w);i.addEventListener("click",()=>n());$.addEventListener("click",()=>n());o.addEventListener("click",e=>{e.target===o&&n()});document.addEventListener("keydown",e=>{!o.hidden&&e.key==="Escape"&&(e.preventDefault(),n())});
