package com.myowntrip.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
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

private val DarkColorScheme = darkColorScheme(
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
  val success: androidx.compose.ui.graphics.Color,
  val warning: androidx.compose.ui.graphics.Color,
  val info: androidx.compose.ui.graphics.Color,
)

val LocalExtendedColors = staticCompositionLocalOf {
  ExtendedColors(
    success = AppColors.successLight,
    warning = AppColors.warningLight,
    info = AppColors.infoLight,
  )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
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

  CompositionLocalProvider(
    LocalExtendedColors provides extendedColors,
  ) {
    MaterialExpressiveTheme(
      colorScheme = colorScheme,
      typography = AppTypography,
      shapes = MOTThemeShapes,
      motionScheme = MotionScheme.standard(),
      content = content,
    )
  }
}
