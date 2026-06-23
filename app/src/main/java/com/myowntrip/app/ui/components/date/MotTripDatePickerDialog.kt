package com.myowntrip.app.ui.components.date

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MOTTextButton
import com.myowntrip.app.ui.theme.MyOwnTripTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

private val Spanish = Locale("es", "ES")
private val YearItemShape = RoundedCornerShape(12.dp)
private val DayShape = CircleShape

private enum class MotDatePickerStep {
  Year,
  Month,
  Day,
}

/**
 * Selector de fecha en tres pasos (año → mes → día) con tokens M3 del [DatePicker] modal.
 * Sustituye el calendario mes a mes con flechas; alineado al patrón M3 de rejilla de años.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MotTripDatePickerDialogImpl(
  title: String,
  initialDate: LocalDate,
  onDismiss: () -> Unit,
  onConfirm: (LocalDate) -> Unit,
  minDate: LocalDate? = null,
  maxDate: LocalDate? = null,
  yearRange: IntRange = defaultMotYearRange(),
) {
  var step by remember { mutableStateOf(MotDatePickerStep.Year) }
  var selectedYear by remember { mutableIntStateOf(initialDate.year) }
  var selectedMonth by remember { mutableIntStateOf(initialDate.monthValue) }
  var selectedDay by remember { mutableIntStateOf(initialDate.dayOfMonth) }

  val pickerColors = DatePickerDefaults.colors()
  val canConfirm = remember(selectedYear, selectedMonth, selectedDay, minDate, maxDate) {
    runCatching { LocalDate.of(selectedYear, selectedMonth, selectedDay) }
      .getOrNull()
      ?.let { date -> isDateInRange(date, minDate, maxDate) } == true
  }

  DatePickerDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      MOTTextButton(
        onClick = {
          onConfirm(LocalDate.of(selectedYear, selectedMonth, selectedDay))
        },
        enabled = canConfirm && step == MotDatePickerStep.Day,
      ) {
        Text("Aceptar")
      }
    },
    dismissButton = {
      MOTTextButton(onClick = onDismiss) { Text("Cancelar") }
    },
    properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = MOTSpacing.componentSm),
    ) {
      MotDatePickerDialogHeader(
        title = title,
        step = step,
        selectedYear = selectedYear,
        selectedMonth = selectedMonth,
        onBack = {
          step = when (step) {
            MotDatePickerStep.Month -> MotDatePickerStep.Year
            MotDatePickerStep.Day -> MotDatePickerStep.Month
            MotDatePickerStep.Year -> MotDatePickerStep.Year
          }
        },
      )
      HorizontalDivider(color = pickerColors.dividerColor)
      when (step) {
        MotDatePickerStep.Year -> {
          MotYearPickerGrid(
            yearRange = yearRange,
            selectedYear = selectedYear,
            colors = pickerColors,
            onYearSelected = { year ->
              selectedYear = year
              step = MotDatePickerStep.Month
            },
          )
        }
        MotDatePickerStep.Month -> {
          MotMonthPickerGrid(
            selectedYear = selectedYear,
            selectedMonth = selectedMonth,
            minDate = minDate,
            maxDate = maxDate,
            colors = pickerColors,
            onMonthSelected = { month ->
              selectedMonth = month
              selectedDay = clampDayForMonth(selectedYear, month, selectedDay, minDate, maxDate)
              step = MotDatePickerStep.Day
            },
          )
        }
        MotDatePickerStep.Day -> {
          MotDayPickerGrid(
            year = selectedYear,
            month = selectedMonth,
            selectedDay = selectedDay,
            minDate = minDate,
            maxDate = maxDate,
            colors = pickerColors,
            onDaySelected = { day -> selectedDay = day },
          )
        }
      }
    }
  }
}

@Composable
private fun MotDatePickerDialogHeader(
  title: String,
  step: MotDatePickerStep,
  selectedYear: Int,
  selectedMonth: Int,
  onBack: () -> Unit,
) {
  val monthLabel = Month.of(selectedMonth).getDisplayName(TextStyle.FULL, Spanish)
  val subtitle = when (step) {
    MotDatePickerStep.Year -> "Elige el año"
    MotDatePickerStep.Month -> selectedYear.toString()
    MotDatePickerStep.Day -> "$monthLabel $selectedYear"
  }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = MOTSpacing.layoutMd, vertical = MOTSpacing.componentSm),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(MOTSpacing.componentXs),
    ) {
      if (step != MotDatePickerStep.Year) {
        MOTIconButton(onClick = onBack) {
          Icon(
            Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Paso anterior",
          )
        }
      }
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          style = MaterialTheme.typography.labelLarge,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
          text = subtitle,
          style = MaterialTheme.typography.titleLarge,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }
    Text(
      text = when (step) {
        MotDatePickerStep.Year -> "Después elegirás mes y día"
        MotDatePickerStep.Month -> "Elige el mes"
        MotDatePickerStep.Day -> "Elige el día"
      },
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.padding(top = MOTSpacing.componentXs),
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MotYearPickerGrid(
  yearRange: IntRange,
  selectedYear: Int,
  colors: androidx.compose.material3.DatePickerColors,
  onYearSelected: (Int) -> Unit,
) {
  LazyVerticalGrid(
    columns = GridCells.Fixed(3),
    modifier = Modifier
      .fillMaxWidth()
      .heightIn(min = 240.dp, max = 320.dp)
      .padding(horizontal = MOTSpacing.layoutMd, vertical = MOTSpacing.componentSm),
    horizontalArrangement = Arrangement.spacedBy(MOTSpacing.componentXs),
    verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentXs),
  ) {
    items(yearRange.toList()) { year ->
      val selected = year == selectedYear
      val isCurrent = year == LocalDate.now().year
      MotPickerCell(
        label = year.toString(),
        selected = selected,
        emphasized = isCurrent && !selected,
        selectedContainerColor = colors.selectedYearContainerColor,
        selectedContentColor = colors.selectedYearContentColor,
        contentColor = if (isCurrent) colors.currentYearContentColor else colors.yearContentColor,
        onClick = { onYearSelected(year) },
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MotMonthPickerGrid(
  selectedYear: Int,
  selectedMonth: Int,
  minDate: LocalDate?,
  maxDate: LocalDate?,
  colors: androidx.compose.material3.DatePickerColors,
  onMonthSelected: (Int) -> Unit,
) {
  val months = Month.entries
  LazyVerticalGrid(
    columns = GridCells.Fixed(3),
    modifier = Modifier
      .fillMaxWidth()
      .heightIn(min = 220.dp, max = 280.dp)
      .padding(horizontal = MOTSpacing.layoutMd, vertical = MOTSpacing.componentSm),
    horizontalArrangement = Arrangement.spacedBy(MOTSpacing.componentXs),
    verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentXs),
  ) {
    items(months.size) { index ->
      val month = months[index]
      val monthValue = month.value
      val enabled = isMonthInRange(selectedYear, monthValue, minDate, maxDate)
      val selected = monthValue == selectedMonth
      MotPickerCell(
        label = month.getDisplayName(TextStyle.SHORT, Spanish).replaceFirstChar {
          if (it.isLowerCase()) it.titlecase(Spanish) else it.toString()
        },
        selected = selected,
        enabled = enabled,
        selectedContainerColor = colors.selectedYearContainerColor,
        selectedContentColor = colors.selectedYearContentColor,
        contentColor = colors.yearContentColor,
        onClick = { if (enabled) onMonthSelected(monthValue) },
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MotDayPickerGrid(
  year: Int,
  month: Int,
  selectedDay: Int,
  minDate: LocalDate?,
  maxDate: LocalDate?,
  colors: androidx.compose.material3.DatePickerColors,
  onDaySelected: (Int) -> Unit,
) {
  val firstOfMonth = LocalDate.of(year, month, 1)
  val daysInMonth = firstOfMonth.lengthOfMonth()
  val leadingEmpty = (firstOfMonth.dayOfWeek.value + 6) % 7
  val today = LocalDate.now()

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = MOTSpacing.layoutMd, vertical = MOTSpacing.componentSm),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceAround,
    ) {
      DayOfWeek.entries.forEach { day ->
        Text(
          text = day.getDisplayName(TextStyle.NARROW, Spanish).uppercase(Spanish),
          style = MaterialTheme.typography.labelMedium,
          color = colors.weekdayContentColor,
          textAlign = TextAlign.Center,
          modifier = Modifier.weight(1f),
        )
      }
    }
    val cells = buildList {
      repeat(leadingEmpty) { add(null) }
      repeat(daysInMonth) { add(it + 1) }
    }
    LazyVerticalGrid(
      columns = GridCells.Fixed(7),
      modifier = Modifier
        .fillMaxWidth()
        .heightIn(min = 220.dp, max = 280.dp),
      horizontalArrangement = Arrangement.spacedBy(2.dp),
      verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
      items(cells.size) { index ->
        val day = cells[index]
        if (day == null) {
          Box(modifier = Modifier.aspectRatio(1f))
        } else {
          val date = LocalDate.of(year, month, day)
          val enabled = isDateInRange(date, minDate, maxDate)
          val selected = day == selectedDay
          val isToday = date == today
          MotDayCell(
            day = day,
            selected = selected,
            enabled = enabled,
            isToday = isToday,
            colors = colors,
            onClick = { if (enabled) onDaySelected(day) },
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MotDayCell(
  day: Int,
  selected: Boolean,
  enabled: Boolean,
  isToday: Boolean,
  colors: androidx.compose.material3.DatePickerColors,
  onClick: () -> Unit,
) {
  val background = when {
    selected -> colors.selectedDayContainerColor
    else -> MaterialTheme.colorScheme.surface
  }
  val content = when {
    !enabled -> colors.disabledDayContentColor
    selected -> colors.selectedDayContentColor
    isToday -> colors.todayContentColor
    else -> colors.dayContentColor
  }
  Box(
    modifier = Modifier
      .aspectRatio(1f)
      .clip(DayShape)
      .then(
        if (isToday && !selected) {
          Modifier.border(1.dp, colors.todayDateBorderColor, DayShape)
        } else {
          Modifier
        },
      )
      .background(background)
      .clickable(enabled = enabled, onClick = onClick)
      .semantics {
        contentDescription = "Día $day"
        if (selected) stateDescription = "Seleccionado"
      },
    contentAlignment = Alignment.Center,
  ) {
    Text(
      text = day.toString(),
      style = MaterialTheme.typography.bodyLarge,
      color = content,
    )
  }
}

@Composable
private fun MotPickerCell(
  label: String,
  selected: Boolean,
  onClick: () -> Unit,
  selectedContainerColor: androidx.compose.ui.graphics.Color,
  selectedContentColor: androidx.compose.ui.graphics.Color,
  contentColor: androidx.compose.ui.graphics.Color,
  enabled: Boolean = true,
  emphasized: Boolean = false,
) {
  val scheme = MaterialTheme.colorScheme
  val shape = YearItemShape
  val background = when {
    selected -> selectedContainerColor
    emphasized -> scheme.surfaceContainerHigh
    else -> scheme.surface
  }
  val textColor = when {
    !enabled -> scheme.onSurface.copy(alpha = 0.38f)
    selected -> selectedContentColor
    else -> contentColor
  }

  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .heightIn(min = 44.dp)
      .then(
        if (selected) Modifier.border(1.dp, scheme.outline, shape) else Modifier,
      )
      .clip(shape)
      .clickable(enabled = enabled, onClick = onClick)
      .semantics {
        contentDescription = label
        if (selected) stateDescription = "Seleccionado"
      },
    color = background,
    shape = shape,
  ) {
    Box(contentAlignment = Alignment.Center) {
      Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        if (selected) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = scheme.primary,
          )
        }
        Text(
          text = label,
          style = MaterialTheme.typography.labelLarge,
          color = textColor,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }
  }
}

private fun isDateInRange(date: LocalDate, minDate: LocalDate?, maxDate: LocalDate?): Boolean {
  val (min, max) = resolveMotDocumentDateBounds(minDate, maxDate)
  return isDateInSelectableRange(date, min, max)
}

private fun isMonthInRange(
  year: Int,
  month: Int,
  minDate: LocalDate?,
  maxDate: LocalDate?,
): Boolean {
  val start = LocalDate.of(year, month, 1)
  val end = start.withDayOfMonth(start.lengthOfMonth())
  if (minDate != null && end.isBefore(minDate)) return false
  if (maxDate != null && start.isAfter(maxDate)) return false
  return true
}

private fun clampDayForMonth(
  year: Int,
  month: Int,
  day: Int,
  minDate: LocalDate?,
  maxDate: LocalDate?,
): Int {
  val maxDay = LocalDate.of(year, month, 1).lengthOfMonth()
  var result = day.coerceIn(1, maxDay)
  var date = LocalDate.of(year, month, result)
  if (minDate != null && date.isBefore(minDate)) {
    result = minDate.dayOfMonth.coerceAtMost(maxDay)
    date = LocalDate.of(year, month, result)
  }
  if (maxDate != null && date.isAfter(maxDate)) {
    result = maxDate.dayOfMonth.coerceAtMost(maxDay)
  }
  return result
}

@Preview(name = "Mot date picker · año")
@Composable
private fun MotYearPickerPreview() {
  MyOwnTripTheme {
    MotYearPickerGrid(
      yearRange = 2024..2028,
      selectedYear = 2026,
      colors = DatePickerDefaults.colors(),
      onYearSelected = {},
    )
  }
}
