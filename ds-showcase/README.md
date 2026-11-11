# MyOwnTrip · DS Showcase

Web de documentación del Design System (M3 nativo editorial).

**Figma** = librería visual (variables + component sets).
**Esta web** = documentación: foundations, fichas de componente, patrones y gobernanza.
**Código** (`designsystem/`, `app/`) = fuente de verdad del comportamiento.

## Arranque local

```bash
cd ds-showcase
npm install
npm run dev
```

Abre la URL de Vite (suele ser `http://localhost:5173`).

## Build estático

```bash
npm run build
npm run preview
```

Salida en `dist/`. **Producción:** https://teresanov.github.io/MyOwnTrip-nativo/
(rama `gh-pages`; despliegue automático en push a `main` vía `deploy-showcase.yml`).

## Arquitectura

| Ruta | Contenido |
|------|-----------|
| `/` | Inicio: estado del sistema y accesos |
| `/empezar.html` | Cómo diseñar una pantalla y cómo implementarla |
| `/color.html` · `/typography.html` · `/shape.html` · `/spacing.html` · `/iconography.html` · `/motion.html` | Foundations |
| `/components.html` | Índice agrupado por para qué sirve |
| `/components/<id>.html` | Ficha de componente |
| `/patterns.html` | Patrones de producto |
| `/gobernanza.html` | Estados, criterios de wrapper, gates |

## Anatomía de una ficha

Página única con índice ancleado. El orden no es estético: **primero lo que se escribe
a mano, después lo que se deriva del código.** Las secciones derivadas llevan la marca
«desde el código» para que la deriva se vea.

1. **Uso** — cuándo usarlo, cuándo no (con alternativa), elegir variante
2. **Ejemplo** — playground interactivo
3. **Do y Don't** — guía de M3, con enlace y fecha de consulta
4. **Reglas MyOwnTrip** — acuerdos del equipo
5. Variantes del set · Anatomía · Comportamiento · Accesibilidad
6. **Tokens y roles** *(desde el código)*
7. **Implementación** *(desde el código)* — import, parámetros, snippet
8. Decisiones, ADR y changelog

Ficha de referencia: `src/data/components/button.json` + `src/pages/button.js`.

## Añadir un componente

1. Crear `src/data/components/<id>.json` — copiar `button.json`, que es la plantilla del esquema.
2. Registrar en `src/data/components.json` con `category` y `status`.
3. Crear `components/<id>.html` + `src/pages/<id>.js`.
4. Añadir la entrada en `vite.config.js` → `PAGES`.

El índice ancleado se monta solo: lee los `<h2 id>` de la página, no hay que mantener una lista.

## Añadir una foundation

1. Crear `src/data/foundations/<id>.json`.
2. Crear `<id>.html` + `src/pages/<id>.js` (4 líneas, copiar `motion.js`).
3. Añadir a `src/data/site.json` → `nav` y a `vite.config.js` → `PAGES`.

No hace falta escribir JS: `foundation-page.js` pinta el JSON.

## Contexto para agentes

```bash
npm run context   # se ejecuta solo en prebuild y predev
```

Genera, desde los mismos JSON que alimentan la web:

- `public/llms.txt` — índice del sistema con enlaces (convención llms.txt).
- `public/ds-context.md` — uso, composables y prohibiciones en texto plano.

Las reglas globales viven en `src/data/rules.json`. La idea es que
`.cursor/rules/*.mdc` deje de repetirlas a mano y apunte a `ds-context.md`:
una regla escrita a mano se desfasa del código, una generada no.

## Datos

| Archivo | Contenido |
|---------|-----------|
| `src/data/site.json` | Arquitectura de navegación, categorías, estados |
| `src/data/rules.json` | Reglas globales (obligatorio / prohibido / gates) |
| `src/data/tokens.json` | Roles de color, state layers, forma — alinear con `Color.kt` |
| `src/data/components.json` | Índice de componentes |
| `src/data/components/*.json` | Fichas |
| `src/data/foundations/*.json` | Foundations |
| `src/data/patterns.json` | Patrones |

Contrato: `docs/design-system/showcase.md` · ADR `003-figma-library-showcase-docs.md`.
