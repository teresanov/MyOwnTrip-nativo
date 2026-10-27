package com.myowntrip.app.ui.theme

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

/** Tema estable para [@Preview][androidx.compose.ui.tooling.preview.Preview] en Android Studio. */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MyOwnTripPreviewTheme(
  darkTheme: Boolean = false,
  content: @Composable () -> Unit,
) {
  CompositionLocalProvider(LocalReduceMotion provides true) {
    MyOwnTripTheme(
      darkTheme = darkTheme,
      dynamicColor = false,
      content = content,
    )
  }
}
