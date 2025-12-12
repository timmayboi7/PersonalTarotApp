package com.timmay.tarot.repo

import com.timmay.tarot.domain.TarotCard
import kotlinx.serialization.json.Json
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class CardStore @Inject constructor(
    @Named("deckStream") stream: InputStream
) {
    private val cards: List<TarotCard>

    init {
        val json = Json { ignoreUnknownKeys = true }
        val text = stream.bufferedReader().use { it.readText() }
        cards = json.decodeFromString(text)
    }

    fun all(): List<TarotCard> = cards
}
