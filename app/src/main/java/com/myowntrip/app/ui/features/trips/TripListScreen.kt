package com.myowntrip.app.ui.features.trips

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myowntrip.app.domain.model.Trip
import com.myowntrip.app.ui.components.home.HomeCap2Preview
import com.myowntrip.app.ui.components.home.HomeCap3ClonePreview
import com.myowntrip.app.ui.components.home.HomeEmptyState
import com.myowntrip.app.ui.components.home.HomeFilterMenuPresentation
import com.myowntrip.app.ui.components.home.HomeTripsContentState
import com.myowntrip.app.ui.components.home.HomeTripsScreen
import java.time.LocalDate

@Composable
fun TripListScreen(
  onCreateTrip: () -> Unit,
  onTripClick: (String) -> Unit,
  viewModel: TripListViewModel = hiltViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val today = remember { LocalDate.now() }
  val visibleTrips = remember(
    uiState.trips,
    uiState.searchQuery,
    uiState.filterPhase,
    uiState.sortOrder,
    today,
  ) {
    applyHomeTripFilters(
      trips = uiState.trips,
      searchQuery = uiState.searchQuery,
      filterPhase = uiState.filterPhase,
      sortOrder = uiState.sortOrder,
      today = today,
    )
  }
  val featuredTrip = visibleTrips.firstOrNull()
  val otherTrips = visibleTrips.drop(1)
  val hasAnyTrips = uiState.trips.isNotEmpty()
  var walletPromoDismissed by remember { mutableStateOf(false) }

  Scaffold(
    floatingActionButton = {
      if (hasAnyTrips) {
        FloatingActionButton(
          onClick = onCreateTrip,
          modifier = Modifier.semantics { contentDescription = "Crear viaje" },
        ) {
          Icon(Icons.Default.Add, contentDescription = null)
        }
      }
    },
  ) { padding ->
    if (!hasAnyTrips) {
      HomeEmptyState(
        modifier = Modifier
          .padding(padding)
          .fillMaxSize(),
        onCreateTrip = onCreateTrip,
      )
      return@Scaffold
    }

    val filterPresentation = if (uiState.filterMenuExpanded) {
      HomeFilterMenuPresentation.Overlay
    } else {
      HomeFilterMenuPresentation.Dropdown
    }

    HomeTripsScreen(
      state = HomeTripsContentState(
        featuredTrip = featuredTrip,
        otherTrips = otherTrips,
        visibleTripCount = visibleTrips.size,
        totalTripCount = uiState.trips.size,
        searchQuery = uiState.searchQuery,
        filterPhase = uiState.filterPhase,
        sortOrder = uiState.sortOrder,
        filterMenuExpanded = uiState.filterMenuExpanded,
        walletPromoDismissed = walletPromoDismissed,
        today = today,
        searchPlaceholder = searchPlaceholder(featuredTrip),
      ),
      onSearchQueryChange = viewModel::onSearchQueryChange,
      onFilterMenuExpandedChange = viewModel::onFilterMenuExpandedChange,
      onFilterPhaseChange = viewModel::onFilterPhaseChange,
      onSortOrderChange = viewModel::onSortOrderChange,
      onWalletPromoDismiss = { walletPromoDismissed = true },
      onTripClick = onTripClick,
      filterMenuPresentation = filterPresentation,
      modifier = Modifier
        .padding(padding)
        .fillMaxSize(),
    )
  }
}

private fun searchPlaceholder(featured: Trip?): String =
  featured?.destination ?: "Buscar viajes"

@Preview(name = "Home cap 2 — con viajes (205:1018)", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun TripListWithTripsPreview() {
  HomeCap2Preview()
}

@Preview(name = "Home cap 3 — búsqueda + menú (228:8161)", showBackground = true, widthDp = 360, heightDp = 1100)
@Composable
fun TripListFilterMenuPreview() {
  HomeCap3ClonePreview()
}
