package com.myowntrip.app.data.repository

import android.content.Context
import android.net.Uri
import com.myowntrip.app.data.local.dao.WalletEntryDao
import com.myowntrip.app.data.local.toDomain
import com.myowntrip.app.data.local.toEntity
import com.myowntrip.app.data.wallet.WalletDocumentContentReader
import com.myowntrip.app.data.wallet.WalletQrExtractor
import com.myowntrip.app.domain.model.EntryType
import com.myowntrip.app.domain.model.WalletEntry
import com.myowntrip.app.domain.wallet.ParsedWalletDocument
import com.myowntrip.app.domain.wallet.WalletDocumentParser
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

  fun observeById(entryId: String): Flow<WalletEntry?> =
    walletEntryDao.observeById(entryId).map { it?.toDomain() }

  suspend fun saveEntry(entry: WalletEntry) {
    walletEntryDao.insert(entry.toEntity())
  }

  suspend fun deleteEntry(entryId: String) {
    val entity = walletEntryDao.getById(entryId) ?: return
    deleteStoredFile(entity.pdfUri)
    walletEntryDao.deleteById(entryId)
  }

  fun deleteStoredFile(fileUri: String?) {
    if (fileUri.isNullOrBlank()) return
    runCatching {
      val uri = Uri.parse(fileUri)
      if (uri.scheme == "file") {
        uri.path?.let { File(it).delete() }
      }
    }
  }

  suspend fun copyAttachmentToTripStorage(tripId: String, sourceUri: Uri, fileName: String): String {
    val tripDir = File(context.filesDir, "trips/$tripId/wallet").apply { mkdirs() }
    val dest = File(tripDir, "${UUID.randomUUID()}_$fileName")
    context.contentResolver.openInputStream(sourceUri)?.use { input ->
      dest.outputStream().use { output -> input.copyTo(output) }
    } ?: error("Cannot read attachment")
    return dest.toURI().toString()
  }

  fun suggestEntryType(mimeType: String?, fileName: String?): EntryType =
    parseDocument(uri = null, mimeType = mimeType, fileName = fileName).type

  fun parseDocument(uri: Uri?, mimeType: String?, fileName: String?): ParsedWalletDocument {
    val contentText = uri?.let { WalletDocumentContentReader.readSearchableText(context, it, mimeType) }.orEmpty()
    val parsed = WalletDocumentParser.parse(fileName = fileName, mimeType = mimeType, contentText = contentText)
    val qrPayload = uri?.let { WalletQrExtractor.extract(context, it, mimeType) }
    return parsed.copy(qrPayload = qrPayload)
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
    qrPayload: String? = null,
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
    qrPayload = qrPayload,
  )
}
