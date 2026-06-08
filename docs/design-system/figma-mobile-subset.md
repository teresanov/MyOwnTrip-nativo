# Figma — Subset móvil MyOwnTrip

Fuente de verdad para la librería [MyOwnTrip_nativo — Design System](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System).  
Copia de restauración: `uWmxOSQfjOxlEJ8k1yzOSX` (CS).

**Criterio (2026-06-08, acordado):** MVP Must + **Should completo** + elevated + sliders.  
**v1.1 (diferido):** Search full-screen/docked, Side sheet.  
**Política:** Figma = librería visual — [ADR 003](../decisions/003-figma-library-showcase-docs.md).

## Foundations (KEEP)

| Recurso | Motivo |
|---------|--------|
| Colección variables `M3` | MTB → [`variables.json`](variables.json) |
| `.Tonal palettes` | `Palettes/*` |
| Página `Styles` (`.Shape` + swatches útiles) | Referencia |
| Página `Icons` | Material Symbols Sharp |

## MVP Must — restaurar

| Página | Sets |
|--------|------|
| **App bars** | App bar + building blocks (sin Bottom app bar, sin XR) |
| **Badges** | Badges |
| **Buttons** | Button, tonal, outline, text, **elevated**; Icon button (4 estilos); FAB, Extended FAB; Toggle (4 estilos) |
| **Cards** | Stacked, Horizontal + Card states BB |
| **Checkboxes** | Checkboxes |
| **Chips** | Filter, Assist, Suggestion, Input |
| **Date & time pickers** | Input + Modal date picker + BB calendario (sin time pickers) |
| **Dialogs** | Basic dialog |
| **Lists** | List, List item, 0 density baseline + BB |
| **Loading & progress** | Linear/Circular progress, Loading indicator + BB |
| **Menu** | Menu, Menu item, BB |
| **Navigation** | Navigation Bar H/V + BB (sin rail, sin XR) |
| **Radio button** | Radio buttons |
| **Search** | **Search bar** solamente |
| **Sheets** | Bottom sheet + BB (sin side sheet → v1.1) |
| **Snackbar** | Snackbar + BB |
| **Switch** | Switch |
| **Tabs** | Tabs + BB primary/secondary |
| **Text fields** | Text field |

## Should — restaurar íntegro

| Página | Sets adicionales |
|--------|------------------|
| **Buttons** | FAB menu + BB; Segmented button + BB; Icon button togglable (4); Standard button group — **no** (excluido) |
| **Chips** | Chip groups |
| **Lists** | List item Accordion, List Item Swipe, -2/-4 density, BB accordion/reveal |
| **Dialogs** | Scrollable list dialog |
| **Sliders** | Standard, Centered, Range |

## v1.1 — no restaurar aún

| Item | Motivo |
|------|--------|
| Search full-screen / docked layout | Búsqueda expandida — v1.1 |
| Side sheet + BB | Panel lateral — v1.1 |
| Navigation drawer | Sin cambio de arquitectura nav |

## Excluir (no restaurar)

| Página / patrón |
|-----------------|
| Getting started, Table of contents, Examples, Shape, Utilities, `---`, Carousel, Toolbars, Tooltips, Avatars |
| XR/*, Navigation rail*, Bottom app bar |
| Split button, Connected/Standard button group |
| Time pickers (Dial, Keyboard, Hour, Period, Docked desktop) |
| List dialog (no scrollable) |
| Doc/demo: Generic avatar, Shape Set, Keyboard utility |

## Variantes (Figma para diseño)

| Eje | Política |
|-----|----------|
| **Size** (botones/icon) | XSmall, Small, Medium, Large — **sin XLarge** |
| **Type**, **State** | Conservar completos |
| **Style** botón | filled, tonal, outline, text, **elevated** |

**m3Canonical (Compose):** estados interactivos vía state layers en runtime.

## Estado actual (2026-06-08)

**113** component sets en archivo activo — alineado al catálogo CS filtrado (excl. Connected button group, layouts Search v1.1, duplicados Card).

| Bloque | Estado |
|--------|--------|
| MVP Must | ✓ |
| Should íntegro | ✓ |
| Elevated + Sliders | ✓ |
| v1.1 (Search layouts, Side sheet) | no restaurado |

## Sincronización

| Artefacto | Acción |
|-----------|--------|
| [`components.md`](components.md) | Prioridades P0–P2 |
| [`ds-showcase/src/data/components.json`](../../ds-showcase/src/data/components.json) | Fichas KEEP |
| Inventario | Actualizar tras restauración |
