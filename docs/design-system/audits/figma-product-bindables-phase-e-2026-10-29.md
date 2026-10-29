# Fase E — Bindables sueltos (producto)

**Fecha:** 2026-10-29  
**Scope:** sets de [figma-compose-bindings.md](../figma-compose-bindings.md) — no kit M3 entero  
**Campos:** fill/stroke color · stroke weight (lados) · (opacity anotada, no forzada)

## Baseline

| Métrica | Valor |
|---------|------:|
| Unbound color+stroke+weight (bruto) | **729** |
| Opacidades <1 anotadas (state-layer, etc.) | ~200 |

## Tokens nuevos (MOT · Spacing)

| Token | Alias | dp |
|-------|-------|---:|
| `mot/stroke/hairline` | `Primitives · Size` / `size/1` | 1 |
| `mot/stroke/emphasis` | `size/2` | 2 |
| `mot/stroke/focus` | `size/3` | 3 |

No usar `semantic/stroke/*` (LEGACY · Semantic Layout).

## Oleada 1 — aplicado

| Acción | Cantidad / detalle |
|--------|--------------------|
| Stroke weights (lados `stroke*Weight`) | ~522 nodos de contrato (outline, chips, TF caret, focus ring, dividers…) |
| Stroke color → `Schemes/Outline Variant` | Menu/item **Divider** LINE (~70) |
| Fill blanco → `Schemes/Surface Container Lowest` | Text field Supporting text + Icon/hero whites (~75) |

**Nota API:** en muchos nodos el bind vive en `strokeTop/Right/Bottom/LeftWeight`, no en `strokeWeight`. Auditar los cuatro lados.

## Clasificación restante

### Documental / estructural (aceptado)

| Qué | Motivo |
|-----|--------|
| Stroke morado `151,71,255` en `COMPONENT_SET` | Chrome Figma del set |
| Safety-zone `#ff00f5` | Guía M3 |
| Colourful logo (fills/strokes arcoíris) | Asset de marca en Assist chip · no Schemes |
| Opacity 0.38 / 0.04 en disabled / state-layer | Comportamiento M3; sin token MOT de opacidad de producto aún |

### Contrato pendiente

**0** fill/stroke/weight de contrato sin variable (re-audit post-oleada).

## Checklist gobernanza

```
[x] Scope = producto (bindings), no todo M3
[x] Tokens stroke MOT (no LEGACY)
[x] Weights focus/outline/hairline enlazados
[x] Dividers Menu → Outline Variant
[x] Whites Supporting text → Surface Container Lowest
[x] Documental clasificado (logo, safety, set chrome, opacity)
[x] 0 bindables de contrato pendientes
```

## Cierre (re-audit)

| | |
|--|--:|
| Contrato unbound (color/stroke/weight) | **0** |
| Documental / estructural restante | **150** (set chrome, safety, Colourful logo) |
