package com.myowntrip.app.ui.features.journal

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTIconButton
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalAddScreen(
  onBack: () -> Unit,
  onSaved: () -> Unit,
  viewModel: JournalAddViewModel = hiltViewModel(),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  val context = LocalContext.current
  val snackbarHostState = remember { SnackbarHostState() }

  val hasLocationPermission = ContextCompat.checkSelfPermission(
    context,
    Manifest.permission.ACCESS_FINE_LOCATION,
  ) == PackageManager.PERMISSION_GRANTED

  val requestCamera = rememberJournalPermissionRequest(
    permission = JournalPermission.Camera,
    onGranted = { viewModel.prepareCameraCapture() },
    onDenied = { viewModel.onPermissionDenied(JournalPermission.Camera) },
  )
  val requestAudio = rememberJournalPermissionRequest(
    permission = JournalPermission.Audio,
    onGranted = { viewModel.startRecording() },
    onDenied = { viewModel.onPermissionDenied(JournalPermission.Audio) },
  )
  val requestLocation = rememberJournalPermissionRequest(
    permission = JournalPermission.Location,
    onGranted = { viewModel.toggleAttachLocation(true) },
    onDenied = {
      viewModel.toggleAttachLocation(false)
      viewModel.onPermissionDenied(JournalPermission.Location)
    },
  )
  val pickGalleryImage = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent(),
  ) { uri: Uri? ->
    if (uri != null) {
      viewModel.importPhotoFromUri(uri)
    }
  }

  LaunchedEffect(state.infoMessage, state.permissionMessage) {
    val message = state.permissionMessage ?: state.infoMessage
    if (message != null) {
      snackbarHostState.showSnackbar(message)
      viewModel.clearMessages()
    }
  }

  if (state.showCamera && state.pendingPhotoFile != null) {
    TakePhotoScreen(
      outputFile = state.pendingPhotoFile!!,
      onPhotoSaved = viewModel::onPhotoCaptured,
      onCancel = viewModel::cancelCamera,
    )
    return
  }

  if (state.isRecording) {
    JournalRecordingDialog(
      recordingLevels = state.recordingLevels,
      onStop = viewModel::stopRecording,
    )
  }

  if (state.isLoading) {
    Scaffold(
      topBar = {
        TopAppBar(
          title = { Text(if (viewModel.isEditMode) "Editar nota" else "Nueva nota") },
          navigationIcon = {
            MOTIconButton(onClick = onBack) {
              Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
          },
        )
      },
    ) { padding ->
      Column(
        modifier = Modifier
          .padding(padding)
          .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        CircularProgressIndicator()
      }
    }
    return
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(if (viewModel.isEditMode) "Editar nota" else "Nueva nota") },
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
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      OutlinedTextField(
        value = state.text,
        onValueChange = viewModel::onTextChange,
        label = { Text("¿Qué pasó hoy?") },
        isError = state.textError != null,
        supportingText = state.textError?.let { { Text(it) } },
        modifier = Modifier.fillMaxWidth(),
      )

      Text(
        text = "Adjuntar desde el móvil",
        style = MaterialTheme.typography.titleSmall,
      )

      if (state.photoPath == null) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          FilterChip(
            selected = false,
            onClick = requestCamera,
            label = { Text("Cámara") },
            leadingIcon = {
              Icon(Icons.Default.PhotoCamera, contentDescription = null)
            },
            modifier = Modifier.semantics { contentDescription = "Hacer foto con la cámara" },
          )
          FilterChip(
            selected = false,
            onClick = { pickGalleryImage.launch("image/*") },
            label = { Text("Galería") },
            leadingIcon = {
              Icon(Icons.Default.PhotoLibrary, contentDescription = null)
            },
            modifier = Modifier.semantics { contentDescription = "Elegir foto de la galería" },
          )
        }
      } else {
        FilterChip(
          selected = true,
          onClick = viewModel::removePhoto,
          label = { Text("Quitar foto") },
          leadingIcon = {
            Icon(Icons.Default.PhotoCamera, contentDescription = null)
          },
          modifier = Modifier.semantics { contentDescription = "Quitar foto adjunta" },
        )
      }

      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        FilterChip(
          selected = state.isRecording || state.audioPath != null,
          onClick = {
            when {
              state.isRecording -> viewModel.stopRecording()
              state.audioPath != null -> viewModel.removeAudio()
              else -> requestAudio()
            }
          },
          label = {
            Text(
              when {
                state.isRecording -> "Detener"
                state.audioPath != null -> "Quitar audio"
                else -> "Voz"
              },
            )
          },
          leadingIcon = {
            Icon(
              if (state.isRecording) Icons.Default.Stop else Icons.Default.Mic,
              contentDescription = null,
            )
          },
          modifier = Modifier.semantics {
            contentDescription = when {
              state.isRecording -> "Detener grabación de voz"
              state.audioPath != null -> "Quitar nota de voz"
              else -> "Grabar nota de voz"
            }
          },
        )
      }

      state.photoPath?.let { path ->
        AsyncImage(
          model = File(path),
          contentDescription = "Foto adjunta",
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(12.dp)),
        )
      }

      if (state.audioPath != null && !state.isRecording) {
        Text(
          text = "Nota de voz adjunta",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            Icons.Default.LocationOn,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
          )
          Text(
            text = "Guardar ubicación",
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
          )
        }
        Switch(
          checked = state.attachLocation,
          onCheckedChange = { enabled ->
            if (enabled && !hasLocationPermission) {
              requestLocation()
            } else {
              viewModel.toggleAttachLocation(enabled)
            }
          },
          modifier = Modifier.semantics {
            contentDescription = "Adjuntar ubicación al guardar la nota"
          },
        )
      }

      MOTButton(
        onClick = {
          viewModel.saveNote(hasLocationPermission = hasLocationPermission, onSuccess = onSaved)
        },
        enabled = !state.isSaving && !state.isRecording,
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text(if (state.isSaving) "Guardando…" else if (viewModel.isEditMode) "Guardar cambios" else "Guardar nota")
      }
    }
  }
}
