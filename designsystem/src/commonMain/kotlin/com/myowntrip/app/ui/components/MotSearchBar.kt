package com.myowntrip.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTIcons
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.motPart

private val SearchBarShape = RoundedCornerShape(28.dp)
private val SearchBarMinHeight = 56.dp

/**
 * Barra de búsqueda con botón de filtros al final. Primer uso: la lista de viajes de Home.
 *
 * - Vacía: tarjeta con borde (OutlinedCard) y lupa al principio.
 * - Con texto: relleno surfaceContainerHigh, sin lupa, y botón «Borrar búsqueda».
 * - [onFilterClick]: abre el menú de filtros. El menú se coloca en [menu], anclado a la barra
 *   (arriba a la derecha), por ejemplo con [MotMenuDropdown].
 */
@Composable
fun MotSearchBar(
  query: String,
  onQueryChange: (String) -> Unit,
  placeholder: String,
  onFilterClick: () -> Unit,
  modifier: Modifier = Modifier,
  filterContentDescription: String = "Filtros y ordenación",
  menu: @Composable BoxScope.() -> Unit = {},
) {
  val hasQuery = query.isNotEmpty()
  Box(modifier = modifier.fillMaxWidth()) {
    if (hasQuery) {
      Surface(
        modifier = Modifier.fillMaxWidth().motPart("container"),
        shape = SearchBarShape,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
      ) {
        MotSearchBarRow(query, onQueryChange, placeholder, onFilterClick, filterContentDescription, showLeadingIcon = false)
      }
    } else {
      OutlinedCard(modifier = Modifier.fillMaxWidth().motPart("container")) {
        MotSearchBarRow(query, onQueryChange, placeholder, onFilterClick, filterContentDescription, showLeadingIcon = true)
      }
    }
    menu()
  }
}

@Composable
private fun MotSearchBarRow(
  query: String,
  onQueryChange: (String) -> Unit,
  placeholder: String,
  onFilterClick: () -> Unit,
  filterContentDescription: String,
  showLeadingIcon: Boolean,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .heightIn(min = SearchBarMinHeight),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    if (showLeadingIcon) {
      Icon(
        imageVector = MOTIcons.Search,
        contentDescription = null,
        modifier = Modifier
          .padding(start = MOTSpacing.layoutMd)
          .size(24.dp)
          .motPart("leadingIcon"),
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
    BasicTextField(
      value = query,
      onValueChange = onQueryChange,
      modifier = Modifier
        .weight(1f)
        .padding(
          start = if (showLeadingIcon) MOTSpacing.componentSm else 20.dp,
          end = MOTSpacing.componentSm,
          top = 14.dp,
          bottom = 14.dp,
        )
        .motPart("input"),
      textStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onSurface,
      ),
      singleLine = true,
      cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
      decorationBox = { innerTextField ->
        Box(contentAlignment = Alignment.CenterStart) {
          if (query.isEmpty()) {
            Text(
              text = placeholder,
              style = MaterialTheme.typography.bodyLarge,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
          }
          innerTextField()
        }
      },
    )
    if (query.isNotEmpty()) {
      MOTIconButton(
        onClick = { onQueryChange("") },
        modifier = Modifier
          .semantics { contentDescription = "Borrar búsqueda" }
          .motPart("clear"),
      ) {
        Icon(
          imageVector = MOTIcons.Close,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
    MOTIconButton(
      onClick = onFilterClick,
      modifier = Modifier
        .semantics { contentDescription = filterContentDescription }
        .motPart("filter"),
    ) {
      Icon(
        imageVector = MOTIcons.Tune,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}
