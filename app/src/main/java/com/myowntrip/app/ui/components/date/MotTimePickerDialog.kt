package com.myowntrip.app.ui.components.date

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerDialogDefaults
import androidx.compose.material3.TimePickerDisplayMode
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MOTTextButton
import java.time.LocalTime

private val MotTimePickerShape = RoundedCornerShape(28.dp)
private val MotTimePickerMinWidth = 280.dp

/**
 * Keyboard picker · 24 h (Figma `52949:28069`).
 * Entrada por teclado por defecto; reloj opcional vía toggle M3.
 *
 * Ancho hug-content con tope = pantalla − padding horizontal (M3 [TimePickerDialog]
 * fija el ancho al [TimeInput] y rompe «Cancelar» + «Aceptar» en español).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotTimePickerDialog(
  initialTime: LocalTime?,
  onDismiss: () -> Unit,
  onConfirm: (LocalTime) -> Unit,
  title: String = "Introduce la hora",
) {
  val seed = initialTime ?: LocalTime.now()
  val timeState = rememberTimePickerState(
    initialHour = seed.hour,
    initialMinute = seed.minute,
    is24Hour = true,
  )
  var displayMode by remember { mutableStateOf(TimePickerDisplayMode.Input) }
  val colors = TimePickerDefaults.colors()
  val maxDialogWidth = LocalConfiguration.current.screenWidthDp.dp -
    MOTSpacing.screenHorizontal * 2

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false),
  ) {
    Surface(
      shape = MotTimePickerShape,
      color = MaterialTheme.colorScheme.surfaceContainerHigh,
      modifier = Modifier
        .widthIn(min = MotTimePickerMinWidth, max = maxDialogWidth)
        .padding(horizontal = MOTSpacing.screenHorizontal),
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
          modifier = Modifier.padding(horizontal = MOTSpacing.layoutMd),
        )
        when (displayMode) {
          TimePickerDisplayMode.Input -> {
            TimeInput(
              state = timeState,
              colors = colors,
              modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = MOTSpacing.layoutMd, vertical = MOTSpacing.componentSm),
            )
          }
          TimePickerDisplayMode.Picker -> {
            TimePicker(
              state = timeState,
              colors = colors,
              modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = MOTSpacing.componentSm),
            )
          }
          else -> {
            TimeInput(
              state = timeState,
              colors = colors,
              modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = MOTSpacing.layoutMd, vertical = MOTSpacing.componentSm),
            )
          }
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
              displayMode = when (displayMode) {
                TimePickerDisplayMode.Input -> TimePickerDisplayMode.Picker
                TimePickerDisplayMode.Picker -> TimePickerDisplayMode.Input
                else -> TimePickerDisplayMode.Input
              }
            },
          )
          Spacer(modifier = Modifier.weight(1f))
          MOTTextButton(onClick = onDismiss) {
            Text("Cancelar", maxLines = 1, overflow = TextOverflow.Ellipsis)
          }
          MOTTextButton(
            onClick = { onConfirm(LocalTime.of(timeState.hour, timeState.minute)) },
          ) {
            Text("Aceptar", maxLines = 1, overflow = TextOverflow.Ellipsis)
          }
        }
      }
    }
  }
}
