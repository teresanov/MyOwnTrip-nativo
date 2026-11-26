---
name: ds-index
description: >-
  LEGADO ARC — índice components.index.json no existe en MOT nativo. Usar
  figma-compose-bindings.md y parity-log. No invocar salvo regeneración explícita del índice.
disable-model-invocation: true
---

# DS Index (ARC) — LEGADO en MOT nativo

Skill para generar `docs/design-system/index/components.index.json` y artefactos ARC.

**Estado nov 2026:** la carpeta `docs/design-system/index/` **no está** en el repo. El mapa operativo es [`figma-compose-bindings.md`](../../../docs/design-system/figma-compose-bindings.md) + [`parity-log.md`](../../../docs/design-system/parity-log.md).

## Usar en su lugar

- Inventario componentes ↔ Figma: **bindings**
- Cierre paridad: **`myowntrip-ds-parity`**
- Pipeline: [`design-pipeline.md`](../../../docs/design-system/governance/design-pipeline.md) **§0**

## Si el usuario pide «regenerar el índice»

Confirmar que quieren **crear** el pipeline ARC en nativo; si no, redirigir a bindings. No citar Pencil ni `SemanticColors` como consumo UI.