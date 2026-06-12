# Marca — logo e icono (MyOwnTrip)

**Fuente de verdad (producto):** [Notion · Brand → Logo e icono de app](https://app.notion.com/p/37c6a48d93c88150add2f0cf30cd2497)  
**Figma:** página **Brand** (librería aparte del DS M3)  
**Código:** `app/.../ui/brand/` · `res/drawable/ic_launcher_*` · `res/drawable/brand_ribbon.xml`

## Sistema de tres niveles

| Nivel | Marca | Contexto | Android |
|-------|-------|----------|---------|
| **W4** | Wordmark «MyOwnTrip» + cinta | Splash, onboarding, store | `MyOwnTripWordmark` (≥ 19dp) |
| **MOT** | Monograma M·O·T + cinta | Toolbar ~21dp | `MyOwnTripMonogram` (16–18dp) |
| **C1** | M en marco-cuaderno + cinta | Launcher, notificaciones | Adaptive icon 3 capas |

Selector automático: `MyOwnTripBrand(height = …)`.

## Colores de marca (logo)

| Token | Hex | Uso |
|-------|-----|-----|
| Tinta | `#4A5864` | Wordmark, marco C1, M/T atenuadas (= `primary` M3 Light) |
| Papel | `#F4F0E8` | Fondo icono C1 |
| **Acento ocre** | `#C48328` | Cinta marcapáginas — **Palettes/Tertiary 60** (más legible que `tertiary` UI) |

> **UI:** `Schemes/Tertiary` = `#825513` (botones, chips). **Logo:** `Brand/ocre` = `#C48328`. No mezclar con **error** (`#B3261E` / `#904A42`).

## Tipografía logo

| Pieza | Fuente | Peso |
|-------|--------|------|
| Wordmark W4 | Fraunces | Medium (500); «Own» itálica |
| MOT — M, T | Fraunces | Light (300) @ ~62% opacidad |
| MOT — O | Fraunces | Bold (700), `#4A5864` |
| C1 — M | Vector (SemiBold-inspired) | En `ic_launcher_foreground.xml` |

Fuentes bundled: `res/font/fraunces.ttf`, `fraunces_italic.ttf`, `inter.ttf`.

## Glifo cinta (marca atómica)

- Forma: marcapáginas con **muesca en V** (`brand_ribbon.xml`).
- **Máximo 1 aparición por pantalla** (icono, firma del wordmark o indicador «guardado» en cards).
- Goma / banda elástica: **descartada**.

## Adaptive icon (Android)

| Capa | Drawable | Contenido |
|------|----------|-----------|
| Background | `ic_launcher_background.xml` | Papel `#F4F0E8` |
| Foreground | `ic_launcher_foreground.xml` | Marco keyline + M + cinta ocre |
| Monochrome | `ic_launcher_monochrome.xml` | Marco + M + cinta (themed Android 13+) |

Zona de respeto: altura de la **M** alrededor del lockup.

## Escala

| Altura lockup | Componente |
|---------------|------------|
| ≥ 19dp | Wordmark W4 |
| 16–18dp | Monograma MOT |
| &lt; 16dp | Solo glifo cinta |

## Reglas (no negociables)

- Sin gradientes ni clichés de viaje (avión, pin, globo).
- Rojo solo para `error` en UI.
- Wordmark/MOT: composables vectoriales (texto Fraunces + `BrandRibbon`); icono: vector drawable.
- Assets en **vector** — no PNG para logo.

## Variantes Figma (pendiente montaje)

- Wordmark W4: positivo · oscuro · monocromo
- MOT: claro · oscuro
- Icono C1
