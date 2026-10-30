---
name: ds-semantic-pipeline
description: Pipeline para sincronizar variables de Figma a Compose usando consumo semantico y gobernanza de MyOwnTrip.
---

# DS Semantic Pipeline - MyOwnTrip

Usa este skill cuando el usuario pida sincronizar, auditar o escalar el sistema de tokens del DS entre Figma y Android Compose.

**Contexto de pipeline:** cambios de **contrato/token global** siguen **Modo B (Figma-first)**; componentes nuevos siguen **Compose → Figma → Showcase → Notion** (`governance/design-pipeline.md`). Compose es la fuente de verdad.

## Objetivo

Mantener una cadena confiable:

1. Primitivos en Figma
2. Semanticos enlazados por alias
3. Consumo en Compose solo desde semanticos

## Reglas no negociables

- No consumir primitivas directas en componentes UI.
- No copiar hex sueltos en semanticos; siempre alias a primitivas.
- Mantener `Primitives · Color` como unica coleccion de primitivos de color.
- Tipografia de documentacion/componentes en Figma: `Roboto Flex` con `text styles` del DS.
- Antes de implementar un componente, consultar la guia oficial de Material 3 del componente y registrar do/don't aplicables.

## Inputs minimos

- `ds-config.json` con `fileKey` y collections.
- Estado actual de `ui/theme/Color.kt` y `ui/theme/Theme.kt`.
- Variables locales de Figma extraidas via MCP.

## Workflow operativo

1. Leer `ds-config.json`.
2. Consultar normas externas: Material 3 (componente objetivo) y WCAG relevante.
3. Obtener variables de Figma (primitivas y semanticas) desde el archivo configurado.
4. Verificar que cada token semantico de color referencia una primitiva por alias.
5. Generar o actualizar mapeo semantico para Compose.
6. Verificar contrato de componente: estados, accesibilidad, interaccion, constraints de contenido y responsive.
7. Validar quality gates:
   - semantic-only consumption
   - alias-chain valid
   - material3 mapping valid
   - contraste base
   - touch target >= 48dp
   - focus visible
   - do/don't material documented
8. Actualizar documentacion en `docs/design-system/`.

## Salida esperada

- Cambios en tema Compose alineados con semanticos.
- Reporte de drift (si existe) entre Figma y Android.
- Notas de gobernanza documentadas.
- Registro de conformidad del componente con Material 3 + excepciones justificadas.

## Criterios de rechazo

- Se detectan componentes consumiendo primitivas directas.
- Se detectan semanticos con hex hardcodeado.
- Falta de paridad light/dark en tokens semanticos criticos.
- No existe evidencia de consulta de guia Material 3 para el componente.
- El componente no cumple requisitos minimos de accesibilidad.
