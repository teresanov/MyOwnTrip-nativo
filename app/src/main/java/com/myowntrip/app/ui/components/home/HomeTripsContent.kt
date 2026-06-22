package com.myowntrip.app.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myowntrip.app.domain.model.Trip
import com.myowntrip.app.ui.components.MotCardStyle
import com.myowntrip.app.ui.components.MotStackedCard
import com.myowntrip.app.ui.components.PreviewCityCovers
import com.myowntrip.app.ui.components.TripHeroCard
import com.myowntrip.app.ui.components.TripListCard
import com.myowntrip.app.ui.features.trips.TripFilterPhase
import com.myowntrip.app.ui.features.trips.TripSortOrder
import com.myowntrip.app.ui.features.trips.previewHomeTrips
import com.myowntrip.app.ui.features.trips.sortTripsForHome
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
)

fun LazyListScope.homeTripsListItems(
  state: HomeTripsContentState,
  onSearchQueryChange: (String) -> Unit,
  onFilterMenuExpandedChange: (Boolean) -> Unit,
  onFilterPhaseChange: (TripFilterPhase) -> Unit,
  onSortOrderChange: (TripSortOrder) -> Unit,
  onTripClick: (String) -> Unit,
  onClearAllData: (() -> Unit)? = null,
  filterMenuPresentation: HomeFilterMenuPresentation = HomeFilterMenuPresentation.Dropdown,
) {
  val horizontal = Modifier.padding(horizontal = MOTSpacing.screenHorizontal)

  item {
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
      onClearAllData = onClearAllData,
      modifier = horizontal,
    )
  }
  if (state.featuredTrip != null) {
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
        text = "Más viajes",
        style = MaterialTheme.typography.titleLarge,
        modifier = horizontal.padding(vertical = MOTSpacing.componentXs),
      )
    }
    items(state.otherTrips, key = { it.id }) { trip ->
      TripListCard(
        trip = trip,
        today = state.today,
        previewCoverRes = previewCoverFor(state, trip.destination),
        onClick = { onTripClick(trip.id) },
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
  onClearAllData: (() -> Unit)? = null,
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
        filterMenuPresentation = filterMenuPresentation,
        onClearAllData = onClearAllData,
      )
    }
    HomeFilterMenuOverlay(
      visible = showOverlay,
      filterPhase = state.filterPhase,
      onFilterPhaseChange = onFilterPhaseChange,
      sortOrder = state.sortOrder,
      onSortOrderChange = onSortOrderChange,
      onDismiss = { onFilterMenuExpandedChange(false) },
      onClearAllData = onClearAllData?.let { clear ->
        {
          onFilterMenuExpandedChange(false)
          clear()
        }
      },
    )
  }
}

@Preview(name = "Home cap 2 — con viajes (205:1018)", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun HomeCap2Preview() {
  val today = LocalDate.of(2026, 6, 17)
  val sorted = sortTripsForHome(previewHomeTrips(), today)
  MyOwnTripTheme {
    HomeTripsScreen(
      state = HomeTripsContentState(
        featuredTrip = sorted.first(),
        otherTrips = sorted.drop(1),
        visibleTripCount = sorted.size,
        totalTripCount = sorted.size,
        searchQuery = "",
        filterPhase = TripFilterPhase.All,
        sortOrder = TripSortOrder.DateUpcoming,
        filterMenuExpanded = false,
        today = today,
        userFirstName = "Raquel",
        searchPlaceholder = "Barcelona",
        greetingOverride = "Buenas tardes",
        usePreviewCityCovers = true,
      ),
      onSearchQueryChange = {},
      onFilterMenuExpandedChange = {},
      onFilterPhaseChange = {},
      onSortOrderChange = {},
      onTripClick = {},
    )
  }
}

@Preview(name = "Home cap 3 — búsqueda + menú (228:8161)", showBackground = true, widthDp = 360, heightDp = 1100)
@Composable
fun HomeCap3ClonePreview() {
  val today = LocalDate.of(2026, 6, 17)
  val sorted = sortTripsForHome(previewHomeTrips(), today)
  MyOwnTripTheme {
    HomeTripsScreen(
      state = HomeTripsContentState(
        featuredTrip = sorted.first(),
        otherTrips = sorted.drop(1),
        visibleTripCount = sorted.size,
        totalTripCount = sorted.size,
        searchQuery = "Barcelona",
        filterPhase = TripFilterPhase.All,
        sortOrder = TripSortOrder.DateUpcoming,
        filterMenuExpanded = true,
        today = today,
        userFirstName = "Raquel",
        searchPlaceholder = "Barcelona",
        greetingOverride = "Buenas tardes",
        usePreviewCityCovers = true,
      ),
      onSearchQueryChange = {},
      onFilterMenuExpandedChange = {},
      onFilterPhaseChange = {},
      onSortOrderChange = {},
      onTripClick = {},
      filterMenuPresentation = HomeFilterMenuPresentation.Overlay,
    )
  }
}
