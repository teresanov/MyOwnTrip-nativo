package com.myowntrip.app.domain.plan

import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.ItineraryBlock
import com.myowntrip.app.domain.model.WalletEntry
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

enum class PlanPlacementConfidence {
  HIGH,
  MEDIUM,
  NONE,
}

data class PlanPlacementSuggestion(
  val dayId: String?,
  val dayNumber: Int?,
  val dayDate: java.time.LocalDate?,
  val time: LocalTime?,
  val confidence: PlanPlacementConfidence,
) {
  val canPlace: Boolean get() = dayId != null

  fun summary(title: String): String = when {
    !canPlace -> "No hay un día del viaje que coincida con la fecha del documento."
    dayNumber != null && time != null ->
      "Día $dayNumber · ${PlanPlacementLogic.formatTime(time)} — $title"
    dayNumber != null -> "Día $dayNumber — $title (sin hora concreta)"
    else -> title
  }
}

object PlanPlacementLogic {
  private val TimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

  fun suggest(entry: WalletEntry, days: List<Day>): PlanPlacementSuggestion {
    val date = entry.date
      ?: return PlanPlacementSuggestion(null, null, null, entry.time, PlanPlacementConfidence.NONE)
    val day = days.find { it.date == date }
      ?: return PlanPlacementSuggestion(null, null, null, entry.time, PlanPlacementConfidence.NONE)
    val confidence = if (entry.time != null) {
      PlanPlacementConfidence.HIGH
    } else {
      PlanPlacementConfidence.MEDIUM
    }
    return PlanPlacementSuggestion(
      dayId = day.id,
      dayNumber = day.dayNumber,
      dayDate = day.date,
      time = entry.time,
      confidence = confidence,
    )
  }

  fun timeLabel(time: LocalTime?): String? = time?.format(TimeFormatter)

  fun parseTime(label: String?): LocalTime? {
    if (label.isNullOrBlank()) return null
    return runCatching { LocalTime.parse(label.trim(), TimeFormatter) }
      .recoverCatching { LocalTime.parse(label.trim(), DateTimeFormatter.ofPattern("H:mm")) }
      .getOrNull()
  }

  fun formatTime(time: LocalTime): String = time.format(TimeFormatter)

  fun sortBlocksForDisplay(blocks: List<ItineraryBlock>): List<ItineraryBlock> =
    blocks.sortedWith(
      compareBy<ItineraryBlock>(
        { parseTime(it.timeLabel) ?: LocalTime.MAX },
        { it.sortOrder },
      ),
    )

  fun insertSorted(blocks: List<ItineraryBlock>, newBlock: ItineraryBlock): List<ItineraryBlock> {
    val newTime = parseTime(newBlock.timeLabel)
    val mutable = blocks.toMutableList()
    val index = if (newTime == null) {
      mutable.size
    } else {
      val idx = mutable.indexOfFirst { block ->
        parseTime(block.timeLabel)?.let { it > newTime } == true
      }
      if (idx == -1) mutable.size else idx
    }
    mutable.add(index, newBlock)
    return mutable.mapIndexed { order, block -> block.copy(sortOrder = order) }
  }
}
