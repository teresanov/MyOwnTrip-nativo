# Fase B.3 — Text field (Figma ↔ Compose)

**Fecha:** 2026-10-29  
**Set:** `Text field` `52798:24373` (60 variantes, **Outlined only**)  
**Compose:** `OutlinedTextField` directo (CreateTrip, Wallet, Expenses…) + `MaterialTheme.shapes.extraSmall`

## Compose SoT

| Aspecto | Valor |
|---------|--------|
| API | `OutlinedTextField` (sin wrapper MOT) |
| Shape | `MaterialTheme.shapes.extraSmall` (4dp) ↔ Figma `Corner/Extra-small` |
| Tipo label / input | `bodySmall` / `bodyLarge` (defaults M3 + `AppTypography`) |
| Color | `colorScheme` (outline, primary focus, error, onSurface…) |

## Estado previo

- Colors / text styles: ya M3 schemes + Inter (`M3/body/*`).
- Radius + pads: **LEGACY** `semantic/radius/text-field/outlined` (4) + `semantic/padding/text-field/*` + `semantic/spacing/none|stack/xs`.

## Cambios

| Antes | Después |
|-------|---------|
| `semantic/radius/text-field/outlined` (4) | primero mal → `Corner/Small` (8); **corregido → `Corner/Extra-small` (4)** = `shapes.extraSmall` |
| `semantic/padding/text-field/horizontal` | **`mot/padding/text-field/horizontal`** (16) |
| `semantic/padding/text-field/vertical` / `stack/xs` | **`mot/padding/text-field/vertical`** (4) / **`mot/spacing/component-xs`** |
| `semantic/spacing/none` | **`mot/spacing/none`** |

Rebinds: **2050** · **legacyLeft: 0**.

Tokens nuevos MOT: `mot/padding/text-field/horizontal|vertical`, `mot/spacing/none`.

## Nota radio (paridad de nombres)

Bug histórico: el tema mapeaba mal el rol extraSmall. Corregido: `shapes.extraSmall`=4 ↔ `Corner/Extra-small`. SoT = M3 shapes (sin `MOTCorner`). Ver [shape.md](../shape.md).

## Checklist

```
[x] Solo Outlined (paridad Compose)
[x] 0 binds LEGACY en subárbol del set
[x] Radius `Corner/Extra-small` ↔ `shapes.extraSmall`
[x] Pads MOT · Spacing
[x] Tipografía Inter (post-limpieza DS)
[x] Fila bindings actualizada
[ ] Instancias design-file CreateTrip/Wallet (si caps existen)
```
