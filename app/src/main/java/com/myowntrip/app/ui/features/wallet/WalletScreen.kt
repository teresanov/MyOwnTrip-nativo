package com.myowntrip.app.ui.features.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myowntrip.app.domain.model.EntryType
import com.myowntrip.app.domain.model.Trip
import com.myowntrip.app.domain.model.WalletEntry
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MyOwnTripTheme
import com.myowntrip.app.ui.theme.rememberMOTButtonShape
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val SpanishLocale = Locale("es", "ES")
private val DateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", SpanishLocale)
private val TimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun WalletScreen(
  trip: Trip?,
  entries: List<WalletEntry>,
  onAddEntry: () -> Unit,
  onImportEntry: () -> Unit = onAddEntry,
  onEntryClick: (String) -> Unit,
  onDeleteEntry: ((String) -> Unit)? = null,
  embeddedInTrip: Boolean = false,
  modifier: Modifier = Modifier,
) {
  var entryPendingDelete by remember { mutableStateOf<WalletEntry?>(null) }

  if (entryPendingDelete != null) {
    WalletDeleteEntryDialog(
      entryTitle = entryPendingDelete!!.title,
      onDismiss = { entryPendingDelete = null },
      onConfirmDelete = {
        onDeleteEntry?.invoke(entryPendingDelete!!.id)
        entryPendingDelete = null
      },
    )
  }

  if (entries.isEmpty()) {
    WalletEmptyState(
      onAddEntry = onAddEntry,
      onImportEntry = onImportEntry,
      modifier = modifier.fillMaxSize(),
    )
    return
  }

  val highlights = remember(entries) {
    entries
      .sortedWith(compareBy<WalletEntry> { it.date == null }.thenBy { it.date })
      .take(4)
  }

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(bottom = MOTSpacing.screenContentBottom),
    verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
  ) {
    item {
      WalletHeader(
        trip = trip,
        entryCount = entries.size,
        showTitle = !embeddedInTrip,
        modifier = Modifier.padding(
          horizontal = MOTSpacing.screenHorizontal,
          vertical = MOTSpacing.componentSm,
        ),
      )
    }

    item {
      WalletQuickActions(
        onAddEntry = onAddEntry,
        onImportEntry = onImportEntry,
        modifier = Modifier.padding(horizontal = MOTSpacing.screenHorizontal),
      )
    }

    if (highlights.isNotEmpty()) {
      item {
        WalletHighlightsSection(
          entries = highlights,
          onEntryClick = onEntryClick,
          modifier = Modifier.padding(top = MOTSpacing.componentSm),
        )
      }
    }

    item {
      Text(
        text = "Todos los documentos",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(
          horizontal = MOTSpacing.screenHorizontal,
          vertical = MOTSpacing.layoutMd,
        ),
      )
    }

    items(entries.sortedByDescending { it.date ?: LocalDate.MIN }, key = { it.id }) { entry ->
      WalletDocumentRow(
        entry = entry,
        onClick = { onEntryClick(entry.id) },
        onDelete = if (onDeleteEntry != null) {
          { entryPendingDelete = entry }
        } else {
          null
        },
      )
      HorizontalDivider(
        modifier = Modifier.padding(horizontal = MOTSpacing.screenHorizontal),
        color = MaterialTheme.colorScheme.outlineVariant,
      )
    }
  }
}

@Composable
private fun WalletHeader(
  trip: Trip?,
  entryCount: Int,
  showTitle: Boolean = true,
  modifier: Modifier = Modifier,
) {
  Column(modifier = modifier) {
    if (showTitle) {
      Text(
        text = "Wallet",
        style = MaterialTheme.typography.headlineLarge,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    }
    trip?.let {
      Text(
        text = it.destination,
        style = if (showTitle) {
          MaterialTheme.typography.titleMedium
        } else {
          MaterialTheme.typography.headlineSmall
        },
        color = if (showTitle) {
          MaterialTheme.colorScheme.onSurfaceVariant
        } else {
          MaterialTheme.colorScheme.onSurface
        },
        modifier = Modifier.padding(top = if (showTitle) 4.dp else 0.dp),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
      Text(
        text = "${it.startDate.format(DateFormatter)} – ${it.endDate.format(DateFormatter)}",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 2.dp),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
      )
    }
    Text(
      text = entryCountLabel(entryCount),
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier.padding(top = if (showTitle) 20.dp else 12.dp),
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
    )
    Text(
      text = "Vuelos, hoteles y documentos en un solo sitio",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.padding(top = 4.dp),
      maxLines = 2,
      overflow = TextOverflow.Ellipsis,
    )
  }
}

@Composable
private fun WalletQuickActions(
  onAddEntry: () -> Unit,
  onImportEntry: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(MOTSpacing.gutterGrid),
  ) {
    MOTButton(
      onClick = onAddEntry,
      modifier = Modifier.weight(1f),
    ) {
      Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
      Spacer(Modifier.width(8.dp))
      Text("Añadir")
    }
    OutlinedButton(
      onClick = onImportEntry,
      modifier = Modifier.weight(1f),
      shape = rememberMOTButtonShape(),
    ) {
      Icon(Icons.Default.Upload, contentDescription = null, modifier = Modifier.size(18.dp))
      Spacer(Modifier.width(8.dp))
      Text("Importar")
    }
  }
}

