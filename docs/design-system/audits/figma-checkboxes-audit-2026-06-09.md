# Auditoría — Checkboxes (variables vs styles)

**Archivo:** [MyOwnTrip_nativo — Design System](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432) · `zrGAL4v6MEMc9hzZemU432`  
**Set:** `Checkboxes` · `51859:5628` · 30 variantes  
**Referencia kit:** [Material 3 Design Kit — Checkboxes](https://www.figma.com/design/4IwWS17QjlbpiYu8jLZRdf/Material-3-Design-Kit--Community-?node-id=55141-14173)  
**Fecha:** 2026-06-09

---

## Resumen

| Métrica | Valor | Estado |
|---------|------:|--------|
| Variantes | 30 | KEEP (6 tipos × 5 estados) |
| Fills vía **paint style** (`fillStyleId`) | **0** | OK (FASE B ya aplicada) |
| Fills vía **variable** (`boundVariables`) | **64** | OK contenedor + state layers + iconos |
| Fills **hex fijos** | **0** (era 40) | **Corregido 2026-06-09** — iconos re-bindeados |
| Strokes vía variable | 16 | OK (unselected / outline) |

**Conclusión:** el problema que ves (“fueron a estilos”) ya **no aplica al contenedor** tras FASE B; lo que queda mal son los **iconos del check** (`check_small` / `check_indeterminate_small`) con **hex crudo** (`#ffffff` + `#1c1b1f`) en lugar de `Schemes/On Primary` o `Schemes/On Error`.

---

## Ejes del set

| Eje | Valores |
|-----|---------|
| **Type** | Selected · Unselected · Indeterminate · Error selected · Error unselected · Error indeterminate |
| **State** | Enabled · Disabled · Hovered · Focused · Pressed |

---

## Qué está bien (variables M3)

| Capa | Variable típica | Tipos |
|------|-----------------|-------|
| `container` fill | `Schemes/Primary` | Selected, Indeterminate |
| `container` fill | `Schemes/Error` | Error selected, Error indeterminate |
| `container` fill | `Schemes/On Surface` | Disabled (marcado/indeterminado) |
| `container` stroke | `Schemes/On Surface Variant` | Unselected Enabled |
| `container` stroke | `Schemes/On Surface` | Unselected Disabled / interacción |
| `container` stroke | `Schemes/Error` | Error unselected |
| `state-layer` | `State Layers/Primary/*` · `On Surface/*` · `Error/*` | Hovered / Focused / Pressed |
| `Ripple` | `Schemes/Primary` · `Schemes/Error` | Pressed |

Alineado con [M3 checkbox specs](https://m3.material.io/components/checkbox/specs) y roles `colorScheme` en Compose.

---

## Qué está mal (hex fijo)

En **todas** las variantes con icono (Selected, Indeterminate, Error selected, Error indeterminate):

| Nodo | Hex actual | Debería ser |
|------|------------|-------------|
| `icon` (FRAME) | `#ffffff` | Sin fill o transparente — el color lo lleva el vector |
| `Vector` | `#1c1b1f` | `Schemes/On Primary` (tipos normales) · `Schemes/On Error` (tipos Error) |
| `Vector` (Disabled, fill `On Surface`) | `#1c1b1f` | `Schemes/On Surface` (o token disabled M3 equivalente) |

`#1c1b1f` ≈ on-surface oscuro: **no cambia con Dark** ni con tu `tertiary`/marca; en Light puede parecer “casi bien” sobre primary pero **rompe tema** y falla `m3Canonical`.

---

## Por qué pasó

1. **FASE B** desenganchó `fillStyleId` del contenedor y migró a variable — bien.
2. Los iconos son **instancias anidadas** (`check_small`, `check_indeterminate_small`); al desenganchar styles, el paint quedó en **hex residual** del kit Google, no en variable.
3. El kit comunitario [M3](https://www.figma.com/design/4IwWS17QjlbpiYu8jLZRdf/Material-3-Design-Kit--Community-?node-id=55141-14173) suele mezclar styles + hex en iconos vectoriales — hay que **re-bindear a mano o script** tras importar.

---

## Fix aplicado (2026-06-09)

| Paso | Acción | Estado |
|------|--------|--------|
| 1 | Quitar fill `#ffffff` del FRAME `icon` | Hecho (20 nodos) |
| 2 | `Vector` → `Schemes/On Primary` (Selected / Indeterminate) | Hecho |
| 3 | `Vector` → `Schemes/On Error` (Error selected / indeterminate) | Hecho |
| 4 | `Vector` → `Schemes/On Surface` (Disabled) | Hecho |
| 5 | Re-auditar: **0** hex fijos · **64** variables | OK |

**Pendiente manual:** probar **Dark** (cambiar modo M3 y verificar contraste del check).

---

## Poda (inventario)

- **KEEP** set completo (30 variantes) — formularios MVP.
- **No** recortar tipos Error (validación M3).
- Opcional futuro: reducir estados si solo diseñáis Enabled + Disabled en Figma (no recomendado; Compose usa state layers).

---

## Referencias

- [`figma-style-variable-audit-2026-06-09.md`](figma-style-variable-audit-2026-06-09.md) — FASE A/B global
- [`figma-prune-inventory.md`](../figma-prune-inventory.md) — Checkboxes KEEP
- Script FASE B: [`.cursor/skills/myowntrip-ds-audit/scripts/figma-phase-b-detach-fill-styles.js`](../../../.cursor/skills/myowntrip-ds-audit/scripts/figma-phase-b-detach-fill-styles.js)
