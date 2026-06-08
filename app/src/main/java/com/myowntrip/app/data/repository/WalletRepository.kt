package com.myowntrip.app.data.repository

import android.content.Context
import android.net.Uri
import com.myowntrip.app.data.local.dao.WalletEntryDao
import com.myowntrip.app.data.local.toDomain
import com.myowntrip.app.data.local.toEntity
import com.myowntrip.app.domain.model.EntryType
import com.myowntrip.app.domain.model.WalletEntry
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalletRepository @Inject constructor(
  @ApplicationContext private val context: Context,
  private val walletEntryDao: WalletEntryDao,
) {
  fun observeByTrip(tripId: String): Flow<List<WalletEntry>> =
    walletEntryDao.observeByTrip(tripId).map { list -> list.map { it.toDomain() } }

  suspend fun saveEntry(entry: WalletEntry) {
    walletEntryDao.insert(entry.toEntity())
  }

  suspend fun copyAttachmentToTripStorage(tripId: String, sourceUri: Uri, fileName: String): String {
    val tripDir = File(context.filesDir, "trips/$tripId/wallet").apply { mkdirs() }
    val dest = File(tripDir, "${UUID.randomUUID()}_$fileName")
    context.contentResolver.openInputStream(sourceUri)?.use { input ->
      dest.outputStream().use { output -> input.copyTo(output) }
    } ?: error("Cannot read attachment")
    return dest.toURI().toString()
  }

  fun suggestEntryType(mimeType: String?, fileName: String?): EntryType {
    val name = fileName?.lowercase().orEmpty()
    return when {
      mimeType?.contains("pdf") == true && (name.contains("flight") || name.contains("boarding")) ->
        EntryType.FLIGHT
      name.contains("hotel") || name.contains("booking") -> EntryType.HOTEL
      name.contains("train") || name.contains("bus") || name.contains("rental") -> EntryType.TRANSPORT
      name.contains("ticket") || name.contains("event") -> EntryType.ACTIVITY
      else -> EntryType.GENERIC
    }
  }

  fun buildEntry(
    tripId: String,
    type: EntryType,
    title: String,
    date: LocalDate?,
    time: LocalTime?,
    fileUri: String?,
    linkUrl: String?,
    notes: String?,
  ): WalletEntry = WalletEntry(
    id = UUID.randomUUID().toString(),
    tripId = tripId,
    type = type,
    title = title,
    date = date,
    time = time,
    pdfUri = fileUri,
    linkUrl = linkUrl,
    notes = notes,
  )
}
