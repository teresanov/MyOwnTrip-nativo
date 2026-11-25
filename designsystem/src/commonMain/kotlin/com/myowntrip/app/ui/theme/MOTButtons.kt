package com.myowntrip.app.ui.theme

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

/**
 * Botones M3 con morph editorial 0→20dp (ADR 004).
 * FAB queda circular — usar [androidx.compose.material3.FloatingActionButton] sin morph.
 *
 * Tamaños = ejes Figma `Size` del set Button (`57994:2227` y gemelos).
 * Icon button: `Size` × `Width` × `Type` del set `57994:10081`.
 */

/** Figma Button `Size`. Pads = State-layer medido en DS. */
enum class MOTButtonSize {
  XSmall,
  Small,
  Medium,
  Large,
  ;

  val contentPadding: PaddingValues
    get() = when (this) {
      XSmall -> MOTButtonXSmallContentPadding
      Small -> MOTButtonSmallContentPadding
      Medium -> MOTButtonMediumContentPadding
      Large -> MOTButtonLargeContentPadding
    }

  /** Glifo del leading icon (Figma instancia Icon). */
  val leadingIconSize: Dp
    get() = when (this) {
      XSmall, Small -> 20.dp
      Medium -> 24.dp
      Large -> 32.dp
    }

  /** Hueco icono–label (Figma `itemSpacing` del State-layer). */
  val leadingIconGap: Dp
    get() = when (this) {
      XSmall -> 4.dp
      Small, Medium -> 8.dp
      Large -> 12.dp
    }
}

/** Figma Icon button `Size`. */
enum class MOTIconButtonSize {
  XSmall,
  Small,
  Medium,
  Large,
}

/** Figma Icon button `Width`. */
enum class MOTIconButtonWidth {
  Narrow,
  Default,
  Wide,
}

/**
 * Figma Icon button `Type`.
 * Square: reposo 0dp → 20dp al interactuar (ADR 004).
 * Round: reposo ya a 20dp (`Corner/Large-increased`). No es círculo; el FAB es el único `Corner/Full`.
 */
enum class MOTIconButtonType {
  Square,
  Round,
}

fun MOTIconButtonSize.containerSize(width: MOTIconButtonWidth): DpSize {
  val height = when (this) {
    MOTIconButtonSize.XSmall -> 32.dp
    MOTIconButtonSize.Small -> 40.dp
    MOTIconButtonSize.Medium -> 56.dp
    MOTIconButtonSize.Large -> 96.dp
  }
  val w = when (this) {
    MOTIconButtonSize.XSmall -> when (width) {
      MOTIconButtonWidth.Narrow -> 28.dp
      MOTIconButtonWidth.Default -> 32.dp
      MOTIconButtonWidth.Wide -> 40.dp
    }
    MOTIconButtonSize.Small -> when (width) {
      MOTIconButtonWidth.Narrow -> 32.dp
      MOTIconButtonWidth.Default -> 40.dp
      MOTIconButtonWidth.Wide -> 52.dp
    }
    MOTIconButtonSize.Medium -> when (width) {
      MOTIconButtonWidth.Narrow -> 48.dp
      MOTIconButtonWidth.Default -> 56.dp
      MOTIconButtonWidth.Wide -> 72.dp
    }
    MOTIconButtonSize.Large -> when (width) {
      MOTIconButtonWidth.Narrow -> 64.dp
      MOTIconButtonWidth.Default -> 96.dp
      MOTIconButtonWidth.Wide -> 128.dp
    }
  }
  return DpSize(w, height)
}

/** State-layer XSmall — Figma Button Size=XSmall (12×6). */
val MOTButtonXSmallContentPadding = PaddingValues(
  horizontal = MOTSpacing.ButtonXSmall.horizontal,
  vertical = MOTSpacing.ButtonXSmall.vertical,
)

/** Figma Size=Small (24×8). Default de [MOTButton] / [MOTOutlinedButton]. */
val MOTButtonSmallContentPadding = PaddingValues(
  horizontal = MOTSpacing.ButtonSmall.horizontal,
  vertical = MOTSpacing.ButtonSmall.vertical,
)

