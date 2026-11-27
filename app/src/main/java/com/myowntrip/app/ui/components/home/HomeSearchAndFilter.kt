package com.myowntrip.app.ui.components.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.myowntrip.app.ui.components.MotMenuDropdown
import com.myowntrip.app.ui.components.MotMenuGroup
import com.myowntrip.app.ui.components.MotMenuItem
import com.myowntrip.app.ui.components.MotMenuPanel
import com.myowntrip.app.ui.components.MotMenuSectionLabel
import com.myowntrip.app.ui.components.MotSearchBar
import com.myowntrip.app.ui.features.trips.TripFilterPhase
import com.myowntrip.app.ui.features.trips.TripSortOrder
import com.myowntrip.app.ui.theme.MyOwnTripTheme

/*
 * Búsqueda y menú de filtros de Home. Las piezas visuales están en el design system
 * (MotSearchBar, MotMenuDropdown, MotMenuPanel, MotMenuItem); aquí solo van las opciones
 * de viajes (TripFilterPhase, TripSortOrder) y la colocación en la pantalla.
 */

object HomeFilterMenuSpec {
  /** Altura del menú en modo overlay, desde el borde superior del cuerpo de Home. */
  val OverlayTop = 192.dp
}

@Composable
fun HomeSearchBar(
  query: String,
  onQueryChange: (String) -> Unit,
  placeholder: String,
  filterMenuExpanded: Boolean,
  onFilterMenuExpandedChange: (Boolean) -> Unit,
  filterPhase: TripFilterPhase,
  onFilterPhaseChange: (TripFilterPhase) -> Unit,
  sortOrder: TripSortOrder,
  onSortOrderChange: (TripSortOrder) -> Unit,
  filterMenuFooter: @Composable (dismissMenu: () -> Unit) -> Unit = {},
  modifier: Modifier = Modifier,
  filterMenuPresentation: HomeFilterMenuPresentation = HomeFilterMenuPresentation.Dropdown,
) {
  val showInlineMenu = filterMenuPresentation == HomeFilterMenuPresentation.Dropdown
  MotSearchBar(
    query = query,
    onQueryChange = onQueryChange,
    placeholder = placeholder,
    onFilterClick = { onFilterMenuExpandedChange(true) },
    modifier = modifier,
    menu = {
      if (showInlineMenu && filterMenuExpanded) {
        HomeFilterDropdownMenu(
          expanded = filterMenuExpanded,
          onDismissRequest = { onFilterMenuExpandedChange(false) },
          filterPhase = filterPhase,
          onFilterPhaseChange = onFilterPhaseChange,
          sortOrder = sortOrder,
          onSortOrderChange = onSortOrderChange,
          filterMenuFooter = filterMenuFooter,
          modifier = Modifier.align(Alignment.TopEnd),
        )
      }
    },
  )
}

@Composable
fun HomeFilterDropdownMenu(
  expanded: Boolean,
  onDismissRequest: () -> Unit,
  filterPhase: TripFilterPhase,
  onFilterPhaseChange: (TripFilterPhase) -> Unit,
  sortOrder: TripSortOrder,
  onSortOrderChange: (TripSortOrder) -> Unit,
  filterMenuFooter: @Composable (dismissMenu: () -> Unit) -> Unit = {},
  modifier: Modifier = Modifier,
) {
  MotMenuDropdown(expanded = expanded, onDismissRequest = onDismissRequest, modifier = modifier) {
    HomeFilterMenuGroups(filterPhase, onFilterPhaseChange, sortOrder, onSortOrderChange)
    filterMenuFooter(onDismissRequest)
  }
}

@Composable
fun HomeFilterMenuPanel(
  filterPhase: TripFilterPhase,
  onFilterPhaseChange: (TripFilterPhase) -> Unit,
  sortOrder: TripSortOrder,
  onSortOrderChange: (TripSortOrder) -> Unit,
  dismissMenu: () -> Unit = {},
  filterMenuFooter: @Composable (dismissMenu: () -> Unit) -> Unit = {},
  modifier: Modifier = Modifier,
) {
  MotMenuPanel(modifier = modifier) {
    HomeFilterMenuGroups(filterPhase, onFilterPhaseChange, sortOrder, onSortOrderChange)
    filterMenuFooter(dismissMenu)
  }
}

