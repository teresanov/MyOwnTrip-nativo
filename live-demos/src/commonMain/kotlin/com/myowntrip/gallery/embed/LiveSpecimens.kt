package com.myowntrip.gallery.embed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.BitmapPainter
import com.myowntrip.app.ui.components.TripCoverPlaceholder
import com.myowntrip.app.ui.components.TripHeroCard
import com.myowntrip.app.ui.components.MotCardStyle
import com.myowntrip.app.ui.components.MotStackedCard
import com.myowntrip.app.ui.components.EyebrowLabelSize
import com.myowntrip.app.ui.components.EyebrowLabelColor
import com.myowntrip.app.ui.components.EyebrowLabel
import com.myowntrip.app.ui.theme.MOTTab
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MOTHorizontalDivider
import com.myowntrip.app.ui.theme.LocalExtendedColors
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRow
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.TextButton
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTButtonSize
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTIconButtonSize
import com.myowntrip.app.ui.theme.MOTIconButtonType
import com.myowntrip.app.ui.theme.MOTIconButtonWidth
import com.myowntrip.app.ui.theme.MOTIcons
import com.myowntrip.app.ui.theme.MOTOutlinedButton
import com.myowntrip.app.ui.theme.MOTTextButton
import com.myowntrip.app.ui.theme.MOTTonalButton
import com.myowntrip.app.ui.theme.MotExposedDropdownTrailingIcon
import com.myowntrip.app.ui.theme.MyOwnTripTheme
import com.myowntrip.gallery.catalog.Catalog

/**
 * Demos vivas incrustadas en las fichas del showcase (modelo B).
 * El HTML de la ficha pasa los parámetros como `data-p-*`; aquí solo se pinta el componente real.
 */
@Composable
fun LiveSpecimen(
  id: String,
  props: Map<String, String>,
  reduceMotion: Boolean,
  sink: PartSink = PartSink { _, _ -> },
  onMeta: (Map<String, Float>) -> Unit = {},
) {
  // Zoom de dibujo (anatomía y medidas): se escala la densidad, así el componente se pinta nítido
  // y su layout es el mismo que en la app, solo que más grande.
  val base = LocalDensity.current
  val zoom = props["zoom"]?.toFloatOrNull() ?: 1f
  SideEffect { onMeta(mapOf("cssPerPx" to 1f / base.density)) }
  CompositionLocalProvider(
    LocalDensity provides Density(base.density * zoom, base.fontScale),
    LocalPartSink provides sink,
  ) {
  MyOwnTripTheme(
    darkTheme = props["theme"] == "dark",
    reduceMotion = reduceMotion || props["reduce"] == "true",
  ) {
    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      when (id) {
        "button/playground" -> ButtonPlayground(props)
        "button/anatomy" -> ButtonAnatomy(props, onMeta)
        "icon-button/playground" -> IconButtonPlayground(props)
        "icon-button/anatomy" -> IconButtonAnatomy(props, onMeta)
        "text-field/playground" -> TextFieldPlayground(props)
        "text-field/anatomy" -> TextFieldAnatomy(props)
        "filter-chip/playground" -> FilterChipPlayground(props)
        "filter-chip/anatomy" -> FilterChipAnatomy(onMeta)
        "snackbar/playground" -> SnackbarPlayground(props)
        "snackbar/anatomy" -> SnackbarAnatomy(props)
        "basic-dialog/playground" -> DialogPlayground(props)
        "basic-dialog/anatomy" -> DialogAnatomy(props)
        // Ejemplos fijos para la cabecera de la ficha y para «Bien / Mal».
        "button/set" -> ButtonSet(props)
        "filter-chip/single" -> FilterChipSingle(props)
        "snackbar/static" -> SnackbarStatic(props)
        "basic-dialog/static" -> DialogStatic(props)
        "tabs/playground" -> TabsPlayground(props)
        "tabs/anatomy" -> TabsAnatomy()
        "divider/playground" -> DividerPlayground(props)
        "divider/anatomy" -> DividerAnatomy()
        "assist-chip/playground" -> AssistChipPlayground(props)
        "assist-chip/anatomy" -> AssistChipAnatomy(onMeta)
        "fab/playground" -> FabPlayground(props)
        "fab/anatomy" -> FabAnatomy()
        "app-bar/playground" -> AppBarPlayground(props)
        "app-bar/anatomy" -> AppBarAnatomy()
        "eyebrow-label/playground" -> EyebrowPlayground(props)
        "eyebrow-label/anatomy" -> EyebrowLabel(text = "Próximo viaje", modifier = Modifier.part("container"))
        "stacked-card/playground" -> StackedCardPlayground(props)
        "trip-hero-card/playground" -> TripHeroPlayground(props)
        // Cualquier specimen del catálogo (button/sizes, icon-button/round…) se puede incrustar por su id.
        else -> Catalog.firstOrNull { it.id == id }?.content?.invoke() ?: Text("Specimen desconocido: $id")
      }
    }
    }
  }
  }
}

