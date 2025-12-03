package com.timmay.tarot.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.timmay.tarot.ui.screens.HomeScreen
import com.timmay.tarot.ui.screens.ReadingScreen
import com.timmay.tarot.ui.screens.SpreadPickerScreen

@Composable
fun TarotAppRoot() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "home") {
        composable("home") { HomeScreen(nav) }
        composable("spread_picker") { SpreadPickerScreen(nav) }
        composable("reading/{spreadId}") { backStackEntry ->
            val spreadId = backStackEntry.arguments?.getString("spreadId") ?: "three_card"
            ReadingScreen(spreadId = spreadId)
        }
    }
}
