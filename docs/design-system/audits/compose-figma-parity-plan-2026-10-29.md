# Plan de paridad Compose ↔ Figma (nativo)

**Fecha:** 2026-10-29  
**SoT código:** `MyOwnTrip_nativo`  
**SoT librería:** Figma `zrGAL4v6` · [MyOwnTrip_nativo — Design System](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432)  
**No usar como SoT:** repo archivo `MyOwnTrip` (`MyOwnTripButton`, `semantic/color/*` custom)

Inventario para trabajar por fases: qué hay en Compose, cómo se enlaza a tokens, qué hay en Figma, qué falta.

---

## 1. Cómo entiende nativo los “semánticos”

No es el sistema del archivo (paths `semantic/padding/button/…`). Aquí el contrato es **Material 3 tematizado**:

| Capa | Dónde | Qué es |
|------|--------|--------|
| **Color** | `MaterialTheme.colorScheme` ← `Theme.kt` / `Color.kt` (`AppColors`) | Roles M3 Light/Dark (= variables Figma colección **M3**) |
| **Shape superficie** | `MaterialTheme.shapes.*` (`MOTThemeShapes`) | `extraSmall`↔`Corner/Extra-small` … `extraLarge`↔`Corner/Extra-large`; botón = `rememberMOTButtonShape` — [shape.md](../shape.md) |
| **Shape botón** | `rememberMOTButtonShape()` vía `MOTButton*` | Reposo 0 → interactivo 20 (**ADR 004**) ↔ `Corner/None` / `Corner/Large-increased` |
| **Type** | `Type.kt` → `AppTypography` | Text styles **Fraunces/Inter** (`M3/{display\|headline\|title\|body\|label}/*`) — sin Roboto; [typography.md](../typography.md) |
| **Spacing de pantalla** | **`MOTSpacing`** | SoT producto (2026-10-29): Figma **`MOT · Spacing`** (`mot/spacing/*`). Colección **LEGACY · Semantic Layout** no usar — [spacing.md](../spacing.md) |
| **Motion** | `Motion.kt` + `MotionScheme` | Morph ~520ms, squish |

**Regla de consumo en UI:** pantallas usan `colorScheme` / wrappers `MOT*` / componentes de `ui/components`. No reintroducir `SemanticColors` del archivo ([components.md](../components.md)).

---

## 2. Capas de Compose (mapa mental)

```
designsystem/          → tema + MOTButton* + brand
app/.../ui/components/ → átomos/moléculas de producto (cards, eyebrow, dates, home shells)
app/.../ui/features/   → pantallas JTBD (trips, wallet, journal…)
```

### 2.1 Tema / wrappers (`designsystem`)

| API | Rol | Figma |
|-----|-----|-------|
| `MyOwnTripTheme` / `MaterialExpressiveTheme` | Color + type + shapes + motion | Colección **M3** + Shape + Typescale |
| `MOTButton` | Filled + morph | **Button** Square |
| `MOTTonalButton` | Tonal (Secondary) + pad XSmall 12×6 documentado | **Button - tonal** |
| `MOTOutlinedButton` / `MOTTextButton` | Outline / Text | **Button - outline** / **text** |
| `MOTIconButton` | Icon + morph | **Icon button** (Square reposo) |
| Brand (`Wordmark`, `Monogram`, `BrandRibbon`) | Marca | Página **Brand** |

### 2.2 Componentes de producto (`app/.../ui/components`)

| Composable | Uso en app (orden magnitud) | Figma set | Estado doc bindings |
|------------|------------------------------|-----------|---------------------|
| `MotStackedCard` / `HomeEmptyStackedCard` | ~8 | Stacked card | ✅ en [figma-compose-bindings.md](../figma-compose-bindings.md) |
| `MotHorizontalCard` / `TripListCard` | ~9 | Horizontal card | ✅ |
| `TripHeroCard` | ~7 | TripHeroCard | ✅ |
| `EyebrowLabel` | ~23 | Eyebrow label | ✅ |
| `DocumentAttachmentCard` | wallet | Document attachment | fila + audit [C](figma-document-attachment-phase-c-2026-10-29.md) |
| Date pack (`MotDate*`, calendar, time) | formularios | Date & time pickers | ⚠️ parcial / sin fila bindings |
| Home (`HomeSearchBar`, filter menu, FAB sheet, speed dial, swipe row) | Home | Search bar, Menu, FAB/FAB menu, List swipe | ⚠️ patrón home sí; sets no todos en tabla |
| `OfflineBanner` | — | — | gap producto |

