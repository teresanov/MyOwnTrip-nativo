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
| **Plan day card** | [61457:28212](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61457-28212) | Content Empty\|Filled\|Unplaced · Trip Current\|Past | `PlanDaySection` + `UnplacedWalletSection` | Misma Card. Unplaced = Current (Compose no lista sin colocar en viaje pasado). Booleanas `Show day title`, `Show document`. TEXT `Doc title` / `Meta`. CTA Unplaced: Button - text + `account_balance_wallet`. Cap **2c** instanciado. |
| **Plan activity** | [61457:28304](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61457-28304) | Wallet True\|False · Show action | `PlanActivityCard(embeddedInDay)` | Time/Title/Wallet TEXT props. |
| **Plan timeline block** | [61465:28416](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61465-28416) | Wallet True\|False · Dragging · Show overflow | `PlanTimelineBlock` | Cuadrante day hub. Celda 56dp; min Wallet 68 / libre 44. Cap **3**. **Publicar** tras rename (quitado «local»). |
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
| **Icon button** (+ outline/standard/tonal + togglable) | [57994:10081](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=57994-10081) | Square reposo (`Corner/None`) | `MOTIconButton` | Morph 0→20; Round = destino 20dp, no `Corner/Full`. FAB es el único círculo. |
| **Text field** | [52798:24373](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=52798-24373) | Outlined only | `OutlinedTextField` (`MaterialTheme.shapes.extraSmall`) | [text-field](../ds-showcase/components/text-field.html) · audit [B.3](audits/figma-textfield-phase-b3-2026-10-29.md) |
| **Filter chip** | [53923:28270](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=53923-28270) | Outlined only | `FilterChip` (`shapes.small`=8) | [filter-chip](../ds-showcase/components/filter-chip.html) · audit [B.3](audits/figma-filterchip-phase-b3-2026-10-29.md) · Elevated CUT Fase D |
| **Assistive chip** | [53923:28089](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=53923-28089) | Outlined \| **Warning** | `AssistChip` / `PlanPlacementSourceChip` | Warning = `warning` fill 14% + borde 35% · cap 10c · hug, no fill |
| **Basic dialog** | [50723:10929](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=50723-10929) | Icon True/False | `AlertDialog` (`shapes.extraLarge`=28) | [basic-dialog](../ds-showcase/components/basic-dialog.html) · audit [B.3](audits/figma-dialog-phase-b3-2026-10-29.md) |
| **Journal recording dialog** | [61531:34355](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61531-34355) | State Recording\|Review | `JournalRecordingDialog` | Recording: Detener + glifo `stop` fill1. Instanciado en **6c1**/ **6c2**. |
| **Snackbar** | [53977:33575](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=53977-33575) | Text / action / close · `# of lines` | `SnackbarHost` | Si el copy ocupa **dos líneas**, variante **Two lines** (no One line). Cap 1i = referencia. [snackbar](../ds-showcase/components/snackbar.html) · audit [B.3](audits/figma-snackbar-phase-b3-2026-10-29.md) |
| **App bar** | [58114:20565](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=58114-20565) | Small / Small-centered (+ KEEP Medium/Large/Search) | `TopAppBar` / `CenterAlignedTopAppBar` | pads `mot/spacing/component-*`; audit [B.3](audits/figma-appbar-phase-b3-2026-10-29.md) |
| **FAB** / Extended / menu | [57998:43426](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=57998-43426) | Circular | `FloatingActionButton` (+ menu Home) | Sin morph 0→20 |
| **Bottom sheet** | [51827:5859](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=51827-5859) | Modal True/False | `ModalBottomSheet` (`shapes.extraLarge`) | pads contenido `MOTSpacing.screenHorizontal`; audit [C](audits/figma-bottomsheet-phase-c-2026-10-29.md) |
| **Date · range field** | [61340:8809](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61340-8809) | `MotDateRangeField` · 2× Outlined editable + calendar icon | `MotDateFormFields.kt` | [date-time-pickers](../ds-showcase/components/date-time-pickers.html) · contrato [date-product](audits/figma-date-product-contract-2026-11-03.md) · kit ref. [51954:18560](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=51954-18540) ARCHIVE |
| **Date · range dialog** | [61348:28897](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61348-28897) · `Surface`=Days/Year/Month · kit ref. Year [51954:18216](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=51954-18216) | `MotBoundedMonthCalendar` en `MotDateRangePickerDialog` (no `DateRangePicker`) | `MotDatePickerDialogs.kt` · `MotCalendarPicker.kt` | ↑ showcase · Paridad 3 superficies Compose |
| **Date · single field** | [61348:29097](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61348-29097) | Outlined read-only + trailing calendar | `MotDateField` | ↑ showcase · Wallet |
| **Date · single dialog** | [61348:29373](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61348-29373) | Date picker M3 single | `MotDatePickerDialog` | ↑ showcase |
| **Time · field** | [61348:29337](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61348-29337) | Outlined + trailing schedule | `MotTimeField` | ↑ showcase · Wallet · plan |
| **Time · dialog** | [61348:29338](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61348-29338) | TimeInput 24h + toggle + acciones | `MotTimePickerDialog` | ↑ showcase |
| ~~Modal / Input date picker~~ | [51954:18136](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=51954-18136) / [51954:18540](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=51954-18540) | **ARCHIVE · M3 kit** | — | No design-file; ver [disparity-remediation-plan-2026-11-03.md](audits/disparity-remediation-plan-2026-11-03.md) |
| **List Item - Swipe** | [59106:13321](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=59106-13321) | `Content` List item \| **Horizontal card** · `Swipe state` Default/Initiate/Revealed/Swipe Action | `SwipeableWalletDocumentRow` / `SwipeableTripListRow` | Home usa **Horizontal card**; Wallet **List item**. Raíz sin fill. **Publicar** el set para cap 2b. |
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
| cap 2 · con viajes | [205:1018](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=205-1018) | `HomeHeroHeader` + `HomeSearchBar` + `TripHeroCard` + `SwipeableTripListRow` (`TripListCard`) | TripHeroCard · Horizontal Outlined · Search bar. Reposo: card sin revelar. |
| cap 2b · swipe revelado | [642:31056](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=642-31056) | `SwipeableTripListRow` | Instancia **List Item - Swipe** `643:34361` · `Content=Horizontal card` · `Swipe state=Revealed`. Sin fill en la raíz. |
| cap 3 · filtros | [228:8161](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=228-8161) | cap 2 + `HomeFilterMenuPanel` / `HomeFilterDropdownMenu` | Search Pressed/overlay + Menu Groups=2 `55141:14250` |

