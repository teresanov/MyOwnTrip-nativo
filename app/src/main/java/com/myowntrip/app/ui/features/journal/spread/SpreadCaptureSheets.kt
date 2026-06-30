package com.myowntrip.app.ui.features.journal.spread

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Keyboard
import com.myowntrip.app.domain.model.ItineraryBlock
import com.myowntrip.app.domain.model.JournalPieceType
import com.myowntrip.app.domain.model.JournalTextStyle
import com.myowntrip.app.platform.media.StickerShape
import com.myowntrip.app.ui.components.EyebrowLabel
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpreadCaptionSheet(
  title: String,
  caption: String,
  captionError: String?,
  pieceType: JournalPieceType,
  textStyle: JournalTextStyle,
  stickerShape: StickerShape,
  photoAttached: Boolean,
  photoPath: String?,
  audioAttached: Boolean,
  isRecording: Boolean,
  isSaving: Boolean,
  onCaptionChange: (String) -> Unit,
  onTextStyleChange: (JournalTextStyle) -> Unit,
  onStickerShapeChange: (StickerShape) -> Unit,
  onRequestCamera: () -> Unit,
  onRequestGallery: () -> Unit,
  onStartRecording: () -> Unit,
  onStopRecording: () -> Unit,
  onSave: () -> Unit,
  onDismiss: () -> Unit,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
    SpreadCaptionSheetContent(
      title = title,
      caption = caption,
      captionError = captionError,
      pieceType = pieceType,
      textStyle = textStyle,
      stickerShape = stickerShape,
      photoAttached = photoAttached,
      photoPath = photoPath,
      audioAttached = audioAttached,
      isRecording = isRecording,
      isSaving = isSaving,
      onCaptionChange = onCaptionChange,
      onTextStyleChange = onTextStyleChange,
      onStickerShapeChange = onStickerShapeChange,
      onRequestCamera = onRequestCamera,
      onRequestGallery = onRequestGallery,
      onStartRecording = onStartRecording,
      onStopRecording = onStopRecording,
      onSave = onSave,
    )
  }
}