@Composable
private fun WalletHighlightsSection(
  entries: List<WalletEntry>,
  onEntryClick: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(modifier = modifier) {
    Text(
      text = "Próximos",
      style = MaterialTheme.typography.titleMedium,
      modifier = Modifier.padding(
        horizontal = MOTSpacing.screenHorizontal,
        vertical = MOTSpacing.componentXs,
      ),
    )
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
        .padding(
          horizontal = MOTSpacing.screenHorizontal,
          vertical = MOTSpacing.componentSm,
        ),
      horizontalArrangement = Arrangement.spacedBy(MOTSpacing.gutterGrid),
    ) {
      entries.forEach { entry ->
        WalletHighlightCard(
          entry = entry,
          onClick = { onEntryClick(entry.id) },
        )
      }
    }
  }
}

@Composable
private fun WalletHighlightCard(
  entry: WalletEntry,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val label = entryTypeLabel(entry.type)
  val hasQr = !entry.qrPayload.isNullOrBlank()
  Surface(
    modifier = modifier
      .width(248.dp)
      .semantics(mergeDescendants = true) {
        contentDescription = buildString {
          append("$label, ${entry.title}")
          if (hasQr) append(", QR de embarque guardado")
        }
      }
      .clickable(onClick = onClick),
    shape = MaterialTheme.shapes.medium,
    color = MaterialTheme.colorScheme.surfaceContainerHigh,
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        EntryTypeIcon(
          type = entry.type,
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            .padding(8.dp),
        )
        Spacer(Modifier.width(10.dp))
        Text(
          text = label,
          style = MaterialTheme.typography.labelLarge,
          color = MaterialTheme.colorScheme.tertiary,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.weight(1f),
        )
        if (hasQr) {
          WalletQrListIcon()
        }
      }
      Text(
        text = entry.title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(top = 16.dp),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
      )
      Text(
        text = entryScheduleLabel(entry),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 8.dp),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
      )
    }
  }
}

@Composable
private fun WalletDocumentRow(
  entry: WalletEntry,
  onClick: () -> Unit,
  onDelete: (() -> Unit)? = null,
  modifier: Modifier = Modifier,
) {
  val label = entryTypeLabel(entry.type)
  val hasQr = !entry.qrPayload.isNullOrBlank()
  ListItem(
    modifier = modifier
      .clickable(onClick = onClick)
      .semantics {
        contentDescription = buildString {
          append("$label, ${entry.title}")
          if (hasQr) append(", QR de embarque guardado")
        }
      },
    headlineContent = {
      Text(
        text = entry.title,
        style = MaterialTheme.typography.titleMedium,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
      )
    },
    supportingContent = {
      Text(
        text = buildString {
          append(label)
          entryScheduleLabel(entry).takeIf { it != "Sin fecha" }?.let { append(" · $it") }
        },
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
      )
    },
    leadingContent = {
      EntryTypeIcon(
        type = entry.type,
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.surfaceContainer)
          .padding(8.dp),
      )
    },
    trailingContent = if (hasQr || onDelete != null) {
      {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
          if (hasQr) WalletQrListIcon()
          if (onDelete != null) {
            MOTIconButton(onClick = onDelete) {
              Icon(
                Icons.Default.Delete,
                contentDescription = "Eliminar documento",
                tint = MaterialTheme.colorScheme.error,
              )
            }
          }
        }
      }
    } else {
      null
    },
    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surface),
  )
}

@Composable
private fun WalletQrListIcon(modifier: Modifier = Modifier) {
  Icon(
    imageVector = Icons.Default.QrCode,
    contentDescription = "QR de embarque guardado",
    modifier = modifier.size(24.dp),
    tint = MaterialTheme.colorScheme.tertiary,
  )
}

