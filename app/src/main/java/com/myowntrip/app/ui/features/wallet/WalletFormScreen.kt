package com.myowntrip.app.ui.features.wallet

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.myowntrip.app.domain.model.EntryType
import com.myowntrip.app.domain.wallet.WalletDocumentParser
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MOTTextButton
import com.myowntrip.app.ui.theme.MyOwnTripTheme

private val WalletAttachmentMimeTypes = arrayOf("application/pdf", "image/*")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletFormScreen(
  onBack: () -> Unit,
  onSaved: () -> Unit,
  onCreateTrip: () -> Unit = {},
  viewModel: WalletFormViewModel = hiltViewModel(),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  val context = LocalContext.current
  var tripExpanded by remember { mutableStateOf(false) }
  var dirty by remember { mutableStateOf(false) }
  var autoPickLaunched by remember { mutableStateOf(false) }
  var showDiscard by remember { mutableStateOf(false) }
  var pickerCancelExitsFlow by remember { mutableStateOf(false) }

  val requestExit: () -> Unit = {
    when {
      state.showConfirm -> viewModel.dismissConfirm()
      state.hasDraft -> showDiscard = true
      else -> onBack()
    }
  }

  val documentPicker = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.OpenDocument(),
  ) { uri ->
    viewModel.clearPickAttachmentOnStart()
    if (uri == null) {
      if (pickerCancelExitsFlow) onBack()
      return@rememberLauncherForActivityResult
    }
    runCatching {
      context.contentResolver.takePersistableUriPermission(
        uri,
        Intent.FLAG_GRANT_READ_URI_PERMISSION,
      )
    }
    val fileName = resolveAttachmentDisplayName(context, uri)
    val mimeType = resolveAttachmentMimeType(context, uri)
    dirty = true
    viewModel.setImportData(
      uri = uri,
      mimeType = mimeType,
      fileName = fileName,
    )
  }

  val launchDocumentPicker: (cancelExitsFlow: Boolean) -> Unit = { cancelExitsFlow ->
    pickerCancelExitsFlow = cancelExitsFlow
    documentPicker.launch(WalletAttachmentMimeTypes)
  }

  LaunchedEffect(state.pickAttachmentOnStart, state.attachmentUri) {
    if (state.pickAttachmentOnStart && state.attachmentUri == null && !autoPickLaunched) {
      autoPickLaunched = true
      launchDocumentPicker(true)
    }
  }

  BackHandler(enabled = state.showConfirm || state.hasDraft) { requestExit() }

  val awaitingSystemPicker = state.pickAttachmentOnStart &&
    state.attachmentUri == null &&
    !state.isParsing

  val screenTitle = when {
    awaitingSystemPicker -> "Importar documento"
    state.isParsing -> "Importar documento"
    state.parseFailed && state.attachmentUri != null -> "Completar manualmente"
    state.isImport && state.attachmentUri != null -> "Revisar importación"
    state.isImportFlow -> "Importar documento"
    else -> "Añadir a Wallet"
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(screenTitle) },
        navigationIcon = {
          MOTIconButton(onClick = if (awaitingSystemPicker) onBack else requestExit) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
          }
        },
      )
    },
  ) { padding ->
    Column(
      modifier = Modifier
        .padding(padding)
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(
          start = MOTSpacing.screenHorizontal,
          end = MOTSpacing.screenHorizontal,
          top = MOTSpacing.screenHorizontal,
          bottom = MOTSpacing.screenContentBottom,
        ),
      verticalArrangement = Arrangement.spacedBy(MOTSpacing.layoutMd),
    ) {
      when {
        awaitingSystemPicker -> WalletAwaitingPickerStep(
          onManualEntry = { viewModel.switchToManualEntry() },
          onCancel = onBack,
        )
        state.isParsing -> WalletParsingStep(
          onCancel = requestExit,
        )
        state.parseFailed && state.attachmentUri != null -> WalletParseFailStep(
          state = state,
          tripExpanded = tripExpanded,
          onTripExpandedChange = { tripExpanded = it },
          onCreateTrip = onCreateTrip,
          onPickFile = { launchDocumentPicker(false) },
          onTripSelected = { dirty = true; viewModel.onTripSelected(it); tripExpanded = false },
          onTitleChange = { dirty = true; viewModel.onTitleChange(it) },
          onTypeChange = { dirty = true; viewModel.onTypeChange(it) },
          onNotesChange = { dirty = true; viewModel.onNotesChange(it) },
          onShowNotes = { viewModel.setShowNotesField(true) },
          onConfirm = { viewModel.requestConfirm() },
          onCancel = requestExit,
        )
        state.isImportFlow -> WalletImportReviewStep(
          state = state,
          onPickFile = { launchDocumentPicker(false) },
          onTitleChange = { dirty = true; viewModel.onTitleChange(it) },
          onTypeChange = { dirty = true; viewModel.onTypeChange(it) },
          onShowTypeCorrection = viewModel::setShowTypeCorrection,
          onConfirm = { viewModel.requestConfirm() },
          onCancel = requestExit,
        )
        else -> WalletManualAddStep(
          state = state,
          tripExpanded = tripExpanded,
          onTripExpandedChange = { tripExpanded = it },
          onCreateTrip = onCreateTrip,
          onTripSelected = { dirty = true; viewModel.onTripSelected(it); tripExpanded = false },
          onTitleChange = { dirty = true; viewModel.onTitleChange(it) },
          onTypeChange = { dirty = true; viewModel.onTypeChange(it) },
          onNotesChange = { dirty = true; viewModel.onNotesChange(it) },
          onShowNotes = { viewModel.setShowNotesField(true) },
          onConfirm = { viewModel.requestConfirm() },
          onCancel = requestExit,
        )
      }
    }
  }

  if (showDiscard) {
    WalletDiscardDialog(
      hasAttachment = state.attachmentUri != null,
      onDismiss = { showDiscard = false },
      onConfirmDiscard = {
        showDiscard = false
        viewModel.abandonDraft()
        onBack()
      },
    )
  }

  if (state.showConfirm && state.pendingEntry != null) {
    WalletConfirmDialog(
      entry = state.pendingEntry!!,
      attachmentFileName = state.attachmentFileName,
      onDismiss = { viewModel.dismissConfirm() },
      onSave = { viewModel.confirmSave(onSaved) },
    )
  }
}

