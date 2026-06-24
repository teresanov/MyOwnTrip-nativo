package com.myowntrip.app.ui.features.wallet

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
import androidx.compose.material3.HorizontalDivider
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
import com.myowntrip.app.domain.model.WalletEntry
import com.myowntrip.app.domain.plan.WalletPlanPlacementInfo
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTSpacing

/**
 * Fila de documento con swipe (archivar / eliminar) y menú por tap (accesibilidad).
 * No usar en el carrusel «Próximos» — solo en la lista plana.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableWalletDocumentRow(
  entry: WalletEntry,
  showArchivedActions: Boolean,
  onClick: () -> Unit,
  onArchive: () -> Unit,
  onUnarchive: () -> Unit,
  onDeleteRequest: () -> Unit,
  modifier: Modifier = Modifier,
  planPlacement: WalletPlanPlacementInfo? = null,
  showDivider: Boolean = true,
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
  val archiveActionLabel = if (showArchivedActions) "Restaurar documento" else "Archivar documento"

  SwipeToDismissBox(
    modifier = modifier.semantics {
      customActions = listOf(
        CustomAccessibilityAction(archiveActionLabel) {
          if (showArchivedActions) onUnarchive() else onArchive()
          true
        },
        CustomAccessibilityAction("Eliminar documento") {
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
      WalletDocumentRow(
        entry = entry,
        planPlacement = planPlacement,
        onClick = onClick,
        trailingActions = {
          MOTIconButton(onClick = { menuExpanded = true }) {
            Icon(
              imageVector = Icons.Default.MoreVert,
              contentDescription = "Acciones del documento",
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
        },
      )
    },
  )
  if (showDivider) {
    HorizontalDivider(
      modifier = Modifier.padding(horizontal = MOTSpacing.screenHorizontal),
      color = MaterialTheme.colorScheme.outlineVariant,
    )
  }
}
