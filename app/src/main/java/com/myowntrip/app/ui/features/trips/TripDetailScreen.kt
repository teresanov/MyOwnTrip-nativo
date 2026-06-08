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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.Expense
import com.myowntrip.app.domain.model.WalletEntry
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(
  tripId: String,
  onBack: () -> Unit,
  onAddWallet: () -> Unit,
  onAddExpense: () -> Unit,
  onDayClick: (String) -> Unit,
  viewModel: TripDetailViewModel = hiltViewModel(),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  var tabIndex by remember { mutableIntStateOf(0) }
  val tabs = listOf("Wallet", "Days", "Expenses")

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(state.trip?.name ?: "Trip") },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
      )
    },
    floatingActionButton = {
      when (tabIndex) {
        0 -> FloatingActionButton(
          onClick = onAddWallet,
          modifier = Modifier.semantics { contentDescription = "Add wallet entry" },
        ) { Icon(Icons.Default.Add, contentDescription = null) }
        2 -> FloatingActionButton(
          onClick = onAddExpense,
          modifier = Modifier.semantics { contentDescription = "Add expense" },
        ) { Icon(Icons.Default.Add, contentDescription = null) }
        else -> {}
      }
    },
  ) { padding ->
    Column(modifier = Modifier.padding(padding).fillMaxSize()) {
      TabRow(selectedTabIndex = tabIndex) {
        tabs.forEachIndexed { index, title ->
          Tab(selected = tabIndex == index, onClick = { tabIndex = index }, text = { Text(title) })
        }
      }
      when (tabIndex) {
        0 -> WalletTab(entries = state.walletEntries)
        1 -> DaysTab(days = state.days, onDayClick = onDayClick)
        2 -> ExpensesTab(expenses = state.expenses)
      }
    }
  }
}

@Composable
private fun WalletTab(entries: List<WalletEntry>) {
  if (entries.isEmpty()) {
    BoxText("No wallet entries yet. Add flights, hotels or documents.")
  } else {
    LazyColumn(
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      items(entries, key = { it.id }) { entry ->
        Card(modifier = Modifier.fillMaxWidth()) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(entry.title, style = MaterialTheme.typography.titleSmall)
            Text(entry.type.name, style = MaterialTheme.typography.bodySmall)
            entry.notes?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
          }
        }
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
private fun BoxText(message: String) {
  Column(
    modifier = Modifier.fillMaxSize().padding(24.dp),
    verticalArrangement = Arrangement.Center,
  ) {
    Text(message, style = MaterialTheme.typography.bodyLarge)
  }
}
