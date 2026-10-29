# Fase B.3 — Filter chip (+ Assistive)

**Fecha:** 2026-10-29  
**Sets:** `Filter chip` `53923:28270` (48 · Outlined only) · `Assistive chip` `53923:28089` (24)  
**Compose:** `FilterChip` / `AssistChip` M3 directo → `MaterialTheme.shapes.small` (8)  
**Consumidores SoT:** `WalletScreen` (`WalletDocumentFilterChips`), `WalletFormScreen` (adjuntos), Journal/Gastos/Restaurantes/Places

## Compose SoT

| Aspecto | Valor |
|---------|--------|
| API | `FilterChip` (sin wrapper MOT) |
| Shape | `MaterialTheme.shapes.small` = 8dp ↔ `Corner/Small` |
| Pads internos | M3 kit: H 8/16 · V **6** · gap 8 (no `MOTSpacing` en runtime) |
| Tipo | `labelLarge` (defaults M3 + `AppTypography`) |
| Color | `colorScheme` (secondary container selected, outline, onSurface…) |
| Gap entre chips (pantalla) | `MOTSpacing.componentSm` (8) · padding fila `screenHorizontal` / `componentXs` |

Home lista de viajes **no** usa `FilterChip` (filtro = `HomeFilterDropdownMenu` / Menu). Wallet sí.

## Paridad shape

| Compose | Figma |
|---------|-------|
| `shapes.small` · 8dp | `Corner/Small` |

No usar `Corner/Extra-small` (eso es Text field / `shapes.extraSmall`).

## Cambios (set DS)

| Antes (LEGACY / literal) | Después |
|--------------------------|---------|
| `semantic/radius/chip` | **`Corner/Small`** |
| `semantic/spacing/stack/sm` (pads H) | **`mot/padding/chip/horizontal-sm`** (8) |
| `semantic/spacing/stack/lg` (pads H) | **`mot/padding/chip/horizontal-lg`** (16) |
| `semantic/spacing/gap/list` | **`mot/spacing/chip/gap`** (8) |
| `paddingTop/Bottom` literales 6 | **`mot/padding/chip/vertical`** (6) |

Tokens MOT: `mot/padding/chip/{horizontal-sm,horizontal-lg,vertical}`, `mot/spacing/chip/gap` · strokes `mot/stroke/hairline|focus` (Fase E).

**Nota gap plan:** “pad vertical M3 6 vs token 8” → cerrado con **6** (`mot/padding/chip/vertical`), alineado a M3 FilterChip.

## Re-verificación (2026-10-29, sesión B.3 continuación)

Live en DS `zrGAL4v6` + design-file Wallet (reconfirmado en re-entrada B.3 post-TextField):

| Gate | Filter chip (48) | Assistive (24) | Wallet design-file |
|------|-----------------:|---------------:|--------------------|
| LEGACY | **0** | **0** | — |
| pads/corners contrato unbound | **0** | **0** | filas `491:14541` / `491:14556` |
| inventos FRAME chip | — | — | **0** |
| instancias | — | — | **3/3** Outlined Label only · Activos Selected |

### Re-entrada post-TextField (misma fecha, turn B.3 orden)

Re-audit plugin: **0 LEGACY**; pads MOT `chip/*` + `Corner/Small` en las **48+24** variantes.  
Nota: un detector que mire solo `boundVariables.cornerRadius` da falsos positivos — el set bindea los **4** radios individuales (`topLeftRadius`…), no el campo uniforme.

Wallet `04 · Wallet`: **6** instancias `Filter chip` (caps 1–2 × 3) · **0** inventos en filas `491:14541` / `491:14556`. Home lista sigue en Menu, no FilterChip.

### Re-audit live (2026-10-29, sesión B.3 continuación post-Wallet CTA)

Plugin Desktop Bridge sobre DS `zrGAL4v6` + design-file `Vf2tNMXy`:

| Gate | Resultado |
|------|-----------|
| Filter chip 48 | **0** LEGACY · **0** pads unbound · **0** corner ≠ `Corner/Small` |
| Assistive 24 | igual |
| Vars contrato | `mot/padding/chip/*` · `mot/spacing/chip/gap` · `mot/stroke/hairline\|focus` · Schemes M3 · Static/Label Large |
| Wallet filas | `491:14541` / `491:14556` · **3/3** INSTANCE Outlined Label only · Activos `Selected=True` · binds fila `mot/spacing/component-sm` + `screen-horizontal` + `component-xs` |
| Inventos | **0** |

