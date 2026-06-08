# Inventario Figma DS — 2026-06-08

**Archivo:** [MyOwnTrip_nativo — Design System](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=58186-19251)  
**Entrada:** nodo `58186:19251`  
**Fuente páginas:** auditoría paint styles (Bridge 2026-06-08) · **sets por página:** clasificación MVP+1 + escaneo live pendiente

## Resumen

| Métrica | Pre-poda | Post-poda (2026-06-08) |
|---------|----------|-------------------------|
| Páginas | 33 | 22 (+ `_archive`) |
| Component sets | 171 | 38 |
| Instancias | 23 105 | 6 458 |
| Paint styles (pre-limpieza tertiary) | 2362 → ~2271 | sin cambio en esta sesión |

Informe completo: [`figma-prune-report-2026-06-08.md`](figma-prune-report-2026-06-08.md)

## Tabla por página

| Página | Acción | Motivo |
|--------|--------|--------|
| Getting started | **DELETE** | Documentación kit — ADR 003 |
| Table of contents | **DELETE** | Índice doc |
| Avatars | **DELETE** | No MVP+1 producto |
| Icons | **KEEP** | Iconografía diseño pantallas |
| Examples | **DELETE** | Demos |
| Shape | **DELETE** | Demos corner; tokens en M3 |
| Styles | **KEEP** | Foundations; podar swatches hardcoded |
| Utilities | **DELETE** | Grids demo kit |
| --- | **DELETE** | Separador |
| App bars | **KEEP** | TopAppBar |
| Badges | **KEEP** | Contadores |
| Buttons | **KEEP** | P0 |
| Cards | **KEEP** | P1 |
| Carousel | **DELETE** | No móvil producto |
| Checkboxes | **KEEP** | Formularios |
| Chips | **KEEP** | FilterChip P0 + assist/suggestion |
| Date & time pickers | **KEEP** + **prune** | Date sí; time fase 2 |
| Dialogs | **KEEP** | AlertDialog |
| Dividers | **KEEP** | Layout listas |
| Lists | **KEEP** | ListItem P1 |
| Loading & progress | **KEEP** | Estados carga |
| Menu | **KEEP** | Dropdown |
| Navigation | **KEEP** + **prune** | Bottom nav sí; rail DELETE |
| Radio button | **KEEP** | Formularios |
| Search | **KEEP** | SearchBar JTBD |
| Sheets | **KEEP** + **prune** | Bottom sheet sí; side DELETE |
| Sliders | **REVIEW** | Mantener MVP+1 (bajo peso) |
| Snackbar | **KEEP** | Feedback JTBD |
| Switch | **KEEP** | Formularios |
| Tabs | **KEEP** | TabRow en TripDetail |
| Text fields | **KEEP** | P0 |
| Toolbars | **DELETE** | Desktop |
| Tooltips | **DELETE** | No prioritario Compose |

## Component sets post-poda (live 2026-06-08)

| Página | Sets | Instancias |
|--------|------|------------|
| App bars | App bar | 147 |
| Badges | Badges | 3 |
| Buttons | Button, Button - tonal/outline/text, Icon button - standard, FAB, Extended FAB | 802 |
| Cards | Stacked card, Horizontal card, Card states (Outlined/Elevated/Filled) | 3+ |
| Checkboxes | Checkboxes | 23 |
| Chips | Suggestion, Filter, Assistive, Input | 231 |
| Date & time pickers | Input date picker, Modal date picker | 369 |
| Dialogs | Basic dialog | 17 |
| Lists | List item 0 density, List, List item | 1962 |
| Loading & progress | 5 progress sets | 344 |
| Menu | 4 menu sets | 1589 |
| Navigation | Navigation Bar H/V | 139 |
| Search | Search bar + layouts | 174 |
| Sheets | Bottom sheet | 50 |
| Snackbar, Switch, Tabs, Text fields, Radio | 1 cada uno | — |

Scripts: [`figma-prune-phase1.js`](../../../.cursor/skills/myowntrip-ds-audit/scripts/figma-prune-phase1.js) · [`figma-prune-phase2.js`](../../../.cursor/skills/myowntrip-ds-audit/scripts/figma-prune-phase2.js)
