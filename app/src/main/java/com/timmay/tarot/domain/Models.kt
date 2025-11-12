package com.timmay.tarot.domain

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

enum class Arcana { MAJOR, MINOR }
enum class Suit { WANDS, CUPS, SWORDS, PENTACLES }

@SuppressLint("UnsafeOptInUsageError")
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
    val imageAsset: String = ""
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Position(val label: String, val notes: String? = null)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Spread(
    val id: String,
    val name: String,
    val positions: List<Position>
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class DrawnCard(
    val cardId: String,
    val isReversed: Boolean,
    val positionIndex: Int
)


/** A card that has been drawn and is part of a reading, with its full data */
data class ReadingCard(val card: TarotCard, val isReversed: Boolean)
