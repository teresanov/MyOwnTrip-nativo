package com.myowntrip.app.ui.features.trips

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.Expense
import com.myowntrip.app.domain.model.Restaurant
import com.myowntrip.app.ui.features.restaurants.statusLabel
import com.myowntrip.app.ui.features.wallet.WalletScreen
import com.myowntrip.app.ui.theme.MOTIconButton
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(
  tripId: String,
  onBack: () -> Unit,
  onAddWallet: () -> Unit,
  onImportWallet: () -> Unit,
  onAddExpense: () -> Unit,
  onAddRestaurant: () -> Unit,
  onWalletEntryClick: (String) -> Unit,
  onDayClick: (String) -> Unit,
  onRestaurantClick: (String) -> Unit,
  viewModel: TripDetailViewModel = hiltViewModel(),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  var tabIndex by remember { mutableIntStateOf(0) }
  val tabs = listOf("Wallet", "Días", "Gastos", "Sitios")

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = state.trip?.name ?: "Viaje",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        },
        navigationIcon = {
          MOTIconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
          }
        },
      )
    },
    floatingActionButton = {
      when (tabIndex) {
        2 -> FloatingActionButton(
          onClick = onAddExpense,
          modifier = Modifier.semantics { contentDescription = "Add expense" },
        ) { Icon(Icons.Default.Add, contentDescription = null) }
        3 -> FloatingActionButton(
          onClick = onAddRestaurant,
          modifier = Modifier.semantics { contentDescription = "Add restaurant" },
        ) { Icon(Icons.Default.Add, contentDescription = null) }
        else -> {}
      }
    },
  ) { padding ->
    Column(modifier = Modifier.padding(padding).fillMaxSize()) {
      ScrollableTabRow(
        selectedTabIndex = tabIndex,
        edgePadding = 0.dp,
      ) {
        tabs.forEachIndexed { index, title ->
          Tab(
            selected = tabIndex == index,
            onClick = { tabIndex = index },
            text = {
              Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
              )
            },
          )
        }
      }
      when (tabIndex) {
        0 -> WalletScreen(
          trip = state.trip,
          entries = state.walletEntries,
          onAddEntry = onAddWallet,
          onImportEntry = onImportWallet,
          onEntryClick = onWalletEntryClick,
          onDeleteEntry = viewModel::deleteWalletEntry,
          embeddedInTrip = true,
        )
        1 -> DaysTab(days = state.days, onDayClick = onDayClick)
        2 -> ExpensesTab(expenses = state.expenses)
        3 -> RestaurantsTab(restaurants = state.restaurants, onRestaurantClick = onRestaurantClick)
      }
    }
  }
}

@Composable
private fun DaysTab(days: List<Day>, onDayClick: (String) -> Unit) {
  LazyColumn(
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    items(days, key = { it.id }) { day ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onDayClick(day.id) },
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("Day ${day.dayNumber}", style = MaterialTheme.typography.titleSmall)
          Text(day.date.toString(), style = MaterialTheme.typography.bodyMedium)
        }
      }
    }
  }
}

@Composable
private fun ExpensesTab(expenses: List<Expense>) {
  if (expenses.isEmpty()) {
    BoxText("No expenses yet.")
  } else {
    val total = expenses.sumOf { it.amount }
    LazyColumn(
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      item {
        Text(
          "Total: ${String.format(Locale.getDefault(), "%.2f", total)} ${expenses.firstOrNull()?.currency ?: "EUR"}",
          style = MaterialTheme.typography.titleMedium,
          modifier = Modifier.padding(bottom = 8.dp),
        )
      }
      items(expenses, key = { it.id }) { expense ->
        Card(modifier = Modifier.fillMaxWidth()) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(expense.concept, style = MaterialTheme.typography.titleSmall)
            Text(
              "${expense.amount} ${expense.currency} · ${expense.category.name}",
              style = MaterialTheme.typography.bodyMedium,
            )
          }
        }
      }
    }
  }
}

@Composable
private fun RestaurantsTab(
  restaurants: List<Restaurant>,
  onRestaurantClick: (String) -> Unit,
) {
  if (restaurants.isEmpty()) {
    BoxText("No restaurants yet. Save places you want to visit.")
  } else {
    LazyColumn(
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      items(restaurants, key = { it.id }) { restaurant ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onRestaurantClick(restaurant.id) },
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(restaurant.name, style = MaterialTheme.typography.titleSmall)
            Text(
              statusLabel(restaurant.status),
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.tertiary,
            )
          }
        }
      }
    }
  }
}

@Composable
private fun BoxText(message: String) {
  Column(
    modifier = Modifier.fillMaxSize().padding(24.dp),
    verticalArrangement = Arrangement.Center,
  ) {
    Text(message, style = MaterialTheme.typography.bodyLarge)
  }
}
