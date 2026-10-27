package com.myowntrip.app.ui.theme

import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
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
 * Morph 0dp → 20dp en hover, focus y pressed — asimétrico (ADR 004 rev. squish + muelle):
 * entrada rápida (el botón reacciona al dedo) y liberación con muelle que rebasa y asienta.
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
    animationSpec = when {
      reduceMotion -> snap()
      interactive -> AppMotion.pressInSpec()
      else -> AppMotion.releaseSpringSpec(Dp.VisibilityThreshold)
    },
    label = "motButtonCorner",
  )

  return RoundedCornerShape(corner)
}

/**
 * Squish de pulsación: el dedo comprime el botón a [AppMotion.PressedButtonScale] con la
 * misma entrada rápida que la esquina; al soltar, muelle con ligero rebase. Solo actúa en
 * pressed — hover y focus morfan la esquina sin escalar. Pasar el MISMO interactionSource
 * que recibe el botón, o la escala nunca se disparará.
 */
@Composable
fun Modifier.motButtonPressSquish(interactionSource: InteractionSource): Modifier {
  val pressed by interactionSource.collectIsPressedAsState()
  val reduceMotion = LocalReduceMotion.current

  val scale by animateFloatAsState(
    targetValue = if (pressed) AppMotion.PressedButtonScale else 1f,
    animationSpec = when {
      reduceMotion -> snap()
      pressed -> AppMotion.pressInSpec()
      else -> AppMotion.releaseSpringSpec()
    },
    label = "motButtonSquish",
  )

  return graphicsLayer {
    scaleX = scale
    scaleY = scale
  }
}
