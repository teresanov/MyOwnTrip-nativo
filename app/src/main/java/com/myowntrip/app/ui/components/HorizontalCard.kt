package com.myowntrip.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.MOTSpacing

/** Ancho fijo de media en fila horizontal — 80dp según design-file Home. */
val MotHorizontalCardMediaWidth = 80.dp

/**
 * Figma: **Horizontal card** `52346:27573` · `Media & text` · 80dp fila en Home cap 2.
 *
 * Properties: `Header text`, `Subhead text`, `Supporting text` (meta fechas).
 */
@Composable
fun MotHorizontalCard(
  title: String,
  subtitle: String,
  supportingText: String?,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  style: MotCardStyle = MotCardStyle.Outlined,
  stateDescription: String? = null,
  media: @Composable () -> Unit,
) {
  val a11y = buildString {
    append(title)
    append(", ")
    append(subtitle)
    if (!supportingText.isNullOrBlank()) {
      append(", ")
      append(supportingText)
    }
  }
  val cardModifier = modifier
    .fillMaxWidth()
    .semantics(mergeDescendants = true) {
      contentDescription = a11y
      if (stateDescription != null) this.stateDescription = stateDescription
    }
    .clickable(onClick = onClick)

  val rowContent: @Composable () -> Unit = {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(80.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Column(
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = MOTSpacing.layoutMd, vertical = MOTSpacing.componentSm),
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleMedium,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(top = 2.dp),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        if (!supportingText.isNullOrBlank()) {
          Text(
            text = supportingText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 2.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        }
      }
      androidx.compose.foundation.layout.Box(
        modifier = Modifier
          .width(MotHorizontalCardMediaWidth)
          .height(80.dp)
          .clip(MaterialTheme.shapes.small),
      ) {
        media()
      }
    }
  }

  when (style) {
    MotCardStyle.Outlined -> OutlinedCard(modifier = cardModifier) { rowContent() }
    MotCardStyle.Elevated -> androidx.compose.material3.ElevatedCard(
      modifier = cardModifier,
      colors = CardDefaults.elevatedCardColors(),
    ) { rowContent() }
  }
}
