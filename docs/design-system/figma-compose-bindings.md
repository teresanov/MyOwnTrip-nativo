---
title: Figma ↔ Compose bindings
description: Mapa canónico entre component sets de la librería Figma, fichas showcase y composables de runtime
status: active
lastUpdated: 2026-06-17
---

# Figma ↔ Compose — bindings

## Regla de oro

**Las pantallas no inventan UI.** Solo ensamblan composables de esta tabla + `MaterialTheme` + copy del design-file.

Si un frame de Figma usa una instancia del DS, **debe existir una fila aquí** antes de cerrar la pantalla.

## Flujo obligatorio (nuevo componente o pantalla)

1. Component set publicado en librería Figma (`zrGAL4v6MEMc9hzZemU432`).
2. Ficha en showcase (`ds-showcase/src/data/components/*.json`) — estado **Ready**.
3. Composable en `app/.../ui/components/` con el mismo nombre lógico.
4. Preview `@Preview` nombrado como el cap de Figma (si aplica).
5. Fila en esta tabla + gate `composeMaterial3MappingValid`.

## Home · Shell `205:813` (`route: trip_list`)

| Cap Figma | Node | Composable(s) | Figma DS |
|-----------|------|---------------|----------|
| cap 1 · vacío | [205:816](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=205-816) | `HomeEmptyState` → `HomePageHero` + `HomeEmptyStackedCard` + `WalletPromoCard` + `MOTButton` | Stacked Outlined Media&text · Stacked Elevated Text only · Button · **Media asset:** `res/drawable-nodpi/home_empty_map.jpg` (export Figma `I215:2935;58710:12855`, script `figma-export-home-empty-map.js`) |
| cap 2 · con viajes | [205:1018](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=205-1018) | `HomeHeroHeader` + `HomeSearchBar` + `TripHeroCard` + `WalletPromoCard` + `TripListCard` | TripHeroCard · Horizontal Outlined · Search bar |
| cap 3 · filtros | [228:8161](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=228-8161) | cap 2 + `HomeFilterMenuPanel` / `HomeFilterDropdownMenu` | Menu `55141:14250` |

Previews de aceptación: `HomeEmptyStatePreview`, `TripListWithTripsPreview`, `TripListFilterMenuPreview`, `HomeFlowReviewPreview`.

## Component sets → Compose

| Figma component set | Node DS | Layout / variante | Compose | Showcase |
|---------------------|---------|-------------------|---------|----------|
| **Stacked card** | [52346:27573](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=52346-27573) | Outlined · Media & text | `MotStackedCard` + `HomeEmptyStackedCard` | [stacked-card](https://teresanov.github.io/MyOwnTrip-nativo/components/stacked-card.html) |
| | | Elevated · Text only | `MotStackedCard` + `WalletPromoCard` | ↑ |
| | | Outlined · Text only | `MotStackedCard` (filtro vacío Home) | ↑ |
| **Horizontal card** | ↑ | Outlined · Media & text · 80dp | `MotHorizontalCard` + `TripListCard` | ↑ |
| **TripHeroCard** | [61199:7862](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61199-7862) | Elevated | `TripHeroCard` | [trip-hero-card](../ds-showcase/components/trip-hero-card.html) |
| **Eyebrow label** | [61202:16834](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61202-16834) | Tertiary · Medium | `EyebrowLabel` | [eyebrow-label](../ds-showcase/components/eyebrow-label.html) |
| **Button** | `55141:14168` | Filled | `MOTButton` | button |
| **Menu** | `55141:14250` | Standard · selección | `HomeFilterDropdownMenu` | — (patrón [`home-filter-menu.md`](patterns/home-filter-menu.md)) |

### Properties Figma → parámetros Kotlin

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

## Qué no duplicar en pantallas

- `OutlinedCard { Text… }` ad hoc → usar `MotStackedCard` o `MotHorizontalCard`.
- Chip como eyebrow en portada → `EyebrowLabel`.
- Banner wallet custom → `WalletPromoCard`.
- Lógica de fase viaje duplicada → `Trip.homePhase()` + helpers en `TripHeroCard` / `HomeHero`.

## Gate de cierre pantalla (añadir a PR UI)

```
[ ] Cada instancia Figma del frame tiene fila en esta tabla
[ ] Preview del cap correspondiente actualizado
[ ] Sin Card/Text M3 sueltos que repliquen un component set del DS
[ ] m3Canonical + android-compose-ux
```

## Archivos runtime

```
app/src/main/java/com/myowntrip/app/ui/components/
  MotCardStyle.kt
  EyebrowLabel.kt
  StackedCard.kt          # MotStackedCard, WalletPromoCard, HomeEmptyStackedCard
  HorizontalCard.kt       # MotHorizontalCard
  TripCoverImage.kt
  TripHeroCard.kt         # TripHeroCard, TripListCard, tripMetaLabel
  home/
    HomeHero.kt           # HomePageHero, HomeHeroHeader, HomeEmptyState
    HomeSearchAndFilter.kt
```
