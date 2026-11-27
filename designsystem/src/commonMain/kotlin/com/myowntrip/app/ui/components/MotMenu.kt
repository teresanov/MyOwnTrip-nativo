package com.myowntrip.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.MOTIcons
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.motPart

/** Medidas del menú de selección (Figma: Menu, filtro de Home). */
object MotMenuDefaults {
  val Width = 328.dp
  val ItemHeight = 44.dp
  val LeadingIconSize = 20.dp
  val Elevation = 3.dp
}

/**
 * Menú de selección desplegable, anclado a su botón: surfaceContainerLow, esquina Large y sombra baja.
 * Dentro van grupos ([MotMenuGroup]) con su título ([MotMenuSectionLabel]) y sus opciones ([MotMenuItem]).
 */
@Composable
fun MotMenuDropdown(
  expanded: Boolean,
  onDismissRequest: () -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable ColumnScope.() -> Unit,
) {
  DropdownMenu(
    expanded = expanded,
    onDismissRequest = onDismissRequest,
    modifier = modifier,
    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
    shadowElevation = MotMenuDefaults.Elevation,
    shape = MaterialTheme.shapes.large,
  ) {
    MotMenuContent(Modifier.width(MotMenuDefaults.Width), content)
  }
}

/** El mismo menú sin ventana propia: para colocarlo a mano en la pantalla (o enseñarlo quieto). */
@Composable
fun MotMenuPanel(
  modifier: Modifier = Modifier,
  content: @Composable ColumnScope.() -> Unit,
) {
  Surface(
    modifier = modifier
      .width(MotMenuDefaults.Width)
      .clip(MaterialTheme.shapes.large)
      .motPart("panel"),
    shape = MaterialTheme.shapes.large,
    color = MaterialTheme.colorScheme.surfaceContainerLow,
    shadowElevation = MotMenuDefaults.Elevation,
    tonalElevation = 0.dp,
  ) {
    MotMenuContent(Modifier, content)
  }
}

@Composable
private fun MotMenuContent(modifier: Modifier, content: @Composable ColumnScope.() -> Unit) {
  Column(
    modifier = modifier.padding(vertical = MOTSpacing.Menu.groupGap),
    verticalArrangement = Arrangement.spacedBy(MOTSpacing.Menu.groupGap),
    content = content,
  )
}

/** Un grupo de opciones del menú (p. ej. «Mostrar» u «Ordenar»). */
@Composable
fun MotMenuGroup(
  modifier: Modifier = Modifier,
  content: @Composable ColumnScope.() -> Unit,
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .clip(MaterialTheme.shapes.small),
    content = content,
  )
}

/** Título de un grupo: labelLarge en onSurfaceVariant. No se puede pulsar. */
@Composable
fun MotMenuSectionLabel(text: String, modifier: Modifier = Modifier) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = MOTSpacing.componentXs)
      .heightIn(min = 32.dp)
      .padding(start = MOTSpacing.componentMd, end = MOTSpacing.componentMd, top = 6.dp),
    contentAlignment = Alignment.CenterStart,
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.labelLarge,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.motPart("sectionLabel"),
    )
  }
}

/**
 * Opción del menú. Seleccionada: relleno secondaryContainer con borde outline y check en primary;
 * el lector de pantalla anuncia «Seleccionado». [destructive]: texto en color error.
 */
@Composable
fun MotMenuItem(
  label: String,
  selected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  destructive: Boolean = false,
) {
  val scheme = MaterialTheme.colorScheme
  val itemShape = MaterialTheme.shapes.medium

  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = MOTSpacing.componentXs, vertical = MOTSpacing.Menu.groupGap),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(MotMenuDefaults.ItemHeight)
        .then(
          if (selected) {
            Modifier
              .background(scheme.secondaryContainer, itemShape)
              .border(1.dp, scheme.outline, itemShape)
          } else {
            Modifier
          },
        )
        .motPart(if (selected) "selectedItem" else "item")
        .clickable(onClick = onClick)
        .padding(horizontal = 12.dp)
        .semantics(mergeDescendants = true) {
          if (selected) stateDescription = "Seleccionado"
        },
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      Box(
        modifier = Modifier.size(MotMenuDefaults.LeadingIconSize),
        contentAlignment = Alignment.Center,
      ) {
        if (selected) {
          Icon(
            imageVector = MOTIcons.Check,
            contentDescription = null,
            modifier = Modifier.size(MotMenuDefaults.LeadingIconSize).motPart("check"),
            tint = scheme.primary,
          )
        }
      }
      Text(
        text = label,
        style = MaterialTheme.typography.labelLarge,
        color = when {
          destructive -> scheme.error
          selected -> scheme.onSecondaryContainer
          else -> scheme.onSurface
        },
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.weight(1f).motPart(if (selected) "selectedLabel" else "label"),
      )
    }
  }
}
