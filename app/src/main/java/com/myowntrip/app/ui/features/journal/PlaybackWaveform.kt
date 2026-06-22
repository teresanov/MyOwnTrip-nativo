package com.myowntrip.app.ui.features.journal

import kotlin.math.sin

internal fun buildPlaybackWaveformLevels(
  isPlaying: Boolean,
  progress: Float,
  phaseMillis: Long,
  barCount: Int = 24,
): List<Float> {
  val phase = phaseMillis / 120.0
  return List(barCount) { index ->
    val position = index.toFloat() / barCount.toFloat()
    val played = position <= progress
    if (isPlaying) {
      val wave = sin(phase + index * 0.55).toFloat()
      (0.22f + (wave + 1f) * 0.32f + if (played) 0.12f else 0f).coerceIn(0.12f, 1f)
    } else if (played && progress > 0f) {
      (0.35f + sin(index * 0.8).toFloat().coerceIn(-1f, 1f) * 0.08f + 0.12f).coerceIn(0.12f, 0.65f)
    } else {
      0.12f
    }
  }
}
