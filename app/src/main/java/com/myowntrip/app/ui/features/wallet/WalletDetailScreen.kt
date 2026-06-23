package com.myowntrip.app.ui.features.wallet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myowntrip.app.domain.model.WalletEntry
import com.myowntrip.app.domain.wallet.isAvailableOffline
import com.myowntrip.app.domain.wallet.isCloudOnly
import com.myowntrip.app.domain.wallet.offlineAvailability
import com.myowntrip.app.ui.components.DocumentAttachmentCard
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MyOwnTripTheme
import java.time.format.DateTimeFormatter
import java.util.Locale

private val SpanishDateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale("es", "ES"))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletDetailScreen(
  onBack: () -> Unit,
  onDeleted: () -> Unit,
  onViewDocument: (source: String, title: String?) -> Unit = { _, _ -> },
  viewModel: WalletDetailViewModel = hiltViewModel(),
) {
  val entry by viewModel.entry.collectAsStateWithLifecycle()
  var showDeleteConfirm by remember { mutableStateOf(false) }

  WalletDetailScaffold(
    entry = entry,
    showDeleteConfirm = showDeleteConfirm,
    onBack = onBack,
    onRequestDelete = { showDeleteConfirm = true },
    onDismissDelete = { showDeleteConfirm = false },
    onConfirmDelete = {
      showDeleteConfirm = false
      viewModel.deleteEntry(onDeleted)
    },
    onViewDocument = onViewDocument,
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WalletDetailScaffold(
  entry: WalletEntry?,
  showDeleteConfirm: Boolean,
  onBack: () -> Unit,
  onRequestDelete: () -> Unit,
  onDismissDelete: () -> Unit,
  onConfirmDelete: () -> Unit,
  onViewDocument: (source: String, title: String?) -> Unit = { _, _ -> },
  modifier: Modifier = Modifier,
) {
  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        title = { Text(entry?.title ?: "Documento") },
        navigationIcon = {
          MOTIconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
          }
        },
        actions = {
          if (entry != null) {
            MOTIconButton(onClick = onRequestDelete) {
              Icon(Icons.Default.Delete, contentDescription = "Eliminar documento")
            }
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
        .padding(
          start = MOTSpacing.screenHorizontal,
          end = MOTSpacing.screenHorizontal,
          top = MOTSpacing.screenHorizontal,
          bottom = MOTSpacing.screenContentBottom,
        ),
    ) {
      if (entry != null) {
        WalletDetailContent(entry = entry, onViewDocument = onViewDocument)
      } else {
        Text("Cargando…")
      }
    }
  }

  if (showDeleteConfirm && entry != null) {
    WalletDeleteEntryDialog(
      entryTitle = entry.title,
      onDismiss = onDismissDelete,
      onConfirmDelete = onConfirmDelete,
    )
  }
}

@Composable
internal fun WalletDetailContent(
  entry: WalletEntry,
  onViewDocument: (source: String, title: String?) -> Unit = { _, _ -> },
) {
  val offline = remember(entry.id, entry.pdfUri, entry.qrPayload, entry.linkUrl) { entry.offlineAvailability() }
  Text(
    entryTypeLabel(entry.type),
    style = MaterialTheme.typography.labelLarge,
    color = MaterialTheme.colorScheme.tertiary,
  )
  if (offline.isAvailableOffline() || offline.isCloudOnly()) {
    WalletOfflineIndicator(
      availability = offline,
      modifier = Modifier.padding(top = 8.dp),
    )
  }
  entry.date?.let {
    Text(
      "Fecha: ${it.format(SpanishDateFormatter)}",
      style = MaterialTheme.typography.bodyMedium,
      modifier = Modifier.padding(top = 8.dp),
    )
  }
  entry.time?.let {
    Text("Hora: $it", style = MaterialTheme.typography.bodyMedium)
  }
  entry.notes?.let {
    Text(it, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 12.dp))
  }
  entry.qrPayload?.let { payload ->
    WalletQrCard(
      payload = payload,
      entryType = entry.type,
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = MOTSpacing.layoutMd),
    )
  }
  entry.pdfUri?.let { uri ->
    DocumentAttachmentCard(
      source = uri,
      fileName = com.myowntrip.app.platform.documents.fileNameFromSource(uri),
      onOpen = { onViewDocument(uri, entry.title) },
      modifier = Modifier.padding(top = MOTSpacing.layoutMd),
    )
  }
  entry.linkUrl?.takeIf { entry.pdfUri == null }?.let { uri ->
    DocumentAttachmentCard(
      source = uri,
      fileName = com.myowntrip.app.platform.documents.fileNameFromSource(uri),
      onOpen = { onViewDocument(uri, entry.title) },
      modifier = Modifier.padding(top = MOTSpacing.layoutMd),
    )
  }
}

@Preview(name = "cap 10 · Detalle documento", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun WalletDetailPreview() {
  MyOwnTripTheme {
    WalletDetailScaffold(
      entry = previewDetailEntry,
      showDeleteConfirm = false,
      onBack = {},
      onRequestDelete = {},
      onDismissDelete = {},
      onConfirmDelete = {},
    )
  }
}

@Preview(name = "cap 11 · Eliminar documento", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun WalletDeleteDialogPreview() {
  MyOwnTripTheme {
    WalletDetailScaffold(
      entry = previewDetailEntry,
      showDeleteConfirm = true,
      onBack = {},
      onRequestDelete = {},
      onDismissDelete = {},
      onConfirmDelete = {},
    )
  }
}
