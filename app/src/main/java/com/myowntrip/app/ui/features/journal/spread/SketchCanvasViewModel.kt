package com.myowntrip.app.ui.features.journal.spread

import android.graphics.Bitmap
import android.graphics.Canvas as AndroidCanvas
import android.graphics.Paint
import android.graphics.PorterDuff
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.JournalRepository
import com.myowntrip.app.platform.location.LocationCapture
import com.myowntrip.app.platform.media.JournalMediaStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.FileOutputStream
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class SketchCanvasViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val journalRepository: JournalRepository,
  private val mediaStorage: JournalMediaStorage,
  private val locationCapture: LocationCapture,
) : ViewModel() {
  private val dayId: String = checkNotNull(savedStateHandle["dayId"])

  private val _isSaving = MutableStateFlow(false)
  val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

  fun saveSketch(
    strokes: List<SketchStroke>,
    canvasSize: IntSize,
    backgroundColor: Color,
    hasLocationPermission: Boolean = false,
    onSaved: () -> Unit,
  ) {
    if (strokes.isEmpty() || canvasSize.width <= 0 || canvasSize.height <= 0) return
    viewModelScope.launch {
      _isSaving.value = true
      val file = mediaStorage.createSketchFile()
      val saved = withContext(Dispatchers.Default) {
        exportSketchBitmap(strokes, canvasSize, backgroundColor, file.absolutePath)
      }
      if (saved) {
        val location = if (hasLocationPermission) locationCapture.getCurrentLocation() else null
        journalRepository.addNote(
          dayId = dayId,
          text = "",
          pieceType = com.myowntrip.app.domain.model.JournalPieceType.SKETCH,
          sketchUri = file.absolutePath,
          latitude = location?.latitude,
          longitude = location?.longitude,
        )
        onSaved()
      }
      _isSaving.value = false
    }
  }

  private fun exportSketchBitmap(
    strokes: List<SketchStroke>,
    canvasSize: IntSize,
    backgroundColor: Color,
    outputPath: String,
  ): Boolean {
    return try {
      val bitmap = Bitmap.createBitmap(canvasSize.width, canvasSize.height, Bitmap.Config.ARGB_8888)
      val canvas = AndroidCanvas(bitmap)
      canvas.drawColor(backgroundColor.toArgb())
      strokes.forEach { stroke ->
        val paint = Paint().apply {
          isAntiAlias = true
          style = Paint.Style.STROKE
          strokeWidth = stroke.strokeWidth
          strokeCap = Paint.Cap.ROUND
          strokeJoin = Paint.Join.ROUND
          color = stroke.color.toArgb()
          if (stroke.isEraser) {
            xfermode = android.graphics.PorterDuffXfermode(PorterDuff.Mode.CLEAR)
          }
        }
        canvas.drawPath(stroke.path.asAndroidPath(), paint)
      }
      FileOutputStream(outputPath).use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
      }
      bitmap.recycle()
      true
    } catch (_: Exception) {
      false
    }
  }
}
