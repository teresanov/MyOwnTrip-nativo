package com.myowntrip.app.ui.components.date

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
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
 * Rango de fechas en **un mes visible** + selección inicio/fin (paridad Figma día + rango).
 *
 * No usamos [androidx.compose.material3.DateRangePicker]: en **material3 1.5.0-alpha12** ese
 * composable es una [LazyColumn] vertical con **todos** los meses del `yearRange` (lista larga),
 * no el calendario de un mes con flechas del kit `Type=Day`.
 *
 * Shell [DatePickerDialog] M3; cuerpo [MotBoundedMonthCalendar] + rejillas año/mes (tokens M3).
 * Fecha única: [MotDatePickerDialog] sí usa [DatePicker] M3.
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

/** Fecha única: [DatePicker] M3 (un mes + selector año/mes en la librería). */
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
  val selectableDates = remember(min, max) { motSelectableDates(min, max) }
  val yearRange = remember(min, max) { min.year..max.year }

  val pickerState = rememberDatePickerState(
    initialSelectedDateMillis = clampedInitial.toUtcMillis(),
    initialDisplayedMonthMillis = clampedInitial.toUtcMillis(),
    yearRange = yearRange,
    initialDisplayMode = DisplayMode.Picker,
    selectableDates = selectableDates,
  )
  val scheme = MaterialTheme.colorScheme
  val colors = DatePickerDefaults.colors(
    containerColor = scheme.surfaceContainerHigh,
  )
  val headline by remember(pickerState.selectedDateMillis, inputHeadline) {
    derivedStateOf {
      pickerState.selectedDateMillis?.toLocalDateUtc()?.let(::formatMotDate) ?: inputHeadline
    }
  }

  DatePickerDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      MOTTextButton(
        onClick = {
          val millis = pickerState.selectedDateMillis ?: return@MOTTextButton
          onConfirm(millis.toLocalDateUtc())
        },
        enabled = pickerState.selectedDateMillis != null,
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
      DatePicker(
        state = pickerState,
        modifier = Modifier.fillMaxWidth(),
        colors = colors,
        title = { Text(supportingText) },
        headline = { Text(headline) },
        showModeToggle = false,
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
