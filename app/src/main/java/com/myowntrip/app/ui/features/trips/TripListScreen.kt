package com.myowntrip.app.ui.features.trips

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myowntrip.app.domain.model.Trip
import com.myowntrip.app.ui.components.OfflineBanner
import com.myowntrip.app.ui.theme.MOTButton
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripListScreen(
  onCreateTrip: () -> Unit,
  onTripClick: (String) -> Unit,
  viewModel: TripListViewModel = hiltViewModel(),
) {
  val trips by viewModel.trips.collectAsStateWithLifecycle()

  Scaffold(
    topBar = {
      TopAppBar(title = { Text("My trips") })
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = onCreateTrip,
        modifier = Modifier.semantics { contentDescription = "Create trip" },
      ) {
        Icon(Icons.Default.Add, contentDescription = null)
      }
    },
  ) { padding ->
    Column(modifier = Modifier.padding(padding).fillMaxSize()) {
      OfflineBanner(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp))
    if (trips.isEmpty()) {
      EmptyTripsState(modifier = Modifier.fillMaxSize(), onCreateTrip = onCreateTrip)
    } else {
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        items(trips, key = { it.id }) { trip ->
          TripCard(trip = trip, onClick = { onTripClick(trip.id) })
        }
      }
    }
    }
  }
}

@Composable
private fun EmptyTripsState(modifier: Modifier = Modifier, onCreateTrip: () -> Unit) {
  Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text("No trips yet", style = MaterialTheme.typography.titleMedium)
      Text(
        "Create your first trip to start your travel notebook",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
      )
      MOTButton(onClick = onCreateTrip) {
        Text("Create trip")
      }
    }
  }
}

@Composable
private fun TripCard(trip: Trip, onClick: () -> Unit) {
  val formatter = DateTimeFormatter.ofPattern("d MMM yyyy")
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .semantics { contentDescription = "Trip ${trip.name}" },
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(trip.name, style = MaterialTheme.typography.titleMedium)
      Text(trip.destination, style = MaterialTheme.typography.bodyMedium)
      Text(
        "${trip.startDate.format(formatter)} – ${trip.endDate.format(formatter)}",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}
