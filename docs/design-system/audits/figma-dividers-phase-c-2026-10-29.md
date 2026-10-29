# Fase C — Dividers

**Fecha:** 2026-10-29  
**Figma:** página Dividers · 7 componentes (`51816:5860` … `51816:5866` + subhead `51816:5872`)  
**Compose:** `MOTHorizontalDivider` → M3 `HorizontalDivider`

## Compose SoT

| Rol | Kotlin |
|-----|--------|
| Color | `colorScheme.outlineVariant` |
| Full-width | `MOTHorizontalDivider()` |
| Inset (listas / flow) | `MOTHorizontalDivider(inset = true)` → pad H `MOTSpacing.screenHorizontal` (16) |
| Separación vertical ligera | `Modifier.padding(vertical = MOTSpacing.componentXs)` |

Consumidores cableados: wallet rows/screen/flow review, Home flow review, Create/Edit trip, TripPlanTab, Restaurant form.

## Figma

| Componente | Cambio |
|------------|--------|
| H/V Inset · Middle-inset | LEGACY `semantic/spacing/gutter` → **`mot/spacing/screen-horizontal`** |
| Divider with subhead | pads 16 unbound + gap 4 → **`screen-horizontal`** + **`component-xs`** |
| LINE stroke | ya `Schemes/Outline Variant` (sin cambio) |
| Full-width | sin inset (OK) |

## Checklist

```
[x] MOTHorizontalDivider + inset
[x] 0 LEGACY gutter en insets
[x] Color Outline Variant
[x] Fila bindings
```
