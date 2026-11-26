package com.myowntrip.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTIcons
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MyOwnTripTheme

/**
 * Figma: **Stacked card** `52346:27573` · layouts `Media & text` y `Text only`.
 *
 * Properties: `Header text`, `Subhead text`, `Supporting text`, avatar leading (Media & text),
 * `Show secondary action` → [onDismiss].
 */
@Composable
fun MotStackedCard(
  style: MotCardStyle,
  headerText: String,
  subheadText: String,
  modifier: Modifier = Modifier,
  supportingText: String? = null,
  leadingInitial: String? = null,
  image: Painter? = null,
  imageContentDescription: String? = null,
  imageHeight: Dp = 200.dp,
  onDismiss: (() -> Unit)? = null,
  dismissContentDescription: String = "Cerrar",
  colors: CardColors? = null,
  headerColor: Color = Color.Unspecified,
  subheadColor: Color = Color.Unspecified,
  supportingColor: Color = Color.Unspecified,
  dismissIconColor: Color = Color.Unspecified,
) {
  val resolvedHeader = if (headerColor == Color.Unspecified) {
    MaterialTheme.colorScheme.onSurface
  } else {
    headerColor
  }
  val resolvedSubhead = if (subheadColor == Color.Unspecified) {
    MaterialTheme.colorScheme.onSurfaceVariant
  } else {
    subheadColor
  }
  val resolvedSupporting = if (supportingColor == Color.Unspecified) {
    MaterialTheme.colorScheme.onSurfaceVariant
  } else {
    supportingColor
  }
  val resolvedDismiss = if (dismissIconColor == Color.Unspecified) {
    MaterialTheme.colorScheme.onSurfaceVariant
  } else {
    dismissIconColor
  }

  val content: @Composable () -> Unit = {
  Column(modifier = Modifier.fillMaxWidth()) {
    Box(modifier = Modifier.fillMaxWidth()) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(MOTSpacing.layoutMd)
          .padding(end = if (onDismiss != null) 32.dp else 0.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MOTSpacing.layoutMd),
      ) {
        if (leadingInitial != null) {
          Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text(
                text = leadingInitial,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          }
        }
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = headerText,
            style = MaterialTheme.typography.titleMedium,
            color = resolvedHeader,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
          )
          Text(
            text = subheadText,
            style = MaterialTheme.typography.bodyMedium,
            color = resolvedSubhead,
            modifier = Modifier.padding(top = MOTSpacing.componentXs),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
          )
        }
      }
      if (onDismiss != null) {
        MOTIconButton(
          onClick = onDismiss,
          modifier = Modifier
            .align(Alignment.TopEnd)
            .semantics { contentDescription = dismissContentDescription },
        ) {
          Icon(
            imageVector = MOTIcons.Close,
            contentDescription = null,
            tint = resolvedDismiss,
          )
        }
      }
    }
    if (supportingText != null && leadingInitial == null && image == null) {
      Text(
        text = supportingText,
        style = MaterialTheme.typography.bodyMedium,
        color = resolvedSupporting,
        modifier = Modifier.padding(
          start = MOTSpacing.layoutMd,
          end = MOTSpacing.layoutMd,
          bottom = MOTSpacing.layoutMd,
        ),
        maxLines = 4,
        overflow = TextOverflow.Ellipsis,
      )
    } else if (supportingText != null && (leadingInitial != null || image != null)) {
      // Media & text: supporting oculto en Home vacío Figma; reservado por si se activa la property.
    }
    if (image != null) {
      Image(
        painter = image,
        contentDescription = imageContentDescription,
        modifier = Modifier
          .fillMaxWidth()
          .height(imageHeight),
        contentScale = ContentScale.Crop,
      )
    }
  }
  }

  when (style) {
    MotCardStyle.Outlined -> {
      OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        colors = colors ?: CardDefaults.outlinedCardColors(),
      ) {
        content()
      }
    }
    MotCardStyle.Elevated -> {
      ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = colors ?: CardDefaults.elevatedCardColors(),
      ) {
        content()
      }
    }
  }
}

