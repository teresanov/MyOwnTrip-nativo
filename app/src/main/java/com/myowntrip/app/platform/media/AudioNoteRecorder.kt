package com.myowntrip.app.platform.media

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioNoteRecorder @Inject constructor(
  @ApplicationContext private val context: Context,
) {
  private var recorder: MediaRecorder? = null
  private var outputFile: File? = null

  val isRecording: Boolean
    get() = recorder != null

  fun start(output: File): Boolean {
    stop()
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) !=
      PackageManager.PERMISSION_GRANTED
    ) {
      return false
    }
    output.parentFile?.mkdirs()
    val mediaRecorder = createRecorder()
    return try {
      mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
      mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
      mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
      mediaRecorder.setAudioSamplingRate(44_100)
      mediaRecorder.setAudioEncodingBitRate(128_000)
      mediaRecorder.setAudioChannels(1)
      mediaRecorder.setOutputFile(output.absolutePath)
      mediaRecorder.prepare()
      mediaRecorder.start()
      outputFile = output
      recorder = mediaRecorder
      true
    } catch (_: Exception) {
      runCatching { mediaRecorder.release() }
      output.delete()
      recorder = null
      outputFile = null
      false
    }
  }

  fun stop(): String? {
    val file = outputFile
    val mediaRecorder = recorder
    recorder = null
    outputFile = null
    if (mediaRecorder == null || file == null) {
      file?.delete()
      return null
    }
    return try {
      mediaRecorder.stop()
      mediaRecorder.release()
      if (file.exists() && file.length() > 0L) file.absolutePath else {
        file.delete()
        null
      }
    } catch (_: Exception) {
      runCatching { mediaRecorder.release() }
      if (file.exists() && file.length() > 0L) file.absolutePath else {
        file.delete()
        null
      }
    }
  }

  fun readMaxAmplitude(): Int =
    try {
      recorder?.maxAmplitude ?: 0
    } catch (_: Exception) {
      0
    }

  private fun createRecorder(): MediaRecorder =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      MediaRecorder(context)
    } else {
      @Suppress("DEPRECATION")
      MediaRecorder()
    }
}