@Composable
fun SpreadCaptionSheetContent(
  title: String,
  caption: String,
  captionError: String?,
  pieceType: JournalPieceType,
  textStyle: JournalTextStyle,
  stickerShape: StickerShape,
  photoAttached: Boolean,
  photoPath: String?,
  audioAttached: Boolean,
  isRecording: Boolean,
  isSaving: Boolean,
  onCaptionChange: (String) -> Unit,
  onTextStyleChange: (JournalTextStyle) -> Unit,
  onStickerShapeChange: (StickerShape) -> Unit,
  onRequestCamera: () -> Unit,
  onRequestGallery: () -> Unit,
  onStartRecording: () -> Unit,
  onStopRecording: () -> Unit,
  onSave: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = MOTSpacing.screenHorizontal)
      .padding(bottom = MOTSpacing.layoutLg),
    verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
  ) {
    Text(text = title, style = MaterialTheme.typography.headlineSmall)
    if (pieceType == JournalPieceType.TEXT) {
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(
          selected = textStyle == JournalTextStyle.HAND,
          onClick = { onTextStyleChange(JournalTextStyle.HAND) },
          label = { Text("A mano") },
          leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
          modifier = Modifier.semantics {
            contentDescription = "Estilo a mano" +
              if (textStyle == JournalTextStyle.HAND) ", seleccionado" else ""
          },
        )
        FilterChip(
          selected = textStyle == JournalTextStyle.TYPED,
          onClick = { onTextStyleChange(JournalTextStyle.TYPED) },
          label = { Text("A máquina") },
          leadingIcon = { Icon(Icons.Default.Keyboard, contentDescription = null) },
          modifier = Modifier.semantics {
            contentDescription = "Estilo a máquina (post-it)" +
              if (textStyle == JournalTextStyle.TYPED) ", seleccionado" else ""
          },
        )
      }
    }
    BasicTextField(
      value = caption,
      onValueChange = onCaptionChange,
      textStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onSurface,
      ),
      modifier = Modifier
        .fillMaxWidth()
        .heightIn(min = 120.dp)
        .semantics { contentDescription = "Escribir en la página" },
      decorationBox = { inner ->
        if (caption.isEmpty()) {
          Text(
            text = placeholderFor(pieceType),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
        inner()
      },
    )
    captionError?.let {
      Text(text = it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
    }

    if (pieceType == JournalPieceType.PHOTO || pieceType == JournalPieceType.STICKER) {
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(
          selected = photoAttached,
          onClick = onRequestCamera,
          label = { Text("Cámara") },
          leadingIcon = { Icon(Icons.Default.PhotoCamera, contentDescription = null) },
        )
        FilterChip(
          selected = false,
          onClick = onRequestGallery,
          label = { Text("Galería") },
          leadingIcon = { Icon(Icons.Default.PhotoLibrary, contentDescription = null) },
        )
      }
    }

    if (pieceType == JournalPieceType.STICKER) {
      Text(
        text = if (photoAttached) "Elige la forma del sticker" else "Forma del recorte",
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
      if (!photoAttached) {
        Text(
          text = "Añade una foto para ver la previsualización.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        StickerShapeChoice("Círculo", StickerShape.CIRCLE, stickerShape, photoPath, onStickerShapeChange)
        StickerShapeChoice("Redondeado", StickerShape.ROUNDED, stickerShape, photoPath, onStickerShapeChange)
        StickerShapeChoice("Marco foto", StickerShape.PHOTO_FRAME, stickerShape, photoPath, onStickerShapeChange)
      }
    }

    if (pieceType == JournalPieceType.AUDIO) {
      MOTButton(
        onClick = if (isRecording) onStopRecording else onStartRecording,
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text(if (isRecording) "Detener grabación" else if (audioAttached) "Volver a grabar" else "Grabar voz")
      }
    }

    MOTButton(
      onClick = onSave,
      enabled = !isSaving && !isRecording,
      modifier = Modifier.fillMaxWidth(),
    ) {
      Text("Pegar en la página")
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventBookmarkPickerSheet(
  blocks: List<ItineraryBlock>,
  onSelect: (String) -> Unit,
  onDismiss: () -> Unit,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
    EventBookmarkPickerContent(blocks = blocks, onSelect = onSelect)
  }
}

@Composable
fun EventBookmarkPickerContent(
  blocks: List<ItineraryBlock>,
  onSelect: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = MOTSpacing.screenHorizontal)
      .padding(bottom = MOTSpacing.layoutLg),
    verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
  ) {
    Text(text = "¿Qué evento marcas?", style = MaterialTheme.typography.headlineSmall)
    if (blocks.isEmpty()) {
      Text(
        text = "No hay actividades en el plan de hoy. Añade una en la pestaña Plan.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    } else {
      blocks.forEach { block ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(block.id) }
            .padding(vertical = 8.dp)
            .semantics { contentDescription = "Evento ${block.title}" },
        ) {
          EyebrowLabel(
            text = buildString {
              append(block.title)
              block.timeLabel?.let { append(" · $it") }
            },
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventBookmarkCaptureSheet(
  eventTitle: String,
  caption: String,
  captionError: String?,
  photoAttached: Boolean,
  audioAttached: Boolean,
  isRecording: Boolean,
  isSaving: Boolean,
  onCaptionChange: (String) -> Unit,
  onRequestCamera: () -> Unit,
  onRequestGallery: () -> Unit,
  onStartRecording: () -> Unit,
  onStopRecording: () -> Unit,
  onSaveText: () -> Unit,
  onSavePhoto: () -> Unit,
  onSaveAudio: () -> Unit,
  onBack: () -> Unit,
  onDismiss: () -> Unit,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
    EventBookmarkCaptureContent(
      eventTitle = eventTitle,
      caption = caption,
      captionError = captionError,
      photoAttached = photoAttached,
      audioAttached = audioAttached,
      isRecording = isRecording,
      isSaving = isSaving,
      onCaptionChange = onCaptionChange,
      onRequestCamera = onRequestCamera,
      onRequestGallery = onRequestGallery,
      onStartRecording = onStartRecording,
      onStopRecording = onStopRecording,
      onSaveText = onSaveText,
      onSavePhoto = onSavePhoto,
      onSaveAudio = onSaveAudio,
      onBack = onBack,
    )
  }
}

@Composable
fun EventBookmarkCaptureContent(
  eventTitle: String,
  caption: String,
  captionError: String?,
  photoAttached: Boolean,
  audioAttached: Boolean,
  isRecording: Boolean,
  isSaving: Boolean,
  onCaptionChange: (String) -> Unit,
  onRequestCamera: () -> Unit,
  onRequestGallery: () -> Unit,
  onStartRecording: () -> Unit,
  onStopRecording: () -> Unit,
  onSaveText: () -> Unit,
  onSavePhoto: () -> Unit,
  onSaveAudio: () -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = MOTSpacing.screenHorizontal)
      .padding(bottom = MOTSpacing.layoutLg),
    verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
  ) {
    EyebrowLabel(text = eventTitle)
    Text(text = "Recuerdo del evento", style = MaterialTheme.typography.headlineSmall)
    BasicTextField(
      value = caption,
      onValueChange = onCaptionChange,
      textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
      modifier = Modifier
        .fillMaxWidth()
        .heightIn(min = 80.dp),
      decorationBox = { inner ->
        if (caption.isEmpty()) {
          Text(
            "¿Qué pasó aquí?",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
        inner()
      },
    )
    captionError?.let {
      Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
    }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      FilterChip(
        selected = photoAttached,
        onClick = onRequestCamera,
        label = { Text("Foto") },
        leadingIcon = { Icon(Icons.Default.PhotoCamera, contentDescription = null) },
      )
      FilterChip(
        selected = false,
        onClick = onRequestGallery,
        label = { Text("Galería") },
        leadingIcon = { Icon(Icons.Default.PhotoLibrary, contentDescription = null) },
      )
    }
    MOTButton(
      onClick = if (isRecording) onStopRecording else onStartRecording,
      modifier = Modifier.fillMaxWidth(),
    ) {
      Text(if (isRecording) "Detener" else "Grabar voz")
    }
    MOTButton(onClick = onSavePhoto, enabled = !isSaving && photoAttached, modifier = Modifier.fillMaxWidth()) {
      Text("Pegar foto del evento")
    }
    MOTButton(onClick = onSaveAudio, enabled = !isSaving && audioAttached, modifier = Modifier.fillMaxWidth()) {
      Text("Pegar voz del evento")
    }
    MOTButton(onClick = onSaveText, enabled = !isSaving && caption.isNotBlank(), modifier = Modifier.fillMaxWidth()) {
      Text("Pegar nota del evento")
    }
    MOTButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
      Text("Elegir otro evento")
    }
  }
}

@Composable
private fun RowScope.StickerShapeChoice(
  label: String,
  shape: StickerShape,
  selected: StickerShape,
  photoPath: String?,
  onSelect: (StickerShape) -> Unit,
) {
  val isSelected = shape == selected
  val borderColor =
    if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
  val borderWidth = if (isSelected) 2.dp else 1.dp
  Column(
    modifier = Modifier
      .weight(1f)
      .clickable { onSelect(shape) }
      .padding(4.dp)
      .semantics {
        contentDescription = "Forma $label" + if (isSelected) ", seleccionada" else ""
      },
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(6.dp),
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
        .border(borderWidth, borderColor, stickerComposeShape(shape))
        .padding(6.dp),
      contentAlignment = Alignment.Center,
    ) {
      StickerShapePreview(photoPath = photoPath, shape = shape, modifier = Modifier.fillMaxSize())
    }
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall,
      color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
    )
  }
}

@Composable
private fun StickerShapePreview(
  photoPath: String?,
  shape: StickerShape,
  modifier: Modifier = Modifier,
) {
  when (shape) {
    StickerShape.PHOTO_FRAME -> {
      Box(
        modifier = modifier
          .clip(RoundedCornerShape(4.dp))
          .background(MaterialTheme.colorScheme.surface)
          .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(4.dp))
          .padding(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 12.dp),
      ) {
        StickerImageOrPlaceholder(photoPath, RoundedCornerShape(1.dp), Modifier.fillMaxSize())
      }
    }
    else -> {
      StickerImageOrPlaceholder(photoPath, stickerComposeShape(shape), modifier)
    }
  }
}

