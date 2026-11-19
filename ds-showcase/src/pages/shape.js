import { initShowcasePage, setupToc } from "../lib/bootstrap.js";
import { mountFoundation } from "../lib/foundation-page.js";
import data from "../data/foundations/shape.json";

initShowcasePage("shape");
mountFoundation(data);

const strip = (html) => html.replace(/<[^>]+>/g, "").replace(/&lt;/g, "<").replace(/&gt;/g, ">");
const main = document.getElementById("foundation-main");

/* ---------- Ejemplo 1: los cinco roles de superficie, cada uno en su componente típico ---------- */

const rolesSection = data.sections.find((s) => s.id === "roles");
const ROLES = rolesSection.table.rows.map(([figma, dp, compose, uso]) => ({
  figma: strip(figma),
  dp: Number(dp),
  compose: strip(compose),
  uso: strip(uso),
}));

/** Maqueta mínima del componente que usa cada rol (1 dp = 1,5 px de dibujo). */
const MOCKS = {
  "Corner/Extra-small": () =>
    `<div class="sd-mock sd-field"><span class="sd-field__label">Destino</span><span>Lisboa</span></div>`,
  "Corner/Small": () =>
    `<div class="sd-chips"><div class="sd-mock sd-chip sd-chip--on">✓ Vuelos</div><div class="sd-mock sd-chip">Hoteles</div></div>`,
  "Corner/Medium": () =>
    `<div class="sd-mock sd-card"><div class="sd-card__img"></div><div class="sd-card__body"><strong>Alfama</strong><span>3 noches · 14 mar</span></div></div>`,
  "Corner/Large": () =>
    `<div class="sd-mock sd-menu"><span>Ordenar por</span><span class="sd-menu__on">✓ Fecha</span><span>Precio</span></div>`,
  "Corner/Extra-large": () =>
    `<div class="sd-mock sd-dialog"><strong>¿Eliminar viaje?</strong><span>Se borrarán sus documentos.</span><div class="sd-dialog__actions"><span>Cancelar</span><span>Eliminar</span></div></div>`,
};

main?.querySelector("#roles")?.closest(".section")?.insertAdjacentHTML(
  "beforeend",
  `<div class="sd-scale" role="list">
     <div class="sd-scale__head" aria-hidden="true">
       <span>Rol</span><span>Radio de la esquina</span><span>En un componente</span>
     </div>
     ${ROLES.map(
       (r) => `
       <div class="sd-row" role="listitem" style="--r:${r.dp}">
         <div class="sd-row__name">
           <strong>${r.figma.replace("Corner/", "")}</strong>
           <span class="sd-row__dp">${r.dp} dp</span>
           <code>${r.figma}</code>
           <code>${r.compose}</code>
         </div>
         <div class="sd-row__sample">
           <div class="sd-sample"><span class="sd-guide" aria-hidden="true"></span></div>
         </div>
         <div class="sd-row__mock">
           ${(MOCKS[r.figma] || (() => ""))()}
           <span class="sd-row__use">${r.uso}</span>
         </div>
       </div>`
     ).join("")}
   </div>
   <p class="section-lead" style="margin-top:12px">Ejemplo en vivo. La columna central compara los cinco radios sobre la misma pieza; el círculo punteado es el radio de la esquina. La columna derecha muestra el componente que usa cada rol. Todo se dibuja a 1,5 px por dp. Los botones del diálogo no van redondeados porque su forma es el morph, no un rol.</p>`
);

/* ---------- Ejemplo 2: morph del botón con lectura del radio en tiempo real ---------- */

const morphSection = main?.querySelector("#morph")?.closest(".section");
morphSection?.insertAdjacentHTML(
  "beforeend",
  `<div class="playground__frame" style="margin-top:20px">
     <div class="playground__canvas">
       <div class="btn-stage">
         <button type="button" class="mot-btn mot-btn--filled" id="morph-btn">Pulsa y mantén</button>
       </div>
       <p class="playground__status">Radio actual: <strong id="morph-dp">0 dp</strong> · 0 dp en reposo, 20 dp al pasar por encima, enfocar o pulsar</p>
     </div>
   </div>
   <p class="section-lead" style="margin-top:8px">Ejemplo en vivo. Es una reproducción en CSS de los tiempos reales: el muelle de salida se aproxima con una curva.</p>`
);

const btn = document.getElementById("morph-btn");
const dpOut = document.getElementById("morph-dp");
if (btn && dpOut) {
  const read = () => {
    const px = parseFloat(getComputedStyle(btn).borderTopLeftRadius) || 0;
    dpOut.textContent = `${Math.round(px)} dp`;
    requestAnimationFrame(read);
  };
  requestAnimationFrame(read);
}

setupToc();
