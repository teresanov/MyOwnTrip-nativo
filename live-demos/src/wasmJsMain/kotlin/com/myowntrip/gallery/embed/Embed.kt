package com.myowntrip.gallery.embed

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalFontFamilyResolver
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
import org.jetbrains.skia.Image as SkiaImage
import androidx.compose.ui.graphics.toComposeImageBitmap
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
  // Imagen de la card de Home vacío: el mismo .jpg que el drawable de la app. No bloquea el montaje.
  loadFont("${base}../assets/home_empty_map.jpg", onLoaded = { bytes ->
    LiveAssets.homeEmptyMap = SkiaImage.makeFromEncoded(bytes).toComposeImageBitmap()
  }, onDone = {})
}

private fun loadFont(url: String, onLoaded: (ByteArray) -> Unit, onDone: () -> Unit) {
  val fail: (JsAny) -> Nothing? = { e ->
    // Sin fuente real: la demo se pinta con la genérica, pero se pinta.
    println("MOT demos: no se pudo cargar la fuente $url: $e")
    onDone()
    null
  }
  // Las dos promesas van anidadas: si se encadenan, Kotlin tipa el segundo paso como Promise
  // mientras el navegador ya entrega el ArrayBuffer, y el cast falla (la fuente nunca se asignaba).
  window.fetch(url).then({ response ->
    response.arrayBuffer().then({ buffer ->
      val arr = Int8Array(buffer)
      onLoaded(ByteArray(arr.length) { i -> arr[i] })
      onDone()
      null
    }, fail)
    null
  }, fail)
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
  // Geometría de las piezas → la ficha (anatomía y medidas), en px CSS relativos al hueco.
  val parts = mutableMapOf<String, Rect>()
  val meta = mutableMapOf<String, Float>()
  var scheduled = false
  fun post() {
    scheduled = false
    val k = meta["cssPerPx"] ?: 1f
    val json = buildString {
      append("{\"meta\":{")
      append(meta.entries.joinToString(",") { "\"${it.key}\":${it.value}" })
      append("},\"parts\":{")
      append(parts.entries.joinToString(",") { (n, r) ->
        "\"$n\":[${r.left * k},${r.top * k},${r.width * k},${r.height * k}]"
      })
      append("}}")
    }
    host.setAttribute("data-mot-parts", json)
    host.dispatchEvent(org.w3c.dom.events.Event("mot:parts"))
  }
  fun schedule() {
    if (scheduled) return
    scheduled = true
    window.setTimeout({ post(); null }, 0)
  }
  val sink = PartSink { name, bounds ->
    if (parts[name] != bounds) {
      parts[name] = bounds
      schedule()
    }
  }
  val onMeta: (Map<String, Float>) -> Unit = { m ->
    if (m.any { meta[it.key] != it.value }) {
      meta.putAll(m)
      schedule()
    }
  }
  ComposeViewport(host) {
    // En web, una fuente cargada desde bytes no se usa hasta precargarla en el resolver;
    // sin esto todo el texto sale en la fuente por defecto (ni Inter ni Fraunces).
    val resolver = LocalFontFamilyResolver.current
    var fontsReady by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
      runCatching {
        resolver.preload(MotSkiaFonts.inter)
        resolver.preload(MotSkiaFonts.fraunces)
      }.onFailure { println("MOT demos: no se pudieron precargar las fuentes: " + it.message) }
      fontsReady = true
    }
    if (fontsReady) LiveSpecimen(id, props, reduceMotion, sink, onMeta)
  }
}
