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
import com.myowntrip.app.platform.documents.resolveFileFromSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

data class WalletDocumentStorage(
  val pdfUri: String?,
  val linkUrl: String?,
)

@Singleton
class WalletRepository @Inject constructor(
  @ApplicationContext private val context: Context,
  private val walletEntryDao: WalletEntryDao,
  private val tripRepository: TripRepository,
  private val planPlacementService: PlanPlacementService,
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

  suspend fun archiveEntry(entryId: String) {
    val existing = walletEntryDao.getById(entryId)?.toDomain() ?: return
    walletEntryDao.insert(existing.copy(archivedAt = System.currentTimeMillis()).toEntity())
  }

  suspend fun unarchiveEntry(entryId: String) {
    val existing = walletEntryDao.getById(entryId)?.toDomain() ?: return
    walletEntryDao.insert(existing.copy(archivedAt = null).toEntity())
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

  suspend fun persistWalletDocument(
    tripId: String,
    sourceUri: Uri,
    fileName: String,
    saveOfflineCopy: Boolean,
  ): WalletDocumentStorage {
    if (!saveOfflineCopy) {
      return WalletDocumentStorage(pdfUri = null, linkUrl = sourceUri.toString())
    }
    val existingLocal = existingAppLocalUri(sourceUri)
    if (existingLocal != null) {
      return WalletDocumentStorage(pdfUri = existingLocal, linkUrl = null)
    }
    val local = copyAttachmentToTripStorage(tripId, sourceUri, fileName)
    return WalletDocumentStorage(pdfUri = local, linkUrl = null)
  }

  private fun existingAppLocalUri(sourceUri: Uri): String? {
    if (sourceUri.scheme != "file") return null
    val path = sourceUri.path ?: return null
    val file = File(path)
    if (!file.exists()) return null
    val filesRoot = context.filesDir.absolutePath
    return if (path.startsWith(filesRoot)) sourceUri.toString() else null
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

  suspend fun importDebugWalletSamples(tripId: String): Int {
    val assetDir = "samples/wallet"
    val fileNames = context.assets.list(assetDir)?.filter { !it.startsWith('.') }.orEmpty()
    if (fileNames.isEmpty()) return 0
    var imported = 0
    for (fileName in fileNames.sorted()) {
      val assetPath = "$assetDir/$fileName"
      val storedUri = copyAssetToTripStorage(tripId, assetPath, fileName)
      val file = resolveFileFromSource(storedUri)
      val uri = file?.let { Uri.fromFile(it) }
      val mimeType = when (fileName.substringAfterLast('.').lowercase()) {
        "pdf" -> "application/pdf"
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        else -> null
      }
      val parsed = parseDocument(uri, mimeType, fileName)
      val entry = buildEntry(
        tripId = tripId,
        type = parsed.type,
        title = parsed.title,
        date = parsed.date,
        time = parsed.time,
        fileUri = storedUri,
        linkUrl = null,
        notes = parsed.notes,
        qrPayload = parsed.qrPayload,
      )
      saveEntry(entry)
      val days = tripRepository.getDaysForTrip(tripId)
      planPlacementService.apply(
        entry = entry,
        days = days,
        enabled = true,
        dayIdOverride = null,
        timeOverride = null,
      )
      imported++
    }
    return imported
  }

  private fun copyAssetToTripStorage(tripId: String, assetPath: String, fileName: String): String {
    val tripDir = File(context.filesDir, "trips/$tripId/wallet").apply { mkdirs() }
    val dest = File(tripDir, "${UUID.randomUUID()}_$fileName")
    context.assets.open(assetPath).use { input ->
      dest.outputStream().use { output -> input.copyTo(output) }
    }
    return dest.toURI().toString()
  }
}
