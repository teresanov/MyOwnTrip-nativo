package com.myowntrip.gallery.catalog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.myowntrip.gallery.specimens.buttonSpecimens
import com.myowntrip.gallery.specimens.iconButtonSpecimens

data class Specimen(
  val id: String,
  val section: String,
  val name: String,
  val canvasAlign: Alignment = Alignment.Center,
  val content: @Composable () -> Unit,
)

val Catalog: List<Specimen> = buttonSpecimens + iconButtonSpecimens
