package com.myowntrip.app.ui.features.journal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTIcons
import com.myowntrip.app.ui.theme.MOTOutlinedButton
import com.myowntrip.app.ui.theme.MOTSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalRecordingDialog(
  recordingLevels: List<Float>,
  onStop: () -> Unit,
  isReviewing: Boolean = false,
  isPlaying: Boolean = false,
  previewProgress: Float = 0f,
  previewPositionMs: Int = 0,
  previewDurationMs: Int = 0,
  isSaving: Boolean = false,
  onTogglePlayback: () -> Unit = {},
  onConfirm: () -> Unit = {},
  onDiscard: () -> Unit = {},
  confirmLabel: String = "Pegar en la página",
) {
  BasicAlertDialog(onDismissRequest = { if (isReviewing) onDiscard() }) {
    Surface(
      shape = MaterialTheme.shapes.extraLarge,
      tonalElevation = 6.dp,
      modifier = Modifier
        .fillMaxWidth()
        .semantics {
          liveRegion = LiveRegionMode.Polite
          contentDescription = if (isReviewing) {
            "Nota de voz grabada. Puedes escucharla, pegarla o descartarla."
          } else {
            "Grabando nota de voz"
          }
        },
    ) {
      Column(
        modifier = Modifier.padding(MOTSpacing.layoutLg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MOTSpacing.layoutMd),
      ) {
        if (isReviewing) {
          Text(
            text = "¿Te gusta esta nota?",
            style = MaterialTheme.typography.titleMedium,
          )
          JournalAudioPlayerBar(
            isPlaying = isPlaying,
            progress = previewProgress,
            positionLabel = formatAudioTime(previewPositionMs),
            durationLabel = formatAudioTime(previewDurationMs),
            waveformLevels = recordingLevels,
            onTogglePlayback = onTogglePlayback,
          )
          MOTButton(
            onClick = onConfirm,
            enabled = !isSaving,
            modifier = Modifier.fillMaxWidth(),
          ) {
            Text(confirmLabel)
          }
          MOTOutlinedButton(
            onClick = onDiscard,
            enabled = !isSaving,
            modifier = Modifier.fillMaxWidth(),
          ) {
            Text("Descartar")
          }
        } else {
          AudioWaveformBars(
            levels = recordingLevels,
            modifier = Modifier.padding(vertical = MOTSpacing.componentSm),
          )
          Text(
            text = "Grabando audio",
            style = MaterialTheme.typography.titleMedium,
          )
          Text(
            text = "Habla con normalidad. Pulsa Detener cuando hayas terminado.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          MOTButton(
            onClick = onStop,
            modifier = Modifier.fillMaxWidth(),
          ) {
            Icon(MOTIcons.Stop, contentDescription = null)
            Text(
              text = "Detener",
              modifier = Modifier.padding(start = MOTSpacing.componentSm),
            )
          }
        }
      }
    }
  }
}
