package com.myowntrip.app.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myowntrip.app.domain.model.Trip
import com.myowntrip.app.ui.features.trips.HomeTripPhase
import com.myowntrip.app.ui.features.trips.homePhase
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MyOwnTripPreviewTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

private val SpanishLocale = Locale("es", "ES")
private val DateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", SpanishLocale)

/** TripHeroCard a partir de un viaje: fase, cuenta atrás y fechas → la card del design system. */
@Composable
fun TripHeroCard(
  trip: Trip,
  today: LocalDate,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  @DrawableRes previewCoverRes: Int? = null,
) {
  val phase = trip.homePhase(today)
  TripHeroCard(
    eyebrow = featuredEyebrow(phase),
    title = trip.name,
    meta = tripMetaLabel(trip),
    stateDescription = phaseStateDescription(phase),
    countdown = tripCountdownLabel(trip, today),
    onClick = onClick,
    modifier = modifier,
    cover = { coverModifier ->
      TripCoverImage(trip = trip, previewCoverRes = previewCoverRes, modifier = coverModifier)
    },
  )
}

@Composable
fun TripListCard(
  trip: Trip,
  today: LocalDate,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  @DrawableRes previewCoverRes: Int? = null,
) {
  val phase = trip.homePhase(today)
  MotHorizontalCard(
    title = trip.name,
    subtitle = trip.destination,
    supportingText = tripMetaLabel(trip),
    onClick = onClick,
    modifier = modifier,
    stateDescription = phaseStateDescription(phase),
  ) {
    TripCoverImage(
      trip = trip,
      previewCoverRes = previewCoverRes,
      modifier = Modifier.fillMaxSize(),
    )
  }
}

private fun tripCountdownLabel(trip: Trip, today: LocalDate): String? {
  if (trip.homePhase(today) != HomeTripPhase.Upcoming) return null
  val days = ChronoUnit.DAYS.between(today, trip.startDate)
  return when (days) {
    0L -> "Sale hoy"
    1L -> "Sale mañana"
    in 2..30 -> "Sale en $days días"
    else -> "Del ${trip.startDate.format(DateFormatter)}"
  }
}

private fun featuredEyebrow(phase: HomeTripPhase): String = when (phase) {
  HomeTripPhase.Current -> "En destino"
  HomeTripPhase.Upcoming -> "Próximo viaje"
  HomeTripPhase.Past -> "Recuerdo"
}

private fun phaseStateDescription(phase: HomeTripPhase): String = when (phase) {
  HomeTripPhase.Current -> "Viaje en curso"
  HomeTripPhase.Upcoming -> "Próximo viaje"
  HomeTripPhase.Past -> "Viaje pasado"
}

fun tripMetaLabel(trip: Trip): String {
  val days = ChronoUnit.DAYS.between(trip.startDate, trip.endDate) + 1
  val duration = if (days == 1L) "1 día" else "$days días"
  return "${trip.startDate.format(DateFormatter)} – ${trip.endDate.format(DateFormatter)} · $duration"
}

@Preview(
  name = "TripHeroCard Elevated 61199-7842",
  showBackground = true,
  widthDp = 360,
  backgroundColor = 0xFFF7F4EF,
)
@Composable
private fun TripHeroCardElevatedPreview() {
  MyOwnTripPreviewTheme {
    TripHeroCard(
      trip = Trip(
        id = "1",
        name = "Barcelona fin de semana",
        destination = "Barcelona",
        startDate = LocalDate.of(2026, 7, 4),
        endDate = LocalDate.of(2026, 7, 6),
        createdAt = 0L,
      ),
      today = LocalDate.of(2026, 6, 17),
      onClick = {},
      previewCoverRes = PreviewCityCovers.coverResForCity("Barcelona"),
      modifier = Modifier.padding(MOTSpacing.screenHorizontal),
    )
  }
}
