# Fase B.3 — App bar

**Fecha:** 2026-10-29  
**Set:** `App bar` `58114:20565` (12 · Config × Elevation)  
**Compose:** `TopAppBar` (Small) en Wallet/Journal/CreateTrip/… — centrado si hay `CenterAlignedTopAppBar` (pocas)

## Config ↔ Compose

| Figma Configuration | Compose típico |
|---------------------|----------------|
| Small | `TopAppBar` |
| Small-centered | `CenterAlignedTopAppBar` |
| Medium / Large / Search / Small-image | KEEP set; uso Compose bajo |

## Cambios

| Antes | Después |
|-------|---------|
| `semantic/spacing/stack/{xs,sm,md}` · `none` | `mot/spacing/component-{xs,sm,md}` · `mot/spacing/none` |
| `spacing/0` (primitivo) | `mot/spacing/none` |

Icon slots ya en `mot/padding/icon-button/0-*` · radios ADR `Corner/Large-increased` (morph destino).

Rebinds: **60** · **legacyLeft: 0** · **primitivesLeft: 0**.

## Gap raíz (`itemSpacing`)

| Config | Figma | Compose |
|--------|------:|---------|
| **Small** (producto) | **4** → `mot/spacing/component-xs` | `TopAppBar` M3 — sin API de gap; token = paridad layout Figma |
| Medium / Large | 8 → `mot/spacing/component-sm` | poco uso |
| **Search** | **-48** sin bind | **No hay en Kotlin.** Truco kit: solape con icono 48dp. Home usa `HomeSearchBar`, no esta config. |
| Small-centered 1 / 6 | kit quirk | sin espejo — no inventar `mot/*` |

## Re-audit live (2026-10-29, orden B.3 App bar+FAB+Search/Menu)

| Set | LEGACY | Notas |
|-----|-------:|-------|
| `App bar` Small Flat | **0** | root pads `mot/spacing/component-{xs,sm}` · icon slots `mot/padding/icon-button/0-*` · Content radius `Corner/Large-increased` |
| Quirks kit | — | Small-centered gap 1/6 unbound · Search root **-48** · Search state-layer **-16** (sin espejo Compose) |

Compose SoT: `TopAppBar` en Wallet/Journal/CreateTrip — config **Small**.

**Re-audit live (2026-10-29, sesión post-Wallet CTA):** set 12 vars · **0** LEGACY · quirks Search/Medium/Large kit unbound intactos (documentados).

**Re-audit live (2026-10-29, post-Wallet CTA — orden B.3 App bar+FAB+Menu/Search):** 12 vars · **0** LEGACY · MOT `mot/spacing/component-*` + icon-button pads · quirks kit unbound intactos (Small-centered 1/6 · Search -48/-16/Content 20).

**Re-audit live (2026-10-29, post-Wallet Import CTA reparado — cierre B.3 App bar):** 12 vars · **0** LEGACY · quirks kit unbound intactos (documentados; sin espejo Compose).

**Re-audit live (2026-10-29, continuación B.3 cierre App bar+FAB+Menu/Search):** 12 vars · **0** LEGACY · pads `mot/spacing/component-{xs,sm,md}` + `mot/padding/icon-button/0-*` · Content radius `Corner/Large-increased`. Quirks kit unbound (Small-centered / Search -48) sin cambio — sin espejo Compose.

## Checklist

```
[x] 0 LEGACY / 0 primitivos Space en pads
[x] Tipografía M3 (Title Large / Headline Medium / …)
[x] Fila bindings
[x] Gap Small/Medium documentado; Search -48 = kit-only
```
