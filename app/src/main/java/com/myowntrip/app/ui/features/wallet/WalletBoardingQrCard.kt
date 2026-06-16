package com.myowntrip.app.ui.features.wallet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.myowntrip.app.data.wallet.WalletQrEncoder
import com.myowntrip.app.ui.theme.MOTSpacing

@Composable
fun WalletBoardingQrCard(
  payload: String,
  modifier: Modifier = Modifier,
  compact: Boolean = false,
) {
  val bitmap = remember(payload) { WalletQrEncoder.encodeBitmap(payload, if (compact) 256 else 512) }
  if (bitmap == null) return

  Card(
    modifier = modifier
      .fillMaxWidth()
      .semantics(mergeDescendants = true) {
        contentDescription = "Código QR de embarque guardado para uso sin conexión"
      },
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
  ) {
    Column(
      modifier = Modifier.padding(MOTSpacing.layoutMd),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
        text = "QR de embarque",
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.tertiary,
      )
      Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null,
        modifier = Modifier
          .padding(top = MOTSpacing.componentSm)
          .size(if (compact) 200.dp else 260.dp),
      )
      Text(
        text = "Muéstralo en la puerta de embarque. Disponible sin conexión.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = MOTSpacing.componentSm),
      )
    }
  }
}
