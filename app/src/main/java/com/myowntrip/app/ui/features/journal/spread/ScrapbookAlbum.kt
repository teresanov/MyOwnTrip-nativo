package com.myowntrip.app.ui.features.journal.spread

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.compose.ui.tooling.preview.Preview
import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.ItineraryBlock
import com.myowntrip.app.domain.model.JournalCanvas
import com.myowntrip.app.domain.model.JournalNote
import com.myowntrip.app.domain.model.JournalPage
import com.myowntrip.app.domain.model.JournalPieceType
import com.myowntrip.app.domain.model.JournalTextStyle
import com.myowntrip.app.ui.brand.BrandColors
import com.myowntrip.app.ui.brand.BrandRibbon
import com.myowntrip.app.ui.theme.FrauncesFamily
import com.myowntrip.app.ui.theme.HandFamily
import com.myowntrip.app.ui.theme.InterFamily
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

private val Hair = Color(0xFFDED7C8)
private val Print = Color(0xFFFCFBF7)
private val PostItPaper = Color(0xFFF2E7C9)
private val AlbumBackground = Color(0xFFE7E0D6)
private val SpanishSheetDate = DateTimeFormatter.ofPattern("EEEE d MMM", Locale("es", "ES"))
private val SpanishStampDate = DateTimeFormatter.ofPattern("d · MMM", Locale("es", "ES"))

/** Transform persistido al soltar una pieza, en coordenadas del lienzo lógico (dp). */
typealias OnPersistTransform = (
  noteId: String,
  canvasX: Float,
  canvasY: Float,
  rotation: Float,
  scale: Float,
  zIndex: Int,
) -> Unit

/**
 * Álbum del Diario: hojas que se pasan (HorizontalPager). Cada hoja es un lienzo
 * lógico fijo (360×N dp) escalado al dispositivo, con piezas colocadas libremente,
 * arrastrables y con snap magnético a la rejilla de 18 dp lógicos.
 */
@Composable
fun ScrapbookAlbum(
  pages: List<AlbumPageUi>,
  modifier: Modifier = Modifier,
  readOnly: Boolean = false,
  initialPageId: String? = null,
  onPersistTransform: OnPersistTransform = { _, _, _, _, _, _ -> },
  onPieceClick: (String) -> Unit = {},
  onAddSheet: (dayId: String) -> Unit = {},
  onActivePageChange: (AlbumPageUi) -> Unit = {},
) {
  if (pages.isEmpty()) {
    AlbumEmptyState(modifier)
    return
  }

  val initialIndex = remember(initialPageId, pages) {
    pages.indexOfFirst { it.pageId == initialPageId }.takeIf { it >= 0 } ?: 0
  }
  val pagerState = rememberPagerState(initialPage = initialIndex, pageCount = { pages.size })
  val scope = rememberCoroutineScope()
  var readingPageId by remember { mutableStateOf<String?>(null) }

  val current = pages[pagerState.currentPage.coerceIn(0, pages.lastIndex)]
  LaunchedEffect(current.pageId) { onActivePageChange(current) }

  val readingPage = readingPageId?.let { id -> pages.firstOrNull { it.pageId == id } }
  if (readingPage != null) {
    AlbumReadingView(
      page = readingPage,
      onPieceClick = onPieceClick,
      onClose = { readingPageId = null },
      modifier = modifier,
    )
    return
  }

  val context = LocalContext.current
  var showCoach by remember {
    mutableStateOf(!readOnly && !hasSeenAlbumCoach(context))
  }

  Box(modifier.fillMaxSize()) {
    Column(Modifier.fillMaxSize().background(AlbumBackground)) {
      if (!readOnly) AlbumHintBar()
      HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize().weight(1f),
        contentPadding = PaddingValues(horizontal = 16.dp),
        pageSpacing = 12.dp,
      ) { i ->
        AlbumSheet(
          page = pages[i],
          draggable = !readOnly,
          onPersistTransform = onPersistTransform,
          onPieceClick = onPieceClick,
        )
      }

      AlbumChrome(
        pages = pages,
        current = current,
        readOnly = readOnly,
        onJumpToDay = { dayId ->
          val target = pages.indexOfFirst { it.day.id == dayId }
          if (target >= 0) scope.launch { pagerState.animateScrollToPage(target) }
        },
        onOpenReading = { readingPageId = current.pageId },
        onAddSheet = { onAddSheet(current.day.id) },
      )
    }

    if (showCoach) {
      AlbumCoachOverlay(
        onDismiss = {
          markAlbumCoachSeen(context)
          showCoach = false
        },
      )
    }
  }
}

