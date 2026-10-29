---
name: ds-index
description: Genera y mantiene el indice canónico del DS (componentes, composición y tokens) para habilitar ARC (Audit/Report/Compose).
---

# DS Index - MyOwnTrip

Este skill crea/actualiza artefactos en `docs/design-system/index/` para que la IA no explore a ciegas.

## Inputs

- `ds-config.json`
- `docs/design-system/templates/component-schema-contract.template.json`
- Codebase Compose en `app/src/main/java/com/example/myowntrip/ui/`

## Salidas (fuente de verdad operativa)

- `docs/design-system/index/components.index.json`
- `docs/design-system/index/composition.index.json` (si aplica)
- `docs/design-system/index/tokens.index.json` (si aplica)

## Protocolo (ARC - Audit)

1. **Inventario**: listar composables canónicos en `ui/components`.
2. **Extraer API**: nombre del composable, params principales, enums de variants/sizes.
3. **Extraer consumo**:
   - Tokens semánticos usados (via `SemanticColors.*` o `MaterialTheme.colorScheme`).
   - Hardcodes prohibidos (Color literals, PrimitiveColors en features).
4. **Extraer composición**:
   - Si el componente compone hijos (slots), registrar children canónicos esperados.
5. **Registrar gates**:
   - A11y mínimo, estados, do/don't Material revisados, paridad dark, composición canónica.

## Reglas

- No escribir “documentación bonita”: el índice debe ser **mínimo y ejecutable**.
- Todo componente canónico debe tener `component:<Name>` como `id`.
- La ausencia de información se marca como `dosDontsReviewed=false` o gates pendientes (no inventar).

## Actualización incremental

Cuando aparece un nuevo componente canónico:

- Añadirlo al índice con flags “pendiente” donde falte evidencia.
- Dejar una entrada `knowledge` si se requiere decisión nueva (ej. do/don't Material, constraints de contenido).
