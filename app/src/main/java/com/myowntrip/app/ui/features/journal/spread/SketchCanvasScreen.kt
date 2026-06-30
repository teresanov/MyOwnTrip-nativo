package com.myowntrip.app.ui.features.journal.spread

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTSpacing

data class SketchStroke(
  val path: Path,
  val color: Color,
  val strokeWidth: Float,
  val isEraser: Boolean = false,
)

private enum class SketchTool { BRUSH, ERASER }

/** Tamaños de trazo (dp lógicos) ofrecidos en la barra. */
private val SketchSizesDp = listOf(2f, 4f, 8f, 16f)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SketchCanvasScreen(
  onBack: () -> Unit,
  onSaved: () -> Unit,
  viewModel: SketchCanvasViewModel = hiltViewModel(),
) {
  val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()
  val strokes = remember { mutableStateListOf<SketchStroke>() }
  var canvasSize by remember { mutableStateOf(IntSize.Zero) }
  val surfaceColor = MaterialTheme.colorScheme.surface

  SketchCanvasScaffold(
    isSaving = isSaving,
    canSave = strokes.isNotEmpty(),
    onBack = onBack,
    onSave = {
      viewModel.saveSketch(
        strokes = strokes.toList(),
        canvasSize = canvasSize,
        backgroundColor = surfaceColor,
        onSaved = onSaved,
      )
    },
  ) {
    SketchCanvasEditor(
      strokes = strokes,
      onCanvasSizeChanged = { canvasSize = it },
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SketchCanvasScaffold(
  isSaving: Boolean,
  canSave: Boolean,
  onBack: () -> Unit,
  onSave: () -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit,
) {
  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        title = { Text("Boceto") },
        navigationIcon = {
          MOTIconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
          }
        },
        actions = {
          MOTIconButton(
            onClick = onSave,
            enabled = canSave && !isSaving,
          ) {
            Icon(Icons.Default.Save, contentDescription = "Guardar boceto")
          }
        },
      )
    },
  ) { padding ->
    Column(
      modifier = Modifier
        .padding(padding)
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.surface),
    ) {
      content()
    }
  }
}

@Composable
fun SketchCanvasEditor(
  strokes: MutableList<SketchStroke>,
  onCanvasSizeChanged: (IntSize) -> Unit,
  modifier: Modifier = Modifier,
  initialTool: SketchToolSelection = SketchToolSelection.Brush,
) {
  val surfaceColor = MaterialTheme.colorScheme.surface
  val density = LocalDensity.current

  val palette = sketchPalette()
  val redo = remember { mutableStateListOf<SketchStroke>() }
  var tool by remember { mutableStateOf(if (initialTool == SketchToolSelection.Eraser) SketchTool.ERASER else SketchTool.BRUSH) }
  var color by remember { mutableStateOf(palette.first().color) }
  var sizeDp by remember { mutableFloatStateOf(4f) }
  var currentPath by remember { mutableStateOf<Path?>(null) }
  var lastPoint by remember { mutableStateOf(Offset.Zero) }

  val activeColor = if (tool == SketchTool.ERASER) surfaceColor else color
  val activeWidthPx = with(density) { (if (tool == SketchTool.ERASER) sizeDp * 2.5f else sizeDp).dp.toPx() }

  Column(modifier = modifier.fillMaxSize()) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .onSizeChanged { size -> onCanvasSizeChanged(IntSize(size.width, size.height)) }
        .semantics { contentDescription = "Lienzo de boceto" },
    ) {
      Canvas(
        modifier = Modifier
          .fillMaxSize()
          .pointerInput(tool, color, sizeDp) {
            detectDragGestures(
              onDragStart = { offset ->
                redo.clear()
                currentPath = Path().apply { moveTo(offset.x, offset.y) }
                lastPoint = offset
              },
              onDrag = { change, _ ->
                change.consume()
                val pos = change.position
                // Suaviza con curva cuadrática hasta el punto medio (look manuscrito).
                val mid = Offset((lastPoint.x + pos.x) / 2f, (lastPoint.y + pos.y) / 2f)
                currentPath?.quadraticBezierTo(lastPoint.x, lastPoint.y, mid.x, mid.y)
                lastPoint = pos
              },
              onDragEnd = {
                currentPath?.let { path ->
                  path.lineTo(lastPoint.x, lastPoint.y)
                  strokes.add(
                    SketchStroke(
                      path = path,
                      color = activeColor,
                      strokeWidth = activeWidthPx,
                      isEraser = tool == SketchTool.ERASER,
                    ),
                  )
                }
                currentPath = null
              },
            )
          },
      ) {
        strokes.forEach { stroke ->
          drawPath(
            path = stroke.path,
            color = stroke.color,
            style = Stroke(
              width = stroke.strokeWidth,
              cap = StrokeCap.Round,
              join = StrokeJoin.Round,
            ),
          )
        }
        currentPath?.let { path ->
          drawPath(
            path = path,
            color = activeColor,
            style = Stroke(width = activeWidthPx, cap = StrokeCap.Round, join = StrokeJoin.Round),
          )
        }
      }
    }

    SketchToolbar(
      tool = tool,
      onToolChange = { tool = it },
      palette = palette,
      selectedColor = color,
      onColorSelected = {
        color = it
        tool = SketchTool.BRUSH
      },
      sizeDp = sizeDp,
      onSizeSelected = { sizeDp = it },
      canUndo = strokes.isNotEmpty(),
      canRedo = redo.isNotEmpty(),
      onUndo = { if (strokes.isNotEmpty()) redo.add(strokes.removeAt(strokes.lastIndex)) },
      onRedo = { if (redo.isNotEmpty()) strokes.add(redo.removeAt(redo.lastIndex)) },
      onClear = {
        while (strokes.isNotEmpty()) redo.add(strokes.removeAt(strokes.lastIndex))
      },
    )
  }
}

