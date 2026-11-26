package com.myowntrip.gallery.embed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.window.ComposeViewport
import com.myowntrip.app.ui.theme.MotSkiaFonts
import kotlinx.browser.document
import kotlinx.browser.window
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.khronos.webgl.get
import org.w3c.dom.Element
import org.w3c.dom.asList

/** Huecos de la página que piden una demo viva: `<div data-mot-specimen="button/playground">`. */
fun liveHosts(): List<Element> = document.querySelectorAll("[data-mot-specimen]").asList().map { it as Element }

/** Carga Inter y Fraunces (los .ttf de designsystem) y después monta cada demo. */
fun mountLiveSpecimens(hosts: List<Element>) {
  val base = hosts.first().getAttribute("data-mot-base") ?: ""
  var pending = 2
  val done = {
    pending -= 1
    if (pending == 0) hosts.forEach(::mount)
  }
  loadFont("${base}font/inter.ttf", onLoaded = { bytes ->
    MotSkiaFonts.inter = FontFamily(
      Font("MOT-Inter-400", bytes, FontWeight.Normal),
      Font("MOT-Inter-500", bytes, FontWeight.Medium),
      Font("MOT-Inter-600", bytes, FontWeight.SemiBold),
    )
  }, onDone = done)
  loadFont("${base}font/fraunces.ttf", onLoaded = { bytes ->
    MotSkiaFonts.fraunces = FontFamily(
      Font("MOT-Fraunces-400", bytes, FontWeight.Normal),
      Font("MOT-Fraunces-600", bytes, FontWeight.SemiBold),
      Font("MOT-Fraunces-700", bytes, FontWeight.Bold),
    )
  }, onDone = done)
}

private fun loadFont(url: String, onLoaded: (ByteArray) -> Unit, onDone: () -> Unit) {
  window.fetch(url)
    .then { response -> response.arrayBuffer() }
    .then { buffer ->
      val arr = Int8Array(buffer as ArrayBuffer)
      onLoaded(ByteArray(arr.length) { i -> arr[i] })
      onDone()
      null
    }
    .catch { _ ->
      // Sin fuente real: la demo se pinta con la genérica del sistema, pero se pinta.
      onDone()
      null
    }
}

private fun readProps(el: Element): Map<String, String> {
  val out = mutableMapOf<String, String>()
  val attrs = el.attributes
  for (i in 0 until attrs.length) {
    val a = attrs.item(i) ?: continue
    if (a.name.startsWith("data-p-")) out[a.name.removePrefix("data-p-")] = a.value
  }
  return out
}

@OptIn(ExperimentalComposeUiApi::class)
private fun mount(host: Element) {
  val id = host.getAttribute("data-mot-specimen") ?: return
  var props by mutableStateOf(readProps(host))
  // La ficha cambia los data-p-* y lanza este evento; Compose recompone con los nuevos valores.
  host.addEventListener("mot:props", { props = readProps(host) })
  val reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches
  host.setAttribute("data-mot-ready", "true")
  ComposeViewport(host) {
    LiveSpecimen(id, props, reduceMotion)
  }
}
