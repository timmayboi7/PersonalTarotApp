package com.timmay.tarot.repo

import com.timmay.tarot.domain.Spread

class SpreadRepository {
    private val spreadStore = SpreadStore()

    fun all(): List<Spread> = spreadStore.all()
    fun byId(id: String): Spread {
        val spreads = all()
        return spreads.firstOrNull { it.id == id } ?: spreads.getOrElse(1) { spreads.first() }
    }
}
