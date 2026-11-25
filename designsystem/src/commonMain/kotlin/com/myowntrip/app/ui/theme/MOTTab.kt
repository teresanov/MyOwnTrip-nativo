package com.myowntrip.app.ui.theme

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

/**
 * [Tab] con pads Figma / [MOTSpacing.Tab] vía overload de contenido
 * (evita el padding interno del slot `text=` de M3).
 */
@Composable
fun MOTTab(
  selected: Boolean,
  onClick: () -> Unit,
  label: String,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
) {
  MOTTab(
    selected = selected,
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
  ) {
    Text(
      text = label,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      modifier = Modifier.padding(
        horizontal = MOTSpacing.Tab.horizontal,
        vertical = MOTSpacing.Tab.vertical,
      ),
    )
  }
}

/**
 * [Tab] con contenido libre; el caller aplica [MOTSpacing.Tab] si hace falta.
 */
@Composable
fun MOTTab(
  selected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  content: @Composable ColumnScope.() -> Unit,
) {
  Tab(
    selected = selected,
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    content = content,
  )
}
