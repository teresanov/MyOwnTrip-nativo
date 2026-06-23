package com.myowntrip.app.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Unarchive
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
import com.myowntrip.app.domain.model.Trip
import com.myowntrip.app.ui.components.TripListCard
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTSpacing
import java.time.LocalDate

/**
 * Fila de viaje con swipe tipo Gmail (archivar / eliminar) y menú por tap (accesibilidad).
 * No usar en el hero destacado — solo en listas «Más viajes» / pasados.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableTripListRow(
  trip: Trip,
  today: LocalDate,
  showArchivedActions: Boolean,
  onClick: () -> Unit,
  onArchive: () -> Unit,
  onUnarchive: () -> Unit,
  onDeleteRequest: () -> Unit,
  modifier: Modifier = Modifier,
  previewCoverRes: Int? = null,
) {
  var menuExpanded by remember { mutableStateOf(false) }
  val dismissState = rememberSwipeToDismissBoxState(
    confirmValueChange = { value ->
      when (value) {
        SwipeToDismissBoxValue.StartToEnd -> {
          if (showArchivedActions) onUnarchive() else onArchive()
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

  val archiveLabel = if (showArchivedActions) "Restaurar" else "Archivar"
  val archiveActionLabel = if (showArchivedActions) "Restaurar viaje" else "Archivar viaje"

  SwipeToDismissBox(
    modifier = modifier.semantics {
      customActions = listOf(
        CustomAccessibilityAction(archiveActionLabel) {
          if (showArchivedActions) onUnarchive() else onArchive()
          true
        },
        CustomAccessibilityAction("Eliminar viaje") {
          onDeleteRequest()
          true
        },
      )
    },
    state = dismissState,
    enableDismissFromStartToEnd = true,
    enableDismissFromEndToStart = true,
    backgroundContent = {
      val direction = dismissState.dismissDirection
      when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(MaterialTheme.colorScheme.secondaryContainer)
              .padding(horizontal = MOTSpacing.layoutMd),
            contentAlignment = Alignment.CenterStart,
          ) {
            Icon(
              imageVector = if (showArchivedActions) Icons.Outlined.Unarchive else Icons.Outlined.Archive,
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
              .padding(horizontal = MOTSpacing.layoutMd),
            contentAlignment = Alignment.CenterEnd,
          ) {
            Icon(
              imageVector = Icons.Default.Delete,
              contentDescription = "Eliminar",
              tint = MaterialTheme.colorScheme.onErrorContainer,
            )
          }
        }
        SwipeToDismissBoxValue.Settled -> Unit
      }
    },
    content = {
      Box {
        TripListCard(
          trip = trip,
          today = today,
          previewCoverRes = previewCoverRes,
          onClick = onClick,
        )
        MOTIconButton(
          onClick = { menuExpanded = true },
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(top = 4.dp, end = 4.dp),
        ) {
          Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Acciones del viaje",
            modifier = Modifier.size(22.dp),
          )
        }
        DropdownMenu(
          expanded = menuExpanded,
          onDismissRequest = { menuExpanded = false },
        ) {
          if (showArchivedActions) {
            DropdownMenuItem(
              text = { Text("Restaurar") },
              leadingIcon = {
                Icon(Icons.Outlined.Unarchive, contentDescription = null)
              },
              onClick = {
                menuExpanded = false
                onUnarchive()
              },
            )
          } else {
            DropdownMenuItem(
              text = { Text("Archivar") },
              leadingIcon = {
                Icon(Icons.Outlined.Archive, contentDescription = null)
              },
              onClick = {
                menuExpanded = false
                onArchive()
              },
            )
          }
          DropdownMenuItem(
            text = { Text("Eliminar", color = MaterialTheme.colorScheme.error) },
            leadingIcon = {
              Icon(
                Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
              )
            },
            onClick = {
              menuExpanded = false
              onDeleteRequest()
            },
          )
        }
      }
    },
  )
}