/* ----------------------------- tips de uso ----------------------------- */

private const val ALBUM_HINTS_PREFS = "journal_album_hints"
private const val ALBUM_COACH_SEEN_KEY = "coach_seen_v1"

private fun hasSeenAlbumCoach(context: Context): Boolean =
  context.getSharedPreferences(ALBUM_HINTS_PREFS, Context.MODE_PRIVATE)
    .getBoolean(ALBUM_COACH_SEEN_KEY, false)

private fun markAlbumCoachSeen(context: Context) {
  context.getSharedPreferences(ALBUM_HINTS_PREFS, Context.MODE_PRIVATE)
    .edit()
    .putBoolean(ALBUM_COACH_SEEN_KEY, true)
    .apply()
}

/** Línea de ayuda discreta y permanente sobre el lienzo editable. */
@Composable
private fun AlbumHintBar() {
  Row(
    Modifier
      .fillMaxWidth()
      .padding(horizontal = 18.dp, vertical = 6.dp)
      .clearAndSetSemantics {
        contentDescription = "Toca un recuerdo para abrirlo; mantén pulsado y arrastra para moverlo."
      },
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      "Toca para abrir · mantén pulsado para mover",
      fontFamily = InterFamily,
      fontSize = 11.sp,
      letterSpacing = 0.3.sp,
      color = BrandColors.Ink.copy(alpha = 0.55f),
    )
  }
}

/** Coachmark de bienvenida (solo la primera vez): explica el modelo de interacción. */
@Composable
private fun AlbumCoachOverlay(onDismiss: () -> Unit) {
  Box(
    Modifier
      .fillMaxSize()
      .background(Color.Black.copy(alpha = 0.55f))
      .pointerInput(Unit) { detectTapGestures { onDismiss() } },
    contentAlignment = Alignment.Center,
  ) {
    Column(
      Modifier
        .padding(horizontal = 32.dp)
        .clip(RoundedCornerShape(18.dp))
        .background(BrandColors.Paper)
        .padding(24.dp)
        // El tap del scrim no debe cerrar al pulsar dentro de la tarjeta.
        .pointerInput(Unit) { detectTapGestures { } },
      verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
      Text(
        "Tu álbum, a tu manera",
        fontFamily = FrauncesFamily, fontWeight = FontWeight.Medium,
        fontSize = 22.sp, color = BrandColors.Ink,
      )
      CoachLine("Toca", "un recuerdo para abrirlo o editarlo.")
      CoachLine("Mantén pulsado", "y arrastra para moverlo por la hoja.")
      CoachLine("Pulsa +", "para añadir recuerdos o una hoja nueva.")
      Box(
        Modifier
          .align(Alignment.End)
          .clip(RoundedCornerShape(50))
          .background(BrandColors.Ink)
          .clickable { onDismiss() }
          .padding(horizontal = 22.dp, vertical = 10.dp)
          .semantics { contentDescription = "Entendido, cerrar la ayuda" },
      ) {
        Text("Entendido", fontFamily = InterFamily, fontSize = 14.sp, color = Color.White)
      }
    }
  }
}

@Composable
private fun CoachLine(lead: String, rest: String) {
  Row(verticalAlignment = Alignment.Top) {
    Box(
      Modifier
        .padding(top = 7.dp, end = 10.dp)
        .size(6.dp)
        .clip(CircleShape)
        .background(BrandColors.AccentOcre),
    )
    Text(
      buildString { append(lead); append(" "); append(rest) },
      fontFamily = InterFamily, fontSize = 14.sp, lineHeight = 20.sp,
      color = BrandColors.Ink.copy(alpha = 0.85f),
    )
  }
}

