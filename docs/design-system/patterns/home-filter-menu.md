# Patrón — Home · menú filtros y ordenación

JTBD: biblioteca de viajes (`trip_list` / `TripListScreen`).  
Design-file: [Shell — Home · flow](https://www.figma.com/design/Vf2tNMXyKAlJSV53A1v4Is/MyOwnTrip_design-file?node-id=205-813) · cap 3.

## Disparador

Pulsar el icono **`tune`** (2.º trailing) en **Search bar** del DS — no chips en fila ni bottom sheet en MVP.

La búsqueda filtra por **nombre y destino** del cuaderno (viaje), no por explorar ciudad.

## Componentes DS

| Pieza | Instancia / variante |
|-------|---------------------|
| Search bar | `State=Enabled` (reposo) · `State=Pressed` (query activa) · `Show avatar=False` · `Show 2nd trailing icon=True` (`tune`) |
| Menú | `Menu` · `Theme=Standard` · `Groups=2` · `Show section label=True` |
| Ítems | `Menu-item/Standard` · `Selected=True/False` |

## Contenido del menú

### Mostrar (selección única)

| Ítem | Valor producto |
|------|----------------|
| Todos los viajes ✓ | default |
| En curso | `TripPhase.Current` |
| Próximos | `TripPhase.Upcoming` |
| Pasados | `TripPhase.Past` |

### Ordenar (selección única)

| Ítem | Valor producto |
|------|----------------|
| Fecha — próximo primero ✓ | `sortTripsForHome` (default) |
| Nombre A–Z | por `trip.name` |
| Destino A–Z | por `trip.destination` |

Ocultar en instancia: `Menu-item 04`, `05`, `06` (placeholders del kit M3).

## Layout en design-file (cap 3)

| Propiedad | Valor |
|-----------|--------|
| Ancho panel | 328dp (ancho contenido con padding 16) |
| Posición | `ABSOLUTE` · `x=0` en Body · `y` = debajo de Search bar + 8dp |
| Comportamiento | **Overlay** sobre TripHeroCard (no empuja el scroll) |
| cap 2 | **Sin menú** — estado reposo con viajes |

Script: `scripts/figma-design-file-home-cap3-search-filters.js` (clona cap 2 → cap 3; **no modifica cap 2**).

## Selección visual (M3)

| Señal | Uso |
|-------|-----|
| Contenedor fila `Selected` | Fondo tertiary / shape seleccionada del Menu-item |
| Icono trailing **`check`** | Ítem activo — **no** `chevron_right` (submenú) ni `radio_button_checked` (semántica de formulario) |
| Sin leading icon | Ítems no seleccionados |

Iconografía: Material Symbols **Sharp** w300 · rol `onSurface` o `primary` según tema del ítem.

Compose: `DropdownMenuItem(selected = …, trailingIcon = { if (selected) Icon(Icons.Default.Check) })` o `selectedLeadingIcon` según alineación.

### Deuda DS Figma

El `Menu-item` publicado mapea `Selected=True` → trailing `radio_button_checked`. **Pendiente:** sustituir por instancia **`check`** en la variante Selected del component set (o documentar override solo en design-file hasta publicar).

## Accesibilidad

- TalkBack: anunciar sección («Mostrar», «Ordenar») y estado «seleccionado» en el ítem activo.
- Contraste selección: ≥ 3:1 entre fila seleccionada y no seleccionada (M3 menus).
- Cerrar menú: tap fuera, back, o al elegir ítem (MVP).

## Compose (pendiente)

`TripListScreen`: `DropdownMenu` anclado al `IconButton` `tune` de la barra de búsqueda; estado `filterPhase` + `sortOrder` en ViewModel.

## Referencias

- [`figma-prune-inventory.md`](../figma-prune-inventory.md) § Menu · § Search · § Shell Home
- [`components.md`](../components.md)
- [`iconography.md`](../iconography.md)
