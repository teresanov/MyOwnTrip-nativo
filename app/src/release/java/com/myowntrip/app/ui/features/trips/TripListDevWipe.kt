package com.myowntrip.app.ui.features.trips

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope

data class TripListDevWipeUi(
  val emptyStateFooter: @Composable () -> Unit = {},
  val filterMenuFooter: @Composable (dismissMenu: () -> Unit) -> Unit = { _ -> },
)

@Composable
fun rememberTripListDevWipe(
  viewModel: TripListViewModel,
  snackbarHostState: SnackbarHostState,
  scope: CoroutineScope,
): TripListDevWipeUi = remember { TripListDevWipeUi() }
