# Auditoría Figma — Button ADR 004 restore

**Fecha:** 2026-10-29  
**Archivo:** [MyOwnTrip_nativo — Design System](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=57994-2227) (`zrGAL4v6`)  
**Motivo:** Una sesión en el repo archivo (`MyOwnTrip`) rebindeó el radio de Button a `semantic/radius/button/square` (8dp) y paddings Compose del DS viejo. Eso contradice [ADR 004](../../decisions/004-button-shape-morph.md).

## Corrección aplicada

Sets: `Button`, `Button - text`, `Button - elevated`, `Button - outline`, `Button - tonal` (100 variantes).

| Propiedad | Antes (erróneo) | Después (ADR 004) |
|-----------|-----------------|-------------------|
| Content radius · Enabled / Disabled | `semantic/radius/button/square` → 8 | `Corner/None` → **0** |
| Content radius · Hovered / Focused / Pressed | (mismo 8) | `Corner/Large-increased` → **20** |
| State-layer pads | `padding/button/*` archivo (p. ej. MD 24×12) | Valores M3 kit: XS 12×6, SM 16×10, MD 24×16, LG 48×32 |
| itemSpacing | `gap/list` / stacks archivo | M3: 4 / 8 / 8 / 12 por size |

Verificación muestra Medium: Enabled `r=0` · Pressed `r=20`.

## SoT

- Compose: `designsystem/.../MOTButtons.kt` + `rememberMOTButtonShape()`
- Docs: [`shape.md`](../shape.md), [`components.md`](../components.md)
- **No** usar `MyOwnTripButton` / `SemanticLayout.Padding.Button` del repo archivo

## Pendiente (fuera de este restore)

- Icon button Square: confirmar `Corner/None` en reposo (checklist `shape.md` §4)
- Chips: radio fijo 8 (`Corner/Small`); pad vertical M3=6 vs token 8 — no bloquea ADR botón
- Specs escritas hoy en el repo archivo: no portar el contrato viejo; bindings Figma↔Compose viven en [`figma-compose-bindings.md`](../figma-compose-bindings.md)
