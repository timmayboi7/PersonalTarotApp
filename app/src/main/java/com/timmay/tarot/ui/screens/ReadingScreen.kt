package com.timmay.tarot.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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

            LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
                item { Text(state.spread.name, style = MaterialTheme.typography.headlineSmall) }

                itemsIndexed(state.cards) { idx, c ->
                    ElevatedCard(modifier = Modifier.padding(vertical = 12.dp)) {
                        Column(Modifier.padding(16.dp)) {
                            // Card image: back first, tap to flip to face from assets
                            val backPainter = rememberAsyncImagePainter("file:///android_asset/cards/Card Back.png")
                            val frontPath = "file:///android_asset/" + c.card.imageAsset
                            val frontPainter = rememberAsyncImagePainter(frontPath)

                            Image(
                                painter = if (revealed[idx]) frontPainter else backPainter,
                                contentDescription = if (revealed[idx]) c.card.name else "Card back",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(2.75f / 4.75f)   // tarot aspect ratio
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { revealed[idx] = !revealed[idx] },
                                contentScale = ContentScale.Crop
                            )

                            Spacer(Modifier.height(12.dp))

                            val title = (idx + 1).toString() + ". " + c.card.name + (if (c.isReversed) " (reversed)" else "")
                            Text(title, style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(4.dp))
                            Text("Position: " + state.spread.positions[idx].label, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(12.dp))
                    Text("Interpretation", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(6.dp))
                    Text(state.prose)
                }
            }
        }
    }
}
