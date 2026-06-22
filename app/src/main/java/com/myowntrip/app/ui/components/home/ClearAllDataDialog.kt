package com.myowntrip.app.ui.components.home

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.myowntrip.app.ui.theme.MOTTextButton

@Composable
fun ClearAllDataDialog(
  onDismiss: () -> Unit,
  onConfirm: () -> Unit,
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Borrar todos los datos") },
    text = {
      Text(
        "Se eliminarán todos los viajes, documentos de Wallet, recuerdos, gastos y archivos guardados en este dispositivo. Esta acción no se puede deshacer.",
        style = MaterialTheme.typography.bodyMedium,
      )
    },
    confirmButton = {
      MOTTextButton(onClick = onConfirm) {
        Text("Borrar todo", color = MaterialTheme.colorScheme.error)
      }
    },
    dismissButton = {
      MOTTextButton(onClick = onDismiss) { Text("Cancelar") }
    },
  )
}
