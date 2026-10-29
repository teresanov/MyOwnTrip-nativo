# Spacing — MyOwnTrip Nativo

**Decisión (2026-10-29):** el espaciado de **producto / pantallas** es **`MOTSpacing`** (Compose) ↔ colección Figma **`MOT · Spacing`**.  
No usar la colección legado **`Semantic · Layout`** (paths `semantic/padding|spacing/…` del repo archivo) para pantallas ni para nuevos binds.

## Principio

**En UI (Figma y Compose) solo semánticos.** Los primitivos (`spacing/12`, `spacing/16`…) viven en **Primitives · Space** y solo se usan como alias detrás de `mot/spacing/*` (o roles **Shape** / **M3**). Nunca bindear un primitivo directo en un frame de producto o en el contrato de un componente.

**Gate (Figma ↔ Compose):** no crear `mot/*` en Figma sin espejo en Kotlin (`MOTSpacing` / `MaterialTheme.shapes`). Orden: Compose SoT → token/nombre → bind Figma. Kit baseline M3 ≠ producto si Compose no lo consume.

| Qué | Dónde | Notas |
|-----|--------|--------|
| Márgenes de pantalla, gutters, stacks entre bloques | `MOTSpacing` / `mot/spacing/*` | Producto |
| Padding interno documentado (p. ej. Button XSmall) | `MOTSpacing.ButtonXSmall` / `mot/padding/button/xsmall-*` | Es **padding**, no spacing de pantalla |
| Color | Colección **M3** / `colorScheme` | Semántico M3 |
| Corner | Colección **Shape** / `MaterialTheme.shapes.*` | ADR 004 en botones |

## API Compose ↔ Figma

Fuente código: `designsystem/.../MOTSpacing.kt`

