package com.myowntrip.app.ui.components.date

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MOTTextButton
import java.time.LocalDate
import java.time.YearMonth

private val MotPickerShape = RoundedCornerShape(28.dp)

/**
 * Selector de rango con calendario acotado (flechas mes a mes, sin scroll infinito).
 * Límites por defecto: hoy → +2 años · equivalente a [CalendarConstraints] + forward validator.
 * Figma: Input date picker · Range (`51954:18554`).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotDateRangePickerDialog(
  initialStartDate: LocalDate,
  initialEndDate: LocalDate,
  onDismiss: () -> Unit,
  onConfirm: (LocalDate, LocalDate) -> Unit,
  supportingText: String = "Selecciona fechas",
  inputHeadline: String = "Introduce las fechas",
  minDate: LocalDate? = null,
  maxDate: LocalDate? = null,
) {
  val (min, max) = resolveMotTripDateBounds(minDate, maxDate)
  val clampedStart = initialStartDate.coerceInRange(min, max)
  val clampedEnd = initialEndDate.coerceInRange(min, max)

  var start by remember { mutableStateOf(clampedStart) }
  var end by remember { mutableStateOf<LocalDate?>(clampedEnd) }
  var displayedMonth by remember {
    mutableStateOf(clampYearMonth(YearMonth.from(clampedStart), min, max))
  }
  val colors = DatePickerDefaults.colors()

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false),
  ) {
    Surface(
      shape = MotPickerShape,
      color = MaterialTheme.colorScheme.surfaceContainerHigh,
      modifier = Modifier.padding(horizontal = MOTSpacing.screenHorizontal),
    ) {
      Column {
        MotPickerDialogHeader(
          supportingText = supportingText,
          headline = when (val rangeEnd = end) {
            null -> formatMotDate(start)
            else -> formatMotDateRange(start, rangeEnd)
          },
          colors = colors,
        )
        MotBoundedMonthCalendar(
          mode = MotCalendarSelectionMode.Range,
          displayedMonth = displayedMonth,
          onMonthChange = { month ->
            displayedMonth = clampYearMonth(month, min, max)
          },
          minDate = min,
          maxDate = max,
          colors = colors,
          selectedStart = start,
          selectedEnd = end,
          onDateSelected = { date ->
            val (newStart, newEnd) = applyRangeSelection(date, start, end)
            start = newStart
            end = newEnd
          },
        )
        MotPickerDialogActions(
          onCancel = onDismiss,
          onConfirm = {
            val rangeEnd = end ?: start
            val rangeStart = minOf(start, rangeEnd)
            val rangeEndDate = maxOf(start, rangeEnd)
            onConfirm(rangeStart, rangeEndDate)
          },
          confirmEnabled = end != null,
        )
      }
    }
  }
}

/**
 * Selector de fecha única con calendario acotado (flechas mes a mes).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotDatePickerDialog(
  initialDate: LocalDate,
  onDismiss: () -> Unit,
  onConfirm: (LocalDate) -> Unit,
  supportingText: String = "Selecciona fecha",
  inputHeadline: String = "Introduce la fecha",
  minDate: LocalDate? = null,
  maxDate: LocalDate? = null,
) {
  val (min, max) = resolveMotDocumentDateBounds(minDate, maxDate)
  val clampedInitial = initialDate.coerceInRange(min, max)

  var selected by remember { mutableStateOf(clampedInitial) }
  var displayedMonth by remember {
    mutableStateOf(clampYearMonth(YearMonth.from(clampedInitial), min, max))
  }
  val colors = DatePickerDefaults.colors()

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false),
  ) {
    Surface(
      shape = MotPickerShape,
      color = MaterialTheme.colorScheme.surfaceContainerHigh,
      modifier = Modifier.padding(horizontal = MOTSpacing.screenHorizontal),
    ) {
      Column {
        MotPickerDialogHeader(
          supportingText = supportingText,
          headline = formatMotDate(selected),
          colors = colors,
        )
        MotBoundedMonthCalendar(
          mode = MotCalendarSelectionMode.Single,
          displayedMonth = displayedMonth,
          onMonthChange = { month ->
            displayedMonth = clampYearMonth(month, min, max)
          },
          minDate = min,
          maxDate = max,
          colors = colors,
          selectedDate = selected,
          onDateSelected = { date -> selected = date },
        )
        MotPickerDialogActions(
          onCancel = onDismiss,
          onConfirm = { onConfirm(selected) },
          confirmEnabled = true,
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MotPickerDialogHeader(
  supportingText: String,
  headline: String,
  colors: androidx.compose.material3.DatePickerColors,
) {
  Column(
    modifier = Modifier.padding(
      start = MOTSpacing.layoutMd,
      end = MOTSpacing.componentSm,
      top = MOTSpacing.componentSm,
      bottom = MOTSpacing.componentXs,
    ),
  ) {
    Text(
      text = supportingText,
      style = MaterialTheme.typography.labelLarge,
      color = colors.titleContentColor,
    )
    Text(
      text = headline,
      style = MaterialTheme.typography.titleLarge,
      color = colors.headlineContentColor,
      modifier = Modifier.padding(top = MOTSpacing.componentXs),
    )
  }
}

@Composable
private fun MotPickerDialogActions(
  onCancel: () -> Unit,
  onConfirm: () -> Unit,
  confirmEnabled: Boolean,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = MOTSpacing.componentSm, vertical = MOTSpacing.componentXs),
    horizontalArrangement = Arrangement.End,
  ) {
    MOTTextButton(onClick = onCancel) { Text("Cancelar") }
    MOTTextButton(onClick = onConfirm, enabled = confirmEnabled) { Text("Aceptar") }
  }
}

/**
 * Selector año → mes → día para flujos que requieran el modal custom del DS (`51954:18136` · Day).
 */
@Composable
fun MotTripDatePickerDialog(
  title: String,
  initialDate: LocalDate,
  onDismiss: () -> Unit,
  onConfirm: (LocalDate) -> Unit,
  minDate: LocalDate? = null,
  maxDate: LocalDate? = null,
  yearRange: IntRange = defaultMotYearRange(),
) {
  val (min, max) = resolveMotDocumentDateBounds(minDate, maxDate)
  MotTripDatePickerDialogImpl(
    title = title,
    initialDate = initialDate.coerceInRange(min, max),
    onDismiss = onDismiss,
    onConfirm = onConfirm,
    minDate = min,
    maxDate = max,
    yearRange = yearRange,
  )
}

private fun LocalDate.coerceInRange(min: LocalDate, max: LocalDate): LocalDate = when {
  isBefore(min) -> min
  isAfter(max) -> max
  else -> this
}
