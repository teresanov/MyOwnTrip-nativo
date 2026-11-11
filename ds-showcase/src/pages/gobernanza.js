import { initShowcasePage, setupToc } from "../lib/bootstrap.js";
import site from "../data/site.json";
import componentsManifest from "../data/components.json";

initShowcasePage("governance");

const counts = componentsManifest.components.reduce((acc, c) => {
  acc[c.status] = (acc[c.status] || 0) + 1;
  return acc;
}, {});

const STATUS_ORDER = ["ready", "in-review", "draft", "deprecated"];
const STATUS_NAME = {
  ready: "Listo",
  "in-review": "En revisión",
  draft: "Borrador",
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
    <h2 id="estados">Estados de publicación</h2>
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
      Cerrar un componente nuevo sin ficha en esta web está <strong>bloqueado</strong>.
    </p>
  </section>

  <section class="section">
    <h2 id="wrapper">Cuándo crear un componente propio</h2>
    <p class="section-lead">
      Por defecto se usa <code>androidx.compose.material3.*</code>. Un wrapper <code>MOT*</code> o
      <code>MyOwnTrip*</code> solo se justifica si se cumple un criterio <strong>y</strong> queda documentado.
    </p>
    <table>
      <thead><tr><th>Criterio</th><th>Ejemplo válido</th><th>Ejemplo inválido</th></tr></thead>
      <tbody>
        <tr>
          <td>Un default de producto repetido en 3 pantallas o más</td>
          <td>Chip de categoría con glifo propio sobre un rol M3</td>
          <td>Copiar el <code>Button</code> del repo archivo</td>
        </tr>
        <tr>
          <td>Comportamiento que M3 no ofrece</td>
          <td><code>MOTButton</code>: aplica el morph de forma del ADR 004</td>
          <td>Renombrar <code>Button</code> sin cambiar nada</td>
        </tr>
        <tr>
          <td>Composición no cubierta por M3</td>
          <td>Tarjeta de entrada Wallet con layout editorial fijo</td>
          <td>Un wrapper que esconde <code>colorScheme</code></td>
        </tr>
      </tbody>
    </table>
    <div class="guideline-grid" style="margin-top:24px">
      <div class="guideline-col guideline-col--do">
        <h3 class="subsection-title">La API del wrapper</h3>
        <ul class="list-dos">
          <li>Delega en el componente M3 subyacente.</li>
          <li>Expone parámetros alineados a M3: <code>modifier</code>, <code>enabled</code>, <code>colors</code> del tema.</li>
        </ul>
      </div>
      <div class="guideline-col guideline-col--dont">
        <h3 class="subsection-title">Lo que no hace</h3>
        <ul class="list-dos list-dos--dont">
          <li>No reintroduce ejes del DS archivado (<code>MyOwnTripButtonVariant</code>).</li>
          <li>No oculta los roles del tema tras parámetros propios.</li>
        </ul>
      </div>
    </div>
  </section>

  <section class="section">
    <h2 id="gate-m3">Gate bloqueante: M3 canónico</h2>
    <p class="section-lead">Se pasa antes que cualquier otro. Si falla uno, la pantalla no se cierra.</p>
    <ol class="list-steps list-steps--compact">
      <li>Prohibidos los tokens de color por estado (<code>hover</code>, <code>pressed</code>, <code>focus</code>, <code>disabled</code>).</li>
      <li>Los estados son state layers en runtime: 8 / 10 / 10 / 16 %; disabled 38 % y 12 %.</li>
      <li>La UI solo usa roles de <code>colorScheme</code>; nunca primitivos ni <code>AppColors</code> en pantallas.</li>
      <li><code>tertiary</code> (acento de marca) y <code>error</code> (validación) son rojos distintos a propósito.</li>
      <li>Los errores se comunican con icono y texto, nunca solo con color.</li>
      <li>Fraunces en Display y Headline; Inter de Title hacia abajo.</li>
    </ol>
  </section>

  <section class="section">
    <h2 id="gates">Gates complementarias</h2>
    <table>
      <thead><tr><th>Gate</th><th>Criterio</th><th>Cuándo</th></tr></thead>
      <tbody>
        <tr><td><code>semanticTokenOnlyConsumption</code></td><td>Sin <code>AppColors</code> en <code>ui/features/</code></td><td>Cada PR de UI</td></tr>
        <tr><td><code>figmaAliasChainValid</code></td><td>Las variables aliasan a roles M3; sin tokens por estado; sin páginas de doc en Figma</td><td>Cambio en la librería</td></tr>
        <tr><td><code>showcaseDocComplete</code></td><td>El componente nuevo o modificado tiene ficha aquí</td><td>Cada componente</td></tr>
        <tr><td><code>a11yCheck</code></td><td>TalkBack, contraste, área táctil 48 dp, escalado al 200 %</td><td>Cada pantalla</td></tr>
        <tr><td><code>visualRegression</code></td><td>Esta web contra el emulador, subconjunto MVP</td><td>Tras un cambio visual grande</td></tr>
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