| Compose | Figma (`MOT · Spacing`) | Valor |
|---------|-------------------------|------:|
| `MOTSpacing.screenHorizontal` | `mot/spacing/screen-horizontal` | 16 |
| `MOTSpacing.screenContentBottom` | `mot/spacing/screen-content-bottom` | 24 |
| `MOTSpacing.layoutMd` | `mot/spacing/layout-md` | 16 |
| `MOTSpacing.layoutLg` | `mot/spacing/layout-lg` | 24 |
| `MOTSpacing.componentSm` | `mot/spacing/component-sm` | 8 |
| `MOTSpacing.componentMd` | `mot/spacing/component-md` | 12 |
| `MOTSpacing.componentXs` | `mot/spacing/component-xs` | 4 |
| `MOTSpacing.gutterGrid` | `mot/spacing/gutter-grid` | 16 |
| `MOTSpacing.screenContentBottomWithFab` | `mot/spacing/screen-content-bottom-with-fab` | 88 |
| `MOTSpacing.ButtonXSmall.horizontal` | `mot/padding/button/xsmall-horizontal` | 12 |
| `MOTSpacing.ButtonXSmall.vertical` | `mot/padding/button/xsmall-vertical` | 6 |
| *(Figma Button SM)* | `mot/padding/button/small-horizontal` / `…-vertical` | 16 / 10 |
| *(Figma Button MD)* | `mot/padding/button/medium-horizontal` / `…-vertical` | 24 / 16 |
| *(Figma Button LG)* | `mot/padding/button/large-horizontal` / `…-vertical` | 48 / 32 |
| Toggle (mismos pads/gaps que Button por Size) | `mot/padding/button/*` + `mot/spacing/component-*` | — |
| Icon button (chrome) | `mot/padding/icon-button/{0,10,md,lg,wide}-*` | 0 / 10 / 16 / 32 / 32×16 |
| *(Figma FAB Default / Medium / Large)* | `mot/padding/fab/{default,medium,large}` | 16 / 26 / 30 |
| *(Figma Extended FAB SM / MD / LG)* | `mot/padding/extended-fab/{small,medium,large}-*` | 16 / 26 / 28×30 |
| Extended FAB gaps | `mot/spacing/extended-fab/gap-{small,medium,large}` | 8 / 12 / 16 |
| *(Figma Segmented + BB segments)* | `mot/padding/segmented/{horizontal,vertical}` · `mot/spacing/segmented/gap` | 12 / 10 · 8 |
| *(Figma Text field Outlined)* | `mot/padding/text-field/{horizontal,vertical}` · `mot/spacing/none` | 16 / 4 · 0 |
| *(Figma Filter / Assistive chip)* | `mot/padding/chip/{horizontal-sm,horizontal-lg,vertical}` · `mot/spacing/chip/gap` | 8 / 16 / 6 · 8 |
| *(Figma Basic dialog)* | `mot/padding/dialog/{container,actions-gutter}` · `mot/spacing/dialog/{section,title-body,actions}-gap` | 24 / 16 · 16 / 12 / 8 |
| *(Figma Snackbar)* | `mot/padding/snackbar/{horizontal,vertical}` · `mot/spacing/snackbar/gap{,-sm,-xs}` | 16 / 12 · 12 / 8 / 4 |
| `MOTSpacing.Menu.groupGap` | `mot/spacing/menu/group-gap` | 2 |
| `MOTSpacing.ListItem.horizontal|vertical|gap` | `mot/padding/list-item/{horizontal,vertical}` · `mot/spacing/list-item/gap` | 16 / 12 / 12 |
| `MOTSpacing.DatePicker.cell` / `dayGridGap` | `mot/spacing|padding/date-picker/cell` · `mot/spacing/date-picker/day-grid-gap` | 10 / 2 |
| `MOTSpacing.Tab.{horizontal,vertical,gap,indicatorInset}` | `mot/padding/tab/{horizontal,vertical,indicator-inset}` · `mot/spacing/tab/gap` | 16 / 14 / 4 / 2 |
| `MOTSpacing.DocumentAttachment.previewHeight` | *(altura frame Figma; pads card = `layout-md` / `component-sm`)* | 180 |
| *(Figma stroke hairline / emphasis / focus)* | `mot/stroke/hairline` · `emphasis` · `focus` → `size/1\|2\|3` | 1 / 2 / 3 |
| *(kit baseline Menu pad V — no producto)* | `mot/padding/menu/vertical` | 8 |
| Icon–label gap XS / SM·MD / LG | `mot/spacing/component-xs` · `component-sm` · `component-md` | 4 / 8 / 12 |

Todos alias de **Primitives · Space** (`spacing/4` … `spacing/88`, más `spacing/26|28|30` para FAB).

### Bind status · página Buttons (2026-10-29)

Auditoría `paddingLeft`+`paddingTop` con valor > 0 en State-layer / raíz: **30/30 sets · 0 unbound**.

Incluye Button×5, Toggle×4, Icon×8, FAB, Extended FAB, FAB menu + BB, Segmented + BB start/middle/end.

## Colección legado `Semantic · Layout`

- Origen: experimento / repo archivo (2026-10).
- Incluye pads de componente (`padding/button/*`, radios `radius/button/square` = 8, etc.) **incompatibles** con ADR 004 y con M3 interno.
- **No** bindear frames de producto ni sets canónicos nuevos a esa colección.
- Migración: al tocar un frame, sustituir binds de inset/gutter de pantalla por `mot/spacing/*`. Pads internos de componentes → dejar M3 / Shape.
- Borrar o archivar la colección cuando no queden binds (fase posterior; no bloquea Fase A).

## Do / Don't

### Do
- En design-file: padding de página / columnas con `mot/spacing/screen-horizontal`, `layout-md|lg`, `component-sm|xs`.
- En Compose: `Modifier.padding(horizontal = MOTSpacing.screenHorizontal)`.

### Don't
- Bindear `spacing/12` (u otro primitivo) en un nodo de UI.
- Enlazar botones a `semantic/radius/button/square` (8) — reposo es `Corner/None`.
- Usar la colección **LEGACY · Semantic Layout**.
- Confundir insets de pantalla (`mot/spacing/layout-md`) con padding del botón (`mot/padding/button/xsmall-*`).
