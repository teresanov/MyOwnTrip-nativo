package com.myowntrip.app

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.CompositionLocalProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.myowntrip.app.ui.features.wallet.WalletFormScreen
import com.myowntrip.app.ui.features.wallet.WalletFormViewModel
import com.myowntrip.app.ui.features.wallet.resolveAttachmentDisplayName
import com.myowntrip.app.ui.theme.LocalReduceMotion
import com.myowntrip.app.ui.theme.MyOwnTripTheme
import com.myowntrip.app.ui.theme.rememberReduceMotion
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImportActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    val shareUri = extractShareUri(intent)
    val mimeType = intent.type
    val fileName = shareUri?.let { resolveAttachmentDisplayName(this, it) }

    setContent {
      val reduceMotion = rememberReduceMotion()
      MyOwnTripTheme {
        CompositionLocalProvider(LocalReduceMotion provides reduceMotion) {
          val viewModel: WalletFormViewModel = hiltViewModel()
          LaunchedEffect(shareUri) {
            viewModel.setImportData(
              uri = shareUri,
              mimeType = mimeType,
              fileName = fileName,
            )
          }
          WalletFormScreen(
            onBack = { finish() },
            onSaved = { finish() },
            viewModel = viewModel,
          )
        }
      }
    }
  }

  private fun extractShareUri(intent: Intent): Uri? {
    return when (intent.action) {
      Intent.ACTION_SEND -> intent.parcelableExtra(Intent.EXTRA_STREAM)
      Intent.ACTION_SEND_MULTIPLE -> intent.parcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)?.firstOrNull()
      else -> null
    }
  }

  private inline fun <reified T : android.os.Parcelable> Intent.parcelableExtra(key: String): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      getParcelableExtra(key, T::class.java)
    } else {
      @Suppress("DEPRECATION")
      getParcelableExtra(key)
    }

  private inline fun <reified T : android.os.Parcelable> Intent.parcelableArrayListExtra(key: String): ArrayList<T>? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      getParcelableArrayListExtra(key, T::class.java)
    } else {
      @Suppress("DEPRECATION")
      getParcelableArrayListExtra(key)
    }
}
