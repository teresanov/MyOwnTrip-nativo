package com.myowntrip.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Familias en Wasm/Desktop. Arrancan con las genéricas del sistema; quien hospeda el DS
 * (la galería o las demos del showcase) puede cargar los .ttf reales de designsystem y
 * asignarlos aquí ANTES de la primera composición.
 */
object MotSkiaFonts {
  var fraunces: FontFamily = FontFamily.Serif
  var inter: FontFamily = FontFamily.SansSerif
  var hand: FontFamily = FontFamily.Cursive
}

actual val FrauncesFamily: FontFamily get() = MotSkiaFonts.fraunces
actual val InterFamily: FontFamily get() = MotSkiaFonts.inter
actual val HandFamily: FontFamily get() = MotSkiaFonts.hand

actual val AppTypography: Typography get() = motTypography()

private fun motTypography() = Typography(
  displayLarge = TextStyle(fontFamily = FrauncesFamily, fontWeight = FontWeight.Normal, fontSize = 57.sp, lineHeight = 64.sp, letterSpacing = (-0.25).sp),
  displayMedium = TextStyle(fontFamily = FrauncesFamily, fontWeight = FontWeight.Normal, fontSize = 45.sp, lineHeight = 52.sp),
  displaySmall = TextStyle(fontFamily = FrauncesFamily, fontWeight = FontWeight.Normal, fontSize = 36.sp, lineHeight = 44.sp),
  headlineLarge = TextStyle(fontFamily = FrauncesFamily, fontWeight = FontWeight.Normal, fontSize = 32.sp, lineHeight = 40.sp),
  headlineMedium = TextStyle(fontFamily = FrauncesFamily, fontWeight = FontWeight.Normal, fontSize = 28.sp, lineHeight = 36.sp),
  headlineSmall = TextStyle(fontFamily = FrauncesFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 32.sp),
  titleLarge = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Normal, fontSize = 22.sp, lineHeight = 28.sp),
  titleMedium = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Medium, fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.15.sp),
  titleSmall = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp),
  bodyLarge = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.5.sp),
  bodyMedium = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.25.sp),
  bodySmall = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.4.sp),
  labelLarge = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp),
  labelMedium = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp),
  labelSmall = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Normal, fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp),
)