Previews: `HomeEmptyStatePreview`, `TripListWithTripsPreview`, `TripListFilterMenuPreview`, `HomeFlowReviewPreview`.

Spacing de página: `MOTSpacing.screenHorizontal` / `layoutMd|Lg` ↔ `mot/spacing/*`.

## Páginas de módulo (Compose `TripDetailScreen` tiene 5 tabs; cada tab vive en su página Figma)

`03` **no** monta Wallet / Plan / Diario / Gastos / Sitios. Editar el viaje: `02`. Day hub Plan\|Diario: `05` (además del tab).

| Página | Flujo Figma | Caps |
|--------|-------------|------|
| `03 · Detalle de viaje` | `261:935` | Vacío de módulos. Pendiente: ficha del viaje si no es `trip_edit`. |
| `04 · Wallet` | [`491:13870`](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=491-13870) | Entrada + import + alta + maleta |
| `05 · Plan` | [`583:16744`](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=583-16744) | **2** `261:941` · **2b** `573:13289` · **2c** `573:13390` · **2d** `573:13443` · **2e** `593:16826` · **2f** `599:19969` · **3–3o** day hub |

### `05 · Plan` — casuísticas `TripPlanTab`

| Cap | Node | Preview / composable | Notas |
|-----|------|----------------------|--------|
| **2** en curso · días vacíos | `261:941` | `TripPlanTab` Current | Instancias **Plan day card** Empty/Current. CTA «Añadir actividades». FAB +. |
| **2b** sin días | `573:13289` | `TripPlanTab` `days.isEmpty()` | Copy bodyLarge/bodyMedium. FAB + (snackbar en **2f**). |
| **2c** actividades + sin colocar | `573:13390` | `TripPlanTab` + `UnplacedWalletSection` | Día 1 Filled + **Plan day card** Content=Unplaced (`649:7280`). |
| **2d** viaje pasado | `573:13443` | `TripPlanTab` `isPastTrip` | Empty/Past · «Ver recuerdos del día». Header **Ver documentos**. Sin FAB. |
| **2e** pasado · Lisboa | `593:16826` | `TripPlanTab` `[Demo] Lisboa pasado` | Filled/Past (días 1–4 seed) + Empty/Past (5–7). Sin colocar no aplica. |
| **2f** FAB sin días | `599:19969` | `TripDetailScreen` FAB Plan | Cuerpo = **2b**. Snackbar «Este viaje aún no tiene días» (One line). |

