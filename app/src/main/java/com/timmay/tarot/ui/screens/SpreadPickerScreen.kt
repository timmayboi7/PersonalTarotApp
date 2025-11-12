package com.timmay.tarot.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.timmay.tarot.viewmodel.SpreadPickerViewModel

@Composable
fun SpreadPickerScreen(nav: NavController, vm: SpreadPickerViewModel = viewModel()) {
    val spreads by vm.spreads.collectAsState()

    LaunchedEffect(Unit) {
        vm.fetchSpreads()
    }

    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        items(spreads) { s ->
            ElevatedCard(onClick = { nav.navigate("reading/" + s.id) }, modifier = Modifier.padding(8.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text(s.name, style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(6.dp))
                    Text(s.positions.joinToString(" • ") { it.label }, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
