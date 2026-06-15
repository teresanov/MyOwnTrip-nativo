package com.myowntrip.app.ui.features.expenses

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myowntrip.app.domain.model.ExpenseCategory
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseFormScreen(
  onBack: () -> Unit,
  onSaved: () -> Unit,
  viewModel: ExpenseFormViewModel = hiltViewModel(),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Quick expense") },
        navigationIcon = {
          MOTIconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
      )
    },
  ) { padding ->
    Column(
      modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
    ) {
      OutlinedTextField(
        value = state.amountText,
        onValueChange = viewModel::onAmountChange,
        label = { Text("Amount") },
        isError = state.amountError != null,
        supportingText = state.amountError?.let { { Text(it) } },
        modifier = Modifier.fillMaxWidth(),
      )
      OutlinedTextField(
        value = state.concept,
        onValueChange = viewModel::onConceptChange,
        label = { Text("Concept (optional)") },
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
      )
      Text("Category", modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
      ExpenseCategory.entries.chunked(3).forEach { row ->
        androidx.compose.foundation.layout.Row {
          row.forEach { category ->
            FilterChip(
              selected = state.category == category,
              onClick = { viewModel.onCategoryChange(category) },
              label = { Text(category.name) },
              modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
            )
          }
        }
      }
      MOTButton(
        onClick = { viewModel.saveQuick(onSaved) },
        modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
      ) {
        Text("Save expense")
      }
    }
  }
}
