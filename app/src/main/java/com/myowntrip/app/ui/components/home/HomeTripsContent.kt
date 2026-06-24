package com.myowntrip.app.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myowntrip.app.domain.model.Trip
import com.myowntrip.app.ui.components.MotCardStyle
import com.myowntrip.app.ui.components.MotStackedCard
import com.myowntrip.app.ui.components.HomeOnlyPastPlanStackedCard
import com.myowntrip.app.ui.components.PreviewCityCovers
import com.myowntrip.app.ui.components.TripHeroCard
import com.myowntrip.app.ui.components.TripListCard
import com.myowntrip.app.ui.components.home.SwipeableTripListRow
import com.myowntrip.app.ui.features.trips.TripFilterPhase
import com.myowntrip.app.ui.features.trips.TripSortOrder
import com.myowntrip.app.ui.features.trips.previewHomeTrips
import com.myowntrip.app.ui.features.trips.previewHomeTripsOnlyPast
import com.myowntrip.app.ui.features.trips.sortTripsForHome
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MyOwnTripTheme
import java.time.LocalDate

/** Presentación del menú filtros — dropdown en barra vs overlay cap 3 (`228:8161`). */
enum class HomeFilterMenuPresentation {
  Dropdown,
  Overlay,
}

data class HomeTripsContentState(
  val featuredTrip: Trip?,
  val otherTrips: List<Trip>,
  val visibleTripCount: Int,
  val totalTripCount: Int,
  val searchQuery: String,
  val filterPhase: TripFilterPhase,
  val sortOrder: TripSortOrder,
  val filterMenuExpanded: Boolean,
  val today: LocalDate,
  val userFirstName: String? = null,
  val searchPlaceholder: String = "Buscar viajes",
  val greetingOverride: String? = null,
  val usePreviewCityCovers: Boolean = false,
  /** Cap 1b (`313:501`): viajes en BD pero ninguno en curso/próximo. */
  val onlyPastMode: Boolean = false,
)

fun LazyListScope.homeTripsListItems(
  state: HomeTripsContentState,
  onSearchQueryChange: (String) -> Unit,
  onFilterMenuExpandedChange: (Boolean) -> Unit,
  onFilterPhaseChange: (TripFilterPhase) -> Unit,
  onSortOrderChange: (TripSortOrder) -> Unit,
  onTripClick: (String) -> Unit,
  onArchiveTrip: (String) -> Unit = {},
  onUnarchiveTrip: (String) -> Unit = {},
  onDeleteTripRequest: (String) -> Unit = {},
  filterMenuFooter: @Composable (dismissMenu: () -> Unit) -> Unit = {},
  onCreateTrip: (() -> Unit)? = null,
  filterMenuPresentation: HomeFilterMenuPresentation = HomeFilterMenuPresentation.Dropdown,
) {
  val horizontal = Modifier.padding(horizontal = MOTSpacing.screenHorizontal)

  item {
    if (state.onlyPastMode) {
      HomeOnlyPastHero(
        userFirstName = state.userFirstName,
        greetingOverride = state.greetingOverride,
        modifier = horizontal,
      )
    } else {
      HomeHeroHeader(
        featuredTrip = state.featuredTrip,
        tripCount = state.visibleTripCount,
        totalTripCount = state.totalTripCount,
        filterPhase = state.filterPhase,
        today = state.today,
        userFirstName = state.userFirstName,
        searchQuery = state.searchQuery,
        greetingOverride = state.greetingOverride,
        modifier = horizontal,
      )
    }
  }
  item {
    HomeSearchBar(
      query = state.searchQuery,
      onQueryChange = onSearchQueryChange,
      placeholder = state.searchPlaceholder,
      filterMenuExpanded = state.filterMenuExpanded,
      onFilterMenuExpandedChange = onFilterMenuExpandedChange,
      filterPhase = state.filterPhase,
      onFilterPhaseChange = onFilterPhaseChange,
      sortOrder = state.sortOrder,
      onSortOrderChange = onSortOrderChange,
      filterMenuPresentation = filterMenuPresentation,
      filterMenuFooter = filterMenuFooter,
      modifier = horizontal,
    )
  }
  if (state.onlyPastMode && onCreateTrip != null) {
    item {
      HomeOnlyPastPlanStackedCard(modifier = horizontal)
    }
    item {
      MOTButton(
        onClick = onCreateTrip,
        modifier = horizontal.fillMaxWidth(),
      ) {
        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(MOTSpacing.componentSm))
        Text("Crear viaje")
      }
    }
  }
  if (!state.onlyPastMode && state.featuredTrip != null) {
    item {
      TripHeroCard(
        trip = state.featuredTrip,
        today = state.today,
        previewCoverRes = previewCoverFor(state, state.featuredTrip.destination),
        onClick = { onTripClick(state.featuredTrip.id) },
        modifier = horizontal,
      )
    }
  } else if (state.visibleTripCount == 0 && state.totalTripCount > 0) {
    item {
      MotStackedCard(
        style = MotCardStyle.Outlined,
        headerText = "Ningún viaje coincide",
        subheadText = "Prueba otro término o cambia los filtros.",
        modifier = horizontal,
      )
    }
  }
  if (state.otherTrips.isNotEmpty()) {
    item {
      Text(
        text = when {
          state.filterPhase == TripFilterPhase.Archived -> "Archivados"
          state.onlyPastMode -> "Viajes anteriores"
          else -> "Más viajes"
        },
        style = MaterialTheme.typography.titleLarge,
        modifier = horizontal.padding(vertical = MOTSpacing.componentXs),
      )
    }
    items(state.otherTrips, key = { it.id }) { trip ->
      SwipeableTripListRow(
        trip = trip,
        today = state.today,
        showArchivedActions = state.filterPhase == TripFilterPhase.Archived,
        previewCoverRes = previewCoverFor(state, trip.destination),
        onClick = { onTripClick(trip.id) },
        onArchive = { onArchiveTrip(trip.id) },
        onUnarchive = { onUnarchiveTrip(trip.id) },
        onDeleteRequest = { onDeleteTripRequest(trip.id) },
        modifier = horizontal,
      )
    }
  }
}

