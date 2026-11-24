package com.myowntrip.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Tipografía MOT en la galería Wasm/Desktop.
 * Fraunces/Inter bundled (`res/font`) viven en el módulo Android; aquí usamos
 * Serif/Sans del runtime hasta portar composeResources.
 */
private val DisplayFamily = FontFamily.Serif
private val UiFamily = FontFamily.SansSerif

val AppTypography = Typography(
  displayLarge = TextStyle(fontFamily = DisplayFamily, fontWeight = FontWeight.Normal, fontSize = 57.sp, lineHeight = 64.sp, letterSpacing = (-0.25).sp),
  displayMedium = TextStyle(fontFamily = DisplayFamily, fontWeight = FontWeight.Normal, fontSize = 45.sp, lineHeight = 52.sp),
  displaySmall = TextStyle(fontFamily = DisplayFamily, fontWeight = FontWeight.Normal, fontSize = 36.sp, lineHeight = 44.sp),
  headlineLarge = TextStyle(fontFamily = DisplayFamily, fontWeight = FontWeight.Normal, fontSize = 32.sp, lineHeight = 40.sp),
  headlineMedium = TextStyle(fontFamily = DisplayFamily, fontWeight = FontWeight.Normal, fontSize = 28.sp, lineHeight = 36.sp),
  headlineSmall = TextStyle(fontFamily = DisplayFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 32.sp),
  titleLarge = TextStyle(fontFamily = UiFamily, fontWeight = FontWeight.Normal, fontSize = 22.sp, lineHeight = 28.sp),
  titleMedium = TextStyle(fontFamily = UiFamily, fontWeight = FontWeight.Medium, fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.15.sp),
  titleSmall = TextStyle(fontFamily = UiFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp),
  bodyLarge = TextStyle(fontFamily = UiFamily, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.5.sp),
  bodyMedium = TextStyle(fontFamily = UiFamily, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.25.sp),
  bodySmall = TextStyle(fontFamily = UiFamily, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.4.sp),
  labelLarge = TextStyle(fontFamily = UiFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp),
  labelMedium = TextStyle(fontFamily = UiFamily, fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp),
  labelSmall = TextStyle(fontFamily = UiFamily, fontWeight = FontWeight.Normal, fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp),
)
