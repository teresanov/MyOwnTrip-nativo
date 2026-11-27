package com.myowntrip.app.ui.components.date

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/*
 * Fechas y horas del design system: textos en español propios (sin depender del idioma del sistema)
 * y cálculos con kotlinx-datetime, para que funcionen igual en Android y en las demos web.
 */

/** Nombres de mes completos, de enero a diciembre. */
internal val MotMonthNames = listOf(
  "enero", "febrero", "marzo", "abril", "mayo", "junio",
  "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre",
)

/** Nombres de mes abreviados (los mismos que usa Android en español). */
internal val MotMonthShortNames = listOf(
  "ene", "feb", "mar", "abr", "may", "jun", "jul", "ago", "sept", "oct", "nov", "dic",
)

/** Iniciales de los días, de lunes a domingo. */
internal val MotWeekdayInitials = listOf("L", "M", "X", "J", "V", "S", "D")

private const val MillisPerDay = 86_400_000L

internal fun String.capitalizeFirst(): String = replaceFirstChar { it.uppercaseChar() }

@OptIn(ExperimentalTime::class)
internal fun motToday(): LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

@OptIn(ExperimentalTime::class)
internal fun motNow(): LocalTime =
  Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time

internal fun LocalDate.toUtcMillis(): Long = toEpochDays().toLong() * MillisPerDay

internal fun Long.toLocalDateUtc(): LocalDate = LocalDate.fromEpochDays(floorDiv(MillisPerDay).toInt())

internal fun LocalDate.lengthOfMonth(): Int {
  val first = LocalDate(year, month.number, 1)
  return first.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY).day
}

private fun Int.pad2(): String = toString().padStart(2, '0')

/** «8 ene 2027». */
internal fun formatMotDate(date: LocalDate): String =
  "${date.day} ${MotMonthShortNames[date.month.number - 1]} ${date.year}"

/** Texto editable en campos de formulario (teclado): «08/01/2027». */
internal fun formatMotDateInput(date: LocalDate): String =
  "${date.day.pad2()}/${date.month.number.pad2()}/${date.year}"

private val SlashDate = Regex("""(\d{1,2})/(\d{1,2})/(\d{4})""")
private val TextMonthDate = Regex("""(\d{1,2})\s+(\p{L}+)\.?\s+(\d{4})""")

/** Acepta «15/03/2026», «5/3/2026» y «8 ene 2027» (con o sin punto, también «sep»). */
internal fun parseMotDateInput(text: String): LocalDate? {
  val trimmed = text.trim()
  if (trimmed.isEmpty()) return null
  SlashDate.matchEntire(trimmed)?.let { m ->
    val (d, mo, y) = m.destructured
    return safeDate(y.toInt(), mo.toInt(), d.toInt())
  }
  TextMonthDate.matchEntire(trimmed)?.let { m ->
    val (d, name, y) = m.destructured
    val key = name.lowercase()
    val month = when (key) {
      "sep" -> 9
      else -> MotMonthShortNames.indexOf(key).takeIf { it >= 0 }?.plus(1)
        ?: MotMonthNames.indexOf(key).takeIf { it >= 0 }?.plus(1)
    } ?: return null
    return safeDate(y.toInt(), month, d.toInt())
  }
  return null
}

private fun safeDate(year: Int, month: Int, day: Int): LocalDate? =
  runCatching { LocalDate(year, month, day) }.getOrNull()

/** Encabezado del picker — siempre con año para rangos entre años distintos. */
internal fun formatMotDateRange(start: LocalDate, end: LocalDate): String =
  if (start == end) {
    formatMotDate(start)
  } else {
    "${formatMotDate(start)} – ${formatMotDate(end)}"
  }

/** «20:00» (24 h). */
internal fun formatMotTime(time: LocalTime): String = "${time.hour.pad2()}:${time.minute.pad2()}"

private val TimeText = Regex("""(\d{1,2}):(\d{2})""")

/** Acepta «19:30» y «9:30». Vacío o no válido: null. */
internal fun parseMotTime(text: String?): LocalTime? {
  val m = TimeText.matchEntire(text?.trim().orEmpty()) ?: return null
  val (h, min) = m.destructured
  return runCatching { LocalTime(h.toInt(), min.toInt()) }.getOrNull()
}

/** Desde hoy: no se planifican viajes en el pasado. */
internal fun defaultMotMinSelectableDate(): LocalDate = motToday()

internal fun defaultMotMaxSelectableDate(): LocalDate = motToday().plus(DatePeriod(years = 2))

/** Ventana aceptada al escribir fechas a mano (más amplia que el calendario). */
internal fun defaultMotMaxAcceptedTripDate(): LocalDate = motToday().plus(DatePeriod(years = 10))

