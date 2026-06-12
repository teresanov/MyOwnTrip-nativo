package com.myowntrip.app.ui.brand

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.FrauncesFamily

enum class MonogramVariant {
  Light,
  Dark,
}

/**
 * Monograma MOT — toolbar (~21dp) y espacios estrechos.
 * M/T Light atenuadas (~62%); O Bold tinta #4A5864; cinta ocre #825513.
 */
@Composable
fun MyOwnTripMonogram(
  modifier: Modifier = Modifier,
  height: Dp = 21.dp,
  variant: MonogramVariant = MonogramVariant.Light,
  showRibbon: Boolean = true,
) {
  val mutedInk = when (variant) {
    MonogramVariant.Light -> BrandColors.Ink.copy(alpha = 0.62f)
    MonogramVariant.Dark -> BrandColors.OnDark.copy(alpha = 0.62f)
  }
  val oColor = when (variant) {
    MonogramVariant.Light -> BrandColors.InkDeep
    MonogramVariant.Dark -> BrandColors.OnDark
  }
  val ribbonTint = when (variant) {
    MonogramVariant.Light -> BrandColors.AccentOcre
    MonogramVariant.Dark -> BrandColors.AccentOcre
  }
  val fontSize = height.value.spFromDpHeight()
  val mutedStyle = TextStyle(
    fontFamily = FrauncesFamily,
    fontWeight = FontWeight.Light,
    fontSize = fontSize,
    letterSpacing = fontSize * (-0.02f),
    color = mutedInk,
  )
  val oStyle = TextStyle(
    fontFamily = FrauncesFamily,
    fontWeight = FontWeight.Bold,
    fontSize = fontSize,
    letterSpacing = fontSize * (-0.02f),
    color = oColor,
  )

  Row(
    modifier = modifier.padding(height * 0.35f),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy((-1).dp),
  ) {
    Text(text = "M", style = mutedStyle)
    Text(text = "O", style = oStyle)
    Text(text = "T", style = mutedStyle)
    if (showRibbon) {
      BrandRibbon(
        modifier = Modifier.padding(start = height * 0.12f),
        height = height * 0.85f,
        tint = ribbonTint,
      )
    }
  }
}

private fun Float.spFromDpHeight(): TextUnit =
  androidx.compose.ui.unit.TextUnit(this * 0.78f, androidx.compose.ui.unit.TextUnitType.Sp)
