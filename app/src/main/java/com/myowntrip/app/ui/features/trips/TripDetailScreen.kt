package com.myowntrip.app.ui.features.trips

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Receipt
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Tab
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myowntrip.app.domain.model.Expense
import com.myowntrip.app.domain.model.Restaurant
import com.myowntrip.app.ui.features.expenses.expenseCategoryLabel
import com.myowntrip.app.ui.features.restaurants.statusLabel
import com.myowntrip.app.ui.features.dayhub.WalletLinkDialog
import com.myowntrip.app.ui.features.wallet.WalletScreen
import com.myowntrip.app.ui.features.wallet.previewWalletEntries
import com.myowntrip.app.ui.features.wallet.previewWalletTrip
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MyOwnTripTheme
import java.time.LocalDate
import java.util.Locale
import kotlinx.coroutines.launch

private val tripDetailTabs = listOf("Wallet", "Plan", "Diario", "Gastos", "Sitios")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(
  tripId: String,
  onBack: () -> Unit,
  onEditTrip: () -> Unit,
  onAddWallet: () -> Unit,
  onImportWallet: () -> Unit,
  onAddJournal: (dayId: String) -> Unit,
  onAddExpense: () -> Unit,
  onAddRestaurant: () -> Unit,
  onWalletEntryClick: (String) -> Unit,
  onDayClick: (String) -> Unit,
  onDayMemoriesClick: (String) -> Unit = onDayClick,
  onJournalNoteClick: (String) -> Unit,
  onRestaurantClick: (String) -> Unit,
  onViewDocument: (source: String, title: String?) -> Unit = { _, _ -> },
  viewModel: TripDetailViewModel = hiltViewModel(),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  var tabIndex by remember(viewModel.initialTab) {
    mutableIntStateOf(viewModel.initialTab.index)
  }
  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()
  val today = remember { LocalDate.now() }

  val addJournalForToday: () -> Unit = {
    viewModel.resolveDefaultJournalDayId(today) { dayId ->
      if (dayId != null) {
        onAddJournal(dayId)
      } else {
        scope.launch {
          snackbarHostState.showSnackbar("No hay días en este viaje todavía")
        }
      }
    }
  }

  Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
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
        actions = {
          MOTIconButton(onClick = onEditTrip) {
            Icon(Icons.Default.Edit, contentDescription = "Editar viaje")
          }
        },
      )
    },
    floatingActionButton = {
      when (tabIndex) {
        1 -> if (!state.isPastTrip) {
          FloatingActionButton(
            onClick = {
              val dayId = state.days.firstOrNull()?.id
              if (dayId != null) {
                onDayClick(dayId)
              } else {
                scope.launch {
                  snackbarHostState.showSnackbar("Este viaje aún no tiene días")
                }
              }
            },
            modifier = Modifier.semantics { contentDescription = "Añadir actividad al día" },
          ) { Icon(Icons.Default.Add, contentDescription = null) }
        }
        2 -> FloatingActionButton(
          onClick = addJournalForToday,
          modifier = Modifier.semantics { contentDescription = "Añadir recuerdo" },
        ) { Icon(Icons.Default.Add, contentDescription = null) }
        3 -> FloatingActionButton(
          onClick = onAddExpense,
          modifier = Modifier.semantics { contentDescription = "Añadir gasto" },
        ) { Icon(Icons.Default.Add, contentDescription = null) }
        4 -> FloatingActionButton(
          onClick = onAddRestaurant,
          modifier = Modifier.semantics { contentDescription = "Añadir sitio" },
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
        tripDetailTabs.forEachIndexed { index, title ->
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
        0 -> {
          val archiveEntry: (String) -> Unit = { entryId ->
            viewModel.archiveWalletEntry(entryId) { title ->
              scope.launch {
                val result = snackbarHostState.showSnackbar(
                  message = "«$title» archivado",
                  actionLabel = "Deshacer",
                  duration = SnackbarDuration.Short,
                )
                if (result == SnackbarResult.ActionPerformed) {
                  viewModel.unarchiveWalletEntry(entryId)
                }
              }
            }
          }
          val unarchiveEntry: (String) -> Unit = { entryId ->
            val title = state.walletEntries.find { it.id == entryId }?.title
            viewModel.unarchiveWalletEntry(entryId)
            scope.launch {
              val result = snackbarHostState.showSnackbar(
                message = if (title != null) "«$title» restaurado" else "Documento restaurado",
                actionLabel = "Deshacer",
                duration = SnackbarDuration.Short,
              )
              if (result == SnackbarResult.ActionPerformed) {
                viewModel.archiveWalletEntry(entryId) {}
              }
            }
          }
          WalletScreen(
            trip = state.trip,
            entries = state.walletEntries,
            planBlocks = state.planBlocks,
            days = state.days,
            filterPhase = state.walletFilterPhase,
            onFilterPhaseChange = viewModel::onWalletFilterPhaseChange,
            onAddEntry = onAddWallet,
            onImportEntry = onImportWallet,
            onLoadDebugSamples = viewModel::loadDebugWalletSamples,
            onEntryClick = onWalletEntryClick,
            onArchiveEntry = archiveEntry,
            onUnarchiveEntry = unarchiveEntry,
            onDeleteEntry = { entryId ->
              viewModel.deleteWalletEntry(entryId)
              scope.launch {
                snackbarHostState.showSnackbar("Documento eliminado")
              }
            },
            embeddedInTrip = true,
            isPastTrip = state.isPastTrip,
          )
        }
        1 -> TripPlanTab(
          days = state.days,
          planBlocks = state.planBlocks,
          walletEntries = state.walletEntries,
          isPastTrip = state.isPastTrip,
          onDayClick = onDayClick,
          onDayMemoriesClick = onDayMemoriesClick,
          onViewWalletDocuments = { tabIndex = 0 },
          onLinkWallet = viewModel::showWalletLinkForBlock,
          onWalletEntryClick = onWalletEntryClick,
        )
        2 -> TripJournalTab(
          sections = state.journalSections,
          onDayClick = onDayClick,
          onNoteClick = onJournalNoteClick,
        )
        3 -> ExpensesTab(expenses = state.expenses, onViewDocument = onViewDocument)
        4 -> RestaurantsTab(restaurants = state.restaurants, onRestaurantClick = onRestaurantClick)
      }
    }
  }

  if (state.showWalletLinkDialog) {
    WalletLinkDialog(
      walletEntries = state.walletEntries.filter { !it.isArchived },
      selectedEntryId = state.pendingWalletEntryId,
      onSelectEntry = viewModel::onPendingWalletEntrySelected,
      onDismiss = viewModel::dismissWalletLinkDialog,
      onConfirm = viewModel::confirmWalletLink,
      onAddToWallet = {
        viewModel.dismissWalletLinkDialog()
        onAddWallet()
      },
    )
  }
}

