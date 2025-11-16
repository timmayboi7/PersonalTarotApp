package com.timmay.tarot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timmay.tarot.domain.TarotRng
import com.timmay.tarot.repo.CardStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZoneId
import javax.inject.Inject

/**
 * ViewModel for the Home screen. Provides the UI state containing the daily card name.
 */
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val cardStore: CardStore,
) : ViewModel() {

    data class Ui(val dailyCardName: String = "…")

    private val _ui = MutableStateFlow(Ui())
    /**
     * Publicly exposed UI state as an immutable StateFlow.
     */
    val ui: StateFlow<Ui> = _ui.asStateFlow()

    init {
        // Fetch the daily card on initialization.
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val cards = cardStore.all()
                if (cards.isNotEmpty()) {
                    val zone = ZoneId.systemDefault()
                    val dailySeed = TarotRng.dailySeed(zone)
                    val idx = kotlin.random.Random(dailySeed).nextInt(cards.size)
                    _ui.value = Ui(cards[idx].name)
                }
            }
        }
    }
}
