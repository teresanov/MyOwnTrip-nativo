package com.myowntrip.app.ui.features.trips

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.TripRepository
import com.myowntrip.app.domain.cover.DestinationCoverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

data class EditTripUiState(
  val isLoading: Boolean = true,
  val name: String = "",
  val destination: String = "",
  val startDate: LocalDate = LocalDate.now(),
  val endDate: LocalDate = LocalDate.now().plusDays(6),
  val originalDayCount: Int = 0,
  val nameError: String? = null,
  val destinationError: String? = null,
  val dateError: String? = null,
  val isSaving: Boolean = false,
  val isDeleting: Boolean = false,
) {
  val willShrinkDays: Boolean
    get() = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1 < originalDayCount
}

@HiltViewModel
class EditTripViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val tripRepository: TripRepository,
  private val destinationCoverRepository: DestinationCoverRepository,
) : ViewModel() {
  private val tripId: String = checkNotNull(savedStateHandle["tripId"])
  private val _uiState = MutableStateFlow(EditTripUiState())
  val uiState: StateFlow<EditTripUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      val trip = tripRepository.observeTrip(tripId).filterNotNull().first()
      val days = tripRepository.getDaysForTrip(tripId)
      _uiState.update {
        it.copy(
          isLoading = false,
          name = trip.name,
          destination = trip.destination,
          startDate = trip.startDate,
          endDate = trip.endDate,
          originalDayCount = days.size,
        )
      }
    }
  }

  fun onNameChange(value: String) = _uiState.update { it.copy(name = value, nameError = null) }

  fun onDestinationChange(value: String) =
    _uiState.update { it.copy(destination = value, destinationError = null) }

  fun onDateRangeChange(start: LocalDate, end: LocalDate) {
    _uiState.update { it.copy(startDate = start, endDate = end, dateError = null) }
  }

  fun save(onSuccess: () -> Unit) {
    if (_uiState.value.isSaving || _uiState.value.isDeleting || !validate()) return
    viewModelScope.launch {
      _uiState.update { it.copy(isSaving = true) }
      try {
        val state = _uiState.value
        tripRepository.updateTrip(
          tripId = tripId,
          name = state.name,
          destination = state.destination,
          startDate = state.startDate,
          endDate = state.endDate,
        )
        val destination = state.destination.trim()
        if (destination.isNotEmpty()) {
          runCatching {
            destinationCoverRepository.attachCoverToTrip(tripId, destination)
          }
        }
        onSuccess()
      } finally {
        _uiState.update { it.copy(isSaving = false) }
      }
    }
  }

  fun delete(onSuccess: () -> Unit) {
    if (_uiState.value.isSaving || _uiState.value.isDeleting) return
    viewModelScope.launch {
      _uiState.update { it.copy(isDeleting = true) }
      try {
        tripRepository.deleteTrip(tripId)
        onSuccess()
      } finally {
        _uiState.update { it.copy(isDeleting = false) }
      }
    }
  }

  private fun validate(): Boolean {
    val state = _uiState.value
    var valid = true
    if (state.name.isBlank()) {
      _uiState.update { it.copy(nameError = "Obligatorio") }
      valid = false
    }
    if (state.destination.isBlank()) {
      _uiState.update { it.copy(destinationError = "Obligatorio") }
      valid = false
    }
    if (state.endDate.isBefore(state.startDate)) {
      _uiState.update { it.copy(dateError = "La fecha de fin debe ser posterior al inicio") }
      valid = false
    }
    return valid
  }
}
