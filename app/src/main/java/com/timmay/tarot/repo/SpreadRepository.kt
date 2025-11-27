package com.timmay.tarot.repo

import android.content.Context
import com.timmay.tarot.domain.Spread
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpreadRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val json = Json { ignoreUnknownKeys = true }

    @Volatile
    private var cached: List<Spread>? = null

    suspend fun all(): List<Spread> {
        val existing = cached
        if (existing != null) return existing
        return withContext(Dispatchers.IO) {
            cached ?: loadSpreads().also { cached = it }
        }
    }

    suspend fun byId(id: String): Spread {
        val spreads = all()
        return spreads.firstOrNull { it.id == id } ?: spreads.first()
    }

    private fun loadSpreads(): List<Spread> {
        return context.assets.open(SPREADS_ASSET).bufferedReader().use { reader ->
            json.decodeFromString<List<Spread>>(reader.readText())
        }
    }

    companion object {
        private const val SPREADS_ASSET = "spreads.json"
    }
}
