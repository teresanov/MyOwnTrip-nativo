package com.myowntrip.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.brand.BrandColors
import com.myowntrip.app.ui.brand.BrandRibbon
import com.myowntrip.app.ui.brand.MonogramVariant
import com.myowntrip.app.ui.brand.MyOwnTripMonogram
import com.myowntrip.app.ui.brand.MyOwnTripWordmark
import com.myowntrip.app.ui.brand.WordmarkVariant
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTTextButton
import com.myowntrip.app.ui.theme.MOTTonalButton
import java.util.Locale

// ---------- Marca ----------

@Composable
fun BrandSection() {
  SpecimenLabel("Wordmark · Positive")
  Specimen { MyOwnTripWordmark(height = 36.dp) }

  SpecimenLabel("Wordmark · Dark (sobre tinta)")
  Specimen(background = BrandColors.Ink) {
    MyOwnTripWordmark(height = 36.dp, variant = WordmarkVariant.Dark)
  }

  SpecimenLabel("Wordmark · Monochrome, sin cinta")
  Specimen { MyOwnTripWordmark(height = 36.dp, variant = WordmarkVariant.Monochrome, showRibbon = false) }

  SpecimenLabel("Monograma MOT · toolbar (21dp) y grande (32dp)")
  Specimen {
    Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
      MyOwnTripMonogram(height = 21.dp)
      MyOwnTripMonogram(height = 32.dp)
    }
  }

  SpecimenLabel("Monograma · Dark")
  Specimen(background = BrandColors.Ink) {
    MyOwnTripMonogram(height = 28.dp, variant = MonogramVariant.Dark)
  }

  SpecimenLabel("Cinta · el acento ocre, con cuentagotas")
  Specimen {
    Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.Bottom) {
      BrandRibbon(height = 16.dp)
      BrandRibbon(height = 24.dp)
      BrandRibbon(height = 36.dp)
    }
  }
}

// ---------- Color ----------

@Composable
fun ColorSection() {
  val scheme = MaterialTheme.colorScheme
  val roles = listOf(
    Triple("primary", scheme.primary, scheme.onPrimary),
    Triple("primaryContainer", scheme.primaryContainer, scheme.onPrimaryContainer),
    Triple("secondary", scheme.secondary, scheme.onSecondary),
    Triple("secondaryContainer", scheme.secondaryContainer, scheme.onSecondaryContainer),
    Triple("tertiary", scheme.tertiary, scheme.onTertiary),
    Triple("tertiaryContainer", scheme.tertiaryContainer, scheme.onTertiaryContainer),
    Triple("surface", scheme.surface, scheme.onSurface),
    Triple("surfaceVariant", scheme.surfaceVariant, scheme.onSurfaceVariant),
    Triple("surfaceContainerHigh", scheme.surfaceContainerHigh, scheme.onSurface),
    Triple("inverseSurface", scheme.inverseSurface, scheme.inverseOnSurface),
    Triple("error", scheme.error, scheme.onError),
    Triple("errorContainer", scheme.errorContainer, scheme.onErrorContainer),
  )
  Text(
    "Cada muestra pinta el rol con su on-color encima: si se lee, la pareja es accesible.",
    style = MaterialTheme.typography.bodySmall,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
  )
  roles.forEach { (name, container, content) ->
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .background(container)
        .padding(horizontal = 16.dp, vertical = 14.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        name,
        style = MaterialTheme.typography.labelLarge,
        color = content,
        modifier = Modifier.weight(1f),
      )
      Text(
        container.toHex(),
        style = MaterialTheme.typography.labelSmall,
        color = content,
      )
    }
  }
}

private fun Color.toHex(): String =
  String.format(Locale.ROOT, "#%06X", 0xFFFFFF and toArgb())

// ---------- Tipografía ----------

@Composable
fun TypographySection() {
  val type = MaterialTheme.typography
  val styles: List<Pair<String, TextStyle>> = listOf(
    "displayLarge · Fraunces" to type.displayLarge,
    "headlineMedium · Fraunces" to type.headlineMedium,
    "titleLarge · Fraunces" to type.titleLarge,
    "titleMedium" to type.titleMedium,
    "bodyLarge · Inter" to type.bodyLarge,
    "bodyMedium · Inter" to type.bodyMedium,
    "labelLarge · Inter" to type.labelLarge,
    "labelSmall · Inter" to type.labelSmall,
  )
  styles.forEach { (name, style) ->
    Column(modifier = Modifier.fillMaxWidth()) {
      Text(
        "$name · ${style.fontSize}",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.tertiary,
      )
      Text("Planifica. Guarda.", style = style)
    }
  }
}

// ---------- Botones ----------

@Composable
fun ButtonsSection() {
  var enabled by rememberSaveable { mutableStateOf(true) }
  var label by rememberSaveable { mutableStateOf("Crear viaje") }

  Text(
    "Mantén pulsado un botón: la forma se funde (morph) al presionar. Eso es el componente real reaccionando, no una imagen.",
    style = MaterialTheme.typography.bodySmall,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
  )

  Row(verticalAlignment = Alignment.CenterVertically) {
    Text("Habilitado", style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(1f))
    Switch(checked = enabled, onCheckedChange = { enabled = it })
  }
  OutlinedTextField(
    value = label,
    onValueChange = { label = it },
    label = { Text("Texto del botón") },
    modifier = Modifier.fillMaxWidth(),
  )

  SpecimenLabel("MOTButton · acción principal")
  Specimen {
    MOTButton(onClick = {}, enabled = enabled) { Text(label) }
  }

  SpecimenLabel("MOTTonalButton · acción secundaria")
  Specimen {
    MOTTonalButton(onClick = {}, enabled = enabled) { Text(label) }
  }

  SpecimenLabel("MOTTextButton · acción terciaria")
  Specimen {
    MOTTextButton(onClick = {}, enabled = enabled) { Text(label) }
  }

  SpecimenLabel("MOTIconButton")
  Specimen {
    MOTIconButton(onClick = {}, enabled = enabled) {
      Icon(Icons.Default.Add, contentDescription = null)
    }
  }
}

// ---------- Soporte ----------

@Composable
fun SpecimenLabel(text: String) {
  Text(
    text,
    style = MaterialTheme.typography.labelMedium,
    color = MaterialTheme.colorScheme.tertiary,
  )
}

/** Lienzo neutro para exponer un espécimen del DS, con fondo opcional (p. ej. tinta). */
@Composable
fun Specimen(
  background: Color = Color.Unspecified,
  content: @Composable () -> Unit,
) {
  val bg = if (background == Color.Unspecified) {
    MaterialTheme.colorScheme.surfaceContainerHigh
  } else {
    background
  }
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(bg)
      .padding(24.dp),
    contentAlignment = Alignment.Center,
  ) {
    content()
  }
}
