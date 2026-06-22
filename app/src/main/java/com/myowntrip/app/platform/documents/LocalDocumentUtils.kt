package com.myowntrip.app.platform.documents

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

enum class DocumentKind {
  IMAGE,
  PDF,
  OTHER,
}

data class ResolvedDocument(
  val displayName: String,
  val kind: DocumentKind,
  val file: File?,
  val uri: Uri,
)

enum class ExternalOpenResult {
  Launched,
  NoAppAvailable,
  Failed,
}

fun documentKindFromName(fileName: String?): DocumentKind {
  val ext = fileName?.substringAfterLast('.', "")?.lowercase().orEmpty()
  return when (ext) {
    "jpg", "jpeg", "png", "webp", "gif", "heic" -> DocumentKind.IMAGE
    "pdf" -> DocumentKind.PDF
    else -> DocumentKind.OTHER
  }
}

fun documentKindFromMime(mimeType: String?): DocumentKind? = when {
  mimeType == "application/pdf" -> DocumentKind.PDF
  mimeType?.startsWith("image/") == true -> DocumentKind.IMAGE
  else -> null
}

fun fileNameFromSource(source: String): String? {
  val uri = runCatching { Uri.parse(source) }.getOrNull() ?: return null
  return when {
    uri.lastPathSegment != null -> uri.lastPathSegment
    uri.scheme == "file" -> uri.path?.substringAfterLast('/')
    else -> File(source).name.takeIf { it.isNotBlank() }
  }
}

fun resolveFileFromSource(source: String): File? {
  if (source.isBlank()) return null
  val uri = runCatching { Uri.parse(source) }.getOrNull()
  val fromUri = when (uri?.scheme) {
    "file" -> uri.path?.let(::File)
    null, "" -> File(source)
    else -> null
  }?.takeIf { it.exists() }
  if (fromUri != null) return fromUri
  return File(source).takeIf { it.exists() }
}

fun resolveLocalDocument(
  context: Context,
  source: String,
  displayName: String? = null,
): ResolvedDocument? {
  val uri = runCatching { Uri.parse(source) }.getOrNull()
  val pathFileName = fileNameFromSource(source)
  val storageFileName = pathFileName?.substringAfter('_')?.takeIf { it.contains('.') }
    ?: pathFileName
  val name = displayName?.takeIf { it.isNotBlank() }
    ?: storageFileName
    ?: "Documento"
  val mimeType = uri?.let { context.contentResolver.getType(it) }
  val mimeKind = documentKindFromMime(mimeType)
  val kind = mimeKind
    ?: documentKindFromName(pathFileName).takeUnless { it == DocumentKind.OTHER }
    ?: documentKindFromName(storageFileName).takeUnless { it == DocumentKind.OTHER }
    ?: documentKindFromName(displayName)
    ?: DocumentKind.OTHER

  val file = resolveFileFromSource(source)
  if (file != null) {
    val resolvedKind = documentKindFromName(file.name).takeUnless { it == DocumentKind.OTHER } ?: kind
    return ResolvedDocument(
      displayName = name,
      kind = resolvedKind,
      file = file,
      uri = Uri.fromFile(file),
    )
  }

  if (uri != null && uri.scheme == "content") {
    val resolvedKind = mimeKind ?: kind
    return ResolvedDocument(displayName = name, kind = resolvedKind, file = null, uri = uri)
  }

  return null
}

fun openDocumentExternally(context: Context, document: ResolvedDocument): ExternalOpenResult {
  val shareUri = when {
    document.file != null -> FileProvider.getUriForFile(
      context,
      "${context.packageName}.fileprovider",
      document.file,
    )
    else -> document.uri
  }
  val mime = when (document.kind) {
    DocumentKind.IMAGE -> "image/*"
    DocumentKind.PDF -> "application/pdf"
    DocumentKind.OTHER -> "*/*"
  }
  val intent = Intent(Intent.ACTION_VIEW).apply {
    setDataAndType(shareUri, mime)
    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
  }
  return try {
    context.startActivity(Intent.createChooser(intent, "Abrir con"))
    ExternalOpenResult.Launched
  } catch (_: ActivityNotFoundException) {
    ExternalOpenResult.NoAppAvailable
  } catch (_: Exception) {
    ExternalOpenResult.Failed
  }
}
