package com.myowntrip.app.ui.navigation

object Routes {
  const val SPLASH = "splash"
  const val TRIP_LIST = "trip_list"
  const val HOME_FLOW_REVIEW = "home_flow_review"
  const val WALLET_FLOW_REVIEW = "wallet_flow_review"
  const val TRIP_CREATE = "trip_create"
  const val TRIP_EDIT = "trip_edit/{tripId}"
  const val TRIP_DETAIL = "trip_detail/{tripId}?tab={tab}"
  const val WALLET_ADD = "wallet_add/{tripId}?pickAttachment={pickAttachment}"
  const val WALLET_IMPORT = "wallet_import?pickAttachment={pickAttachment}"
  const val WALLET_DETAIL = "wallet_detail/{entryId}"
  const val EXPENSE_ADD = "expense_add/{tripId}?dayId={dayId}"
  const val DAY_HUB = "day_hub/{tripId}/{dayId}"
  const val DAY_DETAIL = "day_detail/{tripId}/{dayId}"
  const val JOURNAL_ADD = "journal_add/{dayId}?tripId={tripId}"
  const val JOURNAL_EDIT = "journal_edit/{noteId}?tripId={tripId}"
  const val JOURNAL_DETAIL = "journal_detail/{noteId}"
  const val RESTAURANT_ADD = "restaurant_add/{tripId}"
  const val RESTAURANT_DETAIL = "restaurant_detail/{restaurantId}"
  const val DOCUMENT_VIEWER = "document_viewer?source={source}&title={title}"

  fun tripDetail(tripId: String, tab: String = "wallet") = "trip_detail/$tripId?tab=$tab"
  fun tripDetailJournal(tripId: String) = tripDetail(tripId, tab = "journal")
  fun tripDetailPlan(tripId: String) = tripDetail(tripId, tab = "plan")
  fun tripEdit(tripId: String) = "trip_edit/$tripId"
  fun walletAdd(tripId: String, pickAttachment: Boolean = false) =
    "wallet_add/$tripId?pickAttachment=$pickAttachment"
  fun walletImport(pickAttachment: Boolean = false) =
    "wallet_import?pickAttachment=$pickAttachment"
  fun walletDetail(entryId: String) = "wallet_detail/$entryId"
  fun expenseAdd(tripId: String, dayId: String? = null) =
    if (dayId.isNullOrBlank()) "expense_add/$tripId" else "expense_add/$tripId?dayId=$dayId"
  fun dayHub(tripId: String, dayId: String) = "day_hub/$tripId/$dayId"
  fun dayDetail(tripId: String, dayId: String) = "day_detail/$tripId/$dayId"
  fun journalAdd(dayId: String, tripId: String? = null) =
    if (tripId.isNullOrBlank()) {
      "journal_add/$dayId"
    } else {
      "journal_add/$dayId?tripId=$tripId"
    }
  fun journalEdit(noteId: String, tripId: String? = null) =
    if (tripId.isNullOrBlank()) {
      "journal_edit/$noteId"
    } else {
      "journal_edit/$noteId?tripId=$tripId"
    }
  fun journalDetail(noteId: String) = "journal_detail/$noteId"
  fun restaurantAdd(tripId: String) = "restaurant_add/$tripId"
  fun restaurantDetail(restaurantId: String) = "restaurant_detail/$restaurantId"
  fun documentViewer(source: String, title: String? = null): String {
    val encodedSource = android.net.Uri.encode(source)
    val encodedTitle = android.net.Uri.encode(title.orEmpty())
    return "document_viewer?source=$encodedSource&title=$encodedTitle"
  }
}
