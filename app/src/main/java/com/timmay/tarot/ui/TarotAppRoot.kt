package com.timmay.tarot.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.timmay.tarot.ui.screens.HomeScreen
import com.timmay.tarot.ui.screens.SpreadPickerScreen
import com.timmay.tarot.ui.screens.ReadingScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TarotAppRoot() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "home") {
        composable("home") { HomeScreen(nav) }
        composable("spread_picker") { SpreadPickerScreen(nav) }
        composable("reading/{spreadId}") { back ->
            val spreadId = back.arguments?.getString("spreadId") ?: "three_card"
            ReadingScreen(spreadId)
        }
    }
}
