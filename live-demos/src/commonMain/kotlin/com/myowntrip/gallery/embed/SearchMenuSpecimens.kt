package com.myowntrip.gallery.embed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.components.MotMenuDropdown
import com.myowntrip.app.ui.components.MotMenuGroup
import com.myowntrip.app.ui.components.MotMenuItem
import com.myowntrip.app.ui.components.MotMenuPanel
import com.myowntrip.app.ui.components.MotMenuSectionLabel
import com.myowntrip.app.ui.components.MotSearchBar
import com.myowntrip.app.ui.theme.LocalMotPartReporter

/* Búsqueda y menú de Home, con los textos de la app (HomeSearchAndFilter / HomeTripsContent). */

private val ShowOptions = listOf("Todos los viajes", "En curso", "Próximos", "Pasados", "Archivados")
private val SortOptions = listOf("Fecha — próximo primero", "Nombre A—Z", "Destino A—Z")

/** Los dos grupos del menú de Home: «Mostrar» y «Ordenar». */
@Composable
private fun HomeMenuGroups(show: Int, sort: Int, onShow: (Int) -> Unit, onSort: (Int) -> Unit) {
  MotMenuGroup {
    MotMenuSectionLabel("Mostrar")
    ShowOptions.forEachIndexed { i, label -> MotMenuItem(label = label, selected = i == show, onClick = { onShow(i) }) }
  }
  MotMenuGroup {
    MotMenuSectionLabel("Ordenar")
    SortOptions.forEachIndexed { i, label -> MotMenuItem(label = label, selected = i == sort, onClick = { onSort(i) }) }
  }
}

/** Barra de Home: se puede escribir y el botón de filtros abre el menú real. query: texto inicial. */
@Composable
fun SearchBarPlayground(p: Map<String, String>) {
  var query by remember(p["query"]) { mutableStateOf(p["query"] ?: "") }
  var open by remember { mutableStateOf(false) }
  var show by remember { mutableStateOf(0) }
  var sort by remember { mutableStateOf(0) }
  Box(Modifier.width(PhoneWidth)) {
    MotSearchBar(
      query = query,
      onQueryChange = { query = it },
      placeholder = "Buscar viajes",
      onFilterClick = { open = true },
      menu = {
        MotMenuDropdown(
          expanded = open,
          onDismissRequest = { open = false },
          modifier = Modifier.align(Alignment.TopEnd),
        ) {
          HomeMenuGroups(show, sort, onShow = { show = it; open = false }, onSort = { sort = it; open = false })
        }
      },
    )
  }
}

/** Anatomía: la barra vacía (todas sus piezas) y, debajo, con texto (solo informa del botón de borrar). */
@Composable
fun SearchBarAnatomy() {
  val report = LocalMotPartReporter.current
  val onlyClear: ((String, LayoutCoordinates) -> Unit)? = report?.let { r ->
    { name, coords -> if (name == "clear") r(name, coords) }
  }
  Column(Modifier.width(PhoneWidth), verticalArrangement = Arrangement.spacedBy(16.dp)) {
    MotSearchBar(query = "", onQueryChange = {}, placeholder = "Buscar viajes", onFilterClick = {})
    CompositionLocalProvider(LocalMotPartReporter provides onlyClear) {
      MotSearchBar(query = "Cracovia", onQueryChange = {}, placeholder = "Buscar viajes", onFilterClick = {})
    }
  }
}

/** Menú de Home quieto, sin ventana propia. show / sort: opción seleccionada (índice). Se puede tocar. */
@Composable
fun MenuStatic(p: Map<String, String>) {
  var show by remember(p["show"]) { mutableStateOf(p["show"]?.toIntOrNull() ?: 0) }
  var sort by remember(p["sort"]) { mutableStateOf(p["sort"]?.toIntOrNull() ?: 0) }
  MotMenuPanel {
    HomeMenuGroups(show, sort, onShow = { show = it }, onSort = { sort = it })
  }
}
