package com.myowntrip.app.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.myowntrip.app.ui.features.trips.TripFilterPhase
import com.myowntrip.app.ui.features.trips.TripSortOrder
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MyOwnTripTheme

/** Medidas Figma `228:8306` · cap 3 Home. */
object HomeFilterMenuSpec {
  val Width = 328.dp
  /** Desde el borde superior del body (debajo de status) en design-file. */
  val OverlayTop = 192.dp
  val CornerRadius = 16.dp
  val ListCornerRadius = 8.dp
  val ItemHeight = 44.dp
  val ItemCorner = 12.dp
  val LeadingIconSize = 20.dp
  val Elevation = 3.dp
}

/** Search bar + tune — design-file caps 2/3 · patrón home-filter-menu. */
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
  modifier: Modifier = Modifier,
  filterMenuPresentation: HomeFilterMenuPresentation = HomeFilterMenuPresentation.Dropdown,
) {
  val pressed = query.isNotEmpty()
  val searchShape = RoundedCornerShape(28.dp)
  val showInlineMenu = filterMenuPresentation == HomeFilterMenuPresentation.Dropdown

  Box(modifier = modifier.fillMaxWidth()) {
    if (pressed) {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = searchShape,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
      ) {
        HomeSearchBarRow(
          query = query,
          onQueryChange = onQueryChange,
          placeholder = placeholder,
          onFilterMenuExpandedChange = onFilterMenuExpandedChange,
          showLeadingSearchIcon = false,
        )
      }
    } else {
      OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        HomeSearchBarRow(
          query = query,
          onQueryChange = onQueryChange,
          placeholder = placeholder,
          onFilterMenuExpandedChange = onFilterMenuExpandedChange,
          showLeadingSearchIcon = true,
        )
      }
    }
    if (showInlineMenu && filterMenuExpanded) {
      HomeFilterDropdownMenu(
        expanded = filterMenuExpanded,
        onDismissRequest = { onFilterMenuExpandedChange(false) },
        filterPhase = filterPhase,
        onFilterPhaseChange = onFilterPhaseChange,
        sortOrder = sortOrder,
        onSortOrderChange = onSortOrderChange,
        modifier = Modifier.align(Alignment.TopEnd),
      )
    }
  }
}

