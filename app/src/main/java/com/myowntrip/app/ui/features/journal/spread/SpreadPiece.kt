package com.myowntrip.app.ui.features.journal.spread

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.myowntrip.app.domain.model.ItineraryBlock
import com.myowntrip.app.domain.model.JournalNote
import com.myowntrip.app.domain.model.JournalPieceType
import com.myowntrip.app.ui.components.EyebrowLabel
import com.myowntrip.app.ui.components.EyebrowLabelSize
import com.myowntrip.app.ui.theme.MOTSpacing
import java.io.File
import java.text.DateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SpreadPiece(
  note: JournalNote,
  eventBlock: ItineraryBlock?,
  modifier: Modifier = Modifier,
  rotationDegrees: Float = 0f,
) {
  val pieceLabel = pieceEyebrow(note)
  Column(
    modifier = modifier
      .fillMaxWidth()
      .rotate(rotationDegrees)
      .semantics { contentDescription = "Recuerdo del spread" },
  ) {
    EyebrowLabel(text = pieceLabel, size = EyebrowLabelSize.Small)
    when (effectivePieceType(note)) {
      JournalPieceType.EVENT_BOOKMARK -> EventBookmarkPiece(note, eventBlock)
      JournalPieceType.PHOTO -> PhotoSpreadPiece(note)
      JournalPieceType.AUDIO -> AudioSpreadPiece(note)
      JournalPieceType.SKETCH -> SketchSpreadPiece(note)
      JournalPieceType.STICKER -> StickerSpreadPiece(note)
      JournalPieceType.TEXT -> TextSpreadPiece(note)
    }
  }
}

@Composable
private fun PhotoSpreadPiece(note: JournalNote) {
  Column(modifier = Modifier.padding(top = MOTSpacing.componentSm)) {
    note.photoUri?.let { path ->
      SpreadMediaImage(
        path = path,
        contentDescription = "Foto del recuerdo",
        contentScale = ContentScale.Crop,
        placeholderLabel = "Foto",
        modifier = Modifier
          .fillMaxWidth()
          .height(220.dp)
          .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomEnd = 4.dp, bottomStart = 16.dp)),
      )
    }
    if (note.text.isNotBlank()) {
      Text(
        text = note.text,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(top = MOTSpacing.componentSm),
      )
    }
    SpreadMetaRow(note)
  }
}

@Composable
private fun TextSpreadPiece(note: JournalNote) {
  Text(
    text = note.text,
    style = MaterialTheme.typography.bodyLarge,
    modifier = Modifier.padding(top = MOTSpacing.componentSm),
  )
  SpreadMetaRow(note)
}

@Composable
private fun AudioSpreadPiece(note: JournalNote) {
  Surface(
    modifier = Modifier
      .padding(top = MOTSpacing.componentSm)
      .fillMaxWidth(0.72f),
    shape = RoundedCornerShape(20.dp),
    color = MaterialTheme.colorScheme.secondaryContainer,
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primary) {
        Box(modifier = Modifier.size(32.dp), contentAlignment = Alignment.Center) {
          androidx.compose.material3.Icon(
            Icons.Default.Mic,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(18.dp),
          )
        }
      }
      Column {
        Text("Nota de voz", style = MaterialTheme.typography.labelMedium)
        if (note.text.isNotBlank()) {
          Text(note.text, style = MaterialTheme.typography.bodySmall)
        }
      }
    }
  }
  SpreadMetaRow(note)
}

@Composable
private fun SketchSpreadPiece(note: JournalNote) {
  note.sketchUri?.let { path ->
    SpreadMediaImage(
      path = path,
      contentDescription = "Boceto del recuerdo",
      contentScale = ContentScale.Fit,
      placeholderLabel = "Boceto",
      modifier = Modifier
        .padding(top = MOTSpacing.componentSm)
        .fillMaxWidth(0.85f)
        .height(180.dp)
        .border(
          width = 1.dp,
          color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.32f),
          shape = RoundedCornerShape(8.dp),
        )
        .clip(RoundedCornerShape(8.dp))
        .background(MaterialTheme.colorScheme.surface),
    )
  }
  SpreadMetaRow(note)
}

