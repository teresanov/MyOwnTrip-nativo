# Fase B — Home cards (Figma ↔ Compose)

**Fecha:** 2026-10-29  
**SoT:** Compose nativo + [spacing.md](../spacing.md) + [shape.md](../shape.md)  
**Sets:** Stacked card `52346:27573` · Horizontal card `52350:27876` · TripHeroCard `61199:7862` · Eyebrow label `61202:16834`

## Compose (referencia)

| Composable | Shape | Pads / gaps relevantes |
|------------|-------|------------------------|
| `MotStackedCard` | `OutlinedCard` / `ElevatedCard` → `shapes.medium` = **12** | Contenido `MOTSpacing.layoutMd` (16) |
| `MotHorizontalCard` | idem | Texto `layoutMd` (16); gap título/sub **4**; media **80×80** |
| `TripHeroCard` | Portada `MaterialTheme.shapes.medium` **12** | Overlay / eyebrow inset `layoutMd` (16); gap overlay **8**; CTA tonal; columna gap `componentSm` (8) |
| `EyebrowLabel` | `shapes.small` = **8** | **Medium** 8×4 · **Small** 16×4 |

## Cambios Figma aplicados

| Cambio | Detalle |
|--------|---------|
| Radio card | `semantic/radius/card` → **`Corner/Medium`** (y roots 12 unbound) |
| Pads/gaps 4/8/16/24 | LEGACY `semantic/spacing|padding/*` → **`mot/spacing/*`** |
| Header stacked (era 12) | → **`mot/spacing/layout-md` (16)** = Compose `MOTSpacing.layoutMd` |
| State-layer botón XSmall | Semánticos **`mot/padding/button/xsmall-horizontal|vertical`** (12×6); nunca primitivo suelto |
| Eyebrow Medium | Era 16×8; alineado a Compose **8×4** (`component-sm` / `component-xs`) |
| Eyebrow Small | Alineado a Compose **16×4** (`layout-md` / `component-xs`) |

Stats script: variantes 22 · radios~120 · pads→MOT 66 · gaps→MOT 30 · spacing/12×5 · eyebrow Medium×3.

## Hallazgos / no tocado

| Item | Nota |
|------|------|
| `Text content` gap **32** en Stacked | Sigue; Compose no replica ese gap interno M3 en el mismo nodo — revisar en Fase B+ si molesta visualmente |
| Botones dentro de cards (State-layer 12×6) | Pads M3 del tonal/XSmall; no son `MOTSpacing` de pantalla |
| Design-file Home (`Vf2t…`) | B.2b: ver [figma-home-design-file-phase-b2b-2026-10-29.md](figma-home-design-file-phase-b2b-2026-10-29.md) |
| Xiaomi | Contrastar Home vacío + con viajes tras publish librería |

## Checklist salida

```
[x] Sets card Home: radio Corner/Medium
[x] Binds spacing producto → MOT · Spacing (4/8/16/24)
[x] Eyebrow pads = Compose
[x] Instancias design-file Home migradas → B.2b
[x] Screenshot design-file cap 1+2 → B.2b (Xiaomi: pendiente humano)
```


