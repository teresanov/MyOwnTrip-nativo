package com.myowntrip.app.ui.features.wallet

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.ItineraryRepository
import com.myowntrip.app.data.repository.TripRepository
import com.myowntrip.app.data.repository.WalletRepository
import com.myowntrip.app.domain.model.WalletEntry
import com.myowntrip.app.domain.plan.PlanPlacementDriftLogic
import com.myowntrip.app.domain.plan.WalletPlanPlacementInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletDetailViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val walletRepository: WalletRepository,
  private val itineraryRepository: ItineraryRepository,
  private val tripRepository: TripRepository,
) : ViewModel() {
  private val entryId: String = checkNotNull(savedStateHandle["entryId"])

  val entry: StateFlow<WalletEntry?> = walletRepository.observeById(entryId)
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

  val planPlacement: StateFlow<WalletPlanPlacementInfo?> = entry
    .flatMapLatest { walletEntry ->
      if (walletEntry == null) {
        flowOf(null)
      } else {
        combine(
          itineraryRepository.observeByTrip(walletEntry.tripId),
          tripRepository.observeDays(walletEntry.tripId),
        ) { blocks, days ->
          PlanPlacementDriftLogic.resolveWalletPlanPlacement(walletEntry, blocks, days)
        }
      }
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

  fun deleteEntry(onDeleted: () -> Unit) {
    viewModelScope.launch {
      walletRepository.deleteEntry(entryId)
      onDeleted()
    }
  }
}
