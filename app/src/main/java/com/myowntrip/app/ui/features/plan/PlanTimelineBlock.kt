package com.myowntrip.app.ui.features.plan

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTIcons
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MyOwnTripTheme

/**
 * Figma: **Plan timeline block** (`zrGAL4v6` · Cards).
 *
 * Ejes del set: `Wallet`, `Dragging`, `Title`, `Meta`, `Show overflow`.
 * Átomos M3: [Card] + [Text] + [MOTIconButton]. El menú ⋮ vive en [overflowMenu].
 */
@Composable
fun PlanTimelineBlock(
  title: String,
  meta: String,
  wallet: Boolean,
  modifier: Modifier = Modifier,
  dragging: Boolean = false,
  showOverflow: Boolean = true,
  copyModifier: Modifier = Modifier,
  overflowMenu: @Composable BoxScope.() -> Unit = {},
) {
  val shape = RoundedCornerShape(8.dp)
  val borderColor = if (wallet) {
    MaterialTheme.colorScheme.tertiary
  } else {
    MaterialTheme.colorScheme.outline
  }
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = shape,
    colors = CardDefaults.cardColors(
      containerColor = if (dragging) {
        MaterialTheme.colorScheme.secondaryContainer
      } else {
        MaterialTheme.colorScheme.surfaceContainerHigh
      },
    ),
    elevation = CardDefaults.cardElevation(
      defaultElevation = if (dragging) 6.dp else 1.dp,
    ),
  ) {
    Row(
      modifier = Modifier
        .fillMaxSize()
        .border(1.dp, borderColor, shape)
        .padding(horizontal = MOTSpacing.componentSm, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Column(modifier = Modifier.weight(1f).then(copyModifier)) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleSmall,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        Text(
          text = meta,
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.padding(top = 2.dp),
        )
      }
      if (showOverflow) {
        Box(content = overflowMenu)
      }
    }
  }
}

@Composable
fun PlanTimelineBlockOverflowButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  MOTIconButton(
    onClick = onClick,
    modifier = modifier.semantics { contentDescription = "Opciones de actividad" },
  ) {
    Icon(
      imageVector = MOTIcons.MoreVert,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.onSurfaceVariant,
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun PlanTimelineBlockPreview() {
  MyOwnTripTheme {
    Column(Modifier.padding(MOTSpacing.componentSm)) {
      PlanTimelineBlock(
        title = "Museo del Prado",
        meta = "11:00 · Del documento",
        wallet = true,
        overflowMenu = { PlanTimelineBlockOverflowButton(onClick = {}) },
      )
      PlanTimelineBlock(
        title = "Café en el Retiro",
        meta = "14:30",
        wallet = false,
        modifier = Modifier.padding(top = MOTSpacing.componentSm),
        overflowMenu = { PlanTimelineBlockOverflowButton(onClick = {}) },
      )
      PlanTimelineBlock(
        title = "Hotel Baixa Lisboa",
        meta = "14:00",
        wallet = false,
        showOverflow = false,
        modifier = Modifier.padding(top = MOTSpacing.componentSm),
      )
    }
  }
}
