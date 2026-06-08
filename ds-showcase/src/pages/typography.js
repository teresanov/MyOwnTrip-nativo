import { initShowcasePage } from "../lib/bootstrap.js";
import tokens from "../data/tokens.json";

initShowcasePage("typography");

document.getElementById("typography-main").innerHTML = `
  <section class="section card">
    <p class="type-sample--display">Cuaderno del viaje</p>
    <p><strong>Display</strong> · ${tokens.typography.display} · solo titulares hero</p>
  </section>
  <section class="section card">
    <p class="type-sample--headline">Itinerario del día 3</p>
    <p><strong>Headline</strong> · ${tokens.typography.display}</p>
  </section>
  <section class="section card">
    <p class="type-sample--title">Añadir gasto</p>
    <p><strong>Title</strong> · ${tokens.typography.ui} · UI y secciones</p>
  </section>
  <section class="section card">
    <p class="type-sample--body">Registra el importe en pocos taps. Los gastos se guardan en local aunque no haya red.</p>
    <p><strong>Body</strong> · ${tokens.typography.ui}</p>
  </section>
  <section class="section">
    <h2>Reglas</h2>
    <ul class="list-dos">
      <li>Serif solo Display y Headline.</li>
      <li>Botones, campos y chips: siempre Inter.</li>
      <li>En app: fuentes bundled en <code>res/font/</code>.</li>
    </ul>
  </section>
`;
