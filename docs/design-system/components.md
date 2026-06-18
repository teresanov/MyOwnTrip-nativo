# Componentes — MyOwnTrip Nativo

ADR: [001-m3-native-ds.md](../decisions/001-m3-native-ds.md), [002-brand-editorial-m3.md](../decisions/002-brand-editorial-m3.md), [004-button-shape-morph.md](../decisions/004-button-shape-morph.md).

## Principio

Empezar **M3 puro**: `androidx.compose.material3.*` con el tema MTB.

## Shape en botones (ADR 004)

| | |
|---|---|
| Reposo | `Corner/None` → **0dp** |
| Hover / focus / pressed / selected | `Corner/Large-increased` → **20dp** (morph ~520ms) |
| API | `rememberMOTButtonShape()` — ver [shape.md](shape.md) |
| Figma | Button Square: bindear reposo a `Corner/None`; KEEP Round en toggle/segmented como destino morph |

Cards **12dp** y chips **8dp** fijos; FAB circular (excepción).

**Cards — layouts** (Figma `52346:27573`): `Text only`, `Media only`, `Media & text`, `Slot` — ver [figma-prune-inventory.md](figma-prune-inventory.md) § Cards.

**TripHeroCard** — [61199:7862](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61199-7862): portada 280dp + CTA tonal «Ver detalles»; showcase [`trip-hero-card.html`](../ds-showcase/components/trip-hero-card.html). Eyebrow = instancia **Eyebrow label** (no chip).

**Eyebrow label** — [61202:16834](https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=61202-16834): etiqueta informativa no interactiva; showcase [`eyebrow-label.html`](../ds-showcase/components/eyebrow-label.html). Chips = filtros/acciones; eyebrow = contexto sobre media o surface.

**Home · menú filtros** — patrón documentado en [`patterns/home-filter-menu.md`](patterns/home-filter-menu.md): Search bar + `Menu Groups=2` al pulsar `tune`; ítem seleccionado con **`check`** (no chevron).

## Cuándo crear `MyOwnTrip*`

Solo si M3 no ofrece el default o variante de producto necesaria **y** queda documentado en el **showcase** (o ADR).

| Criterio | Ejemplo válido | Ejemplo inválido |
|----------|----------------|------------------|
| Default de producto repetido en ≥3 pantallas | `MyOwnTripCategoryChip` con icono bespoke + rol M3 | Copiar `Button` del repo archivo |
| Composición no cubierta por M3 | Wallet entry card con layout editorial fijo | Renombrar `Button` a `MyOwnTripButton` sin cambio |
| A11y o JTBD específico | Confirmación H7 con patrón fijo | Wrapper que oculta `colorScheme` |

## API de wrappers

- Delegar en componente M3 subyacente.
- Exponer parámetros alineados a M3 (`Modifier`, `enabled`, `colors` del tema).
- **No** reintroducir ejes del DS archivado (`MyOwnTripButtonVariant`, semánticos propios).

## Figma (solo librería)

Base: **Material 3 Design Kit** tematizado con MTB. Publicar component sets y variables; **no** crear documentación en el archivo Figma.

**Librería Figma (poda / KEEP):** [figma-prune-inventory.md](figma-prune-inventory.md) · informe [audits/figma-prune-report-2026-06-08.md](audits/figma-prune-report-2026-06-08.md).

Subset MVP (prioridad en librería):

| P0 | Button, TextField, FilterChip |
| P1 | IconButton, ListItem, Card |
| P2 | Resto según pantalla |

## Showcase (documentación)

Toda ficha de componente vive en el **showcase externo** — ver [showcase.md](showcase.md). Incluye variantes, estados, a11y y ejemplos Compose.

## Portar del repo archivo

| Portar | No portar |
|--------|-----------|
| JTBD, flows, políticas H7/H8 | Tokens `semantic/color/*` |
| Glifos de categorías | `MyOwnTripButton`, `SemanticColors` |
| Focus ring como capa (futuro) | Matrices Pencil / ARC |