@Composable
private fun ButtonPlayground(p: Map<String, String>) {
  val label = p["label"]?.ifBlank { null } ?: "Guardar gasto"
  val enabled = p["enabled"] != "false"
  val showIcon = p["icon"] == "true"
  val variant = p["variant"] ?: "filled"
  val size = when (variant) {
    "tonal", "text" -> MOTButtonSize.XSmall
    else -> MOTButtonSize.Small
  }
  val content: @Composable RowScope.() -> Unit = {
    if (showIcon) {
      Icon(MOTIcons.Add, contentDescription = null, modifier = Modifier.size(size.leadingIconSize))
      Spacer(Modifier.width(size.leadingIconGap))
    }
    Text(label)
  }
  when (variant) {
    "tonal" -> MOTTonalButton(onClick = {}, enabled = enabled, content = content)
    "outlined" -> MOTOutlinedButton(onClick = {}, enabled = enabled, content = content)
    "text" -> MOTTextButton(onClick = {}, enabled = enabled, content = content)
    else -> MOTButton(onClick = {}, enabled = enabled, content = content)
  }
}

/**
 * Botón para anatomía y medidas: el mismo MOTButton, con sus piezas marcadas.
 * El área táctil mínima se lee del tema (LocalMinimumInteractiveComponentSize) y se informa
 * aparte; dentro se desactiva solo para que la caja medida sea la del contenedor visible.
 */
@Composable
private fun ButtonAnatomy(p: Map<String, String>, onMeta: (Map<String, Float>) -> Unit) {
  val touch: Dp = LocalMinimumInteractiveComponentSize.current
  SideEffect { onMeta(mapOf("touchDp" to touch.value)) }
  val size = MOTButtonSize.Small
  CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
    MOTButton(onClick = {}, modifier = Modifier.part("container"), size = size) {
      Row(Modifier.part("content"), verticalAlignment = Alignment.CenterVertically) {
        Icon(MOTIcons.Add, contentDescription = null, modifier = Modifier.size(size.leadingIconSize).part("icon"))
        Spacer(Modifier.width(size.leadingIconGap))
        Text(p["label"]?.ifBlank { null } ?: "Guardar gasto", modifier = Modifier.part("label"))
      }
    }
  }
}

private fun iconButtonSize(p: Map<String, String>) =
  MOTIconButtonSize.entries.firstOrNull { it.name.equals(p["size"], true) } ?: MOTIconButtonSize.Small

private fun iconButtonWidth(p: Map<String, String>) =
  MOTIconButtonWidth.entries.firstOrNull { it.name.equals(p["width"], true) } ?: MOTIconButtonWidth.Default

/** Estilo de color: el wrapper es Standard por defecto; Filled y Tonal se piden con colors (roles del tema). */
@Composable
private fun iconButtonColors(p: Map<String, String>) = when (p["style"]) {
  "filled" -> IconButtonDefaults.filledIconButtonColors()
  "tonal" -> IconButtonDefaults.filledTonalIconButtonColors()
  else -> IconButtonDefaults.iconButtonColors()
}

private fun iconButtonType(p: Map<String, String>) =
  MOTIconButtonType.entries.firstOrNull { it.name.equals(p["type"], true) } ?: MOTIconButtonType.Square

@Composable
private fun IconButtonPlayground(p: Map<String, String>) {
  MOTIconButton(
    onClick = {},
    enabled = p["enabled"] != "false",
    size = iconButtonSize(p),
    width = iconButtonWidth(p),
    type = iconButtonType(p),
    colors = iconButtonColors(p),
  ) {
    Icon(MOTIcons.Edit, contentDescription = "Editar")
  }
}

