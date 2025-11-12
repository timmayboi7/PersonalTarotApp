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
class SpreadPickerViewModel @Inject constructor(private val spreadRepository: SpreadRepository) : ViewModel() {

    private val _spreads = MutableStateFlow<List<Spread>>(emptyList())
    val spreads: StateFlow<List<Spread>> = _spreads

    fun fetchSpreads() {
        viewModelScope.launch {
            _spreads.value = spreadRepository.all()
        }
    }
}
