package com.myowntrip.app.ui.features.trips

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.myowntrip.app.domain.model.Trip
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTCorner
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MyOwnTripTheme
import com.myowntrip.app.ui.theme.rememberMOTButtonShape
import java.io.File
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

private val SpanishLocale = Locale("es", "ES")
private val DateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", SpanishLocale)

private enum class TripPhase { Current, Upcoming, Past }

private fun Trip.phase(today: LocalDate = LocalDate.now()): TripPhase = when {
  !today.isBefore(startDate) && !today.isAfter(endDate) -> TripPhase.Current
  today.isBefore(startDate) -> TripPhase.Upcoming
  else -> TripPhase.Past
}

private fun sortTripsForHome(trips: List<Trip>, today: LocalDate = LocalDate.now()): List<Trip> =
  trips.sortedWith(
    compareBy<Trip> { trip ->
      when (trip.phase(today)) {
        TripPhase.Current -> 0
        TripPhase.Upcoming -> 1
        TripPhase.Past -> 2
      }
    }.thenBy { trip ->
      when (trip.phase(today)) {
        TripPhase.Upcoming -> trip.startDate.toEpochDay()
        TripPhase.Past -> -trip.endDate.toEpochDay()
        TripPhase.Current -> trip.startDate.toEpochDay()
      }
    },
  )

@Composable
fun TripListScreen(
  onCreateTrip: () -> Unit,
  onTripClick: (String) -> Unit,
  viewModel: TripListViewModel = hiltViewModel(),
) {
  val trips by viewModel.trips.collectAsStateWithLifecycle()
  val today = remember { LocalDate.now() }
  val sortedTrips = remember(trips, today) { sortTripsForHome(trips, today) }
  val featuredTrip = sortedTrips.firstOrNull()
  val otherTrips = sortedTrips.drop(1)

  Scaffold { padding ->
    if (sortedTrips.isEmpty()) {
      HomeEmptyState(
        modifier = Modifier
          .padding(padding)
          .fillMaxSize(),
        onCreateTrip = onCreateTrip,
      )
      return@Scaffold
    }

    LazyColumn(
      modifier = Modifier
        .padding(padding)
        .fillMaxSize(),
      contentPadding = PaddingValues(bottom = MOTSpacing.screenContentBottom),
      verticalArrangement = Arrangement.spacedBy(MOTSpacing.layoutMd),
    ) {
      item {
        HomeHeroHeader(
          featuredTrip = featuredTrip,
          tripCount = sortedTrips.size,
          today = today,
          modifier = Modifier.padding(horizontal = MOTSpacing.screenHorizontal),
        )
      }
      item {
        HomeActionBar(
          featuredTrip = featuredTrip,
          onCreateTrip = onCreateTrip,
          modifier = Modifier.padding(horizontal = MOTSpacing.screenHorizontal),
        )
      }
      item {
        featuredTrip?.let { trip ->
          TripHeroCard(
            trip = trip,
            today = today,
            onClick = { onTripClick(trip.id) },
            modifier = Modifier.padding(horizontal = MOTSpacing.screenHorizontal),
          )
        }
      }
      item {
        WalletPromoBanner(
          modifier = Modifier.padding(horizontal = MOTSpacing.screenHorizontal),
        )
      }
      if (otherTrips.isNotEmpty()) {
        item {
          Text(
            text = "Más viajes",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(
              horizontal = MOTSpacing.screenHorizontal,
              vertical = MOTSpacing.componentXs,
            ),
          )
        }
        items(otherTrips, key = { it.id }) { trip ->
          TripListCard(
            trip = trip,
            today = today,
            onClick = { onTripClick(trip.id) },
            modifier = Modifier.padding(horizontal = MOTSpacing.screenHorizontal),
          )
        }
      }
    }
  }
}

@Composable
private fun HomeHeroHeader(
  featuredTrip: Trip?,
  tripCount: Int,
  today: LocalDate,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier.padding(top = MOTSpacing.layoutMd),
  ) {
    Text(
      text = timeGreeting(),
      style = MaterialTheme.typography.labelLarge,
      color = MaterialTheme.colorScheme.tertiary,
    )
    Text(
      text = homeHeadline(featuredTrip, today),
      style = MaterialTheme.typography.displaySmall,
      modifier = Modifier.padding(top = MOTSpacing.componentXs),
      maxLines = 2,
      overflow = TextOverflow.Ellipsis,
    )
    Text(
      text = homeSubheadline(featuredTrip, tripCount, today),
      style = MaterialTheme.typography.bodyLarge,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.padding(top = MOTSpacing.componentSm),
      maxLines = 2,
      overflow = TextOverflow.Ellipsis,
    )
  }
}

