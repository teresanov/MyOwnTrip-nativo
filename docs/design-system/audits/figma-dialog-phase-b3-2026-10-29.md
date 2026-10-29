# Fase B.3 — Basic dialog

**Fecha:** 2026-10-29  
**Set:** `Basic dialog` `50723:10929` (2 variantes Icon True/False)  
**Compose:** `AlertDialog` (Wallet/Journal…) → default `MaterialTheme.shapes.extraLarge`  
**SoT consumidores:** `WalletConfirmDialog` / `WalletDeleteEntryDialog` / `WalletDiscardDialog` (+ Create/Edit trip discard)

## Paridad shape

| Antes | Después |
|-------|---------|
| Figma `semantic/radius/card` (12) | **`Corner/Extra-large` (28)** |
| Compose `shapes.extraLarge` mal → 20 | **`shapes.extraLarge` = 28** ↔ `Corner/Extra-large` |

Botón morph: `rememberMOTButtonShape` / `Corner/Large-increased` — **fuera** de `shapes.*`.

## Pads / gaps

| LEGACY | MOT |
|--------|-----|
| `semantic/spacing/none` | `mot/spacing/none` |
| `stack/xl` | `mot/padding/dialog/container` (24) |
| `gutter` | `mot/padding/dialog/actions-gutter` (16) |
| `stack/lg` | `mot/spacing/dialog/section-gap` (16) |
| `stack/md` | `mot/spacing/dialog/title-body-gap` (12) |
| `stack/sm` | `mot/spacing/dialog/actions-gap` (8) |

Rebinds: **183** · **legacyLeft: 0**.  
Re-audit live: **0** LEGACY · **0** pads/corners contrato unbound.

**Re-entrada B.3 (post-TextField, misma fecha):** set `50723:10929` · 2 vars · **0 LEGACY** · MOT `mot/padding|spacing/dialog/*` · `Corner/Extra-large`. Wallet caps 7/11/12 = instancias `Basic dialog` (0 inventos en overlay).

**Re-audit live (2026-10-29, post FilterChip confirm):** **0** LEGACY · **0** pads unbound · **0** corner ≠ `Corner/Extra-large` · vars MOT dialog + Static Headline/Body/Label + Schemes Surface Container*.

**Re-audit live (2026-10-29, orden B.3 post-FilterChip):** dump `Icon=False` → root `Corner/Extra-large` ×4 · `mot/spacing/none` · fill `Surface Container High`; content `mot/padding/dialog/container` + `section-gap`; actions `container` + `actions-gap`. Wallet caps 7/11/12 = instancias `Basic dialog` (Confirmar Icon=False · Eliminar/Descartar Icon=True) · **0** inventos siblings en overlay. Copy alineado a `WalletConfirm*` / `WalletDeleteEntryDialog` / `WalletDiscardDialog`.

**Re-audit live (2026-10-29, sesión post-Wallet CTA):** set 2 vars · **0** LEGACY · **0** pads contrato unbound · root `Corner/Extra-large`. Residual r=100 en Content anidado de botones = pill estructural M3 (no contrato). Wallet caps 7/11/12 = `492:3332` / `492:3372` / `492:3414` INSTANCE Basic dialog.

**Re-audit live (2026-10-29, post-Wallet CTA — orden B.3 tras FilterChip):** set 2 · **0** LEGACY · **0** pads/corners unbound · MOT `mot/padding|spacing/dialog/*` · `Corner/Extra-large` · Static Headline/Body/Label + Surface Container*. Wallet 7/11/12 = INSTANCE Basic dialog (Confirmar Icon=False · Eliminar/Descartar Icon=True) · **0** inventos siblings en overlay.

**Re-audit live (2026-10-29, post-Wallet Import CTA reparado — orden B.3 tras FilterChip):** set 2 · **0** LEGACY · **0** pads/corners unbound · `Corner/Extra-large` · MOT dialog/*. Wallet `492:3332` / `492:3372` / `492:3414` = INSTANCE Basic dialog · overlay siblings **0** · copy Confirmar/Eliminar/Descartar intacto.

**Re-audit live (2026-10-29, continuación B.3 post-FilterChip confirm):** set 2 · **0** LEGACY · **0** pads contrato unbound · root `Corner/Extra-large` · MOT `mot/padding|spacing/dialog/*` · Static Headline/Body/Label + Surface Container*. Residual r=100 en Content anidado de botones = pill estructural M3. Wallet caps 7/11/12 = `492:3332` Confirmar Icon=False · `492:3372` Eliminar Icon=True · `492:3414` Descartar Icon=True · overlay siblings **0** inventos.

## Design-file Wallet (gap cerrado)

Caps **7 / 11 / 12** tenían Body con `TEXT` suelto (invento). Sustituido por instancias DS:

| Cap | Instancia | Icon | CTAs |
|-----|-----------|------|------|
| 7 · confirmar | `Basic dialog · Confirmar` `492:3332` | False | Cancelar / Guardar |
| 11 · eliminar | `Basic dialog · Eliminar` `492:3372` | True → glyph `error` | Cancelar / Eliminar |
| 12 · descartar | `Basic dialog · Descartar` `492:3414` | True → glyph `error` | Seguir editando / Descartar |

Overlay `Dialog overlay` (scrim 32%) centrado en phone — layout only; control = `INSTANCE` set Basic dialog.

**Nota:** Confirm Compose es más rico (plan Switch / MotTimeField). Cap 7 resume copy esencial en Supporting text; no inventar Switch suelto.

## Checklist

```
[x] Radius `Corner/Extra-large` ↔ `shapes.extraLarge`
[x] `shapes.extraLarge` = 28; morph botón aparte
[x] 0 binds LEGACY
[x] Pads MOT · Spacing
[x] Fila bindings actualizada
[x] Design-file Wallet caps 7/11/12 = instancias (0 inventos diálogo)
```