### `05 · Plan` — day hub `DayHubScreen`

| Cap | Node | Preview / composable | Notas |
|-----|------|----------------------|--------|
| **3** cuadrante | `593:14790` | `DayHubScreen` + `DayPlanSchedule` | Tabs Plan\|Diario. Hint Wallet. Grid 09–16 · 56dp. **Plan timeline block** Museo (Wallet) 11:00 + Café 14:30. FAB +. Sin Navigation bar. |
| **3b** añadir actividad | `593:14876` | `AddPlanActivitySheet` | Sheet: Outlined vacíos (Label text, sin fill `Surface`), fila Wallet «Vincular documento de Wallet» + «Billete, entrada o reserva (opcional)» + Elegir, MotTimeField vacío, Añadir / Cancelar. Grid = **3**. |
| **3c** vacío | `593:14946` | `DayPlanSchedule` vacío | Copy centrado. FAB +. |
| **3d** vincular Wallet | `593:15068` | `WalletLinkDialogPreview` | Basic dialog + lista radio (Sin documento + preview Wallet). Grid = **3**. |
| **3e** vincular vacío | `593:15181` | `WalletLinkDialogEmptyPreview` | Dialog sin lista. Copy vacío. Grid = **3**. |
| **3f** mover día | `593:15294` | `MoveBlockDayDialog` | Basic dialog Content=Day+time. Café · Día 2 · 14:30. Grid = **3**. Publicar DS para `Block title`. |
| **3g** menú libre | `593:15660` | `DayPlanSchedule` overflow sin Wallet | Menu Standard · Editar hora · Mover a otro día · Vincular documento de Wallet. Sobre Café. Grid = **3**. |
| **3h** menú Wallet | `593:15839` | overflow con Wallet | Menu: Corregir horario · Vincular documento de Wallet. Sobre Museo. Grid = **3**. |
| **3i** arrastre | `593:16000` | drag Café | Café desplazada. `Dragging=True` exige publicar Plan timeline block. |
| **3j** editar hora | `593:16126` | `AlertDialog` | Time field 14:30 · Cancelar / Guardar. Grid = **3**. |
| **3k** corregir horario | `593:16257` | `UpdateDocumentPlanSheet` | Museo · copy Wallet 11:00 · Día del plan · Guardar cambio / Usar hora del documento. |
| **3m** Lisboa pasado | `593:16390` | `DayHub` `isPastTrip` | Día 1 · 12 abr. AVE 08:30 + Hotel 14:00. Sin hint, sin ⋮, sin FAB. |
| **3n** añadir con Wallet | `593:16516` | `AddPlanActivitySheet` linked | Documento vinculado · Museo del Prado · Cambiar. |
| **3o** vacío pasado | `599:20120` | Plan vacío `readOnly` | Día 5 · 16 abr. «No había actividades…». Sin FAB. |
| **3p** snackbar movida | `599:20223` | snackbar Two lines | Día 2. «Actividad movida al Día 2. El documento en Wallet no se ha modificado.» |
| **3q** snackbar hora | `599:20359` | snackbar Two lines | «Hora actualizada en el plan. El documento en Wallet no se ha modificado.» |
| **3r** solape | `599:20494` | misma hora | Museo + Café a las 11:00. Grid = **3**. |
| `06 · Diario` | [`583:16747`](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=583-16747) | **5** `574:13966` · **5b** `574:14129` · **5c** `574:14292` · **6** `617:32975` · **6b** `618:33254` · **6c** `618:33462` · **6c1** `679:36634` · **6c2** `674:33110` · **6d** `620:35581` · **7** `620:39326` |
| `07 · Gastos` | [`261:1087`](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=261-1087) | **3** `261:944` · **3b** `576:14844` · **4** `593:12952` · **4b** `593:13224` · **4c** `593:13526` · **4d** `593:13678` · **4e** `593:13951` · **4f** `593:14219` · **4g** `593:14490` |
| `08 · Sitios` | [`261:1247`](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=261-1247) | **4** `261:947` · **4b** `576:15050` · **4c** `581:15264` · **4d** `581:15447` · **4e** `642:31174` · **4f** `583:15790` · **4g** `583:15942` · **4h** `583:16095` |