/* ----------------------------- Hoja ----------------------------- */

@Composable
private fun AlbumSheet(
  page: AlbumPageUi,
  draggable: Boolean,
  onPersistTransform: OnPersistTransform,
  onPieceClick: (String) -> Unit,
) {
  Column(
    Modifier
      .fillMaxSize()
      .padding(vertical = 12.dp)
      .shadow(10.dp, RoundedCornerShape(18.dp))
      .clip(RoundedCornerShape(18.dp))
      .background(BrandColors.Paper),
  ) {
    AlbumMasthead(page)
    AlbumCanvas(
      page = page,
      draggable = draggable,
      onPersistTransform = onPersistTransform,
      onPieceClick = onPieceClick,
    )
  }
}

@Composable
private fun AlbumMasthead(page: AlbumPageUi) {
  val day = page.day
  Column(Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 8.dp)) {
    if (page.isChapterStart) {
      Text(
        "DÍA ${day.dayNumber}",
        fontFamily = FrauncesFamily, fontWeight = FontWeight.Medium,
        fontSize = 11.sp, letterSpacing = 1.8.sp, color = BrandColors.AccentOcre,
      )
      Text(
        day.title?.takeIf { it.isNotBlank() } ?: day.date.format(SpanishSheetDate),
        fontFamily = FrauncesFamily, fontWeight = FontWeight.Medium,
        fontSize = 26.sp, color = BrandColors.Ink,
      )
      Text(
        day.date.format(SpanishSheetDate),
        fontSize = 12.sp, color = BrandColors.Ink.copy(alpha = 0.75f),
      )
    } else {
      val place = day.title?.takeIf { it.isNotBlank() } ?: "Día ${day.dayNumber}"
      Text(
        "$place · hoja ${page.pageIndexInDay + 1}",
        fontFamily = FrauncesFamily, fontStyle = FontStyle.Italic,
        fontSize = 16.sp, color = BrandColors.Ink.copy(alpha = 0.7f),
      )
    }
  }
}

@Composable
private fun AlbumCanvas(
  page: AlbumPageUi,
  draggable: Boolean,
  onPersistTransform: OnPersistTransform,
  onPieceClick: (String) -> Unit,
) {
  val autoFlow = remember(page.notes) { autoFlowPositions(page.notes) }
  val pageMaxZ = remember(page.notes) { page.notes.maxOfOrNull { it.zIndex } ?: 0 }
  val blockById = remember(page.notes) { emptyMap<String, ItineraryBlock>() }

  BoxWithConstraints(
    Modifier
      .fillMaxSize()
      .clipToBounds()
      .semantics { contentDescription = "Hoja del diario, día ${page.day.dayNumber}" },
  ) {
    val density = LocalDensity.current
    val logicalWidthPx = with(density) { JournalCanvas.LOGICAL_WIDTH_DP.dp.toPx() }
    val factor = if (logicalWidthPx > 0f) constraints.maxWidth / logicalWidthPx else 1f
    val logicalHeightDp = with(density) { (constraints.maxHeight / factor).toDp() }

    if (page.notes.isEmpty()) {
      Column(
        Modifier.align(Alignment.Center).padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Text(
          "Esta hoja está en blanco.",
          fontFamily = FrauncesFamily, fontSize = 18.sp, color = BrandColors.Ink,
        )
        Text(
          "Pega tu primer recuerdo con +.",
          fontSize = 13.sp, color = BrandColors.Ink.copy(alpha = 0.6f),
        )
      }
    }

    Box(
      Modifier
        .width(JournalCanvas.LOGICAL_WIDTH_DP.dp)
        .height(logicalHeightDp)
        .graphicsLayer {
          scaleX = factor
          scaleY = factor
          transformOrigin = TransformOrigin(0f, 0f)
        }
        .drawBehind {
          val step = JournalCanvas.GRID_STEP_DP.dp.toPx()
          val r = 1.1.dp.toPx()
          var y = 8.dp.toPx()
          while (y < size.height) {
            var x = 8.dp.toPx()
            while (x < size.width) {
              drawCircle(Hair, r, Offset(x, y))
              x += step
            }
            y += step
          }
        },
    ) {
      SheetDecorations(page)
      page.notes.forEach { note ->
        val pos = note.logicalPosition() ?: autoFlow[note.id] ?: Offset.Zero
        PlacedPiece(
          note = note,
          initialPosition = pos,
          presentationRotation = note.presentationRotation(),
          draggable = draggable,
          pageMaxZ = pageMaxZ,
          onPersistTransform = onPersistTransform,
          onClick = { onPieceClick(note.id) },
        ) {
          ScrapPieceContent(note = note, block = note.planBlockId?.let(blockById::get))
        }
      }
    }
  }
}

