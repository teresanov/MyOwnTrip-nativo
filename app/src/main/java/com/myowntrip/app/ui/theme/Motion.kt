package com.myowntrip.app.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import android.provider.Settings

object AppMotion {
  const val DurationShort = 200
  const val DurationMedium = 300
  const val DurationLong = 400
  /** Shape morph botones 0→20dp — ADR 004; preview HTML alineado. */
  const val DurationShapeMorph = 520

  /** M3 emphasized decelerate — sin overshoot. */
  val EmphasizedDecelerateEasing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)

  fun tweenShort() = tween<Float>(durationMillis = DurationShort, easing = FastOutSlowInEasing)
  fun tweenMedium() = tween<Float>(durationMillis = DurationMedium, easing = FastOutSlowInEasing)

  fun shapeMorphSpec(): TweenSpec<Dp> = tween(
    durationMillis = DurationShapeMorph,
    easing = EmphasizedDecelerateEasing,
  )
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
