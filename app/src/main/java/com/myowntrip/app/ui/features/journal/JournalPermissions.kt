package com.myowntrip.app.ui.features.journal

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

enum class JournalPermission {
  Camera,
  Audio,
  Location,
}

@Composable
fun rememberJournalPermissionRequest(
  permission: JournalPermission,
  onGranted: () -> Unit,
  onDenied: () -> Unit,
): () -> Unit {
  val context = LocalContext.current
  val androidPermission = when (permission) {
    JournalPermission.Camera -> Manifest.permission.CAMERA
    JournalPermission.Audio -> Manifest.permission.RECORD_AUDIO
    JournalPermission.Location -> Manifest.permission.ACCESS_FINE_LOCATION
  }

  val launcher = rememberLauncherForActivityResult(
    ActivityResultContracts.RequestPermission(),
  ) { granted ->
    if (granted) onGranted() else onDenied()
  }

  return {
    when (ContextCompat.checkSelfPermission(context, androidPermission)) {
      PackageManager.PERMISSION_GRANTED -> onGranted()
      else -> launcher.launch(androidPermission)
    }
  }
}