/* ------------------------- pieza arrastrable ------------------------- */

@Composable
private fun PlacedPiece(
  note: JournalNote,
  initialPosition: Offset,
  presentationRotation: Float,
  draggable: Boolean,
  pageMaxZ: Int,
  onPersistTransform: OnPersistTransform,
  onClick: () -> Unit,
  content: @Composable () -> Unit,
) {
  val density = LocalDensity.current
  val haptics = LocalHapticFeedback.current
  var pos by remember(note.id) { mutableStateOf(initialPosition) }
  var z by remember(note.id) { mutableFloatStateOf(note.zIndex.toFloat()) }
  var lifted by remember(note.id) { mutableStateOf(false) }
  val liftScale by animateFloatAsState(if (lifted) 1.06f else 1f, label = "pieceLift")

  Box(
    Modifier
      .zIndex(if (lifted) Float.MAX_VALUE else z)
      .offset {
        IntOffset(
          with(density) { pos.x.dp.roundToPx() },
          with(density) { pos.y.dp.roundToPx() },
        )
      }
      .graphicsLayer {
        rotationZ = presentationRotation
        scaleX = note.scale * liftScale
        scaleY = note.scale * liftScale
      }
      // Mover: mantener pulsado para "coger" la pieza y luego arrastrar. Va en su
      // propio pointerInput para no competir con el tap; el HorizontalPager cede
      // porque el hijo consume el gesto tras el long-press.
      .then(
        if (draggable) {
          Modifier.pointerInput(note.id) {
            detectDragGesturesAfterLongPress(
              onDragStart = {
                lifted = true
                z = (pageMaxZ + 1).toFloat()
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
              },
              onDrag = { change, drag ->
                change.consume()
                val dxDp = with(density) { drag.x.toDp() }.value
                val dyDp = with(density) { drag.y.toDp() }.value
                pos = Offset(pos.x + dxDp, pos.y + dyDp)
              },
              onDragEnd = {
                lifted = false
                val snapped = Offset(snapToGrid(pos.x), snapToGrid(pos.y))
                pos = snapped
                onPersistTransform(
                  note.id, snapped.x, snapped.y, presentationRotation, note.scale, z.roundToInt(),
                )
              },
              onDragCancel = { lifted = false },
            )
          }
        } else {
          Modifier
        },
      )
      .pointerInput(note.id) {
        detectTapGestures(onTap = { onClick() })
      },
  ) { content() }
}

/* ----------------------------- piezas ----------------------------- */

private fun albumPieceType(note: JournalNote): JournalPieceType = when {
  note.pieceType == JournalPieceType.EVENT_BOOKMARK -> JournalPieceType.EVENT_BOOKMARK
  note.sketchUri != null -> JournalPieceType.SKETCH
  note.stickerUri != null -> JournalPieceType.STICKER
  note.photoUri != null && note.pieceType != JournalPieceType.AUDIO -> JournalPieceType.PHOTO
  note.audioUri != null -> JournalPieceType.AUDIO
  else -> note.pieceType
}

@Composable
private fun ScrapPieceContent(note: JournalNote, block: ItineraryBlock?) {
  when (albumPieceType(note)) {
    JournalPieceType.EVENT_BOOKMARK -> EventPiece(note, block)
    JournalPieceType.PHOTO -> Polaroid(note)
    JournalPieceType.AUDIO -> AudioChip(note)
    JournalPieceType.SKETCH -> SketchPaper(note)
    JournalPieceType.STICKER -> StickerPiece(note)
    JournalPieceType.TEXT ->
      if (note.textStyle == JournalTextStyle.TYPED) PostIt(note.text) else HandNote(note.text)
  }
}

