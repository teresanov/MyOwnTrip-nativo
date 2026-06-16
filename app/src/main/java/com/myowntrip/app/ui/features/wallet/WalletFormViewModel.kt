package com.myowntrip.app.ui.features.wallet

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.TripRepository
import com.myowntrip.app.data.repository.WalletRepository
import com.myowntrip.app.domain.model.EntryType
import com.myowntrip.app.domain.model.Trip
import com.myowntrip.app.domain.model.WalletEntry
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
) {
  val isImportFlow: Boolean
    get() = pickAttachmentOnStart || isImport || attachmentUri != null

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
  tripRepository: TripRepository,
) : ViewModel() {
  private val initialTripId: String? = savedStateHandle["tripId"]
  private val pickAttachmentOnStart: Boolean = savedStateHandle["pickAttachment"] ?: false
  private var importJob: Job? = null

  private val _uiState = MutableStateFlow(
    WalletFormUiState(
      tripId = initialTripId.orEmpty(),
      pickAttachmentOnStart = pickAttachmentOnStart,
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
    importJob?.cancel()
    importJob = viewModelScope.launch {
      _uiState.update {
        it.copy(
          isParsing = true,
          attachmentUri = uri,
          attachmentFileName = fileName,
          showTypeCorrection = false,
          parseFailed = false,
        )
      }
      delay(350)
      ensureActive()
      val parsed = walletRepository.parseDocument(
        uri = uri,
        mimeType = mimeType,
        fileName = fileName,
      )
      _uiState.update {
        it.copy(
          isImport = !parsed.parseFailed,
          isParsing = false,
          parseFailed = parsed.parseFailed,
          type = parsed.type,
          title = parsed.title,
          date = parsed.date,
          time = parsed.time,
          notes = parsed.notes.orEmpty(),
          showNotesField = !parsed.notes.isNullOrBlank(),
          showTypeCorrection = false,
          titleError = null,
          qrPayload = parsed.qrPayload,
        )
      }
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
  fun onTypeChange(type: EntryType) = _uiState.update { it.copy(type = type) }
  fun onTitleChange(value: String) = _uiState.update { it.copy(title = value, titleError = null) }
  fun onDateChange(value: LocalDate?) = _uiState.update { it.copy(date = value) }
  fun onTimeChange(value: LocalTime?) = _uiState.update { it.copy(time = value) }
  fun onNotesChange(value: String) = _uiState.update { it.copy(notes = value) }

  fun setShowTypeCorrection(show: Boolean) = _uiState.update { it.copy(showTypeCorrection = show) }
  fun setShowNotesField(show: Boolean) = _uiState.update { it.copy(showNotesField = show) }

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
      val storedUri = state.attachmentUri?.let { uri ->
        walletRepository.copyAttachmentToTripStorage(
          tripId = state.tripId,
          sourceUri = uri,
          fileName = state.attachmentFileName ?: "attachment",
        )
      }
      val entry = walletRepository.buildEntry(
        tripId = state.tripId,
        type = state.type,
        title = state.title.trim(),
        date = state.date,
        time = state.time,
        fileUri = storedUri,
        linkUrl = null,
        notes = state.notes.ifBlank { null },
        qrPayload = state.qrPayload,
      )
      _uiState.update { it.copy(showConfirm = true, pendingEntry = entry, storedFileUri = storedUri) }
    }
  }

  fun confirmSave(onSuccess: () -> Unit) {
    val entry = _uiState.value.pendingEntry ?: return
    viewModelScope.launch {
      walletRepository.saveEntry(entry)
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
