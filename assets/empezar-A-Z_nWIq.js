import{i as a,b as e,d as o}from"./bootstrap-Bil26Efv.js";a("empezar");document.getElementById("page-header").innerHTML=`
  <p class="page-header__eyebrow">Empezar aquí</p>
  <h1>Cómo se usa este sistema</h1>
  <p class="page-header__lead">
    Dos recorridos: uno para diseñar una pantalla y otro para implementarla.
    Si solo vas a leer una sección de una ficha, lee <strong>Uso</strong>.
  </p>
`;document.getElementById("main").innerHTML=`
  <section class="section">
    <h2 id="disenar">Diseñar una pantalla</h2>
    <ol class="list-steps">
      <li>
        <strong>Busca el componente</strong> en <a href="${e("components.html")}">Componentes</a>.
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
        <a href="${e("gobernanza.html")}">Gobernanza</a> antes de crear uno nuevo.
      </li>
    </ol>
    <div class="callout">
      El estado de cada ficha dice cuánto puedes fiarte de ella.
      <strong>Listo</strong>: Figma publicado, ficha completa y Compose alineado.
      <strong>En revisión</strong>: hay ficha, pero falta validar o migrar al esquema Usage-first.
      <strong>Borrador</strong>: existe en Figma o en código y aún no hay ficha en el índice.
    </div>
  </section>

  <section class="section">
    <h2 id="implementar">Implementar una pantalla</h2>
    <p class="section-lead">
      Una pantalla se construye con componentes del sistema y con las variables del tema. El diseño en Figma
      y el código en Compose siguen la misma regla: cada elemento se define por el papel que cumple en la pantalla,
      no por el valor que tiene hoy.
    </p>

    <ol class="list-steps">
      <li>
        <strong>Variables semánticas, nunca primitivos.</strong>
        Los colores, tipografías y espacios del sistema se organizan por el <em>rol</em> que desempeñan:
        fondo, superficie, acción principal, texto secundario, título medio. El <em>primitivo</em> es el valor en
        bruto que hay detrás en cada momento (un hex como <code>#4A5864</code>, un tamaño como 14 sp, una medida
        como 24 dp) y no se usa directamente en ningún sitio.
        <br>La regla se aplica en dos momentos, siempre al definir cada elemento. En Figma, al asignar la
        variable de color o de texto al elemento. En código, al escribir el elemento, que declara su rol y deja que
        el tema lo resuelva. Así un mismo rol cambia de valor con el modo oscuro, y un cambio de color en el
        sistema llega a toda la app sin tocar ninguna pantalla.
        <br>Ejemplos:
      </li>
    </ol>
    <table>
      <thead><tr><th>Elemento</th><th>Rol (correcto)</th><th>Primitivo (incorrecto)</th></tr></thead>
      <tbody>
        <tr>
          <td>Fondo de un botón principal</td>
          <td><code>colorScheme.primary</code></td>
          <td><code>#4A5864</code></td>
        </tr>
        <tr>
          <td>Texto sobre ese botón</td>
          <td><code>colorScheme.onPrimary</code></td>
          <td><code>#FFFFFF</code></td>
        </tr>
        <tr>
          <td>Título de una tarjeta</td>
          <td><code>typography.titleMedium</code></td>
          <td>Inter 16 sp, Medium</td>
        </tr>
        <tr>
          <td>Espacio interno de un botón</td>
          <td><code>MOTSpacing.ButtonSmall</code></td>
          <td>24 dp × 8 dp</td>
        </tr>
      </tbody>
    </table>

    <ol class="list-steps" start="2">
      <li>
        <strong>Componente del sistema, con su wrapper.</strong>
        Un <em>wrapper</em> es un componente propio que envuelve al de Material 3 y le añade lo que distingue a
        MyOwnTrip. Los botones son un caso: <code>MOTButton</code> y su familia envuelven al botón de Material y
        le dan la forma del sistema, rectangular en reposo y redondeada a 20 dp al interactuar. Usar el botón
        de Material sin el wrapper funciona, pero pierde esa forma. La sección «Implementación» de cada ficha
        indica el componente exacto que debe usarse.
      </li>
      <li>
        <strong>Los estados cambian la opacidad, no el color del componente.</strong>
        Hover, foco, pulsado y arrastrado no tienen un color propio. El componente conserva el color de su rol y,
        encima, se superpone una capa del color del contenido (el rol <code>on*</code>) con una opacidad que depende
        del estado: 8 % al pasar por encima, 10 % con foco, 10 % al pulsar y 16 % al arrastrar, según Material 3.
        Un componente desactivado baja la opacidad del contenido al 38 % y la del contenedor al 12 %. Por eso el
        sistema no incluye variables como <code>primary.hover</code> o <code>texto/desactivado</code>.
        <br>La técnica es la misma en las dos herramientas, pero quien la aplica es distinto:
        <ul>
          <li>
            <strong>En Figma</strong>, la colección de variables «State Layers» tiene una variable por rol y por
            opacidad, nunca por estado. Por ejemplo, <code>State Layers / On Secondary Container / Opacity-08</code>
            es el color del contenido de un botón tonal ya con un 8 % de opacidad; existen también las versiones
            Opacity-10 y Opacity-16. Cada variante de estado del componente (Enabled, Hovered, Focused, Pressed,
            Disabled) tiene una capa «State-layer» cuyo relleno se enlaza a una de esas variables: en un botón tonal,
            Hovered usa Opacity-08 y Focused usa Opacity-10, y Enabled no lleva relleno. El color base del contenedor
            no cambia entre variantes.
          </li>
          <li>
            <strong>En código</strong>, el componente aplica esa misma capa automáticamente cuando detecta la
            interacción (el cursor encima, el foco, el pulsado), con el mismo color y las mismas opacidades. No hay
            variantes de estado que mantener: el estado se calcula al ejecutarse la app.
          </li>
        </ul>
      </li>
      <li>
        <strong>Gates antes de cerrar.</strong>
        Un <em>gate</em> es una comprobación que debe cumplirse para aceptar un cambio: que no haya colores ni
        tamaños escritos a mano, que el componente usado exista en la librería de Figma, que la ficha esté
        al día. Los gates están descritos en <a href="${e("gobernanza.html")}">Gobernanza</a>.
      </li>
    </ol>

    <div class="callout">
      Esta web reproduce el comportamiento (morph, compresión al pulsar) en CSS para poder verlo sin instalar
      nada, pero es una aproximación. El comportamiento exacto se comprueba en las previews de Android Studio,
      que dibujan el componente real.
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
          <td><strong>Previews de Android Studio</strong></td>
          <td>El componente real dibujado, con datos de ejemplo</td>
          <td>Texto explicativo largo</td>
        </tr>
      </tbody>
    </table>
  </section>
`;o();
