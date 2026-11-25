package com.myowntrip.app.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
internal actual fun motColorScheme(darkTheme: Boolean, dynamicColor: Boolean): ColorScheme {
  return if (darkTheme) MotDarkColorScheme else MotLightColorScheme
}

@Composable
internal actual fun MotThemeHost(
  colorScheme: ColorScheme,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = colorScheme,
    typography = AppTypography,
    shapes = MOTThemeShapes,
    content = content,
  )
}
