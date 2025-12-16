package com.timmay.tarot.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import com.timmay.tarot.ui.theme.DeepNavy
import com.timmay.tarot.ui.theme.Gold
import com.timmay.tarot.ui.theme.Midnight
import com.timmay.tarot.viewmodel.ReadingViewModel

private const val CARD_ASPECT_RATIO = 1f / 1.75f

@Composable
fun ReadingScreen(
    spreadId: String,
    vm: ReadingViewModel = hiltViewModel()
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    LaunchedEffect(spreadId) { vm.start(spreadId) }

    when (val state = ui) {
        is ReadingViewModel.Ui.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is ReadingViewModel.Ui.Result -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Midnight, DeepNavy)
                        )
                    )
            ) {
                ReadingContent(state = state, onReveal = vm::reveal)
            }
        }
    }
}

@Composable
private fun ReadingContent(
    state: ReadingViewModel.Ui.Result,
    onReveal: (Int) -> Unit,
) {
    val expandedIndex = rememberSaveable { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = state.spread.name,
                    style = MaterialTheme.typography.headlineSmall
                )

                val onExpand: (Int) -> Unit = { expandedIndex.value = it }
                when (state.spread.id) {
                    "single" -> SingleCardLayout(state, onReveal, onExpand)
                    "three_card", "three_card_love", "yes_no_maybe", "mind_body_spirit" -> ThreeCardLayout(state, onReveal, onExpand)
                    "cross_five" -> CrossFiveLayout(state, onReveal, onExpand)
                    "rectangle_five" -> RectangleFiveLayout(state, onReveal, onExpand)
                    "celtic_cross" -> CelticCrossLayout(state, onReveal, onExpand)
                    "horseshoe" -> HorseshoeLayout(state, onReveal, onExpand)
                    "compatibility_h" -> CompatibilityHLayout(state, onReveal, onExpand)
                    "astrological_twelve" -> AstrologicalLayout(state, onReveal, onExpand)
                    else -> HorizontalListLayout(state, onReveal, onExpand)
                }
            }
        }

        if (state.revealed.all { it }) {
            Spacer(Modifier.height(16.dp))
            SummarySection(state)
        }
    }

    FullscreenCardOverlay(
        state = state,
        expandedIndex = expandedIndex.value,
        onDismiss = { expandedIndex.value = null }
    )
}

@Composable
private fun SingleCardLayout(
    state: ReadingViewModel.Ui.Result,
    onReveal: (Int) -> Unit,
    onExpand: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        CardSlot(index = 0, state = state, onReveal = onReveal, onExpand = onExpand)
    }
}

@Composable
private fun ThreeCardLayout(
    state: ReadingViewModel.Ui.Result,
    onReveal: (Int) -> Unit,
    onExpand: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {
        state.cards.indices.forEach { i ->
            CardSlot(index = i, state = state, onReveal = onReveal, onExpand = onExpand)
        }
    }
}

@Composable
private fun CelticCrossLayout(
    state: ReadingViewModel.Ui.Result,
    onReveal: (Int) -> Unit,
    onExpand: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CardSlot(index = 2, state = state, onReveal = onReveal, onExpand = onExpand)
            Box(contentAlignment = Alignment.Center) {
                CardSlot(index = 0, state = state, onReveal = onReveal, onExpand = onExpand)
                CardSlot(
                    index = 1,
                    state = state,
                    onReveal = onReveal,
                    onExpand = onExpand,
                    modifier = Modifier.rotate(90f)
                )
            }
            CardSlot(index = 3, state = state, onReveal = onReveal, onExpand = onExpand)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CardSlot(index = 4, state = state, onReveal = onReveal, onExpand = onExpand)
            CardSlot(index = 5, state = state, onReveal = onReveal, onExpand = onExpand)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            (6..9).forEach { i ->
                CardSlot(index = i, state = state, onReveal = onReveal, onExpand = onExpand)
            }
        }
    }
}

@Composable
private fun CrossFiveLayout(
    state: ReadingViewModel.Ui.Result,
    onReveal: (Int) -> Unit,
    onExpand: (Int) -> Unit
) {
    // Expected order: Past, Present, Future, Root Cause, Potential (top)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp),
        contentAlignment = Alignment.Center
    ) {
        val offsets = listOf(
            (-150).dp to 0.dp,   // Past (left)
            0.dp to 0.dp,        // Present (center)
            150.dp to 0.dp,      // Future (right)
            0.dp to 140.dp,      // Root Cause (below)
            0.dp to (-140).dp    // Potential (above)
        )
        offsets.forEachIndexed { idx, (x, y) ->
            CardSlot(
                index = idx,
                state = state,
                onReveal = onReveal,
                onExpand = onExpand,
                modifier = Modifier.offset(x = x, y = y)
            )
        }
    }
}