private fun previewCoverFor(state: HomeTripsContentState, destination: String): Int? =
  if (state.usePreviewCityCovers) PreviewCityCovers.coverResForCity(destination) else null

/**
 * Home con viajes — caps 2/3 · Figma `205:1018` / `228:8161`.
 * Con [filterMenuPresentation] Overlay muestra el panel 328dp centrado bajo la search bar.
 */
@Composable
fun HomeTripsScreen(
  state: HomeTripsContentState,
  onSearchQueryChange: (String) -> Unit,
  onFilterMenuExpandedChange: (Boolean) -> Unit,
  onFilterPhaseChange: (TripFilterPhase) -> Unit,
  onSortOrderChange: (TripSortOrder) -> Unit,
  onTripClick: (String) -> Unit,
  onArchiveTrip: (String) -> Unit = {},
  onUnarchiveTrip: (String) -> Unit = {},
  onDeleteTripRequest: (String) -> Unit = {},
  filterMenuFooter: @Composable (dismissMenu: () -> Unit) -> Unit = {},
  onCreateTrip: (() -> Unit)? = null,
  filterMenuPresentation: HomeFilterMenuPresentation = HomeFilterMenuPresentation.Dropdown,
  modifier: Modifier = Modifier,
  contentPadding: PaddingValues = PaddingValues(bottom = MOTSpacing.screenContentBottomWithFab),
) {
  val showOverlay = filterMenuPresentation == HomeFilterMenuPresentation.Overlay &&
    state.filterMenuExpanded

  Box(modifier = modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = contentPadding,
      verticalArrangement = Arrangement.spacedBy(MOTSpacing.layoutMd),
    ) {
      homeTripsListItems(
        state = state,
        onSearchQueryChange = onSearchQueryChange,
        onFilterMenuExpandedChange = onFilterMenuExpandedChange,
        onFilterPhaseChange = onFilterPhaseChange,
        onSortOrderChange = onSortOrderChange,
        onTripClick = onTripClick,
        onArchiveTrip = onArchiveTrip,
        onUnarchiveTrip = onUnarchiveTrip,
        onDeleteTripRequest = onDeleteTripRequest,
        filterMenuPresentation = filterMenuPresentation,
        filterMenuFooter = filterMenuFooter,
        onCreateTrip = onCreateTrip,
      )
    }
    HomeFilterMenuOverlay(
      visible = showOverlay,
      filterPhase = state.filterPhase,
      onFilterPhaseChange = onFilterPhaseChange,
      sortOrder = state.sortOrder,
      onSortOrderChange = onSortOrderChange,
      onDismiss = { onFilterMenuExpandedChange(false) },
      filterMenuFooter = filterMenuFooter,
    )
  }
}

