package com.myowntrip.app.ui.features.wallet

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.ExpenseRepository
import com.myowntrip.app.data.repository.ItineraryRepository
import com.myowntrip.app.data.repository.TripRepository
import com.myowntrip.app.data.repository.WalletRepository
import com.myowntrip.app.domain.model.EntryType
import com.myowntrip.app.domain.model.Expense
import com.myowntrip.app.domain.model.ExpenseCategory
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
  private val expenseRepository: ExpenseRepository,
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

  /** Gasto creado desde esta reserva, si el usuario lo registró. */
  val linkedExpense: StateFlow<Expense?> = expenseRepository.observeByWalletEntry(entryId)
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

  /** Registro explícito, nunca automático: evita duplicados y totales en los que no se confía. */
  fun registerAsExpense() {
    val walletEntry = entry.value ?: return
    val amount = walletEntry.amount ?: return
    if (linkedExpense.value != null) return
    viewModelScope.launch {
      expenseRepository.addExpense(
        tripId = walletEntry.tripId,
        dayId = null,
        amount = amount,
        currency = walletEntry.currency ?: "EUR",
        concept = walletEntry.title,
        category = walletEntry.type.toExpenseCategory(),
        walletEntryId = walletEntry.id,
      )
    }
  }

  fun unregisterExpense() {
    val expense = linkedExpense.value ?: return
    viewModelScope.launch { expenseRepository.deleteExpense(expense.id) }
  }

  fun deleteEntry(onDeleted: () -> Unit) {
    viewModelScope.launch {
      walletRepository.deleteEntry(entryId)
      onDeleted()
    }
  }
}

private fun EntryType.toExpenseCategory(): ExpenseCategory = when (this) {
  EntryType.FLIGHT, EntryType.TRANSPORT -> ExpenseCategory.TRANSPORT
  EntryType.HOTEL -> ExpenseCategory.ACCOMMODATION
  EntryType.ACTIVITY -> ExpenseCategory.ACTIVITY
  EntryType.GENERIC -> ExpenseCategory.OTHER
}
