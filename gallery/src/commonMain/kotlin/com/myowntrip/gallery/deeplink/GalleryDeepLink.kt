package com.myowntrip.gallery.deeplink

expect object GalleryDeepLink {
  fun read(): String?
  fun write(id: String)
}