@Composable
private fun WalletParsingStep(onCancel: () -> Unit) {
  Column(
    modifier = Modifier.fillMaxWidth().padding(vertical = MOTSpacing.layoutLg),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(MOTSpacing.layoutMd),
  ) {
    CircularProgressIndicator()
    Text(
      text = "Analizando documento…",
      style = MaterialTheme.typography.titleMedium,
    )
    Text(
      text = "Detectamos tipo y título para que solo tengas que confirmar.",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    WalletFormCancelButton(label = "Cancelar análisis", onClick = onCancel)
  }
}

@Composable
private fun WalletAwaitingPickerStep(
  onManualEntry: () -> Unit,
  onCancel: () -> Unit,
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = MOTSpacing.layoutLg),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(MOTSpacing.layoutMd),
  ) {
    CircularProgressIndicator()
    Text(
      text = "Selecciona un archivo",
      style = MaterialTheme.typography.titleMedium,
    )
    Text(
      text = "Se abrirá el selector del sistema (Descargas, Drive, etc.). Elige un billete, reserva o PDF del viaje.",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    MOTTextButton(onClick = onManualEntry, modifier = Modifier.fillMaxWidth()) {
      Text("Rellenar manualmente")
    }
    WalletFormCancelButton(label = "Cancelar", onClick = onCancel)
  }
}

@Composable
private fun WalletPickFileStep(
  onPickFile: () -> Unit,
  onManualEntry: () -> Unit,
  onCancel: () -> Unit,
) {
  MOTButton(onClick = onPickFile, modifier = Modifier.fillMaxWidth()) {
    Icon(Icons.Default.AttachFile, contentDescription = null)
    Spacer(Modifier.width(MOTSpacing.componentSm))
    Text("Elegir archivo")
  }
  Text(
    text = "Elige un billete, reserva o PDF del viaje. Lo guardamos en el dispositivo para usarlo sin conexión.",
    style = MaterialTheme.typography.bodyMedium,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
  )
  MOTTextButton(onClick = onManualEntry, modifier = Modifier.fillMaxWidth()) {
    Text("Rellenar manualmente")
  }
  WalletFormCancelButton(label = "Cancelar", onClick = onCancel)
}

