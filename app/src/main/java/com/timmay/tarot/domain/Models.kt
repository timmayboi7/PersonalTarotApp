package com.timmay.tarot.domain

import kotlinx.serialization.Serializable

@Serializable
enum class Arcana { MAJOR, MINOR }
@Serializable
enum class Suit { WANDS, CUPS, SWORDS, PENTACLES }

@Serializable
data class TarotCard(
    val id: String,
    val name: String,
    val arcana: Arcana,
    val suit: Suit? = null,
    val number: Int? = null,
    val keywordsUpright: List<String> = emptyList(),
    val keywordsReversed: List<String> = emptyList(),
    val meaningUpright: String = "",
    val meaningReversed: String = "",
    val imageAsset: String = "",
    val dailyDescription: String = ""
)

data class ReadingCard(
    val card: TarotCard,
    val isReversed: Boolean
)

@Serializable
data class Position(val label: String, val notes: String? = null)

@Serializable
data class Spread(
    val id: String,
    val name: String,
    val positions: List<Position>
)
