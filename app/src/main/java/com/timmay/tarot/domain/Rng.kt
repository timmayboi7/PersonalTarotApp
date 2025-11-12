package com.timmay.tarot.domain

import android.os.Build
import java.security.SecureRandom
import java.time.LocalDate
import java.time.ZoneId
import kotlin.random.Random

object TarotRng {
    fun secureSeed(): Long = SecureRandom().nextLong()

    fun dailySeed(zone: ZoneId): Long {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val day = LocalDate.now(zone).toEpochDay()
            day xor 0x5A5A5A5AL
        } else {
            // Fallback for older devices
            LocalDate.now().toEpochDay() xor 0x5A5A5A5AL
        }
    }

    fun random(seed: Long): Random = Random(seed)
}
