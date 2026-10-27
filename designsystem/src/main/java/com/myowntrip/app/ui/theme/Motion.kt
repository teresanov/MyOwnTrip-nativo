package com.myowntrip.app.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import android.provider.Settings

object AppMotion {
  const val DurationShort = 200
  const val DurationMedium = 300
  const val DurationLong = 400
  /** Entrada de pulsación en botones: el feedback al dedo debe ser casi inmediato (ADR 004 rev. squish + muelle). */
  const val DurationPressIn = 120

  /** M3 emphasized decelerate — sin overshoot. */
  val EmphasizedDecelerateEasing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
  /** M3 emphasized accelerate — entrada rápida de pulsación. */
  val EmphasizedAccelerateEasing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)

  /** Muelle de liberación: rebasa un poco (damping < 1) y asienta en ~480 ms percibidos. */
  const val ReleaseSpringDamping = 0.6f
  const val ReleaseSpringStiffness = 380f
  /** Escala del squish al presionar — el dedo "comprime" el botón. */
  const val PressedButtonScale = 0.96f

  fun tweenShort() = tween<Float>(durationMillis = DurationShort, easing = FastOutSlowInEasing)
  fun tweenMedium() = tween<Float>(durationMillis = DurationMedium, easing = FastOutSlowInEasing)

  fun <T> pressInSpec(): TweenSpec<T> = tween(
    durationMillis = DurationPressIn,
    easing = EmphasizedAccelerateEasing,
  )

  fun <T> releaseSpringSpec(visibilityThreshold: T? = null): SpringSpec<T> = spring(
    dampingRatio = ReleaseSpringDamping,
    stiffness = ReleaseSpringStiffness,
    visibilityThreshold = visibilityThreshold,
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