### 2.3 Controles M3 usados directo en features (sin wrapper MOT)

Conteos aproximados de referencias en `app/.../java` (2026-10-29):

| API M3 | ~refs | Figma KEEP (inventario poda) | Wrapper MOT? |
|--------|------:|------------------------------|--------------|
| `MOTTextButton` | 78 | Button - text | sí |
| `MOTIconButton` | 78 | Icon button* | sí |
| `MOTButton` | 43 | Button | sí |
| `FilterChip` | 37 | Filter chip | no |
| `OutlinedTextField` / `TextField` | 38 / 35 | Text field Outlined | no |
| `SnackbarHost` | 37 | Snackbar | no |
| `FloatingActionButton` | 29 | FAB | no (circular, sin morph) |
| `MOTHorizontalDivider` / `HorizontalDivider` | ~28 | Dividers | sí · [C](figma-dividers-phase-c-2026-10-29.md) |
| `AlertDialog` | 25 | Basic dialog | no |
| Top app bars | 22 | App bar | no |
| `ModalBottomSheet` | 20 | Bottom sheet | no |
| `DropdownMenu` | 55 | Menu | patrón Home sí |
| `DatePicker` | 44 | Date pickers | vía MotDate* |
| `AssistChip` | 5 | Assistive chip | no |
| `SearchBar` | 5 | Search bar | Home |
| `ListItem` | 2 | List item | casi no usado |
| `NavigationBar` | 2 | Navigation Bar | poco |

`MaterialTheme.colorScheme` ~238 · `MOTSpacing` ~307 → el layout de pantalla **sí** consume espaciado semántico de producto; el color va por roles M3.

---

## 3. Qué hay en Figma (librería)

Páginas con component sets relevantes (bridge 2026-10-29): App bars, Badges, Buttons (~30 sets), Cards (Stacked/Horizontal/TripHero + scrapbook…), Chips, Date & time, Dialogs, Lists, Menu, Navigation, Search, Sheets, Snackbar, Tabs, Text fields, Toolbars, Labels (Eyebrow), etc.

**Variables locales:**

| Colección | Modos | Notas |
|-----------|-------|-------|
| **M3** | Light/Dark + contrast | Color SoT |
| **Shape** | Baseline | Corner/None, Small, Medium, Large-increased… |
| Font theme / Typescale | — | Tipo |
| Brand | — | Marca |
| Primitives · Space / Size | Default | Escala numérica |
| **Semantic · Layout** | Default | 55 vars — **legado**; nativo no documenta consumirlas en Compose |

Inventario de poda KEEP/CUT/LATER: [figma-prune-inventory.md](../figma-prune-inventory.md).

**Button (2026-10-29):** restore ADR 004 — reposo `Corner/None`, pressed/hover/focus `Corner/Large-increased`; pads M3 kit. Auditoría: [figma-button-adr004-restore-2026-10-29.md](figma-button-adr004-restore-2026-10-29.md).

---

## 4. Matriz de gaps (priorizada)

### P0 — Canónicos de producto (usar ya en móvil + Figma)

