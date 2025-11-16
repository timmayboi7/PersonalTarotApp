package com.timmay.tarot.repo

import android.content.Context
import com.timmay.tarot.domain.Spread
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpreadStore @Inject constructor(
    @ApplicationContext context: Context
) {
    private val json = Json { ignoreUnknownKeys = true }
    private val spreads: List<Spread>

    init {
        val text = context.assets.open("spreads.json").reader().readText()
        spreads = json.decodeFromString(text)
    }

    fun all(): List<Spread> = spreads

    fun byId(id: String): Spread? = spreads.find { it.id == id }
}