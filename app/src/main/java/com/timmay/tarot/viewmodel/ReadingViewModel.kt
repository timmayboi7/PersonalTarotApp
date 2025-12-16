package com.timmay.tarot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timmay.tarot.di.IoDispatcher
import com.timmay.tarot.domain.CardWithState
import com.timmay.tarot.domain.Interpreter
import com.timmay.tarot.domain.Spread
import com.timmay.tarot.domain.TarotRng
import com.timmay.tarot.repo.CardStore
import com.timmay.tarot.repo.SpreadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ReadingViewModel @Inject constructor(
    private val cardStore: CardStore,
    private val spreadRepository: SpreadRepository,
    private val interpreter: Interpreter,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    sealed class Ui {
        data object Loading : Ui()
        data class Result(
            val spread: Spread,
            val cards: List<CardWithState>,
            val summary: com.timmay.tarot.domain.ReadingSummary,
            val revealed: List<Boolean>,
        ) : Ui()
    }

    private val _ui = MutableStateFlow<Ui>(Ui.Loading)
    val ui = _ui.asStateFlow()
    private var activeSpreadId: String? = null

    fun start(spreadId: String) {
        val current = _ui.value
        if (current is Ui.Result && activeSpreadId == spreadId) return

        viewModelScope.launch {
            _ui.value = Ui.Loading
            withContext(ioDispatcher) {
                val spread = spreadRepository.byId(spreadId)
                // Use a fresh secure seed per reading so draws are not just the deck order.
                val random = TarotRng.random(TarotRng.secureSeed())
                val deck = cardStore.all().shuffled(random)
                val cards = deck.take(spread.positions.size).map {
                    CardWithState(it, random.nextBoolean())
                }
                val summary = interpreter.composeSummary(spread, cards)
                _ui.value = Ui.Result(spread, cards, summary, MutableList(cards.size) { false })
                activeSpreadId = spreadId
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
