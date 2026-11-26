package com.myowntrip.gallery.specimens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTIconButtonSize
import com.myowntrip.app.ui.theme.MOTIconButtonType
import com.myowntrip.app.ui.theme.MOTIconButtonWidth
import com.myowntrip.app.ui.theme.MOTIcons
import com.myowntrip.gallery.catalog.Specimen

val iconButtonSpecimens: List<Specimen> = listOf(
  Specimen("icon-button/default", "Icon button", "Default") {
    MOTIconButton(onClick = {}) {
      Icon(MOTIcons.Edit, contentDescription = "Edit")
    }
  },
  Specimen("icon-button/width", "Icon button", "Width") {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.Bottom) {
      MOTIconButtonWidth.entries.forEach { width ->
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
          MOTIconButton(onClick = {}, width = width) {
            Icon(MOTIcons.Edit, contentDescription = "Edit")
          }
          Text(
            width.name,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }
    }
  },
  Specimen("icon-button/sizes", "Icon button", "Sizes") {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.Bottom) {
      MOTIconButtonSize.entries.forEach { size ->
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
          MOTIconButton(onClick = {}, size = size) {
            Icon(MOTIcons.Edit, contentDescription = "Edit")
          }
          Text(
            size.name,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }
    }
  },
  Specimen("icon-button/round", "Icon button", "Round") {
    MOTIconButton(onClick = {}, type = MOTIconButtonType.Round) {
      Icon(MOTIcons.Edit, contentDescription = "Edit")
    }
  },
  Specimen("icon-button/disabled", "Icon button", "Disabled") {
    MOTIconButton(onClick = {}, enabled = false) {
      Icon(MOTIcons.Edit, contentDescription = "Edit")
    }
  },
)
