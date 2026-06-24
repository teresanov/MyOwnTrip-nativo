package com.myowntrip.app.ui.features.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MyOwnTripTheme

/**
 * Revisión visual del flow Wallet (Figma `04 · Wallet`) — 12 caps apilados desde Compose.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletFlowReviewScreen(
  onBack: () -> Unit = {},
  modifier: Modifier = Modifier,
) {
  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        title = { Text("Wallet · 12 caps") },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
          }
        },
      )
    },
  ) { padding ->
    Column(
      modifier = Modifier
        .padding(padding)
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(vertical = MOTSpacing.layoutMd),
      verticalArrangement = Arrangement.spacedBy(MOTSpacing.layoutLg),
    ) {
      WalletFlowCap("Cap 1 · Wallet con documentos") {
        WalletScreen(
          trip = previewWalletTrip,
          entries = previewWalletEntries,
          onAddEntry = {},
          onImportEntry = {},
          onEntryClick = {},
          embeddedInTrip = true,
          modifier = Modifier.fillMaxSize(),
        )
      }

      flowDivider()

      WalletFlowCap("Cap 2 · Wallet vacío") {
        WalletScreen(
          trip = previewWalletTrip,
          entries = emptyList(),
          onAddEntry = {},
          onImportEntry = {},
          onEntryClick = {},
          embeddedInTrip = true,
          modifier = Modifier.fillMaxSize(),
        )
      }

      flowDivider()

      WalletFlowCap("Cap 3 · Import · esperando picker") {
        WalletFormPreviewScaffold(title = "Importar documento", modifier = Modifier.fillMaxSize()) {
          WalletAwaitingPickerStep(onManualEntry = {}, onCancel = {})
        }
      }

      flowDivider()

      WalletFlowCap("Cap 4 · Import · analizando") {
        WalletFormPreviewScaffold(title = "Importar documento", modifier = Modifier.fillMaxSize()) {
          WalletParsingStep(onCancel = {})
        }
      }

      flowDivider()

      WalletFlowCap("Cap 5 · Import · revisar vuelo") {
        WalletFormPreviewScaffold(title = "Revisar importación", modifier = Modifier.fillMaxSize()) {
          WalletImportReviewStep(
            state = previewFlightImportState,
            onPickFile = {},
            onViewDocument = { _, _ -> },
            onTitleChange = {},
            onTypeChange = {},
            onShowTypeCorrection = {},
            onSaveOfflineCopyChange = {},
            onConfirm = {},
            onCancel = {},
          )
        }
      }

      flowDivider()

      WalletFlowCap("Cap 6 · Import · revisar hotel") {
        WalletFormPreviewScaffold(title = "Revisar importación", modifier = Modifier.fillMaxSize()) {
          WalletImportReviewStep(
            state = previewHotelImportState,
            onPickFile = {},
            onViewDocument = { _, _ -> },
            onTitleChange = {},
            onTypeChange = {},
            onShowTypeCorrection = {},
            onSaveOfflineCopyChange = {},
            onConfirm = {},
            onCancel = {},
          )
        }
      }

      flowDivider()

      WalletFlowCap("Cap 7 · Confirmar guardado") {
        Box(modifier = Modifier.fillMaxSize()) {
          WalletFormPreviewScaffold(title = "Revisar importación", modifier = Modifier.fillMaxSize()) {
            WalletImportReviewStep(
              state = previewFlightImportState,
              onPickFile = {},
            onViewDocument = { _, _ -> },
              onTitleChange = {},
              onTypeChange = {},
              onShowTypeCorrection = {},
              onSaveOfflineCopyChange = {},
              onConfirm = {},
              onCancel = {},
            )
          }
          WalletConfirmDialog(
            entry = previewConfirmEntry(),
            attachmentFileName = "billete-barcelona.pdf",
            onDismiss = {},
            onSave = {},
          )
        }
      }

      flowDivider()

      WalletFlowCap("Cap 8 · EC-PARSE-FAIL") {
        WalletFormPreviewScaffold(title = "Completar manualmente", modifier = Modifier.fillMaxSize()) {
          WalletParseFailStep(
            state = previewParseFailState,
            tripExpanded = false,
            onTripExpandedChange = {},
            onCreateTrip = {},
            onPickFile = {},
            onViewDocument = { _, _ -> },
            onTripSelected = {},
            onTitleChange = {},
            onTypeChange = {},
            onDateChange = {},
            onTimeChange = {},
            onNotesChange = {},
            onShowNotes = {},
            onSaveOfflineCopyChange = {},
            onConfirm = {},
            onCancel = {},
          )
        }
      }

      flowDivider()

      WalletFlowCap("Cap 9 · Alta manual") {
        WalletFormPreviewScaffold(title = "Añadir a Wallet", modifier = Modifier.fillMaxSize()) {
          WalletManualAddStep(
            state = previewManualFormState,
            tripExpanded = false,
            onTripExpandedChange = {},
            onCreateTrip = {},
            onTripSelected = {},
            onTitleChange = {},
            onTypeChange = {},
            onDateChange = {},
            onTimeChange = {},
            onNotesChange = {},
            onShowNotes = {},
            onSaveOfflineCopyChange = {},
            onConfirm = {},
            onCancel = {},
          )
        }
      }

      flowDivider()

      WalletFlowCap("Cap 10 · Detalle documento") {
        WalletDetailScaffold(
          entry = previewDetailEntry,
          planPlacement = null,
          showDeleteConfirm = false,
          onBack = {},
          onRequestDelete = {},
          onDismissDelete = {},
          onConfirmDelete = {},
          modifier = Modifier.fillMaxSize(),
        )
      }

      flowDivider()

      WalletFlowCap("Cap 11 · Eliminar documento") {
        Box(modifier = Modifier.fillMaxSize()) {
          WalletDetailScaffold(
            entry = previewDetailEntry,
            planPlacement = null,
            showDeleteConfirm = true,
            onBack = {},
            onRequestDelete = {},
            onDismissDelete = {},
            onConfirmDelete = {},
            modifier = Modifier.fillMaxSize(),
          )
        }
      }

      flowDivider()

      WalletFlowCap("Cap 12 · Descartar borrador") {
        Box(modifier = Modifier.fillMaxSize()) {
          WalletFormPreviewScaffold(title = "Revisar importación", modifier = Modifier.fillMaxSize()) {
            WalletImportReviewStep(
              state = previewFlightImportState,
              onPickFile = {},
            onViewDocument = { _, _ -> },
              onTitleChange = {},
              onTypeChange = {},
              onShowTypeCorrection = {},
              onSaveOfflineCopyChange = {},
              onConfirm = {},
              onCancel = {},
            )
          }
          WalletDiscardDialog(
            hasAttachment = true,
            onDismiss = {},
            onConfirmDiscard = {},
          )
        }
      }
    }
  }
}

@Composable
private fun WalletFlowCap(
  label: String,
  content: @Composable () -> Unit,
) {
  Text(
    text = label,
    style = MaterialTheme.typography.labelLarge,
    color = MaterialTheme.colorScheme.primary,
    modifier = Modifier.padding(horizontal = MOTSpacing.screenHorizontal),
  )
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .height(800.dp)
      .padding(horizontal = MOTSpacing.screenHorizontal),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 1.dp,
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.surface),
    ) {
      content()
    }
  }
}

@Composable
private fun flowDivider() {
  HorizontalDivider(modifier = Modifier.padding(horizontal = MOTSpacing.screenHorizontal))
}

@Preview(name = "Wallet flow · 12 caps (Figma 04)", showBackground = true, widthDp = 360, heightDp = 11000)
@Composable
fun WalletFlowReviewPreview() {
  MyOwnTripTheme {
    Surface(color = MaterialTheme.colorScheme.surface) {
      WalletFlowReviewScreen()
    }
  }
}
