package com.myowntrip.app.ui.features.journal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.myowntrip.app.domain.model.JournalNote
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTSpacing
import java.io.File
import java.text.DateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalDetailScreen(
  onBack: () -> Unit,
  onEdit: (String) -> Unit,
  onDeleted: () -> Unit,
  viewModel: JournalDetailViewModel = hiltViewModel(),
) {
  val note by viewModel.note.collectAsStateWithLifecycle()
  val playback by viewModel.playback.collectAsStateWithLifecycle()
  var showDeleteConfirm by remember { mutableStateOf(false) }

  if (showDeleteConfirm) {
    JournalDeleteNoteDialog(
      onDismiss = { showDeleteConfirm = false },
      onConfirmDelete = {
        showDeleteConfirm = false
        viewModel.deleteNote(onDeleted)
      },
    )
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Recuerdo") },
        navigationIcon = {
          MOTIconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
          }
        },
        actions = {
          note?.let { current ->
            MOTIconButton(
              onClick = { onEdit(current.id) },
              modifier = Modifier.semantics { contentDescription = "Editar recuerdo" },
            ) {
              Icon(Icons.Default.Edit, contentDescription = null)
            }
            MOTIconButton(
              onClick = { showDeleteConfirm = true },
              modifier = Modifier.semantics { contentDescription = "Eliminar recuerdo" },
            ) {
              Icon(Icons.Default.Delete, contentDescription = null)
            }
          }
        },
      )
    },
  ) { padding ->
    when (val current = note) {
      null -> {
        Column(
          modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          CircularProgressIndicator()
        }
      }
      else -> JournalDetailContent(
        note = current,
        playback = playback,
        onTogglePlayback = { viewModel.togglePlayback(it) },
        modifier = Modifier.padding(padding),
      )
    }
  }
}

@Composable
private fun JournalDetailContent(
  note: JournalNote,
  playback: JournalPlaybackUiState,
  onTogglePlayback: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(
      horizontal = MOTSpacing.screenHorizontal,
      vertical = MOTSpacing.layoutMd,
    ),
    verticalArrangement = Arrangement.spacedBy(MOTSpacing.layoutMd),
  ) {
    item {
      Text(
        text = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault())
          .format(Date(note.createdAt)),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
    note.photoUri?.let { path ->
      item {
        AsyncImage(
          model = File(path),
          contentDescription = "Foto del recuerdo",
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(12.dp)),
        )
      }
    }
    item {
      Text(
        text = note.text,
        style = MaterialTheme.typography.bodyLarge,
      )
    }
    note.audioUri?.let { path ->
      item {
        Card(modifier = Modifier.fillMaxWidth()) {
          JournalAudioPlayerBar(
            isPlaying = playback.isPlaying,
            progress = playback.progress,
            positionLabel = formatAudioTime(playback.positionMs),
            durationLabel = formatAudioTime(playback.durationMs),
            waveformLevels = playback.waveformLevels,
            onTogglePlayback = { onTogglePlayback(path) },
            modifier = Modifier.padding(MOTSpacing.layoutMd),
          )
        }
      }
    }
    if (note.latitude != null && note.longitude != null) {
      item {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
        ) {
          Icon(
            Icons.Default.LocationOn,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary,
          )
          Text(
            text = formatCoordinates(note.latitude, note.longitude),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }
    }
  }
}

private fun formatCoordinates(latitude: Double, longitude: Double): String =
  String.format(Locale.getDefault(), "%.4f, %.4f", latitude, longitude)
