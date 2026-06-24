package com.myowntrip.app.data.demo

import com.myowntrip.app.data.repository.ExpenseRepository
import com.myowntrip.app.data.repository.ItineraryRepository
import com.myowntrip.app.data.repository.JournalRepository
import com.myowntrip.app.data.repository.TripRepository
import com.myowntrip.app.data.repository.WalletRepository
import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.ExpenseCategory
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebugPastTripsSeeder @Inject constructor(
  private val tripRepository: TripRepository,
  private val walletRepository: WalletRepository,
  private val itineraryRepository: ItineraryRepository,
  private val journalRepository: JournalRepository,
  private val expenseRepository: ExpenseRepository,
) : PastTripsDemoLoader {

  override suspend fun seedIfAbsent(): PastTripsDemoResult {
    if (tripRepository.findTripByName(BARCELONA_NAME) != null &&
      tripRepository.findTripByName(LISBOA_NAME) != null
    ) {
      return PastTripsDemoResult(
        tripsCreated = 0,
        message = "Los viajes demo pasados ya están cargados.",
      )
    }
    return seed(force = false)
  }

  override suspend fun seed(force: Boolean): PastTripsDemoResult {
    if (!force &&
      tripRepository.findTripByName(BARCELONA_NAME) != null &&
      tripRepository.findTripByName(LISBOA_NAME) != null
    ) {
      return PastTripsDemoResult(0, "Los viajes demo pasados ya están cargados.")
    }

    var created = 0
    if (tripRepository.findTripByName(BARCELONA_NAME) == null) {
      seedBarcelonaWeekend()
      created++
    }
    if (tripRepository.findTripByName(LISBOA_NAME) == null) {
      seedLisbonApril()
      created++
    }

    return PastTripsDemoResult(
      tripsCreated = created,
      message = when (created) {
        0 -> "No se añadieron viajes (ya existían)."
        1 -> "Se añadió 1 viaje pasado de demo."
        else -> "Se añadieron $created viajes pasados de demo."
      },
    )
  }

  private suspend fun seedBarcelonaWeekend() {
    val tripId = tripRepository.createTrip(
      name = BARCELONA_NAME,
      destination = "Barcelona",
      startDate = LocalDate.of(2026, 3, 20),
      endDate = LocalDate.of(2026, 3, 22),
    )
    val days = tripRepository.getDaysForTrip(tripId)
    val day1 = days.dayOn(LocalDate.of(2026, 3, 20))
    val day2 = days.dayOn(LocalDate.of(2026, 3, 21))
    val day3 = days.dayOn(LocalDate.of(2026, 3, 22))

    walletRepository.importDebugWalletAsset(
      tripId = tripId,
      assetFileName = "boarding-pass-ib3254-madrid-barcelona.pdf",
      dateOverride = LocalDate.of(2026, 3, 20),
      timeOverride = LocalTime.of(9, 15),
      planDayId = day1.id,
      planTimeOverride = LocalTime.of(10, 30),
    )

    walletRepository.importDebugWalletAsset(
      tripId = tripId,
      assetFileName = "hotel-casa-bonay-reserva.pdf",
      dateOverride = LocalDate.of(2026, 3, 20),
      timeOverride = LocalTime.of(15, 0),
      titleOverride = "Casa Bonay Barcelona",
      planDayId = day1.id,
    )

    walletRepository.importDebugWalletAsset(
      tripId = tripId,
      assetFileName = "entrada-sagrada-familia-5jul.pdf",
      dateOverride = LocalDate.of(2026, 3, 21),
      timeOverride = LocalTime.of(11, 0),
      planDayId = day2.id,
    )

    itineraryRepository.addBlockAtEnd(
      dayId = day2.id,
      title = "Cena en el Born",
      timeLabel = "21:00",
      currentCount = itineraryRepository.getBlocksForDay(day2.id).size,
    )

    journalRepository.addNote(
      dayId = day2.id,
      text = "Atardecer en el Born y tapas en una terraza tranquila.",
    )
    journalRepository.addNote(
      dayId = day3.id,
      text = "Último paseo por la playa antes del vuelo de vuelta.",
    )

    expenseRepository.addExpense(
      tripId = tripId,
      dayId = day1.id,
      amount = 42.50,
      concept = "Taxi aeropuerto → hotel",
      category = ExpenseCategory.TRANSPORT,
    )
    expenseRepository.addExpense(
      tripId = tripId,
      dayId = day2.id,
      amount = 68.20,
      concept = "Cena El Born",
      category = ExpenseCategory.FOOD,
    )
  }

  private suspend fun seedLisbonApril() {
    val tripId = tripRepository.createTrip(
      name = LISBOA_NAME,
      destination = "Lisboa",
      startDate = LocalDate.of(2026, 4, 12),
      endDate = LocalDate.of(2026, 4, 18),
    )
    val days = tripRepository.getDaysForTrip(tripId)
    val day1 = days.dayOn(LocalDate.of(2026, 4, 12))
    val day2 = days.dayOn(LocalDate.of(2026, 4, 13))
    val day3 = days.dayOn(LocalDate.of(2026, 4, 14))
    val day4 = days.dayOn(LocalDate.of(2026, 4, 15))
    val day5 = days.dayOn(LocalDate.of(2026, 4, 16))

    walletRepository.importDebugWalletAsset(
      tripId = tripId,
      assetFileName = "renfe-ave-03142-madrid-barcelona.pdf",
      dateOverride = LocalDate.of(2026, 4, 12),
      timeOverride = LocalTime.of(8, 30),
      titleOverride = "AVE 03142 · Madrid → Lisboa",
      planDayId = day1.id,
    )

    walletRepository.importDebugWalletAsset(
      tripId = tripId,
      assetFileName = "hotel-casa-bonay-reserva.pdf",
      dateOverride = LocalDate.of(2026, 4, 12),
      timeOverride = LocalTime.of(14, 0),
      titleOverride = "Hotel Baixa Lisboa",
      planDayId = day1.id,
    )

    walletRepository.importDebugWalletAsset(
      tripId = tripId,
      assetFileName = "museo-prado-entrada-general.pdf",
      dateOverride = LocalDate.of(2026, 4, 13),
      timeOverride = LocalTime.of(10, 0),
      titleOverride = "Museo Nacional de Arte Antiga",
      planDayId = day2.id,
    )

    walletRepository.importDebugWalletAsset(
      tripId = tripId,
      assetFileName = "concierto-mad-cool-arctic-monkeys.pdf",
      dateOverride = LocalDate.of(2026, 4, 14),
      timeOverride = LocalTime.of(21, 30),
      titleOverride = "Concierto no CCB",
      planDayId = day3.id,
    )

    walletRepository.importDebugWalletAsset(
      tripId = tripId,
      assetFileName = "teatro-real-las-bodas-de-figaro.pdf",
      dateOverride = LocalDate.of(2026, 4, 15),
      timeOverride = LocalTime.of(20, 0),
      titleOverride = "Teatro Nacional D. Maria II",
      planDayId = day4.id,
      planTimeOverride = LocalTime.of(19, 15),
    )

    val carRental = walletRepository.importDebugWalletAsset(
      tripId = tripId,
      assetFileName = "europcar-alquiler-coche-madrid.pdf",
      dateOverride = LocalDate.of(2026, 4, 16),
      timeOverride = LocalTime.of(9, 0),
      titleOverride = "Alquiler coche · Sintra",
      planDayId = day5.id,
      placeOnPlan = false,
    )
    walletRepository.archiveEntry(carRental.id)

    itineraryRepository.addBlockAtEnd(
      dayId = day3.id,
      title = "Pasteles de Belém",
      timeLabel = "11:30",
      currentCount = itineraryRepository.getBlocksForDay(day3.id).size,
    )

    journalRepository.addNote(
      dayId = day3.id,
      text = "Cola corta en Pastéis de Belém — mereció la pena.",
    )
    journalRepository.addNote(
      dayId = day5.id,
      text = "Sintra en un día: Palácio da Pena entre niebla.",
    )

    expenseRepository.addExpense(
      tripId = tripId,
      dayId = day1.id,
      amount = 24.90,
      concept = "Viva Viagem (metro)",
      category = ExpenseCategory.TRANSPORT,
    )
    expenseRepository.addExpense(
      tripId = tripId,
      dayId = day3.id,
      amount = 12.60,
      concept = "Pasteis de Belém",
      category = ExpenseCategory.FOOD,
    )
    expenseRepository.addExpense(
      tripId = tripId,
      dayId = day4.id,
      amount = 54.00,
      concept = "Entradas teatro",
      category = ExpenseCategory.ACTIVITY,
    )
  }

  private fun List<Day>.dayOn(date: LocalDate): Day =
    find { it.date == date } ?: error("No day for $date")

  companion object {
    const val BARCELONA_NAME = "[Demo] Barcelona pasado"
    const val LISBOA_NAME = "[Demo] Lisboa pasado"
  }
}
