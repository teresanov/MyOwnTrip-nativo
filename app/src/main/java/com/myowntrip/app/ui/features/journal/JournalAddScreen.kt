package com.myowntrip.app.ui.features.journal

import android.Manifest
import android.content.pm.PackageManager
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
import androidx.compose.material.icons.filled.Stop
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

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("New note") },
        navigationIcon = {
          MOTIconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
        label = { Text("What happened today?") },
        isError = state.textError != null,
        supportingText = state.textError?.let { { Text(it) } },
        modifier = Modifier.fillMaxWidth(),
      )

      Text(
        "Attach from your phone",
        style = MaterialTheme.typography.titleSmall,
      )

      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        FilterChip(
          selected = state.photoPath != null,
          onClick = {
            if (state.photoPath == null) requestCamera() else viewModel.removePhoto()
          },
          label = { Text(if (state.photoPath != null) "Remove photo" else "Photo") },
          leadingIcon = {
            Icon(Icons.Default.PhotoCamera, contentDescription = null)
          },
        )
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
                state.isRecording -> "Stop"
                state.audioPath != null -> "Remove audio"
                else -> "Voice"
              },
            )
          },
          leadingIcon = {
            Icon(
              if (state.isRecording) Icons.Default.Stop else Icons.Default.Mic,
              contentDescription = null,
            )
          },
        )
      }

      state.photoPath?.let { path ->
        AsyncImage(
          model = File(path),
          contentDescription = "Attached photo",
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(12.dp)),
        )
      }

      if (state.audioPath != null) {
        Text(
          "Voice note attached",
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
            "Save location",
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
            contentDescription = "Attach location when saving note"
          },
        )
      }

      MOTButton(
        onClick = {
          viewModel.saveNote(hasLocationPermission = hasLocationPermission, onSuccess = onSaved)
        },
        enabled = !state.isSaving,
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text(if (state.isSaving) "Saving…" else "Save note")
      }
    }
  }
}
