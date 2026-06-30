package com.myowntrip.app.ui.features.places

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.RestaurantRepository
import com.myowntrip.app.domain.model.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlacesMapViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  restaurantRepository: RestaurantRepository,
) : ViewModel() {
  private val tripId: String = checkNotNull(savedStateHandle["tripId"])

  /** Solo sitios del viaje que tienen coordenadas (los que se pueden pinear). */
  val places: StateFlow<List<Restaurant>> = restaurantRepository.observeByTrip(tripId)
    .map { list -> list.filter { it.hasLocation } }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
