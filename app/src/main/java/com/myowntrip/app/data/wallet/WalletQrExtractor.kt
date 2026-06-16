package com.myowntrip.app.data.wallet

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.BarcodeFormat

object WalletQrExtractor {

  fun extract(context: Context, uri: Uri, mimeType: String?): String? {
    return when {
      mimeType == "application/pdf" -> extractFromPdf(context, uri)
      mimeType?.startsWith("image/") == true -> extractFromImage(context, uri)
      else -> extractFromPdf(context, uri) ?: extractFromImage(context, uri)
    }
  }

  private fun extractFromPdf(context: Context, uri: Uri): String? {
    val pfd = openDescriptor(context, uri) ?: return null
    return pfd.use { descriptor ->
      PdfRenderer(descriptor).use { renderer ->
        if (renderer.pageCount == 0) return null
        renderer.openPage(0).use { page ->
          val scale = 2
          val width = page.width * scale
          val height = page.height * scale
          val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
          bitmap.eraseColor(Color.WHITE)
          page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
          decodeFromBitmap(bitmap)
        }
      }
    }
  }

  private fun extractFromImage(context: Context, uri: Uri): String? {
    val bitmap = context.contentResolver.openInputStream(uri)?.use { stream ->
      BitmapFactory.decodeStream(stream)
    } ?: return null
    return decodeFromBitmap(bitmap)
  }

  private fun decodeFromBitmap(bitmap: Bitmap): String? {
    val hints = mapOf(
      DecodeHintType.POSSIBLE_FORMATS to listOf(BarcodeFormat.QR_CODE),
      DecodeHintType.TRY_HARDER to true,
    )
    val source = RGBLuminanceSource(bitmap.width, bitmap.height, bitmap.toArgbPixels())
    val binary = BinaryBitmap(HybridBinarizer(source))
    return runCatching {
      MultiFormatReader().apply { setHints(hints) }.decode(binary).text
    }.getOrNull()
  }

  private fun Bitmap.toArgbPixels(): IntArray {
    val pixels = IntArray(width * height)
    getPixels(pixels, 0, width, 0, 0, width, height)
    return pixels
  }

  private fun openDescriptor(context: Context, uri: Uri): ParcelFileDescriptor? =
    runCatching {
      context.contentResolver.openFileDescriptor(uri, "r")
    }.getOrNull()
}
