package com.myowntrip.app.domain.wallet

import com.myowntrip.app.domain.model.EntryType
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

/**
 * Heurísticas offline para importación Wallet (JTBD 1).
 * Orden de tipo: vuelo → hotel → transporte → actividad → documento genérico.
 */
object WalletDocumentParser {

  private val SpanishLocale = Locale("es", "ES")

  private val FlightNumberRegex = Regex("""\b([A-Za-z]{2})\s?(\d{3,4})\b""", RegexOption.IGNORE_CASE)
  private val RouteArrowRegex = Regex(
    """\b([A-Za-zÀ-ÿ]{3,30})\s*(?:→|->|to|a|—|-)\s*([A-Za-zÀ-ÿ]{3,30})\b""",
    RegexOption.IGNORE_CASE,
  )
  private val IataRouteRegex = Regex("""\b([A-Za-z]{3})\s*[-–]\s*([A-Za-z]{3})\b""", RegexOption.IGNORE_CASE)
  private val IsoDateRegex = Regex("""\b(20\d{2})-(\d{2})-(\d{2})\b""")
  private val DmyDateRegex = Regex("""\b(\d{1,2})[/.-](\d{1,2})[/.-](20\d{2}|\d{2})\b""")
  private val SpanishDateRegex = Regex(
    """\b(\d{1,2})\s+(ene|feb|mar|abr|may|jun|jul|ago|sep|oct|nov|dic)[a-z]*\.?\s+(20\d{2})\b""",
    RegexOption.IGNORE_CASE,
  )
  private val TimeRegex = Regex("""\b([01]?\d|2[0-3]):([0-5]\d)\b""")
  private val CheckInRegex = Regex("""check[- ]?in[:\s]+([^\n,.]{3,40})""", RegexOption.IGNORE_CASE)
  private val HotelNameRegex = Regex(
    """(?:hotel|property|accommodation)[:\s]+([^\n,.]{3,50})""",
    RegexOption.IGNORE_CASE,
  )

  private val FlightKeywords = listOf(
    "boarding pass", "boarding", "vuelo", "flight", "aeropuerto", "airport", "gate", "puerta",
    "seat", "asiento", "departure", "salida", "arrival", "llegada", "pnr", "e-ticket",
  )
  private val HotelKeywords = listOf(
    "hotel", "check-in", "check in", "check-out", "check out", "reservation", "booking confirmation",
    "hospedaje", "alojamiento", "confirmation number", "nights",
  )
  private val TransportKeywords = listOf(
    "rental", "alquiler", "coche", "car hire", "pick-up", "pickup", "renfe", "ave", "train",
    "tren", "bus", "autobús", "metro", "transfer",
  )
  private val ActivityKeywords = listOf(
    "ticket", "entrada", "event", "evento", "museum", "museo", "tour", "excursion", "show",
    "concierto", "seat", "section", "sector",
  )

  fun parse(
    fileName: String?,
    mimeType: String?,
    contentText: String = "",
  ): ParsedWalletDocument {
    val name = fileName.orEmpty()
    val nameLower = name.lowercase(Locale.ROOT)
    val stem = name.substringBeforeLast('.').trim()
    val corpus = buildString {
      append(nameLower.replace('_', ' ').replace('-', ' '))
      append(' ')
      append(contentText.lowercase(Locale.ROOT).take(8_000))
    }.trim()

    val flightNumber = findFlightNumber(corpus) ?: findFlightNumber(name)
    val route = findRoute(corpus) ?: findRoute(name)
    val date = findDate(corpus) ?: findDate(name)
    val time = findTime(corpus) ?: findTime(name)

    val type = classifyType(nameLower, corpus, flightNumber)
    val resolvedDate = if (type == EntryType.HOTEL) {
      findCheckInDate(corpus) ?: findCheckInDate(name) ?: date
    } else {
      date
    }
    val title = buildTitle(type, stem, flightNumber, route, corpus)
    val notes = buildNotes(type, corpus, resolvedDate, time)

    val confidence = assessConfidence(
      type = type,
      title = title,
      stem = stem,
      date = resolvedDate,
      flightNumber = flightNumber,
      route = route,
      corpus = corpus,
    )

    return ParsedWalletDocument(
      type = type,
      title = title.ifBlank { stem.ifBlank { "Documento" } },
      date = resolvedDate,
      time = time,
      notes = notes,
      confidence = confidence,
    )
  }

