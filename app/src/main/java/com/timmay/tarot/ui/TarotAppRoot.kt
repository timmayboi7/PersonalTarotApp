package com.timmay.tarot.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.navArgument
import com.timmay.tarot.ui.screens.HomeScreen
import com.timmay.tarot.ui.screens.ReadingScreen
import com.timmay.tarot.ui.screens.SpreadPickerScreen

@Composable
fun TarotAppRoot() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("spread_picker") { SpreadPickerScreen(navController) }
        composable(
            route = "reading/{spreadId}",
            arguments = listOf(navArgument("spreadId") { type = NavType.StringType })
        ) { backStackEntry ->
            val spreadId = requireNotNull(backStackEntry.arguments?.getString("spreadId"))
            ReadingScreen(spreadId)
        }
    }
}
