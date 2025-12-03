package com.timmay.tarot.repo

import com.timmay.tarot.TarotApp
import com.timmay.tarot.domain.Spread
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.InputStream

class SpreadStore {
    private val json = Json { ignoreUnknownKeys = true }

    fun all(): List<Spread> {
        val stream: InputStream = TarotApp.instance.assets.open("spreads.json")
        val text = stream.bufferedReader().use { it.readText() }
        return json.decodeFromString(text)
    }
}
