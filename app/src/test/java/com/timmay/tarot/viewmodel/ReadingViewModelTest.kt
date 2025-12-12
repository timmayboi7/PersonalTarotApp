package com.timmay.tarot.viewmodel

import com.timmay.tarot.domain.Arcana
import com.timmay.tarot.domain.Interpreter
import com.timmay.tarot.domain.Position
import com.timmay.tarot.domain.Spread
import com.timmay.tarot.domain.Suit
import com.timmay.tarot.domain.TarotCard
import com.timmay.tarot.repo.CardStore
import com.timmay.tarot.repo.SpreadRepository
import com.timmay.tarot.repo.SpreadStore
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
class ReadingViewModelTest {

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
    fun `start loads spread and cards then reveal updates state`() = runTest(dispatcher) {
        val cards = listOf(
            TarotCard(id = "1", name = "Ace of Wands", arcana = Arcana.MINOR, suit = Suit.WANDS, meaningUpright = "Spark"),
            TarotCard(id = "2", name = "High Priestess", arcana = Arcana.MAJOR, meaningUpright = "Intuition"),
            TarotCard(id = "3", name = "Ten of Cups", arcana = Arcana.MINOR, suit = Suit.CUPS, meaningUpright = "Joy")
        )
        val spreads = listOf(
            Spread(
                id = "three_card",
                name = "Three Card",
                positions = listOf(Position("Past"), Position("Present"), Position("Future"))
            )
        )

        val vm = ReadingViewModel(
            cardStore = fakeCardStore(cards),
            spreadRepository = SpreadRepository(fakeSpreadStore(spreads)),
            interpreter = Interpreter(),
            ioDispatcher = dispatcher
        )

        vm.start("three_card")
        advanceUntilIdle()

        val state = vm.ui.value as ReadingViewModel.Ui.Result
        assertEquals(3, state.cards.size)
        assertEquals(listOf(false, false, false), state.revealed)
        assertEquals("Three Card", state.spread.name)

        vm.reveal(1)
        val updated = vm.ui.value as ReadingViewModel.Ui.Result
        assertTrue(updated.revealed[1])
    }

    private fun fakeCardStore(cards: List<TarotCard>): CardStore {
        val json = Json.encodeToString(cards)
        return CardStore(ByteArrayInputStream(json.toByteArray()))
    }

    private fun fakeSpreadStore(spreads: List<Spread>): SpreadStore {
        val json = Json.encodeToString(spreads)
        return SpreadStore(ByteArrayInputStream(json.toByteArray()))
    }
}
