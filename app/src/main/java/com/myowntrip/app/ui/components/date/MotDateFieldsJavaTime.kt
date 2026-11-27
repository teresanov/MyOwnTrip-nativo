package com.myowntrip.app.ui.components.date

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalTime
import java.time.LocalDate
import java.time.LocalTime

/*
 * Los campos de fecha y hora están en el design system (:designsystem, con kotlinx-datetime).
 * La app trabaja con java.time: estas versiones solo convierten en la frontera, para que las
 * pantallas sigan pasando LocalDate / LocalTime de java.time.
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
  MotDateRangeField(
    startDate = startDate.toKotlinLocalDate(),
    endDate = endDate.toKotlinLocalDate(),
    onRangeChange = { start, end -> onRangeChange(start.toJavaLocalDate(), end.toJavaLocalDate()) },
    modifier = modifier,
    label = label,
    startLabel = startLabel,
    endLabel = endLabel,
    error = error,
    enabled = enabled,
    minDate = minDate?.toKotlinLocalDate(),
    maxDate = maxDate?.toKotlinLocalDate(),
    pickerMaxDate = pickerMaxDate?.toKotlinLocalDate(),
  )
}

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
  MotDateField(
    date = date?.toKotlinLocalDate(),
    onDateChange = { onDateChange(it?.toJavaLocalDate()) },
    modifier = modifier,
    label = label,
    placeholder = placeholder,
    enabled = enabled,
    minDate = minDate?.toKotlinLocalDate(),
    maxDate = maxDate?.toKotlinLocalDate(),
  )
}

@Composable
fun MotTimeField(
  time: LocalTime?,
  onTimeChange: (LocalTime?) -> Unit,
  modifier: Modifier = Modifier,
  label: String = "Hora (opcional)",
  placeholder: String = "Ej. 20:00",
  enabled: Boolean = true,
) {
  MotTimeField(
    time = time?.toKotlinLocalTime(),
    onTimeChange = { onTimeChange(it?.toJavaLocalTime()) },
    modifier = modifier,
    label = label,
    placeholder = placeholder,
    enabled = enabled,
  )
}