| Tema | Compose | Figma | Gap |
|------|---------|-------|-----|
| Botón filled/tonal/outline/text | `MOT*` | Button×5 Square | Binding shape OK tras restore; **elevated** en Figma KEEP pero poco/no wrapper MOT elevated |
| Cards Home | MotStacked / Horizontal / TripHero / Eyebrow | Sets OK | Mantener; auditar binds layout vs `MOTSpacing` / Corner Medium |
| Text field | OutlinedTextField directo | Text field Outlined | Fila bindings + pad/radius Outlined (4) vs doc |
| Filter chip | FilterChip directo | Filter chip | ✅ Outlined; `Corner/Small`; pad V **6**; audit [B.3](figma-filterchip-phase-b3-2026-10-29.md) |
| Dialogs confirm | AlertDialog | Basic dialog | ✅ `Corner/Extra-large` ↔ `shapes.extraLarge`=28; Wallet caps 7/11/12 instancias; [B.3](figma-dialog-phase-b3-2026-10-29.md) |
| App bar | TopAppBar M3 | App bar | ✅ 0 LEGACY; gaps kit Search/Medium doc; [B.3](figma-appbar-phase-b3-2026-10-29.md) |
| Snackbar | SnackbarHost | Snackbar | ✅ Inverse + Extra-small; gap longer-action fix; [B.3](figma-snackbar-phase-b3-2026-10-29.md) |
| FAB | FAB M3 | FAB | ✅ Circular · 0 LEGACY / 0 unbound contrato; [B.3](figma-menu-search-phase-b3-2026-10-29.md) |

### P1 — En app, bindings flojos o sets sin fila

| Tema | Notas |
|------|-------|
| Menu + Search (filtros Home) | Patrón documentado; completar fila “Search bar + Menu” en bindings |
| Bottom sheets | Muy usados; set Bottom sheet — auditar instancias en design-file |
| Date/time | MotDate* vs sets Figma — matriz estados |
| List item / swipe | Compose usa poco `ListItem`; Home tiene `SwipeableTripListRow` ↔ List Item - Swipe |
| Document attachment | Compose + set Figma Cards — añadir a bindings |
| Icon button | MOTIconButton vs 4 familias Figma — Square reposo 0 |
| Tabs | Trip detail tabs ↔ Tabs set |
| Dividers | `MOTHorizontalDivider` ↔ Dividers · audit [C](figma-dividers-phase-c-2026-10-29.md) |

### P2 — En Figma KEEP / Should, poco o nada en Compose MVP

Segmented button, Chip groups, Sliders, Carousel, Toolbars, Side sheet, Suggestion/Input chip (si no hay pantalla), Scrollable list dialog, Accordion, Loading indicators ricos, Navigation bar densa, Keyboard/device utilities (diseño only).

**Política:** se quedan en Figma; no adaptar hasta que una pantalla Compose los pida (como acordamos con Segmented).

### P3 — Limpieza / gobernanza

| Item | Acción | Estado |
|------|--------|--------|
| Spacing producto | **`MOTSpacing`** + Figma **`MOT · Spacing`**; legado renombrado | **Hecho** Fase A 2026-10-29 |
| Specs del repo **archivo** | No portar contrato `MyOwnTrip*` | vigente |
| `figma-compose-bindings.md` | Tabla P0 + ADR 004 + MOTSpacing | **Hecho** Fase A |
| Showcase JSON | Cubrir TextField, Chip, Dialog | pendiente |
| Borrar binds a LEGACY · Semantic Layout | Al tocar frames | pendiente (Fase B+) |

---

## 5. Plan de trabajo (fases)

### Fase A — Gobernanza ✅ (2026-10-29)
1. Workspace SoT = **`MyOwnTrip_nativo`**.
2. [figma-compose-bindings.md](../figma-compose-bindings.md) con P0 + ADR 004.
3. Decisión: espaciado de pantalla = **`MOTSpacing` / `MOT · Spacing`** ([spacing.md](../spacing.md)); `Semantic · Layout` → **LEGACY · Semantic Layout (no usar)**.

### Fase B — Auditoría visual P0 (por set)
Orden sugerido (producto en el Xiaomi primero):

