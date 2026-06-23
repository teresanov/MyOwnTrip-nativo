package com.myowntrip.app.ui.components.date

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

internal val MotSpanishLocale = Locale("es", "ES")

private val RangeDateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", MotSpanishLocale)

internal fun LocalDate.toUtcMillis(): Long =
  atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

internal fun Long.toLocalDateUtc(): LocalDate =
  Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()

internal fun formatMotDate(date: LocalDate): String = date.format(RangeDateFormatter)

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

internal fun resolveMotDocumentDateBounds(
  minDate: LocalDate?,
  maxDate: LocalDate?,
): Pair<LocalDate, LocalDate> {
  val min = minDate ?: defaultMotDocumentMinDate()
  val max = maxDate ?: defaultMotDocumentMaxDate()
  return if (max.isBefore(min)) min to min else min to max
}