@Composable
private fun HomeActionBar(
  featuredTrip: Trip?,
  onCreateTrip: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val exploreLabel = featuredTrip?.destination ?: "Mis viajes"
  OutlinedCard(modifier = modifier.fillMaxWidth()) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Row(
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = MOTSpacing.layoutMd, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Icon(
          imageVector = Icons.Default.Search,
          contentDescription = null,
          modifier = Modifier.size(20.dp),
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.width(MOTSpacing.componentSm))
        Text(
          text = exploreLabel,
          style = MaterialTheme.typography.bodyLarge,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }
      VerticalDivider(
        modifier = Modifier.height(48.dp),
        color = MaterialTheme.colorScheme.outlineVariant,
      )
      MOTButton(
        onClick = onCreateTrip,
        modifier = Modifier
          .padding(horizontal = MOTSpacing.componentSm, vertical = MOTSpacing.componentSm)
          .semantics { contentDescription = "Crear viaje" },
      ) {
        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(MOTSpacing.componentSm))
        Text("Nuevo viaje")
      }
    }
  }
}

@Composable
private fun WalletPromoBanner(modifier: Modifier = Modifier) {
  ElevatedCard(
    modifier = modifier.fillMaxWidth(),
    colors = androidx.compose.material3.CardDefaults.elevatedCardColors(
      containerColor = MaterialTheme.colorScheme.tertiaryContainer,
    ),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(MOTSpacing.layoutMd),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(MOTSpacing.layoutMd),
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = "Todo en Wallet",
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.onTertiaryContainer,
        )
        Text(
          text = "Vuelos, hoteles y PDFs en un solo sitio — incluso sin red.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onTertiaryContainer,
          modifier = Modifier.padding(top = 4.dp),
          maxLines = 3,
          overflow = TextOverflow.Ellipsis,
        )
      }
      Icon(
        Icons.Outlined.ConfirmationNumber,
        contentDescription = null,
        modifier = Modifier.size(40.dp),
        tint = MaterialTheme.colorScheme.tertiary,
      )
    }
  }
}

@Composable
private fun HomeEmptyState(
  onCreateTrip: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(MOTSpacing.layoutMd),
  ) {
    Column(
      modifier = Modifier.padding(
        horizontal = MOTSpacing.screenHorizontal,
        vertical = MOTSpacing.layoutMd,
      ),
    ) {
      Text(
        text = timeGreeting(),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.tertiary,
      )
      Text(
        text = "Tu próximo viaje empieza aquí",
        style = MaterialTheme.typography.displaySmall,
        modifier = Modifier.padding(top = MOTSpacing.componentXs),
      )
      Text(
        text = "Crea un cuaderno, guarda documentos y anota recuerdos.",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = MOTSpacing.componentSm),
      )
    }
    HomeActionBar(
      featuredTrip = null,
      onCreateTrip = onCreateTrip,
      modifier = Modifier.padding(horizontal = MOTSpacing.screenHorizontal),
    )
    OutlinedCard(
      modifier = Modifier
        .fillMaxWidth()
        .height(220.dp)
        .padding(horizontal = MOTSpacing.screenHorizontal),
    ) {
      Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "Sin viajes aún",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
          )
          Text(
            text = "El primero es el que más recuerdas",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = MOTSpacing.componentSm),
          )
        }
      }
    }
    WalletPromoBanner(
      modifier = Modifier.padding(horizontal = MOTSpacing.screenHorizontal),
    )
    MOTButton(
      onClick = onCreateTrip,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = MOTSpacing.screenHorizontal),
    ) {
      Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
      Spacer(Modifier.width(MOTSpacing.componentSm))
      Text("Crear mi primer viaje")
    }
    Spacer(Modifier.weight(1f))
  }
}

@Composable
private fun EyebrowLabel(
  text: String,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier.semantics { hideFromAccessibility() },
    shape = MaterialTheme.shapes.small,
    color = MaterialTheme.colorScheme.tertiaryFixedDim,
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.labelMedium,
      color = MaterialTheme.colorScheme.onTertiaryContainer,
      modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
    )
  }
}