val MOTButtonMediumContentPadding = PaddingValues(
  horizontal = MOTSpacing.ButtonMedium.horizontal,
  vertical = MOTSpacing.ButtonMedium.vertical,
)

val MOTButtonLargeContentPadding = PaddingValues(
  horizontal = MOTSpacing.ButtonLarge.horizontal,
  vertical = MOTSpacing.ButtonLarge.vertical,
)

@Composable
fun MOTButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  size: MOTButtonSize = MOTButtonSize.Small,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  contentPadding: PaddingValues = size.contentPadding,
  elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
  colors: ButtonColors = ButtonDefaults.buttonColors(),
  content: @Composable RowScope.() -> Unit,
) {
  Button(
    onClick = onClick,
    modifier = modifier.motButtonPressSquish(interactionSource),
    enabled = enabled,
    interactionSource = interactionSource,
    elevation = elevation,
    shape = rememberMOTButtonShape(interactionSource),
    colors = colors,
    contentPadding = contentPadding,
    content = content,
  )
}

/** Figma: **Button - tonal** · Type=Square · default Size=XSmall. */
@Composable
fun MOTTonalButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  size: MOTButtonSize = MOTButtonSize.XSmall,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  contentPadding: PaddingValues = size.contentPadding,
  elevation: ButtonElevation? = null,
  content: @Composable RowScope.() -> Unit,
) {
  FilledTonalButton(
    onClick = onClick,
    modifier = modifier.motButtonPressSquish(interactionSource),
    enabled = enabled,
    interactionSource = interactionSource,
    elevation = elevation,
    shape = rememberMOTButtonShape(interactionSource),
    colors = ButtonDefaults.filledTonalButtonColors(),
    contentPadding = contentPadding,
    content = content,
  )
}

@Composable
fun MOTOutlinedButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  size: MOTButtonSize = MOTButtonSize.Small,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  contentPadding: PaddingValues = size.contentPadding,
  colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
  content: @Composable RowScope.() -> Unit,
) {
  OutlinedButton(
    onClick = onClick,
    modifier = modifier.motButtonPressSquish(interactionSource),
    enabled = enabled,
    interactionSource = interactionSource,
    shape = rememberMOTButtonShape(interactionSource),
    colors = colors,
    contentPadding = contentPadding,
    content = content,
  )
}

/**
 * Figma: **Button - text** Type=Square.
 * Label: siempre [androidx.compose.material3.Typography.labelLarge].
 */
@Composable
fun MOTTextButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  size: MOTButtonSize = MOTButtonSize.XSmall,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  colors: ButtonColors = ButtonDefaults.textButtonColors(),
  content: @Composable RowScope.() -> Unit,
) {
  TextButton(
    onClick = onClick,
    modifier = modifier.motButtonPressSquish(interactionSource),
    enabled = enabled,
    interactionSource = interactionSource,
    shape = rememberMOTButtonShape(interactionSource),
    colors = colors,
    contentPadding = size.contentPadding,
    content = content,
  )
}

/**
 * Figma Icon button Type=Square · reposo `Corner/None` · interacción `Corner/Large-increased`.
 * [MOTIconButtonType.Round] deja el reposo en 20dp (ADR 004). No círculo.
 */
@Composable
fun MOTIconButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  size: MOTIconButtonSize = MOTIconButtonSize.Small,
  width: MOTIconButtonWidth = MOTIconButtonWidth.Default,
  type: MOTIconButtonType = MOTIconButtonType.Square,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
  content: @Composable () -> Unit,
) {
  val box = size.containerSize(width)
  IconButton(
    onClick = onClick,
    modifier = modifier
      .size(box.width, box.height)
      .motButtonPressSquish(interactionSource),
    enabled = enabled,
    interactionSource = interactionSource,
    shape = rememberMOTButtonShape(
      interactionSource = interactionSource,
      forceActive = type == MOTIconButtonType.Round,
    ),
    colors = colors,
    content = content,
  )
}
