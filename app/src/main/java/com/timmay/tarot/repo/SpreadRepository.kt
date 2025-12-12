package com.timmay.tarot.repo

import com.timmay.tarot.domain.Spread
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpreadRepository @Inject constructor(private val spreadStore: SpreadStore) {

    fun all(): List<Spread> = spreadStore.all()
    fun byId(id: String): Spread {
        val spreads = all()
        return spreads.firstOrNull { it.id == id } ?: spreads.getOrElse(1) { spreads.first() }
    }
}
