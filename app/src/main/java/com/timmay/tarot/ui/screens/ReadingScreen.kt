package com.timmay.tarot.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.timmay.tarot.viewmodel.ReadingViewModel

@Composable
fun ReadingScreen(spreadId: String, vm: ReadingViewModel = viewModel()) {
    val ui by vm.ui.collectAsState()
    LaunchedEffect(spreadId) { vm.start(spreadId) }
    Scaffold(
        topBar = {
            val title = when (val state = ui) {
                is ReadingViewModel.Ui.Result -> state.spread.name
                else -> "Reading"
            }
            CenterAlignedTopAppBar(title = { Text(title) })
        }
    ) { padding ->
        when (val state = ui) {
            is ReadingViewModel.Ui.Loading -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is ReadingViewModel.Ui.Result -> {
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.surface,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                    Color(0xFF0E1116)
                                )
                            )
                        )
                        .padding(padding)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            state.spread.name,
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    itemsIndexed(state.cards) { idx, c ->
                        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp)) {
                                val title = (idx + 1).toString() + ". " + c.card.name + (if (c.isReversed) " (reversed)" else "")
                                Text(title, style = MaterialTheme.typography.titleMedium)
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "Position: " + state.spread.positions[idx].label,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    if (c.isReversed) c.card.meaningReversed else c.card.meaningUpright,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                if (c.card.keywordsUpright.isNotEmpty()) {
                                    Spacer(Modifier.height(6.dp))
                                    Text(
                                        c.card.keywordsUpright.joinToString(" • "),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }

                    item {
                        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp)) {
                                Text("Interpretation", style = MaterialTheme.typography.titleLarge)
                                Spacer(Modifier.height(6.dp))
                                Text(state.prose)
                                Spacer(Modifier.height(12.dp))
                                FilledTonalButton(
                                    onClick = { vm.start(spreadId) },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Draw again")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
