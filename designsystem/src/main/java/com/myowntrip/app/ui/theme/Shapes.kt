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

/**
 * SoT de shape de superficie: roles M3 ([Shapes]) ↔ Figma colección Shape (`Corner/…`).
 *
 * Consumo en UI: `MaterialTheme.shapes.extraSmall|small|medium|large|extraLarge`.
 * No hay un segundo objeto semántico paralelo (`MOTCorner`).
 *
 * | M3 / Compose | Figma | dp |
 * |--------------|-------|---:|
 * | `shapes.extraSmall` | `Corner/Extra-small` | 4 |
 * | `shapes.small` | `Corner/Small` | 8 |
 * | `shapes.medium` | `Corner/Medium` | 12 |
 * | `shapes.large` | `Corner/Large` | 16 |
 * | `shapes.extraLarge` | `Corner/Extra-large` | 28 |
 *
 * Botones: [rememberMOTButtonShape] (ADR 004) — fuera de la escala `shapes.*`.
 */
val MOTThemeShapes = Shapes(
  extraSmall = RoundedCornerShape(4.dp),
  small = RoundedCornerShape(8.dp),
  medium = RoundedCornerShape(12.dp),
  large = RoundedCornerShape(16.dp),
  extraLarge = RoundedCornerShape(28.dp),
)

/** Radios del morph de botón (ADR 004). No forman parte de [MOTThemeShapes]. */
private object MotButtonCornerDp {
  val Rest = 0.dp
  val Active = 20.dp
}

/**
 * Morph 0dp → 20dp en hover, focus y pressed — asimétrico (ADR 004 rev. squish + muelle):
 * entrada rápida (el botón reacciona al dedo) y liberación con muelle que rebasa y asienta.
 * Para selected en toggles, pasar [forceActive] = true.
 *
 * Figma: `Corner/None` ↔ `Corner/Large-increased`.
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
    targetValue = if (interactive) MotButtonCornerDp.Active else MotButtonCornerDp.Rest,
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
