package com.myowntrip.app.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myowntrip.app.domain.model.Trip
import com.myowntrip.app.ui.components.HomeEmptyStackedCard
import com.myowntrip.app.ui.components.tripMetaLabel
import com.myowntrip.app.ui.features.trips.HomeTripPhase
import com.myowntrip.app.ui.features.trips.TripFilterPhase
import com.myowntrip.app.ui.features.trips.homePhase
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MyOwnTripTheme
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

/** Bloque hero de página — Figma `Hero header` en caps Home (`205:816`, `205:1018`). */
@Composable
fun HomePageHero(
  eyebrow: String,
  headline: String,
  subheadline: String,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier.padding(
      horizontal = MOTSpacing.screenHorizontal,
      vertical = MOTSpacing.layoutMd,
    ),
  ) {
    Text(
      text = eyebrow,
      style = MaterialTheme.typography.labelLarge,
      color = MaterialTheme.colorScheme.tertiary,
    )
    Text(
      text = headline,
      style = MaterialTheme.typography.displaySmall,
      modifier = Modifier.padding(top = MOTSpacing.componentXs),
      maxLines = 3,
      overflow = TextOverflow.Ellipsis,
    )
    Text(
      text = subheadline,
      style = MaterialTheme.typography.bodyLarge,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.padding(top = MOTSpacing.componentSm),
      maxLines = 3,
      overflow = TextOverflow.Ellipsis,
    )
  }
}

fun homeEmptyEyebrow(firstName: String?): String =
  if (firstName.isNullOrBlank()) "Hola," else "Hola $firstName,"

fun homeTripsEyebrow(firstName: String?, greeting: String = timeGreeting()): String =
  if (firstName.isNullOrBlank()) greeting else "$greeting, $firstName"

@Composable
fun HomeHeroHeader(
  featuredTrip: Trip?,
  tripCount: Int,
  totalTripCount: Int,
  filterPhase: TripFilterPhase,
  today: LocalDate,
  modifier: Modifier = Modifier,
  userFirstName: String? = null,
  searchQuery: String = "",
  greetingOverride: String? = null,
) {
  val greeting = greetingOverride ?: timeGreeting()
  HomePageHero(
    eyebrow = homeTripsEyebrow(userFirstName, greeting),
    headline = homeHeadline(featuredTrip, today),
    subheadline = homeSubheadline(
      featured = featuredTrip,
      visibleCount = tripCount,
      totalCount = totalTripCount,
      filterPhase = filterPhase,
      today = today,
      searchActive = searchQuery.isNotBlank(),
    ),
    modifier = modifier,
  )
}

/** Cap 1b · Home solo pasados — design-file `313:501`. */
@Composable
fun HomeOnlyPastHero(
  userFirstName: String? = null,
  greetingOverride: String? = null,
  modifier: Modifier = Modifier,
) {
  HomePageHero(
    eyebrow = homeTripsEyebrow(userFirstName, greetingOverride ?: timeGreeting()),
    headline = "No tienes viajes planeados",
    subheadline = "Crea uno nuevo o revisa tus viajes anteriores.",
    modifier = modifier,
  )
}

/** Cap 1 · Home vacío — design-file `205:816`. */
@Composable
fun HomeEmptyState(
  onCreateTrip: () -> Unit,
  footerSlot: @Composable () -> Unit = {},
  userFirstName: String? = null,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier.verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(MOTSpacing.layoutMd),
  ) {
    HomePageHero(
      eyebrow = homeEmptyEyebrow(userFirstName),
      headline = "Tu próximo viaje empieza aquí",
      subheadline = "Crea un cuaderno, guarda documentos y anota recuerdos.",
    )
    HomeEmptyStackedCard(
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
    footerSlot()
    Spacer(Modifier.padding(bottom = MOTSpacing.screenContentBottom))
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
  featured.homePhase(today) == HomeTripPhase.Current -> featured.destination
  featured.homePhase(today) == HomeTripPhase.Upcoming -> featured.destination
  else -> "Tus recuerdos"
}

private fun homeSubheadline(
  featured: Trip?,
  visibleCount: Int,
  totalCount: Int,
  filterPhase: TripFilterPhase,
  today: LocalDate,
  searchActive: Boolean = false,
): String {
  val countForLabel = if (searchActive) totalCount else visibleCount
  if (featured == null) {
    return when {
      filterPhase != TripFilterPhase.All -> filterPhaseLabel(filterPhase)
      totalCount == 0 -> "Planifica, guarda y revive cada viaje"
      else -> "Ningún viaje coincide con la búsqueda o el filtro"
    }
  }
  val countdown = tripCountdownLabel(featured, today)
  val countPart = "$countForLabel ${if (countForLabel == 1) "viaje" else "viajes"}"
  val base = when (featured.homePhase(today)) {
    HomeTripPhase.Current -> "Viaje en curso · $countPart"
    HomeTripPhase.Upcoming -> {
      if (countdown != null) "$countdown · $countPart" else countPart
    }
    HomeTripPhase.Past -> "${featured.name} · ${tripMetaLabel(featured)}"
  }
  return if (filterPhase != TripFilterPhase.All) {
    "$base · ${filterPhaseLabel(filterPhase)}"
  } else {
    base
  }
}

private fun filterPhaseLabel(phase: TripFilterPhase): String = when (phase) {
  TripFilterPhase.All -> "Todos los viajes"
  TripFilterPhase.Current -> "En curso"
  TripFilterPhase.Upcoming -> "Próximos"
  TripFilterPhase.Past -> "Pasados"
  TripFilterPhase.Archived -> "Archivados"
}

private fun tripCountdownLabel(trip: Trip, today: LocalDate): String? {
  if (trip.homePhase(today) != HomeTripPhase.Upcoming) return null
  val days = ChronoUnit.DAYS.between(today, trip.startDate)
  return when (days) {
    0L -> "Sale hoy"
    1L -> "Sale mañana"
    in 2..30 -> "Sale en $days días"
    else -> "Del ${trip.startDate}"
  }
}

@Preview(name = "Home cap 1 — vacío (205:816)")
@Composable
fun HomeEmptyStatePreview() {
  MyOwnTripTheme {
    HomeEmptyState(onCreateTrip = {}, userFirstName = "Raquel")
  }
}

@Preview(name = "Home cap 1b — solo pasados (313:501)")
@Composable
fun HomeOnlyPastHeroPreview() {
  MyOwnTripTheme {
    HomeOnlyPastHero(userFirstName = "Raquel", greetingOverride = "Buenas tardes")
  }
}
