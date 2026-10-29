# Fase B.3 — Menu + Search (chrome Home)

**Fecha:** 2026-10-29

| Set | Node | Resultado |
|-----|------|-----------|
| `Menu` | `58966:3975` | **98** rebinds · 0 LEGACY |
| `Menu item/Standard` | `58966:4100` | **22** rebinds · 0 LEGACY |
| `Search bar` | `52977:33813` | 0 LEGACY; follow-up corner/pads (abajo) |
| `FAB` | `57998:43426` | ya limpio (0) |

Mapa típico: `semantic/spacing/*` → `mot/spacing/*` · `semantic/radius/text-field/outlined` → `Corner/Extra-small`.

## Gap raíz Menu (fix)

`itemSpacing` del set `Menu` estaba **sin variable** (2dp entre listas/grupos).

| Variante | Bind |
|----------|------|
| Groups=2 / 3 | `mot/spacing/menu/group-gap` (→ `spacing/2`) |
| Groups=1 | `mot/spacing/none` |

## Menu (baseline) `54061:36964` Density=0

| Prop | Valor | Bind |
|------|------:|------|
| paddingTop/Bottom | 8 | `mot/padding/menu/vertical` |
| paddingLeft/Right · itemSpacing | 0 | `mot/spacing/none` |
| corner | 4 | `Corner/Extra-small` |

Producto `Menu` `58966:3975`: radius ya `Corner/Large` (16) — alineado a `HomeFilterMenuSpec.CornerRadius`.

## Search bar follow-up (2026-10-29, post FilterChip)

Compose SoT: `HomeSearchBar` → `RoundedCornerShape(28.dp)` ↔ `Corner/Extra-large`.

| Nodo contrato | Antes | Después |
|---------------|-------|---------|
| COMPONENT root corner 28 | unbound | **`Corner/Extra-large`** ×4 |
| COMPONENT `itemSpacing` 4 | unbound | **`mot/spacing/component-xs`** |
| `state-layer` pads 4 | unbound | **`mot/spacing/component-xs`** |

Rebinds: **54** · gate raíz+state-layer pads/corner: **0 unbound**.

**Pendiente documentado (no inventar `mot/*` sin espejo Kotlin):**

| Literal | Dónde | Nota |
|---------|-------|------|
| Content `paddingLeft/Right` **20** | Search bar | Compose usa `20.dp` literal en `HomeSearchBarRow` sin leading icon — gap Compose+Figma |
| `itemSpacing` **-16** | state-layer | solape kit M3 (como App bar Search **-48**) |
| Avatar-target pads 8/12 | opcional | poco uso producto |

Menu nested (List/Content/State Layer) sigue con pads kit unbound densos — **0 LEGACY**; no rebind masivo sin matriz Compose item-a-item.

## Re-audit live (2026-10-29, post FilterChip confirm)

| Set | LEGACY | Notas |
|-----|-------:|-------|
| `App bar` `58114:20565` | **0** | MOT spacing + icon-button pads |
| `FAB` `57998:43426` | **0** | pads `mot/padding/fab/*` |
| `FAB menu` `57998:42986` | **0** | — |
| `Menu` `58966:3975` | **0** | — |
| `Search bar` `52977:33813` | **0** | SoT Home (`HomeSearchBar`) |
| `Search full-screen layout` `59178:4963` | **50** | Residual kit: `semantic/padding|spacing/list-item/*` en State layer anidado. **Compose no usa** full-screen search (Home = `HomeSearchBar`). No rebind masivo sin espejo Kotlin — misma política que pads Menu nested. |
| `Search full-screen layout (baseline)` | **0** | — |

## Re-audit live (2026-10-29, orden B.3 post-Dialog/Snackbar)

