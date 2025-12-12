package com.timmay.tarot.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.timmay.tarot.ui.theme.DeepNavy
import com.timmay.tarot.ui.theme.Gold
import com.timmay.tarot.ui.theme.Ink
import com.timmay.tarot.ui.theme.Midnight
import com.timmay.tarot.viewmodel.HomeScreenViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.ui.collectAsStateWithLifecycle()
    val showFullscreen = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Midnight, DeepNavy)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Daily Guidance",
                style = MaterialTheme.typography.headlineSmall,
                color = Gold
            )
            Text(
                text = "Open today's draw to see the artwork and guidance without cluttering your altar.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.92f)
            )
            Button(
                onClick = {
                    viewModel.fetchDailyCard()
                    showFullscreen.value = true
                },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(0.82f)
            ) {
                Text(text = if (uiState.isDrawn) "Open today's card" else "Reveal daily card")
            }
            OutlinedButton(
                onClick = { navController.navigate("spread_picker") },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(0.82f)
            ) {
                Text(text = "Start a reading")
            }
        }
    }

    DailyCardFullscreen(
        visible = showFullscreen.value && uiState.dailyCardImage != null,
        cardName = uiState.dailyCardName,
        cardImage = uiState.dailyCardImage ?: "",
        description = uiState.dailyDescription,
        onDismiss = { showFullscreen.value = false }
    )
}

@Composable
private fun DailyCardFullscreen(
    visible: Boolean,
    cardName: String,
    cardImage: String,
    description: String,
    onDismiss: () -> Unit
) {
    if (!visible) return
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnClickOutside = true, dismissOnBackPress = true)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Midnight.copy(alpha = 0.9f), DeepNavy.copy(alpha = 0.9f)))),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 24.dp, horizontal = 12.dp)
            ) {
                Text(
                    text = "Today's Portal",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Gold
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .aspectRatio(1f / 1.6f)
                        .shadow(24.dp, RoundedCornerShape(22.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Midnight.copy(alpha = 0.9f),
                                    Ink.copy(alpha = 0.8f)
                                )
                            ),
                            shape = RoundedCornerShape(22.dp)
                        )
                        .padding(8.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(Gold.copy(alpha = 0.35f), Color.Transparent),
                                radius = 420f
                            ),
                            shape = RoundedCornerShape(22.dp)
                        )
                        .padding(8.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent),
                        elevation = CardDefaults.cardElevation(defaultElevation = 18.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        border = BorderStroke(1.5.dp, Brush.linearGradient(listOf(Gold, Gold.copy(alpha = 0.5f))))
                    ) {
                        Box {
                            AsyncImage(
                                model = "file:///android_asset/$cardImage",
                                contentDescription = cardName,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.78f))
                                    .align(Alignment.BottomCenter)
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = cardName,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Gold,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = DeepNavy.copy(alpha = 0.86f)),
                    border = BorderStroke(1.dp, Brush.linearGradient(listOf(Gold.copy(alpha = 0.65f), Gold.copy(alpha = 0.25f))))
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Message",
                            style = MaterialTheme.typography.titleMedium,
                            color = Gold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        HorizontalDivider(color = Gold.copy(alpha = 0.35f))
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(0.55f),
                    border = BorderStroke(1.4.dp, Gold.copy(alpha = 0.8f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Gold)
                ) {
                    Text(text = "Close the veil")
                }
            }
        }
    }
}
