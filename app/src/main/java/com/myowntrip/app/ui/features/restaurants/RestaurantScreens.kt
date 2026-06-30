package com.myowntrip.app.ui.features.restaurants

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myowntrip.app.domain.model.PlaceCategory
import com.myowntrip.app.domain.model.Restaurant
import com.myowntrip.app.domain.model.RestaurantStatus
import com.myowntrip.app.domain.model.supportsReservation
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTIconButton

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RestaurantFormScreen(
  onBack: () -> Unit,
  onSaved: () -> Unit,
  viewModel: RestaurantFormViewModel = hiltViewModel(),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Nuevo sitio") },
        navigationIcon = {
          MOTIconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
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
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      OutlinedTextField(
        value = state.name,
        onValueChange = viewModel::onNameChange,
        label = { Text("Nombre") },
        isError = state.nameError != null,
        supportingText = state.nameError?.let { { Text(it) } },
        modifier = Modifier.fillMaxWidth(),
      )

      Text("Categoría", style = MaterialTheme.typography.labelLarge)
      FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        PlaceCategory.entries.forEach { category ->
          FilterChip(
            selected = state.category == category,
            onClick = { viewModel.onCategoryChange(category) },
            label = { Text(categoryLabel(category)) },
          )
        }
      }

      OutlinedTextField(
        value = state.address,
        onValueChange = viewModel::onAddressChange,
        label = { Text("Dirección (opcional)") },
        modifier = Modifier.fillMaxWidth(),
      )
      OutlinedTextField(
        value = state.notes,
        onValueChange = viewModel::onNotesChange,
        label = { Text("Notas (opcional)") },
        modifier = Modifier.fillMaxWidth(),
      )
      MOTButton(
        onClick = { viewModel.save(onSaved) },
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text("Guardar")
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen(
  onBack: () -> Unit,
  onDeleted: () -> Unit,
  viewModel: RestaurantDetailViewModel = hiltViewModel(),
) {
  val restaurant by viewModel.restaurant.collectAsStateWithLifecycle()
  val context = LocalContext.current
  var showDeleteDialog by remember { mutableStateOf(false) }

  if (showDeleteDialog) {
    AlertDialog(
      onDismissRequest = { showDeleteDialog = false },
      title = { Text("Borrar sitio") },
      text = { Text("¿Seguro que quieres borrar “${restaurant?.name.orEmpty()}”? No se puede deshacer.") },
      confirmButton = {
        TextButton(onClick = {
          showDeleteDialog = false
          viewModel.delete(onDeleted)
        }) { Text("Borrar") }
      },
      dismissButton = {
        TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") }
      },
    )
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(restaurant?.name ?: "Sitio") },
        navigationIcon = {
          MOTIconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
          }
        },
        actions = {
          MOTIconButton(onClick = { showDeleteDialog = true }) {
            Icon(Icons.Default.Delete, contentDescription = "Borrar sitio")
          }
        },
      )
    },
  ) { padding ->
    Column(
      modifier = Modifier
        .padding(padding)
        .fillMaxSize()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      restaurant?.let { item ->
        Text(
          categoryLabel(item.category),
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.tertiary,
        )
        MOTButton(
          onClick = { openPlaceInMaps(context, item) },
          modifier = Modifier.fillMaxWidth(),
        ) {
          Icon(
            Icons.Default.LocationOn,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
          )
          Text("  Abrir en Google Maps")
        }
        Text("Estado", style = MaterialTheme.typography.labelLarge)
        RowOfStatusChips(
          current = item.status,
          category = item.category,
          onSelect = viewModel::updateStatus,
        )
        item.address?.let {
          Text(it, style = MaterialTheme.typography.bodyMedium)
        }
        item.phone?.let {
          Text(it, style = MaterialTheme.typography.bodyMedium)
        }
        item.website?.let {
          Text(it, style = MaterialTheme.typography.bodyMedium)
        }
        item.notes?.let {
          Text(it, style = MaterialTheme.typography.bodyLarge)
        }
      }
    }
  }
}

@Composable
private fun RowOfStatusChips(
  current: RestaurantStatus,
  category: PlaceCategory,
  onSelect: (RestaurantStatus) -> Unit,
) {
  // "Reservado" solo se ofrece en categorías reservables; si un sitio ya lo tiene,
  // se sigue mostrando para no perder el dato aunque cambie de categoría.
  val statuses = RestaurantStatus.entries.filter { status ->
    status != RestaurantStatus.RESERVED ||
      category.supportsReservation ||
      current == RestaurantStatus.RESERVED
  }
  Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
    statuses.forEach { status ->
      FilterChip(
        selected = current == status,
        onClick = { onSelect(status) },
        label = { Text(statusLabel(status)) },
      )
    }
  }
}

fun statusLabel(status: RestaurantStatus): String = when (status) {
  RestaurantStatus.WITHOUT_RESERVATION -> "Por ir"
  RestaurantStatus.RESERVED -> "Reservado"
  RestaurantStatus.VISITED -> "Visitado"
}

/**
 * Abre el sitio en Google Maps (o la app de mapas disponible). Prioridad: enlace canónico
 * de Maps > coordenadas (`geo:` con etiqueta) > búsqueda por nombre/dirección. Así siempre
 * hay forma de abrirlo, incluso un sitio añadido a mano sin coordenadas.
 */
private fun openPlaceInMaps(context: Context, place: Restaurant) {
  val uri = when {
    !place.googleMapsUri.isNullOrBlank() -> place.googleMapsUri!!.toUri()
    place.latitude != null && place.longitude != null -> {
      val label = android.net.Uri.encode(place.name)
      "geo:${place.latitude},${place.longitude}?q=${place.latitude},${place.longitude}($label)".toUri()
    }
    else -> {
      val query = listOfNotNull(place.name.ifBlank { null }, place.address).joinToString(", ")
      "geo:0,0?q=${android.net.Uri.encode(query)}".toUri()
    }
  }
  try {
    context.startActivity(Intent(Intent.ACTION_VIEW, uri))
  } catch (e: ActivityNotFoundException) {
    Toast.makeText(context, "No hay ninguna app de mapas instalada", Toast.LENGTH_SHORT).show()
  }
}

fun categoryLabel(category: PlaceCategory): String = when (category) {
  PlaceCategory.RESTAURANTE -> "Restaurante"
  PlaceCategory.CAFE -> "Café"
  PlaceCategory.BAR -> "Bar"
  PlaceCategory.MUSEO -> "Museo"
  PlaceCategory.MIRADOR -> "Mirador"
  PlaceCategory.MONUMENTO -> "Monumento"
  PlaceCategory.TIENDA -> "Tienda"
  PlaceCategory.SUPERMERCADO -> "Supermercado"
  PlaceCategory.PARQUE -> "Parque"
  PlaceCategory.ALOJAMIENTO -> "Alojamiento"
  PlaceCategory.OTRO -> "Otro"
}
