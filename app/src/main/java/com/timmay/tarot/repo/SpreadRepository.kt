package com.timmay.tarot.repo

import com.timmay.tarot.domain.Spread
import com.timmay.tarot.domain.Position
import javax.inject.Inject

class SpreadRepository @Inject constructor() {
    fun all(): List<Spread> = listOf(
        Spread("single", "Single Card", listOf(Position("Message"))),
        Spread("three_card", "Past • Present • Future", listOf(
            Position("Past"), Position("Present"), Position("Future")
        )),
        Spread("decision", "Two Paths", listOf(Position("Option A"), Position("Option B"))),
        Spread("relationship", "Relationship (6)", listOf(
            Position("You"), Position("Them"), Position("Dynamic"), Position("Obstacle"), Position("Advice"), Position("Outcome")
        )),
        Spread("celtic_cross", "Celtic Cross (10)", listOf(
            Position("Significator"), Position("Crossing"), Position("Crowning"), Position("Root"),
            Position("Past"), Position("Future"), Position("Self"), Position("Environment"),
            Position("Hopes/Fears"), Position("Outcome")
        ))
    )
    fun byId(id: String) = all().firstOrNull { it.id == id } ?: all()[1]
}
