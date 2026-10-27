package com.myowntrip.app.domain.expenses

import com.myowntrip.app.domain.model.ExpenseCategory

/**
 * Heurísticas offline sobre el texto OCR de un ticket (mismo espíritu que
 * [com.myowntrip.app.domain.wallet.WalletDocumentParser] para documentos del Wallet).
 * Los tickets varían muchísimo de formato entre TPVs: esto es *best-effort*, no un OCR de
 * tickets "de verdad" — si no encuentra nada fiable, devuelve null y el usuario rellena a mano.
 */
object ReceiptParser {

  data class ParsedReceipt(
    val amount: Double?,
    val currency: String?,
    val category: ExpenseCategory?,
  )

  // Número con pinta de importe: "12,50", "1.234,56", "12.50"... El "\s?" antes de los 2
  // decimales tolera un espacio suelto que el OCR a veces mete ahí (p. ej. lee "39, 80" en
  // vez de "39,80" — visto en tickets reales).
  private const val MoneyAmountPattern = """\d{1,3}(?:[.,]\d{3})*[.,]\s?\d{2}"""

  // \b delante de "total" es crítico: sin él, "Subtotal" también hace match y captura
  // el subtotal en vez del total real.
  private val TotalLineRegex = Regex(
    """\b(?:total|importe|a pagar|amount due)[^\d]{0,15}($MoneyAmountPattern)""",
    RegexOption.IGNORE_CASE,
  )

  private val MoneyNumberRegex = Regex("""\b$MoneyAmountPattern\b""")

  private val CurrencyCodeRegex = Regex("""\b(EUR|USD|GBP|MAD|CHF|JPY)\b""", RegexOption.IGNORE_CASE)

  // Orden de prioridad: alojamiento → transporte → actividad → comida → (sin señal → null,
  // no se toca la categoría). "ticket"/"entrada" son ambiguos con actividades, van solo ahí.
  private val AccommodationKeywords = listOf(
    "hotel", "hostal", "hostel", "check-in", "check out", "alojamiento", "booking",
    "airbnb", "habitación", "habitacion", "room",
  )
  private val TransportKeywords = listOf(
    "renfe", "taxi", "uber", "cabify", "bolt", "autobús", "autobus", "bus", "metro",
    "tren", "train", "parking", "aparcamiento", "gasolina", "gasolinera", "peaje",
    "toll", "ave", "billete", "vueling", "iberia", "ryanair",
  )
  private val ActivityKeywords = listOf(
    "museo", "museum", "entrada", "tour", "excursión", "excursion", "concierto",
    "concert", "cine", "cinema", "teatro", "theatre", "atracción", "atraccion",
  )
  private val FoodKeywords = listOf(
    "restaurante", "restaurant", "cafetería", "cafeteria", "café", "cafe", "bar",
    "menú", "menu", "tapas", "pizza", "hamburguesa", "desayuno", "almuerzo", "cena",
    "bebida", "cerveza", "vino",
  )

  fun parse(rawText: String): ParsedReceipt {
    if (rawText.isBlank()) return ParsedReceipt(null, null, null)
    return ParsedReceipt(
      amount = findTotalAmount(rawText),
      currency = findCurrency(rawText),
      category = findCategory(rawText),
    )
  }

  private fun findTotalAmount(text: String): Double? {
    // Muchos tickets españoles imprimen "Total Base" (importe SIN IVA) antes de la línea
    // "TOTAL" final con impuestos. Si nos quedamos con la primera coincidencia, cogemos la
    // base y no el total real. En vez de la primera, nos quedamos con la MÁS GRANDE de todas
    // las líneas "total"/"importe" que aparezcan: el total con IVA siempre es ≥ que la base.
    val totalCandidates = TotalLineRegex.findAll(text)
      .mapNotNull { it.groupValues.getOrNull(1)?.let(::parseAmount) }
      .toList()
    if (totalCandidates.isNotEmpty()) return totalCandidates.max()

    // Sin ninguna línea "total"/"importe" localizable: el importe más grande del ticket
    // suele ser el total (mayor que cualquier línea suelta), así que es la mejor apuesta.
    return MoneyNumberRegex.findAll(text)
      .mapNotNull { parseAmount(it.value) }
      .maxOrNull()
  }

  private fun findCurrency(text: String): String? {
    when {
      text.contains('€') -> return "EUR"
      text.contains('$') -> return "USD"
      text.contains('£') -> return "GBP"
    }
    return CurrencyCodeRegex.find(text)?.value?.uppercase()
  }

  private fun findCategory(text: String): ExpenseCategory? {
    val lower = text.lowercase()
    return when {
      AccommodationKeywords.any { lower.contains(it) } -> ExpenseCategory.ACCOMMODATION
      TransportKeywords.any { lower.contains(it) } -> ExpenseCategory.TRANSPORT
      ActivityKeywords.any { lower.contains(it) } -> ExpenseCategory.ACTIVITY
      FoodKeywords.any { lower.contains(it) } -> ExpenseCategory.FOOD
      else -> null
    }
  }

  /** Normaliza "1.234,56" (europeo), "1,234.56" (US) o "39, 80" (espacio suelto de OCR). */
  internal fun parseAmount(raw: String): Double? {
    val clean = raw.filterNot { it.isWhitespace() }
    val hasComma = clean.contains(',')
    val hasDot = clean.contains('.')
    val normalized = when {
      hasComma && hasDot ->
        // El separador que va más a la derecha es el decimal; el otro es de miles.
        if (clean.lastIndexOf(',') > clean.lastIndexOf('.')) {
          clean.replace(".", "").replace(",", ".")
        } else {
          clean.replace(",", "")
        }
      hasComma -> clean.replace(",", ".")
      else -> clean
    }
    return normalized.toDoubleOrNull()?.takeIf { it in 0.01..999_999.0 }
  }
}
