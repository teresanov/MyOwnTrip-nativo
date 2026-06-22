package com.myowntrip.app.ui.features.journal

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.myowntrip.app.ui.theme.MOTIconButton
import java.io.File
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TakePhotoScreen(
  outputFile: File,
  onPhotoSaved: (String) -> Unit,
  onCancel: () -> Unit,
) {
  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current
  var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
  var captureError by remember { mutableStateOf<String?>(null) }
  val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

  DisposableEffect(Unit) {
    onDispose {
      cameraExecutor.shutdown()
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Hacer foto") },
        navigationIcon = {
          MOTIconButton(onClick = onCancel) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
          }
        },
      )
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = {
          val capture = imageCapture ?: return@FloatingActionButton
          val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()
          capture.takePicture(
            outputOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
              override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                onPhotoSaved(outputFile.absolutePath)
              }

              override fun onError(exception: ImageCaptureException) {
                Log.e("TakePhotoScreen", "Capture failed", exception)
                captureError = "No se pudo guardar la foto"
              }
            },
          )
        },
        modifier = Modifier.semantics { contentDescription = "Capturar foto" },
      ) {
        Icon(Icons.Default.CameraAlt, contentDescription = null)
      }
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
          bindCameraPreview(
            context = context,
            lifecycleOwner = lifecycleOwner,
            previewView = previewView,
            onBound = { imageCapture = it },
          )
        },
      )
      captureError?.let { message ->
        Text(
          text = message,
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          color = androidx.compose.material3.MaterialTheme.colorScheme.error,
        )
      }
    }
  }
}

private fun bindCameraPreview(
  context: Context,
  lifecycleOwner: androidx.lifecycle.LifecycleOwner,
  previewView: PreviewView,
  onBound: (ImageCapture) -> Unit,
) {
  val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
  cameraProviderFuture.addListener(
    {
      val cameraProvider = cameraProviderFuture.get()
      val preview = Preview.Builder().build().also {
        it.surfaceProvider = previewView.surfaceProvider
      }
      val capture = ImageCapture.Builder()
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
        .build()
      try {
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
          lifecycleOwner,
          CameraSelector.DEFAULT_BACK_CAMERA,
          preview,
          capture,
        )
        onBound(capture)
      } catch (exception: Exception) {
        Log.e("TakePhotoScreen", "Camera bind failed", exception)
      }
    },
    ContextCompat.getMainExecutor(context),
  )
}
