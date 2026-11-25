package com.myowntrip.app.ui.theme

import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberReduceMotion(): Boolean {
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
