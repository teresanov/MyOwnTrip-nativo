package com.myowntrip.app.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * [HorizontalDivider] de producto.
 *
 * - Color SoT: [MaterialTheme.colorScheme.outlineVariant] ↔ Figma `Schemes/Outline Variant`.
 * - [inset]: padding H [MOTSpacing.screenHorizontal] ↔ Figma Horizontal/Inset|Middle-inset.
 */
@Composable
fun MOTHorizontalDivider(
  modifier: Modifier = Modifier,
  inset: Boolean = false,
  color: Color = MaterialTheme.colorScheme.outlineVariant,
) {
  HorizontalDivider(
    modifier = if (inset) {
      modifier.padding(horizontal = MOTSpacing.screenHorizontal)
    } else {
      modifier
    },
    color = color,
  )
}
