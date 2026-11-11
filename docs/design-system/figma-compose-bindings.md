---
title: Figma ↔ Compose bindings
description: Mapa canónico entre component sets de la librería Figma, fichas showcase y composables de runtime
status: active
lastUpdated: 2026-11-06
---

# Figma ↔ Compose — bindings

## Regla de oro

**Las pantallas no inventan UI.** Solo ensamblan composables de esta tabla + `MaterialTheme` + copy del design-file.

Si un frame de Figma usa una instancia del DS, **debe existir una fila aquí** antes de cerrar la pantalla.

**Export Compose → Figma:** instancias DS con `setProperties` en text props del componente (`Headline`, `Label text`, `Supporting text`, …) — no texto suelto. Script de referencia: `scripts/figma-design-file-wallet-cap1.js` (mismo patrón que `figma-design-file-rebuild-home-flow.js`).

## Tokens de producto (resumen)

| Rol | Compose | Figma |
|-----|---------|-------|
| Color | `MaterialTheme.colorScheme` | Colección **M3** |
| Shape superficies | `MaterialTheme.shapes.*` (`MOTThemeShapes`) | Colección **Shape** `Corner/*` — [shape.md](shape.md) |
| Shape botón | `rememberMOTButtonShape()` vía `MOTButton*` | `Corner/None` → `Corner/Large-increased` ([ADR 004](../decisions/004-button-shape-morph.md)) |
| Spacing **pantalla** | **`MOTSpacing`** | Colección **`MOT · Spacing`** (`mot/spacing/*`) — [spacing.md](spacing.md) |
| Padding interno M3 | Defaults del componente | State-layer del set (no `Semantic · Layout`) |

**Legado:** colección Figma `Semantic · Layout` — no usar en producto. Ver [spacing.md](spacing.md).

## Flujo obligatorio (nuevo componente o pantalla)

1. Component set publicado en librería Figma (`zrGAL4v6MEMc9hzZemU432`).
2. Ficha en showcase (`ds-showcase/src/data/components/*.json`) — estado **Ready** (cuando exista).
3. Composable en `app/.../ui/components/` o wrapper en `designsystem` con el mismo nombre lógico.
4. Preview `@Preview` nombrado como el cap de Figma (si aplica).
5. Fila en esta tabla + gate `composeMaterial3MappingValid`.

## P0 — Component sets → Compose

