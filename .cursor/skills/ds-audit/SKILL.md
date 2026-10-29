---
name: ds-audit
description: Audita el DS contra quality gates (tokens, a11y, Material usage, composición canónica) y genera reportes accionables.
---

# DS Audit - MyOwnTrip

Auditoría operativa del DS usando los artefactos del índice + knowledge base.

Cierre de componente: validar gates del pipeline **Pencil → Figma → Notion → Showcase** (`docs/design-system/governance/design-pipeline.md`).

## Inputs

- `docs/design-system/index/components.index.json`
- `docs/design-system/knowledge/manifest.json`
- Código objetivo en `app/src/main/java/...`

## Output

- `docs/design-system/index/reports/audits/YYYY-MM-DD_HHMMSS.ds-audit.json` (u otra convención versionada; ver `index/reports/README.md`)

## Gates estándar (por componente)

- `semantic-token-only-consumption`
- `figma-alias-chain-valid` (cuando aplique)
- `compose-material3-mapping-valid`
- `dark-mode-parity`
- `a11y-contrast-pass`
- `a11y-touch-target-48dp`
- `a11y-focus-visible`
- `interaction-states-complete`
- `material3-dos-donts-reviewed`
- `canonical-subcomponents-enforced`
- `content-constraints-defined`

## Protocolo (ARC - Report)

1. Seleccionar scope (componentes o paths).
2. Para cada componente del índice:
   - Escanear el archivo fuente por patrones prohibidos:
     - `Color(0x` literals
     - `PrimitiveColors.`
     - `SemanticColors.Light` hardcodeado sin resolver modo
   - Verificar gates declarados en el índice.
3. Enriquecer cada violación con referencias:
   - Primero buscar en knowledge base (`docs/design-system/knowledge/*.entry.json`).
   - Si falta, registrar “pendiente” y proponer crear entry nueva.
4. Generar reporte JSON compacto y accionable.

## Protocolo (ARC - Compose / Heal)

Cuando el usuario pida “arregla estas violaciones”, el agente debe:

- aplicar la corrección mínima,
- evitar romper el canon (usar subcomponentes DS),
- actualizar el índice (si cambió la API),
- y anexar una entry de knowledge si hubo decisión nueva.
