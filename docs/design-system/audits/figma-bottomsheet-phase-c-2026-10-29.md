# Fase C — Bottom sheet

**Fecha:** 2026-10-29  
**Set:** `Bottom sheet` `51827:5859` (Modal True/False)  
**Compose:** `ModalBottomSheet` (Home FAB, Plan, Journal…) — default M3 + pads `MOTSpacing`

## Compose SoT

| Rol | Kotlin |
|-----|--------|
| Shape contenedor | `MaterialTheme.shapes.extraLarge` (28) — explícito en `HomeFabAddSheet` |
| Pad horizontal contenido | `MOTSpacing.screenHorizontal` (16) |
| Pad inferior | `MOTSpacing.screenContentBottom` (24) |
| Gap bloques | `MOTSpacing.layoutMd` (16) / header kit gap → `componentMd` (12) |

## Figma binds (espejo)

| Nodo | Valor | Token |
|------|------:|-------|
| Bottom Sheet top L/R | 28 | `Corner/Extra-large` |
| Header pads | 16 | `mot/spacing/screen-horizontal` |
| Header gap | 12 | `mot/spacing/component-md` |
| Drag handle | pill | `Corner/Full` |

0 LEGACY. Sin `mot/*` nuevos.
