package com.myowntrip.app.ui.components

import androidx.annotation.DrawableRes
import com.myowntrip.app.R
import com.myowntrip.app.data.cover.DestinationCoverNormalizer

/**
 * Portadas empaquetadas **solo para @Preview y revisión Figma** — no usar en runtime.
 * En producción: [com.myowntrip.app.domain.cover.DestinationCoverRepository] + caché local.
 */
object PreviewCityCovers {

  private val coversByKey: Map<String, Int> = mapOf(
    "barcelona" to R.drawable.home_trip_barcelona,
    "lisboa" to R.drawable.home_trip_lisboa,
    "lisbon" to R.drawable.home_trip_lisboa,
    "tokio" to R.drawable.home_trip_tokio,
    "tokyo" to R.drawable.home_trip_tokio,
  )

  @DrawableRes
  fun coverResForCity(cityOrQuery: String): Int? {
    val key = DestinationCoverNormalizer.normalize(cityOrQuery)
    if (key.isEmpty()) return null

    coversByKey[key]?.let { return it }

    coversByKey.entries.firstOrNull { (catalogKey, _) ->
      key == catalogKey || key.startsWith(catalogKey) || catalogKey.startsWith(key)
    }?.let { return it.value }

    return coversByKey.entries
      .filter { (catalogKey, _) -> key.contains(catalogKey) || catalogKey.contains(key) }
      .maxByOrNull { it.key.length }
      ?.value
  }
}
