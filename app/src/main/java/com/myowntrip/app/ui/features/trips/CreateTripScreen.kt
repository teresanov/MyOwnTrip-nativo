package com.myowntrip.app.ui.features.trips

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myowntrip.app.ui.components.DestinationCoverPreview
import com.myowntrip.app.ui.components.date.MotTripDatePickerDialog
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MOTTextButton
import com.myowntrip.app.ui.theme.MyOwnTripTheme
import com.myowntrip.app.ui.theme.rememberMOTButtonShape
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val SpanishDateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale("es", "ES"))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTripScreen(
  onBack: () -> Unit,
  onOpenTrip: (String) -> Unit,
  onImportDocument: (String) -> Unit,
  onAddDocumentManual: (String) -> Unit,
  viewModel: CreateTripViewModel = hiltViewModel(),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  var showStartPicker by remember { mutableStateOf(false) }
  var showEndPicker by remember { mutableStateOf(false) }
  var dirty by remember { mutableStateOf(false) }
  var showDiscard by remember { mutableStateOf(false) }
  val tripSaved = state.savedTripId != null

  BackHandler(enabled = dirty && !tripSaved) { showDiscard = true }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Nuevo viaje") },
        navigationIcon = {
          MOTIconButton(onClick = {
            if (dirty && !tripSaved) showDiscard = true else onBack()
          }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
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
        .padding(
          start = MOTSpacing.screenHorizontal,
          end = MOTSpacing.screenHorizontal,
          top = MOTSpacing.screenHorizontal,
          bottom = MOTSpacing.screenContentBottom,
        ),
      verticalArrangement = Arrangement.spacedBy(MOTSpacing.layoutMd),
    ) {
      if (tripSaved) {
        CreateTripSavedBanner()
      }

      OutlinedTextField(
        value = state.name,
        onValueChange = { dirty = true; viewModel.onNameChange(it) },
        label = { Text("Nombre del viaje") },
        isError = state.nameError != null,
        supportingText = state.nameError?.let { { Text(it) } },
        modifier = Modifier.fillMaxWidth(),
        readOnly = tripSaved,
      )
      OutlinedTextField(
        value = state.destination,
        onValueChange = { dirty = true; viewModel.onDestinationChange(it) },
        label = { Text("Destino") },
        isError = state.destinationError != null,
        supportingText = state.destinationError?.let { { Text(it) } },
        modifier = Modifier.fillMaxWidth(),
        readOnly = tripSaved,
      )
      if (!tripSaved && state.destination.isNotBlank()) {
        DestinationCoverPreview(
          imageModel = state.coverPreviewModel,
          destination = state.destination,
          contentDescription = "Vista previa de ${state.destination}",
          modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(MaterialTheme.shapes.medium),
        )
      }
      OutlinedTextField(
        value = state.startDate.format(SpanishDateFormatter),
        onValueChange = {},
        readOnly = true,
        label = { Text("Fecha de inicio") },
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
          if (!tripSaved) {
            MOTTextButton(onClick = { showStartPicker = true }) { Text("Elegir") }
          }
        },
      )
      OutlinedTextField(
        value = state.endDate.format(SpanishDateFormatter),
        onValueChange = {},
        readOnly = true,
        label = { Text("Fecha de fin") },
        isError = state.dateError != null,
        supportingText = state.dateError?.let { { Text(it) } },
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
          if (!tripSaved) {
            MOTTextButton(onClick = { showEndPicker = true }) { Text("Elegir") }
          }
        },
      )

      HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

      CreateTripDocumentsSection(
        onImport = { viewModel.ensureTripSaved(onImportDocument) },
        onManual = { viewModel.ensureTripSaved(onAddDocumentManual) },
        enabled = !state.isSaving,
      )

      if (tripSaved) {
        MOTButton(
          onClick = { onOpenTrip(state.savedTripId!!) },
          modifier = Modifier.fillMaxWidth(),
          enabled = !state.isSaving,
        ) {
          Text("Ver viaje")
        }
      } else {
        MOTButton(
          onClick = { viewModel.save(onOpenTrip) },
          modifier = Modifier.fillMaxWidth(),
          enabled = !state.isSaving,
        ) {
          if (state.isSaving) {
            CircularProgressIndicator(
              modifier = Modifier.size(18.dp),
              strokeWidth = 2.dp,
              color = MaterialTheme.colorScheme.onPrimary,
            )
            Spacer(Modifier.width(MOTSpacing.componentSm))
            Text("Guardando…")
          } else {
            Text("Guardar viaje")
          }
        }
        MOTTextButton(
          onClick = {
            if (dirty) showDiscard = true else onBack()
          },
          modifier = Modifier.fillMaxWidth(),
          enabled = !state.isSaving,
        ) {
          Text("Cancelar creación")
        }
      }
    }
  }

  if (showStartPicker) {
    MotTripDatePickerDialog(
      title = "Fecha de inicio",
      initialDate = state.startDate,
      onDismiss = { showStartPicker = false },
      onConfirm = { date ->
        dirty = true
        viewModel.onStartDateChange(date)
        if (state.endDate.isBefore(date)) {
          viewModel.onEndDateChange(date)
        }
        showStartPicker = false
      },
    )
  }

  if (showEndPicker) {
    MotTripDatePickerDialog(
      title = "Fecha de fin",
      initialDate = state.endDate,
      minDate = state.startDate,
      onDismiss = { showEndPicker = false },
      onConfirm = { date ->
        dirty = true
        viewModel.onEndDateChange(date)
        showEndPicker = false
      },
    )
  }

  if (showDiscard) {
    androidx.compose.material3.AlertDialog(
      onDismissRequest = { showDiscard = false },
      icon = {
        Icon(
          Icons.Default.Error,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.error,
        )
      },
      title = { Text("¿Descartar cambios?") },
      text = {
        Text("Los datos del viaje no guardados se perderán. Nada se creará hasta que pulses Guardar viaje.")
      },
      confirmButton = {
        MOTTextButton(onClick = { showDiscard = false; onBack() }) {
          Text("Descartar", color = MaterialTheme.colorScheme.error)
        }
      },
      dismissButton = {
        MOTTextButton(onClick = { showDiscard = false }) { Text("Seguir editando") }
      },
    )
  }
}

