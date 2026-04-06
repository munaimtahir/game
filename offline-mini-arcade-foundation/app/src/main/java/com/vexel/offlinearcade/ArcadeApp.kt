package com.vexel.offlinearcade

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun ArcadeApp() {
    val navController = rememberNavController()
    ArcadeNavHost(navController = navController)
}
