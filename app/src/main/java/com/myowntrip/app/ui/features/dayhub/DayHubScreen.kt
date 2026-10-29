package com.myowntrip.app.ui.features.dayhub

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myowntrip.app.ui.features.journal.spread.AlbumPageUi
import com.myowntrip.app.ui.features.journal.spread.JournalCaptureFabMenu
import com.myowntrip.app.ui.features.journal.spread.JournalCaptureHost
import com.myowntrip.app.ui.features.journal.spread.ScrapbookAlbum
import com.myowntrip.app.ui.features.journal.spread.SpreadCaptureViewModel
import com.myowntrip.app.ui.features.journal.spread.buildAlbumPages
import com.myowntrip.app.ui.features.plan.AddPlanActivitySheet
import com.myowntrip.app.ui.features.plan.DayPlanSchedule
import com.myowntrip.app.ui.features.plan.MoveBlockDayDialog
import com.myowntrip.app.ui.features.plan.UpdateDocumentPlanSheet
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTTab
import java.time.format.DateTimeFormatter
import java.util.Locale

private val SpanishDayTitleFormatter =
  DateTimeFormatter.ofPattern("EEEE d MMM", Locale("es", "ES"))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayHubScreen(
  onBack: () -> Unit,
  onNoteClick: (String) -> Unit,
  onOpenSketch: () -> Unit,
  onWalletEntryClick: (String) -> Unit,
  onAddWalletDocument: () -> Unit,
  onNavigateToDay: (tripId: String, dayId: String) -> Unit = { _, _ -> },
  viewModel: DayHubViewModel = hiltViewModel(),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  val captureViewModel: SpreadCaptureViewModel = hiltViewModel()
  var tabIndex by rememberSaveable {
    mutableIntStateOf(viewModel.initialTab.index)
  }
  var fabMenuExpanded by rememberSaveable { mutableStateOf(false) }
  var activeAlbumPage by remember { mutableStateOf<AlbumPageUi?>(null) }
  val tabs = listOf("Plan", "Diario")
  val isPastTrip = state.isPastTrip
  val snackbarHostState = remember { SnackbarHostState() }

  BackHandler(fabMenuExpanded) { fabMenuExpanded = false }

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

  JournalCaptureHost(
    onPieceSaved = { fabMenuExpanded = false },
    activePage = activeAlbumPage,
    resolveTarget = viewModel::resolveCaptureTarget,
    captureViewModel = captureViewModel,
  )

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
      if (!isPastTrip && tabIndex == 0) {
        FloatingActionButton(
          onClick = { viewModel.showAddBlock() },
          modifier = Modifier.semantics { contentDescription = "Añadir actividad" },
        ) { Icon(Icons.Default.Add, contentDescription = null) }
      }
    },
  ) { padding ->
    Box(modifier = Modifier.padding(padding).fillMaxSize()) {
      Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = tabIndex) {
          tabs.forEachIndexed { index, title ->
            MOTTab(
              selected = tabIndex == index,
              onClick = { tabIndex = index },
              label = title,
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
          1 -> {
            val albumPages = remember(state.day, state.journalPages, state.notes) {
              state.day?.let { day ->
                buildAlbumPages(days = listOf(day), pages = state.journalPages, notes = state.notes)
              } ?: emptyList()
            }
            ScrapbookAlbum(
              pages = albumPages,
              readOnly = false,
              onPersistTransform = viewModel::persistPieceTransform,
              onPieceClick = onNoteClick,
              onAddSheet = { viewModel.addJournalSheet() },
              onActivePageChange = { activeAlbumPage = it },
            )
          }
        }
      }

      // El Diario se edita también en viajes pasados: el álbum se completa al volver a casa.
      if (tabIndex == 1) {
        JournalCaptureFabMenu(
          expanded = fabMenuExpanded,
          onExpandedChange = { fabMenuExpanded = it },
          onPhoto = { fabMenuExpanded = false; captureViewModel.startPhotoCapture() },
          onText = { fabMenuExpanded = false; captureViewModel.startWriteCapture() },
          onAudio = { fabMenuExpanded = false; captureViewModel.startAudioCapture() },
          onSticker = { fabMenuExpanded = false; captureViewModel.startStickerCapture() },
          onSketch = { fabMenuExpanded = false; onOpenSketch() },
          modifier = Modifier.align(Alignment.BottomEnd),
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
