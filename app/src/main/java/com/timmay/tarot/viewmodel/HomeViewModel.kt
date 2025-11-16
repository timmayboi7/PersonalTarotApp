package com.timmay.tarot.viewmodel

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timmay.tarot.domain.TarotRng
import com.timmay.tarot.repo.CardStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val cardStore: CardStore) : ViewModel() {

    private val _dailyCard = MutableStateFlow("…")
    val dailyCard: StateFlow<String> = _dailyCard

    fun fetchDailyCard() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val zone = ZoneId.systemDefault()
                    val dailySeed = TarotRng.dailySeed(zone)
                    val cards = cardStore.all()
                    val idx = TarotRng.random(dailySeed).nextInt(cards.size)
                    _dailyCard.value = cards[idx].name
                }
            }
        } else {
            _dailyCard.value = "Feature not available on this device"
        }
    }
}
