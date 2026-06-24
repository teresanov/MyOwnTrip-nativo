package com.myowntrip.app.ui.features.dayhub

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.myowntrip.app.ui.features.journal.JournalNoteCard
import com.myowntrip.app.ui.features.plan.AddPlanActivitySheet
import com.myowntrip.app.ui.features.plan.DayPlanSchedule
import com.myowntrip.app.ui.features.plan.MoveBlockDayDialog
import com.myowntrip.app.ui.features.plan.UpdateDocumentPlanSheet
import com.myowntrip.app.ui.theme.MOTIconButton
import java.time.format.DateTimeFormatter
import java.util.Locale

private val SpanishDayTitleFormatter =
  DateTimeFormatter.ofPattern("EEEE d MMM", Locale("es", "ES"))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayHubScreen(
  onBack: () -> Unit,
  onAddNote: () -> Unit,
  onNoteClick: (String) -> Unit,
  onWalletEntryClick: (String) -> Unit,
  onAddWalletDocument: () -> Unit,
  onNavigateToDay: (tripId: String, dayId: String) -> Unit = { _, _ -> },
  viewModel: DayHubViewModel = hiltViewModel(),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  var tabIndex by remember(viewModel.initialTab) {
    mutableIntStateOf(viewModel.initialTab.index)
  }
  val tabs = listOf("Plan", "Diario")
  val isPastTrip = state.isPastTrip
  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(viewModel) {
    viewModel.events.collect { event ->
      when (event) {
        is DayHubEvent.Snackbar -> {
          snackbarHostState.showSnackbar(
            message = event.message,
            actionLabel = event.actionLabel,
            duration = SnackbarDuration.Short,
          )
        }
        is DayHubEvent.NavigateToDay -> onNavigateToDay(event.tripId, event.dayId)
      }
    }
  }

  Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
    topBar = {
      TopAppBar(
        title = {
          val day = state.day
          Text(
            day?.let {
              "Día ${it.dayNumber} · ${it.date.format(SpanishDayTitleFormatter)}"
            } ?: "Día",
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
      when {
        isPastTrip && tabIndex == 0 -> Unit
        tabIndex == 0 -> FloatingActionButton(
          onClick = { viewModel.showAddBlock() },
          modifier = Modifier.semantics { contentDescription = "Añadir actividad" },
        ) { Icon(Icons.Default.Add, contentDescription = null) }
        else -> FloatingActionButton(
          onClick = onAddNote,
          modifier = Modifier.semantics { contentDescription = "Añadir recuerdo" },
        ) { Icon(Icons.Default.Add, contentDescription = null) }
      }
    },
  ) { padding ->
    Column(modifier = Modifier.padding(padding).fillMaxSize()) {
      TabRow(selectedTabIndex = tabIndex) {
        tabs.forEachIndexed { index, title ->
          Tab(
            selected = tabIndex == index,
            onClick = { tabIndex = index },
            text = { Text(title) },
          )
        }
      }
      when (tabIndex) {
        0 -> DayPlanSchedule(
          day = state.day,
          blocks = state.blocks,
          walletEntries = state.walletEntries,
          tripDays = state.tripDays,
          readOnly = isPastTrip,
          onTimeChange = viewModel::updateBlockTime,
          onLinkWallet = viewModel::showWalletPickerForBlock,
          onWalletEntryClick = onWalletEntryClick,
          onMoveToDay = viewModel::showMoveDayDialog,
          onRequestUpdatePlan = viewModel::showUpdatePlanSheet,
        )
        1 -> JournalTab(
          notes = state.notes,
          dayDate = state.day?.date?.toString(),
          onNoteClick = onNoteClick,
        )
      }
    }
  }

  if (state.showAddBlock) {
    AddPlanActivitySheet(
      title = state.newBlockTitle,
      time = state.newBlockTime,
      linkedEntry = viewModel.walletEntryFor(state.newBlockWalletEntryId),
      onTitleChange = viewModel::onNewBlockTitleChange,
      onTimeChange = viewModel::onNewBlockTimeChange,
      onPickWallet = viewModel::showWalletPickerForNewBlock,
      onDismiss = viewModel::dismissAddBlock,
      onConfirm = viewModel::addBlock,
    )
  }

  if (state.showWalletLinkDialog) {
    WalletLinkDialog(
      walletEntries = state.walletEntries,
      selectedEntryId = state.pendingWalletEntryId,
      onSelectEntry = viewModel::onPendingWalletEntrySelected,
      onDismiss = viewModel::dismissWalletLinkDialog,
      onConfirm = viewModel::confirmWalletLink,
      onAddToWallet = {
        viewModel.dismissWalletLinkDialog()
        onAddWalletDocument()
      },
    )
  }

  state.moveDayBlockId?.let { blockId ->
    if (state.showMoveDayDialog) {
      val block = state.blocks.find { it.id == blockId }
      val linkedWallet = viewModel.walletEntryFor(block?.walletEntryId)
      if (block != null && linkedWallet != null) {
        UpdateDocumentPlanSheet(
          blockTitle = block.title,
          linkedWallet = linkedWallet,
          tripDays = state.tripDays,
          selectedDayId = state.moveDaySelectedDayId,
          time = state.moveDayTime,
          onDaySelected = viewModel::onMoveDaySelected,
          onTimeChange = viewModel::onMoveDayTimeChange,
          onDismiss = viewModel::dismissMoveDayDialog,
          onConfirm = viewModel::confirmMoveDay,
        )
      } else if (block != null) {
        MoveBlockDayDialog(
          blockTitle = block.title,
          tripDays = state.tripDays,
          selectedDayId = state.moveDaySelectedDayId,
          time = state.moveDayTime,
          onDaySelected = viewModel::onMoveDaySelected,
          onTimeChange = viewModel::onMoveDayTimeChange,
          onDismiss = viewModel::dismissMoveDayDialog,
          onConfirm = viewModel::confirmMoveDay,
        )
      }
    }
  }
}

@Composable
private fun JournalTab(
  notes: List<com.myowntrip.app.domain.model.JournalNote>,
  dayDate: String?,
  onNoteClick: (String) -> Unit,
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    item {
      dayDate?.let {
        Text(
          it,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
    if (notes.isEmpty()) {
      item {
        Text(
          "Aún no hay recuerdos para este día. Usa + para añadir una nota, foto o audio.",
          style = MaterialTheme.typography.bodyMedium,
          modifier = Modifier.padding(vertical = 8.dp),
        )
      }
    } else {
      items(notes, key = { it.id }) { note ->
        JournalNoteCard(
          note = note,
          onClick = { onNoteClick(note.id) },
        )
      }
    }
  }
}
