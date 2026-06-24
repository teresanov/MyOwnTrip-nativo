package com.myowntrip.app.ui.features.wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.MOTTextButton

@Composable
fun WalletDiscardDialog(
  onDismiss: () -> Unit,
  onConfirmDiscard: () -> Unit,
  hasAttachment: Boolean,
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    icon = {
      Icon(
        Icons.Default.Error,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.error,
      )
    },
    title = { Text("¿Descartar cambios?") },
    text = {
      Text(
        if (hasAttachment) {
          "Se descartará el archivo seleccionado y los datos detectados. Nada se guardará en Wallet."
        } else {
          "Los cambios no guardados se perderán."
        },
      )
    },
    confirmButton = {
      MOTTextButton(onClick = onConfirmDiscard) {
        Text("Descartar", color = MaterialTheme.colorScheme.error)
      }
    },
    dismissButton = {
      MOTTextButton(onClick = onDismiss) { Text("Seguir editando") }
    },
  )
}

@Composable
fun WalletDuplicateDialog(
  existingTitle: String,
  onDismiss: () -> Unit,
  onDuplicate: () -> Unit,
  onReplace: () -> Unit,
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    icon = {
      Icon(
        Icons.Default.Warning,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.tertiary,
      )
    },
    title = { Text("Documento similar detectado") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Ya tienes «$existingTitle» en este viaje. ¿Qué quieres hacer?")
        MOTTextButton(onClick = onDuplicate, modifier = Modifier.fillMaxWidth()) {
          Icon(Icons.Default.ContentCopy, contentDescription = null)
          Text("Guardar como nuevo")
        }
        MOTTextButton(onClick = onReplace, modifier = Modifier.fillMaxWidth()) {
          Text("Reemplazar existente")
        }
      }
    },
    confirmButton = {},
    dismissButton = {
      MOTTextButton(onClick = onDismiss) { Text("Ignorar") }
    },
  )
}

@Composable
fun WalletDeleteEntryDialog(
  entryTitle: String,
  onDismiss: () -> Unit,
  onConfirmDelete: () -> Unit,
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    icon = {
      Icon(
        Icons.Default.Error,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.error,
      )
    },
    title = { Text("¿Eliminar documento?") },
    text = {
      Text(
        "Se borrará «$entryTitle» y su archivo del dispositivo. " +
          "Si está enlazado al plan, el evento se mantiene sin documento. " +
          "Esta acción no se puede deshacer.",
      )
    },
    confirmButton = {
      MOTTextButton(onClick = onConfirmDelete) {
        Text("Eliminar", color = MaterialTheme.colorScheme.error)
      }
    },
    dismissButton = {
      MOTTextButton(onClick = onDismiss) { Text("Cancelar") }
    },
  )
}

@Composable
fun WalletFormCancelButton(
  label: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  MOTTextButton(onClick = onClick, modifier = modifier.fillMaxWidth()) {
    Text(label)
  }
}
