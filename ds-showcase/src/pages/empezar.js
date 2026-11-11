import { initShowcasePage, setupToc } from "../lib/bootstrap.js";
import { assetUrl } from "../lib/paths.js";

initShowcasePage("empezar");

document.getElementById("page-header").innerHTML = `
  <p class="page-header__eyebrow">Empezar aquí</p>
  <h1>Cómo se usa este sistema</h1>
  <p class="page-header__lead">
    Dos recorridos: uno para diseñar una pantalla y otro para implementarla.
    Si solo vas a leer una sección de una ficha, lee <strong>Uso</strong>.
  </p>
`;

document.getElementById("main").innerHTML = `
  <section class="section">
    <h2 id="disenar">Diseñar una pantalla</h2>
    <ol class="list-steps">
      <li>
        <strong>Busca el componente</strong> en <a href="${assetUrl("components.html")}">Componentes</a>.
        El índice está agrupado por para qué sirve, no por orden alfabético.
      </li>
      <li>
        <strong>Lee «Uso» antes de mirar el ejemplo.</strong> Ahí está cuándo usarlo,
        cuándo no, y a qué otro componente ir si este no encaja.
      </li>
      <li>
        <strong>Usa la instancia de la librería de Figma.</strong> Las pantallas no inventan UI:
        ensamblan instancias del DS más <code>MaterialTheme</code> y el copy del design-file.
      </li>
      <li>
        <strong>Si no existe el componente</strong>, mira los criterios en
        <a href="${assetUrl("gobernanza.html")}">Gobernanza</a> antes de crear uno nuevo.
      </li>
    </ol>
    <div class="callout">
      El estado de cada ficha dice cuánto puedes fiarte de ella.
      <strong>Listo</strong> significa Figma publicado, ficha completa y Compose alineado.
      <strong>Borrador</strong> significa que existe en algún sitio pero nadie lo ha documentado.
    </div>
  </section>

  <section class="section">
    <h2 id="implementar">Implementar una pantalla</h2>
    <ol class="list-steps">
      <li>
        <strong>Consume roles, no valores.</strong> <code>MaterialTheme.colorScheme</code> y
        <code>MaterialTheme.typography</code>. Nunca un hex ni un dp suelto en una pantalla.
      </li>
      <li>
        <strong>Comprueba si hay wrapper.</strong> La sección «Implementación» de cada ficha
        dice el composable exacto. Los botones son <code>MOTButton</code> y familia, no los de M3:
        el wrapper es lo que aplica el morph de forma.
      </li>
      <li>
        <strong>Los estados se pintan en runtime</strong> con state layers (8 / 10 / 10 / 16 %),
        nunca con un color guardado por estado.
      </li>
      <li>
        <strong>Antes de cerrar</strong>, pasa los gates de
        <a href="${assetUrl("gobernanza.html")}">Gobernanza</a>.
      </li>
    </ol>
    <div class="callout">
      El catálogo Compose (módulo <code>:catalog</code>) ejecuta los componentes reales en un
      dispositivo o emulador. Para comprobar comportamiento —morph, squish, escala de fuente—
      es más fiable que esta web, que lo reproduce en CSS.
    </div>
  </section>

  <section class="section">
    <h2 id="capas">Qué documenta cada capa</h2>
    <p class="section-lead">
      Cada cosa vive en un solo sitio. Si encuentras lo mismo en dos capas, una de las dos está obsoleta.
    </p>
    <table>
      <thead><tr><th>Capa</th><th>Qué contiene</th><th>Qué no</th></tr></thead>
      <tbody>
        <tr>
          <td><strong>Esta web</strong></td>
          <td>Fichas de componente, foundations, patrones, reglas</td>
          <td>Implementación completa; el código manda</td>
        </tr>
        <tr>
          <td><strong>Figma</strong></td>
          <td>Librería: variables y component sets para diseñar</td>
          <td>Documentación: ni láminas, ni Do/Don't, ni changelog</td>
        </tr>
        <tr>
          <td><strong>Código</strong></td>
          <td>Comportamiento real, parámetros, tokens del tema</td>
          <td>Criterios de uso y jerarquía</td>
        </tr>
        <tr>
          <td><strong><code>:catalog</code></strong></td>
          <td>Los componentes vivos, interactivos, en Android</td>
          <td>Texto explicativo largo</td>
        </tr>
      </tbody>
    </table>
  </section>

  <section class="section">
    <h2 id="no-hacemos">Lo que este sistema no hace</h2>
    <ul class="list-dos list-dos--dont">
      <li>No hay capa semántica propia de color: los roles son los de M3.</li>
      <li>No hay tokens por estado (<code>primary.hover</code>, <code>text/disabled</code>).</li>
      <li>No se documentan componentes dentro de Figma.</li>
      <li>No se portan contratos del repo archivo (<code>SemanticColors</code>, <code>LayoutTokens</code>).</li>
      <li>No hay un segundo set de iconos de UI.</li>
    </ul>
  </section>
`;

setupToc();