### `06 · Diario` — casuísticas `TripJournalTab`

Chrome trip detail: Status bar + TopAppBar + Tabs Scrollable (Diario). Body `mot/spacing` 8/16. FAB captura = **FAB menu**. Diario se edita también en viajes pasados.

| Cap | Node | Preview / composable | Notas |
|-----|------|----------------------|--------|
| **5** álbum | `574:13966` | `TripJournalTabPreview` | Hint «Toca para abrir · mantén pulsado para mover». **Scrapbook page** Day 1 + **Spread piece** Photo + Text. **Spread album chrome**: contador `Día 1 · 1/1` + pills (una hoja por día). Sin hamburguesa ni 2.º FAB. FAB captura overlay. |
| **5b** vacío | `574:14129` | `TripJournalTabEmptyPreview` | «Aún no hay diario.» / «Empieza a pegar recuerdos con +.» Sin chrome ni hoja. FAB +. |
| **5c** FAB menú | `574:14292` | `JournalCaptureFabMenu` | Foto · Texto · Voz · Sticker · Boceto. Sin Evento: el Diario no da de alta el plan. |
| **6** escribir | `617:32975` | `SpreadCaptionSheet` TEXT | Título «Escribir en la página». Chips **A mano** selected (default `JournalTextStyle.HAND`) · **A máquina**. Campo «¿Qué pasó?». CTA «Pegar en la página». |
| **6b** foto | `618:33254` | `SpreadCaptionSheet` PHOTO | «Añadir foto» · «¿Qué capturas?» · Cámara / Galería · Pegar. |
| **6c** voz | `618:33462` | `SpreadCaptionSheet` AUDIO | Hoja sola: Nota + **Grabar voz**. Sin modal. |
| **6c1** grabando | `679:36634` | Recording overlay | Grabando / Detener + `stop` fill1. Entre 6c y 6c2. |
| **6c2** revisar | `674:33110` | Review overlay | Player + Pegar filled + Descartar outline. |
| **6d** sticker | `620:35581` | `SpreadCaptionSheet` STICKER | «Crear sticker» · Pie opcional · Cámara / Galería · Forma del recorte (Círculo selected) · Pegar. Estado sin foto. |
| **7** boceto | `620:39326` | `SketchCanvasScreen` | TopAppBar Boceto + guardar. Canvas. Toolbar: pincel selected, goma (`MOTIcons.Delete`), undo/redo, borrar todo (`Delete` otra vez). Paleta 7 + grosores 2/4/8/16. |

Gaps: Event bookmark no se captura desde el +; si se ata al plan, será otro flujo.

### `07 · Gastos` — casuísticas `ExpensesTab` / `ExpenseFormScreen`

