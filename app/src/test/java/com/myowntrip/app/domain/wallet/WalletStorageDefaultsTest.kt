package com.myowntrip.app.domain.wallet

import android.net.Uri
import com.myowntrip.app.domain.model.EntryType
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class WalletStorageDefaultsTest {

  @Test
  fun criticalTypes_defaultToOfflineCopy() {
    assertTrue(EntryType.FLIGHT.defaultSaveOfflineCopy())
    assertTrue(EntryType.HOTEL.defaultSaveOfflineCopy())
    assertTrue(EntryType.ACTIVITY.defaultSaveOfflineCopy())
  }

  @Test
  fun optionalTypes_defaultToCloudLink() {
    assertFalse(EntryType.TRANSPORT.defaultSaveOfflineCopy())
    assertFalse(EntryType.GENERIC.defaultSaveOfflineCopy())
  }

  @Test
  fun contentUri_canChooseCloudStorage() {
    assertTrue(Uri.parse("content://com.android.providers.downloads/document/1").canChooseCloudStorage())
    assertTrue(Uri.parse("https://example.com/ticket.pdf").canChooseCloudStorage())
  }

  @Test
  fun localFile_cannotChooseCloudStorage() {
    assertFalse(Uri.parse("file:///data/user/0/com.myowntrip.app/files/ticket.pdf").canChooseCloudStorage())
  }
}
