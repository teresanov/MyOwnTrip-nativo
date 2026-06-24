package com.myowntrip.app.data.repository

import com.myowntrip.app.data.local.dao.ItineraryBlockDao
import com.myowntrip.app.data.local.toDomain
import com.myowntrip.app.data.local.toEntity
import com.myowntrip.app.domain.model.ItineraryBlock
import com.myowntrip.app.domain.plan.PlanPlacementLogic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItineraryRepository @Inject constructor(
  private val itineraryBlockDao: ItineraryBlockDao,
) {
  fun observeByDay(dayId: String): Flow<List<ItineraryBlock>> =
    itineraryBlockDao.observeByDay(dayId).map { blocks -> blocks.map { it.toDomain() } }

  fun observeByTrip(tripId: String): Flow<List<ItineraryBlock>> =
    itineraryBlockDao.observeByTrip(tripId).map { blocks -> blocks.map { it.toDomain() } }

  suspend fun findBlockByWalletEntryId(walletEntryId: String): ItineraryBlock? =
    itineraryBlockDao.findByWalletEntryId(walletEntryId)?.toDomain()

  suspend fun getBlocksForDay(dayId: String): List<ItineraryBlock> =
    itineraryBlockDao.getByDay(dayId).map { it.toDomain() }

  suspend fun addBlockAtEnd(
    dayId: String,
    title: String,
    timeLabel: String?,
    currentCount: Int,
    walletEntryId: String? = null,
  ) {
    itineraryBlockDao.insert(
      ItineraryBlock(
        id = UUID.randomUUID().toString(),
        dayId = dayId,
        title = title,
        timeLabel = timeLabel,
        sortOrder = currentCount,
        walletEntryId = walletEntryId,
      ).toEntity(),
    )
  }

  suspend fun updateBlock(block: ItineraryBlock) {
    itineraryBlockDao.insert(block.toEntity())
  }

  suspend fun saveOrder(blocks: List<ItineraryBlock>) {
    itineraryBlockDao.insertAll(
      blocks.mapIndexed { index, block -> block.copy(sortOrder = index).toEntity() },
    )
  }

  suspend fun moveBlockToDay(
    block: ItineraryBlock,
    targetDayId: String,
    timeLabel: String?,
  ) {
    val normalizedTime = timeLabel?.trim()?.ifBlank { null }
    val updated = block.copy(
      dayId = targetDayId,
      timeLabel = normalizedTime ?: block.timeLabel,
    )

    if (block.dayId != targetDayId) {
      val oldDayBlocks = getBlocksForDay(block.dayId)
        .filter { it.id != block.id }
        .mapIndexed { index, item -> item.copy(sortOrder = index) }
      saveOrder(oldDayBlocks)
    }

    val targetBlocks = getBlocksForDay(targetDayId).filter { it.id != block.id }
    saveOrder(PlanPlacementLogic.insertSorted(targetBlocks, updated))
  }
}
