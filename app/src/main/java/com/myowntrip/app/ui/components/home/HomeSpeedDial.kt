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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Upload
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
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MyOwnTripTheme

data class HomeQuickAction(
  val label: String,
  val icon: ImageVector,
  val contentDescription: String,
  val onClick: () -> Unit,
  val enabled: Boolean = true,
)

@Composable
fun HomeSpeedDial(
  actions: List<HomeQuickAction>,
  expanded: Boolean,
  onExpandedChange: (Boolean) -> Unit,
  modifier: Modifier = Modifier,
) {
  val reduceMotion = LocalReduceMotion.current
  val animMillis = if (reduceMotion) 0 else AppMotion.DurationMedium

  Box(modifier = modifier) {
    AnimatedVisibility(
      visible = expanded,
      enter = fadeIn(tween(animMillis)),
      exit = fadeOut(tween(animMillis)),
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { onExpandedChange(false) },
          )
          .semantics { contentDescription = "Cerrar acciones rápidas" },
      )
    }

    Column(
      modifier = Modifier.align(Alignment.BottomEnd),
      horizontalAlignment = Alignment.End,
      verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
    ) {
      actions.forEach { action ->
        AnimatedVisibility(
          visible = expanded,
          enter = fadeIn(tween(animMillis)) + expandVertically(tween(animMillis)),
          exit = fadeOut(tween(animMillis)) + shrinkVertically(tween(animMillis)),
        ) {
          HomeSpeedDialItem(
            action = action,
            onSelected = {
              if (!action.enabled) return@HomeSpeedDialItem
              onExpandedChange(false)
              action.onClick()
            },
          )
        }
      }

      FloatingActionButton(
        onClick = { onExpandedChange(!expanded) },
        modifier = Modifier.semantics {
          contentDescription = if (expanded) "Cerrar menú de acciones" else "Abrir acciones rápidas"
        },
      ) {
        androidx.compose.material3.Icon(
          imageVector = if (expanded) Icons.Default.Close else Icons.Default.Add,
          contentDescription = null,
        )
      }
    }
  }
}

@Composable
private fun HomeSpeedDialItem(
  action: HomeQuickAction,
  onSelected: () -> Unit,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
  ) {
    Surface(
      shape = MaterialTheme.shapes.small,
      color = MaterialTheme.colorScheme.surfaceContainerHigh,
      tonalElevation = 2.dp,
    ) {
      Text(
        text = action.label,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(horizontal = MOTSpacing.componentSm, vertical = MOTSpacing.componentXs),
      )
    }
    SmallFloatingActionButton(
      onClick = onSelected,
      containerColor = FloatingActionButtonDefaults.containerColor,
      modifier = Modifier
        .size(48.dp)
        .alpha(if (action.enabled) 1f else 0.38f)
        .semantics { contentDescription = action.contentDescription },
    ) {
      androidx.compose.material3.Icon(action.icon, contentDescription = null)
    }
  }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 200)
@Composable
private fun HomeSpeedDialPreview() {
  var expanded by remember { mutableStateOf(true) }
  MyOwnTripTheme {
    Box(Modifier.fillMaxSize()) {
      HomeSpeedDial(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        actions = listOf(
          HomeQuickAction(
            label = "Nuevo viaje",
            icon = Icons.Default.Luggage,
            contentDescription = "Crear viaje",
            onClick = {},
          ),
          HomeQuickAction(
            label = "Documento",
            icon = Icons.Default.Upload,
            contentDescription = "Añadir documento",
            onClick = {},
          ),
          HomeQuickAction(
            label = "Recuerdo",
            icon = Icons.Default.PhotoCamera,
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
