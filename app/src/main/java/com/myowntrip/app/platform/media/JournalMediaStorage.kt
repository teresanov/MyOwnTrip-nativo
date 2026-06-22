package com.myowntrip.app.platform.media

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JournalMediaStorage @Inject constructor(
  @ApplicationContext private val context: Context,
) {
  fun createPhotoFile(): File {
    val dir = File(context.filesDir, "journal/photos").apply { mkdirs() }
    return File(dir, "${UUID.randomUUID()}.jpg")
  }

  fun createAudioFile(): File {
    val dir = File(context.filesDir, "journal/audio").apply { mkdirs() }
    return File(dir, "${UUID.randomUUID()}.m4a")
  }

  fun copyImageFromUri(uri: Uri): String? {
    val destination = createPhotoFile()
    return copyImageToFile(uri, destination)
  }

  fun createReceiptFile(tripId: String): File {
    val dir = File(context.filesDir, "trips/$tripId/receipts").apply { mkdirs() }
    return File(dir, "${UUID.randomUUID()}.jpg")
  }

  fun copyReceiptFromUri(tripId: String, uri: Uri): String? {
    val destination = createReceiptFile(tripId)
    return copyImageToFile(uri, destination)
  }

  private fun copyImageToFile(uri: Uri, destination: File): String? {
    return try {
      context.contentResolver.openInputStream(uri)?.use { input ->
        destination.outputStream().use { output -> input.copyTo(output) }
      } ?: return null
      destination.absolutePath
    } catch (_: Exception) {
      destination.delete()
      null
    }
  }

  fun createWalletPhotoFile(tripId: String): File {
    val dir = File(context.filesDir, "trips/$tripId/wallet").apply { mkdirs() }
    return File(dir, "${UUID.randomUUID()}_entrada.jpg")
  }

  fun uriForFile(file: File): Uri =
    FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}