@Composable
private fun TripHeroCard(
  trip: Trip,
  today: LocalDate,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val phase = trip.phase(today)
  val eyebrow = featuredEyebrow(phase)
  val meta = tripMetaLabel(trip)
  val countdown = tripCountdownLabel(trip, today)
  val a11yPhase = phaseStateDescription(phase)
  val heroContentDescription = tripHeroContentDescription(
    eyebrow = eyebrow,
    countdown = countdown,
    tripName = trip.name,
    meta = meta,
  )
  val portadaShape = RoundedCornerShape(
    topStart = MOTCorner.Medium,
    topEnd = MOTCorner.Medium,
  )

  ElevatedCard(
    modifier = modifier.fillMaxWidth(),
  ) {
    Column(
      verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(280.dp)
          .clip(portadaShape),
      ) {
        TripCoverImage(
          trip = trip,
          modifier = Modifier
            .fillMaxSize()
            .semantics { hideFromAccessibility() },
        )
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                0f to MaterialTheme.colorScheme.scrim.copy(alpha = 0.08f),
                0.45f to MaterialTheme.colorScheme.scrim.copy(alpha = 0.02f),
                0.7f to MaterialTheme.colorScheme.scrim.copy(alpha = 0.55f),
                1f to MaterialTheme.colorScheme.scrim.copy(alpha = 0.82f),
              ),
            )
            .semantics { hideFromAccessibility() },
        )
        EyebrowLabel(
          text = eyebrow,
          modifier = Modifier
            .align(Alignment.TopStart)
            .padding(MOTSpacing.layoutMd),
        )
        Column(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(MOTSpacing.layoutMd)
            .semantics { hideFromAccessibility() },
        ) {
          if (countdown != null) {
            Text(
              text = countdown,
              style = MaterialTheme.typography.titleMedium,
              color = MaterialTheme.colorScheme.tertiaryFixed,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
          }
          Text(
            text = trip.name,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.inverseOnSurface,
            modifier = Modifier.padding(top = if (countdown != null) 4.dp else 0.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
          )
          Text(
            text = meta,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.88f),
            modifier = Modifier.padding(top = MOTSpacing.componentXs),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
          )
        }
      }
      FilledTonalButton(
        onClick = onClick,
        modifier = Modifier
          .fillMaxWidth()
          .heightIn(min = 48.dp)
          .semantics {
            contentDescription = heroContentDescription
            stateDescription = a11yPhase
          },
        shape = rememberMOTButtonShape(),
        colors = ButtonDefaults.filledTonalButtonColors(),
      ) {
        Text("Ver detalles")
      }
    }
  }
}

