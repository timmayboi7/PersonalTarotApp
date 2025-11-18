package com.timmay.tarot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.timmay.tarot.repo.SpreadRepository
import com.timmay.tarot.repo.DeckRepository
import com.timmay.tarot.repo.CardStore
import com.timmay.tarot.domain.*

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
            val spreads = SpreadRepository()
            val spread = spreads.byId(spreadId)
            val deckRepo = DeckRepository(CardStore())
            val seed = TarotRng.secureSeed()
            val shuffled = deckRepo.shuffled(seed)

            val allCards = CardStore().all().associateBy { it.id }
            val dealt = shuffled.take(spread.positions.size).map { d ->
                val card = allCards[d.cardId] ?: error("Card not found: " + d.cardId)
                CardWithCard(card, d.isReversed)
            }
            val prose = Interpreter().compose(spread, dealt)
            _ui.value = Ui.Result(spread, dealt, prose)
        }
    }
}
