# Fase C — Icon button

**Fecha:** 2026-10-29  
**Figma:** Icon button ×4 + togglable ×4 (página Buttons)  
**Compose:** `MOTIconButton` → `rememberMOTButtonShape()` (ADR 004)

## Compose SoT

| Rol | Kotlin |
|-----|--------|
| Shape | Morph **0 → 20** (`MotButtonCornerDp`) en hover/focus/press |
| Pads | M3 `IconButton` touch target (no `MOTSpacing` de chrome) |
| Consumo | App bars, wallet rows, journal, expenses… |

## Figma

Pads ya bound (`mot/padding/icon-button/*`) — audit buttons 2026-10-29.

### Radios Content (esta pasada)

Mapa ADR 004 en frame **Content** (8 sets, 5760 binds de esquina):

| Type / State | Token |
|--------------|-------|
| Square · Enabled / Disabled | `Corner/None` (0) |
| Square · Hovered / Focused / Pressed | `Corner/Large-increased` (20) |
| Round · (todos) | `Corner/Large-increased` (20) |
| Togglable Selected=True | `Corner/Large-increased` (20) |

Verificado muestra Icon button Medium Default: Square Enabled → None · Square Pressed → Large-increased · Round Enabled → Large-increased.

## Checklist

```
[x] Pads icon-button MOT
[x] Square reposo Corner/None
[x] Round / pressed → Large-increased
[x] Compose MOTIconButton morph
[x] Fila bindings + nota audit
```
