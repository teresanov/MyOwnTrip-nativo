package com.myowntrip.app.ui.features.plan

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.myowntrip.app.ui.components.date.MotTimeTextField
import com.myowntrip.app.ui.theme.MOTTextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.ItineraryBlock
import com.myowntrip.app.domain.model.WalletEntry
import com.myowntrip.app.domain.plan.DayPlanScheduleLogic
import com.myowntrip.app.domain.plan.PlanPlacementDriftLogic
import com.myowntrip.app.domain.plan.PlanPlacementLogic
import com.myowntrip.app.domain.plan.TimelineBlockLayout
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTSpacing
import kotlin.math.roundToInt

private val HourHeight: Dp = 56.dp
private val TimeColumnWidth: Dp = 52.dp

/**
 * Vista día con rejilla horaria (estilo agenda) y bloques posicionados.
 * Arrastra un bloque flexible para cambiar su hora en el cuadrante.
 */
@Composable
fun DayPlanSchedule(
  day: Day?,
  blocks: List<ItineraryBlock>,
  walletEntries: List<WalletEntry>,
  tripDays: List<Day>,
  readOnly: Boolean,
  onTimeChange: (blockId: String, time: String) -> Unit,
  onLinkWallet: (String) -> Unit,
  onWalletEntryClick: (String) -> Unit,
  onMoveToDay: (String) -> Unit,
  onRequestUpdatePlan: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  var displayBlocks by remember { mutableStateOf(blocks) }
  var draggingBlockId by remember { mutableStateOf<String?>(null) }
  LaunchedEffect(blocks, draggingBlockId) {
    if (draggingBlockId == null) {
      displayBlocks = blocks
    }
  }

  val layouts = remember(displayBlocks, walletEntries) {
    DayPlanScheduleLogic.timelineLayouts(displayBlocks, walletEntries)
  }
  val hourRange = remember(layouts) {
    DayPlanScheduleLogic.gridHourRange(displayBlocks, walletEntries)
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = MOTSpacing.screenHorizontal),
  ) {
    if (!readOnly) {
      val hasWalletLinked = layouts.any { it.linkedWallet != null }
      Text(
        text = if (hasWalletLinked) {
          "Mantén pulsado una actividad vinculada a Wallet — o usa el menú ⋮ — para corregir su hora o día."
        } else {
          "Arrastra una actividad en el cuadrante para cambiar su hora."
        },
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(vertical = MOTSpacing.componentSm),
      )
    }

    if (layouts.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(vertical = MOTSpacing.layoutLg),
        contentAlignment = Alignment.Center,
      ) {
        Text(
          text = if (readOnly) {
            "No había actividades planificadas para este día."
          } else {
            "Añade actividades con + para colocarlas en el cuadrante."
          },
          style = MaterialTheme.typography.bodyLarge,
        )
      }
      return
    }

    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val hourHeightPx = with(density) { HourHeight.toPx() }
    val gridHeight = HourHeight * (hourRange.last - hourRange.first + 1)

    Row(
      modifier = Modifier
        .weight(1f)
        .verticalScroll(scrollState, enabled = draggingBlockId == null),
    ) {
      DayTimelineHourLabels(
        hourRange = hourRange,
        gridHeight = gridHeight,
      )

      Box(
        modifier = Modifier
          .weight(1f)
          .height(gridHeight)
          .semantics { contentDescription = "Cuadrante horario del día" },
      ) {
        DayTimelineGridLines(hourRange = hourRange)

        layouts.forEach { layout ->
          val startHour = hourRange.first
          val topOffsetPx = ((layout.startMinutes - startHour * 60) / 60f) * hourHeightPx
          val blockHeight = with(density) {
            val base = (layout.durationMinutes / 60f * hourHeightPx).toDp()
            val minHeight = if (layout.linkedWallet != null) 68.dp else 44.dp
            base.coerceAtLeast(minHeight)
          }

          val isDraggingThis = draggingBlockId == layout.block.id
          TimelineGridEvent(
            layout = layout,
            day = day,
            tripDays = tripDays,
            readOnly = readOnly,
            topOffsetPx = topOffsetPx,
            blockHeight = blockHeight,
            hourHeightPx = hourHeightPx,
            gridStartHour = hourRange.first,
            isDragging = isDraggingThis,
            onDragStateChange = { dragging ->
              draggingBlockId = if (dragging) layout.block.id else null
            },
            onTimeChange = { blockId, time ->
              displayBlocks = displayBlocks.map {
                if (it.id == blockId) it.copy(timeLabel = time.ifBlank { null }) else it
              }
              onTimeChange(blockId, time)
            },
            onLinkWallet = { onLinkWallet(layout.block.id) },
            onWalletEntryClick = onWalletEntryClick,
            onMoveToDay = { onMoveToDay(layout.block.id) },
            onRequestUpdatePlan = { onRequestUpdatePlan(layout.block.id) },
            modifier = Modifier.zIndex(
              when {
                isDraggingThis -> 10f
                layout.isFixed -> 1f
                else -> 2f
              },
            ),
          )
        }
      }
    }
  }
}