@Composable
private fun Polaroid(note: JournalNote) {
  Box {
    Column(
      Modifier
        .shadow(7.dp, RoundedCornerShape(2.dp))
        .background(Print, RoundedCornerShape(2.dp))
        .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 26.dp)
        .width(150.dp),
    ) {
      SpreadMediaImage(
        path = note.photoUri.orEmpty(),
        contentDescription = "Foto del recuerdo",
        contentScale = ContentScale.Crop,
        placeholderLabel = "Foto",
        modifier = Modifier
          .fillMaxWidth()
          .height(112.dp)
          .clip(RoundedCornerShape(1.dp)),
      )
      if (note.text.isNotBlank()) {
        Text(
          note.text,
          fontFamily = HandFamily, fontStyle = FontStyle.Italic, fontSize = 16.sp,
          color = BrandColors.Ink,
          modifier = Modifier.padding(top = 6.dp).align(Alignment.CenterHorizontally),
        )
      }
    }
    Box(
      Modifier
        .offset((-8).dp, (-6).dp)
        .graphicsLayer { rotationZ = -32f }
        .width(54.dp).height(18.dp)
        .background(BrandColors.AccentOcre.copy(alpha = 0.42f)),
    )
  }
}

@Composable
private fun StickerPiece(note: JournalNote) {
  // La forma (círculo/redondeado/marco) ya viene horneada en el PNG con transparencia.
  val path = note.stickerUri ?: note.photoUri
  SpreadMediaImage(
    path = path.orEmpty(),
    contentDescription = "Sticker de recuerdo",
    contentScale = ContentScale.Fit,
    placeholderLabel = "Sticker",
    modifier = Modifier.size(112.dp),
  )
}

@Composable
private fun PostIt(text: String) {
  Column(
    Modifier
      .shadow(5.dp, RoundedCornerShape(2.dp))
      .background(PostItPaper, RoundedCornerShape(2.dp))
      .width(150.dp)
      .padding(horizontal = 14.dp, vertical = 14.dp),
  ) {
    Text(
      text,
      fontFamily = InterFamily, fontSize = 14.sp, lineHeight = 20.sp,
      color = BrandColors.Ink,
    )
  }
}

@Composable
private fun HandNote(text: String) {
  Box(Modifier.width(160.dp)) {
    Text(
      text,
      fontFamily = HandFamily, fontStyle = FontStyle.Italic, fontSize = 17.sp,
      color = BrandColors.Ink, lineHeight = 22.sp,
    )
  }
}

@Composable
private fun AudioChip(note: JournalNote) {
  Column(Modifier.width(170.dp)) {
    Row(
      Modifier
        .shadow(4.dp, RoundedCornerShape(20.dp))
        .background(Print, RoundedCornerShape(20.dp))
        .border(1.dp, Hair, RoundedCornerShape(20.dp))
        .padding(start = 8.dp, end = 12.dp, top = 7.dp, bottom = 7.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      Box(
        Modifier.size(26.dp).border(1.5.dp, BrandColors.Ink, CircleShape),
        contentAlignment = Alignment.Center,
      ) { Text("▶", fontSize = 10.sp, color = BrandColors.Ink) }
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.5.dp),
      ) {
        listOf(8, 16, 11, 20, 14, 18, 9, 15).forEach { h ->
          Box(
            Modifier.width(2.5.dp).height(h.dp)
              .background(BrandColors.Ink.copy(alpha = 0.6f), RoundedCornerShape(2.dp)),
          )
        }
      }
    }
    if (note.text.isNotBlank()) {
      Text(
        note.text,
        fontFamily = HandFamily, fontStyle = FontStyle.Italic, fontSize = 16.sp,
        color = BrandColors.Ink, modifier = Modifier.padding(top = 4.dp),
      )
    }
  }
}

