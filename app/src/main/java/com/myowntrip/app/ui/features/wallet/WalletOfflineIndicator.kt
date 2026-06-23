package com.myowntrip.app.ui.features.wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.myowntrip.app.domain.wallet.WalletOfflineAvailability
import com.myowntrip.app.domain.wallet.isAvailableOffline
import com.myowntrip.app.domain.wallet.isCloudOnly

@Composable
fun WalletOfflineIndicator(
  availability: WalletOfflineAvailability,
  modifier: Modifier = Modifier,
  compact: Boolean = false,
) {
  if (!availability.isAvailableOffline() && !availability.isCloudOnly()) return

  val (icon, label) = when (availability) {
    WalletOfflineAvailability.LocalDocument -> Icons.Outlined.WifiOff to "Sin conexión"
    WalletOfflineAvailability.QrCode -> Icons.Default.QrCode to "QR sin conexión"
    WalletOfflineAvailability.CloudReference -> Icons.Outlined.Cloud to "En la nube"
    WalletOfflineAvailability.MetadataOnly -> return
  }

  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp),
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      modifier = Modifier.size(if (compact) 18.dp else 20.dp),
      tint = MaterialTheme.colorScheme.tertiary,
    )
    if (!compact) {
      Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.tertiary,
        maxLines = 1,
      )
    }
  }
}
