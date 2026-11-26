package com.myowntrip.app.ui.features.wallet

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.myowntrip.app.domain.model.WalletEntry
import com.myowntrip.app.domain.plan.WalletPlanPlacementInfo
import com.myowntrip.app.ui.components.MotSwipeableRow
import com.myowntrip.app.ui.theme.MOTHorizontalDivider

/**
 * Fila de documento con swipe (archivar / eliminar) y menú por tap (accesibilidad).
 * No usar en el carrusel «Próximos» — solo en la lista plana.
 * El deslizamiento y el menú son MotSwipeableRow (design system).
 */
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
  MotSwipeableRow(
    archived = showArchivedActions,
    itemNoun = "documento",
    onArchive = onArchive,
    onUnarchive = onUnarchive,
    onDeleteRequest = onDeleteRequest,
    modifier = modifier,
  ) { menuButton ->
    WalletDocumentRow(
      entry = entry,
      planPlacement = planPlacement,
      onClick = onClick,
      trailingActions = { menuButton(Modifier) },
    )
  }
  if (showDivider) {
    MOTHorizontalDivider(inset = true)
  }
}
