package com.myowntrip.app.ui.features.wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WalletFormPreviewScaffold(
  title: String,
  modifier: Modifier = Modifier,
  /** false cuando el contenido ya ancla CTAs (p. ej. WalletImportReviewStep). */
  scrollable: Boolean = true,
  content: @Composable () -> Unit,
) {
  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        title = { Text(title) },
        navigationIcon = {
          MOTIconButton(onClick = {}) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
          }
        },
      )
    },
  ) { padding ->
    val base = Modifier
      .padding(padding)
      .fillMaxSize()
      .padding(
        start = MOTSpacing.screenHorizontal,
        end = MOTSpacing.screenHorizontal,
        top = MOTSpacing.screenHorizontal,
        bottom = MOTSpacing.screenContentBottom,
      )
    if (scrollable) {
      Column(
        modifier = base.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(MOTSpacing.layoutMd),
      ) {
        content()
      }
    } else {
      Column(modifier = base) {
        content()
      }
    }
  }
}
