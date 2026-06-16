package com.myowntrip.app.ui.features.wallet

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

fun resolveAttachmentDisplayName(context: Context, uri: Uri): String? {
  if (uri.scheme == "content") {
    context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
      val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
      if (index >= 0 && cursor.moveToFirst()) {
        return cursor.getString(index)
      }
    }
  }
  return uri.lastPathSegment
}

fun resolveAttachmentMimeType(context: Context, uri: Uri): String? =
  context.contentResolver.getType(uri)
