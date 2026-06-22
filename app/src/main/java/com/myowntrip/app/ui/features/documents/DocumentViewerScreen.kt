package com.myowntrip.app.ui.features.documents

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.myowntrip.app.platform.documents.DocumentKind
import com.myowntrip.app.platform.documents.ExternalOpenResult
import com.myowntrip.app.platform.documents.ResolvedDocument
import com.myowntrip.app.platform.documents.openDocumentExternally
import com.myowntrip.app.platform.documents.resolveLocalDocument
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MOTTextButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentViewerScreen(
  source: String,
  title: String?,
  onBack: () -> Unit,
) {
  val context = LocalContext.current
  val document = remember(source, title) { resolveLocalDocument(context, source, title) }
  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()

  val openExternal: () -> Unit = {
    document?.let { doc ->
      when (openDocumentExternally(context, doc)) {
        ExternalOpenResult.Launched -> Unit
        ExternalOpenResult.NoAppAvailable -> scope.launch {
          snackbarHostState.showSnackbar(
            "No hay ninguna app instalada para abrir este archivo. Ya lo estás viendo aquí dentro de MyOwnTrip.",
          )
        }
        ExternalOpenResult.Failed -> scope.launch {
          snackbarHostState.showSnackbar("No se pudo abrir con otra app.")
        }
      }
    }
  }

  val showExternalAction = document?.kind == DocumentKind.OTHER

  Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
    topBar = {
      TopAppBar(
        title = { Text(document?.displayName ?: title ?: "Documento") },
        navigationIcon = {
          MOTIconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
          }
        },
        actions = {
          if (document != null && showExternalAction) {
            MOTIconButton(onClick = openExternal) {
              Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = "Abrir con otra app")
            }
          }
        },
      )
    },
  ) { padding ->
    Box(
      modifier = Modifier
        .padding(padding)
        .fillMaxSize(),
    ) {
      when {
        document == null -> {
          MissingDocumentMessage()
        }
        document.kind == DocumentKind.IMAGE -> {
          AsyncImage(
            model = document.file ?: document.uri,
            contentDescription = "Imagen del documento",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize(),
          )
        }
        document.kind == DocumentKind.PDF -> {
          PdfPagesViewer(
            document = document,
            onOpenExternal = openExternal,
          )
        }
        else -> {
          UnsupportedDocumentMessage(
            document = document,
            onOpenExternal = openExternal,
          )
        }
      }
    }
  }
}

@Composable
private fun MissingDocumentMessage() {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(MOTSpacing.screenHorizontal),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      "No se encontró el archivo en el dispositivo.",
      style = MaterialTheme.typography.bodyLarge,
      color = MaterialTheme.colorScheme.error,
    )
  }
}

@Composable
private fun PdfPagesViewer(
  document: ResolvedDocument,
  onOpenExternal: () -> Unit,
) {
  val context = LocalContext.current
  var pages by remember { mutableStateOf<List<Bitmap>?>(null) }
  var error by remember { mutableStateOf<String?>(null) }

  DisposableEffect(document) {
    val bitmaps = mutableListOf<Bitmap>()
    val pfd = openPdfDescriptor(context, document)
    if (pfd == null) {
      error = "No se pudo abrir el PDF en el visor."
      return@DisposableEffect onDispose {}
    }
    runCatching {
      PdfRenderer(pfd).use { renderer ->
        if (renderer.pageCount == 0) {
          error = "El PDF no tiene páginas."
          return@runCatching
        }
        for (index in 0 until renderer.pageCount) {
          renderer.openPage(index).use { page ->
            val scale = 2
            val bitmap = Bitmap.createBitmap(
              page.width * scale,
              page.height * scale,
              Bitmap.Config.ARGB_8888,
            )
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            bitmaps.add(bitmap)
          }
        }
      }
    }.onFailure {
      error = "No se pudo mostrar el PDF."
      bitmaps.forEach { it.recycle() }
      bitmaps.clear()
    }
    if (error == null) {
      pages = bitmaps
    }
    onDispose {
      bitmaps.forEach { it.recycle() }
    }
  }

  when {
    error != null -> {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(MOTSpacing.screenHorizontal),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Text(error!!, color = MaterialTheme.colorScheme.error)
        Text(
          "No necesitas otra app: el documento está guardado en MyOwnTrip. Si el problema continúa, vuelve a importarlo.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(top = MOTSpacing.componentSm),
        )
        MOTTextButton(
          onClick = onOpenExternal,
          modifier = Modifier.padding(top = MOTSpacing.layoutMd),
        ) {
          Text("Probar con otra app (opcional)")
        }
      }
    }
    pages == null -> {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
      }
    }
    else -> {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
          .padding(vertical = MOTSpacing.layoutMd),
        verticalArrangement = Arrangement.spacedBy(MOTSpacing.layoutMd),
      ) {
        pages!!.forEachIndexed { index, bitmap ->
          Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Página ${index + 1}",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth,
          )
        }
      }
    }
  }
}

@Composable
private fun UnsupportedDocumentMessage(
  document: ResolvedDocument,
  onOpenExternal: () -> Unit,
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(MOTSpacing.screenHorizontal),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(document.displayName, style = MaterialTheme.typography.titleMedium)
    Text(
      "Este formato no tiene visor integrado en MyOwnTrip.",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.padding(top = MOTSpacing.componentSm),
    )
    MOTTextButton(
      onClick = onOpenExternal,
      modifier = Modifier.padding(top = MOTSpacing.layoutMd),
    ) {
      Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null)
      Text("Probar con otra app")
    }
  }
}

private fun openPdfDescriptor(
  context: android.content.Context,
  document: ResolvedDocument,
): ParcelFileDescriptor? =
  runCatching {
    when {
      document.file != null ->
        ParcelFileDescriptor.open(document.file, ParcelFileDescriptor.MODE_READ_ONLY)
      document.uri.scheme == "content" ->
        context.contentResolver.openFileDescriptor(document.uri, "r")
      else -> null
    }
  }.getOrNull()
