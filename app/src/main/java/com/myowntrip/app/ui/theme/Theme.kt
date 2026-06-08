package com.myowntrip.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
  primary = AppColors.primaryLight,
  onPrimary = AppColors.onPrimaryLight,
  primaryContainer = AppColors.primaryContainerLight,
  onPrimaryContainer = AppColors.onPrimaryContainerLight,
  secondary = AppColors.secondaryLight,
  onSecondary = AppColors.onSecondaryLight,
  secondaryContainer = AppColors.secondaryContainerLight,
  onSecondaryContainer = AppColors.onSecondaryContainerLight,
  tertiary = AppColors.tertiaryLight,
  onTertiary = AppColors.onTertiaryLight,
  tertiaryContainer = AppColors.tertiaryContainerLight,
  onTertiaryContainer = AppColors.onTertiaryContainerLight,
  error = AppColors.errorLight,
  onError = AppColors.onErrorLight,
  errorContainer = AppColors.errorContainerLight,
  onErrorContainer = AppColors.onErrorContainerLight,
  background = AppColors.backgroundLight,
  onBackground = AppColors.onBackgroundLight,
  surface = AppColors.surfaceLight,
  onSurface = AppColors.onSurfaceLight,
  surfaceVariant = AppColors.surfaceVariantLight,
  onSurfaceVariant = AppColors.onSurfaceVariantLight,
  outline = AppColors.outlineLight,
  outlineVariant = AppColors.outlineVariantLight,
  surfaceContainerLow = AppColors.surfaceContainerLowLight,
  surfaceContainerHigh = AppColors.surfaceContainerHighLight,
  inverseSurface = AppColors.inverseSurfaceLight,
  inverseOnSurface = AppColors.inverseOnSurfaceLight,
  inversePrimary = AppColors.inversePrimaryLight,
)

private val DarkColorScheme = darkColorScheme(
  primary = AppColors.primaryDark,
  onPrimary = AppColors.onPrimaryDark,
  primaryContainer = AppColors.primaryContainerDark,
  onPrimaryContainer = AppColors.onPrimaryContainerDark,
  secondary = AppColors.secondaryDark,
  onSecondary = AppColors.onSecondaryDark,
  secondaryContainer = AppColors.secondaryContainerDark,
  onSecondaryContainer = AppColors.onSecondaryContainerDark,
  tertiary = AppColors.tertiaryDark,
  onTertiary = AppColors.onTertiaryDark,
  tertiaryContainer = AppColors.tertiaryContainerDark,
  onTertiaryContainer = AppColors.onTertiaryContainerDark,
  error = AppColors.errorDark,
  onError = AppColors.onErrorDark,
  errorContainer = AppColors.errorContainerDark,
  onErrorContainer = AppColors.onErrorContainerDark,
  background = AppColors.backgroundDark,
  onBackground = AppColors.onBackgroundDark,
  surface = AppColors.surfaceDark,
  onSurface = AppColors.onSurfaceDark,
  surfaceVariant = AppColors.surfaceVariantDark,
  onSurfaceVariant = AppColors.onSurfaceVariantDark,
  outline = AppColors.outlineDark,
  outlineVariant = AppColors.outlineVariantDark,
  surfaceContainerLow = AppColors.surfaceContainerLowDark,
  surfaceContainerHigh = AppColors.surfaceContainerHighDark,
  inverseSurface = AppColors.inverseSurfaceDark,
  inverseOnSurface = AppColors.inverseOnSurfaceDark,
  inversePrimary = AppColors.inversePrimaryDark,
)

data class ExtendedColors(
  val success: androidx.compose.ui.graphics.Color,
  val warning: androidx.compose.ui.graphics.Color,
  val info: androidx.compose.ui.graphics.Color,
)

val LocalExtendedColors = staticCompositionLocalOf {
  ExtendedColors(
    success = AppColors.success,
    warning = AppColors.warning,
    info = AppColors.info,
  )
}

@Composable
fun MyOwnTripTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  CompositionLocalProvider(
    LocalExtendedColors provides ExtendedColors(
      success = AppColors.success,
      warning = AppColors.warning,
      info = AppColors.info,
    ),
  ) {
    MaterialTheme(
      colorScheme = colorScheme,
      typography = AppTypography,
      content = content,
    )
  }
}
