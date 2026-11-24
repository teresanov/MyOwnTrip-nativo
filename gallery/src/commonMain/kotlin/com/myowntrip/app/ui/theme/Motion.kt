package com.myowntrip.app.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.staticCompositionLocalOf

object AppMotion {
  const val DurationShort = 200
  const val DurationMedium = 300
  const val DurationLong = 400
  const val DurationPressIn = 120

  val EmphasizedDecelerateEasing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
  val EmphasizedAccelerateEasing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)

  const val ReleaseSpringDamping = 0.6f
  const val ReleaseSpringStiffness = 380f
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
