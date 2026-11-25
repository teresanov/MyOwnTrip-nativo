package com.myowntrip.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

expect val FrauncesFamily: FontFamily
expect val InterFamily: FontFamily
expect val HandFamily: FontFamily
expect val AppTypography: Typography

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

/**
 * Figma `MOT/album/hand-note`: pluma del diario (HandFamily, Shantell Sans Medium 17/22).
 * Solo notas `JournalTextStyle.HAND`, nunca UI funcional.
 */
val AlbumHandNote = TextStyle(
  fontFamily = HandFamily,
  fontWeight = FontWeight.Medium,
  fontSize = 17.sp,
  lineHeight = 22.sp,
)
