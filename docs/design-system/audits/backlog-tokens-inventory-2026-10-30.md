# Inventario de tokens — backlog de reconciliación (30 oct 2026)

**Fase:** sección 8 de [design-pipeline.md](../governance/design-pipeline.md) · paso 1 (inventario de tokens).  
**Fuente Figma:** `MyOwnTrip_nativo - Design System` (`zrGAL4v6`), vía Figwright, `get_variable_defs` · 628 variables antes de la limpieza (620 después).  
**Fuente Compose:** `designsystem/src/main/java/com/myowntrip/app/ui/theme/` (`Color.kt`, `Shapes.kt`, `Theme.kt`, `MOTSpacing.kt`, `MOTButtons.kt`, `MOTTab.kt`).

**Alcance:** comparación por **nombres**. Los **valores** (hex, dp, pesos) aún no están cotejados.

---

## 1. Colecciones de Figma

| Colección | Modos | Uso |
|-----------|------:|-----|
| M3 (`Palettes`, `Schemes`, `State Layers`, `Extended Colors`, `Add-ons`) | 6 | Color |
| Shape (`Corner/*`) | 1 | Formas |
| Typescale (`Static/*`) y Font theme | 1–2 | Tipografía |
| Brand | 1 | Marca |
| Primitives · Space / Size | 1 | Escalas numéricas |
| MOT · Spacing (`mot/*`) | 1 | Espaciado y relleno por componente |
| LEGACY · Semantic Layout | 1 | **No usar** (decidido) |

## 2. Color

| Situación | Estado |
|-----------|--------|
| Roles M3 en Compose (`primary`, `surface`, `outline`…) | **Coinciden por nombre** con `Schemes/*` |
| Extendidos `success`, `warning`, `info` | **Coinciden** con `Extended Colors/*` |
| `Extended Colors/Sucess` y variantes (4) | **Eliminadas** 30 oct 2026 (errata de `Success`; sin alias ni uso en Compose) |
| `Extended Colors/Custom Color 1` y variantes (4) | **Eliminadas** 30 oct 2026 (sin uso en Compose) |
| `Palettes/*` (rampas, ~100) | Base de las rampas; Compose no las consume directamente (correcto) |

## 3. Espaciado y relleno

**Corrección:** la versión anterior de este inventario decía que `MOTSpacing` tenía 18 propiedades genéricas. Es incorrecto: tiene valores generales y **objetos anidados por componente**.

| Objeto en `MOTSpacing` | Valores | Figma `mot/*` correspondiente |
|------------------------|---------|-------------------------------|
| Generales (`screenHorizontal`, `componentXs/Sm/Md`, `layoutLg/Md`, `gutterGrid`, `none`, `gap`, `screenContentBottom(WithFab)`) | ~10 | `mot/spacing/*` generales |
| `ButtonXSmall` | horizontal, vertical | `mot/padding/button/xsmall-*` |
| `Menu` | groupGap | `mot/spacing/menu/group-gap` |
| `ListItem` | horizontal, vertical, gap | `mot/padding/list-item/*`, `mot/spacing/list-item/gap` |
| `DatePicker` | cell, dayGridGap | `mot/padding/date-picker/cell`, `mot/spacing/date-picker/day-grid-gap` |
| `Tab` | horizontal, vertical, gap, indicatorInset | `mot/padding/tab/*`, `mot/spacing/tab/gap` |
| `DocumentAttachment` | previewHeight | (sin token de Figma) |

**Figma tiene más tokens que Compose consume:** `mot/padding/*` (~70) cubre botones de todos los tamaños, FAB, icon-button, dialog, snackbar, text-field, chip, segmented, extended-fab y date-picker. Compose solo tiene objeto para `ButtonXSmall`, `Menu`, `ListItem`, `DatePicker` y `Tab`.

**Literales en código:** `app/` tiene ~395 `.dp` literales y `designsystem/` ~38. Parte son layout de pantalla y parte padding de componente sin objeto en `MOTSpacing`.

## 4. Forma

| Situación | Estado |
|-----------|--------|
| Figma `Corner/*` (10 pasos, None → Full) | Completa |
| Compose `Shapes.kt` (12 valores) | **Pendiente** de comparar nombres y radios |

## 5. Tipografía

| Situación | Estado |
|-----------|--------|
| Figma `Static/*` (Display, Headline, Title, Body, Label, Button Label) | Completa |
| Compose `AppTypography` (Fraunces, Inter) | **Pendiente** de comparar tamaños y pesos |

## 6. Pendientes

1. **Espaciado (decisión de la norma):** para cada `mot/padding/*` de Figma sin objeto en `MOTSpacing`, añadir el objeto en Compose y consumirlo en el composable. Empezar por los componentes de las pantallas en uso (botones, FAB, snackbar, dialog, text-field).
2. **Literales `.dp`:** sustituir por token cuando exista; los de layout de pantalla se revisan aparte.
3. **Comparación de valores:** colores (hex por rol y modo), radios y tipografía.
4. **Figma-only sin uso en Compose:** `mot/padding/*` de `segmented`, `extended-fab`, `date-picker` y `icon-button` (`wide`, `lg`). Confirmar si se necesitan o se podan.
5. **Figma:** comprobar nodos que estuvieran enlazados a las variables eliminadas; al borrar, esos nodos vuelven a su valor literal.

---

*Siguiente en el backlog:* inventario de componentes (paso 2), luego pantallas (paso 3) y paridad con ficha (paso 4).
