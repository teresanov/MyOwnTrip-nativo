# Showcase P0 — JSON + páginas Vite

**Fecha:** 2026-10-30  
**Alcance:** Text field · Filter chip · Basic dialog · Snackbar  
**SoT runtime:** Compose M3 directo (sin wrappers MOT salvo botones)

## Entregables

| Componente | JSON | Página | `components.json` | Bindings |
|------------|------|--------|-------------------|----------|
| Text field | `ds-showcase/src/data/components/text-field.json` | `components/text-field.html` | `ready` · P0 | [figma-compose-bindings.md](../figma-compose-bindings.md) |
| Filter chip | `filter-chip.json` | `components/filter-chip.html` | `ready` · P0 | idem |
| Basic dialog | `basic-dialog.json` | `components/basic-dialog.html` | `ready` · P0 | idem |
| Snackbar | `snackbar.json` | `components/snackbar.html` | `ready` · P0 | idem |

Cada ficha incluye: propósito, anatomía, variantes del set Figma, tokens (`colorScheme` / MOT spacing donde aplica), Do/Don't, a11y, snippet Compose, ejemplos de producto (donde aplica), changelog con referencia a auditorías B.3.

## Build

```bash
cd ds-showcase && npm run build
```

Entradas Rollup añadidas en `vite.config.js` (`text-field`, `filter-chip`, `basic-dialog`, `snackbar`). Estilos demo en `src/styles.css` (`.mot-field*`, `.filter-chip*`, `.dialog-demo*`, `.snackbar-demo*`).

**2026-10-30 (tarde):** fichas P0 con **playgrounds interactivos** (selección de chips, campo editable + error, modal, snackbar temporal) y helpers en `src/lib/component-page.js`. Copy de `purpose` / directrices reescrito en prosa legible (referencia Material), no telegráfico.

**Verificado:** `npm run build` OK (2026-10-30).

## Navegación

- Índice: `components.json` → hrefs a las cuatro páginas.
- Rail lateral: `initShowcasePage(..., { componentId })` en cada `src/pages/*.js`.

## Gates showcase ([showcase.md](../showcase.md))

```
[x] JSON por componente P0
[x] HTML + JS de página
[x] Status Ready en índice (Button sigue in-review)
[x] Enlaces desde figma-compose-bindings.md
[x] Plan paridad actualizado
[ ] Deploy GH Pages (workflow manual / push gh-pages)
```

## Relación Figma

Node ids y audits B.3 sin cambio; solo se añade capa de documentación externa alineada a [003-figma-library-showcase-docs.md](../../decisions/003-figma-library-showcase-docs.md).
