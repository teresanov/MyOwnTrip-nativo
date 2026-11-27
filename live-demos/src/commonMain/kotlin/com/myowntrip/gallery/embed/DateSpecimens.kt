package com.myowntrip.gallery.embed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.components.date.MotCalendarPickerSurface
import com.myowntrip.app.ui.components.date.MotDateField
import com.myowntrip.app.ui.components.date.MotDateRangeField
import com.myowntrip.app.ui.components.date.MotDateRangePickerContent
import com.myowntrip.app.ui.components.date.MotTimeField
import com.myowntrip.app.ui.components.date.MotYearMonth
import com.myowntrip.app.ui.components.date.formatMotDateRange
import com.myowntrip.app.ui.components.date.motDatePickerColors
import com.myowntrip.app.ui.components.date.motToday
import com.myowntrip.app.ui.theme.motPart
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.plus

/*
 * Fechas y horas con los textos de la app: Crear/Editar viaje (rango) y Wallet (fecha y hora).
 * Los campos son los reales: el botón del final abre el diálogo real.
 */

/**
 * piece: range (Fechas del viaje) · date (Fecha de Wallet) · time (Hora de Wallet).
 * El rango empieza dentro de una semana: el calendario solo deja elegir desde hoy.
 */
@Composable
fun DatePickersPlayground(p: Map<String, String>) {
  val today = remember { motToday() }
  var start by remember { mutableStateOf(today.plus(7, DateTimeUnit.DAY)) }
  var end by remember { mutableStateOf(today.plus(9, DateTimeUnit.DAY)) }
  var date by remember { mutableStateOf<LocalDate?>(null) }
  var time by remember { mutableStateOf<LocalTime?>(null) }
  Column(Modifier.width(PhoneWidth).padding(horizontal = 16.dp)) {
    when (p["piece"]) {
      "date" -> MotDateField(date = date, onDateChange = { date = it }, label = "Fecha (opcional)")
      "time" -> MotTimeField(time = time, onTimeChange = { time = it })
      else -> MotDateRangeField(
        startDate = start,
        endDate = end,
        onRangeChange = { s, e -> start = s; end = e },
        label = "Fechas del viaje",
      )
    }
  }
}

/**
 * Anatomía: el campo de rango y, debajo, el calendario quieto (sin ventana), con un rango de tres
 * días. Fechas fijas para que las medidas no cambien: «hoy» es el 1 de julio de 2026.
 */
@Composable
fun DatePickersAnatomy() {
  val today = LocalDate(2026, 7, 1)
  val start = LocalDate(2026, 7, 4)
  val end = LocalDate(2026, 7, 6)
  val colors = motDatePickerColors()
  Column(Modifier.width(PhoneWidth), verticalArrangement = Arrangement.spacedBy(16.dp)) {
    Column(Modifier.padding(horizontal = 16.dp)) {
      MotDateRangeField(
        startDate = start,
        endDate = end,
        onRangeChange = { _, _ -> },
        label = "Fechas del viaje",
        minDate = today,
      )
    }
    Surface(
      shape = MaterialTheme.shapes.extraLarge,
      color = colors.containerColor,
      modifier = Modifier.motPart("dialog"),
    ) {
      MotDateRangePickerContent(
        supportingText = "Selecciona fechas",
        headline = formatMotDateRange(start, end),
        colors = colors,
        displayedMonth = MotYearMonth(2026, 7),
        onMonthChange = {},
        minDate = today,
        maxDate = LocalDate(2028, 7, 1),
        pickerSurface = MotCalendarPickerSurface.Days,
        onPickerSurfaceChange = {},
        rangeStart = start,
        rangeEnd = end,
        onDateSelected = {},
        today = today,
      )
    }
  }
}
