package com.myowntrip.app.ui.features.expenses

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.ExpenseRepository
import com.myowntrip.app.domain.model.ExpenseCategory
import com.myowntrip.app.platform.media.JournalMediaStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ExpenseFormUiState(
  val tripId: String,
  val dayId: String? = null,
  val amountText: String = "",
  val currency: String = "EUR",
  val concept: String = "",
  val category: ExpenseCategory = ExpenseCategory.FOOD,
  val receiptPath: String? = null,
  val showCamera: Boolean = false,
  val pendingPhotoFile: File? = null,
  val amountError: String? = null,
  val infoMessage: String? = null,
  val isSaving: Boolean = false,
)

@HiltViewModel
class ExpenseFormViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val expenseRepository: ExpenseRepository,
  private val mediaStorage: JournalMediaStorage,
) : ViewModel() {
  private val tripId: String = checkNotNull(savedStateHandle["tripId"])
  private val dayId: String? = savedStateHandle["dayId"]

  private val _uiState = MutableStateFlow(ExpenseFormUiState(tripId = tripId, dayId = dayId))
  val uiState: StateFlow<ExpenseFormUiState> = _uiState.asStateFlow()

  fun onAmountChange(value: String) = _uiState.update { it.copy(amountText = value, amountError = null) }

  fun onConceptChange(value: String) = _uiState.update { it.copy(concept = value) }

  fun onCategoryChange(category: ExpenseCategory) = _uiState.update { it.copy(category = category) }

  fun clearInfoMessage() = _uiState.update { it.copy(infoMessage = null) }

  fun prepareCameraCapture() {
    val file = mediaStorage.createReceiptFile(tripId)
    _uiState.update { it.copy(pendingPhotoFile = file, showCamera = true) }
  }

  fun importReceiptFromUri(uri: Uri) {
    viewModelScope.launch {
      removeReceiptFile(_uiState.value.receiptPath)
      val path = withContext(Dispatchers.IO) { mediaStorage.copyReceiptFromUri(tripId, uri) }
      if (path != null) {
        _uiState.update { it.copy(receiptPath = path, infoMessage = "Ticket adjunto") }
      } else {
        _uiState.update { it.copy(infoMessage = "No se pudo adjuntar la foto del ticket") }
      }
    }
  }

  fun onReceiptCaptured(path: String) {
    removeReceiptFile(_uiState.value.receiptPath)
    _uiState.update {
      it.copy(
        receiptPath = path,
        showCamera = false,
        pendingPhotoFile = null,
        infoMessage = "Ticket adjunto",
      )
    }
  }

  fun cancelCamera() {
    _uiState.value.pendingPhotoFile?.delete()
    _uiState.update { it.copy(showCamera = false, pendingPhotoFile = null) }
  }

  fun removeReceipt() {
    removeReceiptFile(_uiState.value.receiptPath)
    _uiState.update { it.copy(receiptPath = null) }
  }

  fun saveQuick(onSuccess: () -> Unit) {
    val amount = _uiState.value.amountText.replace(',', '.').toDoubleOrNull()
    if (amount == null || amount <= 0) {
      _uiState.update { it.copy(amountError = "Introduce un importe válido") }
      return
    }
    viewModelScope.launch {
      _uiState.update { it.copy(isSaving = true) }
      expenseRepository.addExpense(
        tripId = tripId,
        dayId = _uiState.value.dayId,
        amount = amount,
        currency = _uiState.value.currency,
        concept = _uiState.value.concept,
        category = _uiState.value.category,
        receiptUri = _uiState.value.receiptPath,
      )
      _uiState.update { it.copy(isSaving = false) }
      onSuccess()
    }
  }

  private fun removeReceiptFile(path: String?) {
    path?.let { File(it).delete() }
  }
}
