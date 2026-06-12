package com.myowntrip.app.ui.features.journal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.JournalRepository
import com.myowntrip.app.data.repository.TripRepository
import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.JournalNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class JournalUiState(
  val day: Day? = null,
  val notes: List<JournalNote> = emptyList(),
)

@HiltViewModel
class JournalViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  tripRepository: TripRepository,
  private val journalRepository: JournalRepository,
) : ViewModel() {
  private val dayId: String = checkNotNull(savedStateHandle["dayId"])

  private val _uiState = MutableStateFlow(JournalUiState())
  val uiState: StateFlow<JournalUiState> = _uiState.asStateFlow()

  val notes: StateFlow<List<JournalNote>> = journalRepository.observeByDay(dayId)
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

  init {
    viewModelScope.launch {
      tripRepository.observeDay(dayId).collect { day ->
        _uiState.update { it.copy(day = day) }
      }
    }
    viewModelScope.launch {
      notes.collect { list -> _uiState.update { it.copy(notes = list) } }
    }
  }
}
