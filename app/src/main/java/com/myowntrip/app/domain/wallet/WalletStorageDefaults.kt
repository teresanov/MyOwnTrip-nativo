package com.myowntrip.app.domain.wallet

import android.net.Uri
import com.myowntrip.app.domain.model.EntryType

/** Por defecto, billetes y reservas críticas se guardan en el dispositivo. */
fun EntryType.defaultSaveOfflineCopy(): Boolean = when (this) {
  EntryType.FLIGHT,
  EntryType.HOTEL,
  EntryType.ACTIVITY,
  -> true
  EntryType.TRANSPORT,
  EntryType.GENERIC,
  -> false
}

/** Enlace a nube (Drive, Dropbox…) vs archivo ya en el teléfono. */
fun Uri.canChooseCloudStorage(): Boolean = when (scheme?.lowercase()) {
  "content", "http", "https" -> true
  else -> false
}