| Set | LEGACY | Contrato |
|-----|-------:|----------|
| `FAB` Medium Primary Enabled | **0** | `Corner/Large-increased` · `mot/padding/fab/medium` en State-layer |
| `FAB menu` | **0** | residual root `itemSpacing` 8 unbound (kit Segments=3; no inventar `mot/*` sin SoT) |
| `Menu` Standard Groups 1–3 | **0** | `Corner/Large` (16) ↔ `HomeFilterMenuSpec` · gap Groups≥2 → `mot/spacing/menu/group-gap` |
| `Search bar` Enabled avatar=False | **0** | root `Corner/Extra-large` (28) ↔ `HomeSearchBar` `RoundedCornerShape(28)` · `mot/spacing/component-xs` · state-layer **-16** kit |
| `Search full-screen layout` | **50** | `semantic/padding/list-item/{horizontal,vertical}` (20+20) · `semantic/spacing/stack/md` (10) — **no SoT Compose**; dejar hasta tocar frame |

Home design-file: filtros = Menu (no FilterChip) — paridad Compose `HomeFilterDropdownMenu`.

## Re-audit live (2026-10-29, sesión post-Wallet CTA — cierre B.3)

| Set | LEGACY | Notas |
|-----|-------:|-------|
| `FAB` `57998:43426` (72) | **0** | **0** pads unbound · `mot/padding/fab/*` |
| `Menu` `58966:3975` | **0** | pads nested kit densos unbound (documentados); gap Groups=`mot/spacing/menu/group-gap` |
| `Search bar` `52977:33813` | **0** | Content **20** / state-layer **-16** kit — gap Compose documentado |
| `Search full-screen layout` `59178:4963` | **50** | Residual `semantic/padding\|spacing/list-item\|stack` — sin SoT Compose |
| `App bar` `58114:20565` | **0** | quirks Search/Medium/Large kit unbound (documentados en appbar audit) |

## Re-audit live (2026-10-29, post-Wallet CTA — orden B.3 App bar+FAB+Menu/Search)

| Set | LEGACY | Notas |
|-----|-------:|-------|
| `App bar` 12 | **0** | quirks kit unbound: Small-centered gap 1/6 · Search **-48** / state-layer **-16** / Content 20 — sin espejo Compose |
| `FAB` 72 | **0** | **0** pads unbound · `mot/padding/fab/*` |
| `Menu` 3 | **0** | pads nested kit densos unbound; `mot/spacing/menu/group-gap` |
| `Search bar` 6 | **0** | Content **20** / state-layer **-16** documentados |
| `Search full-screen layout` | **50** | Residual `semantic/padding\|spacing/list-item\|stack` — no SoT Compose |

Home design-file: **0** FilterChip · **1** Menu (`Filter menu`) + Search bar ×3 — paridad `HomeFilterDropdownMenu` / `HomeSearchBar`.

## Re-audit live (2026-10-29, post-Wallet Import CTA reparado — cierre B.3 App bar+FAB+Menu/Search)

| Set | LEGACY | Notas |
|-----|-------:|-------|
| `App bar` 12 | **0** | quirks kit unbound documentados |
| `FAB` 72 | **0** | **0** pads unbound · `mot/padding/fab/*` |
| `Menu` 3 | **0** | nested kit pads densos unbound; `mot/spacing/menu/group-gap` |
| `Search bar` 6 | **0** | Content **20** / state-layer **-16** documentados |
| `Search full-screen layout` | **50** | Residual `semantic/padding\|spacing/list-item\|stack` — no SoT Compose |

Home: **0** FilterChip · **1** Menu · Search bar ×3.

## Re-audit live (2026-10-29, continuación B.3 post-Wallet CTA — cierre orden FilterChip→…→Search)

| Set | LEGACY | Notas |
|-----|-------:|-------|
| `App bar` 12 | **0** | MOT component-* + icon-button pads |
| `FAB` 72 | **0** | **0** pads unbound · `mot/padding/fab/{default,medium,large}` |
| `Menu` 3 | **0** | `mot/spacing/menu/group-gap` · `Corner/Large` |
| `Search bar` 6 | **0** | `Corner/Extra-large` · `mot/spacing/component-xs` |
| `Search full-screen layout` | **0** | Antes residual 50; live actual **0** binds `semantic/padding\|spacing/*` en set + mains de instancias (no SoT Compose; seguir sin productizar) |

Home design-file: **0** FilterChip · **1** Menu (`Filter menu` `228:8306`) · Search bar ×3 — paridad `HomeFilterDropdownMenu` / `HomeSearchBar`.