@Composable
private fun TripListCard(
  trip: Trip,
  today: LocalDate,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val meta = tripMetaLabel(trip)
  val phase = trip.phase(today)

  OutlinedCard(
    modifier = modifier
      .fillMaxWidth()
      .semantics(mergeDescendants = true) {
        contentDescription = "${trip.name}, ${trip.destination}, $meta"
        stateDescription = phaseStateDescription(phase)
      }
      .clickable(onClick = onClick),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(96.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      TripCoverImage(
        trip = trip,
        modifier = Modifier
          .width(96.dp)
          .height(96.dp)
          .clip(MaterialTheme.shapes.small),
      )
      Column(
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = MOTSpacing.layoutMd, vertical = MOTSpacing.componentSm),
      ) {
        Text(
          text = trip.name,
          style = MaterialTheme.typography.titleMedium,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
        )
        Text(
          text = trip.destination,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(top = 2.dp),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        Text(
          text = meta,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.tertiary,
          modifier = Modifier.padding(top = MOTSpacing.componentXs),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }
  }
}

@Composable
private fun TripCoverImage(
  trip: Trip,
  modifier: Modifier = Modifier,
) {
  val coverPath = trip.coverPhoto
  if (coverPath != null) {
    AsyncImage(
      model = File(coverPath),
      contentDescription = null,
      contentScale = ContentScale.Crop,
      modifier = modifier,
    )
  } else {
    Surface(
      modifier = modifier,
      color = MaterialTheme.colorScheme.surfaceContainerHighest,
      shape = MaterialTheme.shapes.small,
    ) {
      Box(contentAlignment = Alignment.Center) {
        Text(
          text = trip.destination.take(2).uppercase(SpanishLocale),
          style = MaterialTheme.typography.headlineLarge,
          color = MaterialTheme.colorScheme.tertiary,
        )
      }
    }
  }
}

private fun timeGreeting(): String {
  val hour = LocalTime.now().hour
  return when (hour) {
    in 6..11 -> "Buenos días"
    in 12..19 -> "Buenas tardes"
    else -> "Buenas noches"
  }
}

private fun homeHeadline(featured: Trip?, today: LocalDate): String = when {
  featured == null -> "Cuaderno del viaje"
  featured.phase(today) == TripPhase.Current -> featured.destination
  featured.phase(today) == TripPhase.Upcoming -> featured.destination
  else -> "Tus recuerdos"
}

private fun homeSubheadline(featured: Trip?, tripCount: Int, today: LocalDate): String {
  if (featured == null) return "Planifica, guarda y revive cada viaje"
  val countdown = tripCountdownLabel(featured, today)
  return when (featured.phase(today)) {
    TripPhase.Current -> "Viaje en curso · $tripCount ${if (tripCount == 1) "cuaderno" else "cuadernos"}"
    TripPhase.Upcoming -> countdown ?: featured.name
    TripPhase.Past -> "${featured.name} · ${tripMetaLabel(featured)}"
  }
}

private fun tripCountdownLabel(trip: Trip, today: LocalDate): String? {
  if (trip.phase(today) != TripPhase.Upcoming) return null
  val days = ChronoUnit.DAYS.between(today, trip.startDate)
  return when (days) {
    0L -> "Sale hoy"
    1L -> "Sale mañana"
    in 2..30 -> "Sale en $days días"
    else -> "Del ${trip.startDate.format(DateFormatter)}"
  }
}

private fun featuredEyebrow(phase: TripPhase): String = when (phase) {
  TripPhase.Current -> "En destino"
  TripPhase.Upcoming -> "Próximo viaje"
  TripPhase.Past -> "Recuerdo"
}

private fun phaseStateDescription(phase: TripPhase): String = when (phase) {
  TripPhase.Current -> "Viaje en curso"
  TripPhase.Upcoming -> "Próximo viaje"
  TripPhase.Past -> "Viaje pasado"
}

private fun tripHeroContentDescription(
  eyebrow: String,
  countdown: String?,
  tripName: String,
  meta: String,
): String = buildString {
  append(eyebrow)
  if (countdown != null) {
    append(". ")
    append(countdown)
  }
  append(". ")
  append(tripName)
  append(". ")
  append(meta)
  append(". Ver detalles")
}

private fun tripMetaLabel(trip: Trip): String {
  val days = ChronoUnit.DAYS.between(trip.startDate, trip.endDate) + 1
  val duration = if (days == 1L) "1 día" else "$days días"
  return "${trip.startDate.format(DateFormatter)} – ${trip.endDate.format(DateFormatter)} · $duration"
}

@Preview(name = "Home — vacío", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun TripListEmptyPreview() {
  MyOwnTripTheme {
    Surface(color = MaterialTheme.colorScheme.surface) {
      HomeEmptyState(onCreateTrip = {})
    }
  }
}

@Preview(name = "Home — con viajes", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun TripListWithTripsPreview() {
  val today = LocalDate.of(2026, 6, 17)
  val trips = listOf(
    Trip(
      id = "1",
      name = "Lisboa en abril",
      destination = "Lisboa",
      startDate = LocalDate.of(2026, 4, 12),
      endDate = LocalDate.of(2026, 4, 18),
      createdAt = 0L,
    ),
    Trip(
      id = "2",
      name = "Barcelona fin de semana",
      destination = "Barcelona",
      startDate = LocalDate.of(2026, 6, 20),
      endDate = LocalDate.of(2026, 6, 22),
      createdAt = 0L,
    ),
    Trip(
      id = "3",
      name = "Tokio otoño",
      destination = "Tokio",
      startDate = LocalDate.of(2025, 11, 1),
      endDate = LocalDate.of(2025, 11, 10),
      createdAt = 0L,
    ),
  )
  val sorted = sortTripsForHome(trips, today)
  MyOwnTripTheme {
    Surface(color = MaterialTheme.colorScheme.surface) {
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = MOTSpacing.screenContentBottom),
        verticalArrangement = Arrangement.spacedBy(MOTSpacing.layoutMd),
      ) {
        item {
          HomeHeroHeader(
            featuredTrip = sorted.first(),
            tripCount = sorted.size,
            today = today,
            modifier = Modifier.padding(horizontal = MOTSpacing.screenHorizontal),
          )
        }
        item {
          HomeActionBar(
            featuredTrip = sorted.first(),
            onCreateTrip = {},
            modifier = Modifier.padding(horizontal = MOTSpacing.screenHorizontal),
          )
        }
        item {
          TripHeroCard(
            trip = sorted.first(),
            today = today,
            onClick = {},
            modifier = Modifier.padding(horizontal = MOTSpacing.screenHorizontal),
          )
        }
        item {
          WalletPromoBanner(
            modifier = Modifier.padding(horizontal = MOTSpacing.screenHorizontal),
          )
        }
        item {
          Text(
            text = "Más viajes",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = MOTSpacing.screenHorizontal),
          )
        }
        items(sorted.drop(1), key = { it.id }) { trip ->
          TripListCard(
            trip = trip,
            today = today,
            onClick = {},
            modifier = Modifier.padding(horizontal = MOTSpacing.screenHorizontal),
          )
        }
      }
    }
  }
}
