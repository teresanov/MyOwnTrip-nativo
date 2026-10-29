# Fase C — Tabs

**Fecha:** 2026-10-29  
**Figma:** página **Tabs** · set [Tabs](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=54563-40023) `54563:40023` + Building Blocks Primary/Secondary  
**Compose:** `MOTTab` + `MOTSpacing.Tab` en Trip detail / Day hub (paridad)

## Compose SoT

| Token | Kotlin | Figma |
|-------|--------|-------|
| Pad H | `MOTSpacing.Tab.horizontal` (16) | `mot/padding/tab/horizontal` |
| Pad V | `MOTSpacing.Tab.vertical` (14) | `mot/padding/tab/vertical` |
| Gap icon–label | `MOTSpacing.Tab.gap` (4) | `mot/spacing/tab/gap` |
| Indicator inset | `MOTSpacing.Tab.indicatorInset` (2) | `mot/padding/tab/indicator-inset` |
| Edge padding scrollable | `MOTSpacing.none` | — |

Primario nuevo: `spacing/14`.

## Consumo

| Superficie | API |
|------------|-----|
| Wrapper | `designsystem` → `MOTTab` (content overload + pads en label; no slot `text=`) |
| Trip detail + preview | `ScrollableTabRow(edgePadding = MOTSpacing.none)` + `MOTTab(label=…)` |
| Day hub | `TabRow` + `MOTTab(label=…)` |

## Figma

~**1700** rebinds en Tabs + 5 BB. Pill → `Corner/Full` · selected chip-like 12 → `Corner/Medium`.
