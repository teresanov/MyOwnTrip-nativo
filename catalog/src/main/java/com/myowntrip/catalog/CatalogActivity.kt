package com.myowntrip.catalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.LocalReduceMotion
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MyOwnTripTheme

/**
 * Catálogo vivo del design system de MyOwnTrip: componentes REALES de :designsystem
 * ejecutándose e interactuando — la fuente de verdad es el código, no una spec.
 */
class CatalogActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent { CatalogApp() }
  }
}

enum class CatalogSection(val title: String, val subtitle: String) {
  Marca("Marca", "Wordmark, monograma y cinta"),
  Color("Color", "Roles M3 del tema, claro y oscuro"),
  Tipografia("Tipografía", "Fraunces + Inter sobre la escala M3"),
  Botones("Botones", "MOTButton y variantes, con controles"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogApp() {
  var darkTheme by rememberSaveable { mutableStateOf<Boolean?>(null) }
  val effectiveDark = darkTheme ?: isSystemInDarkTheme()
  var section by rememberSaveable { mutableStateOf<CatalogSection?>(null) }

  BackHandler(enabled = section != null) { section = null }

  CompositionLocalProvider(LocalReduceMotion provides false) {
    MyOwnTripTheme(darkTheme = effectiveDark, dynamicColor = false) {
      Scaffold(
        topBar = {
          TopAppBar(
            title = { Text(section?.title ?: "Catálogo") },
            navigationIcon = {
              if (section != null) {
                MOTIconButton(onClick = { section = null }) {
                  Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                }
              }
            },
            actions = {
              MOTIconButton(onClick = { darkTheme = !effectiveDark }) {
                Icon(
                  if (effectiveDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                  contentDescription = if (effectiveDark) "Modo claro" else "Modo oscuro",
                )
              }
            },
          )
        },
      ) { padding ->
        Column(
          modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
          when (section) {
            null -> CatalogHome(onOpen = { section = it })
            CatalogSection.Marca -> BrandSection()
            CatalogSection.Color -> ColorSection()
            CatalogSection.Tipografia -> TypographySection()
            CatalogSection.Botones -> ButtonsSection()
          }
        }
      }
    }
  }
}

@Composable
private fun CatalogHome(onOpen: (CatalogSection) -> Unit) {
  Text(
    "El código es la fuente de verdad",
    style = MaterialTheme.typography.headlineSmall,
  )
  Text(
    "Cada pieza de este catálogo es el componente real de :designsystem, vivo e interactivo. Si cambia el código, cambia el catálogo.",
    style = MaterialTheme.typography.bodyMedium,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
  )
  CatalogSection.entries.forEach { entry ->
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clickable { onOpen(entry) },
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(entry.title, style = MaterialTheme.typography.titleMedium)
          Text(
            entry.subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }
    }
  }
}
