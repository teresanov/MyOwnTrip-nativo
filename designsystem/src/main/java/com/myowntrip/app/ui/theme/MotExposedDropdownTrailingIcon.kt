package com.myowntrip.app.ui.theme

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate

/** Chevron de menús expuestos: `keyboard_arrow_down` (catálogo MOTIcons / Figma). */
@Composable
fun MotExposedDropdownTrailingIcon(
  expanded: Boolean,
  modifier: Modifier = Modifier,
) {
  val rotation by animateFloatAsState(
    targetValue = if (expanded) 180f else 0f,
    label = "exposedDropdownChevron",
  )
  Icon(
    imageVector = MOTIcons.KeyboardArrowDown,
    contentDescription = null,
    modifier = modifier.rotate(rotation),
  )
}