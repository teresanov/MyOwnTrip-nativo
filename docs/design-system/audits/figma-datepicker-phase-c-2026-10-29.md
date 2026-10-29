# Fase C — Date / time pickers

**Fecha:** 2026-10-29  
**Compose:** `MotDatePickerDialog` / `MotDateRangePickerDialog` / `MotTimePickerDialog` / `MotTripDatePickerDialog`  
**Figma:** `Modal date picker` `51954:18136` · `Input date picker` `51954:18540`

## Compose SoT (corregido)

| Antes | Después |
|-------|---------|
| `RoundedCornerShape(28.dp)` literales | **`MaterialTheme.shapes.extraLarge`** |
| Year cell `12.dp` | **`MaterialTheme.shapes.medium`** |
| Pads horizontales dialog | `MOTSpacing.screenHorizontal` (ya) |

## Figma (pads/gaps internos)

| Set | Rebinds | Pendientes (quirks kit) |
|-----|--------:|-------------------------|
| Modal date picker | **1508** | `itemSpacing` 36/74/244 · `paddingLeft` 64 |
| Input date picker | **33** | gap 36 · padTop 18 · gap 1 |
| Keyboard / Dial | 59 / 85 | similares |

Tokens nuevos: `mot/spacing|padding/date-picker/cell` (10) · `mot/spacing/date-picker/day-grid-gap` (2) ↔ `MOTSpacing.DatePicker`.

Resto de 4/8/12/16/24 → `mot/spacing/component-*` / `layout-*` ya existentes.
