package com.vexel.offlinearcade

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vexel.offlinearcade.feature.home.HomeScreen
import com.vexel.offlinearcade.feature.challenges.ChallengesScreen
import com.vexel.offlinearcade.feature.stats.StatsScreen
import com.vexel.offlinearcade.feature.settings.SettingsScreen
import com.vexel.offlinearcade.game.lanedrift.LaneDriftScreen
import com.vexel.offlinearcade.game.pulseorbit.PulseOrbitScreen
import com.vexel.offlinearcade.game.stackdrop.StackDropScreen

object Routes {
    const val Home = "home"
    const val PulseOrbit = "pulse_orbit"
    const val LaneDrift = "lane_drift"
    const val StackDrop = "stack_drop"
    const val Challenges = "challenges"
    const val Stats = "stats"
    const val Settings = "settings"
}

@Composable
fun ArcadeNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Home) {
        composable(Routes.Home) {
            HomeScreen(
                onPulseOrbit = { navController.navigate(Routes.PulseOrbit) },
                onLaneDrift = { navController.navigate(Routes.LaneDrift) },
                onStackDrop = { navController.navigate(Routes.StackDrop) },
                onChallenges = { navController.navigate(Routes.Challenges) },
                onStats = { navController.navigate(Routes.Stats) },
                onSettings = { navController.navigate(Routes.Settings) }
            )
        }
        composable(Routes.PulseOrbit) { PulseOrbitScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.LaneDrift) { LaneDriftScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.StackDrop) { StackDropScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.Challenges) { ChallengesScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.Stats) { StatsScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.Settings) { SettingsScreen(onBack = { navController.popBackStack() }) }
    }
}
