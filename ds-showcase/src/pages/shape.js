import { initShowcasePage, setupToc } from "../lib/bootstrap.js";
import { mountFoundation } from "../lib/foundation-page.js";
import data from "../data/foundations/shape.json";

initShowcasePage("shape");
mountFoundation(data);

// Demo del morph: los mismos tiempos que AppMotion, aproximados en CSS.
const demo = document.getElementById("foundation-main");
if (demo) {
  const target = demo.querySelector("#morph")?.closest(".section");
  target?.insertAdjacentHTML(
    "beforeend",
    `<div class="playground__frame" style="margin-top:20px">
       <div class="playground__canvas">
         <div class="btn-stage">
           <button type="button" class="mot-btn mot-btn--filled">Pulsa y mantén</button>
         </div>
         <p class="playground__status">0 dp en reposo · 20 dp mientras lo tocas</p>
       </div>
     </div>`
  );
}

setupToc();
