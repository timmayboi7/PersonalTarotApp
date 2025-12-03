package com.timmay.tarot.domain

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.ZoneId

@Suppress("unused")
class RngTest {
    @Test
    fun `daily seed is consistent`() {
        val zone = ZoneId.of("America/New_York")
        val seed1 = TarotRng.dailySeed(zone)
        val seed2 = TarotRng.dailySeed(zone)
        assertEquals(seed1, seed2)
    }

    @Test
    fun `random is consistent`() {
        val seed = 12345L
        val random1 = TarotRng.random(seed)
        val random2 = TarotRng.random(seed)
        assertEquals(random1.nextInt(), random2.nextInt())
    }
}
