# Figma DS — Inventario de poda (fuente de verdad)

**Única fuente de verdad** para qué conservar, podar y diferir en la librería Figma de MyOwnTrip.

| | |
|---|---|
| **Archivo** | [MyOwnTrip_nativo — Design System](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432) · `zrGAL4v6MEMc9hzZemU432` |
| **Backup (CS, no tocar)** | `uWmxOSQfjOxlEJ8k1yzOSX` |
| **Política** | Figma = librería visual · docs = showcase — [ADR 003](../decisions/003-figma-library-showcase-docs.md) |
| **Última revisión** | 2026-06-11 |

**Leyenda:** **KEEP** · **CUT** · **LATER** (v1.1 o cuando una pantalla lo pida)

**Docs relacionados (no duplicar criterio):**
- Ritual de poda manual → [`figma-manual-prune-checklist.md`](figma-manual-prune-checklist.md)
- [`figma-mobile-subset.md`](figma-mobile-subset.md) — redirige aquí (histórico 2026-06-08)

---

## Norte

- **Móvil + plegable (foldable)** en el bolsillo — sí en producto y en Figma. **Sin tablet, web ni XR.**  
  Breakpoints y frames de referencia → **[`breakpoints.md`](breakpoints.md)** (M3: Compact &lt;600dp, Medium 600–840dp; Fold interior ≈673–794dp; **no** patrones tablet aunque el Fold en landscape toque Expanded por dp).
- **Marca editorial** (ADR 002): Material Symbols **Sharp** · Fraunces + Inter · ink + papel · acento rojo señal (`tertiary` `#D9382C`, con moderación).
- **Shape botones** (ADR 004): reposo **0dp** (`Corner/None`) · morph a **20dp** (`Corner/Large-increased`) en hover / focus / pressed / selected · ver [shape.md](shape.md).
- **Shape morph** (subset Expressive): siempre **None → Large-increased**; **nunca** Round/20dp por defecto en reposo.
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
| Button `Type=Round` (pill, reposo) | **CUT** — reposo = Square 0dp (ADR 004) |
| Toggle / Icon togglable / Segmented `Round` | **KEEP** — geometría **destino** del morph 0→20dp (`Corner/Large-increased`) |
| Botones `Size=XLarge` | **CUT** |
| Text field `Style=Filled` | **CUT** — Outlined |
| Chips `Style=Elevated` (en sets KEEP) | **CUT** — Outlined |
| List/Menu `Density -2/-4` | **CUT** |
| `Theme`/`Type=Vibrant`, `Type=Wave`, morphing Expressive | **CUT** |
| Shape Set decorativo (cookie/heart/arch…) — **no** la escala `.Shape` en `Styles` | **CUT** |
| Avatars 3D / genéricos extra | **CUT** — conservar **Generic avatar** básico (Avatar/Monogram) |
| Roles/variables de color | **NUNCA podar** |

---

## Tiers de componentes (acordado)

### MVP Must — KEEP

| Área | Sets |
|------|------|
| **Foundations** | Variables `M3`, `.Tonal palettes`, `Styles` (`.Shape`), `Icons` (Sharp) |
| **App bars** | Top app bar + BB (sin Bottom app bar, sin XR) |
| **Badges** | Badge Large + Small |
| **Buttons** | filled, tonal, outline, text, **elevated** — **Square** only; Icon button (4) Round+Square; FAB; **Toggle** (4) Round+Square; Segmented Round+Square |
| **Cards** | Stacked, Horizontal + BB states — **Outlined + Elevated**; layout **Media & text** |
| **Checkboxes** | Completos |
| **Chips** | Filter, Assist, Suggestion, Input — **Outlined** |
| **Date & time pickers** | Fecha B+ + hora móvil (Keyboard, Dial vertical) — reserva manual |
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
| **Toolbars** | Horizontal **Standard** (Floating/Docked) + BB Standard — acciones contextuales MVP |
| **Utilities** | Scrim, Focus indicator, status-bar, navigation gestures, Device frame, **Keyboard Portrait + Landscape** |

### Should — KEEP (diseño rico, no bloqueante en código)

