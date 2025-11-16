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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.timmay.tarot.viewmodel.ReadingViewModel


@Composable
fun ReadingScreen(spreadId: String, vm: ReadingViewModel = viewModel()) {
    val ui by vm.ui.collectAsState()
    LaunchedEffect(spreadId) { vm.start(spreadId) }

    when (val state = ui) {
        is ReadingViewModel.Ui.Loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }
        is ReadingViewModel.Ui.Result -> {
            // Track per-card reveal state
            val revealed = remember(state.cards) {
                mutableStateListOf<Boolean>().apply { repeat(state.cards.size) { add(false) } }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.colorScheme.surface
                            )
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

                    itemsIndexed(state.cards) { idx, c ->
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Column(Modifier.padding(20.dp)) {
                                // Card image: back first, tap to flip to face from assets
                                val backPainter = rememberAsyncImagePainter("file:///android_asset/cards/Card Back.png")
                                val frontPath = "file:///android_asset/" + c.card.imageAsset
                                val frontPainter = rememberAsyncImagePainter(frontPath)

                                Box {
                                    Image(
                                        painter = if (revealed[idx]) frontPainter else backPainter,
                                        contentDescription = if (revealed[idx]) c.card.name else "Card back",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(2.75f / 4.75f)   // tarot aspect ratio
                                            .clip(RoundedCornerShape(18.dp))
                                            .clickable { revealed[idx] = !revealed[idx] },
                                        contentScale = ContentScale.Crop
                                    )

                                    Box(
                                        modifier = Modifier
                                            .matchParentSize()
                                            .background(
                                                brush = Brush.verticalGradient(
                                                    0f to Color.Transparent,
                                                    0.8f to MaterialTheme.colorScheme.scrim.copy(alpha = 0.4f)
                                                )
                                            )
                                    )

                                    Column(
                                        modifier = Modifier
                                            .align(Alignment.BottomStart)
                                            .padding(16.dp)
                                    ) {
                                        Text(
                                            text = "Card ${(idx + 1)}",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = Color.White.copy(alpha = 0.85f)
                                        )
                                        Text(
                                            text = c.card.name,
                                            style = MaterialTheme.typography.titleLarge,
                                            color = Color.White
                                        )
                                        if (c.isReversed) {
                                            Text(
                                                text = "Reversed",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = Color.White.copy(alpha = 0.9f)
                                            )
                                        }
                                    }
                                }

                                Spacer(Modifier.height(16.dp))

                                val meaning = if (c.isReversed) c.card.meaningReversed else c.card.meaningUpright

                                Text(
                                    text = state.spread.positions[idx].label,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = meaning,
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
                                state.prose,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }
        }
    }
}
