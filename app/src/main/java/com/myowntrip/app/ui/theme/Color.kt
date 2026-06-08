package com.myowntrip.app.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * M3 color roles from Material Theme Builder (Tonal Spot).
 * Seeds: primary #3D63D1, secondary #219A60.
 * UI must consume [androidx.compose.material3.MaterialTheme.colorScheme], not these directly.
 */
object AppColors {
  val primaryLight = Color(0xFF3D63D1)
  val onPrimaryLight = Color(0xFFFFFFFF)
  val primaryContainerLight = Color(0xFFDDE1FF)
  val onPrimaryContainerLight = Color(0xFF001355)

  val secondaryLight = Color(0xFF219A60)
  val onSecondaryLight = Color(0xFFFFFFFF)
  val secondaryContainerLight = Color(0xFFB8F1D0)
  val onSecondaryContainerLight = Color(0xFF002113)

  val tertiaryLight = Color(0xFF7D5260)
  val onTertiaryLight = Color(0xFFFFFFFF)
  val tertiaryContainerLight = Color(0xFFFFD8E4)
  val onTertiaryContainerLight = Color(0xFF31111D)

  val errorLight = Color(0xFFBA1A1A)
  val onErrorLight = Color(0xFFFFFFFF)
  val errorContainerLight = Color(0xFFFFDAD6)
  val onErrorContainerLight = Color(0xFF410002)

  val backgroundLight = Color(0xFFFDFBFF)
  val onBackgroundLight = Color(0xFF1B1B1F)
  val surfaceLight = Color(0xFFFDFBFF)
  val onSurfaceLight = Color(0xFF1B1B1F)
  val surfaceVariantLight = Color(0xFFE2E2EC)
  val onSurfaceVariantLight = Color(0xFF45464F)
  val outlineLight = Color(0xFF767680)
  val outlineVariantLight = Color(0xFFC6C6D0)
  val surfaceContainerLowLight = Color(0xFFF7F2FA)
  val surfaceContainerHighLight = Color(0xFFECE6F0)
  val inverseSurfaceLight = Color(0xFF303034)
  val inverseOnSurfaceLight = Color(0xFFF3F0F4)
  val inversePrimaryLight = Color(0xFFB8C4FF)

  val primaryDark = Color(0xFFB8C4FF)
  val onPrimaryDark = Color(0xFF002389)
  val primaryContainerDark = Color(0xFF1F4BA8)
  val onPrimaryContainerDark = Color(0xFFDDE1FF)

  val secondaryDark = Color(0xFF9CD5B4)
  val onSecondaryDark = Color(0xFF003822)
  val secondaryContainerDark = Color(0xFF005233)
  val onSecondaryContainerDark = Color(0xFFB8F1D0)

  val tertiaryDark = Color(0xFFEFB8C8)
  val onTertiaryDark = Color(0xFF492532)
  val tertiaryContainerDark = Color(0xFF633B48)
  val onTertiaryContainerDark = Color(0xFFFFD8E4)

  val errorDark = Color(0xFFFFB4AB)
  val onErrorDark = Color(0xFF690005)
  val errorContainerDark = Color(0xFF93000A)
  val onErrorContainerDark = Color(0xFFFFDAD6)

  val backgroundDark = Color(0xFF1B1B1F)
  val onBackgroundDark = Color(0xFFE4E1E6)
  val surfaceDark = Color(0xFF1B1B1F)
  val onSurfaceDark = Color(0xFFE4E1E6)
  val surfaceVariantDark = Color(0xFF45464F)
  val onSurfaceVariantDark = Color(0xFFC6C6D0)
  val outlineDark = Color(0xFF90909A)
  val outlineVariantDark = Color(0xFF45464F)
  val surfaceContainerLowDark = Color(0xFF211F26)
  val surfaceContainerHighDark = Color(0xFF2B2930)
  val inverseSurfaceDark = Color(0xFFE4E1E6)
  val inverseOnSurfaceDark = Color(0xFF303034)
  val inversePrimaryDark = Color(0xFF3D63D1)

  val success = Color(0xFF2E7D32)
  val warning = Color(0xFFF9A825)
  val info = Color(0xFF1565C0)
}
