package com.timmay.tarot.repo

import android.content.Context
import com.timmay.tarot.domain.TarotCard
import kotlinx.serialization.json.Json
import java.io.InputStream

class CardStore(private val stream: InputStream) {
    private val json = Json { ignoreUnknownKeys = true }

    fun all(): List<TarotCard> {
        val text = stream.reader().readText()
        return json.decodeFromString(text)
    }

    companion object {
        @Volatile
        private var instance: CardStore? = null

        fun getInstance(context: Context): CardStore {
            return instance ?: synchronized(this) {
                instance ?: CardStore(
                    context.assets.open("cards/cards.json")
                ).also { instance = it }
            }
        }
    }
}
