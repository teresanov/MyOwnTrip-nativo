package com.myowntrip.app.ui.features.journal.spread

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.ItineraryRepository
import com.myowntrip.app.data.repository.JournalRepository
import com.myowntrip.app.domain.model.ItineraryBlock
import com.myowntrip.app.domain.model.JournalPieceType
import com.myowntrip.app.domain.model.JournalTextStyle
import com.myowntrip.app.platform.location.LocationCapture
import com.myowntrip.app.platform.media.AudioNoteRecorder
import com.myowntrip.app.platform.media.JournalMediaStorage
import com.myowntrip.app.platform.media.StickerShape
import com.myowntrip.app.ui.features.journal.JournalPermission
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class SpreadCaptureStep {
  Idle,
  Caption,
  EventPicker,
  EventCapture,
}

data class SpreadCaptureUiState(
  val step: SpreadCaptureStep = SpreadCaptureStep.Idle,
  val pendingPieceType: JournalPieceType = JournalPieceType.TEXT,
  val textStyle: JournalTextStyle = JournalTextStyle.HAND,
  val stickerShape: StickerShape = StickerShape.CIRCLE,
  val pendingPlanBlockId: String? = null,
  val captionText: String = "",
  val captionError: String? = null,
  val photoPath: String? = null,
  val audioPath: String? = null,
  val isRecording: Boolean = false,
  val recordingLevels: List<Float> = emptyList(),
  val showCamera: Boolean = false,
  val pendingPhotoFile: File? = null,
  val isSaving: Boolean = false,
  val infoMessage: String? = null,
  val permissionMessage: String? = null,
  val dayBlocks: List<ItineraryBlock> = emptyList(),
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SpreadCaptureViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val journalRepository: JournalRepository,
  private val itineraryRepository: ItineraryRepository,
  private val mediaStorage: JournalMediaStorage,
  private val audioRecorder: AudioNoteRecorder,
  private val locationCapture: LocationCapture,
) : ViewModel() {
  // Día destino de la captura. En el Day Hub viene fijado por la ruta; en el Diario
  // del viaje es dinámico (la hoja activa del álbum lo actualiza). Puede ser null hasta
  // que el host resuelve la hoja activa justo antes de guardar.
  private val activeDayId = MutableStateFlow<String?>(savedStateHandle["dayId"])

  private val _uiState = MutableStateFlow(SpreadCaptureUiState())
  val uiState: StateFlow<SpreadCaptureUiState> = _uiState.asStateFlow()

  private var amplitudeJob: Job? = null
  private var pendingAudioPath: String? = null

  /** Hoja activa del álbum donde caerá la pieza. La fija el host antes de guardar. */
  private var targetPageId: String? = null

  /** Día activo (para que el picker de eventos liste los bloques correctos). */
  fun setActiveDay(dayId: String?) {
    activeDayId.value = dayId
  }

  fun setTargetPage(pageId: String?) {
    targetPageId = pageId
  }

  init {
    viewModelScope.launch {
      activeDayId
        .flatMapLatest { id ->
          if (id != null) itineraryRepository.observeByDay(id) else flowOf(emptyList())
        }
        .collect { blocks -> _uiState.update { it.copy(dayBlocks = blocks) } }
    }
  }

  fun openSheet() {
    _uiState.value = SpreadCaptureUiState(dayBlocks = _uiState.value.dayBlocks)
  }

  fun dismissFlow() {
    cancelCamera()
    if (audioRecorder.isRecording) stopRecording()
    discardPendingMedia()
    _uiState.update {
      SpreadCaptureUiState(dayBlocks = it.dayBlocks)
    }
  }

  fun clearMessages() = _uiState.update { it.copy(infoMessage = null, permissionMessage = null) }

  fun onPermissionDenied(kind: JournalPermission) {
    val message = when (kind) {
      JournalPermission.Camera ->
        "Permiso de cámara denegado. Puedes elegir una foto de la galería."
      JournalPermission.Audio ->
        "Permiso de micrófono denegado. La nota de voz no está disponible."
      JournalPermission.Location -> null
    }
    if (message != null) {
      _uiState.update { it.copy(permissionMessage = message) }
    }
  }

  fun startPhotoCapture() {
    discardPendingMedia()
    _uiState.update {
      it.copy(
        pendingPieceType = JournalPieceType.PHOTO,
        step = SpreadCaptureStep.Caption,
        captionText = "",
        captionError = null,
      )
    }
  }

  fun startWriteCapture() {
    discardPendingMedia()
    _uiState.update {
      it.copy(
        pendingPieceType = JournalPieceType.TEXT,
        textStyle = JournalTextStyle.HAND,
        step = SpreadCaptureStep.Caption,
        captionText = "",
        captionError = null,
      )
    }
  }

  fun onTextStyleChange(style: JournalTextStyle) =
    _uiState.update { it.copy(textStyle = style) }

  fun startAudioCapture() {
    discardPendingMedia()
    _uiState.update {
      it.copy(
        pendingPieceType = JournalPieceType.AUDIO,
        step = SpreadCaptureStep.Caption,
        captionText = "",
        captionError = null,
      )
    }
  }

  fun startEventBookmarkCapture() {
    discardPendingMedia()
    _uiState.update {
      it.copy(
        step = SpreadCaptureStep.EventPicker,
        pendingPlanBlockId = null,
      )
    }
  }

  fun startStickerCapture() {
    discardPendingMedia()
    _uiState.update {
      it.copy(
        pendingPieceType = JournalPieceType.STICKER,
        stickerShape = StickerShape.CIRCLE,
        step = SpreadCaptureStep.Caption,
        captionText = "",
        captionError = null,
      )
    }
  }

  fun onStickerShapeChange(shape: StickerShape) =
    _uiState.update { it.copy(stickerShape = shape) }

  fun selectEventBlock(blockId: String) {
    _uiState.update {
      it.copy(
        pendingPlanBlockId = blockId,
        pendingPieceType = JournalPieceType.EVENT_BOOKMARK,
        step = SpreadCaptureStep.EventCapture,
      )
    }
  }

  fun backFromEventCapture() {
    _uiState.update {
      it.copy(step = SpreadCaptureStep.EventPicker, pendingPlanBlockId = null)
    }
  }

  fun onCaptionChange(value: String) =
    _uiState.update { it.copy(captionText = value, captionError = null) }

  fun prepareCameraCapture() {
    val file = mediaStorage.createPhotoFile()
    _uiState.update { it.copy(pendingPhotoFile = file, showCamera = true) }
  }

  fun importPhotoFromUri(uri: Uri) {
    viewModelScope.launch {
      val path = withContext(Dispatchers.IO) { mediaStorage.copyImageFromUri(uri) }
      if (path != null) {
        _uiState.update { it.copy(photoPath = path) }
      } else {
        _uiState.update { it.copy(infoMessage = "No se pudo adjuntar la foto") }
      }
    }
  }

  fun onPhotoCaptured(path: String) {
    _uiState.update {
      it.copy(photoPath = path, showCamera = false, pendingPhotoFile = null)
    }
  }

  fun cancelCamera() {
    _uiState.value.pendingPhotoFile?.delete()
    _uiState.update { it.copy(showCamera = false, pendingPhotoFile = null) }
  }

  fun startRecording() {
    if (audioRecorder.isRecording) return
    pendingAudioPath?.let { File(it).delete() }
    pendingAudioPath = null
    val file = mediaStorage.createAudioFile()
    audioRecorder.start(file)
    _uiState.update { it.copy(isRecording = true, recordingLevels = emptyList()) }
    startAmplitudePolling()
  }

  fun stopRecording() {
    if (!audioRecorder.isRecording) return
    amplitudeJob?.cancel()
    val path = audioRecorder.stop()
    pendingAudioPath = path
    _uiState.update {
      it.copy(
        isRecording = false,
        audioPath = path,
        recordingLevels = emptyList(),
      )
    }
  }

  fun saveCaptionPiece(hasLocationPermission: Boolean, onSuccess: () -> Unit) {
    val state = _uiState.value
    val pieceType = state.pendingPieceType

    when (pieceType) {
      JournalPieceType.TEXT -> {
        if (state.captionText.isBlank()) {
          _uiState.update { it.copy(captionError = "Escribe algo") }
          return
        }
      }
      JournalPieceType.PHOTO, JournalPieceType.STICKER -> {
        if (state.photoPath == null) {
          _uiState.update { it.copy(captionError = "Añade una foto") }
          return
        }
      }
      JournalPieceType.AUDIO -> {
        if (state.audioPath == null) {
          _uiState.update { it.copy(captionError = "Graba una nota de voz") }
          return
        }
      }
      else -> Unit
    }

    if (pieceType == JournalPieceType.STICKER) {
      state.photoPath?.let { saveStickerFromPhoto(it, hasLocationPermission, onSuccess) }
      return
    }

    viewModelScope.launch {
      val dayId = activeDayId.value ?: run {
        _uiState.update { it.copy(captionError = "No se pudo determinar el día") }
        return@launch
      }
      _uiState.update { it.copy(isSaving = true) }
      val location = if (hasLocationPermission) locationCapture.getCurrentLocation() else null
      journalRepository.addNote(
        dayId = dayId,
        text = state.captionText.trim(),
        pieceType = pieceType,
        photoUri = state.photoPath,
        audioUri = state.audioPath,
        planBlockId = state.pendingPlanBlockId,
        latitude = location?.latitude,
        longitude = location?.longitude,
        textStyle = state.textStyle,
        pageId = targetPageId,
      )
      _uiState.update { SpreadCaptureUiState(dayBlocks = state.dayBlocks) }
      onSuccess()
    }
  }

  fun saveEventBookmark(
    captureKind: JournalPieceType,
    hasLocationPermission: Boolean,
    onSuccess: () -> Unit,
  ) {
    val state = _uiState.value
    val planBlockId = state.pendingPlanBlockId ?: return
    when (captureKind) {
      JournalPieceType.TEXT -> {
        if (state.captionText.isBlank()) {
          _uiState.update { it.copy(captionError = "Escribe algo sobre este evento") }
          return
        }
      }
      JournalPieceType.PHOTO -> {
        if (state.photoPath == null) {
          _uiState.update { it.copy(captionError = "Añade una foto del evento") }
          return
        }
      }
      JournalPieceType.AUDIO -> {
        if (state.audioPath == null) {
          _uiState.update { it.copy(captionError = "Graba una nota de voz") }
          return
        }
      }
      else -> Unit
    }

    viewModelScope.launch {
      val dayId = activeDayId.value ?: run {
        _uiState.update { it.copy(captionError = "No se pudo determinar el día") }
        return@launch
      }
      _uiState.update { it.copy(isSaving = true) }
      val location = if (hasLocationPermission) locationCapture.getCurrentLocation() else null
      journalRepository.addNote(
        dayId = dayId,
        text = state.captionText.trim(),
        pieceType = JournalPieceType.EVENT_BOOKMARK,
        photoUri = state.photoPath,
        audioUri = state.audioPath,
        planBlockId = planBlockId,
        latitude = location?.latitude,
        longitude = location?.longitude,
        pageId = targetPageId,
      )
      _uiState.update { SpreadCaptureUiState(dayBlocks = state.dayBlocks) }
      onSuccess()
    }
  }

  fun saveStickerFromPhoto(sourcePath: String, hasLocationPermission: Boolean, onSuccess: () -> Unit) {
    viewModelScope.launch {
      val shape = _uiState.value.stickerShape
      _uiState.update { it.copy(isSaving = true) }
      val stickerPath = withContext(Dispatchers.IO) {
        mediaStorage.maskPhotoAsSticker(sourcePath, shape)
      }
      if (stickerPath == null) {
        _uiState.update { it.copy(isSaving = false, infoMessage = "No se pudo crear el sticker") }
        return@launch
      }
      val dayId = activeDayId.value ?: run {
        _uiState.update { it.copy(isSaving = false, infoMessage = "No se pudo determinar el día") }
        return@launch
      }
      val location = if (hasLocationPermission) locationCapture.getCurrentLocation() else null
      journalRepository.addNote(
        dayId = dayId,
        text = _uiState.value.captionText.trim(),
        pieceType = JournalPieceType.STICKER,
        stickerUri = stickerPath,
        latitude = location?.latitude,
        longitude = location?.longitude,
        pageId = targetPageId,
      )
      _uiState.update { SpreadCaptureUiState(dayBlocks = it.dayBlocks) }
      onSuccess()
    }
  }

  fun saveSketch(path: String, hasLocationPermission: Boolean, onSuccess: () -> Unit) {
    viewModelScope.launch {
      val dayId = activeDayId.value ?: return@launch
      val location = if (hasLocationPermission) locationCapture.getCurrentLocation() else null
      journalRepository.addNote(
        dayId = dayId,
        text = "",
        pieceType = JournalPieceType.SKETCH,
        sketchUri = path,
        latitude = location?.latitude,
        longitude = location?.longitude,
        pageId = targetPageId,
      )
      onSuccess()
    }
  }

  private fun discardPendingMedia() {
    _uiState.value.photoPath?.let { File(it).delete() }
    pendingAudioPath?.let { File(it).delete() }
    pendingAudioPath = null
  }

  private fun startAmplitudePolling() {
    amplitudeJob?.cancel()
    amplitudeJob = viewModelScope.launch {
      while (isActive && audioRecorder.isRecording) {
        val amplitude = audioRecorder.readMaxAmplitude()
        val normalized = (amplitude / 32767f).coerceIn(0.08f, 1f)
        _uiState.update { state ->
          state.copy(recordingLevels = (state.recordingLevels + normalized).takeLast(24))
        }
        delay(75)
      }
    }
  }

  override fun onCleared() {
    amplitudeJob?.cancel()
    if (audioRecorder.isRecording) audioRecorder.stop()
    super.onCleared()
  }
}
