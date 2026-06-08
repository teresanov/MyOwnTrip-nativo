package com.myowntrip.app.ui.features.trips

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.ExpenseRepository
import com.myowntrip.app.data.repository.TripRepository
import com.myowntrip.app.data.repository.WalletRepository
import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.Expense
import com.myowntrip.app.domain.model.Trip
import com.myowntrip.app.domain.model.WalletEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class TripDetailUiState(
  val trip: Trip? = null,
  val days: List<Day> = emptyList(),
  val walletEntries: List<WalletEntry> = emptyList(),
  val expenses: List<Expense> = emptyList(),
  val selectedTab: TripTab = TripTab.Wallet,
)

enum class TripTab { Wallet, Days, Expenses }

@HiltViewModel
class TripDetailViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  tripRepository: TripRepository,
  walletRepository: WalletRepository,
  expenseRepository: ExpenseRepository,
) : ViewModel() {
  private val tripId: String = checkNotNull(savedStateHandle["tripId"])

  private val trip = tripRepository.observeTrip(tripId)
  private val days = tripRepository.observeDays(tripId)
  private val wallet = walletRepository.observeByTrip(tripId)
  private val expenses = expenseRepository.observeByTrip(tripId)

  val uiState: StateFlow<TripDetailUiState> = combine(trip, days, wallet, expenses) { t, d, w, e ->
    TripDetailUiState(trip = t, days = d, walletEntries = w, expenses = e)
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TripDetailUiState())

  fun selectTab(tab: TripTab) {
    // handled in composable via local state for simplicity
  }
}
