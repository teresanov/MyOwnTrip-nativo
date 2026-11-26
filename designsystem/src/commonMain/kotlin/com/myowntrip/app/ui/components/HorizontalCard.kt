package com.myowntrip.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.MOTSpacing

/** Figma `58710:12949` · design-file `214:593` — media 80dp ancho, alto = card. */
val MotHorizontalCardMediaWidth = 80.dp

/** Figma design-file `214:593` · DS Content `58710:12943` / `58710:12944`. */
private val MotHorizontalCardHeight = 80.dp

/** Figma `58710:12946` · gap entre Header y Subhead. */
private val MotHorizontalCardTextGap = 4.dp

/**
 * Figma DS **Horizontal card** Outlined · Media & text · `58710:12943`.
 * Content `58710:12944`: `p-[16px]`, texto `gap-[4px]`, media `80dp` fill.
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
        .height(MotHorizontalCardHeight),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Column(
        modifier = Modifier
          .weight(1f)
          .padding(MOTSpacing.layoutMd),
        verticalArrangement = Arrangement.spacedBy(MotHorizontalCardTextGap),
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
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        if (!supportingText.isNullOrBlank()) {
          Text(
            text = supportingText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        }
      }
      androidx.compose.foundation.layout.Box(
        modifier = Modifier
          .width(MotHorizontalCardMediaWidth)
          .height(MotHorizontalCardHeight),
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
