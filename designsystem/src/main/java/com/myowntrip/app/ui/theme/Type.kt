@file:OptIn(androidx.compose.ui.text.ExperimentalTextApi::class)

package com.myowntrip.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.myowntrip.designsystem.R

private fun frauncesFont(weight: FontWeight) = Font(
  resId = R.font.fraunces,
  weight = weight,
  variationSettings = FontVariation.Settings(
    FontVariation.weight(weight.weight),
    FontVariation.Setting("opsz", 9f),
  ),
)

/**
 * `fraunces_italic.ttf` es variable: el eje `wght` tiene **default 900** (named
 * instance «9pt Black Italic»). Sin `variationSettings`, Compose pinta Black
 * aunque el `FontWeight` del constructor sea Medium.
 */
private fun frauncesItalicFont(weight: FontWeight) = Font(
  resId = R.font.fraunces_italic,
  weight = weight,
  style = FontStyle.Italic,
  variationSettings = FontVariation.Settings(
    FontVariation.weight(weight.weight),
    FontVariation.Setting("opsz", 9f),
  ),
)

val FrauncesFamily = FontFamily(
  frauncesFont(FontWeight.Light),
  frauncesFont(FontWeight.Normal),
  frauncesFont(FontWeight.Medium),
  frauncesFont(FontWeight.SemiBold),
  frauncesFont(FontWeight.Bold),
  frauncesFont(FontWeight.Black),
  frauncesItalicFont(FontWeight.Light),
  frauncesItalicFont(FontWeight.Normal),
  frauncesItalicFont(FontWeight.Medium),
  frauncesItalicFont(FontWeight.SemiBold),
  frauncesItalicFont(FontWeight.Bold),
  frauncesItalicFont(FontWeight.Black),
)

private fun interFont(weight: FontWeight) = Font(
  resId = R.font.inter,
  weight = weight,
  variationSettings = FontVariation.Settings(
    FontVariation.weight(weight.weight),
  ),
)

val InterFamily = FontFamily(
  interFont(FontWeight.Normal),
  interFont(FontWeight.Medium),
  interFont(FontWeight.SemiBold),
)

/**
 * "Pluma" del Diario: tipografía manuscrita, scopeada SOLO a notas a mano del álbum
 * (≥16sp), nunca en UI funcional. TODO: bundlear Shantell Sans en res/font/ y
 * apuntar aquí; de momento usa Fraunces itálica como sustituta (único punto de cambio).
 */
val HandFamily = FontFamily(
  frauncesItalicFont(FontWeight.Medium),
)

val AppTypography = Typography(
  displayLarge = TextStyle(
    fontFamily = FrauncesFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 57.sp,
    lineHeight = 64.sp,
    letterSpacing = (-0.25).sp,
  ),
  displayMedium = TextStyle(
    fontFamily = FrauncesFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 45.sp,
    lineHeight = 52.sp,
    letterSpacing = 0.sp,
  ),
  displaySmall = TextStyle(
    fontFamily = FrauncesFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 36.sp,
    lineHeight = 44.sp,
    letterSpacing = 0.sp,
  ),
  headlineLarge = TextStyle(
    fontFamily = FrauncesFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 32.sp,
    lineHeight = 40.sp,
    letterSpacing = 0.sp,
  ),
  headlineMedium = TextStyle(
    fontFamily = FrauncesFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 28.sp,
    lineHeight = 36.sp,
    letterSpacing = 0.sp,
  ),
  headlineSmall = TextStyle(
    fontFamily = FrauncesFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 24.sp,
    lineHeight = 32.sp,
    letterSpacing = 0.sp,
  ),
  titleLarge = TextStyle(
    fontFamily = InterFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 22.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.sp,
  ),
  titleMedium = TextStyle(
    fontFamily = InterFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.15.sp,
  ),
  titleSmall = TextStyle(
    fontFamily = InterFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.1.sp,
  ),
  bodyLarge = TextStyle(
    fontFamily = InterFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.5.sp,
  ),
  bodyMedium = TextStyle(
    fontFamily = InterFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.25.sp,
  ),
  bodySmall = TextStyle(
    fontFamily = InterFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.4.sp,
  ),
  labelLarge = TextStyle(
    fontFamily = InterFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.1.sp,
  ),
  labelMedium = TextStyle(
    fontFamily = InterFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp,
  ),
  labelSmall = TextStyle(
    fontFamily = InterFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 11.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp,
  ),
)

/**
 * Cortes emphasized: mismo cuerpo que el rol base, peso más alto.
 * Figma `M3/{role}/small-emphasized` · Typescale `Static/{Role}/Weight-emphasized`.
 * M3 Typography 1.5 trae slots emphasized; hasta declararlos en [AppTypography]
 * se exponen como extensiones para no caer al Roboto de defecto.
 */
val Typography.headlineSmallEmphasized: TextStyle
  get() = headlineSmall.copy(fontWeight = FontWeight.Black)

val Typography.labelSmallEmphasized: TextStyle
  get() = labelSmall.copy(fontWeight = FontWeight.Bold)

/**
 * Figma `MOT/album/day-kicker`: Fraunces Bold 11 / lh 16 / tracking 1.8.
 * Excepción editorial del masthead del álbum; no es un rol label.
 */
val AlbumDayKicker = TextStyle(
  fontFamily = FrauncesFamily,
  fontWeight = FontWeight.Bold,
  fontSize = 11.sp,
  lineHeight = 16.sp,
  letterSpacing = 1.8.sp,
)
