# Inventario completo de tokens — Figma + Compose

**Fecha:** 2026-11-03 · **Figma live:** Figwright `get_variable_defs` (plugin conectado)

> **Gobernanza:** contrato de producto date/time → [figma-date-product-contract-2026-11-03.md](figma-date-product-contract-2026-11-03.md). Tokens «reserva» del kit M3 **no** obligan a la app; Figma debe alinearse a Compose.

**Archivo:** MyOwnTrip_nativo - Design System
**Compose SoT consumo UI:** `MaterialTheme.colorScheme` / `typography` / `shapes` + `MOTSpacing` + `LocalExtendedColors`

## Resumen por colección Figma

| Colección | Variables | Consumo Compose | Aplicación Figma |
|-----------|----------:|-----------------|------------------|
| M3 | 322 | `AppColors` → `colorScheme` | Todos los sets DS + láminas |
| Typescale | 100 | `AppTypography` (Fraunces/Inter) | Variables panel + binds en sets |
| MOT · Spacing | 88 | `MOTSpacing.kt` | Sets M3 documentados + producto |
| LEGACY · Semantic Layout (no usar) | 55 | **Prohibido** | Migrar a `mot/*`; no ampliar |
| Primitives · Space | 30 | No directo (alias) | Variables panel + binds en sets |
| Primitives · Size | 13 | No directo (alias) | Variables panel + binds en sets |
| Shape | 10 | `MOTThemeShapes` + ADR 004 botones | Componentes + morph botón |
| Font theme | 7 | Ver doc DS | Variables panel + binds en sets |
| Brand | 5 | `BrandColors` (logo) | Lockups / apariencia marca |

## Color M3 (`Schemes/*`) → Compose

| Token Figma | Rol Compose | Dónde se aplica |
|-------------|-------------|-----------------|
| `Schemes/*` (49 roles × 6 modos) | `MaterialTheme.colorScheme.*` | Toda UI `app/` vía M3 (botones, campos, superficies) |
| `Extended Colors/Success|Warning|Info` (+ containers) | `LocalExtendedColors` | Wallet drift, chips estado plan, indicadores |
| `Palettes/*` | No consumo directo | Base de alias M3 en Figma |
| `State Layers/*` | **Runtime** (8/10/10/16%, disabled 38/12%) | Láminas Figma; **no** importar a Kotlin |

## Shape (`Corner/*`) → Compose

| Figma | Compose | Aplicación |
|-------|---------|------------|
| `Corner/Extra-small` … `Extra-large` | `MaterialTheme.shapes.*` | Cards, chips, sheets, menús |
| `Corner/None` ↔ `Large-increased` | `rememberMOTButtonShape` | Botones producto (ADR 004) |

## Tipografía (`Static/*`) → Compose

| Figma Typescale | Compose | Aplicación Figma | Aplicación app |
|-----------------|---------|------------------|----------------|
| Display/Headline | Fraunces | Sets DS | Títulos pantalla, hero |
| Title/Body/Label | Inter | Sets DS | UI funcional, formularios |
| Button Label * | Inter Medium | Button sets | `MOTButton` label style M3 |

## MOT · Spacing — tabla maestra (88 tokens)

