package com.myowntrip.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.myowntrip.app.domain.model.Trip
import com.myowntrip.app.ui.features.trips.HomeTripPhase
import com.myowntrip.app.ui.features.trips.homePhase
import com.myowntrip.app.ui.theme.MOTCorner
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MOTTonalButton
import com.myowntrip.app.ui.theme.MyOwnTripPreviewTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

private val SpanishLocale = Locale("es", "ES")
private val DateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", SpanishLocale)

/** Figma `61199:7842` · portada 280dp. */
private val TripHeroPortadaHeight = 280.dp

/** Figma `61200:7944` · gap entre countdown / title / meta. */
private val TripHeroOverlayTextGap = 8.dp

/** Figma `61200:8087` · fila CTA 48dp, botón hug dentro. */
private val TripHeroCtaRowHeight = 48.dp

/**
 * Figma: **TripHeroCard** `61199:7862` / variante `61199:7842`.
 * CTA: **Button - tonal** `61200:8087` (XSmall · Square · hug).
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
  val portadaShape = RoundedCornerShape(MOTCorner.Medium)

  Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .height(TripHeroPortadaHeight),
      shape = portadaShape,
      color = MaterialTheme.colorScheme.surfaceContainerLow,
      shadowElevation = 1.dp,
    ) {
      Box(modifier = Modifier.fillMaxSize()) {
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
            .background(tripHeroScrimBrush(MaterialTheme.colorScheme.scrim))
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
          verticalArrangement = Arrangement.spacedBy(TripHeroOverlayTextGap),
        ) {
          if (countdown != null) {
            Text(
              text = countdown,
              style = MaterialTheme.typography.titleMedium,
              color = MaterialTheme.colorScheme.tertiaryFixedDim,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
          }
          Text(
            text = trip.name,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onPrimary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
          )
          Text(
            text = meta,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
          )
        }
      }
    }
    Box(
      modifier = Modifier.height(TripHeroCtaRowHeight),
      contentAlignment = Alignment.CenterStart,
    ) {
      MOTTonalButton(
        onClick = onClick,
        modifier = Modifier.semantics {
          contentDescription = heroContentDescription
          stateDescription = a11yPhase
        },
      ) {
        Text(
          text = "Ver detalles",
          style = MaterialTheme.typography.labelLarge,
        )
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

/** Degradado portada — Figma `61199:7825` (to-t, 3.929% / 62.443%). */
private fun tripHeroScrimBrush(scrim: Color): Brush = Brush.verticalGradient(
  colorStops = arrayOf(
    0f to scrim.copy(alpha = 0.45f),
    0.37557f to scrim.copy(alpha = 0.45f),
    0.96071f to scrim.copy(alpha = 0.9f),
    1f to scrim.copy(alpha = 0.9f),
  ),
)

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
