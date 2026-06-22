package com.myowntrip.app.ui.features.dayhub

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.myowntrip.app.domain.model.WalletEntry
import com.myowntrip.app.ui.features.wallet.entryTypeLabel
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MOTTextButton

@Composable
fun WalletLinkDialog(
  walletEntries: List<WalletEntry>,
  selectedEntryId: String?,
  onSelectEntry: (String?) -> Unit,
  onDismiss: () -> Unit,
  onConfirm: () -> Unit,
  onAddToWallet: () -> Unit,
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Vincular documento de Wallet") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentXs)) {
        Text(
          text = "Elige un billete, entrada o documento ya guardado en este viaje.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (walletEntries.isEmpty()) {
          Text(
            text = "Aún no hay documentos en Wallet para este viaje.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = MOTSpacing.componentSm),
          )
          MOTTextButton(onClick = onAddToWallet) {
            Text("Añadir documento a Wallet")
          }
        } else {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .heightIn(max = 320.dp)
              .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentXs),
          ) {
            WalletLinkOptionRow(
              label = "Sin documento",
              subtitle = null,
              selected = selectedEntryId == null,
              onClick = { onSelectEntry(null) },
            )
            walletEntries.forEach { entry ->
              WalletLinkOptionRow(
                label = entry.title,
                subtitle = entryTypeLabel(entry.type),
                selected = selectedEntryId == entry.id,
                onClick = { onSelectEntry(entry.id) },
              )
            }
          }
        }
      }
    },
    confirmButton = {
      MOTTextButton(
        onClick = onConfirm,
        enabled = walletEntries.isNotEmpty() || selectedEntryId == null,
      ) {
        Text("Confirmar")
      }
    },
    dismissButton = {
      MOTTextButton(onClick = onDismiss) { Text("Cancelar") }
    },
  )
}

@Composable
private fun WalletLinkOptionRow(
  label: String,
  subtitle: String?,
  selected: Boolean,
  onClick: () -> Unit,
) {
  androidx.compose.foundation.layout.Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .semantics { contentDescription = label }
      .padding(vertical = MOTSpacing.componentXs),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    RadioButton(selected = selected, onClick = onClick)
    Column(modifier = Modifier.padding(start = MOTSpacing.componentSm)) {
      Text(label, style = MaterialTheme.typography.bodyLarge)
      subtitle?.let {
        Text(
          it,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
  }
}
