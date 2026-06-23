package com.myowntrip.app.ui.features.wallet

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.PlanPlacementService
import com.myowntrip.app.data.repository.TripRepository
import com.myowntrip.app.data.repository.WalletRepository
import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.EntryType
import com.myowntrip.app.domain.model.Trip
import com.myowntrip.app.domain.model.WalletEntry
import com.myowntrip.app.domain.plan.PlanPlacementLogic
import com.myowntrip.app.domain.wallet.canChooseCloudStorage
import com.myowntrip.app.domain.wallet.defaultSaveOfflineCopy
import com.myowntrip.app.platform.media.JournalMediaStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

data class WalletFormUiState(
  val tripId: String,
  val trips: List<Trip> = emptyList(),
  val type: EntryType = EntryType.GENERIC,
  val title: String = "",
  val date: LocalDate? = null,
  val time: LocalTime? = null,
  val notes: String = "",
  val attachmentUri: Uri? = null,
  val attachmentFileName: String? = null,
  val storedFileUri: String? = null,
  val titleError: String? = null,
  val showConfirm: Boolean = false,
  val pendingEntry: WalletEntry? = null,
  val saved: Boolean = false,
  val isImport: Boolean = false,
  val pickAttachmentOnStart: Boolean = false,
  val isParsing: Boolean = false,
  val parseFailed: Boolean = false,
  val showTypeCorrection: Boolean = false,
  val showNotesField: Boolean = false,
  val qrPayload: String? = null,
  val manualEntryMode: Boolean = false,
  val showCamera: Boolean = false,
  val pendingPhotoFile: File? = null,
  val showQrScan: Boolean = false,
  val planAddToPlan: Boolean = true,
  val planDayId: String? = null,
  val planTime: LocalTime? = null,
  val planTripDays: List<Day> = emptyList(),
  val planCanPlace: Boolean = false,
  val planSummary: String? = null,
  val saveOfflineCopy: Boolean = true,
  val canChooseStorage: Boolean = false,
) {
  val isImportFlow: Boolean
    get() = !manualEntryMode && (pickAttachmentOnStart || isImport || attachmentUri != null)

  val showTripPicker: Boolean
    get() = trips.isNotEmpty() && tripId.isBlank()

  /** Borrador en curso: importación, adjunto o campos editados sin guardar. */
  val hasDraft: Boolean
    get() = showConfirm || isParsing || attachmentUri != null ||
      title.isNotBlank() || notes.isNotBlank()
}