@Composable
private fun StickerImageOrPlaceholder(
  photoPath: String?,
  shape: androidx.compose.ui.graphics.Shape,
  modifier: Modifier = Modifier,
) {
  val clipped = modifier.clip(shape)
  if (photoPath != null) {
    SpreadMediaImage(
      path = photoPath,
      contentDescription = "Previsualización del sticker",
      contentScale = ContentScale.Crop,
      modifier = clipped,
    )
  } else {
    Box(modifier = clipped.background(MaterialTheme.colorScheme.surfaceVariant))
  }
}

private fun stickerComposeShape(shape: StickerShape): androidx.compose.ui.graphics.Shape =
  when (shape) {
    StickerShape.CIRCLE -> CircleShape
    StickerShape.ROUNDED -> RoundedCornerShape(14.dp)
    StickerShape.PHOTO_FRAME -> RoundedCornerShape(4.dp)
  }

private fun placeholderFor(pieceType: JournalPieceType): String = when (pieceType) {
  JournalPieceType.PHOTO -> "¿Qué capturas?"
  JournalPieceType.STICKER -> "Caption opcional para tu sticker…"
  JournalPieceType.AUDIO -> "Caption opcional…"
  else -> "¿Qué pasó?"
}

@Preview(name = "Sheet · Caption foto", showBackground = true, widthDp = 360, heightDp = 420)
@Composable
private fun SpreadCaptionSheetPreview() {
  JournalSpreadPreviewTheme {
    SpreadCaptionSheetContent(
      title = "Escribir en la página",
      caption = "La luz en la sala de Goya.",
      captionError = null,
      pieceType = JournalPieceType.TEXT,
      textStyle = JournalTextStyle.HAND,
      stickerShape = StickerShape.CIRCLE,
      photoAttached = false,
      photoPath = null,
      audioAttached = false,
      isRecording = false,
      isSaving = false,
      onCaptionChange = {},
      onTextStyleChange = {},
      onStickerShapeChange = {},
      onRequestCamera = {},
      onRequestGallery = {},
      onStartRecording = {},
      onStopRecording = {},
      onSave = {},
    )
  }
}

@Preview(name = "Sheet · Picker evento", showBackground = true, widthDp = 360, heightDp = 320)
@Composable
private fun EventBookmarkPickerPreview() {
  JournalSpreadPreviewTheme {
    EventBookmarkPickerContent(
      blocks = listOf(JournalSpreadPreviewSamples.planBlock),
      onSelect = {},
    )
  }
}

@Preview(name = "Sheet · Captura evento", showBackground = true, widthDp = 360, heightDp = 520)
@Composable
private fun EventBookmarkCapturePreview() {
  JournalSpreadPreviewTheme {
    EventBookmarkCaptureContent(
      eventTitle = "Museo del Prado · 11:00",
      caption = "Impresionante la colección permanente.",
      captionError = null,
      photoAttached = false,
      audioAttached = false,
      isRecording = false,
      isSaving = false,
      onCaptionChange = {},
      onRequestCamera = {},
      onRequestGallery = {},
      onStartRecording = {},
      onStopRecording = {},
      onSaveText = {},
      onSavePhoto = {},
      onSaveAudio = {},
      onBack = {},
    )
  }
}
