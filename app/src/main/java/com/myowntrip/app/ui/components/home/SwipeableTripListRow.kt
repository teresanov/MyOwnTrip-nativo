package com.myowntrip.app.ui.components.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.myowntrip.app.domain.model.Trip
import com.myowntrip.app.ui.components.MotSwipeableRow
import com.myowntrip.app.ui.components.TripListCard
import java.time.LocalDate

/**
 * Fila de viaje con swipe tipo Gmail (archivar / eliminar) y menú por tap (accesibilidad).
 * No usar en el hero destacado — solo en listas «Más viajes» / pasados.
 * El deslizamiento y el menú son MotSwipeableRow (design system).
 */
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
  MotSwipeableRow(
    archived = showArchivedActions,
    itemNoun = "viaje",
    onArchive = onArchive,
    onUnarchive = onUnarchive,
    onDeleteRequest = onDeleteRequest,
    modifier = modifier,
  ) { menuButton ->
    Box {
      TripListCard(
        trip = trip,
        today = today,
        previewCoverRes = previewCoverRes,
        onClick = onClick,
      )
      menuButton(
        Modifier
          .align(Alignment.TopEnd)
          .padding(top = 4.dp, end = 4.dp),
      )
    }
  }
}
