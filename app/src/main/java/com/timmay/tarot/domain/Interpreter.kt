package com.timmay.tarot.domain

import javax.inject.Inject

class Interpreter @Inject constructor() {
    fun compose(spread: Spread, cards: List<CardWithState>): String {
        val majors = cards.count { it.card.arcana == Arcana.MAJOR }
        val suits = cards.mapNotNull { it.card.suit }
        val dominantSuit = suits.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key
        val theme = StringBuilder().apply {
            if (majors >= 3) append("Major turning points are at play. ")
            if (dominantSuit != null) append("Energy leans toward " + dominantSuit + ". ")
        }.toString()
        val lines = cards.mapIndexed { i, c ->
            val pos = spread.positions[i].label
            val gist = if (c.isReversed) c.card.meaningReversed else c.card.meaningUpright
            "• " + c.card.name + " in " + pos + ": " + gist
        }
        return (theme + lines.joinToString("\n")).trim()
    }
}

data class CardWithState(val card: TarotCard, val isReversed: Boolean)