| Área | Sets |
|------|------|
| **Buttons** | FAB menu + BB; Segmented button + BB; Icon button togglable (4) — **no** button groups |
| **Chips** | Chip groups |
| **Dialogs** | Scrollable list dialog |
| **Lists** | Accordion, List item Swipe + BB |
| **Sliders** | Standard, Centered, Range — Horizontal, XSmall–Large — **hecho** (CUT Vertical/XLarge) |

### v1.1 — LATER (no podar del CS hasta que haga falta; no publicar aún)

| Item | Motivo |
|------|--------|
| Search full-screen / docked layout | Búsqueda expandida |
| Side sheet + BB | Panel lateral |
| Navigation drawer (patrón M3 actual) | Sin cambio de arquitectura nav |
| Carousel móvil (Hero, Multi-aspect…) | Solo JTBD **galería de fotos** — no viajes/POIs/explorar |
| Rich tooltip | Sobre Plain |
| Recordatorios push / alertas hora | Fuera de picker UI |

### CUT — páginas y sets enteros

| Página / patrón |
|-----------------|
| Getting started, Table of contents, `---` |
| **Examples** (`-Web`, window classes **tablet**; opcional: grid Compact + 1–2 refs foldable interior en `Reference`) |
| **Shape** (página) — Shape Set expresivo 35 var. (cookie/arch/…) · **KEEP** escala en `Styles` → `.Shape` |
| Doc/demo suelta en Utilities (no teclados) |
| **Avatars** — solo 3D u otros genéricos extra · **KEEP** `Generic avatar` (básico, adaptable a marca) |
| **Tooltips** |
| Carousel `Context=Tablet` · `Carousel - Full screen` · Toolbars XR/Vibrant/Vertical |
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
- **KEEP:** `Generic avatar` (`50731:13725`) — Avatar · Monogram · Check; base para diseñadores (adaptable a marca MyOwnTrip)
- **CUT:** 3D u otros sets si reaparecen del CS

### Icons · `55594:2483`
- **KEEP:** librería Sharp w300 (~140)
- **ACCIÓN:** dedup manual de nombres duplicados (cosmético)

### Examples · `55594:2480`
- **CUT:** `-Web` (7, eliminados 2026-06-09) · `Examples/Layout grid` M3 (`56384:120`, 11 variantes tablet/desktop) — **reemplazado** por `Layout grid · mobile` (`60955:133047`, 6 variantes; sección `55343:13515`)
- **KEEP:** `Example layout` (`56554:638`) — 7 pantallas × 3 filas Compact: **Phone base** 360×800 · **Z Fold plegado** 344×880 · **landscape smoke** 800×360 (reflow horizontal, sin recorte a media pantalla); instancias del kit M3
- **KEEP:** `Layout grid · mobile` — 360×800 · 360×880 · 344×880 · 412×915 · 800×360 smoke · 673×841 LATER; guía `60954:132843`
- **REVIEW:** Messaging, Reviews — mantener en Examples hasta decisión producto; no son target JTBD MVP
- **Reference** (página consulta, no MVP): guía `61053:230239` · Flex Window `60955:132903` · Fold landscape A `61053:230253` (2 cols) · Fold landscape B `61053:230281` (mapa + nav/toolbar adaptativos)

### Shape · `58548:7093` vs `Styles` · `.Shape` · `55343:12390`