@Preview(name = "Home cap 1b — solo pasados (313:501)", showBackground = true, widthDp = 360, heightDp = 900)
@Composable
fun HomeOnlyPastPreview() {
  val today = LocalDate.of(2026, 6, 17)
  val sorted = sortTripsForHome(previewHomeTripsOnlyPast(), today)
  var searchQuery by remember { mutableStateOf("") }
  var filterMenuExpanded by remember { mutableStateOf(false) }
  var filterPhase by remember { mutableStateOf(TripFilterPhase.All) }
  var sortOrder by remember { mutableStateOf(TripSortOrder.DateUpcoming) }
  MyOwnTripTheme {
    HomeTripsScreen(
      state = HomeTripsContentState(
        featuredTrip = null,
        otherTrips = sorted,
        visibleTripCount = sorted.size,
        totalTripCount = sorted.size,
        searchQuery = searchQuery,
        filterPhase = filterPhase,
        sortOrder = sortOrder,
        filterMenuExpanded = filterMenuExpanded,
        today = today,
        userFirstName = "Raquel",
        searchPlaceholder = "Buscar viajes",
        greetingOverride = "Buenas tardes",
        usePreviewCityCovers = true,
        onlyPastMode = true,
      ),
      onSearchQueryChange = { searchQuery = it },
      onFilterMenuExpandedChange = { filterMenuExpanded = it },
      onFilterPhaseChange = { filterPhase = it },
      onSortOrderChange = { sortOrder = it },
      onTripClick = {},
      onCreateTrip = {},
    )
  }
}

@Preview(name = "Home cap 2 — con viajes (205:1018)", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun TripListWithTripsPreview() {
  val today = LocalDate.of(2026, 6, 17)
  val sorted = sortTripsForHome(previewHomeTrips(), today)
  var searchQuery by remember { mutableStateOf("") }
  var filterMenuExpanded by remember { mutableStateOf(false) }
  var filterPhase by remember { mutableStateOf(TripFilterPhase.All) }
  var sortOrder by remember { mutableStateOf(TripSortOrder.DateUpcoming) }
  MyOwnTripTheme {
    HomeTripsScreen(
      state = HomeTripsContentState(
        featuredTrip = sorted.first(),
        otherTrips = sorted.drop(1),
        visibleTripCount = sorted.size,
        totalTripCount = sorted.size,
        searchQuery = searchQuery,
        filterPhase = filterPhase,
        sortOrder = sortOrder,
        filterMenuExpanded = filterMenuExpanded,
        today = today,
        userFirstName = "Raquel",
        searchPlaceholder = "Barcelona",
        greetingOverride = "Buenas tardes",
        usePreviewCityCovers = true,
      ),
      onSearchQueryChange = { searchQuery = it },
      onFilterMenuExpandedChange = { filterMenuExpanded = it },
      onFilterPhaseChange = { filterPhase = it },
      onSortOrderChange = { sortOrder = it },
      onTripClick = {},
    )
  }
}

@Preview(name = "Home cap 3 — búsqueda + menú (228:8161)", showBackground = true, widthDp = 360, heightDp = 1100)
@Composable
fun TripListFilterMenuPreview() {
  val today = LocalDate.of(2026, 6, 17)
  val sorted = sortTripsForHome(previewHomeTrips(), today)
  var searchQuery by remember { mutableStateOf("Barcelona") }
  var filterMenuExpanded by remember { mutableStateOf(true) }
  var filterPhase by remember { mutableStateOf(TripFilterPhase.All) }
  var sortOrder by remember { mutableStateOf(TripSortOrder.DateUpcoming) }
  MyOwnTripTheme {
    HomeTripsScreen(
      state = HomeTripsContentState(
        featuredTrip = sorted.first(),
        otherTrips = sorted.drop(1),
        visibleTripCount = sorted.size,
        totalTripCount = sorted.size,
        searchQuery = searchQuery,
        filterPhase = filterPhase,
        sortOrder = sortOrder,
        filterMenuExpanded = filterMenuExpanded,
        today = today,
        userFirstName = "Raquel",
        searchPlaceholder = "Barcelona",
        greetingOverride = "Buenas tardes",
        usePreviewCityCovers = true,
      ),
      onSearchQueryChange = { searchQuery = it },
      onFilterMenuExpandedChange = { filterMenuExpanded = it },
      onFilterPhaseChange = { filterPhase = it },
      onSortOrderChange = { sortOrder = it },
      onTripClick = {},
      filterMenuPresentation = HomeFilterMenuPresentation.Overlay,
    )
  }
}
