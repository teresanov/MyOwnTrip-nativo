# Auditoría hex / variables — Figma DS

**Fecha:** 2026-06-10  
**Archivo:** [MyOwnTrip_nativo — Design System](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432) · `zrGAL4v6MEMc9hzZemU432`  
**Trigger:** Snackbar close affordance con `#1c1b1f` sin bindear (contraste 1,31:1 en Light).

---

## Resumen

| Ámbito | Estado |
|--------|--------|
| **P0 MVP** (Button, Text field, Snackbar, Lists, Search, Navigation…) | **Limpio** — 0 hex semánticos sin variable tras fixes |
| **Icons** (vectores de símbolos) | **Corregido** — 40 vectores → `Schemes/On Surface` |
| **Date pickers** (range highlight) | **Corregido** — 4 nodos → `Schemes/Primary Container` |
| **Snackbar** (close X) | **Corregido** — → `Schemes/Inverse On Surface` |
| **Assistive chip** (brand icon) | **ACEPTADO** — placeholder visual; en producto = logo/favicon real |
| **Icons** (títulos de categoría) | **ACEPTADO** — fuera de librería publicada |
| **Safety-zone** (`#ff00f5`) | **OK ignorar** — overlay dev M3 |
| **Section labels** (`#1a5933`, etc.) | **OK ignorar** — anotaciones de poda |
| **Borde sets** (`#9747ff`) | **OK ignorar** — chrome Figma en `COMPONENT_SET` |

---

## Hallazgo crítico (origen)

El Theme Builder enlazó bien los roles **inverse** del Snackbar:

| Rol | Token | Contraste vs `Inverse Surface` (Light) |
|-----|-------|----------------------------------------|
| Surface | `Schemes/Inverse Surface` | — |
| Texto | `Schemes/Inverse On Surface` | **11,52:1** ✓ |
| Acción | `Schemes/Inverse Primary` | **7,70:1** ✓ |

El **icono close** heredaba `#1c1b1f` (default kit M3 ≈ `onSurface` antiguo), **sin variable** → **1,31:1** en Light (fallo WCAG UI ≥ 3:1).

**Nota:** el tema MyOwnTrip usa `Schemes/On Surface` = `#1f1b13`, no `#1c1b1f`. Los hex del kit Google son doblemente incorrectos: sin bindear y desalineados del theme builder.

---

## Fixes aplicados (2026-06-10)

1. **Snackbar-close-affordance** + instancias en Snackbar → `Schemes/Inverse On Surface`
2. **Icon `close`** en Icons → `Schemes/On Surface` (default librería)
3. **40 iconos** restantes en Icons (vectores sin bindear) → `Schemes/On Surface`
4. **Modal date picker** — `Range highlight start/end` (4) → `Schemes/Primary Container` (antes `#e8def8` fijo)

---

## Por página (MVP)

| Página | Hex sin variable (semántico) | Notas |
|--------|------------------------------|-------|
| Buttons | **0** | ~12 855 fills bindeados |
| Text fields | **0** | |
| Snackbar | **0** | |
| Search | **0** | |
| Lists | **0** | (+ 2 labels poda `#1a5933`) |
| Navigation | **0** | (+ 1 label poda) |
| Date & time pickers | **0** | (+ labels sección MVP/LATER) |
| Chips | **230** | Assistive brand icon — placeholder aceptado |
| Icons | **36** | Títulos categoría — fuera de librería, aceptado |

---

## Assistive chip — ACEPTADO (2026-06-10)

Set `53923:28089` · 48 variantes · **KEEP** (Assist en producto).

`.Building Blocks/Colourful logo` usa colores fijos como **ejemplo visual** del slot brand icon (en app: logotipo o favicon del servicio concreto). No es token de tema — **no acción**.

## Icons — títulos de categoría — ACEPTADO

Hex en labels de organización (`#808080`, `#262626`, …) en página Icons: **no forman parte de la librería publicada** — sin acción.

---

## Ignorar a propósito

| Hex / patrón | Dónde | Motivo |
|--------------|-------|--------|
| `#ff00f5` | Safety-zone en Cards, Menu, Sheets BB | Guía M3, no export |
| `#9747ff` | Stroke de `COMPONENT_SET` | UI Figma |
| `#ffffff` / `#000000` | Máscaras, state-layer vacíos | No semánticos |
| `#1a5933`, `#73591a` | Section labels poda | Documentación interna |
| Paleta Styles / Examples | `#f9f6f4`, `#cfcac0`, … | Swatches y demos |

---

## Regla operativa

> **Todo fill/stroke semántico** (texto, icono, surface, outline, highlight) → variable `Schemes/*` o `State Layers/*`.  
> Hex solo en swatches de `Styles` / `.Tonal palettes` y overlays dev.

**Gate sugerido antes de publicar librería:** plugin audit o script Bridge — 0 vectores `Vector` con `#1c1b1f` / `#1f1b13` sin `boundVariables` en páginas MVP.

---

## Relacionado

- [`figma-style-variable-audit-2026-06-09.md`](figma-style-variable-audit-2026-06-09.md)
- [`figma-prune-inventory.md`](../figma-prune-inventory.md)
- [`color.md`](../color.md) · gate `m3Canonical`
