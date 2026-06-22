package com.myowntrip.app.ui.features.expenses

import com.myowntrip.app.domain.model.ExpenseCategory

fun expenseCategoryLabel(category: ExpenseCategory): String = when (category) {
  ExpenseCategory.FOOD -> "Comida"
  ExpenseCategory.TRANSPORT -> "Transporte"
  ExpenseCategory.ACCOMMODATION -> "Alojamiento"
  ExpenseCategory.ACTIVITY -> "Actividad"
  ExpenseCategory.OTHER -> "Otro"
}
