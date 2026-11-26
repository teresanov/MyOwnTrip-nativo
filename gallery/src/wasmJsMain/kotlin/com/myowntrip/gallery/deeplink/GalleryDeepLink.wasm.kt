package com.myowntrip.gallery.deeplink

import kotlinx.browser.window

actual object GalleryDeepLink {
  actual fun read(): String? {
    val raw = window.location.hash.removePrefix("#").removePrefix("/")
    return raw.ifBlank { null }
  }

  actual fun write(id: String) {
    window.history.replaceState(null, "", "#/$id")
  }
}
