package com.myowntrip.app.domain.expenses

import com.myowntrip.app.domain.model.ExpenseCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ReceiptParserTest {

  @Test
  fun parse_findsTotalKeywordSpanish() {
    val text = """
      CAFETERÍA EL SOL
      Café con leche      2,50
      Tostada             3,00
      TOTAL               5,50 EUR
      GRACIAS POR SU VISITA
    """.trimIndent()
    val result = ReceiptParser.parse(text)
    assertEquals(5.50, result.amount!!, 1e-6)
    assertEquals("EUR", result.currency)
  }

  @Test
  fun parse_findsTotalKeywordEnglish() {
    val text = "Subtotal 10.00\nTax 1.00\nTOTAL 11.00 USD"
    val result = ReceiptParser.parse(text)
    assertEquals(11.00, result.amount!!, 1e-6)
    assertEquals("USD", result.currency)
  }

  @Test
  fun parse_fallsBackToLargestAmountWithoutTotalKeyword() {
    val text = "Item A 3,20\nItem B 8,90\nItem C 1,10"
    val result = ReceiptParser.parse(text)
    // Sin "total": el importe más grande es la mejor apuesta.
    assertEquals(8.90, result.amount!!, 1e-6)
  }

  @Test
  fun parse_prefersFinalTotalOverBaseImponibleBeforeTax() {
    // Ticket real de bar: "Total Base" (sin IVA) aparece ANTES del TOTAL final con IVA.
    val text = """
      BAR EL RINCÓN
      2 Cañas          4,00
      1 Ración         5,00
      Total Base       8,26
      IVA 10%          0,74
      TOTAL            9,00
    """.trimIndent()
    val result = ReceiptParser.parse(text)
    assertEquals(9.00, result.amount!!, 1e-6)
  }

  @Test
  fun parse_realOcrOutputTwoColumnLayoutWithStraySpaces() {
    // Texto EXACTO capturado del log de un ticket real: ML Kit separa etiquetas y números
    // en dos bloques (maquetación en dos columnas del ticket), y mete un espacio suelto
    // antes de los 2 últimos decimales en varios números ("39, 80" en vez de "39,80").
    val text = """
      Mesa: 7
      15/06/2026
      1
      LA TASQUITA DE ENRIQUE
      C/ Ballesta, 6 - MADRID
      1
      2 Croqueta j amón ibérico
      Tlf: 915 32 45 67
      Ensaladilla rusa
      Tortilla de patatas
      1 Agua 50cl
      CIF: B86543210
      2 Copa vino tinto
      1 Café solo
      TOTAL
      Base imponible
      IVA 10
      IVA INCLUIDO
      TARJETA **** 4821
      Gracias por su visita
      Camarero: 03
      14:32
      14,00
      4,50
      9,00
      8,00
      2,50
      1, 80
      32,95
      3, 85
      39, 80
    """.trimIndent()
    val result = ReceiptParser.parse(text)
    assertEquals(39.80, result.amount!!, 1e-6)
  }

  @Test
  fun parse_realBarTicketWordingBaseImponibleThenTotal() {
    // Redacción exacta reportada: "Base Imponible" (sin la palabra "total") + "TOTAL" final.
    val text = """
      BAR EL RINCÓN
      Base Imponible   32,00
      IVA 10%           3,20
      TOTAL            39,00
    """.trimIndent()
    val result = ReceiptParser.parse(text)
    assertEquals(39.00, result.amount!!, 1e-6)
  }

  @Test
  fun parse_detectsCurrencySymbolEuro() {
    val result = ReceiptParser.parse("Total: 12,00 €")
    assertEquals("EUR", result.currency)
  }

  @Test
  fun parse_detectsCurrencySymbolDollar() {
    val result = ReceiptParser.parse("Total \$45.00")
    assertEquals("USD", result.currency)
  }

  @Test
  fun parse_blankTextReturnsNulls() {
    val result = ReceiptParser.parse("")
    assertNull(result.amount)
    assertNull(result.currency)
  }

  @Test
  fun parse_noMoneyLikeNumberReturnsNullAmount() {
    val result = ReceiptParser.parse("Gracias por su visita. Vuelva pronto.")
    assertNull(result.amount)
  }

  @Test
  fun parseAmount_europeanFormatWithThousands() {
    assertEquals(1234.56, ReceiptParser.parseAmount("1.234,56")!!, 1e-6)
  }

  @Test
  fun parseAmount_usFormatWithThousands() {
    assertEquals(1234.56, ReceiptParser.parseAmount("1,234.56")!!, 1e-6)
  }

  @Test
  fun parseAmount_simpleEuropeanDecimal() {
    assertEquals(12.50, ReceiptParser.parseAmount("12,50")!!, 1e-6)
  }

  @Test
  fun parseAmount_simpleUsDecimal() {
    assertEquals(12.50, ReceiptParser.parseAmount("12.50")!!, 1e-6)
  }

  @Test
  fun parseAmount_rejectsOutOfRangeValues() {
    assertNull(ReceiptParser.parseAmount("0,00"))
    assertNull(ReceiptParser.parseAmount("9999999,99"))
  }

  @Test
  fun parse_categoryFood() {
    val text = "RESTAURANTE LA PLAZA\nMenú del día\nBebida\nTOTAL 15,00 EUR"
    assertEquals(ExpenseCategory.FOOD, ReceiptParser.parse(text).category)
  }

  @Test
  fun parse_categoryTransport() {
    val text = "RENFE\nBillete AVE Madrid-Barcelona\nTOTAL 45,00 EUR"
    assertEquals(ExpenseCategory.TRANSPORT, ReceiptParser.parse(text).category)
  }

  @Test
  fun parse_categoryAccommodation() {
    val text = "HOTEL CENTRAL\nCheck-in 12/07/2026\nTOTAL 120,00 EUR"
    assertEquals(ExpenseCategory.ACCOMMODATION, ReceiptParser.parse(text).category)
  }

  @Test
  fun parse_categoryActivity() {
    val text = "MUSEO NACIONAL\nEntrada general\nTOTAL 12,00 EUR"
    assertEquals(ExpenseCategory.ACTIVITY, ReceiptParser.parse(text).category)
  }

  @Test
  fun parse_categoryNullWithoutKeywords() {
    val text = "TOTAL 8,00 EUR"
    assertNull(ReceiptParser.parse(text).category)
  }

  @Test
  fun parse_accommodationTakesPriorityOverFoodKeyword() {
    // Un hotel con "bar" en el ticket (bar del hotel) debe seguir siendo Alojamiento.
    val text = "HOTEL CENTRAL\nMinibar\nTOTAL 20,00 EUR"
    assertEquals(ExpenseCategory.ACCOMMODATION, ReceiptParser.parse(text).category)
  }
}
