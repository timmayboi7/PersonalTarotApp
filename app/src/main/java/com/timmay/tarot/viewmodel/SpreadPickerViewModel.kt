package com.timmay.tarot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timmay.tarot.domain.Spread
import com.timmay.tarot.repo.SpreadStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SpreadPickerViewModel @Inject constructor(
    private val spreadStore: SpreadStore,
) : ViewModel() {

    sealed class Ui {
        data object Loading : Ui()
        data class Result(val spreads: List<Spread>) : Ui()
    }

    private val _ui = MutableStateFlow<Ui>(Ui.Loading)
    val ui = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val spreads = spreadStore.all()
                _ui.value = Ui.Result(spreads)
            }
        }
    }
}