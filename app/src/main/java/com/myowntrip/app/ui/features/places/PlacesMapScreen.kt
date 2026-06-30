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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myowntrip.app.ui.theme.MOTIconButton
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesMapScreen(
  onBack: () -> Unit,
  viewModel: PlacesMapViewModel = hiltViewModel(),
) {
  val places by viewModel.places.collectAsStateWithLifecycle()
  val context = LocalContext.current

  // osmdroid exige un User-Agent propio (OSM bloquea el genérico). Sin key ni cuenta.
  LaunchedEffect(Unit) {
    Configuration.getInstance().userAgentValue = context.packageName
  }

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

    AndroidView(
      modifier = Modifier
        .padding(padding)
        .fillMaxSize(),
      factory = { ctx ->
        MapView(ctx).apply {
          setTileSource(TileSourceFactory.MAPNIK)
          setMultiTouchControls(true)
          onResume()
        }
      },
      update = { map ->
        map.overlays.clear()
        val points = places.mapNotNull { place ->
          val lat = place.latitude ?: return@mapNotNull null
          val lng = place.longitude ?: return@mapNotNull null
          val point = GeoPoint(lat, lng)
          Marker(map).apply {
            position = point
            title = place.name
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
          }.also { map.overlays.add(it) }
          point
        }
        // Encajar la vista a todos los pines (o centrar si es uno solo). post() para esperar
        // a que el mapa tenga tamaño medido.
        map.post {
          if (points.size == 1) {
            map.controller.setZoom(15.0)
            map.controller.setCenter(points.first())
          } else {
            map.zoomToBoundingBox(BoundingBox.fromGeoPoints(points), false, 96)
          }
        }
        map.invalidate()
      },
      onRelease = { map -> map.onDetach() },
    )
  }
}
