package com.myowntrip.app.ui.features.wallet

import com.myowntrip.app.domain.model.EntryType

fun entryTypeLabel(type: EntryType): String = when (type) {
  EntryType.FLIGHT -> "Vuelo"
  EntryType.HOTEL -> "Hotel"
  EntryType.ACTIVITY -> "Actividad"
  EntryType.TRANSPORT -> "Transporte"
  EntryType.GENERIC -> "Documento"
}
