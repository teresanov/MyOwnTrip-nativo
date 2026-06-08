package com.myowntrip.app.ui.features.trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class CreateTripUiState(
  val name: String = "",
  val destination: String = "",
  val startDate: LocalDate = LocalDate.now(),
  val endDate: LocalDate = LocalDate.now().plusDays(6),
  val nameError: String? = null,
  val destinationError: String? = null,
  val dateError: String? = null,
  val savedTripId: String? = null,
)

@HiltViewModel
class CreateTripViewModel @Inject constructor(
  private val tripRepository: TripRepository,
) : ViewModel() {
  private val _uiState = MutableStateFlow(CreateTripUiState())
  val uiState: StateFlow<CreateTripUiState> = _uiState.asStateFlow()

  fun onNameChange(value: String) = _uiState.update { it.copy(name = value, nameError = null) }
  fun onDestinationChange(value: String) =
    _uiState.update { it.copy(destination = value, destinationError = null) }

  fun onStartDateChange(value: LocalDate) =
    _uiState.update { it.copy(startDate = value, dateError = null) }

  fun onEndDateChange(value: LocalDate) =
    _uiState.update { it.copy(endDate = value, dateError = null) }

  fun save(onSuccess: (String) -> Unit) {
    val state = _uiState.value
    var valid = true
    if (state.name.isBlank()) {
      _uiState.update { it.copy(nameError = "Required") }
      valid = false
    }
    if (state.destination.isBlank()) {
      _uiState.update { it.copy(destinationError = "Required") }
      valid = false
    }
    if (state.endDate.isBefore(state.startDate)) {
      _uiState.update { it.copy(dateError = "End date must be after start") }
      valid = false
    }
    if (!valid) return

    viewModelScope.launch {
      val id = tripRepository.createTrip(
        name = state.name.trim(),
        destination = state.destination.trim(),
        startDate = state.startDate,
        endDate = state.endDate,
      )
      _uiState.update { it.copy(savedTripId = id) }
      onSuccess(id)
    }
  }
}
