package com.myowntrip.app.ui.features.trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.TripRepository
import com.myowntrip.app.domain.model.Trip
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TripListViewModel @Inject constructor(
  tripRepository: TripRepository,
) : ViewModel() {
  val trips: StateFlow<List<Trip>> = tripRepository.observeTrips()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
