package com.myowntrip.app.domain.wallet

import com.myowntrip.app.domain.model.EntryType
import com.myowntrip.app.domain.model.WalletEntry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class WalletDuplicateDetectorTest {
  private val tripId = "trip-1"
  private val date = LocalDate.of(2026, 7, 4)

  private fun entry(
    id: String,
    title: String,
    type: EntryType = EntryType.FLIGHT,
    date: LocalDate? = this.date,
    pdfUri: String? = null,
    linkUrl: String? = null,
    archivedAt: Long? = null,
  ) = WalletEntry(
    id = id,
    tripId = tripId,
    type = type,
    title = title,
    date = date,
    pdfUri = pdfUri,
    linkUrl = linkUrl,
    archivedAt = archivedAt,
  )

  @Test
  fun `strong match on same link url`() {
    val existing = entry("1", "Doc", linkUrl = "https://drive.google.com/file/abc")
    val pending = entry("new", "Otro título", linkUrl = "https://drive.google.com/file/abc")
    val match = WalletDuplicateDetector.findDuplicate(pending, listOf(existing))
    assertEquals("1", match?.existing?.id)
    assertEquals(WalletDuplicateStrength.Strong, match?.strength)
  }

  @Test
  fun `strong match on same attachment file name`() {
    val existing = entry("1", "Vuelo", pdfUri = "file:///data/billete.pdf")
    val pending = entry("new", "Copia", pdfUri = "file:///tmp/billete.pdf")
    val match = WalletDuplicateDetector.findDuplicate(
      pending = pending,
      existingEntries = listOf(existing),
      attachmentFileName = "billete.pdf",
    )
    assertEquals(WalletDuplicateStrength.Strong, match?.strength)
  }

  @Test
  fun `medium match on type title and date`() {
    val existing = entry("1", "IB 3254 · Madrid → Barcelona")
    val pending = entry("new", "ib 3254 · madrid → barcelona")
    val match = WalletDuplicateDetector.findDuplicate(pending, listOf(existing))
    assertEquals(WalletDuplicateStrength.Medium, match?.strength)
  }

  @Test
  fun `ignores archived entries`() {
    val archived = entry("1", "IB 3254", archivedAt = 1L)
    val pending = entry("new", "IB 3254")
    assertNull(WalletDuplicateDetector.findDuplicate(pending, listOf(archived)))
  }

  @Test
  fun `no match when fields differ`() {
    val existing = entry("1", "Hotel Casa Bonay", type = EntryType.HOTEL)
    val pending = entry("new", "IB 3254", type = EntryType.FLIGHT)
    assertNull(WalletDuplicateDetector.findDuplicate(pending, listOf(existing)))
  }
}
