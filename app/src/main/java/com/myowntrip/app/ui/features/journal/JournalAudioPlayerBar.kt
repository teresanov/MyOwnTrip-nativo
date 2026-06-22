package com.myowntrip.app.ui.features.journal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTSpacing
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun JournalAudioPlayerBar(
  isPlaying: Boolean,
  progress: Float,
  positionLabel: String,
  durationLabel: String,
  waveformLevels: List<Float>,
  onTogglePlayback: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val levels = waveformLevels.ifEmpty {
    buildPlaybackWaveformLevels(
      isPlaying = isPlaying,
      progress = progress,
      phaseMillis = System.currentTimeMillis(),
    )
  }

  Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
  ) {
    AudioWaveformBars(
      levels = levels,
      barColor = if (isPlaying) {
        MaterialTheme.colorScheme.primary
      } else {
        MaterialTheme.colorScheme.onSurfaceVariant
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = MOTSpacing.componentSm),
    )
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
    ) {
      MOTIconButton(
        onClick = onTogglePlayback,
        modifier = Modifier.semantics {
          contentDescription = if (isPlaying) "Pausar audio" else "Reproducir audio"
        },
      ) {
        androidx.compose.material3.Icon(
          imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
          contentDescription = null,
        )
      }
      Text(
        text = "Nota de voz",
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.weight(1f),
      )
      Text(
        text = "$positionLabel / $durationLabel",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
    LinearProgressIndicator(
      progress = { progress },
      modifier = Modifier.fillMaxWidth(),
    )
  }
}

fun formatAudioTime(millis: Int): String {
  val minutes = TimeUnit.MILLISECONDS.toMinutes(millis.toLong())
  val seconds = TimeUnit.MILLISECONDS.toSeconds(millis.toLong()) % 60
  return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
}
