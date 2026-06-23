package com.myowntrip.app.ui.theme

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Botones M3 con morph editorial 0→20dp (ADR 004).
 * FAB queda circular — usar [androidx.compose.material3.FloatingActionButton] sin morph.
 */

/** State-layer XSmall — Figma Button `58651:12123` (12×6). */
val MOTButtonXSmallContentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)

@Composable
fun MOTButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
  colors: ButtonColors = ButtonDefaults.buttonColors(),
  content: @Composable RowScope.() -> Unit,
) {
  Button(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    interactionSource = interactionSource,
    elevation = elevation,
    shape = rememberMOTButtonShape(interactionSource),
    colors = colors,
    content = content,
  )
}

/** Figma: **Button - tonal** `58651:11237` · Type=Square · Size=XSmall. */
@Composable
fun MOTTonalButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  contentPadding: PaddingValues = MOTButtonXSmallContentPadding,
  elevation: ButtonElevation? = null,
  content: @Composable RowScope.() -> Unit,
) {
  FilledTonalButton(
    onClick = onClick,
    modifier = modifier,
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
fun MOTTextButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  colors: ButtonColors = ButtonDefaults.textButtonColors(),
  content: @Composable RowScope.() -> Unit,
) {
  TextButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    interactionSource = interactionSource,
    shape = rememberMOTButtonShape(interactionSource),
    colors = colors,
    content = content,
  )
}

@Composable
fun MOTIconButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
  content: @Composable () -> Unit,
) {
  IconButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    interactionSource = interactionSource,
    shape = rememberMOTButtonShape(interactionSource),
    colors = colors,
    content = content,
  )
}
