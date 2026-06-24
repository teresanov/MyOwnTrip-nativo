package com.myowntrip.app.ui.features.trips

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.myowntrip.app.ui.components.home.ClearAllDataDialog
import com.myowntrip.app.ui.components.home.HomeFilterMenuItemRow
import com.myowntrip.app.ui.components.home.HomeFilterMenuList
import com.myowntrip.app.ui.components.home.HomeFilterMenuSectionLabel
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MOTTextButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class TripListDevWipeUi(
  val emptyStateFooter: @Composable () -> Unit,
  val filterMenuFooter: @Composable (dismissMenu: () -> Unit) -> Unit,
)

@Composable
fun rememberTripListDevWipe(
  viewModel: TripListViewModel,
  snackbarHostState: SnackbarHostState,
  scope: CoroutineScope,
): TripListDevWipeUi {
  var showClearConfirm by remember { mutableStateOf(false) }
  val requestWipe = remember { { showClearConfirm = true } }

  if (showClearConfirm) {
    ClearAllDataDialog(
      onDismiss = { showClearConfirm = false },
      onConfirm = {
        showClearConfirm = false
        viewModel.clearAllUserData {
          scope.launch {
            snackbarHostState.showSnackbar(
              "Datos borrados. Crea tu primer viaje cuando quieras.",
            )
          }
        }
      },
    )
  }

  return remember(requestWipe) {
    TripListDevWipeUi(
      emptyStateFooter = {
        DevWipeEmptyStateAction(onClick = requestWipe)
      },
      filterMenuFooter = { dismissMenu ->
        DevWipeFilterMenuSection(
          onClick = {
            dismissMenu()
            requestWipe()
          },
        )
      },
    )
  }
}

@Composable
private fun DevWipeEmptyStateAction(onClick: () -> Unit) {
  MOTTextButton(
    onClick = onClick,
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = MOTSpacing.screenHorizontal),
  ) {
    Text("Borrar todos los datos", color = MaterialTheme.colorScheme.error)
  }
}

@Composable
private fun DevWipeFilterMenuSection(onClick: () -> Unit) {
  HomeFilterMenuList {
    HomeFilterMenuSectionLabel("Datos")
    HomeFilterMenuItemRow(
      label = "Borrar todos los datos",
      selected = false,
      onClick = onClick,
      destructive = true,
    )
  }
}
