---
name: ds-semantic-pipeline
description: >-
  Solo edición de variables Figma (colecciones M3/MOT/Shape, alias, modes Light-Dark).
  No usar para pantallas, componentes ni SemanticColors en Compose. Pipeline producto:
  ver docs/design-system/governance/design-pipeline.md §0.
disable-model-invocation: true
---

# DS Semantic Pipeline — MOT nativo (solo variables Figma)

**Alcance acotado:** crear o refactorizar **variables locales** en la librería Figma `zrGAL4v6` (primitivos, alias semánticos en Figma, modos). Export posterior → `variables.json` → `Color.kt` si aplica.

**Fuente de verdad del proceso completo:** [`docs/design-system/governance/design-pipeline.md`](../../../docs/design-system/governance/design-pipeline.md) — **§0 Agentes**.

## No confundir con el pipeline de producto

| Esto | SoT | Skill |
|------|-----|--------|
| Variables / tokens globales | Figma → export | **Este skill** (solo Figma) |
| Composable, pantalla, cap | **Compose** → Figma espejo | **`myowntrip-ds-parity`** |
| Cierre paridad | [`parity-log.md`](../../../docs/design-system/parity-log.md) | **`myowntrip-ds-parity`** |

**Prohibido en este repo (UI Compose):** consumir `SemanticColors`, `text/primary`, primitivos sueltos, tokens por estado (`hover`/`pressed`). En app solo `MaterialTheme.colorScheme`, `MOTSpacing`, `shapes.*` — ver [`m3-native-ui.mdc`](../../rules/m3-native-ui.mdc).

## Cuándo invocar

- Añadir o renombrar colección/variable en Figma DS
- Reenlazar alias (semántico Figma → primitivo)
- Auditar que en **Figma** no queden hex sueltos bindables

**No invocar** para montar pantallas, paridad design-file ni sustituir `myowntrip-ds-audit` (diff Kotlin).

## Workflow (Figma-first solo tokens)

1. Leer [`design-pipeline.md`](../../../docs/design-system/governance/design-pipeline.md) §1 (bidireccional tokens).
2. Auditar subárbol variables en Figma (`resolveVariables` false/true).
3. `Primitives · Color` única para primitivos; semánticos de producto en colecciones **M3** / **MOT · Spacing** / **Shape** (no `LEGACY · Semantic Layout`).
4. Export: `python scripts/export_figma_variables_json.py`
5. Diff: `python .cursor/skills/myowntrip-ds-audit/scripts/diff-variables-kotlin.py`
6. Fila en [`parity-log.md`](../../../docs/design-system/parity-log.md) si hubo cambio publicado.

## Inputs

- File key DS: `zrGAL4v6MEMc9hzZemU432`
- Estado repo: `docs/design-system/variables.json`, `designsystem/.../Color.kt`, `Theme.kt`

`ds-config.json` del repo archivo **no** es SoT de este proyecto.

## Salida

- Variables Figma enlazadas; export JSON actualizado si corresponde
- Sin nuevos `figma-*-audit-*.md` — solo parity-log