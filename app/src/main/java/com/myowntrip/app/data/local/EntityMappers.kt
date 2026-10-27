package com.myowntrip.app.data.local

import com.myowntrip.app.data.local.entity.DayEntity
import com.myowntrip.app.data.local.entity.ExpenseEntity
import com.myowntrip.app.data.local.entity.ItineraryBlockEntity
import com.myowntrip.app.data.local.entity.JournalNoteEntity
import com.myowntrip.app.data.local.entity.JournalPageEntity
import com.myowntrip.app.data.local.entity.RestaurantEntity
import com.myowntrip.app.data.local.entity.TripEntity
import com.myowntrip.app.data.local.entity.WalletEntryEntity
import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.Expense
import com.myowntrip.app.domain.model.ItineraryBlock
import com.myowntrip.app.domain.model.JournalNote
import com.myowntrip.app.domain.model.JournalPage
import com.myowntrip.app.domain.model.JournalPieceType
import com.myowntrip.app.domain.model.JournalTextStyle
import com.myowntrip.app.domain.model.Restaurant
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
  archivedAt = archivedAt,
)

fun Trip.toEntity() = TripEntity(
  id = id,
  name = name,
  destination = destination,
  startDate = startDate,
  endDate = endDate,
  coverPhoto = coverPhoto,
  createdAt = createdAt,
  archivedAt = archivedAt,
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
  qrPayload = qrPayload,
  amount = amount,
  currency = currency,
  archivedAt = archivedAt,
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
  qrPayload = qrPayload,
  amount = amount,
  currency = currency,
  archivedAt = archivedAt,
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
  walletEntryId = walletEntryId,
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
  walletEntryId = walletEntryId,
)

fun JournalNoteEntity.toDomain() = JournalNote(
  id = id,
  dayId = dayId,
  text = text,
  pieceType = pieceType.toJournalPieceType(),
  photoUri = photoUri,
  audioUri = audioUri,
  sketchUri = sketchUri,
  stickerUri = stickerUri,
  planBlockId = planBlockId,
  latitude = latitude,
  longitude = longitude,
  createdAt = createdAt,
  textStyle = textStyle.toJournalTextStyle(),
  pageId = pageId,
  canvasX = canvasX,
  canvasY = canvasY,
  rotation = rotation,
  scale = scale,
  zIndex = zIndex,
)

fun JournalNote.toEntity() = JournalNoteEntity(
  id = id,
  dayId = dayId,
  text = text,
  pieceType = pieceType.name,
  photoUri = photoUri,
  audioUri = audioUri,
  sketchUri = sketchUri,
  stickerUri = stickerUri,
  planBlockId = planBlockId,
  latitude = latitude,
  longitude = longitude,
  createdAt = createdAt,
  textStyle = textStyle.name,
  pageId = pageId,
  canvasX = canvasX,
  canvasY = canvasY,
  rotation = rotation,
  scale = scale,
  zIndex = zIndex,
)

fun JournalPageEntity.toDomain() = JournalPage(
  id = id,
  tripId = tripId,
  dayId = dayId,
  pageIndex = pageIndex,
  paperStyle = paperStyle,
)

fun JournalPage.toEntity() = JournalPageEntity(
  id = id,
  tripId = tripId,
  dayId = dayId,
  pageIndex = pageIndex,
  paperStyle = paperStyle,
)

private fun String.toJournalPieceType(): JournalPieceType =
  runCatching { JournalPieceType.valueOf(this) }.getOrDefault(JournalPieceType.TEXT)

private fun String.toJournalTextStyle(): JournalTextStyle =
  runCatching { JournalTextStyle.valueOf(this) }.getOrDefault(JournalTextStyle.HAND)

fun ItineraryBlockEntity.toDomain() = ItineraryBlock(
  id = id,
  dayId = dayId,
  title = title,
  timeLabel = timeLabel,
  sortOrder = sortOrder,
  walletEntryId = walletEntryId,
)

fun ItineraryBlock.toEntity() = ItineraryBlockEntity(
  id = id,
  dayId = dayId,
  title = title,
  timeLabel = timeLabel,
  sortOrder = sortOrder,
  walletEntryId = walletEntryId,
)

fun RestaurantEntity.toDomain() = Restaurant(
  id = id,
  tripId = tripId,
  dayId = dayId,
  name = name,
  address = address,
  status = status,
  notes = notes,
  category = category,
  latitude = latitude,
  longitude = longitude,
  placeId = placeId,
  phone = phone,
  website = website,
  googleMapsUri = googleMapsUri,
  rating = rating,
)

fun Restaurant.toEntity() = RestaurantEntity(
  id = id,
  tripId = tripId,
  dayId = dayId,
  name = name,
  address = address,
  status = status,
  notes = notes,
  category = category,
  latitude = latitude,
  longitude = longitude,
  placeId = placeId,
  phone = phone,
  website = website,
  googleMapsUri = googleMapsUri,
  rating = rating,
)
