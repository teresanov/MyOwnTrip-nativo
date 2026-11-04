package com.myowntrip.app.ui.components.date

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.MOTIcons
import com.myowntrip.app.ui.theme.MOTSpacing
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

private val Spanish = Locale("es", "ES")

/** Rejillas año/mes alineadas a `DatePicker` M3 (mismos [DatePickerColors] que el modal). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MotYearPickerGrid(
  yearRange: IntRange,
  selectedYear: Int,
  colors: DatePickerColors,
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
internal fun MotMonthPickerGrid(
  selectedYear: Int,
  selectedMonth: Int,
  minDate: LocalDate,
  maxDate: LocalDate,
  colors: DatePickerColors,
  onMonthSelected: (Int) -> Unit,
) {
  LazyVerticalGrid(
    columns = GridCells.Fixed(3),
    modifier = Modifier
      .fillMaxWidth()
      .heightIn(min = 220.dp, max = 280.dp)
      .padding(horizontal = MOTSpacing.layoutMd, vertical = MOTSpacing.componentSm),
    horizontalArrangement = Arrangement.spacedBy(MOTSpacing.componentXs),
    verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentXs),
  ) {
    items(Month.entries.size) { index ->
      val month = Month.entries[index]
      val monthValue = month.value
      val enabled = isMonthInSelectableRange(selectedYear, monthValue, minDate, maxDate)
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

@Composable
internal fun MotPickerCell(
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
  val shape = MaterialTheme.shapes.medium
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
        horizontalArrangement = Arrangement.spacedBy(MOTSpacing.componentXs),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        if (selected) {
          Icon(
            imageVector = MOTIcons.Check,
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

internal fun isMonthInSelectableRange(
  year: Int,
  month: Int,
  minDate: LocalDate,
  maxDate: LocalDate,
): Boolean {
  val start = LocalDate.of(year, month, 1)
  val end = start.withDayOfMonth(start.lengthOfMonth())
  if (end.isBefore(minDate)) return false
  if (start.isAfter(maxDate)) return false
  return true
}
