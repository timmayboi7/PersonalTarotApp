package com.timmay.tarot.domain

import java.security.SecureRandom
import java.time.LocalDate
import java.time.ZoneId
import kotlin.random.Random

object TarotRng {
    fun secureSeed(): Long = SecureRandom().nextLong()
    fun dailySeed(zone: ZoneId): Long {
        val day = LocalDate.vnow(zone).toEpochDay()
        return day xor 0x5A5A5A5AL
    }
    fun random(seed: Long): Random = Random(seed)
}

