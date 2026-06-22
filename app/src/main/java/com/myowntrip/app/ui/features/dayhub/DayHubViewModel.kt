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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DayHubUiState(
  val day: Day? = null,
  val blocks: List<ItineraryBlock> = emptyList(),
  val notes: List<JournalNote> = emptyList(),
  val walletEntries: List<WalletEntry> = emptyList(),
  val showAddBlock: Boolean = false,
  val newBlockTitle: String = "",
  val newBlockTime: String = "",
  val newBlockWalletEntryId: String? = null,
  val showWalletLinkDialog: Boolean = false,
  val walletLinkTargetBlockId: String? = null,
  val pendingWalletEntryId: String? = null,
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

  private val dialogState = MutableStateFlow(DialogOverlayState())

  private val _uiState = MutableStateFlow(DayHubUiState())
  val uiState: StateFlow<DayHubUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      combine(
        tripRepository.observeDay(dayId),
        itineraryRepository.observeByDay(dayId),
        journalRepository.observeByDay(dayId),
        walletRepository.observeByTrip(tripId),
        dialogState,
      ) { day, blocks, notes, walletEntries, dialog ->
        DayHubUiState(
          day = day,
          blocks = blocks,
          notes = notes,
          walletEntries = walletEntries,
          showAddBlock = dialog.showAddBlock,
          newBlockTitle = dialog.newBlockTitle,
          newBlockTime = dialog.newBlockTime,
          newBlockWalletEntryId = dialog.newBlockWalletEntryId,
          showWalletLinkDialog = dialog.showWalletLinkDialog,
          walletLinkTargetBlockId = dialog.walletLinkTargetBlockId,
          pendingWalletEntryId = dialog.pendingWalletEntryId,
        )
      }.collect { merged ->
        _uiState.value = merged
      }
    }
  }

  fun showAddBlock() = dialogState.update {
    it.copy(
      showAddBlock = true,
      newBlockWalletEntryId = null,
    )
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

  fun showWalletPickerForNewBlock() = dialogState.update {
    it.copy(
      showWalletLinkDialog = true,
      walletLinkTargetBlockId = null,
      pendingWalletEntryId = it.newBlockWalletEntryId,
    )
  }

  fun showWalletPickerForBlock(blockId: String) {
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

  fun addBlock() {
    val state = _uiState.value
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

  fun moveBlockUp(index: Int) {
    val blocks = _uiState.value.blocks.toMutableList()
    if (index <= 0 || index >= blocks.size) return
    val item = blocks.removeAt(index)
    blocks.add(index - 1, item)
    persistOrder(blocks)
  }

  fun moveBlockDown(index: Int) {
    val blocks = _uiState.value.blocks.toMutableList()
    if (index < 0 || index >= blocks.size - 1) return
    val item = blocks.removeAt(index)
    blocks.add(index + 1, item)
    persistOrder(blocks)
  }

  private fun persistOrder(blocks: List<ItineraryBlock>) {
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
  )
}
