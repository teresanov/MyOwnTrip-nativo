package com.myowntrip.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.myowntrip.app.ui.features.places.PlaceImportScreen
import com.myowntrip.app.ui.features.places.PlaceImportViewModel
import com.myowntrip.app.ui.theme.LocalReduceMotion
import com.myowntrip.app.ui.theme.MyOwnTripTheme
import com.myowntrip.app.ui.theme.rememberReduceMotion
import dagger.hilt.android.AndroidEntryPoint

/**
 * Recibe un sitio compartido desde Google Maps (ACTION_SEND text/plain): nombre + enlace
 * corto. Confirma y lo guarda en un viaje. Separada de [ImportActivity] (esa es para
 * documentos PDF/imagen del wallet); aquí solo entra texto.
 */
@AndroidEntryPoint
class PlaceImportActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val sharedText = intent?.takeIf { it.action == Intent.ACTION_SEND }
      ?.getStringExtra(Intent.EXTRA_TEXT)
    val subject = intent?.getStringExtra(Intent.EXTRA_SUBJECT)

    setContent {
      val reduceMotion = rememberReduceMotion()
      MyOwnTripTheme {
        CompositionLocalProvider(LocalReduceMotion provides reduceMotion) {
          val viewModel: PlaceImportViewModel = hiltViewModel()
          LaunchedEffect(Unit) {
            viewModel.start(sharedText = sharedText, subject = subject)
          }
          PlaceImportScreen(
            onBack = { finish() },
            onSaved = { finish() },
            viewModel = viewModel,
          )
        }
      }
    }
  }
}