@Composable
private fun DayTimelineHourLabels(hourRange: IntRange, gridHeight: Dp) {
  Column(
    modifier = Modifier
      .width(TimeColumnWidth)
      .height(gridHeight),
  ) {
    for (hour in hourRange) {
      Box(
        modifier = Modifier
          .height(HourHeight)
          .fillMaxWidth(),
        contentAlignment = Alignment.TopEnd,
      ) {
        Text(
          text = DayPlanScheduleLogic.hourLabel(hour),
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(end = 6.dp, top = 2.dp),
        )
      }
    }
  }
}

@Composable
private fun DayTimelineGridLines(hourRange: IntRange) {
  Column(modifier = Modifier.fillMaxSize()) {
    for (hour in hourRange) {
      Box(
        modifier = Modifier
          .height(HourHeight)
          .fillMaxWidth(),
      ) {
        HorizontalDivider(
          modifier = Modifier.align(Alignment.TopCenter),
          color = MaterialTheme.colorScheme.outlineVariant,
        )
        if (hour == hourRange.last) {
          HorizontalDivider(
            modifier = Modifier.align(Alignment.BottomCenter),
            color = MaterialTheme.colorScheme.outlineVariant,
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TimelineGridEvent(
  layout: TimelineBlockLayout,
  day: Day?,
  tripDays: List<Day>,
  readOnly: Boolean,
  topOffsetPx: Float,
  blockHeight: Dp,
  hourHeightPx: Float,
  gridStartHour: Int,
  isDragging: Boolean,
  onDragStateChange: (Boolean) -> Unit,
  onTimeChange: (blockId: String, time: String) -> Unit,
  onLinkWallet: () -> Unit,
  onWalletEntryClick: (String) -> Unit,
  onMoveToDay: () -> Unit,
  onRequestUpdatePlan: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val block = layout.block
  val linked = layout.linkedWallet
  val isWalletLinked = linked != null
  val canDrag = !readOnly && !isWalletLinked
  var dragOffsetPx by remember(block.id) { mutableFloatStateOf(0f) }
  var menuExpanded by remember { mutableStateOf(false) }
  var showTimeDialog by remember { mutableStateOf(false) }
  var editTimeValue by remember { mutableStateOf("") }

  val displayTopPx = topOffsetPx + if (isDragging) dragOffsetPx else 0f
  val timeLabel = block.timeLabel ?: DayPlanScheduleLogic.minutesToTimeLabel(layout.startMinutes)
  val sourceLabel = PlanPlacementDriftLogic.timeSourceLabel(block, linked, day)
  val scheduleMetaLabel = buildString {
    append(timeLabel)
    sourceLabel?.let { append(" · ").append(it) }
  }
  val shape = RoundedCornerShape(8.dp)
  val borderColor = if (isWalletLinked) {
    MaterialTheme.colorScheme.tertiary
  } else {
    MaterialTheme.colorScheme.outline
  }

  val dragModifier = if (canDrag) {
    Modifier.pointerInput(block.id, layout.startMinutes, block.timeLabel) {
      detectDragGesturesAfterLongPress(
        onDragStart = {
          onDragStateChange(true)
          dragOffsetPx = 0f
        },
        onDrag = { change, amount ->
          change.consume()
          dragOffsetPx += amount.y
        },
        onDragEnd = {
          val newMinutes = DayPlanScheduleLogic.minutesFromYOffset(
            offsetYPx = topOffsetPx + dragOffsetPx,
            gridStartHour = gridStartHour,
            hourHeightPx = hourHeightPx,
          )
          onTimeChange(block.id, DayPlanScheduleLogic.minutesToTimeLabel(newMinutes))
          onDragStateChange(false)
          dragOffsetPx = 0f
        },
        onDragCancel = {
          onDragStateChange(false)
          dragOffsetPx = 0f
        },
      )
    }
  } else {
    Modifier
  }

  val interactionModifier = when {
    !readOnly && isWalletLinked ->
      Modifier.combinedClickable(
        onClick = { onRequestUpdatePlan() },
        onLongClick = { onRequestUpdatePlan() },
      )
    canDrag -> dragModifier
    else -> Modifier
  }

  if (showTimeDialog) {
    AlertDialog(
      onDismissRequest = { showTimeDialog = false },
      title = { Text("Editar hora") },
      text = {
        MotTimeTextField(
          value = editTimeValue,
          onValueChange = { editTimeValue = it },
          label = "Hora",
          placeholder = "Ej. 09:15",
        )
      },
      confirmButton = {
        MOTTextButton(
          onClick = {
            onTimeChange(block.id, editTimeValue.trim())
            showTimeDialog = false
          },
        ) {
          Text("Guardar")
        }
      },
      dismissButton = {
        MOTTextButton(onClick = { showTimeDialog = false }) {
          Text("Cancelar")
        }
      },
    )
  }

  Card(
    modifier = modifier
      .padding(horizontal = 4.dp)
      .offset { IntOffset(0, displayTopPx.roundToInt()) }
      .height(blockHeight)
      .fillMaxWidth()
      .semantics {
        contentDescription = buildString {
          append("${block.title}, $timeLabel")
          sourceLabel?.let { append(", $it") }
        }
        val actions = buildList {
          if (!readOnly && isWalletLinked) {
            add(
              CustomAccessibilityAction("Corregir horario") {
                onRequestUpdatePlan()
                true
              },
            )
          }
          if (canDrag) {
            add(
              CustomAccessibilityAction("Mover 15 minutos antes") {
                val shifted = layout.startMinutes - DayPlanScheduleLogic.SNAP_MINUTES
                onTimeChange(block.id, DayPlanScheduleLogic.minutesToTimeLabel(shifted))
                true
              },
            )
            add(
              CustomAccessibilityAction("Mover 15 minutos después") {
                val shifted = layout.startMinutes + DayPlanScheduleLogic.SNAP_MINUTES
                onTimeChange(block.id, DayPlanScheduleLogic.minutesToTimeLabel(shifted))
                true
              },
            )
            add(
              CustomAccessibilityAction("Editar hora") {
                editTimeValue = timeLabel
                showTimeDialog = true
                true
              },
            )
          }
        }
        if (actions.isNotEmpty()) {
          customActions = actions
        }
      },
    shape = shape,
    colors = CardDefaults.cardColors(
      containerColor = if (isDragging) {
        MaterialTheme.colorScheme.secondaryContainer
      } else {
        MaterialTheme.colorScheme.surfaceContainerHigh
      },
    ),
    elevation = CardDefaults.cardElevation(
      defaultElevation = if (isDragging) 6.dp else 1.dp,
    ),
  ) {
    Row(
      modifier = Modifier
        .fillMaxSize()
        .border(1.dp, borderColor, shape)
        .padding(horizontal = MOTSpacing.componentSm, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Column(
        modifier = Modifier
          .weight(1f)
          .then(interactionModifier),
      ) {
        Text(
          text = block.title,
          style = MaterialTheme.typography.titleSmall,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        Text(
          text = scheduleMetaLabel,
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.padding(top = 2.dp),
        )
      }
      if (!readOnly) {
        Box {
          MOTIconButton(
            onClick = { menuExpanded = true },
            modifier = Modifier.semantics { contentDescription = "Opciones de actividad" },
          ) {
            Icon(
              imageVector = Icons.Default.MoreVert,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
          DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
          ) {
            if (isWalletLinked) {
              DropdownMenuItem(
                text = { Text("Corregir horario") },
                leadingIcon = {
                  Icon(Icons.Default.Schedule, contentDescription = null)
                },
                onClick = {
                  menuExpanded = false
                  onRequestUpdatePlan()
                },
              )
            } else {
              DropdownMenuItem(
                text = { Text("Editar hora") },
                leadingIcon = {
                  Icon(Icons.Default.Schedule, contentDescription = null)
                },
                onClick = {
                  menuExpanded = false
                  editTimeValue = timeLabel
                  showTimeDialog = true
                },
              )
              if (tripDays.isNotEmpty()) {
                DropdownMenuItem(
                  text = { Text("Mover a otro día") },
                  leadingIcon = {
                    Icon(Icons.Default.Today, contentDescription = null)
                  },
                  onClick = {
                    menuExpanded = false
                    onMoveToDay()
                  },
                )
              }
            }
            DropdownMenuItem(
              text = { Text("Vincular documento de Wallet") },
              leadingIcon = {
                Icon(Icons.Default.ConfirmationNumber, contentDescription = null)
              },
              onClick = {
                menuExpanded = false
                onLinkWallet()
              },
            )
          }
        }
      }
    }
  }
}
