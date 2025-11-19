package com.timmay.tarot.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.matchParentSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.timmay.tarot.viewmodel.ReadingViewModel

@Composable
fun ReadingScreen(
    spreadId: String,
    viewModel: ReadingViewModel = hiltViewModel()
) {
    val ui by viewModel.ui.collectAsState()

    LaunchedEffect(spreadId) { viewModel.start(spreadId) }

    when (val state = ui) {
        is ReadingViewModel.Ui.Loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }
        is ReadingViewModel.Ui.Result -> ReadingResult(state)
    }
}

@Composable
private fun ReadingResult(state: ReadingViewModel.Ui.Result) {
    val revealed = remember(state.cards) {
        mutableStateListOf<Boolean>().also { list -> repeat(state.cards.size) { list.add(false) } }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    0f to MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    1f to MaterialTheme.colorScheme.background
                )
            )
            .padding(horizontal = 16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                ) {
                    Text(
                        text = state.spread.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "${state.spread.positions.size}-card spread",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )
                }
            }

            itemsIndexed(state.cards) { index, readingCard ->
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(Modifier.padding(20.dp)) {
                        val backPainter = rememberAsyncImagePainter("file:///android_asset/cards/Card Back.png")
                        val frontPainter = rememberAsyncImagePainter("file:///android_asset/${readingCard.card.imageAsset}")

                        Box {
                            Image(
                                painter = if (revealed[index]) frontPainter else backPainter,
                                contentDescription = if (revealed[index]) readingCard.card.name else "Card back",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(2.75f / 4.75f)
                                    .clip(RoundedCornerShape(18.dp))
                                    .clickable { revealed[index] = !revealed[index] },
                                contentScale = ContentScale.Crop
                            )

                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            0f to Color.Transparent,
                                            1f to Color.Black.copy(alpha = 0.35f)
                                        )
                                    )
                            )

                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Card ${index + 1}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                                Text(
                                    text = readingCard.card.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White
                                )
                                if (readingCard.isReversed) {
                                    Text(
                                        text = "Reversed",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        val position = state.spread.positions.getOrNull(index)
                        if (position != null) {
                            Text(
                                text = position.label,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            position.notes?.takeIf { it.isNotBlank() }?.let { notes ->
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = notes,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                        }

                        val meaning = if (readingCard.isReversed) {
                            readingCard.card.meaningReversed
                        } else {
                            readingCard.card.meaningUpright
                        }

                        Text(
                            text = meaning.ifBlank { "Trust your intuition here." },
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp)
                ) {
                    Text(
                        "Interpretation",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = state.prose,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f),
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}
