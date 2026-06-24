package com.myowntrip.app.domain.plan

import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.ItineraryBlock
import com.myowntrip.app.domain.model.WalletEntry

data class PlanPlacementDrift(
  val hasTimeDrift: Boolean,
  val hasDayDrift: Boolean,
) {
  val hasDrift: Boolean get() = hasTimeDrift || hasDayDrift
}

/** Plan vinculado a un documento Wallet con divergencia respecto al ticket. */
data class WalletPlanPlacementInfo(
  val tripId: String,
  val dayId: String,
  val dayNumber: Int,
  val listDriftSuffix: String,
  val detailChipLabel: String,
  val accessibilityDriftPhrase: String,
)

object PlanPlacementDriftLogic {
  fun drift(
    block: ItineraryBlock,
    wallet: WalletEntry?,
    day: Day?,
  ): PlanPlacementDrift = PlanPlacementDrift(
    hasTimeDrift = hasPlanTimeDrift(block, wallet),
    hasDayDrift = hasPlanDayDrift(wallet, day),
  )

  fun hasPlanTimeDrift(block: ItineraryBlock, wallet: WalletEntry?): Boolean {
    if (wallet == null || !PlanPlacementLogic.isTimeFixed(block, wallet)) return false
    val planTime = PlanPlacementLogic.parseTime(block.timeLabel) ?: return false
    return planTime != wallet.time
  }

  fun hasPlanDayDrift(wallet: WalletEntry?, day: Day?): Boolean {
    val documentDate = wallet?.date ?: return false
    val planDate = day?.date ?: return false
    return documentDate != planDate
  }

  /** Hora efectiva mostrada en el cuadrante (respeta override en el plan). */
  fun effectivePlanTime(block: ItineraryBlock, wallet: WalletEntry?) =
    PlanPlacementLogic.parseTime(block.timeLabel)
      ?: wallet?.takeIf { PlanPlacementLogic.isTimeFixed(block, it) }?.time

  fun timeSourceLabel(block: ItineraryBlock, wallet: WalletEntry?, day: Day?): String? {
    if (wallet == null) return null
    return when {
      drift(block, wallet, day).hasDrift -> "Actualizada en el plan"
      PlanPlacementLogic.isTimeFixed(block, wallet) -> "Del documento"
      else -> null
    }
  }

  fun walletDriftChipSummary(block: ItineraryBlock, wallet: WalletEntry?, day: Day?): String? {
    val placementDrift = drift(block, wallet, day)
    if (!placementDrift.hasDrift || day == null) return null
    val parts = buildList {
      if (placementDrift.hasDayDrift) add("Día ${day.dayNumber}")
      if (placementDrift.hasTimeDrift) {
        effectivePlanTime(block, wallet)?.let { PlanPlacementLogic.formatTime(it) }?.let(::add)
      }
    }
    if (parts.isEmpty()) return "Actualizada en el plan"
    return "Actualizada en el plan · ${parts.joinToString(" · ")}"
  }

  fun resolveWalletPlanPlacement(
    entry: WalletEntry,
    blocks: List<ItineraryBlock>,
    days: List<Day>,
  ): WalletPlanPlacementInfo? {
    val block = blocks.find { it.walletEntryId == entry.id } ?: return null
    val day = days.find { it.id == block.dayId } ?: return null
    val placementDrift = drift(block, entry, day)
    if (!placementDrift.hasDrift) return null
    val detailChipLabel = walletDriftChipSummary(block, entry, day) ?: return null
    return WalletPlanPlacementInfo(
      tripId = entry.tripId,
      dayId = day.id,
      dayNumber = day.dayNumber,
      listDriftSuffix = walletListDriftSuffix(block, entry, day),
      detailChipLabel = detailChipLabel,
      accessibilityDriftPhrase = accessibilityDriftPhrase(block, entry, day, placementDrift),
    )
  }

  fun walletListDriftSuffix(block: ItineraryBlock, wallet: WalletEntry, day: Day): String {
    val placementDrift = drift(block, wallet, day)
    val parts = buildList {
      add("Actualizada en el plan")
      if (placementDrift.hasDayDrift) add("Día ${day.dayNumber}")
      if (placementDrift.hasTimeDrift) {
        effectivePlanTime(block, wallet)?.let { PlanPlacementLogic.formatTime(it) }?.let(::add)
      }
    }
    return " · ${parts.joinToString(" · ")}"
  }

  private fun accessibilityDriftPhrase(
    block: ItineraryBlock,
    wallet: WalletEntry,
    day: Day,
    placementDrift: PlanPlacementDrift,
  ): String = buildString {
    append("actualizada en el plan")
    if (placementDrift.hasDayDrift) {
      append(", día ${day.dayNumber}")
    }
    if (placementDrift.hasTimeDrift) {
      effectivePlanTime(block, wallet)?.let { time ->
        append(", a las ${PlanPlacementLogic.formatTime(time)}")
      }
    }
  }
}