/** Icon button para anatomía y medidas, con sus piezas marcadas (ver [ButtonAnatomy]). */
@Composable
private fun IconButtonAnatomy(p: Map<String, String>, onMeta: (Map<String, Float>) -> Unit) {
  val touch: Dp = LocalMinimumInteractiveComponentSize.current
  SideEffect { onMeta(mapOf("touchDp" to touch.value)) }
  CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
    MOTIconButton(
      onClick = {},
      modifier = Modifier.part("container"),
      size = iconButtonSize(p),
      width = iconButtonWidth(p),
      type = iconButtonType(p),
      colors = iconButtonColors(p),
    ) {
      Icon(MOTIcons.Edit, contentDescription = "Editar", modifier = Modifier.part("icon"))
    }
  }
}

/**
 * Text field tal como lo usa la app: icono final solo en campos de fecha (calendario) y en
 * desplegables (chevron); texto de apoyo opcional, obligatorio cuando hay error.
 */
@Composable
private fun TextFieldPlayground(p: Map<String, String>) {
  var value by remember { mutableStateOf(p["value"] ?: "") }
  var expanded by remember { mutableStateOf(false) }
  val error = p["error"] == "true"
  val enabled = p["enabled"] != "false"
  val support = p["support"] == "true"
  OutlinedTextField(
    value = value,
    onValueChange = { value = it },
    modifier = Modifier.width(280.dp),
    enabled = enabled,
    singleLine = true,
    label = { Text(p["label"] ?: "Destino") },
    trailingIcon = when (p["trailing"]) {
      "calendar" -> {
        { MOTIconButton(onClick = {}, enabled = enabled) { Icon(MOTIcons.CalendarMonth, contentDescription = "Abrir calendario") } }
      }
      "dropdown" -> {
        { MOTIconButton(onClick = { expanded = !expanded }, enabled = enabled) { MotExposedDropdownTrailingIcon(expanded = expanded) } }
      }
      else -> null
    },
    isError = error,
    supportingText = when {
      // errorText vacío: error solo con color (ejemplo de lo que no se hace).
      error && p["errortext"] != "" -> { { Text(p["errortext"] ?: "El destino es obligatorio") } }
      support -> { { Text(p["supporttext"] ?: "Ciudad o región") } }
      else -> null
    },
  )
}

/**
 * Text field para anatomía y medidas. Es la misma composición que OutlinedTextField
 * (BasicTextField + DecorationBox + Container), abierta para marcar cada pieza.
 */
@Composable
private fun TextFieldAnatomy(p: Map<String, String>) {
  var value by remember { mutableStateOf(p["value"] ?: "Lisboa") }
  val interaction = remember { MutableInteractionSource() }
  val colors = OutlinedTextFieldDefaults.colors()
  val textColor = MaterialTheme.colorScheme.onSurface
  BasicTextField(
    value = value,
    onValueChange = { value = it },
    modifier = Modifier.width(220.dp).part("field"),
    textStyle = LocalTextStyle.current.copy(color = textColor),
    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
    interactionSource = interaction,
    singleLine = true,
  ) { inner ->
    OutlinedTextFieldDefaults.DecorationBox(
      value = value,
      innerTextField = { Box(Modifier.part("input")) { inner() } },
      enabled = true,
      singleLine = true,
      visualTransformation = VisualTransformation.None,
      interactionSource = interaction,
      label = { Text("Destino", Modifier.part("label")) },
      trailingIcon = { Icon(MOTIcons.CalendarMonth, contentDescription = null, modifier = Modifier.part("trailing")) },
      supportingText = { Text("Ciudad o región", Modifier.part("support")) },
      colors = colors,
      container = {
        OutlinedTextFieldDefaults.Container(
          enabled = true,
          isError = false,
          interactionSource = interaction,
          modifier = Modifier.part("container"),
          colors = colors,
        )
      },
    )
  }
}

private val ChipOptions = listOf("Activos", "Archivados", "Todos")

@Composable
private fun FilterChipPlayground(p: Map<String, String>) {
  // Una sola opción seleccionada por eje, como en Wallet.
  var selected by remember { mutableStateOf("Activos") }
  val enabled = p["enabled"] != "false"
  Row(horizontalArrangement = Arrangement.spacedBy(com.myowntrip.app.ui.theme.MOTSpacing.Chip.gap)) {
    ChipOptions.forEach { option ->
      FilterChip(
        selected = option == selected,
        onClick = { selected = option },
        label = { Text(option) },
        enabled = enabled,
      )
    }
  }
}

