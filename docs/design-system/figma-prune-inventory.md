# Figma DS — Inventario de poda (fuente de verdad)

**Única fuente de verdad** para qué conservar, podar y diferir en la librería Figma de MyOwnTrip.

| | |
|---|---|
| **Archivo** | [MyOwnTrip_nativo — Design System](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432) · `zrGAL4v6MEMc9hzZemU432` |
| **Backup (CS, no tocar)** | `uWmxOSQfjOxlEJ8k1yzOSX` |
| **Política** | Figma = librería visual · docs = showcase — [ADR 003](../decisions/003-figma-library-showcase-docs.md) |
| **Última revisión** | 2026-06-09 |

**Leyenda:** **KEEP** · **CUT** · **LATER** (v1.1 o cuando una pantalla lo pida)

**Docs relacionados (no duplicar criterio):**
- Ritual de poda manual → [`figma-manual-prune-checklist.md`](figma-manual-prune-checklist.md)
- [`figma-mobile-subset.md`](figma-mobile-subset.md) — redirige aquí (histórico 2026-06-08)

---

## Norte

- **Móvil + plegable (foldable)** en el bolsillo — sí en producto y en Figma. **Sin tablet, web ni XR.**  
  Breakpoints y frames de referencia → **[`breakpoints.md`](breakpoints.md)** (M3: Compact &lt;600dp, Medium 600–840dp; Fold interior ≈673–794dp; **no** patrones tablet aunque el Fold en landscape toque Expanded por dp).
- **Marca editorial** (ADR 002): Square / radio ~8dp · Material Symbols **Sharp** · Fraunces + Inter · ink + papel + ocre (`tertiary`, con moderación).
- **Poda por componentes/variantes**, nunca por roles de color (variables `M3` interdependientes).
- **Dos motivos de poda distintos:** (1) **marca/estilo** — ej. botones `Round`, chips `Elevated`; (2) **capacidad de diseño** — no quitar utilidades que un diseñador necesitaría recrear a mano (teclados, device frames). El CS backup no sustituye la librería publicada.
- **Compose:** estados interactivos vía state layers (`m3Canonical`); en Figma conservar ejes Type/State donde el diseño lo necesite, salvo excepciones de marca abajo.

### Variables `M3` (publicadas)

| Modo | Uso |
|------|-----|
| Light, Light High/Medium Contrast | Diseño + accesibilidad |
| Dark, Dark High/Medium Contrast | Diseño + accesibilidad |
| Pink/Rose/…/Purple, Monochrome | **CUT** (ya eliminados 2026-06-09) |

Código canónico: **Light + Dark** → [`variables.json`](variables.json) · [`color.md`](color.md).

### Reglas transversales (todas las páginas)

