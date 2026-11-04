package com.myowntrip.app.ui.components.date

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

internal val MotSpanishLocale = Locale("es", "ES")

private val RangeDateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", MotSpanishLocale)

private val DateInputFormatterSlash =
  DateTimeFormatter.ofPattern("dd/MM/yyyy", MotSpanishLocale)

private val DateInputFormatterFlexible =
  DateTimeFormatter.ofPattern("d/M/uuuu", MotSpanishLocale)

private val DateInputFormatterTextMonth =
  DateTimeFormatter.ofPattern("d MMM uuuu", MotSpanishLocale)

internal fun LocalDate.toUtcMillis(): Long =
  atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

internal fun Long.toLocalDateUtc(): LocalDate =
  Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()

internal fun formatMotDate(date: LocalDate): String = date.format(RangeDateFormatter)

/** Texto editable en campos de formulario (teclado). */
internal fun formatMotDateInput(date: LocalDate): String = date.format(DateInputFormatterSlash)

internal fun parseMotDateInput(text: String): LocalDate? {
  val trimmed = text.trim()
  if (trimmed.isEmpty()) return null
  val formatters = listOf(
    DateInputFormatterSlash,
    DateInputFormatterFlexible,
    DateInputFormatterTextMonth,
    RangeDateFormatter,
  )
  for (formatter in formatters) {
    try {
      return LocalDate.parse(trimmed, formatter)
    } catch (_: Exception) {
      // try next pattern
    }
  }
  return null
}

/** Encabezado del picker — siempre con año para rangos entre años distintos. */
internal fun formatMotDateRange(start: LocalDate, end: LocalDate): String =
  if (start == end) {
    formatMotDate(start)
  } else {
    "${formatMotDate(start)} – ${formatMotDate(end)}"
  }

internal fun formatMotTime(time: LocalTime): String =
  time.format(DateTimeFormatter.ofPattern("HH:mm", MotSpanishLocale))

/** Equivalente a `DateValidatorPointForward.from(hoy)` + ventana de planificación. */
internal fun defaultMotMinSelectableDate(): LocalDate = LocalDate.now()

internal fun defaultMotMaxSelectableDate(): LocalDate =
  LocalDate.now().plusYears(2)

/** Ventana aceptada al escribir fechas a mano (más amplia que el calendario modal). */
internal fun defaultMotMaxAcceptedTripDate(): LocalDate =
  LocalDate.now().plusYears(10)

internal fun defaultMotDocumentMinDate(): LocalDate =
  LocalDate.now().minusYears(10)

internal fun defaultMotDocumentMaxDate(): LocalDate =
  LocalDate.now().plusYears(10)

internal fun yearRangeFor(minDate: LocalDate, maxDate: LocalDate): IntRange =
  minDate.year..maxDate.year

internal fun defaultMotYearRange(): IntRange {
  val min = defaultMotMinSelectableDate()
  val max = defaultMotMaxSelectableDate()
  return yearRangeFor(min, max)
}

internal fun resolveMotTripDateBounds(
  minDate: LocalDate?,
  maxDate: LocalDate?,
): Pair<LocalDate, LocalDate> {
  val min = minDate ?: defaultMotMinSelectableDate()
  val max = maxDate ?: defaultMotMaxSelectableDate()
  return if (max.isBefore(min)) min to min else min to max
}

internal fun resolveMotTripAcceptedDateBounds(
  minDate: LocalDate?,
  maxDate: LocalDate?,
): Pair<LocalDate, LocalDate> {
  val min = minDate ?: defaultMotMinSelectableDate()
  val max = maxDate ?: defaultMotMaxAcceptedTripDate()
  return if (max.isBefore(min)) min to min else min to max
}

internal fun resolveMotTripPickerDateBounds(
  minDate: LocalDate?,
  maxDate: LocalDate?,
): Pair<LocalDate, LocalDate> = resolveMotTripDateBounds(minDate, maxDate)

internal fun normalizeMotDateRange(start: LocalDate, end: LocalDate): Pair<LocalDate, LocalDate> =
  if (end.isBefore(start)) end to start else start to end

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
  if (parsed.isBefore(minDate) || parsed.isAfter(maxDate)) {
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

internal fun resolveMotDocumentDateBounds(
  minDate: LocalDate?,
  maxDate: LocalDate?,
): Pair<LocalDate, LocalDate> {
  val min = minDate ?: defaultMotDocumentMinDate()
  val max = maxDate ?: defaultMotDocumentMaxDate()
  return if (max.isBefore(min)) min to min else min to max
}
