package com.myowntrip.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MOTTonalButton
import com.myowntrip.app.ui.theme.motPart

/** Figma `61199:7842` · portada 280dp. */
private val TripHeroPortadaHeight = 280.dp

/** Figma `61200:7944` · gap entre countdown / title / meta. */
private val TripHeroOverlayTextGap = 8.dp

/** Figma `61200:8087` · fila CTA 48dp, botón hug dentro. */
private val TripHeroCtaRowHeight = 48.dp

/**
 * Figma: **TripHeroCard** `61199:7862` / variante `61199:7842`.
 * CTA: **Button - tonal** `61200:8087` (XSmall · Square · hug).
 *
 * Parte visual: recibe los textos ya resueltos y la portada en [cover]. La traducción de un viaje
 * (fase, cuenta atrás, fechas) vive en la app: `TripHeroCard(trip, today, …)` en TripCards.kt.
 */
@Composable
fun TripHeroCard(
  eyebrow: String,
  title: String,
  meta: String,
  stateDescription: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  countdown: String? = null,
  ctaLabel: String = "Ver detalles",
  cover: @Composable (Modifier) -> Unit,
) {
  val heroContentDescription = tripHeroContentDescription(
    eyebrow = eyebrow,
    countdown = countdown,
    tripName = title,
    meta = meta,
    ctaLabel = ctaLabel,
  )
  Column(
    modifier = modifier.fillMaxWidth().motPart("card"),
    verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .height(TripHeroPortadaHeight)
        .motPart("cover"),
      shape = MaterialTheme.shapes.medium,
      color = MaterialTheme.colorScheme.surfaceContainerLow,
      shadowElevation = 1.dp,
    ) {
      Box(modifier = Modifier.fillMaxSize()) {
        cover(
          Modifier
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
            .padding(MOTSpacing.layoutMd)
            .motPart("eyebrow"),
        )
        Column(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(MOTSpacing.layoutMd)
            .motPart("text")
            .semantics { hideFromAccessibility() },
          verticalArrangement = Arrangement.spacedBy(TripHeroOverlayTextGap),
        ) {
          if (countdown != null) {
            Text(
              text = countdown,
              style = MaterialTheme.typography.titleMedium,
              color = MaterialTheme.colorScheme.tertiaryFixedDim,
              modifier = Modifier.motPart("countdown"),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
          }
          Text(
            text = title,
            modifier = Modifier.motPart("title"),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onPrimary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
          )
          Text(
            text = meta,
            modifier = Modifier.motPart("meta"),
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
        modifier = Modifier.motPart("cta").semantics {
          contentDescription = heroContentDescription
          this.stateDescription = stateDescription
        },
      ) {
        Text(
          text = ctaLabel,
          style = MaterialTheme.typography.labelLarge,
        )
      }
    }
  }
}

private fun tripHeroContentDescription(
  eyebrow: String,
  countdown: String?,
  tripName: String,
  meta: String,
  ctaLabel: String,
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
  append(". ")
  append(ctaLabel)
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
