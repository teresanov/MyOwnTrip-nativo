# ADR 004 — Button shape: reposo 0dp, morph a 20dp

**Estado:** Aceptado  
**Fecha:** 2026-06-11  
**Relacionado:** ADR 002 (marca editorial), ADR 003 (Figma = librería)

## Contexto

Tras explorar radios fijos (M3 kit, 8dp, híbrido 8/12) y 0dp estático en el laboratorio [`button-shape-comparison.html`](../design-system/previews/button-shape-comparison.html), se eligió una firma más editorial: **rectángulo en reposo** con **morph suave a 20dp** en interacción.

En móvil no hay hover; el morph aplica en **pressed**, **focus** y **selected** (tabs, toggles, segmented).

## Decisión

### Botones (Square en Figma)

| Estado | Corner radius | Token Figma |
|--------|---------------|-------------|
| Reposo (default, disabled visual) | **0dp** | `Corner/None` |
| Hover (donde exista) | **20dp** | `Corner/Large-increased` |
| Focus | **20dp** | `Corner/Large-increased` |
| Pressed | **20dp** | `Corner/Large-increased` |
| Selected (toggle, tab, segmented activo) | **20dp** | `Corner/Large-increased` |

- **Todos los tamaños** (XSmall → Large) comparten la misma pareja reposo/interactivo.
- **Estilos:** filled, tonal, outline, text, elevated — misma regla de shape.
- **Morph:** solo **None → Large-increased**; prohibido Round en reposo que vuelva a None.
- **Motion:** ~**520ms**, curva **M3 emphasized decelerate** `(0.05, 0.7, 0.1, 1)`; respetar `LocalReduceMotion` → duración 0.
- **State layers:** color por estado sigue en runtime (8/10/10/16%); el radio no sustituye el feedback de color.

### Excepciones

| Componente | Shape | Motivo |
|------------|-------|--------|
| **FAB / Extended FAB** | Circular (`Corner/Full` o círculo) | Patrón M3; no aplica morph 0→20 |
| **Cards** | **12dp** fijo (`Corner/Medium`) | Contenedor editorial; sin morph |
| **Chips** | **8dp** fijo (`Corner/Small`) | Densidad; selected usa color, no morph de radio obligatorio |
| **Icon button** Square | 0 → 20 en interacción | Misma regla que Button |
| **Toggle / Segmented** | Square reposo + variante **Round** a 20dp como destino del morph | KEEP en Figma (ver inventario) |

### Figma

- **Button** set: Type=**Square** only en reposo; bindear radio a `Corner/None`.
- Conservar variantes **Round** en Toggle, Icon togglable y Segmented como geometría de destino (20dp, no píldora 1000).
- **CUT** Button Type=Round en reposo (sin cambio respecto a inventario previo).

### Compose

- `MOTShapes` / `rememberMOTButtonShape()` en `ui/theme/`.
- `MaterialTheme.shapes` para cards/chips; **override** de `shape` animado en botones.
- `motionScheme = MotionScheme.standard()` global; morph espacial con `AppMotion.DurationShapeMorph`.

## Consecuencias

- Actualizar [`shape.md`](../design-system/shape.md), [`figma-prune-inventory.md`](../design-system/figma-prune-inventory.md) y [`android-compose-ux.md`](../ux/android-compose-ux.md).
- Checklist manual Figma en `shape.md` § Binding.
- Supersede la recomendación «híbrido 8/12» del laboratorio (opción C); A–D quedan como referencia histórica en el HTML.

## Referencias

- Preview: [`docs/design-system/previews/button-shape-comparison.html`](../design-system/previews/button-shape-comparison.html)
- Tokens: [`docs/design-system/variables.json`](../design-system/variables.json) → `Corner/None`, `Corner/Large-increased`
- M3 shape scale: https://m3.material.io/styles/shape/corner-radius-scale
