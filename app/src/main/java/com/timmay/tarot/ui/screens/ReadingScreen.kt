package com.timmay.tarot.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.timmay.tarot.viewmodel.ReadingViewModel

@Composable
fun ReadingScreen(spreadId: String, vm: ReadingViewModel = viewModel()) {
    val ui by vm.ui.collectAsState()
    LaunchedEffect(spreadId) { vm.start(spreadId) }
    when(val state = ui) {
        is ReadingViewModel.Ui.Loading -> Box(Modifier.fillMaxSize()) { CircularProgressIndicator() }
        is ReadingViewModel.Ui.Result -> {
            LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
                item { Text(state.spread.name, style = MaterialTheme.typography.headlineSmall) }
                itemsIndexed(state.cards) { idx, c ->
                    ElevatedCard(modifier = Modifier.padding(vertical = 8.dp)) {
                        Column(Modifier.padding(16.dp)) {
                            val title = (idx + 1).toString() + ". " + c.card.name + (if (c.isReversed) " (reversed)" else "")
                            Text(title)
                            Spacer(Modifier.height(4.dp))
                            Text("Position: " + state.spread.positions[idx].label)
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
