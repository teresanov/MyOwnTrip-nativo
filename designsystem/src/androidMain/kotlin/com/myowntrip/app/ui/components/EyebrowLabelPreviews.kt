package com.myowntrip.app.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.MyOwnTripTheme

// Vistas previas de Android Studio: viven en androidMain porque @Preview es solo de Android.

@Preview
@Composable
internal fun EyebrowLabelPreview() {
  MyOwnTripTheme {
    EyebrowLabel(text = "Próximo viaje")
  }
}
