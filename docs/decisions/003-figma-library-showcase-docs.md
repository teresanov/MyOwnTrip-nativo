# ADR 003 — Figma librería; Showcase documentación

**Estado:** Aceptado  
**Fecha:** 2026-06-08

## Contexto

El equipo construye componentes y variables en Figma (kit M3 tematizado), pero documentar en Figma (láminas, Do/Don't, matrices en canvas) duplica esfuerzo y no escala para dev + negocio.

## Decisión

| Capa | Rol | Qué NO hacer |
|------|-----|--------------|
| **Figma DS** | Librería visual: variables, component sets, instancias para diseño de pantallas | Documentación de componentes (láminas, guías, changelog por componente) |
| **Showcase externo** | **Única fuente** de documentación de componentes (anatomía, variantes, estados, a11y, ejemplos) | — |
| **Repo `docs/design-system/`** | Foundations: color, tipografía, iconos, governance, gates | Fichas por componente (viven en showcase) |
| **Notion DS** | Resumen, enlaces, handoff para humanos | Duplicar matrices del showcase |

## Pipeline

```text
Figma (variables · Bridge) → variables.json → Compose
              ↓
        Showcase externo (documentación componentes)
```

## Consecuencias

- Nuevos componentes: publicar en Figma **y** ficha en showcase antes de marcar Ready.
- Gate `visualRegression`: showcase ↔ emulador (no Figma ↔ emulador para doc).
- Agentes: no generar láminas ni páginas de documentación en Figma.

## Showcase

Política: `docs/design-system/showcase.md` · sitio: `ds-showcase/` (Vite → `dist/`).
