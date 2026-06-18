package com.myowntrip.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.myowntrip.app.ui.features.dayhub.DayHubScreen
import com.myowntrip.app.ui.features.expenses.ExpenseFormScreen
import com.myowntrip.app.ui.features.journal.JournalAddScreen
import com.myowntrip.app.ui.features.restaurants.RestaurantDetailScreen
import com.myowntrip.app.ui.features.restaurants.RestaurantFormScreen
import com.myowntrip.app.ui.features.trips.CreateTripScreen
import com.myowntrip.app.ui.features.trips.TripDetailScreen
import com.myowntrip.app.ui.features.trips.HomeFlowReviewScreen
import com.myowntrip.app.ui.features.trips.TripListScreen
import com.myowntrip.app.ui.features.wallet.WalletDetailScreen
import com.myowntrip.app.ui.features.wallet.WalletFormScreen

@Composable
fun AppNavGraph(
  navController: NavHostController = rememberNavController(),
  startDestination: String = Routes.TRIP_LIST,
) {
  NavHost(navController = navController, startDestination = startDestination) {
    composable(Routes.TRIP_LIST) {
      TripListScreen(
        onCreateTrip = { navController.navigate(Routes.TRIP_CREATE) },
        onTripClick = { navController.navigate(Routes.tripDetail(it)) },
      )
    }
    composable(Routes.HOME_FLOW_REVIEW) {
      HomeFlowReviewScreen(onBack = { navController.popBackStack() })
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
      route = Routes.TRIP_DETAIL,
      arguments = listOf(navArgument("tripId") { type = NavType.StringType }),
    ) {
      val tripId = it.arguments?.getString("tripId")!!
      TripDetailScreen(
        tripId = tripId,
        onBack = { navController.popBackStack() },
        onAddWallet = { navController.navigate(Routes.walletAdd(tripId)) },
        onImportWallet = { navController.navigate(Routes.walletAdd(tripId, pickAttachment = true)) },
        onAddExpense = { navController.navigate(Routes.expenseAdd(tripId)) },
        onAddRestaurant = { navController.navigate(Routes.restaurantAdd(tripId)) },
        onWalletEntryClick = { entryId -> navController.navigate(Routes.walletDetail(entryId)) },
        onDayClick = { dayId -> navController.navigate(Routes.dayHub(tripId, dayId)) },
        onRestaurantClick = { restaurantId ->
          navController.navigate(Routes.restaurantDetail(restaurantId))
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
      )
    }
    composable(Routes.WALLET_IMPORT) {
      WalletFormScreen(
        onBack = { navController.navigate(Routes.TRIP_LIST) { popUpTo(Routes.TRIP_LIST) } },
        onSaved = { navController.navigate(Routes.TRIP_LIST) { popUpTo(Routes.TRIP_LIST) } },
        onCreateTrip = { navController.navigate(Routes.TRIP_CREATE) },
      )
    }
    composable(
      route = Routes.WALLET_DETAIL,
      arguments = listOf(navArgument("entryId") { type = NavType.StringType }),
    ) {
      WalletDetailScreen(
        onBack = { navController.popBackStack() },
        onDeleted = { navController.popBackStack() },
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
      )
    }
    composable(
      route = Routes.DAY_HUB,
      arguments = listOf(
        navArgument("tripId") { type = NavType.StringType },
        navArgument("dayId") { type = NavType.StringType },
      ),
    ) {
      val tripId = it.arguments?.getString("tripId")!!
      val dayId = it.arguments?.getString("dayId")!!
      DayHubScreen(
        onBack = { navController.popBackStack() },
        onAddNote = { navController.navigate(Routes.journalAdd(dayId)) },
        onAddExpense = { navController.navigate(Routes.expenseAdd(tripId, dayId)) },
      )
    }
    composable(
      route = Routes.DAY_DETAIL,
      arguments = listOf(
        navArgument("tripId") { type = NavType.StringType },
        navArgument("dayId") { type = NavType.StringType },
      ),
    ) {
      val tripId = it.arguments?.getString("tripId")!!
      val dayId = it.arguments?.getString("dayId")!!
      DayHubScreen(
        onBack = { navController.popBackStack() },
        onAddNote = { navController.navigate(Routes.journalAdd(dayId)) },
        onAddExpense = { navController.navigate(Routes.expenseAdd(tripId, dayId)) },
      )
    }
    composable(
      route = Routes.JOURNAL_ADD,
      arguments = listOf(navArgument("dayId") { type = NavType.StringType }),
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
