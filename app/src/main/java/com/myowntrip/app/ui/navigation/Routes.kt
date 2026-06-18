package com.myowntrip.app.ui.navigation

object Routes {
  const val TRIP_LIST = "trip_list"
  const val HOME_FLOW_REVIEW = "home_flow_review"
  const val TRIP_CREATE = "trip_create"
  const val TRIP_EDIT = "trip_edit/{tripId}"
  const val TRIP_DETAIL = "trip_detail/{tripId}"
  const val WALLET_ADD = "wallet_add/{tripId}?pickAttachment={pickAttachment}"
  const val WALLET_IMPORT = "wallet_import"
  const val WALLET_DETAIL = "wallet_detail/{entryId}"
  const val EXPENSE_ADD = "expense_add/{tripId}?dayId={dayId}"
  const val DAY_HUB = "day_hub/{tripId}/{dayId}"
  const val DAY_DETAIL = "day_detail/{tripId}/{dayId}"
  const val JOURNAL_ADD = "journal_add/{dayId}"
  const val RESTAURANT_ADD = "restaurant_add/{tripId}"
  const val RESTAURANT_DETAIL = "restaurant_detail/{restaurantId}"

  fun tripDetail(tripId: String) = "trip_detail/$tripId"
  fun tripEdit(tripId: String) = "trip_edit/$tripId"
  fun walletAdd(tripId: String, pickAttachment: Boolean = false) =
    "wallet_add/$tripId?pickAttachment=$pickAttachment"
  fun walletDetail(entryId: String) = "wallet_detail/$entryId"
  fun expenseAdd(tripId: String, dayId: String? = null) =
    if (dayId.isNullOrBlank()) "expense_add/$tripId" else "expense_add/$tripId?dayId=$dayId"
  fun dayHub(tripId: String, dayId: String) = "day_hub/$tripId/$dayId"
  fun dayDetail(tripId: String, dayId: String) = "day_detail/$tripId/$dayId"
  fun journalAdd(dayId: String) = "journal_add/$dayId"
  fun restaurantAdd(tripId: String) = "restaurant_add/$tripId"
  fun restaurantDetail(restaurantId: String) = "restaurant_detail/$restaurantId"
}