@Composable
private fun StickerSpreadPiece(note: JournalNote) {
  val imagePath = note.stickerUri ?: note.photoUri
  imagePath?.let { path ->
    Box(
      modifier = Modifier
        .padding(top = MOTSpacing.componentSm)
        .size(120.dp),
    ) {
      Surface(
        modifier = Modifier
          .size(112.dp)
          .offset(x = 4.dp, y = 6.dp)
          .rotate(-2f),
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 4.dp,
        color = Color.White,
      ) {
        SpreadMediaImage(
          path = path,
          contentDescription = "Sticker de recuerdo",
          contentScale = ContentScale.Crop,
          placeholderLabel = "Sticker",
          modifier = Modifier
            .padding(6.dp)
            .clip(RoundedCornerShape(8.dp)),
        )
      }
    }
  }
  if (note.text.isNotBlank()) {
    Text(
      text = note.text,
      style = MaterialTheme.typography.bodyMedium,
      modifier = Modifier.padding(top = MOTSpacing.componentSm),
    )
  }
  SpreadMetaRow(note)
}

@Composable
private fun EventBookmarkPiece(note: JournalNote, block: ItineraryBlock?) {
  Column(
    modifier = Modifier
      .padding(top = MOTSpacing.componentSm)
      .fillMaxWidth()
      .border(
        width = 1.dp,
        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.28f),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomEnd = 16.dp, bottomStart = 4.dp),
      )
      .padding(MOTSpacing.layoutMd),
  ) {
    val title = block?.title ?: "Evento del plan"
    val time = block?.timeLabel?.let { " · $it" }.orEmpty()
    EyebrowLabel(text = "$title$time")
    when {
      note.photoUri != null -> {
        SpreadMediaImage(
          path = note.photoUri,
          contentDescription = "Foto del evento",
          contentScale = ContentScale.Crop,
          placeholderLabel = "Foto del evento",
          modifier = Modifier
            .padding(top = MOTSpacing.componentSm)
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(8.dp)),
        )
      }
      note.audioUri != null -> AudioSpreadPiece(note.copy(pieceType = JournalPieceType.AUDIO))
      note.text.isNotBlank() -> Text(
        text = note.text,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(top = MOTSpacing.componentSm),
      )
    }
    if (note.photoUri != null && note.text.isNotBlank()) {
      Text(
        text = note.text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(top = MOTSpacing.componentSm),
      )
    }
    SpreadMetaRow(note)
  }
}

@Composable
private fun SpreadMetaRow(note: JournalNote) {
  val time = DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(note.createdAt))
  Text(
    text = time,
    style = MaterialTheme.typography.labelSmall,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
    modifier = Modifier.padding(top = MOTSpacing.componentXs),
  )
}

private fun pieceEyebrow(note: JournalNote): String {
  val time = DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(note.createdAt))
  val typeLabel = when (effectivePieceType(note)) {
    JournalPieceType.PHOTO -> "Foto"
    JournalPieceType.AUDIO -> "Voz"
    JournalPieceType.TEXT -> "Nota"
    JournalPieceType.SKETCH -> "Boceto"
    JournalPieceType.STICKER -> "Sticker"
    JournalPieceType.EVENT_BOOKMARK -> "Evento"
  }
  return "$typeLabel · $time"
}

private fun effectivePieceType(note: JournalNote): JournalPieceType {
  if (note.pieceType == JournalPieceType.EVENT_BOOKMARK) return JournalPieceType.EVENT_BOOKMARK
  if (note.sketchUri != null) return JournalPieceType.SKETCH
  if (note.stickerUri != null) return JournalPieceType.STICKER
  if (note.photoUri != null && note.pieceType != JournalPieceType.AUDIO) return JournalPieceType.PHOTO
  if (note.audioUri != null) return JournalPieceType.AUDIO
  return note.pieceType
}

/** Rotación sutil alternada por índice — sensación de spread pegado a mano. */
fun spreadPieceRotation(index: Int): Float = when (index % 4) {
  0 -> -1.2f
  1 -> 0.8f
  2 -> -0.6f
  else -> 1.4f
}

@Composable
internal fun SpreadMediaImage(
  path: String,
  contentDescription: String,
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.Crop,
  placeholderLabel: String = "Imagen",
) {
  if (File(path).exists()) {
    AsyncImage(
      model = File(path),
      contentDescription = contentDescription,
      contentScale = contentScale,
      modifier = modifier,
    )
  } else {
    SpreadMediaPlaceholder(label = placeholderLabel, modifier = modifier)
  }
}

@Composable
internal fun SpreadMediaPlaceholder(
  label: String,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainerHigh),
    contentAlignment = Alignment.Center,
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
  }
}
