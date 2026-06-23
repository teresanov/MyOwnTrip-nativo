package com.myowntrip.app.ui.features.trips

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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
import com.myowntrip.app.ui.components.home.DeleteTripDialog
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
  val activeTrips = remember(uiState.trips) { uiState.trips.filter { !it.isArchived } }
  val notebookTrips = remember(activeTrips, today) {
    sortTripsForHome(activeTrips, today)
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
  val hasAnyTrips = activeTrips.isNotEmpty()
  val viewingArchived = uiState.filterPhase == TripFilterPhase.Archived
  val hasOnlyPastTrips = hasAnyTrips && !hasAnyCurrentOrUpcomingTrips(activeTrips, today)
  val featuredTrip = when {
    viewingArchived || hasOnlyPastTrips -> null
    else -> visibleTrips.firstOrNull()
  }
  val defaultNotebookId = featuredTrip?.id ?: notebookTrips.firstOrNull()?.id
  val otherTrips = when {
    viewingArchived || hasOnlyPastTrips -> visibleTrips
    else -> visibleTrips.drop(1)
  }
  var speedDialExpanded by remember { mutableStateOf(false) }
  var fabAddSheet by remember { mutableStateOf<HomeFabAddMode?>(null) }
  var showClearConfirm by remember { mutableStateOf(false) }
  var tripPendingDelete by remember { mutableStateOf<Trip?>(null) }
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

  val requestDelete: (String) -> Unit = { tripId ->
    tripPendingDelete = uiState.trips.find { it.id == tripId }
  }

  val archiveTrip: (String) -> Unit = { tripId ->
    viewModel.archiveTrip(tripId) { tripName ->
      scope.launch {
        val result = snackbarHostState.showSnackbar(
          message = "«$tripName» archivado",
          actionLabel = "Deshacer",
          duration = SnackbarDuration.Short,
        )
        if (result == SnackbarResult.ActionPerformed) {
          viewModel.unarchiveTrip(tripId)
        }
      }
    }
  }

  Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
  ) { padding ->
    Box(
      modifier = Modifier
        .padding(padding)
        .fillMaxSize(),
    ) {
      if (!hasAnyTrips && uiState.filterPhase != TripFilterPhase.Archived) {
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
            totalTripCount = activeTrips.size,
            searchQuery = uiState.searchQuery,
            filterPhase = uiState.filterPhase,
            sortOrder = uiState.sortOrder,
            filterMenuExpanded = uiState.filterMenuExpanded,
            today = today,
            searchPlaceholder = if (hasOnlyPastTrips) "Buscar viajes" else searchPlaceholder(featuredTrip),
            onlyPastMode = hasOnlyPastTrips,
          ),
          onSearchQueryChange = viewModel::onSearchQueryChange,
          onFilterMenuExpandedChange = viewModel::onFilterMenuExpandedChange,
          onFilterPhaseChange = viewModel::onFilterPhaseChange,
          onSortOrderChange = viewModel::onSortOrderChange,
          onTripClick = onTripClick,
          onArchiveTrip = archiveTrip,
          onUnarchiveTrip = viewModel::unarchiveTrip,
          onDeleteTripRequest = requestDelete,
          onClearAllData = { showClearConfirm = true },
          onCreateTrip = onCreateTrip,
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

  tripPendingDelete?.let { trip ->
    DeleteTripDialog(
      tripName = trip.name,
      onDismiss = { tripPendingDelete = null },
      onConfirmDelete = {
        viewModel.deleteTrip(trip.id)
        tripPendingDelete = null
        scope.launch {
          snackbarHostState.showSnackbar("Viaje eliminado")
        }
      },
    )
  }
}

private fun searchPlaceholder(featured: Trip?): String =
  featured?.destination ?: "Buscar viajes"
