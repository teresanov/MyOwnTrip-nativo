package com.myowntrip.app.ui.features.plan

import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.myowntrip.app.ui.theme.LocalExtendedColors

@Composable
fun PlanPlacementSourceChip(
  label: String,
  modifier: Modifier = Modifier,
  onClick: (() -> Unit)? = null,
) {
  val warning = LocalExtendedColors.current.warning
  AssistChip(
    onClick = onClick ?: {},
    enabled = onClick != null,
    label = { Text(label) },
    modifier = modifier,
    colors = AssistChipDefaults.assistChipColors(
      labelColor = warning,
      containerColor = warning.copy(alpha = 0.14f),
      disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
      disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ),
    border = AssistChipDefaults.assistChipBorder(
      enabled = onClick != null,
      borderColor = warning.copy(alpha = 0.35f),
      disabledBorderColor = Color.Transparent,
    ),
  )
}
