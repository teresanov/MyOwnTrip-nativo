# Migración `.cursor` desde archivo `MyOwnTrip` → `MyOwnTrip_nativo`

**Fecha:** 2026-10-29

## Motivo

El SoT de app/Compose/agente debe vivir donde está el código. Trabajar en el workspace archivo hacía que rules/skills no cargaran en chats de nativo.

## Copiado a este repo

### Rules
- `figma-instances-only.mdc` (Compose SoT = este repo)
- `figma-ds-documentation.mdc`
- `utilities/` (tokens, bindings, icon-color, skills-integration)
- `components/` (atomic, composition, metadata, material-refs)
- `codebase-index/`

### Skills
- `ds-audit/`, `ds-index/`, `ds-semantic-pipeline/`, `tokens-consumidos/`
- `skills/README.md`

### Actualizado in-place
- `skills/myowntrip-context/` + `references/tech-stack.md`
- `instructions/codebase-context.md`

## Conservado (ya existía en nativo)
- `m3-native-ui.mdc`, `figma-app-flows.mdc`, `android-compose-ux.mdc`, `myowntrip-development.mdc`
- `myowntrip-ds-audit/`, `myowntrip-ux-notion/`

## No copiado
- Scripts temporales de botón (`btn-chunk-*.js`, etc.) del archivo

## Uso
Abrir el chat / workspace en **`MyOwnTrip_nativo`**. El hermano `MyOwnTrip` queda legacy.
