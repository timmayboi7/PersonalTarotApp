package com.timmay.tarot.repo

import com.timmay.tarot.TarotApp
import com.timmay.tarot.domain.TarotCard
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardStore @Inject constructor(
    @ApplicationContext context: Context) {
    private val json = Json { ignoreUnknownKeys = true }
    fun all(): List<TarotCard> {
        val stream: InputStream = TarotApp.instance.assets.open("deck.json")
        val text = stream.bufferedReader().use { it.readText() }
        return json.decodeFromString(text)
    }

    fun all(): List<TarotCard> = cards
}