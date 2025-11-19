package com.timmay.tarot.repo

import android.content.Context
import com.timmay.tarot.domain.TarotCard
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val json = Json { ignoreUnknownKeys = true }

    @Volatile
    private var cached: List<TarotCard>? = null

    suspend fun all(): List<TarotCard> {
        val existing = cached
        if (existing != null) return existing
        return withContext(Dispatchers.IO) {
            cached ?: loadDeck().also { cached = it }
        }
    }

    private fun loadDeck(): List<TarotCard> {
        return context.assets.open(DECK_ASSET).bufferedReader().use { reader ->
            json.decodeFromString<List<TarotCard>>(reader.readText())
        }
    }

    companion object {
        private const val DECK_ASSET = "deck.json"
    }
}
