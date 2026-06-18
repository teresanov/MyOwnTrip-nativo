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
| Posición | Centrado horizontal · `marginTop = 192dp` desde body (`228:8306`) |
| Comportamiento | **Overlay** sobre TripHeroCard (no empuja el scroll) |
| cap 2 | **Sin menú** — estado reposo con viajes |

Script: `scripts/figma-design-file-home-cap3-search-filters.js` (clona cap 2 → cap 3; **no modifica cap 2**).

## Selección visual (M3 — menú de selección, sin submenú)

Patrón canónico para filtros de primer nivel: [Menus M3](https://m3.material.io/components/menus/guidelines) + `DropdownMenuItem.selectedLeadingIcon` en [Compose](https://developer.android.com/reference/kotlin/androidx/compose/material3/DropdownMenuItem.composable).

| Señal | Uso |
|-------|-----|
| **Leading slot siempre visible** | Caja reservada 24dp en todas las filas — alinea labels aunque no haya check |
| **Leading `check`** | Solo en ítem `Selected=True` — color `primary` — **no** en trailing |
| **Trailing oculto** | `Show trailing element=False` — **sin** `chevron_right` (no hay submenú) |
| **Contenedor seleccionado (Expressive)** | `secondaryContainer` + `onSecondaryContainer` · shape 12dp · borde `outline` 1dp (1.4.11) |
| **Fondo menú** | `surfaceContainerLow` |

**No usar:** `radio_button_checked` (semántica de formulario) · `chevron_right` (navegación a subnivel).

Iconografía: Material Symbols **Sharp** w300 · check seleccionado → `primary`.

### Variables y text styles (gate `figmaAliasChainValid`)

| Nodo | Text style | Variable fill |
|------|------------|---------------|
| Section label («Mostrar», «Ordenar») | `M3/label/large` | `Schemes/On Surface Variant` |
| Ítem label no seleccionado | `M3/label/large` | `Schemes/On Surface` |
| Ítem label seleccionado | `M3/label/large` | `Schemes/On Secondary Container` |
| Fondo menú / listas | — | `Schemes/Surface Container Low` |
| State layer seleccionado | — | `Schemes/Secondary Container` + stroke `Schemes/Outline` |
| Check leading | — | `Schemes/Primary` |

**No dejar hex sueltos** en instancias del design-file — rompen propagación. Overrides deben vivir en **Menu-item/Standard** (DS) en variantes `Selected=True/False`.

Script instancia cap 3: `scripts/figma-design-file-filter-menu-bind-vars.js`  
Script selección + expressive: `scripts/figma-menu-item-selection-filter.js`

Compose:

```kotlin
DropdownMenuItem(
    selected = isSelected,
    onClick = { … },
    text = { Text(label) },
    colors = MenuDefaults.selectableItemColors(
        selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        selectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
        selectedLeadingIconColor = MaterialTheme.colorScheme.primary,
    ),
    shapes = MenuDefaults.itemShape(index, count),
    selectedLeadingIcon = {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            modifier = Modifier.size(MenuDefaults.LeadingIconSize),
        )
    },
    // trailingIcon = null — sin chevron
)
```

### Deuda DS Figma

El `Menu-item` publicado enlaza `Selected=True` → trailing `radio_button_checked`. **Corregir** a leading `check` + trailing oculto.

Script: `scripts/figma-menu-item-selection-filter.js` (DS + design-file instancia `228:8306`).

## Accesibilidad

- TalkBack: anunciar sección («Mostrar», «Ordenar») y estado «seleccionado» en el ítem activo.
- Contraste selección: texto/icono ≥ 4.5:1 / 3:1; borde `outline` del chip seleccionado ≥ 3:1 vs `surfaceContainerLow` (la paleta cálida no alcanza 3:1 solo con fill — el borde + check cumplen 1.4.1 y 1.4.11).
- Cerrar menú: tap fuera, back, o al elegir ítem (MVP).

## Compose

`HomeFilterMenuPanel` + `HomeFilterMenuOverlay` en `ui/components/home/HomeSearchAndFilter.kt`:

- Ancho `328.dp` · esquina `16.dp` · `surfaceContainerLow` · elevación `3.dp`
- Ítems custom (no `DropdownMenuItem`): seleccionado = `secondaryContainer` + borde `outline` + check `20.dp`
- Overlay: `HomeFilterMenuSpec.OverlayTop` = `192.dp` · `TopCenter`
- `TripListScreen`: `HomeFilterMenuPresentation.Overlay` al abrir menú

## Referencias

- [`figma-prune-inventory.md`](../figma-prune-inventory.md) § Menu · § Search · § Shell Home
- [`components.md`](../components.md)
- [`iconography.md`](../iconography.md)
