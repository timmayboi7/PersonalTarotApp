package com.timmay.tarot.repo

import com.timmay.tarot.domain.ReadingCard
import com.timmay.tarot.domain.TarotCard
import java.util.Random
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("unused")
@Singleton
class DeckRepository @Inject constructor(
    private val cardStore: CardStore
) {
    fun draw(count: Int, seed: Long): List<ReadingCard> {
        val cards = cardStore.all()
        val deckRandom = Random(seed)
        val reversedRandom = Random(seed xor REVERSED_SALT)
        return cards
            .shuffled(deckRandom)
            .take(count)
            .map { card -> ReadingCard(card, reversedRandom.nextBoolean()) }
    }

    fun pickDailyCard(seed: Long): TarotCard {
        val deck = cardStore.all()
        val random = Random(seed)
        return deck[random.nextInt(deck.size)]
    }

    companion object {
        private const val REVERSED_SALT = 0x7F4A7C15L
    }
}
