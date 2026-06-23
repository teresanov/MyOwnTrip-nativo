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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTSpacing
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

private val Spanish = Locale("es", "ES")
private val DayShape = CircleShape

internal enum class MotCalendarSelectionMode {
  Single,
  Range,
}

internal enum class MotDayRangeRole {
  None,
  RangeMiddle,
  RangeStart,
  RangeEnd,
  Selected,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MotBoundedMonthCalendar(
  mode: MotCalendarSelectionMode,
  displayedMonth: YearMonth,
  onMonthChange: (YearMonth) -> Unit,
  minDate: LocalDate,
  maxDate: LocalDate,
  colors: DatePickerColors,
  selectedDate: LocalDate? = null,
  selectedStart: LocalDate? = null,
  selectedEnd: LocalDate? = null,
  onDateSelected: (LocalDate) -> Unit,
  modifier: Modifier = Modifier,
) {
  val canGoPrevious = displayedMonth > YearMonth.from(minDate)
  val canGoNext = displayedMonth < YearMonth.from(maxDate)
  val monthLabel = buildString {
    append(
      displayedMonth.month.getDisplayName(TextStyle.FULL, Spanish)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Spanish) else it.toString() },
    )
    append(' ')
    append(displayedMonth.year)
  }

  Column(modifier = modifier.fillMaxWidth()) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = MOTSpacing.componentXs),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      MOTIconButton(
        onClick = { onMonthChange(displayedMonth.minusMonths(1)) },
        enabled = canGoPrevious,
      ) {
        Icon(
          Icons.AutoMirrored.Filled.KeyboardArrowLeft,
          contentDescription = "Mes anterior",
        )
      }
      Text(
        text = monthLabel,
        style = MaterialTheme.typography.titleLarge,
        color = colors.weekdayContentColor,
        textAlign = TextAlign.Center,
        modifier = Modifier.weight(1f),
      )
      MOTIconButton(
        onClick = { onMonthChange(displayedMonth.plusMonths(1)) },
        enabled = canGoNext,
      ) {
        Icon(
          Icons.AutoMirrored.Filled.KeyboardArrowRight,
          contentDescription = "Mes siguiente",
        )
      }
    }

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = MOTSpacing.componentXs),
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

    val firstOfMonth = displayedMonth.atDay(1)
    val daysInMonth = displayedMonth.lengthOfMonth()
    val leadingEmpty = (firstOfMonth.dayOfWeek.value + 6) % 7
    val today = LocalDate.now()
    val cells = buildList {
      repeat(leadingEmpty) { add(null) }
      repeat(daysInMonth) { add(displayedMonth.atDay(it + 1)) }
    }

    LazyVerticalGrid(
      columns = GridCells.Fixed(7),
      modifier = Modifier
        .fillMaxWidth()
        .heightIn(min = 220.dp, max = 280.dp)
        .padding(horizontal = MOTSpacing.layoutMd, vertical = MOTSpacing.componentSm),
      horizontalArrangement = Arrangement.spacedBy(2.dp),
      verticalArrangement = Arrangement.spacedBy(2.dp),
      userScrollEnabled = false,
    ) {
      items(cells.size, key = { index -> cells[index]?.toString() ?: "pad-$index" }) { index ->
        val date = cells[index]
        if (date == null) {
          Box(modifier = Modifier.aspectRatio(1f))
        } else {
          val enabled = isDateInSelectableRange(date, minDate, maxDate)
          val role = when (mode) {
            MotCalendarSelectionMode.Single -> {
              if (selectedDate == date) MotDayRangeRole.Selected else MotDayRangeRole.None
            }
            MotCalendarSelectionMode.Range -> dayRangeRole(date, selectedStart, selectedEnd)
          }
          MotCalendarDayCell(
            day = date.dayOfMonth,
            role = role,
            enabled = enabled,
            isToday = date == today,
            colors = colors,
            onClick = { if (enabled) onDateSelected(date) },
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MotCalendarDayCell(
  day: Int,
  role: MotDayRangeRole,
  enabled: Boolean,
  isToday: Boolean,
  colors: DatePickerColors,
  onClick: () -> Unit,
) {
  val background = when (role) {
    MotDayRangeRole.RangeMiddle -> colors.dayInSelectionRangeContainerColor
    MotDayRangeRole.RangeStart,
    MotDayRangeRole.RangeEnd,
    MotDayRangeRole.Selected,
    -> colors.selectedDayContainerColor
    MotDayRangeRole.None -> MaterialTheme.colorScheme.surface
  }
  val content = when {
    !enabled -> colors.disabledDayContentColor
    role == MotDayRangeRole.RangeMiddle -> colors.dayInSelectionRangeContentColor
    role != MotDayRangeRole.None -> colors.selectedDayContentColor
    isToday -> colors.todayContentColor
    else -> colors.dayContentColor
  }

  Box(
    modifier = Modifier
      .aspectRatio(1f)
      .clip(DayShape)
      .then(
        if (isToday && role == MotDayRangeRole.None) {
          Modifier.border(1.dp, colors.todayDateBorderColor, DayShape)
        } else {
          Modifier
        },
      )
      .background(background)
      .clickable(enabled = enabled, onClick = onClick)
      .semantics {
        contentDescription = "Día $day"
        if (role != MotDayRangeRole.None && role != MotDayRangeRole.RangeMiddle) {
          stateDescription = "Seleccionado"
        }
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

internal fun isDateInSelectableRange(
  date: LocalDate,
  minDate: LocalDate,
  maxDate: LocalDate,
): Boolean = !date.isBefore(minDate) && !date.isAfter(maxDate)

internal fun dayRangeRole(
  date: LocalDate,
  start: LocalDate?,
  end: LocalDate?,
): MotDayRangeRole {
  if (start == null) return MotDayRangeRole.None
  if (end == null) {
    return if (date == start) MotDayRangeRole.RangeStart else MotDayRangeRole.None
  }
  val rangeStart = minOf(start, end)
  val rangeEnd = maxOf(start, end)
  return when {
    date == rangeStart && date == rangeEnd -> MotDayRangeRole.Selected
    date == rangeStart -> MotDayRangeRole.RangeStart
    date == rangeEnd -> MotDayRangeRole.RangeEnd
    date.isAfter(rangeStart) && date.isBefore(rangeEnd) -> MotDayRangeRole.RangeMiddle
    else -> MotDayRangeRole.None
  }
}

internal fun applyRangeSelection(
  date: LocalDate,
  start: LocalDate?,
  end: LocalDate?,
): Pair<LocalDate, LocalDate?> = when {
  start == null || end != null -> date to null
  date.isBefore(start) -> date to start
  else -> start to date
}

internal fun clampYearMonth(month: YearMonth, minDate: LocalDate, maxDate: LocalDate): YearMonth {
  val minMonth = YearMonth.from(minDate)
  val maxMonth = YearMonth.from(maxDate)
  return when {
    month.isBefore(minMonth) -> minMonth
    month.isAfter(maxMonth) -> maxMonth
    else -> month
  }
}
