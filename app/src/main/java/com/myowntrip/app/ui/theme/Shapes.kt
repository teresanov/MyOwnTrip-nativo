package com.myowntrip.app.ui.theme

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.snap
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/** Corner tokens alineados a Figma `M3` · Shape (ADR 004). */
object MOTCorner {
  val None = 0.dp
  val Small = 8.dp
  val Medium = 12.dp
  val ButtonActive = 20.dp
}

/** Shapes de superficie — no aplicar a botones (usar [rememberMOTButtonShape]). */
val MOTThemeShapes = Shapes(
  extraSmall = RoundedCornerShape(MOTCorner.Small),
  small = RoundedCornerShape(MOTCorner.Small),
  medium = RoundedCornerShape(MOTCorner.Medium),
  large = RoundedCornerShape(MOTCorner.Medium),
  extraLarge = RoundedCornerShape(MOTCorner.ButtonActive),
)

/**
 * Morph 0dp → 20dp en hover, focus y pressed.
 * Para selected en toggles, pasar [forceActive] = true.
 */
@Composable
fun rememberMOTButtonShape(
  interactionSource: MutableInteractionSource = MutableInteractionSource(),
  forceActive: Boolean = false,
): Shape {
  val pressed by interactionSource.collectIsPressedAsState()
  val focused by interactionSource.collectIsFocusedAsState()
  val hovered by interactionSource.collectIsHoveredAsState()
  val interactive = forceActive || pressed || focused || hovered
  val reduceMotion = LocalReduceMotion.current

  val corner by animateDpAsState(
    targetValue = if (interactive) MOTCorner.ButtonActive else MOTCorner.None,
    animationSpec = if (reduceMotion) snap() else AppMotion.shapeMorphSpec(),
    label = "motButtonCorner",
  )

  return RoundedCornerShape(corner)
}