@Composable
private fun SketchPaper(note: JournalNote) {
  SpreadMediaImage(
    path = note.sketchUri.orEmpty(),
    contentDescription = "Boceto del recuerdo",
    contentScale = ContentScale.Fit,
    placeholderLabel = "Boceto",
    modifier = Modifier
      .width(190.dp)
      .height(150.dp)
      .border(1.dp, BrandColors.AccentOcre.copy(alpha = 0.32f), RoundedCornerShape(8.dp))
      .clip(RoundedCornerShape(8.dp))
      .background(BrandColors.Paper),
  )
}

@Composable
private fun EventPiece(note: JournalNote, block: ItineraryBlock?) {
  Box {
    Column(
      Modifier
        .shadow(5.dp, RoundedCornerShape(3.dp))
        .background(Print, RoundedCornerShape(3.dp))
        .border(1.dp, Hair, RoundedCornerShape(3.dp))
        .padding(horizontal = 13.dp, vertical = 11.dp)
        .width(170.dp),
    ) {
      val title = block?.title ?: "Evento del plan"
      val label = block?.timeLabel?.let { "$title · $it" } ?: title
      Text(
        label.uppercase(Locale.getDefault()),
        fontSize = 10.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp,
        color = BrandColors.Ink,
      )
      when {
        note.photoUri != null -> SpreadMediaImage(
          path = note.photoUri,
          contentDescription = "Foto del evento",
          contentScale = ContentScale.Crop,
          placeholderLabel = "Foto",
          modifier = Modifier
            .padding(top = 6.dp)
            .fillMaxWidth()
            .height(110.dp)
            .clip(RoundedCornerShape(2.dp)),
        )
        note.audioUri != null -> Box(Modifier.padding(top = 6.dp)) {
          AudioChip(note)
        }
        note.text.isNotBlank() -> Text(
          note.text,
          fontFamily = FrauncesFamily, fontSize = 14.sp, color = BrandColors.Ink,
          lineHeight = 18.sp, modifier = Modifier.padding(top = 4.dp),
        )
      }
      if (note.photoUri != null && note.text.isNotBlank()) {
        Text(
          note.text,
          fontFamily = HandFamily, fontStyle = FontStyle.Italic, fontSize = 15.sp,
          color = BrandColors.Ink, modifier = Modifier.padding(top = 6.dp),
        )
      }
    }
    BrandRibbon(
      height = 26.dp,
      modifier = Modifier.align(Alignment.TopEnd).padding(end = 14.dp).offset(y = (-2).dp),
    )
  }
}

/* ----------------- decoración autogenerada (no editable) ----------------- */

/**
 * Sellos y flechas "a mano" generados desde los datos del día. Pura decoración:
 * detrás de las piezas y fuera del árbol de accesibilidad (el masthead ya nombra
 * día y lugar). Posiciones deterministas por hoja para que no "salten".
 */
@Composable
private fun SheetDecorations(page: AlbumPageUi) {
  Box(Modifier.fillMaxSize().clearAndSetSemantics { }) {
    if (page.isChapterStart) {
      val place = page.day.title?.takeIf { it.isNotBlank() }?.uppercase(Locale.getDefault())
        ?: "DÍA ${page.day.dayNumber}"
      Box(
        Modifier.offset(x = 214.dp, y = 14.dp).graphicsLayer { rotationZ = 7f },
      ) {
        Stamp(place, page.day.date.format(SpanishStampDate).uppercase(Locale.getDefault()))
      }
      DoodleArrow(
        Modifier
          .offset(x = 150.dp, y = 92.dp)
          .graphicsLayer { alpha = 0.55f; rotationZ = -6f },
      )
    }
  }
}

@Composable
private fun Stamp(city: String, date: String) {
  Column(
    Modifier
      .border(2.dp, BrandColors.AccentOcre, RoundedCornerShape(8.dp))
      .padding(3.dp)
      .border(1.dp, BrandColors.AccentOcre.copy(alpha = 0.25f), RoundedCornerShape(6.dp))
      .padding(horizontal = 14.dp, vertical = 10.dp)
      .graphicsLayer { alpha = 0.82f },
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      city,
      fontFamily = FrauncesFamily, fontWeight = FontWeight.SemiBold, fontSize = 15.sp,
      letterSpacing = 1.2.sp, color = BrandColors.AccentOcre,
    )
    Text(
      "$date · 2026",
      fontSize = 9.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.6.sp,
      color = BrandColors.AccentOcre,
    )
  }
}

