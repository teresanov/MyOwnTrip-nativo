package com.myowntrip.app.ui.features.trips

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.ExpenseRepository
import com.myowntrip.app.data.repository.ItineraryRepository
import com.myowntrip.app.data.repository.JournalPageRepository
import com.myowntrip.app.data.repository.JournalRepository
import com.myowntrip.app.data.repository.RestaurantRepository
import com.myowntrip.app.data.repository.TripRepository
import com.myowntrip.app.data.repository.WalletRepository
import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.Expense
import com.myowntrip.app.domain.model.ItineraryBlock
import com.myowntrip.app.domain.model.JournalNote
import com.myowntrip.app.domain.model.JournalPage
import com.myowntrip.app.domain.model.Restaurant
import com.myowntrip.app.domain.model.Trip
import com.myowntrip.app.domain.model.WalletEntry
import com.myowntrip.app.ui.features.journal.spread.AlbumPageUi
import com.myowntrip.app.ui.features.journal.spread.CaptureTarget
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
  val journalNotes: List<JournalNote> = emptyList(),
  val journalPages: List<JournalPage> = emptyList(),
  val expenses: List<Expense> = emptyList(),
  val restaurants: List<Restaurant> = emptyList(),
  val showWalletLinkDialog: Boolean = false,
  val walletLinkBlockId: String? = null,
  val pendingWalletEntryId: String? = null,
  val walletFilterPhase: WalletDocumentFilterPhase = WalletDocumentFilterPhase.Active,
  val isPastTrip: Boolean = false,
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
  private val tripRepository: TripRepository,
  private val walletRepository: WalletRepository,
  private val journalRepository: JournalRepository,
  private val journalPageRepository: JournalPageRepository,
  expenseRepository: ExpenseRepository,
  restaurantRepository: RestaurantRepository,
  private val itineraryRepository: ItineraryRepository,
) : ViewModel() {
  private val tripId: String = checkNotNull(savedStateHandle["tripId"])
  val initialTab: TripDetailTab = TripDetailTab.fromRoute(savedStateHandle["tab"])

  private val walletLinkDialog = MutableStateFlow(WalletLinkDialogState())
  private val walletFilterPhase = MutableStateFlow(WalletDocumentFilterPhase.Active)

  private val trip = tripRepository.observeTrip(tripId)
  private val days = tripRepository.observeDays(tripId)
  private val wallet = walletRepository.observeByTrip(tripId)
  private val planBlocks = itineraryRepository.observeByTrip(tripId)
  private val journalNotes = journalRepository.observeByTrip(tripId)
  private val journalPages = journalPageRepository.observeByTrip(tripId)
  private val journalData = combine(journalNotes, journalPages) { notes, pages -> notes to pages }
  private val expenses = expenseRepository.observeByTrip(tripId)
  private val restaurants = restaurantRepository.observeByTrip(tripId)

  val uiState: StateFlow<TripDetailUiState> = combine(
    combine(trip, days, wallet, planBlocks, journalData) { t, d, w, blocks, journal ->
      PlanPartial(t, d, w, blocks, journal.first, journal.second)
    },
    expenses,
    restaurants,
    walletLinkDialog,
    walletFilterPhase,
  ) { partial, e, r, linkDialog, filterPhase ->
    val isPastTrip = partial.trip?.homePhase(LocalDate.now()) == HomeTripPhase.Past
    TripDetailUiState(
      trip = partial.trip,
      days = partial.days,
      walletEntries = partial.walletEntries,
      planBlocks = partial.planBlocks,
      journalNotes = partial.journalNotes,
      journalPages = partial.journalPages,
      expenses = e,
      restaurants = r,
      showWalletLinkDialog = linkDialog.show && !isPastTrip,
      walletLinkBlockId = linkDialog.blockId,
      pendingWalletEntryId = linkDialog.pendingEntryId,
      walletFilterPhase = filterPhase,
      isPastTrip = isPastTrip,
    )
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TripDetailUiState())

  init {
    // Reconciliador a nivel viaje: cualquier pieza sin hoja asignada (capturas o bocetos)
    // se pega a la hoja por defecto de su día. Evita piezas huérfanas sea cual sea la vía.
    viewModelScope.launch {
      journalRepository.observeByTrip(tripId).collect { notes ->
        val orphans = notes.filter { it.pageId == null }
        if (orphans.isNotEmpty()) {
          orphans.groupBy { it.dayId }.forEach { (dId, group) ->
            val page = journalPageRepository.ensureDefaultPage(dId, tripId)
            group.forEach { journalRepository.updateNote(it.copy(pageId = page.id)) }
          }
        }
      }
    }
  }

  /** Persiste posición/rotación/escala/zIndex de una pieza arrastrada en el álbum. */
  fun persistPieceTransform(
    noteId: String,
    canvasX: Float,
    canvasY: Float,
    rotation: Float,
    scale: Float,
    zIndex: Int,
  ) {
    if (uiState.value.isPastTrip) return
    viewModelScope.launch {
      journalRepository.updateNoteTransform(noteId, canvasX, canvasY, rotation, scale, zIndex)
    }
  }

  /** Crea una hoja nueva al final del día indicado (el de la hoja activa del álbum). */
  fun addJournalSheet(dayId: String) {
    if (uiState.value.isPastTrip) return
    viewModelScope.launch {
      journalPageRepository.addPage(dayId = dayId, tripId = tripId)
    }
  }

  /**
   * Día + hoja donde cae una pieza nueva: la hoja activa del álbum si está persistida;
   * si es virtual (día sin hojas todavía), crea/usa la hoja por defecto de ese día. Sin
   * hoja activa, recurre al día de diario por defecto del viaje.
   */
  suspend fun resolveCaptureTarget(active: AlbumPageUi?): CaptureTarget {
    if (active != null && active.isPersisted) {
      return CaptureTarget(active.day.id, active.pageId)
    }
    val dayId = active?.day?.id
      ?: checkNotNull(tripRepository.defaultJournalDayId(tripId, LocalDate.now())) {
        "El viaje no tiene días para el diario"
      }
    return CaptureTarget(dayId, journalPageRepository.ensureDefaultPage(dayId, tripId).id)
  }

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
    if (uiState.value.isPastTrip) return
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

  private data class PlanPartial(
    val trip: Trip?,
    val days: List<Day>,
    val walletEntries: List<WalletEntry>,
    val planBlocks: List<ItineraryBlock>,
    val journalNotes: List<JournalNote>,
    val journalPages: List<JournalPage>,
  )

  private data class WalletLinkDialogState(
    val show: Boolean = false,
    val blockId: String? = null,
    val pendingEntryId: String? = null,
  )
}