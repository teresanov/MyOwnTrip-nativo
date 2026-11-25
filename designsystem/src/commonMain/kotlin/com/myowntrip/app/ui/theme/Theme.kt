package com.myowntrip.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

internal val MotLightColorScheme = lightColorScheme(
  primary = AppColors.primaryLight,
  onPrimary = AppColors.onPrimaryLight,
  primaryContainer = AppColors.primaryContainerLight,
  onPrimaryContainer = AppColors.onPrimaryContainerLight,
  primaryFixed = AppColors.primaryFixedLight,
  primaryFixedDim = AppColors.primaryFixedDimLight,
  onPrimaryFixed = AppColors.onPrimaryFixedLight,
  onPrimaryFixedVariant = AppColors.onPrimaryFixedVariantLight,
  secondary = AppColors.secondaryLight,
  onSecondary = AppColors.onSecondaryLight,
  secondaryContainer = AppColors.secondaryContainerLight,
  onSecondaryContainer = AppColors.onSecondaryContainerLight,
  secondaryFixed = AppColors.secondaryFixedLight,
  secondaryFixedDim = AppColors.secondaryFixedDimLight,
  onSecondaryFixed = AppColors.onSecondaryFixedLight,
  onSecondaryFixedVariant = AppColors.onSecondaryFixedVariantLight,
  tertiary = AppColors.tertiaryLight,
  onTertiary = AppColors.onTertiaryLight,
  tertiaryContainer = AppColors.tertiaryContainerLight,
  onTertiaryContainer = AppColors.onTertiaryContainerLight,
  tertiaryFixed = AppColors.tertiaryFixedLight,
  tertiaryFixedDim = AppColors.tertiaryFixedDimLight,
  onTertiaryFixed = AppColors.onTertiaryFixedLight,
  onTertiaryFixedVariant = AppColors.onTertiaryFixedVariantLight,
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
  surfaceTint = AppColors.surfaceTintLight,
  surfaceBright = AppColors.surfaceBrightLight,
  surfaceDim = AppColors.surfaceDimLight,
  surfaceContainer = AppColors.surfaceContainerLight,
  surfaceContainerLow = AppColors.surfaceContainerLowLight,
  surfaceContainerHigh = AppColors.surfaceContainerHighLight,
  surfaceContainerHighest = AppColors.surfaceContainerHighestLight,
  surfaceContainerLowest = AppColors.surfaceContainerLowestLight,
  inverseSurface = AppColors.inverseSurfaceLight,
  inverseOnSurface = AppColors.inverseOnSurfaceLight,
  inversePrimary = AppColors.inversePrimaryLight,
  scrim = AppColors.scrimLight,
)

internal val MotDarkColorScheme = darkColorScheme(
  primary = AppColors.primaryDark,
  onPrimary = AppColors.onPrimaryDark,
  primaryContainer = AppColors.primaryContainerDark,
  onPrimaryContainer = AppColors.onPrimaryContainerDark,
  primaryFixed = AppColors.primaryFixedDark,
  primaryFixedDim = AppColors.primaryFixedDimDark,
  onPrimaryFixed = AppColors.onPrimaryFixedDark,
  onPrimaryFixedVariant = AppColors.onPrimaryFixedVariantDark,
  secondary = AppColors.secondaryDark,
  onSecondary = AppColors.onSecondaryDark,
  secondaryContainer = AppColors.secondaryContainerDark,
  onSecondaryContainer = AppColors.onSecondaryContainerDark,
  secondaryFixed = AppColors.secondaryFixedDark,
  secondaryFixedDim = AppColors.secondaryFixedDimDark,
  onSecondaryFixed = AppColors.onSecondaryFixedDark,
  onSecondaryFixedVariant = AppColors.onSecondaryFixedVariantDark,
  tertiary = AppColors.tertiaryDark,
  onTertiary = AppColors.onTertiaryDark,
  tertiaryContainer = AppColors.tertiaryContainerDark,
  onTertiaryContainer = AppColors.onTertiaryContainerDark,
  tertiaryFixed = AppColors.tertiaryFixedDark,
  tertiaryFixedDim = AppColors.tertiaryFixedDimDark,
  onTertiaryFixed = AppColors.onTertiaryFixedDark,
  onTertiaryFixedVariant = AppColors.onTertiaryFixedVariantDark,
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
  surfaceTint = AppColors.surfaceTintDark,
  surfaceBright = AppColors.surfaceBrightDark,
  surfaceDim = AppColors.surfaceDimDark,
  surfaceContainer = AppColors.surfaceContainerDark,
  surfaceContainerLow = AppColors.surfaceContainerLowDark,
  surfaceContainerHigh = AppColors.surfaceContainerHighDark,
  surfaceContainerHighest = AppColors.surfaceContainerHighestDark,
  surfaceContainerLowest = AppColors.surfaceContainerLowestDark,
  inverseSurface = AppColors.inverseSurfaceDark,
  inverseOnSurface = AppColors.inverseOnSurfaceDark,
  inversePrimary = AppColors.inversePrimaryDark,
  scrim = AppColors.scrimDark,
)

data class ExtendedColors(
  val success: Color,
  val warning: Color,
  val info: Color,
)

val LocalExtendedColors = staticCompositionLocalOf {
  ExtendedColors(
    success = AppColors.successLight,
    warning = AppColors.warningLight,
    info = AppColors.infoLight,
  )
}

@Composable
fun MyOwnTripTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  reduceMotion: Boolean? = null,
  content: @Composable () -> Unit,
) {
  val colorScheme = motColorScheme(darkTheme, dynamicColor)
  val extendedColors = if (darkTheme) {
    ExtendedColors(
      success = AppColors.successDark,
      warning = AppColors.warningDark,
      info = AppColors.infoDark,
    )
  } else {
    ExtendedColors(
      success = AppColors.successLight,
      warning = AppColors.warningLight,
      info = AppColors.infoLight,
    )
  }

  if (reduceMotion != null) {
    CompositionLocalProvider(
      LocalExtendedColors provides extendedColors,
      LocalReduceMotion provides reduceMotion,
    ) {
      MotThemeHost(colorScheme = colorScheme, content = content)
    }
  } else {
    CompositionLocalProvider(
      LocalExtendedColors provides extendedColors,
    ) {
      MotThemeHost(colorScheme = colorScheme, content = content)
    }
  }
}

@Composable
internal expect fun motColorScheme(darkTheme: Boolean, dynamicColor: Boolean): ColorScheme

@Composable
internal expect fun MotThemeHost(
  colorScheme: ColorScheme,
  content: @Composable () -> Unit,
)
