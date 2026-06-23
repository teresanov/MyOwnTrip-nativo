package com.myowntrip.app.domain.wallet

import android.net.Uri
import com.myowntrip.app.domain.model.EntryType
import com.myowntrip.app.domain.model.WalletEntry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File
import java.time.LocalDate

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class WalletOfflineAvailabilityTest {

  private lateinit var localPdfUri: String

  @org.junit.Before
  fun setup() {
    val file = File.createTempFile("ticket", ".pdf")
    file.writeText("pdf")
    localPdfUri = file.toURI().toString()
  }

  @Test
  fun localPdf_isAvailableOffline() {
    val entry = sampleEntry(pdfUri = localPdfUri)
    assertEquals(WalletOfflineAvailability.LocalDocument, entry.offlineAvailability())
    assertTrue(entry.offlineAvailability().isAvailableOffline())
  }

  @Test
  fun qrOnly_isAvailableOffline() {
    val entry = sampleEntry(qrPayload = "ABC123")
    assertEquals(WalletOfflineAvailability.QrCode, entry.offlineAvailability())
    assertTrue(entry.offlineAvailability().isAvailableOffline())
  }

  @Test
  fun cloudLink_isCloudOnly() {
    val entry = sampleEntry(linkUrl = "content://com.google.android.apps.docs/document/123")
    assertEquals(WalletOfflineAvailability.CloudReference, entry.offlineAvailability())
    assertFalse(entry.offlineAvailability().isAvailableOffline())
    assertTrue(entry.offlineAvailability().isCloudOnly())
  }

  @Test
  fun metadataOnly_isNotAvailableOffline() {
    val entry = sampleEntry()
    assertEquals(WalletOfflineAvailability.MetadataOnly, entry.offlineAvailability())
    assertFalse(entry.offlineAvailability().isAvailableOffline())
  }

  private fun sampleEntry(
    pdfUri: String? = null,
    qrPayload: String? = null,
    linkUrl: String? = null,
  ) = WalletEntry(
    id = "e1",
    tripId = "t1",
    type = EntryType.ACTIVITY,
    title = "Entrada",
    date = LocalDate.of(2026, 7, 5),
    pdfUri = pdfUri,
    qrPayload = qrPayload,
    linkUrl = linkUrl,
  )
}
