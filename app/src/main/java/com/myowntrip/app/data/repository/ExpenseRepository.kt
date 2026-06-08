package com.myowntrip.app.data.repository

import com.myowntrip.app.data.local.dao.ExpenseDao
import com.myowntrip.app.data.local.toDomain
import com.myowntrip.app.data.local.toEntity
import com.myowntrip.app.domain.model.Expense
import com.myowntrip.app.domain.model.ExpenseCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepository @Inject constructor(
  private val expenseDao: ExpenseDao,
) {
  fun observeByTrip(tripId: String): Flow<List<Expense>> =
    expenseDao.observeByTrip(tripId).map { list -> list.map { it.toDomain() } }

  fun observeByDay(dayId: String): Flow<List<Expense>> =
    expenseDao.observeByDay(dayId).map { list -> list.map { it.toDomain() } }

  suspend fun addExpense(
    tripId: String,
    dayId: String?,
    amount: Double,
    currency: String = "EUR",
    concept: String = "",
    category: ExpenseCategory = ExpenseCategory.OTHER,
    receiptUri: String? = null,
  ) {
    expenseDao.insert(
      Expense(
        id = UUID.randomUUID().toString(),
        tripId = tripId,
        dayId = dayId,
        concept = concept.ifBlank { category.name },
        amount = amount,
        currency = currency,
        category = category,
        receiptUri = receiptUri,
      ).toEntity(),
    )
  }
}
