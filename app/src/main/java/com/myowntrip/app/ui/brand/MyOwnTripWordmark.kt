package com.myowntrip.app.ui.brand

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.FrauncesFamily

enum class WordmarkVariant {
  Positive,
  Dark,
  Monochrome,
}

/**
 * Wordmark W4 — splash, onboarding, store, about.
 * Fraunces Medium; «Own» en itálica; cinta ocre a la derecha.
 * Usar cuando la altura del lockup sea ≥ 19dp.
 */
@Composable
fun MyOwnTripWordmark(
  modifier: Modifier = Modifier,
  height: Dp = 32.dp,
  variant: WordmarkVariant = WordmarkVariant.Positive,
  showRibbon: Boolean = true,
) {
  val textColor = when (variant) {
    WordmarkVariant.Positive -> BrandColors.Ink
    WordmarkVariant.Dark -> BrandColors.OnDark
    WordmarkVariant.Monochrome -> BrandColors.Ink
  }
  val ribbonTint = when (variant) {
    WordmarkVariant.Monochrome -> textColor
    else -> BrandColors.AccentOcre
  }
  val fontSize = height.value.spFromDpHeight()
  val style = TextStyle(
    fontFamily = FrauncesFamily,
    fontWeight = FontWeight.Medium,
    fontSize = fontSize,
    letterSpacing = fontSize * (-0.03f),
    color = textColor,
  )
  val ownStyle = style.copy(fontStyle = FontStyle.Italic)

  Row(
    modifier = modifier.padding(height * 0.5f),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(0.dp),
  ) {
    Text(text = "My", style = style)
    Text(text = "Own", style = ownStyle)
    Text(text = "Trip", style = style)
    if (showRibbon) {
      BrandRibbon(
        modifier = Modifier.padding(start = height * 0.22f),
        height = height * 0.62f,
        tint = ribbonTint,
      )
    }
  }
}

private fun Float.spFromDpHeight(): TextUnit =
  androidx.compose.ui.unit.TextUnit(this * 0.72f, androidx.compose.ui.unit.TextUnitType.Sp)
