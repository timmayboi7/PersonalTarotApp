package com.timmay.tarot.domain

import java.security.SecureRandom
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import kotlin.random.Random

object TarotRng {
    private const val SALT = 0x5A5A5A5AL

    fun secureSeed(): Long = SecureRandom().nextLong()

    fun dailySeed(zone: ZoneId?): Long {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && zone != null) {
            val day = LocalDate.now(zone).toEpochDay()
            day xor SALT
        } else {
            val calendar = Calendar.getInstance()
            val dayKey = calendar.get(Calendar.YEAR) * 10000 +
                (calendar.get(Calendar.MONTH) + 1) * 100 +
                calendar.get(Calendar.DAY_OF_MONTH)
            dayKey.toLong() xor SALT
        }
    }
    fun random(seed: Long): Random = Random(seed)
}

