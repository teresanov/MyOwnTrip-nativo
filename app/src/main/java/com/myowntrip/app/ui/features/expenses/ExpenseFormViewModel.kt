package com.myowntrip.app.ui.features.expenses

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.ExpenseRepository
import com.myowntrip.app.domain.model.ExpenseCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExpenseFormUiState(
  val tripId: String,
  val dayId: String? = null,
  val amountText: String = "",
  val currency: String = "EUR",
  val concept: String = "",
  val category: ExpenseCategory = ExpenseCategory.OTHER,
  val amountError: String? = null,
  val saved: Boolean = false,
)

@HiltViewModel
class ExpenseFormViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val expenseRepository: ExpenseRepository,
) : ViewModel() {
  private val tripId: String = checkNotNull(savedStateHandle["tripId"])
  private val dayId: String? = savedStateHandle["dayId"]

  private val _uiState = MutableStateFlow(ExpenseFormUiState(tripId = tripId, dayId = dayId))
  val uiState: StateFlow<ExpenseFormUiState> = _uiState.asStateFlow()

  fun onAmountChange(value: String) = _uiState.update { it.copy(amountText = value, amountError = null) }
  fun onConceptChange(value: String) = _uiState.update { it.copy(concept = value) }
  fun onCategoryChange(category: ExpenseCategory) = _uiState.update { it.copy(category = category) }

  fun saveQuick(onSuccess: () -> Unit) {
    val amount = _uiState.value.amountText.replace(',', '.').toDoubleOrNull()
    if (amount == null || amount <= 0) {
      _uiState.update { it.copy(amountError = "Enter a valid amount") }
      return
    }
    viewModelScope.launch {
      expenseRepository.addExpense(
        tripId = tripId,
        dayId = _uiState.value.dayId,
        amount = amount,
        currency = _uiState.value.currency,
        concept = _uiState.value.concept,
        category = _uiState.value.category,
      )
      _uiState.update { it.copy(saved = true) }
      onSuccess()
    }
  }
}
