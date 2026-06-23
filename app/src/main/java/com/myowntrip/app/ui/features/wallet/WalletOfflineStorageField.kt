package com.myowntrip.app.ui.features.wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.MOTSpacing

@Composable
fun WalletOfflineStorageField(
  saveOnDevice: Boolean,
  onSaveOnDeviceChange: (Boolean) -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(MOTSpacing.layoutMd)
        .semantics(mergeDescendants = true) {
          contentDescription = if (saveOnDevice) {
            "Guardar en el teléfono activado. Disponible sin conexión."
          } else {
            "Solo en la nube. Requiere conexión para abrir el archivo."
          }
        },
      horizontalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = "Guardar en el teléfono",
          style = MaterialTheme.typography.titleSmall,
        )
        Text(
          text = if (saveOnDevice) {
            "Copia en el dispositivo. Disponible sin conexión."
          } else {
            "Enlace a la nube (Drive, Dropbox…). Requiere conexión para abrir."
          },
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(top = 4.dp),
        )
      }
      Switch(
        checked = saveOnDevice,
        onCheckedChange = onSaveOnDeviceChange,
        enabled = enabled,
      )
    }
  }
}
