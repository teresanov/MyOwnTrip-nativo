package com.myowntrip.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.myowntrip.app.ui.features.dayhub.DayHubScreen
import com.myowntrip.app.ui.features.documents.DocumentViewerScreen
import com.myowntrip.app.ui.features.expenses.ExpenseFormScreen
import com.myowntrip.app.ui.features.journal.JournalAddScreen
import com.myowntrip.app.ui.features.journal.JournalDetailScreen
import com.myowntrip.app.ui.features.restaurants.RestaurantDetailScreen
import com.myowntrip.app.ui.features.restaurants.RestaurantFormScreen
import com.myowntrip.app.ui.features.trips.CreateTripScreen
import com.myowntrip.app.ui.features.trips.EditTripScreen
import com.myowntrip.app.ui.features.trips.TripDetailScreen
import com.myowntrip.app.ui.features.trips.HomeFlowReviewScreen
import com.myowntrip.app.ui.features.trips.TripListScreen
import com.myowntrip.app.ui.features.wallet.WalletDetailScreen
import com.myowntrip.app.ui.features.wallet.WalletFlowReviewScreen
import com.myowntrip.app.ui.features.wallet.WalletFormScreen
import com.myowntrip.app.ui.splash.SplashScreen

@Composable
fun AppNavGraph(
  navController: NavHostController = rememberNavController(),
  startDestination: String = Routes.SPLASH,
) {
  NavHost(navController = navController, startDestination = startDestination) {
    composable(Routes.SPLASH) {
      SplashScreen(
        onDone = {
          navController.navigate(Routes.TRIP_LIST) {
            popUpTo(Routes.SPLASH) { inclusive = true }
          }
        },
      )
    }
    composable(Routes.TRIP_LIST) {
      TripListScreen(
        onCreateTrip = { navController.navigate(Routes.TRIP_CREATE) },
        onTripClick = { navController.navigate(Routes.tripDetail(it)) },
        onImportDocument = { tripId ->
          navController.navigate(Routes.walletAdd(tripId, pickAttachment = true))
        },
        onManualDocument = { tripId ->
          navController.navigate(Routes.walletAdd(tripId, pickAttachment = false))
        },
        onAddJournal = { dayId, tripId ->
          navController.navigate(Routes.journalAdd(dayId, tripId))
        },
      )
    }
    composable(Routes.HOME_FLOW_REVIEW) {
      HomeFlowReviewScreen(onBack = { navController.popBackStack() })
    }
    composable(Routes.WALLET_FLOW_REVIEW) {
      WalletFlowReviewScreen(onBack = { navController.popBackStack() })
    }
    composable(Routes.TRIP_CREATE) {
      CreateTripScreen(
        onBack = { navController.popBackStack() },
        onOpenTrip = { tripId ->
          navController.popBackStack()
          navController.navigate(Routes.tripDetail(tripId))
        },
        onImportDocument = { tripId ->
          navController.navigate(Routes.walletAdd(tripId, pickAttachment = true))
        },
        onAddDocumentManual = { tripId ->
          navController.navigate(Routes.walletAdd(tripId, pickAttachment = false))
        },
      )
    }
    composable(
      route = Routes.TRIP_EDIT,
      arguments = listOf(navArgument("tripId") { type = NavType.StringType }),
    ) {
      EditTripScreen(
        onBack = { navController.popBackStack() },
        onSaved = { navController.popBackStack() },
        onDeleted = {
          navController.popBackStack(Routes.TRIP_LIST, inclusive = false)
        },
      )
    }
    composable(
      route = Routes.TRIP_DETAIL,
      arguments = listOf(
        navArgument("tripId") { type = NavType.StringType },
        navArgument("tab") {
          type = NavType.StringType
          defaultValue = "wallet"
        },
      ),
    ) {
      val tripId = it.arguments?.getString("tripId")!!
      TripDetailScreen(
        tripId = tripId,
        onBack = { navController.popBackStack() },
        onEditTrip = { navController.navigate(Routes.tripEdit(tripId)) },
        onAddWallet = { navController.navigate(Routes.walletAdd(tripId)) },
        onImportWallet = { navController.navigate(Routes.walletAdd(tripId, pickAttachment = true)) },
        onAddJournal = { dayId ->
          navController.navigate(Routes.journalAdd(dayId, tripId))
        },
        onAddExpense = { navController.navigate(Routes.expenseAdd(tripId)) },
        onAddRestaurant = { navController.navigate(Routes.restaurantAdd(tripId)) },
        onWalletEntryClick = { entryId -> navController.navigate(Routes.walletDetail(entryId)) },
        onDayClick = { dayId -> navController.navigate(Routes.dayHub(tripId, dayId)) },
        onDayMemoriesClick = { dayId ->
          navController.navigate(Routes.dayHub(tripId, dayId, tab = "journal"))
        },
        onJournalNoteClick = { noteId ->
          navController.navigate(Routes.journalDetail(noteId))
        },
        onRestaurantClick = { restaurantId ->
          navController.navigate(Routes.restaurantDetail(restaurantId))
        },
        onViewDocument = { source, title ->
          navController.navigate(Routes.documentViewer(source, title))
        },
      )
    }
    composable(
      route = Routes.WALLET_ADD,
      arguments = listOf(
        navArgument("tripId") { type = NavType.StringType },
        navArgument("pickAttachment") {
          type = NavType.BoolType
          defaultValue = false
        },
      ),
    ) {
      WalletFormScreen(
        onBack = { navController.popBackStack() },
        onSaved = { navController.popBackStack() },
        onCreateTrip = {
          navController.navigate(Routes.TRIP_CREATE)
        },
        onViewDocument = { source, title ->
          navController.navigate(Routes.documentViewer(source, title))
        },
      )
    }
    composable(
      route = Routes.WALLET_IMPORT,
      arguments = listOf(
        navArgument("pickAttachment") {
          type = NavType.BoolType
          defaultValue = false
        },
      ),
    ) {
      WalletFormScreen(
        onBack = { navController.navigate(Routes.TRIP_LIST) { popUpTo(Routes.TRIP_LIST) } },
        onSaved = { navController.navigate(Routes.TRIP_LIST) { popUpTo(Routes.TRIP_LIST) } },
        onCreateTrip = { navController.navigate(Routes.TRIP_CREATE) },
        onViewDocument = { source, title ->
          navController.navigate(Routes.documentViewer(source, title))
        },
      )
    }
    composable(
      route = Routes.WALLET_DETAIL,
      arguments = listOf(navArgument("entryId") { type = NavType.StringType }),
    ) {
      WalletDetailScreen(
        onBack = { navController.popBackStack() },
        onDeleted = { navController.popBackStack() },
        onViewDocument = { source, title ->
          navController.navigate(Routes.documentViewer(source, title))
        },
        onOpenPlanDay = { tripId, dayId ->
          navController.navigate(Routes.dayHub(tripId, dayId))
        },
      )
    }
    composable(
      route = Routes.EXPENSE_ADD,
      arguments = listOf(
        navArgument("tripId") { type = NavType.StringType },
        navArgument("dayId") {
          type = NavType.StringType
          nullable = true
          defaultValue = null
        },
      ),
    ) {
      ExpenseFormScreen(
        onBack = { navController.popBackStack() },
        onSaved = { navController.popBackStack() },
        onViewDocument = { source, title ->
          navController.navigate(Routes.documentViewer(source, title))
        },
      )
    }
    composable(
      route = Routes.DOCUMENT_VIEWER,
      arguments = listOf(
        navArgument("source") { type = NavType.StringType },
        navArgument("title") {
          type = NavType.StringType
          defaultValue = ""
        },
      ),
    ) {
      val source = it.arguments?.getString("source").orEmpty()
      val title = it.arguments?.getString("title").orEmpty().ifBlank { null }
      DocumentViewerScreen(
        source = source,
        title = title,
        onBack = { navController.popBackStack() },
      )
    }
    composable(
      route = Routes.DAY_HUB,
      arguments = listOf(
        navArgument("tripId") { type = NavType.StringType },
        navArgument("dayId") { type = NavType.StringType },
        navArgument("tab") {
          type = NavType.StringType
          defaultValue = "plan"
        },
      ),
    ) {
      val tripId = it.arguments?.getString("tripId")!!
      val dayId = it.arguments?.getString("dayId")!!
      DayHubScreen(
        onBack = { navController.popBackStack() },
        onAddNote = { navController.navigate(Routes.journalAdd(dayId)) },
        onNoteClick = { noteId -> navController.navigate(Routes.journalDetail(noteId)) },
        onWalletEntryClick = { entryId -> navController.navigate(Routes.walletDetail(entryId)) },
        onAddWalletDocument = { navController.navigate(Routes.walletAdd(tripId)) },
        onNavigateToDay = { targetTripId, targetDayId ->
          navController.navigate(Routes.dayHub(targetTripId, targetDayId)) {
            popUpTo(Routes.dayHub(tripId, dayId)) { inclusive = true }
          }
        },
      )
    }
    composable(
      route = Routes.DAY_DETAIL,
      arguments = listOf(
        navArgument("tripId") { type = NavType.StringType },
        navArgument("dayId") { type = NavType.StringType },
        navArgument("tab") {
          type = NavType.StringType
          defaultValue = "plan"
        },
      ),
    ) {
      val tripId = it.arguments?.getString("tripId")!!
      val dayId = it.arguments?.getString("dayId")!!
      DayHubScreen(
        onBack = { navController.popBackStack() },
        onAddNote = { navController.navigate(Routes.journalAdd(dayId)) },
        onNoteClick = { noteId -> navController.navigate(Routes.journalDetail(noteId)) },
        onWalletEntryClick = { entryId -> navController.navigate(Routes.walletDetail(entryId)) },
        onAddWalletDocument = { navController.navigate(Routes.walletAdd(tripId)) },
        onNavigateToDay = { targetTripId, targetDayId ->
          navController.navigate(Routes.dayHub(targetTripId, targetDayId)) {
            popUpTo(Routes.dayDetail(tripId, dayId)) { inclusive = true }
          }
        },
      )
    }
    composable(
      route = Routes.JOURNAL_ADD,
      arguments = listOf(
        navArgument("dayId") { type = NavType.StringType },
        navArgument("tripId") {
          type = NavType.StringType
          nullable = true
          defaultValue = null
        },
      ),
    ) {
      val returnTripId = it.arguments?.getString("tripId")
      JournalAddScreen(
        onBack = { navController.popBackStack() },
        onSaved = {
          navController.popBackStack()
          if (!returnTripId.isNullOrBlank()) {
            navController.navigate(Routes.tripDetailJournal(returnTripId)) {
              launchSingleTop = true
            }
          }
        },
      )
    }
    composable(
      route = Routes.JOURNAL_DETAIL,
      arguments = listOf(navArgument("noteId") { type = NavType.StringType }),
    ) {
      JournalDetailScreen(
        onBack = { navController.popBackStack() },
        onEdit = { noteId -> navController.navigate(Routes.journalEdit(noteId)) },
        onDeleted = { navController.popBackStack() },
      )
    }
    composable(
      route = Routes.JOURNAL_EDIT,
      arguments = listOf(
        navArgument("noteId") { type = NavType.StringType },
        navArgument("tripId") {
          type = NavType.StringType
          nullable = true
          defaultValue = null
        },
      ),
    ) {
      JournalAddScreen(
        onBack = { navController.popBackStack() },
        onSaved = { navController.popBackStack() },
      )
    }
    composable(
      route = Routes.RESTAURANT_ADD,
      arguments = listOf(navArgument("tripId") { type = NavType.StringType }),
    ) {
      RestaurantFormScreen(
        onBack = { navController.popBackStack() },
        onSaved = { navController.popBackStack() },
      )
    }
    composable(
      route = Routes.RESTAURANT_DETAIL,
      arguments = listOf(navArgument("restaurantId") { type = NavType.StringType }),
    ) {
      RestaurantDetailScreen(onBack = { navController.popBackStack() })
    }
  }
}
