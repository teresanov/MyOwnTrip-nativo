package com.myowntrip.app.ui.features.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.LocalExtendedColors

private val LeadingIconSize = 40.dp
private val DriftDotSize = 10.dp
private val DriftDotBorder = 1.5.dp
private val DriftBorderWidth = 4.dp

@Composable
internal fun WalletPlanDriftLeadingIcon(
  icon: ImageVector,
  showDriftDot: Boolean,
  modifier: Modifier = Modifier,
) {
  val warning = LocalExtendedColors.current.warning
  Box(
    modifier = modifier.size(LeadingIconSize),
    contentAlignment = Alignment.Center,
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      modifier = Modifier
        .size(LeadingIconSize)
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.surfaceContainer)
        .padding(8.dp),
      tint = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    if (showDriftDot) {
      Box(
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .size(DriftDotSize)
          .semantics { invisibleToUser() }
          .border(DriftDotBorder, MaterialTheme.colorScheme.surface, CircleShape)
          .clip(CircleShape)
          .background(warning),
      )
    }
  }
}

internal val WalletPlanDriftBorderWidth = DriftBorderWidth
