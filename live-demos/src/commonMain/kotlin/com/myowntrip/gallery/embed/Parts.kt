package com.myowntrip.gallery.embed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned

/**
 * Geometría real de las piezas de un specimen, para la anatomía y las medidas de la ficha.
 * Cada pieza marcada con [part] informa de su caja (en px del lienzo) cuando se coloca.
 */
fun interface PartSink {
  fun report(name: String, bounds: Rect)
}

val LocalPartSink = staticCompositionLocalOf { PartSink { _, _ -> } }

fun Modifier.part(name: String): Modifier = composed {
  val sink = LocalPartSink.current
  onGloballyPositioned { sink.report(name, it.boundsInRoot()) }
}
