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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTSpacing
import java.time.format.DateTimeFormatter
import java.util.Locale

private val SpanishDateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale("es", "ES"))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletDetailScreen(
  onBack: () -> Unit,
  onDeleted: () -> Unit,
  viewModel: WalletDetailViewModel = hiltViewModel(),
) {
  val entry by viewModel.entry.collectAsStateWithLifecycle()
  var showDeleteConfirm by remember { mutableStateOf(false) }

  Scaffold(
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
            MOTIconButton(onClick = { showDeleteConfirm = true }) {
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
      entry?.let { item ->
        Text(
          entryTypeLabel(item.type),
          style = MaterialTheme.typography.labelLarge,
          color = MaterialTheme.colorScheme.tertiary,
        )
        item.date?.let {
          Text(
            "Fecha: ${it.format(SpanishDateFormatter)}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp),
          )
        }
        item.time?.let {
          Text("Hora: $it", style = MaterialTheme.typography.bodyMedium)
        }
        item.notes?.let {
          Text(it, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 12.dp))
        }
        item.qrPayload?.let { payload ->
          WalletBoardingQrCard(
            payload = payload,
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = MOTSpacing.layoutMd),
          )
        }
        if (item.pdfUri != null) {
          Text(
            "Documento adjunto guardado en el dispositivo para uso sin conexión.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = MOTSpacing.layoutMd),
          )
        }
      } ?: Text("Cargando…")
    }
  }

  if (showDeleteConfirm && entry != null) {
    WalletDeleteEntryDialog(
      entryTitle = entry!!.title,
      onDismiss = { showDeleteConfirm = false },
      onConfirmDelete = {
        showDeleteConfirm = false
        viewModel.deleteEntry(onDeleted)
      },
    )
  }
}
