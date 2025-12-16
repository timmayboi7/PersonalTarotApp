package com.timmay.tarot.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.timmay.tarot.ui.screens.AccountScreen
import com.timmay.tarot.ui.screens.HomeScreen
import com.timmay.tarot.ui.screens.ReadingScreen
import com.timmay.tarot.ui.screens.SpreadPickerScreen
import com.timmay.tarot.ui.splash.TarotGenieSplashFlip

@Composable
fun TarotAppRoot() {
    var showSplash by remember { mutableStateOf(true) }

    if (showSplash) {
        TarotGenieSplashFlip(onFinished = { })
    } else {
        val nav = rememberNavController()
        NavHost(navController = nav, startDestination = "home") {
            composable("home") { HomeScreen(nav) }
            composable("account") { AccountScreen(nav) }
            composable("spread_picker") { SpreadPickerScreen(nav) }
            composable("reading/{spreadId}") { back ->
                val spreadId = back.arguments?.getString("spreadId") ?: "three_card"
                ReadingScreen(spreadId)
            }
        }
    }
}
