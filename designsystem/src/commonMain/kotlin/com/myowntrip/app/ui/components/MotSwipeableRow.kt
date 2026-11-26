package com.myowntrip.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTIcons
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.motPart

/**
 * Fila que se desliza para archivar (o restaurar, si [archived]) hacia la derecha y para eliminar
 * hacia la izquierda. El deslizamiento no completa la acción solo: avisa y la fila vuelve a su sitio
 * (eliminar pide confirmación fuera).
 *
 * Alternativas al gesto, siempre presentes:
 * - el botón de menú «⋮» ([content] recibe `menuButton` y lo coloca donde toque);
 * - dos acciones para lectores de pantalla («Archivar …», «Eliminar …»).
 *
 * [itemNoun]: «documento», «viaje»… para los nombres accesibles.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotSwipeableRow(
  archived: Boolean,
  itemNoun: String,
  onArchive: () -> Unit,
  onUnarchive: () -> Unit,
  onDeleteRequest: () -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable (menuButton: @Composable (Modifier) -> Unit) -> Unit,
) {
  var menuExpanded by remember { mutableStateOf(false) }
  val dismissState = rememberSwipeToDismissBoxState(
    confirmValueChange = { value ->
      when (value) {
        SwipeToDismissBoxValue.StartToEnd -> {
          if (archived) onUnarchive() else onArchive()
          false
        }
        SwipeToDismissBoxValue.EndToStart -> {
          onDeleteRequest()
          false
        }
        SwipeToDismissBoxValue.Settled -> false
      }
    },
    positionalThreshold = { distance -> distance * 0.45f },
  )

  val archiveLabel = if (archived) "Restaurar" else "Archivar"

  SwipeToDismissBox(
    modifier = modifier
      .motPart("row")
      .semantics {
        customActions = listOf(
          CustomAccessibilityAction("$archiveLabel $itemNoun") {
            if (archived) onUnarchive() else onArchive()
            true
          },
          CustomAccessibilityAction("Eliminar $itemNoun") {
            onDeleteRequest()
            true
          },
        )
      },
    state = dismissState,
    enableDismissFromStartToEnd = true,
    enableDismissFromEndToStart = true,
    backgroundContent = {
      when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(MaterialTheme.colorScheme.secondaryContainer)
              .padding(horizontal = MOTSpacing.ListItem.horizontal),
            contentAlignment = Alignment.CenterStart,
          ) {
            Icon(
              imageVector = if (archived) MOTIcons.Unarchive else MOTIcons.Archive,
              contentDescription = archiveLabel,
              tint = MaterialTheme.colorScheme.onSecondaryContainer,
            )
          }
        }
        SwipeToDismissBoxValue.EndToStart -> {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(MaterialTheme.colorScheme.errorContainer)
              .padding(horizontal = MOTSpacing.ListItem.horizontal),
            contentAlignment = Alignment.CenterEnd,
          ) {
            Icon(
              imageVector = MOTIcons.Delete,
              contentDescription = "Eliminar",
              tint = MaterialTheme.colorScheme.onErrorContainer,
            )
          }
        }
        SwipeToDismissBoxValue.Settled -> Unit
      }
    },
    content = {
      content { buttonModifier ->
        // El menú se ancla al botón: se abre justo debajo de «⋮».
        Box(buttonModifier) {
          MOTIconButton(onClick = { menuExpanded = true }, modifier = Modifier.motPart("menu")) {
            Icon(
              imageVector = MOTIcons.MoreVert,
              contentDescription = "Acciones del $itemNoun",
              modifier = Modifier.size(22.dp),
            )
          }
          DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
          ) {
            DropdownMenuItem(
              text = { Text(archiveLabel) },
              leadingIcon = {
                Icon(if (archived) MOTIcons.Unarchive else MOTIcons.Archive, contentDescription = null)
              },
              onClick = {
                menuExpanded = false
                if (archived) onUnarchive() else onArchive()
              },
            )
            DropdownMenuItem(
              text = { Text("Eliminar", color = MaterialTheme.colorScheme.error) },
              leadingIcon = {
                Icon(MOTIcons.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error)
              },
              onClick = {
                menuExpanded = false
                onDeleteRequest()
              },
            )
          }
        }
      }
    },
  )
}