1. **Button** — ya restore; verificar instancias en design-file Home/Wallet  
2. **TripHeroCard + Eyebrow + Horizontal/Stacked** — Home  
3. **Text field** — Crear viaje / Wallet forms  
4. **FilterChip** — ✅ filtros Wallet (+ Journal sheets); Home usa Menu  
5. **Dialog + Snackbar** — ✅ set + Wallet caps 7/11/12 instancias; Snackbar gap fix  
6. **App bar + FAB + Search/Menu** — ✅ 0 LEGACY; quirks kit Search/Medium documentados ([menu-search](figma-menu-search-phase-b3-2026-10-29.md) · [appbar](figma-appbar-phase-b3-2026-10-29.md))

Por cada uno: **Compose SoT** (preview/pantalla + tokens Kotlin) → set Figma al **mismo rol** → `resolveVariables` false/true → **0** `mot/*` huérfanos (sin espejo `MOTSpacing` / `shapes.*`).

### Fase C — P1 features
Sheets, dates, swipe list, document card, tabs, icon buttons.

**C ✅ (2026-10-29):** Bottom sheet · Date/time · List swipe · Tabs (`MOTTab`) · [Document attachment](figma-document-attachment-phase-c-2026-10-29.md) · [Icon button](figma-iconbutton-phase-c-2026-10-29.md) · [Dividers](figma-dividers-phase-c-2026-10-29.md).  
Glyph `visibility` ✅ (Icons + footer Document attachment, 2026-10-29).

### Fase D — Poda / LATER ✅ (2026-10-29)
Revisitado [figma-prune-inventory.md](../figma-prune-inventory.md) con evidencia Compose.  
Detalle: [figma-phase-d-prune-2026-10-29.md](figma-phase-d-prune-2026-10-29.md).  
CUT aplicado: Bottom app bar · Chips Elevated. Demotions Should→LATER: Segmented, Sliders, Chip groups/Suggestion/Input, Extended FAB, Accordion, Badges.

### Fase E — Bindables color / stroke (producto) ✅ (2026-10-29)
Pass en sets de bindings: weights (`mot/stroke/*`), Outline Variant en dividers Menu, whites → Surface Container Lowest.  
**0** contrato unbound. Doc: [figma-product-bindables-phase-e-2026-10-29.md](figma-product-bindables-phase-e-2026-10-29.md).

**Librería publicada** 2026-10-29 (post D/E + visibility): chips Outlined-only, strokes MOT, Document attachment footer.

---

## 6. Checklist de salida por componente

```
[ ] Composable o API M3 identificada en nativo (ruta archivo)
[ ] Fila en figma-compose-bindings.md
[ ] Set Figma KEEP con node id
[ ] Tokens: colorScheme / MaterialTheme.shapes / MOTSpacing (no semantic/* archivo)
[ ] Shape botón = ADR 004 si aplica
[ ] Pantalla Xiaomi o @Preview contrastada
[ ] 0 inventos de UI en design-file (solo instancias)
```

---

## 7. Resumen ejecutivo

- **Compose nativo** = M3 + wrappers MOT + cards/eyebrow/home; color por `colorScheme`, botones por morph 0→20.  
- **Figma** tiene el kit completo KEEP; productizados y documentados del todo solo una franja (cards Home, button, menu patrón).  
- **B.3 P0 cerrado** (FilterChip → Dialog/Snackbar → App bar/FAB/Search/Menu). LEGACY · Semantic Layout: borrar binds al tocar frames. Showcase JSON TextField/Chip/Dialog aún pendiente.  
- **No falta** reinventar `MyOwnTripButton` del archivo.

### Fase B.2 — Cards Home ✅ (2026-10-29)

Sets DS rebindeados a `Corner/Medium` + `MOT · Spacing`; Eyebrow alineado a Compose.  
Detalle: [figma-home-cards-phase-b-2026-10-29.md](figma-home-cards-phase-b-2026-10-29.md).

