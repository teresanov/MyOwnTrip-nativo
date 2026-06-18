package com.myowntrip.app.data.cover

import java.text.Normalizer
import java.util.Locale

object DestinationCoverNormalizer {
  private val Spanish = Locale("es", "ES")

  fun normalize(input: String): String {
    val trimmed = input.trim().lowercase(Spanish)
    if (trimmed.isEmpty()) return ""
    return Normalizer.normalize(trimmed, Normalizer.Form.NFD)
      .replace(Regex("\\p{M}+"), "")
  }

  fun cacheKey(destination: String): String =
    normalize(destination).replace(Regex("[^a-z0-9]+"), "_").take(64)
}