@Composable
private fun CreateTripSavedBanner() {
  Card(
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
  ) {
    Row(
      modifier = Modifier.padding(MOTSpacing.layoutMd),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
    ) {
      Icon(
        Icons.Default.CheckCircle,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onTertiaryContainer,
      )
      Text(
        text = "Viaje guardado. Añade billetes, hoteles o PDFs antes de continuar.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onTertiaryContainer,
      )
    }
  }
}

@Composable
private fun CreateTripDocumentsSection(
  onImport: () -> Unit,
  onManual: () -> Unit,
  enabled: Boolean = true,
) {
  Column(verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm)) {
    Text(
      text = "Documentos del viaje",
      style = MaterialTheme.typography.titleMedium,
    )
    Text(
      text = "Importa el billete o la reserva ahora. Analizamos el archivo y, si no se puede leer, te guiamos a rellenarlo a mano.",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(MOTSpacing.gutterGrid),
    ) {
      OutlinedButton(
        onClick = onImport,
        modifier = Modifier.weight(1f),
        shape = rememberMOTButtonShape(),
        enabled = enabled,
      ) {
        Icon(Icons.Default.Upload, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(MOTSpacing.componentSm))
        Text("Importar")
      }
      MOTButton(onClick = onManual, modifier = Modifier.weight(1f), enabled = enabled) {
        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(MOTSpacing.componentSm))
        Text("Añadir")
      }
    }
  }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun CreateTripScreenPreview() {
  MyOwnTripTheme {
    CreateTripDocumentsSection(onImport = {}, onManual = {})
  }
}