**B.2b ✅:** Home design-file spacing + type + color schemes.  
**B.3 TextField ✅ (2026-10-29):** set Outlined → `Corner/Extra-small` + `mot/padding/text-field/*`; 0 LEGACY. [figma-textfield-phase-b3-2026-10-29.md](figma-textfield-phase-b3-2026-10-29.md).  
**B.3 FilterChip ✅:** Outlined · `Corner/Small` · pads `mot/padding/chip/*`; Wallet filas instancias. [figma-filterchip-phase-b3-2026-10-29.md](figma-filterchip-phase-b3-2026-10-29.md).  
**B.3 Dialog + Snackbar ✅:** Dialog Extra-large; Wallet caps 7/11/12 = Basic dialog; Snackbar Inverse + gap fix. [dialog](figma-dialog-phase-b3-2026-10-29.md) · [snackbar](figma-snackbar-phase-b3-2026-10-29.md).  
**B.3 App bar + FAB + Search/Menu ✅:** 0 LEGACY; FAB limpio; quirks kit documentados. [appbar](figma-appbar-phase-b3-2026-10-29.md) · [menu-search](figma-menu-search-phase-b3-2026-10-29.md).  
**Re-entrada B.3 (post-TextField, misma fecha):** FilterChip / Dialog / Snackbar / App bar / FAB / Menu / Search re-audit live → **0 LEGACY** en sets P0; Wallet Filter chips + Dialog caps OK.

**Confirm live (2026-10-29, sesión post-Wallet CTA):** FilterChip + Assistive + Dialog + Snackbar + App bar/FAB/Menu/`Search bar` → **0 LEGACY** contrato. Residual: `Search full-screen layout` (50 LEGACY list-item; no SoT Compose) — [menu-search](figma-menu-search-phase-b3-2026-10-29.md).

**Confirm live (2026-10-29, continuación orden B.3 FilterChip→Dialog/Snackbar→App bar/FAB/Menu/Search):** re-audit Desktop Bridge — Filter 48 + Assistive 24 + Dialog + Snackbar + App bar Small + FAB + Menu + Search bar → **0 LEGACY** contrato; Wallet Filter chips 6× INSTANCE + Dialog caps 7/11/12 OK. Residual intacto: Search full-screen **50** (`semantic/padding|spacing/list-item|stack`).

**Confirm live (2026-10-29, sesión post-Wallet CTA — reentrada B.3 orden):** Filter 48 + Assistive 24 + Dialog + Snackbar + App bar + FAB (72, 0 pads unbound) + Menu + Search bar → **0 LEGACY** contrato. Wallet Filter chips 6× + Dialog 7/11/12 INSTANCE OK. Residual: Search full-screen **50**.

**Confirm live (2026-10-29, post-Wallet Import CTA — orden B.3 FilterChip→Dialog/Snackbar→App bar/FAB/Menu/Search):** Desktop Bridge — Filter **0** + Assistive **0** + Dialog **0** + Snackbar **0** + App bar **0** + FAB **0** (pads) + Menu **0** + Search bar **0** LEGACY contrato. Wallet Filter 6× INSTANCE + Dialog caps 7/11/12 INSTANCE · Home **0** FilterChip / **4** Menu. Residual: Search full-screen **50**.

**Confirm live (2026-10-29, post-Wallet Import CTA reparado — reentrada B.3 orden):** Filter **0** + Assistive **0** + Dialog **0** + Snackbar **0** + App bar **0** + FAB **0** + Menu **0** + Search bar **0** LEGACY. Wallet Filter 6× + Dialog 7/11/12 INSTANCE · Home **0** FilterChip · **1** Menu · Search bar ×3. Residual: Search full-screen **50**.

**Confirm live (2026-10-29, continuación B.3 orden FilterChip→Dialog/Snackbar→App bar/FAB/Menu/Search):** Desktop Bridge — Filter **0** + Assistive **0** + Dialog **0** + Snackbar **0** + App bar **0** + FAB **0** (pads) + Menu **0** + Search bar **0** LEGACY contrato. Wallet Filter 6× INSTANCE + Dialog 7/11/12 INSTANCE · Home **0** FilterChip · **1** Menu · Search bar ×3. Search full-screen: residual previo **50** → live actual **0** binds LEGACY (sigue no-SoT Compose).

**Siguiente (fuera B.3):** Showcase JSON TextField/Chip/Dialog · P1 residual si aparece en Xiaomi · no productizar Search full-screen.
