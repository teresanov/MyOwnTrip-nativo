package com.myowntrip.app.ui.features.dayhub

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.ItineraryRepository
import com.myowntrip.app.data.repository.JournalRepository
import com.myowntrip.app.data.repository.TripRepository
import com.myowntrip.app.data.repository.WalletRepository
import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.ItineraryBlock
import com.myowntrip.app.domain.model.JournalNote
import com.myowntrip.app.domain.model.WalletEntry
import com.myowntrip.app.domain.plan.DayPlanScheduleLogic
import com.myowntrip.app.domain.plan.PlanPlacementDriftLogic
import com.myowntrip.app.domain.plan.PlanPlacementLogic
import com.myowntrip.app.ui.features.trips.HomeTripPhase
import com.myowntrip.app.ui.features.trips.homePhase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class DayHubTab(val routeValue: String, val index: Int) {
  Plan("plan", 0),
  Journal("journal", 1),
  ;

  companion object {
    fun fromRoute(value: String?): DayHubTab =
      entries.find { it.routeValue == value } ?: Plan
  }
}

sealed interface DayHubEvent {
  data class Snackbar(
    val message: String,
    val actionLabel: String? = null,
  ) : DayHubEvent

  data class NavigateToDay(val tripId: String, val dayId: String) : DayHubEvent
}

data class DayHubUiState(
  val tripId: String = "",
  val day: Day? = null,
  val tripDays: List<Day> = emptyList(),
  val blocks: List<ItineraryBlock> = emptyList(),
  val notes: List<JournalNote> = emptyList(),
  val walletEntries: List<WalletEntry> = emptyList(),
  val isPastTrip: Boolean = false,
  val showAddBlock: Boolean = false,
  val newBlockTitle: String = "",
  val newBlockTime: String = "",
  val newBlockWalletEntryId: String? = null,
  val showWalletLinkDialog: Boolean = false,
  val walletLinkTargetBlockId: String? = null,
  val pendingWalletEntryId: String? = null,
  val showMoveDayDialog: Boolean = false,
  val moveDayBlockId: String? = null,
  val moveDaySelectedDayId: String? = null,
  val moveDayTime: String = "",
)