@Composable
private fun WalletParseFailBanner() {
  Card(
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
  ) {
    Column(
      modifier = Modifier.padding(MOTSpacing.layoutMd),
      verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentXs),
    ) {
      Text(
        text = "No pudimos leer el documento",
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onErrorContainer,
      )
      Text(
        text = "Completa tipo y título a mano. El archivo se guardará igualmente en Wallet.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onErrorContainer,
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun WalletParseFailStep(
  state: WalletFormUiState,
  tripExpanded: Boolean,
  onTripExpandedChange: (Boolean) -> Unit,
  onCreateTrip: () -> Unit,
  onPickFile: () -> Unit,
  onTripSelected: (String) -> Unit,
  onTitleChange: (String) -> Unit,
  onTypeChange: (EntryType) -> Unit,
  onNotesChange: (String) -> Unit,
  onShowNotes: () -> Unit,
  onConfirm: () -> Unit,
  onCancel: () -> Unit,
) {
  WalletParseFailBanner()
  WalletAttachmentPreview(
    attachmentUri = state.attachmentUri,
    fileName = state.attachmentFileName,
    onChangeFile = onPickFile,
  )
  WalletManualAddStep(
    state = state,
    tripExpanded = tripExpanded,
    onTripExpandedChange = onTripExpandedChange,
    onCreateTrip = onCreateTrip,
    onTripSelected = onTripSelected,
    onTitleChange = onTitleChange,
    onTypeChange = onTypeChange,
    onNotesChange = onNotesChange,
    onShowNotes = onShowNotes,
    onConfirm = onConfirm,
    onCancel = onCancel,
    showIntro = false,
  )
}

@Composable
private fun WalletImportReviewStep(
  state: WalletFormUiState,
  onPickFile: () -> Unit,
  onTitleChange: (String) -> Unit,
  onTypeChange: (EntryType) -> Unit,
  onShowTypeCorrection: (Boolean) -> Unit,
  onConfirm: () -> Unit,
  onCancel: () -> Unit,
) {
  WalletAttachmentPreview(
    attachmentUri = state.attachmentUri,
    fileName = state.attachmentFileName,
    onChangeFile = onPickFile,
  )
  Card(
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
  ) {
    Column(
      modifier = Modifier.padding(MOTSpacing.layoutMd),
      verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
    ) {
      Text("Detectado", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.tertiary)
      Text(
        text = entryTypeLabel(state.type),
        style = MaterialTheme.typography.titleMedium,
      )
      Text(
        text = state.title,
        style = MaterialTheme.typography.bodyLarge,
      )
      parsedScheduleLabel(state)?.let { schedule ->
        Text(
          text = schedule,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
      state.notes.takeIf { it.isNotBlank() }?.let { notes ->
        Text(
          text = notes,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
      state.attachmentFileName?.let {
        Text(
          text = it,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
      state.qrPayload?.let { payload ->
        WalletBoardingQrCard(payload = payload, compact = true)
      }
      if (!state.showTypeCorrection) {
        MOTTextButton(onClick = { onShowTypeCorrection(true) }) {
          Text("Cambiar tipo")
        }
      }
    }
  }
  if (state.showTypeCorrection) {
    EntryTypeChips(
      selected = state.type,
      onSelect = { onTypeChange(it); onShowTypeCorrection(false) },
    )
  }
  OutlinedTextField(
    value = state.title,
    onValueChange = onTitleChange,
    label = { Text("Título") },
    isError = state.titleError != null,
    supportingText = state.titleError?.let { { Text(it) } },
    modifier = Modifier.fillMaxWidth(),
  )
  Text(
    text = "Revisa y confirma. Nada se guarda hasta que pulses Confirmar.",
    style = MaterialTheme.typography.bodySmall,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
  )
  MOTButton(onClick = onConfirm, modifier = Modifier.fillMaxWidth()) {
    Text("Confirmar")
  }
  WalletFormCancelButton(label = "Cancelar", onClick = onCancel)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun WalletManualAddStep(
  state: WalletFormUiState,
  tripExpanded: Boolean,
  onTripExpandedChange: (Boolean) -> Unit,
  onCreateTrip: () -> Unit,
  onTripSelected: (String) -> Unit,
  onTitleChange: (String) -> Unit,
  onTypeChange: (EntryType) -> Unit,
  onNotesChange: (String) -> Unit,
  onShowNotes: () -> Unit,
  onConfirm: () -> Unit,
  onCancel: () -> Unit = {},
  showIntro: Boolean = true,
  showCancel: Boolean = true,
) {
  if (showIntro) {
    Text(
      text = "Entrada manual cuando no tienes un archivo a mano.",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
  }
  when {
    state.trips.isEmpty() -> {
      Text(
        "Aún no hay viajes. Crea uno para guardar este documento en Wallet.",
        style = MaterialTheme.typography.bodyMedium,
      )
      MOTButton(onClick = onCreateTrip, modifier = Modifier.fillMaxWidth()) {
        Text("Crear viaje")
      }
    }
    state.showTripPicker -> {
      ExposedDropdownMenuBox(
        expanded = tripExpanded,
        onExpandedChange = onTripExpandedChange,
        modifier = Modifier.fillMaxWidth(),
      ) {
        OutlinedTextField(
          value = state.trips.find { it.id == state.tripId }?.name ?: "Selecciona un viaje",
          onValueChange = {},
          readOnly = true,
          label = { Text("Viaje") },
          trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tripExpanded) },
          modifier = Modifier.menuAnchor().fillMaxWidth(),
        )
        ExposedDropdownMenu(expanded = tripExpanded, onDismissRequest = { onTripExpandedChange(false) }) {
          state.trips.forEach { trip ->
            DropdownMenuItem(
              text = { Text(trip.name) },
              onClick = { onTripSelected(trip.id) },
            )
          }
        }
      }
    }
  }
  OutlinedTextField(
    value = state.title,
    onValueChange = onTitleChange,
    label = { Text("Título") },
    isError = state.titleError != null,
    supportingText = state.titleError?.let { { Text(it) } },
    modifier = Modifier.fillMaxWidth(),
  )
  Text("Tipo", style = MaterialTheme.typography.labelLarge)
  EntryTypeChips(selected = state.type, onSelect = onTypeChange)
  if (state.showNotesField) {
    OutlinedTextField(
      value = state.notes,
      onValueChange = onNotesChange,
      label = { Text("Notas") },
      modifier = Modifier.fillMaxWidth(),
    )
  } else {
    MOTTextButton(onClick = onShowNotes) {
      Text("Añadir notas (opcional)")
    }
  }
  MOTButton(onClick = onConfirm, modifier = Modifier.fillMaxWidth()) {
    Text("Confirmar")
  }
  if (showCancel) {
    WalletFormCancelButton(label = "Cancelar", onClick = onCancel)
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EntryTypeChips(
  selected: EntryType,
  onSelect: (EntryType) -> Unit,
  modifier: Modifier = Modifier,
) {
  FlowRow(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
    verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
  ) {
    EntryType.entries.forEach { type ->
      FilterChip(
        selected = type == selected,
        onClick = { onSelect(type) },
        label = { Text(entryTypeLabel(type)) },
      )
    }
  }
}

@Composable
private fun WalletAttachmentPreview(
  attachmentUri: Uri?,
  fileName: String?,
  onChangeFile: () -> Unit,
) {
  if (attachmentUri != null) {
    val isImage = fileName?.substringAfterLast('.', "")?.lowercase() in setOf("jpg", "jpeg", "png", "webp", "gif")
    if (isImage) {
      AsyncImage(
        model = attachmentUri,
        contentDescription = "Vista previa del adjunto",
        modifier = Modifier.fillMaxWidth().height(160.dp),
        contentScale = ContentScale.Crop,
      )
    } else {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
      ) {
        Column(
          modifier = Modifier.padding(MOTSpacing.layoutMd),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm),
        ) {
          Icon(
            Icons.AutoMirrored.Filled.InsertDriveFile,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          Text(
            text = fileName ?: "Documento",
            style = MaterialTheme.typography.bodyMedium,
          )
        }
      }
    }
    MOTTextButton(onClick = onChangeFile) {
      Text("Cambiar archivo")
    }
  }
}

@Composable
private fun WalletConfirmDialog(
  entry: com.myowntrip.app.domain.model.WalletEntry,
  attachmentFileName: String?,
  onDismiss: () -> Unit,
  onSave: () -> Unit,
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Confirmar guardado") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentXs)) {
        Text("Tipo: ${entryTypeLabel(entry.type)}")
        Text("Título: ${entry.title}")
        parsedScheduleLabel(entry)?.let { Text("Cuándo: $it") }
        entry.qrPayload?.let { Text("QR de embarque: guardado para uso sin conexión.") }
        entry.notes?.let { Text("Notas: $it") }
        if (entry.pdfUri != null || attachmentFileName != null) {
          Text("Adjunto: se guardará en el dispositivo para uso sin conexión.")
        }
        Text(
          "Revisa todos los campos antes de guardar. Nada se almacena hasta que confirmes.",
          modifier = Modifier.padding(top = MOTSpacing.componentSm),
        )
      }
    },
    confirmButton = {
      MOTTextButton(onClick = onSave) { Text("Guardar") }
    },
    dismissButton = {
      MOTTextButton(onClick = onDismiss) { Text("Cancelar") }
    },
  )
}

private fun parsedScheduleLabel(state: WalletFormUiState): String? =
  parsedScheduleLabel(state.date, state.time)

private fun parsedScheduleLabel(entry: com.myowntrip.app.domain.model.WalletEntry): String? =
  parsedScheduleLabel(entry.date, entry.time)

private fun parsedScheduleLabel(date: java.time.LocalDate?, time: java.time.LocalTime?): String? {
  val dateLabel = date?.let { WalletDocumentParser.formatParsedDate(it) }
  val timeLabel = time?.let { WalletDocumentParser.formatParsedTime(it) }
  return when {
    dateLabel != null && timeLabel != null -> "$dateLabel · $timeLabel"
    dateLabel != null -> dateLabel
    timeLabel != null -> timeLabel
    else -> null
  }
}

@Preview(name = "Import · analizando", showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun WalletFormParsingPreview() {
  MyOwnTripTheme {
    WalletParsingStep(onCancel = {})
  }
}

@Preview(name = "Import · revisar", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun WalletFormImportReviewPreview() {
  MyOwnTripTheme {
    WalletImportReviewStep(
      state = WalletFormUiState(
        tripId = "trip-1",
        type = EntryType.FLIGHT,
        title = "IB 3254 · Madrid → Barcelona",
        date = java.time.LocalDate.of(2026, 6, 14),
        time = java.time.LocalTime.of(9, 15),
        attachmentFileName = "billete-barcelona.pdf",
        isImport = true,
        qrPayload = "M1DEMO/PAX EIB3254 MADBCNIB 3254 314Y014A0001 349>5180  5140BIB              2A825513825513 0000",
      ),
      onPickFile = {},
      onTitleChange = {},
      onTypeChange = {},
      onShowTypeCorrection = {},
      onConfirm = {},
      onCancel = {},
    )
  }
}

@Preview(name = "Manual · alta", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun WalletFormManualPreview() {
  MyOwnTripTheme {
    WalletManualAddStep(
      state = WalletFormUiState(tripId = "trip-1", title = ""),
      tripExpanded = false,
      onTripExpandedChange = {},
      onCreateTrip = {},
      onTripSelected = {},
      onTitleChange = {},
      onTypeChange = {},
      onNotesChange = {},
      onShowNotes = {},
      onConfirm = {},
      showCancel = false,
    )
  }
}
