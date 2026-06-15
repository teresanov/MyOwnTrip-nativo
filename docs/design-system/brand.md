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
| Tinta (`ink`) | `#4A5864` | My/Trip (W4+), M/T MOT (fill 100%), marco C1, monocromo |
| Tinta profunda (`ink-deep`) | `#4A5864` | Token DS; sin uso en lockups jun 2026 |
| Papel (`paper`) | `#F4F0E8` | Fondo icono C1 |
| **Acento ocre** (`ocre`) | `#C48328` | **Own** (W4+ / dark), **O** (MOT light), cinta |
| Sobre oscuro (`on-dark`) | `#F9EFE2` | My/Trip (W4 dark), MOT dark (M/O/T) |

### Bindings por variante (Figma ↔ Compose)

| Activo | Variante | My / Trip / M / T | Own / O | Cinta |
|--------|----------|-------------------|---------|-------|
| W4 | Positive | `ink` | `ocre` | `ocre` |
| W4 | Dark | `on-dark` | `ocre` | `ocre` |
| W4 | Monochrome | `ink` | `ink` | `ink` |
| MOT | Light | `ink` fill + **Appearance 85%** (Muted) | `ocre` (Emphasis) | `ocre` |
| MOT | Dark | `on-dark` fill + **Appearance 85%** (Muted) | `on-dark` (Emphasis) | `ocre` |
| C1 | — | `ink` (M) | — | `ocre` |

### Accesibilidad (contraste, jun 2026)

Criterio: texto grande / logotipo ≥ **3:1** · texto normal ≥ **4.5:1** · gráficos UI ≥ **3:1** (WCAG 2.2). Fondos de referencia: `paper` `#F4F0E8`, `Schemes/Surface` Light `#FFF8F2`, Dark `#17130B`.

| Par | Ratio | Large 3:1 | Notas |
|-----|-------|-----------|-------|
| `ink` sobre `paper` / surface | 6.4–7.0:1 | ✓ | My, Trip, monocromo |
| `on-dark` sobre surface dark | 16.3:1 | ✓ | W4 / MOT dark |
| `ocre` sobre surface dark | 5.8:1 | ✓ | Own dark, cinta en dark |
| `ocre` sobre surface light | **3.0:1** | ✓ (límite) | Own positive, O MOT — OK en splash/UI (`surface`) |
| `ink` + Appearance **85%** sobre surface light (M/T MOT) | **4.8:1** | ✓ normal | Mínimo editorial que cumple WCAG; Compose `MotMutedLayerOpacity` |

> Los lockups son **logotipos** (exención WCAG 1.4.3 para texto de marca). Aun así, el acento ocre cumple 3:1 sobre `surface` Light donde se usa el wordmark en app.

> **UI:** `Schemes/Tertiary` = `#825513` (botones, chips). **Logo:** `Brand/ocre` = `#C48328`. No mezclar con **error** (`#B3261E` / `#904A42`).

## Tipografía logo

| Pieza | Fuente | Peso |
|-------|--------|------|
| Wordmark W4 | Fraunces | Medium (500); «Own» itálica **ocre** en +/dark |
| MOT — M, T | Fraunces | Light (300) — **Brand/MOT/Muted**; fill `ink` / `on-dark` + **Appearance 85%** |
| MOT — O | Fraunces | Bold (700) — **Brand/MOT/Emphasis**; `ocre` (light) / `on-dark` (dark) |
| C1 — M | Vector (SemiBold-inspired) | En `ic_launcher_foreground.xml` |

Fuentes bundled: `res/font/fraunces.ttf`, `fraunces_italic.ttf`, `inter.ttf`.

**Estilos Figma (`Brand/*`):** tipografía en text styles; color en variables de nodo (limitación API).

| Estilo | Uso |
|--------|-----|
| `Brand/W4/My-Trip` · `Brand/W4/Own` | Wordmark |
| `Brand/MOT/Muted` · `Brand/MOT/Emphasis` | Monograma |
| `Brand/C1/M/96` · `Brand/C1/M/48` | Glifo M del icono |
| `Brand/Meta/Label` · `Section-Title` · `Note` | Labels del frame showcase |

Scripts Bridge: `scripts/figma-brand-bind-vars-part*.js`, `figma-brand-text-styles.js`.

**Quirk Bridge:** al bindear con color base `#000`, Figma muestra el enlace correcto pero el canvas en negro/blanco hasta unlink+⌘Z. Los scripts resuelven el valor de la variable como color del paint antes de `setBoundVariableForPaint` (ver `figma-brand-bind-utils.js`).

**MOT muted (M/T):** fill enlazado a variable al **100%**; atenuación vía **Appearance** (capa) al **85%** — no opacidad del fill (rompe el link en UI). Compose: `BrandColors.MotMutedLayerOpacity` (= `Ink`/`OnDark` con alpha 0.85).

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

## Interacción editorial (botones)

Firma de marca en controles táctiles — ADR [`004-button-shape-morph`](../decisions/004-button-shape-morph.md):

| Estado | Radio | Notas |
|--------|-------|-------|
| Reposo | **0dp** | Rectángulo editorial |
| Pressed / focus / hover | **20dp** | Morph ~520ms, curva M3 emphasized decelerate |
| Reduce motion | 0ms | Sin animación de radio |

**Compose:** `MOTButton`, `MOTTextButton`, `MOTIconButton` en `ui/theme/MOTButtons.kt` — delegan en `rememberMOTButtonShape()`. **FAB** permanece circular (excepción ADR 004).

**Figma (Design System):** Button Type=Square en reposo; binding `Corner/None` → `Corner/Large-increased` en interacción. Publicado en librería [MyOwnTrip_nativo — Design System](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System).

## Variantes Figma (logo)

Página **Brand** (librería aparte del DS M3):

| Activo | Variantes |
|--------|-----------|
| Wordmark W4 | positivo · oscuro · monocromo |
| MOT | claro · oscuro |
| Icono C1 | adaptive (3 capas) |

Montaje en Figma en curso; assets de referencia en `app/.../ui/brand/` y `res/drawable/ic_launcher_*`.
