package com.timmay.tarot.repo

import android.content.res.AssetManager
import com.timmay.tarot.TarotApp
import com.timmay.tarot.domain.TarotCard
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class CardStore {
    private val json = Json { ignoreUnknownKeys = true }
    fun all(): List<TarotCard> {
        val assets: AssetManager = TarotApp.instance.assets
        assets.open("deck.json").use { stream ->
            val text = stream.reader().readText()
            return json.decodeFromString(text)
        }
    }
}
