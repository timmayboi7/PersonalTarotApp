package com.timmay.tarot.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.timmay.tarot.viewmodel.ReadingViewModel

private const val CARD_ASPECT_RATIO = 1f / 1.75f

@Composable
fun ReadingScreen(
    spreadId: String,
    vm: ReadingViewModel = hiltViewModel()
) {
    val ui by vm.ui.collectAsState()
    LaunchedEffect(spreadId) { vm.start(spreadId) }

    when (val state = ui) {
        is ReadingViewModel.Ui.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is ReadingViewModel.Ui.Result -> {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(state.cards) { i, cardWithState ->
                        val (card, isReversed) = cardWithState
                        val cardModifier = Modifier
                            .aspectRatio(CARD_ASPECT_RATIO)
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
                            .clickable { vm.reveal(i) }

                        if (state.revealed[i]) {
                            AsyncImage(
                                model = "file:///android_asset/${card.imageAsset}",
                                contentDescription = card.name,
                                modifier = cardModifier.rotate(if (isReversed) 180f else 0f)
                            )
                        } else {
                            AsyncImage(
                                model = "file:///android_asset/card_back.png",
                                contentDescription = "Card back",
                                modifier = cardModifier
                            )
                        }
                    }
                }

                if (state.revealed.all { it }) {
                    Text(
                        text = state.prose,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
