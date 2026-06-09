# Auditoría DS M3 — Poda Figma móvil MVP+1

**Fecha:** 2026-06-08  
**Archivo:** [MyOwnTrip_nativo — Design System](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System)  
**Alcance:** Figma librería · subset móvil MVP+1

## Resumen

| Métrica | Antes | Después | Δ |
|---------|-------|---------|---|
| Páginas (sin `_archive`) | 33 | 22 | −11 |
| Component sets | 171 | 38 | −133 |
| Variantes (aprox.) | — | 1236 | — |
| Instancias en archivo | 23 105 | 6 458 | −72% |

**Resultado inicial:** PASS con deuda (restauración masiva completada el mismo día — ver § Restauración).

## Fase 1 — Páginas eliminadas

`Getting started`, `Table of contents`, `Avatars`, `Examples`, `Shape`, `Utilities`, `---`, `Carousel`, `Toolbars`, `Tooltips`, `Sliders`

## Fase 1 — Sets eliminados (muestra)

- Desktop/XR: `XR/*`, `Bottom app bar`, `Side Sheet`, `Navigation Rail*`, `Docked input date picker [desktop]`
- Botones no MVP: `Toggle button*`, `Split button`, `Segmented button*`, `FAB menu*`, `Connected/Standard button group*`
- Tiempo: `Dial picker`, `Keyboard picker`, `Period Selector*`, `hour-line`
- Otros: `Chip groups`, `List dialog`, `Scrollable list dialog`, sliders `Centered`/`Range`

## Fase 2 — Sets conservados (librería móvil)

| Página | Sets restantes |
|--------|----------------|
| App bars | App bar |
| Badges | Badges |
| Buttons | Button (filled/tonal/outline/text), Toggle button (filled/tonal/outline/elevated), Icon button - standard, FAB, Extended FAB |
| Chips | Suggestion, Filter, Assistive, Input chip |
| Date & time pickers | Input date picker, Modal date picker |
| Dialogs | Basic dialog |
| Lists | List item (0 density), List, List item |
| Loading & progress | Linear/Circular progress, Loading indicator |
| Menu | Menu (baseline), Menu, Menu item Standard/Vibrant |
| Navigation | Navigation Bar Horizontal/Vertical |
| Search | Search bar, full-screen/docked layout |
| Sheets | Bottom sheet |
| Snackbar | Snackbar |
| Switch, Tabs, Text fields, Checkboxes, Radio | 1 set cada uno |
| Foundations | Icons (página), Styles `.Shape` |

## m3Canonical

- Sin nuevos tokens de color por estado en sets conservados
- Variables `M3` intactas
- `brokenStyleRefsInButtons`: **0**

## Restauración subset acordado (2026-06-08, tarde)

**Criterio:** MVP Must + **Should íntegro** + `Button - elevated` + página **Sliders**.  
**v1.1 diferido:** Search full-screen/docked, Side sheet (eliminados del archivo activo).

| Métrica | Tras poda | Tras restauración |
|---------|-----------|-------------------|
| Component sets (páginas móvil) | 38 | **113** |
| Página Sliders | eliminada | recreada (3 sets) |

### Lotes restaurados desde CS (`uWmxOSQfjOxlEJ8k1yzOSX`)

- **Buttons:** elevated, Segmented + BB, Icon togglable ×4
- **Sliders:** Standard, Centered, Range
- **Should:** Chip groups, Scrollable list dialog, Lists (−2/−4 density, Accordion, Swipe, BB), Menu/Navigation/Date/Tabs/Sheets/Loading/Snackbar BB, App bars BB
- **v1.1:** eliminados `Search full-screen layout`, `Search docked layout` (conserva solo `Search bar`)
- **XLarge:** podado en elevated + togglable ×4 (300 → 240 var. c/u)

### Estado final por página

| Página | Sets |
|--------|------|
| Buttons | 30 |
| Menu | 13 |
| Lists | 12 |
| App bars | 7 |
| Loading & progress | 7 |
| Date & time pickers | 6 |
| Tabs | 6 |
| Cards | 5 |
| Navigation | 5 |
| Chips | 5 |
| Sliders | 3 |
| Snackbar | 3 |
| Dialogs | 2 |
| Sheets | 2 |
| Resto (1 c/u) | Badges, Checkboxes, Radio, Search, Switch, Text fields, Styles |

## Deuda residual

1. Huérfanos duplicados tras `clone()` — limpiar manualmente si molestan en el lienzo.
2. Snapshot `pre-prune 2026-06-08` en cuenta Figma para rollback.

## Descartado (no aplica)

- Pencil / semánticos custom / State Layers en Kotlin

## Referencias

- Allowlist: [`figma-prune-inventory.md`](../figma-prune-inventory.md)
- Inventario pre: [`figma-inventory-2026-06-08.md`](figma-inventory-2026-06-08.md)
- Scripts: [`.cursor/skills/myowntrip-ds-audit/scripts/`](../../../.cursor/skills/myowntrip-ds-audit/scripts/)
