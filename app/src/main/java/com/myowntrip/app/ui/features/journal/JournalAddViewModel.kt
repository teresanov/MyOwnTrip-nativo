package com.myowntrip.app.ui.features.journal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.JournalRepository
import com.myowntrip.app.platform.location.LocationCapture
import com.myowntrip.app.platform.media.AudioNoteRecorder
import com.myowntrip.app.platform.media.JournalMediaStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class JournalAddUiState(
  val text: String = "",
  val textError: String? = null,
  val photoPath: String? = null,
  val audioPath: String? = null,
  val isRecording: Boolean = false,
  val attachLocation: Boolean = true,
  val isSaving: Boolean = false,
  val showCamera: Boolean = false,
  val pendingPhotoFile: File? = null,
  val infoMessage: String? = null,
  val permissionMessage: String? = null,
)

@HiltViewModel
class JournalAddViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val journalRepository: JournalRepository,
  private val mediaStorage: JournalMediaStorage,
  private val audioRecorder: AudioNoteRecorder,
  private val locationCapture: LocationCapture,
) : ViewModel() {
  private val dayId: String = checkNotNull(savedStateHandle["dayId"])

  private val _uiState = MutableStateFlow(JournalAddUiState())
  val uiState: StateFlow<JournalAddUiState> = _uiState.asStateFlow()

  fun onTextChange(value: String) = _uiState.update { it.copy(text = value, textError = null) }

  fun clearMessages() = _uiState.update { it.copy(infoMessage = null, permissionMessage = null) }

  fun onPermissionDenied(kind: JournalPermission) {
    val message = when (kind) {
      JournalPermission.Camera -> "Camera permission denied. You can still save a text note."
      JournalPermission.Audio -> "Microphone permission denied. Voice note unavailable."
      JournalPermission.Location -> "Location permission denied. Note will save without coordinates."
    }
    _uiState.update { it.copy(permissionMessage = message) }
  }

  fun prepareCameraCapture() {
    val file = mediaStorage.createPhotoFile()
    _uiState.update { it.copy(pendingPhotoFile = file, showCamera = true) }
  }

  fun onPhotoCaptured(path: String) {
    _uiState.update {
      it.copy(
        photoPath = path,
        showCamera = false,
        pendingPhotoFile = null,
        infoMessage = "Photo attached",
      )
    }
  }

  fun cancelCamera() {
    _uiState.value.pendingPhotoFile?.delete()
    _uiState.update { it.copy(showCamera = false, pendingPhotoFile = null) }
  }

  fun removePhoto() {
    _uiState.value.photoPath?.let { File(it).delete() }
    _uiState.update { it.copy(photoPath = null) }
  }

  fun startRecording() {
    if (audioRecorder.isRecording) return
    val file = mediaStorage.createAudioFile()
    audioRecorder.start(file)
    _uiState.update { it.copy(isRecording = true, audioPath = null) }
  }

  fun stopRecording() {
    if (!audioRecorder.isRecording) return
    val path = audioRecorder.stop()
    _uiState.update {
      it.copy(
        isRecording = false,
        audioPath = path,
        infoMessage = if (path != null) "Voice note attached" else "Could not save voice note",
      )
    }
  }

  fun removeAudio() {
    _uiState.value.audioPath?.let { File(it).delete() }
    _uiState.update { it.copy(audioPath = null) }
  }

  fun toggleAttachLocation(enabled: Boolean) {
    _uiState.update { it.copy(attachLocation = enabled) }
  }

  fun saveNote(hasLocationPermission: Boolean, onSuccess: () -> Unit) {
    if (_uiState.value.text.isBlank()) {
      _uiState.update { it.copy(textError = "Write something") }
      return
    }
    if (_uiState.value.isRecording) {
      stopRecording()
    }
    viewModelScope.launch {
      _uiState.update { it.copy(isSaving = true) }
      val location = if (_uiState.value.attachLocation && hasLocationPermission) {
        locationCapture.getCurrentLocation()
      } else {
        null
      }
      if (_uiState.value.attachLocation && !hasLocationPermission) {
        _uiState.update {
          it.copy(infoMessage = "Saved without location (permission not granted)")
        }
      }
      journalRepository.addNote(
        dayId = dayId,
        text = _uiState.value.text.trim(),
        photoUri = _uiState.value.photoPath,
        audioUri = _uiState.value.audioPath,
        latitude = location?.latitude,
        longitude = location?.longitude,
      )
      _uiState.update { it.copy(isSaving = false, text = "") }
      onSuccess()
    }
  }

  override fun onCleared() {
    if (audioRecorder.isRecording) {
      audioRecorder.stop()
    }
    super.onCleared()
  }
}
