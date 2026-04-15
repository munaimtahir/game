package com.vexel.offlinearcade

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vexel.offlinearcade.core.model.ArcadeFeedback
import com.vexel.offlinearcade.core.model.ArcadeSnapshot
import com.vexel.offlinearcade.core.model.RunResult
import com.vexel.offlinearcade.feature.challenges.ChallengesScreen
import com.vexel.offlinearcade.feature.home.HomeScreen
import com.vexel.offlinearcade.feature.settings.SettingsScreen
import com.vexel.offlinearcade.feature.stats.StatsScreen
import com.vexel.offlinearcade.game.lanedrift.LaneDriftScreen
import com.vexel.offlinearcade.game.pulseorbit.PulseOrbitScreen
import com.vexel.offlinearcade.game.stackdrop.StackDropScreen

@Composable
fun ArcadeNavHost(
    navController: NavHostController,
    snapshot: ArcadeSnapshot,
    feedback: ArcadeFeedback,
    onToggleSound: (Boolean) -> Unit,
    onToggleMusic: (Boolean) -> Unit,
    onToggleVibration: (Boolean) -> Unit,
    onToggleReducedEffects: (Boolean) -> Unit,
    onToggleHighContrast: (Boolean) -> Unit,
    onUnlockTheme: (String) -> Unit,
    onSelectTheme: (String) -> Unit,
    onRecordRun: (RunResult) -> Unit,
) {
    NavHost(navController = navController, startDestination = Routes.Home) {
        composable(Routes.Home) {
            HomeScreen(
                profile = snapshot.profile,
                stats = snapshot.stats,
                todayChallenges = snapshot.challenges,
                onPulseOrbit = { navController.navigate(Routes.PulseOrbit) },
                onLaneDrift = { navController.navigate(Routes.LaneDrift) },
                onStackDrop = { navController.navigate(Routes.StackDrop) },
                onChallenges = { navController.navigate(Routes.Challenges) },
                onStats = { navController.navigate(Routes.Stats) },
                onSettings = { navController.navigate(Routes.Settings) },
            )
        }
        composable(Routes.PulseOrbit) {
            PulseOrbitScreen(
                stats = snapshot.statsByGame[com.vexel.offlinearcade.core.model.GameId.PULSE_ORBIT],
                settings = snapshot.settings,
                feedback = feedback,
                onRunComplete = onRecordRun,
                onBack = { navController.popBackStack() },
            )
        }
        composable(Routes.LaneDrift) {
            LaneDriftScreen(
                stats = snapshot.statsByGame[com.vexel.offlinearcade.core.model.GameId.LANE_DRIFT],
                settings = snapshot.settings,
                feedback = feedback,
                onRunComplete = onRecordRun,
                onBack = { navController.popBackStack() },
            )
        }
        composable(Routes.StackDrop) {
            StackDropScreen(
                stats = snapshot.statsByGame[com.vexel.offlinearcade.core.model.GameId.STACK_DROP],
                settings = snapshot.settings,
                feedback = feedback,
                onRunComplete = onRecordRun,
                onBack = { navController.popBackStack() },
            )
        }
        composable(Routes.Challenges) {
            ChallengesScreen(challenges = snapshot.challenges, onBack = { navController.popBackStack() })
        }
        composable(Routes.Stats) {
            StatsScreen(stats = snapshot.stats, onBack = { navController.popBackStack() })
        }
        composable(Routes.Settings) {
            SettingsScreen(
                settings = snapshot.settings,
                themes = snapshot.themes,
                selectedThemeId = snapshot.profile.selectedThemeId,
                premiumUnlocked = snapshot.profile.premiumUnlocked,
                onToggleSound = onToggleSound,
                onToggleMusic = onToggleMusic,
                onToggleVibration = onToggleVibration,
                onToggleReducedEffects = onToggleReducedEffects,
                onToggleHighContrast = onToggleHighContrast,
                onSelectTheme = onSelectTheme,
                onUnlockTheme = onUnlockTheme,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
