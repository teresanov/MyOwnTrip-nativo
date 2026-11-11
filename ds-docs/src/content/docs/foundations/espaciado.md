---
title: Espaciado
description: MOTSpacing para el layout de pantalla; el padding interno de un componente lo pone el componente.
sidebar:
  order: 4
---

El espaciado se divide en dos cosas que conviene no mezclar:

- **Layout de pantalla** — el margen lateral, el aire entre bloques, el colchón inferior de una
  lista. Eso es `MOTSpacing`.
- **Padding interno de un componente** — lo que separa el label del borde de un botón. Eso lo pone
  el componente de Material 3 por defecto, y no se toca salvo que la ficha diga lo contrario.

## Valores de pantalla

| En código | dp | Para qué |
|---|---:|---|
| `MOTSpacing.screenHorizontal` | 16 | Margen lateral de pantalla |
| `MOTSpacing.layoutMd` | 16 | Separación entre bloques |
| `MOTSpacing.layoutLg` | 24 | Separación entre secciones |
| `MOTSpacing.componentXs` | 4 | Hueco mínimo |
| `MOTSpacing.componentSm` | 8 | Hueco pequeño |
| `MOTSpacing.componentMd` | 12 | Hueco medio |
| `MOTSpacing.componentLg` | 16 | Hueco grande |
| `MOTSpacing.gutterGrid` | 16 | Calle entre columnas |
| `MOTSpacing.screenContentBottom` | 24 | Colchón inferior en contenido con scroll |
| `MOTSpacing.screenContentBottomWithFab` | 88 | Igual, cuando hay un FAB flotante |

En Figma, la colección equivalente es **MOT · Spacing** (`mot/spacing/*`).

:::caution[Colección obsoleta]
La colección `Semantic · Layout` de Figma es legado y no se usa en producto. Si una maqueta la
referencia, está desactualizada.
:::

## El caso del botón

Los botones sí tienen su padding documentado, porque el sistema se aparta del valor por defecto en
la variante tonal:

| | Horizontal | Vertical |
|---|---:|---:|
| Por defecto (`MOTButton`, `MOTOutlinedButton`) | 24 | 8 |
| Tonal (`MOTTonalButton`) | 12 | 6 |

La consecuencia es de accesibilidad: un tonal con ese padding se queda en unos 32 dp de alto
visible, así que el área táctil hay que llevarla a 48 dp aunque no se vea.

La fuente de verdad es `MOTSpacing.kt` en el módulo `designsystem`. Para el detalle del mapeo con
Figma, ver `docs/design-system/spacing.md`.
