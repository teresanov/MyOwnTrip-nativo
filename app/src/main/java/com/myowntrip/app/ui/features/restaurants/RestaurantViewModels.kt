package com.myowntrip.app.ui.features.restaurants

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.RestaurantRepository
import com.myowntrip.app.domain.model.Restaurant
import com.myowntrip.app.domain.model.RestaurantStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RestaurantFormUiState(
  val tripId: String,
  val name: String = "",
  val address: String = "",
  val notes: String = "",
  val nameError: String? = null,
)

@HiltViewModel
class RestaurantFormViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val restaurantRepository: RestaurantRepository,
) : ViewModel() {
  private val tripId: String = checkNotNull(savedStateHandle["tripId"])

  private val _uiState = MutableStateFlow(RestaurantFormUiState(tripId = tripId))
  val uiState: StateFlow<RestaurantFormUiState> = _uiState.asStateFlow()

  fun onNameChange(value: String) = _uiState.update { it.copy(name = value, nameError = null) }
  fun onAddressChange(value: String) = _uiState.update { it.copy(address = value) }
  fun onNotesChange(value: String) = _uiState.update { it.copy(notes = value) }

  fun save(onSuccess: () -> Unit) {
    val state = _uiState.value
    if (state.name.isBlank()) {
      _uiState.update { it.copy(nameError = "Required") }
      return
    }
    viewModelScope.launch {
      restaurantRepository.addRestaurant(
        tripId = tripId,
        name = state.name,
        address = state.address.ifBlank { null },
        dayId = null,
        notes = state.notes.ifBlank { null },
      )
      onSuccess()
    }
  }
}

@HiltViewModel
class RestaurantDetailViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val restaurantRepository: RestaurantRepository,
) : ViewModel() {
  private val restaurantId: String = checkNotNull(savedStateHandle["restaurantId"])

  val restaurant: StateFlow<Restaurant?> = restaurantRepository.observeById(restaurantId)
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

  fun updateStatus(status: RestaurantStatus) {
    viewModelScope.launch {
      restaurantRepository.updateStatus(restaurantId, status)
    }
  }
}
