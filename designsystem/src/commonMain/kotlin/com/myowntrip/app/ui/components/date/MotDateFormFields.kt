package com.myowntrip.app.ui.components.date

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTIcons
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.motPart
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

/**
 * Rango de fechas del viaje: dos campos que se pueden escribir con el teclado («dd/mm/aaaa») y un
 * botón que abre el calendario ([MotDateRangePickerDialog]). Al salir de un campo se comprueba la
 * fecha; si fin va antes que inicio, se ordenan solas.
 */
@Composable
fun MotDateRangeField(
  startDate: LocalDate,
  endDate: LocalDate,
  onRangeChange: (LocalDate, LocalDate) -> Unit,
  modifier: Modifier = Modifier,
  label: String = "Fechas del viaje",
  startLabel: String = "Fecha inicio",
  endLabel: String = "Fecha fin",
  error: String? = null,
  enabled: Boolean = true,
  minDate: LocalDate? = null,
  maxDate: LocalDate? = null,
  pickerMaxDate: LocalDate? = null,
) {
  val (acceptedMin, acceptedMax) = resolveMotTripAcceptedDateBounds(minDate, maxDate)
  val (_, pickerMax) = resolveMotTripDateBounds(minDate, pickerMaxDate)

  var startText by remember { mutableStateOf(formatMotDateInput(startDate)) }
  var endText by remember { mutableStateOf(formatMotDateInput(endDate)) }
  var startLocalError by remember { mutableStateOf<String?>(null) }
  var endLocalError by remember { mutableStateOf<String?>(null) }
  var showPicker by remember { mutableStateOf(false) }

  LaunchedEffect(startDate, endDate) {
    startText = formatMotDateInput(startDate)
    endText = formatMotDateInput(endDate)
    startLocalError = null
    endLocalError = null
  }

  fun tryCommitFromInput() {
    val (startValidation, parsedStart) = validateMotDateInput(startText, acceptedMin, acceptedMax)
    val (endValidation, parsedEnd) = validateMotDateInput(endText, acceptedMin, acceptedMax)
    startLocalError = motDateInputErrorMessage(startValidation)
    endLocalError = motDateInputErrorMessage(endValidation)
    if (startValidation != MotDateInputValidation.Valid || endValidation != MotDateInputValidation.Valid) {
      return
    }
    val (rangeStart, rangeEnd) = normalizeMotDateRange(parsedStart!!, parsedEnd!!)
    startText = formatMotDateInput(rangeStart)
    endText = formatMotDateInput(rangeEnd)
    startLocalError = null
    endLocalError = null
    if (rangeStart != startDate || rangeEnd != endDate) {
      onRangeChange(rangeStart, rangeEnd)
    }
  }

  val displayRange = formatMotDateRange(startDate, endDate)

  Row(
    modifier = modifier
      .fillMaxWidth()
      .semantics { contentDescription = "$label, $displayRange" },
    verticalAlignment = Alignment.Top,
    horizontalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
  ) {
    OutlinedTextField(
      value = startText,
      onValueChange = {
        startText = it
        startLocalError = null
      },
      enabled = enabled,
      label = { Text(startLabel) },
      placeholder = { Text("dd/mm/aaaa") },
      isError = error != null || startLocalError != null,
      supportingText = {
        when {
          startLocalError != null -> Text(startLocalError!!)
          error != null -> Text(error)
        }
      },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
      singleLine = true,
      modifier = Modifier
        .weight(1f)
        .motPart("startField")
        .onFocusChanged { focus ->
          if (!focus.isFocused) tryCommitFromInput()
        },
    )
    OutlinedTextField(
      value = endText,
      onValueChange = {
        endText = it
        endLocalError = null
      },
      enabled = enabled,
      label = { Text(endLabel) },
      placeholder = { Text("dd/mm/aaaa") },
      isError = error != null || endLocalError != null,
      supportingText = {
        when {
          endLocalError != null -> Text(endLocalError!!)
          error != null && startLocalError == null -> Text(error)
        }
      },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
      singleLine = true,
      modifier = Modifier
        .weight(1f)
        .motPart("endField")
        .onFocusChanged { focus ->
          if (!focus.isFocused) tryCommitFromInput()
        },
    )
    if (enabled) {
      MOTIconButton(
        onClick = {
          tryCommitFromInput()
          showPicker = true
        },
        modifier = Modifier
          .align(Alignment.CenterVertically)
          .motPart("calendarButton"),
      ) {
        Icon(MOTIcons.CalendarMonth, contentDescription = "Abrir calendario de fechas")
      }
    }
  }

  if (showPicker) {
    MotDateRangePickerDialog(
      initialStartDate = startDate,
      initialEndDate = endDate,
      minDate = acceptedMin,
      maxDate = pickerMax,
      onDismiss = { showPicker = false },
      onConfirm = { start, end ->
        onRangeChange(start, end)
        showPicker = false
      },
    )
  }
}

/** Campo de formulario para una fecha. Al tocarlo se abre [MotDatePickerDialog]. */
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

  MotPickerTextField(
    value = displayValue,
    label = label,
    placeholder = placeholder,
    enabled = enabled,
    contentDescription = if (date != null) "$label, $displayValue" else label,
    onOpen = { showPicker = true },
    modifier = modifier,
  ) {
    Icon(MOTIcons.CalendarMonth, contentDescription = "Abrir calendario")
  }

  if (showPicker) {
    MotDatePickerDialog(
      initialDate = date ?: motToday(),
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

/** Campo de formulario para una hora (24 h). Al tocarlo se abre [MotTimePickerDialog]. */
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

  MotPickerTextField(
    value = displayValue,
    label = label,
    placeholder = placeholder,
    enabled = enabled,
    contentDescription = if (time != null) "$label, $displayValue" else label,
    onOpen = { showPicker = true },
    modifier = modifier,
  ) {
    Icon(MOTIcons.Schedule, contentDescription = "Abrir selector de hora")
  }

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

/** Igual que [MotTimeField], para pantallas que guardan la hora como texto («19:30»). */
@Composable
fun MotTimeTextField(
  value: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier,
  label: String = "Hora (opcional)",
  placeholder: String = "Ej. 19:30",
  enabled: Boolean = true,
) {
  val parsed = remember(value) { parseMotTime(value) }
  MotTimeField(
    time = parsed,
    onTimeChange = { time -> onValueChange(time?.let(::formatMotTime).orEmpty()) },
    modifier = modifier,
    label = label,
    placeholder = placeholder,
    enabled = enabled,
  )
}

/** Campo de solo lectura que abre un selector al tocarlo o al pulsar su icono final. */
@Composable
private fun MotPickerTextField(
  value: String,
  label: String,
  placeholder: String,
  enabled: Boolean,
  contentDescription: String,
  onOpen: () -> Unit,
  modifier: Modifier,
  icon: @Composable () -> Unit,
) {
  OutlinedTextField(
    value = value,
    onValueChange = {},
    readOnly = true,
    enabled = enabled,
    label = { Text(label) },
    placeholder = { Text(placeholder) },
    modifier = modifier
      .motPart("field")
      .semantics { this.contentDescription = contentDescription }
      .clickable(enabled = enabled, onClick = onOpen),
    trailingIcon = {
      if (enabled) {
        MOTIconButton(onClick = onOpen, modifier = Modifier.motPart("trailingButton"), content = icon)
      }
    },
  )
}
