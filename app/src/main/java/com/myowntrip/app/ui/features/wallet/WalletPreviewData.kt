package com.myowntrip.app.ui.features.wallet

import android.net.Uri
import com.myowntrip.app.domain.model.EntryType
import com.myowntrip.app.domain.model.Trip
import com.myowntrip.app.domain.model.WalletEntry
import java.time.LocalDate
import java.time.LocalTime

/** Viaje demo Barcelona — 4–6 jul 2026 (alineado con `docs/samples/wallet`). */
internal val previewWalletTrip = Trip(
  id = "trip-1",
  name = "Barcelona fin de semana",
  destination = "Barcelona",
  startDate = LocalDate.of(2026, 7, 4),
  endDate = LocalDate.of(2026, 7, 6),
  createdAt = 0L,
)

/** Misma maleta que [previewWalletTrip], fechas en diciembre 2026 (viaje aún no pasado). */
internal val previewCurrentWalletTrip = previewWalletTrip.copy(
  id = "trip-current",
  startDate = LocalDate.of(2026, 12, 4),
  endDate = LocalDate.of(2026, 12, 6),
)

internal val previewWalletEntries = listOf(
  WalletEntry(
    id = "1",
    tripId = "trip-1",
    type = EntryType.FLIGHT,
    title = "IB 3254 · Madrid → Barcelona",
    date = LocalDate.of(2026, 7, 4),
    time = LocalTime.of(9, 15),
    qrPayload = "M1DEMO/PAX EIB3254 MADBCNIB 3254 314Y014A0001 349>5180  5140BIB              2A825513825513 0000",
  ),
  WalletEntry(
    id = "2",
    tripId = "trip-1",
    type = EntryType.HOTEL,
    title = "Hotel Casa Bonay",
    date = LocalDate.of(2026, 7, 4),
    notes = "Check-in 15:00",
  ),
  WalletEntry(
    id = "3",
    tripId = "trip-1",
    type = EntryType.TRANSPORT,
    title = "AVE 03142",
    date = LocalDate.of(2026, 7, 6),
    time = LocalTime.of(18, 30),
  ),
  WalletEntry(
    id = "4",
    tripId = "trip-1",
    type = EntryType.ACTIVITY,
    title = "Entrada Sagrada Familia",
    date = LocalDate.of(2026, 7, 5),
    time = LocalTime.of(11, 0),
  ),
)

internal val previewCurrentWalletEntries = previewWalletEntries.map { entry ->
  entry.copy(
    tripId = previewCurrentWalletTrip.id,
    date = entry.date?.withMonth(12),
  )
}

internal val previewFlightImportState = WalletFormUiState(
  tripId = "trip-1",
  type = EntryType.FLIGHT,
  title = "IB 3254 · Madrid → Barcelona",
  date = LocalDate.of(2026, 7, 4),
  time = LocalTime.of(9, 15),
  attachmentUri = Uri.parse("content://preview/billete-barcelona.pdf"),
  attachmentFileName = "billete-barcelona.pdf",
  isImport = true,
  qrPayload = previewWalletEntries.first().qrPayload,
  canChooseStorage = true,
  saveOfflineCopy = true,
)

internal val previewHotelImportState = WalletFormUiState(
  tripId = "trip-1",
  type = EntryType.HOTEL,
  title = "Hotel Casa Bonay",
  date = LocalDate.of(2026, 7, 4),
  time = LocalTime.of(15, 0),
  notes = "Check-in 15:00 · Confirmación H-88421",
  attachmentUri = Uri.parse("content://preview/hotel-casa-bonay-reserva.pdf"),
  attachmentFileName = "hotel-casa-bonay-reserva.pdf",
  isImport = true,
  showNotesField = true,
  canChooseStorage = true,
  saveOfflineCopy = true,
)

internal val previewParseFailState = WalletFormUiState(
  tripId = "trip-1",
  type = EntryType.GENERIC,
  title = "",
  attachmentUri = Uri.parse("content://preview/documento-ilegible.pdf"),
  attachmentFileName = "documento-ilegible.pdf",
  parseFailed = true,
  canChooseStorage = true,
  saveOfflineCopy = false,
)

internal val previewManualFormState = WalletFormUiState(
  tripId = "trip-1",
  trips = listOf(previewWalletTrip),
  title = "",
)

internal val previewManualWithAttachmentState = previewManualFormState.copy(
  title = "Entrada Teatro Real",
  type = EntryType.ACTIVITY,
  date = LocalDate.of(2026, 12, 5),
  time = LocalTime.of(19, 15),
  attachmentUri = Uri.parse("content://preview/entrada-teatro.jpg"),
  attachmentFileName = "entrada-teatro.jpg",
)

internal val previewNoTripsFormState = WalletFormUiState(
  tripId = "",
  trips = emptyList(),
  title = "",
)

internal val previewTitleErrorFormState = previewManualFormState.copy(
  title = "",
  titleError = "Obligatorio",
)

internal val previewTypeCorrectionState = previewFlightImportState.copy(
  showTypeCorrection = true,
)

internal val previewArchivedWalletEntries = previewCurrentWalletEntries.map { entry ->
  if (entry.type == EntryType.ACTIVITY) entry.copy(archivedAt = 1L) else entry
}

internal val previewAllArchivedWalletEntries = previewCurrentWalletEntries.map {
  it.copy(archivedAt = 1L)
}

internal val previewDetailEntry = previewWalletEntries.first().copy(
  pdfUri = "file://preview/billete-barcelona.pdf",
)

internal val previewDetailWithAmount = previewDetailEntry.copy(
  amount = 89.0,
  currency = "EUR",
)

internal fun previewConfirmEntry() = WalletEntry(
  id = "new-1",
  tripId = "trip-1",
  type = EntryType.FLIGHT,
  title = "IB 3254 · Madrid → Barcelona",
  date = LocalDate.of(2026, 7, 4),
  time = LocalTime.of(9, 15),
  pdfUri = "file://preview/billete-barcelona.pdf",
  qrPayload = previewWalletEntries.first().qrPayload,
)