/** Dos chips (uno seleccionado) para medir pieza, label y hueco entre chips. */
@Composable
private fun FilterChipAnatomy(onMeta: (Map<String, Float>) -> Unit) {
  // Como en ButtonAnatomy: el área táctil se informa aparte y se desactiva para medir el chip visible.
  val touch: Dp = LocalMinimumInteractiveComponentSize.current
  SideEffect { onMeta(mapOf("touchDp" to touch.value)) }
  CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
  Row(horizontalArrangement = Arrangement.spacedBy(com.myowntrip.app.ui.theme.MOTSpacing.Chip.gap)) {
    FilterChip(
      selected = true,
      onClick = {},
      modifier = Modifier.part("container"),
      label = { Text("Vuelos", Modifier.part("label")) },
    )
    FilterChip(
      selected = false,
      onClick = {},
      modifier = Modifier.part("chip2"),
      label = { Text("Hoteles", Modifier.part("label2")) },
    )
  }
  }
}

@Composable
private fun SnackbarPlayground(p: Map<String, String>) {
  val host = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()
  val withAction = p["action"] != "false"
  val withClose = p["close"] == "true"
  Box(Modifier.fillMaxSize()) {
    MOTButton(
      onClick = {
        scope.launch {
          host.currentSnackbarData?.dismiss()
          host.showSnackbar(
            message = p["message"] ?: "Gasto guardado",
            actionLabel = if (withAction) "Deshacer" else null,
            withDismissAction = withClose,
            duration = SnackbarDuration.Short,
          )
        }
      },
      modifier = Modifier.align(Alignment.TopCenter).padding(top = 24.dp),
    ) { Text("Guardar gasto") }
    SnackbarHost(host, Modifier.align(Alignment.BottomCenter).padding(16.dp))
  }
}

/** Snackbar con sus piezas marcadas; mismos colores que el que pinta SnackbarHost. */
@Composable
private fun SnackbarAnatomy(p: Map<String, String>) {
  Snackbar(
    // Sin ancho fijo: Modifier.width rompe la maquetación interna del Snackbar (acción fuera, cerrar encima del texto).
    modifier = Modifier.padding(horizontal = 56.dp).part("container"),
    action = {
      TextButton(
        onClick = {},
        colors = ButtonDefaults.textButtonColors(contentColor = SnackbarDefaults.actionColor),
      ) { Text("Deshacer", Modifier.part("action")) }
    },
    dismissAction = {
      androidx.compose.material3.IconButton(onClick = {}) {
        Icon(MOTIcons.Close, contentDescription = null, modifier = Modifier.part("close"))
      }
    },
  ) {
    Text(p["message"] ?: "Gasto guardado", Modifier.part("message"))
  }
}

@Composable
private fun DialogPlayground(p: Map<String, String>) {
  var open by remember { mutableStateOf(false) }
  val withIcon = p["icon"] == "true"
  MOTButton(onClick = { open = true }) { Text("Mostrar diálogo") }
  if (open) {
    AlertDialog(
      onDismissRequest = { open = false },
      icon = if (withIcon) {
        { Icon(MOTIcons.Error, contentDescription = null, tint = MaterialTheme.colorScheme.error) }
      } else null,
      title = { Text(p["title"] ?: "¿Eliminar documento?") },
      text = { Text(p["body"] ?: "Se borrará el documento y su archivo del dispositivo.") },
      confirmButton = { MOTTextButton(onClick = { open = false }) { ConfirmLabel(p) } },
      dismissButton = { MOTTextButton(onClick = { open = false }) { Text(p["dismiss"] ?: "Cancelar") } },
    )
  }
}

/** Diálogo abierto con sus piezas marcadas. La pantalla de detrás mide el scrim. */
@Composable
private fun DialogAnatomy(p: Map<String, String>) {
  Box(Modifier.fillMaxSize().part("scrim"))
  AlertDialog(
    onDismissRequest = {},
    modifier = Modifier.part("container"),
    icon = { Icon(MOTIcons.Error, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.part("icon")) },
    title = { Text("¿Eliminar «Lisboa 2026»?", Modifier.part("title")) },
    text = { Text("Se borrarán sus documentos y gastos. No se puede deshacer.", Modifier.part("body")) },
    confirmButton = { MOTTextButton(onClick = {}, modifier = Modifier.part("confirm")) { Text("Eliminar", color = MaterialTheme.colorScheme.error) } },
    dismissButton = { MOTTextButton(onClick = {}, modifier = Modifier.part("dismiss")) { Text("Cancelar") } },
  )
}

