package com.myowntrip.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.LocalExtendedColors
import com.myowntrip.app.ui.theme.motPart

private val LeadingIconSize = 40.dp
private val WarningDotSize = 10.dp
private val WarningDotBorder = 1.5.dp
private val WarningBorderWidth = 4.dp

/**
 * Fila de lista de producto sobre [ListItem] de Material 3: icono en círculo, título, texto de apoyo
 * y un hueco al final ([trailing]). Primer uso: la lista de documentos de Wallet.
 *
 * - [warningSuffix]: aviso que se añade al texto de apoyo en color warning; la fila lleva además un
 *   borde izquierdo y un punto en el icono del mismo color (en Wallet: «el plan cambió»).
 * - [dimmed]: la fila se atenúa (en Wallet: documento ya guardado en el móvil, sin nada pendiente).
 * - [contentDescription]: frase completa para lectores de pantalla; la fila se anuncia entera.
 */
@Composable
fun MotListItem(
  title: String,
  supportingText: String,
  icon: ImageVector,
  contentDescription: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  warningSuffix: String? = null,
  dimmed: Boolean = false,
  trailing: (@Composable () -> Unit)? = null,
) {
  val warning = LocalExtendedColors.current.warning
  val hasWarning = warningSuffix != null
  ListItem(
    modifier = modifier
      .motPart("container")
      .alpha(if (dimmed) 0.72f else 1f)
      .then(
        if (hasWarning) {
          // Encima del contenido: con drawBehind el fondo del ListItem tapaba el borde y no se veía.
          Modifier.drawWithContent {
            drawContent()
            drawRect(
              color = warning,
              topLeft = Offset.Zero,
              size = Size(WarningBorderWidth.toPx(), size.height),
            )
          }
        } else {
          Modifier
        },
      )
      .clickable(onClick = onClick)
      .semantics { this.contentDescription = contentDescription },
    headlineContent = {
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.fillMaxWidth().motPart("title"),
      )
    },
    supportingContent = {
      if (warningSuffix != null) {
        Text(
          text = buildAnnotatedString {
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) { append(supportingText) }
            withStyle(SpanStyle(color = warning)) { append(warningSuffix) }
          },
          style = MaterialTheme.typography.bodyMedium,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.fillMaxWidth().motPart("supporting"),
        )
      } else {
        Text(
          text = supportingText,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.fillMaxWidth().motPart("supporting"),
        )
      }
    },
    leadingContent = {
      MotListLeadingIcon(icon = icon, showWarningDot = hasWarning, modifier = Modifier.motPart("leading"))
    },
    trailingContent = trailing?.let { content -> { Box(Modifier.motPart("trailing")) { content() } } },
    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surface),
  )
}

/** Icono de 40 dp en círculo surfaceContainer; con [showWarningDot], punto warning abajo a la derecha. */
@Composable
fun MotListLeadingIcon(
  icon: ImageVector,
  showWarningDot: Boolean,
  modifier: Modifier = Modifier,
) {
  val warning = LocalExtendedColors.current.warning
  Box(
    modifier = modifier.size(LeadingIconSize),
    contentAlignment = Alignment.Center,
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      modifier = Modifier
        .size(LeadingIconSize)
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.surfaceContainer)
        .padding(8.dp),
      tint = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    if (showWarningDot) {
      Box(
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .size(WarningDotSize)
          .semantics { invisibleToUser() }
          .border(WarningDotBorder, MaterialTheme.colorScheme.surface, CircleShape)
          .clip(CircleShape)
          .background(warning)
          .motPart("warningDot"),
      )
    }
  }
}
