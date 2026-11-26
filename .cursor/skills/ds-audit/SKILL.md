---
name: ds-audit
description: >-
  LEGADO ARC — no usar para cierres en MOT nativo. Requiere components.index.json
  inexistente. Usar myowntrip-ds-parity + parity-log o myowntrip-ds-audit para tokens.
disable-model-invocation: true
---

# DS Audit (ARC) — LEGADO en MOT nativo

Este skill describe el pipeline **ARC** del repo archivo (`../MyOwnTrip`): informes JSON bajo `docs/design-system/index/reports/`, gates con `SemanticColors`, índice `components.index.json`.

**En `MyOwnTrip_nativo` ese índice no existe y no es el pipeline vigente.**

## Usar en su lugar

| Objetivo | Herramienta |
|----------|-------------|
| Paridad Compose ↔ Figma, caps, bindings | **`myowntrip-ds-parity`** + [`parity-log.md`](../../../docs/design-system/parity-log.md) |
| Drift `variables.json` ↔ `Color.kt`, m3Canonical | **`myowntrip-ds-audit`** |
| Mapa componentes ↔ código | [`figma-compose-bindings.md`](../../../docs/design-system/figma-compose-bindings.md) |
| Proceso único | [`docs/design-system/governance/design-pipeline.md`](../../../docs/design-system/governance/design-pipeline.md) **§0** |

## Solo reactivar ds-audit si…

- Se implementa y mantiene `docs/design-system/index/components.index.json` en este repo, **y**
- Se actualiza este skill para gates **m3Canonical** (sin `SemanticColors` en UI).

Hasta entonces: **no generar** `*.ds-audit.json` ni seguir protocolo ARC aquí.