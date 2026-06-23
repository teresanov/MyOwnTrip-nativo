package com.myowntrip.app.ui.features.trips

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myowntrip.app.domain.model.Trip
import com.myowntrip.app.ui.components.home.ClearAllDataDialog
import com.myowntrip.app.ui.components.home.HomeEmptyState
import com.myowntrip.app.ui.components.home.HomeFabAddMode
import com.myowntrip.app.ui.components.home.HomeFabAddSheet
import com.myowntrip.app.ui.components.home.HomeFilterMenuPresentation
import com.myowntrip.app.ui.components.home.HomeQuickAction
import com.myowntrip.app.ui.components.home.HomeSpeedDial
import com.myowntrip.app.ui.components.home.HomeTripsContentState
import com.myowntrip.app.ui.components.home.HomeTripsScreen
import com.myowntrip.app.ui.theme.MOTSpacing
import java.time.LocalDate
import kotlinx.coroutines.launch

@Composable
fun TripListScreen(
  onCreateTrip: () -> Unit,
  onTripClick: (String) -> Unit,
  onImportDocument: (tripId: String) -> Unit,
  onManualDocument: (tripId: String) -> Unit,
  onAddJournal: (dayId: String, tripId: String) -> Unit,
  viewModel: TripListViewModel = hiltViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val today = remember { LocalDate.now() }
  val notebookTrips = remember(uiState.trips, today) {
    sortTripsForHome(uiState.trips, today)
  }
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
  val defaultNotebookId = featuredTrip?.id ?: notebookTrips.firstOrNull()?.id
  val otherTrips = visibleTrips.drop(1)
  val hasAnyTrips = uiState.trips.isNotEmpty()
  var speedDialExpanded by remember { mutableStateOf(false) }
  var fabAddSheet by remember { mutableStateOf<HomeFabAddMode?>(null) }
  var showClearConfirm by remember { mutableStateOf(false) }
  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()

  val openFabSheet: (HomeFabAddMode) -> Unit = { mode ->
    speedDialExpanded = false
    fabAddSheet = mode
  }

  val resolveSouvenir: (String) -> Unit = { tripId ->
    viewModel.resolveJournalDayId(tripId, today) { dayId ->
      if (dayId != null) {
        onAddJournal(dayId, tripId)
      } else {
        scope.launch {
          snackbarHostState.showSnackbar("No hay días en este viaje todavía")
        }
      }
    }
  }

  val speedDialActions = remember {
    listOf(
      HomeQuickAction(
        label = "Nuevo viaje",
        icon = Icons.Default.Luggage,
        contentDescription = "Crear viaje",
        onClick = onCreateTrip,
      ),
      HomeQuickAction(
        label = "Recuerdo",
        icon = Icons.Default.Edit,
        contentDescription = "Añadir recuerdo al cuaderno",
        onClick = { openFabSheet(HomeFabAddMode.Souvenir) },
      ),
      HomeQuickAction(
        label = "Documento",
        icon = Icons.Default.Upload,
        contentDescription = "Añadir documento a Wallet",
        onClick = { openFabSheet(HomeFabAddMode.Document) },
      ),
    )
  }

  fabAddSheet?.let { mode ->
    HomeFabAddSheet(
      mode = mode,
      trips = notebookTrips,
      defaultTripId = defaultNotebookId,
      onDismiss = { fabAddSheet = null },
      onCreateTrip = onCreateTrip,
      onImportDocument = onImportDocument,
      onManualDocument = onManualDocument,
      onAddSouvenir = resolveSouvenir,
    )
  }

  Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
  ) { padding ->
    Box(
      modifier = Modifier
        .padding(padding)
        .fillMaxSize(),
    ) {
      if (!hasAnyTrips) {
        HomeEmptyState(
          onCreateTrip = onCreateTrip,
          onClearAllData = { showClearConfirm = true },
          modifier = Modifier.fillMaxSize(),
        )
      } else {
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
            today = today,
            searchPlaceholder = searchPlaceholder(featuredTrip),
          ),
          onSearchQueryChange = viewModel::onSearchQueryChange,
          onFilterMenuExpandedChange = viewModel::onFilterMenuExpandedChange,
          onFilterPhaseChange = viewModel::onFilterPhaseChange,
          onSortOrderChange = viewModel::onSortOrderChange,
          onTripClick = onTripClick,
          onClearAllData = { showClearConfirm = true },
          filterMenuPresentation = filterPresentation,
          modifier = Modifier.fillMaxSize(),
        )
      }

      HomeSpeedDial(
        actions = speedDialActions,
        expanded = speedDialExpanded,
        onExpandedChange = { speedDialExpanded = it },
        modifier = Modifier
          .fillMaxSize()
          .padding(bottom = MOTSpacing.screenContentBottomWithFab),
      )
    }
  }

  if (showClearConfirm) {
    ClearAllDataDialog(
      onDismiss = { showClearConfirm = false },
      onConfirm = {
        showClearConfirm = false
        viewModel.clearAllUserData {
          scope.launch {
            snackbarHostState.showSnackbar("Datos borrados. Crea tu primer viaje cuando quieras.")
          }
        }
      },
    )
  }
}

private fun searchPlaceholder(featured: Trip?): String =
  featured?.destination ?: "Buscar viajes"
