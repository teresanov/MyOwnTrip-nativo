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
      2026-06-14
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
    assertEquals(LocalDate.of(2026, 6, 14), result.date)
    assertEquals(LocalTime.of(9, 15), result.time)
    assertFalse(result.parseFailed)
  }

  @Test
  fun findDate_spanishMonth() {
    val date = WalletDocumentParser.findDate("check-in 15:00 14 jun 2026")
    assertEquals(LocalDate.of(2026, 6, 14), date)
  }

  @Test
  fun parse_hotelBookingFromFilename() {
    val result = WalletDocumentParser.parse(
      fileName = "hotel-casa-bonay-booking.pdf",
      mimeType = "application/pdf",
      contentText = "Booking confirmation check-in 15:00 14 jun 2026",
    )

    assertEquals(EntryType.HOTEL, result.type)
    assertEquals(LocalDate.of(2026, 6, 14), result.date)
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
      Check-in: 14 jun 2026 desde las 15:00
      Check-out: 16 jun 2026 hasta las 11:00
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
    assertEquals(LocalDate.of(2026, 6, 14), result.date)
    assertEquals(LocalTime.of(15, 0), result.time)
    assertTrue(result.notes?.contains("Check-in") == true)
    assertFalse(result.parseFailed)
  }

  @Test
  fun parse_aveTicket() {
    val result = WalletDocumentParser.parse(
      fileName = "ave-03142.pdf",
      mimeType = "application/pdf",
      contentText = "RENFE AVE 03142 Madrid - Barcelona 16/06/2026 18:30",
    )

    assertEquals(EntryType.TRANSPORT, result.type)
    assertTrue(result.title.contains("AVE 03142"))
    assertEquals(LocalDate.of(2026, 6, 16), result.date)
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
  fun parse_activityTicket() {
    val result = WalletDocumentParser.parse(
      fileName = "entrada-sagrada-familia.pdf",
      mimeType = "application/pdf",
      contentText = "Ticket event: Sagrada Familia 15 jun 2026 11:00",
    )

    assertEquals(EntryType.ACTIVITY, result.type)
    assertEquals(LocalDate.of(2026, 6, 15), result.date)
    assertEquals(LocalTime.of(11, 0), result.time)
  }
}
