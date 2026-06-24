package com.myowntrip.app.domain.plan

import com.myowntrip.app.domain.model.ItineraryBlock
import com.myowntrip.app.domain.model.WalletEntry
import java.time.LocalTime

data class TimelineBlockLayout(
  val block: ItineraryBlock,
  val startMinutes: Int,
  val durationMinutes: Long,
  val isFixed: Boolean,
  val linkedWallet: WalletEntry?,
)

object DayPlanScheduleLogic {
  const val GRID_START_HOUR = 6
  const val GRID_END_HOUR = 23
  const val SNAP_MINUTES = 15

  fun isBlockTimeEditable(block: ItineraryBlock, linkedWallet: WalletEntry?): Boolean = true

  fun resortAfterTimeEdit(
    blocks: List<ItineraryBlock>,
    editedBlockId: String,
    walletEntries: List<WalletEntry>,
  ): List<ItineraryBlock> {
    val edited = blocks.find { it.id == editedBlockId } ?: return blocks

    val others = blocks.filter { it.id != editedBlockId }
    return if (edited.timeLabel.isNullOrBlank()) {
      blocks.map { if (it.id == editedBlockId) edited else it }
    } else {
      PlanPlacementLogic.insertSorted(others, edited)
    }
  }

  fun gridHourRange(blocks: List<ItineraryBlock>, walletEntries: List<WalletEntry>): IntRange {
    val layouts = timelineLayouts(blocks, walletEntries)
    if (layouts.isEmpty()) return GRID_START_HOUR..GRID_END_HOUR

    val minStart = layouts.minOf { it.startMinutes }
    val maxEnd = layouts.maxOf { it.startMinutes + it.durationMinutes.toInt() }
    val startHour = (minStart / 60 - 1).coerceAtLeast(GRID_START_HOUR)
    val endHour = ((maxEnd + 59) / 60 + 1).coerceAtMost(GRID_END_HOUR)
    return startHour..endHour.coerceAtLeast(startHour)
  }

  fun timelineLayouts(
    blocks: List<ItineraryBlock>,
    walletEntries: List<WalletEntry>,
  ): List<TimelineBlockLayout> {
    val walletById = walletEntries.associateBy { it.id }
    val sorted = PlanPlacementLogic.sortBlocksForDisplay(blocks)
    return sorted.mapIndexed { index, block ->
      val wallet = block.walletEntryId?.let(walletById::get)
      val startMinutes = blockStartMinutes(block, wallet, index)
      TimelineBlockLayout(
        block = block,
        startMinutes = startMinutes,
        durationMinutes = PlanPlacementLogic.DEFAULT_DURATION_MINUTES,
        isFixed = PlanPlacementLogic.isTimeFixed(block, wallet),
        linkedWallet = wallet,
      )
    }
  }

  fun blockStartMinutes(block: ItineraryBlock, wallet: WalletEntry?, sortIndex: Int): Int {
    val time = PlanPlacementDriftLogic.effectivePlanTime(block, wallet)
    if (time != null) return time.hour * 60 + time.minute
    return (9 * 60) + (sortIndex * PlanPlacementLogic.DEFAULT_DURATION_MINUTES.toInt())
  }

  fun minutesToTimeLabel(minutes: Int): String {
    val clamped = minutes.coerceIn(GRID_START_HOUR * 60, GRID_END_HOUR * 60 + 59)
    val time = LocalTime.of(clamped / 60, clamped % 60)
    return PlanPlacementLogic.formatTime(time)
  }

  fun snapMinutes(minutes: Int, step: Int = SNAP_MINUTES): Int {
    val snapped = ((minutes + step / 2) / step) * step
    return snapped.coerceIn(GRID_START_HOUR * 60, GRID_END_HOUR * 60)
  }

  fun minutesFromYOffset(
    offsetYPx: Float,
    gridStartHour: Int,
    hourHeightPx: Float,
  ): Int {
    val minutes = gridStartHour * 60 + ((offsetYPx / hourHeightPx) * 60f).toInt()
    return snapMinutes(minutes)
  }

  fun hourLabel(hour: Int): String = "%02d:00".format(hour)
}
