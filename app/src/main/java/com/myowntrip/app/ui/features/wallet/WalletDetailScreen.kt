package com.myowntrip.app.ui.features.wallet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.myowntrip.app.ui.theme.MOTIconButton
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletDetailScreen(
  onBack: () -> Unit,
  viewModel: WalletDetailViewModel = hiltViewModel(),
) {
  val entry by viewModel.entry.collectAsStateWithLifecycle()

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(entry?.title ?: "Wallet entry") },
        navigationIcon = {
          MOTIconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
      )
    },
  ) { padding ->
  Column(
    modifier = Modifier
      .padding(padding)
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(16.dp),
  ) {
    entry?.let { item ->
      Text(item.type.name, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.tertiary)
      item.date?.let {
        Text(
          "Date: ${it.format(DateTimeFormatter.ofPattern("d MMM yyyy"))}",
          style = MaterialTheme.typography.bodyMedium,
          modifier = Modifier.padding(top = 8.dp),
        )
      }
      item.time?.let {
        Text("Time: $it", style = MaterialTheme.typography.bodyMedium)
      }
      item.notes?.let {
        Text(it, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 12.dp))
      }
      item.pdfUri?.let { uri ->
        AsyncImage(
          model = uri,
          contentDescription = "Document preview",
          modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .padding(top = 16.dp),
          contentScale = ContentScale.Fit,
        )
        Text(
          "Offline document available",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(top = 8.dp),
        )
      }
    } ?: Text("Loading…")
  }
  }
}
