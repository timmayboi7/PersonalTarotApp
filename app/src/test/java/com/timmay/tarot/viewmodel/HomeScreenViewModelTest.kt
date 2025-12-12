package com.timmay.tarot.viewmodel

import com.timmay.tarot.domain.Arcana
import com.timmay.tarot.domain.TarotCard
import com.timmay.tarot.repo.CardStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchDailyCard draws deterministically for the day`() = runTest(dispatcher) {
        val cards = listOf(
            TarotCard(id = "a", name = "Alpha", arcana = Arcana.MAJOR, dailyDescription = "First"),
            TarotCard(id = "b", name = "Beta", arcana = Arcana.MINOR, dailyDescription = "Second")
        )
        val vm = HomeScreenViewModel(fakeCardStore(cards), dispatcher)

        vm.fetchDailyCard()
        advanceUntilIdle()

        val firstDraw = vm.ui.value
        assertTrue(firstDraw.dailyCardName in cards.map { it.name })
        assertTrue(firstDraw.isDrawn)

        vm.fetchDailyCard() // same day draw should keep the card
        advanceUntilIdle()

        val secondDraw = vm.ui.value
        assertEquals(firstDraw.dailyCardName, secondDraw.dailyCardName)
        assertEquals(firstDraw.drawDate, secondDraw.drawDate)
    }

    private fun fakeCardStore(cards: List<TarotCard>): CardStore {
        val json = Json.encodeToString(cards)
        return CardStore(ByteArrayInputStream(json.toByteArray()))
    }
}