@HiltViewModel
class WalletFormViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val walletRepository: WalletRepository,
  private val planPlacementService: PlanPlacementService,
  private val tripRepository: TripRepository,
  private val mediaStorage: JournalMediaStorage,
) : ViewModel() {
  private val initialTripId: String? = savedStateHandle["tripId"]
  private val pickAttachmentOnStart: Boolean = savedStateHandle["pickAttachment"] ?: false
  private var importJob: Job? = null

  private val _uiState = MutableStateFlow(
    WalletFormUiState(
      tripId = initialTripId.orEmpty(),
      pickAttachmentOnStart = pickAttachmentOnStart,
      manualEntryMode = !pickAttachmentOnStart,
    ),
  )
  val uiState: StateFlow<WalletFormUiState> = _uiState.asStateFlow()

  val allTrips: StateFlow<List<Trip>> = tripRepository.observeTrips()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

  init {
    viewModelScope.launch {
      allTrips.collect { trips ->
        _uiState.update { state ->
          val tripId = when {
            state.tripId.isNotBlank() -> state.tripId
            trips.size == 1 -> trips.first().id
            else -> state.tripId
          }
          state.copy(trips = trips, tripId = tripId)
        }
      }
    }
  }

  fun setImportData(
    uri: Uri?,
    mimeType: String?,
    fileName: String?,
  ) {
    if (uri == null) return
    attachDocument(uri, mimeType, fileName, fromImport = true)
  }

  fun attachDocument(
    uri: Uri,
    mimeType: String?,
    fileName: String?,
    fromImport: Boolean = false,
  ) {
    importJob?.cancel()
    importJob = viewModelScope.launch {
      _uiState.update {
        it.copy(
          isParsing = true,
          attachmentUri = uri,
          attachmentFileName = fileName,
          showTypeCorrection = false,
          parseFailed = false,
          manualEntryMode = if (fromImport) false else it.manualEntryMode,
        )
      }
      delay(350)
      ensureActive()
      val parsed = walletRepository.parseDocument(
        uri = uri,
        mimeType = mimeType,
        fileName = fileName,
      )
      _uiState.update { state ->
        val manual = state.manualEntryMode
        val resolvedType = if (state.title.isBlank()) parsed.type else state.type
        val canChoose = uri.canChooseCloudStorage()
        state.copy(
          isImport = !manual && !parsed.parseFailed,
          isParsing = false,
          parseFailed = !manual && parsed.parseFailed,
          type = resolvedType,
          title = state.title.ifBlank { parsed.title },
          date = state.date ?: parsed.date,
          time = state.time ?: parsed.time,
          notes = if (state.notes.isBlank()) parsed.notes.orEmpty() else state.notes,
          showNotesField = state.showNotesField || !parsed.notes.isNullOrBlank(),
          showTypeCorrection = false,
          titleError = null,
          qrPayload = parsed.qrPayload ?: state.qrPayload,
          canChooseStorage = canChoose,
          saveOfflineCopy = if (canChoose) resolvedType.defaultSaveOfflineCopy() else true,
        )
      }
    }
  }

  fun prepareCameraCapture() {
    val tripId = _uiState.value.tripId
    if (tripId.isBlank()) {
      _uiState.update { it.copy(titleError = "Selecciona un viaje primero") }
      return
    }
    val file = mediaStorage.createWalletPhotoFile(tripId)
    _uiState.update { it.copy(showCamera = true, pendingPhotoFile = file) }
  }

  fun cancelCamera() {
    _uiState.value.pendingPhotoFile?.delete()
    _uiState.update { it.copy(showCamera = false, pendingPhotoFile = null) }
  }

  fun onPhotoCaptured(path: String) {
    val file = File(path)
    val uri = Uri.fromFile(file)
    _uiState.update { it.copy(showCamera = false, pendingPhotoFile = null) }
    attachDocument(uri, "image/jpeg", file.name)
  }

  fun showQrScanner() {
    _uiState.update { it.copy(showQrScan = true) }
  }

  fun cancelQrScan() {
    _uiState.update { it.copy(showQrScan = false) }
  }

  fun onQrScanned(payload: String) {
    _uiState.update { it.copy(showQrScan = false, qrPayload = payload) }
  }

  fun clearAttachment() {
    _uiState.update {
      it.copy(
        attachmentUri = null,
        attachmentFileName = null,
        isImport = false,
        parseFailed = false,
      )
    }
  }

  fun cancelImport() {
    importJob?.cancel()
    importJob = null
    _uiState.update {
      it.copy(
        isParsing = false,
        isImport = false,
        parseFailed = false,
        pickAttachmentOnStart = false,
        attachmentUri = null,
        attachmentFileName = null,
        title = "",
        type = EntryType.GENERIC,
        date = null,
        time = null,
        notes = "",
        showNotesField = false,
        showTypeCorrection = false,
        qrPayload = null,
        titleError = null,
      )
    }
  }

  fun prepareAbandon() = abandonDraft()

  fun abandonDraft() {
    importJob?.cancel()
    importJob = null
    walletRepository.deleteStoredFile(_uiState.value.storedFileUri)
    val tripId = _uiState.value.tripId
    val trips = _uiState.value.trips
    _uiState.value = WalletFormUiState(tripId = tripId, trips = trips)
  }

  fun onTripSelected(tripId: String) = _uiState.update { it.copy(tripId = tripId) }
  fun onTypeChange(type: EntryType) = _uiState.update { state ->
    state.copy(
      type = type,
      saveOfflineCopy = if (state.canChooseStorage) type.defaultSaveOfflineCopy() else state.saveOfflineCopy,
    )
  }
  fun onTitleChange(value: String) = _uiState.update { it.copy(title = value, titleError = null) }
  fun onDateChange(value: LocalDate?) {
    _uiState.update { state ->
      val next = state.copy(date = value)
      next.withPlanPlacementPreview(next.pendingEntry ?: state.toPendingEntryPreview())
    }
  }

  fun onTimeChange(value: LocalTime?) {
    _uiState.update { state ->
      val next = state.copy(time = value)
      next.withPlanPlacementPreview(next.pendingEntry ?: state.toPendingEntryPreview())
    }
  }

  fun onPlanAddToPlanChange(enabled: Boolean) = _uiState.update { it.copy(planAddToPlan = enabled) }

  fun onPlanDaySelected(dayId: String) = _uiState.update { it.copy(planDayId = dayId) }

  fun onPlanTimeChange(value: LocalTime?) = _uiState.update { it.copy(planTime = value) }

  private fun WalletFormUiState.toPendingEntryPreview(): WalletEntry =
    walletRepository.buildEntry(
      tripId = tripId,
      type = type,
      title = title.trim().ifBlank { "Documento" },
      date = date,
      time = time,
      fileUri = storedFileUri,
      linkUrl = null,
      notes = notes.ifBlank { null },
      qrPayload = qrPayload,
    )

  private fun WalletFormUiState.withPlanPlacement(
    entry: WalletEntry,
    days: List<Day>,
  ): WalletFormUiState {
    val suggestion = PlanPlacementLogic.suggest(entry, days)
    return copy(
      planTripDays = days,
      planCanPlace = suggestion.canPlace,
      planDayId = planDayId ?: suggestion.dayId,
      planTime = planTime ?: suggestion.time ?: entry.time,
      planAddToPlan = if (suggestion.canPlace) planAddToPlan else false,
      planSummary = suggestion.summary(entry.title),
    )
  }

  private fun WalletFormUiState.withPlanPlacementPreview(entry: WalletEntry): WalletFormUiState {
    val suggestion = PlanPlacementLogic.suggest(entry, planTripDays)
    return copy(
      planCanPlace = suggestion.canPlace,
      planDayId = planDayId ?: suggestion.dayId,
      planTime = planTime ?: suggestion.time ?: entry.time,
      planAddToPlan = if (suggestion.canPlace) planAddToPlan else false,
      planSummary = suggestion.summary(entry.title),
    )
  }
  fun onNotesChange(value: String) = _uiState.update { it.copy(notes = value) }

  fun setShowTypeCorrection(show: Boolean) = _uiState.update { it.copy(showTypeCorrection = show) }
  fun setShowNotesField(show: Boolean) = _uiState.update { it.copy(showNotesField = show) }

  fun onSaveOfflineCopyChange(enabled: Boolean) =
    _uiState.update { it.copy(saveOfflineCopy = enabled) }

  fun requestConfirm() {
    val state = _uiState.value
    if (state.tripId.isBlank()) {
      _uiState.update {
        it.copy(
          titleError = if (state.trips.isEmpty()) "Crea un viaje primero" else "Selecciona un viaje",
        )
      }
      return
    }
    if (state.title.isBlank()) {
      _uiState.update { it.copy(titleError = "Obligatorio") }
      return
    }
    viewModelScope.launch {
      val storage = state.attachmentUri?.let { uri ->
        walletRepository.persistWalletDocument(
          tripId = state.tripId,
          sourceUri = uri,
          fileName = state.attachmentFileName ?: "attachment",
          saveOfflineCopy = state.saveOfflineCopy || !state.canChooseStorage,
        )
      }
      val entry = walletRepository.buildEntry(
        tripId = state.tripId,
        type = state.type,
        title = state.title.trim(),
        date = state.date,
        time = state.time,
        fileUri = storage?.pdfUri,
        linkUrl = storage?.linkUrl,
        notes = state.notes.ifBlank { null },
        qrPayload = state.qrPayload,
      )
      val days = tripRepository.getDaysForTrip(state.tripId)
      _uiState.update {
        it.copy(showConfirm = true, pendingEntry = entry, storedFileUri = storage?.pdfUri)
          .withPlanPlacement(entry, days)
      }
    }
  }

  fun confirmSave(onSuccess: () -> Unit) {
    val state = _uiState.value
    val entry = state.pendingEntry ?: return
    viewModelScope.launch {
      walletRepository.saveEntry(entry)
      if (state.planAddToPlan && state.planCanPlace) {
        planPlacementService.apply(
          entry = entry,
          days = state.planTripDays,
          enabled = true,
          dayIdOverride = state.planDayId,
          timeOverride = state.planTime,
        )
      }
      _uiState.update { it.copy(saved = true, showConfirm = false) }
      onSuccess()
    }
  }

  fun clearPickAttachmentOnStart() =
    _uiState.update { it.copy(pickAttachmentOnStart = false) }

  fun switchToManualEntry() {
    _uiState.update {
      it.copy(
        pickAttachmentOnStart = false,
        manualEntryMode = true,
        isImport = false,
        parseFailed = false,
        isParsing = false,
        attachmentUri = null,
        attachmentFileName = null,
        title = "",
        type = EntryType.GENERIC,
        date = null,
        time = null,
        notes = "",
        showNotesField = false,
        titleError = null,
        qrPayload = null,
      )
    }
  }

  fun dismissConfirm() {
    val storedUri = _uiState.value.storedFileUri
    walletRepository.deleteStoredFile(storedUri)
    _uiState.update {
      it.copy(showConfirm = false, pendingEntry = null, storedFileUri = null)
    }
  }
}
