package com.myowntrip.app.ui.components.date

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MOTTextButton
import java.time.LocalDate
import java.time.YearMonth

/**
 * Rango de fechas de producto: cabecera M3 + [MotBoundedMonthCalendar] (círculos, paridad Figma).
 * Mes/año vía rejillas al pulsar el título del mes. Shell [DatePickerDialog] por medición estable.
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

  var rangeStart by remember { mutableStateOf(clampedStart) }
  var rangeEnd by remember { mutableStateOf<LocalDate?>(clampedEnd) }
  var displayedMonth by remember {
    mutableStateOf(clampYearMonth(YearMonth.from(clampedStart), min, max))
  }
  var pickerSurface by remember { mutableStateOf(MotCalendarPickerSurface.Days) }

  val scheme = MaterialTheme.colorScheme
  val colors = DatePickerDefaults.colors(
    containerColor = scheme.surfaceContainerHigh,
  )
  val headline = remember(rangeStart, rangeEnd, inputHeadline) {
    when (val end = rangeEnd) {
      null -> inputHeadline
      else -> formatMotDateRange(rangeStart, end)
    }
  }
  val canConfirm = rangeEnd != null

  DatePickerDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      MOTTextButton(
        onClick = {
          val end = rangeEnd ?: return@MOTTextButton
          onConfirm(minOf(rangeStart, end), maxOf(rangeStart, end))
        },
        enabled = canConfirm,
      ) {
        Text("Aceptar")
      }
    },
    dismissButton = {
      MOTTextButton(onClick = onDismiss) { Text("Cancelar") }
    },
    properties = DialogProperties(usePlatformDefaultWidth = false),
    colors = colors,
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = MOTSpacing.screenHorizontal)
        .padding(bottom = MOTSpacing.componentSm),
    ) {
      MotPickerDialogHeader(
        supportingText = supportingText,
        headline = headline,
        colors = colors,
        onHeadlineClick = {
          if (pickerSurface == MotCalendarPickerSurface.Days) {
            pickerSurface = MotCalendarPickerSurface.Year
          }
        },
      )
      MotBoundedMonthCalendar(
        mode = MotCalendarSelectionMode.Range,
        displayedMonth = displayedMonth,
        onMonthChange = { displayedMonth = clampYearMonth(it, min, max) },
        minDate = min,
        maxDate = max,
        colors = colors,
        pickerSurface = pickerSurface,
        onPickerSurfaceChange = { pickerSurface = it },
        selectedStart = rangeStart,
        selectedEnd = rangeEnd,
        onDateSelected = { date ->
          val (start, end) = applyRangeSelection(date, rangeStart, rangeEnd)
          rangeStart = start
          rangeEnd = end
          displayedMonth = clampYearMonth(YearMonth.from(start), min, max)
        },
      )
    }
  }
}

/** Fecha única: mismo calendario acotado + rejillas año/mes. */
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

  var selectedDate by remember { mutableStateOf(clampedInitial) }
  var displayedMonth by remember {
    mutableStateOf(clampYearMonth(YearMonth.from(clampedInitial), min, max))
  }
  var pickerSurface by remember { mutableStateOf(MotCalendarPickerSurface.Days) }

  val scheme = MaterialTheme.colorScheme
  val colors = DatePickerDefaults.colors(
    containerColor = scheme.surfaceContainerHigh,
  )
  val headline = remember(selectedDate, inputHeadline) {
    formatMotDate(selectedDate)
  }

  DatePickerDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      MOTTextButton(onClick = { onConfirm(selectedDate) }) {
        Text("Aceptar")
      }
    },
    dismissButton = {
      MOTTextButton(onClick = onDismiss) { Text("Cancelar") }
    },
    properties = DialogProperties(usePlatformDefaultWidth = false),
    colors = colors,
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = MOTSpacing.screenHorizontal)
        .padding(bottom = MOTSpacing.componentSm),
    ) {
      MotPickerDialogHeader(
        supportingText = supportingText,
        headline = headline,
        colors = colors,
        onHeadlineClick = {
          if (pickerSurface == MotCalendarPickerSurface.Days) {
            pickerSurface = MotCalendarPickerSurface.Year
          }
        },
      )
      MotBoundedMonthCalendar(
        mode = MotCalendarSelectionMode.Single,
        displayedMonth = displayedMonth,
        onMonthChange = { displayedMonth = clampYearMonth(it, min, max) },
        minDate = min,
        maxDate = max,
        colors = colors,
        pickerSurface = pickerSurface,
        onPickerSurfaceChange = { pickerSurface = it },
        selectedDate = selectedDate,
        onDateSelected = { date ->
          selectedDate = date
          displayedMonth = clampYearMonth(YearMonth.from(date), min, max)
        },
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MotPickerDialogHeader(
  supportingText: String,
  headline: String,
  colors: androidx.compose.material3.DatePickerColors,
  onHeadlineClick: (() -> Unit)? = null,
) {
  Column(
    modifier = Modifier.padding(
      start = MOTSpacing.layoutMd,
      end = MOTSpacing.componentSm,
      top = MOTSpacing.componentLg,
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
      modifier = Modifier
        .padding(top = MOTSpacing.componentXs)
        .then(
          if (onHeadlineClick != null) {
            Modifier.clickable(onClick = onHeadlineClick)
          } else {
            Modifier
          },
        ),
    )
  }
}

/**
 * Wizard año → mes → día. **Sin consumidor en `app/` hoy** — no es lámina PRODUCT en Figma
 * hasta que una pantalla lo use (`figma-date-product-contract-2026-11-03.md`).
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
