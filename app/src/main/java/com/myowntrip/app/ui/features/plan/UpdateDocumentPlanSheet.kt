package com.myowntrip.app.ui.features.plan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.WalletEntry
import com.myowntrip.app.domain.plan.PlanPlacementLogic
import com.myowntrip.app.domain.wallet.WalletDocumentParser
import com.myowntrip.app.ui.components.date.MotTimeTextField
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MOTTextButton
import java.time.format.DateTimeFormatter
import java.util.Locale

private val SpanishDayFormatter =
  DateTimeFormatter.ofPattern("EEEE d MMM", Locale("es", "ES"))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateDocumentPlanSheet(
  blockTitle: String,
  linkedWallet: WalletEntry,
  tripDays: List<Day>,
  selectedDayId: String?,
  time: String,
  onDaySelected: (String) -> Unit,
  onTimeChange: (String) -> Unit,
  onDismiss: () -> Unit,
  onConfirm: () -> Unit,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var dayExpanded by remember { mutableStateOf(false) }
  val selectedDay = tripDays.find { it.id == selectedDayId }
  val documentSchedule = PlanPlacementLogic.walletDocumentScheduleLabel(linkedWallet)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = MOTSpacing.screenHorizontal)
        .padding(bottom = MOTSpacing.screenContentBottom),
      verticalArrangement = Arrangement.spacedBy(MOTSpacing.layoutMd),
    ) {
      Text("Corregir horario", style = MaterialTheme.typography.titleLarge)
      Text(
        text = blockTitle,
        style = MaterialTheme.typography.titleMedium,
      )
      Text(
        text = buildString {
          append("La fecha y hora del documento en Wallet son ")
          append(documentSchedule)
          append(
            ". Si el vuelo o la reserva cambió, indica cuándo ocurre realmente. " +
              "El documento en Wallet no se modifica.",
          )
        },
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
      if (tripDays.size > 1) {
        ExposedDropdownMenuBox(
          expanded = dayExpanded,
          onExpandedChange = { dayExpanded = it },
          modifier = Modifier.fillMaxWidth(),
        ) {
          OutlinedTextField(
            value = selectedDay?.let { day ->
              "Día ${day.dayNumber} · ${day.date.format(SpanishDayFormatter)}"
            }.orEmpty(),
            onValueChange = {},
            readOnly = true,
            label = { Text("Día del plan") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dayExpanded) },
            modifier = Modifier
              .menuAnchor()
              .fillMaxWidth(),
          )
          ExposedDropdownMenu(
            expanded = dayExpanded,
            onDismissRequest = { dayExpanded = false },
          ) {
            tripDays.forEach { day ->
              DropdownMenuItem(
                text = {
                  Text(
                    "Día ${day.dayNumber} · ${WalletDocumentParser.formatParsedDate(day.date)}",
                  )
                },
                onClick = {
                  onDaySelected(day.id)
                  dayExpanded = false
                },
              )
            }
          }
        }
      }
      MotTimeTextField(
        value = time,
        onValueChange = onTimeChange,
        label = "Hora en el plan",
        placeholder = "Ej. 09:15",
        modifier = Modifier.fillMaxWidth(),
      )
      MOTButton(
        onClick = onConfirm,
        modifier = Modifier.fillMaxWidth(),
        enabled = selectedDayId != null,
      ) {
        Text("Guardar cambio")
      }
      MOTTextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
        Text("Usar hora del documento")
      }
    }
  }
}
