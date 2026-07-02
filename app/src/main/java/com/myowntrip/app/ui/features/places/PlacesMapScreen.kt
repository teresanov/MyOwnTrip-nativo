package com.myowntrip.app.ui.features.places

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myowntrip.app.ui.theme.MOTIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesMapScreen(
  onBack: () -> Unit,
  viewModel: PlacesMapViewModel = hiltViewModel(),
) {
  val places by viewModel.places.collectAsStateWithLifecycle()

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Mapa de sitios") },
        navigationIcon = {
          MOTIconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
          }
        },
      )
    },
  ) { padding ->
    if (places.isEmpty()) {
      Box(
        modifier = Modifier
          .padding(padding)
          .fillMaxSize(),
        contentAlignment = Alignment.Center,
      ) {
        Text(
          "Ningún sitio tiene ubicación todavía.",
          style = MaterialTheme.typography.bodyLarge,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
      return@Scaffold
    }

    PlacesMiniMap(
      places = places,
      interactive = true,
      modifier = Modifier
        .padding(padding)
        .fillMaxSize(),
    )
  }
}
