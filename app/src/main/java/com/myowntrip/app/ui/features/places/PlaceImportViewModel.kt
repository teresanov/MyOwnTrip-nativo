package com.myowntrip.app.ui.features.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.RestaurantRepository
import com.myowntrip.app.data.repository.TripRepository
import com.myowntrip.app.data.share.PlaceLinkResolver
import com.myowntrip.app.data.share.PlaceShareParser
import com.myowntrip.app.domain.model.PlaceCategory
import com.myowntrip.app.domain.model.Trip
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlaceImportUiState(
  val name: String = "",
  val category: PlaceCategory = PlaceCategory.OTRO,
  val googleMapsUri: String? = null,
  val latitude: Double? = null,
  val longitude: Double? = null,
  val trips: List<Trip> = emptyList(),
  val selectedTripId: String? = null,
  /** Resolviendo el enlace corto para sacar coordenadas. */
  val resolving: Boolean = false,
) {
  val hasLocation: Boolean get() = latitude != null && longitude != null
  val canSave: Boolean get() = name.isNotBlank() && selectedTripId != null
}

@HiltViewModel
class PlaceImportViewModel @Inject constructor(
  private val restaurantRepository: RestaurantRepository,
  private val tripRepository: TripRepository,
  private val linkResolver: PlaceLinkResolver,
) : ViewModel() {

  private val _uiState = MutableStateFlow(PlaceImportUiState())
  val uiState: StateFlow<PlaceImportUiState> = _uiState.asStateFlow()

  private var started = false

  /** Arranca con lo que llegó por el share. Idempotente (sobrevive a recomposiciones). */
  fun start(sharedText: String?, subject: String?) {
    if (started) return
    started = true

    val parsed = PlaceShareParser.parseShared(sharedText, subject)
    // Si Maps compartió la URL larga, las coordenadas ya vienen dentro: úsalas sin red.
    val inlineLatLng = PlaceShareParser.extractLatLng(parsed.url)
    _uiState.update {
      it.copy(
        name = parsed.name.orEmpty(),
        googleMapsUri = parsed.url,
        latitude = inlineLatLng?.first,
        longitude = inlineLatLng?.second,
        // Solo hace falta resolver por red si hay enlace corto SIN coordenadas inline.
        resolving = parsed.url != null && inlineLatLng == null,
      )
    }

    viewModelScope.launch {
      val trips = tripRepository.getTrips()
        .filter { !it.isArchived }
        .sortedByDescending { it.createdAt }
      _uiState.update { it.copy(trips = trips, selectedTripId = trips.firstOrNull()?.id) }

      val url = parsed.url
      if (inlineLatLng == null && url != null) {
        val latlng = PlaceShareParser.extractLatLng(linkResolver.resolveChain(url))
        _uiState.update {
          it.copy(latitude = latlng?.first, longitude = latlng?.second, resolving = false)
        }
      }
    }
  }

  fun onNameChange(value: String) = _uiState.update { it.copy(name = value) }
  fun onCategoryChange(value: PlaceCategory) = _uiState.update { it.copy(category = value) }
  fun onTripSelected(tripId: String) = _uiState.update { it.copy(selectedTripId = tripId) }

  fun save(onSaved: () -> Unit) {
    val state = _uiState.value
    val tripId = state.selectedTripId
    if (state.name.isBlank() || tripId == null) return
    viewModelScope.launch {
      restaurantRepository.addRestaurant(
        tripId = tripId,
        name = state.name,
        address = null,
        dayId = null,
        notes = null,
        category = state.category,
        latitude = state.latitude,
        longitude = state.longitude,
        googleMapsUri = state.googleMapsUri,
      )
      onSaved()
    }
  }
}
