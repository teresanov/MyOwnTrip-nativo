package com.myowntrip.app.ui.features.plan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.myowntrip.app.domain.model.WalletEntry
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MOTTextButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlanActivitySheet(
  title: String,
  time: String,
  linkedEntry: WalletEntry?,
  onTitleChange: (String) -> Unit,
  onTimeChange: (String) -> Unit,
  onPickWallet: () -> Unit,
  onDismiss: () -> Unit,
  onConfirm: () -> Unit,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = MOTSpacing.screenHorizontal)
        .padding(bottom = MOTSpacing.screenContentBottom),
      verticalArrangement = Arrangement.spacedBy(MOTSpacing.layoutMd),
    ) {
      Text("Añadir actividad", style = MaterialTheme.typography.titleLarge)
      OutlinedTextField(
        value = title,
        onValueChange = onTitleChange,
        label = { Text("Qué haces") },
        placeholder = { Text("Ej. Entrada al teatro, visita al museo") },
        modifier = Modifier.fillMaxWidth(),
      )
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Icon(
          Icons.Default.Link,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.tertiary,
        )
        Column(modifier = Modifier.weight(1f)) {
          Text(
            if (linkedEntry != null) "Documento vinculado" else "Vincular documento de Wallet",
            style = MaterialTheme.typography.labelLarge,
          )
          Text(
            linkedEntry?.title ?: "Billete, entrada o reserva (opcional)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
        MOTTextButton(onClick = onPickWallet) {
          Text(if (linkedEntry != null) "Cambiar" else "Elegir")
        }
      }
      OutlinedTextField(
        value = time,
        onValueChange = onTimeChange,
        label = { Text("Hora (opcional)") },
        placeholder = { Text("Ej. 19:30") },
        modifier = Modifier.fillMaxWidth(),
      )
      MOTButton(onClick = onConfirm, modifier = Modifier.fillMaxWidth()) {
        Text("Añadir actividad")
      }
      MOTTextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
        Text("Cancelar")
      }
    }
  }
}
