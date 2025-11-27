package com.timmay.tarot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timmay.tarot.domain.Interpreter
import com.timmay.tarot.domain.ReadingCard
import com.timmay.tarot.domain.Spread
import com.timmay.tarot.domain.TarotRng
import com.timmay.tarot.repo.DeckRepository
import com.timmay.tarot.repo.SpreadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadingViewModel @Inject constructor(
    private val spreadRepository: SpreadRepository,
    private val deckRepository: DeckRepository,
    private val interpreter: Interpreter
) : ViewModel() {

class ReadingViewModel: ViewModel() {
    sealed class Ui {
        data object Loading: Ui()
        data class Result(
            val spread: Spread,
            val cards: List<CardWithCard>,
            val prose: String
        ): Ui()
    }

    private val _ui = MutableStateFlow<Ui>(Ui.Loading)
    val ui: StateFlow<Ui> = _ui

    fun start(spreadId: String) {
        viewModelScope.launch {
            _ui.value = Ui.Loading
            val spread = spreadRepository.byId(spreadId)
            val seed = TarotRng.secureSeed()
            val dealt = deckRepository.draw(spread.positions.size, seed)
            val prose = interpreter.compose(spread, dealt)
            _ui.value = Ui.Result(spread, dealt, prose)
        }
    }
}
