/**
 * Índice ancleado de la página. Lee los <h2 id> que ya existen en el contenido,
 * así que no hay que mantener una lista aparte: si añades una sección, aparece.
 */
export function mountToc(containerId = "page-toc", scopeId = "contenido") {
  const nav = document.getElementById(containerId);
  const scope = document.getElementById(scopeId);
  if (!nav || !scope) return;

  const headings = [...scope.querySelectorAll("h2[id]")];
  if (headings.length < 2) {
    nav.hidden = true;
    return;
  }

  nav.innerHTML = `
    <p class="toc__title">En esta página</p>
    <ul class="toc__list">
      ${headings
        .map((h) => `<li><a href="#${h.id}" data-toc="${h.id}">${h.dataset.tocLabel || h.textContent.trim()}</a></li>`)
        .join("")}
    </ul>
  `;

  const links = new Map([...nav.querySelectorAll("[data-toc]")].map((a) => [a.dataset.toc, a]));

  const setActive = (id) => {
    links.forEach((a, key) => a.classList.toggle("is-active", key === id));
  };

  // Marca la última sección cuyo encabezado ha pasado por la parte alta del viewport.
  const observer = new IntersectionObserver(
    (entries) => {
      const visible = entries
        .filter((e) => e.isIntersecting)
        .sort((a, b) => a.boundingClientRect.top - b.boundingClientRect.top);
      if (visible.length) setActive(visible[0].target.id);
    },
    { rootMargin: "-80px 0px -70% 0px", threshold: 0 }
  );

  headings.forEach((h) => observer.observe(h));
  setActive(headings[0].id);
}
