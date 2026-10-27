package com.myowntrip.app.ui.features.expenses

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.ExpenseRepository
import com.myowntrip.app.domain.expenses.ReceiptParser
import com.myowntrip.app.domain.model.Expense
import com.myowntrip.app.domain.model.ExpenseCategory
import com.myowntrip.app.platform.media.JournalMediaStorage
import com.myowntrip.app.platform.media.ReceiptTextRecognizer
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
  val tripId: String = "",
  val expenseId: String? = null,
  val dayId: String? = null,
  val amountText: String = "",
  val currency: String = "EUR",
  val concept: String = "",
  val category: ExpenseCategory = ExpenseCategory.FOOD,
  val receiptPath: String? = null,
  val walletEntryId: String? = null,
  val showCamera: Boolean = false,
  val pendingPhotoFile: File? = null,
  val amountError: String? = null,
  val currencyError: String? = null,
  val infoMessage: String? = null,
  val isSaving: Boolean = false,
  val isLoading: Boolean = false,
  val isScanningReceipt: Boolean = false,
) {
  val isEditMode: Boolean get() = expenseId != null
}

/** Monedas de acceso rápido; cualquier otra se escribe a mano (código ISO 4217, ej. MAD). */
val CommonCurrencies = listOf("EUR", "USD", "GBP")

@HiltViewModel
class ExpenseFormViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val expenseRepository: ExpenseRepository,
  private val mediaStorage: JournalMediaStorage,
  private val receiptTextRecognizer: ReceiptTextRecognizer,
) : ViewModel() {
  private val expenseId: String? = savedStateHandle["expenseId"]

  private val _uiState = MutableStateFlow(
    ExpenseFormUiState(
      tripId = (savedStateHandle.get<String>("tripId")).orEmpty(),
      expenseId = expenseId,
      dayId = savedStateHandle["dayId"],
      isLoading = expenseId != null,
    ),
  )
  val uiState: StateFlow<ExpenseFormUiState> = _uiState.asStateFlow()

  init {
    expenseId?.let { id ->
      viewModelScope.launch {
        expenseRepository.getExpense(id)?.let { expense ->
          _uiState.update {
            it.copy(
              tripId = expense.tripId,
              dayId = expense.dayId,
              amountText = formatAmountForEditing(expense.amount),
              currency = expense.currency,
              concept = expense.concept,
              category = expense.category,
              receiptPath = expense.receiptUri,
              walletEntryId = expense.walletEntryId,
              isLoading = false,
            )
          }
        } ?: _uiState.update { it.copy(isLoading = false) }
      }
    }
  }

  fun onAmountChange(value: String) = _uiState.update { it.copy(amountText = value, amountError = null) }

  fun onConceptChange(value: String) = _uiState.update { it.copy(concept = value) }

  fun onCategoryChange(category: ExpenseCategory) = _uiState.update { it.copy(category = category) }

  fun onCurrencyChange(value: String) =
    _uiState.update { it.copy(currency = value.uppercase(), currencyError = null) }

  fun clearInfoMessage() = _uiState.update { it.copy(infoMessage = null) }

  fun prepareCameraCapture() {
    val file = mediaStorage.createReceiptFile(_uiState.value.tripId)
    _uiState.update { it.copy(pendingPhotoFile = file, showCamera = true) }
  }

  fun importReceiptFromUri(uri: Uri) {
    viewModelScope.launch {
      removeReceiptFile(_uiState.value.receiptPath)
      val path = withContext(Dispatchers.IO) { mediaStorage.copyReceiptFromUri(_uiState.value.tripId, uri) }
      if (path != null) {
        _uiState.update { it.copy(receiptPath = path, infoMessage = "Ticket adjunto") }
        scanReceipt(path)
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
    scanReceipt(path)
  }

  /**
   * OCR + heurísticas sobre la foto del ticket (best-effort, sin API key). Solo rellena el
   * importe si el usuario aún no ha escrito nada (para no pisarle un valor ya introducido a
   * mano); la moneda solo se ajusta si sigue en el valor por defecto EUR sin tocar.
   */
  private fun scanReceipt(path: String) {
    viewModelScope.launch {
      _uiState.update { it.copy(isScanningReceipt = true) }
      val text = withContext(Dispatchers.Default) {
        receiptTextRecognizer.recognizeText(File(path))
      }
      val parsed = ReceiptParser.parse(text)
      _uiState.update { state ->
        var next = state.copy(isScanningReceipt = false)
        if (parsed.amount != null && state.amountText.isBlank()) {
          next = next.copy(
            amountText = formatAmountForEditing(parsed.amount),
            amountError = null,
            infoMessage = "Importe detectado del ticket — revísalo antes de guardar",
          )
        }
        if (parsed.currency != null && parsed.currency != state.currency && state.currency == "EUR") {
          next = next.copy(currency = parsed.currency)
        }
        // Solo si sigue en el valor por defecto (FOOD) sin tocar: no pisar una elección manual.
        if (parsed.category != null && state.category == ExpenseCategory.FOOD) {
          next = next.copy(category = parsed.category)
        }
        next
      }
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
    val state = _uiState.value
    val amount = state.amountText.replace(',', '.').toDoubleOrNull()
    if (amount == null || amount <= 0) {
      _uiState.update { it.copy(amountError = "Introduce un importe válido") }
      return
    }
    if (state.currency.isBlank()) {
      _uiState.update { it.copy(currencyError = "Elige o escribe una moneda") }
      return
    }
    viewModelScope.launch {
      _uiState.update { it.copy(isSaving = true) }
      if (state.isEditMode) {
        expenseRepository.updateExpense(
          Expense(
            id = state.expenseId!!,
            tripId = state.tripId,
            dayId = state.dayId,
            concept = state.concept.ifBlank { state.category.name },
            amount = amount,
            currency = state.currency,
            category = state.category,
            receiptUri = state.receiptPath,
            walletEntryId = state.walletEntryId,
          ),
        )
      } else {
        expenseRepository.addExpense(
          tripId = state.tripId,
          dayId = state.dayId,
          amount = amount,
          currency = state.currency,
          concept = state.concept,
          category = state.category,
          receiptUri = state.receiptPath,
        )
      }
      _uiState.update { it.copy(isSaving = false) }
      onSuccess()
    }
  }

  fun delete(onDeleted: () -> Unit) {
    val id = _uiState.value.expenseId ?: return
    viewModelScope.launch {
      expenseRepository.deleteExpense(id)
      removeReceiptFile(_uiState.value.receiptPath)
      onDeleted()
    }
  }

  private fun removeReceiptFile(path: String?) {
    path?.let { File(it).delete() }
  }

  private fun formatAmountForEditing(amount: Double): String =
    if (amount == amount.toLong().toDouble()) amount.toLong().toString() else amount.toString()
}
