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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.MOTIcons
import com.myowntrip.app.ui.theme.MOTSpacing
import kotlinx.datetime.LocalDate

/** Rejillas año/mes alineadas a `DatePicker` M3 (mismos [DatePickerColors] que el modal). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MotYearPickerGrid(
  yearRange: IntRange,
  selectedYear: Int,
  currentYear: Int,
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
      val isCurrent = year == currentYear
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
    items(12) { index ->
      val monthValue = index + 1
      val enabled = isMonthInSelectableRange(selectedYear, monthValue, minDate, maxDate)
      MotPickerCell(
        label = MotMonthShortNames[index].capitalizeFirst(),
        selected = monthValue == selectedMonth,
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
  selectedContainerColor: Color,
  selectedContentColor: Color,
  contentColor: Color,
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
