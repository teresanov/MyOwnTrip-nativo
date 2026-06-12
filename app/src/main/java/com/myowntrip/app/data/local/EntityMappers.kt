package com.myowntrip.app.data.local

import com.myowntrip.app.data.local.entity.DayEntity
import com.myowntrip.app.data.local.entity.ExpenseEntity
import com.myowntrip.app.data.local.entity.JournalNoteEntity
import com.myowntrip.app.data.local.entity.TripEntity
import com.myowntrip.app.data.local.entity.WalletEntryEntity
import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.Expense
import com.myowntrip.app.domain.model.JournalNote
import com.myowntrip.app.domain.model.Trip
import com.myowntrip.app.domain.model.WalletEntry

fun TripEntity.toDomain() = Trip(
  id = id,
  name = name,
  destination = destination,
  startDate = startDate,
  endDate = endDate,
  coverPhoto = coverPhoto,
  createdAt = createdAt,
)

fun Trip.toEntity() = TripEntity(
  id = id,
  name = name,
  destination = destination,
  startDate = startDate,
  endDate = endDate,
  coverPhoto = coverPhoto,
  createdAt = createdAt,
)

fun DayEntity.toDomain() = Day(
  id = id,
  tripId = tripId,
  date = date,
  dayNumber = dayNumber,
  title = title,
)

fun Day.toEntity() = DayEntity(
  id = id,
  tripId = tripId,
  date = date,
  dayNumber = dayNumber,
  title = title,
)

fun WalletEntryEntity.toDomain() = WalletEntry(
  id = id,
  tripId = tripId,
  type = type,
  title = title,
  date = date,
  time = time,
  pdfUri = pdfUri,
  linkUrl = linkUrl,
  notes = notes,
)

fun WalletEntry.toEntity() = WalletEntryEntity(
  id = id,
  tripId = tripId,
  type = type,
  title = title,
  date = date,
  time = time,
  pdfUri = pdfUri,
  linkUrl = linkUrl,
  notes = notes,
)

fun ExpenseEntity.toDomain() = Expense(
  id = id,
  tripId = tripId,
  dayId = dayId,
  concept = concept,
  amount = amount,
  currency = currency,
  category = category,
  receiptUri = receiptUri,
)

fun Expense.toEntity() = ExpenseEntity(
  id = id,
  tripId = tripId,
  dayId = dayId,
  concept = concept,
  amount = amount,
  currency = currency,
  category = category,
  receiptUri = receiptUri,
)

fun JournalNoteEntity.toDomain() = JournalNote(
  id = id,
  dayId = dayId,
  text = text,
  photoUri = photoUri,
  audioUri = audioUri,
  latitude = latitude,
  longitude = longitude,
  createdAt = createdAt,
)

fun JournalNote.toEntity() = JournalNoteEntity(
  id = id,
  dayId = dayId,
  text = text,
  photoUri = photoUri,
  audioUri = audioUri,
  latitude = latitude,
  longitude = longitude,
  createdAt = createdAt,
)
