# Auditoría — Binding shape botones (ADR 004)

**Fecha:** 2026-06-11  
**Archivo Figma:** `zrGAL4v6MEMc9hzZemU432` · Buttons `55141:14168`  
**Repo:** alineado (`Shapes.kt`, `variables.json`, ADR 004)  
**Figma:** binding manual pendiente (esta checklist)

## Resumen

| Capa | Estado |
|------|--------|
| Decisión producto | ✅ ADR 004 |
| Tokens `variables.json` | ✅ `Corner/None`=0, `Corner/Large-increased`=20 |
| Compose `Shapes.kt` | ✅ `rememberMOTButtonShape()` |
| Preview HTML | ✅ `button-shape-comparison.html` |
| Figma component bindings | ✅ Aplicado vía Desktop Bridge (2026-06-12) |

## Aplicado en Figma (Bridge 2026-06-12)

| Área | Acción | Nodos |
|------|--------|-------|
| Button (5 sets) | `Content` → `Corner/None` | 100 |
| Toggle (4 sets) | Square → None · Round → Large-increased | 321 (+81 tonal vía parse nombre) |
| Icon button (4 sets) | Square → None · Round → 20 | 480 |
| Icon togglable (4 sets) | Square → None · Round → 20 | 960 |
| Cards | Contenedor principal → `Corner/Medium` | 10 |

**Nota:** `Toggle button - tonal` tenía errores en el component set; binding resuelto parseando `Type=` del nombre de variante.

## Pendiente / verificación manual

- [ ] Publicar librería Figma (si hay cambios sin publicar).
- [ ] Revisar instancias en `Example layout`.
- [ ] Segmented button: sin `Content` con radio — morph solo en Compose.

## Referencias

- [shape.md](../shape.md)
- [figma-prune-inventory.md](../figma-prune-inventory.md) § Buttons
- [004-button-shape-morph.md](../../decisions/004-button-shape-morph.md)
