package com.timmay.tarot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timmay.tarot.domain.Spread
import com.timmay.tarot.repo.SpreadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpreadPickerViewModel @Inject constructor(
    private val spreadRepository: SpreadRepository
) : ViewModel() {

    sealed interface UiState {
        data object Loading : UiState
        data class Ready(val spreads: List<Spread>) : UiState
        data class Error(val message: String) : UiState
    }

    private val _ui = MutableStateFlow<UiState>(UiState.Loading)
    val ui: StateFlow<UiState> = _ui

    fun fetchSpreads() {
        viewModelScope.launch {
            _ui.value = UiState.Loading
            try {
                val spreads = spreadRepository.all()
                _ui.value = UiState.Ready(spreads)
            } catch (error: Throwable) {
                _ui.value = UiState.Error(error.message ?: "Unable to load spreads")
            }
        }
    }
}
