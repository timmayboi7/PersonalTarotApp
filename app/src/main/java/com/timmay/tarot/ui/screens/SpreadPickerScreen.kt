package com.timmay.tarot.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.BorderStroke
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.timmay.tarot.domain.Spread
import com.timmay.tarot.ui.theme.Fog
import com.timmay.tarot.ui.theme.DeepNavy
import com.timmay.tarot.ui.theme.Gold
import com.timmay.tarot.ui.theme.Midnight
import com.timmay.tarot.viewmodel.SpreadPickerViewModel
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpreadPickerScreen(
    nav: NavController,
    vm: SpreadPickerViewModel = hiltViewModel()
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    val selectedDeckId = remember { mutableStateOf("single_deck") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Choose a Spread", color = Gold) },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Gold)
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Midnight, DeepNavy)
                    )
                )
                .padding(padding)
        ) {
            when (val state = ui) {
                is SpreadPickerViewModel.Ui.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is SpreadPickerViewModel.Ui.Result -> {
                    val spreadMap = state.spreads.associateBy { it.id }
                    val decks = deckDefinitions().mapNotNull { def ->
                        val spreads = def.spreadIds.mapNotNull { spreadMap[it] }
                        if (spreads.isEmpty()) null else Deck(def.id, def.faceLabel, def.subtitle, def.accent, spreads)
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        Text(
                            text = "Pick a deck to reveal spreads. Tap a revealed card to start reading.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Fog
                        )
                        decks.forEach { deck ->
                            SpreadDeckCard(
                                deck = deck,
                                isSelected = selectedDeckId.value == deck.id,
                                onSelect = { selectedDeckId.value = deck.id },
                                onOpenSpread = { nav.navigate("reading/" + it.id) }
                            )
                        }
                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun Chip(text: String) {
    Card(
        shape = RoundedCornerShape(50),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

private data class DeckDefinition(
    val id: String,
    val faceLabel: String,
    val subtitle: String,
    val accent: Color,
    val spreadIds: List<String>
)

private data class Deck(
    val id: String,
    val faceLabel: String,
    val subtitle: String,
    val accent: Color,
    val spreads: List<Spread>
)

private fun deckDefinitions(): List<DeckDefinition> = listOf(
    DeckDefinition(
        id = "single_deck",
        faceLabel = "Single Card",
        subtitle = "Direct guidance from one card",
        accent = Gold,
        spreadIds = listOf("single")
    ),
    DeckDefinition(
        id = "three_deck",
        faceLabel = "3-Card",
        subtitle = "Quick clarity in threes",
        accent = Fog,
        spreadIds = listOf("three_card", "yes_no_maybe", "mind_body_spirit")
    ),
    DeckDefinition(
        id = "five_deck",
        faceLabel = "5-Card",
        subtitle = "More context and pattern",
        accent = Color(0xFF9BD0D9),
        spreadIds = listOf("cross_five", "rectangle_five")
    ),
    DeckDefinition(
        id = "advanced_deck",
        faceLabel = "Advanced",
        subtitle = "Deep spreads for bigger stories",
        accent = Color(0xFFEFA4C6),
        spreadIds = listOf("celtic_cross", "compatibility_h", "horseshoe")
    )
)

@Composable
private fun SpreadDeckCard(
    deck: Deck,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onOpenSpread: (Spread) -> Unit
) {
    Card(
        onClick = onSelect,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 14.dp else 8.dp),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(deck.faceLabel, style = MaterialTheme.typography.titleMedium, color = deck.accent)
                    Text(deck.subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                }
                Card(
                    colors = CardDefaults.cardColors(containerColor = deck.accent.copy(alpha = 0.16f)),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = if (isSelected) "Open" else "Tap",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = deck.accent
                    )
                }
            }
            if (isSelected) {
                SpreadFan(spreads = deck.spreads, accent = deck.accent, onOpenSpread = onOpenSpread)
            }
        }
    }
}

@Composable
private fun SpreadFan(
    spreads: List<Spread>,
    accent: Color,
    onOpenSpread: (Spread) -> Unit
) {
    SpreadCarousel(
        spreads = spreads,
        accent = accent,
        onOpenSpread = onOpenSpread
    )
}

@Composable
private fun SpreadCarousel(
    spreads: List<Spread>,
    accent: Color,
    onOpenSpread: (Spread) -> Unit
) {
    val activeIndex = remember { mutableStateOf(spreads.size / 2) }
    val baseOffset = 110.dp
    val baseRotation = 10f
    val baseHeight = 330.dp
    val density = LocalDensity.current
    var dragPx by remember { mutableStateOf(0f) }
    val dragThreshold = 80f

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(baseHeight)
                .pointerInput(spreads.size) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            dragPx += dragAmount
                        },
                        onDragEnd = {
                            when {
                                dragPx > dragThreshold -> activeIndex.value = (activeIndex.value - 1).coerceAtLeast(0)
                                dragPx < -dragThreshold -> activeIndex.value = (activeIndex.value + 1).coerceAtMost(spreads.lastIndex)
                            }
                            dragPx = 0f
                        },
                        onDragCancel = { dragPx = 0f }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            spreads.forEachIndexed { index, spread ->
                val delta = index - activeIndex.value
                val distance = abs(delta)
                val dragDp = with(density) { dragPx.toDp() }
                val targetOffset = baseOffset * delta + dragDp * 0.35f
                val targetRotation = baseRotation * delta + dragPx / 30f
                val targetScale = if (delta == 0) 1.08f else 0.9f - 0.05f * (distance - 1).coerceAtLeast(0)
                val targetElevation = if (delta == 0) 18.dp else 8.dp
                val targetZ = 8f - distance
                val targetAlpha = (0.95f - 0.16f * distance).coerceIn(0.45f, 0.95f)
                val targetYOffset = (distance * 14).dp

                val animatedOffset by animateDpAsState(targetOffset, label = "offset")
                val animatedRotation by animateFloatAsState(targetRotation, label = "rotation")
                val animatedScale by animateFloatAsState(targetScale, label = "scale")
                val animatedYOffset by animateDpAsState(targetYOffset, label = "yOffset")

                Card(
                    onClick = {
                        if (delta == 0) {
                            onOpenSpread(spread)
                        } else {
                            activeIndex.value = index
                        }
                    },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161F2A)),
                    elevation = CardDefaults.cardElevation(defaultElevation = targetElevation),
                    shape = RoundedCornerShape(15.dp),
                    border = BorderStroke(1.dp, if (delta == 0) accent else accent.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .offset(x = animatedOffset, y = animatedYOffset)
                        .graphicsLayer {
                            rotationZ = animatedRotation
                            scaleX = animatedScale
                            scaleY = animatedScale
                        }
                        .fillMaxWidth(0.58f)
                        .zIndex(targetZ)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(spread.name, style = MaterialTheme.typography.titleMedium, color = accent, textAlign = TextAlign.Center)
                        Text(
                            text = spread.positions.joinToString(" • ") { it.label },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = targetAlpha),
                            textAlign = TextAlign.Center
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Chip("Cards: ${spread.positions.size}")
                            Chip(if (delta == 0) "Tap to draw" else "Tap to focus")
                        }
                    }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Card(
                onClick = { activeIndex.value = (activeIndex.value - 1).coerceAtLeast(0) },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF223040)),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Text("<", modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp), color = accent)
            }
            Card(
                onClick = { activeIndex.value = (activeIndex.value + 1).coerceAtMost(spreads.lastIndex) },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF223040)),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Text(">", modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp), color = accent)
            }
        }
    }
}
