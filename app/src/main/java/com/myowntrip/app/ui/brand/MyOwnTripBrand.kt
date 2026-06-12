package com.myowntrip.app.ui.brand

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Selector de nivel de marca según altura disponible:
 * ≥ 19dp → W4 wordmark · ≥ 16dp → MOT · &lt; 16dp → solo cinta.
 */
@Composable
fun MyOwnTripBrand(
  modifier: Modifier = Modifier,
  height: Dp,
  wordmarkVariant: WordmarkVariant = WordmarkVariant.Positive,
  monogramVariant: MonogramVariant = MonogramVariant.Light,
) {
  when {
    height >= 19.dp -> MyOwnTripWordmark(
      modifier = modifier,
      height = height,
      variant = wordmarkVariant,
    )
    height >= 16.dp -> MyOwnTripMonogram(
      modifier = modifier,
      height = height,
      variant = monogramVariant,
    )
    else -> BrandRibbon(
      modifier = modifier,
      height = height.coerceAtLeast(12.dp),
    )
  }
}
