package com.myowntrip.app.domain.wallet

import com.myowntrip.app.domain.model.EntryType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class WalletDocumentParserTest {

  @Test
  fun parse_flightBoardingPass_extractsTypeTitleDateTime() {
    val content = """
      BOARDING PASS
      IB 3254
      Madrid - Barcelona
      2026-07-04
      Departure 09:15
      Gate B12
    """.trimIndent()

    val result = WalletDocumentParser.parse(
      fileName = "boarding-pass.pdf",
      mimeType = "application/pdf",
      contentText = content,
    )

    assertEquals(EntryType.FLIGHT, result.type)
    assertEquals("IB 3254 · Madrid → Barcelona", result.title)
    assertEquals(LocalDate.of(2026, 7, 4), result.date)
    assertEquals(LocalTime.of(9, 15), result.time)
    assertFalse(result.parseFailed)
  }

  @Test
  fun findDate_spanishMonth() {
    val date = WalletDocumentParser.findDate("check-in 15:00 4 jul 2026")
    assertEquals(LocalDate.of(2026, 7, 4), date)
  }

  @Test
  fun parse_hotelBookingFromFilename() {
    val result = WalletDocumentParser.parse(
      fileName = "hotel-casa-bonay-booking.pdf",
      mimeType = "application/pdf",
      contentText = "Booking confirmation check-in 15:00 4 jul 2026",
    )

    assertEquals(EntryType.HOTEL, result.type)
    assertEquals(LocalDate.of(2026, 7, 4), result.date)
    assertEquals(LocalTime.of(15, 0), result.time)
    assertFalse(result.parseFailed)
  }

  @Test
  fun parse_hotelRealisticConfirmation_extractsTitleDateCheckInTime() {
    val content = """
      Confirmación de reserva
      Hotel Casa Bonay
      Gran Via de les Corts Catalanes, 700
      08010 Barcelona, España
      Huésped principal: María García López
      Confirmation number: 4829174630
      Check-in: 4 jul 2026 desde las 15:00
      Check-out: 6 jul 2026 hasta las 11:00
      Habitación: Superior Double
      Hotel: Casa Bonay Barcelona
      Booking confirmation
    """.trimIndent()

    val result = WalletDocumentParser.parse(
      fileName = "hotel-casa-bonay-reserva.pdf",
      mimeType = "application/pdf",
      contentText = content,
    )

    assertEquals(EntryType.HOTEL, result.type)
    assertEquals("Casa Bonay Barcelona", result.title)
    assertEquals(LocalDate.of(2026, 7, 4), result.date)
    assertEquals(LocalTime.of(15, 0), result.time)
    assertTrue(result.notes?.contains("Check-in") == true)
    assertFalse(result.parseFailed)
  }

  @Test
  fun parse_aveTicket() {
    val result = WalletDocumentParser.parse(
      fileName = "ave-03142.pdf",
      mimeType = "application/pdf",
      contentText = "RENFE AVE 03142 Madrid - Barcelona 06/07/2026 18:30",
    )

    assertEquals(EntryType.TRANSPORT, result.type)
    assertTrue(result.title.contains("AVE 03142"))
    assertEquals(LocalDate.of(2026, 7, 6), result.date)
    assertEquals(LocalTime.of(18, 30), result.time)
  }

  @Test
  fun parse_scannedImageGeneric_failsToManual() {
    val result = WalletDocumentParser.parse(
      fileName = "scan001.jpg",
      mimeType = "image/jpeg",
      contentText = "",
    )

    assertEquals(EntryType.GENERIC, result.type)
    assertTrue(result.parseFailed)
  }

  @Test
  fun parse_invoiceWithFlightLikeInvoiceNumber_isNotFlight() {
    val content = """
      FACTURA Nº FA 2026-0412
      Fecha de emisión: 04/07/2026
      NIF: B12345678
      Concepto: Traslado privado al aeropuerto
      Base imponible: 100,00 €
      IVA 21%: 21,00 €
      Total: 121,00 €
    """.trimIndent()

    val result = WalletDocumentParser.parse(
      fileName = "factura-traslado.pdf",
      mimeType = "application/pdf",
      contentText = content,
    )

    assertEquals(EntryType.GENERIC, result.type)
  }

  @Test
  fun parse_realisticInvoiceWithCardPayment_isGenericNotFlight() {
    // Texto de factura real con datos personales sustituidos.
    val content = """
      PAGADA
      Proveedor Hosting sl
      CIF: B00000000
      FACTURA P-2020-00000
      Fecha de creación: 14/08/2020
      Fecha de vencimiento: 13/09/2020
      Cliente
      Nombre Apellido
      CIF o DNI: 00000000X
      Descripción Total
      Basico 50 GB 1 GB Ram garantizada (13/09/2020 - 12/09/2021) EUR23.88EUR
      Gestion administrativa EUR1.16EUR
      Subtotal EUR25.04EUR
      21.00% IVA EUR5.26EUR
      Total EUR30.30EUR
      Transacciones
      Fecha de transacción Medio de Pago Número de transacción Total
      19/08/2020 Tarjeta 00000000AA000000A EUR30.30EUR
      PDF generado el 19/08/2020
    """.trimIndent()

    val result = WalletDocumentParser.parse(
      fileName = "Orden-P-2020-00000.pdf",
      mimeType = "application/pdf",
      contentText = content,
    )

    assertEquals(EntryType.GENERIC, result.type)
    assertEquals(LocalDate.of(2020, 8, 14), result.date)
  }

  @Test
  fun parse_invoiceMentioningVuelo_isNotFlight() {
    val content = """
      Factura
      Servicio: vuelo de reposición de equipaje
      Importe: 45,00 €
      IVA incluido
    """.trimIndent()

    val result = WalletDocumentParser.parse(
      fileName = "factura-servicio.pdf",
      mimeType = "application/pdf",
      contentText = content,
    )

    assertEquals(EntryType.GENERIC, result.type)
  }

  @Test
  fun parse_filenameContainingAveAsSubstring_isNotTransport() {
    val result = WalletDocumentParser.parse(
      fileName = "clave-acceso.pdf",
      mimeType = "application/pdf",
      contentText = "",
    )

    assertEquals(EntryType.GENERIC, result.type)
  }

  @Test
  fun parse_activityTicket() {
    val result = WalletDocumentParser.parse(
      fileName = "entrada-sagrada-familia.pdf",
      mimeType = "application/pdf",
      contentText = "Ticket event: Sagrada Familia 5 jul 2026 11:00",
    )

    assertEquals(EntryType.ACTIVITY, result.type)
    assertEquals(LocalDate.of(2026, 7, 5), result.date)
    assertEquals(LocalTime.of(11, 0), result.time)
  }
}