@Composable
fun HomeFilterMenuOverlay(
  visible: Boolean,
  filterPhase: TripFilterPhase,
  onFilterPhaseChange: (TripFilterPhase) -> Unit,
  sortOrder: TripSortOrder,
  onSortOrderChange: (TripSortOrder) -> Unit,
  onDismiss: () -> Unit,
  filterMenuFooter: @Composable (dismissMenu: () -> Unit) -> Unit = {},
  modifier: Modifier = Modifier,
  overlayTop: Dp = HomeFilterMenuSpec.OverlayTop,
) {
  if (!visible) return

  Box(modifier = modifier.fillMaxSize().navigationBarsPadding()) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .zIndex(1f)
        .clickable(
          interactionSource = MutableInteractionSource(),
          indication = null,
          onClick = onDismiss,
        ),
    )
    HomeFilterMenuPanel(
      filterPhase = filterPhase,
      onFilterPhaseChange = onFilterPhaseChange,
      sortOrder = sortOrder,
      onSortOrderChange = onSortOrderChange,
      dismissMenu = onDismiss,
      filterMenuFooter = filterMenuFooter,
      modifier = Modifier
        .align(Alignment.TopCenter)
        .padding(top = overlayTop)
        .zIndex(2f),
    )
  }
}

/** Las opciones de viajes: qué viajes mostrar y en qué orden. */
@Composable
private fun HomeFilterMenuGroups(
  filterPhase: TripFilterPhase,
  onFilterPhaseChange: (TripFilterPhase) -> Unit,
  sortOrder: TripSortOrder,
  onSortOrderChange: (TripSortOrder) -> Unit,
) {
  MotMenuGroup {
    MotMenuSectionLabel("Mostrar")
    MotMenuItem(
      label = "Todos los viajes",
      selected = filterPhase == TripFilterPhase.All,
      onClick = { onFilterPhaseChange(TripFilterPhase.All) },
    )
    MotMenuItem(
      label = "En curso",
      selected = filterPhase == TripFilterPhase.Current,
      onClick = { onFilterPhaseChange(TripFilterPhase.Current) },
    )
    MotMenuItem(
      label = "Próximos",
      selected = filterPhase == TripFilterPhase.Upcoming,
      onClick = { onFilterPhaseChange(TripFilterPhase.Upcoming) },
    )
    MotMenuItem(
      label = "Pasados",
      selected = filterPhase == TripFilterPhase.Past,
      onClick = { onFilterPhaseChange(TripFilterPhase.Past) },
    )
    MotMenuItem(
      label = "Archivados",
      selected = filterPhase == TripFilterPhase.Archived,
      onClick = { onFilterPhaseChange(TripFilterPhase.Archived) },
    )
  }
  MotMenuGroup {
    MotMenuSectionLabel("Ordenar")
    MotMenuItem(
      label = "Fecha — próximo primero",
      selected = sortOrder == TripSortOrder.DateUpcoming,
      onClick = { onSortOrderChange(TripSortOrder.DateUpcoming) },
    )
    MotMenuItem(
      label = "Nombre A—Z",
      selected = sortOrder == TripSortOrder.NameAz,
      onClick = { onSortOrderChange(TripSortOrder.NameAz) },
    )
    MotMenuItem(
      label = "Destino A—Z",
      selected = sortOrder == TripSortOrder.DestinationAz,
      onClick = { onSortOrderChange(TripSortOrder.DestinationAz) },
    )
  }
}

@Preview(name = "Menú de filtros de Home", widthDp = 328)
@Composable
private fun HomeFilterMenuPanelPreview() {
  MyOwnTripTheme {
    HomeFilterMenuPanel(
      filterPhase = TripFilterPhase.All,
      onFilterPhaseChange = {},
      sortOrder = TripSortOrder.DateUpcoming,
      onSortOrderChange = {},
    )
  }
}
