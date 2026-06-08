# ADR 001 — Design System M3 nativo

**Estado:** Aceptado  
**Fecha:** 2026-06-08

## Contexto
El repo `MyOwnTrip` construyó un DS custom (semánticos propios, Pencil, ARC) incompatible con M3 estructural estricto. Este repo (`MyOwnTrip_nativo`) es el producto activo.

## Decisión
1. **Roles M3** como única capa de color en UI (`MaterialTheme.colorScheme`).
2. **Estados** = state layers M3 en runtime; **sin** tokens `*/hover`, `*/pressed`, `*/disabled`.
3. **Componentes** = `androidx.compose.material3.*` directos en MVP.
4. **Secondary buttons** = tonal (`secondaryContainer` + `onSecondaryContainer`).
5. **Surfaces:** `surfaceContainerLow` / `surfaceContainerHigh` para tiers secundarios.
6. **Bordes:** solo `outline` / `outlineVariant`; sin `border/strong`.
7. **Texto terciario:** eliminado; usar `onSurfaceVariant`.
8. **MTB scheme:** Tonal Spot · seeds primary `#3D63D1`, secondary `#219A60`.
9. **Custom colors:** `success`, `warning`, `info` vía `LocalExtendedColors`; focus ring en iteración futura.

## Consecuencias
- Figma nuevo: **MyOwnTrip · Design System (M3 Native)** — sembrar con MTB (tarea de diseño en paralelo).
- Repo `MyOwnTrip` congelado como archivo.
- Sin Pencil en pipeline.

## Referencia histórica
Mapa de equivalencias: `../MyOwnTrip/docs/design-system/migrations/2026-06-05_semantic-to-m3-role-map.md`
