package com.myowntrip.app.ui.features.wallet

import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.myowntrip.app.ui.theme.MOTIconButton
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrScanScreen(
  onQrScanned: (String) -> Unit,
  onCancel: () -> Unit,
) {
  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current
  var scanError by remember { mutableStateOf<String?>(null) }
  val scanned = remember { AtomicBoolean(false) }
  val analyzerExecutor = remember { Executors.newSingleThreadExecutor() }

  DisposableEffect(Unit) {
    onDispose { analyzerExecutor.shutdown() }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Escanear QR") },
        navigationIcon = {
          MOTIconButton(onClick = onCancel) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
          }
        },
      )
    },
  ) { padding ->
    Box(
      modifier = Modifier
        .padding(padding)
        .fillMaxSize(),
      contentAlignment = Alignment.BottomCenter,
    ) {
      AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
          PreviewView(ctx).apply {
            layoutParams = ViewGroup.LayoutParams(
              ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT,
            )
            scaleType = PreviewView.ScaleType.FILL_CENTER
          }
        },
        update = { previewView ->
          bindQrScanner(
            context = context,
            lifecycleOwner = lifecycleOwner,
            previewView = previewView,
            executor = analyzerExecutor,
            onDecoded = { payload ->
              if (scanned.compareAndSet(false, true)) {
                onQrScanned(payload)
              }
            },
            onError = { scanError = it },
          )
        },
      )
      ColumnHint(scanError)
    }
  }
}

@Composable
private fun ColumnHint(error: String?) {
  Text(
    text = error ?: "Apunta la cámara al código QR de la entrada o billete.",
    style = MaterialTheme.typography.bodyMedium,
    color = if (error != null) {
      MaterialTheme.colorScheme.error
    } else {
      MaterialTheme.colorScheme.onSurface
    },
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp),
  )
}

private fun bindQrScanner(
  context: android.content.Context,
  lifecycleOwner: androidx.lifecycle.LifecycleOwner,
  previewView: PreviewView,
  executor: java.util.concurrent.ExecutorService,
  onDecoded: (String) -> Unit,
  onError: (String) -> Unit,
) {
  val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
  cameraProviderFuture.addListener(
    {
      val cameraProvider = cameraProviderFuture.get()
      val preview = Preview.Builder().build().also {
        it.surfaceProvider = previewView.surfaceProvider
      }
      val analysis = ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()
      val reader = MultiFormatReader().apply {
        setHints(
          mapOf(
            DecodeHintType.POSSIBLE_FORMATS to listOf(BarcodeFormat.QR_CODE),
            DecodeHintType.TRY_HARDER to true,
          ),
        )
      }
      analysis.setAnalyzer(executor) { imageProxy ->
        try {
          val plane = imageProxy.planes[0]
          val buffer = plane.buffer
          val data = ByteArray(buffer.remaining())
          buffer.get(data)
          val source = PlanarYUVLuminanceSource(
            data,
            imageProxy.width,
            imageProxy.height,
            0,
            0,
            imageProxy.width,
            imageProxy.height,
            false,
          )
          val result = reader.decodeWithState(BinaryBitmap(HybridBinarizer(source)))
          onDecoded(result.text)
        } catch (_: Exception) {
          // keep scanning
        } finally {
          imageProxy.close()
        }
      }
      try {
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
          lifecycleOwner,
          CameraSelector.DEFAULT_BACK_CAMERA,
          preview,
          analysis,
        )
      } catch (exception: Exception) {
        Log.e("QrScanScreen", "Camera bind failed", exception)
        onError("No se pudo usar la cámara")
      }
    },
    ContextCompat.getMainExecutor(context),
  )
}
