package com.myowntrip.app.ui.features.trips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.components.home.HomeEmptyState
import com.myowntrip.app.ui.components.home.HomeFilterMenuPresentation
import com.myowntrip.app.ui.components.home.HomeTripsContentState
import com.myowntrip.app.ui.components.home.HomeTripsScreen
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MyOwnTripTheme
import java.time.LocalDate

/**
 * Revisión visual del flow Home (Figma `205:813`) — caps 1, 2 y 3 apilados.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFlowReviewScreen(
  onBack: () -> Unit = {},
  modifier: Modifier = Modifier,
) {
  val today = LocalDate.of(2026, 6, 17)
  val sorted = sortTripsForHome(previewHomeTrips(), today)
  val featured = sorted.first()

  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        title = { Text("Home · 3 caps") },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
          }
        },
      )
    },
  ) { padding ->
    Column(
      modifier = Modifier
        .padding(padding)
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(vertical = MOTSpacing.layoutMd),
      verticalArrangement = Arrangement.spacedBy(MOTSpacing.layoutLg),
    ) {
      FlowCapLabel("Cap 1 · Home vacío (205:816)")
      HomeEmptyState(
        onCreateTrip = {},
        userFirstName = "Raquel",
        modifier = Modifier.fillMaxWidth(),
      )

      HorizontalDivider(modifier = Modifier.padding(horizontal = MOTSpacing.screenHorizontal))

      FlowCapLabel("Cap 2 · Home con viajes (205:1018)")
      HomeTripsScreen(
        state = HomeTripsContentState(
          featuredTrip = featured,
          otherTrips = sorted.drop(1),
          visibleTripCount = sorted.size,
          totalTripCount = sorted.size,
          searchQuery = "",
          filterPhase = TripFilterPhase.All,
          sortOrder = TripSortOrder.DateUpcoming,
          filterMenuExpanded = false,
          today = today,
          userFirstName = "Raquel",
          searchPlaceholder = "Barcelona",
          greetingOverride = "Buenas tardes",
          usePreviewCityCovers = true,
        ),
        onSearchQueryChange = {},
        onFilterMenuExpandedChange = {},
        onFilterPhaseChange = {},
        onSortOrderChange = {},
        onTripClick = {},
        modifier = Modifier
          .fillMaxWidth()
          .height(800.dp),
      )

      HorizontalDivider(modifier = Modifier.padding(horizontal = MOTSpacing.screenHorizontal))

      FlowCapLabel("Cap 3 · Búsqueda + menú filtros (228:8161)")
      HomeTripsScreen(
        state = HomeTripsContentState(
          featuredTrip = featured,
          otherTrips = sorted.drop(1),
          visibleTripCount = sorted.size,
          totalTripCount = sorted.size,
          searchQuery = "Barcelona",
          filterPhase = TripFilterPhase.All,
          sortOrder = TripSortOrder.DateUpcoming,
          filterMenuExpanded = true,
          today = today,
          userFirstName = "Raquel",
          searchPlaceholder = "Barcelona",
          greetingOverride = "Buenas tardes",
          usePreviewCityCovers = true,
        ),
        onSearchQueryChange = {},
        onFilterMenuExpandedChange = {},
        onFilterPhaseChange = {},
        onSortOrderChange = {},
        onTripClick = {},
        filterMenuPresentation = HomeFilterMenuPresentation.Overlay,
        modifier = Modifier
          .fillMaxWidth()
          .height(1100.dp),
      )
    }
  }
}

@Composable
private fun FlowCapLabel(text: String) {
  Text(
    text = text,
    style = MaterialTheme.typography.labelLarge,
    color = MaterialTheme.colorScheme.primary,
    modifier = Modifier.padding(horizontal = MOTSpacing.screenHorizontal),
  )
}

@Preview(name = "Home flow · 3 caps (Figma 205:813)", showBackground = true, widthDp = 360, heightDp = 3200)
@Composable
fun HomeFlowReviewPreview() {
  MyOwnTripTheme {
    Surface(color = MaterialTheme.colorScheme.surface) {
      HomeFlowReviewScreen()
    }
  }
}
