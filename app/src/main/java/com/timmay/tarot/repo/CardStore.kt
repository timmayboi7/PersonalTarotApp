package com.timmay.tarot.repo

import android.content.Context
import com.timmay.tarot.domain.TarotCard
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardStore @Inject constructor(
    @ApplicationContext context: Context) {
    private val json = Json { ignoreUnknownKeys = true }
    private val cards: List<TarotCard>

    init {
        val text = context.assets.open("deck.json").reader().readText()
        cards = json.decodeFromString(text)
    }

    fun all(): List<TarotCard> = cards
}