package com.myowntrip.app.ui.features.journal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.DateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayDetailScreen(
  onBack: () -> Unit,
  viewModel: JournalViewModel = hiltViewModel(),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  var showAdd by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(state.day?.let { "Day ${it.dayNumber}" } ?: "Day") },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
      )
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { showAdd = true },
        modifier = Modifier.semantics { contentDescription = "Add journal note" },
      ) {
        Icon(Icons.Default.Add, contentDescription = null)
      }
    },
  ) { padding ->
    LazyColumn(
      modifier = Modifier.padding(padding).fillMaxSize(),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      item {
        Text(
          state.day?.date?.toString() ?: "",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
      if (state.notes.isEmpty()) {
        item { Text("No notes for this day yet.") }
      } else {
        items(state.notes, key = { it.id }) { note ->
          Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(note.text, style = MaterialTheme.typography.bodyLarge)
              Text(
                DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(note.createdAt)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          }
        }
      }
    }
  }

  if (showAdd) {
    androidx.compose.material3.AlertDialog(
      onDismissRequest = { showAdd = false },
      title = { Text("New note") },
      text = {
        OutlinedTextField(
          value = state.text,
          onValueChange = viewModel::onTextChange,
          label = { Text("Note") },
          isError = state.textError != null,
          supportingText = state.textError?.let { { Text(it) } },
          modifier = Modifier.fillMaxWidth(),
        )
      },
      confirmButton = {
        androidx.compose.material3.TextButton(
          onClick = {
            viewModel.saveNote {
              showAdd = false
            }
          },
        ) { Text("Save") }
      },
      dismissButton = {
        androidx.compose.material3.TextButton(onClick = { showAdd = false }) { Text("Cancel") }
      },
    )
  }
}
