package com.myowntrip.app.platform.media

import android.media.MediaPlayer
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioNotePlayer @Inject constructor() {
  private var player: MediaPlayer? = null

  val currentPath: String?
    get() = player?.let { currentLoadedPath }

  private var currentLoadedPath: String? = null

  fun isPlaying(): Boolean = player?.isPlaying == true

  /**
   * Reproduce el audio. Tolera rutas inexistentes/corruptas o errores del MediaPlayer
   * sin propagar excepciones: si algo falla, libera el reproductor e invoca [onComplete]
   * para que la UI vuelva a estado parado en lugar de colgar la app.
   */
  fun play(path: String, onComplete: () -> Unit = {}) {
    if (currentLoadedPath == path && player != null && player?.isPlaying == false) {
      runCatching { player?.start() }
      return
    }
    stop()
    val file = File(path)
    if (!file.exists() || file.length() == 0L) {
      onComplete()
      return
    }
    try {
      player = MediaPlayer().apply {
        setOnErrorListener { _, _, _ ->
          this@AudioNotePlayer.stop()
          onComplete()
          true
        }
        setOnCompletionListener {
          this@AudioNotePlayer.stop()
          onComplete()
        }
        setDataSource(path)
        prepare()
        start()
      }
      currentLoadedPath = path
    } catch (_: Exception) {
      stop()
      onComplete()
    }
  }

  fun pause() {
    if (player?.isPlaying == true) {
      runCatching { player?.pause() }
    }
  }

  fun toggle(path: String, onComplete: () -> Unit = {}) {
    val active = player
    if (currentLoadedPath == path && active != null) {
      if (active.isPlaying) pause() else runCatching { active.start() }
    } else {
      play(path, onComplete)
    }
  }

  fun stop() {
    runCatching { player?.release() }
    player = null
    currentLoadedPath = null
  }

  fun currentPositionMs(): Int = runCatching { player?.currentPosition }.getOrNull() ?: 0

  fun durationMs(): Int = runCatching { player?.duration }.getOrNull() ?: 0
}
