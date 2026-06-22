package com.myowntrip.app.ui.features.plan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.myowntrip.app.domain.model.ItineraryBlock
import com.myowntrip.app.domain.model.WalletEntry
import com.myowntrip.app.ui.features.wallet.entryTypeLabel
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MOTTextButton

@Composable
fun PlanActivityCard(
  block: ItineraryBlock,
  linkedWalletEntry: WalletEntry?,
  onLinkWallet: () -> Unit,
  onWalletEntryClick: (String) -> Unit,
  modifier: Modifier = Modifier,
  embeddedInDay: Boolean = false,
  showReorder: Boolean = false,
  onMoveUp: () -> Unit = {},
  onMoveDown: () -> Unit = {},
) {
  val content: @Composable () -> Unit = {
    Column(modifier = Modifier.padding(if (embeddedInDay) 0.dp else MOTSpacing.layoutMd)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
          block.timeLabel?.let {
            Text(
              it,
              style = MaterialTheme.typography.labelMedium,
              color = MaterialTheme.colorScheme.tertiary,
            )
          }
          Text(block.title, style = MaterialTheme.typography.titleSmall)
        }
        if (showReorder) {
          MOTIconButton(
            onClick = onMoveUp,
            modifier = Modifier.semantics { contentDescription = "Subir actividad" },
          ) {
            Icon(Icons.Default.KeyboardArrowUp, contentDescription = null)
          }
          MOTIconButton(
            onClick = onMoveDown,
            modifier = Modifier.semantics { contentDescription = "Bajar actividad" },
          ) {
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
          }
        }
      }
      if (linkedWalletEntry != null) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = MOTSpacing.componentSm)
            .clickable { onWalletEntryClick(linkedWalletEntry.id) }
            .semantics { contentDescription = "Abrir documento ${linkedWalletEntry.title}" },
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
        ) {
          Icon(
            Icons.Default.ConfirmationNumber,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary,
          )
          Column(modifier = Modifier.weight(1f)) {
            Text(linkedWalletEntry.title, style = MaterialTheme.typography.bodyMedium)
            Text(
              entryTypeLabel(linkedWalletEntry.type),
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
          Text(
            "Ver",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.tertiary,
          )
        }
      }
      MOTTextButton(
        onClick = onLinkWallet,
        modifier = Modifier.padding(top = MOTSpacing.componentXs),
      ) {
        Icon(Icons.Default.Link, contentDescription = null)
        Text(
          if (linkedWalletEntry != null) {
            "Cambiar documento de Wallet"
          } else {
            "Vincular documento de Wallet"
          },
        )
      }
    }
  }
  if (embeddedInDay) {
    Column(modifier = modifier.fillMaxWidth()) { content() }
  } else {
    Card(modifier = modifier.fillMaxWidth()) { content() }
  }
}
