package com.myowntrip.app.ui.features.wallet

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.myowntrip.app.domain.model.EntryType
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTTextButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletFormScreen(
  onBack: () -> Unit,
  onSaved: () -> Unit,
  onCreateTrip: () -> Unit = {},
  viewModel: WalletFormViewModel = hiltViewModel(),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  var typeExpanded by remember { mutableStateOf(false) }
  var tripExpanded by remember { mutableStateOf(false) }
  var dirty by remember { mutableStateOf(false) }

  BackHandler(enabled = dirty && !state.showConfirm) { onBack() }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(if (state.isImport) "Review import" else "Add wallet entry") },
        navigationIcon = {
          MOTIconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
    ) {
      if (state.attachmentUri != null) {
        AsyncImage(
          model = state.attachmentUri,
          contentDescription = "Attachment preview",
          modifier = Modifier.fillMaxWidth().height(160.dp),
          contentScale = ContentScale.Crop,
        )
      }

      if (state.trips.isNotEmpty()) {
        ExposedDropdownMenuBox(
          expanded = tripExpanded,
          onExpandedChange = { tripExpanded = it },
          modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        ) {
          OutlinedTextField(
            value = state.trips.find { it.id == state.tripId }?.name ?: "Select trip",
            onValueChange = {},
            readOnly = true,
            label = { Text("Trip") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tripExpanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
          )
          ExposedDropdownMenu(expanded = tripExpanded, onDismissRequest = { tripExpanded = false }) {
            state.trips.forEach { trip ->
              DropdownMenuItem(
                text = { Text(trip.name) },
                onClick = {
                  dirty = true
                  viewModel.onTripSelected(trip.id)
                  tripExpanded = false
                },
              )
            }
          }
        }
      } else {
        Text(
          "No trips yet. Create one to save this document in Wallet.",
          style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
          modifier = Modifier.padding(top = 12.dp),
        )
        MOTButton(
          onClick = onCreateTrip,
          modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        ) {
          Text("Create trip")
        }
      }

      ExposedDropdownMenuBox(
        expanded = typeExpanded,
        onExpandedChange = { typeExpanded = it },
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
      ) {
        OutlinedTextField(
          value = state.type.name,
          onValueChange = {},
          readOnly = true,
          label = { Text("Type") },
          trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
          modifier = Modifier.menuAnchor().fillMaxWidth(),
        )
        ExposedDropdownMenu(expanded = typeExpanded, onDismissRequest = { typeExpanded = false }) {
          EntryType.entries.forEach { type ->
            DropdownMenuItem(
              text = { Text(type.name) },
              onClick = {
                dirty = true
                viewModel.onTypeChange(type)
                typeExpanded = false
              },
            )
          }
        }
      }

      OutlinedTextField(
        value = state.title,
        onValueChange = { dirty = true; viewModel.onTitleChange(it) },
        label = { Text("Title") },
        isError = state.titleError != null,
        supportingText = state.titleError?.let { { Text(it) } },
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
      )

      OutlinedTextField(
        value = state.notes,
        onValueChange = { dirty = true; viewModel.onNotesChange(it) },
        label = { Text("Notes") },
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
      )

      MOTButton(
        onClick = { viewModel.requestConfirm() },
        modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
      ) {
        Text("Review and confirm")
      }
    }
  }

  if (state.showConfirm && state.pendingEntry != null) {
    val entry = state.pendingEntry!!
    AlertDialog(
      onDismissRequest = { viewModel.dismissConfirm() },
      title = { Text("Confirm save") },
      text = {
        Column {
          Text("Type: ${entry.type.name}")
          Text("Title: ${entry.title}")
          entry.notes?.let { Text("Notes: $it") }
          Text("Review all fields before saving. Nothing is stored until you confirm.")
        }
      },
      confirmButton = {
        MOTTextButton(onClick = { viewModel.confirmSave(onSaved) }) { Text("Save") }
      },
      dismissButton = {
        MOTTextButton(onClick = { viewModel.dismissConfirm() }) { Text("Edit") }
      },
    )
  }
}
