package com.timmay.tarot.domain

import javax.inject.Inject

class Interpreter @Inject constructor() {
    fun composeSummary(spread: Spread, cards: List<CardWithState>): ReadingSummary {
        val majors = cards.count { it.card.arcana == Arcana.MAJOR }
        val suits = cards.mapNotNull { it.card.suit }
        val dominantSuit = suits.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key
        val theme = StringBuilder().apply {
            if (majors >= 3) append("Major turning points are at play. ")
            if (dominantSuit != null) append("Energy leans toward $dominantSuit. ")
        }.toString().trim()
        val lines = cards.mapIndexed { i, c ->
            val pos = spread.positions.getOrNull(i)?.label ?: "Position ${i + 1}"
            val gist = if (c.isReversed && c.card.meaningReversed.isNotBlank()) {
                c.card.meaningReversed
            } else {
                c.card.meaningUpright
            }
            SummaryLine(
                position = pos,
                name = c.card.name,
                isReversed = c.isReversed,
                meaning = gist
            )
        }
        return ReadingSummary(theme = theme, lines = lines)
    }
}

data class CardWithState(val card: TarotCard, val isReversed: Boolean)

data class SummaryLine(
    val position: String,
    val name: String,
    val isReversed: Boolean,
    val meaning: String
)

data class ReadingSummary(val theme: String, val lines: List<SummaryLine>)