internal fun defaultMotDocumentMinDate(): LocalDate = motToday().minus(DatePeriod(years = 10))

internal fun defaultMotDocumentMaxDate(): LocalDate = motToday().plus(DatePeriod(years = 10))

private fun orderedBounds(min: LocalDate, max: LocalDate): Pair<LocalDate, LocalDate> =
  if (max < min) min to min else min to max

internal fun resolveMotTripDateBounds(
  minDate: LocalDate?,
  maxDate: LocalDate?,
): Pair<LocalDate, LocalDate> =
  orderedBounds(minDate ?: defaultMotMinSelectableDate(), maxDate ?: defaultMotMaxSelectableDate())

internal fun resolveMotTripAcceptedDateBounds(
  minDate: LocalDate?,
  maxDate: LocalDate?,
): Pair<LocalDate, LocalDate> =
  orderedBounds(minDate ?: defaultMotMinSelectableDate(), maxDate ?: defaultMotMaxAcceptedTripDate())

internal fun resolveMotDocumentDateBounds(
  minDate: LocalDate?,
  maxDate: LocalDate?,
): Pair<LocalDate, LocalDate> =
  orderedBounds(minDate ?: defaultMotDocumentMinDate(), maxDate ?: defaultMotDocumentMaxDate())

internal fun normalizeMotDateRange(start: LocalDate, end: LocalDate): Pair<LocalDate, LocalDate> =
  if (end < start) end to start else start to end

internal fun LocalDate.coerceInRange(min: LocalDate, max: LocalDate): LocalDate = when {
  this < min -> min
  this > max -> max
  else -> this
}

internal enum class MotDateInputValidation {
  Valid,
  Empty,
  InvalidFormat,
  OutOfRange,
}

internal fun validateMotDateInput(
  text: String,
  minDate: LocalDate,
  maxDate: LocalDate,
): Pair<MotDateInputValidation, LocalDate?> {
  if (text.isBlank()) return MotDateInputValidation.Empty to null
  val parsed = parseMotDateInput(text) ?: return MotDateInputValidation.InvalidFormat to null
  if (parsed < minDate || parsed > maxDate) {
    return MotDateInputValidation.OutOfRange to parsed
  }
  return MotDateInputValidation.Valid to parsed
}

internal fun motDateInputErrorMessage(validation: MotDateInputValidation): String? = when (validation) {
  MotDateInputValidation.Valid,
  MotDateInputValidation.Empty,
  -> null
  MotDateInputValidation.InvalidFormat -> "Fecha no válida"
  MotDateInputValidation.OutOfRange -> "Fecha fuera del rango permitido"
}

/* ---- Calendario: mes visible y selección ---- */

/** Un mes concreto (año + mes 1–12), para el calendario. */
internal data class MotYearMonth(val year: Int, val month: Int) : Comparable<MotYearMonth> {
  override fun compareTo(other: MotYearMonth): Int =
    compareValuesBy(this, other, MotYearMonth::year, MotYearMonth::month)

  fun plusMonths(n: Int): MotYearMonth {
    val index = year * 12 + (month - 1) + n
    return MotYearMonth(index.floorDiv(12), index.mod(12) + 1)
  }

  fun atDay(day: Int): LocalDate = LocalDate(year, month, day)

  fun lengthOfMonth(): Int = atDay(1).lengthOfMonth()

  companion object {
    fun from(date: LocalDate): MotYearMonth = MotYearMonth(date.year, date.month.number)
  }
}

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

internal fun isDateInSelectableRange(
  date: LocalDate,
  minDate: LocalDate,
  maxDate: LocalDate,
): Boolean = date in minDate..maxDate

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
    date > rangeStart && date < rangeEnd -> MotDayRangeRole.RangeMiddle
    else -> MotDayRangeRole.None
  }
}

internal fun applyRangeSelection(
  date: LocalDate,
  start: LocalDate?,
  end: LocalDate?,
): Pair<LocalDate, LocalDate?> = when {
  start == null || end != null -> date to null
  date < start -> date to start
  else -> start to date
}

internal fun clampYearMonth(month: MotYearMonth, minDate: LocalDate, maxDate: LocalDate): MotYearMonth {
  val minMonth = MotYearMonth.from(minDate)
  val maxMonth = MotYearMonth.from(maxDate)
  return when {
    month < minMonth -> minMonth
    month > maxMonth -> maxMonth
    else -> month
  }
}

internal fun isMonthInSelectableRange(
  year: Int,
  month: Int,
  minDate: LocalDate,
  maxDate: LocalDate,
): Boolean {
  val ym = MotYearMonth(year, month)
  val start = ym.atDay(1)
  val end = ym.atDay(ym.lengthOfMonth())
  return end >= minDate && start <= maxDate
}
