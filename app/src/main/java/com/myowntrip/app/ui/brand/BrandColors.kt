package com.myowntrip.app.ui.brand

import androidx.compose.ui.graphics.Color

/**
 * Colores de marca (logo / wordmark), alineados con roles M3.
 * Tinta = `primary` Light (#4A5864) · Ocre = `tertiary` (#825513).
 */
object BrandColors {
  val Ink = Color(0xFF4A5864)
  /** Misma tinta que [Ink] desde jun 2026 (gris-azul unificado). */
  val InkDeep = Ink
  val Paper = Color(0xFFF4F0E8)
  val AccentOcre = Color(0xFF825513)
  val OnDark = Color(0xFFF9EFE2)
}
