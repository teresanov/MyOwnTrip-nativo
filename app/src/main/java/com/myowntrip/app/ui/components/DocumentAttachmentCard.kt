package com.myowntrip.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.myowntrip.app.platform.documents.DocumentKind
import com.myowntrip.app.platform.documents.fileNameFromSource
import com.myowntrip.app.platform.documents.documentKindFromName
import com.myowntrip.app.ui.theme.MOTSpacing
import java.io.File

@Composable
fun DocumentAttachmentCard(
  source: String,
  fileName: String?,
  onOpen: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val kind = documentKindFromName(fileName ?: fileNameFromSource(source))
  val model = when {
    source.startsWith("content://") || source.startsWith("file://") -> source
    else -> File(source)
  }
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clickable(onClick = onOpen)
      .semantics { contentDescription = "Ver documento adjunto" },
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
  ) {
    Column(modifier = Modifier.padding(MOTSpacing.layoutMd)) {
      when (kind) {
        DocumentKind.IMAGE -> {
          AsyncImage(
            model = model,
            contentDescription = "Vista previa",
            contentScale = ContentScale.Crop,
            modifier = Modifier
              .fillMaxWidth()
              .height(180.dp),
          )
        }
        else -> {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = MOTSpacing.layoutMd),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
          ) {
            Icon(
              Icons.AutoMirrored.Filled.InsertDriveFile,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
              fileName ?: "Documento PDF",
              style = MaterialTheme.typography.bodyMedium,
            )
          }
        }
      }
      Text(
        text = "Toca para ver el documento",
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier.padding(top = MOTSpacing.componentSm),
      )
      Icon(
        Icons.Default.Visibility,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier
          .align(Alignment.End)
          .padding(top = MOTSpacing.componentXs),
      )
    }
  }
}
