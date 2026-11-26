package com.myowntrip.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/** Portada de reserva cuando el viaje no tiene foto: las dos primeras letras del destino. */
@Composable
fun TripCoverPlaceholder(
  destination: String,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier,
    color = MaterialTheme.colorScheme.surfaceContainerHighest,
    shape = MaterialTheme.shapes.small,
  ) {
    Box(contentAlignment = Alignment.Center) {
      Text(
        text = destination.take(2).uppercase(),
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.tertiary,
      )
    }
  }
}
