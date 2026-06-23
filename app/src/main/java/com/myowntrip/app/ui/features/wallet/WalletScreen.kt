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
import androidx.compose.material3.FilterChip
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
import com.myowntrip.app.BuildConfig
import com.myowntrip.app.domain.model.EntryType
import com.myowntrip.app.domain.model.Trip
import com.myowntrip.app.domain.model.WalletEntry
import com.myowntrip.app.domain.wallet.accessibilityLabel
import com.myowntrip.app.domain.wallet.isAvailableOffline
import com.myowntrip.app.domain.wallet.isCloudOnly
import com.myowntrip.app.domain.wallet.offlineAvailability
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MOTTextButton
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
  filterPhase: WalletDocumentFilterPhase = WalletDocumentFilterPhase.Active,
  onFilterPhaseChange: (WalletDocumentFilterPhase) -> Unit = {},
  onAddEntry: () -> Unit,
  onImportEntry: () -> Unit = onAddEntry,
  onLoadDebugSamples: (() -> Unit)? = null,
  onEntryClick: (String) -> Unit,
  onArchiveEntry: (String) -> Unit = {},
  onUnarchiveEntry: (String) -> Unit = {},
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

  val activeCount = remember(entries) { entries.count { !it.isArchived } }

  if (activeCount == 0 && entries.isEmpty()) {
    WalletEmptyState(
      onAddEntry = onAddEntry,
      onImportEntry = onImportEntry,
      onLoadDebugSamples = onLoadDebugSamples,
      modifier = modifier.fillMaxSize(),
    )
    return
  }

  val visibleEntries = remember(entries, filterPhase) {
    applyWalletDocumentFilters(entries, filterPhase)
      .sortedByDescending { it.date ?: LocalDate.MIN }
  }
  val highlights = remember(entries) { walletHighlights(entries) }
  val showHighlights = filterPhase == WalletDocumentFilterPhase.Active && highlights.isNotEmpty()

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(bottom = MOTSpacing.screenContentBottom),
    verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
  ) {
    item {
      WalletHeader(
        trip = trip,
        entryCount = activeCount,
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

    item {
      WalletDocumentFilterChips(
        filterPhase = filterPhase,
        onFilterPhaseChange = onFilterPhaseChange,
        modifier = Modifier.padding(
          horizontal = MOTSpacing.screenHorizontal,
          vertical = MOTSpacing.componentXs,
        ),
      )
    }

    if (showHighlights) {
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
        text = walletListSectionTitle(filterPhase),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(
          horizontal = MOTSpacing.screenHorizontal,
          vertical = MOTSpacing.layoutMd,
        ),
      )
    }

    if (visibleEntries.isEmpty()) {
      item {
        Text(
          text = walletEmptyFilterMessage(filterPhase),
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(
            horizontal = MOTSpacing.screenHorizontal,
            vertical = MOTSpacing.componentSm,
          ),
        )
      }
    } else {
      items(visibleEntries, key = { it.id }) { entry ->
        SwipeableWalletDocumentRow(
          entry = entry,
          showArchivedActions = entry.isArchived,
          onClick = { onEntryClick(entry.id) },
          onArchive = { onArchiveEntry(entry.id) },
          onUnarchive = { onUnarchiveEntry(entry.id) },
          onDeleteRequest = { entryPendingDelete = entry },
          showDivider = entry != visibleEntries.last(),
        )
      }
    }
  }
}

@Composable
private fun WalletDocumentFilterChips(
  filterPhase: WalletDocumentFilterPhase,
  onFilterPhaseChange: (WalletDocumentFilterPhase) -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
  ) {
    FilterChip(
      selected = filterPhase == WalletDocumentFilterPhase.Active,
      onClick = { onFilterPhaseChange(WalletDocumentFilterPhase.Active) },
      label = { Text("Activos") },
    )
    FilterChip(
      selected = filterPhase == WalletDocumentFilterPhase.Archived,
      onClick = { onFilterPhaseChange(WalletDocumentFilterPhase.Archived) },
      label = { Text("Archivados") },
    )
    FilterChip(
      selected = filterPhase == WalletDocumentFilterPhase.All,
      onClick = { onFilterPhaseChange(WalletDocumentFilterPhase.All) },
      label = { Text("Todos") },
    )
  }
}

private fun walletListSectionTitle(filterPhase: WalletDocumentFilterPhase): String = when (filterPhase) {
  WalletDocumentFilterPhase.Active -> "Documentos activos"
  WalletDocumentFilterPhase.Archived -> "Archivados"
  WalletDocumentFilterPhase.All -> "Todos los documentos"
}

private fun walletEmptyFilterMessage(filterPhase: WalletDocumentFilterPhase): String = when (filterPhase) {
  WalletDocumentFilterPhase.Active -> "No hay documentos activos. Archiva los ya usados o añade uno nuevo."
  WalletDocumentFilterPhase.Archived -> "No hay documentos archivados."
  WalletDocumentFilterPhase.All -> "Este viaje aún no tiene documentos."
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
  val offline = remember(entry.id, entry.pdfUri, entry.qrPayload, entry.linkUrl) { entry.offlineAvailability() }
  val hasQr = !entry.qrPayload.isNullOrBlank()
  Surface(
    modifier = modifier
      .width(248.dp)
      .semantics(mergeDescendants = true) {
        contentDescription = buildString {
          append("$label, ${entry.title}")
          offline.accessibilityLabel()?.let { append(", $it") }
          if (hasQr && offline.isAvailableOffline()) {
            append(", código QR guardado")
          }
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
        WalletOfflineIndicator(availability = offline, compact = true)
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
      if (offline.isAvailableOffline() || offline.isCloudOnly()) {
        WalletOfflineIndicator(
          availability = offline,
          modifier = Modifier.padding(top = 8.dp),
        )
      }
    }
  }
}

@Composable
internal fun WalletDocumentRow(
  entry: WalletEntry,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val label = entryTypeLabel(entry.type)
  val offline = remember(entry.id, entry.pdfUri, entry.qrPayload, entry.linkUrl) { entry.offlineAvailability() }
  ListItem(
    modifier = modifier
      .clickable(onClick = onClick)
      .semantics {
        contentDescription = buildString {
          append("$label, ${entry.title}")
          offline.accessibilityLabel()?.let { append(", $it") }
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
    trailingContent = if (offline.isAvailableOffline() || offline.isCloudOnly()) {
      {
        WalletOfflineIndicator(availability = offline, compact = true)
      }
    } else {
      null
    },
    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surface),
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
  onLoadDebugSamples: (() -> Unit)? = null,
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
    if (BuildConfig.DEBUG && onLoadDebugSamples != null) {
      MOTTextButton(
        onClick = onLoadDebugSamples,
        modifier = Modifier.padding(top = MOTSpacing.layoutMd),
      ) {
        Text("Cargar samples de prueba")
      }
    }
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

@Preview(name = "cap 1 · Wallet · con documentos", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun WalletScreenPreview() {
  MyOwnTripTheme {
    Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
      WalletScreen(
        trip = previewWalletTrip,
        entries = previewWalletEntries,
        onAddEntry = {},
        onEntryClick = {},
        embeddedInTrip = true,
      )
    }
  }
}

@Preview(name = "cap 2 · Wallet · vacío", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun WalletScreenEmptyPreview() {
  MyOwnTripTheme {
    Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
      WalletScreen(
        trip = previewWalletTrip,
        entries = emptyList(),
        onAddEntry = {},
        onImportEntry = {},
        onEntryClick = {},
        embeddedInTrip = true,
      )
    }
  }
}
