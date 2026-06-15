package com.myowntrip.app.ui.features.dayhub

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTTextButton
import java.io.File
import java.text.DateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayHubScreen(
  onBack: () -> Unit,
  onAddNote: () -> Unit,
  onAddExpense: () -> Unit,
  viewModel: DayHubViewModel = hiltViewModel(),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  var tabIndex by remember { mutableIntStateOf(0) }
  val tabs = listOf("Plan", "Journal")

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(state.day?.let { "Day ${it.dayNumber}" } ?: "Day")
        },
        navigationIcon = {
          MOTIconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
      )
    },
    floatingActionButton = {
      when (tabIndex) {
        0 -> FloatingActionButton(
          onClick = { viewModel.showAddBlock() },
          modifier = Modifier.semantics { contentDescription = "Add itinerary block" },
        ) { Icon(Icons.Default.Add, contentDescription = null) }
        1 -> FloatingActionButton(
          onClick = onAddNote,
          modifier = Modifier.semantics { contentDescription = "Add journal note" },
        ) { Icon(Icons.Default.Add, contentDescription = null) }
      }
    },
  ) { padding ->
    Column(modifier = Modifier.padding(padding).fillMaxSize()) {
      TabRow(selectedTabIndex = tabIndex) {
        tabs.forEachIndexed { index, title ->
          Tab(
            selected = tabIndex == index,
            onClick = { tabIndex = index },
            text = { Text(title) },
          )
        }
      }
      when (tabIndex) {
        0 -> PlanTab(
          blocks = state.blocks,
          onMoveUp = viewModel::moveBlockUp,
          onMoveDown = viewModel::moveBlockDown,
        )
        1 -> JournalTab(
          notes = state.notes,
          dayDate = state.day?.date?.toString(),
          onAddExpense = onAddExpense,
        )
      }
    }
  }

  if (state.showAddBlock) {
    AlertDialog(
      onDismissRequest = { viewModel.dismissAddBlock() },
      title = { Text("Add block") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = state.newBlockTitle,
            onValueChange = viewModel::onNewBlockTitleChange,
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
          )
          OutlinedTextField(
            value = state.newBlockTime,
            onValueChange = viewModel::onNewBlockTimeChange,
            label = { Text("Time (optional)") },
            modifier = Modifier.fillMaxWidth(),
          )
        }
      },
      confirmButton = {
        MOTTextButton(onClick = { viewModel.addBlock() }) { Text("Add") }
      },
      dismissButton = {
        MOTTextButton(onClick = { viewModel.dismissAddBlock() }) { Text("Cancel") }
      },
    )
  }
}

@Composable
private fun PlanTab(
  blocks: List<com.myowntrip.app.domain.model.ItineraryBlock>,
  onMoveUp: (Int) -> Unit,
  onMoveDown: (Int) -> Unit,
) {
  if (blocks.isEmpty()) {
    Column(
      modifier = Modifier.fillMaxSize().padding(24.dp),
      verticalArrangement = Arrangement.Center,
    ) {
      Text("No blocks yet. Add activities for this day.")
      Text(
        "Use the arrows to reorder without drag.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 8.dp),
      )
    }
  } else {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      itemsIndexed(blocks, key = { _, block -> block.id }) { index, block ->
        Card(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Column(modifier = Modifier.weight(1f)) {
              block.timeLabel?.let {
                Text(
                  it,
                  style = MaterialTheme.typography.labelMedium,
                  color = MaterialTheme.colorScheme.tertiary,
                )
              }
              Text(block.title, style = MaterialTheme.typography.titleSmall)
            }
            MOTIconButton(
              onClick = { onMoveUp(index) },
              modifier = Modifier.semantics { contentDescription = "Move block up" },
            ) {
              Icon(Icons.Default.KeyboardArrowUp, contentDescription = null)
            }
            MOTIconButton(
              onClick = { onMoveDown(index) },
              modifier = Modifier.semantics { contentDescription = "Move block down" },
            ) {
              Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
            }
          }
        }
      }
    }
  }
}

@Composable
private fun JournalTab(
  notes: List<com.myowntrip.app.domain.model.JournalNote>,
  dayDate: String?,
  onAddExpense: () -> Unit,
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    item {
      dayDate?.let {
        Text(
          it,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
      MOTButton(
        onClick = onAddExpense,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
      ) {
        Text("Add expense for this day")
      }
    }
    if (notes.isEmpty()) {
      item { Text("No notes for this day yet.") }
    } else {
      items(notes.size, key = { notes[it].id }) { index ->
        val note = notes[index]
        Card(modifier = Modifier.fillMaxWidth()) {
          Column(modifier = Modifier.padding(16.dp)) {
            note.photoUri?.let { path ->
              AsyncImage(
                model = File(path),
                contentDescription = "Note photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                  .fillMaxWidth()
                  .height(160.dp)
                  .padding(bottom = 8.dp)
                  .clip(RoundedCornerShape(8.dp)),
              )
            }
            Text(note.text, style = MaterialTheme.typography.bodyLarge)
            Row(
              modifier = Modifier.padding(top = 8.dp),
              horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
              if (note.audioUri != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.Mic, contentDescription = null)
                  Text("Voice", modifier = Modifier.padding(start = 4.dp))
                }
              }
              if (note.latitude != null && note.longitude != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.LocationOn, contentDescription = null)
                  Text(
                    formatCoordinates(note.latitude, note.longitude),
                    modifier = Modifier.padding(start = 4.dp),
                  )
                }
              }
            }
            Text(
              DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(note.createdAt)),
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(top = 4.dp),
            )
          }
        }
      }
    }
  }
}

private fun formatCoordinates(latitude: Double, longitude: Double): String =
  String.format(Locale.getDefault(), "%.4f, %.4f", latitude, longitude)
