package com.myowntrip.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.myowntrip.app.ui.navigation.AppNavGraph
import com.myowntrip.app.ui.theme.LocalReduceMotion
import com.myowntrip.app.ui.theme.MyOwnTripTheme
import com.myowntrip.app.ui.theme.rememberReduceMotion
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val reduceMotion = rememberReduceMotion()
      MyOwnTripTheme {
        CompositionLocalProvider(LocalReduceMotion provides reduceMotion) {
          AppNavGraph()
        }
      }
    }
  }
}
