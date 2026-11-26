package com.myowntrip.gallery.specimens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTButtonSize
import com.myowntrip.app.ui.theme.MOTIcons
import com.myowntrip.app.ui.theme.MOTOutlinedButton
import com.myowntrip.app.ui.theme.MOTTextButton
import com.myowntrip.app.ui.theme.MOTTonalButton
import com.myowntrip.gallery.catalog.Specimen

@Composable
private fun RowScope.LeadingLabel(
  label: String = "Button",
  size: MOTButtonSize,
  showIcon: Boolean = true,
  loading: Boolean = false,
  error: Boolean = false,
) {
  if (loading) {
    CircularProgressIndicator(
      modifier = Modifier.size(18.dp),
      strokeWidth = 2.dp,
      color = MaterialTheme.colorScheme.onPrimary,
    )
    Spacer(Modifier.width(size.leadingIconGap))
  } else if (showIcon) {
    Icon(MOTIcons.Add, contentDescription = null, modifier = Modifier.size(size.leadingIconSize))
    Spacer(Modifier.width(size.leadingIconGap))
  }
  Text(
    text = label,
    color = if (error) MaterialTheme.colorScheme.error else androidx.compose.ui.graphics.Color.Unspecified,
  )
}

val buttonSpecimens: List<Specimen> = listOf(
  Specimen("button/default", "Button", "Default") {
    MOTButton(onClick = {}) {
      LeadingLabel(size = MOTButtonSize.Small)
    }
  },
  Specimen("button/tonal", "Button", "Tonal") {
    MOTTonalButton(onClick = {}) {
      LeadingLabel(size = MOTButtonSize.XSmall)
    }
  },
  Specimen("button/outlined", "Button", "Outlined") {
    MOTOutlinedButton(onClick = {}) {
      LeadingLabel(size = MOTButtonSize.Small)
    }
  },
  Specimen("button/text", "Button", "Text") {
    MOTTextButton(onClick = {}) {
      LeadingLabel(size = MOTButtonSize.XSmall)
    }
  },
  Specimen(
    id = "button/sizes",
    section = "Button",
    name = "Sizes",
    canvasAlign = Alignment.CenterStart,
  ) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
      MOTButtonSize.entries.forEach { size ->
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text(
            size.name,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          MOTButton(onClick = {}, size = size) {
            LeadingLabel(size = size)
          }
        }
      }
    }
  },
  Specimen("button/disabled", "Button", "Disabled") {
    MOTButton(onClick = {}, enabled = false) {
      LeadingLabel(size = MOTButtonSize.Small)
    }
  },
  Specimen("button/loading", "Button", "Loading") {
    MOTButton(onClick = {}, enabled = false) {
      LeadingLabel(size = MOTButtonSize.Small, showIcon = false, loading = true)
    }
  },
  Specimen("button/text-error", "Button", "Text error") {
    MOTTextButton(onClick = {}) {
      LeadingLabel(size = MOTButtonSize.XSmall, showIcon = false, error = true)
    }
  },
)