/** Texto de la acción de confirmar: en error cuando la acción destruye algo (como en WalletDialogs). */
@Composable
private fun ConfirmLabel(p: Map<String, String>) {
  val label = p["confirm"] ?: "Eliminar"
  if (p["destructive"] == "true") Text(label, color = MaterialTheme.colorScheme.error) else Text(label)
}

/** Botones en fila con textos de la app. set=do: solo Filled + Outlined. */
@Composable
private fun ButtonSet(p: Map<String, String>) {
  Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
    MOTButton(onClick = {}) { Text("Guardar cambios") }
    if (p["set"] != "do") MOTTonalButton(onClick = {}) { Text("Registrar como gasto") }
    MOTOutlinedButton(onClick = {}) { Text("Cancelar") }
  }
}

/** Un chip suelto (ejemplo de mal uso: un verbo de acción no es un filtro). */
@Composable
private fun FilterChipSingle(p: Map<String, String>) {
  FilterChip(selected = p["selected"] != "false", onClick = {}, label = { Text(p["label"] ?: "Guardar") })
}

/** Snackbar fijo, con los colores que pinta SnackbarHost. filled=true: acción con MOTButton (mal uso). */
@Composable
private fun SnackbarStatic(p: Map<String, String>) {
  val action = p["action"]?.ifBlank { null }
  Snackbar(
    modifier = Modifier.padding(horizontal = 16.dp),
    action = action?.let { label ->
      {
        if (p["filled"] == "true") {
          MOTButton(onClick = {}) { Text(label) }
        } else {
          TextButton(
            onClick = {},
            colors = ButtonDefaults.textButtonColors(contentColor = SnackbarDefaults.actionColor),
          ) { Text(label) }
        }
      }
    },
  ) { Text(p["message"] ?: "«Billete tren» archivado") }
}

/** Diálogo ya abierto. filledconfirm=true: confirmación con MOTButton y sin cancelar (mal uso). */
@Composable
private fun DialogStatic(p: Map<String, String>) {
  val filled = p["filledconfirm"] == "true"
  AlertDialog(
    onDismissRequest = {},
    icon = if (p["icon"] == "true") {
      { Icon(MOTIcons.Error, contentDescription = null, tint = MaterialTheme.colorScheme.error) }
    } else null,
    title = { Text(p["title"] ?: "¿Eliminar documento?") },
    text = { Text(p["body"] ?: "Se borrará el documento y su archivo del dispositivo.") },
    confirmButton = {
      if (filled) MOTButton(onClick = {}) { Text(p["confirm"] ?: "OK") }
      else MOTTextButton(onClick = {}) { ConfirmLabel(p) }
    },
    dismissButton = if (filled) null else {
      { MOTTextButton(onClick = {}) { Text(p["dismiss"] ?: "Cancelar") } }
    },
  )
}

/* ---------- Tramo 2 ---------- */

/** Pestañas del día (DayHub) y del viaje (TripDetail), con los textos de la app. */
private val DayTabs = listOf("Plan", "Diario")
private val TripTabs = listOf("Wallet", "Plan", "Diario", "Gastos", "Sitios")

/** Ancho de pantalla de móvil para las piezas que ocupan todo el ancho. */
private val PhoneWidth = 360.dp

/** row=fixed: TabRow de DayHub (2 pestañas). row=scrollable: ScrollableTabRow del viaje (5 pestañas). */
@Composable
private fun TabsPlayground(p: Map<String, String>) {
  var index by remember { mutableStateOf(0) }
  val enabled = p["enabled"] != "false"
  if (p["row"] == "scrollable") {
    ScrollableTabRow(selectedTabIndex = index.coerceAtMost(TripTabs.lastIndex), edgePadding = MOTSpacing.none, modifier = Modifier.width(PhoneWidth)) {
      TripTabs.forEachIndexed { i, title -> MOTTab(selected = index == i, onClick = { index = i }, label = title, enabled = enabled) }
    }
  } else {
    TabRow(selectedTabIndex = index.coerceAtMost(DayTabs.lastIndex), modifier = Modifier.width(PhoneWidth)) {
      DayTabs.forEachIndexed { i, title -> MOTTab(selected = index == i, onClick = { index = i }, label = title, enabled = enabled) }
    }
  }
}