enum class SketchToolSelection { Brush, Eraser }

private data class SketchColor(val label: String, val color: Color)

@Composable
private fun sketchPalette(): List<SketchColor> = listOf(
  SketchColor("Tinta", MaterialTheme.colorScheme.onSurface),
  SketchColor("Ocre", MaterialTheme.colorScheme.tertiary),
  SketchColor("Rojo", Color(0xFFC2412D)),
  SketchColor("Naranja", Color(0xFFD9852B)),
  SketchColor("Verde", Color(0xFF2E7D52)),
  SketchColor("Azul", Color(0xFF2D5BC2)),
  SketchColor("Rosa", Color(0xFFC2417F)),
)

@Composable
private fun SketchToolbar(
  tool: SketchTool,
  onToolChange: (SketchTool) -> Unit,
  palette: List<SketchColor>,
  selectedColor: Color,
  onColorSelected: (Color) -> Unit,
  sizeDp: Float,
  onSizeSelected: (Float) -> Unit,
  canUndo: Boolean,
  canRedo: Boolean,
  onUndo: () -> Unit,
  onRedo: () -> Unit,
  onClear: () -> Unit,
) {
  Surface(tonalElevation = 3.dp) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = MOTSpacing.screenHorizontal, vertical = MOTSpacing.componentSm),
      verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        FilledIconToggleButton(
          checked = tool == SketchTool.BRUSH,
          onCheckedChange = { onToolChange(SketchTool.BRUSH) },
          modifier = Modifier.semantics {
            contentDescription = "Pincel" + if (tool == SketchTool.BRUSH) ", activo" else ""
          },
        ) { Icon(Icons.Default.Brush, contentDescription = null) }
        FilledIconToggleButton(
          checked = tool == SketchTool.ERASER,
          onCheckedChange = { onToolChange(SketchTool.ERASER) },
          modifier = Modifier.semantics {
            contentDescription = "Goma de borrar" + if (tool == SketchTool.ERASER) ", activo" else ""
          },
        ) { Icon(Icons.Default.Delete, contentDescription = null) }

        Spacer(Modifier.weight(1f))

        IconButton(
          onClick = onUndo,
          enabled = canUndo,
          modifier = Modifier.semantics { contentDescription = "Deshacer" },
        ) { Icon(Icons.AutoMirrored.Filled.Undo, contentDescription = null) }
        IconButton(
          onClick = onRedo,
          enabled = canRedo,
          modifier = Modifier.semantics { contentDescription = "Rehacer" },
        ) { Icon(Icons.AutoMirrored.Filled.Redo, contentDescription = null) }
        IconButton(
          onClick = onClear,
          enabled = canUndo,
          modifier = Modifier.semantics { contentDescription = "Borrar todo" },
        ) { Icon(Icons.Default.Delete, contentDescription = null) }
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        palette.forEach { swatch ->
          val isSelected = tool == SketchTool.BRUSH && swatch.color == selectedColor
          Box(
            modifier = Modifier
              .size(30.dp)
              .clip(CircleShape)
              .background(swatch.color, CircleShape)
              .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.outlineVariant,
                shape = CircleShape,
              )
              .clickable { onColorSelected(swatch.color) }
              .semantics {
                contentDescription = "Color ${swatch.label}" + if (isSelected) ", seleccionado" else ""
              },
          )
        }
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
      ) {
        SketchSizesDp.forEach { size ->
          val isSelected = size == sizeDp
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(
                if (isSelected) MaterialTheme.colorScheme.secondaryContainer
                else MaterialTheme.colorScheme.surfaceVariant,
                CircleShape,
              )
              .clickable { onSizeSelected(size) }
              .semantics {
                contentDescription = "Grosor ${size.toInt()}" + if (isSelected) ", seleccionado" else ""
              },
            contentAlignment = Alignment.Center,
          ) {
            Box(
              Modifier
                .size((size + 4f).dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface, CircleShape),
            )
          }
        }
      }
    }
  }
}

private fun previewSampleStrokes(ink: Color): List<SketchStroke> {
  val path = Path().apply {
    moveTo(40f, 120f)
    cubicTo(80f, 40f, 160f, 200f, 280f, 80f)
    lineTo(300f, 160f)
  }
  return listOf(
    SketchStroke(path = path, color = ink, strokeWidth = 6f),
  )
}

@Preview(name = "Boceto · lienzo", showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun SketchCanvasScreenPreview() {
  JournalSpreadPreviewTheme {
    JournalSpreadPreviewFrame(height = 640.dp) {
      val strokes = remember {
        mutableStateListOf<SketchStroke>().apply {
          addAll(previewSampleStrokes(Color(0xFF825513)))
        }
      }
      SketchCanvasScaffold(
        isSaving = false,
        canSave = true,
        onBack = {},
        onSave = {},
        modifier = Modifier.fillMaxSize(),
      ) {
        SketchCanvasEditor(
          strokes = strokes,
          onCanvasSizeChanged = {},
        )
      }
    }
  }
}
