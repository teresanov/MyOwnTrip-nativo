import { initShowcasePage, setupToc } from "../lib/bootstrap.js";
import site from "../data/site.json";
import componentsManifest from "../data/components.json";

initShowcasePage("governance");

const counts = componentsManifest.components.reduce((acc, c) => {
  acc[c.status] = (acc[c.status] || 0) + 1;
  return acc;
}, {});

const STATUS_ORDER = ["ready", "in-review", "draft"];
const STATUS_NAME = {
  ready: "Listo",
  "in-review": "En revisión",
  draft: "En construcción",
  deprecated: "Obsoleto",
};

document.getElementById("page-header").innerHTML = `
  <p class="page-header__eyebrow">Gobernanza</p>
  <h1>Reglas del sistema</h1>
  <p class="page-header__lead">
    Qué significa cada estado, cuándo se puede crear un componente propio
    y qué hay que pasar antes de cerrar una pantalla.
  </p>
`;

document.getElementById("main").innerHTML = `
  <section class="section">
    <h2 id="estados">Estado de cada ficha</h2>
    <p class="section-lead">
      Esta web es un trabajo en curso. Cada ficha lleva una etiqueta que dice en qué punto está,
      con el mismo criterio para todas:
    </p>
    <table>
      <thead><tr><th>Estado</th><th>Criterio</th><th>Hoy</th></tr></thead>
      <tbody>
        ${STATUS_ORDER.map(
          (s) => `
          <tr>
            <td><span class="chip chip--${s === "in-review" ? "review" : s}">${STATUS_NAME[s]}</span></td>
            <td>${site.statusLabels[s].split("—")[1]?.trim() || site.statusLabels[s]}</td>
            <td>${counts[s] || 0}</td>
          </tr>`
        ).join("")}
      </tbody>
    </table>
    <p class="section-lead" style="margin-top:16px">
      El ejemplo, la anatomía y las medidas se hacen sobre el componente real de la app, compilado para la web:
      lo que se ve en la ficha es lo que se ve en el móvil. Si una ficha todavía enseña una recreación en HTML,
      lo dice debajo del ejemplo.
    </p>
    <p class="section-lead">
      Un componente nuevo no se da por cerrado sin su ficha en esta web.
    </p>
  </section>

  <section class="section">
    <h2 id="wrapper">Cuándo crear un componente propio</h2>
    <p class="section-lead">
      Por defecto se usa el componente de Material 3 tal cual, con el tema de la app. Un componente propio
      (los que empiezan por <code>MOT</code> o <code>Mot</code>) solo se justifica si cumple uno de estos criterios
      <strong>y</strong> queda documentado en esta web.
    </p>
    <table>
      <thead><tr><th>Criterio</th><th>Ejemplo válido</th><th>Ejemplo inválido</th></tr></thead>
      <tbody>
        <tr>
          <td>Una configuración de la app que se repite en tres pantallas o más</td>
          <td>La fila de documento de Wallet (<code>MotListItem</code>): icono en círculo, aviso del plan y disponibilidad sin conexión</td>
          <td>Copiar el botón del proyecto antiguo</td>
        </tr>
        <tr>
          <td>Un comportamiento que Material 3 no ofrece</td>
          <td><code>MOTButton</code>: aplica el morph de forma del ADR 004</td>
          <td>Renombrar el botón de Material 3 sin cambiar nada</td>
        </tr>
        <tr>
          <td>Una composición que Material 3 no trae</td>
          <td>La portada del viaje en Home (<code>TripHeroCard</code>)</td>
          <td>Un componente que esconde los colores del tema tras parámetros propios</td>
        </tr>
      </tbody>
    </table>
    <div class="guideline-grid" style="margin-top:24px">
      <div class="guideline-col guideline-col--do">
        <h3 class="subsection-title">Cómo es por dentro</h3>
        <ul class="list-dos">
          <li>Usa el componente de Material 3 y le añade solo lo que justifica su existencia.</li>
          <li>Ofrece los mismos parámetros que Material 3: <code>modifier</code>, <code>enabled</code>, <code>colors</code> del tema.</li>
        </ul>
      </div>
      <div class="guideline-col guideline-col--dont">
        <h3 class="subsection-title">Lo que no hace</h3>
        <ul class="list-dos list-dos--dont">
          <li>No recupera variantes del sistema antiguo.</li>
          <li>No oculta los roles del tema tras parámetros propios.</li>
        </ul>
      </div>
    </div>
  </section>

  <section class="section">
    <h2 id="gate-m3">Reglas que bloquean el cierre de una pantalla</h2>
    <p class="section-lead">
      Antes de dar por terminada una pantalla, en Figma o en Compose, se revisan estas seis reglas.
      Son <strong>bloqueantes</strong>: si una no se cumple, la pantalla no se cierra hasta corregirla.
      Se revisan antes que todo lo demás porque las seis salen de usar Material 3 como es, y saltarse
      una rompe el tema oscuro, la accesibilidad o la coherencia entre Figma y la app.
    </p>
    <table>
      <thead><tr><th>Regla</th><th>Qué significa</th><th>Por qué</th></tr></thead>
      <tbody>
        <tr>
          <td>Sin colores por estado</td>
          <td>No existen variables como «Button Hover» o «Button Press».</td>
          <td>El color de cada estado lo calcula Material 3; una variable por estado se queda vieja en cuanto cambia el tema.</td>
        </tr>
        <tr>
          <td>Los estados son una capa encima</td>
          <td>En los estados Hover, Focus, Press y Drag aparece sobre el componente un velo del color de su contenido al 8, 10, 10 y 16 % de opacidad. Deshabilitado: el contenido al 38 % y el contenedor al 12 %.</td>
          <td>Así funciona igual en tema claro y oscuro sin añadir colores.</td>
        </tr>
        <tr>
          <td>Solo roles del tema</td>
          <td>Las pantallas usan roles (<code>primary</code>, <code>surface</code>, <code>onSurfaceVariant</code>…), nunca un código de color ni un tono suelto de la paleta.</td>
          <td>El rol cambia solo con el tema; un color fijo no.</td>
        </tr>
        <tr>
          <td><code>tertiary</code> no es <code>error</code></td>
          <td><code>tertiary</code> es el ocre de la marca; <code>error</code> es el rojo de los avisos. No se intercambian.</td>
          <td>Un acento de marca no puede leerse como un fallo, ni un fallo pasar por decoración.</td>
        </tr>
        <tr>
          <td>Errores con icono y texto</td>
          <td>Un error nunca se comunica solo con color.</td>
          <td>Quien no distingue bien los colores también tiene que entenderlo.</td>
        </tr>
        <tr>
          <td>Fraunces solo en títulos</td>
          <td>Fraunces en los estilos Display y Headline; Inter de Title hacia abajo, y siempre en botones, campos y chips.</td>
          <td>La voz editorial va en los títulos; el resto tiene que leerse rápido.</td>
        </tr>
      </tbody>
    </table>
  </section>

  <section class="section">
    <h2 id="gates">Otras comprobaciones</h2>
    <p class="section-lead">No bloquean por sí solas, pero cada una tiene su momento.</p>
    <table>
      <thead><tr><th>Comprobación</th><th>Qué se mira</th><th>Cuándo</th></tr></thead>
      <tbody>
        <tr><td>Pantallas sin colores sueltos</td><td>Ninguna pantalla de la app usa colores fuera del tema.</td><td>En cada cambio de pantalla</td></tr>
        <tr><td>Variables de Figma enlazadas</td><td>Cada variable de la librería apunta a un rol de Material 3; no hay variables por estado ni páginas de documentación en Figma.</td><td>Al cambiar la librería</td></tr>
        <tr><td>Ficha en esta web</td><td>Todo componente nuevo o modificado tiene su ficha aquí.</td><td>En cada componente</td></tr>
        <tr><td>Accesibilidad</td><td>Lector de pantalla, contraste, área táctil de 48 dp y texto ampliado al 200 %.</td><td>En cada pantalla</td></tr>
        <tr><td>Comparación visual</td><td>Esta web contra la app en el emulador, en un grupo de pantallas de referencia.</td><td>Tras un cambio visual grande</td></tr>
      </tbody>
    </table>
  </section>

  <section class="section">
    <h2 id="contexto-agente">Contexto para agentes</h2>
    <p class="section-lead">
      Las fichas de esta web se publican también en un formato que un agente puede leer,
      generado en cada build desde los mismos JSON.
    </p>
    <ul class="list-dos">
      <li><code>llms.txt</code> — índice del sistema y enlaces, según la convención emergente.</li>
      <li><code>ds-context.md</code> — uso, composables y prohibiciones de cada componente, en texto plano.</li>
    </ul>
    <div class="callout">
      El objetivo es que las reglas de <code>.cursor/rules/</code> dejen de mantenerse a mano
      y apunten a este archivo. Hoy hay reglas con valores desfasados respecto al código.
    </div>
  </section>
`;

setupToc();