| Figma component set | Node DS | Layout / variante | Compose | Showcase / notas |
|---------------------|---------|-------------------|---------|------------------|
| **Stacked card** | [52346:27573](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=52346-27573) | Outlined · Media & text | `MotStackedCard` + `HomeEmptyStackedCard` | [stacked-card](https://teresanov.github.io/MyOwnTrip-nativo/components/stacked-card.html) |
| | | Elevated · Text only | `MotStackedCard` | ↑ |
| | | Outlined · Text only | `MotStackedCard` (filtro vacío Home) | ↑ |
| **Horizontal card** | ↑ | Outlined · Media & text · 80dp | `MotHorizontalCard` + `TripListCard` | ↑ |
| **TripHeroCard** | [61199:7862](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61199-7862) | Elevated | `TripHeroCard` | [trip-hero-card](../ds-showcase/components/trip-hero-card.html) |
| **Eyebrow label** | [61202:16834](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61202-16834) | Tertiary · Medium | `EyebrowLabel` | Medium **8×4** / Small **16×4**; `Corner/Small` · [eyebrow-label](../ds-showcase/components/eyebrow-label.html) · audit [Fase B](audits/figma-home-cards-phase-b-2026-10-29.md) |
| **Document attachment** | [61258:28665](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61258-28665) | Kind=Image\|File | `DocumentAttachmentCard` | `shapes.medium` · pads `layoutMd` · preview 180; audit [C](audits/figma-document-attachment-phase-c-2026-10-29.md) |
| **Wallet offline storage** | [61402:27244](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61402-27244) | Selected True\|False | `WalletOfflineStorageField` | Card `surfaceContainerLow` + Switch · caps 5–7. No es un layout de Stacked card. |
| **Wallet QR card** | [61403:27255](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61403-27255) | Size Default\|Compact | `WalletQrCard` | Card `surfaceContainerLowest` + QR generado · Default 260 / Compact 200 · instanciado en caps 5/7b/10/10c |
| **Wallet highlight card** | [61419:27164](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61419-27164) | Elevated 248dp · props Header/Title/Meta/Leading/Show offline | `WalletHighlightCard` (carrusel Próximos) | Extraído de Stacked (2026-11-10). Instanciado en cap 1b y tiras clónicas |
| **Wallet parse error card** | [61419:27178](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61419-27178) | Elevated | Banner parse fail en `WalletFormScreen` | Extraído de Stacked Layout=Error. Instanciado en cap 8 |
| **Spread piece** | [61258:28716](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61258-28716) | Type=Photo…Event bookmark | Piezas en hoja Spread | Card especial · átomos: Eyebrow label, iconos; página **Cards** |
| **Scrapbook page** | [61265:29035](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61265-29035) | State=Empty\|Full | Marco hoja álbum | Card especial · composición Spread piece; **Cards** |
| **Button** | [57994:2227](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=57994-2227) | Filled · Square | `MOTButton` | ADR 004; restore 2026-10-29 |
| **Button - tonal** | [58651:11237](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=58651-11237) | Tonal · Square · XSmall pads 12×6 | `MOTTonalButton` | Secondary de producto |
| **Button - outline** | [58650:10213](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=58650-10213) | Outline · Square | `MOTOutlinedButton` | |
| **Button - text** | [58650:8094](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=58650-8094) | Text · Square | `MOTTextButton` | |
| **Button - elevated** | [58650:9294](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=58650-9294) | Elevated · Square | *(sin wrapper MOT; M3 `Button`+elevation si hace falta)* | KEEP Figma; poco uso Compose |
| **Icon button** (+ outline/standard/tonal + togglable) | [57994:10081](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=57994-10081) | Square reposo | `MOTIconButton` | Morph 0→20; Round=`Corner/Large-increased`; audit [C](audits/figma-iconbutton-phase-c-2026-10-29.md) |
| **Text field** | [52798:24373](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=52798-24373) | Outlined only | `OutlinedTextField` (`MaterialTheme.shapes.extraSmall`) | [text-field](../ds-showcase/components/text-field.html) · audit [B.3](audits/figma-textfield-phase-b3-2026-10-29.md) |
| **Filter chip** | [53923:28270](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=53923-28270) | Outlined only | `FilterChip` (`shapes.small`=8) | [filter-chip](../ds-showcase/components/filter-chip.html) · audit [B.3](audits/figma-filterchip-phase-b3-2026-10-29.md) · Elevated CUT Fase D |
| **Assistive chip** | [53923:28089](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=53923-28089) | Outlined \| **Warning** | `AssistChip` / `PlanPlacementSourceChip` | Warning = `warning` fill 14% + borde 35% · cap 10c · hug, no fill |
| **Basic dialog** | [50723:10929](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=50723-10929) | Icon True/False | `AlertDialog` (`shapes.extraLarge`=28) | [basic-dialog](../ds-showcase/components/basic-dialog.html) · audit [B.3](audits/figma-dialog-phase-b3-2026-10-29.md) |
| **Snackbar** | [53977:33575](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=53977-33575) | Text / action / close · `# of lines` | `SnackbarHost` | Si el copy ocupa **dos líneas**, variante **Two lines** (no One line). Cap 1i = referencia. [snackbar](../ds-showcase/components/snackbar.html) · audit [B.3](audits/figma-snackbar-phase-b3-2026-10-29.md) |
| **App bar** | [58114:20565](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=58114-20565) | Small / Small-centered (+ KEEP Medium/Large/Search) | `TopAppBar` / `CenterAlignedTopAppBar` | pads `mot/spacing/component-*`; audit [B.3](audits/figma-appbar-phase-b3-2026-10-29.md) |
| **FAB** / Extended / menu | [57998:43426](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=57998-43426) | Circular | `FloatingActionButton` (+ menu Home) | Sin morph 0→20 |
| **Bottom sheet** | [51827:5859](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=51827-5859) | Modal True/False | `ModalBottomSheet` (`shapes.extraLarge`) | pads contenido `MOTSpacing.screenHorizontal`; audit [C](audits/figma-bottomsheet-phase-c-2026-10-29.md) |
| **Date · range field** | [61340:8809](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61340-8809) | `MotDateRangeField` · 2× Outlined editable + calendar icon | `MotDateFormFields.kt` | Ref. Input Range Date return [51954:18560](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=51954-18540) |
| **Date · range dialog** | [61348:28897](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61348-28897) · `Surface`=Days/Year/Month · kit ref. Year [51954:18216](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=51954-18216) | `MotBoundedMonthCalendar` en `MotDateRangePickerDialog` (no `DateRangePicker`) | `MotDatePickerDialogs.kt` · `MotCalendarPicker.kt` | Paridad 3 superficies Compose |
| **Date · single field** | [61348:29097](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61348-29097) | Outlined read-only + trailing calendar | `MotDateField` | Wallet |
| **Date · single dialog** | [61348:29373](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61348-29373) | Date picker M3 single | `MotDatePickerDialog` | ↑ |
| **Time · field** | [61348:29337](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61348-29337) | Outlined + trailing schedule | `MotTimeField` | Wallet · plan |
| **Time · dialog** | [61348:29338](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61348-29338) | TimeInput 24h + toggle + acciones | `MotTimePickerDialog` | ↑ |
| ~~Modal / Input date picker~~ | [51954:18136](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=51954-18136) / [51954:18540](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=51954-18540) | **ARCHIVE · M3 kit** | — | No design-file; ver [disparity-remediation-plan-2026-11-03.md](audits/disparity-remediation-plan-2026-11-03.md) |
| **List Item - Swipe** | [59106:13321](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=59106-13321) | Swipe states | `SwipeableTripListRow` / `SwipeableWalletDocumentRow` | `MOTSpacing.ListItem.*`; audit [C](audits/figma-list-swipe-phase-c-2026-10-29.md) · página **Lists** |
| **✳️ MyOwnTrip/Icon/Slot_master** | [61426:27443](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61426-27443) | `Size` sm/md · `Circle` False/True · **Icon** swap | Botones y consumo genérico | En **List item** Leading=Icon el well 40 y el glifo 24 viven en el list item (`Show circle` + `Icon`), no en Slot_master. |
| **List item** (Wallet rows) | [51964:63037](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=51964-63037) | Leading=Icon · `Show circle` · `Show drift` · **Icon** swap | `WalletDocumentRow` / `WalletPlanDriftLeadingIcon` | Glifo por `entryTypeIcon` (`flight`/`hotel`/`directions_car`/`confirmation_number`). Círculo 40 + icono 24. Puntito Warning 10 en la esquina del well (`Show drift`), no dentro del glifo 24. **Publicar** List item para que `Show drift` llegue al design-file. |
| **flight_drift** | [61433:3879](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61433-3879) | Glifo 24 (`flight` + `Drift dot`) | `WalletPlanDriftLeadingIcon(showDriftDot=true)` | El layer `Drift dot` tiene que estar **visible** en el maestro. List item → **Icon** swap a este componente (cap 1 IB con drift). |
| **Tabs** | [54563:40023](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=54563-40023) | Primary/Secondary + BB | `MOTTab` + `TabRow` / `ScrollableTabRow` | `MOTSpacing.Tab.*`; audit [C](audits/figma-tabs-phase-c-2026-10-29.md) · página **Tabs** |
| **Dividers** | [51816:5860](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=51816-5860) | Full-width / Inset / Middle-inset (+ V + subhead) | `MOTHorizontalDivider` | `outlineVariant` · inset=`screenHorizontal`; audit [C](audits/figma-dividers-phase-c-2026-10-29.md) |
| **Search bar** | [52977:33813](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=52977-33813) | Enabled / Pressed · `Show 2nd trailing=True` (`tune`) | `HomeSearchBar` (`RoundedCornerShape(28)`) | `Corner/Extra-large` · pads `component-xs`; gap Content **20** sin token — [B.3 menu-search](audits/figma-menu-search-phase-b3-2026-10-29.md) |
| **Menu** | [58966:3975](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=58966-3975) (+ item `58966:4100`) | Standard · Groups=2 · section labels | `HomeFilterDropdownMenu` | `shapes.large` + `MOTSpacing.Menu.groupGap`; [home-filter-menu.md](patterns/home-filter-menu.md) |
| **Home · Search + Menu** | Search + Menu (arriba) | Cap 2: search reposo, menú cerrado. Cap 3: overlay 328dp, `tune` abre Menu | `HomeSearchBar` + `HomeFilterDropdownMenu` / `HomeFilterMenuPanel` | R6.1 · no chips en fila · [home-filter-menu.md](patterns/home-filter-menu.md) |

Plan de auditoría P0→P1: [audits/compose-figma-parity-plan-2026-10-29.md](audits/compose-figma-parity-plan-2026-10-29.md).

### Properties Figma → parámetros Kotlin (cards / hero)

| Property Figma | Parámetro Compose |
|----------------|-------------------|
| `Header text` | `headerText` |
| `Subhead text` | `subheadText` |
| `Supporting text` | `supportingText` |
| Avatar leading (Media & text) | `leadingInitial` — **oculto** en Home vacío (`205:816`) |
| Media image | `imageRes` (bundled, preferido) / `imageUrl` / `TripCoverImage` |
| `Show secondary action` | `onDismiss` |
| `Style` Outlined / Elevated | `MotCardStyle` |
| Eyebrow `Label text` | `EyebrowLabel(text = …)` |
| TripHero `Title text` / `Meta text` | `trip.name` / `tripMetaLabel(trip)` |

## Home · Shell `205:813` (`route: trip_list`)

| Cap Figma | Node | Composable(s) | Figma DS |
|-----------|------|---------------|----------|
| cap 1 · vacío | [205:816](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=205-816) | `HomeEmptyState` → `HomePageHero` + `HomeEmptyStackedCard` + `MOTButton` | Stacked Outlined Media&text · Button · **Media asset:** `res/drawable-nodpi/home_empty_map.jpg` |
| cap 2 · con viajes | [205:1018](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=205-1018) | `HomeHeroHeader` + `HomeSearchBar` + `TripHeroCard` + `SwipeableTripListRow` (`TripListCard`) | TripHeroCard · Horizontal Outlined · Search bar. Swipe Compose envuelve la card; **no** hay cap List Item-Swipe Revealed en Home. |
| cap 3 · filtros | [228:8161](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=228-8161) | cap 2 + `HomeFilterMenuPanel` / `HomeFilterDropdownMenu` | Search Pressed/overlay + Menu Groups=2 `55141:14250` |

Previews: `HomeEmptyStatePreview`, `TripListWithTripsPreview`, `TripListFilterMenuPreview`, `HomeFlowReviewPreview`.

Spacing de página: `MOTSpacing.screenHorizontal` / `layoutMd|Lg` ↔ `mot/spacing/*`.

## Páginas de módulo (Compose `TripDetailScreen` tiene 5 tabs; cada tab vive en su página Figma)

`03` **no** monta Wallet / Plan / Diario / Gastos / Sitios. Editar el viaje: `02`. Day hub Plan\|Diario: `05` (además del tab).

| Página | Flujo Figma | Caps |
|--------|-------------|------|
| `03 · Detalle de viaje` | `261:935` | Vacío de módulos. Pendiente: ficha del viaje si no es `trip_edit`. |
| `04 · Wallet` | [`491:13870`](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=491-13870) | Entrada + import + alta + maleta |
| `05 · Plan` | [`583:16744`](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=583-16744) | **2** `261:941` · **2b** `573:13289` · **2c** `573:13390` · **2d** `573:13443` |
| `06 · Diario` | [`583:16747`](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=583-16747) | **5** `574:13966` · **5b** `574:14129` · **5c** `574:14292` |
| `07 · Gastos` | [`261:1087`](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=261-1087) | **3** `261:944` · **3b** `576:14844` |
| `08 · Sitios` | [`261:1247`](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=261-1247) | **4** `261:947` · **4b** `576:15050` · **4c** `581:15264` · **4d** `581:15447` · **4f** `583:15790` · **4g** `583:15942` · **4h** `583:16095` · **4e** pin pendiente |

### `08 · Sitios` — casuísticas

| Cap | Node | Preview / composable | Notas |
|-----|------|----------------------|--------|
| **4** sitios · vacío | `261:947` | `EmptyPlacesState` | 2 líneas + FAB + |
| **4b** sitios · lista | `576:15050` | `RestaurantsTab` cards | Solo cards Text only + FAB. **Sin mapa** en el tab. |
| **4c** Nuevo sitio | `581:15264` | `RestaurantFormScreen` | Outline Fill «Buscar en Google Maps» + location. Alta a mano. |
| **4d** Guardar sitio · sin pin | `581:15447` | `PlaceImportScreen` | Chips de **todos** los viajes no archivados. |
| **4e** *(pendiente)* | — | `hasLocation` | No montar hasta arreglar el pin. |
| **4f** detalle · restaurante | `583:15790` | `RestaurantDetailScreen` | Cal Pep · Reservado · Abrir Maps · delete. |
| **4g** detalle · sin reserva | `583:15942` | mirador | Bunkers · Por ir / Visitado. |
| **4h** detalle · borrar | `583:16095` | `AlertDialog` | Borrar sitio. |

## Wallet · flujo de app `04 · Wallet` — [`491:13870`](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=491-13870)

Fuente Compose: `WalletPreviewData` · `@Preview` (matriz de casos). Figma espeja esos previews con instancias DS. Una captura de dispositivo es un instante, no el catálogo: **si preview ≠ captura, gana el preview.** Mapa cap ↔ preview: [`docs/samples/wallet/caps/README.md`](../../samples/wallet/caps/README.md).

Tablero en **cuatro tiras** (no una fila lineal): **Entrada** (cap 2 + vincular 14) → **Importar · archivos del sistema** (3–8) o **Añadir a mano** (1… + 9 + 12) → **Ya en la maleta** (10–13). La herramienta (Importar vs Añadir/FAB) elige el camino. Lectura: arriba abajo por tiras; dentro, izquierda a derecha. **Sin flechas de conector** — título, «Cómo leer» y nombres de tira/cap son la guía.

| Cap | Node Figma | Capture SoT | Preview Compose | Ruta |
|-----|------------|-------------|-----------------|------|
| cap 1 · viaje pasado con documentos | [491:13876](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=491-13876) | `cap-01.png` (cuando exista) · captura `[Demo] Barcelona pasado` | `WalletScreenPreview` (`isPastTrip=true`) · **FAB** `+` → cap 9 | `trip_detail/{tripId}` tab Wallet · lista plana, sin chips ni Próximos |
| cap 1b · viaje en curso | [532:6435](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=532-6435) | — | `WalletScreenInProgressPreview` | Chips Activos/Archivados/Todos · carrusel **Próximos** · Documentos activos · FAB |
| cap 1c · filtro Archivados | [533:7343](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=533-7343) | — | mismo `WalletScreen` · `filterPhase=Archived` | Sin Próximos · vacío «No hay documentos archivados.» |
| cap 1d · filtro Todos | [533:7746](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=533-7746) | — | mismo `WalletScreen` · `filterPhase=All` | Sin Próximos · «Todos los documentos» |
| cap 1e · Archivados con documentos | [533:8118](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=533-8118) | — | `WalletScreenArchivedPreview` | Fila Sagrada · Restaurar/swipe |
| cap 1f · Activos vacío | [533:8406](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=533-8406) | — | `WalletScreenActiveEmptyPreview` | Todo archivado · «No hay documentos activos…» |
| cap 1g · swipe Archivar | [534:13012](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=534-13012) | — | `SwipeableWalletDocumentRow` | List Item - Swipe Revealed · 2 acciones archive/delete · leading flight |
| cap 1h · menú acciones | [534:9746](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=534-9746) | — | menú ⋮ Archivar / Eliminar | Sobre listado en curso |
| cap 1i · snackbar archivado | [534:8881](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=534-8881) | — | snackbar «archivado» + Deshacer | `TripDetailScreen` · instancia **Two lines** (copy de dos líneas; no One line) |
| cap 2 · vacío | [491:13915](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=491-13915) | `cap-02.png` | `WalletScreenEmptyPreview` | **Añadir** → cap 9 · **Importar** → selector/galería del sistema (cap 3 debajo) |
| cap 14 · vincular Wallet | [534:13526](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=534-13526) | — | `WalletLinkDialogPreview` | Plan / Day Hub · `WalletLinkDialog` |
| cap 14b · vincular vacío | [534:14423](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=534-14423) | — | `WalletLinkDialogEmptyPreview` | «Añadir documento a Wallet» |
| cap 3 · esperando picker | [491:13928](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=491-13928) | `cap-03.png` | `WalletAwaitingPickerPreview` | `wallet_add?pickAttachment=true` · **Rellenar manualmente** → cap 9 · Cancelar/atrás sale |
| cap 4 · analizando | [529:5097](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=529-5097) | `cap-04.png` | `WalletFormParsingPreview` | Tras elegir archivo · Circular + Linear · Cancelar análisis |
| cap 5 · revisar vuelo | [529:5242](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=529-5242) | `cap-05.png` | `WalletFormImportReviewPreview` | PDF enganchado · Detectado · QR compacto · Confirmar (aún no guarda). Instancia **Wallet QR card** Size=Compact tras publicar |
| cap 5b · cambiar tipo | [534:11037](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=534-11037) | — | `WalletFormTypeCorrectionPreview` | Chips de tipo visibles |
| cap 6 · revisar hotel | [529:5395](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=529-5395) | `cap-06.png` | `WalletFormImportReviewHotelPreview` | Igual que cap 5 con Casa Bonay |
| cap 7 · confirmar | [491:13964](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=491-13964) · Basic dialog `492:3332` | `cap-07.png` | `WalletConfirmDialogPreview` | Diálogo encima de cap 5. Guardar / Cancelar |
| cap 7b · confirmar con plan | [534:10017](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=534-10017) | — | `WalletConfirmWithPlanPreview` | Switch **Añadir al plan** + MotTimeField 09:15 · Cancelar / Guardar |
| cap 7c · confirmar sin plan | [534:10171](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=534-10171) | — | `WalletConfirmNoPlanPreview` | Fecha fuera del viaje |
| cap 7d · documento similar | [534:10325](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=534-10325) | — | `WalletDuplicateDialogPreview` | Guardar como nuevo / Reemplazar / Ignorar |
| cap 8 · parse fail | [529:5516](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=529-5516) | `cap-08.png` | `WalletParseFailPreview` | Banner error + PDF ilegible + formulario a mano |
| cap 9 · Añadir a Wallet | [491:13980](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=491-13980) | `cap-09.png` | `WalletFormManualPreview` · `WalletManualAddStep` | Destino del FAB, de **Añadir** y de **Rellenar manualmente** (cap 3). Chip **Galería** abre la galería del sistema encima del formulario (no se dibuja el picker). Chip **Escanear QR** → cap 9b |
| cap 9b · Escanear QR | [533:7611](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=533-7611) | — | `QrScanScreen` | Cámara a pantalla completa · slot (sin foto) · «Apunta la cámara al código QR…» |
| cap 9c · Hacer foto | [534:10563](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=534-10563) | — | `TakePhotoScreenPreview` | Cámara · FAB capturar · slot |
| cap 9d · con adjunto | [534:10630](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=534-10630) | — | `WalletFormManualAttachmentPreview` | Foto/PDF puesto · Quitar adjunto |
| cap 9e · sin viajes | [534:10849](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=534-10849) | — | `WalletFormNoTripsPreview` | Crear viaje |
| cap 9f · título obligatorio | [534:13289](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=534-13289) | — | `WalletFormTitleErrorPreview` | Text field Error · Obligatorio |
| cap 10 · detalle | [491:13987](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=491-13987) | `cap-10.png` | `WalletDetailPreview` · `previewDetailEntry` | `wallet_detail/{entryId}` · **Document attachment** + QR (instancia **Wallet QR card** Size=Default tras publicar) · papelera → cap 11 |
| cap 10b · detalle hotel | `503:6332` | `cap-10b.png` | `previewWalletEntries` HOTEL + `previewHotelImportState` | `hotel-casa-bonay-reserva.pdf` |
| cap 10c · actividad · drift | `503:6415` | `cap-10c.png` | `DebugPastTripsSeeder` · Teatro Nacional D. Maria II | chip plan · `teatro-real-las-bodas-de-figaro.pdf` |
| cap 10d · registrar gasto | [534:12770](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=534-12770) | — | `WalletDetailExpensePreview` | Precio + **Registrar como gasto** |
| cap 13 · visor PDF | `503:6745` | `cap-13.png` | `DocumentViewerScreen` · `billete-barcelona.pdf` | Página de muestra (boarding pass + QR). Sin papelera: borrar vive en detalle (cap 10/11) |
| cap 13b · visor entrada | `530:6002` | — | `DocumentViewerScreen` · `teatro-real-las-bodas-de-figaro.pdf` | Segunda muestra: entrada Teatro Real · Las bodas de Fígaro (desde cap 10c) |
| cap 13c · visor imagen | [534:12865](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=534-12865) | `cap-13c.png` | `DocumentViewerScreen` IMAGE | Foto de entrada maquetada · `entrada-teatro.jpg` |
| cap 13d · archivo no encontrado | [534:12938](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=534-12938) | — | `MissingDocumentPreview` | «No se encontró el archivo en el dispositivo.» |
| cap 11 · eliminar | `491:13996` · diálogo `492:3372` sobre detalle cap 10 | `cap-11.png` | `WalletDeleteDialogPreview` · `WalletDetailScaffold` | Papelera del app bar · Basic dialog Eliminar · el visor (cap 13) no borra |
| cap 12 · descartar | `491:14001` · instancia Basic dialog `492:3414` (icon `error`) | `cap-12.png` | `WalletDiscardDialogPreview` | `wallet_add` dialog · con adjunto |
| cap 12b · descartar sin adjunto | [534:10506](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=534-10506) | — | `WalletDiscardNoAttachmentPreview` | «Los cambios no guardados se perderán.» |

Flow review: `WalletFlowReviewPreview`.

Scripts (Desktop Bridge · `Vf2tNMXyKAlJSV53A1v4Is`): `scripts/figma-design-file-wallet-cap1.js` (cap 1 · gate captura) · `scripts/figma-design-file-wallet-flows-parity-part-a.js` (cap 10 vuelo + cap 2 vacío) · `scripts/figma-design-file-wallet-flows-parity-part-b.js` (10b/10c/13) · `figma-design-file-wallet-flows-parity.js` (completo).

## Viajes · crear y editar `02 · Viajes — crear y editar`

| Cap | Node Figma | Composable(s) | Figma DS |
|-----|------------|---------------|----------|
| Crear viaje | [261:926](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=261-926) | `CreateTripScreen` | **MotDateRangeField** `61340:8809` |
| Editar viaje | [261:929](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=261-929) | `EditTripScreen` | ↑ |

Previews: `CreateTripScreenPreview`, `EditTripScreenPreview`.

## Diario · Spread `05 · Día · Spread`

Spec: [`journal-spread-design-spec.md`](journal-spread-design-spec.md)

**Librería DS:** cards especiales en página **Cards** → marco [Spread · special cards](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61370-27708) (`61370:27708`): **Spread piece**, **Scrapbook page**. **No es card:** [Spread album chrome](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61262-28962) (`61262:28962`) → **App bars** (contador + **Day pill** [61261:28761](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61261-28761) en **Chips**). FAB captura = **FAB menu** (**Buttons**). Compose: `AlbumChrome` privado en `ScrapbookAlbum.kt`.

| Cap / frame | Preview Compose | Ruta |
|-------------|-----------------|------|
| Spread vacío | `DaySpreadEmptyPreview` | `day_hub/{tripId}/{dayId}?tab=journal` |
| Spread con piezas | `DaySpreadPreview` | ↑ |
| Sheet captura | `AddToSpreadSheetPreview` | Day Hub FAB |
| Lienzo boceto | `SketchCanvasScreenPreview` | `journal_sketch/{dayId}` |
| Índice viaje | `TripJournalTabPreview` | `trip_detail/{tripId}?tab=journal` |

## Qué no duplicar en pantallas

- `OutlinedCard { Text… }` ad hoc → `MotStackedCard` / `MotHorizontalCard`.
- Chip como eyebrow en portada → `EyebrowLabel`.
- `Button` M3 suelto para CTA de producto → `MOTButton` / `MOTTonalButton` / etc. (morph ADR 004).
- Lógica de fase viaje duplicada → `Trip.homePhase()` + helpers en `TripHeroCard` / `HomeHero`.

## Gate de cierre pantalla (PR UI)

```
[ ] Cada instancia Figma del frame tiene fila en esta tabla
[ ] Preview del cap correspondiente actualizado
[ ] Sin Card/Text M3 sueltos que repliquen un component set del DS
[ ] Spacing de página = MOTSpacing / mot/spacing/* (no Semantic · Layout)
[ ] Botones = ADR 004 (reposo 0)
[ ] m3Canonical + android-compose-ux
```
