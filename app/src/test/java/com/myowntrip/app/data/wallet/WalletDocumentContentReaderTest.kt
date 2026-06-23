package com.myowntrip.app.data.wallet

import com.myowntrip.app.domain.wallet.WalletDocumentParser
import com.myowntrip.app.domain.model.EntryType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.time.LocalDate
import java.time.LocalTime

class WalletDocumentContentReaderTest {

  @Test
  fun extractPdfRoughText_readsParenthesisStrings() {
    val bytes = "%PDF-1.4 BT (IB 3254) Tj (Madrid - Barcelona) Tj".toByteArray(Charsets.ISO_8859_1)
    val text = WalletDocumentContentReader.extractPdfRoughText(bytes)
    assertTrue(text.contains("IB 3254"))
    assertTrue(text.contains("Madrid"))
  }

  @Test
  fun extractPdfRoughText_hotelSample_extractsBookingFields() {
    val pdf = locateSamplePdf("hotel-casa-bonay-reserva.pdf")
    val text = WalletDocumentContentReader.extractPdfRoughText(pdf)
    assertTrue("Texto extraído: $text", text.contains("Hotel Casa Bonay"))
    assertTrue(text.contains("4 jul 2026"))
    assertTrue(text.contains("Check-in"))

    val parsed = WalletDocumentParser.parse(
      fileName = "hotel-casa-bonay-reserva.pdf",
      mimeType = "application/pdf",
      contentText = text,
    )
    assertEquals(EntryType.HOTEL, parsed.type)
    assertEquals("Casa Bonay Barcelona", parsed.title)
    assertEquals(LocalDate.of(2026, 7, 4), parsed.date)
    assertEquals(LocalTime.of(15, 0), parsed.time)
  }

  private fun locateSamplePdf(name: String): ByteArray {
    val cwd = File(checkNotNull(System.getProperty("user.dir")))
  val candidates = listOf(
      cwd.resolve("src/test/resources/wallet/$name"),
      cwd.resolve("../docs/samples/wallet/$name"),
      cwd.resolve("../../docs/samples/wallet/$name"),
    )
    val file = candidates.firstOrNull { it.exists() }
      ?: error("No se encontró $name en ${candidates.joinToString()}")
    return file.readBytes()
  }
}
