package com.myowntrip.app.ui.features.wallet

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myowntrip.app.data.repository.WalletRepository
import com.myowntrip.app.domain.model.WalletEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletDetailViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val walletRepository: WalletRepository,
) : ViewModel() {
  private val entryId: String = checkNotNull(savedStateHandle["entryId"])

  val entry: StateFlow<WalletEntry?> = walletRepository.observeById(entryId)
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

  fun deleteEntry(onDeleted: () -> Unit) {
    viewModelScope.launch {
      walletRepository.deleteEntry(entryId)
      onDeleted()
    }
  }
}
