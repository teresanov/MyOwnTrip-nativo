package com.myowntrip.app.ui.components.date

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.myowntrip.app.domain.plan.PlanPlacementLogic
import com.myowntrip.app.ui.theme.MOTIconButton
import java.time.LocalDate
import java.time.LocalTime

/**
 * Campo de formulario para rango de fechas. Abre [MotDateRangePickerDialog] al tocar.
 * Figma: Input date picker · Range (`51954:18554`).
 */
@Composable
fun MotDateRangeField(
  startDate: LocalDate,
  endDate: LocalDate,
  onRangeChange: (LocalDate, LocalDate) -> Unit,
  modifier: Modifier = Modifier,
  label: String = "Fechas del viaje",
  error: String? = null,
  enabled: Boolean = true,
  minDate: LocalDate? = defaultMotMinSelectableDate(),
  maxDate: LocalDate? = defaultMotMaxSelectableDate(),
) {
  var showPicker by remember { mutableStateOf(false) }
  val displayValue = formatMotDateRange(startDate, endDate)

  OutlinedTextField(
    value = displayValue,
    onValueChange = {},
    readOnly = true,
    enabled = enabled,
    label = { Text(label) },
    isError = error != null,
    supportingText = error?.let { { Text(it) } },
    modifier = modifier
      .semantics { contentDescription = "$label, $displayValue" }
      .clickable(enabled = enabled) { showPicker = true },
    trailingIcon = {
      if (enabled) {
        MOTIconButton(onClick = { showPicker = true }) {
          Icon(
            Icons.Outlined.CalendarMonth,
            contentDescription = "Abrir calendario de fechas",
          )
        }
      }
    },
  )

  if (showPicker) {
    MotDateRangePickerDialog(
      initialStartDate = startDate,
      initialEndDate = endDate,
      minDate = minDate,
      maxDate = maxDate,
      onDismiss = { showPicker = false },
      onConfirm = { start, end ->
        onRangeChange(start, end)
        showPicker = false
      },
    )
  }
}

/**
 * Campo de formulario para una fecha. Abre [MotDatePickerDialog] al tocar.
 */
@Composable
fun MotDateField(
  date: LocalDate?,
  onDateChange: (LocalDate?) -> Unit,
  modifier: Modifier = Modifier,
  label: String = "Fecha",
  placeholder: String = "Sin fecha",
  enabled: Boolean = true,
  minDate: LocalDate? = null,
  maxDate: LocalDate? = null,
) {
  var showPicker by remember { mutableStateOf(false) }
  val displayValue = date?.let(::formatMotDate).orEmpty()

  OutlinedTextField(
    value = displayValue,
    onValueChange = {},
    readOnly = true,
    enabled = enabled,
    label = { Text(label) },
    placeholder = { Text(placeholder) },
    modifier = modifier
      .semantics {
        contentDescription = if (date != null) "$label, $displayValue" else label
      }
      .clickable(enabled = enabled) { showPicker = true },
    trailingIcon = {
      if (enabled) {
        MOTIconButton(onClick = { showPicker = true }) {
          Icon(
            Icons.Outlined.CalendarMonth,
            contentDescription = "Abrir calendario",
          )
        }
      }
    },
  )

  if (showPicker) {
    MotDatePickerDialog(
      initialDate = date ?: LocalDate.now(),
      minDate = minDate,
      maxDate = maxDate,
      onDismiss = { showPicker = false },
      onConfirm = { selected ->
        onDateChange(selected)
        showPicker = false
      },
    )
  }
}

/**
 * Campo de formulario para hora (24 h). Abre [MotTimePickerDialog] al tocar.
 * Figma: Keyboard picker (`52949:28069`).
 */
@Composable
fun MotTimeField(
  time: LocalTime?,
  onTimeChange: (LocalTime?) -> Unit,
  modifier: Modifier = Modifier,
  label: String = "Hora (opcional)",
  placeholder: String = "Ej. 20:00",
  enabled: Boolean = true,
) {
  var showPicker by remember { mutableStateOf(false) }
  val displayValue = time?.let(::formatMotTime).orEmpty()

  OutlinedTextField(
    value = displayValue,
    onValueChange = {},
    readOnly = true,
    enabled = enabled,
    label = { Text(label) },
    placeholder = { Text(placeholder) },
    modifier = modifier
      .semantics {
        contentDescription = if (time != null) "$label, $displayValue" else label
      }
      .clickable(enabled = enabled) { showPicker = true },
    trailingIcon = {
      if (enabled) {
        MOTIconButton(onClick = { showPicker = true }) {
          Icon(
            Icons.Outlined.Schedule,
            contentDescription = "Abrir selector de hora",
          )
        }
      }
    },
  )

  if (showPicker) {
    MotTimePickerDialog(
      initialTime = time,
      onDismiss = { showPicker = false },
      onConfirm = { selected ->
        onTimeChange(selected)
        showPicker = false
      },
    )
  }
}

/**
 * Variante texto para flujos que almacenan la hora como [String] (p. ej. plan del día).
 */
@Composable
fun MotTimeTextField(
  value: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier,
  label: String = "Hora (opcional)",
  placeholder: String = "Ej. 19:30",
  enabled: Boolean = true,
) {
  val parsed = remember(value) { PlanPlacementLogic.parseTime(value) }
  MotTimeField(
    time = parsed,
    onTimeChange = { time -> onValueChange(time?.let(PlanPlacementLogic::formatTime).orEmpty()) },
    modifier = modifier,
    label = label,
    placeholder = placeholder,
    enabled = enabled,
  )
}
