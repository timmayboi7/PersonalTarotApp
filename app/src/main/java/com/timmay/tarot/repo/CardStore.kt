package com.timmay.tarot.repo

import com.timmay.tarot.domain.TarotCard
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import java.io.InputStream

class CardStore {
    private val json = Json { ignoreUnknownKeys = true }
    fun all(): List<TarotCard> {
        val stream: InputStream = CardStore::class.java.classLoader!!
            .getResourceAsStream("assets/deck.json")
            ?: error("deck.json not found")
        val text = stream.reader().readText()
        return json.decodeFromString(text)
    }
}
