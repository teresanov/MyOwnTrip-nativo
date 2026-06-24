package com.myowntrip.app.ui.features.trips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.ItineraryBlock
import com.myowntrip.app.domain.model.WalletEntry
import com.myowntrip.app.domain.plan.PlanPlacementLogic
import com.myowntrip.app.domain.wallet.WalletDocumentParser
import com.myowntrip.app.ui.features.plan.PlanActivityCard
import com.myowntrip.app.ui.features.wallet.entryTypeLabel
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MOTTextButton
import com.myowntrip.app.ui.theme.MyOwnTripTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val SpanishDayDateFormatter =
  DateTimeFormatter.ofPattern("EEEE d MMM", Locale("es", "ES"))

@Composable
fun TripPlanTab(
  days: List<Day>,
  planBlocks: List<ItineraryBlock>,
  walletEntries: List<WalletEntry>,
  onDayClick: (String) -> Unit,
  onLinkWallet: (String) -> Unit,
  onWalletEntryClick: (String) -> Unit,
  modifier: Modifier = Modifier,
  isPastTrip: Boolean = false,
  onDayMemoriesClick: (String) -> Unit = onDayClick,
  onViewWalletDocuments: () -> Unit = {},
) {
  if (days.isEmpty()) {
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(horizontal = MOTSpacing.screenHorizontal, vertical = MOTSpacing.layoutLg),
      verticalArrangement = Arrangement.Center,
    ) {
      Text(
        text = "Este viaje aún no tiene días",
        style = MaterialTheme.typography.bodyLarge,
      )
      Text(
        text = "Los días se crean al definir las fechas de inicio y fin del cuaderno.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = MOTSpacing.componentSm),
      )
    }
    return
  }

  val blocksByDay = planBlocks.groupBy { it.dayId }
  val linkedWalletIds = planBlocks.mapNotNull { it.walletEntryId }.toSet()
  val unplacedEntries = walletEntries.filter { !it.isArchived && it.id !in linkedWalletIds }

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(
      horizontal = MOTSpacing.screenHorizontal,
      vertical = MOTSpacing.layoutMd,
    ),
    verticalArrangement = Arrangement.spacedBy(MOTSpacing.layoutMd),
  ) {
    item {
      Column(verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentXs)) {
        Text(
          text = if (isPastTrip) "Así quedó tu plan" else "Plan del viaje",
          style = MaterialTheme.typography.titleMedium,
        )
        Text(
          text = if (isPastTrip) {
            "Día a día — lo que tenías previsto"
          } else {
            "Actividades por día. Toca «Reordenar día» para abrir el cuadrante horario."
          },
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (isPastTrip) {
          MOTTextButton(onClick = onViewWalletDocuments) {
            Text("Ver documentos")
          }
        }
      }
    }
    items(days, key = { it.id }) { day ->
      PlanDaySection(
        day = day,
        blocks = PlanPlacementLogic.sortBlocksForDisplay(blocksByDay[day.id].orEmpty()),
        walletEntries = walletEntries,
        isPastTrip = isPastTrip,
        onDayClick = { onDayClick(day.id) },
        onDayMemoriesClick = { onDayMemoriesClick(day.id) },
        onLinkWallet = onLinkWallet,
        onWalletEntryClick = onWalletEntryClick,
      )
    }
    if (!isPastTrip && unplacedEntries.isNotEmpty()) {
      item {
        UnplacedWalletSection(
          entries = unplacedEntries,
          onWalletEntryClick = onWalletEntryClick,
        )
      }
    }
  }
}

@Composable
private fun PlanDaySection(
  day: Day,
  blocks: List<ItineraryBlock>,
  walletEntries: List<WalletEntry>,
  onDayClick: () -> Unit,
  onDayMemoriesClick: () -> Unit,
  onLinkWallet: (String) -> Unit,
  onWalletEntryClick: (String) -> Unit,
  isPastTrip: Boolean = false,
) {
  val dateLabel = day.date.format(SpanishDayDateFormatter).replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
  }
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .semantics { contentDescription = "Día ${day.dayNumber}, $dateLabel" },
  ) {
    Column(modifier = Modifier.padding(MOTSpacing.layoutMd)) {
      Text(text = "Día ${day.dayNumber}", style = MaterialTheme.typography.titleSmall)
      Text(
        text = dateLabel,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
      day.title?.takeIf { it.isNotBlank() }?.let { title ->
        Text(
          text = title,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.tertiary,
          modifier = Modifier.padding(top = MOTSpacing.componentXs),
        )
      }

      if (blocks.isEmpty()) {
        Text(
          text = if (isPastTrip) "Sin actividades registradas en este día." else "Sin actividades en este día.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(top = MOTSpacing.layoutMd),
        )
      } else {
        Column(
          modifier = Modifier.padding(top = MOTSpacing.layoutMd),
          verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
        ) {
          blocks.forEachIndexed { index, block ->
            if (index > 0) {
              HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }
            val linked = block.walletEntryId?.let { id -> walletEntries.find { it.id == id } }
            PlanActivityCard(
              block = block,
              linkedWalletEntry = linked,
              readOnly = isPastTrip,
              onLinkWallet = { onLinkWallet(block.id) },
              onWalletEntryClick = onWalletEntryClick,
              embeddedInDay = true,
            )
          }
        }
      }

      if (isPastTrip) {
        MOTTextButton(
          onClick = onDayMemoriesClick,
          modifier = Modifier.padding(top = MOTSpacing.componentSm),
        ) {
          Text("Ver recuerdos del día")
        }
      } else {
        MOTTextButton(
          onClick = onDayClick,
          modifier = Modifier.padding(top = MOTSpacing.componentSm),
        ) {
          Text(if (blocks.isEmpty()) "Añadir actividades" else "Reordenar día")
        }
      }
    }
  }
}

@Composable
private fun UnplacedWalletSection(
  entries: List<WalletEntry>,
  onWalletEntryClick: (String) -> Unit,
) {
  Card(modifier = Modifier.fillMaxWidth()) {
    Column(
      modifier = Modifier.padding(MOTSpacing.layoutMd),
      verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
    ) {
      Text(text = "Sin colocar en el plan", style = MaterialTheme.typography.titleSmall)
      Text(
        text = "Documentos en Wallet que aún no tienen actividad en un día.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
      entries.forEach { entry ->
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Column(modifier = Modifier.padding(vertical = MOTSpacing.componentXs)) {
          Text(entry.title, style = MaterialTheme.typography.bodyMedium)
          Text(
            buildString {
              append(entryTypeLabel(entry.type))
              entry.date?.let { append(" · ${WalletDocumentParser.formatParsedDate(it)}") }
              entry.time?.let { append(" · ${WalletDocumentParser.formatParsedTime(it)}") }
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          MOTTextButton(onClick = { onWalletEntryClick(entry.id) }) {
            Text("Ver en Wallet")
          }
        }
      }
    }
  }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 520)
@Composable
private fun TripPlanTabPreview() {
  val days = listOf(
    Day("d1", "t1", LocalDate.of(2026, 6, 12), 1, "Llegada"),
    Day("d2", "t1", LocalDate.of(2026, 6, 13), 2, null),
  )
  MyOwnTripTheme {
    TripPlanTab(
      days = days,
      planBlocks = emptyList(),
      walletEntries = emptyList(),
      onDayClick = {},
      onLinkWallet = {},
      onWalletEntryClick = {},
    )
  }
}
