package com.myowntrip.app.ui.features.journal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.myowntrip.app.domain.model.JournalNote
import java.io.File
import java.text.DateFormat
import java.util.Date
import java.util.Locale

@Composable
fun JournalNoteCard(
  note: JournalNote,
  onClick: (() -> Unit)? = null,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .then(
        if (onClick != null) {
          Modifier
            .clickable(onClick = onClick)
            .semantics { contentDescription = "Ver recuerdo" }
        } else {
          Modifier
        },
      ),
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      note.photoUri?.let { path ->
        AsyncImage(
          model = File(path),
          contentDescription = "Foto del recuerdo",
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
            Text("Audio", modifier = Modifier.padding(start = 4.dp))
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

private fun formatCoordinates(latitude: Double, longitude: Double): String =
  String.format(Locale.getDefault(), "%.4f, %.4f", latitude, longitude)
