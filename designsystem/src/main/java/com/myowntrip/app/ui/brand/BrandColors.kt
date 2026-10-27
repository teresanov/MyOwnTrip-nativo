package com.myowntrip.app.ui.brand

import androidx.compose.ui.graphics.Color

/**
 * Colores de marca (logo / wordmark), alineados con roles M3.
 * Tinta = `primary` Light (#4A5864).
 * Ocre logo = Palettes/Tertiary 60 (#C48328); UI `tertiary` sigue en #825513.
 */
object BrandColors {
  val Ink = Color(0xFF4A5864)
  /** Reservado (token Figma `ink-deep`); lockups jun 2026 usan `ink` u `ocre`. */
  val InkDeep = Ink
  val Paper = Color(0xFFF4F0E8)
  val AccentOcre = Color(0xFFC48328)
  val OnDark = Color(0xFFF9EFE2)

  /**
   * MOT M/T muted — Figma Appearance 85% con fill enlazado a `ink`/`on-dark` al 100%.
   * En Compose: alpha en color (equivalente a layer opacity).
   */
  const val MotMutedLayerOpacity = 0.85f
}
