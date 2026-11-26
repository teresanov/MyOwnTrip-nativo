package com.myowntrip.gallery.embed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTButtonSize
import com.myowntrip.app.ui.theme.MOTIcons
import com.myowntrip.app.ui.theme.MOTOutlinedButton
import com.myowntrip.app.ui.theme.MOTTextButton
import com.myowntrip.app.ui.theme.MOTTonalButton
import com.myowntrip.app.ui.theme.MyOwnTripTheme
import com.myowntrip.gallery.catalog.Catalog

/**
 * Demos vivas incrustadas en las fichas del showcase (modelo B).
 * El HTML de la ficha pasa los parámetros como `data-p-*`; aquí solo se pinta el componente real.
 */
@Composable
fun LiveSpecimen(
  id: String,
  props: Map<String, String>,
  reduceMotion: Boolean,
  sink: PartSink = PartSink { _, _ -> },
  onMeta: (Map<String, Float>) -> Unit = {},
) {
  // Zoom de dibujo (anatomía y medidas): se escala la densidad, así el componente se pinta nítido
  // y su layout es el mismo que en la app, solo que más grande.
  val base = LocalDensity.current
  val zoom = props["zoom"]?.toFloatOrNull() ?: 1f
  SideEffect { onMeta(mapOf("cssPerPx" to 1f / base.density)) }
  CompositionLocalProvider(
    LocalDensity provides Density(base.density * zoom, base.fontScale),
    LocalPartSink provides sink,
  ) {
  MyOwnTripTheme(
    darkTheme = props["theme"] == "dark",
    reduceMotion = reduceMotion || props["reduce"] == "true",
  ) {
    Box(
      Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
      contentAlignment = Alignment.Center,
    ) {
      when (id) {
        "button/playground" -> ButtonPlayground(props)
        "button/anatomy" -> ButtonAnatomy(props, onMeta)
        // Cualquier specimen del catálogo (button/sizes, icon-button/round…) se puede incrustar por su id.
        else -> Catalog.firstOrNull { it.id == id }?.content?.invoke() ?: Text("Specimen desconocido: $id")
      }
    }
  }
  }
}

@Composable
private fun ButtonPlayground(p: Map<String, String>) {
  val label = p["label"]?.ifBlank { null } ?: "Guardar gasto"
  val enabled = p["enabled"] != "false"
  val showIcon = p["icon"] == "true"
  val variant = p["variant"] ?: "filled"
  val size = when (variant) {
    "tonal", "text" -> MOTButtonSize.XSmall
    else -> MOTButtonSize.Small
  }
  val content: @Composable RowScope.() -> Unit = {
    if (showIcon) {
      Icon(MOTIcons.Add, contentDescription = null, modifier = Modifier.size(size.leadingIconSize))
      Spacer(Modifier.width(size.leadingIconGap))
    }
    Text(label)
  }
  when (variant) {
    "tonal" -> MOTTonalButton(onClick = {}, enabled = enabled, content = content)
    "outlined" -> MOTOutlinedButton(onClick = {}, enabled = enabled, content = content)
    "text" -> MOTTextButton(onClick = {}, enabled = enabled, content = content)
    else -> MOTButton(onClick = {}, enabled = enabled, content = content)
  }
}

/**
 * Botón para anatomía y medidas: el mismo MOTButton, con sus piezas marcadas.
 * El área táctil mínima se lee del tema (LocalMinimumInteractiveComponentSize) y se informa
 * aparte; dentro se desactiva solo para que la caja medida sea la del contenedor visible.
 */
@Composable
private fun ButtonAnatomy(p: Map<String, String>, onMeta: (Map<String, Float>) -> Unit) {
  val touch: Dp = LocalMinimumInteractiveComponentSize.current
  SideEffect { onMeta(mapOf("touchDp" to touch.value)) }
  val size = MOTButtonSize.Small
  CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
    MOTButton(onClick = {}, modifier = Modifier.part("container"), size = size) {
      Row(Modifier.part("content"), verticalAlignment = Alignment.CenterVertically) {
        Icon(MOTIcons.Add, contentDescription = null, modifier = Modifier.size(size.leadingIconSize).part("icon"))
        Spacer(Modifier.width(size.leadingIconGap))
        Text(p["label"]?.ifBlank { null } ?: "Guardar gasto", modifier = Modifier.part("label"))
      }
    }
  }
}
