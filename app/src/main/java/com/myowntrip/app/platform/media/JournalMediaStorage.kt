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

  fun uriForFile(file: File): Uri =
    FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}
