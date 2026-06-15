package com.myowntrip.app.ui.features.restaurants

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.myowntrip.app.domain.model.RestaurantStatus
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantFormScreen(
  onBack: () -> Unit,
  onSaved: () -> Unit,
  viewModel: RestaurantFormViewModel = hiltViewModel(),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("New restaurant") },
        navigationIcon = {
          MOTIconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      OutlinedTextField(
        value = state.name,
        onValueChange = viewModel::onNameChange,
        label = { Text("Name") },
        isError = state.nameError != null,
        supportingText = state.nameError?.let { { Text(it) } },
        modifier = Modifier.fillMaxWidth(),
      )
      OutlinedTextField(
        value = state.address,
        onValueChange = viewModel::onAddressChange,
        label = { Text("Address (optional)") },
        modifier = Modifier.fillMaxWidth(),
      )
      OutlinedTextField(
        value = state.notes,
        onValueChange = viewModel::onNotesChange,
        label = { Text("Notes (optional)") },
        modifier = Modifier.fillMaxWidth(),
      )
      MOTButton(
        onClick = { viewModel.save(onSaved) },
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text("Save")
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen(
  onBack: () -> Unit,
  viewModel: RestaurantDetailViewModel = hiltViewModel(),
) {
  val restaurant by viewModel.restaurant.collectAsStateWithLifecycle()

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(restaurant?.name ?: "Restaurant") },
        navigationIcon = {
          MOTIconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
      )
    },
  ) { padding ->
    Column(
      modifier = Modifier
        .padding(padding)
        .fillMaxSize()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      restaurant?.let { item ->
        Text("Status", style = MaterialTheme.typography.labelLarge)
        RowOfStatusChips(current = item.status, onSelect = viewModel::updateStatus)
        item.address?.let {
          Text(it, style = MaterialTheme.typography.bodyMedium)
        }
        item.notes?.let {
          Text(it, style = MaterialTheme.typography.bodyLarge)
        }
      }
    }
  }
}

@Composable
private fun RowOfStatusChips(
  current: RestaurantStatus,
  onSelect: (RestaurantStatus) -> Unit,
) {
  Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
    RestaurantStatus.entries.forEach { status ->
      FilterChip(
        selected = current == status,
        onClick = { onSelect(status) },
        label = { Text(statusLabel(status)) },
      )
    }
  }
}

fun statusLabel(status: RestaurantStatus): String = when (status) {
  RestaurantStatus.WITHOUT_RESERVATION -> "Pending"
  RestaurantStatus.RESERVED -> "Reserved"
  RestaurantStatus.VISITED -> "Visited"
}
