package com.myowntrip.app.ui.features.trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.AppDataRepository
import com.myowntrip.app.data.repository.TripRepository
import com.myowntrip.app.domain.cover.DestinationCoverRepository
import com.myowntrip.app.domain.model.Trip
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class TripListUiState(
  val trips: List<Trip> = emptyList(),
  val searchQuery: String = "",
  val filterPhase: TripFilterPhase = TripFilterPhase.All,
  val sortOrder: TripSortOrder = TripSortOrder.DateUpcoming,
  val filterMenuExpanded: Boolean = false,
)

@HiltViewModel
class TripListViewModel @Inject constructor(
  private val tripRepository: TripRepository,
  private val appDataRepository: AppDataRepository,
  private val destinationCoverRepository: DestinationCoverRepository,
) : ViewModel() {
  private val controls = MutableStateFlow(
    TripListUiState(),
  )

  val uiState: StateFlow<TripListUiState> = combine(
    tripRepository.observeTrips(),
    controls,
  ) { trips, state ->
    state.copy(trips = trips)
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TripListUiState())

  init {
    viewModelScope.launch {
      uiState
        .map { it.trips }
        .distinctUntilChanged()
        .collect { trips ->
          trips
            .filter { trip -> trip.coverPhoto.isNullOrBlank() || !java.io.File(trip.coverPhoto).isFile }
            .forEach { trip ->
              launch {
                runCatching {
                  destinationCoverRepository.attachCoverToTrip(trip.id, trip.destination)
                }
              }
            }
        }
    }
  }

  fun onSearchQueryChange(query: String) {
    controls.update { it.copy(searchQuery = query) }
  }

  fun onFilterPhaseChange(phase: TripFilterPhase) {
    controls.update { it.copy(filterPhase = phase, filterMenuExpanded = false) }
  }

  fun onSortOrderChange(order: TripSortOrder) {
    controls.update { it.copy(sortOrder = order, filterMenuExpanded = false) }
  }

  fun onFilterMenuExpandedChange(expanded: Boolean) {
    controls.update { it.copy(filterMenuExpanded = expanded) }
  }

  fun resolveJournalDayId(
    tripId: String,
    today: LocalDate = LocalDate.now(),
    onResult: (String?) -> Unit,
  ) {
    viewModelScope.launch {
      onResult(tripRepository.defaultJournalDayId(tripId, today))
    }
  }

  fun clearAllUserData(onDone: () -> Unit) {
    viewModelScope.launch {
      appDataRepository.clearAllUserData()
      controls.value = TripListUiState()
      onDone()
    }
  }
}