@Composable
private fun RectangleFiveLayout(
    state: ReadingViewModel.Ui.Result,
    onReveal: (Int) -> Unit,
    onExpand: (Int) -> Unit
) {
    // Expected order: Theme (center), Fear/Block (top), Desire (right), Conflict (bottom), Outside Perspective (left)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp),
        contentAlignment = Alignment.Center
    ) {
        val offsets = listOf(
            0.dp to 0.dp,            // Theme
            0.dp to (-140).dp,       // Fear/Block (top)
            160.dp to 0.dp,          // Desire (right)
            0.dp to 140.dp,          // Conflict (bottom)
            (-160).dp to 0.dp        // Outside Perspective (left)
        )
        offsets.forEachIndexed { idx, (x, y) ->
            CardSlot(
                index = idx,
                state = state,
                onReveal = onReveal,
                onExpand = onExpand,
                modifier = Modifier.offset(x = x, y = y)
            )
        }
    }
}

@Composable
private fun HorseshoeLayout(
    state: ReadingViewModel.Ui.Result,
    onReveal: (Int) -> Unit,
    onExpand: (Int) -> Unit
) {
    // 7-card arc: Past, Present, Future, Advice, External, Hopes/Fears, Outcome
    val positions = listOf(
        (-180).dp to (-80).dp, // Card 1 - upper left
        (-130).dp to 20.dp,    // Card 2
        (-80).dp to 100.dp,    // Card 3 - bottom of the curve
        0.dp to 130.dp,        // Card 4 - bottom middle
        80.dp to 100.dp,       // Card 5
        130.dp to 20.dp,       // Card 6
        180.dp to (-80).dp     // Card 7 - upper right
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp),
        contentAlignment = Alignment.Center
    ) {
        positions.forEachIndexed { i, (x, y) ->
            CardSlot(
                index = i,
                state = state,
                onReveal = onReveal,
                onExpand = onExpand,
                modifier = Modifier.offset(x = x, y = y)
            )
        }
    }
}

@Composable
private fun AstrologicalLayout(
    state: ReadingViewModel.Ui.Result,
    onReveal: (Int) -> Unit,
    onExpand: (Int) -> Unit
) {
    // 12-card circle for zodiac signs, starting at top and moving clockwise
    val offsets = listOf(
        0.dp to (-200).dp,     // Aries
        100.dp to (-170).dp,   // Taurus
        170.dp to (-100).dp,   // Gemini
        200.dp to 0.dp,        // Cancer
        170.dp to 100.dp,      // Leo
        100.dp to 170.dp,      // Virgo
        0.dp to 200.dp,        // Libra
        (-100).dp to 170.dp,   // Scorpio
        (-170).dp to 100.dp,   // Sagittarius
        (-200).dp to 0.dp,     // Capricorn
        (-170).dp to (-100).dp,// Aquarius
        (-100).dp to (-170).dp // Pisces
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(440.dp),
        contentAlignment = Alignment.Center
    ) {
        offsets.forEachIndexed { i, (x, y) ->
            CardSlot(
                index = i,
                state = state,
                onReveal = onReveal,
                onExpand = onExpand,
                modifier = Modifier.offset(x = x, y = y)
            )
        }
    }
}

@Composable
private fun CompatibilityHLayout(
    state: ReadingViewModel.Ui.Result,
    onReveal: (Int) -> Unit,
    onExpand: (Int) -> Unit
) {
    // Order: You (Mind, Heart, Root/Passion), Bond, Partner (Mind, Heart, Root/Passion)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp),
        contentAlignment = Alignment.Center
    ) {
        val offsets = listOf(
            (-180).dp to (-140).dp, // You — Mind
            (-180).dp to 0.dp,      // You — Heart
            (-180).dp to 140.dp,    // You — Root/Passion
            0.dp to 0.dp,           // Bond Between (horizontal bridge)
            (180).dp to (-140).dp,  // Partner — Mind
            (180).dp to 0.dp,       // Partner — Heart
            (180).dp to 140.dp      // Partner — Root/Passion
        )
        offsets.forEachIndexed { i, (x, y) ->
            CardSlot(
                index = i,
                state = state,
                onReveal = onReveal,
                onExpand = onExpand,
                modifier = Modifier.offset(x = x, y = y)
            )
        }
    }
}
@Composable
private fun HorizontalListLayout(
    state: ReadingViewModel.Ui.Result,
    onReveal: (Int) -> Unit,
    onExpand: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(state.cards) { i, _ ->
            CardSlot(index = i, state = state, onReveal = onReveal, onExpand = onExpand)
        }
    }
}

