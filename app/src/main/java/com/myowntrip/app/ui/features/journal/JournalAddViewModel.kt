package com.myowntrip.app.ui.features.journal

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.JournalRepository
import com.myowntrip.app.domain.model.JournalNote
import com.myowntrip.app.platform.location.LocationCapture
import com.myowntrip.app.platform.media.AudioNoteRecorder
import com.myowntrip.app.platform.media.JournalMediaStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class JournalAddUiState(
  val text: String = "",
  val textError: String? = null,
  val photoPath: String? = null,
  val audioPath: String? = null,
  val isRecording: Boolean = false,
  val recordingLevels: List<Float> = emptyList(),
  val attachLocation: Boolean = true,
  val isSaving: Boolean = false,
  val isLoading: Boolean = false,
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
  private val dayId: String? = savedStateHandle.get<String>("dayId")
  private val noteId: String? = savedStateHandle.get<String>("noteId")
  private var loadedNote: JournalNote? = null
  private var amplitudeJob: Job? = null

  val isEditMode: Boolean = noteId != null

  private val _uiState = MutableStateFlow(JournalAddUiState(isLoading = noteId != null))
  val uiState: StateFlow<JournalAddUiState> = _uiState.asStateFlow()

  init {
    noteId?.let { id ->
      viewModelScope.launch {
        journalRepository.getNote(id)?.let { note ->
          loadedNote = note
          _uiState.update {
            it.copy(
              text = note.text,
              photoPath = note.photoUri,
              audioPath = note.audioUri,
              attachLocation = note.latitude != null || note.longitude != null,
              isLoading = false,
            )
          }
        } ?: _uiState.update { it.copy(isLoading = false) }
      }
    }
  }

  fun onTextChange(value: String) = _uiState.update { it.copy(text = value, textError = null) }

  fun clearMessages() = _uiState.update { it.copy(infoMessage = null, permissionMessage = null) }

  fun onPermissionDenied(kind: JournalPermission) {
    val message = when (kind) {
      JournalPermission.Camera ->
        "Permiso de cámara denegado. Puedes adjuntar una foto desde la galería o guardar solo texto."
      JournalPermission.Audio ->
        "Permiso de micrófono denegado. La nota de voz no está disponible."
      JournalPermission.Location ->
        "Permiso de ubicación denegado. La nota se guardará sin coordenadas."
    }
    _uiState.update { it.copy(permissionMessage = message) }
  }

  fun prepareCameraCapture() {
    val file = mediaStorage.createPhotoFile()
    _uiState.update { it.copy(pendingPhotoFile = file, showCamera = true) }
  }

  fun importPhotoFromUri(uri: Uri) {
    viewModelScope.launch {
      replacePhoto(_uiState.value.photoPath, null)
      val path = withContext(Dispatchers.IO) { mediaStorage.copyImageFromUri(uri) }
      if (path != null) {
        _uiState.update {
          it.copy(
            photoPath = path,
            infoMessage = "Foto adjunta",
          )
        }
      } else {
        _uiState.update { it.copy(infoMessage = "No se pudo adjuntar la foto") }
      }
    }
  }

  fun onPhotoCaptured(path: String) {
    replacePhoto(_uiState.value.photoPath, path)
    _uiState.update {
      it.copy(
        photoPath = path,
        showCamera = false,
        pendingPhotoFile = null,
        infoMessage = "Foto adjunta",
      )
    }
  }

  fun cancelCamera() {
    _uiState.value.pendingPhotoFile?.delete()
    _uiState.update { it.copy(showCamera = false, pendingPhotoFile = null) }
  }

  fun removePhoto() {
    replacePhoto(_uiState.value.photoPath, null)
    _uiState.update { it.copy(photoPath = null) }
  }

  fun startRecording() {
    if (audioRecorder.isRecording) return
    replaceAudio(_uiState.value.audioPath, null)
    val file = mediaStorage.createAudioFile()
    audioRecorder.start(file)
    _uiState.update { it.copy(isRecording = true, audioPath = null, recordingLevels = emptyList()) }
    startAmplitudePolling()
  }

  fun stopRecording() {
    if (!audioRecorder.isRecording) return
    amplitudeJob?.cancel()
    val path = audioRecorder.stop()
    _uiState.update {
      it.copy(
        isRecording = false,
        audioPath = path,
        recordingLevels = emptyList(),
        infoMessage = if (path != null) {
          "Nota de voz adjunta"
        } else {
          "No se pudo guardar la nota de voz"
        },
      )
    }
  }

  fun removeAudio() {
    replaceAudio(_uiState.value.audioPath, null)
    _uiState.update { it.copy(audioPath = null) }
  }

  fun toggleAttachLocation(enabled: Boolean) {
    _uiState.update { it.copy(attachLocation = enabled) }
  }

  fun saveNote(hasLocationPermission: Boolean, onSuccess: () -> Unit) {
    if (_uiState.value.text.isBlank()) {
      _uiState.update { it.copy(textError = "Escribe algo") }
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
          it.copy(infoMessage = "Guardada sin ubicación (permiso no concedido)")
        }
      }
      val latitude = if (_uiState.value.attachLocation) location?.latitude else null
      val longitude = if (_uiState.value.attachLocation) location?.longitude else null

      if (isEditMode) {
        val existing = loadedNote ?: return@launch
        if (existing.photoUri != null && existing.photoUri != _uiState.value.photoPath) {
          File(existing.photoUri).delete()
        }
        if (existing.audioUri != null && existing.audioUri != _uiState.value.audioPath) {
          File(existing.audioUri).delete()
        }
        journalRepository.updateNote(
          existing.copy(
            text = _uiState.value.text.trim(),
            photoUri = _uiState.value.photoPath,
            audioUri = _uiState.value.audioPath,
            latitude = latitude,
            longitude = longitude,
          ),
        )
      } else {
        val targetDayId = dayId ?: return@launch
        journalRepository.addNote(
          dayId = targetDayId,
          text = _uiState.value.text.trim(),
          photoUri = _uiState.value.photoPath,
          audioUri = _uiState.value.audioPath,
          latitude = latitude,
          longitude = longitude,
        )
      }
      _uiState.update { it.copy(isSaving = false) }
      onSuccess()
    }
  }

  private fun startAmplitudePolling() {
    amplitudeJob?.cancel()
    amplitudeJob = viewModelScope.launch {
      while (isActive && audioRecorder.isRecording) {
        val amplitude = audioRecorder.readMaxAmplitude()
        val normalized = (amplitude / 32767f).coerceIn(0.08f, 1f)
        _uiState.update { state ->
          state.copy(
            recordingLevels = (state.recordingLevels + normalized).takeLast(24),
          )
        }
        delay(75)
      }
    }
  }

  private fun replacePhoto(oldPath: String?, newPath: String?) {
    if (oldPath != null && oldPath != newPath && oldPath != loadedNote?.photoUri) {
      File(oldPath).delete()
    }
  }

  private fun replaceAudio(oldPath: String?, newPath: String?) {
    if (oldPath != null && oldPath != newPath && oldPath != loadedNote?.audioUri) {
      File(oldPath).delete()
    }
  }

  override fun onCleared() {
    amplitudeJob?.cancel()
    if (audioRecorder.isRecording) {
      audioRecorder.stop()
    }
    super.onCleared()
  }
}
