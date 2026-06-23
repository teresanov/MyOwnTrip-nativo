package com.myowntrip.app.ui.composepreview

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.myowntrip.app.domain.model.Trip
import com.myowntrip.app.ui.components.PreviewCityCovers
import com.myowntrip.app.ui.components.TripHeroCard
import com.myowntrip.app.ui.theme.MOTSpacing
import java.time.LocalDate

@Preview(
  name = "TripHeroCard Elevated 61199-7842",
  showBackground = true,
  widthDp = 360,
  backgroundColor = 0xFFF7F4EF,
)
@Composable
fun TripHeroCardElevatedPreview() {
  MyOwnTripPreviewTheme {
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
      previewCoverRes = PreviewCityCovers.coverResForCity("Barcelona"),
      modifier = Modifier.padding(MOTSpacing.screenHorizontal),
    )
  }
}