/** TabRow de DayHub con sus piezas marcadas. Indicador y divisor son los que pone TabRow por defecto. */
@Composable
private fun TabsAnatomy() {
  TabRow(
    selectedTabIndex = 0,
    modifier = Modifier.width(280.dp).part("container"),
    indicator = { positions -> TabRowDefaults.SecondaryIndicator(Modifier.tabIndicatorOffset(positions[0]).part("indicator")) },
    divider = { HorizontalDivider(Modifier.part("divider")) },
  ) {
    MOTTab(selected = true, onClick = {}, modifier = Modifier.part("tab")) {
      Text(
        "Plan",
        modifier = Modifier.padding(horizontal = MOTSpacing.Tab.horizontal, vertical = MOTSpacing.Tab.vertical).part("label"),
      )
    }
    MOTTab(selected = false, onClick = {}, modifier = Modifier.part("tab2")) {
      Text("Diario", modifier = Modifier.padding(horizontal = MOTSpacing.Tab.horizontal, vertical = MOTSpacing.Tab.vertical))
    }
  }
}

/** Dos filas de lista separadas por un divisor, como en Wallet. inset=false: divisor a todo el ancho. */
@Composable
private fun DividerPlayground(p: Map<String, String>) {
  Column(Modifier.width(PhoneWidth)) {
    Text("Billete tren", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(MOTSpacing.screenHorizontal))
    MOTHorizontalDivider(inset = p["inset"] != "false")
    Text("Reserva hotel", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(MOTSpacing.screenHorizontal))
  }
}

@Composable
private fun DividerAnatomy() {
  // Igual que inset = true (la sangría va por fuera de la línea), pero con la marca dentro de la sangría
  // para que la medida sea la de la línea y no la del hueco.
  Box(Modifier.width(180.dp).padding(vertical = 24.dp).part("container")) {
    MOTHorizontalDivider(modifier = Modifier.padding(horizontal = MOTSpacing.screenHorizontal).part("line"))
  }
}

/**
 * Copia fiel de PlanPlacementSourceChip (vive en :app, features/plan): AssistChip con el color de aviso.
 * Si cambia allí, hay que cambiarlo aquí hasta que el chip pase al design system.
 */
@Composable
private fun PlanPlacementChip(label: String, onClick: (() -> Unit)?, modifier: Modifier = Modifier, labelModifier: Modifier = Modifier) {
  val warning = LocalExtendedColors.current.warning
  AssistChip(
    onClick = onClick ?: {},
    enabled = onClick != null,
    label = { Text(label, labelModifier) },
    modifier = modifier,
    colors = AssistChipDefaults.assistChipColors(
      labelColor = warning,
      containerColor = warning.copy(alpha = 0.14f),
      disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
      disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ),
    border = AssistChipDefaults.assistChipBorder(
      enabled = onClick != null,
      borderColor = warning.copy(alpha = 0.35f),
      disabledBorderColor = Color.Transparent,
    ),
  )
}

@Composable
private fun AssistChipPlayground(p: Map<String, String>) {
  PlanPlacementChip(
    label = p["label"] ?: "Actualizada en el plan · Día 2",
    onClick = if (p["enabled"] != "false") ({}) else null,
  )
}

@Composable
private fun AssistChipAnatomy(onMeta: (Map<String, Float>) -> Unit) {
  val touch: Dp = LocalMinimumInteractiveComponentSize.current
  SideEffect { onMeta(mapOf("touchDp" to touch.value)) }
  CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
  PlanPlacementChip(
    label = "Actualizada en el plan · Día 2",
    onClick = {},
    modifier = Modifier.part("container"),
    labelModifier = Modifier.part("label"),
  )
  }
}

/** FAB de las pestañas del viaje: icono Añadir, sin texto. */
@Composable
private fun FabPlayground(p: Map<String, String>) {
  FloatingActionButton(onClick = {}) {
    Icon(MOTIcons.Add, contentDescription = p["description"] ?: "Añadir gasto")
  }
}

