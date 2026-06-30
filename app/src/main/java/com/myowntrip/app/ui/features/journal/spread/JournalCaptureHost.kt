package com.myowntrip.app.ui.features.journal.spread

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.animateIcon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myowntrip.app.domain.model.JournalPieceType
import com.myowntrip.app.ui.features.journal.JournalPermission
import com.myowntrip.app.ui.features.journal.JournalRecordingDialog
import com.myowntrip.app.ui.features.journal.TakePhotoScreen
import com.myowntrip.app.ui.features.journal.rememberJournalPermissionRequest
import kotlinx.coroutines.launch

/** Día + hoja donde debe caer una pieza nueva. */
data class CaptureTarget(val dayId: String, val pageId: String)

/**
 * Host de captura del Diario reutilizable (Day Hub y Diario del viaje). Renderiza la
 * cámara, el diálogo de grabación y las hojas inferiores de captión/evento, y fija el
 * día + la hoja activa como destino justo antes de cada guardado.
 */
@Composable
fun JournalCaptureHost(
  onPieceSaved: () -> Unit,
  activePage: AlbumPageUi?,
  resolveTarget: suspend (AlbumPageUi?) -> CaptureTarget,
  captureViewModel: SpreadCaptureViewModel = hiltViewModel(),
) {
  val captureState by captureViewModel.uiState.collectAsStateWithLifecycle()
  val context = LocalContext.current
  val scope = rememberCoroutineScope()

  // Antes de cualquier guardado, fija el día + la hoja activa como destino de la pieza.
  fun saveOnActivePage(save: () -> Unit) {
    scope.launch {
      val target = resolveTarget(activePage)
      captureViewModel.setActiveDay(target.dayId)
      captureViewModel.setTargetPage(target.pageId)
      save()
    }
  }

  val hasLocationPermission = ContextCompat.checkSelfPermission(
    context,
    Manifest.permission.ACCESS_FINE_LOCATION,
  ) == PackageManager.PERMISSION_GRANTED

  val requestCamera = rememberJournalPermissionRequest(
    permission = JournalPermission.Camera,
    onGranted = { captureViewModel.prepareCameraCapture() },
    onDenied = { captureViewModel.onPermissionDenied(JournalPermission.Camera) },
  )
  val requestAudio = rememberJournalPermissionRequest(
    permission = JournalPermission.Audio,
    onGranted = { captureViewModel.startRecording() },
    onDenied = { captureViewModel.onPermissionDenied(JournalPermission.Audio) },
  )
  val pickGalleryImage = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent(),
  ) { uri: Uri? ->
    if (uri != null) captureViewModel.importPhotoFromUri(uri)
  }

  fun closeAll() {
    captureViewModel.dismissFlow()
    onPieceSaved()
  }

  if (captureState.showCamera && captureState.pendingPhotoFile != null) {
    TakePhotoScreen(
      outputFile = captureState.pendingPhotoFile!!,
      onPhotoSaved = captureViewModel::onPhotoCaptured,
      onCancel = captureViewModel::cancelCamera,
    )
    return
  }

  if (captureState.isRecording) {
    JournalRecordingDialog(
      recordingLevels = captureState.recordingLevels,
      onStop = captureViewModel::stopRecording,
    )
  }

  when (captureState.step) {
    SpreadCaptureStep.Caption -> {
      SpreadCaptionSheet(
        title = captionTitle(captureState.pendingPieceType),
        caption = captureState.captionText,
        captionError = captureState.captionError,
        pieceType = captureState.pendingPieceType,
        textStyle = captureState.textStyle,
        stickerShape = captureState.stickerShape,
        photoAttached = captureState.photoPath != null,
        photoPath = captureState.photoPath,
        audioAttached = captureState.audioPath != null,
        isRecording = captureState.isRecording,
        isSaving = captureState.isSaving,
        onCaptionChange = captureViewModel::onCaptionChange,
        onRequestCamera = requestCamera,
        onRequestGallery = { pickGalleryImage.launch("image/*") },
        onStartRecording = requestAudio,
        onStopRecording = captureViewModel::stopRecording,
        onTextStyleChange = captureViewModel::onTextStyleChange,
        onStickerShapeChange = captureViewModel::onStickerShapeChange,
        onSave = {
          if (captureState.pendingPieceType == JournalPieceType.STICKER) {
            val path = captureState.photoPath
            if (path != null) {
              saveOnActivePage {
                captureViewModel.saveStickerFromPhoto(path, hasLocationPermission) { closeAll() }
              }
            } else {
              captureViewModel.onCaptionChange(captureState.captionText)
            }
          } else {
            saveOnActivePage {
              captureViewModel.saveCaptionPiece(hasLocationPermission) { closeAll() }
            }
          }
        },
        onDismiss = { captureViewModel.dismissFlow() },
      )
    }
    SpreadCaptureStep.EventPicker -> {
      EventBookmarkPickerSheet(
        blocks = captureState.dayBlocks,
        onSelect = captureViewModel::selectEventBlock,
        onDismiss = { captureViewModel.dismissFlow() },
      )
    }
    SpreadCaptureStep.EventCapture -> {
      val block = captureState.dayBlocks.find { it.id == captureState.pendingPlanBlockId }
      EventBookmarkCaptureSheet(
        eventTitle = block?.let { b ->
          buildString {
            append(b.title)
            b.timeLabel?.let { append(" · $it") }
          }
        } ?: "Evento",
        caption = captureState.captionText,
        captionError = captureState.captionError,
        photoAttached = captureState.photoPath != null,
        audioAttached = captureState.audioPath != null,
        isRecording = captureState.isRecording,
        isSaving = captureState.isSaving,
        onCaptionChange = captureViewModel::onCaptionChange,
        onRequestCamera = requestCamera,
        onRequestGallery = { pickGalleryImage.launch("image/*") },
        onStartRecording = requestAudio,
        onStopRecording = captureViewModel::stopRecording,
        onSaveText = {
          saveOnActivePage {
            captureViewModel.saveEventBookmark(JournalPieceType.TEXT, hasLocationPermission) { closeAll() }
          }
        },
        onSavePhoto = {
          saveOnActivePage {
            captureViewModel.saveEventBookmark(JournalPieceType.PHOTO, hasLocationPermission) { closeAll() }
          }
        },
        onSaveAudio = {
          saveOnActivePage {
            captureViewModel.saveEventBookmark(JournalPieceType.AUDIO, hasLocationPermission) { closeAll() }
          }
        },
        onBack = captureViewModel::backFromEventCapture,
        onDismiss = { captureViewModel.dismissFlow() },
      )
    }
    SpreadCaptureStep.Idle -> Unit
  }
}