**No confundir** (ver [M3 corner radius scale](https://m3.material.io/styles/shape/corner-radius-scale)):

| Qué | Dónde | Acción |
|-----|-------|--------|
| **Escala de radios** (None → Full, tokens Shape 1–9) | Página **Styles** → set `.Shape` | **KEEP** — enlazan componentes M3; botones **None 0dp** + morph **20dp**; cards **Medium 12dp**; chips **Small 8dp** (ADR 004) |
| **Shape library** decorativa (Circle, Arch, Fan, cookie…) | Página **Shape** → `Shape Set` (35) | **CUT** — “momentos de delight” visuales; no sustituye la escala; texto denso → evitar |

- **CUT:** página **Shape** entera (solo el set demostrativo). **No** borrar `.Shape` en Styles.

### App bars · `55141:14169`
- **KEEP:** Top App bar (12 variantes útiles)
- **CUT:** XR (6) · Bottom app bar

### Badges · `55141:14167` — **KEEP** (2)

### Buttons · `55141:14168` — **hecho** (2026-06-10) · binding shape **hecho Figma** (2026-06-12, Bridge)
- **Button** (5 estilos): **Square** only en reposo · XSmall–Large · State completo (−100 Round estático)
- **ADR 004:** corner Square → `Corner/None` (0) · Round destino → `Corner/Large-increased` (20) — binding aplicado en librería (auditoría 2026-06-12)
- **Icon button** (4) + **Icon togglable** (4): Square **+** Round (morph destino 20dp) · sin XLarge
- **Toggle** (4): Square **+** Round (morph) · sin XLarge
- **Segmented** + BB + **FAB menu** + FAB/Extended: KEEP · FAB = circular (excepción); segmented Square+Round
- **XLarge:** eliminado en todos los sets (−410)
- **CUT:** Split button (144) · Connected + Standard button groups + BB Connected (86) — **hecho**
- **Checklist binding:** [shape.md](shape.md) § Binding Figma

### Cards · `55141:14171` · [Stacked card](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=52346-27573)

- **KEEP:** Stacked + Horizontal · **Outlined + Elevated** · **CUT Filled** — **hecho** (−9)
- **Stacked · Layout** (eje `Layout` en component set):
  | Layout | Contenido | Uso producto |
  |--------|-----------|--------------|
  | `Media & text` | Header (avatar) + media + copy + acciones | Viaje destacado, card editorial completa |
  | `Text only` | **Header** (`Header` + `Subhead`) + **close** · cuerpo = solo `Supporting text` · sin avatar, sin Headline/Title, sin acciones | Mensaje descartable (promo, aviso) |
  | `Media only` | **Solo imagen** (fill persistente, ~16:9) | Decoración / portada cuando no hay copy |
  | `Slot` | Composición custom | Prototipo; no abusar en MVP |
- **Horizontal · Layout:** `Media & text` · `Only Text` · `Slot` (sin `Media only` — patrón fila compacta)
- **Regla superficie:** Outlined = listas/feeds; Elevated = destacados; **nunca Filled**
- **TripHeroCard** · [component set](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61199-7862) (no variante de Stacked):
  | Parte | Contenido |
  |-------|-----------|
  | Tamaño | **360×368** (portada 280 + CTA tonal 48 + gap) |
  | Superficie | `Style=Elevated` (default Home) · `Outlined` |
  | Portada | `Background` (image fill) + `Scrim` + **Eyebrow label** (`Color=Tertiary, Size=Medium`) + `Content` (countdown, title, meta) |
  | CTA | **Button - tonal** XSmall «Ver detalles» **bajo** la imagen — **única acción** |
  | Properties | `Countdown text`, `Show countdown`, `Title text`, `Meta text` |
  | Eyebrow / CTA label | Eyebrow: property `Label text` en instancia Eyebrow label · CTA: editar en instancia Button |
  | Uso | Home — viaje destacado |

### Labels · página [61202:16812](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61202-16812)

Etiquetas **informativas no interactivas**. No sustituyen chips (filtros/acciones).

| Componente | Variantes | Uso producto |
|------------|-----------|--------------|
| **Eyebrow label** · [61202:16834](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61202-16834) | `Color` = Tertiary · Surface · Secondary × `Size` = Medium · Small | Fase/contexto sobre media (TripHeroCard) o surface plana |
| Property | `Label text` (TEXT) | «Próximo viaje», «En destino», «Recuerdo», etc. |
| Sizing | **HUG** — ancho según copy + padding; **no** ancho fijo ni FILL del padre | En instancia dentro de TripHeroCard: posicionar top-start con padding del card |

**Regla:** `Color=Tertiary` sobre fotos/scrims. Chips Assist **Outlined** solo en superficie plana e interactivos. Eyebrow label **sin** onClick en producto.

### Design-file · Shell Home · `00 · Shell & transversal`

Flujo **[205:813](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=205-813)** · `route: trip_list`

| Cap | Estado | Notas |
|-----|--------|-------|
| cap 1 · Home vacío | [205:816](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=205-816) | Search bar placeholder «Buscar destinos o viajes» |
| cap 2 · Home con viajes | [205:1018](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=205-1018) | TripHeroCard + Wallet + Horizontal cards · **sin** chips metadata |
| cap 3 · búsqueda + menú | [228:8161](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=228-8161) | Clone cap 2 · Search Pressed · Menu overlay 328dp — ver [`patterns/home-filter-menu.md`](patterns/home-filter-menu.md) |

Sesión y pendientes: [`docs/sessions/2026-06-17-home-ds-figma.md`](../sessions/2026-06-17-home-ds-figma.md)

### Carousel · `55141:14172`

**Regla producto (2026-06):** carrusel **solo para imágenes** — p. ej. galería «mis fotos guardadas» cuando exista esa pantalla. **No** para viajes, POIs, explorar ciudad ni home editorial; ahí lista vertical + card (outlined/elevated). Filas horizontales de cards en Examples ≠ set «Carousel» de Figma.

| Set / variante | Acción |
|----------------|--------|
| `Carousel` · `Context=Mobile` (Hero, Multi-browse, Uncontained, Center-aligned hero, Multi-aspect ratio) | **LATER** — solo si hay JTBD galería de fotos; plantillas M3 en librería, no roadmap de patrones |
| `Carousel` · `Context=Tablet` (todas) | **CUT** |
| `Carousel - Full screen` | **CUT** — patrón inmersivo desktop/tablet |

MVP: sin pantalla galería cerrada → **no diseñar con estos sets**; backup CS + preview [`previews/carousel-mobile-comparison.html`](previews/carousel-mobile-comparison.html).

### Checkboxes · `55141:14173` — **KEEP**

### Chips · `55141:14174`
- **KEEP:** Filter, Assist, Suggestion, Input, Chip groups — Outlined
- **CUT:** Elevated

### Date & time pickers · `55141:14175`
- **Sección `MVP · Date pickers`:** Input Range + Single · Modal (Day, Full-screen range, Year) + BB calendario
- **UX fecha (B+ híbrido):** ida–regreso · preformato manual · calendario auto-regreso
- **Sección `LATER · Time pickers`:** **no podar** — etiquetar solo. Keyboard · Dial vertical · BB Hour, Period Selector, Input, Direct Input, Clock faces (reserva manual)
- **CUT aplicado (time desktop):** Dial horizontal (2) · Period Selector Horizontal · hour-line (24)
- **CUT fecha desktop:** Docked input date [desktop] — **hecho**
- **Política LATER:** etiqueta de sección visible · **nunca borrar** del archivo · CUT solo variantes desktop explícitas

### Dialogs · `55141:14176` — **hecho**
- **MVP:** Basic dialog (2) · Scrollable list dialog (2)
- **LATER:** List dialog (2) — listas cortas fijas · sección etiquetada, no borrar
- **CUT:** XR/XR Dialog (4) — fuera scope móvil

### Dividers · `55141:14177` — **hecho**
- **MVP:** 7 componentes (horizontal + vertical · full-width, inset, middle-inset, subhead)

### Lists · `55141:14249` — **hecho**
- **MVP:** List item density **0** (238) · List (baseline) · List/Accordion/Swipe sets + BB (sección patterns)
- **CUT:** List item density **-2** (217) · **-4** (197) · List -2/-4 baseline — **hecho**

### Loading & progress · `55141:14252` — **hecho**
- **MVP:** Linear/Circular determinate + indeterminate · **Type=Flat** (4dp/8dp) · BB flat/track/stop
- **LATER:** Loading indicator Steps 1–7 (morphing) — etiquetado, no borrar
- **CUT:** Type=Wave en los 4 progress sets (34 var.) · BB Segment wave (4 var.) — **hecho**

### Menu · `55141:14250` — **hecho**
- **MVP:** Menu density **0** · Theme **Standard** · Menu item/Standard + BB
- **CUT:** density -2/-4 (BB + baseline variants) · Theme **Vibrant** + Menu item/Vibrant — **hecho**
- **Selección en menú (producto):** ítem activo = leading slot reservado + **`check`** al seleccionar — trailing/chevron **oculto** (sin submenú) · **no** `radio_button_checked`
- **Script:** `scripts/figma-menu-item-selection-filter.js` — corrige component set + instancia Filter menu
- **Deuda kit:** variante `Selected=True` del Menu-item publicado aún enlaza `radio_button_checked` en trailing → corregir a leading `check` antes de próxima publicación librería
- **Patrón Home filtros:** [`patterns/home-filter-menu.md`](patterns/home-filter-menu.md) · design-file cap 3 · script `figma-design-file-home-cap3-search-filters.js`

### Navigation · `55141:14251` — **hecho**
- **MVP:** Navigation Bar H/V (3–6 items) + BB Vertical/Horizontal nav items
- **CUT:** secciones Deprecated (drawer + Nav item legacy) · Navigation Rail · Expanded · XR Rail/Bar — **hecho**
- **LATER (v1.1+):** restaurar rail desde CS si nav adaptativa (`NavigationSuiteScaffold`) — ver [`breakpoints.md`](breakpoints.md) §4.4

### Radio · `55141:14253` · Switch · `55141:14257` — **KEEP** (sin cambios)

### Search · `55141:14254` — **hecho**
- **MVP:** Search bar (6)
- **LATER:** Search full-screen layout (baseline + set) — etiquetado, no borrar
- **CUT:** Search docked layout (desktop) — **hecho**

### Sliders · `55141:14255` — **hecho**
- **KEEP:** Standard, Centered, Range — Horizontal; sizes XSmall–Large; State + Value completos (diseño)
- **CUT:** Vertical, XLarge — **hecho** (−156 variantes: Standard −72, Centered −72, Range −12)

### Snackbar · `55141:14256` — **hecho** (KEEP, sin poda)
- **Snackbar** (10): Configuration Text only / Text & action / Text & longer action · 1–2 líneas · close affordance
- **BB:** Snackbar-action + close-affordance (4 estados c/u)

### Tabs · `55141:14258` — **hecho** (KEEP, sin poda)
- **Tabs** (10): Primary + Secondary · Fixed + Scrollable · Label only / Icon only / Label & icon
- **BB:** Primary tabs (3) + Secondary tabs (2) — 8 estados c/u

### Text fields · `55141:14259` — **hecho**
- **KEEP:** Outlined — State (5) + Text configurations (3) + Leading/Trailing icon — **60 variantes**
- **CUT:** Filled — **hecho** (−60)

### Toolbars · `58295:22726` — **hecho**
- **MVP:** sección `MVP · Toolbar horizontal (Standard)` — Toolbar (2): Floating/Docked · Horizontal · Standard + BB Standard (3 sets) — **acordado 2026-06-10** (uso probable en MVP)
- **CUT:** Vibrant (2 variantes Toolbar + 3 BB) · Vertical (2 variantes) — **hecho** (−4 variantes, −3 BB sets)
- **XR:** no presente

### Tooltips · `55141:14261` — **hecho** (página ya eliminada; 0 sets en archivo)

### Sheets · `55141:14170` — **hecho**
- **MVP:** sección `MVP · Bottom sheet` — Bottom sheet (2) + BB Content (2)
- **LATER:** sección `LATER · Side sheet` — Side Sheet (4) + BB Content (2) · nota visible · **no borrar**

---

## Focos de peso (orden de ejecución)

1. ~~Buttons~~ **hecho** — Square en Button; XLarge; CUT Split + groups
2. Lists — densidades -2/-4
3. Navigation — Rail + Expanded + XR + Deprecated
4. ~~Text fields — Filled (~60)~~ **hecho**
5. Chips — Elevated + sets LATER si se difieren
6. Examples web + window classes grandes
7. Shape Set (página) · Carousel tablet/full-screen · ~~Tooltips~~ **hecho**

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