@Composable
private fun DoodleArrow(modifier: Modifier) {
  Box(
    modifier.size(90.dp, 70.dp).drawBehind {
      val s = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
      val path = androidx.compose.ui.graphics.Path().apply {
        moveTo(5.dp.toPx(), 60.dp.toPx())
        cubicTo(
          30.dp.toPx(), 50.dp.toPx(),
          20.dp.toPx(), 15.dp.toPx(),
          55.dp.toPx(), 18.dp.toPx(),
        )
      }
      drawPath(path, BrandColors.Ink, style = s)
      val head = androidx.compose.ui.graphics.Path().apply {
        moveTo(48.dp.toPx(), 12.dp.toPx())
        lineTo(57.dp.toPx(), 18.dp.toPx())
        lineTo(49.dp.toPx(), 26.dp.toPx())
      }
      drawPath(head, BrandColors.Ink, style = s)
    },
  )
}

/* ----------------------------- chrome ----------------------------- */

@Composable
private fun AlbumChrome(
  pages: List<AlbumPageUi>,
  current: AlbumPageUi,
  readOnly: Boolean,
  onJumpToDay: (String) -> Unit,
  onOpenReading: () -> Unit,
  onAddSheet: () -> Unit,
) {
  val days = remember(pages) { pages.map { it.day }.distinctBy { it.id }.sortedBy { it.dayNumber } }
  Row(
    Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 12.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      "Día ${current.day.dayNumber} · ${current.pageIndexInDay + 1}/${current.pageCountInDay}",
      fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BrandColors.Ink,
    )
    Spacer(Modifier.width(12.dp))
    days.take(5).forEach { d ->
      val active = current.day.id == d.id
      Box(
        Modifier
          .padding(end = 6.dp)
          .clip(RoundedCornerShape(50))
          .background(if (active) BrandColors.Ink else Color.Transparent)
          .border(
            1.dp,
            BrandColors.Ink.copy(alpha = if (active) 0f else 0.35f),
            RoundedCornerShape(50),
          )
          .clickable { onJumpToDay(d.id) }
          .padding(horizontal = 10.dp, vertical = 6.dp),
      ) {
        Text(
          "Día ${d.dayNumber}",
          fontSize = 12.sp, fontWeight = FontWeight.Medium,
          color = if (active) Color.White else BrandColors.Ink,
        )
      }
    }
    Spacer(Modifier.weight(1f))
    Box(
      Modifier
        .size(40.dp)
        .clip(CircleShape)
        .border(1.dp, BrandColors.Ink.copy(alpha = 0.35f), CircleShape)
        .clickable { onOpenReading() }
        .semantics { contentDescription = "Ordenar y leer la hoja" },
      contentAlignment = Alignment.Center,
    ) { Text("≡", fontSize = 18.sp, color = BrandColors.Ink) }
    if (!readOnly) {
      Spacer(Modifier.width(8.dp))
      Box(
        Modifier
          .size(48.dp)
          .shadow(6.dp, CircleShape)
          .background(BrandColors.Ink, CircleShape)
          .clickable { onAddSheet() }
          .semantics { contentDescription = "Nueva hoja" },
        contentAlignment = Alignment.Center,
      ) { Text("+", color = Color.White, fontSize = 24.sp) }
    }
  }
}

/* --------------------- vista de lectura accesible --------------------- */

/**
 * "Ordenar": vista lineal de la hoja en orden estable (zIndex, createdAt), pensada
 * para TalkBack. Reutiliza los renderers de pieza de [SpreadPiece].
 */