@Composable
private fun HomeSearchBarRow(
  query: String,
  onQueryChange: (String) -> Unit,
  placeholder: String,
  onFilterMenuExpandedChange: (Boolean) -> Unit,
  showLeadingSearchIcon: Boolean,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .heightIn(min = 56.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    if (showLeadingSearchIcon) {
      Icon(
        imageVector = Icons.Default.Search,
        contentDescription = null,
        modifier = Modifier
          .padding(start = MOTSpacing.layoutMd)
          .size(24.dp),
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
    BasicTextField(
      value = query,
      onValueChange = onQueryChange,
      modifier = Modifier
        .weight(1f)
        .padding(
          start = if (showLeadingSearchIcon) MOTSpacing.componentSm else 20.dp,
          end = MOTSpacing.componentSm,
          top = 14.dp,
          bottom = 14.dp,
        ),
      textStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onSurface,
      ),
      singleLine = true,
      cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
      decorationBox = { innerTextField ->
        Box(contentAlignment = Alignment.CenterStart) {
          if (query.isEmpty()) {
            Text(
              text = placeholder,
              style = MaterialTheme.typography.bodyLarge,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
          }
          innerTextField()
        }
      },
    )
    if (query.isNotEmpty()) {
      IconButton(
        onClick = { onQueryChange("") },
        modifier = Modifier.semantics { contentDescription = "Borrar búsqueda" },
      ) {
        Icon(
          imageVector = Icons.Default.Close,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
    IconButton(
      onClick = { onFilterMenuExpandedChange(true) },
      modifier = Modifier.semantics { contentDescription = "Filtros y ordenación" },
    ) {
      Icon(
        imageVector = Icons.Outlined.Tune,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFilterDropdownMenu(
  expanded: Boolean,
  onDismissRequest: () -> Unit,
  filterPhase: TripFilterPhase,
  onFilterPhaseChange: (TripFilterPhase) -> Unit,
  sortOrder: TripSortOrder,
  onSortOrderChange: (TripSortOrder) -> Unit,
  modifier: Modifier = Modifier,
) {
  DropdownMenu(
    expanded = expanded,
    onDismissRequest = onDismissRequest,
    modifier = modifier,
    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
    shadowElevation = HomeFilterMenuSpec.Elevation,
    shape = RoundedCornerShape(HomeFilterMenuSpec.CornerRadius),
  ) {
    HomeFilterMenuPanelContent(
      filterPhase = filterPhase,
      onFilterPhaseChange = onFilterPhaseChange,
      sortOrder = sortOrder,
      onSortOrderChange = onSortOrderChange,
      modifier = Modifier.width(HomeFilterMenuSpec.Width),
    )
  }
}

/** Panel overlay 328dp — Figma `228:8306` · `filter_menu`. */
@Composable
fun HomeFilterMenuPanel(
  filterPhase: TripFilterPhase,
  onFilterPhaseChange: (TripFilterPhase) -> Unit,
  sortOrder: TripSortOrder,
  onSortOrderChange: (TripSortOrder) -> Unit,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier
      .width(HomeFilterMenuSpec.Width)
      .clip(RoundedCornerShape(HomeFilterMenuSpec.CornerRadius)),
    shape = RoundedCornerShape(HomeFilterMenuSpec.CornerRadius),
    color = MaterialTheme.colorScheme.surfaceContainerLow,
    shadowElevation = HomeFilterMenuSpec.Elevation,
    tonalElevation = 0.dp,
  ) {
    HomeFilterMenuPanelContent(
      filterPhase = filterPhase,
      onFilterPhaseChange = onFilterPhaseChange,
      sortOrder = sortOrder,
      onSortOrderChange = onSortOrderChange,
    )
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
  modifier: Modifier = Modifier,
  overlayTop: androidx.compose.ui.unit.Dp = HomeFilterMenuSpec.OverlayTop,
) {
  if (!visible) return

  Box(modifier = modifier.fillMaxSize()) {
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
      modifier = Modifier
        .align(Alignment.TopCenter)
        .padding(top = overlayTop)
        .zIndex(2f),
    )
  }
}

@Composable
internal fun HomeFilterMenuPanelContent(
  filterPhase: TripFilterPhase,
  onFilterPhaseChange: (TripFilterPhase) -> Unit,
  sortOrder: TripSortOrder,
  onSortOrderChange: (TripSortOrder) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier.padding(vertical = 2.dp),
    verticalArrangement = Arrangement.spacedBy(2.dp),
  ) {
    HomeFilterMenuList {
      HomeFilterMenuSectionLabel("Mostrar")
      HomeFilterMenuItemRow(
        label = "Todos los viajes",
        selected = filterPhase == TripFilterPhase.All,
        onClick = { onFilterPhaseChange(TripFilterPhase.All) },
      )
      HomeFilterMenuItemRow(
        label = "En curso",
        selected = filterPhase == TripFilterPhase.Current,
        onClick = { onFilterPhaseChange(TripFilterPhase.Current) },
      )
      HomeFilterMenuItemRow(
        label = "Próximos",
        selected = filterPhase == TripFilterPhase.Upcoming,
        onClick = { onFilterPhaseChange(TripFilterPhase.Upcoming) },
      )
      HomeFilterMenuItemRow(
        label = "Pasados",
        selected = filterPhase == TripFilterPhase.Past,
        onClick = { onFilterPhaseChange(TripFilterPhase.Past) },
      )
    }
    HomeFilterMenuList {
      HomeFilterMenuSectionLabel("Ordenar")
      HomeFilterMenuItemRow(
        label = "Fecha — próximo primero",
        selected = sortOrder == TripSortOrder.DateUpcoming,
        onClick = { onSortOrderChange(TripSortOrder.DateUpcoming) },
      )
      HomeFilterMenuItemRow(
        label = "Nombre A—Z",
        selected = sortOrder == TripSortOrder.NameAz,
        onClick = { onSortOrderChange(TripSortOrder.NameAz) },
      )
      HomeFilterMenuItemRow(
        label = "Destino A—Z",
        selected = sortOrder == TripSortOrder.DestinationAz,
        onClick = { onSortOrderChange(TripSortOrder.DestinationAz) },
      )
    }
  }
}

@Composable
private fun HomeFilterMenuList(content: @Composable () -> Unit) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(HomeFilterMenuSpec.ListCornerRadius)),
  ) {
    content()
  }
}

@Composable
private fun HomeFilterMenuSectionLabel(text: String) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 4.dp)
      .heightIn(min = 32.dp)
      .padding(start = 12.dp, end = 12.dp, top = 6.dp),
    contentAlignment = Alignment.CenterStart,
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.labelLarge,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
  }
}

@Composable
private fun HomeFilterMenuItemRow(
  label: String,
  selected: Boolean,
  onClick: () -> Unit,
) {
  val scheme = MaterialTheme.colorScheme
  val itemShape = RoundedCornerShape(HomeFilterMenuSpec.ItemCorner)

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 4.dp, vertical = 2.dp),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(HomeFilterMenuSpec.ItemHeight)
        .then(
          if (selected) {
            Modifier
              .background(scheme.secondaryContainer, itemShape)
              .border(1.dp, scheme.outline, itemShape)
          } else {
            Modifier
          },
        )
        .clickable(onClick = onClick)
        .padding(horizontal = 12.dp)
        .semantics(mergeDescendants = true) {
          if (selected) stateDescription = "Seleccionado"
        },
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      Box(
        modifier = Modifier.size(HomeFilterMenuSpec.LeadingIconSize),
        contentAlignment = Alignment.Center,
      ) {
        if (selected) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            modifier = Modifier.size(HomeFilterMenuSpec.LeadingIconSize),
            tint = scheme.primary,
          )
        }
      }
      Text(
        text = label,
        style = MaterialTheme.typography.labelLarge,
        color = if (selected) scheme.onSecondaryContainer else scheme.onSurface,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.weight(1f),
      )
    }
  }
}

@Preview(name = "Filter menu · Figma 228:8306", widthDp = 328)
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
