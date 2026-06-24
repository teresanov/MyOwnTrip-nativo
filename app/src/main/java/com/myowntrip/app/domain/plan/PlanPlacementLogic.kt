package com.myowntrip.app.domain.plan

import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.EntryType
import com.myowntrip.app.domain.model.ItineraryBlock
import com.myowntrip.app.domain.model.WalletEntry
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

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
  const val DEFAULT_DURATION_MINUTES = 60L

  private val walletFixedTypes = setOf(
    EntryType.FLIGHT,
    EntryType.HOTEL,
    EntryType.TRANSPORT,
    EntryType.ACTIVITY,
  )

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

  fun walletDocumentScheduleLabel(entry: WalletEntry): String {
    val date = entry.date?.format(DateTimeFormatter.ofPattern("d MMM yyyy", Locale("es", "ES")))
    val time = entry.time?.format(TimeFormatter)
    return when {
      date != null && time != null -> "$date · $time"
      date != null -> date
      time != null -> time
      else -> "sin fecha concreta"
    }
  }

  fun isTimeFixed(block: ItineraryBlock, linkedWallet: WalletEntry?): Boolean =
    linkedWallet?.time != null && linkedWallet.type in walletFixedTypes

  fun sortBlocksForDisplay(blocks: List<ItineraryBlock>): List<ItineraryBlock> =
    blocks.sortedBy { it.sortOrder }

  fun recalculateDaySchedule(
    blocks: List<ItineraryBlock>,
    walletEntries: List<WalletEntry>,
    dayStart: LocalTime = LocalTime.of(9, 0),
    defaultDurationMinutes: Long = DEFAULT_DURATION_MINUTES,
  ): List<ItineraryBlock> {
    if (blocks.isEmpty()) return blocks

    val walletById = walletEntries.associateBy { it.id }
    val ordered = blocks
      .sortedBy { it.sortOrder }
      .mapIndexed { index, block -> block.copy(sortOrder = index) }

    var cursor = dayStart
    val result = mutableListOf<ItineraryBlock>()

    for (block in ordered) {
      val wallet = block.walletEntryId?.let(walletById::get)
      if (isTimeFixed(block, wallet)) {
        val fixedTime = if (PlanPlacementDriftLogic.hasPlanTimeDrift(block, wallet)) {
          parseTime(block.timeLabel)
        } else {
          wallet?.time
        }
        val preservedLabel = fixedTime?.let(::formatTime) ?: block.timeLabel
        if (fixedTime != null) {
          val fixedEnd = fixedTime.plusMinutes(defaultDurationMinutes)
          if (cursor.isBefore(fixedEnd)) {
            cursor = fixedEnd
          }
        }
        result += block.copy(timeLabel = preservedLabel)
      } else {
        val assigned = cursor
        result += block.copy(timeLabel = formatTime(assigned))
        cursor = assigned.plusMinutes(defaultDurationMinutes)
      }
    }

    return result
  }

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
