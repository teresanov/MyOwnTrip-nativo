package com.myowntrip.app.ui.components.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.myowntrip.app.domain.model.Trip
import com.myowntrip.app.ui.features.trips.previewHomeTrips
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MyOwnTripTheme

enum class HomeFabAddMode {
  Document,
  Souvenir,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFabAddSheet(
  mode: HomeFabAddMode,
  trips: List<Trip>,
  defaultTripId: String?,
  onDismiss: () -> Unit,
  onCreateTrip: () -> Unit,
  onImportDocument: (tripId: String) -> Unit,
  onManualDocument: (tripId: String) -> Unit,
  onAddSouvenir: (tripId: String) -> Unit,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var tripMenuExpanded by remember { mutableStateOf(false) }
  var selectedTripId by remember(trips, defaultTripId) {
    mutableStateOf(
      defaultTripId?.takeIf { id -> trips.any { it.id == id } }
        ?: trips.firstOrNull()?.id,
    )
  }
  val selectedTrip = trips.find { it.id == selectedTripId }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = MOTSpacing.screenHorizontal)
        .padding(bottom = MOTSpacing.screenContentBottom),
      verticalArrangement = Arrangement.spacedBy(MOTSpacing.layoutMd),
    ) {
      Text(
        text = when (mode) {
          HomeFabAddMode.Document -> "Añadir documento"
          HomeFabAddMode.Souvenir -> "Añadir recuerdo"
        },
        style = MaterialTheme.typography.titleLarge,
      )
      Text(
        text = when (mode) {
          HomeFabAddMode.Document ->
            "Los billetes, reservas y PDFs se guardan en Wallet del cuaderno que elijas."
          HomeFabAddMode.Souvenir ->
            "Las notas y fotos del viaje van al diario del cuaderno que elijas."
        },
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )

      if (trips.isEmpty()) {
        Text(
          text = "Crea un cuaderno de viaje antes de añadir contenido.",
          style = MaterialTheme.typography.bodyMedium,
        )
        MOTButton(
          onClick = {
            onDismiss()
            onCreateTrip()
          },
          modifier = Modifier.fillMaxWidth(),
        ) {
          Text("Crear cuaderno")
        }
        return@Column
      }

      Text(
        text = "Cuaderno",
        style = MaterialTheme.typography.labelLarge,
      )
      ExposedDropdownMenuBox(
        expanded = tripMenuExpanded,
        onExpandedChange = { tripMenuExpanded = it },
        modifier = Modifier
          .fillMaxWidth()
          .semantics { contentDescription = "Seleccionar cuaderno de viaje" },
      ) {
        OutlinedTextField(
          value = selectedTrip?.name ?: "Selecciona un cuaderno",
          onValueChange = {},
          readOnly = true,
          label = { Text("Cuaderno de viaje") },
          supportingText = selectedTrip?.let {
            { Text(it.destination) }
          },
          trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tripMenuExpanded) },
          modifier = Modifier
            .menuAnchor()
            .fillMaxWidth(),
        )
        ExposedDropdownMenu(
          expanded = tripMenuExpanded,
          onDismissRequest = { tripMenuExpanded = false },
        ) {
          trips.forEach { trip ->
            DropdownMenuItem(
              text = {
                Column {
                  Text(trip.name, maxLines = 1)
                  Text(
                    trip.destination,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                  )
                }
              },
              onClick = {
                selectedTripId = trip.id
                tripMenuExpanded = false
              },
            )
          }
        }
      }

      HorizontalDivider()

      when (mode) {
        HomeFabAddMode.Document -> {
          DocumentActionRow(
            title = "Importar archivo",
            subtitle = "PDF o imagen desde el dispositivo",
            icon = { Icon(Icons.Default.Upload, contentDescription = null) },
            contentDescription = "Importar documento a Wallet",
            enabled = selectedTripId != null,
            onClick = {
              val tripId = selectedTripId ?: return@DocumentActionRow
              onDismiss()
              onImportDocument(tripId)
            },
          )
          DocumentActionRow(
            title = "Entrada manual",
            subtitle = "Vuelo, hotel, transporte, actividad u otro",
            icon = { Icon(Icons.AutoMirrored.Filled.NoteAdd, contentDescription = null) },
            contentDescription = "Añadir documento manual a Wallet",
            enabled = selectedTripId != null,
            onClick = {
              val tripId = selectedTripId ?: return@DocumentActionRow
              onDismiss()
              onManualDocument(tripId)
            },
          )
        }
        HomeFabAddMode.Souvenir -> {
          DocumentActionRow(
            title = "Nota o foto",
            subtitle = "Texto, imagen, audio o ubicación en el día del viaje",
            icon = { Icon(Icons.Default.Edit, contentDescription = null) },
            contentDescription = "Añadir recuerdo al diario",
            enabled = selectedTripId != null,
            onClick = {
              val tripId = selectedTripId ?: return@DocumentActionRow
              onDismiss()
              onAddSouvenir(tripId)
            },
          )
        }
      }
    }
  }
}

@Composable
private fun DocumentActionRow(
  title: String,
  subtitle: String,
  icon: @Composable () -> Unit,
  contentDescription: String,
  enabled: Boolean,
  onClick: () -> Unit,
) {
  ListItem(
    headlineContent = { Text(title) },
    supportingContent = { Text(subtitle) },
    leadingContent = icon,
    modifier = Modifier
      .fillMaxWidth()
      .semantics { this.contentDescription = contentDescription }
      .clickable(enabled = enabled, onClick = onClick),
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun HomeFabAddSheetDocumentPreview() {
  MyOwnTripTheme {
    HomeFabAddSheet(
      mode = HomeFabAddMode.Document,
      trips = previewHomeTrips(),
      defaultTripId = "2",
      onDismiss = {},
      onCreateTrip = {},
      onImportDocument = {},
      onManualDocument = {},
      onAddSouvenir = {},
    )
  }
}