@HiltViewModel
class DayHubViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  tripRepository: TripRepository,
  private val itineraryRepository: ItineraryRepository,
  journalRepository: JournalRepository,
  walletRepository: WalletRepository,
) : ViewModel() {
  private val dayId: String = checkNotNull(savedStateHandle["dayId"])
  private val tripId: String = checkNotNull(savedStateHandle["tripId"])
  val initialTab: DayHubTab = DayHubTab.fromRoute(savedStateHandle["tab"])

  private val dialogState = MutableStateFlow(DialogOverlayState())

  private val _uiState = MutableStateFlow(DayHubUiState())
  val uiState: StateFlow<DayHubUiState> = _uiState.asStateFlow()

  private val _events = MutableSharedFlow<DayHubEvent>(extraBufferCapacity = 4)
  val events: SharedFlow<DayHubEvent> = _events.asSharedFlow()

  init {
    viewModelScope.launch {
      combine(
        combine(
          tripRepository.observeDay(dayId),
          tripRepository.observeDays(tripId),
          itineraryRepository.observeByDay(dayId),
          journalRepository.observeByDay(dayId),
        ) { day, tripDays, blocks, notes ->
          Quad(day, tripDays, blocks, notes)
        },
        combine(
          walletRepository.observeByTrip(tripId),
          tripRepository.observeTrip(tripId),
          dialogState,
        ) { walletEntries, trip, dialog -> Triple(walletEntries, trip, dialog) },
      ) { dayData, meta ->
        val day = dayData.first
        val tripDays = dayData.second
        val blocks = dayData.third
        val notes = dayData.fourth
        val (walletEntries, trip, dialog) = meta
        val isPastTrip = trip?.homePhase(LocalDate.now()) == HomeTripPhase.Past
        DayHubUiState(
          tripId = tripId,
          day = day,
          tripDays = tripDays,
          blocks = blocks,
          notes = notes,
          walletEntries = walletEntries,
          isPastTrip = isPastTrip,
          showAddBlock = dialog.showAddBlock && !isPastTrip,
          newBlockTitle = dialog.newBlockTitle,
          newBlockTime = dialog.newBlockTime,
          newBlockWalletEntryId = dialog.newBlockWalletEntryId,
          showWalletLinkDialog = dialog.showWalletLinkDialog && !isPastTrip,
          walletLinkTargetBlockId = dialog.walletLinkTargetBlockId,
          pendingWalletEntryId = dialog.pendingWalletEntryId,
          showMoveDayDialog = dialog.showMoveDayDialog && !isPastTrip,
          moveDayBlockId = dialog.moveDayBlockId,
          moveDaySelectedDayId = dialog.moveDaySelectedDayId,
          moveDayTime = dialog.moveDayTime,
        )
      }.collect { merged ->
        _uiState.value = merged
      }
    }
  }

  fun showAddBlock() {
    if (_uiState.value.isPastTrip) return
    dialogState.update {
      it.copy(
        showAddBlock = true,
        newBlockWalletEntryId = null,
      )
    }
  }

  fun dismissAddBlock() = dialogState.update {
    it.copy(
      showAddBlock = false,
      newBlockTitle = "",
      newBlockTime = "",
      newBlockWalletEntryId = null,
    )
  }

  fun onNewBlockTitleChange(value: String) =
    dialogState.update { it.copy(newBlockTitle = value) }

  fun onNewBlockTimeChange(value: String) =
    dialogState.update { it.copy(newBlockTime = value) }

  fun showWalletPickerForNewBlock() {
    if (_uiState.value.isPastTrip) return
    dialogState.update {
      it.copy(
        showWalletLinkDialog = true,
        walletLinkTargetBlockId = null,
        pendingWalletEntryId = it.newBlockWalletEntryId,
      )
    }
  }

  fun showWalletPickerForBlock(blockId: String) {
    if (_uiState.value.isPastTrip) return
    val currentLink = _uiState.value.blocks.find { it.id == blockId }?.walletEntryId
    dialogState.update {
      it.copy(
        showWalletLinkDialog = true,
        walletLinkTargetBlockId = blockId,
        pendingWalletEntryId = currentLink,
      )
    }
  }

  fun dismissWalletLinkDialog() = dialogState.update {
    it.copy(
      showWalletLinkDialog = false,
      walletLinkTargetBlockId = null,
      pendingWalletEntryId = null,
    )
  }

  fun onPendingWalletEntrySelected(entryId: String?) =
    dialogState.update { it.copy(pendingWalletEntryId = entryId) }

  fun confirmWalletLink() {
    if (_uiState.value.isPastTrip) return
    val dialog = dialogState.value
    val entryId = dialog.pendingWalletEntryId
    val targetBlockId = dialog.walletLinkTargetBlockId
    viewModelScope.launch {
      if (targetBlockId == null) {
        dialogState.update {
          it.copy(
            newBlockWalletEntryId = entryId,
            showWalletLinkDialog = false,
            pendingWalletEntryId = null,
          )
        }
      } else {
        val block = _uiState.value.blocks.find { it.id == targetBlockId } ?: return@launch
        itineraryRepository.updateBlock(block.copy(walletEntryId = entryId))
        dismissWalletLinkDialog()
      }
    }
  }

  fun showUpdatePlanSheet(blockId: String) {
    if (_uiState.value.isPastTrip) return
    val state = _uiState.value
    val block = state.blocks.find { it.id == blockId } ?: return
    val wallet = walletEntryFor(block.walletEntryId) ?: return
    dialogState.update {
      it.copy(
        showMoveDayDialog = true,
        moveDayBlockId = blockId,
        moveDaySelectedDayId = block.dayId,
        moveDayTime = block.timeLabel.orEmpty().ifBlank {
          wallet.time?.let(PlanPlacementLogic::formatTime).orEmpty()
        },
      )
    }
  }

  fun showMoveDayDialog(blockId: String) {
    if (_uiState.value.isPastTrip) return
    val state = _uiState.value
    val block = state.blocks.find { it.id == blockId } ?: return
    dialogState.update {
      it.copy(
        showMoveDayDialog = true,
        moveDayBlockId = blockId,
        moveDaySelectedDayId = block.dayId,
        moveDayTime = block.timeLabel.orEmpty(),
      )
    }
  }

  fun dismissMoveDayDialog() = dialogState.update {
    it.copy(
      showMoveDayDialog = false,
      moveDayBlockId = null,
      moveDaySelectedDayId = null,
      moveDayTime = "",
    )
  }

  fun onMoveDaySelected(dayId: String) =
    dialogState.update { it.copy(moveDaySelectedDayId = dayId) }

  fun onMoveDayTimeChange(value: String) =
    dialogState.update { it.copy(moveDayTime = value) }

  fun confirmMoveDay() {
    if (_uiState.value.isPastTrip) return
    val dialog = dialogState.value
    val blockId = dialog.moveDayBlockId ?: return
    val targetDayId = dialog.moveDaySelectedDayId ?: return
    val state = _uiState.value
    val block = state.blocks.find { it.id == blockId } ?: return
    val targetDay = state.tripDays.find { it.id == targetDayId }

    viewModelScope.launch {
      itineraryRepository.moveBlockToDay(
        block = block,
        targetDayId = targetDayId,
        timeLabel = dialog.moveDayTime,
      )
      dismissMoveDayDialog()

      val dayLabel = targetDay?.let { "Día ${it.dayNumber}" } ?: "otro día"
      if (targetDayId != dayId) {
        _events.emit(
          DayHubEvent.Snackbar(
            message = "Actividad movida al $dayLabel. El documento en Wallet no se ha modificado.",
          ),
        )
        _events.emit(DayHubEvent.NavigateToDay(tripId, targetDayId))
      } else {
        _events.emit(
          DayHubEvent.Snackbar(
            message = "Hora actualizada en el plan. El documento en Wallet no se ha modificado.",
          ),
        )
      }
    }
  }

  fun addBlock() {
    val state = _uiState.value
    if (state.isPastTrip) return
    val title = state.newBlockTitle.trim()
    if (title.isBlank()) return
    viewModelScope.launch {
      itineraryRepository.addBlockAtEnd(
        dayId = dayId,
        title = title,
        timeLabel = state.newBlockTime.ifBlank { null },
        currentCount = state.blocks.size,
        walletEntryId = state.newBlockWalletEntryId,
      )
      dismissAddBlock()
    }
  }

  fun moveBlock(fromIndex: Int, toIndex: Int) {
    if (_uiState.value.isPastTrip) return
    val state = _uiState.value
    val blocks = state.blocks.toMutableList()
    if (fromIndex !in blocks.indices || toIndex !in blocks.indices || fromIndex == toIndex) return
    val item = blocks.removeAt(fromIndex)
    blocks.add(toIndex, item)
    applyBlocksOrder(blocks)
  }

  fun applyBlocksOrder(orderedBlocks: List<ItineraryBlock>) {
    if (_uiState.value.isPastTrip) return
    val recalculated = PlanPlacementLogic.recalculateDaySchedule(
      orderedBlocks,
      _uiState.value.walletEntries,
    )
    persistBlocks(recalculated)
  }

  fun updateBlockTime(blockId: String, timeLabel: String) {
    if (_uiState.value.isPastTrip) return
    val state = _uiState.value
    val block = state.blocks.find { it.id == blockId } ?: return
    val wallet = walletEntryFor(block.walletEntryId)
    val day = state.day
    val hadDocumentTime = wallet != null &&
      PlanPlacementLogic.isTimeFixed(block, wallet) &&
      !PlanPlacementDriftLogic.drift(block, wallet, day).hasDrift

    val normalized = timeLabel.trim().ifBlank { null }
    val updated = block.copy(timeLabel = normalized)
    val resorted = DayPlanScheduleLogic.resortAfterTimeEdit(
      blocks = state.blocks.map { if (it.id == blockId) updated else it },
      editedBlockId = blockId,
      walletEntries = state.walletEntries,
    )
    persistBlocks(resorted)

    if (hadDocumentTime) {
      _events.tryEmit(
        DayHubEvent.Snackbar(
          message = "Hora actualizada en el plan. El documento en Wallet no se ha modificado.",
        ),
      )
    }
  }

  fun moveBlockUp(blockId: String) {
    val blocks = _uiState.value.blocks
    val index = blocks.indexOfFirst { it.id == blockId }
    if (index <= 0) return
    moveBlock(index, index - 1)
  }

  fun moveBlockDown(blockId: String) {
    val blocks = _uiState.value.blocks
    val index = blocks.indexOfFirst { it.id == blockId }
    if (index < 0 || index >= blocks.size - 1) return
    moveBlock(index, index + 1)
  }

  private fun persistBlocks(blocks: List<ItineraryBlock>) {
    viewModelScope.launch {
      itineraryRepository.saveOrder(blocks)
    }
  }

  fun walletEntryFor(id: String?): WalletEntry? =
    id?.let { walletId -> _uiState.value.walletEntries.find { it.id == walletId } }

  private data class DialogOverlayState(
    val showAddBlock: Boolean = false,
    val newBlockTitle: String = "",
    val newBlockTime: String = "",
    val newBlockWalletEntryId: String? = null,
    val showWalletLinkDialog: Boolean = false,
    val walletLinkTargetBlockId: String? = null,
    val pendingWalletEntryId: String? = null,
    val showMoveDayDialog: Boolean = false,
    val moveDayBlockId: String? = null,
    val moveDaySelectedDayId: String? = null,
    val moveDayTime: String = "",
  )

  private data class Quad<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
  )
}