SoT Compose intacto: `WalletDocumentFilterChips` → `FilterChip` + `MOTSpacing.componentSm` gap; shape `shapes.small`=8.

### Re-audit live (2026-10-29, orden B.3 post-TextField — FilterChip primero)

Misma sesión de continuación B.3 (Compose SoT → set → design-file):

| Gate | Resultado |
|------|-----------|
| Filter chip 48 | **0** LEGACY · **0** pads/corners contrato unbound · **0** corner ≠ `Corner/Small` |
| Assistive 24 | **0** LEGACY · pads MOT en `state-layer` · residual unbound = Colourful logo `Rectangle 2` + Focus indicator (documental/estructural) |
| Vars Filter | `Corner/Small` · `mot/padding/chip/*` · `mot/spacing/chip/gap` · `mot/stroke/hairline\|focus` · Schemes* · State Layers* · Static/Label Large/* |
| Wallet | **6** instancias set `Filter chip` · Label only · Activos `Selected=True` · copy Activos/Archivados/Todos · filas binds `mot/spacing/{component-sm,screen-horizontal,component-xs}` · **0** inventos |
| Home | sigue Menu (`HomeFilterDropdownMenu`), no FilterChip — alineado a Compose |

### Re-audit live (2026-10-29, sesión post-Wallet CTA — orden B.3 FilterChip primero)

Desktop Bridge DS `zrGAL4v6` + design-file `Vf2tNMXy`:

| Gate | Filter chip 48 | Assistive 24 | Wallet `04 · Wallet` |
|------|---------------:|-------------:|----------------------|
| LEGACY | **0** | **0** | — |
| pads/corners contrato unbound | **0** | **0** | filas `491:14541` / `491:14556` |
| corner ≠ `Corner/Small` | **0** | **0** | — |
| inventos | — | — | **0** (3/3 INSTANCE × 2 caps) |

Vars Filter: `Corner/Small` · `mot/padding/chip/{horizontal-sm,horizontal-lg,vertical}` · `mot/spacing/chip/gap` · `mot/stroke/hairline\|focus` · Schemes* · State Layers* · Static/Label Large/*.  
Wallet: Label only · trailing=False · Activos `Selected=True` · copy Activos/Archivados/Todos · fila binds `mot/spacing/{component-sm,screen-horizontal,component-xs}`.  
Icon vectors: `Schemes/On Surface` (M3) — **0** `semantic/color/text/*`.

## Evidencia `resolveVariables` (2026-10-29 re-audit)

Muestra Enabled · Label only · Selected=False:

| Nodo | Binds |
|------|-------|
| COMPONENT root | `Corner/Small` ×4 · `mot/stroke/hairline` · `Schemes/Outline Variant` |
| `state-layer` | pads `mot/padding/chip/horizontal-lg` + `vertical` · gap `mot/spacing/chip/gap` · `Corner/Small` |
| `label-text` | Schemes On Surface Variant + Static/Label Large/* |

Gate contrato (excl. glifos VECTOR, Focus indicator INSTANCE, Colourful logo Assistive):

| Set | Variantes | LEGACY | pads unbound | corner unbound |
|-----|----------:|-------:|-------------:|---------------:|
| Filter chip | 48 | **0** | **0** | **0** |
| Assistive chip | 24 | **0** | **0** | **0** |

Documental / estructural (aceptado, Fase E): chrome `COMPONENT_SET`, strokes de glifo, Focus indicator radius, logo arcoíris Assistive.

Icon fills en vectors bajo `/icon/`: `Schemes/On Surface` (M3) — **0** binds `semantic/color/text/*`.

### Re-audit live (2026-10-29, post-Wallet CTA — reentrada orden B.3 FilterChip→…)

Desktop Bridge DS `zrGAL4v6` + design-file `Vf2tNMXy` (sesión continuación post-Wallet Import CTA):

| Gate | Filter chip 48 | Assistive 24 | Wallet `04 · Wallet` | Home `01 · Home` |
|------|---------------:|-------------:|----------------------|------------------|
| LEGACY | **0** | **0** | — | — |
| pads/corners contrato unbound | **0** | **0** | filas OK | — |
| corner ≠ `Corner/Small` | **0** | **0** | — | — |
| inventos / FilterChip | — | — | **0** (6× INSTANCE) | **0** FilterChip · **4** Menu |

`resolveVariables` muestra Enabled · Label only · Selected=False: root `Corner/Small` ×4 · `mot/stroke/hairline` · `Schemes/Outline Variant`; `state-layer` pads `mot/padding/chip/{horizontal-lg,horizontal-sm,vertical}` + `mot/spacing/chip/gap`; `label-text` Schemes On Surface Variant + Static/Label Large/*.

Wallet caps 1–2: copy Activos/Archivados/Todos · Activos `Selected=True` · Label only · fila binds `mot/spacing/{component-sm,screen-horizontal,component-xs}`. Home filtros = Menu (`HomeFilterDropdownMenu`), no FilterChip — paridad Compose.

### Re-audit live (2026-10-29, post-Wallet Import CTA reparado — orden B.3 FilterChip primero)

Desktop Bridge DS `zrGAL4v6` + design-file `Vf2tNMXy` (sesión tras fix WalletImportReviewStep CTA):

| Gate | Filter chip 48 | Assistive 24 | Wallet `04 · Wallet` | Home `01 · Home` |
|------|---------------:|-------------:|----------------------|------------------|
| LEGACY | **0** | **0** | — | — |
| pads/corners contrato unbound | **0** | **0** | filas OK | — |
| corner ≠ `Corner/Small` | **0** | **0** | — | — |
| inventos / FilterChip | — | — | **0** (6× INSTANCE) | **0** FilterChip · **1** Menu (`Filter menu`) |

`resolveVariables` muestra Enabled · Label only · Selected=False: root `Corner/Small` ×4 · `mot/stroke/hairline` · `Schemes/Outline Variant`; `state-layer` pads H 16/8 · V **6** · gap 8 (`mot/padding|spacing/chip/*`); `label-text` Schemes On Surface Variant + Static/Label Large/*.  
Icon vectors: **0** binds `semantic/color/text/*`.

### Re-audit live (2026-10-29, continuación B.3 post-Wallet Import CTA — FilterChip primero)

Desktop Bridge DS `zrGAL4v6` + design-file `Vf2tNMXy` (sesión tras Wallet CTA Confirmar sin icono):

| Gate | Filter chip 48 | Assistive 24 | Wallet `04 · Wallet` | Home `01 · Home` |
|------|---------------:|-------------:|----------------------|------------------|
| LEGACY | **0** | **0** | — | — |
| pads/corners contrato unbound | **0** | **0*** | filas OK | — |
| corner ≠ `Corner/Small` | **0** | **0** | — | — |
| inventos / FilterChip | — | — | **0** (6× INSTANCE) | **0** FilterChip · **1** Menu (`Filter menu`) + Search bar ×3 |

\*Assistive residual unbound corners = `Rectangle 2` logo arcoíris (documental/estructural Fase E).

`resolveVariables` Enabled · Label only · Selected=False: root `Corner/Small` ×4 · `mot/stroke/hairline` · `Schemes/Outline Variant`; `state-layer` pads H 16/8 · V **6** · gap 8; `label-text` On Surface Variant + Static/Label Large/*. Selected=True: fill `Schemes/Secondary Container` · label `On Secondary Container`. Icon Vector: `Schemes/On Surface` — **0** `semantic/color/text/*`.

Wallet caps 1–2 filas `491:14541` / `491:14556`: Activos/Archivados/Todos · Activos `Selected=True` · Label only · trailing=False · fila binds `mot/spacing/{component-sm,screen-horizontal,component-xs}`. Home filtros = Menu (`HomeFilterDropdownMenu`), no FilterChip — paridad Compose `WalletDocumentFilterChips`.

## Design-file (gap cerrado)

Wallet caps **sin** fila de filtros Compose → se añadieron instancias DS:

| Cap | Frame | Chips |
|-----|-------|-------|
| cap 1 — con documentos | `Filter chips` `491:14541` tras Quick actions | Activos (Selected) · Archivados · Todos |
| cap 2 — vacío | `Filter chips` `491:14556` tras Quick actions | igual |

Fila: auto-layout H · binds `mot/spacing/component-sm` (gap) · `screen-horizontal` · `component-xs`.  
**3/3** hijos = `INSTANCE` set `Filter chip` (Outlined). Día & itinerario ya tenía 6 instancias Outlined (Journal sheets).

## Checklist

```
[x] Composable SoT identificado (Wallet + forms)
[x] Solo Outlined (Elevated CUT Fase D)
[x] Radius `Corner/Small` ↔ `shapes.small`
[x] 0 binds LEGACY en Filter + Assistive
[x] Pads/gaps MOT · Spacing
[x] Tipografía Label Large / schemes M3
[x] Fila bindings actualizada
[x] Design-file Wallet: instancias Filter chip (no inventos)
[x] 0 bindables de contrato pendientes
```
