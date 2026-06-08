package com.myowntrip.app.ui.navigation

object Routes {
  const val TRIP_LIST = "trip_list"
  const val TRIP_CREATE = "trip_create"
  const val TRIP_DETAIL = "trip_detail/{tripId}"
  const val WALLET_ADD = "wallet_add/{tripId}"
  const val WALLET_IMPORT = "wallet_import"
  const val EXPENSE_ADD = "expense_add/{tripId}"
  const val DAY_DETAIL = "day_detail/{tripId}/{dayId}"
  const val JOURNAL_ADD = "journal_add/{dayId}"

  fun tripDetail(tripId: String) = "trip_detail/$tripId"
  fun walletAdd(tripId: String) = "wallet_add/$tripId"
  fun expenseAdd(tripId: String) = "expense_add/$tripId"
  fun dayDetail(tripId: String, dayId: String) = "day_detail/$tripId/$dayId"
  fun journalAdd(dayId: String) = "journal_add/$dayId"
}
