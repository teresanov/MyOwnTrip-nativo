package com.myowntrip.app.platform.media

import android.media.MediaPlayer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioNotePlayer @Inject constructor() {
  private var player: MediaPlayer? = null

  val currentPath: String?
    get() = player?.let { currentLoadedPath }

  private var currentLoadedPath: String? = null

  fun isPlaying(): Boolean = player?.isPlaying == true

  fun play(path: String, onComplete: () -> Unit = {}) {
    if (currentLoadedPath == path && player != null && !player!!.isPlaying) {
      player?.start()
      return
    }
    stop()
    currentLoadedPath = path
    player = MediaPlayer().apply {
      setDataSource(path)
      prepare()
      setOnCompletionListener {
        stop()
        onComplete()
      }
      start()
    }
  }

  fun pause() {
    if (player?.isPlaying == true) {
      player?.pause()
    }
  }

  fun toggle(path: String, onComplete: () -> Unit = {}) {
    if (currentLoadedPath == path && player != null) {
      if (player!!.isPlaying) pause() else player?.start()
    } else {
      play(path, onComplete)
    }
  }

  fun stop() {
    player?.release()
    player = null
    currentLoadedPath = null
  }

  fun currentPositionMs(): Int = player?.currentPosition ?: 0

  fun durationMs(): Int = player?.duration ?: 0
}
