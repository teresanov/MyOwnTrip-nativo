# Componentes — MyOwnTrip Nativo

ADR: [001-m3-native-ds.md](../decisions/001-m3-native-ds.md), [002-brand-editorial-m3.md](../decisions/002-brand-editorial-m3.md).

## Principio

Empezar **M3 puro**: `androidx.compose.material3.*` con el tema MTB.

## Cuándo crear `MyOwnTrip*`

Solo si M3 no ofrece el default o variante de producto necesaria **y** queda documentado aquí o en un ADR.

| Criterio | Ejemplo válido | Ejemplo inválido |
|----------|----------------|------------------|
| Default de producto repetido en ≥3 pantallas | `MyOwnTripCategoryChip` con icono bespoke + rol M3 | Copiar `Button` del repo archivo |
| Composición no cubierta por M3 | Wallet entry card con layout editorial fijo | Renombrar `Button` a `MyOwnTripButton` sin cambio |
| A11y o JTBD específico | Confirmación H7 con patrón fijo | Wrapper que oculta `colorScheme` |

## API de wrappers

- Delegar en componente M3 subyacente.
- Exponer parámetros alineados a M3 (`Modifier`, `enabled`, `colors` del tema).
- **No** reintroducir ejes del DS archivado (`MyOwnTripButtonVariant`, semánticos propios).

## Figma

Base: clon del **Material 3 Design Kit** tematizado con MTB (state layers del kit, no fills por token de estado).

Subset MVP (prioridad):

| P0 | Button, TextField, FilterChip |
| P1 | IconButton, ListItem, Card |
| P2 | Resto según pantalla |

## Portar del repo archivo

| Portar | No portar |
|--------|-----------|
| JTBD, flows, políticas H7/H8 | Tokens `semantic/color/*` |
| Glifos de categorías | `MyOwnTripButton`, `SemanticColors` |
| Focus ring como capa (futuro) | Matrices Pencil / ARC |