| Regla | Acción |
|-------|--------|
| `Context=Tablet`, layouts **tablet** (10"+), `-Web` | **CUT** |
| Window **Expanded** solo si es patrón tablet/desktop | **CUT** — foldable interior ≠ tablet |
| `XR/*`, sección Deprecated, Bottom app bar, Navigation rail* | **CUT** |
| Botones `Type=Round` (pill) | **CUT** — editorial = Square ~8dp |
| Botones `Size=XLarge` | **CUT** |
| Text field `Style=Filled` | **CUT** — Outlined |
| Chips `Style=Elevated` (en sets KEEP) | **CUT** — Outlined |
| List/Menu `Density -2/-4` | **CUT** |
| `Theme`/`Type=Vibrant`, `Type=Wave`, morphing Expressive | **CUT** |
| 3D Avatars, Shape Set decorativo (cookie/heart/…) | **CUT** |
| Roles/variables de color | **NUNCA podar** |

---

## Tiers de componentes (acordado)

### MVP Must — KEEP

| Área | Sets |
|------|------|
| **Foundations** | Variables `M3`, `.Tonal palettes`, `Styles` (`.Shape`), `Icons` (Sharp) |
| **App bars** | Top app bar + BB (sin Bottom app bar, sin XR) |
| **Badges** | Badge Large + Small |
| **Buttons** | filled, tonal, outline, text, **elevated**; Icon button (4); FAB, Extended FAB; **Toggle** (4) — solo `Type=Square` |
| **Cards** | Stacked, Horizontal + BB states — **Outlined + Elevated**; layout **Media & text** |
| **Checkboxes** | Completos |
| **Chips** | Filter, Assist, Suggestion, Input — **Outlined** |
| **Date pickers** | Input + Modal + BB calendario (sin time pickers) |
| **Dialogs** | Basic dialog |
| **Lists** | List item `Density 0` + BB |
| **Loading** | Linear/Circular `Type=Flat` + Loading indicator BB (sin morphing Steps) |
| **Menu** | `Density 0`, `Theme=Standard` + BB |
| **Navigation** | Navigation Bar H/V + BB |
| **Radio / Switch** | Completos |
| **Search** | **Search bar** solamente |
| **Sheets** | Bottom sheet + BB |
| **Snackbar** | Snackbar + BB |
| **Tabs** | Primary + **Secondary** + BB |
| **Text fields** | Outlined, todos los estados |
| **Utilities** | Scrim, Focus indicator, status-bar, navigation gestures, Device frame, **Keyboard Portrait + Landscape** |

### Should — KEEP (diseño rico, no bloqueante en código)

| Área | Sets |
|------|------|
| **Buttons** | FAB menu + BB; Segmented button + BB; Icon button togglable (4) — **no** button groups |
| **Chips** | Chip groups |
| **Dialogs** | Scrollable list dialog |
| **Lists** | Accordion, List item Swipe + BB |
| **Sliders** | Standard, Centered, Range — Horizontal, Small/Medium |

### v1.1 — LATER (no podar del CS hasta que haga falta; no publicar aún)

| Item | Motivo |
|------|--------|
| Search full-screen / docked layout | Búsqueda expandida |
| Side sheet + BB | Panel lateral |
| Navigation drawer (patrón M3 actual) | Sin cambio de arquitectura nav |
| Carousel (Hero, Multi-browse) | Galería de sitios |
| Toolbars Expressive | Toolbar flotante + FAB |
| Rich tooltip | Sobre Plain |
| Time pickers (Dial, Keyboard…) | Recordatorios / hora vuelo |

### CUT — páginas y sets enteros

| Página / patrón |
|-----------------|
| Getting started, Table of contents, `---` |
| **Examples** (`-Web`, window classes **tablet**; opcional: grid Compact + 1–2 refs foldable interior en `Reference`) |
| **Shape** — Shape Set expresivo (conservar escala de tokens en `Styles`) |
| Doc/demo suelta en Utilities (no teclados) |
| **Avatars** (genéricos del kit) |
| **Tooltips** |
| Carousel `Context=Tablet`, Toolbars XR/Vibrant/Vertical |
| Split button, Connected/Standard button group |
| List dialog (no scrollable) si existe Scrollable |
| Time pickers desktop (Docked, Horizontal dial) |
| Card `Style=Filled` · Bottom app bar |

---

## Inventario por página (node IDs)

### Utilities · `55594:2484`
- **KEEP:** Scrim · Focus indicator · Slot-component · status-bar · navigation gestures · Device frame (1)
- **KEEP:** Keyboard **Portrait** (4) · **Landscape** (4) — mockups con teclado (móvil vertical, plegable desplegado / horizontal).
- **CUT:** Keyboard **Floating** (4) — patrón multitarea tipo tablet, fuera de scope.

### Avatars · `55595:3788`
- **CUT:** página entera (3D + Generic)

### Icons · `55594:2483`
- **KEEP:** librería Sharp w300 (~140)
- **ACCIÓN:** dedup manual de nombres duplicados (cosmético)

### Examples · `55594:2480`
- **CUT:** `-Web` (7) · Messaging, Reviews · `Examples/Layout grid` M3 (`56384:120`, 11 variantes tablet/desktop) — **reemplazado** por `Layout grid · mobile` (`60953:132824`, 6 variantes + landscape smoke; doc sección `55343:13515`)
- **KEEP:** `Layout grid · mobile` — 360×800 base · 360×880 Flip stress · 344×880 min width · 412×915 grande · 673×841 Medium LATER · 800×360 landscape smoke; guía `60954:132843`
- **Reference:** Flex Window `60955:132903` (260×272 cover Flip) — **CUT MVP**
- **OPCIONAL:** fold landscape 840×673 en página `Reference` (frame suelto, no component set)

### Shape · `58548:7093`
- **CUT:** Shape Set expresivo (35). Radios funcionales viven en `Styles`/tokens.

### App bars · `55141:14169`
- **KEEP:** Top App bar (12 variantes útiles)
- **CUT:** XR (6) · Bottom app bar

### Badges · `55141:14167` — **KEEP** (2)

### Buttons · `55141:14168` — 🚨 mayor peso
- **KEEP (MVP+Should):** Button, Icon button, FAB, Toggle, Segmented, FAB menu, Icon togglable — `Type=Square`, sin XLarge
- **CUT:** `Type=Round` (~1.260) · XLarge · Split · button groups

### Cards · `55141:14171`
- **KEEP:** Stacked + Horizontal, Outlined + Elevated, Media & text
- **CUT:** Filled

### Carousel · `55141:14172` — **LATER** móvil · **CUT** Tablet

### Checkboxes · `55141:14173` — **KEEP**

### Chips · `55141:14174`
- **KEEP:** Filter, Assist, Suggestion, Input, Chip groups — Outlined
- **CUT:** Elevated

### Date & time pickers · `55141:14175`
- **KEEP (MVP):** Input + Modal + BB calendario
- **CUT:** Docked desktop, Horizontal dial
- **LATER:** time pickers

### Dialogs · `55141:14176`
- **KEEP:** Basic · Scrollable list dialog
- **CUT:** XR (4) · list dialog no scrollable

### Dividers · `55141:14177` — **KEEP** (7)

### Lists · `55141:14249` — 🚨 peso
- **KEEP:** Density 0 · Accordion · Swipe + BB
- **CUT:** Density -2/-4

### Loading & progress · `55141:14252`
- **KEEP:** Linear/Circular Flat · Loading indicator BB
- **CUT:** Wave · Steps morphing 1–7

### Menu · `55141:14250` — **KEEP** Standard d0 · **CUT** -2/-4, Vibrant

### Navigation · `55141:14251`
- **KEEP:** Navigation Bar
- **CUT:** Rail, Expanded, XR, Deprecated drawer

### Radio · `55141:14253` · Switch · `55141:14257` — **KEEP**

### Search · `55141:14254`
- **KEEP:** Search bar
- **LATER:** full-screen · **CUT:** docked desktop

### Sliders · `55141:14255` — **KEEP (Should):** Standard, Centered, Range
- **CUT:** Vertical, XLarge

### Snackbar · `55141:14256` — **KEEP**

### Tabs · `55141:14258` — **KEEP** Primary + Secondary

### Text fields · `55141:14259` — **KEEP** Outlined · **CUT** Filled

### Toolbars · `58295:22726` — **LATER** · **CUT** XR, Vibrant, Vertical

### Tooltips · `55141:14261` — **CUT**

### Sheets · `55141:14170`
- **KEEP:** Bottom sheet + BB
- **CUT:** Side sheet · **LATER** v1.1

---

## Focos de peso (orden de ejecución)

1. Buttons — Round + XLarge (~1.300 variantes)
2. Lists — densidades -2/-4
3. Navigation — Rail + Expanded + XR + Deprecated
4. Text fields — Filled (~60)
5. Chips — Elevated + sets LATER si se difieren
6. Examples web + window classes grandes
7. 3D Avatars · Shape Set · Tooltips

### Ritual

Ver pasos en [`figma-manual-prune-checklist.md`](figma-manual-prune-checklist.md) §6.

1. Doc/demos → 2. Grande/XR/Deprecated → 3. Decorativo Expressive → 4. Trim variantes KEEP → 5. Verificar bindings `M3` → 6. Publicar

---

## Sincronización

| Artefacto | Acción |
|-----------|--------|
| [`components.md`](components.md) | Prioridades P0–P2 |
| [`ds-showcase/src/data/components.json`](../../ds-showcase/src/data/components.json) | Fichas KEEP |
| [`audits/`](audits/) | Nota de trazabilidad tras cada bloque de poda |

## Estado librería

| Fecha | Hito |
|-------|------|
| 2026-06-09 | Variables `M3` publicadas (6 modos) · ~8208 elementos pre-poda |
| — | Poda manual en curso (este inventario) |
