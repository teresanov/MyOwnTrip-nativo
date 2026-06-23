package com.myowntrip.app.domain.wallet

import com.myowntrip.app.domain.model.WalletEntry
import com.myowntrip.app.platform.documents.resolveFileFromSource

/** Qué partes de una entrada Wallet están en el dispositivo sin red. */
enum class WalletOfflineAvailability {
  /** PDF o imagen guardados localmente. */
  LocalDocument,
  /** QR utilizable sin archivo adjunto. */
  QrCode,
  /** Archivo enlazado en la nube; requiere conexión para abrir. */
  CloudReference,
  /** Solo metadatos; sin archivo ni enlace. */
  MetadataOnly,
}

fun WalletEntry.offlineAvailability(): WalletOfflineAvailability {
  if (resolveFileFromSource(pdfUri.orEmpty()) != null) {
    return WalletOfflineAvailability.LocalDocument
  }
  if (!qrPayload.isNullOrBlank()) {
    return WalletOfflineAvailability.QrCode
  }
  if (!linkUrl.isNullOrBlank()) {
    return WalletOfflineAvailability.CloudReference
  }
  return WalletOfflineAvailability.MetadataOnly
}

fun WalletOfflineAvailability.isAvailableOffline(): Boolean =
  this == WalletOfflineAvailability.LocalDocument || this == WalletOfflineAvailability.QrCode

fun WalletOfflineAvailability.isCloudOnly(): Boolean =
  this == WalletOfflineAvailability.CloudReference

fun WalletOfflineAvailability.accessibilityLabel(): String? = when (this) {
  WalletOfflineAvailability.LocalDocument -> "Guardado en el teléfono, disponible sin conexión"
  WalletOfflineAvailability.QrCode -> "Código QR disponible sin conexión"
  WalletOfflineAvailability.CloudReference -> "En la nube, requiere conexión"
  WalletOfflineAvailability.MetadataOnly -> null
}
