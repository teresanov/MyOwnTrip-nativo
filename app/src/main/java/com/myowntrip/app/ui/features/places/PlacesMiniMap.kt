package com.myowntrip.app.ui.features.places

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.myowntrip.app.domain.model.Restaurant
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

/**
 * Mapa OSM (osmdroid, sin API key) con un pin por sitio con ubicación, encajado a los
 * pines. [interactive] = false para una miniatura de solo vistazo dentro de una lista
 * (sin paneo/zoom propio); el tap para abrir el mapa completo lo gestiona el llamador
 * con un overlay encima, porque el AndroidView se queda con el touch si se lo dejamos.
 */
@Composable
fun PlacesMiniMap(
  places: List<Restaurant>,
  modifier: Modifier = Modifier,
  interactive: Boolean = true,
) {
  val context = LocalContext.current
  DisposableEffect(Unit) {
    val prefs = context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
    Configuration.getInstance().load(context, prefs)
    Configuration.getInstance().userAgentValue = context.packageName
    onDispose { }
  }

  AndroidView(
    modifier = modifier,
    factory = { ctx ->
      val prefs = ctx.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
      Configuration.getInstance().load(ctx, prefs)
      Configuration.getInstance().userAgentValue = ctx.packageName
      MapView(ctx).apply {
        setDestroyMode(false)
        setTileSource(TileSourceFactory.MAPNIK)
        setMultiTouchControls(interactive)
        isClickable = interactive
        if (!interactive) {
          // Miniatura: bloquea pan/zoom propio, el tap lo captura el overlay del llamador.
          setOnTouchListener { _, _ -> true }
        }
        onResume()
      }
    },
    update = { map ->
      map.overlays.removeAll { it is Marker }
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
      map.post {
        if (points.size == 1) {
          map.controller.setZoom(15.0)
          map.controller.setCenter(points.first())
        } else if (points.isNotEmpty()) {
          map.zoomToBoundingBox(BoundingBox.fromGeoPoints(points), false, 96)
        }
        map.invalidate()
      }
    },
    onRelease = { map ->
      map.onPause()
    },
  )
}
