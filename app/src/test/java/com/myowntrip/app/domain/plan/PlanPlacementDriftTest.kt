package com.myowntrip.app.domain.plan

import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.EntryType
import com.myowntrip.app.domain.model.ItineraryBlock
import com.myowntrip.app.domain.model.WalletEntry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class PlanPlacementDriftTest {

  private val day1 = Day("d1", "trip", LocalDate.of(2026, 7, 4), 1, null)
  private val day2 = Day("d2", "trip", LocalDate.of(2026, 7, 5), 2, null)

  @Test
  fun hasPlanTimeDrift_whenPlanTimeDiffersFromWallet() {
    val wallet = flightWallet(time = LocalTime.of(9, 15))
    val block = block(time = "10:30", walletId = wallet.id)
    assertTrue(PlanPlacementDriftLogic.hasPlanTimeDrift(block, wallet))
  }

  @Test
  fun hasPlanDayDrift_whenPlanDayDiffersFromWalletDate() {
    val wallet = flightWallet(
      date = LocalDate.of(2026, 7, 4),
      time = LocalTime.of(9, 15),
    )
    val block = block(time = "09:15", walletId = wallet.id, dayId = day2.id)
    assertTrue(PlanPlacementDriftLogic.hasPlanDayDrift(wallet, day2))
    assertFalse(PlanPlacementDriftLogic.hasPlanDayDrift(wallet, day1))
  }

  @Test
  fun timeSourceLabel_showsDelDocumentoWhenAligned() {
    val wallet = flightWallet(time = LocalTime.of(9, 15))
    val block = block(time = "09:15", walletId = wallet.id)
    assertEquals("Del documento", PlanPlacementDriftLogic.timeSourceLabel(block, wallet, day1))
  }

  @Test
  fun timeSourceLabel_showsActualizadaWhenDrift() {
    val wallet = flightWallet(time = LocalTime.of(9, 15))
    val block = block(time = "10:30", walletId = wallet.id)
    assertEquals(
      "Actualizada en el plan",
      PlanPlacementDriftLogic.timeSourceLabel(block, wallet, day1),
    )
  }

  @Test
  fun walletDriftChipSummary_includesDayAndTime() {
    val wallet = flightWallet(
      date = LocalDate.of(2026, 7, 4),
      time = LocalTime.of(9, 15),
    )
    val block = block(time = "10:30", walletId = wallet.id, dayId = day2.id)
    assertEquals(
      "Actualizada en el plan · Día 2 · 10:30",
      PlanPlacementDriftLogic.walletDriftChipSummary(block, wallet, day2),
    )
  }

  @Test
  fun walletListDriftSuffix_includesPlanSummary() {
    val wallet = flightWallet(
      date = LocalDate.of(2026, 7, 4),
      time = LocalTime.of(9, 15),
    )
    val block = block(time = "10:30", walletId = wallet.id, dayId = day2.id)
    assertEquals(
      " · Actualizada en el plan · Día 2 · 10:30",
      PlanPlacementDriftLogic.walletListDriftSuffix(block, wallet, day2),
    )
  }

  @Test
  fun resolveWalletPlanPlacement_returnsInfoWhenDrift() {
    val wallet = flightWallet(time = LocalTime.of(9, 15))
    val block = block(time = "10:30", walletId = wallet.id)
    val info = PlanPlacementDriftLogic.resolveWalletPlanPlacement(
      entry = wallet,
      blocks = listOf(block),
      days = listOf(day1),
    )
    requireNotNull(info)
    assertEquals("trip", info.tripId)
    assertEquals("d1", info.dayId)
    assertTrue(info.accessibilityDriftPhrase.contains("actualizada en el plan"))
  }

  @Test
  fun recalculateDaySchedule_preservesOverriddenFlightTime() {
    val wallet = flightWallet(time = LocalTime.of(9, 15))
    val blocks = listOf(
      block("flight", time = "10:30", walletId = wallet.id),
    )
    val result = PlanPlacementLogic.recalculateDaySchedule(blocks, listOf(wallet))
    assertEquals("10:30", result.single().timeLabel)
  }

  private fun flightWallet(
    date: LocalDate? = LocalDate.of(2026, 7, 4),
    time: LocalTime?,
  ) = WalletEntry(
    id = "w1",
    tripId = "trip",
    type = EntryType.FLIGHT,
    title = "IB 3254",
    date = date,
    time = time,
  )

  private fun block(
    id: String = "b1",
    time: String?,
    walletId: String,
    dayId: String = day1.id,
  ) = ItineraryBlock(
    id = id,
    dayId = dayId,
    title = "Vuelo",
    timeLabel = time,
    sortOrder = 0,
    walletEntryId = walletId,
  )
}