@Composable
private fun FabAnatomy() {
  FloatingActionButton(onClick = {}, modifier = Modifier.part("container")) {
    Icon(MOTIcons.Add, contentDescription = null, modifier = Modifier.part("icon"))
  }
}

/** TopAppBar como en la app: volver + título + acción opcional (editar o borrar). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBarPlayground(p: Map<String, String>) {
  TopAppBar(
    modifier = Modifier.width(PhoneWidth),
    title = { Text(p["title"] ?: "Editar viaje", maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis) },
    navigationIcon = {
      MOTIconButton(onClick = {}) { Icon(MOTIcons.ArrowBack, contentDescription = "Volver") }
    },
    actions = {
      when (p["action"]) {
        "edit" -> MOTIconButton(onClick = {}) { Icon(MOTIcons.Edit, contentDescription = "Editar viaje") }
        "delete" -> MOTIconButton(onClick = {}) { Icon(MOTIcons.Delete, contentDescription = "Eliminar documento") }
        else -> {}
      }
    },
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBarAnatomy() {
  TopAppBar(
    modifier = Modifier.width(PhoneWidth).part("container"),
    title = { Text("Cracovia finde", Modifier.part("title")) },
    navigationIcon = {
      MOTIconButton(onClick = {}, modifier = Modifier.part("nav")) { Icon(MOTIcons.ArrowBack, contentDescription = null) }
    },
    actions = {
      MOTIconButton(onClick = {}, modifier = Modifier.part("action")) { Icon(MOTIcons.Edit, contentDescription = null) }
    },
  )
}

/* ---------- Tramo 3: componentes movidos al design system ---------- */

/** color: tertiary | surface | secondary · size: medium | small. */
@Composable
private fun EyebrowPlayground(p: Map<String, String>) {
  EyebrowLabel(
    text = p["text"]?.ifBlank { null } ?: "Próximo viaje",
    color = when (p["color"]) {
      "surface" -> EyebrowLabelColor.Surface
      "secondary" -> EyebrowLabelColor.Secondary
      else -> EyebrowLabelColor.Tertiary
    },
    size = if (p["size"] == "small") EyebrowLabelSize.Small else EyebrowLabelSize.Medium,
  )
}

/** layout: media (Home vacío) | text (¿Listo para el siguiente?) | search (sin resultados). Textos de la app. */
@Composable
private fun StackedCardPlayground(p: Map<String, String>) {
  val style = if (p["style"] == "elevated") MotCardStyle.Elevated else MotCardStyle.Outlined
  val modifier = Modifier.width(PhoneWidth).padding(horizontal = MOTSpacing.screenHorizontal)
  when (p["layout"]) {
    "text" -> MotStackedCard(
      style = style,
      headerText = "¿Listo para el siguiente?",
      subheadText = "Crea un cuaderno nuevo",
      supportingText = "Tus documentos y recuerdos de viajes anteriores siguen en Wallet.",
      modifier = modifier,
    )
    "search" -> MotStackedCard(
      style = style,
      headerText = "Ningún viaje coincide",
      subheadText = "Prueba otro término o cambia los filtros.",
      modifier = modifier,
    )
    else -> MotStackedCard(
      style = style,
      headerText = "Sin viajes todavía",
      subheadText = "Planea tu primera aventura",
      image = LiveAssets.homeEmptyMap?.let { BitmapPainter(it) },
      imageContentDescription = "Mapa con chincheta de viaje",
      modifier = modifier,
    )
  }
}

/** phase: upcoming | current | past. Textos como los calcula la app (TripCards.kt); portada de reserva. */
@Composable
private fun TripHeroPlayground(p: Map<String, String>) {
  val phase = p["phase"] ?: "upcoming"
  TripHeroCard(
    eyebrow = when (phase) { "current" -> "En destino"; "past" -> "Recuerdo"; else -> "Próximo viaje" },
    title = "Cracovia finde",
    meta = "20 nov 2026 – 22 nov 2026 · 3 días",
    stateDescription = when (phase) { "current" -> "Viaje en curso"; "past" -> "Viaje pasado"; else -> "Próximo viaje" },
    countdown = if (phase == "upcoming") "Sale en 12 días" else null,
    onClick = {},
    modifier = Modifier.width(PhoneWidth).padding(horizontal = MOTSpacing.screenHorizontal),
    cover = { m -> TripCoverPlaceholder(destination = "Cracovia", modifier = m) },
  )
}
