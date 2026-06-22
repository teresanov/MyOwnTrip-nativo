package com.myowntrip.app.ui.features.journal

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.myowntrip.app.ui.theme.MOTTextButton

@Composable
fun JournalDeleteNoteDialog(
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
    title = { Text("¿Eliminar recuerdo?") },
    text = {
      Text(
        "Se borrará la nota y los archivos adjuntos del dispositivo. Esta acción no se puede deshacer.",
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
