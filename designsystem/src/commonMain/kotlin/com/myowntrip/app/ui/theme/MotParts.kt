package com.myowntrip.app.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned

/**
 * Piezas con nombre de un componente (contenedor, etiqueta, imagen…), para la documentación:
 * la anatomía y las medidas del showcase se dibujan sobre estas piezas del componente real.
 *
 * En la app nadie escucha ([LocalMotPartReporter] es null) y el modificador no añade nada.
 * Solo las demos del showcase (live-demos) proveen un receptor.
 */
val LocalMotPartReporter = staticCompositionLocalOf<((name: String, coordinates: LayoutCoordinates) -> Unit)?> { null }

fun Modifier.motPart(name: String): Modifier = this.then(
  Modifier.composed {
    val report = LocalMotPartReporter.current
    if (report == null) Modifier else Modifier.onGloballyPositioned { report(name, it) }
  },
)
