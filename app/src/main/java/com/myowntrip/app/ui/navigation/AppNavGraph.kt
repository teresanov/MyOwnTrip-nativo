package com.myowntrip.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.myowntrip.app.ui.features.expenses.ExpenseFormScreen
import com.myowntrip.app.ui.features.journal.DayDetailScreen
import com.myowntrip.app.ui.features.trips.CreateTripScreen
import com.myowntrip.app.ui.features.trips.TripDetailScreen
import com.myowntrip.app.ui.features.trips.TripListScreen
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
    composable(Routes.TRIP_CREATE) {
      CreateTripScreen(
        onBack = { navController.popBackStack() },
        onCreated = { tripId ->
          navController.popBackStack()
          navController.navigate(Routes.tripDetail(tripId))
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
        onAddExpense = { navController.navigate(Routes.expenseAdd(tripId)) },
        onDayClick = { dayId -> navController.navigate(Routes.dayDetail(tripId, dayId)) },
      )
    }
    composable(
      route = Routes.WALLET_ADD,
      arguments = listOf(navArgument("tripId") { type = NavType.StringType }),
    ) {
      WalletFormScreen(
        onBack = { navController.popBackStack() },
        onSaved = { navController.popBackStack() },
      )
    }
    composable(Routes.WALLET_IMPORT) {
      WalletFormScreen(
        onBack = { navController.navigate(Routes.TRIP_LIST) { popUpTo(Routes.TRIP_LIST) } },
        onSaved = { navController.navigate(Routes.TRIP_LIST) { popUpTo(Routes.TRIP_LIST) } },
      )
    }
    composable(
      route = Routes.EXPENSE_ADD,
      arguments = listOf(navArgument("tripId") { type = NavType.StringType }),
    ) {
      ExpenseFormScreen(
        onBack = { navController.popBackStack() },
        onSaved = { navController.popBackStack() },
      )
    }
    composable(
      route = Routes.DAY_DETAIL,
      arguments = listOf(
        navArgument("tripId") { type = NavType.StringType },
        navArgument("dayId") { type = NavType.StringType },
      ),
    ) {
      DayDetailScreen(onBack = { navController.popBackStack() })
    }
  }
}
