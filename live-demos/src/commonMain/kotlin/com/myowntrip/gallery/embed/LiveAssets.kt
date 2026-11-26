package com.myowntrip.gallery.embed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap

/** Imágenes reales de la app que las demos cargan del showcase (mismo archivo que el drawable). */
object LiveAssets {
  /** `home_empty_map.jpg`: la imagen de la card de Home vacío. */
  var homeEmptyMap: ImageBitmap? by mutableStateOf(null)
}
