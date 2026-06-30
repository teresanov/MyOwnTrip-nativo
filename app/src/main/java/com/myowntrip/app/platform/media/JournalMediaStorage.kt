package com.myowntrip.app.platform.media

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.net.Uri
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/** Forma del recorte de un sticker (se hornea en el PNG con transparencia). */
enum class StickerShape { CIRCLE, ROUNDED, PHOTO_FRAME }

@Singleton
class JournalMediaStorage @Inject constructor(
  @ApplicationContext private val context: Context,
) {
  fun createPhotoFile(): File {
    val dir = File(context.filesDir, "journal/photos").apply { mkdirs() }
    return File(dir, "${UUID.randomUUID()}.jpg")
  }

  fun createAudioFile(): File {
    val dir = File(context.filesDir, "journal/audio").apply { mkdirs() }
    return File(dir, "${UUID.randomUUID()}.m4a")
  }

  fun createSketchFile(): File {
    val dir = File(context.filesDir, "journal/sketches").apply { mkdirs() }
    return File(dir, "${UUID.randomUUID()}.png")
  }

  fun createStickerFile(): File {
    val dir = File(context.filesDir, "journal/stickers").apply { mkdirs() }
    return File(dir, "${UUID.randomUUID()}.png")
  }

  /**
   * Recorta la foto a la [shape] elegida y guarda un PNG con transparencia (el "sticker").
   * CIRCLE/ROUNDED enmascaran con bordes suaves (anti-alias); PHOTO_FRAME añade un marco
   * blanco tipo foto instantánea. Hace center-crop a cuadrado y limita el tamaño.
   */
  fun maskPhotoAsSticker(sourcePhotoPath: String, shape: StickerShape): String? {
    val source = File(sourcePhotoPath)
    if (!source.exists()) return null
    val destination = createStickerFile()
    return try {
      val decoded = BitmapFactory.decodeFile(sourcePhotoPath) ?: return null
      val side = minOf(decoded.width, decoded.height)
      val cropped = Bitmap.createBitmap(
        decoded,
        (decoded.width - side) / 2,
        (decoded.height - side) / 2,
        side,
        side,
      )
      val maxDim = 1024
      val photo = if (side > maxDim) {
        Bitmap.createScaledBitmap(cropped, maxDim, maxDim, true)
      } else {
        cropped
      }

      val out = Bitmap.createBitmap(photo.width, photo.height, Bitmap.Config.ARGB_8888)
      val canvas = Canvas(out)
      val w = out.width.toFloat()
      val h = out.height.toFloat()
      when (shape) {
        StickerShape.CIRCLE, StickerShape.ROUNDED -> {
          canvas.drawBitmap(photo, 0f, 0f, null)
          val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
          }
          val maskPath = Path().apply {
            if (shape == StickerShape.CIRCLE) {
              addCircle(w / 2f, h / 2f, minOf(w, h) / 2f, Path.Direction.CW)
            } else {
              val r = w * 0.14f
              addRoundRect(RectF(0f, 0f, w, h), r, r, Path.Direction.CW)
            }
          }
          canvas.drawPath(maskPath, maskPaint)
        }
        StickerShape.PHOTO_FRAME -> {
          canvas.drawColor(Color.WHITE)
          val border = w * 0.06f
          val inner = RectF(border, border, w - border, h - border * 2.4f)
          canvas.drawBitmap(photo, null, inner, Paint(Paint.FILTER_BITMAP_FLAG))
        }
      }

      FileOutputStream(destination.absolutePath).use { stream ->
        out.compress(Bitmap.CompressFormat.PNG, 100, stream)
      }
      destination.absolutePath
    } catch (_: Exception) {
      destination.delete()
      null
    }
  }

  fun copyImageFromUri(uri: Uri): String? {
    val destination = createPhotoFile()
    return copyImageToFile(uri, destination)
  }

  fun createReceiptFile(tripId: String): File {
    val dir = File(context.filesDir, "trips/$tripId/receipts").apply { mkdirs() }
    return File(dir, "${UUID.randomUUID()}.jpg")
  }

  fun copyReceiptFromUri(tripId: String, uri: Uri): String? {
    val destination = createReceiptFile(tripId)
    return copyImageToFile(uri, destination)
  }

  private fun copyImageToFile(uri: Uri, destination: File): String? {
    return try {
      context.contentResolver.openInputStream(uri)?.use { input ->
        destination.outputStream().use { output -> input.copyTo(output) }
      } ?: return null
      destination.absolutePath
    } catch (_: Exception) {
      destination.delete()
      null
    }
  }

  fun createWalletPhotoFile(tripId: String): File {
    val dir = File(context.filesDir, "trips/$tripId/wallet").apply { mkdirs() }
    return File(dir, "${UUID.randomUUID()}_entrada.jpg")
  }

  fun uriForFile(file: File): Uri =
    FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}
