# Iconografía — MyOwnTrip Nativo

ADR: [002-brand-editorial-m3.md](../decisions/002-brand-editorial-m3.md).

## UI funcional (obligatorio)

| Parámetro | Valor |
|-----------|-------|
| Set | **Material Symbols** (nativo M3) |
| Estilo | **Sharp** |
| Peso | **300** en Figma (Sharp); Compose: ligero (200) hasta exponer peso en runtime |
| Color | Roles `colorScheme` (`onSurface`, `primary`, `onSurfaceVariant`, …) |

- Usar `Icons` / `Icon` de Material 3 o vector drawable del set oficial.
- **No** crear ni mantener un set propio de iconos de UI (navegación, acciones, estados).

## Iconos propios (permitido)

| Tipo | Uso | Ubicación |
|------|-----|-----------|
| Logo | Marca app, splash | `res/drawable/` |
| Spot art / ilustración | Empty states, onboarding | `res/drawable/` |
| Categorías bespoke | Gastos, Wallet, tipos de entrada | `res/drawable/` — glifos de producto |

Los iconos propios no sustituyen acciones estándar (cerrar, atrás, más, editar).

## Accesibilidad

- Iconos informativos: `contentDescription` significativo.
- Decorativos: `contentDescription = null` o `Modifier.semantics { invisibleToUser() }`.
- Error / éxito / aviso: **icono + texto**; no depender solo del color del icono.

## Figma

Kit M3 Design Kit tematizado; catálogo **Material Symbols Sharp (w300)** en la sección Icons del DS. Sin biblioteca paralela de UI icons.
