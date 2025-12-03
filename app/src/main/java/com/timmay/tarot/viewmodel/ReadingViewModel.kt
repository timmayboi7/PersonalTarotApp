package com.timmay.tarot.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timmay.tarot.domain.CardWithState
import com.timmay.tarot.domain.Interpreter
import com.timmay.tarot.domain.Spread
import com.timmay.tarot.domain.TarotRng
import com.timmay.tarot.repo.CardStore
import com.timmay.tarot.repo.SpreadStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class ReadingViewModel @Inject constructor(
    private val cardStore: CardStore,
    private val spreadStore: SpreadStore,
    private val interpreter: Interpreter,
) : ViewModel() {

    sealed class Ui {
        data object Loading : Ui()
        data class Result(
            val spread: Spread,
            val cards: List<CardWithState>,
            val prose: String,
            val revealed: List<Boolean>,
        ) : Ui()
    }

    private val _ui = MutableStateFlow<Ui>(Ui.Loading)
    val ui = _ui.asStateFlow()

    fun start(spreadId: String) {
        viewModelScope.launch {
            _ui.value = Ui.Loading
            withContext(Dispatchers.IO) {
                val spread = spreadStore.all().first { it.id == spreadId }
                val seed = TarotRng.dailySeed(ZoneId.systemDefault())
                val random = TarotRng.random(seed)
                val deck = cardStore.all().shuffled(random)
                val cards = deck.take(spread.positions.size).map {
                    CardWithState(it, random.nextBoolean())
                }
                val prose = interpreter.compose(spread, cards)
                _ui.value = Ui.Result(spread, cards, prose, MutableList(cards.size) { false })
            }
        }
    }

    fun reveal(index: Int) {
        val current = _ui.value
        if (current is Ui.Result) {
            val revealed = current.revealed.toMutableList()
            revealed[index] = true
            _ui.value = current.copy(revealed = revealed)
        }
    }
}
