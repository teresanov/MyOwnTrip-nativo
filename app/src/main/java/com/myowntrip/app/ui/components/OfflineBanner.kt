package com.myowntrip.app.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Placeholder sync indicator — offline-first: local is source of truth.
 * Supabase sync queue is post-MVP.
 */
@Composable
fun OfflineBanner(modifier: Modifier = Modifier) {
  Surface(
    modifier = modifier,
    color = MaterialTheme.colorScheme.surfaceContainerHigh,
    tonalElevation = 1.dp,
  ) {
    Text(
      text = "Offline ready · changes saved on device",
      style = MaterialTheme.typography.labelMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    )
  }
}
