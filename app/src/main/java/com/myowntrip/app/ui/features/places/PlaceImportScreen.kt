package com.myowntrip.app.ui.features.places

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myowntrip.app.domain.model.PlaceCategory
import com.myowntrip.app.ui.features.restaurants.categoryLabel
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTIconButton

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PlaceImportScreen(
  onBack: () -> Unit,
  onSaved: () -> Unit,
  viewModel: PlaceImportViewModel = hiltViewModel(),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Guardar sitio") },
        navigationIcon = {
          MOTIconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
          }
        },
      )
    },
  ) { padding ->
    if (state.trips.isEmpty()) {
      Column(
        modifier = Modifier
          .padding(padding)
          .fillMaxSize()
          .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Text(
          "Crea un viaje antes de guardar sitios.",
          style = MaterialTheme.typography.bodyLarge,
          textAlign = TextAlign.Center,
        )
        MOTButton(onClick = onBack) { Text("Entendido") }
      }
      return@Scaffold
    }

    Column(
      modifier = Modifier
        .padding(padding)
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      OutlinedTextField(
        value = state.name,
        onValueChange = viewModel::onNameChange,
        label = { Text("Nombre") },
        modifier = Modifier.fillMaxWidth(),
      )

      LocationStatus(resolving = state.resolving, hasLocation = state.hasLocation, hasLink = state.googleMapsUri != null)

      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Categoría", style = MaterialTheme.typography.labelLarge)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          PlaceCategory.entries.forEach { category ->
            FilterChip(
              selected = state.category == category,
              onClick = { viewModel.onCategoryChange(category) },
              label = { Text(categoryLabel(category)) },
            )
          }
        }
      }

      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Añadir al viaje", style = MaterialTheme.typography.labelLarge)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          state.trips.forEach { trip ->
            FilterChip(
              selected = state.selectedTripId == trip.id,
              onClick = { viewModel.onTripSelected(trip.id) },
              label = { Text(trip.name) },
            )
          }
        }
      }

      MOTButton(
        onClick = { viewModel.save(onSaved) },
        enabled = state.canSave,
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text("Guardar sitio")
      }
    }
  }
}

@Composable
private fun LocationStatus(resolving: Boolean, hasLocation: Boolean, hasLink: Boolean) {
  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
    when {
      resolving -> {
        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
        Text("Buscando la ubicación…", style = MaterialTheme.typography.bodyMedium)
      }
      hasLocation -> {
        Icon(
          Icons.Default.LocationOn,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.tertiary,
          modifier = Modifier.size(18.dp),
        )
        Text("Ubicación detectada", style = MaterialTheme.typography.bodyMedium)
      }
      hasLink -> Text(
        "Sin coordenadas, pero podrás abrirlo en Maps.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
      else -> Text(
        "Sitio sin enlace de mapa.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}
