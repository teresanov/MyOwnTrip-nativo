package com.myowntrip.app.platform.media

import android.media.MediaRecorder
import android.os.Build
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioNoteRecorder @Inject constructor() {
  private var recorder: MediaRecorder? = null
  private var outputFile: File? = null

  val isRecording: Boolean
    get() = recorder != null

  fun start(output: File) {
    stop()
    outputFile = output
    val mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      MediaRecorder()
    } else {
      @Suppress("DEPRECATION")
      MediaRecorder()
    }
    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
    mediaRecorder.setOutputFile(output.absolutePath)
    mediaRecorder.prepare()
    mediaRecorder.start()
    recorder = mediaRecorder
  }

  fun stop(): String? {
    val file = outputFile ?: return null
    return try {
      recorder?.apply {
        stop()
        release()
      }
      file.absolutePath
    } catch (_: Exception) {
      file.delete()
      null
    } finally {
      recorder = null
      outputFile = null
    }
  }

  fun readMaxAmplitude(): Int =
    try {
      recorder?.maxAmplitude ?: 0
    } catch (_: Exception) {
      0
    }
}
