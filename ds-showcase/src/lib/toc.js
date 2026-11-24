/**
 * Agrupa el índice como en el Storybook de Lumiboard: guía / specs / código.
 * Si el h2 trae data-toc-group, manda; si no, se infiere por el id.
 */
const TOC_GROUP_BY_ID = {
  uso: "Guía",
  "do-dont": "Guía",
  reglas: "Guía",
  ejemplo: "Guía",
  decisiones: "Guía",
  relacionados: "Guía",
  disenar: "Guía",
  implementar: "Guía",
  anatomia: "Specs",
  medidas: "Specs",
  "variantes-del-set": "Specs",
  variantes: "Specs",
  comportamiento: "Specs",
  "figma-compose": "Specs",
  pantallas: "Specs",
  tokens: "Código",
  implementacion: "Código",
  changelog: "Código",
  accesibilidad: "Accesibilidad",
  a11y: "Accesibilidad",
  foundations: "Este manual",
  estado: "Este manual",
  pipeline: "Este manual",
};

function tocGroup(heading) {
  return heading.dataset.tocGroup || TOC_GROUP_BY_ID[heading.id] || "";
}

function tocItemsHtml(headings) {
  return headings
    .map(
      (h) =>
        `<li><a href="#${h.id}" data-toc="${h.id}">${h.dataset.tocLabel || h.textContent.trim()}</a></li>`
    )
    .join("");
}

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

  const groups = [];
  headings.forEach((h) => {
    const label = tocGroup(h);
    const last = groups[groups.length - 1];
    if (label && last && last.label === label) last.items.push(h);
    else if (label) groups.push({ label, items: [h] });
    else groups.push({ label: "", items: [h] });
  });

  const grouped = groups.some((g) => g.label);
  const body = grouped
    ? groups
        .map(
          (g) => `
      ${g.label ? `<p class="toc__group">${g.label}</p>` : ""}
      <ul class="toc__list">${tocItemsHtml(g.items)}</ul>`
        )
        .join("")
    : `<ul class="toc__list">${tocItemsHtml(headings)}</ul>`;

  nav.innerHTML = `
    <p class="toc__title">En esta página</p>
    ${body}
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
