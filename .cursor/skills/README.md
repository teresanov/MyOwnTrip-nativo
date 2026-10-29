# Skills (MyOwnTrip)

## Núcleo DS agentic (ARC)

- `ds-index/` - genera/actualiza el índice del DS (inventario, relaciones).
- `ds-audit/` - audita y genera reportes accionables (violaciones + referencias).
- `ds-semantic-pipeline/` - sincroniza/valida tokens Figma -> Compose (semánticos, alias, modos).

## Contexto producto/UX

- `myowntrip-context/` - fuente de verdad del proyecto (producto, requisitos, modelo de datos, stack).
- `myowntrip-ux-notion/` - research UX (resumen).

## Utilidades

- `tokens-consumidos/` - sólo cuando pidas reportar consumo.

## Regla de uso

Cuando el usuario pida “escalar DS agéntico”:

1. Cargar `.cursor/instructions/codebase-context.md`
2. Ejecutar ARC: `ds-index` -> `ds-audit` -> (heal) -> `ds-semantic-pipeline` si toca tokens
3. Registrar conocimiento en `docs/design-system/knowledge/` cuando haya nueva decisión.