  private fun classifyType(
    nameLower: String,
    corpus: String,
    flightNumber: String?,
  ): EntryType {
    if (flightNumber != null || score(corpus, FlightKeywords) >= 2 || nameLower.contains("boarding")) {
      return EntryType.FLIGHT
    }
    if (score(corpus, HotelKeywords) >= 2 || nameLower.contains("hotel") || nameLower.contains("booking")) {
      return EntryType.HOTEL
    }
    if (score(corpus, TransportKeywords) >= 2 ||
      nameLower.contains("train") || nameLower.contains("rental") || nameLower.contains("ave")
    ) {
      return EntryType.TRANSPORT
    }
    if (score(corpus, ActivityKeywords) >= 2 ||
      nameLower.contains("ticket") || nameLower.contains("entrada")
    ) {
      return EntryType.ACTIVITY
    }
    return EntryType.GENERIC
  }

  private fun buildTitle(
    type: EntryType,
    stem: String,
    flightNumber: String?,
    route: Pair<String, String>?,
    corpus: String,
  ): String = when (type) {
    EntryType.FLIGHT -> {
      val number = flightNumber
      val routeLabel = route?.let { "${it.first} → ${it.second}" }
      when {
        number != null && routeLabel != null -> "$number · $routeLabel"
        number != null -> number
        routeLabel != null -> routeLabel
        else -> humanizeStem(stem)
      }
    }
    EntryType.HOTEL -> {
      findHotelName(corpus, stem)
        ?.let { titleCaseRoute(it) }
        ?: humanizeStem(stem).takeIf { it.length >= 3 }
        ?: "Reserva de hotel"
    }
    EntryType.TRANSPORT -> {
      val number = findTransportNumber(corpus) ?: findTransportNumber(stem)
      when {
        number != null && route != null -> "$number · ${route.first} → ${route.second}"
        number != null -> number
        route != null -> "${route.first} → ${route.second}"
        else -> humanizeStem(stem)
      }
    }
    EntryType.ACTIVITY -> {
      val event = Regex("""(?:event|evento|ticket|entrada)[:\s]+([^\n,.]{3,50})""", RegexOption.IGNORE_CASE)
        .find(corpus)?.groupValues?.getOrNull(1)?.trim()
      event ?: humanizeStem(stem).takeIf { it.length >= 3 } ?: "Entrada"
    }
    EntryType.GENERIC -> humanizeStem(stem)
  }

  private fun buildNotes(
    type: EntryType,
    corpus: String,
    date: LocalDate?,
    time: LocalTime?,
  ): String? {
    val parts = mutableListOf<String>()
    when (type) {
      EntryType.HOTEL -> CheckInRegex.find(corpus)?.groupValues?.getOrNull(1)?.trim()?.let {
        parts += "Check-in: $it"
      }
      else -> Unit
    }
    if (date == null && time == null) return parts.takeIf { it.isNotEmpty() }?.joinToString(" · ")
    return parts.takeIf { it.isNotEmpty() }?.joinToString(" · ")
  }

  private fun assessConfidence(
    type: EntryType,
    title: String,
    stem: String,
    date: LocalDate?,
    flightNumber: String?,
    route: Pair<String, String>?,
    corpus: String,
  ): ParseConfidence {
    val titleFromStemOnly = title.equals(humanizeStem(stem), ignoreCase = true) ||
      title.equals(stem, ignoreCase = true)
    val hasSignal = flightNumber != null || route != null || date != null || type != EntryType.GENERIC
    val hasKeyword = score(corpus, FlightKeywords + HotelKeywords + TransportKeywords + ActivityKeywords) >= 1

    return when {
      !hasSignal && !hasKeyword && titleFromStemOnly -> ParseConfidence.FAILED
      type != EntryType.GENERIC && (date != null || flightNumber != null || route != null) -> ParseConfidence.HIGH
      type != EntryType.GENERIC || date != null -> ParseConfidence.LOW
      else -> ParseConfidence.FAILED
    }
  }

  private fun score(text: String, keywords: List<String>): Int =
    keywords.count { text.contains(it) }

  private fun findFlightNumber(text: String): String? =
    FlightNumberRegex.find(text)?.let {
      "${it.groupValues[1].uppercase(Locale.ROOT)} ${it.groupValues[2]}"
    }

  private fun findHotelName(corpus: String, stem: String): String? {
    Regex("""hotel:\s*([^\n,.]{3,80})""", RegexOption.IGNORE_CASE)
      .find(corpus)?.groupValues?.getOrNull(1)?.trim()?.let { sanitizeHotelName(it) }
      ?.takeIf { it.length >= 3 }
      ?.let { return it }
    val stemLabel = humanizeStem(stem).lowercase(SpanishLocale)
    return HotelNameRegex.findAll(corpus)
      .mapNotNull { it.groupValues.getOrNull(1)?.trim()?.let(::sanitizeHotelName) }
      .filter { it.length >= 3 && !it.equals(stemLabel, ignoreCase = true) }
      .maxByOrNull { it.length }
  }

