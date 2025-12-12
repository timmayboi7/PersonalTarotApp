package com.timmay.tarot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timmay.tarot.domain.TarotRng
import com.timmay.tarot.di.IoDispatcher
import com.timmay.tarot.repo.CardStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

/**
 * ViewModel for the Home screen. Provides the UI state containing the daily card name.
 */
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val cardStore: CardStore,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    data class Ui(
        val dailyCardName: String = "Tap to draw",
        val dailyCardImage: String? = null,
        val dailyDescription: String = "",
        val isDrawn: Boolean = false,
        val drawDate: LocalDate? = null
    )

    private val _ui = MutableStateFlow(Ui())
    /**
     * Publicly exposed UI state as an immutable StateFlow.
     */
    val ui: StateFlow<Ui> = _ui.asStateFlow()

    fun fetchDailyCard() {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                val cards = cardStore.all()
                if (cards.isNotEmpty()) {
                    val zone = ZoneId.systemDefault()
                    val today = LocalDate.now(zone)
                    val current = _ui.value
                    if (current.isDrawn && current.drawDate == today) return@withContext
                    val dailySeed = TarotRng.dailySeed(zone)
                    val idx = kotlin.random.Random(dailySeed).nextInt(cards.size)
                    val card = cards[idx]
                    _ui.value = Ui(
                        dailyCardName = card.name,
                        dailyCardImage = card.imageAsset,
                        dailyDescription = card.dailyDescription.ifBlank { card.meaningUpright },
                        isDrawn = true,
                        drawDate = today
                    )
                }
            }
        }
    }
}
