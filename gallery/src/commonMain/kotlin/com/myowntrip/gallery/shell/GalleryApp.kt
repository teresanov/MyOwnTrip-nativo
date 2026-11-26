package com.myowntrip.gallery.shell

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.MOTIcons
import com.myowntrip.app.ui.theme.MyOwnTripTheme
import com.myowntrip.gallery.catalog.Catalog
import com.myowntrip.gallery.catalog.Specimen
import com.myowntrip.gallery.deeplink.GalleryDeepLink

@Composable
fun GalleryApp() {
  var darkTheme by remember { mutableStateOf(false) }
  var reduceMotion by remember { mutableStateOf(false) }
  var query by remember { mutableStateOf("") }
  var selectedId by remember {
    mutableStateOf(GalleryDeepLink.read()?.takeIf { id -> Catalog.any { it.id == id } } ?: Catalog.first().id)
  }

  LaunchedEffect(selectedId) {
    GalleryDeepLink.write(selectedId)
  }

  val selected = Catalog.firstOrNull { it.id == selectedId } ?: Catalog.first()
  val filtered = remember(query) {
    val q = query.trim()
    if (q.isEmpty()) Catalog
    else Catalog.filter {
      it.section.contains(q, ignoreCase = true) || it.name.contains(q, ignoreCase = true)
    }
  }

  MyOwnTripTheme(darkTheme = darkTheme, reduceMotion = reduceMotion) {
    Row(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
      Sidebar(
        query = query,
        onQueryChange = { query = it },
        items = filtered,
        selectedId = selected.id,
        onSelect = { selectedId = it },
      )
      VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant)
      Column(Modifier.weight(1f).fillMaxHeight()) {
        Toolbar(
          specimen = selected,
          darkTheme = darkTheme,
          onToggleDark = { darkTheme = !darkTheme },
          reduceMotion = reduceMotion,
          onToggleMotion = { reduceMotion = !reduceMotion },
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        CanvasPane(specimen = selected)
      }
    }
  }
}

@Composable
private fun Sidebar(
  query: String,
  onQueryChange: (String) -> Unit,
  items: List<Specimen>,
  selectedId: String,
  onSelect: (String) -> Unit,
) {
  val grouped = remember(items) { items.groupBy { it.section } }
  Column(
    modifier = Modifier
      .width(248.dp)
      .fillMaxHeight()
      .background(MaterialTheme.colorScheme.surfaceContainerLow)
      .padding(horizontal = 12.dp, vertical = 16.dp),
  ) {
    Text(
      "MyOwnTrip",
      style = MaterialTheme.typography.titleSmall,
      color = MaterialTheme.colorScheme.onSurface,
    )
    Text(
      "Gallery",
      style = MaterialTheme.typography.labelSmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Spacer(Modifier.height(16.dp))
    TextField(
      value = query,
      onValueChange = onQueryChange,
      modifier = Modifier.fillMaxWidth(),
      placeholder = { Text("Filter") },
      singleLine = true,
      textStyle = MaterialTheme.typography.bodySmall,
      colors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
      ),
      shape = RoundedCornerShape(8.dp),
    )
    Spacer(Modifier.height(16.dp))
    Column(
      modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      grouped.forEach { (section, specimens) ->
        Text(
          section,
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        )
        specimens.forEach { specimen ->
          val selected = specimen.id == selectedId
          val shape = RoundedCornerShape(8.dp)
          Text(
            specimen.name,
            style = MaterialTheme.typography.bodyMedium,
            color = if (selected) {
              MaterialTheme.colorScheme.onSecondaryContainer
            } else {
              MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier
              .fillMaxWidth()
              .clip(shape)
              .background(
                if (selected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
              )
              .clickable(role = Role.Button) { onSelect(specimen.id) }
              .padding(horizontal = 8.dp, vertical = 8.dp),
          )
        }
      }
    }
  }
}

@Composable
private fun Toolbar(
  specimen: Specimen,
  darkTheme: Boolean,
  onToggleDark: () -> Unit,
  reduceMotion: Boolean,
  onToggleMotion: () -> Unit,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(52.dp)
      .padding(horizontal = 16.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      specimen.section,
      style = MaterialTheme.typography.labelMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Text(
      "  /  ",
      style = MaterialTheme.typography.labelMedium,
      color = MaterialTheme.colorScheme.outline,
    )
    Text(
      specimen.name,
      style = MaterialTheme.typography.titleSmall,
      color = MaterialTheme.colorScheme.onSurface,
    )
    Spacer(Modifier.weight(1f))
    Text(
      if (reduceMotion) "Motion off" else "Motion on",
      style = MaterialTheme.typography.labelSmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier
        .clip(RoundedCornerShape(8.dp))
        .clickable(role = Role.Button, onClick = onToggleMotion)
        .padding(horizontal = 8.dp, vertical = 6.dp),
    )
    IconButton(onClick = onToggleDark) {
      Icon(
        imageVector = if (darkTheme) MOTIcons.LightMode else MOTIcons.DarkMode,
        contentDescription = if (darkTheme) "Light" else "Dark",
        modifier = Modifier.size(20.dp),
      )
    }
  }
}

@Composable
private fun CanvasPane(specimen: Specimen) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(32.dp),
    contentAlignment = specimen.canvasAlign,
  ) {
    specimen.content()
  }
}