@Composable
private fun AlbumReadingView(
  page: AlbumPageUi,
  onPieceClick: (String) -> Unit,
  onClose: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val ordered = remember(page.notes) {
    page.notes.sortedWith(compareBy({ it.zIndex }, { it.createdAt }))
  }
  Column(modifier.fillMaxSize().background(BrandColors.Paper)) {
    Row(
      Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        "Día ${page.day.dayNumber} · lectura",
        fontFamily = FrauncesFamily, fontSize = 18.sp, color = BrandColors.Ink,
        modifier = Modifier.weight(1f),
      )
      Box(
        Modifier
          .clip(RoundedCornerShape(50))
          .border(1.dp, BrandColors.Ink.copy(alpha = 0.35f), RoundedCornerShape(50))
          .clickable { onClose() }
          .padding(horizontal = 14.dp, vertical = 8.dp)
          .semantics { contentDescription = "Volver al lienzo" },
      ) { Text("Lienzo", fontSize = 13.sp, color = BrandColors.Ink) }
    }
    if (ordered.isEmpty()) {
      Text(
        "Esta hoja está en blanco.",
        fontFamily = FrauncesFamily, fontSize = 16.sp, color = BrandColors.Ink,
        modifier = Modifier.padding(18.dp),
      )
    } else {
      LazyColumn(
        contentPadding = PaddingValues(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        itemsIndexed(ordered, key = { _, n -> n.id }) { _, note ->
          SpreadPiece(
            note = note,
            eventBlock = null,
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onPieceClick(note.id) },
          )
        }
      }
    }
  }
}

@Composable
private fun AlbumEmptyState(modifier: Modifier) {
  Box(
    modifier.fillMaxSize().background(AlbumBackground),
    contentAlignment = Alignment.Center,
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        "Aún no hay diario.",
        fontFamily = FrauncesFamily, fontSize = 20.sp, color = BrandColors.Ink,
      )
      Text(
        "Empieza a pegar recuerdos con +.",
        fontSize = 13.sp, color = BrandColors.Ink.copy(alpha = 0.6f),
        modifier = Modifier.padding(top = 4.dp),
      )
    }
  }
}

/* ----------------------------- preview ----------------------------- */

@Preview(name = "Diario · álbum (datos)", showBackground = true, widthDp = 360, heightDp = 760)
@Composable
private fun ScrapbookAlbumDataPreview() {
  val day1 = Day("d1", "t1", LocalDate.of(2026, 6, 12), 3, "Madrid")
  val day2 = Day("d2", "t1", LocalDate.of(2026, 6, 13), 4, "Sevilla")
  val pagesDb = listOf(
    JournalPage("p1", "t1", "d1", 0, null),
    JournalPage("p2", "t1", "d1", 1, null),
    JournalPage("p3", "t1", "d2", 0, null),
  )
  val notes = listOf(
    JournalNote(
      id = "n1", dayId = "d1", text = "la luz de Goya ✶",
      pieceType = JournalPieceType.PHOTO, createdAt = 1_750_000_000_000,
      pageId = "p1", canvasX = 16f, canvasY = 18f, rotation = -4f,
    ),
    JournalNote(
      id = "n2", dayId = "d1", text = "Reservar mesa para mañana 21:00",
      pieceType = JournalPieceType.TEXT, textStyle = JournalTextStyle.TYPED,
      createdAt = 1_750_000_100_000,
      pageId = "p1", canvasX = 150f, canvasY = 230f, rotation = 2f,
    ),
    JournalNote(
      id = "n3", dayId = "d1", text = "Impresionante la colección permanente.",
      pieceType = JournalPieceType.EVENT_BOOKMARK, planBlockId = "b1",
      createdAt = 1_750_000_200_000, pageId = "p1", canvasX = 18f, canvasY = 320f, rotation = 1f,
    ),
    JournalNote(
      id = "n4", dayId = "d1", text = "Retiro al atardecer",
      pieceType = JournalPieceType.PHOTO, createdAt = 1_750_000_300_000,
      pageId = "p2", canvasX = 150f, canvasY = 18f, rotation = 5f,
    ),
  )
  val pages = buildAlbumPages(listOf(day1, day2), pagesDb, notes)
  JournalSpreadPreviewTheme {
    JournalSpreadPreviewFrame(height = 760.dp) {
      ScrapbookAlbum(pages = pages, modifier = Modifier.fillMaxSize())
    }
  }
}
