package com.myowntrip.app.data.wallet

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

object WalletQrEncoder {

  fun encodeBitmap(payload: String, sizePx: Int = 512): Bitmap? {
    if (payload.isBlank()) return null
    return runCatching {
      val hints = mapOf(EncodeHintType.MARGIN to 1, EncodeHintType.CHARACTER_SET to "UTF-8")
      val matrix = MultiFormatWriter().encode(payload, BarcodeFormat.QR_CODE, sizePx, sizePx, hints)
      matrix.toBitmap()
    }.getOrNull()
  }

  private fun BitMatrix.toBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    for (x in 0 until width) {
      for (y in 0 until height) {
        bitmap.setPixel(x, y, if (get(x, y)) Color.BLACK else Color.WHITE)
      }
    }
    return bitmap
  }
}
