package com.timmay.tarot.domain

import javax.inject.Inject

class Interpreter @Inject constructor() {
    fun compose(spread: Spread, cards: List<ReadingCard>): String {
        if (cards.isEmpty()) return "No cards were drawn."
        val majors = cards.count { it.card.arcana == Arcana.MAJOR }
        val suits = cards.mapNotNull { it.card.suit }
        val dominantSuit = suits.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key
        val theme = buildString {
            if (majors >= cards.size / 2) append("Major turning points are at play. ")
            if (dominantSuit != null) append("Energy leans toward $dominantSuit. ")
        }
        val lines = cards.mapIndexed { index, readingCard ->
            val positionLabel = spread.positions.getOrNull(index)?.label ?: "Position ${index + 1}"
            val gist = (if (readingCard.isReversed) {
                readingCard.card.meaningReversed
            } else {
                readingCard.card.meaningUpright
            }).ifBlank {
                val keywords = if (readingCard.isReversed) readingCard.card.keywordsReversed else readingCard.card.keywordsUpright
                keywords.joinToString(separator = ", ") { it }
            }
            "• ${readingCard.card.name} in $positionLabel: $gist"
        }
        return (theme + lines.joinToString("\n")).trim()
    }
}