Chrome trip detail: Status bar + TopAppBar + Tabs (Gastos). Tab **3/3b**. Formulario **4–4g** es pantalla propia (`ExpenseFormScreen`), no tab. Sin `@Preview` nombrado aún: el SoT es el composable.

| Cap | Node | Preview / composable | Notas |
|-----|------|----------------------|--------|
| **3** vacío | `261:944` | `ExpensesTab` vacío | «Aún no hay gastos.» FAB +. |
| **3b** lista | `576:14844` | `ExpensesTab` con datos | Total por moneda. Secciones **Reservas** (Wallet) / **En viaje**. Cards concepto + importe. |
| **4** añadir | `593:12952` | `ExpenseFormScreen` alta | Intro «Registra el gasto en el momento…». TopAppBar «Añadir gasto». Importe · chips EUR/USD/GBP/Otra · Concepto · Categoría (Comida…Otro) · Ticket Hacer foto / Galería · Guardar gasto. |
| **4b** validación + otra moneda | `593:13224` | mismo · error + `Otra` | Importe inválido + «Introduce un importe válido». Chip Otra · Código de moneda (MAD). |
| **4c** hacer foto | `593:13526` | `TakePhotoScreen` | Cámara a pantalla completa. Mismo composable que Diario/Wallet. |
| **4d** leyendo ticket | `593:13678` | `isScanningReceipt` | Foto adjunta + «Leyendo el ticket…». Phone `clipsContent` para que el scroll no pinte Importe sobre el chrome. |
| **4e** ticket adjunto | `593:13951` | `receiptPath` sin scan | Miniatura + «Toca la imagen…» · Quitar foto · Guardar gasto. Mismo clip que **4d**. |
| **4f** editar | `593:14219` | `isEditMode` | TopAppBar «Editar gasto» + delete. CTA «Guardar cambios». |
| **4g** borrar | `593:14490` | `AlertDialog` | Sobre **4f**. «Borrar gasto» · no se puede deshacer · Cancelar / Borrar. |

El copy de intro («Registra el gasto en el momento. Puedes fotografiar el ticket del TPV o de la cafetería.») está en todas las caps de formulario (**4, 4b, 4d–4g**). **4c** es cámara a pantalla completa y no lo lleva, igual que Compose.

### `08 · Sitios` — casuísticas

| Cap | Node | Preview / composable | Notas |
|-----|------|----------------------|--------|
| **4** sitios · vacío | `261:947` | `EmptyPlacesState` | 2 líneas + FAB + |
| **4b** sitios · lista | `576:15050` | `RestaurantsTab` cards | Solo cards Text only + FAB. **Sin mapa** en el tab. |
| **4c** Nuevo sitio | `581:15264` | `RestaurantFormScreen` | Outline Fill «Buscar en Google Maps» + location. Alta a mano. |
| **4d** Guardar sitio · sin pin | `581:15447` | `PlaceImportScreen` | Body FILL + pack start (`spacedBy` `layout-md`). CTA es último hijo hug, no footer anclado. Copy: «Sin coordenadas, pero podrás abrirlo en Maps.» |
| **4e** Guardar sitio · ubicación detectada | `642:31174` | `PlaceImportScreen` `hasLocation` | Igual que 4d. `LocationStatus`: `Slot_master` sm `location_on` (tertiary) + `body/medium`. Pin: URL/HTML o geocode del nombre (no destino del viaje). |
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

**Librería DS:** cards especiales en página **Cards** → marco [Spread · special cards](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61370-27708) (`61370:27708`): **Spread piece**, **Scrapbook page**. **No es card:** [Spread album chrome](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61262-28962) (`61262:28962`) → **App bars** (contador + **Day pill** [61261:28761](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61261-28761)). Maestro y caps: solo contador + pills; sin hamburguesa ni 2.º FAB. FAB captura = overlay de `TripDetailScreen`. Compose: `AlbumChrome` en `ScrapbookAlbum.kt`.

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
