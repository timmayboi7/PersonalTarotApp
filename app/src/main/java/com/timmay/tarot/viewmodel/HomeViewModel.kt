package com.timmay.tarot.viewmodel

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timmay.tarot.domain.TarotCard
import com.timmay.tarot.domain.TarotRng
import com.timmay.tarot.repo.DeckRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.ZoneId
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val deckRepository: DeckRepository
) : ViewModel() {

    sealed interface DailyCardState {
        data object Loading : DailyCardState
        data class Ready(val card: TarotCard, val isReversed: Boolean) : DailyCardState
        data class Error(val message: String) : DailyCardState
    }

    private val _dailyCard = MutableStateFlow<DailyCardState>(DailyCardState.Loading)
    val dailyCard: StateFlow<DailyCardState> = _dailyCard

    fun fetchDailyCard() {
        viewModelScope.launch {
            _dailyCard.value = DailyCardState.Loading
            try {
                val zone = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) ZoneId.systemDefault() else null
                val seed = TarotRng.dailySeed(zone)
                val card = deckRepository.pickDailyCard(seed)
                val reversed = Random(seed xor ORIENTATION_SALT).nextBoolean()
                _dailyCard.value = DailyCardState.Ready(card, reversed)
            } catch (error: Throwable) {
                _dailyCard.value = DailyCardState.Error(error.message ?: "Unable to draw a card")
            }
        }
    }

    companion object {
        private const val ORIENTATION_SALT = 0x9E3779B97F4A7C15uL.toLong()
    }
}
