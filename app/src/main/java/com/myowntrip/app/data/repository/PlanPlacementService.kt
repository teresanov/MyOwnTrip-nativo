package com.myowntrip.app.data.repository

import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.ItineraryBlock
import com.myowntrip.app.domain.model.WalletEntry
import com.myowntrip.app.domain.plan.PlanPlacementLogic
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlanPlacementService @Inject constructor(
  private val itineraryRepository: ItineraryRepository,
) {
  suspend fun apply(
    entry: WalletEntry,
    days: List<Day>,
    enabled: Boolean,
    dayIdOverride: String?,
    timeOverride: LocalTime?,
  ) {
    if (!enabled) return
    val suggestion = PlanPlacementLogic.suggest(entry, days)
    val dayId = dayIdOverride ?: suggestion.dayId ?: return
    val time = timeOverride ?: suggestion.time ?: entry.time
    val timeLabel = PlanPlacementLogic.timeLabel(time)
    val title = entry.title.trim().ifBlank { "Actividad" }

    val existing = itineraryRepository.findBlockByWalletEntryId(entry.id)
    val targetDayBlocks = itineraryRepository.getBlocksForDay(dayId)

    val block = if (existing != null) {
      existing.copy(
        dayId = dayId,
        title = title,
        timeLabel = timeLabel,
        walletEntryId = entry.id,
      )
    } else {
      ItineraryBlock(
        id = UUID.randomUUID().toString(),
        dayId = dayId,
        title = title,
        timeLabel = timeLabel,
        sortOrder = targetDayBlocks.size,
        walletEntryId = entry.id,
      )
    }

    if (existing != null && existing.dayId != dayId) {
      val oldDayBlocks = itineraryRepository.getBlocksForDay(existing.dayId)
        .filter { it.id != existing.id }
        .mapIndexed { index, item -> item.copy(sortOrder = index) }
      itineraryRepository.saveOrder(oldDayBlocks)
    }

    val sourceBlocks = if (existing != null) {
      targetDayBlocks.filter { it.id != existing.id }
    } else {
      targetDayBlocks
    }
    itineraryRepository.saveOrder(PlanPlacementLogic.insertSorted(sourceBlocks, block))
  }
}
