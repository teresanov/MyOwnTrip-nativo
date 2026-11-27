package com.myowntrip.app.ui.components.date

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
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
import com.myowntrip.app.ui.theme.motPart
import kotlinx.datetime.LocalDate

/**
 * Rango de fechas en **un mes visible** + selección inicio/fin.
 *
 * No usa [androidx.compose.material3.DateRangePicker]: en material3 1.5 ese componente es una lista
 * vertical con todos los meses del rango de años, no el calendario de un mes con flechas.
 * Marco [DatePickerDialog] de Material 3; cuerpo [MotBoundedMonthCalendar] + rejillas año/mes.
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
    mutableStateOf(clampYearMonth(MotYearMonth.from(clampedStart), min, max))
  }
  var pickerSurface by remember { mutableStateOf(MotCalendarPickerSurface.Days) }

  val colors = motDatePickerColors()
  val headline = remember(rangeStart, rangeEnd, inputHeadline) {
    when (val end = rangeEnd) {
      null -> inputHeadline
      else -> formatMotDateRange(rangeStart, end)
    }
  }

  DatePickerDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      MOTTextButton(
        onClick = {
          val end = rangeEnd ?: return@MOTTextButton
          onConfirm(minOf(rangeStart, end), maxOf(rangeStart, end))
        },
        enabled = rangeEnd != null,
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
    MotDateRangePickerContent(
      supportingText = supportingText,
      headline = headline,
      colors = colors,
      displayedMonth = displayedMonth,
      onMonthChange = { displayedMonth = clampYearMonth(it, min, max) },
      minDate = min,
      maxDate = max,
      pickerSurface = pickerSurface,
      onPickerSurfaceChange = { pickerSurface = it },
      rangeStart = rangeStart,
      rangeEnd = rangeEnd,
      onDateSelected = { date ->
        val (start, end) = applyRangeSelection(date, rangeStart, rangeEnd)
        rangeStart = start
        rangeEnd = end
        displayedMonth = clampYearMonth(MotYearMonth.from(start), min, max)
      },
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun motDatePickerColors(): DatePickerColors =
  DatePickerDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)

/** Cuerpo del diálogo de rango: cabecera + calendario. También sirve para enseñarlo quieto. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MotDateRangePickerContent(
  supportingText: String,
  headline: String,
  colors: DatePickerColors,
  displayedMonth: MotYearMonth,
  onMonthChange: (MotYearMonth) -> Unit,
  minDate: LocalDate,
  maxDate: LocalDate,
  pickerSurface: MotCalendarPickerSurface,
  onPickerSurfaceChange: (MotCalendarPickerSurface) -> Unit,
  rangeStart: LocalDate?,
  rangeEnd: LocalDate?,
  onDateSelected: (LocalDate) -> Unit,
  modifier: Modifier = Modifier,
  today: LocalDate = motToday(),
) {
  Column(
    modifier = modifier
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
          onPickerSurfaceChange(MotCalendarPickerSurface.Year)
        }
      },
    )
    MotBoundedMonthCalendar(
      mode = MotCalendarSelectionMode.Range,
      displayedMonth = displayedMonth,
      onMonthChange = onMonthChange,
      minDate = minDate,
      maxDate = maxDate,
      colors = colors,
      pickerSurface = pickerSurface,
      onPickerSurfaceChange = onPickerSurfaceChange,
      selectedStart = rangeStart,
      selectedEnd = rangeEnd,
      onDateSelected = onDateSelected,
      today = today,
    )
  }
}

/** Fecha única: [DatePicker] de Material 3 (un mes + selector de año en la librería). */
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

  val pickerState = rememberDatePickerState(
    initialSelectedDateMillis = clampedInitial.toUtcMillis(),
    initialDisplayedMonthMillis = clampedInitial.toUtcMillis(),
    yearRange = min.year..max.year,
    initialDisplayMode = DisplayMode.Picker,
    selectableDates = selectableDates,
  )
  val colors = motDatePickerColors()
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
internal fun motSelectableDates(min: LocalDate, max: LocalDate): SelectableDates =
  object : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean =
      isDateInSelectableRange(utcTimeMillis.toLocalDateUtc(), min, max)

    override fun isSelectableYear(year: Int): Boolean = year in min.year..max.year
  }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MotPickerDialogHeader(
  supportingText: String,
  headline: String,
  colors: DatePickerColors,
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
      modifier = Modifier.motPart("supportingText"),
    )
    Text(
      text = headline,
      style = MaterialTheme.typography.titleLarge,
      color = colors.headlineContentColor,
      modifier = Modifier
        .padding(top = MOTSpacing.componentXs)
        .motPart("headline")
        .then(
          if (onHeadlineClick != null) Modifier.clickable(onClick = onHeadlineClick) else Modifier,
        ),
    )
  }
}
