package com.myowntrip.app.platform.media

import android.graphics.BitmapFactory
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

/**
 * OCR on-device con ML Kit (sin API key, sin conexión, sin cuenta de Google Cloud) para leer
 * el texto de la foto de un ticket. Solo extrae texto en bruto; [ReceiptParser] es quien
 * intenta sacarle un importe y una moneda.
 */
@Singleton
class ReceiptTextRecognizer @Inject constructor() {

  private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

  /** Devuelve el texto reconocido, o cadena vacía si la imagen no se pudo leer/procesar. */
  suspend fun recognizeText(imageFile: File): String {
    val bitmap = runCatching { BitmapFactory.decodeFile(imageFile.path) }.getOrNull() ?: return ""
    val image = InputImage.fromBitmap(bitmap, 0)
    return suspendCancellableCoroutine { cont ->
      recognizer.process(image)
        .addOnSuccessListener { result ->
          if (cont.isActive) cont.resume(result.text)
        }
        .addOnFailureListener {
          if (cont.isActive) cont.resume("")
        }
    }
  }
}