| Token Figma | Espejo Compose | Usos Compose (.kt) | Aplicación Figma (resumen) | Estado |
|-------------|----------------|--------------------|----------------------------|--------|
| `mot/padding/button/large-horizontal` | `MOTSpacing.ButtonLarge.horizontal` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/button/large-vertical` | `MOTSpacing.ButtonLarge.vertical` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/button/medium-horizontal` | `MOTSpacing.ButtonMedium.horizontal` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/button/medium-vertical` | `MOTSpacing.ButtonMedium.vertical` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/button/small-horizontal` | `MOTSpacing.ButtonSmall.horizontal` | 1 archivos | Button SM State-layer (24, nov 2026) | Activo |
| `mot/padding/button/small-vertical` | `MOTSpacing.ButtonSmall.vertical` | 1 archivos | Button SM (8) | Activo |
| `mot/padding/button/xsmall-horizontal` | `MOTSpacing.ButtonXSmall.horizontal` | 1 archivos | Button/Toggle XSmall; TextButton Small corregido | Activo |
| `mot/padding/button/xsmall-vertical` | `MOTSpacing.ButtonXSmall.vertical` | 1 archivos | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Activo |
| `mot/padding/chip/horizontal-lg` | `MOTSpacing.Chip.horizontalLg` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/chip/horizontal-sm` | `MOTSpacing.Chip.horizontalSm` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/chip/vertical` | `MOTSpacing.Chip.vertical` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/date-picker/cell` | `MOTSpacing.DatePicker.cell` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/date-picker/field-top` | `MOTSpacing.DatePicker.fieldTop` | **0** | Date Input · Date / Date return | **Reserva** (espejo sin uso app) |
| `mot/padding/dialog/container` | `MOTSpacing.Dialog.container` | **0** | Basic dialog · content/actions | **Reserva** (espejo sin uso app) |
| `mot/padding/extended-fab/large-horizontal` | `—` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Figma-only (sin espejo Kotlin) |
| `mot/padding/extended-fab/large-vertical` | `—` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Figma-only (sin espejo Kotlin) |
| `mot/padding/extended-fab/medium-horizontal` | `—` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Figma-only (sin espejo Kotlin) |
| `mot/padding/extended-fab/medium-vertical` | `—` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Figma-only (sin espejo Kotlin) |
| `mot/padding/extended-fab/small-horizontal` | `—` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Figma-only (sin espejo Kotlin) |
| `mot/padding/extended-fab/small-vertical` | `—` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Figma-only (sin espejo Kotlin) |
| `mot/padding/fab/default` | `MOTSpacing.Fab.default` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/fab/large` | `MOTSpacing.Fab.large` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/fab/medium` | `MOTSpacing.Fab.medium` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/icon-button/0-horizontal` | `MOTSpacing.IconButton.zeroHorizontal` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/icon-button/0-vertical` | `MOTSpacing.IconButton.zeroVertical` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/icon-button/10-horizontal` | `MOTSpacing.IconButton.tenHorizontal` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/icon-button/10-vertical` | `MOTSpacing.IconButton.tenVertical` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/icon-button/lg-horizontal` | `MOTSpacing.IconButton.lgHorizontal` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/icon-button/lg-vertical` | `MOTSpacing.IconButton.lgVertical` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/icon-button/md-horizontal` | `MOTSpacing.IconButton.mdHorizontal` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/icon-button/md-vertical` | `MOTSpacing.IconButton.mdVertical` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/icon-button/wide-horizontal` | `MOTSpacing.IconButton.wideHorizontal` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/icon-button/wide-vertical` | `MOTSpacing.IconButton.wideVertical` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/list-item/horizontal` | `MOTSpacing.ListItem.horizontal` | 2 archivos | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Activo |
| `mot/padding/list-item/vertical` | `MOTSpacing.ListItem.vertical` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/menu/horizontal` | `MOTSpacing.Menu.horizontal` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/menu/vertical` | `MOTSpacing.Menu.vertical` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/segmented/horizontal` | `—` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Figma-only (sin espejo Kotlin) |
| `mot/padding/segmented/vertical` | `—` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Figma-only (sin espejo Kotlin) |
| `mot/padding/snackbar/horizontal` | `MOTSpacing.Snackbar.horizontal` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/snackbar/vertical` | `MOTSpacing.Snackbar.vertical` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/tab/horizontal` | `MOTSpacing.Tab.horizontal` | 1 archivos | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Activo |
| `mot/padding/tab/indicator-inset` | `MOTSpacing.Tab.indicatorInset` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/padding/tab/vertical` | `MOTSpacing.Tab.vertical` | 1 archivos | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Activo |
| `mot/padding/text-field/horizontal` | `MOTSpacing.TextField.horizontal` | **0** | Text field, Time Input field | **Reserva** (espejo sin uso app) |
| `mot/padding/text-field/outlined-vertical` | `MOTSpacing.TextField.outlinedVertical` | **0** | Text field set · 252 nodos (60 variantes) | **Reserva** (espejo sin uso app) |
| `mot/padding/text-field/vertical` | `MOTSpacing.TextField.vertical` | **0** | Time Input (4), no Outlined producto | **Reserva** (espejo sin uso app) |
| `mot/spacing/button-icon-gap` | `—` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Figma-only (sin espejo Kotlin) |
| `mot/spacing/button-icon-gap-large` | `—` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Figma-only (sin espejo Kotlin) |
| `mot/spacing/button-icon-gap-xsmall` | `—` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Figma-only (sin espejo Kotlin) |
| `mot/spacing/chip/gap` | `MOTSpacing.Chip.gap` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/spacing/component-md` | `MOTSpacing.componentMd` | 1 archivos | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Activo |
| `mot/spacing/component-sm` | `MOTSpacing.componentSm` | 29 archivos | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Activo |
| `mot/spacing/component-xs` | `MOTSpacing.componentXs` | 16 archivos | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Activo |
| `mot/spacing/date-picker/block-gap` | `MOTSpacing.DatePicker.blockGap` | **0** | Modal · Frame 2608508 | **Reserva** (espejo sin uso app) |
| `mot/spacing/date-picker/cell` | `MOTSpacing.DatePicker.cell` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/spacing/date-picker/day-grid-gap` | `MOTSpacing.DatePicker.dayGridGap` | 2 archivos | Modal date picker `Type=Day`; sets Input grid | Activo |
| `mot/spacing/date-picker/header-gap` | `MOTSpacing.DatePicker.headerGap` | **0** | Date Input set · Header | **Reserva** (espejo sin uso app) |
| `mot/spacing/date-picker/label-gap` | `MOTSpacing.DatePicker.labelGap` | **0** | Modal date picker · Label and Date | **Reserva** (espejo sin uso app) |
| `mot/spacing/date-picker/selection-row-gap` | `MOTSpacing.DatePicker.selectionRowGap` | **0** | Modal · Local Selection Row | **Reserva** (espejo sin uso app) |
| `mot/spacing/dialog/actions-gap` | `MOTSpacing.Dialog.actionsGap` | **0** | Basic dialog · acciones | **Reserva** (espejo sin uso app) |
| `mot/spacing/dialog/divider-gap` | `MOTSpacing.Dialog.dividerGap` | **0** | Basic dialog · hueco divisor | **Reserva** (espejo sin uso app) |
| `mot/spacing/dialog/section-gap` | `MOTSpacing.Dialog.sectionGap` | **0** | Basic dialog · título/cuerpo/divisor | **Reserva** (espejo sin uso app) |
| `mot/spacing/extended-fab/gap-large` | `—` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Figma-only (sin espejo Kotlin) |
| `mot/spacing/extended-fab/gap-medium` | `—` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Figma-only (sin espejo Kotlin) |
| `mot/spacing/extended-fab/gap-small` | `—` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Figma-only (sin espejo Kotlin) |
| `mot/spacing/gutter-grid` | `MOTSpacing.gutterGrid` | 2 archivos | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Activo |
| `mot/spacing/keyboard-picker/gap` | `MOTSpacing.KeyboardPicker.gap` | **0** | Keyboard picker · Format 12/24h itemSpacing 20 | **Reserva** (KDoc) |
| `mot/spacing/layout-lg` | `MOTSpacing.layoutLg` | 8 archivos | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Activo |
| `mot/spacing/layout-md` | `MOTSpacing.layoutMd` | 31 archivos | Stacks, cards, calendario modal (padding grid 16) | Activo |
| `mot/spacing/list-item/gap` | `MOTSpacing.ListItem.gap` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/spacing/menu/group-gap` | `MOTSpacing.Menu.groupGap` | 1 archivos | Home · menú filtros (`HomeSearchAndFilter`) | Activo |
| `mot/spacing/none` | `MOTSpacing.none` | 1 archivos | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Activo |
| `mot/spacing/screen-content-bottom` | `MOTSpacing.screenContentBottom` | 11 archivos | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Activo |
| `mot/spacing/screen-content-bottom-with-fab` | `MOTSpacing.screenContentBottomWithFab` | 2 archivos | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Activo |
| `mot/spacing/screen-horizontal` | `MOTSpacing.screenHorizontal` | 27 archivos | Design-file / pantallas; márgenes columna producto | Activo |
| `mot/spacing/segmented/gap` | `—` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Figma-only (sin espejo Kotlin) |
| `mot/spacing/snackbar/gap` | `MOTSpacing.Snackbar.gap` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/spacing/snackbar/gap-sm` | `MOTSpacing.Snackbar.gapSm` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/spacing/snackbar/gap-xs` | `MOTSpacing.Snackbar.gapXs` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/spacing/tab/gap` | `MOTSpacing.Tab.gap` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | **Reserva** (espejo sin uso app) |
| `mot/spacing/time-input/content-gap` | `MOTSpacing.TimeInput.contentGap` | **0** | Time Input · default gap 10 | **Reserva** (KDoc) |
| `mot/spacing/time-input/field-gap` | `MOTSpacing.TimeInput.fieldGap` | **0** | Time Input · Input Field gap 1 | **Reserva** (KDoc) |
| `mot/spacing/time-input/label-gap` | `MOTSpacing.TimeInput.labelGap` | **0** | Direct Input · gap a Time label | **Reserva** (KDoc) |
| `mot/spacing/time-input/label-top` | `MOTSpacing.TimeInput.labelTop` | **0** | Sin nodo enlazado (reserva) | **Reserva** (KDoc) |
| `mot/stroke/emphasis` | `—` | **0** | Time Input Selected=True · stroke 2px | Figma-only (sin espejo Kotlin) |
| `mot/stroke/focus` | `—` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Figma-only (sin espejo Kotlin) |
| `mot/stroke/hairline` | `—` | **0** | Sets DS (Button, FAB, Dialog, …) — ver `spacing.md` | Figma-only (sin espejo Kotlin) |

## Compose — tokens `MOTSpacing` más usados en `app/`

| Propiedad | Archivos | Figma |
|-----------|----------|-------|
| `MOTSpacing.layoutMd` | 31 | `mot/spacing/layout-md` |
| `MOTSpacing.componentSm` | 29 | `mot/spacing/component-sm` |
| `MOTSpacing.screenHorizontal` | 27 | `mot/spacing/screen-horizontal` |
| `MOTSpacing.componentXs` | 16 | `mot/spacing/component-xs` |
| `MOTSpacing.screenContentBottom` | 11 | `mot/spacing/screen-content-bottom` |
| `MOTSpacing.layoutLg` | 8 | `mot/spacing/layout-lg` |
| `MOTSpacing.DatePicker.dayGridGap` | 2 | `mot/spacing/date-picker/day-grid-gap` |
| `MOTSpacing.screenContentBottomWithFab` | 2 | `mot/spacing/screen-content-bottom-with-fab` |
| `MOTSpacing.ListItem.horizontal` | 2 | `mot/padding/list-item/horizontal` |
| `MOTSpacing.gutterGrid` | 2 | `mot/spacing/gutter-grid` |
| `MOTSpacing.DocumentAttachment.previewHeight` | 1 | `—` |
| `MOTSpacing.Menu` | 1 | `—` |
| `MOTSpacing.Menu.groupGap` | 1 | `mot/spacing/menu/group-gap` |
| `MOTSpacing.componentMd` | 1 | `mot/spacing/component-md` |
| `MOTSpacing.none` | 1 | `mot/spacing/none` |
| `MOTSpacing.ButtonXSmall` | 1 | `—` |
| `MOTSpacing.ButtonXSmall.horizontal` | 1 | `mot/padding/button/xsmall-horizontal` |
| `MOTSpacing.ButtonXSmall.vertical` | 1 | `mot/padding/button/xsmall-vertical` |
| `MOTSpacing.ButtonSmall` | 1 | `—` |
| `MOTSpacing.ButtonSmall.horizontal` | 1 | `mot/padding/button/small-horizontal` |
| `MOTSpacing.ButtonSmall.vertical` | 1 | `mot/padding/button/small-vertical` |
| `MOTSpacing.Tab` | 1 | `—` |
| `MOTSpacing.Tab.horizontal` | 1 | `mot/padding/tab/horizontal` |
| `MOTSpacing.Tab.vertical` | 1 | `mot/padding/tab/vertical` |

## Apartado: tokens de reserva

Definición pipeline: existen en **Figma** (y a menudo en nodos del kit) y en **`MOTSpacing`/`BrandColors`**, pero **ningún composable de `app/`** los lee. Cambiarlos en Figma no altera la app hasta conectar.

| Token | Compose | ¿Enlazado en Figma? | Motivo reserva |
|-------|---------|---------------------|----------------|
| `mot/spacing/time-input/label-top` | `MOTSpacing.TimeInput.labelTop` | No | Sin consumidor Compose / kit ahead of app |
| `mot/spacing/time-input/field-gap` | `MOTSpacing.TimeInput.fieldGap` | Sí | Sin consumidor Compose / kit ahead of app |
| `mot/spacing/time-input/content-gap` | `MOTSpacing.TimeInput.contentGap` | Sí | Sin consumidor Compose / kit ahead of app |
| `mot/spacing/time-input/label-gap` | `MOTSpacing.TimeInput.labelGap` | Sí | Sin consumidor Compose / kit ahead of app |
| `mot/spacing/date-picker/header-gap` | `MOTSpacing.DatePicker.headerGap` | Sí | Sin consumidor Compose / kit ahead of app |
| `mot/padding/date-picker/field-top` | `MOTSpacing.DatePicker.fieldTop` | Sí | Sin consumidor Compose / kit ahead of app |
| `mot/spacing/date-picker/label-gap` | `MOTSpacing.DatePicker.labelGap` | Sí | Sin consumidor Compose / kit ahead of app |
| `mot/spacing/date-picker/selection-row-gap` | `MOTSpacing.DatePicker.selectionRowGap` | Sí | Sin consumidor Compose / kit ahead of app |
| `mot/spacing/date-picker/block-gap` | `MOTSpacing.DatePicker.blockGap` | Sí | Sin consumidor Compose / kit ahead of app |
| `mot/spacing/keyboard-picker/gap` | `MOTSpacing.KeyboardPicker.gap` | Sí | Sin consumidor Compose / kit ahead of app |
| `mot/padding/chip/*` | `MOTSpacing.Chip.*` | Sí | Sin consumidor Compose / kit ahead of app |
| `mot/padding/dialog/*` | `MOTSpacing.Dialog.*` | Sí | Sin consumidor Compose / kit ahead of app |
| `mot/padding/fab/*` | `MOTSpacing.Fab.*` | Sí | Sin consumidor Compose / kit ahead of app |
| `mot/padding/snackbar/*` | `MOTSpacing.Snackbar.*` | Sí | Sin consumidor Compose / kit ahead of app |
| `mot/padding/menu/vertical|horizontal` | `MOTSpacing.Menu.vertical/horizontal` | Sí | Sin consumidor Compose / kit ahead of app |
| `mot/padding/icon-button/*` | `MOTSpacing.IconButton.*` | Sí | Sin consumidor Compose / kit ahead of app |
| `mot/padding/text-field/*` | `MOTSpacing.TextField.*` | Sí | Sin consumidor Compose / kit ahead of app |
| `Brand `ink-deep`` | `BrandColors.InkDeep` | Sí | Sin consumidor Compose / kit ahead of app |

### Detalle reserva Date & time (auditoría 2026-10-30 / 2026-11-03)

- **Input date:** `header-gap`, `field-top` → sets Input; app usa `MotDateRangeField` (un solo `OutlinedTextField`).
- **Modal date:** `label-gap`, `selection-row-gap`, `block-gap` → modal M3 kit; app usa `MotTripDatePickerDialog` con `dayGridGap` + `layoutMd` only.
- **Time input:** gaps en sets Time Input; app usa `TimePicker` M3 (`MotTimePickerDialog`), no réplica Figma 1:1.
- **Keyboard picker:** `keyboard-picker/gap` → Format 12/24h; Compose no implementa keyboard picker custom.
- **Chip / Dialog / FAB / Snackbar / IconButton / TextField objects:** espejo para paridad kit; M3 aplica padding por defecto en app.
- **`ink-deep`:** token Brand; lockups usan `ink` / `ocre` (`BrandColors.InkDeep` = alias de `Ink`).

## Figma-only sin espejo Kotlin

| Token | Aplicación Figma | Notas |
|-------|------------------|-------|
| `mot/padding/extended-fab/*` | Sets Extended FAB, Segmented, Date cells | Sin objeto `MOTSpacing`; M3 o kit |
| `mot/spacing/extended-fab/gap-*` | Sets Extended FAB, Segmented, Date cells | Sin objeto `MOTSpacing`; M3 o kit |
| `mot/padding/segmented/*` | Sets Extended FAB, Segmented, Date cells | Sin objeto `MOTSpacing`; M3 o kit |
| `mot/spacing/segmented/gap` | Sets Extended FAB, Segmented, Date cells | Sin objeto `MOTSpacing`; M3 o kit |
| `mot/spacing/button-icon-gap*` | Sets Extended FAB, Segmented, Date cells | Sin objeto `MOTSpacing`; M3 o kit |
| `mot/padding/date-picker/cell` | Sets Extended FAB, Segmented, Date cells | Sin objeto `MOTSpacing`; M3 o kit |

## LEGACY · Semantic Layout (55)

**No usar.** Sustituir binds por `mot/*` al tocar frames. Ver `docs/design-system/spacing.md`.

## Limitaciones de este inventario

- **Nodos Figma:** la columna «Aplicación Figma» combina auditorías (`figma-datetime-matrix`, `spacing.md`, dialog B.3) + reglas de página Buttons (30/30 bound). No lista cada `nodeId` × 630 variables.
- **`docs/design-system/variables.json`:** export parcial (~512 entradas); **live Figma = 630** variables (Figwright nov 2026).
- **Literales `.dp` en `app/`:** ~395 valores sin token; fuera del alcance de fila por fila.

## Referencias

- `docs/design-system/spacing.md`
- `docs/design-system/audits/backlog-tokens-inventory-2026-10-30.md`
- `docs/design-system/audits/figma-datetime-matrix-2026-10-30.md`
- `designsystem/.../MOTSpacing.kt`, `Color.kt`, `Theme.kt`, `Shapes.kt`, `Type.kt`
