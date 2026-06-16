package com.myowntrip.app.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Escala alineada con Figma · colección **M3 Spacing & Layout** (Mobile Compact).
 * Solo layout de pantalla — no sustituye padding interno de componentes M3.
 */
object MOTSpacing {
  val screenHorizontal = 16.dp
  /** Colchón inferior en contenido scrolleable (zona segura gesto / barra / FAB). */
  val screenContentBottom = 24.dp
  val layoutMd = 16.dp
  val layoutLg = 24.dp
  val componentSm = 8.dp
  val componentXs = 4.dp
  val gutterGrid = 16.dp
  /** Lista con FAB flotante en la misma pantalla. */
  val screenContentBottomWithFab = 88.dp
}
