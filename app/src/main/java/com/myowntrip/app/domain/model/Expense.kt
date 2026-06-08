package com.myowntrip.app.domain.model

data class Expense(
  val id: String,
  val tripId: String,
  val dayId: String? = null,
  val concept: String,
  val amount: Double,
  val currency: String,
  val category: ExpenseCategory,
  val receiptUri: String? = null,
)
