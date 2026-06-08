package com.myowntrip.app.ui.theme

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import android.provider.Settings

object AppMotion {
  const val DurationShort = 200
  const val DurationMedium = 300
  const val DurationLong = 400

  fun tweenShort() = tween<Float>(durationMillis = DurationShort, easing = FastOutSlowInEasing)
  fun tweenMedium() = tween<Float>(durationMillis = DurationMedium, easing = FastOutSlowInEasing)
}

val LocalReduceMotion = staticCompositionLocalOf { false }

@Composable
fun rememberReduceMotion(): Boolean {
  val context = LocalContext.current
  return try {
    Settings.Global.getFloat(
      context.contentResolver,
      Settings.Global.TRANSITION_ANIMATION_SCALE,
    ) == 0f
  } catch (_: Exception) {
    false
  }
}
