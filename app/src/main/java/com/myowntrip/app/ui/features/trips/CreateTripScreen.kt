package com.myowntrip.app.ui.features.trips

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTTextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTripScreen(
  onBack: () -> Unit,
  onCreated: (String) -> Unit,
  viewModel: CreateTripViewModel = hiltViewModel(),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  var showStartPicker by remember { mutableStateOf(false) }
  var showEndPicker by remember { mutableStateOf(false) }
  var dirty by remember { mutableStateOf(false) }
  var showDiscard by remember { mutableStateOf(false) }

  BackHandler(enabled = dirty) { showDiscard = true }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("New trip") },
        navigationIcon = {
          MOTIconButton(onClick = {
            if (dirty) showDiscard = true else onBack()
          }) {
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
      OutlinedTextField(
        value = state.name,
        onValueChange = { dirty = true; viewModel.onNameChange(it) },
        label = { Text("Trip name") },
        isError = state.nameError != null,
        supportingText = state.nameError?.let { { Text(it) } },
        modifier = Modifier.fillMaxWidth(),
      )
      OutlinedTextField(
        value = state.destination,
        onValueChange = { dirty = true; viewModel.onDestinationChange(it) },
        label = { Text("Destination") },
        isError = state.destinationError != null,
        supportingText = state.destinationError?.let { { Text(it) } },
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
      )
      OutlinedTextField(
        value = state.startDate.toString(),
        onValueChange = {},
        readOnly = true,
        label = { Text("Start date") },
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 12.dp),
        trailingIcon = {
          MOTTextButton(onClick = { showStartPicker = true }) { Text("Pick") }
        },
      )
      OutlinedTextField(
        value = state.endDate.toString(),
        onValueChange = {},
        readOnly = true,
        label = { Text("End date") },
        isError = state.dateError != null,
        supportingText = state.dateError?.let { { Text(it) } },
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 12.dp),
        trailingIcon = {
          MOTTextButton(onClick = { showEndPicker = true }) { Text("Pick") }
        },
      )
      MOTButton(
        onClick = { viewModel.save(onCreated) },
        modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
      ) {
        Text("Save trip")
      }
    }
  }

  if (showStartPicker) {
    val pickerState = rememberDatePickerState()
    DatePickerDialog(
      onDismissRequest = { showStartPicker = false },
      confirmButton = {
        MOTTextButton(onClick = {
          pickerState.selectedDateMillis?.let { millis ->
            dirty = true
            viewModel.onStartDateChange(millis.toLocalDate())
          }
          showStartPicker = false
        }) { Text("OK") }
      },
      dismissButton = {
        MOTTextButton(onClick = { showStartPicker = false }) { Text("Cancel") }
      },
    ) { DatePicker(state = pickerState) }
  }

  if (showEndPicker) {
    val pickerState = rememberDatePickerState()
    DatePickerDialog(
      onDismissRequest = { showEndPicker = false },
      confirmButton = {
        MOTTextButton(onClick = {
          pickerState.selectedDateMillis?.let { millis ->
            dirty = true
            viewModel.onEndDateChange(millis.toLocalDate())
          }
          showEndPicker = false
        }) { Text("OK") }
      },
      dismissButton = {
        MOTTextButton(onClick = { showEndPicker = false }) { Text("Cancel") }
      },
    ) { DatePicker(state = pickerState) }
  }

  if (showDiscard) {
    androidx.compose.material3.AlertDialog(
      onDismissRequest = { showDiscard = false },
      title = { Text("Discard changes?") },
      confirmButton = {
        MOTTextButton(onClick = { showDiscard = false; onBack() }) { Text("Discard") }
      },
      dismissButton = {
        MOTTextButton(onClick = { showDiscard = false }) { Text("Keep editing") }
      },
    )
  }
}

private fun Long.toLocalDate(): LocalDate =
  Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
