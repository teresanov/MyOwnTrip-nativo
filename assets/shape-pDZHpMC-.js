import{i as m,d as u}from"./bootstrap-Bpr3y4NI.js";import{m as h}from"./foundation-page-DE6ZXlIT.js";const _="Forma",f="Foundations",b="Las superficies usan los roles de forma de M3. Los botones son la excepción: tienen un morph propio que vive fuera de esa escala.",g="designsystem/.../ui/theme/Shapes.kt → MOTThemeShapes · Figma: colección Shape (Corner/*)",v=[{id:"como-funciona",title:"Cómo funciona la forma",paragraphs:["La forma es el radio de las esquinas de un elemento. El sistema no deja que cada pantalla elija un radio: define una escala de cinco <strong>roles</strong>, de Extra-small a Extra-large, y cada elemento pide el que le corresponde. Los valores en dp existen en un solo sitio: el objeto <code>MOTThemeShapes</code> en Compose y la colección Shape (<code>Corner/*</code>) en Figma. Cambiar un rol ahí cambia todos los elementos que lo usan.","Los roles se aplican a <strong>superficies</strong>: cualquier contenedor con fondo propio, como tarjetas, chips, campos de texto, menús, diálogos y hojas inferiores. Una superficie tiene una forma estable, que no cambia mientras se usa.","Los <strong>botones</strong> son la excepción. Su forma no es estable: es rectangular en reposo (0 dp) y se redondea a 20 dp al pasar por encima, enfocarlo o pulsarlo. Como un rol es un valor fijo y esto es un comportamiento, el morph queda fuera de la escala: en Compose lo calcula <code>rememberMOTButtonShape</code> y en Figma son dos radios, <code>Corner/None</code> y <code>Corner/Large-increased</code>."],table:{head:["Elemento","Forma","Cómo se aplica"],rows:[["Superficie de contenido: tarjeta, chip, campo, menú, diálogo","Un rol de la escala","<code>MaterialTheme.shapes.&lt;rol&gt;</code> · <code>Corner/&lt;Rol&gt;</code>"],["Control pulsable: botón, icon button","Morph de 0 a 20 dp","<code>rememberMOTButtonShape()</code> · <code>Corner/None</code> → <code>Corner/Large-increased</code>"],["FAB","Circular","Patrón de Material 3; no aplica el morph"]]}},{id:"roles",title:"Roles de superficie",lead:"UI y Figma hablan el mismo rol: en Compose <code>MaterialTheme.shapes.extraSmall</code>, en Figma <code>Corner/Extra-small</code>. Los dp solo viven dentro de <code>MOTThemeShapes</code>. El ejemplo de cada rol es el componente que lo consume, no una pieza genérica.",table:{head:["Figma","dp","Compose","Uso"],rows:[["<code>Corner/Extra-small</code>","4","<code>shapes.extraSmall</code>","Text field"],["<code>Corner/Small</code>","8","<code>shapes.small</code>","Chips, eyebrow label"],["<code>Corner/Medium</code>","12","<code>shapes.medium</code>","Cards, hero"],["<code>Corner/Large</code>","16","<code>shapes.large</code>","Superficies grandes, menú"],["<code>Corner/Extra-large</code>","28","<code>shapes.extraLarge</code>","Diálogos y sheets"]]}},{id:"morph",title:"El morph del botón",lead:"La firma visual del sistema: rectángulo en reposo, redondeado al tocar. Está <strong>fuera</strong> de <code>MaterialTheme.shapes</code> porque no es un rol de superficie, es un comportamiento.",table:{head:["Estado","dp","Figma"],rows:[["Reposo y disabled","0","<code>Corner/None</code>"],["Hover, focus, pressed, selected","20","<code>Corner/Large-increased</code>"]]},notes:["API: <code>rememberMOTButtonShape(interactionSource, forceActive)</code>.","Prohibido el camino inverso: nunca Round en reposo que vuelva a None.",'Detalle de tiempos y curvas en <a href="motion.html">Movimiento</a>; aplicación en la ficha de <a href="components/button.html">Button</a>.']},{id:"excepciones",title:"Excepciones",table:{head:["Componente","Forma","Motivo"],rows:[["FAB y Extended FAB","Circular","Patrón M3; no aplica el morph"],["Cards","12 dp fijo","Contenedor editorial, sin morph"],["Chips","8 dp fijo","Densidad; <em>selected</em> se comunica con color"],["Icon button (Square)","0 → 20 dp","Misma regla que Button"],["Toggle y Segmented","Square en reposo, Round como destino","El morph necesita geometría de destino"]]}},{id:"reglas",title:"Reglas",dos:["Consumir el rol (<code>shapes.medium</code>), no el dp.","Para botones, usar <code>rememberMOTButtonShape()</code>.","En Dev Mode, el <code>codeSyntax</code> ANDROID de cada <code>Corner/*</code> debe apuntar al rol de Compose."],donts:["No crear un segundo semántico de forma tipo <code>MOTCorner</code>.","No pasar <code>MaterialTheme.shapes.*</code> como <code>shape</code> de un botón.","No bindear un primitivo de radio directo en un frame de producto."]}],y=[{label:"docs/design-system/shape.md",ref:"docs/design-system/shape.md"},{label:"ADR 004 — Button shape morph",ref:"docs/decisions/004-button-shape-morph.md"}],C={name:_,eyebrow:f,lead:b,sourceOfTruth:g,sections:v,refs:y};m("shape");h(C);const s=document.getElementById("foundation-main");var i;const a=(i=s==null?void 0:s.querySelector("#roles"))==null?void 0:i.closest(".section");a==null||a.insertAdjacentHTML("beforeend",`<div class="sd-catalog">
    <article class="sd-role">
      <header class="sd-role__head">
        <h3 class="sd-role__title">Extra-small · 4 dp</h3>
        <p class="sd-role__codes"><code>Corner/Extra-small</code> · <code>shapes.extraSmall</code></p>
        <p class="sd-role__link">Espécimen de <a href="components/text-field.html">Text field</a> · <code>OutlinedTextField</code></p>
      </header>
      <div class="playground__frame">
        <div class="playground__canvas">
          <label class="mot-field">
            <span class="mot-field__label">Nombre del viaje</span>
            <input class="mot-field__input" type="text" value="Barcelona 2026" autocomplete="off" />
          </label>
        </div>
      </div>
    </article>

    <article class="sd-role">
      <header class="sd-role__head">
        <h3 class="sd-role__title">Small · 8 dp</h3>
        <p class="sd-role__codes"><code>Corner/Small</code> · <code>shapes.small</code></p>
        <p class="sd-role__link">Espécimen de <a href="components/filter-chip.html">Filter chip</a> · <code>FilterChip</code>. Eyebrow label usa el mismo rol; su ficha no tiene playground.</p>
      </header>
      <div class="playground__frame">
        <div class="playground__canvas">
          <div class="filter-chip-row sd-chip-demo" role="group" aria-label="Filtro de documentos">
            <button type="button" class="filter-chip filter-chip--selected" aria-pressed="true" data-value="Activos">Activos</button>
            <button type="button" class="filter-chip" aria-pressed="false" data-value="Archivados">Archivados</button>
            <button type="button" class="filter-chip" aria-pressed="false" data-value="Todos">Todos</button>
          </div>
        </div>
      </div>
    </article>

    <article class="sd-role">
      <header class="sd-role__head">
        <h3 class="sd-role__title">Medium · 12 dp</h3>
        <p class="sd-role__codes"><code>Corner/Medium</code> · <code>shapes.medium</code></p>
        <p class="sd-role__link">Espécimen de <a href="components/stacked-card.html">Stacked card</a> · <code>MotStackedCard</code>. TripHeroCard usa el mismo rol.</p>
      </header>
      <div class="playground__frame">
        <div class="playground__canvas">
          <figure class="card-demo card-demo--stacked">
            <div class="card-demo__header">
              <div>
                <strong>Sin viajes todavía</strong>
                <p>Planea tu primera aventura</p>
              </div>
            </div>
            <img src="/assets/home_empty_map.jpg" alt="Mapa con chincheta de viaje" width="360" height="200" loading="lazy" />
          </figure>
        </div>
      </div>
    </article>

    <article class="sd-role">
      <header class="sd-role__head">
        <h3 class="sd-role__title">Large · 16 dp</h3>
        <p class="sd-role__codes"><code>Corner/Large</code> · <code>shapes.large</code></p>
        <p class="sd-role__link">Espécimen de <code>HomeFilterMenuPanel</code> (Home · tune). No hay ficha de Menu; el contenedor es 16 dp. La fila seleccionada usa <code>shapes.medium</code>.</p>
      </header>
      <div class="playground__frame">
        <div class="playground__canvas">
          <div class="home-filter-menu" style="width:min(100%,328px)">
            <p class="home-filter-menu__label">Mostrar</p>
            <button type="button" class="home-filter-menu__item" data-group="phase" data-value="all" aria-pressed="true">
              <span class="home-filter-menu__check" aria-hidden="true">✓</span>
              <span>Todos los viajes</span>
            </button>
            <button type="button" class="home-filter-menu__item" data-group="phase" data-value="current" aria-pressed="false">
              <span class="home-filter-menu__check" aria-hidden="true"></span>
              <span>En curso</span>
            </button>
            <button type="button" class="home-filter-menu__item" data-group="phase" data-value="upcoming" aria-pressed="false">
              <span class="home-filter-menu__check" aria-hidden="true"></span>
              <span>Próximos</span>
            </button>
            <button type="button" class="home-filter-menu__item" data-group="phase" data-value="past" aria-pressed="false">
              <span class="home-filter-menu__check" aria-hidden="true"></span>
              <span>Pasados</span>
            </button>
            <button type="button" class="home-filter-menu__item" data-group="phase" data-value="archived" aria-pressed="false">
              <span class="home-filter-menu__check" aria-hidden="true"></span>
              <span>Archivados</span>
            </button>
            <p class="home-filter-menu__label">Ordenar</p>
            <button type="button" class="home-filter-menu__item" data-group="sort" data-value="date" aria-pressed="true">
              <span class="home-filter-menu__check" aria-hidden="true">✓</span>
              <span>Fecha — próximo primero</span>
            </button>
            <button type="button" class="home-filter-menu__item" data-group="sort" data-value="name" aria-pressed="false">
              <span class="home-filter-menu__check" aria-hidden="true"></span>
              <span>Nombre A—Z</span>
            </button>
            <button type="button" class="home-filter-menu__item" data-group="sort" data-value="dest" aria-pressed="false">
              <span class="home-filter-menu__check" aria-hidden="true"></span>
              <span>Destino A—Z</span>
            </button>
          </div>
        </div>
      </div>
    </article>

    <article class="sd-role">
      <header class="sd-role__head">
        <h3 class="sd-role__title">Extra-large · 28 dp</h3>
        <p class="sd-role__codes"><code>Corner/Extra-large</code> · <code>shapes.extraLarge</code></p>
        <p class="sd-role__link">Espécimen de <a href="components/basic-dialog.html">Basic dialog</a> · <code>AlertDialog</code>. Las acciones son <code>MOTTextButton</code> (morph, no este rol). Las sheets de Home usan el mismo radio de contenedor.</p>
      </header>
      <div class="playground__frame">
        <div class="playground__canvas">
          <div class="dialog-demo" style="max-width:320px">
            <div class="dialog-demo__icon" aria-hidden="true">!</div>
            <p class="dialog-demo__title">¿Eliminar documento?</p>
            <p class="dialog-demo__body">Se borrará el documento y su archivo del dispositivo. Esta acción no se puede deshacer.</p>
            <div class="dialog-demo__actions">
              <button type="button" class="mot-btn mot-btn--text">Cancelar</button>
              <button type="button" class="mot-btn mot-btn--text dialog-demo__destructive">Eliminar</button>
            </div>
          </div>
        </div>
      </div>
    </article>
  </div>
  <p class="section-lead" style="margin-top:12px">
    El showcase no monta Compose. Cada bloque reutiliza el HTML de la ficha (el menú, el de <code>HomeFilterMenuPanel</code>). El radio es el que ya tiene ese componente, 1 px = 1 dp.
  </p>`);const t=a==null?void 0:a.querySelector(".sd-chip-demo");t==null||t.querySelectorAll(".filter-chip").forEach(e=>{e.addEventListener("click",()=>{t.querySelectorAll(".filter-chip").forEach(o=>{o.classList.remove("filter-chip--selected"),o.setAttribute("aria-pressed","false")}),e.classList.add("filter-chip--selected"),e.setAttribute("aria-pressed","true")})});a==null||a.querySelectorAll(".home-filter-menu__item").forEach(e=>{e.addEventListener("click",()=>{const o=e.dataset.group;a.querySelectorAll(`.home-filter-menu__item[data-group="${o}"]`).forEach(d=>{const l=d===e;d.setAttribute("aria-pressed",l?"true":"false"),d.querySelector(".home-filter-menu__check").textContent=l?"✓":""})})});var p;const r=(p=s==null?void 0:s.querySelector("#morph"))==null?void 0:p.closest(".section");r==null||r.insertAdjacentHTML("beforeend",`<div class="playground__frame" style="margin-top:20px">
     <div class="playground__canvas">
       <div class="btn-stage">
         <button type="button" class="mot-btn mot-btn--filled" id="morph-btn">Pulsa y mantén</button>
       </div>
       <p class="playground__status">Radio actual: <strong id="morph-dp">0 dp</strong> · 0 dp en reposo, 20 dp al pasar por encima, enfocar o pulsar</p>
     </div>
   </div>
   <p class="section-lead" style="margin-top:8px">Ejemplo en vivo. Es una reproducción en CSS de los tiempos reales: el muelle de salida se aproxima con una curva.</p>`);const n=document.getElementById("morph-btn"),c=document.getElementById("morph-dp");if(n&&c){const e=()=>{const o=parseFloat(getComputedStyle(n).borderTopLeftRadius)||0;c.textContent=`${Math.round(o)} dp`,requestAnimationFrame(e)};requestAnimationFrame(e)}u();
