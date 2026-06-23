package com.myowntrip.app.ui.features.trips

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myowntrip.app.ui.components.date.MotDateRangeField
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MOTTextButton
import com.myowntrip.app.ui.theme.MyOwnTripTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTripScreen(
  onBack: () -> Unit,
  onSaved: () -> Unit,
  onDeleted: () -> Unit,
  viewModel: EditTripViewModel = hiltViewModel(),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  var dirty by remember { mutableStateOf(false) }
  var showDiscard by remember { mutableStateOf(false) }
  var showDeleteConfirm by remember { mutableStateOf(false) }

  BackHandler(enabled = dirty) { showDiscard = true }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Editar viaje") },
        navigationIcon = {
          MOTIconButton(onClick = {
            if (dirty) showDiscard = true else onBack()
          }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
          }
        },
      )
    },
  ) { padding ->
    if (state.isLoading) {
      Column(
        modifier = Modifier
          .padding(padding)
          .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
      ) {
        CircularProgressIndicator()
      }
      return@Scaffold
    }

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
      OutlinedTextField(
        value = state.name,
        onValueChange = { dirty = true; viewModel.onNameChange(it) },
        label = { Text("Nombre del viaje") },
        isError = state.nameError != null,
        supportingText = state.nameError?.let { { Text(it) } },
        modifier = Modifier.fillMaxWidth(),
        enabled = !state.isSaving && !state.isDeleting,
      )
      OutlinedTextField(
        value = state.destination,
        onValueChange = { dirty = true; viewModel.onDestinationChange(it) },
        label = { Text("Destino") },
        isError = state.destinationError != null,
        supportingText = state.destinationError?.let { { Text(it) } },
        modifier = Modifier.fillMaxWidth(),
        enabled = !state.isSaving && !state.isDeleting,
      )
      MotDateRangeField(
        startDate = state.startDate,
        endDate = state.endDate,
        onRangeChange = { start, end ->
          dirty = true
          viewModel.onDateRangeChange(start, end)
        },
        label = "Fechas del viaje",
        error = state.dateError,
        enabled = !state.isSaving && !state.isDeleting,
        modifier = Modifier.fillMaxWidth(),
      )
      if (state.willShrinkDays) {
        Text(
          text = "Al acortar las fechas se eliminarán días del cuaderno y el contenido asociado a esos días.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }

      MOTButton(
        onClick = { viewModel.save(onSaved) },
        modifier = Modifier.fillMaxWidth(),
        enabled = !state.isSaving && !state.isDeleting,
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
          Text("Guardar cambios")
        }
      }

      HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

      Text(
        text = "Zona de peligro",
        style = MaterialTheme.typography.titleMedium,
      )
      Text(
        text = "Eliminar el viaje borra también Wallet, plan, diario y gastos asociados.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
      MOTTextButton(
        onClick = { showDeleteConfirm = true },
        modifier = Modifier.fillMaxWidth(),
        enabled = !state.isSaving && !state.isDeleting,
      ) {
        if (state.isDeleting) {
          CircularProgressIndicator(
            modifier = Modifier.size(18.dp),
            strokeWidth = 2.dp,
          )
          Spacer(Modifier.width(MOTSpacing.componentSm))
        }
        Text("Eliminar viaje", color = MaterialTheme.colorScheme.error)
      }
    }
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
      text = { Text("Los cambios no guardados se perderán.") },
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

  if (showDeleteConfirm) {
    androidx.compose.material3.AlertDialog(
      onDismissRequest = { showDeleteConfirm = false },
      icon = {
        Icon(
          Icons.Default.Error,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.error,
        )
      },
      title = { Text("¿Eliminar viaje?") },
      text = {
        Text("Se borrarán todos los datos de «${state.name}». Esta acción no se puede deshacer.")
      },
      confirmButton = {
        MOTTextButton(
          onClick = {
            showDeleteConfirm = false
            viewModel.delete(onDeleted)
          },
        ) {
          Text("Eliminar", color = MaterialTheme.colorScheme.error)
        }
      },
      dismissButton = {
        MOTTextButton(onClick = { showDeleteConfirm = false }) { Text("Cancelar") }
      },
    )
  }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
private fun EditTripFormPreview() {
  MyOwnTripTheme {
    Column(
      modifier = Modifier.padding(MOTSpacing.screenHorizontal),
      verticalArrangement = Arrangement.spacedBy(MOTSpacing.layoutMd),
    ) {
      MotDateRangeField(
        startDate = LocalDate.of(2026, 7, 4),
        endDate = LocalDate.of(2026, 7, 6),
        onRangeChange = { _, _ -> },
        label = "Fechas del viaje",
      )
    }
  }
}
