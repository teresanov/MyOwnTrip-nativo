package com.myowntrip.app.ui.brand

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.myowntrip.app.R

/**
 * Glifo atómico: cinta marcapáginas ocre con muesca en V.
 * Máximo 1 aparición por pantalla (icono, wordmark o indicador «guardado»).
 */
@Composable
fun BrandRibbon(
  modifier: Modifier = Modifier,
  height: Dp = 20.dp,
  tint: Color = BrandColors.AccentOcre,
) {
  val aspect = 10f / 19.5f
  Icon(
    painter = painterResource(R.drawable.brand_ribbon),
    contentDescription = null,
    modifier = modifier
      .height(height)
      .width(height * aspect),
    tint = tint,
  )
}

/**
 * Variante monocroma (toolbar oscuro, estados sin acento).
 */
@Composable
fun BrandRibbonMonochrome(
  modifier: Modifier = Modifier,
  height: Dp = 20.dp,
  tint: Color = LocalContentColor.current,
) {
  BrandRibbon(modifier = modifier, height = height, tint = tint)
}
