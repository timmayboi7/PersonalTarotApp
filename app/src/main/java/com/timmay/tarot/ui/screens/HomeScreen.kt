package com.timmay.tarot.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.ZoneId
import com.timmay.tarot.domain.TarotRng
import com.timmay.tarot.repo.CardStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun HomeScreen(nav: NavController) {
    val zone = remember { ZoneId.systemDefault() }
    val dailySeed = remember { TarotRng.dailySeed(zone) }
    var daily by remember { mutableStateOf("…") }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val cards = CardStore().all()
            val idx = kotlin.random.Random(dailySeed).nextInt(cards.size)
            daily = cards[idx].name
        }
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Daily Card", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(6.dp))
            Text(daily, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(24.dp))
            Button(onClick = { nav.navigate("spread_picker") }) { Text("Start a reading") }
        }
    }
}
