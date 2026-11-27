package com.myowntrip.app.ui.components.date

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerDialogDefaults
import androidx.compose.material3.TimePickerDisplayMode
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MOTTextButton
import com.myowntrip.app.ui.theme.motPart
import kotlinx.datetime.LocalTime

private val MotTimePickerMinWidth = 280.dp

/**
 * Selector de hora (24 h). Se abre con el teclado numérico; el botón de la esquina cambia al reloj.
 *
 * El ancho se ajusta al contenido, con tope en el ancho de pantalla menos los márgenes: el diálogo
 * de Material 3 fija el ancho al de la entrada y corta «Cancelar» y «Aceptar» en español.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotTimePickerDialog(
  initialTime: LocalTime?,
  onDismiss: () -> Unit,
  onConfirm: (LocalTime) -> Unit,
  title: String = "Introduce la hora",
) {
  val seed = initialTime ?: motNow()
  val timeState = rememberTimePickerState(
    initialHour = seed.hour,
    initialMinute = seed.minute,
    is24Hour = true,
  )
  var displayMode by remember { mutableStateOf(TimePickerDisplayMode.Input) }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false),
  ) {
    BoxWithConstraints(contentAlignment = Alignment.Center) {
      MotTimePickerContent(
        state = timeState,
        title = title,
        displayMode = displayMode,
        onDisplayModeChange = { displayMode = it },
        onDismiss = onDismiss,
        onConfirm = { onConfirm(LocalTime(timeState.hour, timeState.minute)) },
        maxWidth = maxWidth - MOTSpacing.screenHorizontal * 2,
      )
    }
  }
}

/** Contenido del selector de hora, sin ventana: para el diálogo y para enseñarlo quieto. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MotTimePickerContent(
  state: TimePickerState,
  title: String,
  displayMode: TimePickerDisplayMode,
  onDisplayModeChange: (TimePickerDisplayMode) -> Unit,
  onDismiss: () -> Unit,
  onConfirm: () -> Unit,
  maxWidth: Dp,
  modifier: Modifier = Modifier,
) {
  val colors = TimePickerDefaults.colors()
  Surface(
    shape = MaterialTheme.shapes.extraLarge,
    color = MaterialTheme.colorScheme.surfaceContainerHigh,
    modifier = modifier
      .widthIn(min = MotTimePickerMinWidth, max = maxWidth)
      .padding(horizontal = MOTSpacing.screenHorizontal)
      .motPart("container"),
  ) {
    Column(
      modifier = Modifier.padding(
        top = MOTSpacing.layoutMd,
        bottom = MOTSpacing.componentSm,
      ),
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
          .padding(horizontal = MOTSpacing.layoutMd)
          .motPart("title"),
      )
      if (displayMode == TimePickerDisplayMode.Picker) {
        TimePicker(
          state = state,
          colors = colors,
          modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(horizontal = MOTSpacing.componentSm),
        )
      } else {
        TimeInput(
          state = state,
          colors = colors,
          modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(horizontal = MOTSpacing.layoutMd, vertical = MOTSpacing.componentSm)
            .motPart("input"),
        )
      }
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = MOTSpacing.componentSm, end = MOTSpacing.layoutMd),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
      ) {
        TimePickerDialogDefaults.DisplayModeToggle(
          displayMode = displayMode,
          onDisplayModeChange = {
            onDisplayModeChange(
              if (displayMode == TimePickerDisplayMode.Picker) {
                TimePickerDisplayMode.Input
              } else {
                TimePickerDisplayMode.Picker
              },
            )
          },
          modifier = Modifier.motPart("modeToggle"),
        )
        Spacer(modifier = Modifier.weight(1f))
        MOTTextButton(onClick = onDismiss) {
          Text("Cancelar", maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        MOTTextButton(onClick = onConfirm, modifier = Modifier.motPart("confirm")) {
          Text("Aceptar", maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
      }
    }
  }
}