@Composable
private fun ExpensesTab(
  expenses: List<Expense>,
  onViewDocument: (source: String, title: String?) -> Unit,
) {
  if (expenses.isEmpty()) {
    BoxText("Aún no hay gastos.")
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
        val receiptPath = expense.receiptUri
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .then(
              if (receiptPath != null) {
                Modifier.clickable {
                  onViewDocument(
                    receiptPath,
                    expense.concept.ifBlank { "Ticket" },
                  )
                }
              } else {
                Modifier
              },
            ),
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
            ) {
              Text(expense.concept, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
              if (receiptPath != null) {
                Icon(
                  Icons.Default.Receipt,
                  contentDescription = "Ver ticket",
                  modifier = Modifier.size(20.dp),
                  tint = MaterialTheme.colorScheme.tertiary,
                )
              }
            }
            Text(
              "${expense.amount} ${expense.currency} · ${expenseCategoryLabel(expense.category)}",
              style = MaterialTheme.typography.bodyMedium,
            )
            if (receiptPath != null) {
              Text(
                "Toca para ver el ticket",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(top = 4.dp),
              )
            }
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
    BoxText("Aún no hay sitios guardados.")
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
  name = "cap 1 · Trip detail · tab Wallet",
  showBackground = true,
  widthDp = 360,
  heightDp = 800,
)
@Composable
fun TripDetailWalletTabPreview() {
  MyOwnTripTheme {
    Scaffold(
      topBar = {
        TopAppBar(
          title = {
            Text(
              text = previewWalletTrip.name,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
          },
          navigationIcon = {
            MOTIconButton(onClick = {}) {
              Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
              )
            }
          },
        )
      },
    ) { padding ->
      Column(modifier = Modifier.padding(padding).fillMaxSize()) {
        ScrollableTabRow(
          selectedTabIndex = 0,
          edgePadding = 0.dp,
        ) {
          tripDetailTabs.forEachIndexed { index, title ->
            Tab(
              selected = index == 0,
              onClick = {},
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
        WalletScreen(
          trip = previewWalletTrip,
          entries = previewWalletEntries,
          onAddEntry = {},
          onImportEntry = {},
          onEntryClick = {},
          onDeleteEntry = {},
          embeddedInTrip = true,
        )
      }
    }
  }
}
