---
title: Figma ↔ Compose bindings
description: Mapa canónico entre component sets de la librería Figma, fichas showcase y composables de runtime
status: active
lastUpdated: 2026-10-29
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
| **Button** | [57994:2227](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=57994-2227) | Filled · Square | `MOTButton` | ADR 004; restore 2026-10-29 |
| **Button - tonal** | [58651:11237](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=58651-11237) | Tonal · Square · XSmall pads 12×6 | `MOTTonalButton` | Secondary de producto |
| **Button - outline** | [58650:10213](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=58650-10213) | Outline · Square | `MOTOutlinedButton` | |
| **Button - text** | [58650:8094](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=58650-8094) | Text · Square | `MOTTextButton` | |
| **Button - elevated** | [58650:9294](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=58650-9294) | Elevated · Square | *(sin wrapper MOT; M3 `Button`+elevation si hace falta)* | KEEP Figma; poco uso Compose |
| **Icon button** (+ outline/standard/tonal + togglable) | [57994:10081](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=57994-10081) | Square reposo | `MOTIconButton` | Morph 0→20; Round=`Corner/Large-increased`; audit [C](audits/figma-iconbutton-phase-c-2026-10-29.md) |
| **Text field** | [52798:24373](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=52798-24373) | Outlined only | `OutlinedTextField` (`MaterialTheme.shapes.extraSmall`) | Radius `Corner/Extra-small`; pads `mot/padding/text-field/*`; audit [B.3](audits/figma-textfield-phase-b3-2026-10-29.md) |
| **Filter chip** | [53923:28270](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=53923-28270) | Outlined only | `FilterChip` (`shapes.small`=8) | `Corner/Small` · pads `mot/padding/chip/*`; audit [B.3](audits/figma-filterchip-phase-b3-2026-10-29.md) · Elevated CUT Fase D |
| **Assistive chip** | [53923:28089](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=53923-28089) | Outlined only | `AssistChip` (`shapes.small`) | Misma paridad shape/pads que Filter · Elevated CUT Fase D |
| **Basic dialog** | [50723:10929](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=50723-10929) | Icon True/False | `AlertDialog` (`shapes.extraLarge`=28) | `Corner/Extra-large` · pads `mot/padding/dialog/*`; audit [B.3](audits/figma-dialog-phase-b3-2026-10-29.md) |
| **Snackbar** | [53977:33575](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=53977-33575) | Text / action / close | `SnackbarHost` | `Corner/Extra-small` · Inverse schemes · pads `mot/padding/snackbar/*`; audit [B.3](audits/figma-snackbar-phase-b3-2026-10-29.md) |
| **App bar** | [58114:20565](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=58114-20565) | Small / Small-centered (+ KEEP Medium/Large/Search) | `TopAppBar` / `CenterAlignedTopAppBar` | pads `mot/spacing/component-*`; audit [B.3](audits/figma-appbar-phase-b3-2026-10-29.md) |
| **FAB** / Extended / menu | [57998:43426](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=57998-43426) | Circular | `FloatingActionButton` (+ menu Home) | Sin morph 0→20 |
| **Bottom sheet** | [51827:5859](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=51827-5859) | Modal True/False | `ModalBottomSheet` (`shapes.extraLarge`) | pads contenido `MOTSpacing.screenHorizontal`; audit [C](audits/figma-bottomsheet-phase-c-2026-10-29.md) |
| **Modal / Input date picker** | [51954:18136](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=51954-18136) / [51954:18540](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=51954-18540) | Day / Range… | `MotDate*Dialog` / `MotTripDatePickerDialog` | contenedor `shapes.extraLarge`; audit [C](audits/figma-datepicker-phase-c-2026-10-29.md) |
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

## Wallet · flujo de app `04 · Wallet` — [`266:75`](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=266-75)

Fuente Compose: `WalletFlowReviewScreen` · ruta dev `wallet_flow_review`.

| Cap | Node Figma | Preview Compose | Ruta |
|-----|------------|-----------------|------|
| cap 1 · con documentos | [266:78](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=266-78) | `TripDetailWalletTabPreview` | `trip_detail/{tripId}` tab Wallet |
| cap 2 · vacío | — | `WalletScreenEmptyPreview` | `trip_detail/{tripId}` empty |
| cap 3 · esperando picker | — | `WalletAwaitingPickerPreview` | `wallet_add?pickAttachment=true` |
| cap 4 · analizando | — | `WalletFormParsingPreview` | `wallet_add` |
| cap 5 · revisar vuelo | — | `WalletFormImportReviewPreview` | `wallet_add` |
| cap 6 · revisar hotel | — | `WalletFormImportReviewHotelPreview` | `wallet_add` |
| cap 7 · confirmar | `491:13962` · instancia Basic dialog `492:3332` | `WalletConfirmDialogPreview` | `wallet_add` dialog |
| cap 8 · parse fail | — | `WalletParseFailPreview` | `wallet_add` EC-PARSE-FAIL |
| cap 9 · manual | — | `WalletFormManualPreview` | `wallet_add?pickAttachment=false` |
| cap 10 · detalle | — | `WalletDetailPreview` | `wallet_detail/{entryId}` |
| cap 11 · eliminar | `491:13994` · instancia Basic dialog `492:3372` (icon `error`) | `WalletDeleteDialogPreview` | `wallet_detail` dialog |
| cap 12 · descartar | `491:14001` · instancia Basic dialog `492:3414` (icon `error`) | `WalletDiscardDialogPreview` | `wallet_add` dialog |

Flow review: `WalletFlowReviewPreview`.

## Diario · Spread `05 · Día · Spread`

Spec: [`journal-spread-design-spec.md`](journal-spread-design-spec.md)

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
