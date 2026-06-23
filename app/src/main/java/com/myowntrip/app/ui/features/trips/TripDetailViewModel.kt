package com.myowntrip.app.ui.features.trips

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.ExpenseRepository
import com.myowntrip.app.data.repository.ItineraryRepository
import com.myowntrip.app.data.repository.JournalRepository
import com.myowntrip.app.data.repository.RestaurantRepository
import com.myowntrip.app.data.repository.TripRepository
import com.myowntrip.app.data.repository.WalletRepository
import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.Expense
import com.myowntrip.app.domain.model.ItineraryBlock
import com.myowntrip.app.domain.model.JournalNote
import com.myowntrip.app.domain.model.Restaurant
import com.myowntrip.app.domain.model.Trip
import com.myowntrip.app.domain.model.WalletEntry
import com.myowntrip.app.ui.features.wallet.WalletDocumentFilterPhase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TripDetailUiState(
  val trip: Trip? = null,
  val days: List<Day> = emptyList(),
  val walletEntries: List<WalletEntry> = emptyList(),
  val planBlocks: List<ItineraryBlock> = emptyList(),
  val journalSections: List<JournalDaySection> = emptyList(),
  val expenses: List<Expense> = emptyList(),
  val restaurants: List<Restaurant> = emptyList(),
  val showWalletLinkDialog: Boolean = false,
  val walletLinkBlockId: String? = null,
  val pendingWalletEntryId: String? = null,
  val walletFilterPhase: WalletDocumentFilterPhase = WalletDocumentFilterPhase.Active,
)

enum class TripDetailTab(val routeValue: String, val index: Int) {
  Wallet("wallet", 0),
  Plan("plan", 1),
  Journal("journal", 2),
  Expenses("expenses", 3),
  Restaurants("restaurants", 4),
  ;

  companion object {
    fun fromRoute(value: String?): TripDetailTab =
      entries.find { it.routeValue == value } ?: Wallet
  }
}

@HiltViewModel
class TripDetailViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  tripRepository: TripRepository,
  walletRepository: WalletRepository,
  journalRepository: JournalRepository,
  expenseRepository: ExpenseRepository,
  restaurantRepository: RestaurantRepository,
  private val itineraryRepository: ItineraryRepository,
) : ViewModel() {
  private val tripId: String = checkNotNull(savedStateHandle["tripId"])
  val initialTab: TripDetailTab = TripDetailTab.fromRoute(savedStateHandle["tab"])
  private val walletRepository: WalletRepository = walletRepository
  private val tripRepository: TripRepository = tripRepository

  private val walletLinkDialog = MutableStateFlow(WalletLinkDialogState())
  private val walletFilterPhase = MutableStateFlow(WalletDocumentFilterPhase.Active)

  private val trip = tripRepository.observeTrip(tripId)
  private val days = tripRepository.observeDays(tripId)
  private val wallet = walletRepository.observeByTrip(tripId)
  private val planBlocks = itineraryRepository.observeByTrip(tripId)
  private val journalNotes = journalRepository.observeByTrip(tripId)
  private val expenses = expenseRepository.observeByTrip(tripId)
  private val restaurants = restaurantRepository.observeByTrip(tripId)

  val uiState: StateFlow<TripDetailUiState> = combine(
    combine(trip, days, wallet, planBlocks, journalNotes) { t, d, w, blocks, notes ->
      PlanPartial(t, d, w, blocks, notes)
    },
    expenses,
    restaurants,
    walletLinkDialog,
    walletFilterPhase,
  ) { partial, e, r, linkDialog, filterPhase ->
    TripDetailUiState(
      trip = partial.trip,
      days = partial.days,
      walletEntries = partial.walletEntries,
      planBlocks = partial.planBlocks,
      journalSections = buildJournalSections(partial.days, partial.journalNotes),
      expenses = e,
      restaurants = r,
      showWalletLinkDialog = linkDialog.show,
      walletLinkBlockId = linkDialog.blockId,
      pendingWalletEntryId = linkDialog.pendingEntryId,
      walletFilterPhase = filterPhase,
    )
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TripDetailUiState())

  fun onWalletFilterPhaseChange(phase: WalletDocumentFilterPhase) {
    walletFilterPhase.value = phase
  }

  fun deleteWalletEntry(entryId: String) {
    viewModelScope.launch {
      walletRepository.deleteEntry(entryId)
    }
  }

  fun archiveWalletEntry(entryId: String, onArchived: (String) -> Unit) {
    viewModelScope.launch {
      val entry = uiState.value.walletEntries.find { it.id == entryId } ?: return@launch
      walletRepository.archiveEntry(entryId)
      onArchived(entry.title)
    }
  }

  fun unarchiveWalletEntry(entryId: String) {
    viewModelScope.launch {
      walletRepository.unarchiveEntry(entryId)
    }
  }

  fun loadDebugWalletSamples() {
    viewModelScope.launch {
      walletRepository.importDebugWalletSamples(tripId)
    }
  }

  fun resolveDefaultJournalDayId(
    today: LocalDate = LocalDate.now(),
    onResult: (String?) -> Unit,
  ) {
    viewModelScope.launch {
      onResult(tripRepository.defaultJournalDayId(tripId, today))
    }
  }

  fun showWalletLinkForBlock(blockId: String) {
    val current = uiState.value.planBlocks.find { it.id == blockId }?.walletEntryId
    walletLinkDialog.update {
      WalletLinkDialogState(show = true, blockId = blockId, pendingEntryId = current)
    }
  }

  fun dismissWalletLinkDialog() {
    walletLinkDialog.value = WalletLinkDialogState()
  }

  fun onPendingWalletEntrySelected(entryId: String?) {
    walletLinkDialog.update { it.copy(pendingEntryId = entryId) }
  }

  fun confirmWalletLink() {
    val dialog = walletLinkDialog.value
    val blockId = dialog.blockId ?: return
    val block = uiState.value.planBlocks.find { it.id == blockId } ?: return
    viewModelScope.launch {
      itineraryRepository.updateBlock(block.copy(walletEntryId = dialog.pendingEntryId))
      dismissWalletLinkDialog()
    }
  }

  fun walletEntryFor(id: String?): WalletEntry? =
    id?.let { entryId -> uiState.value.walletEntries.find { it.id == entryId } }

  private data class PlanPartial(
    val trip: Trip?,
    val days: List<Day>,
    val walletEntries: List<WalletEntry>,
    val planBlocks: List<ItineraryBlock>,
    val journalNotes: List<JournalNote>,
  )

  private data class WalletLinkDialogState(
    val show: Boolean = false,
    val blockId: String? = null,
    val pendingEntryId: String? = null,
  )
}