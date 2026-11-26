package com.myowntrip.gallery

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.myowntrip.gallery.shell.GalleryApp

fun main() = application {
  Window(
    onCloseRequest = ::exitApplication,
    title = "MyOwnTrip Gallery",
  ) {
    GalleryApp()
  }
}