  private fun sanitizeHotelName(raw: String): String =
    raw.substringBefore('·')
      .substringBefore(" booking")
      .substringBefore(" check-in")
      .trim()

  private fun findTransportNumber(text: String): String? {
    val ave = Regex("""\bAVE\s?\d{2,5}\b""", RegexOption.IGNORE_CASE).find(text)?.value
    if (ave != null) return ave.uppercase(Locale.ROOT).replace(Regex("\\s+"), " ")
    return FlightNumberRegex.find(text)?.let { "${it.groupValues[1]} ${it.groupValues[2]}" }
  }

  private fun findRoute(text: String): Pair<String, String>? {
    IataRouteRegex.find(text)?.let {
      return it.groupValues[1].uppercase(Locale.ROOT) to it.groupValues[2].uppercase(Locale.ROOT)
    }
    RouteArrowRegex.find(text)?.let {
      val from = it.groupValues[1].trim()
      val to = it.groupValues[2].trim()
      if (from.length >= 3 && to.length >= 3) {
        return titleCaseRoute(from) to titleCaseRoute(to)
      }
    }
    return null
  }

  internal fun findDate(text: String): LocalDate? {
    IsoDateRegex.find(text)?.let {
      return safeDate(it.groupValues[1].toInt(), it.groupValues[2].toInt(), it.groupValues[3].toInt())
    }
    SpanishDateRegex.find(text)?.let {
      val month = spanishMonth(it.groupValues[2])
      return safeDate(it.groupValues[3].toInt(), month, it.groupValues[1].toInt())
    }
    DmyDateRegex.find(text)?.let {
      val day = it.groupValues[1].toInt()
      val month = it.groupValues[2].toInt()
      var year = it.groupValues[3].toInt()
      if (year < 100) year += 2000
      return safeDate(year, month, day)
    }
    return null
  }

  private fun findCheckInDate(text: String): LocalDate? {
    val section = Regex("""check[- ]?in[^\n]{0,100}""", RegexOption.IGNORE_CASE)
      .find(text)?.value ?: return null
    return findDate(section)
  }

  internal fun findTime(text: String): LocalTime? {
    findCheckInTime(text)?.let { return it }
    val match = TimeRegex.find(text) ?: return null
    return runCatching {
      LocalTime.of(match.groupValues[1].toInt(), match.groupValues[2].toInt())
    }.getOrNull()
  }

  private fun findCheckInTime(text: String): LocalTime? {
    val match = Regex(
      """check[- ]?in[^\n]{0,60}?(\d{1,2}:\d{2})""",
      RegexOption.IGNORE_CASE,
    ).find(text) ?: return null
    return runCatching {
      val parts = match.groupValues[1].split(':')
      LocalTime.of(parts[0].toInt(), parts[1].toInt())
    }.getOrNull()
  }

  private fun safeDate(year: Int, month: Int, day: Int): LocalDate? = runCatching {
    LocalDate.of(year, month, day)
  }.getOrNull()

  private fun spanishMonth(token: String): Int = when (token.lowercase(Locale.ROOT).take(3)) {
    "ene" -> 1
    "feb" -> 2
    "mar" -> 3
    "abr" -> 4
    "may" -> 5
    "jun" -> 6
    "jul" -> 7
    "ago" -> 8
    "sep" -> 9
    "oct" -> 10
    "nov" -> 11
    "dic" -> 12
    else -> throw DateTimeParseException("Unknown month", token, 0)
  }

  private fun titleCaseRoute(value: String): String =
    value.split(' ').joinToString(" ") { word ->
      word.lowercase(SpanishLocale).replaceFirstChar { c ->
        if (c.isLowerCase()) c.titlecase(SpanishLocale) else c.toString()
      }
    }

  private fun humanizeStem(stem: String): String =
    stem
      .replace('_', ' ')
      .replace('-', ' ')
      .replace(Regex("\\s+"), " ")
      .trim()
      .split(' ')
      .joinToString(" ") { word ->
        word.lowercase(SpanishLocale).replaceFirstChar { c ->
          if (c.isLowerCase()) c.titlecase(SpanishLocale) else c.toString()
        }
      }

  fun formatParsedDate(date: LocalDate): String =
    date.format(DateTimeFormatter.ofPattern("d MMM yyyy", SpanishLocale))

  fun formatParsedTime(time: LocalTime): String =
    time.format(DateTimeFormatter.ofPattern("HH:mm"))
}
