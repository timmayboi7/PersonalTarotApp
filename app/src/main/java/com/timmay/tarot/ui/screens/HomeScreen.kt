package com.timmay.tarot.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.ZoneId
import com.timmay.tarot.domain.TarotRng
import com.timmay.tarot.repo.CardStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.dailyCard.collectAsState()

    LaunchedEffect(Unit) { viewModel.fetchDailyCard() }

    Surface(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (val ui = state) {
                is HomeViewModel.DailyCardState.Loading -> CircularProgressIndicator()
                is HomeViewModel.DailyCardState.Error -> {
                    Text(
                        text = ui.message,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
                is HomeViewModel.DailyCardState.Ready -> {
                    Text("Daily Card", style = MaterialTheme.typography.headlineMedium)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        ui.card.name,
                        style = MaterialTheme.typography.displaySmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(6.dp))
                    if (ui.isReversed) {
                        Text(
                            text = "Reversed",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(6.dp))
                    }
                    val keywords = if (ui.isReversed) ui.card.keywordsReversed else ui.card.keywordsUpright
                    if (keywords.isNotEmpty()) {
                        Text(
                            keywords.joinToString(" • "),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(6.dp))
                    }
                    val meaning = if (ui.isReversed) ui.card.meaningReversed else ui.card.meaningUpright
                    if (meaning.isNotBlank()) {
                        Text(
                            meaning,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Button(onClick = { navController.navigate("spread_picker") }) {
                Text("Start a reading")
            }
        }
    }
}