@Composable
private fun EntryTypeIcon(
  type: EntryType,
  modifier: Modifier = Modifier,
) {
  Icon(
    imageVector = entryTypeIcon(type),
    contentDescription = null,
    modifier = modifier,
    tint = MaterialTheme.colorScheme.onSurfaceVariant,
  )
}

@Composable
private fun WalletEmptyState(
  onAddEntry: () -> Unit,
  onImportEntry: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .padding(horizontal = MOTSpacing.screenHorizontal)
      .padding(bottom = MOTSpacing.screenContentBottom),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      text = "Tu maleta digital",
      style = MaterialTheme.typography.headlineMedium,
      maxLines = 2,
      overflow = TextOverflow.Ellipsis,
    )
    Text(
      text = "Centraliza vuelos, hoteles y PDFs del viaje. Todo disponible sin conexión cuando lo necesites.",
      style = MaterialTheme.typography.bodyLarge,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.padding(top = MOTSpacing.layoutMd, bottom = MOTSpacing.layoutLg),
      maxLines = 4,
      overflow = TextOverflow.Ellipsis,
    )
    WalletQuickActions(
      onAddEntry = onAddEntry,
      onImportEntry = onImportEntry,
      modifier = Modifier.fillMaxWidth(),
    )
  }
}

private fun entryCountLabel(count: Int): String = when (count) {
  1 -> "1 documento"
  else -> "$count documentos"
}

private fun entryTypeIcon(type: EntryType): ImageVector = when (type) {
  EntryType.FLIGHT -> Icons.Default.Flight
  EntryType.HOTEL -> Icons.Default.Hotel
  EntryType.ACTIVITY -> Icons.Default.ConfirmationNumber
  EntryType.TRANSPORT -> Icons.Default.DirectionsCar
  EntryType.GENERIC -> Icons.AutoMirrored.Filled.InsertDriveFile
}

private fun entryScheduleLabel(entry: WalletEntry): String {
  val date = entry.date?.format(DateFormatter)
  val time = entry.time?.format(TimeFormatter)
  return when {
    date != null && time != null -> "$date · $time"
    date != null -> date
    else -> "Sin fecha"
  }
}

@Preview(name = "Wallet — con documentos", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun WalletScreenPreview() {
  MyOwnTripTheme {
    Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
      WalletScreen(
        trip = previewTrip(),
        entries = previewWalletEntries(),
        onAddEntry = {},
        onEntryClick = {},
      )
    }
  }
}

@Preview(name = "Wallet — vacío", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun WalletScreenEmptyPreview() {
  MyOwnTripTheme {
    Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
      WalletScreen(
        trip = previewTrip(),
        entries = emptyList(),
        onAddEntry = {},
        onImportEntry = {},
        onEntryClick = {},
      )
    }
  }
}

private fun previewTrip() = Trip(
  id = "trip-1",
  name = "Barcelona fin de semana",
  destination = "Barcelona",
  startDate = LocalDate.of(2026, 6, 14),
  endDate = LocalDate.of(2026, 6, 16),
  createdAt = 0L,
)

private fun previewWalletEntries() = listOf(
  WalletEntry(
    id = "1",
    tripId = "trip-1",
    type = EntryType.FLIGHT,
    title = "IB 3254 · Madrid → Barcelona",
    date = LocalDate.of(2026, 6, 14),
    time = LocalTime.of(9, 15),
    qrPayload = "M1DEMO/PAX EIB3254 MADBCNIB 3254 314Y014A0001 349>5180  5140BIB              2A825513825513 0000",
  ),
  WalletEntry(
    id = "2",
    tripId = "trip-1",
    type = EntryType.HOTEL,
    title = "Hotel Casa Bonay",
    date = LocalDate.of(2026, 6, 14),
    notes = "Check-in 15:00",
  ),
  WalletEntry(
    id = "3",
    tripId = "trip-1",
    type = EntryType.TRANSPORT,
    title = "AVE 03142",
    date = LocalDate.of(2026, 6, 16),
    time = LocalTime.of(18, 30),
  ),
  WalletEntry(
    id = "4",
    tripId = "trip-1",
    type = EntryType.ACTIVITY,
    title = "Entrada Sagrada Familia",
    date = LocalDate.of(2026, 6, 15),
    time = LocalTime.of(11, 0),
  ),
)
