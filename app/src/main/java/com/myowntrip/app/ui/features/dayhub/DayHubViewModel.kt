package com.myowntrip.app.ui.features.dayhub

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.ItineraryRepository
import com.myowntrip.app.data.repository.JournalRepository
import com.myowntrip.app.data.repository.TripRepository
import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.ItineraryBlock
import com.myowntrip.app.domain.model.JournalNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DayHubUiState(
  val day: Day? = null,
  val blocks: List<ItineraryBlock> = emptyList(),
  val notes: List<JournalNote> = emptyList(),
  val showAddBlock: Boolean = false,
  val newBlockTitle: String = "",
  val newBlockTime: String = "",
)

@HiltViewModel
class DayHubViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  tripRepository: TripRepository,
  private val itineraryRepository: ItineraryRepository,
  journalRepository: JournalRepository,
) : ViewModel() {
  private val dayId: String = checkNotNull(savedStateHandle["dayId"])

  private val _uiState = MutableStateFlow(DayHubUiState())
  val uiState: StateFlow<DayHubUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      combine(
        tripRepository.observeDay(dayId),
        itineraryRepository.observeByDay(dayId),
        journalRepository.observeByDay(dayId),
      ) { day, blocks, notes ->
        DayHubUiState(day = day, blocks = blocks, notes = notes)
      }.collect { merged ->
        _uiState.update { current ->
          merged.copy(
            showAddBlock = current.showAddBlock,
            newBlockTitle = current.newBlockTitle,
            newBlockTime = current.newBlockTime,
          )
        }
      }
    }
  }

  fun showAddBlock() = _uiState.update { it.copy(showAddBlock = true) }
  fun dismissAddBlock() = _uiState.update { it.copy(showAddBlock = false, newBlockTitle = "", newBlockTime = "") }
  fun onNewBlockTitleChange(value: String) = _uiState.update { it.copy(newBlockTitle = value) }
  fun onNewBlockTimeChange(value: String) = _uiState.update { it.copy(newBlockTime = value) }

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
}
