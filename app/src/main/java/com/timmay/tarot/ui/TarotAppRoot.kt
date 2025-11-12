package com.timmay.tarot.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.timmay.tarot.ui.screens.HomeScreen
import com.timmay.tarot.ui.screens.SpreadPickerScreen
import com.timmay.tarot.ui.screens.ReadingScreen

@Composable
fun TarotAppRoot() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "home") {
        composable("home") { HomeScreen(nav) }
        composable("spread_picker") { SpreadPickerScreen(nav) }
        composable("reading/{spreadId}") { back ->
            val spreadId = remember { back.arguments?.getString("spreadId") ?: throw IllegalStateException("spreadId not found") }
            ReadingScreen(spreadId)
        }
    }
}
