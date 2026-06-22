package com.myowntrip.app.ui.features.journal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.JournalRepository
import com.myowntrip.app.domain.model.JournalNote
import com.myowntrip.app.platform.media.AudioNotePlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class JournalPlaybackUiState(
  val isPlaying: Boolean = false,
  val progress: Float = 0f,
  val positionMs: Int = 0,
  val durationMs: Int = 0,
  val waveformLevels: List<Float> = emptyList(),
)

@HiltViewModel
class JournalDetailViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val journalRepository: JournalRepository,
  private val audioPlayer: AudioNotePlayer,
) : ViewModel() {
  private val noteId: String = checkNotNull(savedStateHandle["noteId"])

  val note: StateFlow<JournalNote?> = journalRepository.observeNote(noteId)
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

  private val _playback = MutableStateFlow(JournalPlaybackUiState())
  val playback: StateFlow<JournalPlaybackUiState> = _playback.asStateFlow()

  private var progressJob: Job? = null

  fun togglePlayback(audioPath: String) {
    if (audioPlayer.isPlaying() && audioPlayer.currentPath == audioPath) {
      audioPlayer.pause()
      stopProgressPolling(keepWaveform = true)
      updateWaveform(isPlaying = false)
      _playback.update { it.copy(isPlaying = false) }
      return
    }
    if (audioPlayer.currentPath == audioPath && !audioPlayer.isPlaying()) {
      audioPlayer.play(audioPath) { onPlaybackComplete() }
      startProgressPolling()
      _playback.update { it.copy(isPlaying = true) }
      return
    }
    audioPlayer.play(audioPath) { onPlaybackComplete() }
    startProgressPolling()
    _playback.update {
      it.copy(
        isPlaying = true,
        durationMs = audioPlayer.durationMs(),
      )
    }
  }

  fun deleteNote(onDeleted: () -> Unit) {
    viewModelScope.launch {
      stopProgressPolling(keepWaveform = false)
      audioPlayer.stop()
      journalRepository.deleteNote(noteId)
      onDeleted()
    }
  }

  private fun onPlaybackComplete() {
    stopProgressPolling(keepWaveform = false)
    _playback.update {
      it.copy(
        isPlaying = false,
        progress = 0f,
        positionMs = 0,
        waveformLevels = buildPlaybackWaveformLevels(
          isPlaying = false,
          progress = 0f,
          phaseMillis = System.currentTimeMillis(),
        ),
      )
    }
  }

  private fun startProgressPolling() {
    progressJob?.cancel()
    progressJob = viewModelScope.launch {
      while (isActive && audioPlayer.currentPath != null) {
        val duration = audioPlayer.durationMs().coerceAtLeast(1)
        val position = audioPlayer.currentPositionMs()
        val progress = position.toFloat() / duration.toFloat()
        val isPlaying = audioPlayer.isPlaying()
        _playback.update {
          it.copy(
            isPlaying = isPlaying,
            positionMs = position,
            durationMs = duration,
            progress = progress,
            waveformLevels = buildPlaybackWaveformLevels(
              isPlaying = isPlaying,
              progress = progress,
              phaseMillis = System.currentTimeMillis(),
            ),
          )
        }
        if (!isPlaying && position == 0) break
        delay(if (isPlaying) 80 else 200)
      }
    }
  }

  private fun stopProgressPolling(keepWaveform: Boolean) {
    progressJob?.cancel()
    progressJob = null
    if (!keepWaveform) {
      _playback.update { it.copy(waveformLevels = emptyList()) }
    }
  }

  private fun updateWaveform(isPlaying: Boolean) {
    val current = _playback.value
    _playback.update {
      it.copy(
        waveformLevels = buildPlaybackWaveformLevels(
          isPlaying = isPlaying,
          progress = current.progress,
          phaseMillis = System.currentTimeMillis(),
        ),
      )
    }
  }

  override fun onCleared() {
    stopProgressPolling(keepWaveform = false)
    audioPlayer.stop()
    super.onCleared()
  }
}
