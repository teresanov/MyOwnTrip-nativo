package com.myowntrip.app.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.MyOwnTripTheme

/**
 * Figma: **Eyebrow label** `61202:16834` · property `Label text`.
 * Etiqueta informativa no interactiva (no chip).
 */
enum class EyebrowLabelColor {
  Tertiary,
  Surface,
  Secondary,
}

enum class EyebrowLabelSize {
  Medium,
  Small,
}

@Composable
fun EyebrowLabel(
  text: String,
  modifier: Modifier = Modifier,
  color: EyebrowLabelColor = EyebrowLabelColor.Tertiary,
  size: EyebrowLabelSize = EyebrowLabelSize.Medium,
) {
  val (containerColor, labelColor, textStyle) = eyebrowTokens(color, size)
  Surface(
    modifier = modifier.semantics { hideFromAccessibility() },
    shape = MaterialTheme.shapes.small,
    color = containerColor,
  ) {
    Text(
      text = text,
      style = textStyle,
      color = labelColor,
      modifier = Modifier.padding(
        horizontal = 16.dp,
        vertical = if (size == EyebrowLabelSize.Medium) 6.dp else 4.dp,
      ),
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
    )
  }
}

@Composable
private fun eyebrowTokens(
  color: EyebrowLabelColor,
  size: EyebrowLabelSize,
): Triple<Color, Color, TextStyle> {
  val scheme = MaterialTheme.colorScheme
  val typography = MaterialTheme.typography
  val textStyle = when (size) {
    EyebrowLabelSize.Medium -> typography.labelMedium
    EyebrowLabelSize.Small -> typography.labelSmall
  }
  return when (color) {
    EyebrowLabelColor.Tertiary -> Triple(
      scheme.tertiaryFixedDim,
      scheme.onTertiaryContainer,
      textStyle,
    )
    EyebrowLabelColor.Surface -> Triple(
      scheme.surfaceContainerHigh,
      scheme.onSurfaceVariant,
      textStyle,
    )
    EyebrowLabelColor.Secondary -> Triple(
      scheme.secondaryContainer,
      scheme.onSecondaryContainer,
      textStyle,
    )
  }
}

@Preview
@Composable
private fun EyebrowLabelPreview() {
  MyOwnTripTheme {
    EyebrowLabel(text = "Próximo viaje")
  }
}