/** Speed Dial (Material 3 Expressive) con las acciones de captura del Diario. */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun JournalCaptureFabMenu(
  expanded: Boolean,
  onExpandedChange: (Boolean) -> Unit,
  onPhoto: () -> Unit,
  onText: () -> Unit,
  onAudio: () -> Unit,
  onSticker: () -> Unit,
  onSketch: () -> Unit,
  modifier: Modifier = Modifier,
) {
  FloatingActionButtonMenu(
    expanded = expanded,
    modifier = modifier,
    button = {
      ToggleFloatingActionButton(
        checked = expanded,
        onCheckedChange = onExpandedChange,
        modifier = Modifier.semantics {
          contentDescription = if (expanded) "Cerrar menú de añadir" else "Añadir a la página"
        },
      ) {
        Icon(
          painter = rememberVectorPainter(if (expanded) Icons.Default.Close else Icons.Default.Add),
          contentDescription = null,
          modifier = Modifier.animateIcon({ checkedProgress }),
        )
      }
    },
  ) {
    FloatingActionButtonMenuItem(
      onClick = onPhoto,
      text = { Text("Foto") },
      icon = { Icon(Icons.Default.PhotoCamera, contentDescription = null) },
    )
    FloatingActionButtonMenuItem(
      onClick = onText,
      text = { Text("Texto") },
      icon = { Icon(Icons.Default.Edit, contentDescription = null) },
    )
    FloatingActionButtonMenuItem(
      onClick = onAudio,
      text = { Text("Voz") },
      icon = { Icon(Icons.Default.Mic, contentDescription = null) },
    )
    FloatingActionButtonMenuItem(
      onClick = onSticker,
      text = { Text("Sticker") },
      icon = { Icon(Icons.Default.StickyNote2, contentDescription = null) },
    )
    FloatingActionButtonMenuItem(
      onClick = onSketch,
      text = { Text("Boceto") },
      icon = { Icon(Icons.Default.Brush, contentDescription = null) },
    )
  }
}

private fun captionTitle(pieceType: JournalPieceType): String = when (pieceType) {
  JournalPieceType.PHOTO -> "Añadir foto"
  JournalPieceType.AUDIO -> "Nota de voz"
  JournalPieceType.STICKER -> "Crear sticker"
  else -> "Escribir en la página"
}
