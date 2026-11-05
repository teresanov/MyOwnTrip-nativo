---
title: Figma ↔ Compose bindings
description: Mapa canónico entre component sets de la librería Figma, fichas showcase y composables de runtime
status: active
lastUpdated: 2026-11-03
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
| **Assistive chip** | [53923:28089](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=53923-28089) | Outlined only | `AssistChip` (`shapes.small`) | Misma paridad shape/pads que Filter · Elevated CUT Fase D |
| **Basic dialog** | [50723:10929](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=50723-10929) | Icon True/False | `AlertDialog` (`shapes.extraLarge`=28) | [basic-dialog](../ds-showcase/components/basic-dialog.html) · audit [B.3](audits/figma-dialog-phase-b3-2026-10-29.md) |
| **Snackbar** | [53977:33575](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=53977-33575) | Text / action / close | `SnackbarHost` | [snackbar](../ds-showcase/components/snackbar.html) · audit [B.3](audits/figma-snackbar-phase-b3-2026-10-29.md) |
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
| **Tabs** | [54563:40023](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=54563-40023) | Primary/Secondary + BB | `MOTTab` + `TabRow` / `ScrollableTabRow` | `MOTSpacing.Tab.*`; audit [C](audits/figma-tabs-phase-c-2026-10-29.md) · página **Tabs** |
| **Dividers** | [51816:5860](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=51816-5860) | Full-width / Inset / Middle-inset (+ V + subhead) | `MOTHorizontalDivider` | `outlineVariant` · inset=`screenHorizontal`; audit [C](audits/figma-dividers-phase-c-2026-10-29.md) |
| **Search bar** | [52977:33813](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=52977-33813) | — | `HomeSearchBar` (`RoundedCornerShape(28)`) | `Corner/Extra-large` · pads `component-xs`; gap Content **20** sin token — [B.3 menu-search](audits/figma-menu-search-phase-b3-2026-10-29.md) |
| **Menu** | [58966:3975](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=58966-3975) (+ item `58966:4100`) | Standard | `HomeFilterDropdownMenu` | `shapes.large` + `MOTSpacing.Menu.groupGap`; [home-filter-menu.md](patterns/home-filter-menu.md) |

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
| cap 2 · con viajes | [205:1018](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=205-1018) | `HomeHeroHeader` + `HomeSearchBar` + `TripHeroCard` + `TripListCard` | TripHeroCard · Horizontal Outlined · Search bar |
| cap 3 · filtros | [228:8161](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=228-8161) | cap 2 + `HomeFilterMenuPanel` / `HomeFilterDropdownMenu` | Menu `55141:14250` |

Previews: `HomeEmptyStatePreview`, `TripListWithTripsPreview`, `TripListFilterMenuPreview`, `HomeFlowReviewPreview`.

Spacing de página: `MOTSpacing.screenHorizontal` / `layoutMd|Lg` ↔ `mot/spacing/*`.

## Wallet · flujo de app `04 · Wallet` — [`491:13870`](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=491-13870)

Fuente Compose: `WalletPreviewData` · `@Preview`. **SoT visual:** capturas en [`docs/samples/wallet/caps/README.md`](../../samples/wallet/caps/README.md) (`cap-NN.png`). **Portero:** instancias DS; si preview ≠ captura → gana captura.

| Cap | Node Figma | Capture SoT | Preview Compose | Ruta |
|-----|------------|-------------|-----------------|------|
| cap 1 · con documentos | [491:13876](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=491-13876) | `cap-01.png` | `TripDetailWalletTabPreview` | `trip_detail/{tripId}` tab Wallet |
| cap 2 · vacío | [491:13915](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=491-13915) | `cap-02.png` | `WalletScreenEmptyPreview` | `trip_detail/{tripId}` empty |
| cap 3 · esperando picker | — | `cap-03.png` | `WalletAwaitingPickerPreview` | `wallet_add?pickAttachment=true` |
| cap 4 · analizando | — | `cap-04.png` | `WalletFormParsingPreview` | `wallet_add` |
| cap 5 · revisar vuelo | — | `cap-05.png` | `WalletFormImportReviewPreview` | `wallet_add` |
| cap 6 · revisar hotel | — | `cap-06.png` | `WalletFormImportReviewHotelPreview` | `wallet_add` |
| cap 7 · confirmar | `491:13962` · instancia Basic dialog `492:3332` | `cap-07.png` | `WalletConfirmDialogPreview` | `wallet_add` dialog |
| cap 8 · parse fail | — | `cap-08.png` | `WalletParseFailPreview` | `wallet_add` EC-PARSE-FAIL |
| cap 9 · manual | [491:13980](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=491-13980) | `cap-09.png` | `WalletFormManualPreview` | `wallet_add?pickAttachment=false` · **MotDateField** + **MotTimeField** |
| cap 10 · detalle | [491:13987](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=491-13987) | `cap-10.png` | `WalletDetailPreview` · `previewDetailEntry` | `wallet_detail/{entryId}` · **Document attachment** + QR (gap **WalletQrCard** DS) |
| cap 10b · detalle hotel | `503:6332` | `cap-10b.png` | `previewWalletEntries` HOTEL + `previewHotelImportState` | `hotel-casa-bonay-reserva.pdf` |
| cap 10c · actividad · drift | `503:6415` | `cap-10c.png` | `DebugPastTripsSeeder` · Teatro Nacional D. Maria II | chip plan · `teatro-real-las-bodas-de-figaro.pdf` |
| cap 13 · visor PDF | `503:6745` | `cap-13.png` | `DocumentViewerScreen` · `billete-barcelona.pdf` | document viewer |
| cap 11 · eliminar | `491:13994` · instancia Basic dialog `492:3372` (icon `error`) | `cap-11.png` | `WalletDeleteDialogPreview` | `wallet_detail` dialog |
| cap 12 · descartar | `491:14001` · instancia Basic dialog `492:3414` (icon `error`) | `cap-12.png` | `WalletDiscardDialogPreview` | `wallet_add` dialog |

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
