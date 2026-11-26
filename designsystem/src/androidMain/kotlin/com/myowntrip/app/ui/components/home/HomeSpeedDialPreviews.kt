package com.myowntrip.app.ui.components.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.AppMotion
import com.myowntrip.app.ui.theme.LocalReduceMotion
import com.myowntrip.app.ui.theme.MOTIcons
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MyOwnTripTheme

// Vistas previas de Android Studio: viven en androidMain porque @Preview es solo de Android.

@Preview(showBackground = true, widthDp = 360, heightDp = 200)
@Composable
internal fun HomeSpeedDialPreview() {
  var expanded by remember { mutableStateOf(true) }
  MyOwnTripTheme {
    Box(Modifier.fillMaxSize()) {
      HomeSpeedDial(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        actions = listOf(
          HomeQuickAction(
            label = "Nuevo viaje",
            icon = MOTIcons.Luggage,
            contentDescription = "Crear viaje",
            onClick = {},
          ),
          HomeQuickAction(
            label = "Documento",
            icon = MOTIcons.Upload,
            contentDescription = "Añadir documento",
            onClick = {},
          ),
          HomeQuickAction(
            label = "Recuerdo",
            icon = MOTIcons.PhotoCamera,
            contentDescription = "Añadir recuerdo",
            onClick = {},
          ),
        ),
        modifier = Modifier
          .fillMaxSize()
          .padding(MOTSpacing.screenHorizontal),
      )
    }
  }
}
