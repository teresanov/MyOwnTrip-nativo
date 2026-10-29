# Fase B.3 — Snackbar

**Fecha:** 2026-10-29  
**Set:** `Snackbar` `53977:33575` (10 variantes)  
**Compose:** `SnackbarHost` M3 directo (DayHub, Journal, Expenses, Documents…)

## Paridad

| Rol | Compose | Figma |
|-----|---------|-------|
| Shape | default M3 ≈ `shapes.extraSmall` (4) | **`Corner/Extra-small`** |
| Color | `inverseSurface` / `inverseOnSurface` / `inversePrimary` | Schemes Inverse* |
| Tipo | `bodyMedium` / `labelLarge` | `M3/body/medium` · `M3/label/large` |

## Cambios layout

| LEGACY | MOT / Shape |
|--------|-------------|
| `semantic/radius/snackbar` (4) | `Corner/Extra-small` |
| `semantic/spacing/none` | `mot/spacing/none` |
| `gutter` | `mot/padding/snackbar/horizontal` (16) |
| `stack/md` | `mot/spacing/snackbar/gap` / pads vertical vía mismo 12 |
| `stack/sm` · `stack/xs` | `mot/spacing/snackbar/gap-sm` · `gap-xs` |

Tokens nuevos: `mot/padding/snackbar/{horizontal,vertical}`, `mot/spacing/snackbar/gap{,-sm,-xs}`.

Rebinds: **304** · **legacyLeft: 0**.

Pill `r=100` en Inverse-button / Close = estructural M3 (no bindable Corner Full obligatorio).

## Fix follow-up (2026-10-29)

Variante `Text & longer action · Two lines · Show close=False` tenía `itemSpacing` **14** unbound.  
Rebind → **`mot/spacing/snackbar/gap`** (12), paridad con la hermana `Show close=True`.  
Gate post-fix: **0** roots Snackbar con gap unbound.

**Re-entrada B.3 (post-TextField):** set `53977:33575` · 10 vars · **0 LEGACY** · **0** pads/corners contrato unbound · Inverse Surface + `Corner/Extra-small`.

**Re-audit live (2026-10-29, post FilterChip confirm):** **0** LEGACY · **0** pads unbound · **0** corner ≠ `Corner/Extra-small` · Inverse* + `mot/padding|spacing/snackbar/*`.

**Re-audit live (2026-10-29, orden B.3 post-FilterChip):** sample Text only · Two lines · close=True → `Corner/Extra-small` ×4 · `mot/padding/snackbar/horizontal` · `mot/spacing/snackbar/gap` · Inverse Surface/On Surface · Static Body Medium. Gate set 10 vars: **0** LEGACY · **0** pads/corners contrato unbound. Pill `r=100` Close = estructural M3.

**Re-audit live (2026-10-29, sesión post-Wallet CTA):** 10 vars · **0** LEGACY · **0** pads unbound · root `Corner/Extra-small`. Residual r=100 en Inverse-button / Close Container = estructural.

**Re-audit live (2026-10-29, post-Wallet CTA — orden B.3 tras Dialog):** 10 vars · **0** LEGACY · **0** pads/corners unbound · Inverse Surface/On Surface/Primary · `Corner/Extra-small` · `mot/padding/snackbar/horizontal` · `mot/spacing/snackbar/gap{,-sm,-xs}` · Static Body/Label.

**Re-audit live (2026-10-29, post-Wallet Import CTA reparado — orden B.3 tras Dialog):** 10 vars · **0** LEGACY · **0** pads/corners unbound · Inverse* · `Corner/Extra-small` · MOT snackbar/*.

**Re-audit live (2026-10-29, continuación B.3 post-Dialog):** 10 vars · **0** LEGACY · **0** pads contrato unbound · root `Corner/Extra-small` · Inverse Surface/On Surface/Primary · `mot/padding/snackbar/horizontal` · `mot/spacing/snackbar/gap{,-sm,-xs}`. Residual r=100 Close Container = estructural M3.

## Checklist

```
[x] Radius Extra-small ↔ shapes.extraSmall
[x] 0 LEGACY
[x] Pads MOT
[x] Tipografía M3
[x] Fila bindings
[x] 0 itemSpacing raíz unbound (incl. longer-action)
```
