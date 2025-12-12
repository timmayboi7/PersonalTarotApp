package com.timmay.tarot.repo

import com.timmay.tarot.domain.Spread
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton


@Singleton
class SpreadStore @Inject constructor(
    @Named("spreadsStream") stream: InputStream
) {
    private val json = Json { ignoreUnknownKeys = true }
    private val spreads: List<Spread>

    init {
        val text = stream.bufferedReader().use { it.readText() }
        spreads = json.decodeFromString(text)
    }

    fun all(): List<Spread> = spreads
}
