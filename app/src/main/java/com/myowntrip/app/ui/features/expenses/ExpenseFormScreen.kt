package com.myowntrip.app.ui.features.expenses

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.myowntrip.app.domain.model.ExpenseCategory
import com.myowntrip.app.ui.features.journal.JournalPermission
import com.myowntrip.app.ui.features.journal.TakePhotoScreen
import com.myowntrip.app.ui.features.journal.rememberJournalPermissionRequest
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTSpacing
import java.io.File

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ExpenseFormScreen(
  onBack: () -> Unit,
  onSaved: () -> Unit,
  onViewDocument: (source: String, title: String?) -> Unit = { _, _ -> },
  viewModel: ExpenseFormViewModel = hiltViewModel(),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  val snackbarHostState = remember { SnackbarHostState() }

  val requestCamera = rememberJournalPermissionRequest(
    permission = JournalPermission.Camera,
    onGranted = { viewModel.prepareCameraCapture() },
    onDenied = {},
  )

  val pickGalleryImage = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent(),
  ) { uri: Uri? ->
    if (uri != null) viewModel.importReceiptFromUri(uri)
  }

  LaunchedEffect(state.infoMessage) {
    state.infoMessage?.let {
      snackbarHostState.showSnackbar(it)
      viewModel.clearInfoMessage()
    }
  }

  if (state.showCamera && state.pendingPhotoFile != null) {
    TakePhotoScreen(
      outputFile = state.pendingPhotoFile!!,
      onPhotoSaved = viewModel::onReceiptCaptured,
      onCancel = viewModel::cancelCamera,
    )
    return
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Añadir gasto") },
        navigationIcon = {
          MOTIconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
          }
        },
      )
    },
    snackbarHost = { SnackbarHost(snackbarHostState) },
  ) { padding ->
    Column(
      modifier = Modifier
        .padding(padding)
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(MOTSpacing.screenHorizontal)
        .padding(bottom = MOTSpacing.screenContentBottom),
      verticalArrangement = Arrangement.spacedBy(MOTSpacing.layoutMd),
    ) {
      Text(
        text = "Registra el gasto en el momento. Puedes fotografiar el ticket del TPV o de la cafetería.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
      OutlinedTextField(
        value = state.amountText,
        onValueChange = viewModel::onAmountChange,
        label = { Text("Importe") },
        supportingText = { Text("EUR") },
        isError = state.amountError != null,
        modifier = Modifier.fillMaxWidth(),
      )
      state.amountError?.let { error ->
        Text(
          text = error,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.error,
        )
      }
      OutlinedTextField(
        value = state.concept,
        onValueChange = viewModel::onConceptChange,
        label = { Text("Concepto (opcional)") },
        placeholder = { Text("Ej. Café, taxi al aeropuerto") },
        modifier = Modifier.fillMaxWidth(),
      )
      Text(
        text = "Categoría",
        style = MaterialTheme.typography.labelLarge,
      )
      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
        verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
      ) {
        ExpenseCategory.entries.forEach { category ->
          FilterChip(
            selected = state.category == category,
            onClick = { viewModel.onCategoryChange(category) },
            label = { Text(expenseCategoryLabel(category)) },
          )
        }
      }
      Text(
        text = "Ticket o recibo",
        style = MaterialTheme.typography.labelLarge,
      )
      if (state.receiptPath == null) {
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
        ) {
          FilterChip(
            selected = false,
            onClick = requestCamera,
            label = { Text("Hacer foto") },
            leadingIcon = { Icon(Icons.Default.PhotoCamera, contentDescription = null) },
            modifier = Modifier.semantics { contentDescription = "Fotografiar ticket" },
          )
          FilterChip(
            selected = false,
            onClick = { pickGalleryImage.launch("image/*") },
            label = { Text("Galería") },
            leadingIcon = { Icon(Icons.Default.PhotoLibrary, contentDescription = null) },
            modifier = Modifier.semantics { contentDescription = "Elegir foto del ticket" },
          )
        }
      } else {
        AsyncImage(
          model = File(state.receiptPath!!),
          contentDescription = "Foto del ticket",
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
              onViewDocument(
                state.receiptPath!!,
                state.concept.ifBlank { "Ticket" },
              )
            },
        )
        Text(
          text = "Toca la imagen para verla a pantalla completa",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.tertiary,
        )
        FilterChip(
          selected = true,
          onClick = viewModel::removeReceipt,
          label = { Text("Quitar foto") },
        )
      }
      MOTButton(
        onClick = { viewModel.saveQuick(onSaved) },
        enabled = !state.isSaving,
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text(if (state.isSaving) "Guardando…" else "Guardar gasto")
      }
    }
  }
}
