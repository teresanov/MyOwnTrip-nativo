package com.myowntrip.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myowntrip.app.domain.model.Trip
import com.myowntrip.app.ui.features.trips.HomeTripPhase
import com.myowntrip.app.ui.features.trips.homePhase
import com.myowntrip.app.ui.theme.MOTCorner
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MyOwnTripTheme
import com.myowntrip.app.ui.theme.rememberMOTButtonShape
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

private val SpanishLocale = Locale("es", "ES")
private val DateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", SpanishLocale)

/**
 * Figma: **TripHeroCard** `61199:7862` — portada 280dp + CTA tonal «Ver detalles».
 */
@Composable
fun TripHeroCard(
  trip: Trip,
  today: LocalDate,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  @DrawableRes previewCoverRes: Int? = null,
) {
  val phase = trip.homePhase(today)
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

  ElevatedCard(modifier = modifier.fillMaxWidth()) {
    Column(verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm)) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(280.dp)
          .clip(portadaShape),
      ) {
        TripCoverImage(
          trip = trip,
          previewCoverRes = previewCoverRes,
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
fun TripListCard(
  trip: Trip,
  today: LocalDate,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  @DrawableRes previewCoverRes: Int? = null,
) {
  val meta = tripMetaLabel(trip)
  val phase = trip.homePhase(today)
  MotHorizontalCard(
    title = trip.name,
    subtitle = trip.destination,
    supportingText = meta,
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

fun tripMetaLabel(trip: Trip): String {
  val days = ChronoUnit.DAYS.between(trip.startDate, trip.endDate) + 1
  val duration = if (days == 1L) "1 día" else "$days días"
  return "${trip.startDate.format(DateFormatter)} – ${trip.endDate.format(DateFormatter)} · $duration"
}

@Preview
@Composable
private fun TripHeroCardPreview() {
  MyOwnTripTheme {
    TripHeroCard(
      trip = Trip(
        id = "1",
        name = "Barcelona fin de semana",
        destination = "Barcelona",
        startDate = LocalDate.of(2026, 6, 20),
        endDate = LocalDate.of(2026, 6, 22),
        createdAt = 0L,
      ),
      today = LocalDate.of(2026, 6, 17),
      onClick = {},
      modifier = Modifier.padding(MOTSpacing.screenHorizontal),
    )
  }
}
