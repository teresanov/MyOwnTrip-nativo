import { initShowcasePage, setupToc } from "../lib/bootstrap.js";
import { mountFoundation } from "../lib/foundation-page.js";
import data from "../data/foundations/shape.json";

initShowcasePage("shape");
mountFoundation(data);

const main = document.getElementById("foundation-main");

/* ---------- Ejemplo 1: cada rol en el componente que lo consume ---------- */

const roleSectionEl = main?.querySelector("#roles")?.closest(".section");

roleSectionEl?.insertAdjacentHTML(
  "beforeend",
  `<div class="sd-catalog">
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
  </p>`
);

const chipDemo = roleSectionEl?.querySelector(".sd-chip-demo");
chipDemo?.querySelectorAll(".filter-chip").forEach((chip) => {
  chip.addEventListener("click", () => {
    chipDemo.querySelectorAll(".filter-chip").forEach((other) => {
      other.classList.remove("filter-chip--selected");
      other.setAttribute("aria-pressed", "false");
    });
    chip.classList.add("filter-chip--selected");
    chip.setAttribute("aria-pressed", "true");
  });
});

roleSectionEl?.querySelectorAll(".home-filter-menu__item").forEach((item) => {
  item.addEventListener("click", () => {
    const group = item.dataset.group;
    roleSectionEl.querySelectorAll(`.home-filter-menu__item[data-group="${group}"]`).forEach((other) => {
      const on = other === item;
      other.setAttribute("aria-pressed", on ? "true" : "false");
      other.querySelector(".home-filter-menu__check").textContent = on ? "✓" : "";
    });
  });
});

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
