package com.myowntrip.app.ui.features.plan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.wallet.WalletDocumentParser
import com.myowntrip.app.ui.components.date.MotTimeTextField
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MOTTextButton
import java.time.format.DateTimeFormatter
import java.util.Locale

private val SpanishDayFormatter =
  DateTimeFormatter.ofPattern("EEEE d MMM", Locale("es", "ES"))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoveBlockDayDialog(
  blockTitle: String,
  tripDays: List<Day>,
  selectedDayId: String?,
  time: String,
  onDaySelected: (String) -> Unit,
  onTimeChange: (String) -> Unit,
  onDismiss: () -> Unit,
  onConfirm: () -> Unit,
) {
  var dayExpanded by remember { mutableStateOf(false) }
  val selectedDay = tripDays.find { it.id == selectedDayId }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Mover a otro día") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(MOTSpacing.componentSm)) {
        Text(
          text = blockTitle,
          style = MaterialTheme.typography.titleSmall,
        )
        Text(
          text = "El documento en Wallet no se modifica; solo cambia dónde aparece en el plan.",
          style = MaterialTheme.typography.bodySmall,
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
          label = "Hora en el plan (opcional)",
          placeholder = "Ej. 09:15",
        )
      }
    },
    confirmButton = {
      MOTTextButton(
        onClick = onConfirm,
        enabled = selectedDayId != null,
      ) {
        Text("Guardar")
      }
    },
    dismissButton = {
      MOTTextButton(onClick = onDismiss) { Text("Cancelar") }
    },
  )
}