@Composable
private fun CardSlot(
    index: Int,
    state: ReadingViewModel.Ui.Result,
    onReveal: (Int) -> Unit,
    onExpand: ((Int) -> Unit)?,
    modifier: Modifier = Modifier,
    ) {
        val cardWithState = state.cards.getOrNull(index) ?: return
        val (card, isReversed) = cardWithState
        val revealed = state.revealed.getOrNull(index) == true
        val positionLabel = state.spread.positions.getOrNull(index)?.label ?: "Card ${index + 1}"
        val cardWidth = 120.dp
        val scale by animateFloatAsState(
            targetValue = if (revealed) 1f else 0.95f,
            animationSpec = tween(180),
            label = "cardScale"
    )
    val alpha by animateFloatAsState(
        targetValue = if (revealed) 1f else 0.85f,
        animationSpec = tween(180),
        label = "cardAlpha"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(cardWidth)
    ) {
        Card(
            modifier = Modifier
                .aspectRatio(CARD_ASPECT_RATIO)
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                    onReveal(index)
                    onExpand?.invoke(index)
                }
                .scale(scale),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            val imageModifier = Modifier
                .fillMaxSize()
                .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
                .graphicsLayer { this.alpha = alpha }
            if (revealed) {
                AsyncImage(
                    model = "file:///android_asset/${card.imageAsset}",
                    contentDescription = card.name,
                    modifier = imageModifier.rotate(if (isReversed) 180f else 0f),
                    contentScale = ContentScale.Crop
                )
            } else {
                AsyncImage(
                    model = "file:///android_asset/cards/Card%20Back.png",
                    contentDescription = "Card back",
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun SummarySection(state: ReadingViewModel.Ui.Result) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "Reading Summary",
                style = MaterialTheme.typography.titleMedium
            )
            if (state.summary.theme.isNotBlank()) {
                Text(
                    text = state.summary.theme,
                    style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 22.sp)
                )
            }
            state.summary.lines.forEach { line ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${line.position}: ${line.name}" + if (line.isReversed) " (reversed)" else "",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Text(
                            text = line.meaning,
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FullscreenCardOverlay(
    state: ReadingViewModel.Ui.Result,
    expandedIndex: Int?,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val svgLoader = remember {
        ImageLoader.Builder(context)
            .components { add(SvgDecoder.Factory()) }
            .build()
    }
    val cardWithState = expandedIndex?.let { state.cards.getOrNull(it) } ?: return
    val (card, isReversed) = cardWithState
    val positionLabel = state.spread.positions.getOrNull(expandedIndex ?: 0)?.label.orEmpty()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        val scale by animateFloatAsState(
            targetValue = 1f,
            animationSpec = tween(240),
            label = "dialogScale"
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onDismiss() }
                .background(Color(0xCC000000)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(CARD_ASPECT_RATIO)
                    .scale(scale),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                val plaqueShape = RoundedCornerShape(14.dp)
                Box {
                    AsyncImage(
                        model = "file:///android_asset/${card.imageAsset}",
                        contentDescription = card.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(if (isReversed) 180f else 0f),
                        contentScale = ContentScale.Crop
                    )
                    // Top ornate plaque
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.82f)
                            .padding(top = 16.dp)
                            .align(Alignment.TopCenter)
                            .clip(plaqueShape)
                            .border(
                                1.6.dp,
                                Brush.linearGradient(listOf(Gold, Gold.copy(alpha = 0.65f))),
                                plaqueShape
                            )
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xFF0D1524),
                                        Color(0xFF162233)
                                    )
                                )
                            )
                            .padding(vertical = 10.dp, horizontal = 14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp, 2.dp)
                                    .background(Gold.copy(alpha = 0.7f))
                            )
                            Text(
                                text = positionLabel.uppercase(),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    letterSpacing = 1.2.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Gold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                            Box(
                                modifier = Modifier
                                    .size(10.dp, 2.dp)
                                    .background(Gold.copy(alpha = 0.7f))
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.92f),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0x991A1A1A),
                                    Color(0x730E0E0E)
                                )
                            )
                        )
                )
                AsyncImage(
                    model = "file:///android_asset/title_block.svg",
                    imageLoader = svgLoader,
                    contentDescription = "Title border",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    contentScale = ContentScale.FillBounds
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.7f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = card.name,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontSize = 28.sp,
                            letterSpacing = 1.1.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = Gold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
