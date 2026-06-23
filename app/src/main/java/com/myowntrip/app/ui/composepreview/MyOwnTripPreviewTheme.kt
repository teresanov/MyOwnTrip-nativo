package com.myowntrip.app.ui.composepreview

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.myowntrip.app.ui.theme.LocalReduceMotion
import com.myowntrip.app.ui.theme.MyOwnTripTheme

/** Tema estable para @Preview en Android Studio (sin dynamic color). */
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
